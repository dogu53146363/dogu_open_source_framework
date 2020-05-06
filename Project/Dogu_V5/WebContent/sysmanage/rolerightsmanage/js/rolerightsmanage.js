var selectedRoleId = "";
$(document).ready(function() {
	getLimitType();
	getRoleInfo();
});
$(window).resize(function() {
	document.getElementById("roleTableid").style.height = (document.documentElement.clientHeight-120)+"px";
	document.getElementById("menuDivId").style.marginTop = "-"+(document.documentElement.clientHeight-78)+"px";
	document.getElementById("ztree").style.height = (document.documentElement.clientHeight-131)+"px";
});
//获取角色信息
function getRoleInfo(){
	var rolename = document.getElementById("rolenameid").value;
	rolename = encodeURI(rolename);
	$.ajax({
		type : 'post',
		url : rootPath + '/rolerightsmanage/getRoleInfo',
		beforeSend: function () {
			showLoad();
		},
		data : {
			rolename : rolename
		},
		success : function(data) {
			createRoleTable(data);
			getMenuInfo();
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
//创建左面的角色表格
function createRoleTable(data){
	selectedRoleId = "";
	document.getElementById("roleTableid").innerHTML = "";
	var innertable = "<table class='layui-table' style='margin-top:0px;'>";
	innertable += "<tr><td>&nbsp</td>";
	innertable += "<td style='text-align:center;color:#FF0000;width:75%;'>&nbsp;&nbsp;角色名</td></tr>";
	for(var i=0;i<data.length;i++){
		innertable += "<tr>";
		innertable += "<td style='text-align:center;'><input class='roleCheckBox' type='checkbox' onclick=\"selectMenuAfterSelectedRole('" + data[i].F_ROLENUM + "')\" value = '"+data[i].F_ROLENUM+"' /></td>";
		innertable += "<td style='text-align:left;'>&nbsp;&nbsp;"+data[i].F_ROLENAME+"</td>";
		innertable += "</tr>";
	}
	innertable += "</table>";
	document.getElementById("roleTableid").innerHTML = innertable;
	document.getElementById("roleTableid").style.height = (document.documentElement.clientHeight-120)+"px";
}
//获取菜单字典
function getMenuInfo(){
	document.getElementById("menuDivId").style.marginTop = "-"+(document.documentElement.clientHeight-78)+"px";
	document.getElementById("ztree").style.height = (document.documentElement.clientHeight-131)+"px";
	//初始化树
	var setting = {
		async : {
			enable : true,
			type : 'post',
			url : rootPath + '/rolerightsmanage/getMenuInfo',
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
				rootPId : -1
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
//选中相应的节点
function selectMenuAfterSelectedRole(account){
	var checkbox = document.getElementsByClassName("roleCheckBox");
	for(var b = 0;b <checkbox.length;b++){
		if(checkbox[b].value == account){
			checkbox[b].checked = true;
			selectedRoleId = checkbox[b].value;
		}else{
			checkbox[b].checked = false;
		}
	}
	var ztree = $.fn.zTree.getZTreeObj("ztree");
	ztree.checkAllNodes(false); 
	$.ajax({
		type : 'post',
		url : rootPath + '/rolerightsmanage/getSelectTreeNode',
		beforeSend: function () {
			showLoad();
		},
		data : {
			account : account,
			type : "ROLE"
		},
		success : function(data) {
			var node = null;
			for(var i=0;i<data.list.length;i++){
				node = ztree.getNodeByParam("id",data.list[i].F_MENUID);
				if(node != null && node != "" && node != "null" && typeof(node) != "null"){
					ztree.checkNode(node,true);//指定选中ID的节点
				}
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
//保存修改后的权限
function SaveRoleLimit(){
	var allmenuid = "";
	var treeObj = $.fn.zTree.getZTreeObj("ztree"), nodes = treeObj
			.getCheckedNodes(true), v = "";
	for ( var i = 0; i < nodes.length; i++) {
		allmenuid += nodes[i].id + ",";
	}
	if(selectedRoleId == ""){
		parent.layer.alert("请选择所要操作的角色！",{icon: 7});
	}else{
		$.ajax({
			type : 'post',
			url : rootPath + '/rolerightsmanage/saveRoleRights',
			beforeSend: function () {
				showLoad();
			},
			data : {
				roleid : selectedRoleId,
				menuid : allmenuid,
				type : "ROLE"
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
	}
}
//获取控制权限的类型
function getLimitType(){
	$.ajax({
		type : 'post',
		url : rootPath + '/rolerightsmanage/getRightsType',
		beforeSend: function () {
			showLoad();
		},
		data : {
			
		},
		success : function(data) {
			if(data.type == "ROLE"){
				
			}else if(data.type == "ORG"){
				parent.layer.alert("注意：当前设置的权限控制模式为【组织机构行权限】，配置此功能中的内容将不起作用！",{icon: 7});
			}else{
				parent.layer.alert("注意：当前设置的权限控制模式为混合模式！",{icon: 7});
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