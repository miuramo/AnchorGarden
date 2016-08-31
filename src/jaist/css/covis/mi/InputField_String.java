package jaist.css.covis.mi;

import jaist.css.covis.SrcWindow;
import jaist.css.covis.cls.Covis_String;

import javax.swing.JTextField;

public class InputField_String extends JTextField implements InputField {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2251681272731799372L;

	public InputField_String(String orig){
		super(orig, 5);
		setBackground(Covis_String.defaultColor);
		setFont(SrcWindow.sans22);
		setAlignmentX(3.0f);
		setToolTipText("String");

	}
	String type;
	@Override
	public Object getSelectedItemObject() {
		return new String(this.getText());
	}
	
	public String getSelectedItemString() {
		return new String("\""+this.getText()+"\"");
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
