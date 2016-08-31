package jaist.css.covis.filefilter;

import java.io.File;

/**
 * 以前DB化するまえは，それぞれのPDAから読み込んだ筆記データをShortStroke Log (sslog, *.ss) 
 * またはそれをGZIPで圧縮した*.ssgファイルとして保存したり，呼び出したりしていた．
 * 
 * そのときの名残り．
 * 
 * @author miuramo
 *
 */
@Deprecated
public class SSFileFilter extends javax.swing.filechooser.FileFilter implements
		java.io.FilenameFilter {
	String suffix;

	public SSFileFilter(String s) {
		suffix = s;
	}

	public boolean accept(File dir) {
		String name = dir.getName();
		if (name.endsWith(suffix) || name.endsWith(".ssg") || dir.isDirectory()) {
			return true;
		} else {
			return false;
		}
	}

	public boolean accept(File dir, String name) {
		if (name.endsWith(suffix) || name.endsWith(".ssg")) {
			return true;
		} else {
			return false;
		}
	}

	public String getDescription() {
		return "ss log (*.ss, *.ssg)";
	}
}
