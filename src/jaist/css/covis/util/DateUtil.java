package jaist.css.covis.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Date Utility Class
 * 
 * @author miuramo
 *
 */
public class DateUtil {
	/**
	 * Longで示された時刻を，フォーマットした文字列を返す
	 * @param time Longで示された時刻
	 * @return フォーマットした文字列
	 */
	public static String convert(long time) {
		Date d = new Date(time);
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		return df.format(d);
	}
}
