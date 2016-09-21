package wfm.weixin.vo.response;

import wfm.weixin.vo.Message;

/**
 * 用于回复的微信语音消息
 * @author 吴福明
 *
 */
public class VoiceResMessage extends Message{
	
	private Voice voice;//语音文件

	public Voice getVoice() {
		return voice;
	}

	public void setVoice(Voice voice) {
		this.voice = voice;
	}

}
