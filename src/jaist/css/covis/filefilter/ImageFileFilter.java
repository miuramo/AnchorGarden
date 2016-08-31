package jaist.css.covis.filefilter;

import java.io.File;

/**
 * �ȑO�́CBookEditor�ƌĂ΂��y�[�W�ҏW�p�̓��ʂ̃v���O�������N�����Ȃ���
 * �y�[�W�̃}�[�N��ҏW�ł��Ȃ������D
 * BookEditor�ŉ摜�t�@�C����ǂݍ���Ŕw�i�摜�Ƃ���Ƃ��Ɏg�p����Ă����D
 * 
 * ���݂́C����PageBrowser�ɉ摜�t�@�C�����h���b�O���h���b�v���邾���Ńy�[�W�摜�̐ݒ�
 * ���ł���̂ŁC�s�v�D
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
