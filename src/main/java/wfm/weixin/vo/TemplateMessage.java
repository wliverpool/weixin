package wfm.weixin.vo;

import java.util.Map;

/**
 * 微信模板消息
 * @author 吴福明
 *
 */
public class TemplateMessage {
	
	private String touser;//发送的目标微信id
	private String template_id;//使用的模板id
	private String url;//点击模板消息之后跳转的url
	private Map<String, TemplateMessageParamInfo> data;//模板参数list
	
	public String getTouser() {
		return touser;
	}

	public void setTouser(String touser) {
		this.touser = touser;
	}
	
	public String getTemplate_id() {
		return template_id;
	}
	
	public void setTemplate_id(String template_id) {
		this.template_id = template_id;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public Map<String, TemplateMessageParamInfo> getData() {
		return data;
	}

	public void setData(Map<String, TemplateMessageParamInfo> data) {
		this.data = data;
	}
	
}
