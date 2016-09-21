package wfm.weixin.vo.response;

import wfm.weixin.vo.Message;

/**
 * 用于回复的微信音乐消息
 * @author 吴福明
 *
 */
public class MusicResMessage extends Message{
	
	private Music music;//音乐文件信息

	public Music getMusic() {
		return music;
	}

	public void setMusic(Music music) {
		this.music = music;
	}

}
