package wfm.weixin.vo;

/**
 * 微信群发消息发送完成的事件推送消息
 * @author 吴福明
 *
 */
public class MassFinishMessage extends EventMessage {
	
	private String msgId;//消息id
	/*
	 * 群发的结构，为“send success”或“send fail”或“err(num)”。
	 * 但send success时，也有可能因用户拒收公众号的消息、系统错误等原因造成少量用户接收失败。
	 * err(num)是审核失败的具体原因，可能的情况如下：
	 * err(10001), //涉嫌广告 err(20001), //涉嫌政治 err(20004), 
	 * //涉嫌社会 err(20002), //涉嫌色情 err(20006), //涉嫌违法犯罪 err(20008), 
	 * //涉嫌欺诈 err(20013), //涉嫌版权 err(22000), //涉嫌互推(互相宣传) err(21000), 
	 * //涉嫌其他
	 */
	private String status;
	private String totalCount;//group_id下粉丝数；或者openid_list中的粉丝数
	//过滤（过滤是指特定地区、性别的过滤、用户设置拒收的过滤，用户接收已超4条的过滤）后，
	//准备发送的粉丝数，原则上，FilterCount = SentCount + ErrorCount
	private String filterCount;
	private String sentCount;//发送成功的粉丝数
	private String errorCount;//发送失败的粉丝数

	public String getMsgId() {
		return msgId;
	}
	
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getTotalCount() {
		return totalCount;
	}
	
	public void setTotalCount(String totalCount) {
		this.totalCount = totalCount;
	}
	
	public String getFilterCount() {
		return filterCount;
	}
	
	public void setFilterCount(String filterCount) {
		this.filterCount = filterCount;
	}
	
	public String getSentCount() {
		return sentCount;
	}
	
	public void setSentCount(String sentCount) {
		this.sentCount = sentCount;
	}
	
	public String getErrorCount() {
		return errorCount;
	}
	
	public void setErrorCount(String errorCount) {
		this.errorCount = errorCount;
	}

}
