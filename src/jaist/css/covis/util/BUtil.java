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
	 * 面積を返す
	 * @param pagemanager 領域
	 * @return 面積
	 */
	public static int getArea(PBounds pb) {
		double w = pb.getWidth();
		double h = pb.getHeight();
		return (int) (w * h);
	}

	/**
	 * 引数pbで与えた領域を，引数rateだけ拡大した新しい領域を返す
	 * @param pagemanager 領域
	 * @param rate ズーム率
	 * @return 新しい領域
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
	 * 引数pbで与えた領域を，引数rateだけ拡大した新しい領域を返す（ただしX方向のみ）
	 * @param pagemanager 領域
	 * @param rate ズーム率
	 * @return 新しい領域
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
	 * 引数pbで与えた領域に，指定した幅と高さを加えた新しい領域を返す
	 * @param pagemanager 領域
	 * @param plusx 横方向追加分
	 * @param plusy 縦方向追加分
	 * @return 新しい領域
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
	 * pos が 1から3のときは，指定した領域を縦の長さを半分にした領域を計算し，それが元の領域の上か，中心か，下に移動する．
	 * pos が0のときは，領域をそのまま返す
	 * @param pagemanager 領域
	 * @param pos 場所
	 * @return 新しい領域
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
	 * ズーム説明用の領域を計算する（現在は未使用）
	 * @param firstpb 最初の領域
	 * @param panelpb パネルの領域
	 * @param cameraview カメラの表示領域
	 * @param width 幅
	 * @return 新しい領域
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
