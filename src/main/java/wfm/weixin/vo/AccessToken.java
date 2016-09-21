package wfm.weixin.vo;

import net.sf.json.JSONObject;
import wfm.weixin.business.service.WeixinService;
import wfm.weixin.util.HttpUtil;

/**
 * 微信接口的access token凭证类
 * 
 * @author 吴福明
 *
 */
public class AccessToken {

	private static final AccessToken ACCESS_TOKEN = new AccessToken();  

	private String access_token;// 获取到的凭证
	private long expires_in;// 凭证有效时间，单位：秒
	private long createTimestamp;// 创建时间时间戳,单位秒

	private AccessToken() {

	}

	public static AccessToken getInstance() {
		long current = System.currentTimeMillis()/1000;
		//当access token为空或者超时时从微信服务器端获取access token
		if(null==ACCESS_TOKEN.getAccess_token()||"".equals(ACCESS_TOKEN.getAccess_token())||((ACCESS_TOKEN.getCreateTimestamp()+ACCESS_TOKEN.getExpires_in())<current)){
			ACCESS_TOKEN.getAccessTokenByHttpGet();
		}
		return ACCESS_TOKEN;
	}

	public String getAccess_token() {
		return access_token;
	}

	public long getCreateTimestamp() {
		return createTimestamp;
	}

	public void setCreateTimestamp(long createTimestamp) {
		this.createTimestamp = createTimestamp;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public long getExpires_in() {
		return expires_in;
	}

	public void setExpires_in(long expires_in) {
		this.expires_in = expires_in;
	}

	/**
	 * 通过http的get请求获取AccessToken
	 */
	public void getAccessTokenByHttpGet() {
		System.out.println("====================通过http的get方式获取access_token======================");
		// 设置请求的地址和参数
		String url = WeixinService.ACCESS_TOKEN_URL.replace("APPID", WeixinService.APPID).replace("APPSECRET",WeixinService.APPSECRET);
		JSONObject jsonObject = HttpUtil.getJsonResponseByHttpGet(url);
		if (null != jsonObject) {
			if(null!=jsonObject.getString("access_token")&&!"".equals(jsonObject.getString("access_token"))){
				setAccess_token(jsonObject.getString("access_token"));
				setExpires_in(jsonObject.getLong("expires_in"));
				setCreateTimestamp(System.currentTimeMillis() / 1000);
			}
			
		}
	}

}
