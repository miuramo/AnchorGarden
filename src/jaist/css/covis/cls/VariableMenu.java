package jaist.css.covis.cls;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import jaist.css.covis.mi.WrapMethod;
import jaist.css.covis.util.FramePopup;


public class VariableMenu extends JPopupMenu implements FramePopup {
	Variable v;
	JFrame frame;
	private static final long serialVersionUID = -3662668322301800275L;

	JMenu methodMenu;
	JMenuItem deleteMenuItem;

	public VariableMenu(Variable _v) {
		this.v = _v;

		JMenuItem menuItem;

		setLightWeightPopupEnabled(false);

		menuItem = new JMenuItem("cancel");
		add(menuItem);


		addSeparator();
		//		
		//		menuItem = new JMenuItem("method");
		//		add(menuItem);
		//		
		//		menuItem.addActionListener(new ActionListener() {
		//			public void actionPerformed(ActionEvent e) {
		//				String mes = JOptionPane.showInputDialog(null,"input method name");
		//				Method meth = null;
		//				try {
		//					meth = v.anchor.dest.getClass().getMethod(mes, new Class<?>[]{});
		//				} catch (SecurityException e1) {
		//					e1.printStackTrace();
		//				} catch (NoSuchMethodException e1) {
		//					e1.printStackTrace();
		//				}
		//				if (meth != null){
		//					try {
		////						meth.invoke(v.anchor.dest, new Object[]{null});
		//						meth.invoke(v.anchor.dest);
		//					} catch (IllegalArgumentException e1) {
		//						e1.printStackTrace();
		//					} catch (IllegalAccessException e1) {
		//						// TODO Auto-generated catch block
		//						e1.printStackTrace();
		//					} catch (InvocationTargetException e1) {
		//						// TODO Auto-generated catch block
		//						e1.printStackTrace();
		//					}
		//				}
		//				
		//			}
		//		});

		/*		if (false){
			addSeparator();
			menuItem = new JMenuItem("rename");
			add(menuItem);
			menuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					//					System.out.println(e.getSource().getClass().getName());
					v.rename(frame);
				}
			});
		}
		 */
		if (v.cv_class instanceof Covis_primitive && !v.isArray){
			addSeparator();
			menuItem = new JMenuItem("edit value");
			add(menuItem);
			menuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					//					System.out.println(e.getSource().getClass().getName());
					((Covis_primitive)v.cv_class).edit(frame,v);
				}
			});
		}


	}

	public void showWithFrame(Component c, int x, int y, JFrame _f) {
		frame = _f;

		if (deleteMenuItem != null) remove(deleteMenuItem);

		if (v.anchor != null)
			if (v.anchor.destObject != null){
				if (methodMenu != null) remove(methodMenu);
				//メソッド探索開始
				methodMenu = new JMenu("Method");
				add(methodMenu);
				//			Method[] methods = v.anchor.destObject.getClass().getDeclaredMethods();
				Method[] methods = v.anchor.destObject.getClass().getMethods();
				for(Method m: methods){
					if (m.getName().startsWith("covis_")){
						String mname = m.toString().replaceAll("java\\.lang\\.", "");
						//										System.out.println("mname "+mname);
						String mname2 = mname.replaceAll("jaist\\.css\\.covis\\.cls\\.Covis\\_", "");
						//										System.out.println("mname2 " +mname2);
						String mname3 = mname2.replaceAll("covis\\_", "");
						//										System.out.println("mname3 "+mname3);

						//２つめのスペースから，.までを削除
						int lastspcPos = mname3.lastIndexOf(" ");
						int lastdotPos = mname3.lastIndexOf(".");
						String mname4 = mname3.substring(0,lastspcPos)+" "+mname3.substring(lastdotPos+1);
						//										System.out.println("mname4 "+mname4);
						JMenuItem mi = new JMenuItem(new WrapMethod(m, v.anchor.destObject, mname4, v));
						methodMenu.add(mi);
					}
				}

			}
		deleteMenuItem = new JMenuItem("delete");
		add(deleteMenuItem);
		deleteMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				v.dispose();
			}
		});

		show(c, x, y);
	}
}

