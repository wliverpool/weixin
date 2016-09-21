package wfm.weixin.vo;

/**
 * 模板消息发送事件消息
 * @author 吴福明
 *
 */
public class TemplateMsgSendFinishMessage extends EventMessage {
	
	private String status;//模板消息发送状态:success、failed:user block、failed: system failed

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
