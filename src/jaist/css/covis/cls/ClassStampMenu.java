package jaist.css.covis.cls;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import jaist.css.covis.CoVisBuffer;
import jaist.css.covis.Selectable;
import jaist.css.covis.util.FramePopup;


public class ClassStampMenu extends JPopupMenu implements FramePopup, ActionListener{
	ClassStamp cstamp;
	JFrame frame;
	private static final long serialVersionUID = -3662668322301800275L;

	CoVisBuffer buffer;
	public ClassStampMenu(ClassStamp _f, CoVisBuffer buf) {
		this.cstamp = _f;

		buffer = buf;
		if (buffer==null) JOptionPane.showMessageDialog(null, "buffer is null");

		JMenuItem menuItem;

		setLightWeightPopupEnabled(false);

		menuItem = new JMenuItem("cancel");
		add(menuItem);

		addSeparator();
		if (!cstamp.isSelected()){
			menuItem = new JMenuItem("select");
			add(menuItem);
			menuItem.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					ArrayList<Selectable> sel = new ArrayList<Selectable>();
					if (ClassStamp.selectedType != null){
						ClassStamp.selectedType.setSelected(false, sel);
					}
					cstamp.toggleSelected(sel);
				}
			});

			addSeparator();
		}
		menuItem = new JMenuItem("inspect");
		add(menuItem);
		menuItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				Class<?> clazz = cstamp.cv_type.getClass();
				ClassInspector ci = new ClassInspector(clazz);
				ci.inspect_print();
			}
		});

		addSeparator();

		if (cstamp.cv_type instanceof Covis_primitive){
			menuItem = new JMenuItem("create variable (automatic)");
			add(menuItem);
			menuItem.addActionListener(this);

			menuItem = new JMenuItem("create variable (manual)");
			add(menuItem);
			menuItem.addActionListener(this);
		} else if (cstamp.cv_type instanceof Covis_Array){
			menuItem = new JMenuItem("create variable (automatic)");
			add(menuItem);
			menuItem.addActionListener(this);

			menuItem = new JMenuItem("create variable (manual)");
			add(menuItem);
			menuItem.addActionListener(this);

			menuItem = new JMenuItem("create array (automatic): new "+cstamp.cv_type.getClsName().substring(0,cstamp.cv_type.getClsName().length()-2)+"[ 4 ]");
			add(menuItem);
			menuItem.addActionListener(this);

			menuItem = new JMenuItem("create array (manual): new "+cstamp.cv_type.getClsName().substring(0,cstamp.cv_type.getClsName().length()-2)+"[ N ]");
			add(menuItem);
			menuItem.addActionListener(this);

		} else if (cstamp.cv_type instanceof Covis_Object){
			menuItem = new JMenuItem("create variable (automatic)");
			add(menuItem);
			menuItem.addActionListener(this);

			menuItem = new JMenuItem("create variable (manual)");
			add(menuItem);
			menuItem.addActionListener(this);

			menuItem = new JMenuItem("create object (automatic): new "+cstamp.cv_type.getClsName()+"()");
			add(menuItem);
			menuItem.addActionListener(this);

			menuItem = new JMenuItem("create object (manual): new "+cstamp.cv_type.getClsName()+"("+cstamp.cv_type.getConstructorArgs()+")");
			add(menuItem);
			menuItem.addActionListener(this);
		}
		//		addSeparator();
		//
		//		menuItem = new JMenuItem("レイアウト");
		//		add(menuItem);
		//		menuItem.addActionListener(new ActionListener() {
		//			public void actionPerformed(ActionEvent e) {
		//				f.layout(500);
		//			}
		//		});

	}
	public void showWithFrame(Component c, int x, int y, JFrame _f) {
		frame = _f;
		show(c, x, y);
	}
	/**
	 * 右メニューから生成
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (buffer==null) JOptionPane.showMessageDialog(null, "buffer is null");
		if (arg0.getActionCommand().indexOf("variable")>-1){
			if (arg0.getActionCommand().indexOf("manual")>-1){
				buffer.varField.addVariable(cstamp,false);
			} else {
				buffer.varField.addVariable(cstamp,true);
			}
		}
		if (arg0.getActionCommand().indexOf("new")>-1){
			if (arg0.getActionCommand().indexOf("manual")>-1){
				buffer.objField.addObject(cstamp,false);
			} else {
				buffer.objField.addObject(cstamp,true);
			}
		}

	}

}
