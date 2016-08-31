package jaist.css.covis.cls;

import jaist.css.covis.CoVisBuffer;

import java.awt.Color;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PText;

public class Covis_int extends Covis_primitive {
	private static final long serialVersionUID = 8131819427660903628L;
	public static Color defaultColor = new Color(140,200,255);
	
	public Covis_int(CoVisBuffer buf, boolean isAuto){
		super(buf, isAuto);
		color = defaultColor;
		setPaint(color);
		setStroke(basicStroke);
	}
	public Covis_int(Color c, CoVisBuffer buf, boolean isAuto){
		super(c, buf, isAuto);
	}
	public void init(boolean isAuto){
		setPathToRectangle(0, 0, 40, 50);
		setValue("0");
	}
	public boolean setValue(String s){
		try{
			Integer.parseInt(s);
		}catch(NumberFormatException ex){
			return false;
		}
		value = s.trim();
		
		if (valueText == null){
			valueText = new PText();
			valueText.setScale(2);
			addChild(valueText);
			valueText.setGreekThreshold(1.0d);			
		}
		valueText.setText(value);
		valueTextOffset = -6f;
		justify();
		return true;
	}
	
	public Color getClassColor(){
		return defaultColor;
	}
	
	public static int objCount = 0;
	public static int objAryCount = 0;
	public String getNextVarName(boolean isAry){
		StringBuffer sb = new StringBuffer(getClsName());
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
	
	public PNode getToolTipNode(){
		PNode temp = super.getToolTipNode();
		if (temp != null) temp.setPaint(defaultColor);
		return temp;
	}
	
//	@Override
//	public void clear_objCount() {
//		objCount = 0;
//		objAryCount = 0;
//		System.out.println("clear count "+getClsName());
//	}
}
