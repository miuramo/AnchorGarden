package jaist.css.covis.cls;

import jaist.css.covis.CoVisBuffer;
import jaist.css.covis.ToolTipProvider;
import jaist.css.covis.fm.FlowMenu_TMRG;

import java.awt.Color;
import java.awt.Font;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;
import edu.umd.cs.piccolo.util.PDimension;

public class StaticField extends PPath implements Layoutable , ToolTipProvider, ClickHandler, Move {
	private static final long serialVersionUID = -6949546345615986003L;
	public static PNode nullToolTip = new PNode();
	public String typeName; // Variable, Object, Class
	public Color color;
	PText caption;
	public static Font bold = new Font("sansserif", Font.BOLD, 12);
	public static float top = 40;
	public CoVisBuffer buffer;
	public StaticField(String _typeName, Color c, CoVisBuffer buf){
		color = c;
		typeName = _typeName;
		buffer = buf;
		setPathToRectangle(0, 0, 200, 100);
		setPaint(color);
		setStrokePaint(null);
//		setStroke(new BasicStroke(1));
		scale(1);
//		setTransparency(0.4f);
		
		this.addAttribute("moveTarget", this);
		this.addAttribute("info", "Field "+toString());
		this.addAttribute("selectable", this);
		
		caption = new PText(typeName);
		caption.scale(3);
		caption.setFont(bold);
		caption.setOffset(10,0);
		caption.addAttribute("moveTarget", this);
		caption.addAttribute("dragLayout", this);
		addChild(caption);
		
		this.addAttribute("clickHandler", this);
	}
	
	public void layout(int dur){
		layoutExceptOne(null, dur);
	}
	
	public void layoutExceptOne(PNode operationNode, int dur) {
		List<PNode> col = getChildrenReference();
		TreeMap<Double,PNode> map = new TreeMap<Double, PNode>();
		for(PNode p: col) {
			double y = p.getYOffset();
			while(map.get(y)!=null) y+=0.001;
			if (p != caption) map.put(y, p);
		}
		
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
			offsety += 1;
		}
		animateToBounds(0, 0, maxx, maxy+top, dur);
	}

//
//	public PNode getToolTipNode() {
//		if (ClassStamp.selectedType == null) return nullToolTip;
//		PNode p = ClassStamp.selectedType.getToolTipNode();
//		p.setTransparency(0.6f);
//		return p;
//	}
	@Override
	public PNode getToolTipNode() {
		return null;
	}

	@Override
	public void clicked(PInputEvent e, FlowMenu_TMRG fmenu) {
	}

//	public void clicked(PInputEvent e, FlowMenu_TMRG fmenu) {
//		if (ClassStamp.selectedType == null) {
//			Informer.playSound("RecEnd.wav");
//			return;
//		}
//		else {
//			boolean isAuto = buffer.getWindow().isAutoMode_obj.isSelected();
//			//SHIFT押していたら，マニュアル/オートをトグル
//			if (e.isShiftDown()) isAuto = !isAuto;
//			Covis_Object o = null;
//			try {
//				o = (Covis_Object) ClassStamp.selectedType.cv_type.createNew(fmenu.window.frame, isAuto);
//			} catch (InvocationTargetException e1) {
////				e1.printStackTrace();
//			}
//			if (o == null) {
//				Informer.playSound("RecEnd.wav");
//				return;
//			}
//			addChild(o);
//			buffer.putHistoryNew("new", o);
////			Informer.playSound("hwandsw.wav");
//			o.addAttribute("moveTarget", o);
//			o.addAttribute("selectable", o);
//			o.setOffset(e.getPositionRelativeTo(this));
//			if (o instanceof Covis_Object)
//				((Covis_Object)o).tc.transparencyThread_Start(0, this);
//			Informer.playSound("Fairydust.wav");
//		}
//	}
	
	public void move(PDimension d){
		translate(d.getWidth(), d.getHeight()); //履歴に関係ない動作
		
		for(PNode p: getChildrenReference()) {
			if (p instanceof Move) ((Move)p).move(new PDimension());
		}
	}

	public void addObject(ClassStamp f) {
		boolean isAuto = buffer.getWindow().isAutoMode_obj.isSelected();
		addObject(f, isAuto);
	}
	public void addObject(ClassStamp f, boolean isAuto) {
		Covis_Object o = null;
		try {
			o = (Covis_Object) f.cv_type.createNew(buffer.getWindow().frame, isAuto);
		} catch (InvocationTargetException e1) {
//			e1.printStackTrace();
		}
		if (o == null) {
			Informer.playSound("RecEnd.wav");
			return;
		}
		addChild(o);
		buffer.putHistoryNew("new", o);
//		Informer.playSound("hwandsw.wav");
		o.addAttribute("moveTarget", o);
		o.addAttribute("selectable", o);
		o.setOffset(getWidth()/2, getHeight()/2);
		if (o instanceof Covis_Object)
			((Covis_Object)o).tc.transparencyThread_Start(0, this, 100);
		Informer.playSound("Fairydust.wav");
	}
	public void addObject(Covis_Object o){
		addChild(o);
//		buffer.putHistoryNew("new", o);
//		Informer.playSound("hwandsw.wav");
		o.addAttribute("moveTarget", o);
		o.addAttribute("selectable", o);
		o.setOffset(getWidth()/2, getHeight()/2);
		if (o instanceof Covis_Object)
			((Covis_Object)o).tc.transparencyThread_Start(0, this, 200);
		Informer.playSound("Fairydust.wav");		
	}
	
	public void clearObjVarName(){
		List<PNode> col = new ArrayList<PNode>(getChildrenReference());
		for(PNode p: col) {
			if (p instanceof Covis_Object){
				Covis_Object co= (Covis_Object)p;
//				v.myrefs.clear();
				for(Anchor a: co.anchors_member){
					a.srcVariable.clearVarNamesAry();
				}
			}
		}
	}

}
