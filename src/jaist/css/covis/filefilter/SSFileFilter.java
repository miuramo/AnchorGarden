package jaist.css.covis.filefilter;

import java.io.File;

/**
 * �ȑODB������܂��́C���ꂼ���PDA����ǂݍ��񂾕M�L�f�[�^��ShortStroke Log (sslog, *.ss) 
 * �܂��͂����GZIP�ň��k����*.ssg�t�@�C���Ƃ��ĕۑ�������C�Ăяo�����肵�Ă����D
 * 
 * ���̂Ƃ��̖��c��D
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
