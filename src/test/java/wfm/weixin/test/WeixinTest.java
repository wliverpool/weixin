package wfm.weixin.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import net.sf.json.JSONObject;
import wfm.weixin.business.service.WeixinService;
import wfm.weixin.vo.AccessToken;
import wfm.weixin.vo.AccessTokenPage;
import wfm.weixin.vo.Group;
import wfm.weixin.vo.JsApiTicket;
import wfm.weixin.vo.QrSceneInfo;
import wfm.weixin.vo.TemplateMessageParamInfo;
import wfm.weixin.vo.UserInfo;
import wfm.weixin.vo.UserList;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring.xml"})
public class WeixinTest {

	@Autowired
	private WeixinService weixinService;
	/*
	@Test
	public void testGetAccessTokenByHttpGet(){
		long start = System.currentTimeMillis();
		AccessToken token = AccessToken.getInstance();
		long end1 = System.currentTimeMillis();
		System.out.println("票据:"+token.getAccess_token());
		System.out.println("有效时间:"+token.getExpires_in());
		assertEquals(7200, token.getExpires_in());
		AccessToken token2 = AccessToken.getInstance();
		long end2 = System.currentTimeMillis();
		System.out.println("end1-start:"+(end1-start));
		System.out.println("end2-end1:"+(end2-end1));
		System.out.println("票据2:"+token2.getAccess_token());
		System.out.println("有效时间2:"+token2.getExpires_in());
	}
	
	@Test
	public void testUploadFile()throws FileNotFoundException{
		String mediaId = weixinService.uploadFile("C:\\Users\\Mittermeyer\\Pictures\\Saved Pictures\\lfc_crest.png", "thumb");
		assertNotNull(mediaId);
		//DpotLtIgBuWwCI0Ra6pfKDmReuAZ2YCK-4oUYDHusuDISxKzIHN1psJ5RY_S0GDv
		System.out.println(mediaId);
	}
	
	@Test
	public void testCreateMenu(){
		boolean flag = weixinService.createMenu();
		assertTrue(flag);
	}
	
	@Test
	public void testQueryMenu(){
		JSONObject jsonObject = weixinService.queryMenu();
		System.out.println(jsonObject.toString());
		assertNotNull(jsonObject);
	}
	
	@Test
	public void testDeleteMenu(){
		boolean flag = weixinService.deleteMenu();
		assertTrue(flag);
	}
	
	@Test
	public void testCreateGroup(){
		Group group = weixinService.createGroupByName("分组2");
		assertNotNull(group);
		assertNotNull(group.getId());
		System.out.println("id:"+group.getId()+";name:"+group.getName());
	}

	@Test
	public void testQueryGroup(){
		Group[] groups = weixinService.queryGroup();
		assertNotNull(groups);
		assertTrue(groups.length>0);
		for(Group g : groups){
			System.out.println("id:"+g.getId()+";name:"+g.getName());
		}
	}
	
	@Test
	public void testDeleteGroup(){
		boolean flag = weixinService.deleteGroupById(101L);
		assertTrue(flag);
	}
	
	@Test
	public void testUpdateGroup(){
		boolean flag = weixinService.updateGroupById(102L, "修改名称");
		assertTrue(flag);
	}
	
	@Test
	public void testQueryUserInfo(){
		UserInfo user = weixinService.queryUserInfo("oGUsUwCLHXFgODBNzHhRoA_0E3w8");
		assertNotNull(user);
		System.out.println(user.getOpenid()+";"+user.getNickname()+";"+user.getHeadimgurl()+";"+user.getSubscribe());
	}
	
	@Test
	public void testQueryUserList(){
		UserList list = weixinService.queryUserList("");
		assertNotNull(list);
		assertTrue(list.getTotal()>0);
		assertTrue(list.getCount()>0);
		assertNotNull(list.getUsers());
		assertTrue(list.getUsers().size()>0);
		List<UserInfo> users = list.getUsers();
		System.out.println(list.getTotal());
		System.out.println(list.getCount());
		for(UserInfo user : users){
			System.out.println(user.getOpenid()+";"+user.getNickname()+";"+user.getHeadimgurl()+";"+user.getSubscribe());
		}
		System.out.println(list.getNext_openid());
	}
	
	@Test
	public void testUpdateUserGroup(){
		String[] openIds = new String[]{"oGUsUwCLHXFgODBNzHhRoA_0E3w8"};
		long groupId = 102L;
		boolean flag = weixinService.updateUserGroup(openIds, groupId);
		assertTrue(flag);
	}
	
	@Test
	public void testQueryUserGroup(){
		long groupId = weixinService.queryUserGroupByOpenId("oGUsUwCLHXFgODBNzHhRoA_0E3w8");
		assertTrue(groupId>0);
		System.out.println("groupId:"+groupId);
	}
	
	@Test
	public void testSendTemplateMessage(){
		UserInfo user = weixinService.queryUserInfo("oGUsUwCLHXFgODBNzHhRoA_0E3w8");
		
		//构建模板消息参数
		Map<String, TemplateMessageParamInfo> param = new HashMap<String, TemplateMessageParamInfo>();
		TemplateMessageParamInfo nameParamInfo = new TemplateMessageParamInfo();
		nameParamInfo.setValue(user.getNickname());
		nameParamInfo.setColor("#173177");
		
		TemplateMessageParamInfo moneyParamInfo = new TemplateMessageParamInfo();
		moneyParamInfo.setValue("100.34");
		moneyParamInfo.setColor("#173177");
		
		TemplateMessageParamInfo dateParamInfo = new TemplateMessageParamInfo();
		dateParamInfo.setValue(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		dateParamInfo.setColor("#173177");
		
		param.put("name", nameParamInfo);
		param.put("date", dateParamInfo);
		param.put("money", moneyParamInfo);
		
		boolean flag = weixinService.sendTemplateMessage("oGUsUwCLHXFgODBNzHhRoA_0E3w8", param, "cn.liverpoolfc.com");
		assertTrue(flag);
	}
	
	@Test
	public void testCreateQrScene(){
		QrSceneInfo info = weixinService.createQrscene(0, "tteasadflkafjd");
		assertNotNull(info);
		//{"ticket":"gQH48DoAAAAAAAAAASxodHRwOi8vd2VpeGluLnFxLmNvbS9xL1Rqc1pqcDNtXzVPQ0JYRFRlUlBkAAIECabHVwMEAAAAAA==","url":"http://weixin.qq.com/q/TjsZjp3m_5OCBXDTeRPd"}
		System.out.println(info.getTicket());//
		System.out.println(info.getUrl());//
	}
	
	@Test
	public void testGetQrCode()throws UnsupportedEncodingException{
		boolean flag = weixinService.getQrCode("gQH48DoAAAAAAAAAASxodHRwOi8vd2VpeGluLnFxLmNvbS9xL1Rqc1pqcDNtXzVPQ0JYRFRlUlBkAAIECabHVwMEAAAAAA==","C:\\tools\\1.jpg");
		assertTrue(flag);
	}
	
	@Test
	public void testTransferUrl(){
		String originalUrl = "http://mp.weixin.qq.com/debug/cgi-bin/sandboxinfo?action=showinfo&t=sandbox/index";
		String url = weixinService.transferUrl(originalUrl);
		assertNotNull(url);
		assertTrue(url.length()<originalUrl.length());
		System.out.println(url);
	}
	
	@Test
	public void testIsAccessTokenPageIsEffect(){
		//{"access_token":"MOURU44Gjj7NwhccuaIntpQmxEvfLP4XUJa1i2T1XbHxDle6xzvOGE0Z_kvi5EV6XSmDarSDqyNGCZ9_yglBsbmLa_tfAgS1sH0debxu6lw","expires_in":7200,"refresh_token":"hhlNAPAWolT9LxydZsYGxgCdUBdsNt-eMzdo4556Op9TIPUY7OgCKyPh0It4Ja_vgzNPhtxn01nGrQyYGirMNsl_KbUPhQf68jDotyzrJiQ","openid":"oGUsUwCLHXFgODBNzHhRoA_0E3w8","scope":"snsapi_userinfo"}
		boolean flag = weixinService.isAccessTokenPageIsEffect("oGUsUwCLHXFgODBNzHhRoA_0E3w8", "MOURU44Gjj7NwhccuaIntpQmxEvfLP4XUJa1i2T1XbHxDle6xzvOGE0Z_kvi5EV6XSmDarSDqyNGCZ9_yglBsbmLa_tfAgS1sH0debxu6lw");
		assertTrue(flag);
	}
	
	@Test
	public void testRefreshPageAccessToken(){
		AccessTokenPage accessTokenPage = weixinService.refreshPageAccessToken("hhlNAPAWolT9LxydZsYGxgCdUBdsNt-eMzdo4556Op9TIPUY7OgCKyPh0It4Ja_vgzNPhtxn01nGrQyYGirMNsl_KbUPhQf68jDotyzrJiQ");
		assertNotNull(accessTokenPage);
		assertNotNull(accessTokenPage.getAccess_token());
		assertNotNull(accessTokenPage.getRefresh_token());
		System.out.println(accessTokenPage.getAccess_token());
		System.out.println(accessTokenPage.getRefresh_token());
	}*/
	
	@Test
	public void testGetJsApiTicketByHttpGet(){
		long start = System.currentTimeMillis();
		JsApiTicket ticket = JsApiTicket.getInstance();
		long end1 = System.currentTimeMillis();
		System.out.println("票据:"+ticket.getTicket());
		System.out.println("有效时间:"+ticket.getExpires_in());
		assertEquals(7200, ticket.getExpires_in());
		JsApiTicket ticket2 = JsApiTicket.getInstance();
		long end2 = System.currentTimeMillis();
		System.out.println("end1-start:"+(end1-start));
		System.out.println("end2-end1:"+(end2-end1));
		System.out.println("票据2:"+ticket2.getTicket());
		System.out.println("有效时间2:"+ticket2.getExpires_in());
	}
}
