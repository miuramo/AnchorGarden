package jaist.css.covis.cls;

import jaist.css.covis.CoVisBuffer;
import jaist.css.covis.ToolTipProvider;

import java.awt.BasicStroke;
import java.awt.Color;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.TreeMap;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PText;
import edu.umd.cs.piccolo.util.PDimension;

public class Covis_primitive extends Covis_Type implements ToolTipProvider, ToFront, Move {
	public static BasicStroke basicStroke = new BasicStroke(3f,BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 90.0f);
	public static BasicStroke dottedStroke = new BasicStroke(3f,BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER, 90.0f, new float[]{5,10},0);
	public static BasicStroke thinStroke = new BasicStroke(1.5f,BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 90.0f);

//	float[] dash = { DASH_WIDTH, DASH_WIDTH };
//	strokes = new Stroke[NUM_STROKES];
//	for (int i = 0; i < NUM_STROKES; i++) {
//		strokes[i] = new BasicStroke(2, BasicStroke.CAP_BUTT,
//				BasicStroke.JOIN_MITER, 1, dash, i);
//	}
	public static PNode nullNode = new PNode();
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
	
//	public CovisObj_TransparencyControl tc;
	
	public static Color defaultColor = new Color(255,255,222);
	public Color color;
	
	public PText valueText;
	public float valueTextOffset = -9.5f;
	public void justify(){
		double vw = valueText.getWidth();
		double vh = valueText.getHeight();
		double scalex, scaley;
		scalex = getWidth()/vw;
		scaley = getHeight()/vh;
		if (scalex > 2d) scalex = 2d;
		if (scaley > 2d) scaley = 2d;
		if (scalex < scaley) scaley = scalex;
		valueText.setScale(scaley*0.95);
		vw = valueText.getWidth();
		vh = valueText.getHeight();
		double x = (getWidth()-vw)/2;
		double y = (getHeight()-vh)/2;
		valueText.setOffset(x+valueTextOffset,y);
	}
	public String value;
	public boolean setValue(String s){
		return false;
	}
	public String getValue(){
		return value;
	}
	public void edit(JFrame f, Variable v){
		edit(f,v,false);
	}
	
	public void edit(JFrame f, Variable v, boolean isnew){
		String input;
		input = JOptionPane.showInputDialog(f, "Input Value for "+v.getBaseVarName(), getValue());
		if (input == null) return;
		if (!setValue(input)){
			JOptionPane.showMessageDialog(f,"Value is not accepted.","Error",JOptionPane.WARNING_MESSAGE);
		}
		if (!isnew) {
			buffer.putHistoryEditValue(v); //変更したらソースコードに追加
			Informer.playSound("Pop.wav");
		}
		
	}

	//　複数リンクからの参照がありうるので，HashSet
	HashSet<Anchor> anchors_incoming;
	
	public PText tooltip;
	
	long toFront_ts;
	Class<?> type;
	
	public Covis_primitive(CoVisBuffer buf, boolean isAuto){
		buffer = buf;
		init(isAuto);
		color = defaultColor;
		setPaint(color);
//		tc = new CovisObj_TransparencyControl(this);
		anchors_incoming = new HashSet<Anchor>();
		addAttribute("tooltip", this);
		setStroke(dottedStroke);
		type = getClass();
	}
	public Covis_primitive(Color c, CoVisBuffer buf, boolean isAuto){
		this(buf,isAuto);
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
	public Covis_primitive defaultObject;
	
	public Covis_primitive Covis_clone(boolean isAuto){
		return new Covis_primitive(buffer,true);
	}
	public Covis_primitive createNew(JFrame f, boolean isAuto){
		return null;
	}
	public Covis_primitive newInstance(boolean isAuto){
		Constructor<?> constructor;
		Covis_primitive cv_prim = null;
		try {
			constructor = getClass().getConstructor(CoVisBuffer.class, boolean.class);
			cv_prim = (Covis_primitive) constructor.newInstance(buffer, isAuto);
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
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return cv_prim;
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

	}
	public void detach(Anchor anchor){

	}
	public void detachAll() {
	
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
//	public String referenceVarNames(){
//		if (anchors_incoming.size()==0) return null;
//		StringBuffer sb = new StringBuffer();
//		TreeMap<Double,Anchor> map = new TreeMap<Double, Anchor>();
//		for(Anchor p: anchors_incoming) {
//			map.put(p.getGlobalTranslation().getY(), p);
//		}
//		for(Anchor a: map.values()){
//			sb.append(a.getVarName()+"\n");
//		}
//		return sb.toString().substring(0, sb.length()-1);
//	}
	public String referenceVarNames(){
		if (getAttribute("parentAry")!=null){
			String refsrc = ((Covis_Array)getAttribute("parentAry")).referenceVarNames()+"\n";
			String indexStr = "["+(String)getAttribute("index")+"]";
			String refsrc2 = refsrc.replaceAll("\n",indexStr+" \n");
			return refsrc2;
		}
		return null;
	}
	
	public PNode getToolTipNode(){
		if (tooltip == null) {
			String refVarNames = referenceVarNames();
			if (refVarNames == null) return null;
			tooltip = new PText(refVarNames);
			tooltip.setPaint(defaultColor);
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
	}
	public void move(PDimension d){
		translate(d.getWidth(), d.getHeight()); //履歴に関係ない動作
		for(Anchor a: anchors_incoming){
			a.start_RewindThread();
		}
	}
	@Override
	public PNode createToolTip() {
		return nullNode;
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
	//デフォルトコンストラクタの引数(プリミティブなので，必要としないはずだが)
	@Override
	public String getConstructorArgs() {
		return "";
	}

}
