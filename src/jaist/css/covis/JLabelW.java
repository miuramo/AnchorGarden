package jaist.css.covis;

import java.awt.Font;

import javax.swing.JLabel;

public class JLabelW extends JLabel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4720296888345083391L;
	public static Font sans14 = new Font("sansserif", Font.BOLD, 14);
	public static Font sans22 = new Font("sansserif", Font.BOLD, 22);
	public static Font sans30 = new Font("sansserif", Font.BOLD, 30);

	public JLabelW(String s){
		super(s,JLabel.CENTER);
		setFont(sans22);
	}

}
