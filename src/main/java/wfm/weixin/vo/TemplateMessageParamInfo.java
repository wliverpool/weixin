package wfm.weixin.vo;

/**
 * 微信模板参数信息
 * @author 吴福明
 *
 */
public class TemplateMessageParamInfo {
	
	private String value;//模板消息参数的值
	private String color;//显示模板消息参数的值使用的字体颜色
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public String getColor() {
		return color;
	}
	
	public void setColor(String color) {
		this.color = color;
	}

}
