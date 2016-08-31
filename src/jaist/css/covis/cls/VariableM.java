package jaist.css.covis.cls;

import jaist.css.covis.CoVisBuffer;
import jaist.css.covis.Selectable;
import jaist.css.covis.hist.CVHist_Var;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.util.PDimension;

public class VariableM extends Variable{
	private static final long serialVersionUID = 1922932400458120787L;
	
	

	public VariableM(Covis_Type co, JFrame frame, CoVisBuffer buf, String _varname, Covis_Type obj){
		super(co,frame,buf,true);
		setPathToRectangle(0,0,20,20);
		removeChild(handle);
		handle = null;
		
		isEnabled = false;
		object = obj;
		
		setVarName_Base(_varname);
//		setPaint(cv_class.getClassColor());
		setStrokePaint(null);
//		setStroke(new BasicStroke(1));

//		co.setScale(0.3f);
//		addChild(cv_class);
//		layout(0);

//		addAttribute("info", "ClassStamp "+this.toString());
////		addAttribute("selectable", this);
//		addAttribute("moveTargetY", this);
//		addAttribute("dragLayout", this);

		
		addAttribute("info", null);
		addAttribute("moveTargetY", null);
		addAttribute("dragLayout", null);

		addAttribute("moveTarget", co);
		addAttribute("tooltip", co);

		if (cv_class instanceof Covis_Object || isArray){
//			removeChild(anchor);
//			anchor = new Anchor(type, this);
//			anchor.setVarName(varname);
//			addChild(anchor);
			anchor.setOffset(20,20);
			anchor.setAnchorEnabled(false); //まだ触れない．変数からリンクされたら，触れるようにする．
			anchor.anchortab.addAttribute("moveTarget", co);
			anchor.anchortab.addAttribute("tooltip", co);
		} else {
//			// プリミティブなので，１つだけ追加
//			Covis_primitive prim = (Covis_primitive) cv_class;
//			addChild(prim);
//			prim.setPathToRectangle(0,0,30,(float)getHeight());
//			prim.setStroke(new BasicStroke(2));
//			prim.justify();
//			prim.setOffset(getWidth()-prim.getWidth(), (getHeight()-prim.getHeight())/2);
//			prim.addAttribute("moveTargetY", this);
//			prim.addAttribute("dragLayout", this);
//			prim.valueText.addAttribute("moveTargetY", this);
//			prim.valueText.addAttribute("dragLayout", this);
//			prim.valueText.addAttribute("popupMenu", new VariableMenu(this));
		}

//		addAttribute("popupMenu", new VariableMenu(this));
//		handle.addAttribute("popupMenu", new VariableMenu(this));
//		caption.addAttribute("popupMenu", new VariableMenu(this));
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

//	public String getBaseVarName() {
//		return varname_base;
//	}

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
				input = JOptionPane.showInputDialog(f, "Input new name for "+cv_class.getClsName()+"[] variable.", getBaseVarName());
			} else {
				input = JOptionPane.showInputDialog(f, "Input new name for "+cv_class.getClsName()+" variable.", getBaseVarName());
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

		setVarName_Base(input);
		if (anchor != null) {
			anchor.setVarName(getBaseVarName());
		}
		caption.setText(getTypeName()+"  "+getBaseVarName());
		if (anchor != null && anchor.destObject != null){
			anchor.destObject.tooltip = null; //変数名ツールチップを再構築
		}
		return getBaseVarName();
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
		if (cv_class instanceof Covis_Object || isArray) return getTypeName()+" "+getBaseVarName()+";";
		else return getTypeName()+" "+getBaseVarName()+" = "+((Covis_primitive)cv_class).value+";"; //getValue()にすると，''がとれてしまうので
	}
	public void setCVHist(CVHist_Var _cvhist) {
		cvhist = _cvhist;
	}
	public String getEditValueInfo() {
		if (cv_class instanceof Covis_Object || isArray) return getTypeName()+" "+getBaseVarName()+";";
		else return getBaseVarName()+" = "+((Covis_primitive)cv_class).value+";"; //getValue()にすると，''がとれてしまうので
	}
}
