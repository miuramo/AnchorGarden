package jaist.css.covis.fm;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.util.PAffineTransform;

/**
 * 範囲指定ズームのためのアクション
 * 
 * @author miuramo
 *
 */
@SuppressWarnings("serial")
public class ZoomRegionStateAction extends StateAction {
	PPath selection;

	Point2D cp;

	private Stroke[] strokes = null;

	private float strokeNum = 0;

	final static int NUM_STROKES = 10;

	final static int DASH_WIDTH = 15;

	FlowMenu_TMRG ownersv;

	public ZoomRegionStateAction(FlowMenu_TMRG f, String n) {
		super(f, n);
		float[] dash = { DASH_WIDTH, DASH_WIDTH };
		strokes = new Stroke[NUM_STROKES];
		for (int i = 0; i < NUM_STROKES; i++) {
			strokes[i] = new BasicStroke(3, BasicStroke.CAP_BUTT,
					BasicStroke.JOIN_MITER, 1, dash, i);
		}
		ownersv = (FlowMenu_TMRG) f;
	}

	public void drag(PInputEvent e) {
		super.drag(e);
		Point2D cmp = owner.camerap;
		cp = e.getPositionRelativeTo(owner.camera);
		selection.setPathToRectangle((float) cmp.getX(), (float) cmp.getY(),
				(float) (cp.getX() - cmp.getX()), (float) (cp.getY() - cmp
						.getY()));

		float origStrokeNum = strokeNum;
		strokeNum = (strokeNum + 0.5f) % NUM_STROKES; // Increment by partial
														// steps to slow down
														// animation
		if ((int) strokeNum != (int) origStrokeNum) {
			selection.setStroke(strokes[(int) strokeNum]);
		}
	}

	public void endDrag(PInputEvent e) {
		Point2D cmp = owner.camerap;
		cp = e.getPositionRelativeTo(owner.camera);
		removeAllChildren();
		if (cmp.getX() < cp.getX() && cmp.getY() < cp.getY()) {
			// カメラ座標から，グローバル（パネル）座標への変換
			PAffineTransform at = owner.camera.getViewTransform();
			Rectangle2D globalb = selection.getGlobalBounds();
			Rectangle2D layerb = new Rectangle2D.Double();
			at.inverseTransform(globalb, layerb);
			owner.camera.animateViewToCenterBounds(layerb, true, 1000);
		}
	}

	public void paint() {
		Point2D cmp = owner.camerap;
		selection = new PPath();
		selection.setPathToRectangle((float) cmp.getX(), (float) cmp.getY(), 0,
				0);
		selection.setStrokePaint(Color.blue);
		selection.setPaint(null);
		selection.setStroke(strokes[0]);
		addChild(selection);
	}
}
