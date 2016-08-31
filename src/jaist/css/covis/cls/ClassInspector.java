package jaist.css.covis.cls;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ClassInspector {
	Class<?> targetClass;
	public ClassInspector(Class<?> target){
		targetClass = target;
	}

	public void inspect_print() {
		inspect_print_recursive(targetClass,0);
	}
	public void Systemoutprintln(String s, int n){
		for(int i=0;i<n;i++){
			System.out.print(" ");
		}
		System.out.println(s);
	}
	public void inspect_print_recursive(Class<?> c, int n) {
		Systemoutprintln("Name: "+c.getName(),n);
		Systemoutprintln("CanonicalName :"+c.getCanonicalName(),n);
		Systemoutprintln("============Fields",n);
		Field[] fs = c.getDeclaredFields();
		for(Field f: fs){
//			Systemoutprintln("Modifier: "+Modifier.toString(f.getModifiers()),n);
//			Systemoutprintln("Name: "+f.getName(),n);
//			Systemoutprintln("Type: "+f.getType().toString(),n);
			Systemoutprintln(""+f.toString(), n);
		}
		Method[] ms = c.getDeclaredMethods();
		Systemoutprintln("============Methods",n);
		for(Method m: ms){
//			Systemoutprintln("Modifier: "+Modifier.toString(m.getModifiers()),n);
//			Systemoutprintln("Name: "+m.getName(),n);
//			Systemoutprintln("Return Type: "+m.getReturnType().toString(),n);
			Systemoutprintln(""+m.toString(),n);
		}
		Constructor<?>[] conss = c.getDeclaredConstructors();
		Systemoutprintln("============Constructors",n);
		for(Constructor<?> cons : conss){
//			Systemoutprintln("Modifier: "+Modifier.toString(cons.getModifiers()),n);
//			Systemoutprintln("Name: "+cons.getName(),n);
			Systemoutprintln(""+cons.toString(),n);
		}
		Systemoutprintln("",n);
		Class<?> superclazz = c.getSuperclass();
		if (superclazz==null) return;
		else inspect_print_recursive(superclazz,n+2);
	}



}
