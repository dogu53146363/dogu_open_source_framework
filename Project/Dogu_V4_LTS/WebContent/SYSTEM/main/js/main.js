$(document).ready(function() {
	initMenu();
});
//初始化菜单
function initMenu(){
	layui.config({
	    version: true,
	    base: rootPath + '/SYSTEM/lib/layui-third-party/'
	}).use(['layer', 'admin', 'index', 'laytpl', 'element'], function () {
        var $ = layui.jquery;
        var layer = layui.layer;
        var admin = layui.admin;
        var index = layui.index;
        var laytpl = layui.laytpl;
        var element = layui.element;
		
        // ajax渲染侧边栏
        $.get(rootPath + '/SystemManage/getMenuInfo', function (res) {
        	if(res.code == 200){
        		var sideNav = document.getElementById("sideNav").innerHTML;
        		laytpl(sideNav).render(res.data, function (html) {
	                $('*[lay-filter=admin-side-nav]').html(html);
	                element.render('nav');
	                admin.activeNav(rootPath+'/SYSTEM/myDesktop/jsp/myDesktop.jsp');//默认首页
	            });
	            //查询"我的桌面"路径
	            $.ajax({
					type : 'post',
					url : rootPath + '/SystemManage/getDeskTopPath',
					success : function(data) {
						//设置我的桌面
						if(data.PATH != null && data.PATH != ""){
							index.loadHome({
					            menuPath: rootPath+'/'+data.PATH,
					            menuName: '<i class="layui-icon layui-icon-home"></i>'
					        });
						}else{
							index.loadHome({
					            menuPath: '',
					            menuName: '<i class="layui-icon layui-icon-home"></i>'
					        });
						}
					},
					error : function(XMLHttpRequest, textStatus, errorThrown) {
						if(errorThrown == "Forbidden" || XMLHttpRequest.status == "403"){
							parent.layer.alert(ForbiddenMsg,{icon: 7});
						}else{
							parent.layer.alert(errorThrown,{icon: 7});
						}
					}
				});
		        //判断是否锁屏
		        var SrceenLock = getCookie("SrceenLock");
				if(SrceenLock == "Y"){
					lock();
				}
        	}else{
        		parent.layer.alert(res.msg,{icon: 7});
        	}
        }, 'json');
    });
}
//获取个人信息
function getUserInfo(){
	$.ajax({
		type : 'post',
		url : rootPath + '/SystemManage/getIndexChangeUserInfo',
		success : function(data) {
			initUserInfo(data);
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			if(errorThrown == "Forbidden" || XMLHttpRequest.status == "403"){
				parent.layer.alert(ForbiddenMsg,{icon: 7});
			}else{
				parent.layer.alert(errorThrown,{icon: 7});
			}
		}
	});
}
//初始化用户信息
function initUserInfo(data){
	var sex_f = "";
	var sex_m = "";
	if(data.sex == "F"){
		sex_f = "selected='selected'";
	}else if(data.sex == "M"){
		sex_m = "selected='selected'";
	}
	changeUserInfoIndex = layer.open({
		type: 1,
		area: ['360px','465px'],
		fix: true, //不固定
		maxmin: false,
		shade:0.4,
		title: '个人信息',
		content: "<div style='width:355px'>"+
					"<table class='layui-table' style='text-align:center;width:100%;'>" +
						"<tr style='height:20px;'></tr>"+
						"<tr style='height:40px;'>" +
							"<td style='font-size:20px;width:80px;'>账号：</td>"+
							"<td id='changeaccount' style='font-size:20px;'>"+data.account+"</td>"+
						"</tr>"+
						"<tr style='height:40px;'>" +
							"<td style='font-size:20px;'>姓名：</td>"+
							"<td><input id='changename' type='text' value='"+data.username+"'style='height:100%;width:100%;border:0pt;font-size:20px;text-align:center;'/></td>"+
						"</tr>"+
						"<tr style='height:40px;'>" +
							"<td style='font-size:20px;'>性别：</td>"+
							"<td><select id='changesex' style='width:100%;height:100%;border:0pt;font-size:20px;text-align:center;text-align-last: center;'><option value = 'F' "+sex_f+">男</option><option value = 'M' "+sex_m+">女</option></select></td>"+
						"</tr>"+
						"<tr style='height:40px;'>" +
							"<td style='font-size:20px;'>电话：</td>"+
							"<td><input id='changetel' type='text' value='"+data.tel+"'style='height:100%;width:100%;border:0pt;font-size:20px;text-align:center;'/></td>"+
						"</tr>"+
						"<tr style='height:40px;'>" +
							"<td style='font-size:20px;'>地址：</td>"+
							"<td><input id='changeaddress' type='text' value='"+data.address+"'style='height:100%;width:100%;border:0pt;font-size:20px;text-align:center;'/></td>"+
						"</tr>"+
						"<tr style='height:40px;'>" +
							"<td style='font-size:20px;'>邮箱：</td>"+
							"<td><input id='changeemail' type='text' value='"+data.email+"'style='height:100%;width:100%;border:0pt;font-size:20px;text-align:center;'/></td>"+
						"</tr>"+
						"<tr style='height:40px;'>" +
							"<td style='font-size:20px;'>Q&nbsp;Q：</td>"+
							"<td><input id='changeqq' type='text' value='"+data.qq+"'style='height:100%;width:100%;border:0pt;font-size:20px;text-align:center;'/></td>"+
						"</tr>"+
						"<tr>"+
							"<td style='height:50px;' colspan='2'>"+
								"<input type='button' class='layui-btn' value='确定' onclick='sureChangeInfo()' style='width:100px;height:35px;font-size:16px;'/>"+
								"　　　<input type='button' class='layui-btn' value='取消' onclick='cancelChangeInfo()' style='width:100px;height:35px;font-size:16px;'/>"+
							"</td>"+
						"</tr>"+
					"</table>"+
				"</div>"
	});
}
//确定修改信息方法
function sureChangeInfo(){
	var name = document.getElementById("changename").value;
	var sex = document.getElementById("changesex").value;
	var tel = document.getElementById("changetel").value;
	var address = document.getElementById("changeaddress").value;
	var email = document.getElementById("changeemail").value;
	var qq = document.getElementById("changeqq").value;
	//手机号正则
	var isPhone = /^([0-9]{3,4}-)?[0-9]{7,8}$/;
	var isMob = /^((\+?86)|(\(\+86\)))?(13[012356789][0-9]{8}|15[012356789][0-9]{8}|18[02356789][0-9]{8}|147[0-9]{8}|1349[0-9]{7})$/;
	//email正则
	var emailreg = /\w@\w*\.\w/;
	//QQ号正则
	var qqreg = /^\d{5,10}$/;
	if($.trim(name)==""||name==null){
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
			url : rootPath + '/SystemManage/indexChangeUser',
			data : {
				name : name,
				sex: sex,
				tel : tel,
				address : address,
				email : email,
				qq : qq
			},
			success : function(data) {
				layer.alert(data.message,{icon: 1});
				cancelChangeInfo();//关闭信息窗口
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				if(errorThrown == "Forbidden" || XMLHttpRequest.status == "403"){
					parent.layer.alert(ForbiddenMsg,{icon: 7});
				}else{
					parent.layer.alert(errorThrown,{icon: 7});
				}
			}
		});
	}
}
//隐藏修改信息页面
function cancelChangeInfo(){
	layer.close(changeUserInfoIndex);
}
//弹出更改密码页面
function ChangePsw(){
	setPswIndex = layer.open({
		type: 1,
		area: ['360px','290px'],
		fix: false, //不固定
		maxmin: false,
		shade:0.4,
		title: '修改密码',
		content: "<div style='width:355px'>"+
					"<table class='layui-table' style='text-align:center;'>" +
						"<tr style='height:20px;'></tr>"+
						"<tr style='height:40px;'>" +
							"<td style='font-size:20px;width:100px;'>旧密码：</td>"+
							"<td><input id='oldpassword' type='password' value=''style='height:100%;width:100%;border:0pt;font-size:20px;text-align:center;'/></td>"+
						"</tr>"+
						"<tr style='height:40px;'>" +
							"<td style='font-size:20px;'>新密码：</td>"+
							"<td><input id='newpassword' type='password' value=''style='height:100%;width:100%;border:0pt;font-size:20px;text-align:center;'/></td>"+
						"</tr>"+
						"<tr style='height:40px;'>" +
							"<td style='font-size:20px;'>密码确认：</td>"+
							"<td><input id='againpassword' type='password' value=''style='height:100%;width:100%;border:0pt;font-size:20px;text-align:center;'/></td>"+
						"</tr>"+
						"<tr>"+
							"<td style='height:40px;' colspan='2'>"+
								"<input type='button' class='layui-btn' value='确定' onclick='sureChangePsw()' style='width:100px;height:35px;font-size:16px;'/>"+
								"　　　<input type='button' class='layui-btn' value='取消' onclick='cancelChangePsw()' style='width:100px;height:35px;font-size:16px;'/>"+
							"</td>"+
						"</tr>"+
					"</table>"+
				"</div>"
	});
}
//确定修改密码
function sureChangePsw(){
	var oldpassword = document.getElementById("oldpassword").value;
	oldpassword = hex_md5(oldpassword);
	var newpassword = document.getElementById("newpassword").value;
	newpassword = hex_md5(newpassword);
	var againpassword = document.getElementById("againpassword").value;
	againpassword = hex_md5(againpassword);
	if(newpassword != againpassword){
		layer.alert("两次密码不一致！",{icon: 7})
	}else{
		$.ajax({
			type : 'post',
			url : rootPath + '/SystemManage/validatePassword',
			data : {
				oldpassword : oldpassword,
				newpassword: newpassword
			},
			success : function(data) {
				if(data.code == "0"){
					layer.alert(data.message,{icon: 1});
					cancelChangePsw();
				}else{
					layer.alert(data.message,{icon: 7});
				}
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				if(errorThrown == "Forbidden" || XMLHttpRequest.status == "403"){
					parent.layer.alert(ForbiddenMsg,{icon: 7});
				}else{
					parent.layer.alert(errorThrown,{icon: 7});
				}
			}
		});
	}
}
//取消更改密码
function cancelChangePsw(){
	layer.close(setPswIndex);
}
//锁定屏幕前先输入一次锁屏密码
function prepareLock(){
	layer.open({
        title: false,
        type: 1,
        closeBtn: 0,
        anim: 6,
        content: $('#lock-input').html(),
        shade: [0.9, '#393D49'],
        success: function (layero, lockNumIndex) {
            layero.find('button#putLockNum').on('click', function () {
                var pwd = $('#lockNum').val();
                if (pwd == '' || pwd == null) {
                    layer.msg('请设置解锁码..', {
                        icon: 2,
                        time: 1000
                    });
                }else{
                	setCookie("LockPsw",pwd,100);
                	layer.close(lockNumIndex);
					lock();
				}
            });
            layero.find('button#cancleLock').on('click', function () {
            	layer.close(lockNumIndex);
            });
        }
    });
}
//锁屏实现方法
var isShowLock = false;
var tryUnlockTime = 1;
function lock() {
    if (isShowLock){
    	return;
    }
    //自定页
    layer.open({
        title: false,
        type: 1,
        closeBtn: 0,
        anim: 6,
        content: $('#lock-temp').html(),
        shade: [0.9, '#393D49'],
        success: function (layero, lockIndex) {
            isShowLock = true;
            setCookie("SrceenLock","Y",100);
            //绑定解锁按钮的点击事件
            $('#unlock').on('click', function () {
                var pwd = $('#lockPwd').val();
                if (pwd == '' || pwd == null) {
                    layer.msg('请输入解锁码..', {
                        icon: 2,
                        time: 1000
                    });
                    return;
                }
        		if(tryUnlockTime >= 5){
            		parent.layer.alert("尝试次数过多，3秒后将自动退出！",{icon: 7});
            		delCookie("autologin");// 设置cookie立即失效
					delCookie("username");// 设置cookie立即失效
					delCookie("password");// 设置cookie立即失效
            		setTimeout("lockLogOut()","3000");
            	}else{
            		unlock(pwd);
            	}
            });
			/**
			 * 解锁操作方法
			 * @param {String} 用户名
			 * @param {String} 密码
			 */
            var unlock = function (pwd) {
            	var unLockNum = getCookie("LockPsw",pwd,100);
            	if(pwd == unLockNum){
            		tryUnlockTime = 1;
            		 //关闭锁屏层
                	layer.close(lockIndex);
                	delCookie("SrceenLock");
                	isShowLock = false;
            	}else{
            		parent.layer.alert("解锁码不正确，您还有"+(5-tryUnlockTime)+"机会！",{icon: 7});
            		tryUnlockTime ++;
            	}
            };
        }
    });
}
//锁屏的登出
function lockLogOut(){
	$.ajax({
		type : 'post',
		url : rootPath + '/loginout',
		success : function(data) {
			if (data == "success") {
				window.location.href = rootPath;
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			if(errorThrown == "Forbidden" || XMLHttpRequest.status == "403"){
				parent.layer.alert(ForbiddenMsg,{icon: 7});
			}else{
				parent.layer.alert(errorThrown,{icon: 7});
			}
		}
	});
}