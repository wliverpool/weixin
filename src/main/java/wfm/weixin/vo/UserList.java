package wfm.weixin.vo;

import java.util.List;

/**
 * 关注微信号的用户列表信息
 * @author 吴福明
 *
 */
public class UserList {
	
	private int total;//关注该公众账号的总用户数
	private int count;//拉取的OPENID个数，最大值为10000
	private String next_openid;//拉取列表的最后一个用户的OPENID
	private List<UserInfo> users;//用户列表数据
	
	public int getTotal() {
		return total;
	}
	
	public void setTotal(int total) {
		this.total = total;
	}
	
	public int getCount() {
		return count;
	}
	
	public void setCount(int count) {
		this.count = count;
	}
	
	public String getNext_openid() {
		return next_openid;
	}
	
	public void setNext_openid(String next_openid) {
		this.next_openid = next_openid;
	}
	
	public List<UserInfo> getUsers() {
		return users;
	}
	
	public void setUsers(List<UserInfo> users) {
		this.users = users;
	}

}
