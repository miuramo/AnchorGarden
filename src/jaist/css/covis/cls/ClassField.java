package jaist.css.covis.cls;

import jaist.css.covis.CoVisBuffer;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Timer;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PText;

public class ClassField extends Field {
	private static final long serialVersionUID = 3524513728998576206L;
	public static PNode nullToolTip = new PNode();
	public String typeName; // Variable, Object, Class
	public Color color;
	PText caption;
	public static Font bold = new Font("sansserif", Font.BOLD, 12);
	public static float top = 40;
	public Timer rewindTimer;
	public CoVisBuffer buffer;
	public ClassField(String _typeName, Color c, CoVisBuffer buf){
		super(_typeName, c);
	}
	
	public ArrayList<ClassStamp> getTypeNodes(){
		List<PNode> col = new ArrayList<PNode>(getChildrenReference());
		ArrayList<ClassStamp> ret = new ArrayList<ClassStamp>();
		for(PNode p: col) {
			if (p instanceof ClassStamp){
				ClassStamp v = (ClassStamp)p;
				ret.add(v);
			}
		}
		return ret;
	}
}
