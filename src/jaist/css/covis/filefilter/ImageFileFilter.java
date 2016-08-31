package jaist.css.covis.filefilter;

import java.io.File;

/**
 * 以前は，BookEditorと呼ばれるページ編集用の特別のプログラムを起動しないと
 * ページのマークを編集できなかった．
 * BookEditorで画像ファイルを読み込んで背景画像とするときに使用されていた．
 * 
 * 現在は，直接PageBrowserに画像ファイルをドラッグ＆ドロップするだけでページ画像の設定
 * ができるので，不要．
 * 
 * @author miuramo
 *
 */
@Deprecated
public class ImageFileFilter extends javax.swing.filechooser.FileFilter implements
		java.io.FilenameFilter {
	String suffix;

	public ImageFileFilter(String s) {
		suffix = s;
	}

	public boolean accept(File dir) {
		// if (dir.isDirectory()) return true;
		String name = dir.getName();
		if (name.endsWith(suffix) || name.endsWith(".png")
				|| name.endsWith(".jpg") || name.endsWith(".gif")
				|| dir.isDirectory()) {
			return true;
		} else {
			return false;
		}
	}

	public boolean accept(File dir, String name) {
		// if (dir.isDirectory()) return true;
		if (name.endsWith(suffix) || name.endsWith(".png")
				|| name.endsWith(".jpg") || name.endsWith(".gif")
				|| dir.isDirectory()) {
			return true;
		} else {
			return false;
		}
	}

	public String getDescription() {
		return "image file (*.png, *.jpg, *.gif)";
	}
}
