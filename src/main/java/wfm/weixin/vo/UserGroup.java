package wfm.weixin.vo;

/**
 * 微信用户所属组信息
 * @author 吴福明
 *
 */
public class UserGroup {
	
	private String[] openid_list;//用户唯一标识符openid的列表（size不能超过50）
	private long to_groupid;//分组id
	
	public String[] getOpenid_list() {
		return openid_list;
	}
	
	public void setOpenid_list(String[] openid_list) {
		this.openid_list = openid_list;
	}
	
	public long getTo_groupid() {
		return to_groupid;
	}
	
	public void setTo_groupid(long to_groupid) {
		this.to_groupid = to_groupid;
	}

}
