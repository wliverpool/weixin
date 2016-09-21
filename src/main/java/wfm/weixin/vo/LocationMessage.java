package wfm.weixin.vo;

/**
 * 微信地理位置消息
 * 
 * @author 吴福明
 *
 */
public class LocationMessage extends Message {
	
	private String msgId;//消息id
	private String locationX;//地理经度坐标
	private String locationY;//地理纬度坐标
	private String label;//地名
	private String scale;//范围
	
	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
	
	public String getLocationX() {
		return locationX;
	}
	
	public void setLocationX(String locationX) {
		this.locationX = locationX;
	}
	
	public String getLocationY() {
		return locationY;
	}
	
	public void setLocationY(String locationY) {
		this.locationY = locationY;
	}
	
	public String getLabel() {
		return label;
	}
	
	public void setLabel(String label) {
		this.label = label;
	}
	
	public String getScale() {
		return scale;
	}
	
	public void setScale(String scale) {
		this.scale = scale;
	}

}
