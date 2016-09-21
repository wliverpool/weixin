package wfm.weixin.vo;

/**
 * 微信菜单按钮基类
 * @author 吴福明
 *
 */
public class Button {
	
	private String type;//菜单按钮的响应动作类型
	private String name;//	菜单按钮标题
	private Button[] sub_button;//子菜单按钮数组
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public Button[] getSub_button() {
		return sub_button;
	}

	public void setSub_button(Button[] sub_button) {
		this.sub_button = sub_button;
	}

}
