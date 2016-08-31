package jaist.css.covis.pui;


import java.util.ArrayList;
import java.util.Iterator;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PPath;

/**
 * PMenuItemを並べて表示するための受け皿(メニュールート)
 * @author miuramo
 *
 */
@SuppressWarnings("serial")
public class PMenuRoot extends PPath implements CameraFittable {
	PMenuItem_Abstract selected = null; // 選択中のメニュー

	ArrayList<PMenuItem_Abstract> pmenulist;

	public PMenuRoot() {
		pmenulist = new ArrayList<PMenuItem_Abstract>();
	}

	public void layoutChildren() {
		double xOffset = 0;
		double yOffset = 0;

		Iterator<?> i = getChildrenIterator();
		while (i.hasNext()) {
			PNode each = (PNode) i.next();
			each.setOffset(xOffset - each.getX(), yOffset);
			xOffset += each.getWidth();// + 2; //(gap)
		}
	}

	public void add(PMenuItem_Abstract childMenu) {
		childMenu.parent = this;
		addChild(childMenu);
		pmenulist.add(childMenu);
	}

	public void setSelected(PMenuItem_Abstract childMenu) {
		for (PMenuItem_Abstract pm : pmenulist) {
			if (pm.isToggle) {
				if (pm == childMenu) {
					pm.setSelected(true);
				} else {
					pm.setSelected(false);
				}
			}
		}
		selected = childMenu;
	}
}