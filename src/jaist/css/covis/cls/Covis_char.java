package jaist.css.covis.cls;

import jaist.css.covis.CoVisBuffer;

import java.awt.Color;

import edu.umd.cs.piccolo.nodes.PText;

public class Covis_char extends Covis_primitive {
	private static final long serialVersionUID = 8131819427660903628L;
	public static Color defaultColor = new Color(248,184,120);
	
	public Covis_char(CoVisBuffer buf, boolean isAuto){
		super(buf, isAuto);
		color = defaultColor;
		setPaint(color);
		setStroke(basicStroke);
	}
	public Covis_char(Color c, CoVisBuffer buf, boolean isAuto){
		super(c, buf, isAuto);
	}
	public void init(boolean isAuto){
		setPathToRectangle(0, 0, 40, 50);
		setValue("a");
//		value = "'a'";
//		valueText = new PText(value);
//		valueText.setScale(2);
//		addChild(valueText);
//		valueText.setGreekThreshold(1.0d);
//		valueTextOffset = -6f;
//		justify();
	}
	public boolean setValue(String s){
		if (s == null){
			return false;
		}
		if (s.equals("")){
			s = "\\0";
		} else {
			s = s.trim().substring(0,1);
		}
		value = "'"+s+"'";
		if (valueText == null){
			valueText = new PText(value);
			valueText.setScale(2);
			addChild(valueText);
			valueText.setGreekThreshold(1.0d);
		}
		valueText.setText(value);
		valueTextOffset = -6f;
		justify();
		return true;
	}
	public String getValue(){
		return value.substring(1,value.length()-1);
	}
//	public void edit(JFrame f, Variable v){
//		super.edit(f,v);
//	}
	
	public Color getClassColor(){
		return defaultColor;
	}
	
	public static int objCount = 0;
	public static int objAryCount = 0;
	public String getNextVarName(boolean isAry){
		StringBuffer sb = new StringBuffer(getClsName().substring(0,2));
		if (isAry) {
			objAryCount++;
			sb.append("Ary");
//			sb.append("_");
			sb.append(objAryCount);
		} else {
			objCount++;
			sb.append(objCount);
		}
		return sb.toString();
	}
	
//	@Override
//	public void clear_objCount() {
//		objCount = 0;
//		objAryCount = 0;
//		System.out.println("clear count "+getClsName());
//	}
}
