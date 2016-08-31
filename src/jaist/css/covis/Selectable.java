package jaist.css.covis;

import java.util.ArrayList;
import java.util.Hashtable;

import edu.umd.cs.piccolo.PNode;

public interface Selectable {
	boolean isSelected();
	void setSelected(boolean f, ArrayList<Selectable> list);
	void toggleSelected(ArrayList<Selectable> list);
	void removeLabel(Hashtable<PNode, PNode> trash);
	void toFront();
	public ArrayList<Selectable> getAllChildren();
	public PNode theNode();
}
