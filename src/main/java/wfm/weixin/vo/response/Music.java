package wfm.weixin.vo.response;

/**
 * 微信音乐文件
 * 缩略图（thumb）：64KB，支持JPG格式
 * @author 吴福明
 *
 */
public class Music {
	
	private String title;//音乐标题
	private String description;//音乐描述
	private String musicUrl;//音乐链接
	private String hQMusicUrl;//高质量音乐链接，WIFI环境优先使用该链接播放音乐
	private String thumbMediaId;//缩略图的媒体id，通过素材管理接口上传多媒体文件，得到的id
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getMusicUrl() {
		return musicUrl;
	}
	
	public void setMusicUrl(String musicUrl) {
		this.musicUrl = musicUrl;
	}
	
	public String gethQMusicUrl() {
		return hQMusicUrl;
	}
	
	public void sethQMusicUrl(String hQMusicUrl) {
		this.hQMusicUrl = hQMusicUrl;
	}
	
	public String getThumbMediaId() {
		return thumbMediaId;
	}
	
	public void setThumbMediaId(String thumbMediaId) {
		this.thumbMediaId = thumbMediaId;
	}

}
