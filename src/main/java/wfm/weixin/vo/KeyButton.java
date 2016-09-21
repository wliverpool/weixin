package wfm.weixin.vo;

/**
 * 微信click类型菜单按钮
 * 支持微信菜单按钮中一下几个事件类型的key值:
 * click：点击推事件、scancode_push：扫码推事件、scancode_push：扫码推事件
 * pic_sysphoto：弹出系统拍照发图、pic_photo_or_album：弹出拍照或者相册发图
 * pic_weixin：弹出微信相册发图器、location_select：弹出地理位置选择器
 * media_id：下发消息（除文本消息）、view_limited：跳转图文消息URL
 * @author 吴福明
 *
 */
public class KeyButton extends Button {

	private String key;//菜单按钮KEY值，用于消息接口推送，不超过128字节
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

}
