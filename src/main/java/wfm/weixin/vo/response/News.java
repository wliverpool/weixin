package wfm.weixin.vo.response;

/**
 * 微信图文信息类
 * @author 吴福明
 *
 */
public class News {
	
	private String title;//图文信息标题
	private String description;//图文信息描述
	private String picUrl;//图片链接，支持JPG、PNG格式，较好的效果为大图360*200，小图200*200
	private String url;//点击图文信息跳转链接
	
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
	
	public String getPicUrl() {
		return picUrl;
	}
	
	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}

}
