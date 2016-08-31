package jaist.css.covis.cls;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ArrayInitializeWindow extends JDialog {

	private static final long serialVersionUID = -7025036939612024059L;
	public JTextField[] jtfary;
	public String valtype;
	public ArrayInitializeWindow(JFrame parent, int size, String type){
		super(parent, "Initialize Array", true);
		valtype = type;
		JPanel jp = new JPanel(new GridLayout(2,size));
		jtfary = new JTextField[size];
		for(int i=0;i<size;i++){
			jp.add(new JLabel("  ["+i+"]"));
		}
		for(int i=0;i<size;i++){
			jtfary[i] = new JTextField("0", 2);
			jp.add(jtfary[i]);
		}
		getContentPane().add(new JLabel("Input "+type+ "values."), BorderLayout.NORTH);
		getContentPane().add(jp, BorderLayout.CENTER);
		
		JPanel jp2 = new JPanel();
		JButton random = new JButton("Random");
		random.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				setRandomValue(valtype);
			}
		});
		jp2.add(random);
		
		JButton ok = new JButton("Make Array");
		ok.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				setVisible(false);
			}
		});
		jp2.add(ok);
		
		getContentPane().add(jp2, BorderLayout.SOUTH);
		pack();
		
		int parentwidth = parent.getWidth();
		int parentheight = parent.getHeight();
		int childwidth = getWidth();
		int childheight = getHeight();
		int locx = parent.getLocation().x + (parentwidth - childwidth)/2;
		int locy = parent.getLocation().y + (parentheight - childheight)/2;
		setLocation(locx,locy);
//		setVisible(true);
	}
	public void setRandomValue(String type){
		if (type.equals("int")){
			Random rand = new Random();
			for(int i=0;i<jtfary.length;i++){
				jtfary[i].setText(String.valueOf(rand.nextInt(19)-9));
			}
		}
	}
	public static String getCSVString(JFrame parent, int size, String type){
		ArrayInitializeWindow aiw = new ArrayInitializeWindow(parent, size, type);
		aiw.setVisible(true);
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<size;i++){
			if (aiw.jtfary[i].getText().equals("") ){
				if (type.equals("int")) aiw.jtfary[i].setText("0");
			}
			sb.append(aiw.jtfary[i].getText()+",");
		}
		return sb.toString().substring(0,sb.length()-1);
	}
	public static void main(String[] s){
		String ss = getCSVString(null, 10, "int");
		System.out.println(ss);
	}
}
