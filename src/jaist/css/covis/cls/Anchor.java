package jaist.css.covis.cls;

import jaist.css.covis.CoVisBuffer;
import jaist.css.covis.ToolTipProvider;

import java.awt.BasicStroke;
import java.awt.Color;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;
import edu.umd.cs.piccolo.util.PBounds;
import edu.umd.cs.piccolo.util.PDimension;

class AnchorTab extends PPath implements Move, ToolTipProvider {
	private static final long serialVersionUID = 3651392444867276102L;
	Anchor anchor;
	PNode tooltip;
	public AnchorTab(Anchor a){
		anchor = a;
		setPathToEllipse(-20, -20, 20, 20);
		setPaint(Color.orange);
		setStrokePaint(Color.orange.darker());
		setStroke(new BasicStroke(1));
		setAnchorEnabled(false);
	}
	public void move(PDimension d){
		translate(d.getWidth(), d.getHeight()); //履歴に関係ない動作
	}
	//	public String referenceVarNames(){
	//		return anchor.srcVariable.getVarName();
	//	}
	public PNode getToolTipNode(){
		tooltip = null;
		if (tooltip == null) {
			PText pt = new PText(anchor.srcVariable.getVarName());
			pt.setPaint(Color.orange);
			tooltip = pt;
		}
		return tooltip;
	}
	public void setAnchorEnabled(boolean active){
		if (active){
			addAttribute("moveTarget", this);
			addAttribute("tooltip", this);
		} else {
			if (anchor.srcVariable.object != null){
				addAttribute("moveTarget", anchor.srcVariable.object);
				addAttribute("tooltip", anchor.srcVariable.object);
			} else {
				addAttribute("moveTarget", anchor.srcVariable);
				addAttribute("tooltip", anchor.srcVariable);
			}
		}
	}
}

public class Anchor extends PPath implements Runnable, DragNotice {
	private static final long serialVersionUID = -8491055463977967303L;
	//	Variable var;
	public Covis_Object destObject;
	public Covis_Object lastDestObject;
	AnchorTab anchortab;

	RefLink link;
	Thread rewindThread;
	public boolean isOperating = false;

	public Class<?> type;
	public String varName;

	long toFront_ts;
	int aryIndex = -1;
	//	public PNode src; // Variable or Covis_Array
	public Variable srcVariable; // Variable or Covis_Array
	public boolean lastDetach = false; //最後にアタッチされていたオブジェクト（はずして，すぐに同じおぶじぇくとにつけるのは禁止）
	public Anchor(Class<?> t, Variable _src, int aI){
		this(t,_src);
		aryIndex = aI;
	}
	public Anchor(Class<?> t, Variable _src){
		type = t;
		srcVariable = _src;
		aryIndex = -1;
		setPathToEllipse(-20, -20, 20, 20);
		//		setPaint(Color.orange);
		//		setStrokePaint(Color.orange.darker());
		//		setStroke(new BasicStroke(1));
		setPaint(null);
		setStrokePaint(null);

		//		co.setScale(0.3f);
		//		addChild(cv_class);
		//		layout(0);

		//		addAttribute("moveTargetY", this);
		addAttribute("info", "Anchor "+this.toString());
		//		addAttribute("selectable", this);
		//		addAttribute("moveTarget", this);
		//		addAttribute("dragLayout", this);

		anchortab = new AnchorTab(this);

		link = new RefLink(this,anchortab,this);
		addChild(link);
		anchortab.addAttribute("moveLink", link);
		addAttribute("moveLink", link);

		addChild(anchortab);
		anchortab.addAttribute("dragNotice", this);
		anchortab.addAttribute("toFront", this);

		rewindThread = new Thread(this);
		rewindThread.start();
	}
	public void start_RewindThread(){
		if (rewindThread == null){
			rewindThread = new Thread(this);
			rewindThread.start();
			//			System.out.println("Start thread");
		}
	}
	// インカミングリンクが入ったら，trueになる＞アンカータブが動くようになる．
	public void setAnchorEnabled(boolean active){
		if (active){
			anchortab.addAttribute("moveTarget", this.anchortab);
			anchortab.addAttribute("tooltip", this.anchortab);
		} else {
			anchortab.addAttribute("moveTarget", srcVariable.object);
			anchortab.addAttribute("tooltip", srcVariable.object);
		}
	}


	public void run() {
		int count = 0;
		while(count < 10){
			if (!isOperating) {
				PDimension d = link.diff();
				anchortab.translate(d.getWidth(), d.getHeight());
				link.update();
				repaint();
				double size = d.getHeight()*d.getWidth();
				if (size < 0.00000001) count++;
				//				preSize = size;
				//				count++;
			}
			//			System.out.print(".");
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		rewindThread = null;
	}

	public void setDragNotice(boolean f) {
		isOperating = f;
	}

	public void drag(PInputEvent e, CoVisBuffer buffer) {
		if (!srcVariable.isEnabled()) {
			anchortab.setPaint(Color.gray);
			return;
		}
		boolean isAnyOverlap = false;
		boolean isTypeMatch = true;
		toFront(System.currentTimeMillis());
		PBounds pb = anchortab.getGlobalBounds();
		for(PNode co: buffer.objField.getAllNodes()){
			if (!(co instanceof Covis_Object)) continue;
			PBounds cob = co.getGlobalBounds();
			if (cob.contains(pb) || pb.intersects(cob)){
				Covis_Object candidate = (Covis_Object) co;
				// type が candidate のスーパークラス（type hoge = candidateができる）ならtrue
				//				System.out.println("type "+type.toString());
				//				System.out.println("can "+candidate.type.toString());
				// if (candidate instanceof type)
				if (type.isAssignableFrom(candidate.type)){
					//					if (candidate.getClsName().equals(var.cv_class.getClsName())){	//TODO:本当はサブクラスチェック
					destObject = candidate;
					anchortab.setPaint(Color.red);
					isAnyOverlap = true;
				} else {
					isTypeMatch = false;
				}
			}
		}
		if (!isAnyOverlap) {
			if (isTypeMatch && srcVariable.isEnabled()) anchortab.setPaint(Color.orange); else {
				anchortab.setPaint(Color.gray);
			}
			if (destObject != null){
				destObject.detach(this);
				lastDetach = true;
				destObject = null;
			}
		}
	}

	public void endDrag(PInputEvent e, CoVisBuffer buffer) {
		if (anchortab.getPaint() == Color.gray) Informer.playSound("RecEnd.wav");

		anchortab.setPaint(Color.orange);
		start_RewindThread();
		if (!srcVariable.isEnabled()) {
			return;
		}
		if (destObject != null){
			destObject.attach(this);
		} else {
			if (lastDetach) {
				if (lastDestObject != null){
					buffer.putHistoryUnLink("unlink", this);
					lastDestObject = null;
				}
				lastDetach = false;
			}
		}
	}
	public void setVarName(String s){
		varName = getVarName();
		anchortab.tooltip = null;
		if (destObject != null) {
			destObject.tooltip = null;
			if (destObject instanceof Covis_Array){
				Covis_Array ca = ((Covis_Array)destObject);
				for(Anchor a: ca.anchors_member){
					a.anchortab.tooltip = null;
					if (a.destObject != null) a.destObject.tooltip = null;
				}
			}
		}
	}
	//TODO:

	public Class<?> getVarClass(){
		return srcVariable.type;
		//		if (src instanceof Variable) return ((Variable)src).type;
		//		else if (src instanceof Covis_Array) return ((Covis_Array)src).type.getComponentType();
		//		else return null;
	}
	//TODO:

	public String getVarName(){
		return srcVariable.getBaseVarName();
		//		if (src instanceof Variable) return ((Variable)src).varname;
		//		else if (src instanceof Covis_Array) {
		//			//			System.out.println("yes, covis_array");
		//			Covis_Array ca = ((Covis_Array)src);
		//			if (ca.referenceVarNames() != null){
		//				String[] o = ca.referenceVarNames().split("\n");
		//				StringBuffer sb = new StringBuffer();
		//				for(String s:o){
		//					sb.append(s+"["+aryIndex+"]\n");
		//				}
		//				return sb.toString().substring(0, sb.length()-1);
		//			}
		//		}
		//		System.out.println("Anchor.getVarNameがnullを返しました");
		//		return null;
	}
	public Variable getSrcVariable(){
		return srcVariable;
	}

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
		}
	}
	//TODO:
	public String getClsName(){
		return srcVariable.getTypeName();
		//		if (src instanceof Covis_Array){
		//			return ((Covis_Array)src).elementObj.getClsName();
		//		} else if (src instanceof Covis_Object){
		//			return ((Covis_Object)src).getClsName();
		//		} else if (src instanceof Variable){ 
		//			return ((Variable)src).getTypeName();
		//		}
		//		return "error_in_Anchor_getClsName()";
	}
}
