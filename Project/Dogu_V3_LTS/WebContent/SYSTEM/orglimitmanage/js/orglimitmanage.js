var selectedOrgId = "";
$(document).ready(function() {
	getLimitType();
	initAll();
});
function initAll(){
	selectedOrgId = "";
	getOrgZtreeInfo();
	getMenuZtreeInfo();
}
//窗口变化自适应
$(window).resize(function() {
	document.getElementById("orgZtree").style.height = (document.documentElement.clientHeight-130)+"px";
	document.getElementById("menuDivId").style.marginTop = "-"+(document.documentElement.clientHeight-69)+"px";
	document.getElementById("menuZtree").style.height = (document.documentElement.clientHeight-130)+"px";
});
//获取组织机构树信息
function getOrgZtreeInfo(){
	// 初始化树
	document.getElementById("orgZtree").style.height = (document.documentElement.clientHeight-130)+"px";
	var setting = {
		async : {
			enable : true,
			type : 'post',
			url : rootPath + '/SystemManage/LimitGetOrgInfo',
			otherParam: []
		},
		check : {
			enable : false,
		},
		data : {
			simpleData : {
				enable : true,
				idKey : "f_org_id",
				pIdKey : "f_org_pid",
				rootPId : 0
			// 根节点
			}
		},
		callback : {
			onAsyncSuccess : orgZtreeOnAsyncSuccess,
			onAsyncError: orgZtreeOnAsyncError,
			beforeClick : orgBeforeClick,
		/** 回调函数的设置，异步提交成功的回调函数* */
		}
	};
	$.fn.zTree.init($("#orgZtree"), setting);
}
// 回调方法中的展开左右的树节点
function orgZtreeOnAsyncSuccess(event, treeId, treeNode, msg) {
	var treeObj = $.fn.zTree.getZTreeObj(treeId);
	treeObj.expandAll(true);
	var nodes = treeObj.getNodes();
}
//ztree请求错误
function orgZtreeOnAsyncError(event, treeId, treeNode, XMLHttpRequest, textStatus, errorThrown) {
    if(errorThrown == "Forbidden" || XMLHttpRequest.status == "403"){
		parent.layer.alert(ForbiddenMsg,{icon: 7});
	}else{
		parent.layer.alert(errorThrown,{icon: 7});
	}
};
//单击某个树的节点
function orgBeforeClick(treeId, treeNode) {
	selectedOrgId = treeNode["f_org_id"];
	selectMenuAfterSelectedOrg(selectedOrgId);
}
//初始化菜单树
function getMenuZtreeInfo(){
	document.getElementById("menuDivId").style.marginTop = "-"+(document.documentElement.clientHeight-69)+"px";
	document.getElementById("menuZtree").style.height = (document.documentElement.clientHeight-130)+"px";
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
	$.fn.zTree.init($("#menuZtree"), setting);
}
// 回调方法中的展开左右的树节点
function zTreeOnAsyncSuccess(event, treeId, treeNode, msg) {
	var zTree = $.fn.zTree.getZTreeObj("menuZtree");
	zTree.expandAll(true);
}
// 选中相应的节点
function selectMenuAfterSelectedOrg(account){
	var ztree = $.fn.zTree.getZTreeObj("menuZtree");
	ztree.checkAllNodes(false); 
	$.ajax({
		type : 'post',
		url : rootPath + '/SystemManage/getSelectTreeNode',
		beforeSend: function () {
			showLoad();
		},
		data : {
			account : account,
			type : "ORG"
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
function SaveOrgLimit(){
	var allmenuid = "";
	var treeObj = $.fn.zTree.getZTreeObj("menuZtree"), nodes = treeObj
			.getCheckedNodes(true), v = "";
	for ( var i = 0; i < nodes.length; i++) {
		allmenuid += nodes[i].id + ",";
	}
	if(selectedOrgId == ""){
		parent.layer.alert("请选择所要操作的组织机构！",{icon: 7});
	}else{
		$.ajax({
			type : 'post',
			url : rootPath + '/SystemManage/SaveOrgLimit',
			beforeSend: function () {
				showLoad();
			},
			data : {
				orgid : selectedOrgId,
				menuid : allmenuid,
				type : "ORG"
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
//获取控制权限的类型
function getLimitType(){
	$.ajax({
		type : 'post',
		url : rootPath + '/SystemManage/getLimitType',
		beforeSend: function () {
			showLoad();
		},
		data : {
			
		},
		success : function(data) {
			closeLoad();
			if(data.type == "ROLE"){
				parent.layer.alert("注意：当前设置的权限控制模式为【角色行权限】，配置此功能中的内容将不起作用！",{icon: 7});
			}else if(data.type == "ORG"){
				
			}else{
				parent.layer.alert("注意：当前设置的权限控制模式为混合模式！",{icon: 7});
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