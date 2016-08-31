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
	 * Long�Ŏ����ꂽ�������C�t�H�[�}�b�g�����������Ԃ�
	 * @param time Long�Ŏ����ꂽ����
	 * @return �t�H�[�}�b�g����������
	 */
	public static String convert(long time) {
		Date d = new Date(time);
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		return df.format(d);
	}
}
