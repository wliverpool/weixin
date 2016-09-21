package wfm.weixin.vo;

/**
 * 微信推送事件消息类
 * @author 吴福明
 *
 */
public class EventMessage extends Message {
	
	private String event;//事件类型，具体有subscribe(订阅)、unsubscribe(取消订阅)等等
	/*
	 * 事件KEY值，
	 * 当事件是由keybutton按钮产生的，与自定义菜单接口中KEY值对应，
	 * 当事件是由viewbutton按钮产生的，则是viewbutton中设置的跳转URL
	 */
	private String eventKey;

	public String getEventKey() {
		return eventKey;
	}

	public void setEventKey(String eventKey) {
		this.eventKey = eventKey;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

}
