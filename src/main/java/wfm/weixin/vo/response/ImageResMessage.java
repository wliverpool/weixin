package wfm.weixin.vo.response;

import wfm.weixin.vo.Message;

/**
 * 用于回复的微信图片消息
 * 
 * @author 吴福明
 *
 */
public class ImageResMessage extends Message{
	
	private Image image;//图片文件

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

}
