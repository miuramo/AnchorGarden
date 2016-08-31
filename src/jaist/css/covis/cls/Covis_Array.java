package jaist.css.covis.cls;

import jaist.css.covis.CoVisBuffer;

import java.awt.Color;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;
import edu.umd.cs.piccolo.util.PDimension;

public class Covis_Array extends Covis_Object {
	private static final long serialVersionUID = 8131819427660903628L;
	public static Color defaultColor = new Color(222,122,10);
	public static String lastSelectedClass = "Object";
	public Covis_Type elementObj;
	public String elementClassStr; // あらかじめ，定義してもよい（"int", "String"など）
	public static int lastAryaNum = 4;

	//	ArrayList<Anchor> anchors_member;
	ArrayList<String> data4primitive;
	ArrayList<Covis_primitive> primitives;
	int sizeofArray;

	public Covis_Array(CoVisBuffer buf, boolean isAuto){
		super(buf, isAuto);
		color = defaultColor;
		setPaint(color);
		setStroke(basicStroke);
		elementObj = this;
		init_length(4,isAuto);
	}
	public Covis_Array(CoVisBuffer buf, boolean isAuto, String presetElementClass){
		super(buf, isAuto);
		elementClassStr = presetElementClass;
		Class<?> c;
		try {
			c = Class.forName("jaist.css.covis.cls.Covis_"+elementClassStr);
			color = (Color) c.getField("defaultColor").get(null);
			Constructor<?> con = c.getConstructor(CoVisBuffer.class, boolean.class);
			elementObj = (Covis_Type) con.newInstance(buf, true);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		setPaint(color);
		setStroke(basicStroke);
		init_length(4,isAuto);
	}
	public Covis_Array(Color c, CoVisBuffer buf, boolean isAuto){
		super(c, buf, isAuto);
	}
	public Covis_Array(Covis_Type obj, Class<?> t, CoVisBuffer buf, int arySize, boolean isAuto){
		super(buf, isAuto);
		type = t;
		color = obj.getClassColor();
		elementObj = obj;
		setPaint(color);
		setStroke(basicStroke);
		init_length(arySize, isAuto);
	}
	public String getClsName(){
		if (elementClassStr == null){
			int p = getClass().getName().lastIndexOf("_");
			return getClass().getName().substring(p+1)+"[]";
		} else {
			return elementClassStr+"[]";
		}
	}
	public void setValues(String s, int size){
		data4primitive = new ArrayList<String>();
		primitives = new ArrayList<Covis_primitive>();
		String[] sary = s.split(",");
		for(int i=0;i<sary.length;i++){
			//			String ts = String.valueOf(s.charAt(i));
			//			System.out.println(ts);
			if (size <= i) break;
			Covis_primitive prim = null;
			try {
				prim = (Covis_primitive)elementObj.newInstance(true);
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			prim.setPathToRectangle(0, 0, 25, 46);
			prim.setPaint(null);
			prim.setStrokePaint(null);
			prim.setValue(sary[i]);
			prim.justify();
			prim.setOffset(i*25,2);
			addChild(prim);
			data4primitive.add(prim.getValue()); // ts では，charのときに'' で囲まれない
			prim.valueText.addAttribute("moveTarget", this);
			prim.valueText.addAttribute("popupMenu", new ArrayPrimitiveMenu(prim,this));
			prim.addAttribute("index", String.valueOf(i));
			prim.valueText.addAttribute("tooltip", prim);
			prim.addAttribute("parentAry", this);
			primitives.add(prim);
		}
	}
	// もともとのinitは，早く呼ばれすぎてサイズを反映できない.　なので，何もしない
	public void init(boolean isAuto){
	}
	public void init_length(int size, boolean isAuto){
		setPathToRectangle(0, 0, size*25, 50);

		for(int i=0;i<size;i++){
			PPath sep = PPath.createLine(i*25+25,0,i*25+25,50);
			addChild(sep);
			sep.addAttribute("moveTarget", this);
			sep.addAttribute("tooltip", this);

			// TODO:交通整理が必要（プリミティブの場合は，あとで取り除く必要がある）
			if (elementObj instanceof Covis_Object){
				Variable v;
				if (elementObj != null){
					v = new VariableM(elementObj,null,buffer,"["+String.valueOf(i)+"]",this);
				} else {
					v = new VariableM(this,null,buffer,"["+String.valueOf(i)+"]",this);
				}
				v.anchor.type = ((Covis_Object)elementObj).type;
				v.anchor.setAnchorEnabled(false); //まだ触れない．変数からリンクされたら，触れるようにする．
				anchors_member.add(v.anchor);
				v.setOffset(i*25+3,27);
				addChild(v);
				buffer.putHistoryVar("var", v, false);


				/*				Anchor a;
				if (elementObj != null){
					a = new VariableM(elementObj.getClass(),null,buffer,"["+String.valueOf(i)+"]");
				} else {
					a = new Anchor(getClass(), this, idx);
				}
				a.type = ((Covis_Object)elementObj).type;
				a.setAnchorEnabled(false); //まだ触れない．変数からリンクされたら，触れるようにする．
				aryAnchors.add(a);
				a.setOffset(i*25+25-2,47);
				addChild(a);*/
			} else {

			}
			// インデックス値を右肩にはりつける
			PText ptidx = new PText("["+String.valueOf(i)+"]");
			ptidx.setScale(0.6);
			ptidx.setOffset(i*25+13,4);
			ptidx.setTextPaint(Color.red);
			addChild(ptidx);
			ptidx.addAttribute("moveTarget", this);
			ptidx.addAttribute("tooltip", this);

//			idx++;
		}

		if (elementObj instanceof Covis_int){
			if (isAuto){
				String s = "0,0,0,0,0,0,0,0,0,0,0";//.substring(0, size);
				setValues(s,size);
			} else {
				String s = ArrayInitializeWindow.getCSVString(buffer.getWindow().frame, size, "int");
				setValues(s,size);
			}
		} else if (elementObj instanceof Covis_char){
			if (isAuto){
				String s = "a,b,c,d,e,f,g,h,i,j,k,l,m,n";//.substring(0, size);
				setValues(s,size);
			} else {
				String s = ArrayInitializeWindow.getCSVString(buffer.getWindow().frame, size, "char");
				setValues(s,size);
			}
		}

		sizeofArray = size;
	}

	public Covis_Object createNew(JFrame frame, boolean isAuto){
		if (elementClassStr == null){
			// 型（クラス・プリミティブ）選択
			String s = (String)JOptionPane.showInputDialog(
					frame,
					"Choose Type of the Array",
					"Choose Type",
					JOptionPane.QUESTION_MESSAGE,
					null,
					ClassStamp.possibilities,
					lastSelectedClass);
			if ((s != null) && (s.length() > 0)) {
				//				System.out.println(s+" Selected.");
				lastSelectedClass = s;
				elementClassStr = s;
			} else {
				return null;
			}
		}

		int aryLength = 4;

		if (!isAuto){
			String val = JOptionPane.showInputDialog(buffer.getWindow().frame, "Input array size. [1-10]", "4");
			if (val != null) {
				aryLength = Integer.parseInt(val);
				if (aryLength < 1) aryLength = 1;
				if (aryLength > 10) aryLength = 10;
			} else {
				return null;
			}
		}
		try {
			Class<?> c = Class.forName("jaist.css.covis.cls.Covis_"+elementClassStr);
			Constructor<?> constructor = c.getConstructor(CoVisBuffer.class, boolean.class);
			Covis_Type cv_class = (Covis_Type) constructor.newInstance(buffer,true);
			type = Array.newInstance(c, aryLength).getClass();

			return new Covis_Array(cv_class, type, buffer, aryLength, isAuto);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return null;
	}
	public Covis_Object Covis_clone(boolean isAuto){
		return new Covis_Array(buffer, isAuto);
	}
	public Color getClassColor(){
		return defaultColor;
	}

	public static int objCount = 0;
	public static int objAryCount = 0;
	public String getNextVarName(boolean isAry){
		StringBuffer sb = new StringBuffer(getClsName().toLowerCase().substring(0,3));
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
	/**
	 * オーバーライド
	 */
	public void detach(Anchor anchor){
		super.detach(anchor);
		checkAtRefDetached();
	}
	/**
	 * オーバーライド
	 */
	public void attach(Anchor anchor){
		super.attach(anchor);
		checkAtRefDetached();
		//		for(Anchor a: aryAnchors){
		//		a.anchortab.tooltip = null;
		//		if (a.dest != null) a.dest.tooltip = null;
		//		a.setAnchorEnabled(true);
		//		}
		for(Anchor a: anchors_member){ //基本的にはcheckAtRefDetachedを利用し，アンカー有効化だけ，別に処理
			a.setAnchorEnabled(true);  //本当は，はずしたときには無効化をしないといけないのでは（だけど，全部外れたかどうかチェックする必要があるので大変）
		}
	}
	/**
	 * リファレンスが切れたときに，チェックする
	 */
	public void checkAtRefDetached(){
		HashSet<Anchor> temp = new HashSet<Anchor>();
		for(Anchor a: anchors_incoming){
			if (a.srcVariable.object != this) temp.add(a);
			a.anchortab.tooltip = null;
		}
		if (temp.size()==0){
//			tc.tick_transparency[0] = -0.05f; //一旦離したら早く消える
			tc.transparencyThread_Start(0, this, 50);
			addAttribute("moveLink", null);
		}
		for(Anchor a: anchors_member){
			a.anchortab.tooltip = null;
			if (a.destObject != null) a.destObject.tooltip = null;
		}
		if (primitives != null){
			for(Covis_primitive prim : primitives){
				prim.tooltip = null;
			}
		}
		tooltip = null;
	}

	public void dispose(){
		for(Anchor a: anchors_member){
			if (a.destObject != null) a.destObject.detach(a);
		}
		super.dispose();
	}
	public void move(PDimension d){
		super.move(d);
		for(Anchor a: anchors_member){
			a.start_RewindThread();
		}
	}
	//	overwrite
	public String getConstructorInfo() {
		StringBuffer sb = new StringBuffer();
		if (elementObj instanceof Covis_primitive){
			if (buffer.getWindow().isAutoMode.isSelected()){ // isAuto
				sb.append("new "+elementObj.getClsName()+"["+sizeofArray+"]");
			} else {
				String defaultVar = "0";
				boolean hasUndefaultValue = false;
				if (elementObj instanceof Covis_int) defaultVar = "0";

				sb.append("new "+elementObj.getClsName()+"[]");
				sb.append("{");
				for(String s: data4primitive){
					if (elementObj instanceof Covis_char){
						sb.append("'"+s+"',");
					} else {
						sb.append(s+",");
					}
					if (!s.equals(defaultVar)) hasUndefaultValue = true;
				}
				sb = new StringBuffer(sb.substring(0, sb.length()-1));
				sb.append("}");

				if (!hasUndefaultValue){
					sb = new StringBuffer();
					sb.append("new "+elementObj.getClsName()+"["+sizeofArray+"]");
				}
			}
		} else {
			sb.append("new "+elementObj.getClsName()+"["+sizeofArray+"]");
		}
		return sb.append(";").toString();
	}
	/**
	 * 配列の要素変更用（プリミティブ）
	 * @param v
	 * @return
	 */
	public String getEditValueInfo(Covis_primitive v) {
		//参照名の１つ+[x] = val
		return referenceVarNames().split("\n")[0]+
		"["+(String)v.getAttribute("index")+"] = "+v.getValue()+";";
	}
	public Covis_Object newInstance(boolean isAuto){
		Constructor<?> constructor;
		Covis_Object cv_class = null;
		try {
			if (elementClassStr == null){
				constructor = getClass().getConstructor(CoVisBuffer.class, boolean.class);
				cv_class = (Covis_Object) constructor.newInstance(buffer,isAuto);
			} else {
				constructor = getClass().getConstructor(CoVisBuffer.class, boolean.class,String.class);
				cv_class = (Covis_Object) constructor.newInstance(buffer,isAuto,elementClassStr);
			}

		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return cv_class;
	}

	@Override
	public void clear_objCount() {
		try {
			Field f = getClass().getField("objCount");
			f.setInt(getClass(), 0);
			Field fg = getClass().getField("objAryCount");
			fg.setInt(getClass(), 0);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		if (elementObj instanceof Covis_Object){
			for(Anchor a: anchors_member){
				if (a.destObject == null){
					sb.append("null,");
				} else {
					sb.append(a.destObject.toString()+",");
				}
			}
		} else {
			for(String s: data4primitive){
				sb.append(s+",");
			}
		}
		return sb.toString().substring(0, sb.toString().length()-1)+"}";
	}
	
}
