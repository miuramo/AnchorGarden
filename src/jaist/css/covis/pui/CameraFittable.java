package jaist.css.covis.pui;

import edu.umd.cs.piccolo.util.PBounds;

/**
 * ��ʕ\���̈�ɂ҂����葵���Ĕz�u�����I�u�W�F�N�g���������ׂ��C���^�t�F�[�X�D
 * 
 * pui.PMenuRoot ���������Ă���D
 * @author miuramo
 *
 */
public interface CameraFittable {
	public void setOffset(double x, double y);

	public void setPathToRectangle(float x, float y, float w, float h);

	public PBounds getFullBounds();
}
