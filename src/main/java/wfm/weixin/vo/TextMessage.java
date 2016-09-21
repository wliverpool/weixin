package wfm.weixin.vo;

/**
 * 微信文本消息类
 * @author 吴福明
 *
 */
public class TextMessage extends Message{
	
	private String msgId;//消息id
	private String content;//文本消息内容
	
	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}

}
