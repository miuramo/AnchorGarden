package jaist.css.covis.cls;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.util.PBounds;
import edu.umd.cs.piccolo.util.PNodeFilter;

/**
 * 引数に与えたノードの領域が，boundsの領域に完全に入っているかどうかを調べるフィルタ
 * 
 * @author miuramo
 *
 */
public class PNodeBoundsIntersectsFilter implements PNodeFilter {
	PBounds bounds; // 判定する領域

	public PNodeBoundsIntersectsFilter(PBounds r) {
		bounds = r;
	}

	public boolean accept(PNode aNode) {
		if (aNode instanceof Covis_Object){
			System.out.println(aNode.getGlobalFullBounds().toString());
			if (bounds.intersects(aNode.getGlobalFullBounds()) || bounds.contains(aNode.getGlobalFullBounds())) { // 基準boundsが，arpと重なる場合のみ
				return true;
			}
		}
		return false;
	}

	public boolean acceptChildrenOf(PNode aNode) {
		return true;
	}
}
