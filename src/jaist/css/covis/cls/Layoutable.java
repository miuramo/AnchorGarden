package jaist.css.covis.cls;

import edu.umd.cs.piccolo.PNode;

public interface Layoutable {
	public void layout(int dur);
	public void layoutExceptOne(PNode pn, int dur);
}
