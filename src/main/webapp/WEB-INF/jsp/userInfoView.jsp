<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>微信网页高级用户授权信息</title>
<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script type="text/javascript">
//微信JS-SDK配置信息
wx.config({
    debug: true, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
    appId: 'wxa3655d48828c7006', // 必填，公众号的唯一标识
    timestamp:'${timestamp}' , // 必填，生成签名的时间戳
    nonceStr: '${noncestr}', // 必填，生成签名的随机串
    signature: '${signature}',// 必填，签名，见附录1
    jsApiList: ['onMenuShareTimeline','onMenuShareAppMessage','chooseImage','scanQRCode'] // 必填，需要使用的JS接口列表，所有JS接口列表见http://mp.weixin.qq.com/wiki/7/aaa137b55fb2e0456bf8dd9148dd613f.html附录2
});
wx.ready(function(){
	/* config信息验证后会执行ready方法，所有接口调用都必须在config接口获得结果之后，config是一个客户端的异步操作，所以如果需要在页面加载时就调用相关接口，
	则须把相关接口放在ready函数中调用来确保正确执行。对于用户触发时才调用的接口，则可以直接调用，不需要放在ready函数中。*/
	//分享朋友圈
	wx.onMenuShareTimeline({
	    title: '分享到朋友圈测试', // 分享标题
	    link: 'http://5bf7ce9.ittun.com/weixin/weixinPage/testLink', // 分享链接
	    imgUrl: 'http://5bf7ce9.ittun.com/weixin/static/images/lfc_crest.png', // 分享图标
	    success: function () { 
	        // 用户确认分享后执行的回调函数
	        alert("分享成功");
	    },
	    cancel: function () { 
	        // 用户取消分享后执行的回调函数
	    	alert("取消分享");
	    }
	});
	//分享朋友
	wx.onMenuShareAppMessage({
	    title: '分享朋友测试', // 分享标题
	    desc: '分享朋友测试描述', // 分享描述
	    link: 'http://5bf7ce9.ittun.com/weixin/weixinPage/testLink', // 分享链接
	    imgUrl: 'http://5bf7ce9.ittun.com/weixin/static/images/lfc_crest.png', // 分享图标
	    type: '', // 分享类型,music、video或link，不填默认为link
	    dataUrl: '', // 如果type是music或video，则要提供数据链接，默认为空
	    success: function () { 
	        // 用户确认分享后执行的回调函数
	    	alert("分享成功");
	    },
	    cancel: function () { 
	        // 用户取消分享后执行的回调函数
	    	alert("取消分享");
	    }
	});
	
});
wx.error(function(res){
    // config信息验证失败会执行error函数，如签名过期导致验证失败，具体错误信息可以打开config的debug模式查看，也可以在返回的res参数中查看，对于SPA可以在这里更新签名。

});
function showImage(){
	//拍照或从手机相册中选图
	wx.chooseImage({
	    count: 2, // 默认9,最多选择照片数量
	    sizeType: ['original', 'compressed'], // 可以指定是原图还是压缩图，默认二者都有
	    sourceType: ['album', 'camera'], // 可以指定来源是相册还是相机，默认二者都有
	    success: function (res) {
	    	// 返回选定照片的本地ID列表，localId可以作为img标签的src属性显示图片
	        var localIds = res.localIds; 
	        alert(localIds);
	    }
	});
}
function scan(){
	wx.scanQRCode({
	    needResult: 1, // 默认为0，扫描结果由微信处理，1则直接返回扫描结果，
	    scanType: ["qrCode","barCode"], // 可以指定扫二维码还是一维码，默认二者都有
	    success: function (res) {
		    var result = res.resultStr; // 当needResult 为 1 时，扫码返回的结果
		    alert(result);
		}
	});
}
</script>
</head>
<body>${timestamp},${noncestr},${signature}<br/>
<button onclick="showImage()">选择相册</button><br/>
<button onclick="scan()">扫码</button>
</body>
</html>