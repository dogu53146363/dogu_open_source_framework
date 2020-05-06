var selecteduserid = "";
$(document).ready(function() {
	getUserInfo();
});
$(window).resize(function() {
	document.getElementById("userTableId").style.height = (document.documentElement.clientHeight-120)+"px";
	document.getElementById("menuDivId").style.marginTop = "-"+(document.documentElement.clientHeight-69)+"px";
	document.getElementById("ztree").style.height = (document.documentElement.clientHeight-131)+"px";
});
//获取用户信息
function getUserInfo(){
	var account = document.getElementById("accountid").value;
	var username = document.getElementById("usernameid").value;
	$.ajax({
		type : 'post',
		url : rootPath + '/SystemManage/LimitgetUserInfo',
		beforeSend: function () {
			showLoad();
		},
		data : {
			account : account,
			username : username
		},
		success : function(data) {
			closeLoad();
			createusertable(data);
			getMenuInfo();
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			closeLoad();
			if(errorThrown == "Forbidden" || XMLHttpRequest.status == "403"){
				parent.layer.alert(ForbiddenMsg,{icon: 7});
			}else{
				parent.layer.alert(errorThrown,{icon: 7});
			}
		}
	});
}
//创建左面的用户表格
function createusertable(data){
	selecteduserid = "";
	document.getElementById("userTableId").innerHTML = "";
	var innertable = "<table class='layui-table' style='margin-top:0px;'>";
	innertable += "<tr><td>&nbsp;</td>";
	innertable += "<td style='text-align:center;color:#FF0000;width:40%;'>&nbsp;&nbsp;用户名</td>";
	innertable += "<td style='text-align:center;color:#FF0000;width:50%;'>&nbsp;&nbsp;姓名</td></tr>";
	for(var i=0;i<data.length;i++){
		innertable += "<tr>";
		innertable += "<td style='text-align:center;'><input class='usercheckbox' type='checkbox' onclick=\"selectMenuAfterSelectedUser('" + data[i].F_ACCOUNT + "')\" value = '"+data[i].F_ACCOUNT+"' /></td>";
		innertable += "<td style='text-align:left;'>"+data[i].F_ACCOUNT+"</td>";
		innertable += "<td style='text-align:left;'>"+data[i].F_USERNAME+"</td>";
		innertable += "</tr>";
	}
	innertable += "</table>";
	document.getElementById("userTableId").innerHTML = innertable;
	document.getElementById("userTableId").style.height = (document.documentElement.clientHeight-120)+"px";
}
//获取菜单字典
function getMenuInfo(){
	document.getElementById("menuDivId").style.marginTop = "-"+(document.documentElement.clientHeight-69)+"px";
	document.getElementById("ztree").style.height = (document.documentElement.clientHeight-131)+"px";
	//初始化树
	var setting = {
		async : {
			enable : true,
			type : 'post',
			url : rootPath + '/SystemManage/LimitgetMenuInfo',
		// /dataFilter: filter
		},
		check : {
			enable : true,
			chkStyle : "checkbox",
			chkboxType : {
				"Y" : "s",
				"N" : "ps"
			}
		},
		data : {
			simpleData : {
				enable : true,
				idKey : "id",
				pIdKey : "pId",
				rootPId : 0
			// 根节点
			}
		},
		callback : {
			onAsyncSuccess : zTreeOnAsyncSuccess,
		/** 回调函数的设置，异步提交成功的回调函数* */
		}
	};
	$.fn.zTree.init($("#ztree"), setting);
}
// 回调方法中的展开左右的树节点
function zTreeOnAsyncSuccess(event, treeId, treeNode, msg) {
	var zTree = $.fn.zTree.getZTreeObj("ztree");
	zTree.expandAll(true);
}
// 选中相应的节点
function selectMenuAfterSelectedUser(account){
	var checkbox = document.getElementsByClassName("usercheckbox");
	for(var b = 0;b <checkbox.length;b++){
		if(checkbox[b].value == account){
			checkbox[b].checked = true;
			selecteduserid = checkbox[b].value;
		}else{
			checkbox[b].checked = false;
		}
	}
	var ztree = $.fn.zTree.getZTreeObj("ztree");
	ztree.checkAllNodes(false); 
	$.ajax({
		type : 'post',
		url : rootPath + '/SystemManage/getSelectTreeNode',
		beforeSend: function () {
			showLoad();
		},
		data : {
			account : account,
			type : "USER"
		},
		success : function(data) {
			closeLoad();
			var node = null;
			for(var i=0;i<data.list.length;i++){
				node = ztree.getNodeByParam("id",data.list[i].F_MENUID);
				if(node != null && node != "" && node != "null" && typeof(node) != "null"){
					ztree.checkNode(node,true);//指定选中ID的节点
				}
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			closeLoad();
			if(errorThrown == "Forbidden" || XMLHttpRequest.status == "403"){
				parent.layer.alert(ForbiddenMsg,{icon: 7});
			}else{
				parent.layer.alert(errorThrown,{icon: 7});
			}
		}
	});
}
//保存修改后的权限
function SaveUserLimit(){
	var allmenuid = "";
	var treeObj = $.fn.zTree.getZTreeObj("ztree"), nodes = treeObj
			.getCheckedNodes(true), v = "";
	for ( var i = 0; i < nodes.length; i++) {
		allmenuid += nodes[i].id + ",";
	}
	if(selecteduserid == ""){
		parent.layer.alert("请选择所要操作的用户！",{icon: 7});
	}else{
		$.ajax({
			type : 'post',
			url : rootPath + '/SystemManage/SaveUserLimit',
			beforeSend: function () {
				showLoad();
			},
			data : {
				userid : selecteduserid,
				menuid : allmenuid,
				type : "USER"
			},
			success : function(data) {
				closeLoad();
				parent.layer.alert(data.message,{icon: 1});
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				closeLoad();
				if(errorThrown == "Forbidden" || XMLHttpRequest.status == "403"){
					parent.layer.alert(ForbiddenMsg,{icon: 7});
				}else{
					parent.layer.alert(errorThrown,{icon: 7});
				}
			}
		});
	}
}