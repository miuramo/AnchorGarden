package jaist.css.covis.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 *  ファイル読み書きクラス
 *
 * @author miuramo
 *
 */
public class FileReadWriter {

	/** ファイルを読み込み，各行を配列に入れて返す
	 * @param fn ファイル名
	 * @return 文字列の配列（各行が要素）
	 */
	public static String[] getLines(String fn) {
		if (fn == null) return new String[]{""};
		ArrayList<String> list = new ArrayList<String>();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					new FileInputStream(fn)));
			String line = null;
			/* ファイル読み込み */
			while ((line = br.readLine()) != null) {
				list.add(line);
			}
			/* ファイルを閉じます */
			br.close();
		} catch (IOException err) {
			System.out.println("ReadError:" + fn);
		}
		return (String[]) list.toArray(new String[list.size()]);
	}

	/** 配列をファイルに書き込む 
	 * 
	 * @param fn ファイル名
	 * @param data 文字列の配列（各行が要素）
	 */
	public static void putLines(String fn, String data[]) {
		try {
			FileWriter filewriter = new FileWriter(fn, false);
			for (int i = 0; i < data.length; i++) {
				/* 改行文字追加 */
				data[i] = data[i] + "\n";
				/* 文字列を書き込みます */
				filewriter.write(data[i]);
			}
			/* ファイルを閉じます */
			filewriter.close();
		} catch (IOException e) {
			System.out.println("WriteError:" + fn);
		}
	}

	/** ArrayListをファイルに書き込む 
	 * 
	 * @param fn ファイル名
	 * @param list 文字列のArrayList（各要素が行となる）
	 */
	public static void putLines(String fn, ArrayList<String> list) {
		try {
			FileWriter filewriter = new FileWriter(fn, false);
			for (String s : list) {
				/* 文字列を書き込みます */
				filewriter.write(s + "\n");
			}
			/* ファイルを閉じます */
			filewriter.close();
		} catch (IOException e) {
			System.out.println("WriteError:" + fn);
		}
	}

	/**
	 *  文字列を分割して配列で返します

	 * @param delim 区切り文字
	 * @param text 分割したいテキスト
	 * @return 分割した文字列の配列
	 */
	public static String[] split(String delim, String text) {
		int index = -1;
		ArrayList<String> list = new ArrayList<String>();
		while ((index = text.indexOf(delim)) != -1) {
			list.add(text.substring(0, index));
			text = text.substring(index + delim.length());
		}
		list.add(text);
		String[] ret = (String[]) list.toArray(new String[list.size()]);
		return ret;
	}
}
