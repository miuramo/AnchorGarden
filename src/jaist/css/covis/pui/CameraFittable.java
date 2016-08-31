package jaist.css.covis.pui;

import edu.umd.cs.piccolo.util.PBounds;

/**
 * 画面表示領域にぴったり揃えて配置されるオブジェクトが実装すべきインタフェース．
 * 
 * pui.PMenuRoot が実装している．
 * @author miuramo
 *
 */
public interface CameraFittable {
	public void setOffset(double x, double y);

	public void setPathToRectangle(float x, float y, float w, float h);

	public PBounds getFullBounds();
}
