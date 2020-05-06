<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>无拦截器示例</title>
<%@ include file="../../../SYSTEM/commom/common.jsp"%>
<script type="text/javascript" src="../js/noInterceptor.js?version=${version}"></script>
</head>
<body>
	<br>
	不被拦截器拦截的Action示例
	<br>
	<br>
	请在不登录的情况下直接访问：
	<br>
	<br>
	http://Uri:Port/ProjectName/Example/NoInterceptor
	<br>
	<br>
	(如:<a href="http://localhost:8080/Dogu/example/noInterceptor/jsp/noInterceptor.jsp" target="blank">http://www.dogu.site:40000/Dogu/Example/NoInterceptor</a>)
</body>
</html>