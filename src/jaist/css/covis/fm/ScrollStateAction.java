package jaist.css.covis.fm;

import java.awt.geom.Point2D;

import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.util.PDimension;

/**
 * ‰æ–Ê‚Ìƒpƒjƒ“ƒO
 * 
 * @author miuramo
 *
 */
@SuppressWarnings("serial")
public class ScrollStateAction extends StateAction {
	public ScrollStateAction(FlowMenu_TMRG f, String n) {
		super(f, n);
	}

	public void drag(PInputEvent e) {
		super.drag(e);
		Point2D cp = e.getPosition();
		if (owner.camera.getViewBounds().contains(cp)) {
			PDimension d = e.getDelta();
			owner.camera.translateView(d.getWidth(), d.getHeight());
			// owner.camera.setViewOffset(cp.getX()-owner.camerap.getX(),cp.getY()-owner.camerap.getY());
			// System.out.println("dx: "+(cp.getX())+" dy:
			// "+(cp.getY()-owner.camerap.getY()));
		}
	}
}