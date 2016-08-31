package jaist.css.covis.cls;

import jaist.css.covis.CoVisBuffer;

import java.awt.Color;

public class Covis_Rect extends Covis_Object {
	private static final long serialVersionUID = 8131819427660903628L;
	public static Color defaultColor = new Color(178,242,188);
	public Covis_Rect(CoVisBuffer buf, boolean isAuto){
		super(buf, isAuto);
		color = defaultColor;
		setPaint(color);
		setStroke(basicStroke);
	}
	public Covis_Rect(Color c, CoVisBuffer buf, boolean isAuto){
		super(c, buf, isAuto);
	}
	public void init(boolean isAuto){
		setPathToRectangle(0, 0, 80, 50);
	}
	public Covis_Object Covis_clone(boolean isAuto){
		return new Covis_Rect(buffer, isAuto);
	}
	public Color getClassColor(){
		return defaultColor;
	}
	
	public static int objCount = 0;
	public static int objAryCount = 0;
	public String getNextVarName(boolean isAry){
		StringBuffer sb = new StringBuffer(getClsName().toLowerCase().substring(0,4));
		if (isAry) {
			objAryCount++;
			sb.append("Ary");
			sb.append(objAryCount);
		} else {
			objCount++;
			sb.append(objCount);
		}
		return sb.toString();
	}
	@Override
	public void clear_objCount() {
		objCount = 0;
		objAryCount = 0;
		System.out.println("clear count "+getClsName());
	}
}
