<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>验证码示例</title>
<%@ include file="../../../SYSTEM/commom/common.jsp"%>
<script type="text/javascript" src="../../VerificationCode/js/VerificationCode.js?version=${version}"></script>
</head>
<body>
	<div>
		<img id="VerificationCodeImgId" src="/Dogu/Example/VerificationCode" style="width: 80px;height: 30px;"/>
		<input id="VerificationCodeId" value="" type="text"/>
		<button onclick="CheckVerificationCode()" class="commonSureBtnCls" style="height: 25px;">验证</button>
	</div>
</body>
</html>