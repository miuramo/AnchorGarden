package jaist.css.covis.mi;

import jaist.css.covis.SrcWindow;
import jaist.css.covis.cls.Covis_int;

import javax.swing.JTextField;

public class InputField_int extends JTextField implements InputField {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2251681272731799372L;

	public InputField_int(int n){
		super(String.valueOf(n),5);
		setBackground(Covis_int.defaultColor);
		setFont(SrcWindow.sans22);
		setAlignmentX(3.0f);
		setToolTipText("int");

	}
	String type;
	@Override
	public Object getSelectedItemObject() {
		return new Integer(Integer.valueOf(this.getText()));
	}
	
	public String getSelectedItemString() {
		int val = Integer.parseInt(this.getText());
		return String.valueOf(val);
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public void setType(String s) {
		type = s;
	}

}
