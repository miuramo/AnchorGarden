package jaist.css.covis.mi;

import jaist.css.covis.JLabelW;
import jaist.css.covis.JLabelWL;
import jaist.css.covis.cls.Covis_Object;
import jaist.css.covis.cls.Covis_String;
import jaist.css.covis.cls.Variable;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import edu.umd.cs.piccolo.activities.PActivity;

public class MethodInvocationDialog extends JDialog implements KeyListener {
	private static final long serialVersionUID = 1852035735398130391L;

	JFrame parent;
	JTextField jtfa, jtfb;
	JButton ok;

	WrapMethod wm;
	Variable var;

	public MethodInvocationDialog(JFrame p, String title, WrapMethod _wm, String mes1, Variable v) {
		super(p, title, true);
		parent = p;
		wm = _wm;
		var = v;

		//		final JComboBox[] jcb = new JComboBox[wm.paramClses.length];
		final InputField[] jcb = new InputField[wm.paramClses.length];
		for(int i=0;i<wm.paramClses.length;i++){
			System.out.println(wm.paramClses[i].toString());
			if (wm.paramClses[i].toString().equals("int")){
				jcb[i] = new InputField_int(i+1);
			} else if (wm.paramClses[i].toString().equals("class java.lang.String")){
				jcb[i] = new InputField_String("");
			} else {
				jcb[i] = new InputField_Reference(wm.candidates.get(wm.paramClses[i]));
			}
			((Component)jcb[i]).addKeyListener(this);
		}

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(new JLabelW(mes1), BorderLayout.NORTH);
		JPanel inner = new JPanel();
//		inner.setLayout(new GridLayout(2,wm.paramClses.length*2+1));
//		inner.add(new JLabel());
//		for(int i=0;i<wm.paramClses.length;i++){
//			inner.add(new JLabelW(wm.paramClses[i].toString().replaceAll("jaist\\.css\\.covis\\.cls\\.Covis\\_", "").replaceAll("class", "")));
//			inner.add(new JLabel());
//		}
		inner.add(new JLabelW(Variable.getShortestName(var.getVarNamesAry())+"."+wm.method.getName().replace("covis_", "")+"("));
		for(int i=0;i<wm.paramClses.length;i++){
			inner.add((Component) jcb[i]);
			if (i < wm.paramClses.length-1) inner.add(new JLabelW(","));
			else inner.add(new JLabelWL(")"));
		}
		getContentPane().add(inner, BorderLayout.CENTER);
		ok = new JButton("ok");
		getContentPane().add(ok, BorderLayout.SOUTH);
		pack();
		setLocation(p.getLocation().x + (p.getWidth()-this.getWidth())/2, p.getLocation().y +(p.getHeight()-this.getHeight())/2);

		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object[] args = new Object[wm.paramClses.length];
				for(int i=0;i<wm.paramClses.length;i++){
					args[i] = jcb[i].getSelectedItemObject();
				}
				//メソッドコールを文字列に
				StringBuffer sb = new StringBuffer();
				sb.append(Variable.getShortestName(var.getVarNamesAry())+"."+wm.method.getName().replace("covis_", "")+"(");
				for(int i=0;i<wm.paramClses.length;i++){
					sb.append(jcb[i].getSelectedItemString()+",");
				}

				invokeMethod(args, sb.toString().substring(0, sb.length()-1)+");");

				setVisible(false);
			}

		});
		//		jtfb.addKeyListener(new KeyAdapter() {
		//			public void keyPressed(KeyEvent e) {
		//				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
		//					jtfb = null;
		//					dispose();
		//				}
		//			}
		//		});
	}
	public void invokeMethod(Object[] args, String methodcall) {
		Object retValObject = null;
		try {
			retValObject = wm.method.invoke(wm.variable.anchor.destObject, args);
		} catch (IllegalArgumentException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		} catch (InvocationTargetException e1) {
			e1.printStackTrace();
		}

		if (retValObject instanceof Covis_Object){
			Covis_Object coob = (Covis_Object)retValObject;
			if (coob.anchors_incoming.size()<1){
				wm.obj.buffer.objField.addObject(coob);
			}
			if (coob instanceof Covis_String){
				wm.obj.buffer.putHistoryMethodNew("new", coob, methodcall+"//> "+coob.toString());
			} else {
				wm.obj.buffer.putHistoryMethodNew("new", coob, methodcall);
			}/*
			} else {
				if (coob instanceof Covis_BTree){
					wm.obj.buffer.putHistoryMethodNew("new", coob, methodcall);
				} else  {
					wm.obj.buffer.putHistoryMethod("method", coob, methodcall+"//> "+coob.toString(),true);
				}
			}*/
			highlightReturnObject(coob);
		} else {
			if (retValObject != null){
				wm.obj.buffer.putHistoryMethod("method", retValObject, methodcall+"//> "+retValObject.toString(), true);
			} else {
				//				System.out.println(wm.method.getReturnType().toString());
				if (wm.method.getReturnType().toString().equals("void"))//元々voidだから期待してない
					wm.obj.buffer.putHistoryMethod("method", retValObject, methodcall, true);
				else
					wm.obj.buffer.putHistoryMethod("method", retValObject, methodcall+"//> null", true);
			}
		}
	}

	private void highlightReturnObject(Covis_Object coob) {
		//		PActivity z1 = coob.animateToZoom(2.0f,300);
		coob.setScale(2.0f);
		PActivity z2 = coob.animateToZoom(1.0f,300);
		PActivity p1 = coob.animateToTransparency(0f, 300);
		PActivity p2 = coob.animateToTransparency(1f, 300);
		PActivity p11 = coob.animateToTransparency(0f, 300);
		PActivity p12 = coob.animateToTransparency(1f, 300);
		PActivity p21 = coob.animateToTransparency(0f, 300);
		PActivity p22 = coob.animateToTransparency(1f, 300);
		PActivity p31 = coob.animateToTransparency(0f, 300);
		PActivity p32 = coob.animateToTransparency(1f, 300);
		p1.setStartTime(System.currentTimeMillis());
		p2.startAfter(p1);
		p11.startAfter(p2);
		p12.startAfter(p11);
		p21.startAfter(p12);
		p22.startAfter(p21);
		p31.startAfter(p22);
		p32.startAfter(p31);
		z2.startAfter(p32);
	}
	@Override
	public void keyPressed(KeyEvent arg0) {
		if (arg0.getKeyCode() == KeyEvent.VK_ENTER){
			ok.doClick();
		}
	}
	@Override
	public void keyReleased(KeyEvent arg0) {	}
	@Override
	public void keyTyped(KeyEvent arg0) {	}

	public static void showDialog(JFrame parent, String title, WrapMethod _wm, String mes1, Variable v) {
		MethodInvocationDialog d = new MethodInvocationDialog(parent,
				title, _wm, mes1, v);
		d.setVisible(true);
		if (d.jtfa != null && d.jtfb != null){
			//			frac.a.setValue(d.jtfa.getText());
			//			frac.b.setValue(d.jtfb.getText());
		} else
			return;
	}
}