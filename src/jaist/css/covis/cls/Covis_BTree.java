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

import edu.umd.cs.piccolo.nodes.PText;

public class Covis_BTree extends Covis_Object {

	private static final long serialVersionUID = 8906063645409838445L;
	public static Color defaultColor = new Color(100,255,150);
	public Covis_BTree(CoVisBuffer buf, boolean isAuto){
		super(buf, isAuto);
		color = defaultColor;
		setPaint(color);
		setStroke(basicStroke);
	}
	public Covis_BTree(Color c, CoVisBuffer buf, boolean isAuto){
		super(c, buf, isAuto);
	}
	Covis_int val;
	VariableM left, right;
	// overwrite
	public String getConstructorInfo() {
		//		return "new "+getClsName()+"("+valueText.getText()+"); // or, just "+valueText.getText();
		return "new "+getClsName()+"("+val.value+");";
	}

	//	public String getConstructorArguments() {
	//		return a.value+", "+b.value;
	//	}

	public void init(boolean isAuto){
		setPathToRectangle(0, 0, 100,60);

		val = new Covis_int(buffer, isAuto);
		val.valueText.addAttribute("moveTarget", this);
		val.valueText.addAttribute("tooltip", this);

		val.setValue("5");
		if (isAuto){
		} else {
			BTreeConstructorDialog dialog = BTreeConstructorDialog.showDialog(buffer.getWindow().frame, this, "Constructor of BTree", "new BTree( a );");
			if (dialog.isCanceled()) {
				this.setVisible(false);
				return;
			}
		}

		val.setScale(0.9f);
		val.addAttribute("moveTarget", this);
		val.addAttribute("tooltip", this);
		val.offset(30, 5);
		addChild(val);

		// インデックス値を右肩にはりつける
		PText ptidx0 = new PText("var");
		ptidx0.setScale(0.6);
		ptidx0.setOffset(42,5);
		ptidx0.setTextPaint(Color.blue);
		addChild(ptidx0);
		ptidx0.addAttribute("moveTarget", this);
		ptidx0.addAttribute("tooltip", this);


		PText ptidx = new PText("left");
		ptidx.setScale(0.6);
		ptidx.setOffset(10,25);
		ptidx.setTextPaint(Color.blue);
		addChild(ptidx);
		ptidx.addAttribute("moveTarget", this);
		ptidx.addAttribute("tooltip", this);

		PText ptidx2 = new PText("right");
		ptidx2.setScale(0.6);
		ptidx2.setOffset(77,25);
		ptidx2.setTextPaint(Color.blue);
		addChild(ptidx2);
		ptidx2.addAttribute("moveTarget", this);
		ptidx2.addAttribute("tooltip", this);

		left = new VariableM(this,null,buffer,"left",this);
		right = new VariableM(this, null, buffer,"right",this);//new Anchor(this.getClass(), this);

		addChild(left);
		left.setOffset(5,35);
		addChild(right);
		right.setOffset(73,35);
		anchors_member.add(left.anchor);
		anchors_member.add(right.anchor);
		left.addAttribute("moveTarget", this);
		left.addAttribute("tooltip", this);
		right.addAttribute("moveTarget", this);
		right.addAttribute("tooltip", this);
		
		buffer.putHistoryVar("var", right, false);
		buffer.putHistoryVar("var", left, false);


	}
	public Covis_Object Covis_clone(boolean isAuto){
		return new Covis_BTree(buffer, isAuto);
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
		return " a ";
	}

	//	/**
	//	 * オーバーライド
	//	 */
	//	public void detach(Anchor anchor){
	//		super.detach(anchor);
	//		checkAtRefDetached();
	//	}
	//	/**
	//	 * オーバーライド
	//	 */
	//	public void attach(Anchor anchor){
	//		super.attach(anchor);
	//		checkAtRefDetached();
	////		for(Anchor a: aryAnchors){
	////		a.anchortab.tooltip = null;
	////		if (a.dest != null) a.dest.tooltip = null;
	////		a.setAnchorEnabled(true);
	////		}
	//		for(Anchor a: anchors_member){ //基本的にはcheckAtRefDetachedを利用し，アンカー有効化だけ，別に処理
	//			a.setAnchorEnabled(true);  //本当は，はずしたときには無効化をしないといけないのでは（だけど，全部外れたかどうかチェックする必要があるので大変）
	//		}
	//	}
	//	/**
	//	 * リファレンスが切れたときに，チェックする
	//	 */
	//	public void checkAtRefDetached(){
	//		HashSet<Anchor> temp = new HashSet<Anchor>();
	//		for(Anchor a: anchors_incoming){
	//			if (a.src.object != this) temp.add(a);
	//			a.anchortab.tooltip = null;
	//		}
	//		if (temp.size()==0){
	//			tc.tick_transparency[0] = -0.05f; //一旦離したら早く消える
	//			tc.transparencyThread_Start(0, this);
	//			addAttribute("moveLink", null);
	//		}
	//		for(Anchor a: anchors_member){
	//			a.anchortab.tooltip = null;
	//			if (a.dest != null) a.dest.tooltip = null;
	//		}
	//		tooltip = null;
	//	}
	public void covis_addNode(int n){
		if (n < Integer.parseInt(this.val.getValue())){
			if (left.anchor.link.dest==null){
				
				Covis_BTree cb = new Covis_BTree(buffer,true);
				cb.val.setValue(String.valueOf(n));//本当は，コンストラクタで一緒にすべし

				buffer.putHistoryNew("method", cb, false);
				buffer.objField.addObject(cb);
				cb.attach(left.anchor);
				cb.setOffsetAlignment(this, -60, 80);
			} else {
				((Covis_BTree)left.anchor.link.dest).covis_addNode(n);
			}
		} else {
			if (right.anchor.link.dest==null){
				Covis_BTree cb = new Covis_BTree(buffer,true);
				cb.val.setValue(String.valueOf(n));//本当は，コンストラクタで一緒にすべし

				buffer.putHistoryNew("method", cb, false);
				buffer.objField.addObject(cb);
				cb.attach(right.anchor);
				cb.setOffsetAlignment(this, 120, 80);
			} else {
				((Covis_BTree)right.anchor.link.dest).covis_addNode(n);
			}
		}
	}
	public Covis_BTree covis_addNodeWithRet(int n){
		if (n < Integer.parseInt(this.val.getValue())){
			if (left.anchor.link.dest==null){
				
				Covis_BTree cb = new Covis_BTree(buffer,true);
				cb.val.setValue(String.valueOf(n));//本当は，コンストラクタで一緒にすべし

//				buffer.putHistoryMethodNew("method", cb, false);
				buffer.objField.addObject(cb);
				cb.attach(left.anchor);
				cb.setOffsetAlignment(this, -60, 80);
				return cb;
			} else {
				return ((Covis_BTree)left.anchor.link.dest).covis_addNodeWithRet(n);
			}
		} else {
			if (right.anchor.link.dest==null){
				Covis_BTree cb = new Covis_BTree(buffer,true);
				cb.val.setValue(String.valueOf(n));//本当は，コンストラクタで一緒にすべし

//				buffer.putHistoryMethodNew("method", cb, false);
				buffer.objField.addObject(cb);
				cb.attach(right.anchor);
				cb.setOffsetAlignment(this, 120, 80);
				return cb;
			} else {
				return ((Covis_BTree)right.anchor.link.dest).covis_addNodeWithRet(n);
			}
		}
	}
	public boolean covis_findNode(int n){
		int myval = Integer.parseInt(this.val.getValue());
		if (n == myval) return true;
		if (n < myval){
			if (left.anchor.link.dest==null){
				return false;
			}
			else {
				return ((Covis_BTree)left.anchor.link.dest).covis_findNode(n);
			}
		} else {
			if (right.anchor.link.dest==null){
				return false;
			} else {
				return ((Covis_BTree)right.anchor.link.dest).covis_findNode(n);
			}
		}
	}
	public Covis_BTree covis_findNodeObj(int n){
		int myval = Integer.parseInt(this.val.getValue());
		if (n == myval) return this;
		if (n < myval){
			if (left.anchor.link.dest==null){
				return null;
			}
			else {
				return ((Covis_BTree)left.anchor.link.dest).covis_findNodeObj(n);
			}
		} else {
			if (right.anchor.link.dest==null){
				return null;
			} else {
				return ((Covis_BTree)right.anchor.link.dest).covis_findNodeObj(n);
			}
		}
	}
	public String toString(){
		return "["+val.getValue()+"]";
	}

//	public Covis_BTree covis_add_and_return(int n){
//		if (n < Integer.parseInt(this.val.getValue())){
//			if (left.anchor.link.dest==null){
//				Covis_BTree cb = new Covis_BTree(buffer,true);
//				cb.val.setValue(String.valueOf(n));
////				buffer.objField.addObject(cb);
//				cb.attach(left.anchor);
//				cb.setOffsetAlignment(this, -60, 80);
//				return cb;
//			} else {
//				((Covis_BTree)left.anchor.link.dest).covis_add(n);
//			}
//		} else {
//			if (right.anchor.link.dest==null){
//				Covis_BTree cb = new Covis_BTree(buffer,true);
//				cb.val.setValue(String.valueOf(n));
////				buffer.objField.addObject(cb);
//				cb.attach(right.anchor);
//				cb.setOffsetAlignment(this, 120, 80);
//				return cb;
//			} else {
//				((Covis_BTree)right.anchor.link.dest).covis_add(n);
//			}
//		}
//		return null;
//	}


	public static String classdef = "" +
"public class BTree {\n"+
"   int val;   //数値\n"+
"   BTree left; //左リンク\n"+
"   BTree right; //右リンク\n"+
"   \n"+
"   public BTree() {\n"+
"      val = 5;\n"+
"   }\n"+
"   public BTree(int ival) {\n"+
"      val = ival;\n"+
"   }\n"+
"   public void addNode(int n){\n"+
"      if (n < val){\n"+
"         if (left == null) \n"+
"             left = new BTree(n);\n"+
"           else left.addNode(n);\n"+
"      } else {\n"+
"         if (right == null)\n"+
"             right = new BTree(n);\n"+
"           else right.addNode(n);\n"+
"      }\n"+
"   }\n"+
"   public BTree addNodeWithRet(int n){\n"+
"      if (n < val){\n"+
"         if (left == null){\n"+
"             left = new BTree(n);\n"+
"             return left;\n"+
"          } else left.addNodeWithRet(n);\n"+
"      } else {\n"+
"         if (right == null){\n"+
"             right = new BTree(n);\n"+
"             return right;\n"+
"          } else right.addNodeWithRet(n);\n"+
"      }\n"+
"      return null;"+
"   }\n"+
"   public boolean findNode(int n){\n"+
"      if (n == val) return true;\n"+
"      if (n < val){\n"+
"         if (left == null) \n"+
"             return false;\n"+
"           else return left.findNode(n);\n"+
"      } else {\n"+
"         if (right == null)\n"+
"             return false;\n"+
"           else return right.findNode(n);\n"+
"      }\n"+
"   }\n"+
"   public BTree findNodeObj(int n){\n"+
"      if (n == val) return this;\n"+
"      if (n < val){\n"+
"         if (left == null) \n"+
"            return null;\n"+
"         else return left.findNodeObj(n);\n"+
"      } else {\n"+
"         if (right == null)\n"+
"           return null;\n"+
"         else return right.findNodeObj(n);\n"+
"      }\n"+
"   }\n"+
"}"; 


}

class BTreeConstructorDialog extends JDialog implements KeyListener {
	private static final long serialVersionUID = 1852035735398130391L;

	JFrame parent;

	JTextField jtfa;

	JButton ok;
	
	boolean canceled = true;

	public BTreeConstructorDialog(JFrame p, Covis_BTree frac, String title, String mes1) {
		super(p, title, true);
		parent = p;
		jtfa = new JTextField(frac.val.getValue());
		jtfa.setFont(SrcWindow.sans30);
		jtfa.setBackground(Covis_int.defaultColor);
		jtfa.addKeyListener(this);//Enterおしたら確定
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(new JLabelW(mes1), BorderLayout.NORTH);
		JPanel inner = new JPanel();
		inner.setLayout(new GridLayout(1,2));
		inner.add(new JLabelW("a"));
		inner.add(jtfa);
		getContentPane().add(inner, BorderLayout.CENTER);
		ok = new JButton("ok");
		getContentPane().add(ok, BorderLayout.SOUTH);
		//		pack();
		setSize(200,120);
		setLocation(p.getLocation().x + (p.getWidth() - this.getWidth())/2, p.getLocation().y +(p.getHeight()-this.getHeight())/2);

		jtfa.setCaretPosition(jtfa.getText().length());

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
					dispose();
				}
			}
		});
	}
	public boolean isCanceled(){
		return canceled;
	}

	public static BTreeConstructorDialog showDialog(JFrame parent, Covis_BTree btre, String title, String mes1) {
		BTreeConstructorDialog d = new BTreeConstructorDialog(parent,
				btre, title, mes1);
		d.setVisible(true);
		if (d.jtfa != null){
			int v = Integer.parseInt(d.jtfa.getText());
			btre.val.setValue(String.valueOf(v));
		}
		return d;
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
}

