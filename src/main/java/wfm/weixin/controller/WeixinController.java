package wfm.weixin.controller;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import wfm.weixin.business.service.WeixinService;

/**
 * 微信controller类
 * @author 吴福明
 *
 */
@Controller
@RequestMapping("/weixin")
public class WeixinController {
	
	@Autowired
	private WeixinService weixinService;
	
	public WeixinService getWeixinService() {
		return weixinService;
	}

	public void setWeixinService(WeixinService weixinService) {
		this.weixinService = weixinService;
	}

	/**
	 * 微信接入验证,根据微信公众平台api
	 * https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421135319&token=&lang=zh_CN
	 * 对接入参数signature,timestamp,nonce,echostr请求进行校验,若确认此次GET请求来自微信服务器,
	 * 请原样返回echostr参数内容,则接入生效,成为开发者成功,否则接入失败.
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/accessValidate",method=RequestMethod.GET)
	public String accessValidate(HttpServletRequest request, HttpServletResponse response){
		//获取参数
		String signature = request.getParameter("signature");
		String timestamp = request.getParameter("timestamp");
		String nonce = request.getParameter("nonce");
		String echostr = request.getParameter("echostr");
		
		//判断系统生成的加密字符串与微信生成的是否相同
		if(weixinService.checkSignature(signature, timestamp, nonce)){
			return html(echostr, response);
		}
		
		return null;
	}
	
	/**
	 * 微信消息处理,根据微信公众平台api
	 * https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421140453&token=&lang=zh_CN
	 * 接受输入流,处理消息并返回给微信客户端
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/accessValidate",method=RequestMethod.POST)
	public String dealMessage(HttpServletRequest request, HttpServletResponse response){
		//接受消息输入流
		InputStream messageIn = null;
		try {
			messageIn = request.getInputStream();
			//获得返回的消息
			String replay = weixinService.generateReplayMessage(messageIn);
			System.out.println("replay:"+replay);
			//返回消息
			if(null!=replay){
				return xml(replay, response);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		} finally{
			if(null!=messageIn){
				try {
					messageIn.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return null;
	}
	
	private String ajax(String content, String type, HttpServletResponse response) {
		try {
			response.setContentType(type + ";charset=UTF-8");
			response.setCharacterEncoding("UTF-8");
			response.setHeader("Pragma", "No-cache");
			response.setHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expires", 0);
			response.getWriter().write(content);
			response.getWriter().flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private String xml(String xml, HttpServletResponse response) {
		return ajax(xml, "text/xml", response);
	}
	
	private String html(String html, HttpServletResponse response) {
		return ajax(html, "text/html", response);
	}

}
