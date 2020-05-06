<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<% 
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/"; 
%> 
<!DOCTYPE HTML>
<html>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>404</title>
<%@ include file="/SYSTEM/common/jsp/common.jsp"%>
<link rel="stylesheet" href="${ctx}/SYSTEM/404page/css/error-page.css?version=${version}"/>
</head>
<body>
	<!-- 加载动画 -->
	<div class="page-loading">
	    <div class="ball-loader">
	        <span></span><span></span><span></span><span></span>
	    </div>
	</div>
	
	<!-- 正文开始 -->
	<div class="error-page">
	    <img class="error-page-img" src="${ctx}/SYSTEM/404page/img/ic_404.svg">
	    <div class="error-page-info">
	        <h1>404</h1>
	        <div class="error-page-info-desc">啊哦，你访问的页面不存在(⋟﹏⋞)</div>
	        <div>
	            <button id="clickCloseId" class="layui-btn"></button>
	        </div>
	    </div>
	</div>
	
	<script>
		var contextPath = "<%= contextPath%>";
		var topWindow = $(window.parent.document);
		iframe = topWindow.find(".layui-tab-title");
		if(iframe.length > 0){
			document.getElementById("clickCloseId").innerHTML = "关闭当前页";
			document.getElementById("clickCloseId").onclick=function(){
				closeCurrentTab();
			};
			//下面的方式是可以关闭任何页面的方法
			/*var closePath = this.location.pathname;
			closePath = closePath.replace(contextPath+"/","");
			document.getElementById("clickCloseId").innerHTML = "关闭当前页";
			document.getElementById("clickCloseId").onclick=function(){
				closeTab(closePath);
			};*/
		}else{
			document.getElementById("clickCloseId").innerHTML = "点击前往首页";
			document.getElementById("clickCloseId").onclick=function(){
				window.location.href = rootPath;
			};
		}
	</script>
</body>
</html>