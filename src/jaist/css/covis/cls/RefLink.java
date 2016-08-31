package jaist.css.covis.cls;

import jaist.css.covis.fm.FlowMenu_TMRG;

import java.awt.Color;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.util.PDimension;

/**
 * アンカータブと変数などとをつなぐ「線」
 * @author miuramo
 */
public class RefLink extends PPath implements LinkMovable, ClickHandler {
	private static final long serialVersionUID = 6035096413162581382L;

	PNode src;
	PNode knob;
	Covis_Object dest = null;
	Anchor anchor;
	public RefLink(PNode t1, PNode t2, Anchor a){
		src = t1;
		knob = t2;
		anchor = a;
		setStroke(ClassStamp.roundrectStroke);
		setStrokePaint(Color.gray);
		
		addAttribute("clickHandler", this);
	}
	
	public void update(){
		Point2D srcP = src.getGlobalTranslation();
		Point2D knobP = knob.getGlobalTranslation();
		float dx = (float)(knobP.getX() - srcP.getX() - 10);
		float dy = (float)(knobP.getY() - srcP.getY() - 10);
		setPathTo(new Line2D.Float(-10,-10, dx, dy));
	}
	
	public PDimension diff(){
		PNode target;
		Point2D targetP;
		if (dest != null) target = dest; else target = src;
		targetP = target.getGlobalTranslation();
		Point2D destP = knob.getGlobalTranslation();
		double dx = destP.getX() - targetP.getX();
		double dy = destP.getY() - targetP.getY();
		if (dest != null) {
			dx-=10; dy-=10 + dest.attachPointOffset(anchor);
			return new PDimension(-dx,-dy);
 		} else {
 			return new PDimension(-dx*3/5,-dy*3/5);
 		}		
	}

	public void clicked(PInputEvent e, FlowMenu_TMRG fmenu) {
//		System.out.println("reflink click");
		anchor.toFront(System.currentTimeMillis());
	}

}
