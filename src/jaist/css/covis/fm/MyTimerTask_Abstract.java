package jaist.css.covis.fm;

import java.awt.Cursor;
import java.util.TimerTask;

/**
 * 一般的なタイマータスク
 * 
 * 
 * @author miuramo
 *
 */
public class MyTimerTask_Abstract extends TimerTask {
	public static Cursor handcursor = new Cursor(Cursor.HAND_CURSOR);

	public static Cursor defaultcursor = new Cursor(Cursor.DEFAULT_CURSOR);

	float x, y;

	AbstractFlowMenu fm_super;

	public MyTimerTask_Abstract(float _x, float _y, AbstractFlowMenu f) {
		x = _x;
		y = _y;
		fm_super = f;
	}

	public void run() {
	}

	public void preopen() {
	}

	public void openMenu() {
		fm_super.showMenu(true);
	}
}