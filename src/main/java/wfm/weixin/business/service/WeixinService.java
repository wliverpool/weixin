package wfm.weixin.business.service;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.dom4j.DocumentException;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

import net.sf.json.JSONArray;
import net.sf.json.JSONNull;
import net.sf.json.JSONObject;
import wfm.weixin.util.FileUtil;
import wfm.weixin.util.HttpUtil;
import wfm.weixin.util.MenuUtil;
import wfm.weixin.util.MessageUtil;
import wfm.weixin.vo.AccessToken;
import wfm.weixin.vo.AccessTokenPage;
import wfm.weixin.vo.EventMessage;
import wfm.weixin.vo.Group;
import wfm.weixin.vo.GroupInfo;
import wfm.weixin.vo.LocationMessage;
import wfm.weixin.vo.MassFinishMessage;
import wfm.weixin.vo.MassNews;
import wfm.weixin.vo.Menu;
import wfm.weixin.vo.Message;
import wfm.weixin.vo.QrCodeMessage;
import wfm.weixin.vo.QrSceneInfo;
import wfm.weixin.vo.TemplateMessage;
import wfm.weixin.vo.TemplateMessageParamInfo;
import wfm.weixin.vo.TemplateMsgSendFinishMessage;
import wfm.weixin.vo.TextMessage;
import wfm.weixin.vo.UserGroup;
import wfm.weixin.vo.UserInfo;
import wfm.weixin.vo.UserList;
import wfm.weixin.vo.request.ImageReqMessage;
import wfm.weixin.vo.request.VoiceReqMessage;
import wfm.weixin.vo.response.Music;
import wfm.weixin.vo.response.News;

/**
 * 微信服务类
 * @author 吴福明
 *
 */
@Service
public class WeixinService {
	
	private static final String DOMAIN_NAME = "http://4534ca38.ittun.com/";
	// 微信Token(令牌)
	private static final String token = "mittermeyer";
	// 微信第三方用户唯一凭证
	public static final String APPID = "wxa3655d48828c7006";
	// 微信第三方用户唯一凭证密钥
	public static final String APPSECRET = "3e0e1983fd62841c157f192f87c200a5";
	//从微信获取accsesstoken的接口地址
	public static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
	//上传微信临时素材的接口地址
	public static final String UPLOAD_URL = "https://api.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE";
	//创建自定义菜单
	public static final String CREATE_MENU = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
	//查询自定义菜单
	public static final String QUERY_MENU = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=ACCESS_TOKEN";
	//删除自定义菜单
	public static final String DELETE_MENU = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=ACCESS_TOKEN";
	//创建分组
	public static final String CREATE_GROUP = "https://api.weixin.qq.com/cgi-bin/groups/create?access_token=ACCESS_TOKEN";
	//查询分组
	public static final String QUERY_GROUP = "https://api.weixin.qq.com/cgi-bin/groups/get?access_token=ACCESS_TOKEN";
	//删除分组
	public static final String DELETE_GROUP = "https://api.weixin.qq.com/cgi-bin/groups/delete?access_token=ACCESS_TOKEN";
	//修改分组名
	public static final String UPDATE_GROUP = "https://api.weixin.qq.com/cgi-bin/groups/update?access_token=ACCESS_TOKEN";
	//获取用户列表
	public static final String USER_LIST = "https://api.weixin.qq.com/cgi-bin/user/get?access_token=ACCESS_TOKEN&next_openid=NEXT_OPENID";
	//获取用户详细信息
	public static final String USER_INFO = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
	//修改用户所属组
	public static final String UPDATE_USER_GROUP = "https://api.weixin.qq.com/cgi-bin/groups/members/batchupdate?access_token=ACCESS_TOKEN";
	//查询用户所在分组
	public static final String QUERY_USER_GROUP = "https://api.weixin.qq.com/cgi-bin/groups/getid?access_token=ACCESS_TOKEN";
	//发送模板消息
	public static final String SEND_TEMPLATE_MESSAGE = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";
	//设置微信号所属行业
	public static final String SET_INDUSTRY = "https://api.weixin.qq.com/cgi-bin/template/api_set_industry?access_token=ACCESS_TOKEN";
	//获得微信号所在行业的模板消息id
	public static final String GET_TEMPLATEID = "https://api.weixin.qq.com/cgi-bin/template/api_add_template?access_token=ACCESS_TOKEN";
	//创建带参数二维码
	public static final String CREATE_QRSCENE = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=ACCESS_TOKEN";
	//长链接转成短链接
	public static final String TRANSFER_URL = "https://api.weixin.qq.com/cgi-bin/shorturl?access_token=ACCESS_TOKEN";
	//获取二维码
	public static final String GET_QRCODE = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=TICKET";
	//获取微信网页授权的code
	public static final String GET_PAGE_CODE = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect";
	//获取微信网页授权的access token
	public static final String GET_PAGE_ACCESSTOKEN = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
	//获取微信网页授权获取微信用户信息
	public static final String GET_PAGE_USERINFO = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
	//检验微信网页授权的access token是否有效
	public static final String PAGE_ACCESSTOKEN_EFFECT = "https://api.weixin.qq.com/sns/auth?access_token=ACCESS_TOKEN&openid=OPENID";
	//刷新网页授权access_token
	public static final String PAGE_ACCESSTOKEN_REFRESH = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=APPID&grant_type=refresh_token&refresh_token=REFRESH_TOKEN";
	//获得网页jsapi_ticket
	public static final String GET_TICKET= "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";
	//通过openid群发消息
	public static final String SEND_MASS_OPENID = "https://api.weixin.qq.com/cgi-bin/message/mass/send?access_token=ACCESS_TOKEN";
	//上传图片获取微信图片地址
	public static final String GETURL_UPLOAD_IMAGE = "https://api.weixin.qq.com/cgi-bin/media/uploadimg?access_token=ACCESS_TOKEN";
	//查询群发消息状态
	public static final String GET_MASS_STATUS = "https://api.weixin.qq.com/cgi-bin/message/mass/get?access_token=ACCESS_TOKEN";
	//上传图文消息素材
	public static final String UPLOAD_NEWS= "https://api.weixin.qq.com/cgi-bin/media/uploadnews?access_token=ACCESS_TOKEN";
	//通过分组群发消息
	public static final String SEND_MASS_GROUP = "https://api.weixin.qq.com/cgi-bin/message/mass/sendall?access_token=ACCESS_TOKEN";
	//预览群发消息
	public static final String PREVIEW_MASS = "https://api.weixin.qq.com/cgi-bin/message/mass/preview?access_token=ACCESS_TOKEN";
	
	/**
	 * 验证微信平台发送的接入参数是否一致
	 * @param signature  微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。
	 * @param timestamp  时间戳
	 * @param nonce      微信平台根据公众号随机生成的随机数
	 * @return
	 */
	public boolean checkSignature(String signature,String timestamp,String nonce){
		
		String[] arrParams = new String[]{token,timestamp,nonce};
		//排序
		Arrays.sort(arrParams);
		
		//生成字符串
		StringBuffer content = new StringBuffer();
		for(int i=0;i<arrParams.length;i++){
			content.append(arrParams[i]);
		}
		//sha1加密
		String temp = DigestUtils.sha1Hex(content.toString());
		//比较加密之后是否与signature相同
		return temp.equals(signature);
		
	}
	
	public String generateReplayMessage(InputStream messageIn)throws IOException,DocumentException{
		
		String replay = null;
		//转换输入流的xml格式
		Message msg = MessageUtil.xmlToMessage(messageIn);
		
		//根据消息类型生成回复消息
		if(MessageUtil.MESSAGE_TEXT.equals(msg.getMsgType())){
			TextMessage textMsg = (TextMessage) msg;
			StringBuffer str = new StringBuffer();
			//文本消息
			if("1".equals(textMsg.getContent())){
				//关键字1消息的回复
				str.append("利物浦足球俱乐部队，简称利物浦队，是英格兰足球超级联赛的球队之一。球队位于英格兰西北默西赛德郡港口城市利物浦，于1892年成立，是英格兰的一支足球俱乐部。\n\n");
				str.append("利物浦是英格兰足球历史上最成功的俱乐部之一，也是欧洲乃至世界最成功的足球俱乐部之一。利物浦一共夺取过18次英格兰甲级联赛冠军、7次英格兰足总杯冠军、8次英格兰联赛杯冠军、5次欧洲冠军联赛冠军以及3次欧洲联盟杯冠军，也曾为已解散的G-14创立成员，乃是英格兰历史上最成功的球队之一。");
				replay = MessageUtil.generateXmlFormatTextMessage(textMsg.getToUserName(),textMsg.getFromUserName(),str.toString());
			}else if("2".equals(textMsg.getContent())){
				//关键字2消息的回复
				str.append("22.西蒙·米尼奥莱\n\n34.亚当·博格丹\n\n3.何塞·恩里克\n\n4.科洛·图雷\n\n6.德扬·洛夫伦\n\n17.玛玛杜·萨科\n\n37.马丁·斯科特尔\n\n38.约翰·弗拉纳甘\n\n18.阿尔贝托·莫雷诺\n\n2.纳撒尼尔·克莱因\n\n12.乔·戈麦斯\n\n10.菲利普·库迪尼奥\n\n14.乔丹·亨德森\n\n20.亚当·拉拉纳\n\n21.卢卡斯·雷瓦\n\n23.埃姆雷·詹\n\n24.乔·阿伦\n\n33.乔丹·伊比\n\n11.罗伯托·菲尔米诺\n\n7.菲利普·米尔纳\n\n");
				replay = MessageUtil.generateXmlFormatTextMessage(textMsg.getToUserName(),textMsg.getFromUserName(),str.toString());
			}else if("3".equals(textMsg.getContent())){
				//关键字3消息的回复,是一个图文消息列表
				List<News> replayNews = new ArrayList<News>();
				News news = new News();
				news.setTitle("利物浦中文官网");
				news.setDescription("利物浦足球俱乐部中文官方网站");
				news.setPicUrl(DOMAIN_NAME+"/weixin/static/images/lfc_crest.png");
				news.setUrl("cn.liverpoolfc.com");
				replayNews.add(news);
				replay = MessageUtil.generateXmlFormatNewsMessage(textMsg.getToUserName(),textMsg.getFromUserName(), replayNews);
			}else if("4".equals(textMsg.getContent())){
				//关键字4图片消息
				replay = MessageUtil.generateXmlFormatImageMessage(textMsg.getToUserName(),textMsg.getFromUserName(), "Cte7Yv_ccxWdydZYQ5X_ZCj3wmzJqJhKES-aQhOiwtOoaJ_wEGMkTfzVeEuMYWkF");
			}else if("5".equals(textMsg.getContent())){
				//关键字5音乐消息
				Music music = new Music();
				music.setTitle("利物浦队歌");
				music.setDescription("你永远不会独行");
				music.setMusicUrl(DOMAIN_NAME+"weixin/static/music/You_never_walk_alone.mp3");
				music.sethQMusicUrl(DOMAIN_NAME+"weixin/static/music/You_never_walk_alone_High.mp3");
				//缩略图需提前上传素材获得mediaid
				music.setThumbMediaId("DpotLtIgBuWwCI0Ra6pfKDmReuAZ2YCK-4oUYDHusuDISxKzIHN1psJ5RY_S0GDv");
				replay = MessageUtil.generateXmlFormatMusicMessage(textMsg.getToUserName(), textMsg.getFromUserName(), music);
			}else if("?".equals(textMsg.getContent())||"？".equals(textMsg.getContent())){
				//关键字?消息的回复
				replay = MessageUtil.generateXmlFormatTextMessage(textMsg.getToUserName(),textMsg.getFromUserName(),getMainReplayInfo());
			}else{
				//普通文本内容的回复
				str.append("你说的是：").append(textMsg.getContent()).append("，吗？");
				replay = MessageUtil.generateXmlFormatTextMessage(textMsg.getToUserName(),textMsg.getFromUserName(),str.toString());
			}
			
		}else if(MessageUtil.MESSAGE_EVENT.equals(msg.getMsgType())){
			//消息推送事件
			EventMessage eventMsg = (EventMessage) msg;
			//根据消息时间的类型再做分类
			if(MessageUtil.MESSAGE_EVENT_SUBSRIBE.equals(eventMsg.getEvent())){
				//关注事件类型消息
				//设置关注事件消息返回内容
				replay = MessageUtil.generateXmlFormatTextMessage(eventMsg.getToUserName(), eventMsg.getFromUserName(), getMainReplayInfo());
			}else if(MessageUtil.MESSAGE_EVENT_CLICK.equals(eventMsg.getEvent())){
				//菜单按钮的click事件类型消息
				//click事件类型消息返回内容
				replay = MessageUtil.generateXmlFormatTextMessage(eventMsg.getToUserName(), eventMsg.getFromUserName(), getMainReplayInfo());
			}else if(MessageUtil.MESSAGE_EVENT_VIEW.equals(eventMsg.getEvent())){
				//菜单按钮的view事件类型消息
				//view事件类型消息返回内容,实际生成的返回消息微信不会回复,可以进行程序逻辑处理
				replay = MessageUtil.generateXmlFormatTextMessage(eventMsg.getToUserName(), eventMsg.getFromUserName(), eventMsg.getEventKey());
			}else if(MessageUtil.MESSAGE_EVENT_SCANCODE_PUSH.equals(eventMsg.getEvent())){
				//菜单按钮的扫码推事件类型消息
				//扫码推事件类型消息返回内容,实际生成的返回消息微信不会回复,可以进行程序逻辑处理
				replay = MessageUtil.generateXmlFormatTextMessage(eventMsg.getToUserName(), eventMsg.getFromUserName(), eventMsg.getEventKey());
			}else if(MessageUtil.MESSAGE_EVENT_TEMPLATEMSGFINISH.equals(eventMsg.getEvent())){
				//模板消息发送完成事件
				TemplateMsgSendFinishMessage m = (TemplateMsgSendFinishMessage) eventMsg;
				String content = "发送状态:" +m.getStatus();
				replay = MessageUtil.generateXmlFormatTextMessage(eventMsg.getToUserName(), eventMsg.getFromUserName(), content);
			}else if(MessageUtil.MESSAGE_EVENT_SCAN.equals(eventMsg.getEvent())){
				//扫描二维码事件
				QrCodeMessage qrMsg = (QrCodeMessage) eventMsg;
				String content = "发送状态:" +qrMsg.getEventKey() + ";" + qrMsg.getTicket();
				replay = MessageUtil.generateXmlFormatTextMessage(eventMsg.getToUserName(), eventMsg.getFromUserName(), content);
			}else if(MessageUtil.MESSAGE_EVENT_MASSSENDFINISH.equals(eventMsg.getEvent())){
				MassFinishMessage massFinishMessage = (MassFinishMessage)eventMsg;
				System.out.println(JSONObject.fromObject(massFinishMessage).toString());
			}
		}else if(MessageUtil.MESSAGE_LOCATION.equals(msg.getMsgType())){
			//菜单按钮的location_select事件类型消息
			//location_select事件类型消息返回内容
			LocationMessage locationMessage = (LocationMessage) msg;
			replay = MessageUtil.generateXmlFormatTextMessage(locationMessage.getToUserName(), locationMessage.getFromUserName(), locationMessage.getLabel());
		}else if(MessageUtil.MESSAGE_VOICE.equals(msg.getMsgType())){
			//接收到语音消息
			VoiceReqMessage voiceMsg = (VoiceReqMessage) msg;
			String content = voiceMsg.getRecognition();
			if(null != content&& !"".equals(content)){
				content = "收到的语音解析结果："+content;
			}else{
				content = "您说的太模糊了，能不能重新说下呢？";
			}
			replay = MessageUtil.generateXmlFormatTextMessage(voiceMsg.getToUserName(), voiceMsg.getFromUserName(), content);
		}else if(MessageUtil.MESSAGE_IMAGE.equals(msg.getMsgType())){
			//接收到图片消息
			ImageReqMessage imageMsg = (ImageReqMessage) msg;
			//从图片消息中提取图片链接地址返回一个包含这个图片链接地址的文本消息
			String picUrl = imageMsg.getPicUrl();
			replay = MessageUtil.generateXmlFormatTextMessage(imageMsg.getToUserName(), imageMsg.getFromUserName(), picUrl);
		}
		
		return replay;
		
	}
	
	/**
	 * 获取微信群发消息状态
	 * @param msgId  微信群发消息id
	 * @return
	 */
	public String getMassMessageSendStatus(String msgId){
		//获取access_token
		AccessToken accessToken = AccessToken.getInstance();
		String url = GET_MASS_STATUS.replace("ACCESS_TOKEN", accessToken.getAccess_token());
		String json = "{\"msg_id\": \""+msgId+"\"}";
		JSONObject resultJsonObject = HttpUtil.getJsonResponseByHttpPost(url, json);
		System.out.println(resultJsonObject.toString());
		if(null != resultJsonObject){
			if(null!=resultJsonObject.get("msg_status")&&!(resultJsonObject.get("msg_status") instanceof JSONNull)){
				return resultJsonObject.getString("msg_status");
			}
		}
		return null;
	}
	
	/**
	 * 根据分组进行群发消息
	 * @param isToAll  用于设定是否向全部用户发送，值为true或false，选择true该消息群发给所有用户，选择false可根据group_id发送给指定群组的用户
	 * @param groupId  群发到的分组的group_id，参加用户管理中用户分组接口，若is_to_all值为true，可不填写group_id
	 * @param msgType  消息类型
	 * @param msgContent   消息内容:发送文本消息时为文本消息的内容,其他消息时为上传消息的素材media_id
	 * @return   是否发送成功
	 */
	public boolean sendMassMessageByGroup(boolean isToAll,long groupId,String msgType,String msgContent){
		//获取access_token
		AccessToken accessToken = AccessToken.getInstance();
		String url = SEND_MASS_GROUP.replace("ACCESS_TOKEN", accessToken.getAccess_token());
		//组成群发信息
		JSONObject message = new JSONObject();
		JSONObject filter = new JSONObject();
		filter.put("is_to_all", isToAll);
		filter.put("group_id", groupId);
		message.put("filter", filter);
		if(MessageUtil.MESSAGE_TEXT.equals(msgType)){
			JSONObject content = new JSONObject();
			content.put("content", msgContent);
			message.put("text", content);
			message.put("msgtype", MessageUtil.MESSAGE_TEXT);
		}else if(MessageUtil.MESSAGE_IMAGE.equals(msgType)){
			JSONObject content = new JSONObject();
			content.put("media_id", msgContent);
			message.put("image", content);
			message.put("msgtype", MessageUtil.MESSAGE_IMAGE);
		}else if(MessageUtil.MESSAGE_MASSNEWS.equals(msgType)){
			JSONObject content = new JSONObject();
			content.put("media_id", msgContent);
			message.put("mpnews", content);
			message.put("msgtype", MessageUtil.MESSAGE_MASSNEWS);
		}
		System.out.println(message.toString());
		JSONObject resultJsonObject = HttpUtil.getJsonResponseByHttpPost(url, message.toString());
		System.out.println(resultJsonObject.toString());
		if(null != resultJsonObject){
			int result = resultJsonObject.getInt("errcode");
			if(result != 0){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 预览群发消息
	 * @param openId  预览群发消息的微信openId
	 * @param msgType   消息类型
	 * @param msgContent   消息内容:发送文本消息时为文本消息的内容,其他消息时为上传消息的素材media_id
	 * @return   是否预览成功
	 */
	public boolean previewMassMessage(String openId,String msgType,String msgContent){
		//获取access_token
		AccessToken accessToken = AccessToken.getInstance();
		String url = PREVIEW_MASS.replace("ACCESS_TOKEN", accessToken.getAccess_token());
		//组成群发信息
		JSONObject message = new JSONObject();
		message.put("touser", openId);
		if(MessageUtil.MESSAGE_TEXT.equals(msgType)){
			JSONObject content = new JSONObject();
			content.put("content", msgContent);
			message.put("text", content);
			message.put("msgtype", MessageUtil.MESSAGE_TEXT);
		}else if(MessageUtil.MESSAGE_IMAGE.equals(msgType)){
			JSONObject content = new JSONObject();
			content.put("media_id", msgContent);
			message.put("image", content);
			message.put("msgtype", MessageUtil.MESSAGE_IMAGE);
		}else if(MessageUtil.MESSAGE_MASSNEWS.equals(msgType)){
			JSONObject content = new JSONObject();
			content.put("media_id", msgContent);
			message.put("mpnews", content);
			message.put("msgtype", MessageUtil.MESSAGE_MASSNEWS);
		}
		System.out.println(message.toString());
		JSONObject resultJsonObject = HttpUtil.getJsonResponseByHttpPost(url, message.toString());
		System.out.println(resultJsonObject.toString());
		if(null != resultJsonObject){
			int result = resultJsonObject.getInt("errcode");
			if(result != 0){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 根据OpenID列表群发消息
	 * @param openIdList   微信openId列表
	 * @param msgType   消息类型
	 * @param msgContent   消息内容:发送文本消息时为文本消息的内容,其他消息时为上传消息的素材media_id
	 * @return  是否发送
	 */
	public boolean sendMassMessageByOpenIDs(List<String> openIdList,String msgType,String msgContent){
		//获取access_token
		AccessToken accessToken = AccessToken.getInstance();
		String url = SEND_MASS_OPENID.replace("ACCESS_TOKEN", accessToken.getAccess_token());
		//组成群发信息
		JSONObject message = new JSONObject();
		JSONArray openIds = JSONArray.fromObject(openIdList);
		message.put("touser", openIds);
		if(MessageUtil.MESSAGE_TEXT.equals(msgType)){
			JSONObject content = new JSONObject();
			content.put("content", msgContent);
			message.put("text", content);
			message.put("msgtype", MessageUtil.MESSAGE_TEXT);
		}else if(MessageUtil.MESSAGE_IMAGE.equals(msgType)){
			JSONObject content = new JSONObject();
			content.put("media_id", msgContent);
			message.put("image", content);
			message.put("msgtype", MessageUtil.MESSAGE_IMAGE);
		}else if(MessageUtil.MESSAGE_MASSNEWS.equals(msgType)){
			JSONObject content = new JSONObject();
			content.put("media_id", msgContent);
			message.put("mpnews", content);
			message.put("msgtype", MessageUtil.MESSAGE_MASSNEWS);
		}
		System.out.println(message.toString());
		JSONObject resultJsonObject = HttpUtil.getJsonResponseByHttpPost(url, message.toString());
		System.out.println(resultJsonObject.toString());
		if(null != resultJsonObject){
			int result = resultJsonObject.getInt("errcode");
			if(result != 0){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 上传微信图文消息并获取对应的media_id
	 * @param massNewsList  群发图文消息列表
	 * @return  图文消息的media_id
	 */
	public String uploadNews(List<MassNews> massNewsList){
		//获取access token
		AccessToken accessToken = AccessToken.getInstance();
		String url = UPLOAD_NEWS.replace("ACCESS_TOKEN", accessToken.getAccess_token());
		//组装图文消息列表
		JSONObject jsonObject = new JSONObject();
		JSONArray articles = JSONArray.fromObject(massNewsList);
		jsonObject.put("articles", articles);
		JSONObject resultJsonObject = HttpUtil.getJsonResponseByHttpPost(url, jsonObject.toString());
		System.out.println(resultJsonObject.toString());
		if(null != resultJsonObject){
			if(null!=resultJsonObject.get("media_id")&&!(resultJsonObject.get("media_id") instanceof JSONNull)){
				return resultJsonObject.getString("media_id");
			}
		}
		return null;
	}
	
	/**
	 * 上传图片并返回图片的url
	 * @param imagePath  图片路径
	 * @return   上传图片的微信url
	 * @throws FileNotFoundException
	 */
	public String uploadImage(String imagePath)throws FileNotFoundException{
		//获取access token
		AccessToken accessToken = AccessToken.getInstance();
		//设置上传url和参数
		String url = GETURL_UPLOAD_IMAGE.replace("ACCESS_TOKEN", accessToken.getAccess_token());
		//通过http请求上传素材
		String result = HttpUtil.uploadFileByHttp(imagePath, url);	
		String imageUrl = "";
		//响应结果转换成json格式
		if(null!=result&&!"".equals(result)){
			JSONObject jsonObject = JSONObject.fromObject(result);
			System.out.println("result:"+result);
			if(null!=jsonObject.get("url")&&!(jsonObject.get("url") instanceof JSONNull)){
				imageUrl = jsonObject.getString("url");
			}
		}
		return imageUrl;
	}
	
	/**
	 * 上传微信临时素材
	 * @param filePath  素材文件路径
	 * @param type  素材类型(图片（image）: 2M，支持PNG\JPEG\JPG\GIF格式、 语音（voice）：2M，播放长度不超过60s，支持AMR\MP3格式、视频（video）：10MB，支持MP4格式、缩略图（thumb）：64KB，支持JPG格式)
	 * @return   临时素材上传后，获取的唯一标识
	 */
	public String uploadFile(String filePath,String type)throws FileNotFoundException{
		//获取access token
		AccessToken accessToken = AccessToken.getInstance();
		//设置上传url和参数
		String url = UPLOAD_URL.replace("ACCESS_TOKEN", accessToken.getAccess_token()).replace("TYPE", type);
		//通过http请求上传素材
		String result = HttpUtil.uploadFileByHttp(filePath, url);	
		String mediaId = "";
		//响应结果转换成json格式
		if(null!=result&&!"".equals(result)){
			JSONObject jsonObject = JSONObject.fromObject(result);
			System.out.println("result:"+result);
			String mediaIdName = "media_id";
			if(!"image".equals(type)){
				mediaIdName = type + "_" + mediaIdName;
			}
			mediaId = jsonObject.getString(mediaIdName);
		}
		return mediaId;
	}
	
	/**
	 * 创建微信分组
	 * @param groupName  分组名
	 * @return  微信分组信息
	 */
	public Group createGroupByName(String groupName){
		Group newGroup = null;
		//获取access token
		AccessToken accessToken = AccessToken.getInstance();
		//创建分组
		String url = CREATE_GROUP.replace("ACCESS_TOKEN", accessToken.getAccess_token());
		Group group = new Group();
		group.setName(groupName);
		GroupInfo info = new GroupInfo();
		info.setGroup(group);
		JSON json = (JSON)JSON.toJSON(info);
		JSONObject resultJsonObject = HttpUtil.getJsonResponseByHttpPost(url, json.toString());
		System.out.println(resultJsonObject.toString());
		if(null != resultJsonObject){
			GroupInfo newInfo = (GroupInfo)JSONObject.toBean(resultJsonObject, GroupInfo.class);
			newGroup = newInfo.getGroup();
		}
		return newGroup;
	}
	
	/**
	 * 修改微信分组信息
	 * @return  是否修改成功
	 */
	public boolean updateGroupById(long groupId,String updateName){
		//获取access token
		AccessToken accessToken = AccessToken.getInstance();
		//创建菜单
		String url = UPDATE_GROUP.replace("ACCESS_TOKEN", accessToken.getAccess_token());
		Group group = new Group();
		group.setId(groupId);
		group.setName(updateName);
		GroupInfo info = new GroupInfo();
		info.setGroup(group);
		JSON json = (JSON)JSON.toJSON(info);
		JSONObject resultJsonObject = HttpUtil.getJsonResponseByHttpPost(url, json.toString());
		System.out.println(resultJsonObject.toString());
		if(null != resultJsonObject){
			int result = resultJsonObject.getInt("errcode");
			if(result != 0){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 查询微信分组
	 * @return  微信分组数组信息
	 */
	public Group[] queryGroup(){
		//获取access token
		AccessToken accessToken = AccessToken.getInstance();
		//创建菜单
		String url = QUERY_GROUP.replace("ACCESS_TOKEN", accessToken.getAccess_token());
		JSONObject resultJsonObject = HttpUtil.getJsonResponseByHttpGet(url);
		JSONArray array = resultJsonObject.getJSONArray("groups");
		Group[] groups = (Group[])JSONArray.toArray(array, Group.class);
		return groups;
	}
	
	/**
	 * 删除微信分组
	 * @return  是否删除成功
	 */
	public boolean deleteGroupById(long id){
		//获取access token
		AccessToken accessToken = AccessToken.getInstance();
		//创建菜单
		String url = DELETE_GROUP.replace("ACCESS_TOKEN", accessToken.getAccess_token());
		Group group = new Group();
		group.setId(id);
		GroupInfo info = new GroupInfo();
		info.setGroup(group);
		JSON json = (JSON)JSON.toJSON(info);
		JSONObject resultJsonObject = HttpUtil.getJsonResponseByHttpPost(url, json.toString());
		System.out.println(resultJsonObject.toString());
		if(null != resultJsonObject){
			int result = resultJsonObject.getInt("errcode");
			if(result != 0){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 移动用户所属组
	 * @return  是否移动成功
	 */
	public boolean updateUserGroup(String[] openIds,long groupId){
		//获取access token
		AccessToken accessToken = AccessToken.getInstance();
		//创建菜单
		String url = UPDATE_USER_GROUP.replace("ACCESS_TOKEN", accessToken.getAccess_token());
		UserGroup userGroup = new UserGroup();
		userGroup.setOpenid_list(openIds);
		userGroup.setTo_groupid(groupId);
		JSON json = (JSON)JSON.toJSON(userGroup);
		System.out.println(json.toString());
		JSONObject resultJsonObject = HttpUtil.getJsonResponseByHttpPost(url, json.toString());
		System.out.println(resultJsonObject.toString());
		if(null != resultJsonObject){
			int result = resultJsonObject.getInt("errcode");
			if(result != 0){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 查询用户所属组
	 * @return  用户所属组id
	 */
	public long queryUserGroupByOpenId(String openId){
		//获取access token
		AccessToken accessToken = AccessToken.getInstance();
		//创建菜单
		String url = QUERY_USER_GROUP.replace("ACCESS_TOKEN", accessToken.getAccess_token());
		String jsonString = "{\"openid\":\"" + openId + "\"}";
		JSONObject resultJsonObject = HttpUtil.getJsonResponseByHttpPost(url, jsonString);
		if(null != resultJsonObject){
			long result = resultJsonObject.getLong("groupid");
			return result;
		}
		return 0;
	}
	
	/**
	 * 创建微信菜单
	 * @return  是否创建成功
	 */
	public boolean createMenu(){
		//获取access token
		AccessToken accessToken = AccessToken.getInstance();
		//创建菜单
		String url = CREATE_MENU.replace("ACCESS_TOKEN", accessToken.getAccess_token());
		Menu menu = MenuUtil.initMenu();
		JSON json = (JSON)JSON.toJSON(menu);
		JSONObject resultJsonObject = HttpUtil.getJsonResponseByHttpPost(url, json.toString());
		System.out.println(resultJsonObject.toString());
		if(null != resultJsonObject){
			int result = resultJsonObject.getInt("errcode");
			if(result != 0){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 查询微信菜单
	 * @return  微信菜单json数据
	 */
	public JSONObject queryMenu(){
		//获取access token
		AccessToken accessToken = AccessToken.getInstance();
		//创建菜单
		String url = QUERY_MENU.replace("ACCESS_TOKEN", accessToken.getAccess_token());
		JSONObject resultJsonObject = HttpUtil.getJsonResponseByHttpGet(url);
		
		return resultJsonObject;
	}
	
	/**
	 * 删除微信菜单
	 * @return  是否删除成功
	 */
	public boolean deleteMenu(){
		//获取access token
		AccessToken accessToken = AccessToken.getInstance();
		//创建菜单
		String url = DELETE_MENU.replace("ACCESS_TOKEN", accessToken.getAccess_token());
		JSONObject resultJsonObject = HttpUtil.getJsonResponseByHttpGet(url);
		System.out.println(resultJsonObject.toString());
		if(null != resultJsonObject){
			int result = resultJsonObject.getInt("errcode");
			if(result != 0){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 查询微信号关注用户列表
	 * @return  关注用户列表
	 */
	public UserList queryUserList(String nextOpenId){
		UserList userList = new UserList();
		//获取access token
		AccessToken accessToken = AccessToken.getInstance();
		//创建菜单
		String url = USER_LIST.replace("ACCESS_TOKEN", accessToken.getAccess_token()).replace("NEXT_OPENID", nextOpenId);
		JSONObject resultJsonObject = HttpUtil.getJsonResponseByHttpGet(url);
		System.out.println(resultJsonObject);
		if(null!=resultJsonObject){
			userList.setCount(resultJsonObject.getInt("count"));
			userList.setTotal(resultJsonObject.getInt("total"));
			userList.setNext_openid((String)resultJsonObject.get("next_openid"));
			JSONObject data = resultJsonObject.getJSONObject("data");
			if(null!=data){
				JSONArray array = data.getJSONArray("openid");
				if(null!=array&&array.size()>0){
					List<UserInfo> users = new ArrayList<UserInfo>();
					for(int i=0;i<array.size();i++){
						String openId = array.getString(i);
						UserInfo user = queryUserInfo(openId);
						users.add(user);
					}
					userList.setUsers(users);
				}
			}
		}
		return userList;
	}
	
	/**
	 * 查询微信用户详细信息
	 * @return  微信用户详细信息
	 */
	public UserInfo queryUserInfo(String openId){
		UserInfo user = null;
		//获取access token
		AccessToken accessToken = AccessToken.getInstance();
		//创建菜单
		String url = USER_INFO.replace("ACCESS_TOKEN", accessToken.getAccess_token()).replace("OPENID", openId);
		JSONObject resultJsonObject = HttpUtil.getJsonResponseByHttpGet(url);
		System.out.println(resultJsonObject);
		if(null!=resultJsonObject){
			user = new UserInfo();
			user.setOpenid((String)resultJsonObject.get("openid"));
			user.setHeadimgurl((String)resultJsonObject.get("headimgurl"));
			user.setNickname((String)resultJsonObject.get("nickname"));
			user.setSubscribe(String.valueOf(resultJsonObject.get("subscribe")));
		}
		return user;
	}
	
	/**
	 * 设置微信公众号所属行业
	 * 行业id在如下微信api中查找
	 * http://mp.weixin.qq.com/wiki/17/304c1885ea66dbedf7dc170d84999a9d.html
	 * @param industryId1  行业id1
	 * @param industryId2  行业id2
	 */
	public void setIndustry(String industryId1,String industryId2){
		//获取access token
		AccessToken accessToken = AccessToken.getInstance();
		String url = SET_INDUSTRY.replace("ACCESS_TOKEN", accessToken.getAccess_token());
		String json = "{\"industry_id1\":\"" + industryId1 + "\",\"industry_id2\":\"" + industryId2 + "\"}";
		JSONObject resultJsonObject = HttpUtil.getJsonResponseByHttpPost(url, json);
		System.out.println(resultJsonObject);
	}
	
	/**
	 * 获得模板消息的模板id
	 * @param template_id_short  模板库中模板的编号，有“TM**”和“OPENTMTM**”等形式
	 * @return   模板id
	 */
	public String getTemplateMessageId(String template_id_short){
		String templateId = "";
		//获取access token
		AccessToken accessToken = AccessToken.getInstance();
		String url = GET_TEMPLATEID.replace("ACCESS_TOKEN", accessToken.getAccess_token());
		String json = "{\"template_id_short\":\"" + template_id_short + "\"}";
		JSONObject resultJsonObject = HttpUtil.getJsonResponseByHttpPost(url, json);
		if(null!=resultJsonObject){
			templateId = resultJsonObject.getString("template_id");
		}
		return templateId;
	}
	
	/**
	 * 发送模板消息
	 * @param toOpenId  发送对象的微信id
	 * @param param   发送的模板消息中的参数
	 * @param templateUrl 点击模板消息之后跳转的url
	 * @return
	 */
	public boolean sendTemplateMessage(String toOpenId,Map<String, TemplateMessageParamInfo> param,String templateUrl){
		
		//获取access token
		AccessToken accessToken = AccessToken.getInstance();
		String url = SEND_TEMPLATE_MESSAGE.replace("ACCESS_TOKEN", accessToken.getAccess_token());
		
		//微信测试号的模板id可以在测试号中新建，但是实际的公众号的模板id是需要在微信公众号中设置所属行业,通过所属行业才能新建模板获得模板id
		String templateId = "32RzJ6emuH05TBVY8oPOlAySYFKkb5GD0HwJIZiEd5w";
		//构建模板消息
		TemplateMessage message = new TemplateMessage();
		message.setTemplate_id(templateId);
		message.setTouser(toOpenId);
		message.setUrl(templateUrl);
		message.setData(param);
		
		JSON json = (JSON)JSON.toJSON(message);
		JSONObject resultJsonObject = HttpUtil.getJsonResponseByHttpPost(url, json.toString());
		System.out.println(resultJsonObject.toString());
		if(null != resultJsonObject){
			int result = resultJsonObject.getInt("errcode");
			if(result != 0){
				return false;
			}
		}
		return true;
		
	}
	
	/**
	 * 创建微信二维码
	 * @param expireTime   二维码过期时间,单位:秒
	 * @param sceneMsg   二维码中消息
	 * @return  微信二维码
	 */
	public QrSceneInfo createQrscene(long expireTime,String sceneMsg){
		QrSceneInfo qrSceneInfo = null;
		//获取access token
		AccessToken accessToken = AccessToken.getInstance();
		String url = CREATE_QRSCENE.replace("ACCESS_TOKEN", accessToken.getAccess_token());
		//创建二维码信息
		StringBuffer sb = new StringBuffer();
		long id = 0L;
		if(expireTime>0){
			//临时二维码
			try {
				id = Long.parseLong(sceneMsg);
			} catch (Exception e) {
				throw new NumberFormatException("临时的sceneMsg必须为32位非0整型");
			}
			sb.append("{\"expire_seconds\": " + expireTime + ", \"action_name\": \"QR_SCENE\", \"action_info\": {\"scene\": {\"scene_id\": " + id + "}}}");
		}else{
			//永久二维码
			try {
				id = Long.parseLong(sceneMsg);
				sb.append("{\"action_name\": \"QR_LIMIT_SCENE\", \"action_info\": {\"scene\": {\"scene_id\": " + id + "}}}");
			} catch (Exception e) {
				sb.append("{\"action_name\": \"QR_LIMIT_STR_SCENE\", \"action_info\": {\"scene\": {\"scene_str\": \"" + sceneMsg + "\"}}}");
			}
			
		}
		JSONObject resultJsonObject = HttpUtil.getJsonResponseByHttpPost(url, sb.toString());
		System.out.println(resultJsonObject.toString());
		if(null!=resultJsonObject){
			qrSceneInfo = (QrSceneInfo)JSONObject.toBean(resultJsonObject, QrSceneInfo.class);
		}
		return qrSceneInfo;
	}
	
	public boolean getQrCode(String ticket,String filePath){
		String url = null;
		InputStream in = null;
		try {
			url = GET_QRCODE.replace("TICKET", URLEncoder.encode(ticket,"UTF-8"));
			byte[] data = HttpUtil.getResponseByHttpGet(url);
			FileUtil.generateFile(filePath,data);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(in!=null){
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return false;
	}
	
	/**
	 * 将一条长链接转成短链接
	 * @param originalUrl  需要转换的长链接url
	 * @return
	 */
	public String transferUrl(String originalUrl){
		//获取access token
		AccessToken accessToken = AccessToken.getInstance();
		String url = TRANSFER_URL.replace("ACCESS_TOKEN", accessToken.getAccess_token());
		//创建请求数据
		StringBuffer sb = new StringBuffer();
		sb.append("{\"action\":\"long2short\",\"long_url\":\"" + originalUrl + "\"}");
		JSONObject resultJsonObject = HttpUtil.getJsonResponseByHttpPost(url, sb.toString());
		System.out.println(resultJsonObject.toString());
		if(null!=resultJsonObject){
			int result = resultJsonObject.getInt("errcode");
			if(result == 0){
				return resultJsonObject.getString("short_url");
			}
		}
		return null;
	}
	
	/**
	 * 获取微信网页授权的code
	 * @param  redirectUrl   需要被code重定向的url链接
	 * @param scope  应用授权作用域，snsapi_base,snsapi_userinfo 
	 * @param dataInfo   重定向后会带上state参数，开发者可以填写a-zA-Z0-9的参数值，最多128字节
	 * @return  微信网页授权的code
	 */
	public String getPageCodeUrl(String redirectUrl,String scope,String dataInfo)throws UnsupportedEncodingException{
		String pageCodeUrl = GET_PAGE_CODE.replace("APPID", APPID).replace("REDIRECT_URI", URLEncoder.encode(redirectUrl, "UTF-8")).replace("SCOPE", scope).replace("STATE", dataInfo);
		System.out.println(pageCodeUrl);
		return pageCodeUrl;
	}
	
	/**
	 * 获取微信网页的access_token,有超时时间
	 * @param code  微信网页授权的code
	 * @return
	 */
	public AccessTokenPage getPageAccessToken(String code){
		AccessTokenPage accessTokenPage = null;
		String url = GET_PAGE_ACCESSTOKEN.replace("APPID", APPID).replace("SECRET", APPSECRET).replace("CODE", code);
		JSONObject resultJsonObject = HttpUtil.getJsonResponseByHttpGet(url);
		System.out.println(resultJsonObject);
		if(null!=resultJsonObject){
			String access_token = resultJsonObject.getString("access_token");
			if(null != access_token && !"".equals(access_token)){
				accessTokenPage = new AccessTokenPage();
				accessTokenPage.setAccess_token(access_token);
				accessTokenPage.setCreateTimestamp(System.currentTimeMillis());
				accessTokenPage.setExpires_in(resultJsonObject.getLong("expires_in"));
				accessTokenPage.setRefresh_token(resultJsonObject.getString("refresh_token"));
				accessTokenPage.setScope(resultJsonObject.getString("scope"));
				//accessTokenPage.setUnionid(resultJsonObject.getString("unionid"));
				accessTokenPage.setOpenid(resultJsonObject.getString("openid"));
			}
		}
		return accessTokenPage;
	}
	
	/**
	 * 通过微信网页access_token和微信openId获取微信用户信息
	 * @param openId  微信openId
	 * @param accessTokenPage  微信网页access_token
	 * @return
	 */
	public UserInfo getUserInfoInPage(String openId,String accessTokenPage){
		UserInfo userInfo = null;
		String url = GET_PAGE_USERINFO.replace("ACCESS_TOKEN", accessTokenPage).replace("OPENID", openId);
		JSONObject resultJsonObject = HttpUtil.getJsonResponseByHttpGet(url);
		System.out.println(resultJsonObject);
		if(null!=resultJsonObject){
			String nickname = resultJsonObject.getString("nickname");
			if(null!=nickname&&!"".equals(nickname)){
				userInfo = new UserInfo();
				userInfo.setHeadimgurl(resultJsonObject.getString("headimgurl"));
				userInfo.setNickname(nickname);
				userInfo.setOpenid(openId);
			}
		}
		return userInfo;
	}
	
	/**
	 * 通过微信openId判断当前微信网页  AccessToken是否有效
	 * @param openId  微信openId
	 * @param accessTokenPage  	当前微信网页access_token
	 * @return
	 */
	public boolean isAccessTokenPageIsEffect(String openId,String accessTokenPage){
		String url = PAGE_ACCESSTOKEN_EFFECT.replace("ACCESS_TOKEN", accessTokenPage).replace("OPENID", openId);
		JSONObject resultJsonObject = HttpUtil.getJsonResponseByHttpGet(url);
		System.out.println(resultJsonObject);
		if(null!=resultJsonObject){
			String errmsg = resultJsonObject.getString("errmsg");
			if("ok".equals(errmsg)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 刷新微信网页access_token
	 * @param refreshToken
	 * @return
	 */
	public AccessTokenPage refreshPageAccessToken(String refreshToken){
		AccessTokenPage accessTokenPage = null;
		String url = PAGE_ACCESSTOKEN_REFRESH.replace("APPID", APPID).replace("REFRESH_TOKEN", refreshToken);
		JSONObject resultJsonObject = HttpUtil.getJsonResponseByHttpGet(url);
		System.out.println(resultJsonObject);
		if(null!=resultJsonObject){
			String access_token = resultJsonObject.getString("access_token");
			if(null != access_token && !"".equals(access_token)){
				accessTokenPage = new AccessTokenPage();
				accessTokenPage.setAccess_token(access_token);
				accessTokenPage.setCreateTimestamp(System.currentTimeMillis());
				accessTokenPage.setExpires_in(resultJsonObject.getLong("expires_in"));
				accessTokenPage.setRefresh_token(resultJsonObject.getString("refresh_token"));
				accessTokenPage.setScope(resultJsonObject.getString("scope"));
				//accessTokenPage.setUnionid(resultJsonObject.getString("unionid"));
				accessTokenPage.setOpenid(resultJsonObject.getString("openid"));
			}
		}
		return accessTokenPage;
	}
	
	private String getMainReplayInfo(){
		StringBuffer str = new StringBuffer();
		str.append("欢迎关注永远的利物浦,请按照菜单提示进行操作:\n\n");
		str.append("1、利物浦俱乐部介绍\n");
		str.append("2、利物浦球员介绍\n");
		str.append("3、利物浦官网消息\n");
		str.append("4、利物浦图片\n");
		str.append("5、利物浦队歌\n\n");
		str.append("回复?调出此菜单");
		return str.toString();
	}

}
