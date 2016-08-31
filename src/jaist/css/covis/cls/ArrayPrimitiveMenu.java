package jaist.css.covis.cls;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import jaist.css.covis.util.FramePopup;


public class ArrayPrimitiveMenu extends JPopupMenu implements FramePopup {
	
	private static final long serialVersionUID = 8027248166373847225L;
	Covis_primitive v;
	Covis_Array a;
	JFrame f;

	public ArrayPrimitiveMenu(Covis_primitive _v, Covis_Array _a) {
		this.v = _v;
		this.a = _a;

		JMenuItem menuItem;

		setLightWeightPopupEnabled(false);

		menuItem = new JMenuItem("cancel");
		add(menuItem);

		addSeparator();
		menuItem = new JMenuItem("edit value");
		add(menuItem);
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String input;
				input = JOptionPane.showInputDialog(f, "Input Value", v.getValue());
				if (input == null) return;
				if (!v.setValue(input)){
					JOptionPane.showMessageDialog(f,"Value is not accepted.","Error",JOptionPane.WARNING_MESSAGE);
					return;
				}
				v.buffer.putHistoryEditValueArray(v,a); //変更したらソースコードに追加
				Informer.playSound("Pop.wav");
			}
		});
	}

	public void showWithFrame(Component c, int x, int y, JFrame _f) {
		f = _f;
		show(c, x, y);
	}

}
