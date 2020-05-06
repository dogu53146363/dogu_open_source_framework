/** main.js By Dogu E_mail:lzq_jn@qq.com HomePage:http://www.dogu.site */

var tab;
var tryUnlockTime = 1;
var changeUserInfoIndex = 0;
var setPswIndex = 0;
$(document).ready(function(){
	getMenuInfo();
});
//获取菜单信息
function getMenuInfo(){
	$.ajax({
		type : 'post',
		url : rootPath + '/SystemManage/getMenuInfo',
		success : function(data) {
			var SrceenLock = getCookie("SrceenLock");
			if(SrceenLock == "Y"){
				lock();
			}
			getMyDeskTopPagePath(data);//获取"我的桌面"路径
			if(data.AT_MESSAGE != ""){
				parent.layer.alert(decodeURI(data.AT_MESSAGE),{icon: 7});
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
//获取mydesktop路径
function getMyDeskTopPagePath(initData){
	$.ajax({
		type : 'post',
		url : rootPath + '/SystemManage/getDeskTopPath',
		success : function(data) {
			if(data.PATH != null && data.PATH != ""){
				var deskTopPagePath = data.PATH;
				//检验"我的桌面"路径是否配置正确
				$.ajax({
					type : 'post',
					url : rootPath + '/'+data.PATH,
					success : function(data) {
						$(".layui-tab-title").html("<li class='layui-this' lay-id='MAINPAGE'><i class='fa fa-dashboard' aria-hidden='true'></i> <cite>我的桌面</cite></li>");
						$(".layui-tab-content").html("<div id='mydesktop' class='layui-tab-item layui-show'><iframe class='iframe_content' src='"+deskTopPagePath+"'></iframe></div>");
						initMainPage(initData);//初始化main页面
					},
					error : function(XMLHttpRequest, textStatus, errorThrown) {
						initMainPage(initData);//初始化main页面
						parent.layer.alert("【我的桌面】页面路径配置不正确或未分配给当前登陆用户权限，请检查！",{icon: 7});
					}
				});
			}else{
				initMainPage(initData);//初始化main页面
				parent.layer.alert("未设置【我的桌面】页面路径，请【'系统设置-->系统配置管理】中添加配置编号为【DESKTOP_PATH】的系统变量！",{icon: 7});
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			initMainPage(initData);//初始化main页面
			if(errorThrown == "Forbidden" || XMLHttpRequest.status == "403"){
				parent.layer.alert(ForbiddenMsg,{icon: 7});
			}else{
				parent.layer.alert(errorThrown,{icon: 7});
			}
		}
	});
}
//初始化mainPage
function initMainPage(initData){
	document.getElementById("UserName").innerHTML = initData.USER_NAME+"&nbsp;&nbsp;<i class='fa fa-user-circle-o'></i>";
	layui.config({
	    base: 'SYSTEM/main/js/',
	    version: new Date().getTime()
	}).use(['element', 'layer', 'navbar', 'tab'], function () {
	    var element = layui.element,
	        $ = layui.jquery,
	        layer = layui.layer,
	        navbar = layui.navbar();
	    tab = layui.tab({
	        elem: '.admin-nav-card', //设置选项卡容器
	        openWait: true,//是否显示等待
	        maxSetting: {//最大可打开tab页的数量
	        	max: 20,
	        	tipMsg: '为了系统的流畅性，最多只能打开20个tab标签！'
	        },
	        showClosed: true, //是否显示tab标签的关闭按钮
	        showContextMenu: true,//tab标签右键是否显示菜单
	        autoRefresh: true,//打开重复页面是否自动刷新
	        openOnSwitch : true,//是否打开切换刷新
	        showFooter: true,//是否展示footer
	        onSwitch: function (data) {//切换tab标签触发的事件
	        	var i = 0;
	        	var iframeIndex = 0;
	        	var tabIndex = data.index;
				$('.layui-tab-title').find('li').find("cite").each(function(){
					var title = $(this).html();//获取当前tab窗口的title
					if(tabIndex == i){
						//刷新
						$('.layui-tab-content').find('.layui-tab-item').find("iframe").each(function(){
							if(iframeIndex == tabIndex && (title == "需要刷新的title")){
								this.src = this.src;//刷新
							}
							iframeIndex++;
						});
					}
					i++;
				});
	        },
	        closeBefore: function (obj) { //tab 关闭之前触发的事件
	            //console.log(obj);
	            //obj.title  -- 标题
	            //obj.url    -- 链接地址
	            //obj.id     -- id
	            //obj.tabId  -- lay-id
	            //有需要的时候再打开，判断某个表确认关闭后再关闭
	            /**if (obj.title == 'BTable') {
	                layer.confirm('确定要关闭' + obj.title + '吗?', { icon: 3, title: '系统提示' }, function (index) {
	                    //因为confirm是非阻塞的，所以这里关闭当前tab需要调用一下deleteTab方法
	                    tab.deleteTab(obj.tabId);
	                    layer.close(index);
	                });
	                //返回true会直接关闭当前tab
	                return false;
	            }*/
	            return true;
	        }
	    });
	    //iframe自适应
	    $(window).on('resize', function () {
	        var $content = $('.admin-nav-card .layui-tab-content');
	        if(tab.config.showFooter){
	        	$("#admin-footer").css('display','block');
	        	$content.height($(this).height() - 130);
	        }else{
	        	$("#admin-footer").css('display','none');
	        	$content.height($(this).height() - 110);
	        }
	        $content.find('iframe').each(function () {
	            $(this).height($content.height());
	        });
	    }).resize();
	
	    //设置navbar
	    navbar.set({
	        spreadOne: true,
	        elem: '#admin-navbar-side',
	        cached: true,
	        data: initData.MENU_DATA,
			cached: false
			//url: 'datas/nav.json'*/
	    });
	    //渲染navbar
	    navbar.render();
	    //监听点击事件
	    navbar.on('click(side)', function (data) {
	        tab.tabAdd(data.field);
	    });
		//宽度伸缩
	    $('.admin-side-toggle').on('click', function () {
	        var sideWidth = $('#admin-side').width();
	        if (sideWidth == 200) {
	            $('#admin-body').animate({
	                left: '0'
	            }); //admin-footer
	            $('#admin-footer').animate({
	                left: '0'
	            });
	            $('#admin-side').animate({
	                width: '0'
	            });
	        } else {
	            $('#admin-body').animate({
	                left: '200px'
	            });
	            $('#admin-footer').animate({
	                left: '200px'
	            });
	            $('#admin-side').animate({
	                width: '200px'
	            });
	        }
	    });
	    //全屏模式
	    $('.admin-side-full').on('click', function () {
	        var docElm = document.documentElement;
	        //W3C  
	        if (docElm.requestFullscreen) {
	            docElm.requestFullscreen();
	        }
	        //FireFox  
	        else if (docElm.mozRequestFullScreen) {
	            docElm.mozRequestFullScreen();
	        }
	        //Chrome等  
	        else if (docElm.webkitRequestFullScreen) {
	            docElm.webkitRequestFullScreen();
	        }
	        //IE11
	        else if (elem.msRequestFullscreen) {
	            elem.msRequestFullscreen();
	        }
	        layer.msg('按Esc或F11即可退出全屏');
	    });
	    //跳转到首页
	    $('.admin-goto-index').on('click', function () {
	        window.open(rootPath+"/index.jsp");
	    });
	    //个人信息
	    $('#userInfo').on('click', function () {
	        getUserInfo();
	    });
		//设置
	    $('#setPas').on('click', function () {
	        indexChangePsw();
	    });
	    //清除缓存
	    $('#clearCached').on('click', function () {
	        navbar.cleanCached();
	        layer.alert('清除完成!', { icon: 1, title: '系统提示' }, function () {
	            location.reload();//刷新
	        });
	    });
		//锁屏
	    $('#lock').on('click', function () {
	        prepareLock();
	    });
	    //登出
	    $('#logOut').on('click', function () {
	        loginout();
	    });
	    //手机设备的简单适配
	    var treeMobile = $('.site-tree-mobile'),
	        shadeMobile = $('.site-mobile-shade');
	    treeMobile.on('click', function () {
	        $('body').addClass('site-mobile');
	    });
	    shadeMobile.on('click', function () {
	        $('body').removeClass('site-mobile');
	    });
	});
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
            layero.find('button#unlock').on('click', function () {
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
/*登出*/
function loginout() {
	layer.confirm('确定退出?', {
		icon : 3,
		title : '提示'
	}, function(index) {
		layer.close(index);
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
function indexChangePsw(){
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