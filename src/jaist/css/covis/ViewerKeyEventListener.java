package jaist.css.covis;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Hashtable;

import edu.umd.cs.piccolo.PCamera;
import edu.umd.cs.piccolo.event.PBasicInputEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.util.PBounds;
import jaist.css.covis.pui.PMenuItem_Abstract;
import jaist.css.covis.util.BUtil;

/**
 * キーボードからのキー入力イベントを処理するクラス
 * @author miuramo
 *
 */
public class ViewerKeyEventListener extends PBasicInputEventHandler implements KeyListener {
	AnchorGarden viewer;

	Hashtable<Character, PMenuItem_Abstract> shortCutKeyMap;
//	Category log;
	
	public ViewerKeyEventListener(AnchorGarden _viewer) {
		viewer = _viewer;
		shortCutKeyMap = new Hashtable<Character, PMenuItem_Abstract>();
//		log = Logger.getLogger(ViewerKeyEventListener.class.getName());
	}

	public void addPMenuToShortcutList(PMenuItem_Abstract pm) {
		shortCutKeyMap.put(pm.text.charAt(0), pm);
	}

	public void keyPressed(PInputEvent e) {
//		log.debug("keyCode=" + e.getKeyCode() + " keyChar ["
//				+ e.getKeyChar() + "]");
		if (e.getKeyCode() == 27) {// ESC
			viewer.zoomHomePane(500);
		}
		if (e.getKeyCode() == 36) {// HOME
			viewer.getCanvas().getCamera().animateViewToCenterBounds(
					viewer.buffer.layer.getFullBounds(), true, 1000);
			// layoutHome();
		}
		if (e.getKeyCode() == 34) {// PageDown + 59
			PCamera pc = viewer.getCanvas().getCamera();
			PBounds pb = pc.getViewBounds();
			pb = BUtil.zoomBounds(pb, 0.9);
			pc.animateViewToCenterBounds(pb, true, 0);
		}
		if (e.getKeyCode() == 33) {// PageUp -
			PCamera pc = viewer.getCanvas().getCamera();
			PBounds pb = pc.getViewBounds();
			pb = BUtil.zoomBounds(pb, 1.1);
			pc.animateViewToCenterBounds(pb, true, 0);
		}
		if (e.getKeyCode() == 32) {
//			log.debug("scale: "
//					+ viewer.getCanvas().getCamera().getViewScale());
		}
		if (e.getKeyCode() == 112){ // Function 1 (F1) で生徒番号のトグル
//			viewer.buffer.viewcontroller.manageLayer("cover", !viewer.buffer.viewcontroller.getLayerState("cover"));
		}
		if (e.getKeyCode() == 113){ // Function 2 (F2) で調査
//			viewer.buffer.viewcontroller.manageLayer("drawInfo", !viewer.buffer.viewcontroller.getLayerState("drawInfo"));
		}
		if (e.getKeyCode() == 116){ // Function 5 (F5)
			viewer.buffer.updateFromDB();
		}
		
		if (e.getKeyCode() == 127){ // Del
//			viewer.buffer.updateFromDB();
			viewer.flowmenuEventHandler.selectionHandler.removeSelected();
		}
//		if (e.getKeyCode() == 90 && e.isControlDown()){ // Ctrl-Z = Undo Del
//			viewer.flowmenuEventHandler.selectionHandler.undoRemoveSelected();
//		}
		
		PMenuItem_Abstract pmenu = shortCutKeyMap.get(e.getKeyChar());
		if (pmenu != null)
			pmenu.doClick(e);
		else {
			pmenu = shortCutKeyMap.get(Character.toLowerCase(e.getKeyChar()));
			if (pmenu != null)
				pmenu.doCapicalShortcutKeySelected(e);
		}
	}

	public void mousePressed(PInputEvent event) {
		event.getInputManager().setKeyboardFocus(this);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == 27) {// ESC
			viewer.zoomHomePane(500);
		}
		if (e.getKeyCode() == 36) {// HOME
			viewer.getCanvas().getCamera().animateViewToCenterBounds(
					viewer.buffer.layer.getFullBounds(), true, 1000);
			// layoutHome();
		}
		if (e.getKeyCode() == 34) {// PageDown + 59
			PCamera pc = viewer.getCanvas().getCamera();
			PBounds pb = pc.getViewBounds();
			pb = BUtil.zoomBounds(pb, 0.9);
			pc.animateViewToCenterBounds(pb, true, 0);
		}
		if (e.getKeyCode() == 33) {// PageUp -
			PCamera pc = viewer.getCanvas().getCamera();
			PBounds pb = pc.getViewBounds();
			pb = BUtil.zoomBounds(pb, 1.1);
			pc.animateViewToCenterBounds(pb, true, 0);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}
