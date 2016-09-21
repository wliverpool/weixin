package wfm.weixin.vo;

/**
 * 微信分组类
 * @author 吴福明
 *
 */
public class Group {
	
	private long id;//分组id，由微信分配
	private String name;//分组名字，UTF8编码
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

}
