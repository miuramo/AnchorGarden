package jaist.css.covis.filefilter;

import java.io.File;

/**
 * ブックファイル用フィルタ
 * 
 * 以前は，複数のページデータを集めて，Bookファイルを１つ読み込むだけですむようにしていた．
 * 現在は，ページデータは単体でファイルに保存しておき，組み合わせて並び替えたりしながら読み込める
 * ようにした．
 * @author miuramo
 *
 */

@Deprecated
public class BookFileFilter extends javax.swing.filechooser.FileFilter implements
		java.io.FilenameFilter {
	String suffix;

	public BookFileFilter(String s) {
		suffix = s;
	}

	public boolean accept(File dir) {
		// if (dir.isDirectory()) return true;
		String name = dir.getName();
		if (name.endsWith(suffix) || name.endsWith(".book")
				|| dir.isDirectory()) {
			return true;
		} else {
			return false;
		}
	}

	public boolean accept(File dir, String name) {
		// if (dir.isDirectory()) return true;
		if (name.endsWith(suffix) || name.endsWith(".book")
				|| dir.isDirectory()) {
			return true;
		} else {
			return false;
		}
	}

	public String getDescription() {
		return "book file (*.book)";
	}
}
