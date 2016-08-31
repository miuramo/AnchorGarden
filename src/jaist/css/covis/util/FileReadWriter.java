package jaist.css.covis.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 *  �t�@�C���ǂݏ����N���X
 *
 * @author miuramo
 *
 */
public class FileReadWriter {

	/** �t�@�C����ǂݍ��݁C�e�s��z��ɓ���ĕԂ�
	 * @param fn �t�@�C����
	 * @return ������̔z��i�e�s���v�f�j
	 */
	public static String[] getLines(String fn) {
		if (fn == null) return new String[]{""};
		ArrayList<String> list = new ArrayList<String>();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					new FileInputStream(fn)));
			String line = null;
			/* �t�@�C���ǂݍ��� */
			while ((line = br.readLine()) != null) {
				list.add(line);
			}
			/* �t�@�C������܂� */
			br.close();
		} catch (IOException err) {
			System.out.println("ReadError:" + fn);
		}
		return (String[]) list.toArray(new String[list.size()]);
	}

	/** �z����t�@�C���ɏ������� 
	 * 
	 * @param fn �t�@�C����
	 * @param data ������̔z��i�e�s���v�f�j
	 */
	public static void putLines(String fn, String data[]) {
		try {
			FileWriter filewriter = new FileWriter(fn, false);
			for (int i = 0; i < data.length; i++) {
				/* ���s�����ǉ� */
				data[i] = data[i] + "\n";
				/* ��������������݂܂� */
				filewriter.write(data[i]);
			}
			/* �t�@�C������܂� */
			filewriter.close();
		} catch (IOException e) {
			System.out.println("WriteError:" + fn);
		}
	}

	/** ArrayList���t�@�C���ɏ������� 
	 * 
	 * @param fn �t�@�C����
	 * @param list �������ArrayList�i�e�v�f���s�ƂȂ�j
	 */
	public static void putLines(String fn, ArrayList<String> list) {
		try {
			FileWriter filewriter = new FileWriter(fn, false);
			for (String s : list) {
				/* ��������������݂܂� */
				filewriter.write(s + "\n");
			}
			/* �t�@�C������܂� */
			filewriter.close();
		} catch (IOException e) {
			System.out.println("WriteError:" + fn);
		}
	}

	/**
	 *  ������𕪊����Ĕz��ŕԂ��܂�

	 * @param delim ��؂蕶��
	 * @param text �����������e�L�X�g
	 * @return ��������������̔z��
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
