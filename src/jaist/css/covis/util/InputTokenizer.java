package jaist.css.covis.util;

import java.util.ArrayList;

/**
 * InputTokenizer
 * 
 * 日本語，英語の文章を区切って Card（ラベル）に配置しやすくするための クラス
 * 
 * MeCab(日本語構文解析器)がインストールされていて，日本語文章なら mecabTokenize() そうでなければ tokenize()を使うように
 * するとよい．(33-37行目)
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
		orig = src.trim(); // 前後の空白は取り除かれる
		tokens = new ArrayList<String>();

		tokenize();
		/*
		 * if (orig.indexOf(" ")>0) tokenize(); // 空白があるのはおそらく英語だろう＞tokenize()
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
				// 一旦区切る
				if (sb.length() > 0)
					tokens.add(sb.toString());
				sb = new StringBuffer();
				// そして，同じものを集める
				sb.append(charary[i]);
				i++;
				while (charary[i] == ' ') {
					sb.append(charary[i]);
					i++;
				}
				currentB = Character.UnicodeBlock.of(charary[i]);
				// 集まったら，次へ
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
