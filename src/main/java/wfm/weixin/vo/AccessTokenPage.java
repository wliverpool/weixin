package wfm.weixin.vo;

/**
 * 微信网页的access token凭证类
 * @author 吴福明
 *
 */
public class AccessTokenPage {
	
	private String access_token;// 获取到的凭证
	private long expires_in;// 凭证有效时间，单位：秒
	private long createTimestamp;// 创建时间时间戳,单位秒
	private String refresh_token;//用户刷新access_token
	private String scope;//用户授权的作用域
	private String unionid;//只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段
	private String openid;//	用户唯一标识，请注意，在未关注公众号时，用户访问公众号的网页，也会产生一个用户和公众号唯一的OpenID
	
	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getAccess_token() {
		return access_token;
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
	
	public long getCreateTimestamp() {
		return createTimestamp;
	}
	
	public void setCreateTimestamp(long createTimestamp) {
		this.createTimestamp = createTimestamp;
	}
	
	public String getRefresh_token() {
		return refresh_token;
	}
	
	public void setRefresh_token(String refresh_token) {
		this.refresh_token = refresh_token;
	}
	
	public String getScope() {
		return scope;
	}
	
	public void setScope(String scope) {
		this.scope = scope;
	}
	
	public String getUnionid() {
		return unionid;
	}
	
	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}

}
