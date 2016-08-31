package jaist.css.covis;

import edu.umd.cs.piccolo.event.PDragSequenceEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.util.PDimension;

/**
 * ATNWindow‰Eãƒƒjƒ…[‚Ì[pan]
 * @author miuramo
 *
 */
public class MyPPanEventHandler extends PDragSequenceEventHandler {
	AnchorGarden window;
	
	public MyPPanEventHandler(AnchorGarden _window){
		window = _window;
	}
	public void drag(PInputEvent e) {
		PDimension d = e.getDelta();
		window.canvas.getCamera().translateView(d.getWidth(), d.getHeight());
	}
}
