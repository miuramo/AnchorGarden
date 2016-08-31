package jaist.css.covis.util;

/**
 * �S�p�𔼊p�ɕϊ�����N���X
 * 
 * @author miuramo
 *
 */
public class ZenkakuHankakuConverter {
	static final java.lang.String kana1 = "����������������������¯�������������������֬�������ܦݰ�ߤ�";

	static final java.lang.String kana2 = "�A�C�E�G�I�@�B�D�F�H�J�L�N�P�R�T�V�X�Z�\�^�`�c�b�e�g�i�j�k�l�m�n�q�t�w�z�}�~�����������������������������������[�J�K�A�B";

	// static final java.lang.String �Ђ炪�� =
	// "�����������������������������������������������ĂƂȂɂʂ˂̂͂Ђӂւق܂݂ނ߂�����������������[�J�K�A�B�������������������������Âłǃ��΂тԂׂڂς҂Ղ؂�";

	/**
	 * HTML�I�����񑀍�@�\ ���\�b�h�Ƃ��ĒP��@�\�ł���̂ŁA�Ƃ肠���������ɂ���B
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
	 * �Ƃ肠�������� �ʉӏ��ɂ���ׂ��@�\�ł��邪�B
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
	 * ���p�J�^�J�i��S�p�J�^�J�i�ɕϊ�������{�ꐳ�K�� 1�����P�ʁB
	 */
	public static char toKanaFull(char kana) {
		int index;

		if ((index = kana1.indexOf(kana)) >= 0) {
			kana = kana2.charAt(index);
		}

		return kana;
	}

	/**
	 * ���p�J�^�J�i��S�p�J�^�J�i�ɂ��A���_����������B ���{�ꐳ�K�� �g�p�� �X�֔ԍ����� JPRS RACE�h���C��
	 */
	public static java.lang.String toKanaFull(java.lang.String str) {
		StringBuffer str2;
		char kkv;
		str2 = new StringBuffer();
		for (int i = 0; i < str.length(); i++) {
			kkv = toKanaFull(str.charAt(i));
			if (kkv == '�J') {
				kkv = str2.charAt(str2.length() - 1);
				kkv++;
				str2.deleteCharAt(str2.length() - 1);
			} else if (kkv == '�K') {
				kkv = str2.charAt(str2.length() - 1);
				kkv += 2;
				str2.deleteCharAt(str2.length() - 1);
			}
			str2.append(kkv);

		}

		return str2.toString();
	}

	/**
	 * ���{�ꐳ�K�� �S�p/���p�J�^�J�i���Ђ炪�Ȃɂ���
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
	 * �Ђ炪�Ȃ�S�p�J�^�J�i�ɂ���
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

	static final java.lang.String �S�p�L�� = "�{�|���^���b�I�H�h�����������f�M�i�j�m�n�C�D�G�F�Q�����O";

	static final java.lang.String ���p�L�� = "+-*/=|!?\"#@$%&'`()[],.;:_<>^";

	/**
	 * �p������𔼊p�����ɐ��K������B �����S�ŁB �X�֔ԍ��A�d�b�ԍ��A���{��h���C����
	 */
	public static java.lang.String toHalf(java.lang.String str) {
		StringBuffer str2;
		str2 = new StringBuffer();
		char ch;
		int idx;
		for (int i = 0; i < str.length(); i++) {
			ch = str.charAt(i);
			if (ch >= '��' && ch <= '��') {
				ch += 'a' - '��';
			} else if (ch >= '�`' && ch <= '�y') {
				ch += 'A' - '�`';
			} else if (ch >= '�O' && ch <= '�X') {
				ch += '0' - '�O';
			} else if (ch == '�@') {
				ch = ' ';
			} else if ((idx = �S�p�L��.indexOf(ch)) >= 0) {
				ch = ���p�L��.charAt(idx);
			} else if (ch == '��') { // ���f�͔���
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
				ch += '��' - 'a';
			} else if (ch >= 'A' && ch <= 'Z') {
				ch += '�`' - 'A';
			} else if (ch >= '0' && ch <= '9') {
				ch += '�O' - '0';
			} else if (ch == ' ') {
				ch = '�@';
			} else if ((idx = ���p�L��.indexOf(ch)) >= 0) {
				ch = �S�p�L��.charAt(idx);
			} else if (ch == '��') { // ���f�͔���
				ch = '\\';
			}
			str2.append(ch);
		}
		return str2.toString();
	}

}
