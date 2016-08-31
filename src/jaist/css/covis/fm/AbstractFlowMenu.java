package jaist.css.covis.fm;

import java.awt.Cursor;
import java.awt.geom.Point2D;

import javax.swing.JFrame;

import edu.umd.cs.piccolo.PCamera;
import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PDragSequenceEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;

/**
 * フローメニューの共通部分
 * @author miuramo
 *
 */
public class AbstractFlowMenu extends PDragSequenceEventHandler {

	public static Cursor handcursor = new Cursor(Cursor.HAND_CURSOR);

	public static Cursor defaultcursor = new Cursor(Cursor.DEFAULT_CURSOR);

	// public V geopanel;
	// public PPath squiggle;
	public int strokecount;

	public long stime;

	public PNode target;

	public JFrame frame;

	// public StudentPanel sptarget;
	public Point2D tp;

	public State fmenu, fmenu_orig;

	public PCanvas canvas;

	public PCamera camera;

	public Point2D basep;

	public Point2D camerap;

	// public PBounds sptargetbb;
	// private State now_state;
	public java.util.Timer daemon;

	public boolean outofbounds = false;
	
//	Category log;

	// public Vector redpenv;
	// double[2] 配列のベクトルを入れるだけ。作ったら各spanelにまかせる。StringBufferはここでは不要。あとで外す。

	public PNode getTarget() {
		return target;
	}

	// public StudentPanel getSpTarget(){
	// return sptarget;
	// }
	public int getX() {
		return (int) camerap.getX();
	}

	public int getY() {
		return (int) camerap.getY();
	}

	// メニューを表示，または非表示にする
	public void showMenu(boolean f) {
		if (f) {
			fmenu = fmenu_orig;
			fmenu.paint();
			if (fmenu != null)
				camera.addChild(fmenu);
			if (frame != null)
				frame.setCursor(handcursor);
		} else {
			if (daemon != null) {
				daemon.cancel();
				daemon = null;
			}
			if (fmenu != null) {
				camera.addChild(fmenu);
				camera.removeChild(fmenu);
			}
			fmenu = null;
			target = null;
			strokecount = 0;
		}
	}

	// メニューが表示中かどうか（直接，カメラの子ノードとして存在するかどうかを調べる）
	public boolean isMenuShown() {
		if (fmenu == null)
			return false;
		return fmenu.isDescendentOf(camera);
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

	public AbstractFlowMenu(JFrame f, PCanvas can, PCamera cam) {
		frame = f;
		canvas = can;
		camera = cam;
		
//		log = Logger.getLogger(AbstractFlowMenu.class.getName());
	}

	public void startDrag_pre(PInputEvent e) {
		target = e.getPickedNode();
		tp = target.getOffset();
		basep = e.getPosition();
//		log.debug(basep);
		camerap = e.getPositionRelativeTo(camera);
	}

	public void startDrag_post(PInputEvent e) {
		// サブクラスで，必要なMyTimerTask_forYourClass を起動し，スケジュールする
	}

	public void startDrag(PInputEvent e) {
		super.startDrag(e);
		// canvas.setCursor(handcursor);
	}

	public void drag(PInputEvent e) {
		super.drag(e);
	}

	public void endDrag(PInputEvent e) {
		super.endDrag(e);
		endDrag_first(e);
		endDrag_mid(e);
		endDrag_end(e);
	}

	public void endDrag_first(PInputEvent e) {
		if (fmenu != null)
			fmenu.endDrag(e);
		if (daemon != null) {
			daemon.cancel();
			daemon = null;
		}
	}

	public void endDrag_mid(PInputEvent e) {
	}

	public void endDrag_end(PInputEvent e) {
		showMenu(false);
		outofbounds = false;
		if (frame != null)
			frame.setCursor(defaultcursor);
	}
}
