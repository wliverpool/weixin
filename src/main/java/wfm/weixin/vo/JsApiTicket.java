package wfm.weixin.vo;

import net.sf.json.JSONObject;
import wfm.weixin.business.service.WeixinService;
import wfm.weixin.util.HttpUtil;

/**
 * 微信js接口的ticket凭证类
 * 
 * @author 吴福明
 *
 */
public class JsApiTicket {
	
	private static final JsApiTicket JS_API_TICKET = new JsApiTicket();  

	private String ticket;// 获取到的凭证
	private long expires_in;// 凭证有效时间，单位：秒
	private long createTimestamp;// 创建时间时间戳,单位秒

	private JsApiTicket() {

	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public static JsApiTicket getInstance() {
		long current = System.currentTimeMillis()/1000;
		//当ticket为空或者超时时从微信服务器端获取ticket
		if(null==JS_API_TICKET.getTicket()||"".equals(JS_API_TICKET.getTicket())||((JS_API_TICKET.getCreateTimestamp()+JS_API_TICKET.getExpires_in())<current)){
			JS_API_TICKET.getAccessTokenByHttpGet();
		}
		return JS_API_TICKET;
	}

	public long getCreateTimestamp() {
		return createTimestamp;
	}

	public void setCreateTimestamp(long createTimestamp) {
		this.createTimestamp = createTimestamp;
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
		System.out.println("====================通过http的get方式获取jsapiticket======================");
		AccessToken accessToken = AccessToken.getInstance();
		String url = WeixinService.GET_TICKET.replace("ACCESS_TOKEN", accessToken.getAccess_token());
		JSONObject resultJsonObject = HttpUtil.getJsonResponseByHttpGet(url);
		if(null!=resultJsonObject){
			if(null!=resultJsonObject.get("ticket")){
				setTicket(resultJsonObject.getString("ticket"));
				setExpires_in(resultJsonObject.getLong("expires_in"));
				setCreateTimestamp(System.currentTimeMillis() / 1000);
			}
		}
	}

}
