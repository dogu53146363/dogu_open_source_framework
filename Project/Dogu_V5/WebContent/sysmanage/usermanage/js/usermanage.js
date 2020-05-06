var AddUserInfoIndex = 0;
var ChangeUserInfoIndex = 0;
$(document).ready(function() {
	getRoleInfo();
});
//获取角色字典
function getRoleInfo(){
	$.ajax({
		type : 'post',
		url : rootPath + '/usermanage/getRoleInfo',
		beforeSend: function () {
			showLoad();
		},
		data : {
			use : "Y"
		},
		success : function(data) {
			document.getElementById("roleid").options.length=0;
			document.getElementById("roleid").options.add(new Option("全部",""));
			for(var i=0;i<data.length;i++){
				document.getElementById("roleid").options.add(new Option(data[i].F_ROLENAME,data[i].F_ROLENUM));
			}
			getUserInfo();
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			if(errorThrown == "Forbidden" || XMLHttpRequest.status == "403"
				 || XMLHttpRequest.status == "404"){
				parent.layer.alert(ForbiddenMsg,{icon: 7});
			}else{
				parent.layer.alert(errorThrown,{icon: 7});
			}
		},
		complete: function(XMLHttpRequest, textStatus) {
			closeLoad();
		}
	});
}
//获取用户信息
function getUserInfo(){
	var rolenum = document.getElementById("roleid").value;
	var username = document.getElementById("usernameid").value;
	var account = document.getElementById("accountid").value;
	var orgname = document.getElementById("orgid").value;
	username = encodeURI(username);
	orgname = encodeURI(orgname);
	layui.use(['layer', 'table', 'element'], function(){
		var table = layui.table //表格
		,element = layui.element //元素操作
		//执行一个 table 实例
		table.render({
			elem: '#dataTable'
			,height: 'full-70'
			,url: rootPath + '/usermanage/getUserInfo' //数据接口
			,method: "post"
			,where: {
				account : account,
				username : username,
				rolenum : rolenum,
				orgname : orgname
			}
			,title: '用户表'
			,page: true //开启分页
			,limit: 10
			,toolbar: 'default' //开启工具栏，此处显示默认图标，可以自定义模板，详见文档
			,totalRow: false //开启合计行
			,cols: [[ //表头
				 {type: 'checkbox', fixed: 'left'}
				,{type: 'numbers', title: '序号', width: 60, fixed: 'left', align: 'center'}
				,{field: 'F_ACCOUNT', title: '账号', sort: true, fixed: 'left'}
				,{field: 'F_USERNAME', title: '用户名	', sort: true}
				,{field: 'F_ROLENAME', title: '角色', sort: true}
				,{field: 'F_ORG_NAME', title: '组织机构', sort: true}
				,{field: 'F_SEX', title: '性别', sort: true, templet: '#sexMask', width: 80}
				,{field: 'F_TEL', title: '电话', sort: true} 
				,{field: 'F_E_MAIL', title: 'E-Mail', sort: true}
				,{field: 'F_QQ', title: 'QQ', sort: true, sort: true}
				,{field: 'F_ADDRESS', title: '地址', sort: true}
				,{fixed: 'right', title: '操作', width: 200, align:'center', toolbar: '#dataTableBar'}
			]]
		});
		
		//监听行工具事件
		table.on('tool(dataTable)', function(obj){ //注：tool 是工具条事件名，test 是 table 原始容器的属性 lay-filter="对应的值"
			var data = obj.data;//获得当前行数据
			var layEvent = obj.event;//获得 lay-event 对应的值
			if(layEvent == 'edit'){
				initChangeUserInfoTable(data.F_ACCOUNT);
			} else if(layEvent == 'resetPsw'){
				restpassword(data.F_ACCOUNT);
			} else if(layEvent == 'delete'){
				delUser(data.F_ACCOUNT);
			}
		});
		
		//监听头工具栏事件
		table.on('toolbar(dataTable)', function(obj){
			var checkStatus = table.checkStatus(obj.config.id);
			var data = checkStatus.data; //获取选中的数据
			switch(obj.event){
				case 'add':
					addtUser();
				break;
				case 'update':
				if(data.length == 0){
					layer.msg('请选择一行！');
				} else if(data.length > 1){
					layer.msg('只能编辑一行！');
				} else {
					initChangeUserInfoTable(data[0].F_ACCOUNT);
				}
				break;
				case 'delete':
				if(data.length == 0){
					layer.msg('至少选择一行！');
				} else {
					var accountLs = "";
					for(var i=0;i<data.length;i++){
						accountLs += data[i].F_ACCOUNT+","
					}
					delUser(accountLs);
				}
				break;
			};
		});
	});
}

//重置密码
function restpassword(account){
	parent.layer.confirm("确定要重置该用户密码吗？(默认密码为:123456)", {
		icon : 3,
		title : '提示'
	}, function(index) {
		parent.layer.close(index);
		$.ajax({
			type : 'post',
			url : rootPath + '/usermanage/restPassword',
			beforeSend: function () {
				showLoad();
			},
			data : {
				account : account
			},
			success : function(data) {
				parent.layer.alert(data.message,{icon: 1});
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				if(errorThrown == "Forbidden" || XMLHttpRequest.status == "403"
					 || XMLHttpRequest.status == "404"){
					parent.layer.alert(ForbiddenMsg,{icon: 7});
				}else{
					parent.layer.alert(errorThrown,{icon: 7});
				}
			},
			complete: function(XMLHttpRequest, textStatus) {
				closeLoad();
			}
		});
	});
}
//删除用户
function delUser(account){
	parent.layer.confirm("确定删除用户?", {
		icon : 3,
		title : '提示'
	}, function(index) {
		parent.layer.close(index);
		$.ajax({
			type : 'post',
			url : rootPath + '/usermanage/deleteUser',
			beforeSend: function () {
				showLoad();
			},
			data : {
				account : account
			},
			success : function(data) {
				parent.layer.alert(data.message,{icon: 1});
				getUserInfo();
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				if(errorThrown == "Forbidden" || XMLHttpRequest.status == "403"
					 || XMLHttpRequest.status == "404"){
					parent.layer.alert(ForbiddenMsg,{icon: 7});
				}else{
					parent.layer.alert(errorThrown,{icon: 7});
				}
			},
			complete: function(XMLHttpRequest, textStatus) {
				closeLoad();
			}
		});
	});
}
//显示增加用户层
function addtUser(){
	var addUserContent = "<table class='layui-table'>"+
		"<tr>"+
		"<td style='text-align: right;'>帐　　号：</td>"+
		"<td><input class='layui-input' id='account' placeholder='*请输入用户名' type='text' style='width: 170px;'/></td>"+
		"<td style='text-align: right;'>密　　码：</td>"+
		"<td><input class='layui-input' id='password' placeholder='*请输入密码' type='password' style='width: 170px;'/></td>"+
		"</tr>"+
		"<tr>"+
		"<td style='text-align: right;'>确认密码：</td>"+
		"<td>"+
		"<input class='layui-input' id='againpassword' placeholder='*请再次输入密码' type='password' style='width: 170px;'/>"+
		"<input class='layui-input' id='hiddenpassword' type='password' style='display: none;'/>"+
		"</td>"+
		"<td style='text-align: right;'>角　　色：</td>"+
		"<td>"+
		"<select class='layui-select' id='role' style='width: 170px;'></select>"+
		"</td>"+
		"</tr>"+
		"<tr>"+
		"<td style='text-align: right;'>组织机构：</td>"+
		"<td>"+
		"<select class='layui-select' id='org' style='width: 170px;'></select>"+
		"</td>"+
		"<td style='text-align: right;'>姓　　名：</td>"+
		"<td><input class='layui-input' id='name' placeholder='*请输入姓名' type='text' style='width: 170px;'/></td>"+
		"<tr>"+
		"<td style='text-align: right;'>性　　别：</td>"+
		"<td>"+
		"<select class='layui-select' id='sex' style='width: 170px;'>"+
		"<option value='' selected='selected'>请选择</option>"+
		"<option value='F'>男</option>"+
		"<option value='M'>女</option>"+
		"</select>"+
		"</td>"+
		"<td style='text-align: right;'>电　　话：</td>"+
		"<td><input class='layui-input' id='tel' placeholder='*请输入电话号码' type='text' style='width: 170px;'/></td>"+
		"</tr>"+
		"<tr>"+
		"<td style='text-align: right;'>邮　　箱：</td>"+
		"<td><input class='layui-input' id='email' placeholder='*请输入邮箱' type='text' style='width: 170px;'/></td>"+
		"<td style='text-align: right;'>地　　址：</td>"+
		"<td><input class='layui-input' id='address' placeholder='*请输入地址' type='text' style='width: 170px;'/></td>"+
		"</tr>"+
		"<tr>"+
		"<td style='text-align: right;'>Q　　&nbsp;Q：</td>"+
		"<td><input class='layui-input' id='qq' placeholder='*请输入QQ号' type='text' style='width: 170px;'/></td>"+
		"</tr>"+
		"<tr style='text-align: center;'>"+
		"<td style='text-align: center;' colspan='4'>"+
		"<input id='sureBtn' type='button' class='layui-btn' value='确定' onclick='sureAddUserInfo()'/>"+
		"<input id='cancelBtn' type='button' value='取消' class='layui-btn' onclick='cancelAddUser()'/>"+
		"</td>"+
		"</tr>"+
		"</table>";
	AddUserInfoIndex = layer.open({
		type: 1,
		area: ['650px','480px'],
		fix: true, //不固定
		maxmin: false,
		shade:0.4,
		title: '新增用户',
		content: addUserContent
	});
	//初始化角色选项
	$.ajax({
		type : 'post',
		url : rootPath + '/usermanage/getRoleInfo',
		beforeSend: function () {
			showLoad();
		},
		data : {
			use : "Y"
		},
		success : function(data) {
			document.getElementById("role").options.length = 0;
			document.getElementById("role").options.add(new Option("请选择",""));
			for(var i=0;i<data.length;i++){
				document.getElementById("role").options.add(new Option(data[i].F_ROLENAME,data[i].F_ROLENUM));
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			if(errorThrown == "Forbidden" || XMLHttpRequest.status == "403"
				 || XMLHttpRequest.status == "404"){
				parent.layer.alert(ForbiddenMsg,{icon: 7});
			}else{
				parent.layer.alert(errorThrown,{icon: 7});
			}
		},
		complete: function(XMLHttpRequest, textStatus) {
			closeLoad();
		}
	});
	//初始化组织机构选项
	$.ajax({
		type : 'post',
		url : rootPath + '/usermanage/getOrgInfo',
		beforeSend: function () {
			showLoad();
		},
		data : {
			use : "Y"
		},
		success : function(data) {
			document.getElementById("org").options.length = 0;
			document.getElementById("org").options.add(new Option("请选择",""));
			for(var i=0;i<data.length;i++){
				document.getElementById("org").options.add(new Option(data[i].F_ORG_NAME,data[i].F_ORG_ID));
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			if(errorThrown == "Forbidden" || XMLHttpRequest.status == "403"
				 || XMLHttpRequest.status == "404"){
				parent.layer.alert(ForbiddenMsg,{icon: 7});
			}else{
				parent.layer.alert(errorThrown,{icon: 7});
			}
		},
		complete: function(XMLHttpRequest, textStatus) {
			closeLoad();
		}
	});
}
//添加用户信息
function sureAddUserInfo(){
	var account = document.getElementById("account").value;
	var password = document.getElementById("password").value;
	password = hex_md5(password);
	var againpassword = document.getElementById("againpassword").value;
	againpassword = hex_md5(againpassword);
	var role = document.getElementById("role").value;
	var org = document.getElementById("org").value;
	var name = document.getElementById("name").value;
	var sex = document.getElementById("sex").value;
	var tel = document.getElementById("tel").value;
	var address = document.getElementById("address").value;
	var email = document.getElementById("email").value;
	var qq = document.getElementById("qq").value;
	//手机号正则
	var isPhone = /^([0-9]{3,4}-)?[0-9]{7,8}$/;
	var isMob = /^((\+?86)|(\(\+86\)))?(13[012356789][0-9]{8}|15[012356789][0-9]{8}|18[02356789][0-9]{8}|147[0-9]{8}|1349[0-9]{7})$/;
	//email正则
	var emailreg = /\w@\w*\.\w/;
	//QQ号正则
	var qqreg = /^\d{5,10}$/;
	if($.trim(account)==""||account==null){
		parent.layer.alert("请输入账号！",{icon: 7});
		return;
	}else if($.trim(password)==""||password==null){
		parent.layer.alert("请输入密码！",{icon: 7});
		return;
	}else if($.trim(againpassword)==""||againpassword==null){
		parent.layer.alert("请再次输入密码！",{icon: 7});
		return;
	}else if(password != againpassword){
		parent.layer.alert("两次密码不一致,请重新输入！",{icon: 7});
		return;
	}else if($.trim(role)==""||role==null){
		parent.layer.alert("请选择角色！",{icon: 7});
		return;
	}else if($.trim(org)==""||org==null){
		parent.layer.alert("请选择组织机构！",{icon: 7});
		return;
	}else if($.trim(name)==""||name==null){
		parent.layer.alert("请输入姓名！",{icon: 7});
		return;
	}else if($.trim(sex)==""||sex==null){
		parent.layer.alert("请选择性别！",{icon: 7});
		return;
	}else if(!isPhone.test(tel)&&!isMob.test(tel)){
		parent.layer.alert("请输入正确的手机号！",{icon: 7});
		return;
	}else if($.trim(address)==""||address==null){
		parent.layer.alert("请输入地址！",{icon: 7});
		return;
	}else if(!emailreg.test(email)){
		parent.layer.alert("请输入正确的邮箱地址！",{icon: 7});
		return;
	}else if(!qqreg.test(qq)){
		parent.layer.alert("请输入正确的QQ号！",{icon: 7});
		return;
	}else{
		name = encodeURI(name);
		address = encodeURI(address);
		$.ajax({
			type : 'post',
			url : rootPath + '/usermanage/addUser',
			beforeSend: function () {
				showLoad();
			},
			data : {
				account : account,
				password : password,
				role : role,
				org : org,
				name : name,
				sex: sex,
				tel : tel,
				address : address,
				email : email,
				qq : qq
			},
			success : function(data) {
				if(data.code=="0"){
					parent.layer.alert(data.message,{icon: 1});
					getUserInfo();
					cancelAddUser();
				}else{
					parent.layer.alert(data.message,{icon: 7});
				}
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				if(errorThrown == "Forbidden" || XMLHttpRequest.status == "403"
					 || XMLHttpRequest.status == "404"){
					parent.layer.alert(ForbiddenMsg,{icon: 7});
				}else{
					parent.layer.alert(errorThrown,{icon: 7});
				}
			},
			complete: function(XMLHttpRequest, textStatus) {
				closeLoad();
			}
		});
	}
}
//隐藏增加用户层
function cancelAddUser(){
	layer.close(AddUserInfoIndex);
}
//更改用户信息之前先查询角色
function initChangeUserInfoTable(account){
	var changeUserInfoContent = "<table class='layui-table'>"+
		"<tr>"+
		"<td style='text-align: right;'>帐　　号：</td>"+
		"<td><input class='layui-input' id='account' disabled='disabled' placeholder='*请输入用户名' type='text' style='width: 170px;'/></td>"+
		"<td style='text-align: right;'>密　　码：</td>"+
		"<td><input class='layui-input' id='password' placeholder='*请输入密码' type='password' style='width: 170px;'/></td>"+
		"</tr>"+
		"<tr>"+
		"<td style='text-align: right;'>确认密码：</td>"+
		"<td>"+
		"<input class='layui-input' id='againpassword' placeholder='*请再次输入密码' type='password' style='width: 170px;'/>"+
		"<input class='layui-input' id='hiddenpassword' type='password' style='display: none;'/>"+
		"</td>"+
		"<td style='text-align: right;'>角　　色：</td>"+
		"<td>"+
		"<select class='layui-select' id='role' style='width: 170px;'></select>"+
		"</td>"+
		"</tr>"+
		"<tr>"+
		"<td style='text-align: right;'>组织机构：</td>"+
		"<td>"+
		"<select class='layui-select' id='org' style='width: 170px;'></select>"+
		"</td>"+
		"<td style='text-align: right;'>姓　　名：</td>"+
		"<td><input class='layui-input' id='name' placeholder='*请输入姓名' type='text' style='width: 170px;'/></td>"+
		"<tr>"+
		"<td style='text-align: right;'>性　　别：</td>"+
		"<td>"+
		"<select class='layui-select' id='sex' style='width: 170px;'>"+
		"<option value='' selected='selected'>请选择</option>"+
		"<option value='F'>男</option>"+
		"<option value='M'>女</option>"+
		"</select>"+
		"</td>"+
		"<td style='text-align: right;'>电　　话：</td>"+
		"<td><input class='layui-input' id='tel' placeholder='*请输入电话号码' type='text' style='width: 170px;'/></td>"+
		"</tr>"+
		"<tr>"+
		"<td style='text-align: right;'>邮　　箱：</td>"+
		"<td><input class='layui-input' id='email' placeholder='*请输入邮箱' type='text' style='width: 170px;'/></td>"+
		"<td style='text-align: right;'>地　　址：</td>"+
		"<td><input class='layui-input' id='address' placeholder='*请输入地址' type='text' style='width: 170px;'/></td>"+
		"</tr>"+
		"<tr>"+
		"<td style='text-align: right;'>Q　　&nbsp;Q：</td>"+
		"<td><input class='layui-input' id='qq' placeholder='*请输入QQ号' type='text' style='width: 170px;'/></td>"+
		"</tr>"+
		"<tr style='text-align: center;'>"+
		"<td style='text-align: center;' colspan='4'>"+
		"<input id='sureBtn' type='button' class='layui-btn' value='确定'/>"+
		"<input id='cancelBtn' type='button' value='取消' class='layui-btn' onclick='cancelAddUser()'/>"+
		"</td>"+
		"</tr>"+
		"</table>";
	ChangeUserInfoIndex = layer.open({
		type: 1,
		area: ['650px','480px'],
		fix: true, //不固定
		maxmin: false,
		shade:0.4,
		title: '修改用户信息',
		content: changeUserInfoContent
	});
	//初始化角色
	$.ajax({
		type : 'post',
		url : rootPath + '/usermanage/getRoleInfo',
		beforeSend: function () {
			showLoad();
		},
		data : {
			use : "Y"
		},
		success : function(data) {
			if(data.length > 0){
				document.getElementById("role").options.length=0;
				document.getElementById("role").options.add(new Option("请选择",""));
				for(var i=0;i<data.length;i++){
					document.getElementById("role").options.add(new Option(data[i].F_ROLENAME,data[i].F_ROLENUM));
				}
				initChangeUserInfoData(account);
			}else{
				cancelChangeUserInfo();
				parent.layer.alert("无可用角色，请先维护角色！",{icon: 7});
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			if(errorThrown == "Forbidden" || XMLHttpRequest.status == "403"
				 || XMLHttpRequest.status == "404"){
				parent.layer.alert(ForbiddenMsg,{icon: 7});
			}else{
				parent.layer.alert(errorThrown,{icon: 7});
			}
		},
		complete: function(XMLHttpRequest, textStatus) {
			closeLoad();
		}
	});
}
//更改用户信息之前获取用户信息(复用用户增加，但是帐号不可修改)
function initChangeUserInfoData(account){
	//初始化组织机构选项
	$.ajax({
		type : 'post',
		url : rootPath + '/usermanage/getOrgInfo',
		beforeSend: function () {
			showLoad();
		},
		data : {
			use : "Y"
		},
		success : function(data) {
			if(data.length > 0){
				document.getElementById("org").options.length = 0;
				document.getElementById("org").options.add(new Option("请选择",""));
				for(var i=0;i<data.length;i++){
					document.getElementById("org").options.add(new Option(data[i].F_ORG_NAME,data[i].F_ORG_ID));
				}
				getOneUserInfo(account);
			}else{
				cancelChangeUserInfo();
				parent.layer.alert("无可用组织机构，请先维护组织机构！",{icon: 7});
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			if(errorThrown == "Forbidden" || XMLHttpRequest.status == "403"
				 || XMLHttpRequest.status == "404"){
				parent.layer.alert(ForbiddenMsg,{icon: 7});
			}else{
				parent.layer.alert(errorThrown,{icon: 7});
			}
		},
		complete: function(XMLHttpRequest, textStatus) {
			closeLoad();
		}
	});
}
function getOneUserInfo(account){
	$.ajax({
		type : 'post',
		url : rootPath + '/usermanage/getOneUserInfo',
		beforeSend: function () {
			showLoad();
		},
		data : {
			account : account
		},
		success : function(data) {
			if(data.code == "0"){
				document.getElementById("account").value = data.account;
				document.getElementById("password").value = data.password;
				document.getElementById("againpassword").value = data.password;
				document.getElementById("hiddenpassword").value = data.password;
				document.getElementById("role").value = data.role;
				document.getElementById("org").value = data.org;
				document.getElementById("name").value = data.name;
				document.getElementById("sex").value = data.sex;
				document.getElementById("tel").value = data.tel;
				document.getElementById("address").value = data.address;
				document.getElementById("email").value = data.email;
				document.getElementById("qq").value = data.qq;
				document.getElementById("sureBtn").onclick = function(){sureChangeUserInfo(account)};
				document.getElementById("cancelBtn").onclick = function(){cancelChangeUserInfo()};
			}else{
				cancelChangeUserInfo();
				parent.layer.alert(data.message,{icon: 7});
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			if(errorThrown == "Forbidden" || XMLHttpRequest.status == "403"
				 || XMLHttpRequest.status == "404"){
				parent.layer.alert(ForbiddenMsg,{icon: 7});
			}else{
				parent.layer.alert(errorThrown,{icon: 7});
			}
		},
		complete: function(XMLHttpRequest, textStatus) {
			closeLoad();
		}
	});
}
//更改用户信息
function sureChangeUserInfo(account){
	var password = document.getElementById("password").value;
	var againpassword = document.getElementById("againpassword").value;
	var role = document.getElementById("role").value;
	var org = document.getElementById("org").value;
	var name = document.getElementById("name").value;
	var sex = document.getElementById("sex").value;
	var tel = document.getElementById("tel").value;
	var address = document.getElementById("address").value;
	var email = document.getElementById("email").value;
	var qq = document.getElementById("qq").value;
	//手机号正则
	var isPhone = /^([0-9]{3,4}-)?[0-9]{7,8}$/;
	var isMob = /^((\+?86)|(\(\+86\)))?(13[012356789][0-9]{8}|15[012356789][0-9]{8}|18[02356789][0-9]{8}|147[0-9]{8}|1349[0-9]{7})$/;
	//email正则
	var emailreg = /\w@\w*\.\w/;
	//QQ号正则
	var qqreg = /^\d{5,10}$/;
	if($.trim(account)==""||account==null){
		parent.layer.alert("请输入账号！",{icon: 7});
		return;
	}else if($.trim(password)==""||password==null){
		parent.layer.alert("请输入密码！",{icon: 7});
		return;
	}else if($.trim(againpassword)==""||againpassword==null){
		parent.layer.alert("请再次输入密码！",{icon: 7});
		return;
	}else if(password != againpassword){
		parent.layer.alert("两次密码不一致,请重新输入！",{icon: 7});
		return;
	}else if($.trim(role)==""||role==null){
		parent.layer.alert("请选择角色！",{icon: 7});
		return;
	}else if($.trim(org)==""||org==null){
		parent.layer.alert("请选择组织机构！",{icon: 7});
		return;
	}else if($.trim(name)==""||name==null){
		parent.layer.alert("请输入姓名！",{icon: 7});
		return;
	}else if($.trim(sex)==""||sex==null){
		parent.layer.alert("请选择性别！",{icon: 7});
		return;
	}else if(!isPhone.test(tel)&&!isMob.test(tel)){
		parent.layer.alert("请输入正确的手机号！",{icon: 7});
		return;
	}else if($.trim(address)==""||address==null){
		parent.layer.alert("请输入地址！",{icon: 7});
		return;
	}else if(!emailreg.test(email)){
		parent.layer.alert("请输入正确的邮箱地址！",{icon: 7});
		return;
	}else if(!qqreg.test(qq)){
		parent.layer.alert("请输入正确的QQ号！",{icon: 7});
		return;
	}else{
		if(document.getElementById("hiddenpassword").value != document.getElementById("password").value){
			password = hex_md5(password);
		}
		name = encodeURI(name);
		address = encodeURI(address);
		$.ajax({
			type : 'post',
			url : rootPath + '/usermanage/updateUserInfo',
			beforeSend: function () {
				showLoad();
			},
			data : {
				account : account,
				password : password,
				role : role,
				org : org,
				name : name,
				sex: sex,
				tel : tel,
				address : address,
				email : email,
				qq : qq
			},
			success : function(data) {
				parent.layer.alert(data.message,{icon: 1});
				getUserInfo();
				cancelChangeUserInfo();
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				if(errorThrown == "Forbidden" || XMLHttpRequest.status == "403"
					 || XMLHttpRequest.status == "404"){
					parent.layer.alert(ForbiddenMsg,{icon: 7});
				}else{
					parent.layer.alert(errorThrown,{icon: 7});
				}
			},
			complete: function(XMLHttpRequest, textStatus) {
				closeLoad();
			}
		});
	}
}
//取消修改用户信息
function cancelChangeUserInfo(){
	layer.close(ChangeUserInfoIndex);
}