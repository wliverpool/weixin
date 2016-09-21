package wfm.weixin.controller;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import wfm.weixin.business.service.WeixinService;
import wfm.weixin.vo.AccessTokenPage;
import wfm.weixin.vo.JsApiTicket;
import wfm.weixin.vo.UserInfo;

/**
 * 微信嵌套网页的controller类
 * @author 吴福明
 *
 */
@Controller
@RequestMapping("/weixinPage")
public class WeixinPageController {
	//微信网页基本授权作用域
	public static final String SNSAPI_BASE = "snsapi_base";
	//微信网页高级授权作用域
	public static final String SNSAPI_USERINFO = "snsapi_userinfo";
	
	@Autowired
	private WeixinService weixinService;

	public WeixinService getWeixinService() {
		return weixinService;
	}

	public void setWeixinService(WeixinService weixinService) {
		this.weixinService = weixinService;
	}
	
	/**
	 * 获取微信网页基本授权的url
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getWeixinPageBaseInfo")
	public String getWeixinPageBaseInfo(HttpServletRequest request, HttpServletResponse response)throws UnsupportedEncodingException{
		//获取微信的授权时需要使用code
		String redirectUrl = request.getScheme()+"://"+request.getServerName()+request.getContextPath()+"/weixinPage/baseInfoView";
		String url = weixinService.getPageCodeUrl(redirectUrl, SNSAPI_BASE, "其他信息");
		return "redirect:"+url;
	}
	
	/**
	 * 微信网页基本授权之后处理逻辑的url
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/baseInfoView")
	public String baseInfoView(HttpServletRequest request, HttpServletResponse response){
		//获取基本授权
		String code = request.getParameter("code");
		//String state = request.getParameter("state");
		AccessTokenPage accessTokenPage = weixinService.getPageAccessToken(code);
		System.out.println("openId:"+accessTokenPage.getOpenid());
		return "baseInfoView";
	}
	
	/**
	 * 获取微信用户信息授权的url
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getWeixinPageUserInfo")
	public String getWeixinPageUserInfo(HttpServletRequest request, HttpServletResponse response)throws UnsupportedEncodingException{
		//获取微信的授权时需要使用code
		String redirectUrl = request.getScheme()+"://"+request.getServerName()+request.getContextPath()+"/weixinPage/userInfoView";
		String url = weixinService.getPageCodeUrl(redirectUrl, SNSAPI_USERINFO, "其他信息");
		return "redirect:"+url;
	}
	
	/**
	 * 微信用户信息授权之后处理逻辑的url
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/userInfoView")
	public String userInfoView(HttpServletRequest request, HttpServletResponse response){
		//获取用户信息授权
		String code = request.getParameter("code");
		//String state = request.getParameter("state");
		AccessTokenPage accessTokenPage = weixinService.getPageAccessToken(code);
		System.out.println("openId:"+accessTokenPage.getOpenid());
		UserInfo userInfo = weixinService.getUserInfoInPage(accessTokenPage.getOpenid(), accessTokenPage.getAccess_token());
		System.out.println("nickname:"+userInfo.getNickname());
		//获取页面调用微信js接口的授权验证信息,参见http://mp.weixin.qq.com/wiki/7/aaa137b55fb2e0456bf8dd9148dd613f.html#.E8.B0.83.E8.B5.B7.E5.BE.AE.E4.BF.A1.E6.89.AB.E4.B8.80.E6.89.AB.E6.8E.A5.E5.8F.A3
		long timestamp = System.currentTimeMillis();
		request.setAttribute("timestamp", timestamp);
		String noncestr = UUID.randomUUID().toString();
		request.setAttribute("noncestr", noncestr);
		StringBuffer currentUrlBuffer = request.getRequestURL();
        String queryString =request.getQueryString();
        if(StringUtils.isNotBlank(queryString)){
        	currentUrlBuffer.append("?").append(queryString);
        }
		System.out.println("currentUrlBuffer:"+currentUrlBuffer.toString());
		/* JS-SDK使用权限签名算法
		 * 1、获取access_token
		 * 2、用第一步拿到的access_token 采用http GET方式请求获得jsapi_ticket
		 * 3、签名生成规则如下：参与签名的字段包括noncestr（随机字符串）, 有效的jsapi_ticket, timestamp（时间戳）, url（当前网页的URL，不包含#及其后面部分） 。
		 * 对所有待签名参数按照字段名的ASCII 码从小到大排序（字典序）后，使用URL键值对的格式（即key1=value1&key2=value2…）拼接成字符串string1。这里需要注意的是所有参数名均为小写字符。
		 * 对string1作sha1加密，字段名和字段值都采用原始值，不进行URL 转义。
		 */
		String signature = "jsapi_ticket="+JsApiTicket.getInstance().getTicket()+"&noncestr="+noncestr+"&timestamp="+timestamp+"&url="+currentUrlBuffer.toString();
		signature = DigestUtils.sha1Hex(signature);
		request.setAttribute("signature", signature);
		return "userInfoView";
	}

	@RequestMapping(value = "/testLink")
	public String testLink(HttpServletRequest request, HttpServletResponse response){
		return "testLink";
	}
	
}
