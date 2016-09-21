package wfm.weixin.vo.response;

/**
 * 微信图片文件
 * 图片（image）: 1M，支持JPG格式
 * @author 吴福明
 *
 */
public class Image {
	
	private String mediaId;//通过素材管理接口上传多媒体文件，得到的id。

	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

}
