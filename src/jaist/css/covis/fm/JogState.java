package jaist.css.covis.fm;

import jaist.css.covis.AnchorGarden;

import java.awt.Font;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;

/**
 * ジョグダイアル状態
 * 
 * @author miuramo
 *
 */
public @SuppressWarnings("serial")
class JogState extends State {
	protected int width = 10;

	protected int value = 0;

	protected static double[] limit = { SIZE / 4.0, SIZE / 2.0,
			SIZE / 4.0 * 5.0, SIZE / 4.0 * 5.0 };

	// 無保証のフィールド
	AnchorGarden sview;

	public JogState(AbstractFlowMenu f, String n) {
		super(f, n);

		// 無保証のフィールドをセット
		sview = ((FlowMenu_TMRG) f).window;
	}

	public String getValue() {
		return String.valueOf(value);
	}

	public void updateMenu(int index) {
		@SuppressWarnings("unused")
		int temp = old_index;
		int diff;
		if (index != old_index) {
			if (index != 1)
				itempath[index].setPaint(choose);
			if (old_index != 1)
				itempath[old_index].setPaint(fill);
			if (index != 1 && old_index != 1) {
				diff = old_index - index;
				if (Math.abs(diff) > 14) { // 2->17 or 17->2
					diff = -diff / 15;
				}
				sview.zoomPane(diff);
			}
		}
		old_index = index;
	}

	public int getPlace(int x, int y) {
		int xx, yy, id;
		int rr;
		int mx = owner.getX();
		int my = owner.getY();
		xx = x - mx;
		yy = my - y;
		distance = rr = (int) (Math.sqrt(xx * xx + yy * yy));
		if (rr < limit[1]) { // メニューの中心
			id = 1;
		} else { // メニューの外側
			double r = Math.atan((double) yy / (double) xx) / RD;
			if (xx < 0) {
				r += 180.0;
			}
			if (r < 67.5) {
				r += 360.0;
			}
			id = (int) ((r - 67.5) / 22.5) + 2;
		}
		return id;
	}

	public void drag(PInputEvent e) {
		super.drag(e);
		Point2D cp = e.getPositionRelativeTo(owner.camera);
		index = getPlace((int) cp.getX(), (int) cp.getY());
		updateMenu(index);
		// System.out.println("x: "+cp.getX()+" y: "+cp.getY() +" st "+index);
	}

	public void paint() {
		removeAllChildren();
		int mx = owner.getX();
		int my = owner.getY();
		// int sx = 0, sy = 0;

		// for(int k=0; k<3; k++){
		for (int k = 1; k < 2; k++) {
			Ellipse2D.Double ed2 = new Ellipse2D.Double(-limit[k] - 1,
					-limit[k] - 1, limit[k] * 2 + 2, limit[k] * 2 + 2);
			Arc2D.Double ac = new Arc2D.Double(-limit[k + 1], -limit[k + 1],
					limit[k + 1] * 2, limit[k + 1] * 2, 0, -21, Arc2D.PIE);
			// ^^^
			Area a = new Area();
			a.add(new Area(ac));
			a.subtract(new Area(ed2));
			AffineTransform at = new AffineTransform();

			for (int i = 0; i < 16; i++) {
				// g2.setStroke(new
				// BasicStroke(2,BasicStroke.CAP_ROUND,BasicStroke.CAP_ROUND));

				at.setToTranslation(mx, my);
				at.rotate(Math.toRadians(-89.5 - i * 22.5));
				Area b = a.createTransformedArea(at);

				PPath bpath = new PPath(b);
				// if(state == i+2 && limit[k] < distance && (distance <
				// limit[k+1] || k ==2)){
				// bpath.setPaint(choose);
				// }
				// else{
				bpath.setPaint(fill);
				bpath.setStroke(null);
				addChild(bpath);
				itempath[i + 2] = bpath;
			}
		}
		Font f = (new PText()).getFont();
		Font nf = new Font(f.getFontName(), Font.BOLD, (int) (16 * SIZE / 80.0));
		// FontMetrics fm = g2.getFontMetrics(nf);
		String s = "<<++  -->>";
		char[] c = new char[s.length()];
		s.getChars(0, s.length(), c, 0);
		PText pt = new PText(s);
		pt.setFont(nf);
		// topleftnumText.offset(mx - fm.charsWidth(c, 0, c.length)/2,
		// (int)(my+limit[1]+10));
		pt.offset(mx - 45, (int) (my + limit[1] + 10));

		s = getValue();
		c = new char[s.length()];
		s.getChars(0, s.length(), c, 0);
		PText pt2 = new PText(s);
		pt2.setFont(nf);
		pt2.offset(mx, my);
		// pt2.offset(mx-fm.charsWidth(c, 0, c.length)/2, my);
		addChild(pt);
		addChild(pt2);
	}

	public void setWidth(int rr) {
		if (rr < limit[1]) {
			width = 1;
		} else if (rr < limit[2]) {
			width = 10;
		} else {
			// width=50;
			width = 10;
		}
	}
}
