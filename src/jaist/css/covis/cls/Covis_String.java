package jaist.css.covis.cls;

import jaist.css.covis.CoVisBuffer;

import java.awt.Color;

import javax.swing.JOptionPane;

import edu.umd.cs.piccolo.nodes.PText;

public class Covis_String extends Covis_Object {
	private static final long serialVersionUID = 8131819427660903628L;
	public static Color defaultColor = new Color(255,232,96);
	Covis_primitive elementObj;

	PText valueText;
	public Covis_String(CoVisBuffer buf, boolean isAuto){
		super(buf, isAuto);
		color = defaultColor;
		setPaint(color);
		setStroke(basicStroke);
	}
	public Covis_String(Color c, CoVisBuffer buf, boolean isAuto){
		super(c, buf, isAuto);
	}
	public void init(boolean isAuto) throws NullPointerException {
		setPathToRectangle(0, 0, 100, 50);
		if (isAuto){
			setValues("Moji");
		} else {
			String val = JOptionPane.showInputDialog(buffer.getWindow().frame, "Input String.", "Input String", JOptionPane.QUESTION_MESSAGE);
			if (val == null) {
				val = "Moji";
				throw new NullPointerException();
			}
			setValues(val);
		}
	}
	public void setValues(String s){
		if (valueText == null) valueText = new PText();
		valueText.setText('"'+s+'"');
		valueText.setScale(2.2);
		valueText.addAttribute("moveTarget", this);
		valueText.setOffset(4,10);
		addChild(valueText);
		setPathToRectangle(0, 0, (float)(8+valueText.getWidth()*valueText.getScale()), (float)(20+valueText.getHeight()*valueText.getScale()));

	}
	public Covis_Object Covis_clone(boolean isAuto){
		return new Covis_String(buffer, isAuto);
	}
	public Color getClassColor(){
		return defaultColor;
	}
	

	public static int objCount = 0;
	public static int objAryCount = 0;
	public String getNextVarName(boolean isAry){
		StringBuffer sb = new StringBuffer(getClsName().toLowerCase().substring(0,3));
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
	// overwrite
	public String getConstructorInfo() {
//		return "new "+getClsName()+"("+valueText.getText()+"); // or, just "+valueText.getText();
		return "new "+getClsName()+"("+valueText.getText()+");";
	}
	
//	public Covis_String covis_toString(){
//		Covis_String cs = new Covis_String(buffer,true);
//		cs.setValues(toString());
//		buffer.putHistoryNew("new", cs, false);
//		return cs;
//	}
	public Covis_String covis_toString(){
//		Covis_String cs = new Covis_String(buffer,true);
//		cs.setValues(toString());
//		buffer.putHistoryNew("new", cs, false);
//		return cs;
		return this;
	}
	public String toString(){
		return valueText.getText().substring(1,valueText.getText().length()-1);
	}
//		return ;
	public boolean covis_equals(Covis_String arg){		
		return this.covis_toString().equals(arg.covis_toString());
	}
	public boolean covis_eqeq_isSame(Covis_String arg){
		return (this==arg);
	}
	public void covis_setText(String s){
		setValues(s);
	}
	public void covis_append(String s){
		setValues(toString()+s);
	}
//	@Override
//	public void clear_objCount() {
//		objCount = 0;
//		objAryCount = 0;
//		System.out.println("clear count "+getClsName());
//	}
}
