package jaist.css.covis;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPopupMenu;

/**
 * ポップアップメニュー起動用のイベントリスナ
 * 
 * ControlPanelの出番が減ったため，未使用
 * @author miuramo
 *
 */
@Deprecated
public class PopupListener extends MouseAdapter {
	JPopupMenu popup_withshift;

	JPopupMenu popup_normal;

	public PopupListener(JPopupMenu p_shift, JPopupMenu p_normal) {
		popup_withshift = p_shift;
		popup_normal = p_normal;
	}

	public void mousePressed(MouseEvent e) {
		maybeShowPopup(e);
	}

	public void mouseReleased(MouseEvent e) {
		maybeShowPopup(e);
	}

	private void maybeShowPopup(MouseEvent e) {
		if (e.isPopupTrigger()) {
			if (e.isShiftDown()) {
				popup_withshift.show(e.getComponent(), e.getX(), e.getY());
			} else {
				popup_normal.show(e.getComponent(), e.getX(), e.getY());
			}
		}
	}
}