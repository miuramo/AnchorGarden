package jaist.css.covis.cls;

import jaist.css.covis.CoVisBuffer;
import edu.umd.cs.piccolo.event.PInputEvent;

public interface DragNotice {
	public void setDragNotice(boolean f);
	public void drag(PInputEvent e, CoVisBuffer buffer);
	public void endDrag(PInputEvent e, CoVisBuffer buffer);
}
