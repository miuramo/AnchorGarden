package jaist.css.covis.cls;

import jaist.css.covis.CoVisBuffer;
import jaist.css.covis.ToolTipProvider;
import jaist.css.covis.hist.CVHist_New;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.geom.Point2D;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.TreeMap;

import javax.swing.JFrame;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.activities.PActivity;
import edu.umd.cs.piccolo.nodes.PText;
import edu.umd.cs.piccolo.util.PDimension;

public class Covis_Object extends Covis_Type implements ToolTipProvider, ToFront, Move {
	public static BasicStroke basicStroke = new BasicStroke(3f,BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 90.0f);
	public static BasicStroke dottedStroke = new BasicStroke(3f,BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER, 90.0f, new float[]{5,10},0);
	public static BasicStroke thinStroke = new BasicStroke(1.5f,BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 90.0f);

	//	float[] dash = { DASH_WIDTH, DASH_WIDTH };
	//	strokes = new Stroke[NUM_STROKES];
	//	for (int i = 0; i < NUM_STROKES; i++) {
	//		strokes[i] = new BasicStroke(2, BasicStroke.CAP_BUTT,
	//				BasicStroke.JOIN_MITER, 1, dash, i);
	//	}

	private static final long serialVersionUID = 1676721160662045517L;
	public static int objCount = 0;
	public static int objAryCount = 0;
	public String getNextVarName(boolean isAry){
		StringBuffer sb = new StringBuffer(getClsName().toLowerCase().substring(0,3));
		if (isAry) {
			objAryCount++;
			sb.append("Ary");
			sb.append(objAryCount);
		} else {
			objCount++;
			sb.append(objCount);
		}
		return sb.toString();
	}

	public CovisObj_TransparencyControl tc;

	public static Color defaultColor = new Color(255,255,222);
	public Color color;

	//　複数リンクからの参照がありうるので，HashSet
	public HashSet<Anchor> anchors_incoming;
	public ArrayList<Anchor> anchors_member;

	//	public Hashtable<String, Covis_Type> myrefs;

	public PText tooltip;

	long toFront_ts;
	public Class<?> type;

	public CVHist_New cvhist;
	public CoVisBuffer buffer;
	public boolean isAuto;

	public Covis_Object(CoVisBuffer buf, boolean isAuto) throws NullPointerException{
		buffer = buf;
		anchors_member = new ArrayList<Anchor>();
		//		myrefs = new Hashtable<String, Covis_Type>();
		init(isAuto);
		color = defaultColor;
		setPaint(color);
		tc = new CovisObj_TransparencyControl(this);
		anchors_incoming = new HashSet<Anchor>();
		addAttribute("tooltip", this);
		setStroke(dottedStroke);
		type = getClass();
	}
	public Covis_Object(Color c, CoVisBuffer buf, boolean isAuto){
		this(buf, isAuto);
		color = c;
		setPaint(color);
	}
	public void init(boolean isAuto){
		setPathToRectangle(0, 0, 80 ,50);	
	}
	public String getClsName(){
		int p = getClass().getName().lastIndexOf("_");
		return getClass().getName().substring(p+1);
	}
	private boolean isSelected;
	private boolean isHighlighted;
	public Covis_Object defaultObject;

	public Covis_Object Covis_clone(){
		return new Covis_Object(buffer,true);
	}
	public Covis_Object createNew(JFrame f, boolean isAuto) throws InvocationTargetException{
		return newInstance(isAuto);
	}
	public Covis_Object newInstance(boolean isAuto) throws InvocationTargetException {
		Constructor<?> constructor;
		Covis_Object cv_class = null;
		try {
			constructor = getClass().getConstructor(CoVisBuffer.class, boolean.class);
			cv_class = (Covis_Object) constructor.newInstance(buffer,isAuto);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return cv_class;
	}
	public void setHighlight(boolean f){
		isHighlighted = f;
	}
	public boolean isHighlighted(){
		return isHighlighted;
	}
	public void setSelected(boolean s){
		isSelected = s;
	}
	public boolean isSelected(){
		return isSelected;
	}
	public Color getClassColor(){
		return defaultColor;
	}
	public void attach(Anchor anchor){
		if (getTransparency()<0.1f) return; //あまり薄かったらアタッチさせません
		//		System.out.println("at "+toString());
		Informer.playSound("Laser2.wav");

		if (anchor.link.dest != null && anchor.link.dest != this) return; //anchor.link.dest.detach(anchor);
		anchors_incoming.add(anchor);
		anchor.link.dest = this;
		anchor.destObject = this;//これがないと，BTreeアンリンクのときに消えない
		addAttribute("moveLink", anchor.link);
		tc.transparencyThread_Start(1, this, 100);
		//		setTransparency(1.0f);
		//		anchor.anchortab.tooltip = null;
		tooltip = null;
		//		move(new PDimension());
		anchor.toFront(System.currentTimeMillis());
		for(Anchor a: anchors_incoming){
			a.start_RewindThread();
			//			a.anchortab.tooltip = null;
		}
		//		anchor.lastAttach = null;
		//ここで，有効な変数からリンクされているかチェックし，setAnchorEnabledを再設定する．アタッチもデタッチも共通
		setMemberVariableEnabled(checkValidVariable(new Hashtable<Integer,Object>()));
		buffer.objField.clearObjVarName();
		buffer.varField.updateVarNameChain();

		buffer.putHisotryLink("lnk", anchor, this);
		anchor.lastDestObject = this;
	}
	public void setMemberVariableEnabled(boolean checkValidVariable) {
		//		if (anchors_member == null) return;
		for(Anchor a: anchors_member){
			a.srcVariable.setEnabled(checkValidVariable);
			//			a.setAnchorEnabled(checkValidVariable);
		}
	}
	public boolean checkValidVariable(Hashtable<Integer,Object> checked) {
		if (checked.containsKey(this.hashCode())) return false;
		checked.put(this.hashCode(), this);
		boolean b = false;
		for(Anchor a: anchors_incoming){
			if (a.srcVariable.object == null) return true; //Variableだから
			if (a.srcVariable instanceof VariableM){
				VariableM vm = (VariableM) a.srcVariable;
				if (vm.object instanceof Covis_Object){
					if (((Covis_Object)vm.object).checkValidVariable(checked)) b = true;
				}
			}
		}
		return b;
	}
	public void appendVarNameRecursive(Variable v, Hashtable<Integer, Object> checked) {
		if (checked.containsKey(this.hashCode())) return;
		checked.put(this.hashCode(), this);
		//		myrefs.put(v.getVarName(), v.cv_class);
		for(Anchor a: anchors_member){
			for(String s: v.getVarNamesAry()){
//				System.out.println("  addVarNames "+s+ " to "+a.srcVariable.getBaseVarName());
				a.srcVariable.addVarNames(s);
			}
			a.srcVariable.appendVarNameRecursive(checked);
		}
	}

	public void detach(Anchor anchor){
//				System.out.println("detach "+toString());
		//		Informer.playSound("Default.wav");
		anchors_incoming.remove(anchor);
		anchor.destObject = null;//ここはまだ切らない．その都合上，ここに変数再編成を書くことができない，はウソでした．
		anchor.link.dest = null; //ここは切らないと，リンク線が残る

		setMemberVariableEnabled(checkValidVariable(new Hashtable<Integer,Object>()));
		buffer.objField.clearObjVarName();
		buffer.varField.updateVarNameChain();
		

		if (anchors_incoming.size()==0 || !checkValidVariable(new Hashtable<Integer,Object>()) ){
			//			System.out.println("hogehoge");
			tc.tick_transparency[0] = -0.05f; //一旦離したら早く消える
			tc.transparencyThread_Start(0, this, 100);
			addAttribute("moveLink", null);
		}
		tooltip = null;
		//		move(new PDimension());
		for(Anchor a: anchors_incoming){
			a.start_RewindThread();
			//			a.anchortab.tooltip = null;
		}
//		anchor.lastDestObject = null;

	}
	public void detachAll() {
		HashSet<Anchor> set = new HashSet<Anchor>(anchors_incoming);
		for(Anchor a:set){
			detach(a);
			a.destObject = null;
		}
		//		if (anchors_member == null) return;
		HashSet<Anchor> set2 = new HashSet<Anchor>(anchors_member);
		for(Anchor a:set2){
			if (a.destObject == null) continue;
			a.destObject.detach(a);
		}
	}
	public int attachPointOffset(Anchor mya) {
		if (anchors_incoming.size()==1) return 0;
		TreeMap<Double,Anchor> map = new TreeMap<Double, Anchor>();
		for(Anchor p: anchors_incoming) {
			double y = p.getGlobalTranslation().getY();
			while(map.get(y)!=null) y+=0.01;
			map.put(y, p);
		}
		int yy = 0;
		for(Anchor a: map.values()){
			if (a == mya) return yy;
			yy += 10;
		}
		return 0;
	}
	public String referenceVarNames(){
		if (anchors_incoming.size()==0) return null;
		StringBuffer sb = new StringBuffer();
		for(Anchor a: anchors_incoming){
			sb.append(a.srcVariable.getVarName()+"\n");
		}
		
//		TreeMap<Double,Anchor> map = new TreeMap<Double, Anchor>();
//		for(Anchor p: anchors_incoming) {
//			double yy = p.getGlobalTranslation().getY();
//			while (map.get(yy)!= null) yy+=0.01;
//			map.put(yy, p);
//		}
//		for(Anchor a: map.values()){
//			sb.append(a.getVarName()+"\n");
//		}
		return sb.toString().substring(0, sb.length()-1);
	}
	public ArrayList<String> referenceVarNamesAry(){
		ArrayList<String> temp = new ArrayList<String>();
		for(Anchor a: anchors_incoming){
			temp.addAll(a.srcVariable.getVarNamesAry());
		}
		return temp;
	}
	// anchors_incoming をハッシュにして返す
	public TreeMap<String,Anchor> referenceAnchors(){
		TreeMap<String,Anchor> map = new TreeMap<String, Anchor>();
		for(Anchor p: anchors_incoming){
			map.put(p.getVarName(), p);
		}
		return map;
	}
	//TODO:ツールチップ
	public PNode getToolTipNode(){
		tooltip = null;
		if (tooltip == null) {
			PText pt = new PText(referenceVarNames()+"\n(hash "+ String.valueOf(hashCode())+")");
			pt.setPaint(Color.white);
			tooltip = pt;
		}
		return tooltip;
	}

	/**
	 * ToFrontインタフェースの実装
	 */
	public void toFront(long ts) {
		if (toFront_ts == ts) return;
		toFront_ts = ts;

		PNode parent = getParent();
		if (parent != null){
			//			removeFromParent();
			parent.addChild(this);
			if (parent instanceof ToFront) ((ToFront)parent).toFront(ts);
			if (parent.getAttribute("toFront")!=null){
				((ToFront)parent.getAttribute("toFront")).toFront(ts);
			}
			for(Anchor p: anchors_incoming){
				p.toFront(ts);
			}
		}
	}
	public void checkAtRefDetached(){
	}
	public void dispose(){
		//		System.out.println("disposed");
		buffer.updateSourceWindow();
	}
	public void move(PDimension d){
		translate(d.getWidth(), d.getHeight()); //履歴に関係ない動作
		for(Anchor a: anchors_incoming){
			a.start_RewindThread();
		}
		for(Anchor a: anchors_member){
			a.start_RewindThread();
		}
	}
	@Override
	public PNode createToolTip() {
		try {
			return newInstance(true);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}
	public String getConstructorInfo() {
		return "new "+getClsName()+"();";
	}
	//	public String getConstructorArguments() {
	//		return "";
	//	}
	public void setCVHist(CVHist_New _cvhist) {
		cvhist = _cvhist;
	}
	public static String getClsNameStatic(Class<?> elementType) {
		//		System.out.println(elementType.getSimpleName());
		int p = elementType.getSimpleName().lastIndexOf("_");
		return elementType.getSimpleName().substring(p+1);
	}
	@Override
	public void clear_objCount() {
		try {
			Field f = getClass().getField("objCount");
			f.setInt(getClass(), 0);
			Field fg = getClass().getField("objAryCount");
			fg.setInt(getClass(), 0);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	//デフォルトコンストラクタの引数
	@Override
	public String getConstructorArgs() {
		return "";
	}
	public void setOffsetAlignment(Covis_Object o, int dx, int dy){
		Point2D pos = o.getOffset();
		Point2D newpos = new Point2D.Double(pos.getX()+dx, pos.getY()+dy);
		setOffset(newpos);
	}
	public Covis_String covis_toString(){
		Covis_String cs = new Covis_String(buffer,true);
		cs.setValues(toString());
		buffer.putHistoryNew("new", cs, false);
		return cs;
	}
	public String toString(){
		return String.valueOf(hashCode());
	}
	public PActivity animateToZoom(float f, long l) {
		PActivity p = animateToPositionScaleRotation(getOffset().getX(), getOffset().getY(), f, 0, l);
		return p;
	}


}
