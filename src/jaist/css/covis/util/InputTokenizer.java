package jaist.css.covis.util;

import java.util.ArrayList;

/**
 * InputTokenizer
 * 
 * ���{��C�p��̕��͂���؂��� Card�i���x���j�ɔz�u���₷�����邽�߂� �N���X
 * 
 * MeCab(���{��\����͊�)���C���X�g�[������Ă��āC���{�ꕶ�͂Ȃ� mecabTokenize() �����łȂ���� tokenize()���g���悤��
 * ����Ƃ悢�D(33-37�s��)
 * 
 * @author miuramo
 * 
 */
public class InputTokenizer {
	String orig;

	ArrayList<String> tokens;

	// static Vector<Character.UnicodeBlock> distinguish;
	// static {
	// distinguish.add(Character.UnicodeBlock)
	// }

	public InputTokenizer(String src) {
		orig = src.trim(); // �O��̋󔒂͎�菜�����
		tokens = new ArrayList<String>();

		tokenize();
		/*
		 * if (orig.indexOf(" ")>0) tokenize(); // �󔒂�����̂͂����炭�p�ꂾ�낤��tokenize()
		 * else mecabTokenize();
		 */
	}

	public void tokenize() {
		char[] charary;

		charary = orig.toCharArray();
		Character.UnicodeBlock currentB = null, prevB = null;
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < charary.length; i++) {
			currentB = Character.UnicodeBlock.of(charary[i]);
			if (charary[i] == ' ') {
				// ��U��؂�
				if (sb.length() > 0)
					tokens.add(sb.toString());
				sb = new StringBuffer();
				// �����āC�������̂��W�߂�
				sb.append(charary[i]);
				i++;
				while (charary[i] == ' ') {
					sb.append(charary[i]);
					i++;
				}
				currentB = Character.UnicodeBlock.of(charary[i]);
				// �W�܂�����C����
				if (sb.length() > 0)
					tokens.add(sb.toString());
				sb = new StringBuffer();
				sb.append(charary[i]);
			} else if (currentB == prevB
					&& currentB != Character.UnicodeBlock.HIRAGANA) {
				sb.append(charary[i]);
			} else {
				if (sb.length() > 0)
					tokens.add(sb.toString());
				sb = new StringBuffer();
				sb.append(charary[i]);
			}
			prevB = currentB;
		}
		tokens.add(sb.toString());
	}

	public void print() {
		for (String s : tokens) {
			System.out.println("[" + s + "]");
		}
	}

//	public Vector<Token> getTokens() {
//		Vector<Token> tok = new Vector<Token>();
//		for (String s : tokens) {
//			tok.add(new Token(s));
//		}
//		return tok;
//	}

//	public static void main(String[] args) {
//		BufferedReader stdin = new BufferedReader(new InputStreamReader(
//				System.in));
//		String strnum;
//		try {
//			while ((strnum = stdin.readLine()) != null) {
//				InputTokenizer it = new InputTokenizer(strnum);
//				it.print();
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
}
