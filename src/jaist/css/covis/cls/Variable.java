package jaist.css.covis.cls;

import jaist.css.covis.CoVisBuffer;
import jaist.css.covis.Selectable;
import jaist.css.covis.hist.CVHist_Var;

import java.awt.BasicStroke;
import java.awt.Color;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.TreeMap;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;
import edu.umd.cs.piccolo.util.PDimension;

public class Variable extends PPath implements Layoutable, Selectable, ToFront , Move{
	public static BasicStroke roundrectStroke = new BasicStroke(6f,BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 90.0f);

	private static final long serialVersionUID = -2274392520995258292L;

	public Covis_Type cv_class;
	public Class<?> type;
	public Class<?> elementType;
	boolean isArray = false;
	//	public Covis_Object cv_class_for_tooptip;
	//	public static Variable selected_stamp;
	//	public static ArrayList<Variable> stamps;
	//	static{
	//	stamps = new ArrayList<Variable>();
	//	}
	private boolean isSelected;
	
	public boolean isEnabled = true;
	public Covis_Type object = null;//これがあるとMember

	PPath handle;
	PText caption;

	public Anchor anchor;
	private String varname_base;
	private HashSet<String> varnames;

	public CVHist_Var cvhist;

	long toFront_ts = 0;
	public CoVisBuffer buffer;

	public static float top = 0;
	
	public void setVarName_Base(String s){
		varname_base = s;
//		varnames.add(s);
	}
	public boolean isEnabled(){
		return isEnabled;
	}
	public void setEnabled(boolean b){
		isEnabled = b;
		anchor.setAnchorEnabled(b);
	}
	public Variable(Covis_Type co, JFrame frame, CoVisBuffer buf, boolean isMember){
		cv_class = co;
		buffer = buf;
		varnames = new HashSet<String>();
		if (cv_class instanceof Covis_Array){
			// 型（クラス・プリミティブ）選択
			String s;
			if (((Covis_Array)cv_class).elementClassStr == null){
				s = (String)JOptionPane.showInputDialog(
						frame,
						"Choose Type of the Array",
						"Choose Type",
						JOptionPane.QUESTION_MESSAGE,
						null,
						ClassStamp.possibilities,
						Covis_Array.lastSelectedClass);
				if ((s != null) && (s.length() > 0)) {
					//					System.out.println(s+" Selected.");
					Covis_Array.lastSelectedClass = s;
				} else {
					type = null;
				}
			} else {
				s = ((Covis_Array)cv_class).elementClassStr;
			}
			try {
				Class<?> c = Class.forName("jaist.css.covis.cls.Covis_"+s);
				Constructor<?> constructor = c.getConstructor(CoVisBuffer.class, boolean.class);
				cv_class = (Covis_Type) constructor.newInstance(buffer,true);

				type = Array.newInstance(c, 4).getClass();
				elementType = cv_class.getClass();

				System.out.println("array "+type.toString());
				System.out.println("ele "+elementType.toString());
				isArray = true;
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}
		} else {
			type = cv_class.getClass();
			elementType = type;
		}
		setPathToRectangle(0,0,250,40);
		setPaint(cv_class.getClassColor());
		setStrokePaint(Color.gray);
		setStroke(new BasicStroke(1));

		//		co.setScale(0.3f);
		//		addChild(cv_class);
		//		layout(0);

		addAttribute("info", "ClassStamp "+this.toString());
		//		addAttribute("selectable", this);
		addAttribute("moveTargetY", this);
		addAttribute("dragLayout", this);

		handle = new PPath(this.getPathReference());
		handle.addAttribute("moveTargetY", this);
		handle.addAttribute("dragLayout", this);
		handle.setTransparency(0.0f);
		handle.setPaint(Color.pink);
		handle.setStrokePaint(Color.red);
		handle.setStroke(roundrectStroke);
		handle.setPickable(true);
		handle.moveToFront();
		handle.addAttribute("info", "ClassStamp handle");
		//		handle.addAttribute("selectable", this);
		//		handle.addAttribute("exclusiveSelectable", stamps);
		addChild(handle);

		if (!isMember){
			varname_base = cv_class.getNextVarName(isArray);
			caption = new PText(getTypeName()+"  "+varname_base);
			caption.scale(2.2f);
			caption.setOffset(10,3);
			caption.addAttribute("moveTargetY", this);
			caption.addAttribute("dragLayout", this);
			addChild(caption);
			caption.addAttribute("popupMenu", new VariableMenu(this));
		}

		if (cv_class instanceof Covis_Object || isArray){
			anchor = new Anchor(type, this);
			anchor.setVarName(varname_base);
			addChild(anchor);
			anchor.setOffset(245,30);
			if (!isMember){
				anchor.setAnchorEnabled(true);
			}
		} else {
			// プリミティブなので，１つだけ追加
			Covis_primitive prim = (Covis_primitive) cv_class;
			addChild(prim);
			prim.setPathToRectangle(0,0,30,(float)getHeight());
			prim.setStroke(new BasicStroke(2));
			prim.justify();
			prim.setOffset(getWidth()-prim.getWidth(), (getHeight()-prim.getHeight())/2);
			prim.addAttribute("moveTargetY", this);
			prim.addAttribute("dragLayout", this);
			prim.valueText.addAttribute("moveTargetY", this);
			prim.valueText.addAttribute("dragLayout", this);
			prim.valueText.addAttribute("popupMenu", new VariableMenu(this));
		}

		addAttribute("popupMenu", new VariableMenu(this));
		handle.addAttribute("popupMenu", new VariableMenu(this));
		//		stamps.add(this);
	}
	public String getTypeName(){
		if (!isArray){
			//			if (type.isAssignableFrom(Covis_Object.class)){
			return cv_class.getClsName();
		} else {
			return cv_class.getClsName()+"[]";
		}
	}

	public void layout(int dur){
		layoutExceptOne(null, dur);
	}

	public void layoutExceptOne(PNode operationNode, int dur) {
		List<PNode> col = getChildrenReference();
		TreeMap<Double,PNode> map = new TreeMap<Double, PNode>();
		for(PNode p: col) map.put(p.getYOffset(), p);

		double offsetx = 10;
		double endx = 10;
		double offsety = 10;
		double endy = 10;
		double maxx = 0, maxy = 0; 
		for(PNode p : map.values()){
			//			p.setOffset(offsetx, offsety);
			double px = p.getFullBounds().width;
			double py = p.getFullBounds().height;
			if (maxx < offsetx + endx + px) maxx = offsetx + endx + px;
			if (maxy < offsety + endy + py) maxy = offsety + endy + py;
			if (operationNode != p) p.animateToPositionScaleRotation(offsetx, offsety+top, 1, 0, dur);
			offsety += py;
			offsety += 10;
		}
		animateToBounds(0, top, maxx, maxy, dur);
	}

	public ArrayList<Selectable> getAllChildren() {
		return null;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean f, ArrayList<Selectable> list) {
		isSelected = f;
		if (f) handle.setTransparency(0.8f); else handle.setTransparency(0.0f);
		if (f && list != null && !list.contains(this)) list.add(this);
		if (!f && list != null && list.contains(this)) list.remove(this);
	}

	public PNode theNode() {
		return this;
	}

	public void toggleSelected(ArrayList<Selectable> list) {
		setSelected(!isSelected, list);
	}

	public void removeLabel(Hashtable<PNode, PNode> trash) {
		if (trash != null) trash.put(this, getParent());
		removeFromParent();
	}
	//	public PNode getToolTipNode(){
	//	if (cv_class_for_tooptip == null) {
	//	cv_class_for_tooptip = cv_class.Covis_clone();
	//	cv_class_for_tooptip.setScale(0.5f);
	//	}
	//	return cv_class_for_tooptip;
	//	}

	public String getBaseVarName() {
		return varname_base;
	}
	public void addVarNames(String prefix){
//		System.out.println("Variable.addVarNames"+prefix);
		varnames.add(prefix);
	}
	public String getVarName(){
		StringBuffer sb = new StringBuffer();
		for(String s: getVarNamesAry()){
			sb.append(s+"\n");
		}
		if (sb.length()==0) return sb.toString();
		return sb.toString().substring(0, sb.toString().length()-1);
	}
	public ArrayList<String> getVarNamesAry(){
		ArrayList<String> news = new ArrayList<String>();
		if (this instanceof VariableM){
			String sepdot = ".";
			if (object instanceof Covis_Array) sepdot = "";
			
			for(String s: varnames){
				news.add(s+sepdot+varname_base);
			}
		} else {
			news.add(varname_base);
		}
		return news;
	}
	public void clearVarNamesAry(){
		varnames.clear();
	}

	//	この変数を削除する
	public void dispose() {
		if (anchor != null && anchor.destObject != null){
			anchor.destObject.detach(anchor);
		}
		Layoutable layout = (Layoutable) getParent();
		removeFromParent();
		layout.layout(200);
	}

	//	変数名変更
	public String rename(JFrame f) {
		String input;
		while (true){
			if (isArray) {
				input = JOptionPane.showInputDialog(f, "Input new name for "+cv_class.getClsName()+"[] variable.", varname_base);
			} else {
				input = JOptionPane.showInputDialog(f, "Input new name for "+cv_class.getClsName()+" variable.", varname_base);
			}
			if (input == null) return null;
			if (input.equals("")) return null;
			// 変数名重複チェック
			// 親に聞くのが一番
			VarField parent = (VarField) getParent();

			if (parent.checkExistName(input, this)) {
				JOptionPane.showMessageDialog(null, "Same name is already used.\n\nPlease input other name.", "Rename Error", JOptionPane.WARNING_MESSAGE);
				continue;
			}
			if (!input.matches("^[a-zA-Z_][a-zA-Z0-9]*")){
				JOptionPane.showMessageDialog(null, "Name should start with alphabet or _.\n\nPlease input other name.", "Rename Error", JOptionPane.WARNING_MESSAGE);
				continue;
			}
			break;
		}

		varname_base = input;
		if (anchor != null) {
			anchor.setVarName(varname_base);
		}
		caption.setText(getTypeName()+"  "+varname_base);
		if (anchor != null && anchor.destObject != null){
			anchor.destObject.tooltip = null; //変数名ツールチップを再構築
		}
		return varname_base;
	}
	public void toFront(){
		toFront(System.currentTimeMillis());
	}
	public void toFront(long ts) {
		if (toFront_ts == ts) return;
		toFront_ts = ts;
		PNode parent = getParent();
		if (parent != null){
			//			removeFromParent();
			parent.addChild(this);
			if (parent instanceof ToFront) ((ToFront)parent).toFront(ts);
		}
	}
	public void move(PDimension d){
		translate(d.getWidth(), d.getHeight()); //履歴に関係ない動作
		if (anchor != null) anchor.start_RewindThread();
	}
	public String getConstructorInfo() {
		if (cv_class instanceof Covis_Object || isArray) return getTypeName()+" "+varname_base+";";
		else return getTypeName()+" "+varname_base+" = "+((Covis_primitive)cv_class).value+";"; //getValue()にすると，''がとれてしまうので
	}
	public void setCVHist(CVHist_Var _cvhist) {
		cvhist = _cvhist;
	}
	public String getEditValueInfo() {
		if (cv_class instanceof Covis_Object || isArray) return getTypeName()+" "+varname_base+";";
		else return varname_base+" = "+((Covis_primitive)cv_class).value+";"; //getValue()にすると，''がとれてしまうので
	}
	public void appendVarNameRecursive(Hashtable<Integer, Object> checked) {
//		System.out.println(varname_base);
		if (anchor == null) return;
		if (anchor.destObject != null){
//			if (checked.containsKey(this.hashCode())) return;
//			checked.put(this.hashCode(), this);
//			System.out.println("V append "+getVarName());
			anchor.destObject.appendVarNameRecursive(this, checked);
		}		
	}
	
	public static String getShortestName(ArrayList<String> src){
		if (src.size()==0) return null;
		if (src.size()==1) return src.get(0);
		String temp = src.get(0);
		for(String s: src){
			if (s.length() < temp.length()) temp = s;
		}
		return temp;
	}
}
