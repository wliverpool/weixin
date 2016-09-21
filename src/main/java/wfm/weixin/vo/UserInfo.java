package wfm.weixin.vo;

/**
 * 微信用户基本信息
 * @author 吴福明
 *
 */
public class UserInfo {
	
	private String openid;//用户的标识，对当前公众号唯一
	private String subscribe;//用户是否订阅该公众号标识，值为0时，代表此用户没有关注该公众号，拉取不到其余信息。
	private String nickname;//用户的昵称
	private String headimgurl;//用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空。若用户更换头像，原有头像URL将失效。
	
	public String getOpenid() {
		return openid;
	}
	
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	
	public String getSubscribe() {
		return subscribe;
	}
	
	public void setSubscribe(String subscribe) {
		this.subscribe = subscribe;
	}
	
	public String getNickname() {
		return nickname;
	}
	
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	public String getHeadimgurl() {
		return headimgurl;
	}
	
	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}
	

}
