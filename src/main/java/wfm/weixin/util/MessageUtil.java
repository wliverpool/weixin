package wfm.weixin.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import wfm.weixin.vo.EventMessage;
import wfm.weixin.vo.LocationMessage;
import wfm.weixin.vo.Message;
import wfm.weixin.vo.QrCodeMessage;
import wfm.weixin.vo.TemplateMsgSendFinishMessage;
import wfm.weixin.vo.TextMessage;
import wfm.weixin.vo.request.ImageReqMessage;
import wfm.weixin.vo.request.VoiceReqMessage;
import wfm.weixin.vo.response.Music;
import wfm.weixin.vo.response.News;
import wfm.weixin.vo.response.Video;

/**
 * 微信消息的工具类
 * 微信回复消息api
 * http://mp.weixin.qq.com/wiki/9/2c15b20a16019ae613d413e30cac8ea1.html#.E5.9B.9E.E5.A4.8D.E5.9B.BE.E6.96.87.E6.B6.88.E6.81.AF
 * @author 吴福明
 *
 */
public class MessageUtil {
	//消息类型常量
	public static final String MESSAGE_TEXT = "text";//文本消息
	public static final String MESSAGE_IMAGE = "image";//图片消息
	public static final String MESSAGE_VOICE = "voice";//语音消息
	public static final String MESSAGE_VIDEO = "video";//视频消息
	public static final String MESSAGE_SHORTVIDEO = "shortvideo";//小视频消息
	public static final String MESSAGE_LINK = "link";//链接消息
	public static final String MESSAGE_LOCATION = "location";//地理位置消息
	public static final String MESSAGE_EVENT = "event";//接收事件消息
	//以下事件都是MsgType="event"时,会产生的子事件分类在消息的xml中会多产生一个标签<Event></Event>
	public static final String MESSAGE_EVENT_SUBSRIBE = "subscribe";//关注事件消息
	public static final String MESSAGE_EVENT_UNSUBSCRIBE = "unsubscribe";//取消事件消息
	public static final String MESSAGE_EVENT_LOCATION = "LOCATION";//上报地理位置事件消息
	public static final String MESSAGE_EVENT_CLICK = "CLICK";//自定义菜单CLICK事件消息
	public static final String MESSAGE_EVENT_VIEW = "VIEW";//菜单跳转链接时的事件消息
	public static final String MESSAGE_EVENT_SCANCODE_PUSH = "scancode_push";//自定义菜单扫码推事件消息
	public static final String MESSAGE_EVENT_TEMPLATEMSGFINISH = "TEMPLATESENDJOBFINISH";//模版消息发送任务完成事件消息
	public static final String MESSAGE_EVENT_SCAN = "SCAN";//用户已关注时的事件消息
	
	
	/**
	 * 微信xml的消息信息转换为Message类型
	 * @param in  xml格式输入流
	 * @return  message类型的子类
	 * @throws IOException
	 * @throws DocumentException
	 */
	public static Message xmlToMessage(InputStream in)throws IOException,DocumentException{
		
		Message message = null;
		Map<String, String> messageMap = new HashMap<String,String>();
		//读取并解析xml格式
		SAXReader reader = new SAXReader();
		Document doc = reader.read(in);
		Element root = doc.getRootElement();
		List<Element> list = root.elements();
		
		for(Element e : list){
			messageMap.put(e.getName(), e.getText());
		}
		System.out.println("messageMap:"+messageMap);
		if(MessageUtil.MESSAGE_TEXT.equals(messageMap.get("MsgType"))){
			message = createTextMessageByMap(messageMap);
		}else if(MessageUtil.MESSAGE_EVENT.equals(messageMap.get("MsgType"))){
			if(MessageUtil.MESSAGE_EVENT_TEMPLATEMSGFINISH.equals(messageMap.get("Event"))){
				message = createTemplateMsgSendFinishMessageByMap(messageMap);
			}else if(MessageUtil.MESSAGE_EVENT_SCAN.equals(messageMap.get("Event"))){
				message = createQrCodeMessageByMap(messageMap);
			}else{
				message = createEventMessageByMap(messageMap);
			}
		}else if(MessageUtil.MESSAGE_LOCATION.equals(messageMap.get("MsgType"))){
			message = createLocationMessageByMap(messageMap);
		}else if(MessageUtil.MESSAGE_VOICE.equals(messageMap.get("MsgType"))){
			message = createVoiceMessageByMap(messageMap);
		}else if(MessageUtil.MESSAGE_IMAGE.equals(messageMap.get("MsgType"))){
			message = createImageMessageByMap(messageMap);
		}
		
		return message;
		
	}
	
	/**
	 * 生成文本消息
	 * @param toUserName   消息发送的目标微信用户id
	 * @param fromUserName   消息发出的微信用户id
	 * @param content   文本消息内容
	 * @return  xml字符串格式的微信文本消息
	 */
	public static String generateXmlFormatTextMessage(String fromUserName,String toUserName,String content){
		StringBuffer str = new StringBuffer();  
        str.append("<xml>");  
        str.append("<ToUserName><![CDATA[" + toUserName + "]]></ToUserName>");  
        str.append("<FromUserName><![CDATA[" + fromUserName + "]]></FromUserName>");  
        str.append("<CreateTime>" + Calendar.getInstance().getTimeInMillis()/1000 + "</CreateTime>");  
        str.append("<MsgType><![CDATA[text]]></MsgType>");  
        str.append("<Content><![CDATA[" + content + "]]></Content>");  
        str.append("</xml>");  
        return str.toString();
	}
	
	/**
	 * 生成语音消息
	 * @param fromUserName  消息发送的目标微信用户id
	 * @param toUserName   消息发出的微信用户id
	 * @param mediaId   语音文件的素材id
	 * @return  xml字符串格式的微信语音消息
	 */
	public static String generateXmlFormatVoiceMessage(String fromUserName,String toUserName,String mediaId){
		StringBuffer str = new StringBuffer();  
		str.append("<xml>");  
        str.append("<ToUserName><![CDATA[" + toUserName + "]]></ToUserName>");  
        str.append("<FromUserName><![CDATA[" + fromUserName + "]]></FromUserName>");  
        str.append("<CreateTime>" + Calendar.getInstance().getTimeInMillis()/1000 + "</CreateTime>");  
        str.append("<MsgType><![CDATA[voice]]></MsgType>");  
        str.append("<Voice><MediaId><![CDATA[" + mediaId + "]]></MediaId></Voice>");  
        str.append("</xml>");  
        return str.toString();
	}
	
	/**
	 * 生成图片消息
	 * @param fromUserName  消息发送的目标微信用户id
	 * @param toUserName   消息发出的微信用户id
	 * @param mediaId   图片文件的素材id
	 * @return  xml字符串格式的微信图片消息
	 */
	public static String generateXmlFormatImageMessage(String fromUserName,String toUserName,String mediaId){
		StringBuffer str = new StringBuffer();  
		str.append("<xml>");  
        str.append("<ToUserName><![CDATA[" + toUserName + "]]></ToUserName>");  
        str.append("<FromUserName><![CDATA[" + fromUserName + "]]></FromUserName>");  
        str.append("<CreateTime>" + Calendar.getInstance().getTimeInMillis()/1000 + "</CreateTime>");  
        str.append("<MsgType><![CDATA[image]]></MsgType>");  
        str.append("<Image><MediaId><![CDATA[" + mediaId + "]]></MediaId></Image>");  
        str.append("</xml>");  
        return str.toString();
	}
	
	/**
	 * 生成微信图文消息
	 * @param fromUserName  消息发送的目标微信用户id
	 * @param toUserName   消息发出的微信用户id
	 * @param newsList   图文信息list
	 * @return   xml字符串格式的微信图文消息
	 */
	public static String generateXmlFormatNewsMessage(String fromUserName,String toUserName,List<News> newsList){
		StringBuffer str = new StringBuffer();  
        str.append("<xml>");  
        str.append("<ToUserName><![CDATA[" + toUserName + "]]></ToUserName>");  
        str.append("<FromUserName><![CDATA[" + fromUserName + "]]></FromUserName>");  
        str.append("<CreateTime>" + Calendar.getInstance().getTimeInMillis()/1000 + "</CreateTime>");  
        str.append("<MsgType><![CDATA[news]]></MsgType>");  
        str.append("<ArticleCount>" + newsList.size() + "</ArticleCount>");
        str.append("<Articles>");
        for(int i = 0;i < newsList.size();i++){
        	str.append("<item>");
        	str.append("<Title><![CDATA[" + newsList.get(i).getTitle() + "]]></Title>");
        	str.append("<Description>" + newsList.get(i).getDescription() + "</Description>");
        	str.append("<PicUrl>" + newsList.get(i).getPicUrl() + "</PicUrl>");
        	str.append("<Url>" + newsList.get(i).getUrl() + "</Url>");
        	str.append("</item>");
        }
        str.append("</Articles>");
        str.append("</xml>");  
		return str.toString();
	}
	
	/**
	 * 生成微信音乐消息
	 * @param fromUserName  消息发送的目标微信用户id
	 * @param toUserName    消息发出的微信用户id
	 * @param music  微信音乐文件
	 * @return  xml字符串格式的微信音乐消息
	 */
	public static String generateXmlFormatMusicMessage(String fromUserName,String toUserName,Music music){
		
		StringBuffer str = new StringBuffer();  
        str.append("<xml>");  
        str.append("<ToUserName><![CDATA[" + toUserName + "]]></ToUserName>");  
        str.append("<FromUserName><![CDATA[" + fromUserName + "]]></FromUserName>");  
        str.append("<CreateTime>" + Calendar.getInstance().getTimeInMillis()/1000 + "</CreateTime>");  
        str.append("<MsgType><![CDATA[music]]></MsgType>");  
        str.append("<Music>");
        str.append("<Title><![CDATA[" + music.getTitle() + "]]></Title>");
        str.append("<Description><![CDATA[" + music.getDescription() + "]]></Description>");
        str.append("<MusicUrl><![CDATA[" + music.getMusicUrl() + "]]></MusicUrl>");
        str.append("<HQMusicUrl><![CDATA[" + music.gethQMusicUrl() + "]]></HQMusicUrl>");
        str.append("<ThumbMediaId><![CDATA[" + music.getThumbMediaId() + "]]></ThumbMediaId>");
        str.append("</Music>");
        str.append("</xml>");  
		return str.toString();
		
	}
	
	/**
	 * 生成微信视频消息
	 * @param fromUserName  消息发送的目标微信用户id
	 * @param toUserName    消息发出的微信用户id
	 * @param video  微信视频文件
	 * @return  xml字符串格式的微信视频消息
	 */
	public static String generateXmlFormatVideoMessage(String fromUserName,String toUserName,Video video){
		
		StringBuffer str = new StringBuffer();  
        str.append("<xml>");  
        str.append("<ToUserName><![CDATA[" + toUserName + "]]></ToUserName>");  
        str.append("<FromUserName><![CDATA[" + fromUserName + "]]></FromUserName>");  
        str.append("<CreateTime>" + Calendar.getInstance().getTimeInMillis()/1000 + "</CreateTime>");  
        str.append("<MsgType><![CDATA[video]]></MsgType>");  
        str.append("<Video>");
        str.append("<Title><![CDATA[" + video.getTitle() + "]]></Title>");
        str.append("<Description><![CDATA[" + video.getDescription() + "]]></Description>");
        str.append("<MediaId><![CDATA[" + video.getMediaId() + "]]></MediaId>");
        str.append("</Video>");
        str.append("</xml>");  
		return str.toString();
		
	}
	
	/**
	 * 根据map格式的微信消息生成对应的文本消息
	 * @param messageMap  map格式的微信消息
	 * @return  生成文本消息类型
	 */
	private static TextMessage createTextMessageByMap(Map<String, String> messageMap){
		TextMessage message = new TextMessage();
		message.setCreateTime(messageMap.get("CreateTime"));
		message.setFromUserName(messageMap.get("FromUserName"));
		message.setToUserName(messageMap.get("ToUserName"));
		message.setMsgType(MessageUtil.MESSAGE_TEXT);
		message.setContent(messageMap.get("Content"));
		message.setMsgId(messageMap.get("MsgId"));
		return message;
	}
	
	/**
	 * 根据map格式的微信消息生成对应的文本消息
	 * @param messageMap  map格式的微信消息
	 * @return  生成文本消息类型
	 */
	private static EventMessage createEventMessageByMap(Map<String, String> messageMap){
		EventMessage message = new EventMessage();
		message.setCreateTime(messageMap.get("CreateTime"));
		message.setFromUserName(messageMap.get("FromUserName"));
		message.setToUserName(messageMap.get("ToUserName"));
		message.setMsgType(MessageUtil.MESSAGE_EVENT);
		message.setEvent(messageMap.get("Event"));
		message.setEventKey(messageMap.get("EventKey"));
		return message;
	}
	
	/**
	 * 根据map格式的微信消息生成对应的地理位置消息
	 * @param messageMap  map格式的微信消息
	 * @return  生成地理位置消息类型
	 */
	private static LocationMessage createLocationMessageByMap(Map<String, String> messageMap){
		LocationMessage message = new LocationMessage();
		message.setCreateTime(messageMap.get("CreateTime"));
		message.setFromUserName(messageMap.get("FromUserName"));
		message.setToUserName(messageMap.get("ToUserName"));
		message.setMsgType(MessageUtil.MESSAGE_LOCATION);
		message.setLabel(messageMap.get("Label"));
		message.setLocationX(messageMap.get("Location_X"));
		message.setLocationY(messageMap.get("Location_Y"));
		message.setScale(messageMap.get("Scale"));
		message.setMsgId(messageMap.get("MsgId"));
		return message;
	}
	
	/**
	 * 根据map格式的微信消息生成对应的语音消息
	 * @param messageMap  map格式的微信消息
	 * @return  生成语音消息类型
	 */
	private static VoiceReqMessage createVoiceMessageByMap(Map<String, String> messageMap){
		VoiceReqMessage message = new VoiceReqMessage();
		message.setCreateTime(messageMap.get("CreateTime"));
		message.setFromUserName(messageMap.get("FromUserName"));
		message.setToUserName(messageMap.get("ToUserName"));
		message.setMsgType(MessageUtil.MESSAGE_VOICE);
		message.setFormat(messageMap.get("Format"));
		message.setMediaId(messageMap.get("MediaId"));
		message.setRecognition(messageMap.get("Recognition"));
		message.setMsgId(messageMap.get("MsgId"));
		return message;
	}
	
	/**
	 * 根据map格式的微信消息生成对应的语音消息
	 * @param messageMap  map格式的微信消息
	 * @return  生成语音消息类型
	 */
	private static ImageReqMessage createImageMessageByMap(Map<String, String> messageMap){
		ImageReqMessage message = new ImageReqMessage();
		message.setCreateTime(messageMap.get("CreateTime"));
		message.setFromUserName(messageMap.get("FromUserName"));
		message.setToUserName(messageMap.get("ToUserName"));
		message.setMsgType(MessageUtil.MESSAGE_IMAGE);
		message.setPicUrl(messageMap.get("PicUrl"));
		message.setMediaId(messageMap.get("MediaId"));
		message.setMsgId(messageMap.get("MsgId"));
		return message;
	}
	
	/**
	 * 根据map格式的微信消息生成对应的模版消息发送任务完成事件消息
	 * @param messageMap  map格式的微信消息
	 * @return  生成模版消息发送任务完成事件消息
	 */
	private static TemplateMsgSendFinishMessage createTemplateMsgSendFinishMessageByMap(Map<String, String> messageMap){
		TemplateMsgSendFinishMessage message = new TemplateMsgSendFinishMessage();
		message.setCreateTime(messageMap.get("CreateTime"));
		message.setFromUserName(messageMap.get("FromUserName"));
		message.setToUserName(messageMap.get("ToUserName"));
		message.setMsgType(MessageUtil.MESSAGE_EVENT);
		message.setEvent(messageMap.get("Event"));
		message.setStatus(messageMap.get("Status"));
		return message;
	}
	
	/**
	 * 根据map格式的微信消息生成对应的二维码扫描事件消息
	 * @param messageMap  map格式的微信消息
	 * @return  生成二维码扫描事件消息
	 */
	private static QrCodeMessage createQrCodeMessageByMap(Map<String, String> messageMap){
		QrCodeMessage message = new QrCodeMessage();
		message.setCreateTime(messageMap.get("CreateTime"));
		message.setFromUserName(messageMap.get("FromUserName"));
		message.setToUserName(messageMap.get("ToUserName"));
		message.setMsgType(MessageUtil.MESSAGE_EVENT);
		message.setEvent(messageMap.get("Event"));
		message.setEventKey(messageMap.get("EventKey"));
		message.setTicket(messageMap.get("Ticket"));
		return message;
	}

}
