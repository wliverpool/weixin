package wfm.weixin.vo.response;

/**
 * 微信语音文件
 * 语音（voice）：2M，播放长度不超过60s，支持AMR\MP3格式
 * @author 吴福明
 *
 */
public class Voice {
	
	private String mediaId;//通过素材管理接口上传多媒体语音文件，得到的id

	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

}
