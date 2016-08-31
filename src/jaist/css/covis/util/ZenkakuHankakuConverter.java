package jaist.css.covis.util;

/**
 * 全角を半角に変換するクラス
 * 
 * @author miuramo
 *
 */
public class ZenkakuHankakuConverter {
	static final java.lang.String kana1 = "ｱｲｳｴｵｧｨｩｪｫｶｷｸｹｺｻｼｽｾｿﾀﾁﾂｯﾃﾄﾅﾆﾇﾈﾉﾊﾋﾌﾍﾎﾏﾐﾑﾒﾓﾔﾕﾖｬｭｮﾗﾘﾙﾚﾛﾜｦﾝｰﾞﾟ､｡";

	static final java.lang.String kana2 = "アイウエオァィゥェォカキクケコサシスセソタチツッテトナニヌネノハヒフヘホマミムメモヤユヨャュョラリルレロワヲンー゛゜、。";

	// static final java.lang.String ひらがな =
	// "あいうえおぁぃぅぇぉかきくけこさしすせそたちつってとなにぬねのはひふへほまみむめもやゆよゃゅょらりるれろわをんー゛゜、。がぎぐげござじずぜぞだぢづでどヴばびぶべぼぱぴぷぺぽ";

	/**
	 * HTML的文字列操作機能 メソッドとして単一機能であるので、とりあえずここにある。
	 */
	public static java.lang.String toHtml(java.lang.String a) {
		StringBuffer sb = new StringBuffer();
		char ch;
		for (int i = 0; i < a.length(); i++) {
			ch = a.charAt(i);
			switch (ch) {
			case '<':
				sb.append("&lectureTable;");
				break;
			case '>':
				sb.append("&gt;");
				break;
			case '"':
				sb.append("&quot;");
				break;
			case '&':
				sb.append("&amp;");
				break;
			default:
				sb.append((char) ch);
				break;
			}
		}
		return sb.toString();
	}

	/**
	 * とりあえず判定 別箇所にあるべき機能であるが。
	 */
	public static boolean isEmail(java.lang.String email) {
		boolean ret = false;
		int at;
		if ((at = email.indexOf('@')) >= 0) {
			if (email.indexOf('.', at) >= at) {
				ret = true;
			}
		}
		return ret;
	}

	/**
	 * 半角カタカナを全角カタカナに変換する日本語正規化 1文字単位。
	 */
	public static char toKanaFull(char kana) {
		int index;

		if ((index = kana1.indexOf(kana)) >= 0) {
			kana = kana2.charAt(index);
		}

		return kana;
	}

	/**
	 * 半角カタカナを全角カタカナにし、濁点も統合する。 日本語正規化 使用例 郵便番号辞書 JPRS RACEドメイン
	 */
	public static java.lang.String toKanaFull(java.lang.String str) {
		StringBuffer str2;
		char kkv;
		str2 = new StringBuffer();
		for (int i = 0; i < str.length(); i++) {
			kkv = toKanaFull(str.charAt(i));
			if (kkv == '゛') {
				kkv = str2.charAt(str2.length() - 1);
				kkv++;
				str2.deleteCharAt(str2.length() - 1);
			} else if (kkv == '゜') {
				kkv = str2.charAt(str2.length() - 1);
				kkv += 2;
				str2.deleteCharAt(str2.length() - 1);
			}
			str2.append(kkv);

		}

		return str2.toString();
	}

	/**
	 * 日本語正規化 全角/半角カタカナをひらがなにする
	 */
	public static java.lang.String kanaUpper(java.lang.String str) {
		StringBuffer str2;
		str2 = new StringBuffer();
		char ch;
		str = toKanaFull(str);
		for (int i = 0; i < str.length(); i++) {
			ch = str.charAt(i);
			// idx = kana2.indexOf(ch);
			if (ch >= 0x30A0 && ch <= 0x30FA) {
				ch -= 0x60;
			}
			str2.append(ch);
		}
		return str2.toString();
	}

	/**
	 * ひらがなを全角カタカナにする
	 */
	public static java.lang.String kanaLower(java.lang.String str) {
		StringBuffer str2;
		// char[] str2;
		str2 = new StringBuffer();
		char ch;
		// str = kanaconv(str);
		for (int i = 0; i < str.length(); i++) {
			ch = str.charAt(i);
			// idx = kana2.indexOf(ch);
			if (ch >= 0x3040 && ch <= 0x309A) {
				ch += 0x60;
			}
			str2.append(ch);
		}
		return str2.toString();
	}

	static final java.lang.String 全角記号 = "＋－＊／＝｜！？”＃＠＄％＆’｀（）［］，．；：＿＜＞＾";

	static final java.lang.String 半角記号 = "+-*/=|!?\"#@$%&'`()[],.;:_<>^";

	/**
	 * 英数字列を半角文字に正規化する。 未完全版。 郵便番号、電話番号、日本語ドメイン等
	 */
	public static java.lang.String toHalf(java.lang.String str) {
		StringBuffer str2;
		str2 = new StringBuffer();
		char ch;
		int idx;
		for (int i = 0; i < str.length(); i++) {
			ch = str.charAt(i);
			if (ch >= 'ａ' && ch <= 'ｚ') {
				ch += 'a' - 'ａ';
			} else if (ch >= 'Ａ' && ch <= 'Ｚ') {
				ch += 'A' - 'Ａ';
			} else if (ch >= '０' && ch <= '９') {
				ch += '0' - '０';
			} else if (ch == '　') {
				ch = ' ';
			} else if ((idx = 全角記号.indexOf(ch)) >= 0) {
				ch = 半角記号.charAt(idx);
			} else if (ch == '￥') { // 判断は微妙
				ch = '\\';
			}
			str2.append(ch);
		}
		return str2.toString();
	}

	public static String toZenkaku(String str) {
		StringBuffer str2;
		str2 = new StringBuffer();
		char ch;
		int idx;
		for (int i = 0; i < str.length(); i++) {
			ch = str.charAt(i);
			if (ch >= 'a' && ch <= 'z') {
				ch += 'ａ' - 'a';
			} else if (ch >= 'A' && ch <= 'Z') {
				ch += 'Ａ' - 'A';
			} else if (ch >= '0' && ch <= '9') {
				ch += '０' - '0';
			} else if (ch == ' ') {
				ch = '　';
			} else if ((idx = 半角記号.indexOf(ch)) >= 0) {
				ch = 全角記号.charAt(idx);
			} else if (ch == '￥') { // 判断は微妙
				ch = '\\';
			}
			str2.append(ch);
		}
		return str2.toString();
	}

}
