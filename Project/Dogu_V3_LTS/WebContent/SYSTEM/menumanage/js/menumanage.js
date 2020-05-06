var deleteMenuId = "";
var changeMenuId = "";
$(document).ready(function() {
	initMenuTree();
});
//窗口变化自适应
$(window).resize(function() {
	document.getElementById("ztree").style.height = (document.documentElement.clientHeight-130)+"px";
	document.getElementById("mxTableDIvId").style.height = (document.documentElement.clientHeight-120)+"px";
	document.getElementById("addmenudivid").style.marginTop = "-"+(document.documentElement.clientHeight-68)+"px";
});
//初始化菜单树
function initMenuTree(){
	document.getElementById("ztree").style.height = (document.documentElement.clientHeight-130)+"px";
	document.getElementById("mxTableDIvId").style.height = (document.documentElement.clientHeight-120)+"px";
	document.getElementById("addmenudivid").style.marginTop = "-"+(document.documentElement.clientHeight-68)+"px";
	// 初始化树
	var setting = {
		async : {
			enable : true,
			type : 'post',
			url : rootPath + '/SystemManage/QueryMenuTree',
		},
		check : {
			enable : true,
			chkStyle : "checkbox",
			chkboxType : {
				"Y" : "",
				"N" : ""
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
			onAsyncError: zTreeOnAsyncError,
			beforeClick : beforeClick,
			onCheck : onCheck,// 获取所有选中节点
		/** 回调函数的设置，异步提交成功的回调函数* */
		}
	};
	$.fn.zTree.init($("#ztree"), setting);
}
//刷新按钮
function refresh(){
	initMenuTree();
	hideaddmenutablediv();
	deleteMenuId = "";
	changeMenuId = "";
}
function beforeClick(treeId, treeNode) {
	changeMenuId = treeNode["menuid"];
	if(changeMenuId == "MENU"){
		hideaddmenutablediv();
		parent.parent.layer.alert("该菜单不能修改！",{icon: 7});
		return;
	}else{
		changeMenu();
	}
}
// 回调方法中的展开左右的树节点
function zTreeOnAsyncSuccess(event, treeId, treeNode, msg) {
	var zTree = $.fn.zTree.getZTreeObj("ztree");
	zTree.expandAll(true);
	var node = zTree.getNodeByParam('id', 1);// 获取id为1的点
	zTree.selectNode(node);// 选择点
}
//ztree请求错误
function zTreeOnAsyncError(event, treeId, treeNode, XMLHttpRequest, textStatus, errorThrown) {
    if(errorThrown == "Forbidden" || XMLHttpRequest.status == "403"){
		parent.layer.alert(ForbiddenMsg,{icon: 7});
	}else{
		parent.layer.alert(errorThrown,{icon: 7});
	}
};
// 获取选中的节点的值
function onCheck(e, treeId, treeNode) {
	deleteMenuId= "";
	var treeObj = $.fn.zTree.getZTreeObj("ztree"), nodes = treeObj
			.getCheckedNodes(true), v = "";
	for ( var i = 0; i < nodes.length; i++) {
		deleteMenuId += nodes[i].menuid + ",";
	}
	hideaddmenutablediv();
}
// 点击新增弹出新增菜单页面
function showaddMenudiv() {
	document.getElementById("addmenudivid").style.display = "block";
	document.getElementById("menu").value = "";
	document.getElementById("menuname").value = "";
	document.getElementById("pagepath").value = "";
	document.getElementById("menujb").value = "";
	document.getElementById("use").checked = false;
	document.getElementById("intercept").checked = false;
	document.getElementById("icon").value = "";
	document.getElementById("surebtn").onclick = function() {
		addmenu()
	};
}
// 点击修改弹出修改页面
function changeMenu() {
	if (changeMenuId == "") {
		parent.layer.alert("请先选择菜单！",{icon: 7});
		return;
	} else {
		var menuid = changeMenuId;
		$.ajax({
			type : 'post',
			url : rootPath + '/SystemManage/getOneMenuInfo',
			beforeSend: function () {
				showLoad();
			},
			data : {
				menuid : menuid
			},
			success : function(data) {
				closeLoad();
				if(data.code == "0"){
					document.getElementById("addmenudivid").style.display = "block";
					document.getElementById("menu").value = data.menu;
					document.getElementById("oldmenu").value = data.menu;
					document.getElementById("menuname").value = data.menuname;
					document.getElementById("pagepath").value = data.pagepath;
					document.getElementById("menujb").value = data.menujb;
					//是否启用
					if (data.use == "Y") {
						document.getElementById("use").checked = true;
					} else {
						document.getElementById("use").checked = false;
					}
					//是否被拦截器拦截
					if (data.intercept == "Y") {
						document.getElementById("intercept").checked = true;
					} else {
						document.getElementById("intercept").checked = false;
					}
					//是否显示
					if (data.show == "Y") {
						document.getElementById("show").checked = true;
					} else {
						document.getElementById("show").checked = false;
					}
					//菜单图标
					document.getElementById("icon").value = data.icon;
					//将确定按钮改为update
					document.getElementById("surebtn").onclick = function() {
						updateMenu();
					};
				}else{
					document.getElementById("addmenudivid").style.display = "none";
					parent.layer.alert(data.message,{icon: 7});
				}
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				closeLoad();
				document.getElementById("addmenudivid").style.display = "none";
				if(errorThrown == "Forbidden" || XMLHttpRequest.status == "403"){
					parent.layer.alert(ForbiddenMsg,{icon: 7});
				}else{
					parent.layer.alert(errorThrown,{icon: 7});
				}
			}
		});
	}
}
// 自动设置菜单的级数
function setMenuJSandIsNum(getvalue) {
	if (isNaN(getvalue)) {
		document.getElementById("menu").value = "";
		parent.layer.alert("菜单编号必须为数字！",{icon: 7});
	} else {
		if (getvalue.length == 4) {
			document.getElementById("menujb").value = "1";
		} else if (getvalue.length == 7) {
			document.getElementById("menujb").value = "2";
		} else {
			document.getElementById("menujb").value = "";
		}
	}
}
// 新增页面中的确定按钮(往后台传值并插入到库内)
function addmenu() {
	var menuid = document.getElementById("menu").value;
	var menuname = document.getElementById("menuname").value;
	var pagepath = document.getElementById("pagepath").value;
	var menujb = document.getElementById("menujb").value;
	var use = document.getElementById("use");
	var show  = document.getElementById("show");
	var intercept = document.getElementById("intercept");
	var icon = document.getElementById("icon").value;
	//是否启用
	var ifuse = "N";
	if (use.checked) {
		ifuse = "Y";
	}
	//是否显示
	var ifshow = "N";
	if(show.checked){
		ifshow = "Y";
	}
	//是否被拦截器拦截
	var ifintercept = "N";
	if (intercept.checked) {
		ifintercept = "Y";
	}
	if ($.trim(menuid) == "" || menuid == null) {
		parent.layer.alert("请输入菜单编号！",{icon: 7});
		return;
	} else if ($.trim(menuname) == "" || menuname == null) {
		parent.layer.alert("请输入菜单名称！",{icon: 7});
		return;
	} else if ($.trim(menujb) == "" || menujb == null) {
		parent.layer.alert("请选择菜单级别！",{icon: 7});
		return;
	} else if (($.trim(pagepath) == "" || pagepath == null) && menujb == "2") {
		parent.layer.alert("请输入菜单对应页面的路径！",{icon: 7});
		return;
	} else {
		menuname = encodeURI(menuname);
		$.ajax({
			type : 'post',
			url : rootPath + '/SystemManage/AddMenu',
			beforeSend: function () {
				showLoad();
			},
			data : {
				menuid : menuid,
				menuname : menuname,
				pagepath : pagepath,
				menujb : menujb,
				ifuse : ifuse,
				ifshow : ifshow,
				ifintercept : ifintercept,
				icon : icon
			},
			success : function(data) {
				closeLoad();
				if (data.code == "0") {
					hideaddmenutablediv();
					initMenuTree();
					parent.layer.alert(data.message,{icon: 1});
				} else {
					parent.layer.alert(data.message,{icon: 7});
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
}
// 修改页面中的确定按钮(往后台传值并更新字段)
function updateMenu() {
	var menuid = document.getElementById("menu").value;
	var oldmenuid = document.getElementById("oldmenu").value;
	var menuname = document.getElementById("menuname").value;
	var pagepath = document.getElementById("pagepath").value;
	var menujb = document.getElementById("menujb").value;
	var use = document.getElementById("use");
	var show  = document.getElementById("show");
	var intercept = document.getElementById("intercept");
	var icon = document.getElementById("icon").value;
	//是否启用
	var ifuse = "N";
	if (use.checked) {
		ifuse = "Y";
	}
	//是否显示
	var ifshow = "N";
	if(show.checked){
		ifshow = "Y";
	}
	//是否被拦截器拦截
	var ifintercept = "N";
	if (intercept.checked) {
		ifintercept = "Y";
	}
	if ($.trim(menuid) == "" || menuid == null) {
		parent.layer.alert("请输入菜单编号！",{icon: 7});
		return;
	} else if ($.trim(menuname) == "" || menuname == null) {
		parent.layer.alert("请输入菜单名称！",{icon: 7});
		return;
	} else if ($.trim(menujb) == "" || menujb == null) {
		parent.layer.alert("请选择菜单级别！",{icon: 7});
		return;
	} else if (($.trim(pagepath) == "" || pagepath == null) && menujb == "2") {
		parent.layer.alert("请输入菜单对应页面的路径！",{icon: 7});
		return;
	} else {
		menuname = encodeURI(menuname);
		$.ajax({
			type : 'post',
			url : rootPath + '/SystemManage/UpdateMenu',
			beforeSend: function () {
				showLoad();
			},
			data : {
				menuid : menuid,
				oldmenuid : oldmenuid,
				menuname : menuname,
				pagepath : pagepath,
				menujb : menujb,
				ifuse : ifuse,
				ifshow : ifshow,
				ifintercept : ifintercept,
				icon : icon
			},
			success : function(data) {
				closeLoad();
				if (data.code == "0") {
					hideaddmenutablediv();
					initMenuTree();
					parent.layer.alert(data.message,{icon: 1});
				} else {
					parent.layer.alert(data.message,{icon: 7});
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
}
// 删除菜单
function deleteMenu() {
	var menuid = deleteMenuId.split(",");
	if(menuid[0] == "MENU"){
		parent.layer.alert("顶级菜单不能删除！",{icon: 7});
		return;
	}else if(menuid.length < 1){
		parent.layer.alert("请选择要删除的菜单！",{icon: 7});
		return;
	}else if(menuid[0] == ""){
		parent.layer.alert("请选择要删除的菜单！",{icon: 7});
		return;
	}else {
		parent.layer.confirm('确认删除该菜单吗？如果删除的为母菜单则相应的子菜单也会被删除！', {
			icon : 3,
			title : '提示'
		}, function(index) {
			parent.layer.close(index);
			$.ajax({
				type : 'post',
				url : rootPath + '/SystemManage/DeleteMenu',
				beforeSend: function () {
					showLoad();
				},
				data : {
					menuid : deleteMenuId
				},
				success : function(data) {
					closeLoad();
					hideaddmenutablediv();
					initMenuTree();
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
		});
	}
}
// 隐藏新增/修改菜单div
function hideaddmenutablediv() {
	document.getElementById("addmenudivid").style.display = "none";
}
