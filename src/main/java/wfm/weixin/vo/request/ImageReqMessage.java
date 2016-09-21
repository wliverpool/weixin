package wfm.weixin.vo.request;

import wfm.weixin.vo.Message;

/**
 * 从微信接收到的图片消息
 * @author 吴福明
 *
 */
public class ImageReqMessage extends Message {
	
	private String mediaId;//图片消息媒体id，可以调用多媒体文件下载接口拉取数据。
	private String picUrl;//图片链接
	private String msgId;//消息id，64位整型
	
	public String getMediaId() {
		return mediaId;
	}
	
	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}
	
	public String getPicUrl() {
		return picUrl;
	}
	
	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
	
	public String getMsgId() {
		return msgId;
	}
	
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

}
