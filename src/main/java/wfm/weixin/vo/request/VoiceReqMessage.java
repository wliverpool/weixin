package wfm.weixin.vo.request;

import wfm.weixin.vo.Message;

/**
 * 从微信接收到的语音消息
 * @author 吴福明
 *
 */
public class VoiceReqMessage extends Message {
	
	private String format;//语音格式：amr
	private String mediaId;//语音消息媒体id，可以调用多媒体文件下载接口拉取该媒体
	private String msgId;//消息id，64位整型
	private String recognition;//语音识别结果，UTF8编码
	
	public String getFormat() {
		return format;
	}
	
	public void setFormat(String format) {
		this.format = format;
	}
	
	public String getMediaId() {
		return mediaId;
	}
	
	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}
	
	public String getMsgId() {
		return msgId;
	}
	
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
	
	public String getRecognition() {
		return recognition;
	}
	
	public void setRecognition(String recognition) {
		this.recognition = recognition;
	}

}
