package jaist.css.covis.fm;

import jaist.css.covis.AnchorGarden;
import jaist.css.covis.CoVisWindow_SelectionRegionHandler;
import jaist.css.covis.Selectable;
import jaist.css.covis.cls.ClickHandler;
import jaist.css.covis.cls.DragNotice;
import jaist.css.covis.cls.Layoutable;
import jaist.css.covis.cls.LinkMovable;
import jaist.css.covis.cls.Move;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.util.PBounds;
import edu.umd.cs.piccolo.util.PDimension;

/**
 * フローメニュー
 * 
 * @author miuramo
 *
 */
public class FlowMenu_TMRG extends AbstractFlowMenu {

//	Category log;

	public AnchorGarden window;

	public Point2D inkbasep;

	public PBounds sptargetbb;

	public String info; // PNode.getAttribute("info");

	public PNode moveTarget; // PNode.getAttribute("moveTarget");

	public PNode moveTargetY; // PNode.getAttribute("moveTargetY");

	public Selectable selectTarget; // PNode.getAttribute("selectable");

	public ArrayList<Selectable> exclusiveSelectionBag = new ArrayList<Selectable>();

	public ClickHandler clickHandler; // PNode.getAttribute("clickHandler");

	public DragNotice dragNotice;

	public long lastRelease;

	public ArrayList<PInputEvent> drags;

	public CoVisWindow_SelectionRegionHandler selectionHandler;

	public boolean isShiftDown;

	public boolean enabled = true;

	public ArrayList<Selectable> childGroupingBag = new ArrayList<Selectable>();

	public PNode getTarget() {
		return target;
	}

	public int getX() {
		return (int) camerap.getX();
	}

	public int getY() {
		return (int) camerap.getY();
	}

	public void changeState(State newstate) {
		if (fmenu != null) {
			camera.addChild(fmenu);
			camera.removeChild(fmenu);
		}
		fmenu = newstate;
		fmenu.paint();
		camera.addChild(fmenu);
	}

	public FlowMenu_TMRG(AnchorGarden _window) {
		super(_window.frame, _window.getCanvas(), _window.getCanvas().getCamera());
//		log = Logger.getLogger(FlowMenu_TMRG.class.getName());

		window = _window;

		selectionHandler = new CoVisWindow_SelectionRegionHandler(window);

		drags = new ArrayList<PInputEvent>();

		// fmenu setup
		fmenu_orig = new DefaultState(this, "");
	}





	public void startDrag_pre(PInputEvent e) {
		super.startDrag_pre(e);
	}

	public void startDrag_post(PInputEvent e) {
		Object o = target.getAttribute("NoFlowMenu");
		if (o != null && o.toString().equals("true")) {
		} else {
			// TODO:メニューの起動
			/*
			daemon = new java.util.Timer();
			daemon.schedule(new MyTimerTask_GKJFlowMenuOpen((float) camerap.getX(),
					(float) camerap.getY(), this), 3000);
			 */
		}
	}

	@SuppressWarnings("unchecked")
	public void startDrag(PInputEvent e) {
		super.startDrag(e);
		startDrag_pre(e);

		if (target.getAttribute("info")!=null){
			info = (String) target.getAttribute("info");
//			log.debug(info);
//			log.debug(target.getBounds().toString());
		}
		if (target.getAttribute("moveTarget")!=null){
			moveTarget = (PNode) target.getAttribute("moveTarget");
		}
		if (target.getAttribute("moveTargetY")!=null){
			moveTargetY = (PNode) target.getAttribute("moveTargetY");
		}
		if (target.getAttribute("dragNotice")!=null){
			dragNotice = (DragNotice) target.getAttribute("dragNotice");
			dragNotice.setDragNotice(true);
		}
		if (target.getAttribute("exclusiveSelectable")!=null){
			selectTarget = (Selectable) target.getAttribute("selectable");
			exclusiveSelectionBag = (ArrayList<Selectable>) target.getAttribute("exclusiveSelectable");
		}
		if (target.getAttribute("clickHandler")!=null){
			clickHandler = (ClickHandler) target.getAttribute("clickHandler");
		}
		if (e.isShiftDown()) {
			isShiftDown = true;
			enabled = false;
			selectionHandler.startDrag(e);
			startDrag_post(e);
			return;
		}
		startDrag_post(e);

		window.setShowToolTip(false);

	}

	public void drag(PInputEvent e) {
		super.drag(e);

		if (fmenu != null)
			fmenu.drag(e);
		else {
			if (isShiftDown && e.isShiftDown()) {
				selectionHandler.drag(e);
				return;
			}
			drags.add(e);
			if (moveTarget != null){
				PDimension d = e.getDeltaRelativeTo(window.canvas.getLayer());
				if (moveTarget instanceof Move) ((Move)moveTarget).move(d);
				//				System.out.println("XY: "+moveTarget.getTransform().getTranslateX()+" "+moveTarget.getTransform().getTranslateY());
				if (moveTarget.getAttribute("moveLink") != null){
					LinkMovable lm = (LinkMovable) moveTarget.getAttribute("moveLink");
					lm.update();
				}
				if (dragNotice != null){
					dragNotice.drag(e, window.buffer);
				}
			} else if (moveTargetY != null){
				PDimension d = e.getDeltaRelativeTo(window.canvas.getLayer());
				if (moveTargetY  instanceof Move) ((Move)moveTargetY).move(new PDimension(0,d.getHeight()));

				if (moveTargetY.getAttribute("dragLayout")!=null){
					Object o = ((PNode)moveTargetY.getAttribute("dragLayout")).getParent();
					if (o instanceof Layoutable){
						Layoutable layout = (Layoutable) (((PNode)moveTargetY.getAttribute("dragLayout")).getParent());
						layout.layoutExceptOne(moveTargetY, 0);
					}
				}
				if (moveTargetY.getAttribute("moveLink")!=null){
					LinkMovable lm = (LinkMovable) moveTargetY.getAttribute("moveLink");
					lm.update();
				}
			} else {
				PDimension d = e.getDelta();
				window.canvas.getCamera().translateView(d.getWidth(), d.getHeight());
			}
		}

	}

	public void endDrag(PInputEvent e) {
		super.endDrag(e);

		if (fmenu != null)
			fmenu.endDrag(e);
		//		if (isShiftDown) {
		//			selectionHandler.endDrag(e);
		//		}
		if (drags.size() < 3){
			if (selectTarget != null){
				ArrayList<Selectable> copyBag = new ArrayList<Selectable>(exclusiveSelectionBag);
				copyBag.remove(selectTarget);
				for(Selectable sel : copyBag){
					sel.setSelected(false, null);
				}
				selectTarget.toggleSelected(null);
			}
			if (clickHandler != null){
				clickHandler.clicked(e, this);
			}		
		}
		clickHandler = null;

		for(Selectable sel: new ArrayList<Selectable>(childGroupingBag)) sel.setSelected(false, childGroupingBag);

		if (daemon != null) {
			daemon.cancel();
			daemon = null;
		}
		// long ctime = (new Date().getTime() - stime);
		//		if (System.currentTimeMillis() - lastRelease < 350){ //ダブルクリック判定 dbl
		//			if (moveTarget != null){
		//				PBounds pb = moveTarget.getGlobalFullBounds();
		//				pb = BUtil.zoomBounds(pb, 1.05);
		//				window.canvas.getCamera().animateViewToCenterBounds(pb, true, 500);
		//			}
		//		}


		moveTarget = null;

		if (moveTargetY != null && moveTargetY.getAttribute("dragLayout")!=null){
			Object o = ((PNode)moveTargetY.getAttribute("dragLayout")).getParent();
			if (o instanceof Layoutable){
				Layoutable layout = (Layoutable) (((PNode)moveTargetY.getAttribute("dragLayout")).getParent());
				layout.layout(200);
			}
		}
		moveTargetY = null;
		selectTarget = null;
		lastRelease = System.currentTimeMillis();


		if (dragNotice != null){
			dragNotice.endDrag(e,window.buffer);
			dragNotice.setDragNotice(false);
			dragNotice = null;
		}

		window.setShowToolTip(true);
		drags.clear();
		enabled = true;
		isShiftDown = false;
	}

	public void endDrag_mid(PInputEvent e) {
		super.endDrag_mid(e);	
	}
}
