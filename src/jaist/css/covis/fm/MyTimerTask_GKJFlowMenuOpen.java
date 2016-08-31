package jaist.css.covis.fm;

/**
 * フローメニューが開くときのタイマータスク
 * 
 * @author miuramo
 *
 */
public class MyTimerTask_GKJFlowMenuOpen extends MyTimerTask_Abstract {
	FlowMenu_TMRG fm;

	public MyTimerTask_GKJFlowMenuOpen(float _x, float _y, AbstractFlowMenu f) {
		super(_x, _y, f);
		fm = (FlowMenu_TMRG) f;

	}

	public void run() {
		if (fm.enabled && fm.target != null && fm.drags.size() < 3 && fm.window.flowmenuEventHandler.getEventFilter() == fm.window.b1mask) {// target != null means
														// "drugging",
														// strokecount < 5 means
														// "no squiggle"
			preopen();
			openMenu();
		}
	}

	public void preopen() {
	}

	public void openMenu() {
		super.openMenu();
		fm.window.setShowToolTip(false);
	}
}