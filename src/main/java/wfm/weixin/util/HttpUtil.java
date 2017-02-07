package wfm.weixin.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import net.sf.json.JSONObject;

/**
 * http请求的工具类
 * 
 * @author 吴福明
 *
 */
public class HttpUtil {

	public static byte[] getResponseByHttpGet(String url) {
		// 创建默认的httpClient实例
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpEntity entity = null;
		ByteArrayOutputStream out = null;
		try {
			// 用get方法发送http请求
			HttpGet get = new HttpGet(url);
			System.out.println("执行get请求:...." + get.getURI());
			CloseableHttpResponse httpResponse = null;
			// 发送get请求
			httpResponse = httpClient.execute(get);
			try {
				// response实体
				entity = httpResponse.getEntity();
				InputStream in = entity.getContent();
				out = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024 * 4];
				int n = 0;
				while ((n = in.read(buffer)) != -1) {
					out.write(buffer, 0, n);
				}
				return out.toByteArray();
			} finally {
				httpResponse.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (httpClient != null) {
					httpClient.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return null;
	}

	/**
	 * 从URL下载文件
	 * @param url    远程url
	 * @param filePath  文件存放路径
	 * @return
	 */
	public static boolean downloadFileFromUrl(String url,String filePath) {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(url);
		InputStream in = null;
		FileOutputStream out = null;
		try {
			CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
			// 获取response的返回头信息
			Header contentHead = httpResponse.getFirstHeader("Content-Disposition");
			// 开始解析文件头信息，这里使用的是HeaderElement对象作为文件头的基础信息
			HeaderElement[] elements = contentHead.getElements();
			String filerName = null;
			for (HeaderElement el : elements) {
				// 遍历，获取filename。filename信息对应的就是下载文件的文件名称。
				NameValuePair pair = el.getParameterByName("filename");
				filerName = pair.getValue();
			}
			// 由于这个地址是下载地址，返回的是字节流信息。所有这里直接将返回的body转换成字节流
			in = httpResponse.getEntity().getContent();
			out = new FileOutputStream(new File(filePath + filerName));
			byte[] b = new byte[1024];
			int len = 0;
			byte[] content = new byte[0];
			int length = 0;
			// 写文件，这里将所有的内容都缓存到内存中，最后一次性写入
			while ((len = in.read(b)) > 0) {
				// out.write(b,0,len);
				length = content.length;
				content = Arrays.copyOf(content, length + len);// 扩容
				System.arraycopy(b, 0, content, length, len);// 将第二个数组与第一个数组合并
			}
			out.write(content, 0, content.length);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (httpClient != null) {
					httpClient.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			if (out != null){
				try {
					out.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}	
		}
		return false;
	}

	/**
	 * 通过http的get方法请求http地址
	 * 
	 * @param url
	 * @return 获取请求地址的json返回格式
	 */
	public static JSONObject getJsonResponseByHttpGet(String url) {
		// 创建默认的httpClient实例
		CloseableHttpClient httpClient = HttpClients.createDefault();
		JSONObject jsonObject = null;
		try {
			// 用get方法发送http请求
			HttpGet get = new HttpGet(url);
			System.out.println("执行get请求:...." + get.getURI());
			CloseableHttpResponse httpResponse = null;
			// 发送get请求
			httpResponse = httpClient.execute(get);
			try {
				// response实体
				HttpEntity entity = httpResponse.getEntity();
				if (null != entity) {
					String result = EntityUtils.toString(entity, "UTF-8");
					jsonObject = JSONObject.fromObject(result);
				}
			} finally {
				httpResponse.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (httpClient != null) {
					httpClient.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return jsonObject;

	}

	/**
	 * 通过http的POST方法请求http地址
	 * 
	 * @param url
	 * @param outStr
	 * @return 获取请求地址的json返回格式
	 */
	public static JSONObject getJsonResponseByHttpPost(String url, String outStr) {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		JSONObject jsonObject = null;
		try {
			HttpPost post = new HttpPost(url);
			// 创建参数列表
			// List<NameValuePair> list = new ArrayList<NameValuePair>();
			// list.add(new BasicNameValuePair("j_username", "admin"));
			// list.add(new BasicNameValuePair("j_password", "admin"));
			// url格式编码
			// UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(list,
			// "UTF-8");
			// post.setEntity(uefEntity);
			post.setEntity(new StringEntity(outStr, "UTF-8"));
			System.out.println("POST 请求...." + post.getURI());
			// 执行请求
			CloseableHttpResponse httpResponse = httpClient.execute(post);
			try {
				HttpEntity entity = httpResponse.getEntity();
				if (null != entity) {
					String result = EntityUtils.toString(entity, "UTF-8");
					jsonObject = JSONObject.fromObject(result);
				}
			} finally {
				httpResponse.close();
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (httpClient != null) {
					httpClient.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return jsonObject;
	}

	/**
	 * 用http的方式上传文件
	 * 
	 * @param filePath
	 *            素材文件路径
	 * @param url
	 *            上传地址
	 * @return 媒体文件上传后，获取的唯一标识
	 * @throws FileNotFoundException
	 */
	public static String uploadFileByHttp(String filePath, String url) throws FileNotFoundException {

		File file = new File(filePath);
		if (!file.exists() || !file.isFile()) {
			throw new FileNotFoundException("文件不存在");
		}
		// 设置http链接
		URL urlObj = null;
		OutputStream out = null;
		DataInputStream in = null;
		BufferedReader reader = null;
		String result = null;
		try {
			urlObj = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Charset", "UTF-8");
			String boundary = "---------7d4a6d158c9";
			conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

			StringBuffer sb = new StringBuffer();
			sb.append("--");
			sb.append(boundary);
			sb.append("\r\n");
			sb.append("Content-Disposition:form-data;name=\"file\";filename=\"" + file.getName() + "\"\r\n");
			sb.append("Content-Type:application/octet-stream\r\n\r\n");

			byte[] head = sb.toString().getBytes("UTF-8");
			// 获得上传输出流
			out = new DataOutputStream(conn.getOutputStream());
			// 输出http头信息
			out.write(head);
			// 文件上传
			in = new DataInputStream(new FileInputStream(file));
			int bytes = 0;
			byte[] bufferOut = new byte[1024];
			while ((bytes = in.read(bufferOut)) != -1) {
				// 输出读取到的文件流数据
				out.write(bufferOut, 0, bytes);
			}
			// 输出尾部信息
			byte[] foot = ("\r\n").getBytes("UTF-8");
			out.write(foot);

			byte[] end_data = ("\r\n--" + boundary + "--\r\n").getBytes();
			out.write(end_data);
			out.flush();

			StringBuffer buffer = new StringBuffer();
			// 读取上传响应结果
			reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
			result = buffer.toString();
		} catch (IOException e) {
			// TODO: handle exception
		} finally {
			if (null != in) {
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (null != out) {
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (null != reader) {
				try {
					reader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return result;
	}

}
