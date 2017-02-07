package wfm.weixin.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import wfm.weixin.vo.MassNews;
import wfm.weixin.vo.QrSceneInfo;
import wfm.weixin.vo.TemplateMessageParamInfo;
import wfm.weixin.vo.UserInfo;
import wfm.weixin.vo.UserList;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring.xml"})
public class WeixinTest {

	@Autowired
	private WeixinService weixinService;
	
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
		String mediaId = weixinService.uploadFile("C:\\tmp\\33333.jpg", "thumb");
		assertNotNull(mediaId);
		//DpotLtIgBuWwCI0Ra6pfKDmReuAZ2YCK-4oUYDHusuDISxKzIHN1psJ5RY_S0GDv
		//S7HoJ_5gY-c_TEJIf8ROPtvDDocz3HY3veN904fHM5KkoO8AQ8ED4ekg0K4zS4qR
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
	}
	
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
	
	@Test
	public void testMassMessage(){
		List<String> openIds = new ArrayList<String>();
		openIds.add("oGUsUwCLHXFgODBNzHhRoA_0E3w8");
		openIds.add("oGUsUwCLHXFgODBNzHhRoA_0E3w8");
		boolean flag = weixinService.sendMassMessageByOpenIDs(openIds,"mpnews","JvJpMwCsWr2JH1l9TpWEUwBqn3cX7mmEFIXkvKfmYQn8OUketHTV5uPrm1HHFPGT");
		assertTrue(flag);
		//"msg_id":3147483651
		//
	}
	
	@Test
	public void testUploadImage()throws FileNotFoundException{
		String url = weixinService.uploadImage("C:\\tmp\\33333.jpg");
		//http://mmbiz.qpic.cn/mmbiz_jpg/bxylJP9yUdDtczQG3ico3DEzKQ83WA0t02thKosQUibno01SBM86W2xqnLW84QmOsgxWF5yNGcN92RiaBLbDfj3Ag/0
		assertNotNull(url);
		System.out.println(url);
	}
	
	@Test
	public void testGetMassSendStatus(){
		String status = weixinService.getMassMessageSendStatus("3147483651");
		assertNotNull(status);
		System.out.println(status);
	}
	
	@Test
	public void testUploadMassNews(){
		MassNews massNews = new MassNews();
		massNews.setAuthor("test");
		massNews.setContent("<div class=\"bd\"><p class=\"pic\"><img src=\"http://mmbiz.qpic.cn/mmbiz_jpg/bxylJP9yUdDtczQG3ico3DEzKQ83WA0t0TxTCKLeR47uguNQUGV3RV3EFGTIL93KooXrzah6AIF19qxnUWdqwtw/0\" alt=\"\"></p><p>4-1主场战胜莱斯特城后，马内表示，他对球队的发挥很满意，但同时也能看到进步的空间。</p><p>比赛中，红军在主场发挥了自己的进攻实力，菲尔米诺梅开二度，马内、拉拉纳分别进球，帮助球队战胜卫冕冠军。</p><p>不过，马内表示，他依然认为利物浦本赛季可以做得更好。</p><p>“每个人都很高兴，这场胜利对我们很关键。”他说。</p><p>“我觉得这不是我们表现最好的比赛，但我们配得上胜利，因为我们创造了很多机会。我们可以再进两三个球的，但我觉得已经很满意了。”</p><p>“我猜我很容易适应这里的球风，因为这里的球员都特别特别优秀。”</p><p>“我们对今天的波斯很开心，这会帮助我们在接下来的比赛中顺利发挥。”</p></div>");
		massNews.setContent_source_url("http://cn.liverpoolfc.com/news/show/6518.html");
		massNews.setDigest("win picture");
		massNews.setShow_cover_pic("1");
		massNews.setThumb_media_id("AZj_FOD5Rt4X5i2Yj_8asTfR-dIRP0jdGVQ7wkic0QR_nspTqKmW33dR9PSDiMOY");
		massNews.setTitle("马内：我们很满意，但可以更强");
		MassNews massNews2 = new MassNews();
		massNews2.setAuthor("test1");
		massNews2.setContent("<div class=\"bd\"><p class=\"pic\"><img src=\"http://mmbiz.qpic.cn/mmbiz_jpg/bxylJP9yUdDtczQG3ico3DEzKQ83WA0t06mB9miaNxcAlSLLeh3Krkf7uhpY4aTh1meqlWwplY5YdqJBnrOoUf2w/0\" alt=\"\"></p><p>拉拉纳相信，利物浦4-1大胜莱斯特城的比赛给刚有新面貌的安菲尔德树立了新的标准。</p><p>在主看台竣工后的第一场比赛中，利物浦势不可当，菲尔米诺梅开二度，拉拉纳和马内分别进球，帮助红军战胜对手。</p><p>“我觉得我们的表现很出色。”拉拉纳赛后表示。</p><p>“我们在上半场就进了两球。中场休息时我们充满斗志，这场比赛是安菲尔德本赛季的新标准。”</p><p>主看台的竣工为安菲尔德新加了8500个座位，拉拉纳表示：“我们绝对感受到了更加热烈的气氛，看台已经完全不同了，气氛已经更加热烈了。”</p><p>比赛中，利物浦共计射门17次。拉拉纳、马内和菲尔米诺分别破门。</p><p>“进球的感觉很好，我们拥有世界级的球员，每个人都很努力。”</p><p>“每个人都创造了自己的机会，并不仅仅是我、马内和菲尔米诺。”</p></div>");
		massNews2.setContent_source_url("http://cn.liverpoolfc.com/news/show/6519.html");
		massNews2.setDigest("win22 picture22");
		massNews2.setShow_cover_pic("0");
		massNews2.setThumb_media_id("jaHh5CcW_nWNUr2hRo7N6Lsxl18MjI39D7ejavJHGntUCWlrBicoX1-Jejbt79fk");
		massNews2.setTitle("拉拉纳：莱斯特城的比赛是安菲尔德的里程碑");
		List<MassNews> list = new ArrayList<MassNews>();
		list.add(massNews);
		list.add(massNews2);
		String result = weixinService.uploadNews(list);
		assertNotNull(result);
		System.out.println(result);
	}
	
	@Test
	public void testSendMassByGroup(){
		boolean flag = weixinService.sendMassMessageByGroup(false, 102, "mpnews", "JvJpMwCsWr2JH1l9TpWEUwBqn3cX7mmEFIXkvKfmYQn8OUketHTV5uPrm1HHFPGT");
		assertTrue(flag);
	}
	
	@Test
	public void testPreviewMass(){
		boolean flag = weixinService.previewMassMessage("oGUsUwCLHXFgODBNzHhRoA_0E3w8", "mpnews", "JvJpMwCsWr2JH1l9TpWEUwBqn3cX7mmEFIXkvKfmYQn8OUketHTV5uPrm1HHFPGT");
		assertTrue(flag);
	}
	
	@Test
	public void testDownloadMedia(){
		weixinService.downloadMedia("YAq4S8CInf3U3Oxg3MZFTcHT-haJcC5YHWlfeu33czAwp2AG2KUaVZdOTI_xpXDU","/Users/mittermeyer/Documents/");
	}
}
