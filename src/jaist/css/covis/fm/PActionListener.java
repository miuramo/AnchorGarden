package jaist.css.covis.fm;

import edu.umd.cs.piccolo.event.PInputEvent;

/**
 * FlowMenuでメニューアイテムが選択されたときに，実行される標準的なアクションのためのインタフェース
 * 
 * @author miuramo
 *
 */
public interface PActionListener {
	public void actionPerformed(PInputEvent e);
}