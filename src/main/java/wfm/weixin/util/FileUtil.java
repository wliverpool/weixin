package wfm.weixin.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtil {

	public static void generateFile(String filePath, byte[] data)throws IOException {
		FileOutputStream fos = null;
		if(null!=data&&data.length>0){
			try {
				File file = new File(filePath);
				fos = new FileOutputStream(file);
				fos.write(data);
			} catch (IOException e) {
				e.printStackTrace();
				throw e;
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

}
