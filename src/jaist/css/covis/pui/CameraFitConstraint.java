package jaist.css.covis.pui;

import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import edu.umd.cs.piccolo.PCamera;
import edu.umd.cs.piccolo.util.PBounds;

/**
 * �\���̈�i�L�����o�X�j�̃T�C�Y�ύX�ɍ��킹�āC�҂����背�C�A�E�g���邽�߂�PropertyChangeListener
 * 
 * ������PMenu (Piccolo Menu:����ɍ쐬�����ȈՃ{�^���j�̈ʒu�𒲐����邽�߂Ɏg�p
 * 
 * @author miuramo
 * 
 */
public class CameraFitConstraint implements PropertyChangeListener {
	public static enum PositionEnum {
		TOPLEFT, TOPRIGHT, TOPFILL, TOPCENTER, BOTTOMLEFT, BOTTOMRIGHT, BOTTOMFILL; // Cascading Style Sheet �̃}�[�W����`�ɂ�������тƓ���
	}

	CameraFittable target;

	PCamera camera;

	PositionEnum constraint;

	Dimension defaultGap;

	public CameraFitConstraint(CameraFittable t, PCamera cam, PositionEnum con,
			Dimension _defaultGap) {
		target = t;
		camera = cam;
		constraint = con;
//		defaultOffset = _defaultOffset;
		defaultGap = _defaultGap;
	}

	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getSource() == camera) {
			if (evt.getPropertyName().equals("bounds")) {
				fitTo(camera.getBoundsReference());
			}
		}
	}
	public void fitTo(PBounds b){
		if (constraint == PositionEnum.TOPRIGHT)
			fitToTopRight(camera.getBoundsReference());
		else if (constraint == PositionEnum.TOPLEFT)
			fitToTopLeft(camera.getBoundsReference());
		else if (constraint == PositionEnum.TOPCENTER)
			fitToTopCenter(camera.getBoundsReference());

	}

	public void fitToTopRight(PBounds b) {
		// Bounds���ω�������I�I�I
		// resize((float)b.width);
		target.setOffset(b.width - target.getFullBounds().width, 0);

		// preBounds = b;
	}
	public void fitToTopLeft(PBounds b) {
		// Bounds���ω�������I�I�I
		// resize((float)b.width);
		target.setOffset(0, 0);
		// preBounds = b;
	}
	public void fitToTopCenter(PBounds b) {
		// Bounds���ω�������I�I�I
		// resize((float)b.width);
		target.setOffset(b.getWidth()/2 - target.getFullBounds().width/2, 0);
		// preBounds = b;
	}


	public void fitToBottomFill(PBounds b) {
		// Bounds���ω�������I�I�I
		resize((float) b.width);
		target.setOffset(defaultGap.getWidth(), b.height
				- target.getFullBounds().height - 2);

		// preBounds = b;
	}

	public void resize(float width) {
		target.setPathToRectangle((float) 0, (float) 0,
				(float) (width - (defaultGap.getWidth() * 2)),
				(float) defaultGap.getHeight());
	}
}