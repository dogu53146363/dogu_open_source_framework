/** 
*通用js每个页面必须引用
*/
//以下代码是配置layui扩展模块的目录
layui.config({
    version: true,
    base: rootPath + '/SYSTEM/lib/layui-third-party/'
}).use(['layer', 'admin'], function () {
    var $ = layui.jquery;
    var layer = layui.layer;//注册layer
    var admin = layui.admin;
    // 移除loading动画
    setTimeout(function () {
		admin.removeLoading();
    }, window == top ? 600 : 100);
});
//超时提示
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
//打开新tab页
function openNewTab(title, url){
	layui.config({
	    version: true,
	    base: rootPath + '/SYSTEM/lib/layui-third-party/'
	}).use(['index'],function(){
		var index = layui.index;
		index.openTab({
			title: title, 
		    url: url,
		    end: function() {
		        // table.reload('userTable'); 
		    }
		});
	});
}
//根据url关闭tab页
function closeTab(url){
	layui.config({
	    version: true,
	    base: rootPath + '/SYSTEM/lib/layui-third-party/'
	}).use(['index'],function(){
		var index = layui.index;
		index.closeTab(url);
	});
}
//关闭当前tab页
function closeCurrentTab(){
	parent.layui.admin.events.closeThisTabs();
}
//根据url刷新tab
function refreshTab(url){
	layui.config({
	    version: true,
	    base: rootPath + '/SYSTEM/lib/layui-third-party/'
	}).use(['admin'],function(){
		var admin = layui.admin;
		layui.admin.refresh(url);
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
	//判断如果是刚打开页面有可能layer还没注册完成
	if(typeof(layer) != "undefined"){
		layer.close(loadIndex);
	}
}