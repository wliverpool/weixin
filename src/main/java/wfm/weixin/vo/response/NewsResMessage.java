package wfm.weixin.vo.response;

import java.util.List;

import wfm.weixin.vo.Message;

/**
 * 用于回复的微信图文消息类
 * @author 吴福明
 *
 */
public class NewsResMessage extends Message {
	
	private int articleCount;//图文消息个数，限制为10条以内
	private List<News> articles;//多条图文消息信息，默认第一个item为大图,注意，如果图文数超过10，则将会无响应
	
	public int getArticleCount() {
		return articleCount;
	}
	
	public void setArticleCount(int articleCount) {
		this.articleCount = articleCount;
	}
	
	public List<News> getArticles() {
		return articles;
	}
	
	public void setArticles(List<News> articles) {
		this.articles = articles;
	}

}
