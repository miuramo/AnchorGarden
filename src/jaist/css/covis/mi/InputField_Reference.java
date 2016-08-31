package jaist.css.covis.mi;

import jaist.css.covis.SrcWindow;

import java.util.HashMap;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

public class InputField_Reference extends JComboBox implements InputField {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5269150644561511322L;
	HashMap<String, Object> hashsrc;
	
	public InputField_Reference(HashMap<String, Object> hash){
		super();
//		String[] ary = new String[hash.size()];
		DefaultComboBoxModel dcbm = new DefaultComboBoxModel();
		for(Object o: hash.keySet().toArray()){
			String s = (String)o;
			dcbm.addElement(s);
		}
		setModel(dcbm);
		hashsrc = hash;
		setFont(SrcWindow.sans22);
		setAlignmentX(3.0f);
	}
	String type;
	@Override
	public Object getSelectedItemObject() {
		return hashsrc.get(getSelectedItem());
	}
	public String getSelectedItemString() {
		return getSelectedItem().toString();
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
