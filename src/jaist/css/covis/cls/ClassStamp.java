package jaist.css.covis.cls;

import jaist.css.covis.CoVisBuffer;
import jaist.css.covis.Selectable;

import java.awt.BasicStroke;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.TreeMap;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;
import edu.umd.cs.piccolo.util.PDimension;

public class ClassStamp extends PPath implements Layoutable, Selectable, Move {
	public static BasicStroke roundrectStroke = new BasicStroke(4f,BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 90.0f);

	private static final long serialVersionUID = -2274392520995258292L;
	public static Object[] possibilities = {"Object", "Oval", "Rect", "String", "int", "char"};
	public Covis_Type cv_type;
	PNode cv_class_for_tooltip;
	PText tooltip_classname;
	public static ClassStamp selectedType;
	public static ArrayList<ClassStamp> stamps;
	static{
		stamps = new ArrayList<ClassStamp>();
	}
	private boolean isSelected;

	PPath handle;
	PText caption;
	public static float top = 27;
	public CoVisBuffer buffer;
	public ClassStamp(Covis_Type co, CoVisBuffer buf){
		cv_type = co;
		buffer = buf;

		setPathToRectangle(0,top,200,100);
		setPaint(new Color(230,230,200));
		setStrokePaint(Color.gray);
		setStroke(new BasicStroke(1));

		addChild(cv_type);
		layout(0);

		//		addAttribute("moveTargetY", this);
		addAttribute("info", "ClassStamp "+this.toString());
		addAttribute("selectable", this);
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
		handle.addAttribute("selectable", this);
		handle.addAttribute("exclusiveSelectable", stamps);
		addChild(handle);

		caption = new PText(cv_type.getClsName());
		caption.scale(2);
		caption.setOffset(10,-2);
		caption.addAttribute("moveTargetY", this);
		caption.addAttribute("dragLayout", this);
		addChild(caption);

		stamps.add(this);

		addAttribute("popupMenu", new ClassStampMenu(this, buffer));
		cv_type.addAttribute("popupMenu", new ClassStampMenu(this, buffer));
		handle.addAttribute("popupMenu", new ClassStampMenu(this, buffer));
		caption.addAttribute("popupMenu", new ClassStampMenu(this, buffer));
	}
	public String toString(){
		return cv_type.getClsName();
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


		if (f) {
			handle.setTransparency(0.8f);
			//			Informer.playSound("Default.wav");
			Informer.playSound("Pop.wav");
		} else {
			handle.setTransparency(0.0f);
		}
		if (f && list != null && !list.contains(this)) list.add(this);
		if (!f && list != null && list.contains(this)) list.remove(this);
		if (f) selectedType = this; else {
			if (selectedType == this) {
				selectedType = null;
			}
		}
	}

	public PNode theNode() {
		return this;
	}

	public void toFront() {
		getParent().addChild(this);
	}

	public void toggleSelected(ArrayList<Selectable> list) {
		setSelected(!isSelected, list);
	}

	public void removeLabel(Hashtable<PNode, PNode> trash) {
		if (trash != null) trash.put(this, getParent());
		removeFromParent();
	}
	public PNode getToolTipNode(){
		if (cv_class_for_tooltip == null) {
			cv_class_for_tooltip = cv_type.createToolTip();
			cv_class_for_tooltip.setScale(0.5f);
		}
		return cv_class_for_tooltip;
	}

	public PNode getToolTipNode_forVariable() {
		if (tooltip_classname == null) {
			tooltip_classname = new PText("Click to add variable\nof '"+cv_type.getClsName()+"'");
			tooltip_classname.setScale(1.5f);
		}
		return tooltip_classname;
	}
	public void move(PDimension d){
		translate(d.getWidth(), d.getHeight()); //óöóÇ…ä÷åWÇ»Ç¢ìÆçÏ
	}
}
