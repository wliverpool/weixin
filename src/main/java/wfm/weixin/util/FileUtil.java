package wfm.weixin.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtil {

	public static void generateFile(String filePath, byte[] data) {
		FileOutputStream fos = null;
		try {
			File file = new File(filePath);
			fos = new FileOutputStream(file);
			fos.write(data);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != fos) {
				try {
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

}
