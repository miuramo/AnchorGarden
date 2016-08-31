package jaist.css.covis.filefilter;

import java.io.File;

/**
 * �u�b�N�t�@�C���p�t�B���^
 * 
 * �ȑO�́C�����̃y�[�W�f�[�^���W�߂āCBook�t�@�C�����P�ǂݍ��ނ����ł��ނ悤�ɂ��Ă����D
 * ���݂́C�y�[�W�f�[�^�͒P�̂Ńt�@�C���ɕۑ����Ă����C�g�ݍ��킹�ĕ��ёւ����肵�Ȃ���ǂݍ��߂�
 * �悤�ɂ����D
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
