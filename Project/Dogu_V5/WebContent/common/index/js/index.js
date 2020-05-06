var countdown = 5;
var useRegIndex = 0;
var ForgetPswIndex = 0;
var OpenVCode = false;
$(document).ready(function() {
	initIndex();
});
//首页初始化统一入口
function initIndex() {
	document.onkeydown = keyDownSearch;//监听回车键点击登录按钮
	checkSavedLoginInfo();//检查是否勾选了记住登录信息
	checkOpenUserRegAndForgetPswAndvalidateCode();//检查是否开启用户注册和忘记密码以及验证码验证功能
}
//检查是否开启用户注册和忘记密码功能
function checkOpenUserRegAndForgetPswAndvalidateCode(){
	$.ajax({
		type : 'post',
		url : rootPath + '/checkOpenUserRegAndForgetPswAndvalidateCode',
		dataType : 'json',
		success : function(data) {
			var RAndPInner = "";
			var VCodeInner = "";
			if(data.REG == true || data.REG == "true"){
				RAndPInner += "<a href='javascript:;' onclick='userReg()'>用户注册</a>";
				RAndPInner += "&nbsp;&nbsp;";
			}
			if(data.PSW == true || data.PSW == "true"){
				RAndPInner += "<a href='javascript:;' onclick='forgetPassword()'>忘记密码？</a>";
			}
			if(data.VCODE == true || data.VCODE == "true"){
				VCodeInner = "<label class='icode' data-icon='p'> 验证码</label>"+
				"<input id='icode' type='text' placeholder='验证码' />"+
				"<a id='validateCode' style='width: 150px;height: 30px;background-color: #EEE;float: right;margin-top:8px;'>"+
				"<img src='"+rootPath+"/getValidateImg' onclick='changeValidateCode(this)' style='width: 100%;height: 100%; cursor:pointer;'/>"+
				"</a>";
				document.getElementById("validateCodePId").style.display = "block";
			}
			document.getElementById("UserRegAndForgetPsw").innerHTML = RAndPInner;
			document.getElementById("validateCodePId").innerHTML = VCodeInner;
		},
		error : function(data, error) {
			parent.layer.alert(error,{icon: 7});
		}
	});
}
// 监听回车事件
function keyDownSearch(e) {
	// 兼容FF和IE和Opera
	var theEvent = e || window.event;
	var code = theEvent.keyCode || theEvent.which || theEvent.charCode;
	var canClick = document.getElementById("loginCheckBtn").disabled;
	if (code == 13 && canClick == false) {
		var UserRegOrForgetPswShow = document.getElementsByClassName("layui-layer-shade");
		if(UserRegOrForgetPswShow.length == 0){
			logincheck();
		}
	}
}
// 检查是否保存帐号和密码
function checkSavedLoginInfo(){
	if (getCookie("autologin") == "yes") {
		document.getElementById("loginkeeping").checked = true;
		document.getElementById("username").value = getCookie("username");
		document.getElementById("password").value = getCookie("password");
	} else {
		document.getElementById("password").value = "";
		document.getElementById("password").value = "";
		document.getElementById("loginkeeping").checked = false;
	}
}
//更换验证码
function changeValidateCode(obj){
	obj.src = rootPath+"/getValidateImg?time="+Math.random();
}
//登录
function logincheck() {
	document.getElementById("loginCheckBtn").style.backgroundColor = "#AAA";
	document.getElementById("loginCheckBtn").disabled = "disabled";
	$.ajax({
		type : 'post',
		url : rootPath + '/logincheck',
		dataType : 'json',
		data : {
			account : $('#username').val(),
			password : hex_md5($('#password').val()),
			icode : $('#icode').val()
		},
		success : function(data) {
			window.setTimeout("enableLogin()",5000);
			if (data.id == "success") {
				if (document.getElementById("loginkeeping").checked) {
					setCookie("username", $('#username').val(), 30);// 保存30天的cookie
					setCookie("password", $('#password').val(), 30);// 保存30天的cookie
					setCookie("autologin", "yes", 30);// 保存30天的cookie
				} else {
					setCookie("autologin", "no", 0);// 设置cookie立即失效
					setCookie("username", "", 0);// 设置cookie立即失效
					setCookie("password", "", 0);// 设置cookie立即失效
				}
				delCookie("SrceenLock");
				window.location.href = rootPath;
			} else {
				countdown = 5;
				settime(5);
				$("#message").text(data.message);
			}
		},
		error : function(data, error) {
			parent.layer.alert(error,{icon: 7});
		}
	});
}

//定时事件设置登录按钮隔5秒不可点击
function enableLogin(){
	document.getElementById("loginCheckBtn").style.backgroundColor = "#4AB3C6";
	document.getElementById("loginCheckBtn").disabled = "";
	document.getElementById("message").innerHTML = "";//清空提示栏
	document.getElementById("loginCheckBtn").value = "登录";
}
//倒计时5秒
function settime(val) {
	val = countdown;
	countdown--;
	document.getElementById("loginCheckBtn").value = "登录("+val+"s)";
	if(val > 1){
		setTimeout(function() {
			settime(val);
		}, 1000);
	}
}
//用户注册
function userReg(){
	var useRegContent = "<table class='layui-table'>"+
		"<tr>"+
		"<td style='text-align: right;width: 150px;'>帐　　号：</td>"+
		"<td><input id='regaccount' class='layui-input' placeholder='*请输入用户名' type='text' style='width: 260px;'/></td>"+
		"</tr>"+
		"<tr>"+
		"<td style='text-align: right;'>密　　码：</td>"+
		"<td><input id='regpassword' class='layui-input' placeholder='*请输入密码' type='password' style='width: 260px;'/></td>"+
		"</tr>"+
		"<tr>"+
		"<td style='text-align: right;'>确认密码：</td>"+
		"<td><input id='regagainpassword' class='layui-input' placeholder='*请再次输入密码' type='password' style='width: 260px;'/></td>"+
		"</tr>"+
		"<tr>"+
		"<td style='text-align: right;'>姓　　名：</td>"+
		"<td><input id='regname' class='layui-input' placeholder='*请输入姓名' type='text' style='width: 260px;'/></td>"+
		"</tr>"+
		"<tr>"+
		"<td style='text-align: right;'>性　　别：</td>"+
		"<td>"+
		"<select id='regsex' style='width: 260px;height:30px;'>"+
		"<option value='' selected='selected'>请选择</option>"+
		"<option value='F'>男</option>"+
		"<option value='M'>女</option>"+
		"</select>"+
		"</td>"+
		"</tr>"+
		"<tr>"+
		"<td style='text-align: right;'>电　　话：</td>"+
		"<td><input id='regtel' class='layui-input' placeholder='*请输入电话号码' type='text' style='width: 260px;'/></td>"+
		"</tr>"+
		"<tr>"+
		"<td style='text-align: right;'>地　　址：</td>"+
		"<td><input id='regaddress' class='layui-input' placeholder='*请输入地址' type='text' style='width: 260px;'/></td>"+
		"</tr>"+
		"<tr>"+
		"<td style='text-align: right;'>邮　　箱：</td>"+
		"<td><input id='regemail' class='layui-input' placeholder='*请输入邮箱' type='text' style='width: 260px;'/></td>"+
		"</tr>"+
		"<tr>"+
		"<td style='text-align: right;'>Q　　&nbsp;Q：</td>"+
		"<td><input id='regqq' class='layui-input' placeholder='*请输入QQ号' type='text' style='width: 260px;'/></td>"+
		"</tr>"+
		"<tr style='text-align: center;'>"+
		"<td colspan='2'>"+
		"<input type='button' value='确定' class='layui-btn' onclick='sureUseReg()'/>"+
		"<input type='button' value='取消' class='layui-btn' onclick='cancelUseReg()'/>"+
		"</td>"+
		"</tr>"+
		"</table>"
	useRegIndex = layer.open({
		type: 1,
		area: ['430px','640px'],
		fix: true, //不固定
		maxmin: false,
		shade:0.4,
		title: '用户注册',
		content: useRegContent,
	});
	document.getElementById("regaccount").focus();
}
//隐藏用户注册
function cancelUseReg(){
	layer.close(useRegIndex);
}
//确定用户注册
function sureUseReg(){
	var account = document.getElementById("regaccount").value;
	var password = document.getElementById("regpassword").value;
	password = hex_md5(password);
	var againpassword = document.getElementById("regagainpassword").value;
	againpassword = hex_md5(againpassword);
	var name = document.getElementById("regname").value;
	var sex = document.getElementById("regsex").value;
	var tel = document.getElementById("regtel").value;
	var address = document.getElementById("regaddress").value;
	var email = document.getElementById("regemail").value;
	var qq = document.getElementById("regqq").value;
	//手机号正则
	var isPhone = /^([0-9]{3,4}-)?[0-9]{7,8}$/;
	var isMob = /^((\+?86)|(\(\+86\)))?(13[012356789][0-9]{8}|15[012356789][0-9]{8}|18[02356789][0-9]{8}|147[0-9]{8}|1349[0-9]{7})$/;
	//email正则
	var emailreg = /\w@\w*\.\w/;
	//QQ号正则
	var qqreg = /^\d{5,10}$/;
	if($.trim(account)==""||account==null){
		layer.alert("请输入账号！",{icon: 7});
		return;
	}else if($.trim(password)==""||password==null){
		layer.alert("请输入密码！",{icon: 7});
		return;
	}else if($.trim(againpassword)==""||againpassword==null){
		layer.alert("请再次输入密码！",{icon: 7});
		return;
	}else if(password != againpassword){
		layer.alert("两次密码不一致,请重新输入！",{icon: 7});
		return;
	}else if($.trim(name)==""||name==null){
		layer.alert("请输入姓名！",{icon: 7});
		return;
	}else if($.trim(sex)==""||sex==null){
		layer.alert("请选择性别！",{icon: 7});
		return;
	}else if(!isPhone.test(tel)&&!isMob.test(tel)){
		layer.alert("请输入正确的手机号！",{icon: 7});
		return;
	}else if($.trim(address)==""||address==null){
		layer.alert("请输入地址！",{icon: 7});
		return;
	}else if(!emailreg.test(email)){
		layer.alert("请输入正确的邮箱地址！",{icon: 7});
		return;
	}else if(!qqreg.test(qq)){
		layer.alert("请输入正确的QQ号！",{icon: 7});
		return;
	}else{
		name = encodeURI(name);
		address = encodeURI(address);
		$.ajax({
			type : 'post',
			url : rootPath + '/UseReg',
			data : {
				account : account,
				password : password,
				name : name,
				sex: sex,
				tel : tel,
				address : address,
				email : email,
				qq : qq
			},
			success : function(data) {
				if(data.code=="0"){
					cancelUseReg();
					layer.alert(data.message,{icon: 1});
				}else{
					layer.alert(data.message,{icon: 7});
				}
			},
			error : function(data, error) {
				parent.layer.alert(error,{icon: 7});
			}
		});
	}
}
//找回密码
function forgetPassword(){
	var forgetPswContent = 
		"<table class='layui-table'>"+
		"<tr>"+
		"<td style='width: 150px;text-align: right;'>帐　　号：</td>"+
		"<td><input id='forgetpswaccount' class='layui-input' placeholder='*请输入用户名' type='text' style='width: 260px;'/></td>"+
		"</tr>"+
		"<tr>"+
		"<td style='text-align: right;'>电　　话：</td>"+
		"<td><input id='forgetpswtel' class='layui-input' placeholder='*请输入电话号码' type='text' style='width: 260px;'/></td>"+
		"</tr>"+
		"<tr>"+
		"<td style='text-align: right;'>邮　　箱：</td>"+
		"<td><input id='forgetpswemail' class='layui-input' placeholder='*请输入邮箱' type='text' style='width: 260px;'/></td>"+
		"</tr>"+
		"<tr>"+
		"<td style='text-align: right;'>方　　式：</td>"+
		"<td>"+
		"<select id='method' style='width: 260px;height:30px;'>"+
		"<option value='MSG'>短信</option>"+
		"<option value='MAIL'>邮箱</option>"+
		"</select>"+
		"</td>"+
		"</tr>"+
		"<tr style='text-align: center;'>"+
		"<td style='text-align: center;' colspan='2'>"+
		"<input type='button' value='确定' class='layui-btn' onclick='sureFogetPsw()'/>"+
		"<input type='button' value='取消' class='layui-btn' onclick='hideFogetPsw()'/>"+
		"</td>"+
		"</tr>"+
		"</table>"
	ForgetPswIndex = layer.open({
		type: 1,
		area: ['430px','350px'],
		fix: true, //不固定
		maxmin: false,
		shade:0.4,
		title: '忘记密码',
		content: forgetPswContent,
	});
	document.getElementById("forgetpswaccount").focus();
}
//确定找回密码
function sureFogetPsw(){
	var account = document.getElementById("forgetpswaccount").value;
	var tel = document.getElementById("forgetpswtel").value;
	var email = document.getElementById("forgetpswemail").value;
	var method = document.getElementById("method").value;
	if($.trim(account)==""||account==null){
		layer.alert("请输入账号！",{icon: 7});
		return;
	}else{
		$.ajax({
			type : 'post',
			url : rootPath + '/forgetPSW',
			data : {
				account : account,
				tel : tel,
				email : email,
				method : method
			},
			success : function(data) {
				if(data.code=="0"){
					$("#FogetPswCover").fadeOut(300);
					layer.alert(data.message,{icon: 1});
				}else{
					layer.alert(data.message,{icon: 7});
				}
			},
			error : function(data, error) {
				parent.layer.alert(error,{icon: 7});
			}
		});
	}
}
//取消找回密码
function hideFogetPsw(){
	layer.close(ForgetPswIndex);
}