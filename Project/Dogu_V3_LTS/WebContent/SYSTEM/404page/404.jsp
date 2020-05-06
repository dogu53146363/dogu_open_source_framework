<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<% 
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/"; 
%> 
<!DOCTYPE HTML>
<html>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>404</title>
<%@ include file="/SYSTEM/commom/common.jsp"%>
<script type="text/javascript" src="<%=basePath%>SYSTEM/lib/jquery/3.3.1/jquery-3.3.1.min.js"></script>
<script type="text/javascript" src="<%=basePath%>SYSTEM/commom/common.js"></script>
<script type="text/javascript">
var countdown = 5;
$(document).ready(function() {
	document.getElementById('clickCloseId').onclick = function(){};
	document.getElementById("clickCloseId").href = "javascript:void(0);";
	var sessionUserInfo = document.getElementById("sessionUserInfoId").value;
	if(sessionUserInfo != "" && sessionUserInfo != null){
		document.getElementById("no_2_Text").innerHTML = "您浏览的页面不存在";
		document.getElementById("no_3_Text").innerHTML = "或您没有权限查看该页面";
		var topWindow = $(window.parent.document);
		iframe = topWindow.find(".layui-tab-title");
		//如果是在iframe中打开的话
		if(iframe.length > 0){
			document.getElementById("clickCloseId").innerHTML = "点击关闭该页面";
			document.getElementById("clickCloseId").onclick=function(){
				removeIframe();
			};
			document.getElementById("no_4_Text").innerHTML = "秒后将自动关闭该页面";
			settime(5);
			window.setTimeout(function(){
				removeIframe();
			},5000);
		}else{//否则
			document.getElementById("clickCloseId").innerHTML = "点击前往首页";
			document.getElementById("clickCloseId").href = rootPath;
			document.getElementById("no_4_Text").innerHTML = "秒后将跳转到首页";
			settime(5);
			window.setTimeout(function(){
				document.getElementById("clickCloseId").click();
			},5000);
		}
	}else{
		var topWindow = $(window.parent.document);
		iframe = topWindow.find(".layui-tab-title");
		if(iframe.length > 0){
			document.getElementById('clickCloseId').onclick = function(){
				parent.location.reload();
			};
			document.getElementById("clickCloseId").innerHTML = "点击前往登录页面";
			document.getElementById("clickCloseId").href = rootPath;
			document.getElementById("no_2_Text").innerHTML = "您还没有登录，或登录已超时";
			document.getElementById("no_3_Text").innerHTML = "请重新登！";
			document.getElementById("no_4_Text").innerHTML = "秒后将跳转到登录页面";
			settime(5);
			window.setTimeout(function(){
				parent.location.reload();
			},5000);
		}else{
			document.getElementById("clickCloseId").innerHTML = "点击前往登录页面";
			document.getElementById("clickCloseId").href = rootPath;
			document.getElementById("no_2_Text").innerHTML = "您还没有登录，或登录已超时";
			document.getElementById("no_3_Text").innerHTML = "请重新登！";
			document.getElementById("no_4_Text").innerHTML = "秒后将跳转到登录页面";
			settime(5);
			window.setTimeout(function(){
				document.getElementById("clickCloseId").click();
			},5000);
		}
	}
});

//倒计时5秒
function settime(val) {
	val = countdown;
	countdown--;
	document.getElementById("timmer").innerHTML = val;
	if(val > 1){
		setTimeout(function() {
			settime(val);
		}, 1000);
	}
}

//关闭iframe
function removeIframe(){
	var tab = parent.tab;
    tab.deleteTab(tab.getCurrentTabId());
}

</script>
</head>
<body>
	<div style="text-align: center;">
		<input id="sessionUserInfoId" style="display: none;" value="${f_account}" />
		<h1 id="" style="text-align: center;font-size: 45px;margin-top: 10%;">啊哦~出错了</h1>
		<h3 id="no_2_Text" style="text-align: center;font-size: 40px;"></h3>
		<h3 id="no_3_Text" style="text-align: center;font-size: 40px;"></h3>
		<h3 style="text-align: center;font-size: 30px;">
			<span id="timmer"></span>
			<span id="no_4_Text"></span>
		</h3>
		<a id="clickCloseId" href="javascript:void(0);"
			style="text-align: center;display: block;font-size:30px;color:blue;text-decoration:underline;"></a>
	</div>
</body>
</html>