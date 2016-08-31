package jaist.css.covis.cls;

import jaist.css.covis.CoVisBuffer;
import jaist.css.covis.ToolTipProvider;
import jaist.css.covis.fm.FlowMenu_TMRG;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.TreeMap;

import javax.swing.Timer;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;
import edu.umd.cs.piccolo.util.PDimension;

public class VarField extends PPath implements Layoutable , ToolTipProvider, ClickHandler, Move {
	private static final long serialVersionUID = 3524513728998576206L;
	public static PNode nullToolTip = new PNode();
	public String typeName; // Variable, Object, Class
	public Color color;
	PText caption;
	public static Font bold = new Font("sansserif", Font.BOLD, 12);
	public static float top = 40;
	public Timer rewindTimer;
	public CoVisBuffer buffer;
	public static ArrayList<VarField> vflist;
	static {
		vflist = new ArrayList<VarField>();
	}
	/**
	 * VarField複数化計画
	 * Buffer.setHistLinkCode_ConcatinateVarDefAndNew がうまく探せるように再設計する
	 * @param _typeName
	 * @param c
	 * @param buf
	 */
	
	public VarField(String _typeName, Color c, CoVisBuffer buf){
		color = c;
		typeName = _typeName;
		buffer = buf;
		setPathToRectangle(0, 0, 255, 100);
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
		
		rewindTimer = new Timer(100, new ActionListener(){
			public void actionPerformed(ActionEvent e){
				move(new PDimension());
				rewindTimer.stop();
			}
		});
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
		double endy = 30; //少し余白をつくり，変数追加しやすく
		double maxx = 0, maxy = 0; 
		for(PNode p : map.values()){
//			p.setOffset(offsetx, offsety);
			double px = p.getBounds().width;
			double py = p.getBounds().height;
			if (maxx < offsetx + endx + px) maxx = offsetx + endx + px;
			if (maxy < offsety + endy + py) maxy = offsety + endy + py;
			if (operationNode != p) {
				p.animateToPositionScaleRotation(offsetx, offsety+top, 1, 0, dur);
				if (p instanceof Move) ((Move)p).move(new PDimension());
			}
			offsety += py;
			offsety += 10;
		}
		if (maxx < 200) maxx = 200;
		if (maxy+top < 100) maxy = 100-top;
		animateToBounds(0, 0, maxx, maxy+top, dur);
		
//		System.out.println("Layout");
		
		rewindTimer.start();
	}
	public PNode getToolTipNode() {
		if (ClassStamp.selectedType == null) {
			PText p = new PText("First, select one ' Type '\non the left column");
			p.setScale(1.4f);
			p.setTextPaint(Color.red);
			return p;
//			return nullToolTip;
		}
		PNode p = ClassStamp.selectedType.getToolTipNode_forVariable();
		return p;
	}

	public void clicked(PInputEvent e, FlowMenu_TMRG fmenu) {
		if (ClassStamp.selectedType == null) return;
		else {
			
			Variable var = null;
			try {
				var = new Variable(ClassStamp.selectedType.cv_type.newInstance(true), fmenu.window.frame, buffer,false);
			} catch (InvocationTargetException e1) {
				e1.printStackTrace();
			}
			if (var.type == null) return;
			var.setTransparency(0.0f);
			addChild(var);
			var.addAttribute("moveTargetY", var);
//			var.addAttribute("selectable", var);
			var.setOffset(e.getPositionRelativeTo(this));
			layout(200);
			boolean isAuto = buffer.getWindow().isAutoMode.isSelected();
			//SHIFT押していたら，マニュアル/オートをトグル
			if (e.isShiftDown()) isAuto = !isAuto;

			if (!isAuto) if (var.rename(buffer.getWindow().frame) == null) {
				var.dispose();
				return;
			}
			if (!isAuto && var.cv_class instanceof Covis_primitive && !var.isArray) ((Covis_primitive)var.cv_class).edit(buffer.getWindow().frame,var,true);
			var.setTransparency(1.0f);
//			Informer.playSound("Default.wav");
			Informer.playSound("Pop.wav");
			buffer.putHistoryVar("var", var, true);
		}
	}
	
	public void addVariable(ClassStamp cs){
		boolean isAuto = buffer.getWindow().isAutoMode.isSelected();
		addVariable(cs,isAuto);
	}
	public void addVariable(ClassStamp cs, boolean isAuto){
		Variable var = null;
		try {
			var = new Variable(cs.cv_type.newInstance(true), buffer.getWindow().frame, buffer,false);
		} catch (InvocationTargetException e1) {
			e1.printStackTrace();
		}
		if (var.type == null) return;
		var.setTransparency(0.0f);
		addChild(var);
		var.addAttribute("moveTargetY", var);
//		var.addAttribute("selectable", var);
		var.setOffset(0,this.getHeight());
		layout(200);
		if (!isAuto) if (var.rename(buffer.getWindow().frame) == null) {
			var.dispose();
			return;
		}
		if (!isAuto && var.cv_class instanceof Covis_primitive && !var.isArray) ((Covis_primitive)var.cv_class).edit(buffer.getWindow().frame,var,true);
		var.setTransparency(1.0f);
//		Informer.playSound("Default.wav");
		Informer.playSound("Pop.wav");
		buffer.putHistoryVar("var", var, true);
	}

	public boolean checkExistName(String input, Variable variable) {
		List<PNode> col = getChildrenReference();
		for(PNode p: col) {
			if (p instanceof Variable){
				Variable v = (Variable)p;
				if (v.getBaseVarName().equals(input) && v != variable) return true;
			}
		}
		return false;
	}
	
	public void move(PDimension d){
		translate(d.getWidth(), d.getHeight()); //履歴に関係ない動作
		for(PNode p: getChildrenReference()) {
			if (p instanceof Move) ((Move)p).move(new PDimension());
		}
	}

	public void removeAllVariables() {
		List<PNode> col = new ArrayList<PNode>(getChildrenReference());
		for(PNode p: col) {
			if (p instanceof Variable){
				Variable v = (Variable)p;
				v.dispose();
			}
		}
		layout(500);
	}
	public Variable findVar(String head){
		List<PNode> col = new ArrayList<PNode>(getChildrenReference());
		for(PNode p: col) {
			if (p instanceof Variable){
				Variable v = (Variable)p;
				if (head.startsWith(v.getBaseVarName())) return v;
			}
		}
		return null;
	}
	
	public void updateVarNameChain(){
		List<PNode> col = new ArrayList<PNode>(getChildrenReference());
		for(PNode p: col) {
			if (p instanceof Variable){
				Variable v = (Variable)p;
//				System.out.println("start OrigVariable "+v.getVarName());
				v.appendVarNameRecursive(new Hashtable<Integer,Object>());//具体的には，リンク先に名前を設定（追加）．その先にVarMがあれば，さらに追加していく．
			}
		}
		layout(500);
	}
}
