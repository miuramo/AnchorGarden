package jaist.css.covis.cls;

import jaist.css.covis.CoVisBuffer;
import jaist.css.covis.JLabelW;
import jaist.css.covis.SrcWindow;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;

public class Covis_Frac extends Covis_Object {

	private static final long serialVersionUID = 8906063645409838445L;
	public static Color defaultColor = new Color(200,170,255);
	public Covis_Frac(CoVisBuffer buf, boolean isAuto){
		super(buf, isAuto);
		color = defaultColor;
		setPaint(color);
		setStroke(basicStroke);
	}
	public Covis_Frac(Color c, CoVisBuffer buf, boolean isAuto){
		super(c, buf, isAuto);
	}
	Covis_int a,b;
	// overwrite
	public String getConstructorInfo() {
//		return "new "+getClsName()+"("+valueText.getText()+"); // or, just "+valueText.getText();
		return "new "+getClsName()+"("+a.value+","+b.value+");";
	}

//	public String getConstructorArguments() {
//		return a.value+", "+b.value;
//	}

	public void init(boolean isAuto){
		setPathToRectangle(0, 0, 100,60);
		PPath sep = PPath.createLine(60,7,40,55);
		sep.setStroke(Covis_Object.basicStroke);
		sep.addAttribute("moveTarget", this);
		sep.addAttribute("tooltip", this);
		addChild(sep);
		
		a = new Covis_int(buffer, isAuto);
		a.addAttribute("moveTarget", this);
		a.addAttribute("tooltip", this);
		a.valueText.addAttribute("moveTarget", this);
		a.valueText.addAttribute("tooltip", this);
		a.setValue("2");
		b = new Covis_int(buffer, isAuto);
		b.addAttribute("moveTarget", this);
		b.addAttribute("tooltip", this);
		b.valueText.addAttribute("moveTarget", this);
		b.valueText.addAttribute("tooltip", this);
		b.setValue("3");
		if (isAuto){
		} else {
			FracConstructorDialog dialog = FracConstructorDialog.showDialog(buffer.getWindow().frame, this, "Constructor of Frac", "new Frac( a , b );");
			if (dialog.isCanceled()) {
				this.setVisible(false);
				return;
			}
		}

		a.setScale(0.8f);
		b.setScale(0.8f);
		a.offset(5, 5);
		b.offset(63, 15);
		addChild(a);
		addChild(b);
		
		// インデックス値を右肩にはりつける
		PText ptidx = new PText("a");
		ptidx.setScale(0.6);
		ptidx.setOffset(18,5);
		ptidx.setTextPaint(Color.blue);
		addChild(ptidx);
		ptidx.addAttribute("moveTarget", this);
		ptidx.addAttribute("tooltip", this);

		PText ptidx2 = new PText("b");
		ptidx2.setScale(0.6);
		ptidx2.setOffset(77,15);
		ptidx2.setTextPaint(Color.blue);
		addChild(ptidx2);
		ptidx2.addAttribute("moveTarget", this);
		ptidx2.addAttribute("tooltip", this);
	}
	public Covis_Object Covis_clone(boolean isAuto){
		return new Covis_Frac(buffer, isAuto);
	}
	public Color getClassColor(){
		return defaultColor;
	}
	
	public static int objCount = 0;
	public static int objAryCount = 0;
	public String getNextVarName(boolean isAry){
		StringBuffer sb = new StringBuffer(getClsName().toLowerCase().substring(0,4));
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
	@Override
	public void clear_objCount() {
		objCount = 0;
		objAryCount = 0;
		System.out.println("clear count "+getClsName());
	}
	//デフォルトコンストラクタの引数(プリミティブなので，必要としないはずだが)
	@Override
	public String getConstructorArgs() {
		return " a, b ";
	}
	
	public void covis_reduce(){
		int ia = Integer.valueOf(a.value);
		int ib = Integer.valueOf(b.value);
		if (ib == 0) return;
		int g = gcd(ia,ib);
		ia = ia/g;
		ib = ib/g;
		a.setValue(String.valueOf(ia));
		b.setValue(String.valueOf(ib));
	}
	public int gcd(int m, int n){
        while (m != n) {
            if (m < n) {
                n -= m;
            } else {
                m -= n;
            }
        }
        return n;
    }
	/////////////////////////////////////////////////////////////////////////////////////
/*	public float covis_getFloatValue(){
		int ia = Integer.valueOf(a.value);
		int ib = Integer.valueOf(b.value);
		System.out.println("in method "+(float)ia/ib);
		return ((float)ia/(float)ib);
	}
	public int covis_getBunsi(){
		int ia = Integer.valueOf(a.value);
		System.out.println("in method "+ia);
		return ia;
	}
	public int covis_getBumbo(){
		int ib= Integer.valueOf(b.value);
		System.out.println("in method "+ib);
		return ib;
	}*/
	public void covis_setBunsiBumbo(int fa, int fb){
		a.setValue(String.valueOf(fa));
		b.setValue(String.valueOf(fb));
	}
	public String covis_print(){
		return toString();
	}
	public Covis_Frac covis_plus_new(Covis_Frac other){
		int ia = Integer.valueOf(a.value);
		int ib = Integer.valueOf(b.value);
		int oa = Integer.valueOf(other.a.value);
		int ob = Integer.valueOf(other.b.value);
		if (ib == 0 || ob == 0) return null;	
		ia = (ia*ob)+(oa*ib);
		ib = ib * ob;
		Covis_Frac cf = new Covis_Frac(buffer,true);
		cf.a.setValue(String.valueOf(ia));
		cf.b.setValue(String.valueOf(ib));
		return cf;
	}
	public void covis_plus(Covis_Frac other){
		int ia = Integer.valueOf(a.value);
		int ib = Integer.valueOf(b.value);
		int oa = Integer.valueOf(other.a.value);
		int ob = Integer.valueOf(other.b.value);
		if (ib == 0 || ob == 0) return;	
		ia = (ia*ob)+(oa*ib);
		ib = ib * ob;
		a.setValue(String.valueOf(ia));
		b.setValue(String.valueOf(ib));
	}
	public void covis_minus(Covis_Frac other){
		int ia = Integer.valueOf(a.value);
		int ib = Integer.valueOf(b.value);
		int oa = Integer.valueOf(other.a.value);
		int ob = Integer.valueOf(other.b.value);
		if (ib == 0 || ob == 0) return;	
		ia = (ia*ob)-(oa*ib);
		ib = ib * ob;
		a.setValue(String.valueOf(ia));
		b.setValue(String.valueOf(ib));
	}
	public Covis_Frac covis_minus_new(Covis_Frac other){
		int ia = Integer.valueOf(a.value);
		int ib = Integer.valueOf(b.value);
		int oa = Integer.valueOf(other.a.value);
		int ob = Integer.valueOf(other.b.value);
		if (ib == 0 || ob == 0) return null;	
		ia = (ia*ob)-(oa*ib);
		ib = ib * ob;
		Covis_Frac cf = new Covis_Frac(buffer,true);
		cf.a.setValue(String.valueOf(ia));
		cf.b.setValue(String.valueOf(ib));
		return cf;
	}

	public void covis_mul(Covis_Frac other){
		int ia = Integer.valueOf(a.value);
		int ib = Integer.valueOf(b.value);
		int oa = Integer.valueOf(other.a.value);
		int ob = Integer.valueOf(other.b.value);
		if (ib == 0 || ob == 0) return;	
		ia = ia * oa;
		ib = ib * ob;
		a.setValue(String.valueOf(ia));
		b.setValue(String.valueOf(ib));
	}
	public Covis_Frac covis_mul_new(Covis_Frac other){
		int ia = Integer.valueOf(a.value);
		int ib = Integer.valueOf(b.value);
		int oa = Integer.valueOf(other.a.value);
		int ob = Integer.valueOf(other.b.value);
		if (ib == 0 || ob == 0) return null;	
		ia = ia * oa;
		ib = ib * ob;
		Covis_Frac cf = new Covis_Frac(buffer,true);
		cf.a.setValue(String.valueOf(ia));
		cf.b.setValue(String.valueOf(ib));
		return cf;
	}
	public void covis_div(Covis_Frac other){
		int ia = Integer.valueOf(a.value);
		int ib = Integer.valueOf(b.value);
		int oa = Integer.valueOf(other.a.value);
		int ob = Integer.valueOf(other.b.value);
		if (ib == 0 || ob == 0) return;
		ia = ia * ob;
		ib = ib * oa;
		a.setValue(String.valueOf(ia));
		b.setValue(String.valueOf(ib));
	}
	public Covis_Frac covis_div_new(Covis_Frac other){
		int ia = Integer.valueOf(a.value);
		int ib = Integer.valueOf(b.value);
		int oa = Integer.valueOf(other.a.value);
		int ob = Integer.valueOf(other.b.value);
		if (ib == 0 || ob == 0) return null;	
		ia = ia * ob;
		ib = ib * oa;
		Covis_Frac cf = new Covis_Frac(buffer,true);
		cf.a.setValue(String.valueOf(ia));
		cf.b.setValue(String.valueOf(ib));
		return cf;
	}
	public String toString(){
		return a.value+"/"+b.value;
	}
	
	public static String classdef = "public class Frac {\n"+
"   int a; //bunsi\n"+
"   int b; //bumbo\n"+
"\n"+
"   public Frac() {\n"+
"      a = 2;  b = 3;\n"+
"   }\n"+
"   public Frac(int ia, int ib) {\n"+
"      a = ia;  b = ib;\n"+
"   }\n"+
//"   public float getFloatValue(){\n"+
//"      return ((float)a/(float)b);\n"+
//"   }\n"+
"   public void setBunsiBumbo(int fa, int fb){\n"+
"      a = fa;  b = fb;\n"+
"   }\n"+
"   public void print(){\n"+
"      System.out.println(a+\"/\"+b);\n"+
"   }\n"+
"   public void plus(Frac f){\n"+
"      int na = a * f.b + b * f.a;\n"+
"      int nb = b * f.b;\n"+
"      a = na;  b = nb;\n"+
"   }\n"+
"   public Frac plus_new(Frac f){\n"+
"      int na = a * f.b + b * f.a;\n"+
"      int nb = b * f.b;\n"+
"      return new Frac(na,nb);\n"+
"   }\n"+
"   public void minus(Frac f){ } //略\n"+
"   public Frac minus_new(Frac f){ } //略\n"+
"   public void mul(Frac f){ } //略\n"+
"   public Frac mul_new(Frac f){ } //略\n"+
"   public void div(Frac f){ } //略\n"+
"   public Frac div_new(Frac f){ } //略\n"+
"   public void reduce(){ //約分 \n"+
"      int g = gcd(a,b);\n"+
"      a = a/g; b = b/g;\n"+
"   }\n"+
"   public int gcd(int m, int n){\n"+
"      while (m != n) {\n"+
"         if (m < n) { n -= m; }\n"+
"         else { m -= n;  }\n"+
"      }\n"+
"      return n;\n"+
"   }\n"+
"}\n"; 

}

class FracConstructorDialog extends JDialog implements KeyListener {
	private static final long serialVersionUID = 1852035735398130391L;

	JFrame parent;

	JTextField jtfa, jtfb;

	JButton ok;
	boolean canceled = true;

	public FracConstructorDialog(JFrame p, Covis_Frac frac, String title, String mes1) {
		super(p, title, true);
		parent = p;
		jtfa = new JTextField(frac.a.getValue());
		jtfa.setFont(SrcWindow.sans30);
		jtfa.setBackground(Covis_int.defaultColor);
		jtfa.addKeyListener(this);
		jtfb = new JTextField(frac.b.getValue());
		jtfb.setFont(SrcWindow.sans30);
		jtfb.setBackground(Covis_int.defaultColor);
		jtfb.addKeyListener(this);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(new JLabelW(mes1), BorderLayout.NORTH);
		JPanel inner = new JPanel();
		inner.setLayout(new GridLayout(2,2));
		inner.add(new JLabelW("a"));
		inner.add(jtfa);
		inner.add(new JLabelW("b"));
		inner.add(jtfb);
		getContentPane().add(inner, BorderLayout.CENTER);
		ok = new JButton("ok");
		getContentPane().add(ok, BorderLayout.SOUTH);
//		pack();
		setSize(200,180);
		setLocation(p.getLocation().x + (p.getWidth() - this.getWidth())/2, p.getLocation().y +(p.getHeight()-this.getHeight())/2);

		jtfa.setCaretPosition(jtfa.getText().length());
		jtfb.setCaretPosition(jtfb.getText().length());

		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				canceled = false;
				setVisible(false);
			}
		});
//		cancel.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				tarea = null;
//				dispose();
//			}
//		});

		jtfa.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					jtfa = null;
					jtfb = null;
					dispose();
				}
			}
		});
		jtfb.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					jtfa = null;
					jtfb = null;
					dispose();
				}
			}
		});
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
	
	public boolean isCanceled(){
		return canceled;
	}

	public static FracConstructorDialog showDialog(JFrame parent, Covis_Frac frac, String title, String mes1) {
		FracConstructorDialog d = new FracConstructorDialog(parent,
				frac, title, mes1);
		d.setVisible(true);
		if (d.jtfa != null && d.jtfb != null){
			frac.a.setValue(String.valueOf(Integer.parseInt(d.jtfa.getText())));
			frac.b.setValue(String.valueOf(Integer.parseInt(d.jtfb.getText())));
		}
		return d;
	}
}

