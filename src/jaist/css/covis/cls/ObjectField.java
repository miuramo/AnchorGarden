package jaist.css.covis.cls;

import jaist.css.covis.CoVisBuffer;
import jaist.css.covis.ToolTipProvider;
import jaist.css.covis.fm.FlowMenu_TMRG;

import java.awt.Color;
import java.awt.Font;
import java.awt.geom.Point2D;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;
import edu.umd.cs.piccolo.util.PBounds;
import edu.umd.cs.piccolo.util.PDimension;

public class ObjectField extends PPath implements Layoutable , ToolTipProvider, ClickHandler, Move {
	private static final long serialVersionUID = 3524513728998576206L;
	public static PNode nullToolTip = new PNode();
	public String typeName; // Variable, Object, Class
	public Color color;
	PText caption;
	public static Font bold = new Font("sansserif", Font.BOLD, 12);
	public static float top = 40;
	public CoVisBuffer buffer;
	public ObjectField(String _typeName, Color c, CoVisBuffer buf){
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
			if (p != caption) map.put(p.getYOffset(), p);
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
			offsety += 10;
		}
		animateToBounds(0, 0, maxx, maxy+top, dur);
	}
	public PNode getToolTipNode() {
		if (ClassStamp.selectedType == null) return nullToolTip;
		PNode p = ClassStamp.selectedType.getToolTipNode();
		p.setTransparency(0.6f);
		return p;
	}

	public void clicked(PInputEvent e, FlowMenu_TMRG fmenu) {
//		if (e.isShiftDown()){
//			SrcExecutor s = new SrcExecutor(buffer);
//			String f = JOptionPane.showInputDialog(null);
//			s.execute(f);
//			return;
//		}
		if (ClassStamp.selectedType == null) {
			Informer.playSound("RecEnd.wav");
			return;
		}
		else {
			boolean isAuto = buffer.getWindow().isAutoMode_obj.isSelected();
			//SHIFT押していたら，マニュアル/オートをトグル
			if (e.isShiftDown()) isAuto = !isAuto;
			Covis_Object o = null;
			try {
				o = (Covis_Object) ClassStamp.selectedType.cv_type.createNew(fmenu.window.frame, isAuto);
			} catch (InvocationTargetException e1) {
				//				e1.printStackTrace();
			}
			if (o == null) {
				Informer.playSound("RecEnd.wav");
				return;
			}
			if (o.getVisible()==false) return;//TODO:ここは，コンストラクタダイアログがキャンセルされたときに使う
			addChild(o);
			buffer.putHistoryNew("new", o);
			//			Informer.playSound("hwandsw.wav");
			o.addAttribute("moveTarget", o);
			o.addAttribute("selectable", o);
			o.setOffset(e.getPositionRelativeTo(this));
			if (o instanceof Covis_Object)
				((Covis_Object)o).tc.transparencyThread_Start(0, this, 100);
			Informer.playSound("Fairydust.wav");
		}
	}

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
	public Point2D getNextNewObjectPosition(){
		//まず，下方向の余裕をさがす
		double ycandidate = 0;
		List<PNode> col = new ArrayList<PNode>(getChildrenReference());
		for(PNode p: col) {
			double tempy = p.getYOffset()+p.getHeight();
			if (ycandidate < tempy) ycandidate = tempy;
		}
		if (ycandidate < this.getHeight()- 60){
			return new Point2D.Double(20,ycandidate+20);
		}
		//次に，右方向でさがす
		//		PPath checkPath = PPath.createRectangle(0, 0, 110, 55);
		for(int y = 1; y< (this.getHeight()/20)-4; y++){
			for(int x = 1; x< (this.getWidth()/20)-1; x++){
				//				checkPath.setOffset(x*120,y*60);
				PNodeBoundsIntersectsFilter rpf = new PNodeBoundsIntersectsFilter(
						new PBounds(x*20+getX()+getOffset().getX()-10,y*20+getY()+getOffset().getY()+10,120,80));
				ArrayList<PNode> pnbuf = new ArrayList<PNode>();
				this.getAllNodes(rpf, pnbuf);
				for(PNode pn: pnbuf){
					System.out.println("x="+x+" y="+y+" "+pn.toString());
				}
				if (pnbuf.size()==0) {
					System.out.println("found x="+x+" y="+y);
					return new Point2D.Double(x*20,y*20+30);
				}
			}			
		}
		return new Point2D.Double(getWidth()/3, getHeight()/3);
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
		o.setOffset(getNextNewObjectPosition());
		if (o instanceof Covis_Object)
			((Covis_Object)o).tc.transparencyThread_Start(0, this, 100);
		Informer.playSound("Fairydust.wav");
	}
	//メソッド内でよばれたとき，200%の時間をかけて，ゆっくりとフェードアウト
	public void addObject(Covis_Object o){
		addChild(o);
		//		buffer.putHistoryNew("new", o);
		//		Informer.playSound("hwandsw.wav");
		o.addAttribute("moveTarget", o);
		o.addAttribute("selectable", o);
		if (o.anchors_incoming.size()<1){
			o.setOffset(getNextNewObjectPosition());
			((Covis_Object)o).tc.transparencyThread_Start(0, this, 200);
			Informer.playSound("Fairydust.wav");		
		}
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
