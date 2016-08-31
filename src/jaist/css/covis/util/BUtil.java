package jaist.css.covis.util;

import edu.umd.cs.piccolo.util.PBounds;

/**
 * Bounds Utility Class
 * 
 * @author miuramo
 *
 */
public class BUtil {
	/**
	 * �ʐς�Ԃ�
	 * @param pagemanager �̈�
	 * @return �ʐ�
	 */
	public static int getArea(PBounds pb) {
		double w = pb.getWidth();
		double h = pb.getHeight();
		return (int) (w * h);
	}

	/**
	 * ����pb�ŗ^�����̈���C����rate�����g�債���V�����̈��Ԃ�
	 * @param pagemanager �̈�
	 * @param rate �Y�[����
	 * @return �V�����̈�
	 */
	public static PBounds zoomBounds(PBounds pb, double rate) {
		double x = pb.getX();
		double y = pb.getY();
		double w = pb.getWidth();
		double h = pb.getHeight();
		double nw = w * rate;
		double nh = h * rate;
		double nx = x - (nw - w) / 2;
		double ny = y - (nh - h) / 2;
		return new PBounds(nx, ny, nw, nh);
	}
	
	/**
	 * ����pb�ŗ^�����̈���C����rate�����g�債���V�����̈��Ԃ��i������X�����̂݁j
	 * @param pagemanager �̈�
	 * @param rate �Y�[����
	 * @return �V�����̈�
	 */
	public static PBounds zoomBoundsX(PBounds pb, double rate) {
		double x = pb.getX();
		double y = pb.getY();
		double w = pb.getWidth();
		double h = pb.getHeight();
		double nw = w * rate;
		double nh = h;
		double nx = x - (nw - w) / 2;
		double ny = y - (nh - h) / 2;
		return new PBounds(nx, ny, nw, nh);
	}

	/**
	 * ����pb�ŗ^�����̈�ɁC�w�肵�����ƍ������������V�����̈��Ԃ�
	 * @param pagemanager �̈�
	 * @param plusx �������ǉ���
	 * @param plusy �c�����ǉ���
	 * @return �V�����̈�
	 */
	public static PBounds plusBounds(PBounds pb, int plusx, int plusy) {
		double x = pb.getX();
		double y = pb.getY();
		double w = pb.getWidth();
		double h = pb.getHeight();
		double nw = w + plusx;
		double nh = h + plusy;
		double nx = x - (nw - w) / 2;
		double ny = y - (nh - h) / 2;
		return new PBounds(nx, ny, nw, nh);
	}

	/**
	 * pos �� 1����3�̂Ƃ��́C�w�肵���̈���c�̒����𔼕��ɂ����̈���v�Z���C���ꂪ���̗̈�̏ォ�C���S���C���Ɉړ�����D
	 * pos ��0�̂Ƃ��́C�̈�����̂܂ܕԂ�
	 * @param pagemanager �̈�
	 * @param pos �ꏊ
	 * @return �V�����̈�
	 */
	// pos = {0, 1, 2, 3 } = {full, top, middle, bottom};
	public static PBounds halfBounds(PBounds pb, int pos) {
		double x = pb.getX();
		double y = pb.getY();
		double w = pb.getWidth();
		double h = pb.getHeight();
		if (pos == 0) {
			return new PBounds(pb);
		}
		if (pos == 1) {
			double nx = x;
			double ny = y;
			double nw = w;
			double nh = h / 2;
			return new PBounds(nx, ny, nw, nh);
		}
		if (pos == 2) {
			double nx = x;
			double ny = y + h / 4;
			double nw = w;
			double nh = h / 2;
			return new PBounds(nx, ny, nw, nh);
		}
		if (pos == 3) {
			double nx = x;
			double ny = y + h / 2;
			double nw = w;
			double nh = h / 2;
			return new PBounds(nx, ny, nw, nh);
		}
		return null;
	}

	/**
	 * �Y�[�������p�̗̈���v�Z����i���݂͖��g�p�j
	 * @param firstpb �ŏ��̗̈�
	 * @param panelpb �p�l���̗̈�
	 * @param cameraview �J�����̕\���̈�
	 * @param width ��
	 * @return �V�����̈�
	 */
	public static PBounds explainBounds(PBounds firstpb, PBounds panelpb,
			PBounds cameraview, double width) {
		double fpbw = firstpb.width;
		double zoom = width / fpbw;
		PBounds tempb = zoomBoundsX(firstpb, zoom);

		double preferredheight = width * cameraview.height / cameraview.width;

		double tx = tempb.x;
		double ty = tempb.y;
		double tw = tempb.width;
		double th = tempb.height;
		double px = panelpb.x;
		double py = panelpb.y;
		double pw = panelpb.width;
		double ph = panelpb.height;
		if (tx < px) {
			tx = px;
		}
		if (px + pw < tx + tw) {
			tx = (px + pw) - tw;
		}
		double tcy = ty + th / 2;
		ty = tcy - preferredheight / 2;
		th = preferredheight;
		if (ty < py) {
			ty = py;
		}
		if (py + ph < ty + th) {
			ty = (py + ph) - th;
		}
		return new PBounds(tx, ty, tw, th);
	}

	/**
	 * Check whether piece is completely included in large. True if not
	 * included.
	 * 
	 * @param large
	 * @param piece
	 * @return True if piece is not included in large
	 */
	public static boolean isHamidashi(PBounds large, PBounds piece) {
		// if (piece.x < large.x) return true;
		// if (piece.y < large.y) return true;
		// if (large.x+large.width < piece.x+piece.width) return true;
		// if (large.y+large.height < piece.y + piece.height) return true;
		// return false;
		return !large.contains(piece);
	}
}
