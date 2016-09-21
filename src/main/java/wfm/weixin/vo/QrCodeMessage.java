package wfm.weixin.vo;

/**
 * 扫描二维码推送的事件消息
 * @author 吴福明
 *
 */
public class QrCodeMessage extends EventMessage{
	
	private String ticket;//二维码的ticket，可用来换取二维码图片

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
	
}
