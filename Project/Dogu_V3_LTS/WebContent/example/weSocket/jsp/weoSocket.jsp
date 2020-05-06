<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<!-- for HTML5 -->
<%@ include file="/SYSTEM/commom/common.jsp"%>
<title>Java后端WebSocket的Tomcat实现</title>
</head>
<body>
	<div style="text-align: center;">
		<h1>欢迎来到Dogu开源框架聊天室</h1>
		<div>使用说明：(1)、输入'自己的名字'，然后点击'打开WebSocket连接'按钮，直到显示：WebSocket连接成功</div>
		<div>
			　　　　　　　(2)、输入'聊天对象的名字'以及'想要发送的内容'，点击'发送'，果对方在线，则对方就可以收到信息
		</div>
		<div>　　　　　　(3)、可以开两个浏览器每个浏览器开一个WebSocket，然后自己跟自己聊天玩，除非你显得无聊</div>
		自己的名字：<input id="UserName" type="text" />
		<br />
		<br />
		聊天对象的名字：<input id="To_User" type="text" />
		<br />
		<br />
		想要发送的内容：<input id="text" type="text" />
		<br />
		<br />
		<button onclick="OpenWebSocket()">打开WebSocket连接</button>
		<button onclick="closeWebSocket()">关闭WebSocket连接</button>
		<button onclick="send()">发送消息</button>
		<hr />
		<div id="message"></div>
	</div>
</body>
<script type="text/javascript">
	var websocket = null; 
	function OpenWebSocket(){
		var userName = document.getElementById("UserName").value;
		if(userName != null && userName != ""){
			userName = encodeURI(userName);
			userName = encodeURI(userName);
			$.ajax({
				type : 'post',
				url : rootPath + '/Example/QueryOnlinePerson',
				data : {
					USERNAME : userName
				},
				success : function(data) {
					if(data.F_NUM == "0"){
						//判断当前浏览器是否支持WebSocket
						if ('WebSocket' in window) {
							var address = rootPath.replace("http","ws");
							address = address+"/websocket?USERID="+userName;
							websocket = new WebSocket(address);
						} else {
							parent.layer.alert("当前浏览器不支持WebSocket",{icon: 7});
							//alert('当前浏览器不支持websocket')
						}
						//连接发生错误的回调方法
						websocket.onerror = function() {
							parent.layer.alert("WebSocket连接发生错误",{icon: 7});
							//setMessageInnerHTML("WebSocket连接发生错误");
						}; //连接成功建立的回调方法
						websocket.onopen = function() {
							parent.layer.alert("WebSocket连接成功",{icon: 1});
							//setMessageInnerHTML("WebSocket连接成功");
							document.getElementById("UserName").disabled = "disabled";
						} //接收到消息的回调方法
						websocket.onmessage = function(event) {
							setMessageInnerHTML(event.data);
						} //连接关闭的回调方法
						websocket.onclose = function() {
							parent.layer.alert("WebSocket连接关闭",{icon: 7});
							//setMessageInnerHTML("WebSocket连接关闭");
						} //监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
						window.onbeforeunload = function() {
							closeWebSocket();
						}
					}else{
						
					}
				},
				error : function(XMLHttpRequest, textStatus, errorThrown) {
					if(errorThrown == "Forbidden"){
						parent.layer.alert(ForbiddenMsg,{icon: 7});
					}else{
						parent.layer.alert(errorThrown,{icon: 7});
					}
				}
			});
		}else{
			parent.layer.alert("请给自己取一个名字！",{icon: 7});
			//alert("请给自己取一个名字！");
		}
	}
	//将消息显示在网页上
	function setMessageInnerHTML(innerHTML) {
		document.getElementById('message').innerHTML += innerHTML + '<br/>';
	}
	//关闭WebSocket连接
	function closeWebSocket() {
		websocket.close();
		document.getElementById("UserName").disabled = "";
	} //发送消息
	function send() {
		var MyUserName = document.getElementById("UserName").value;
		MyUserName = encodeURI(MyUserName);
		var To_User = document.getElementById("To_User").value;
		To_User = encodeURI(To_User);
		if(To_User != null && To_User !=""){
			var message = document.getElementById('text').value;
			if(message == "" || message == null){
				parent.layer.alert("请输入发送的消息",{icon: 7});
				//alert("请输入发送的消息");
			}else{
				message = encodeURI(message);
				websocket.send(MyUserName+"-MESSAGE-"+To_User+"-MESSAGE-"+message);
			}
		}else{
			parent.layer.alert("请输入想发送给谁",{icon: 7});
			//alert("请输入想发送给谁");
		}
	}
</script>
</html>