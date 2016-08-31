package jaist.css.covis;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


public class ZipUtil {

	public static byte[] read(ZipFile zfile, ZipEntry zent){
		BufferedInputStream in;
		ByteArrayOutputStream varyBuf = new ByteArrayOutputStream();
		final int LS = 1024;
		int b;
		try {
			in = new BufferedInputStream(zfile.getInputStream(zent));
			byte buf[] = new byte[LS];
			while((b = in.read(buf, 0, buf.length)) != -1 ) {
				varyBuf.write(buf,0,b) ;
			}
			varyBuf.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return varyBuf.toByteArray();
	}
}
