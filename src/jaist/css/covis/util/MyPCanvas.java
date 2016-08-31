package jaist.css.covis.util;

import java.awt.AWTException;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;

import edu.umd.cs.piccolo.PCamera;
import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PInputManager;
import edu.umd.cs.piccolo.event.PBasicInputEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.event.PInputEventFilter;
import edu.umd.cs.piccolo.event.PPanEventHandler;
import edu.umd.cs.piccolo.util.PBounds;
import edu.umd.cs.piccolox.event.PSelectionEventHandler;
import edu.umd.cs.piccolox.event.PZoomToEventHandler;

@SuppressWarnings("serial")



public class MyPCanvas extends PCanvas {

	static PInputEventFilter b1mask = new PInputEventFilter(
			InputEvent.BUTTON1_MASK);

	static PInputEventFilter b2mask = new PInputEventFilter(
			InputEvent.BUTTON2_MASK);

	static PInputEventFilter b3mask = new PInputEventFilter(
			InputEvent.BUTTON3_MASK);

	static PInputEventFilter disablemask = new PInputEventFilter(0);

	PBasicInputEventHandler flowmenuEventHandler;

	PSelectionEventHandler selectionEventHandler;

	PPanEventHandler panEventHandler;

	PZoomToEventHandler zoomeh;

	Point2D cursorpoint;

	public PCamera cam;

	WheelZoomEventHandler wheelzoom;

	public MouseWheelRotationListener wheelListener;

	/**
	 * ホイール回転時のズーム方向．1または-1．デフォルトは1 = 手前（下）回転でズームイン
	 */
//	public int wheelrotationdirection = -1;

	/**
	 * カーソルを中央にもってくるかどうか
	 */
	public boolean moveMouseCursorOnZoomIn = true;
	Robot robot;

	/**
	 * ズーム時の幅
	 */
	public float wheelZoomRatio = 1.0f;

	public MyPCanvas() {
		super();
		initialize();
		try {
			robot = new Robot();
		} catch (AWTException e) {
		}
	}
	/**
	 * 擬似的なクリック
	 * @param x
	 * @param y
	 */
	public void pseudoClick(int x, int y){
		getRoot().getDefaultInputManager().processEventFromCamera(new MouseEvent(this,0,0,MouseEvent.BUTTON1,x,y,1,true), MouseEvent.MOUSE_PRESSED, getCamera());
		getRoot().getDefaultInputManager().processEventFromCamera(new MouseEvent(this,0,0,MouseEvent.BUTTON1,x,y,1,false), MouseEvent.MOUSE_RELEASED, getCamera());
	}
	
	public void setWheelRotationDirection(int d){
		if (wheelzoom != null) wheelzoom.setDirection(d);
	}
	public void setMouseWheelRotationListener(MouseWheelRotationListener l){
		wheelListener = l;
	}
	public void setWheelZoomRatio(float f){
		wheelZoomRatio = f;
	}

	public PBasicInputEventHandler getWheelListener() {
		return wheelzoom;
	}

	class WheelZoomEventHandler extends PBasicInputEventHandler {
		public int direction = -1;
		public void setDirection(int d){
			direction = d;
		}
		public void mouseWheelRotated(PInputEvent e) {
			if (!e.isControlDown() && !e.isShiftDown() && !e.isAltDown()) {
				PCamera pc = getCamera();

//				System.out.println("wheel rotation: " + pc.getAttribute("hoge"));
				PBounds pb = pc.getViewBounds();
//				System.out.println(pb.toString());
				float f = 1.0f + (0.2f * direction * e.getWheelRotation());
				if (e.getWheelRotation() //下に回すと正になるので
						* direction < 0) {
					pb = zoomBounds_focusbyCursor(pb, f);
				} else {
					pb = BUtil.zoomBounds(pb, f);
				}
				if (pb.x == 0 && pb.y == 0) {
					System.out.println("ズームイン位置がまだ準備前");
					return;
				}
				pc.animateViewToCenterBounds(pb, true, 0);
				if (MyPCanvas.this.wheelListener != null) MyPCanvas.this.wheelListener.mouseWheelRotated(f);
			}
		}
	}

	public void initialize() {
		cam = getCamera();

		disablemask.rejectAllEventTypes();

		wheelzoom = new WheelZoomEventHandler();
		wheelzoom.setEventFilter(b2mask);
		cam.addInputEventListener(wheelzoom);

		// panEventHandler = new PPanEventHandler();
		// getPanEventHandler().setEventFilter(disablemask);
		// panEventHandler.setEventFilter(b2mask);
		// addInputEventListener(panEventHandler);
		getPanEventHandler().setEventFilter(b2mask);
		// check current cursor position
		addInputEventListener(new PInputManager() {
			public void mouseMoved(PInputEvent e) {
				cursorpoint = e.getCanvasPosition();
				try {
					e.getPath().getPathTransformTo(getLayer()).inverseTransform(cursorpoint, cursorpoint);
				} catch (RuntimeException ex) {
					cursorpoint = e.getPosition(); // Camera付きのメニューボタンや，背景部分(PCamera)のとき
				}
				// TODO: 本来であればRuntimeExceptionを出さずに処理したいのだが．．
//				e.getPickedNode().localToParent(cursorpoint);
//				System.out.println(e.getPickedNode().getRoot().getClass().getName());
//				if (e.getPickedNode() instanceof PCamera) 
//				else cursorpoint = e.getPositionRelativeTo(getLayer());
 catch (NoninvertibleTransformException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
	}

	public PBounds zoomBounds_focusbyCursor(PBounds pb, double rate) {
		double x = pb.getX();
		double y = pb.getY();
		double w = pb.getWidth();
		double h = pb.getHeight();
		double nw = w * rate;
		double nh = h * rate;
		Point2D camcp = cam.getViewBounds().getCenter2D();
		double camcx = camcp.getX();
		double camcy = camcp.getY();
//		System.out.println("camcp "+camcx+" "+camcy);
		double curx = cursorpoint.getX();
		double cury = cursorpoint.getY();
//		System.out.println("cursor "+curx+" "+cury);
		double nx = x - (nw - w) / 2 + curx - camcx;
		double ny = y - (nh - h) / 2 + cury - camcy;
		PBounds ret = new PBounds(nx, ny, nw, nh);
		cursorpoint = ret.getCenter2D();
		if (moveMouseCursorOnZoomIn) {
			moveCursorPointCenter();
		} else {			
		}
		return ret;
	}

	public void moveCursorPointCenter() {
		Point canvasglobalp = getLocationOnScreen();
		int canvasw = getWidth();
		int canvash = getHeight();
		if (robot != null)
			robot.mouseMove((int) (canvasglobalp.getX() + canvasw / 2),
					(int) (canvasglobalp.getY() + canvash / 2));
	}
}

//@SuppressWarnings("serial")
//class MyPCanvas_noPointerZoom extends MyPCanvas {
//public PBounds zoomBounds_focusbyCursor(PBounds pb, double rate) {
//return BUtil.zoomBounds(pb, rate);
//}
//}
