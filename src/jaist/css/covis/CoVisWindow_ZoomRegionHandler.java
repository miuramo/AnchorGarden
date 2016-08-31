package jaist.css.covis;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import edu.umd.cs.piccolo.PCamera;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PDragSequenceEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.util.PAffineTransform;
import edu.umd.cs.piccolo.util.PBounds;
import jaist.css.covis.util.BUtil;
import jaist.css.covis.util.FramePopup;

/**
 * ATNWindowで右ドラッグしながら青色のズーム範囲矩形を表示し，ズームする機能を実現するクラス
 * @author miuramo
 *
 */
public class CoVisWindow_ZoomRegionHandler extends PDragSequenceEventHandler {
	AnchorGarden viewer;

	PPath selection;

	PNode target;
	
	private Stroke[] strokes = null;

	private float strokeNum = 0;
	
	private int dragCount = 0;

	final static int NUM_STROKES = 10;

	final static int DASH_WIDTH = 15;

	Point2D cmp; // ドラッグ開始座標

	Point2D cp; //ドラッグ中の座標
	
	Point2D max_ext_cp; // 左上方向（通常とは逆方向）一番遠くにドラッグしたときの座標
//	double dist_max_ext_cp; //そのときの距離
	PBounds initialBounds; //ドラッグ開始時の視野

	public CoVisWindow_ZoomRegionHandler(AnchorGarden _viewer) {
		this.viewer = _viewer;

		float[] dash = { DASH_WIDTH, DASH_WIDTH };
		strokes = new Stroke[NUM_STROKES];
		for (int i = 0; i < NUM_STROKES; i++) {
			strokes[i] = new BasicStroke(2, BasicStroke.CAP_BUTT,
					BasicStroke.JOIN_MITER, 1, dash, i);
		}
	}

	public void startDrag(PInputEvent e) {
		super.startDrag(e);
		target = e.getPickedNode();
		cmp = e.getPositionRelativeTo(viewer.getCanvas().getCamera());
		max_ext_cp = cmp; // 初期化
		selection = new PPath();
		selection.setPathToRectangle((float) cmp.getX(), (float) cmp.getY(), 0,
				0);
		selection.setStrokePaint(Color.blue);//青色
		selection.setPaint(null);
		selection.setStroke(strokes[0]);
		viewer.getCanvas().getCamera().addChild(selection);
		initialBounds = viewer.getCanvas().getCamera().getViewBounds();
		dragCount = 0;
	}

	public void drag(PInputEvent e) {
		super.drag(e);
		dragCount++;
		cp = e.getPositionRelativeTo(viewer.getCanvas().getCamera());
		float cp_cmp_x = (float)(cp.getX() - cmp.getX());
		float cp_cmp_y = (float)(cp.getY() - cmp.getY());
		
		selection.setPathToRectangle((float) cmp.getX(), (float) cmp.getY(),
				cp_cmp_x, cp_cmp_y);

		// 右下にドラッグしているときの，青い点線矩形の点線パターンを変化させる
		float origStrokeNum = strokeNum;
		strokeNum = (strokeNum + 0.3f) % NUM_STROKES; 
		if ((int) strokeNum != (int) origStrokeNum) {
			selection.setStroke(strokes[(int) strokeNum]);
		}
		
//		 左上方向（通常とは逆方向）一番遠くにドラッグしたときの処理
		if (cp_cmp_x < 0 || cp_cmp_y < 0){			
			PCamera pc = viewer.getCanvas().getCamera();
			float f;
			if (cp_cmp_x < 0) f = Math.abs((float)(viewer.getCanvas().getWidth()/(viewer.getCanvas().getWidth() - cp.distance(cmp))));
			else f = Math.abs((float)((viewer.getCanvas().getWidth() - cp.distance(cmp))/viewer.getCanvas().getWidth()));
//			System.out.println("f "+f);
			PBounds pb = BUtil.zoomBounds(initialBounds, f);
			pc.animateViewToCenterBounds(pb, true, 0);
		}
	}

	public void endDrag(PInputEvent e) {
		super.endDrag(e);
		cp = e.getPositionRelativeTo(viewer.getCanvas().getCamera());
		if (dragCount < 6 && target.getAttribute("popupMenu") != null){
			FramePopup popup = (FramePopup) target.getAttribute("popupMenu");
			popup.showWithFrame(viewer.getCanvas(), (int) cp.getX(), (int) cp.getY(), viewer.frame);
		}
		selection.removeFromParent();
		if (cmp.getX() < cp.getX() && cmp.getY() < cp.getY()) {
			// カメラ座標から，グローバル（パネル）座標への変換
			PAffineTransform at = viewer.getCanvas().getCamera().getViewTransform();
			Rectangle2D globalb = selection.getGlobalBounds();
			Rectangle2D layerb = new Rectangle2D.Double();
			at.inverseTransform(globalb, layerb);
			viewer.getCanvas().getCamera().animateViewToCenterBounds(layerb, true, 1000);
		}
	}
}
