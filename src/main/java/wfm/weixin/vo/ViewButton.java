package wfm.weixin.vo;

/**
 * 微信view类型菜单按钮
 * @author 吴福明
 *
 */
public class ViewButton extends Button{
	
	private String url;//网页链接，用户点击菜单按钮可打开链接，不超过256字节

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
