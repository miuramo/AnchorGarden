package jaist.css.covis.fm;

import java.awt.Font;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;

/**
 * àÍî ìIÇ»ÅuèÛë‘Åv
 * 
 * @author miuramo
 *
 */
@SuppressWarnings("serial")
public class DefaultState extends State {
	public DefaultState(AbstractFlowMenu f, String n) {
		super(f, n);
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
		Ellipse2D.Double ed2 = new Ellipse2D.Double(-SIZE / 2.0 - SIZE / 16.0,
				-SIZE / 2.0 - SIZE / 16.0, SIZE + SIZE / 8.0, SIZE + SIZE / 8.0);
		// Arc2D.Double ac = new
		// Arc2D.Double(-SIZE,-SIZE,SIZE*2.0,SIZE*2.0,0,-44, Arc2D.PIE);
		Arc2D.Double ac = new Arc2D.Double(-SIZE / 4.0 * 5.0,
				-SIZE / 4.0 * 5.0, SIZE / 2.0 * 5.0, SIZE / 2.0 * 5.0, 0,
				-43.5, Arc2D.PIE);
		@SuppressWarnings("unused")
		Rectangle2D.Double rd = new Rectangle2D.Double(0, 0, SIZE, SIZE);

		Area a = new Area();
		a.add(new Area(ac));
		a.subtract(new Area(ed2));
		AffineTransform at = new AffineTransform();

		int mx = owner.getX();
		int my = owner.getY();
		int sx = 0, sy = 0;

		Font f = (new PText()).getFont();
		Font nf = new Font(f.getFontName(), Font.BOLD, (int) (14 * SIZE / 80.0));
		// g2.setFont(nf);

		for (int i = 0; i < 8; i++) {

			at.setToTranslation(mx, my);
			at.rotate(Math.toRadians(-112 - i * 45));
			Area b = a.createTransformedArea(at);

			itempath[i + 2] = new PPath(b);
			itempath[i + 2].setStroke(null);
			if (nextstates[i + 2] == null) {
				itempath[i + 2].setPaint(no);
			} else {
				if (nextstates[i + 2].isEnabled()) {
					itempath[i + 2].setPaint(fill);
				} else {
					itempath[i + 2].setPaint(no);
				}
			}
			// p.setStroke(new
			// BasicStroke(2,BasicStroke.CAP_ROUND,BasicStroke.CAP_ROUND));
			// p.setStrokePaint(Color.gray);

			// if(!isEnable(i+2)){
			// p.setPaint(no);//continue;
			// // p.setStrokePaint(no);
			// }
			// if(state == i+2){
			// p.setPaint(choose);
			// // p.setStrokePaint(choose);
			// }
			// else{
			// p.setPaint(fill);
			// // p.setStrokePaint(fill);
			// }
			addChild(itempath[i + 2]);
		}
		for (int i = 0; i < 8; i++) {
			switch (i) {
			case 0:
				sx = (int) (Math.cos(105.0 * RD) * (double) SIZE + mx);
				sy = (int) (-1.3 * Math.sin(135.0 * RD) * (double) SIZE + my);
				break;
			case 1:
				sx = (int) (Math.cos(150.0 * RD) * (double) SIZE + mx);
				sy = (int) (-1.2 * Math.sin(150.0 * RD) * (double) SIZE + my);
				break;
			// case 2: sx = (int)(Math.cos(180.0*RD)*(double)SIZE+mx);
			case 2:
				sx = (int) (1.1 * Math.cos(180.0 * RD) * (double) SIZE + mx);
				sy = (int) (-Math.sin(180.0 * RD) * (double) SIZE + my);
				break;
			case 3:
				sx = (int) (Math.cos(210.0 * RD) * (double) SIZE + mx);
				sy = (int) (-Math.sin(220.0 * RD) * (double) SIZE + my);
				break;
			case 4:
				sx = (int) (Math.cos(255.0 * RD) * (double) SIZE + mx);
				sy = (int) (-Math.sin(235.0 * RD) * (double) SIZE + my);
				break;
			case 5:
				sx = (int) (1.4 * Math.cos(290.0 * RD) * (double) SIZE + mx);
				sy = (int) (-Math.sin(320.0 * RD) * (double) SIZE + my);
				break;
			case 6:
				sx = (int) (1.1 * Math.cos(55.0 * RD) * (double) SIZE + mx);
				sy = (int) (-Math.sin(0.0 * RD) * (double) SIZE + my);
				break;
			case 7:
				sx = (int) (1.5 * Math.cos(75 * RD) * (double) SIZE + mx);
				sy = (int) (-1.3 * Math.sin(30.0 * RD) * (double) SIZE + my);
				break;
			}
			// g2.setColor(strColor);
			String sst = getString(i + 2);
			if (sst != null) {
				PText pt = new PText(sst);
				pt.offset(sx, sy);
				pt.setFont(nf);
				// System.out.println(i + " "+getString(i+2)+" "+sx+" "+sy);
				addChild(pt);
			}
		}
		PText pt2 = new PText(getName());
		pt2.offset((mx - SIZE / 2.0 + 20), my);
		pt2.setFont(nf);
		addChild(pt2);
	}
}
