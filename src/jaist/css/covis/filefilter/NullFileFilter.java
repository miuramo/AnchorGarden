package jaist.css.covis.filefilter;

import java.io.File;

/**
 * Javaのファイルダイアログ（JFileChooser）で，特定の拡張子のファイルのみを
 * 選んで表示したいときに，用いる．
 * 
 * 拡張子部分をコンストラクタで指定できるため，現在も使用中．
 * 
 * @author miuramo
 *
 */
public class NullFileFilter extends javax.swing.filechooser.FileFilter implements
		java.io.FilenameFilter {
	String suffix;

	/**
	 * コンストラクタ
	 * @param s 拡張子の文字列(例，txt)
	 */
	public NullFileFilter(String s) {
		suffix = s;
	}

	public boolean accept(File dir) {
		String name = dir.getName();
		if (name.endsWith(suffix) || dir.isDirectory()) {
			return true;
		} else {
			return false;
		}
	}

	public boolean accept(File dir, String name) {
		if (name.endsWith(suffix)) {
			return true;
		} else {
			return false;
		}
	}

	public String getDescription() {
		return suffix;
	}
}
