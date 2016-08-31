package jaist.css.covis.util;

import java.util.Random;
import java.util.StringTokenizer;

/**
 * String Utility Class
 * 
 * @author miuramo
 *
 */
public class StringUtil {
	public static String randomize(String src, String formatstr) {
		if (src == null)
			return src;
		if (formatstr != null) {
			// src からフォーマット文字列を取り除く（例： 1|: ... 位置1に，:を挿入）
			StringTokenizer st = new StringTokenizer(formatstr, "|");
			int insertpos = Integer.parseInt(st.nextToken());
			// String insertstr = st.nextToken();
			StringBuffer sb = new StringBuffer(src);
			sb.deleteCharAt(insertpos);
			src = sb.toString();
		}

		Random rand = new Random();
		int r = rand.nextInt(5);
		String ret = "";
		if (r < 3) {
			ret = src;
		} else if (r == 3) {
			ret = shuffle(src);
		} else if (r == 4) {
			ret = replace(src);
		}
		if (ret == null)
			return ret;

		// if (formatstr != null){
		// // src からフォーマット文字列を取り除く（例： 1|: ... 位置1に，:を挿入）
		// StringTokenizer st = new StringTokenizer(formatstr,"|");
		// int insertpos = Integer.parseInt(st.nextToken());
		// String insertstr = st.nextToken();
		// if (insertpos <= ret.length()){ // 長さが規定に満たなければ，追加しない
		// StringBuffer sb = new StringBuffer(ret);
		// sb.insert(insertpos,insertstr);
		// ret = sb.toString();
		// }
		// }
		return ret;
	}

	public static String shuffle(String src) {
		Random rand = new Random();
		if (src.length() <= 0)
			return src;
		int count = rand.nextInt(src.length());
		String res = null;
		for (int i = 0; i < count; i++) {
			res = StringUtil.rotate(src);
		}
		return res;
	}

	public static String rotate(String src) {
		StringBuffer sb = new StringBuffer();
		sb.append(src.substring(1));
		sb.append(src.charAt(0));
		return sb.toString();
	}

	public static String replace(String src) {
		Random rand = new Random();
		StringBuffer sb = new StringBuffer(src);
		if (sb.length() <= 0)
			return sb.toString();
		int s = rand.nextInt(sb.length());
		char c = sb.charAt(s);
		sb.replace(s, s + 1, String.valueOf((char) (c + 1)));
		return sb.toString();
	}
}
