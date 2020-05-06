<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>Dogu开源框架</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ include file="/SYSTEM/common/jsp/common.jsp"%>
<link rel="shortcut icon" href="${ctx}/SYSTEM/index/ico/favicon.ico" /> 
<link rel="stylesheet" href="SYSTEM/index/css/index.css?version=${version}">
<script type="text/javascript" src="SYSTEM/index/js/index.js?version=${version}"></script>
<script type="text/javascript" src="SYSTEM/lib/md5/md5.js?version=${version}"></script>
</head>
<body>
	<div id="wrapper">
		<div id="login">
			<h1 style="margin-top: 30px;">Dogu开源框架</h1>
			<p>
				<label for="username" class="uname" data-icon="u">用户名</label>
				<input id="username" type="text" placeholder="用户名" />
			</p>
			<p>
				<label class="youpasswd" data-icon="p"> 密码</label>
				<input id="password" type="password" placeholder="密码" />
			</p>
			<p id="validateCodePId" style="display: none;"></p>
			<p class="keeplogin">
				<input type="checkbox" id="loginkeeping" />
				<label for="loginkeeping">记住密码</label>
			</p>
			<p class="login button">
				<input id="loginCheckBtn" class="logincheck" type="button" value="登录" onclick="logincheck()" />
			<br>
			<div id="UserRegAndForgetPsw" style="text-align: right;"></div>
			<div id="message" class="loginmessage"></div>
		</div>
	</div>
</body>
</html>