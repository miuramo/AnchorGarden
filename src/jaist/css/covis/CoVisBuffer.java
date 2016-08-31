package jaist.css.covis;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.TreeMap;

import javax.swing.JCheckBox;
import javax.swing.Timer;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PPath;
import jaist.css.covis.cls.Anchor;
import jaist.css.covis.cls.ClassField;
import jaist.css.covis.cls.ClassFieldMenu;
import jaist.css.covis.cls.ClassStamp;
import jaist.css.covis.cls.Covis_Array;
import jaist.css.covis.cls.Covis_BTree;
import jaist.css.covis.cls.Covis_Frac;
import jaist.css.covis.cls.Covis_Object;
import jaist.css.covis.cls.Covis_Oval;
import jaist.css.covis.cls.Covis_Rect;
import jaist.css.covis.cls.Covis_String;
import jaist.css.covis.cls.Covis_int;
import jaist.css.covis.cls.Covis_primitive;
import jaist.css.covis.cls.ObjectField;
import jaist.css.covis.cls.StaticField;
import jaist.css.covis.cls.VarField;
import jaist.css.covis.cls.Variable;
import jaist.css.covis.hist.CVHist_Link;
import jaist.css.covis.hist.CVHist_Method;
import jaist.css.covis.hist.CVHist_MethodNew;
import jaist.css.covis.hist.CVHist_New;
import jaist.css.covis.hist.CVHist_Unlink;
import jaist.css.covis.hist.CVHist_Value;
import jaist.css.covis.hist.CVHist_ValueArray;
import jaist.css.covis.hist.CVHist_Var;
import jaist.css.covis.hist.CVHistory;

/**
 * 生徒パネルビューの表示データ（モデル）．ATNWindowがビューとなり，このATNBufferを参照して表示する．
 * @author miuramo
 *
 */
public class CoVisBuffer extends RootBuffer {
	public static final Color stafcolor = new Color(255,255,240,200);
	public static final Color objfcolor = new Color(200,255,240,200);
	public static final Color varfcolor = new Color(220,220,255,200);
	public static final Color typefcolor = new Color(255,222,56,200);
	public static final Color linkcolor = new Color(200,255,200,200);
	public static final Color ulinkcolor = new Color(255,200,200);
	public static final Color varcolor = new Color(140,200,255);
	public static final Color methodcolor = new Color(255,250,255);

	/**
	 * Log4J
	 */
//	Category log;
	public CoVisProperty covisProperty;
	JCheckBox updateContCB;

	public ObjectField objField;
	public ClassField clsField;
	public VarField varField;
	public StaticField staField;
	Timer clsFieldInitTimer;

	ArrayList<ClassStamp>[] advancedClass;

//	InfoGetter ig;

	public TreeMap<Long, CVHistory> history;

	public void putHistoryMethod(String type, Object obj, String methodcall, boolean isShow){
		CVHist_Method cvhist = new CVHist_Method(obj, this, methodcall, isShow);
		//		obj.setCVHist(cvhist);
		history.put(System.currentTimeMillis(), cvhist);
		updateSourceWindow();
	}
	public void putHistoryMethodNew(String type, Covis_Object obj, String methodcall){
		CVHist_MethodNew cvhist = new CVHist_MethodNew(obj, this, methodcall);
		obj.setCVHist(cvhist);
		history.put(System.currentTimeMillis(), cvhist);
		updateSourceWindow();
	}
	public void putHistoryNew(String type, Covis_Object obj){
		putHistoryNew(type, obj, true);
	}
	public void putHistoryNew(String type, Covis_Object obj, boolean isShow){
		CVHist_New cvhist = new CVHist_New(obj, this, isShow);
		obj.setCVHist(cvhist);
		if (isShow) history.put(System.currentTimeMillis(), cvhist);
		updateSourceWindow();
	}
	public void putHistoryVar(String type, Variable var) {
		putHistoryVar(type, var, false);
	}
	public void putHistoryVar(String type, Variable var, boolean isShow) {
		CVHist_Var cvhist = new CVHist_Var(var, this, isShow);
		var.setCVHist(cvhist);
		if (isShow) history.put(System.currentTimeMillis(), cvhist);
		if (isShow) updateSourceWindow();
	}
	public void putHistoryEditValue(Variable var){
		CVHist_Value cvhist = new CVHist_Value(var, this);
		//		var.setCVHist(cvhist);
		history.put(System.currentTimeMillis(), cvhist);
		updateSourceWindow();
	}
	public void putHistoryEditValueArray(Covis_primitive v, Covis_Array a) {
		CVHist_ValueArray cvhist = new CVHist_ValueArray(v,a,this);
		history.put(System.currentTimeMillis(), cvhist);
		updateSourceWindow();
	}

	public void putHisotryLink(String type, Anchor a, Covis_Object obj){
		if (obj==null) {System.out.println("obj=null"); return;}
		if (obj.cvhist==null) {System.out.println("obj.cvhist=null"); return;}
		if (obj.cvhist.getCode()==null) {System.out.println("obj.cvhist.getCode()=null"); return;}
		if (a.getSrcVariable()==null) {System.out.println("a.getVarIfAvailable()=null"); return;}

		if (obj.cvhist.getCode().startsWith("new ") || (obj.cvhist instanceof CVHist_MethodNew && !obj.cvhist.isConsumed()) ){ //newで始まるばあい
			// もし，NEWが先なら，変数だけつけると変数定義が後になってしまうため，変数定義もつける
			//			if (a.getSrcVariable() != null){
			// String[] strAry1 = new String[4];
			//				if (a.getSrcVariable().cvhist==null) {System.out.println("a.getVarIfAvailable().cvhist=null"); return;}

			if (obj.cvhist.tstamp < a.getSrcVariable().cvhist.tstamp){ //変数のほうを後に作成したなら
				String vardefine = a.getSrcVariable().cvhist.getCode();
				vardefine = vardefine.substring(0, vardefine.length()-1);
				//					obj.cvhist.setCode(vardefine +" = "+obj.cvhist.getCode());
				setHistLinkCode_ConcatinateVarDefAndNew(a, vardefine, obj);
				a.getSrcVariable().cvhist.setAlive(false);
			} else {
				//					obj.cvhist.setCode(a.getVarName()+" = "+obj.cvhist.getCode());
				//					System.out.println(a.getVarName()+" "+a.varName);
				setHistLinkCode_ConcatinateVarDefAndNew(a, Variable.getShortestName(a.srcVariable.getVarNamesAry()), obj);//ここここ
				//					a.getSrcVariable().cvhist.setAlive(false);
			}
			obj.cvhist.setConsumed(true);
			//incoming>0 のオブジェクトであれば，全部Consumedにする．過去のnewオブジェクトにリンクできなくなるが．
			for(CVHistory cvh : history.values()){
				if (cvh instanceof CVHist_New){
					CVHist_New cvhnew = (CVHist_New)cvh;
					if (cvhnew.getObject().anchors_incoming.size()>0){
						cvhnew.setConsumed(true);
					}
				}
			}
			//			}
		} else { //リンクの場合
			CVHist_Link cvhist = new CVHist_Link(a, obj, this);
			if (cvhist.getCode() != null)
				history.put(System.currentTimeMillis(), cvhist);
		}
		updateSourceWindow();
	}
	private void setHistLinkCode_ConcatinateVarDefAndNew(Anchor a, String varDefOrVarName, Covis_Object obj){
		System.out.println("setHistLinkCode_ConcatinateVarDefAndNew varDefOrVarName "+varDefOrVarName);
		obj.cvhist.setCode(varDefOrVarName+
				" = "+obj.cvhist.getCode(),true);

		return;
	}

	public void putHistoryUnLink(String type, Anchor anchor) {
		System.out.println("putHistoryUnlink");
		CVHist_Unlink cvhist = new CVHist_Unlink(anchor, this);
		if (!cvhist.getCode().startsWith("null"))
			history.put(System.currentTimeMillis(), cvhist);
		updateSourceWindow();
	}

	public void updateSourceWindow(){
		StringBuffer sb = new StringBuffer();
		for(CVHistory s: history.values()){
			if (s.isAlive()) sb.append(s.getCode()+"\n");
		}
//		addLog(10,sb.toString()); //TODO: ログ追加
		if (getWindow().srcWindow != null) 
			getWindow().srcWindow.jta.setText(sb.toString());
	}

	public CoVisBuffer(){
		this(null);
	}

	public CoVisBuffer(CoVisProperty _sprop) {
		super();
//		log = Logger.getLogger(CoVisBuffer.class.getName());
		if (_sprop == null) _sprop = CoVisBuffer.loadProperty();
		covisProperty = _sprop;

		//		viewcontroller = new ViewController(this);
		//		updateStateAction = new DBUpdateContinuousAction("連続", "general/Refresh16", "連続更新", new Integer(KeyEvent.VK_C));

		history = new TreeMap<Long, CVHistory>();

//		if (AnchorGarden.LOGGINGSRC){
//			ig = new InfoGetter();//TODO:ここでログイン必要かどうか決める
//		}
//		initialize(); //このinitialize は上のstudent stock initialize, StudentPanel stock initialize の後で行う必要がある
//		if (AnchorGarden.LOGGINGSRC){
//			ig.addLog(1, "started");
//		}

	}
//	public void addLog(int i, String src) {
//		if (ig != null) ig.addLog(i, src);
//	}
//	public void addLogNow(int i, String src) {
//		if (ig != null) ig.addLogNow(i, src);
//	}

	@SuppressWarnings("unchecked")
	public void initialize() {
		layer.setTransparency(0.0f); //初期化時のごたごたを隠すため(ATNWindow.initialize()の最後で除除に表示)

		int width = 1100;
		int height = 900;

		PNode bg = new PNode();
		for(int i=0;i<=width;i+=100){
			PPath line = PPath.createLine(i, 0, i, height);
			line.setPaint(Color.LIGHT_GRAY);
			line.setTransparency(0.3f);
			bg.addChild(line);
		}
		for(int j=0;j<=height;j+=100){
			PPath line = PPath.createLine(0, j, width, j);
			line.setPaint(Color.LIGHT_GRAY);
			line.setTransparency(0.3f);
			bg.addChild(line);
		}
		layer.addChild(bg);

		objField = new ObjectField("Object", objfcolor, this);
		objField.setOffset(500,0);
		objField.setPathToRectangle(0, 0, 600, height);
		objField.addAttribute("tooltip", objField);
		layer.addChild(objField);

		varField = new VarField("Variable", varfcolor, this);
		varField.addAttribute("tooltip", varField);
		varField.setOffset(180,0);
		layer.addChild(varField);

		clsField = new ClassField("Type", typefcolor, this);
		//		clsField.setOffset(840,0);
		clsField.setOffset(0,0);
		layer.addChild(clsField);

		//		staField = new StaticField("Class (Static)", stafcolor, this);
		//		staField.setOffset(350,-160);
		//		staField.setPathToRectangle(0, 0, 750, 120);
		//		staField.addAttribute("tooltip", staField);
		//		layer.addChild(staField);




		advancedClass = new ArrayList[3];
		for(int i=0;i<advancedClass.length; i++){
			advancedClass[i] = new ArrayList<ClassStamp>();
		}

		ClassStamp newP;

		newP = new ClassStamp(new Covis_Frac(this,true) ,this);
		advancedClass[1].add(newP);
		clsField.addChild(newP);
		newP = new ClassStamp(new Covis_Array(this,true,"Frac"), this);
		advancedClass[1].add(newP);
		clsField.addChild(newP);


		newP = new ClassStamp(new Covis_BTree(this,true) ,this);
		advancedClass[1].add(newP);
		clsField.addChild(newP);
		//		newP = new ClassStamp(new Covis_Array(this,true,"BTree"), this);
		//		advancedClass[1].add(newP);
		//		clsField.addChild(newP);


		newP = new ClassStamp(new Covis_int(this,true) ,this);
		advancedClass[0].add(newP);
		clsField.addChild(newP);
		newP = new ClassStamp(new Covis_Array(this,true,"int") ,this);
		advancedClass[0].add(newP);
		clsField.addChild(newP);
		newP = new ClassStamp(new Covis_String(this,true) ,this);
		advancedClass[0].add(newP);
		clsField.addChild(newP);
		newP = new ClassStamp(new Covis_Array(this,true,"String") ,this);
		advancedClass[0].add(newP);
		clsField.addChild(newP);

		// Advanced Classes
		newP = new ClassStamp(new Covis_Object(this,true) ,this);
		advancedClass[2].add(newP);
//		clsField.addChild(newP);
		newP = new ClassStamp(new Covis_Array(this,true,"Object") ,this);
		advancedClass[2].add(newP);
		//		clsField.addChild(newP);
		newP = new ClassStamp(new Covis_Oval(this,true) ,this);
		advancedClass[2].add(newP);
		newP = new ClassStamp(new Covis_Array(this,true,"Oval") ,this);
		advancedClass[2].add(newP);
		newP = new ClassStamp(new Covis_Rect(this,true) ,this);
		advancedClass[2].add(newP);
		newP = new ClassStamp(new Covis_Array(this,true,"Rect") ,this);
		advancedClass[2].add(newP);


		//		newP = new ClassStamp(new Covis_Random(this,true));
		//		advancedClass.add(newP);
		//		newP = new ClassStamp(new Covis_char(this,true));
		//		advancedClass.add(newP);
		//		newP = new ClassStamp(new Covis_Array(this,true,"char"));
		//		advancedClass.add(newP);
		//		newP = new ClassStamp(new CopyOfCovis_Array(this,true));
		//		advancedClass.add(newP);

		clsField.layout(1);
		//		clsField.layoutByToString(0);

		clsField.addAttribute("popupMenu", new ClassFieldMenu(clsField,this));

	}
	public void createInitTimer(){
		if (clsFieldInitTimer == null){
			clsFieldInitTimer = new Timer(500, new ActionListener(){
				public void actionPerformed(ActionEvent e){
					//					clsField.layout(200);
					while(getWindow()==null)
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
						getWindow().zoomHomePane(1400);

						//					System.out.println("clsField init");
						clsFieldInitTimer.stop();
						clsFieldInitTimer = null;
				}
			});
			clsFieldInitTimer.start();
		}
	}

	public void updateFromDB(){
		//		mysql.updateFromDB();
	}

	public static CoVisProperty loadProperty() {
		return new CoVisProperty(null);
	}
	public void resetWorld() {
		varField.removeAllVariables();
		history.clear();
		for(ClassStamp ct: clsField.getTypeNodes()){
			ct.cv_type.clear_objCount();
		}
	}

	public void showAdvancedClasses(int level) {
		for(int i=0;i<advancedClass.length;i++){
			for(ClassStamp cp: advancedClass[i]){
				cp.removeFromParent();
				if (ClassStamp.selectedType == cp){
					cp.toggleSelected(null);
				}
			}
		}
		for(int i=0;i<=level;i++){
			for(ClassStamp cp: advancedClass[i]){
				clsField.addChild(cp);
			}
		}
		//		clsField.layout(1);
		clsField.layoutByToString(0);
	}
	public void addVariableField() {
		VarField vf = new VarField("Variable", new Color(220,220,255), this);
		vf.addAttribute("tooltip", varField);
		vf.setOffset(200,0);
		layer.addChild(vf);

	}
}
