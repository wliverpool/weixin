package wfm.weixin.vo;

/**
 * 微信回复消息的基类
 * 
 * @author 吴福明
 *
 */
public abstract class Message {

	private String toUserName;// 开发者微信号
	private String fromUserName;// 发送方帐号（一个OpenID）
	private String createTime;// 消息创建时间 （整型）
	private String msgType;// text

	public String getToUserName() {
		return toUserName;
	}

	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}

	public String getFromUserName() {
		return fromUserName;
	}

	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

}
