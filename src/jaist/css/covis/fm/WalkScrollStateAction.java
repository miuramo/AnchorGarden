package jaist.css.covis.fm;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.TimerTask;

import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.nodes.PPath;

/**
 * ウォーク・スクロールのときのアクション
 * 
 * @author miuramo
 *
 */

public @SuppressWarnings("serial")
class WalkScrollStateAction extends StateAction {
	PPath arrow;

	double sumx, sumy;

	Point2D cp;

	java.util.Timer walktask = null;

//	Category log; // Log4J
	
	public WalkScrollStateAction(FlowMenu_TMRG f, String n) {
		super(f, n);
//		log = Logger.getLogger(WalkScrollStateAction.class.getName());
	}

	class WalkTimerTask extends TimerTask {
		public void run() {
//			log.debug("dx: " + sumx + " dy: " + sumy);
//			owner.camera.translateView(-sumx, -sumy);
		}
	}

	public void drag(PInputEvent e) {
		super.drag(e);
		cp = e.getPositionRelativeTo(owner.camera);
		sumx = cp.getX() - owner.camerap.getX();
		sumy = cp.getY() - owner.camerap.getY();
		if (walktask == null) {
			walktask = new java.util.Timer();
			walktask.schedule(new WalkTimerTask(), 100, 100);
		}
	}

	public void endDrag(PInputEvent e) {
		walktask.cancel();
		walktask = null;
		super.endDrag(e);
		removeAllChildren();
	}

	public void paint() {
		Point2D cmp = owner.camerap;
		arrow = PPath.createEllipse((float) cmp.getX(), (float) cmp.getY(),
				10f, 10f);
		arrow.setPaint(Color.blue);
		arrow.setStrokePaint(null);
		addChild(arrow);
	}
}
