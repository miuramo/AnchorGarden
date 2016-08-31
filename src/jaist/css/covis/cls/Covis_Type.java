package jaist.css.covis.cls;

import jaist.css.covis.CoVisBuffer;

import java.awt.Color;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PPath;

@SuppressWarnings("serial")
public abstract class Covis_Type extends PPath {
	public CoVisBuffer buffer;

	public abstract String getClsName();
	public abstract Covis_Type createNew(JFrame f, boolean isAuto) throws InvocationTargetException;
	public abstract PNode createToolTip();
	public abstract Color getClassColor();
	public abstract String getNextVarName(boolean isArray);
	public abstract Covis_Type newInstance(boolean isAuto) throws InvocationTargetException;
	public abstract void clear_objCount();
	public abstract String getConstructorArgs();
}
