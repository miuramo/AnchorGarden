package jaist.css.covis.mi;

import jaist.css.covis.cls.Anchor;
import jaist.css.covis.cls.Covis_Object;
import jaist.css.covis.cls.Variable;

import java.awt.event.ActionEvent;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Map.Entry;

import javax.swing.AbstractAction;

import edu.umd.cs.piccolo.PNode;

public class WrapMethod extends AbstractAction {
	private static final long serialVersionUID = -3933799327221553449L;
	Method method;
	Covis_Object obj;
	Variable variable;
	public WrapMethod(Method m, Covis_Object o, String mname, Variable var){
		super(mname);
		obj = o; method = m; variable = var;
	}
	Class<?>[] paramClses;
	HashMap<Class<?>,HashMap<String,Object>> candidates;
	@Override
	public void actionPerformed(ActionEvent arg0) {
		System.out.println(method.toString());
		paramClses = method.getParameterTypes();
		if (paramClses.length==0){ //引数なければ，そのまま実行！
			Object[] arg = null;
			MethodInvocationDialog mid = new MethodInvocationDialog(obj.buffer.getWindow().frame, "invoke method", this, "select arguments", variable);
			//メソッドコールを文字列に
			StringBuffer sb = new StringBuffer();
			sb.append(Variable.getShortestName(variable.getVarNamesAry())+"."+method.getName().replace("covis_", "")+"();");

			mid.invokeMethod(arg, sb.toString());
			return;
		}
		candidates = new HashMap<Class<?>, HashMap<String,Object>>();
		for(Class<?> c: paramClses){
			if (candidates.containsKey(c)) continue;

//			System.out.println(c.toString()+"を探索");
			//格納容器
			HashMap<String, Object> temp = new HashMap<String, Object>();
			for(PNode pn: obj.buffer.objField.getAllNodes()){
				if (c.isInstance(pn)){
					Covis_Object o = (Covis_Object)pn;
					
					TreeMap<String,Anchor> map = o.referenceAnchors();
					for(Entry<String,Anchor> set: map.entrySet()){
						String s = set.getKey();
						Anchor a = set.getValue();
						//						System.out.println("a "+a.getVarClass());
						//						System.out.println("c "+c.getName());
						if (c.isAssignableFrom(a.getVarClass())){
							temp.put(Variable.getShortestName(a.srcVariable.getVarNamesAry()), a.destObject);
							System.out.println(s);
						}
					}
				}
			}
			candidates.put(c,temp);
		}
		//オブジェクトの候補（参照のための変数）はあつまった．
		MethodInvocationDialog.showDialog(obj.buffer.getWindow().frame, "invoke method", this, "select arguments", variable);
	}	
}

