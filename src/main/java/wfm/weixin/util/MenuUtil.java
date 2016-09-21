package wfm.weixin.util;

import wfm.weixin.vo.Button;
import wfm.weixin.vo.KeyButton;
import wfm.weixin.vo.Menu;
import wfm.weixin.vo.ViewButton;

/**
 * 微信菜单的工具类 微信菜单api
 * http://mp.weixin.qq.com/wiki/13/43de8269be54a0a6f64413e4dfa94f39.html
 * 
 * @author 吴福明
 *
 */
public class MenuUtil {

	/**
	 * 初始化微信菜单
	 * 
	 * @return
	 */
	public static Menu initMenu() {
		Menu menu = new Menu();

		KeyButton button1 = new KeyButton();
		button1.setKey("testclick");
		button1.setName("测试单击事件");
		button1.setType("click");

		ViewButton button2 = new ViewButton();
		button2.setName("利物浦中文官网");
		button2.setType("view");
		button2.setUrl("http://cn.liverpoolfc.com/");

		KeyButton button3 = new KeyButton();
		button3.setKey("testlocation_select");
		button3.setName("测试弹出地理位置");
		button3.setType("location_select");

		KeyButton button4 = new KeyButton();
		button4.setKey("testscancode_push");
		button4.setName("测试扫码");
		button4.setType("scancode_push");

		Button button5 = new Button();
		button5.setName("事件测试");
		button5.setSub_button(new Button[] { button3, button4 });

		menu.setButton(new Button[] { button1, button2, button5 });

		return menu;
	}

}
