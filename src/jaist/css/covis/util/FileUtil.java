package jaist.css.covis.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * File Utility Class
 * 
 * @author miuramo
 *
 */
@Deprecated
public class FileUtil {
	
	/**
	 * �o�C�i���t�@�C�����o�C�g��Ƃ��ēǂݍ���
	 * @param path �t�@�C���̃p�X
	 * @return �o�C�i���z��f�[�^
	 */
	public byte[] path2byte(String path) {
		File f = new File(path);
		long fsize = f.length();

		ByteArrayOutputStream varyBuf = new ByteArrayOutputStream((int) fsize);
		InputStream in = null;
		int b;
		try {
			in = new BufferedInputStream(new FileInputStream(path));
			final int LS = 1024;
			byte buf[] = new byte[LS];

			while ((b = in.read(buf, 0, buf.length)) != -1) {
				varyBuf.write(buf, 0, b);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
		}
		return varyBuf.toByteArray();
	}

	public void byte2file(String path, byte[] b) {
	}

	public byte[] byte2gzip(byte[] source) {
		return null;
	}

	public byte[] byte2zip(byte[] source) {
		return null;
	}

}

// �ȉ��{�̕��̓ǂݍ���
// 45: String line;
// 46: // ByteArrayOutputStream�ɓǂݍ��񂾓��e���o�͂��Ă���
// 47: ByteArrayOutputStream receivedBuffer = new ByteArrayOutputStream();
// 48: while((size=in.readLine(buffer,0,buffer.length)) != -1){
// 49: line = new String(buffer,0, size);
// 50: if (line.indexOf(boundary)!=-1) break;
// 51: receivedBuffer.write(buffer,0,size);
// 52: }
// 53: in.close();
// 54:
// 55: // ��M�����f�[�^���t�@�C���ɏo��
// 56: FileOutputStream fout = new FileOutputStream(fileName);
// 57: byte buf[] = receivedBuffer.toByteArray();
// 58: // boundary�̑O�ɏo�͂�����s(CR+LF)������ăt�@�C���ɏo��
// 59: fout.write(buf, 0, buf.length-2);
// 60: fout.close();

