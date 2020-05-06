<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>视频示例</title>
<%@ include file="../../../SYSTEM/commom/common.jsp"%>
<link href="../css/video-js.css?version=${version}" rel="stylesheet">
<script src="../js/video.min.js?version=${version}"></script>
<!-- If you'd like to support IE8 -->
<script src="../js/videojs-ie8.min.js?version=${version}"></script>
<style>
body {
	background-color: #191919
}

.m {
	width: 640px;
	height: 264;
	margin-left: auto;
	margin-right: auto;
	margin-top: 100px;
}
</style>
</head>

<body>
	<div class="m">
		<video id="my-video" class="video-js" controls preload="auto"
			width="640" height="264" poster="../img/MY_VIDEO_POSTER.jpg" data-setup="{}">
		<source src="http://vjs.zencdn.net/v/oceans.mp4" type="video/mp4">
		<source src="http://vjs.zencdn.net/v/oceans.webm" type="video/webm">
		<source src="http://vjs.zencdn.net/v/oceans.ogv" type="video/ogg">
		</video>
		<script type="text/javascript">
			var myPlayer = videojs('my-video');
			videojs("my-video").ready(function() {
				var myPlayer = this;
				myPlayer.play();
			});
		</script>
	</div>
</body>
</html>