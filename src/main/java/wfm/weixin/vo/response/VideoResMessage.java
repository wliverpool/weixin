package wfm.weixin.vo.response;

import wfm.weixin.vo.Message;

/**
 * 用于回复的微信视频消息
 * @author 吴福明
 *
 */
public class VideoResMessage extends Message{
	
	private Video video;//微信视频文件

	public Video getVideo() {
		return video;
	}

	public void setVideo(Video video) {
		this.video = video;
	}

}
