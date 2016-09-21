package wfm.weixin.vo.response;

/**
 * 微信视频文件
 * 视频（video）：10MB，支持MP4格式
 * @author 吴福明
 *
 */
public class Video {

	private String title;//视频的标题
	private String mediaId;//通过素材管理接口上传多媒体视频文件，得到的id
	private String description;//视频的描述
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getMediaId() {
		return mediaId;
	}
	
	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
}
