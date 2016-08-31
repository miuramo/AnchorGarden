package jaist.css.covis.cls;

import java.awt.Color;
import java.awt.Font;
import java.util.List;
import java.util.TreeMap;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;
import edu.umd.cs.piccolo.util.PDimension;

public class Field extends PPath implements Layoutable , Move{
	private static final long serialVersionUID = 3524513728998576206L;

	public String typeName; // Variable, Object, Class
	public Color color;
	PText caption;
	public static Font bold = new Font("sansserif", Font.BOLD, 12);
	public static float top = 40;
	public Field(String _typeName, Color c){
		color = c;
		typeName = _typeName;
		setPathToRectangle(0, 0, 200, 100);
		setPaint(color);
		setStrokePaint(null);
//		setStroke(new BasicStroke(1));
		scale(1);
//		setTransparency(0.4f);
		
		this.addAttribute("moveTarget", this);
		this.addAttribute("info", "Field "+toString());
		this.addAttribute("selectable", this);
		
		caption = new PText(typeName);
		caption.scale(3);
		caption.setFont(bold);
		caption.setOffset(10,0);
		caption.addAttribute("moveTarget", this);
		caption.addAttribute("dragLayout", this);
		addChild(caption);
	}
	
	public void layout(int dur){
		layoutExceptOne(null, dur);
	}

	public void layoutExceptOne(PNode operationNode, int dur) {
		List<PNode> col = getChildrenReference();
		TreeMap<Double,PNode> map = new TreeMap<Double, PNode>();
		for(PNode p: col) {
			double y = p.getYOffset();
			while(map.get(y)!=null) y+=0.001;
			if (p != caption) map.put(y, p);
		}
		
		double offsetx = 10;
		double endx = 10;
		double offsety = 10;
		double endy = 10;
		double maxx = 0, maxy = 0; 
		for(PNode p : map.values()){
//			p.setOffset(offsetx, offsety);
			double px = p.getFullBounds().width;
			double py = p.getFullBounds().height;
			if (maxx < offsetx + endx + px) maxx = offsetx + endx + px;
			if (maxy < offsety + endy + py) maxy = offsety + endy + py;
			if (operationNode != p) p.animateToPositionScaleRotation(offsetx, offsety+top, 1, 0, dur);
			offsety += py;
			offsety += 1;
		}
		animateToBounds(0, 0, maxx, maxy+top, dur);
	}
	
	public void layoutByToString(int dur){
		List<PNode> col = getChildrenReference();
		TreeMap<String,PNode> map = new TreeMap<String, PNode>();
		for(PNode p: col) {			
			if (p != caption) map.put(p.toString(), p);
		}
		
		double offsetx = 10;
		double endx = 10;
		double offsety = 10;
		double endy = 10;
		double maxx = 0, maxy = 0; 
		for(PNode p : map.values()){
//			System.out.println(p.toString());
			p.setOffset(offsetx, offsety+top);
			double px = p.getFullBounds().width;
			double py = p.getFullBounds().height;
			if (maxx < offsetx + endx + px) maxx = offsetx + endx + px;
			if (maxy < offsety + endy + py) maxy = offsety + endy + py;
			offsety += py;
			offsety += 1;
		}
		animateToBounds(0, 0, maxx, maxy+top, dur);
	}
	
	public void move(PDimension d){
		translate(d.getWidth(), d.getHeight()); //—š—ð‚ÉŠÖŒW‚È‚¢“®ì
	}
}
