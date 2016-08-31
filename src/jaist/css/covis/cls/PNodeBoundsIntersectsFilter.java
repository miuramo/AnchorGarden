package jaist.css.covis.cls;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.util.PBounds;
import edu.umd.cs.piccolo.util.PNodeFilter;

/**
 * �����ɗ^�����m�[�h�̗̈悪�Cbounds�̗̈�Ɋ��S�ɓ����Ă��邩�ǂ����𒲂ׂ�t�B���^
 * 
 * @author miuramo
 *
 */
public class PNodeBoundsIntersectsFilter implements PNodeFilter {
	PBounds bounds; // ���肷��̈�

	public PNodeBoundsIntersectsFilter(PBounds r) {
		bounds = r;
	}

	public boolean accept(PNode aNode) {
		if (aNode instanceof Covis_Object){
			System.out.println(aNode.getGlobalFullBounds().toString());
			if (bounds.intersects(aNode.getGlobalFullBounds()) || bounds.contains(aNode.getGlobalFullBounds())) { // �bounds���Carp�Əd�Ȃ�ꍇ�̂�
				return true;
			}
		}
		return false;
	}

	public boolean acceptChildrenOf(PNode aNode) {
		return true;
	}
}
