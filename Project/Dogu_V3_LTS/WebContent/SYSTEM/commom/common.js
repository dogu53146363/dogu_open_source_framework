/**
 * 通用js
 */
var rootPath = getRootPath(true);
//js获取项目根路径，如： http://localhost:8080/Dogu
function getRootPath(includeProjectName){
	var returnRootPath = "";
    //获取当前网址，如： http://localhost:8080/Dogu/XXX/XXX.jsp
    var curWwwPath = window.document.location.href;
    //获取主机地址之后的目录，如：Dogu/XXX/XXX.jsp
    var pathName = window.document.location.pathname;
    var pos = curWwwPath.indexOf(pathName);
    //获取主机地址，如： http://localhost:8080
    var localhostPaht = curWwwPath.substring(0,pos);
    //获取带"/"的项目名，如：/Dogu
	var projectName = pathName.substring(0,pathName.substr(1).indexOf('/')+1);
	//发布的时候访问地址是否已包含项目路径
	if(includeProjectName){
		returnRootPath = localhostPaht+projectName;
	}else{
		returnRootPath = localhostPaht;
	}
    return returnRootPath;
}
var ForbiddenMsg = "登陆超时，或该账号已从其他端登陆，请刷新页面重新登录！";
//设置cookie
function setCookie(name,value,Days){
	var exp = new Date();
	exp.setTime(exp.getTime() + Days*24*60*60*1000);
	document.cookie = name + "="+ escape (value) + ";expires=" + exp.toGMTString();
}
//获取cookie的值
function getCookie(name){
	var arr,reg=new RegExp("(^| )"+name+"=([^;]*)(;|$)");
	if(arr=document.cookie.match(reg))
	return unescape(arr[2]);
	else
	return null;
}
//删除cookie
function delCookie(name){
	var exp = new Date();
	exp.setTime(exp.getTime() - 1);
	var cval=getCookie(name);
	if(cval!=null)
	document.cookie= name + "="+cval+";expires="+exp.toGMTString();
}

//注册layer方法
layui.define(['layer'], function(exports) {
	"use strict";
	var $ = layui.jquery,
		layer = layui.layer;
	var common = {
		/**
		 * 抛出一个异常错误信息
		 * @param {String} msg
		 */
		throwError: function(msg) {
			throw new Error(msg);
			return;
		},
		/**
		 * 弹出一个错误提示
		 * @param {String} msg
		 */
		msgError: function(msg) {
			layer.msg(msg, {
				icon: 5
			});
			return;
		}
	};
	exports('common', common);
});
//打开新tab页
function openNewTab(url,icon,title){
	parent.tab.tabAdd({
        href: url, //地址
        icon: icon,//icon
        title: title//标题
    });
}
//显示等待
var loadIndex = 0;
function showLoad(){
	//判断如果是刚打开页面有可能layer还没注册完成
	if(typeof(layer) != "undefined"){
		loadIndex = layer.load(0, { shade: [0.01, '#fff'] });
	}
}
//关闭等待
function closeLoad(){
	layer.close(loadIndex);
}