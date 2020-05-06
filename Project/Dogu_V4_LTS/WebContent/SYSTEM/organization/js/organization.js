var queryOrgId = "";
var AddOrgIndex = 0;
var ChangeOrgIndex = 0;
$(document).ready(function() {
	initAll();
});
function initAll(){
	getOrgZtreeInfo();
}
//窗口变化自适应
$(window).resize(function() {
	document.getElementById("ztree").style.height = (document.documentElement.clientHeight-130)+"px";
	document.getElementById("dataTableDiv").style.marginTop = "-"+(document.documentElement.clientHeight-70)+"px";
});
//获取组织机构树信息
function getOrgZtreeInfo(){
	// 初始化树
	document.getElementById("ztree").style.height = (document.documentElement.clientHeight-130)+"px";
	var setting = {
		async : {
			enable : true,
			type : 'post',
			url : rootPath + '/SystemManage/getOrgInfo',
			otherParam: []
		},
		check : {
			enable : true,
			chkboxType:  { "Y": "", "N": "" }
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
			onAsyncSuccess : zTreeOnAsyncSuccess,
			onAsyncError: zTreeOnAsyncError,
			beforeClick : beforeClick,
		/** 回调函数的设置，异步提交成功的回调函数* */
		}
	};
	$.fn.zTree.init($("#ztree"), setting);
}
// 回调方法中的展开左右的树节点
function zTreeOnAsyncSuccess(event, treeId, treeNode, msg) {
	var treeObj = $.fn.zTree.getZTreeObj(treeId);
	treeObj.expandAll(true);
	var nodes = treeObj.getNodes();
	if (nodes.length>0) {
		treeObj.selectNode(nodes[0]);
	}else{
		queryOrgId = "-1";
	}
	getRootID();
}
//获取到根节点ID
function getRootID() {
    var treeObj = $.fn.zTree.getZTreeObj("ztree");
	var node = treeObj.getNodesByFilter(function (node) { return node.level == 0 }, true);
	//如果没有组织机构的时候则不查询table
	if(node != null){
		queryOrgId = node.f_org_id;
	}
	initOrgTable();
}
//ztree请求错误
function zTreeOnAsyncError(event, treeId, treeNode, XMLHttpRequest, textStatus, errorThrown) {
    if(errorThrown == "Forbidden" || XMLHttpRequest.status == "403"){
		parent.layer.alert(ForbiddenMsg,{icon: 7});
	}else{
		parent.layer.alert(errorThrown,{icon: 7});
	}
};
//单击某个树的节点
function beforeClick(treeId, treeNode) {
	queryOrgId = treeNode["f_org_id"];
	initOrgTable();
}
//初始化组织机构表
function initOrgTable(){
	document.getElementById("dataTableDiv").style.marginTop = "-"+(document.documentElement.clientHeight-70)+"px";
	layui.use(['layer', 'table', 'element'], function(){
		var table = layui.table //表格
		,element = layui.element //元素操作
		//执行一个 table 实例
		table.render({
			elem: '#dataTable'
			,height: 'full-80'
			,url: rootPath + '/SystemManage/getOrgTable' //数据接口
			,method: "post"
			,where: {
				orgID : queryOrgId
			}
			,title: '用户表'
			,page: true //开启分页
			,limit: 10
			,toolbar: 'default' //开启工具栏，此处显示默认图标，可以自定义模板，详见文档
			,totalRow: false //开启合计行
			,cols: [[ //表头
				 {type: 'checkbox', fixed: 'left'}
				,{field: 'id', title: '序号', width: 60, fixed: 'left', align: 'center'}
				,{field: 'f_org_id', title: '组织机构编号', width: 150, sort: false, fixed: 'left'}
				,{field: 'f_org_name', title: '组织机构名称', width: 200, sort: false}
				,{field: 'f_org_pid', title: '上级组织机构编号', width: 150, sort: false}
				,{field: 'f_use', title: '是否使用', width: 120, sort: false, templet: '#statusMask'}
				,{field: 'f_note', title: '备注', width: 250, align:'center', sort: false} 
				,{fixed: 'right', title: '操作', width: 200, align:'center', toolbar: '#dataTableBar'}
			]]
		});
		
		//监听行工具事件
		table.on('tool(dataTable)', function(obj){ //注：tool 是工具条事件名，test 是 table 原始容器的属性 lay-filter="对应的值"
			var data = obj.data;//获得当前行数据
			var layEvent = obj.event;//获得 lay-event 对应的值
			if(layEvent == 'edit'){
				initChangeOrgInfoTable(data.f_org_id);
			} else if(layEvent == 'delete'){
				delOrg(data.f_org_id);
			}
		});
		
		//监听头工具栏事件
		table.on('toolbar(dataTable)', function(obj){
			var checkStatus = table.checkStatus(obj.config.id);
			var data = checkStatus.data; //获取选中的数据
			switch(obj.event){
				case 'add':
					addOrg();
				break;
				case 'update':
				if(data.length == 0){
					layer.msg('请选择一行！');
				} else if(data.length > 1){
					layer.msg('只能编辑一行！');
				} else {
					initChangeOrgInfoTable(data[0].f_org_id);
				}
				break;
				case 'delete':
				if(data.length == 0){
					layer.msg('至少选择一行！');
				} else {
					var orgLs = "";
					for(var i=0;i<data.length;i++){
						orgLs += data[i].f_org_id+","
					}
					delOrg(orgLs);
				}
				break;
			};
		});
	});
}
//添加组织机构
function addOrg(){
	var addOrgContext = 
		"<table class='layui-table'>"+
		"<tr>"+
		"<td style='text-align: right;'>组织机构编号：</td>"+
		"<td><input id='orgId' class='layui-input' placeholder='*请输入组织机构编号' type='text' style='width: 250px;'/></td>"+
		"</tr>"+
		"<tr>"+
		"<td style='text-align: right;'>组织机构名称：</td>"+
		"<td><input id='orgName' class='layui-input' placeholder='*请输入组织机构名称' type='text' style='width: 250px;'/></td>"+
		"</tr>"+
		"<td style='text-align: right;'>所属组织机构编号：</td>"+
		"<td><input id='pOrgId' class='layui-input' placeholder='*请输入所属组织机构编号' value='"+queryOrgId+"' type='text' style='width: 250px;'/></td>"+
		"</tr>"+
		"<td style='text-align: right;'>备注：</td>"+
		"<td><input id='Note' class='layui-input' placeholder='*请输入备注' type='text' style='width: 250px;'/></td>"+
		"</tr>"+
		"<tr>"+
		"<td style='text-align: right;'>是否启用：</td>"+
		"<td>"+
		"&nbsp;<input id='Use' type='checkbox'/>"+
		"</td>"+
		"</tr>"+
		"<tr>"+
		"<td style='text-align: center;' colspan='2'>"+
		"<input id='sureBtn' type='button' class='layui-btn' value='确定' onclick='sureAddOrg()'/>"+
		"<input id='cancelBtn' type='button' value='取消' class='layui-btn' onclick='cancelAddOrg()'/>"+
		"</td>"+
		"</tr>"+
		"</table>";
	AddOrgIndex = layer.open({
		type: 1,
		area: ['450px','400px'],
		fix: true, //不固定
		maxmin: false,
		shade:0.4,
		title: '新增组织机构',
		content: addOrgContext
	});
}
//确定添加组织机构
function sureAddOrg(){
	var orgId = document.getElementById("orgId").value;
	var orgName = document.getElementById("orgName").value;
	var pOrgId = document.getElementById("pOrgId").value;
	var Note = document.getElementById("Note").value;
	var usecheckbox = document.getElementById("Use");
	var use = "N";
	if(usecheckbox.checked){
		use = "Y";
	}
	if($.trim(orgId) == "" || orgId == null){
		parent.layer.alert("请输入组织机构编号！",{icon: 7});
		return;
	}else if($.trim(orgName) == "" || orgName == null){
		parent.layer.alert("请请输入组织机构名称！",{icon: 7});
		return;
	}else if($.trim(pOrgId) == "" || pOrgId == null){
		parent.layer.alert("请请所属组织机构编号！",{icon: 7});
		return;
	}else if(orgId.length < pOrgId.length+2 && pOrgId != "-1"){
		parent.layer.alert("组织机构编号规则不正确，请重新填写！",{icon: 7});
		return;
	}else {
		if(pOrgId+"_" != orgId.substring(0,pOrgId.length+1) && pOrgId != "-1"){
			parent.layer.alert("组织机构编号规则不正确，请重新填写！",{icon: 7});
			return;
		}else{
			if(use == "N"){
				parent.layer.confirm('注意,添加的此组织机构状态为不启用！', {
					icon : 3,
					title : '提示'
				}, function(index) {
					parent.layer.close(index);
					orgName = encodeURI(orgName);
					Note = encodeURI(Note);
					$.ajax({
						type : 'post',
						url : rootPath + '/SystemManage/addOrg',
						data : {
							orgId : orgId,
							orgName : orgName,
							pOrgId : pOrgId,
							Note : Note,
							use : use
						},
						success : function(data) {
							if (data.code == "0") {
								cancelAddOrg();
								initAll();
								parent.layer.alert(data.message,{icon: 1});
							} else {
								parent.layer.alert(data.message,{icon: 7});
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
			}else{
				orgName = encodeURI(orgName);
				Note = encodeURI(Note);
				$.ajax({
					type : 'post',
					url : rootPath + '/SystemManage/addOrg',
					data : {
						orgId : orgId,
						orgName : orgName,
						pOrgId : pOrgId,
						Note : Note,
						use : use
					},
					success : function(data) {
						if (data.code == "0") {
							cancelAddOrg();
							initAll();
							parent.layer.alert(data.message,{icon: 1});
						} else {
							parent.layer.alert(data.message,{icon: 7});
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
	}
}
//取消添加组织机构
function cancelAddOrg(){
	layer.close(AddOrgIndex);
}
//修改组织机构信息初始化
function initChangeOrgInfoTable(orgId){
	var changeOrgContext = 
		"<table class='layui-table'>"+
		"<tr>"+
		"<td style='text-align: right;'>组织机构编号：</td>"+
		"<td><input id='orgId' class='layui-input' disabled='disabled' placeholder='*请输入组织机构编号' type='text' style='width: 250px;'/></td>"+
		"</tr>"+
		"<tr>"+
		"<td style='text-align: right;'>组织机构名称：</td>"+
		"<td><input id='orgName' class='layui-input' placeholder='*请输入组织机构名称' type='text' style='width: 250px;'/></td>"+
		"</tr>"+
		"<td style='text-align: right;'>所属组织机构编号：</td>"+
		"<td><input id='pOrgId' class='layui-input' placeholder='*请输入所属组织机构编号' type='text' style='width: 250px;'/></td>"+
		"</tr>"+
		"<td style='text-align: right;'>备注：</td>"+
		"<td><input id='Note' class='layui-input' placeholder='*请输入备注' type='text' style='width: 250px;'/></td>"+
		"</tr>"+
		"<tr>"+
		"<td style='text-align: right;'>是否启用：</td>"+
		"<td>"+
		"&nbsp;<input id='Use' type='checkbox'/>"+
		"</td>"+
		"</tr>"+
		"<tr>"+
		"<td style='text-align: center;' colspan='2'>"+
		"<input id='sureBtn' type='button' class='layui-btn' value='确定' onclick='sureChangeOrg()'/>"+
		"<input id='cancelBtn' type='button' value='取消' class='layui-btn' onclick='cancelChangeOrg()'/>"+
		"</td>"+
		"</tr>"+
		"</table>";
	ChangeOrgIndex = layer.open({
		type: 1,
		area: ['450px','400px'],
		fix: true, //不固定
		maxmin: false,
		shade:0.4,
		title: '修改组织机构',
		content: changeOrgContext
	});
	$.ajax({
		type : 'post',
		url : rootPath + '/SystemManage/getOneOrgInfo',
		beforeSend: function () {
			showLoad();
		},
		data : {
			orgId : orgId
		},
		success : function(data) {
			closeLoad();
			if(data.code == "0"){
				document.getElementById("orgId").value = data.orgId;
				document.getElementById("orgName").value = data.orgName;
				document.getElementById("pOrgId").value = data.orgPId;
				document.getElementById("Note").value = data.note;
				if(data.use == "Y"){
					document.getElementById("Use").checked = true;
				}else{
					document.getElementById("Use").checked = false;
				}
			}else{
				cancelChangeOrg();
				parent.layer.alert(data.message,{icon: 7});
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			closeLoad();
			cancelChangeOrg();
			if(errorThrown == "Forbidden" || XMLHttpRequest.status == "403"){
				parent.layer.alert(ForbiddenMsg,{icon: 7});
			}else{
				parent.layer.alert(errorThrown,{icon: 7});
			}
		}
	});
}
//确定修改组织机构
function sureChangeOrg(){
	var orgId = document.getElementById("orgId").value;
	var orgName = document.getElementById("orgName").value;
	var pOrgId = document.getElementById("pOrgId").value;
	var Note = document.getElementById("Note").value;
	var usecheckbox = document.getElementById("Use");
	var use = "N";
	if(usecheckbox.checked){
		use = "Y";
	}
	if($.trim(orgId) == "" || orgId == null){
		parent.layer.alert("请输入组织机构编号！",{icon: 7});
		return;
	}else if($.trim(orgName) == "" || orgName == null){
		parent.layer.alert("请请输入组织机构名称！",{icon: 7});
		return;
	}else if($.trim(pOrgId) == "" || pOrgId == null){
		parent.layer.alert("请请所属组织机构编号！",{icon: 7});
		return;
	}else if(use == "N"){
		parent.layer.confirm('注意,添加的此组织机构状态为不启用！', {
			icon : 3,
			title : '提示'
		}, function(index) {
			parent.layer.close(index);
			orgName = encodeURI(orgName);
			Note = encodeURI(Note);
			$.ajax({
				type : 'post',
				url : rootPath + '/SystemManage/changeOrg',
				beforeSend: function () {
					showLoad();
				},
				data : {
					orgId : orgId,
					orgName : orgName,
					pOrgId : pOrgId,
					Note : Note,
					use : use
				},
				success : function(data) {
					closeLoad();
					if (data.code == "0") {
						cancelChangeOrg();
						initAll();
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
		});
	}else{
		orgName = encodeURI(orgName);
		Note = encodeURI(Note);
		$.ajax({
			type : 'post',
			url : rootPath + '/SystemManage/changeOrg',
			beforeSend: function () {
				showLoad();
			},
			data : {
				orgId : orgId,
				orgName : orgName,
				pOrgId : pOrgId,
				Note : Note,
				use : use
			},
			success : function(data) {
				closeLoad();
				if (data.code == "0") {
					cancelChangeOrg();
					initAll();
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
//取消修改组织机构
function cancelChangeOrg(){
	layer.close(ChangeOrgIndex);
}
//头部删除选中组织机构
function titleDelete(){
	var treeObj = $.fn.zTree.getZTreeObj("ztree");
	var checkedNode = treeObj.getCheckedNodes(true);
	if(checkedNode.length < 1){
		parent.layer.alert("请选择要删除的节点！",{icon: 7});
		return;
	}else{
		var orgIdLs = "";
		for(var i=0;i<checkedNode.length;i++){
			if(i == 0){
				orgIdLs += checkedNode[i].f_org_id;
			}else{
				orgIdLs += ","+checkedNode[i].f_org_id;
			}
		}
		parent.layer.confirm('确认删除该节点吗？如果删除的节点下包含子节点，则相应的子节点也会被删除！', {
			icon : 3,
			title : '提示'
		}, function(index) {
			parent.layer.close(index);
			$.ajax({
				type : 'post',
				url : rootPath + '/SystemManage/beforeDelOrgQuery',
				beforeSend: function () {
					showLoad();
				},
				data : {
					orgId : orgIdLs
				},
				success : function(data) {
					closeLoad();
					if(data.code == "0"){
						sureDeleteOrg(orgIdLs);
					}else{
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
		});
	}
}
//删除组织机构
function delOrg(deleteOrgId){
	var orgIdLs = deleteOrgId.split(",");
	if(orgIdLs.length < 1){
		parent.layer.alert("请选择要删除的节点！",{icon: 7});
		return;
	}else if(orgIdLs[0] == ""){
		parent.layer.alert("请选择要删除的节点！",{icon: 7});
		return;
	}else {
		parent.layer.confirm('确认删除该节点吗？如果删除的节点下包含子节点，则相应的子节点也会被删除！', {
			icon : 3,
			title : '提示'
		}, function(index) {
			parent.layer.close(index);
			//删除组织机构之前的查询(查询是否还有用户属于该组织机构)
			$.ajax({
				type : 'post',
				url : rootPath + '/SystemManage/beforeDelOrgQuery',
				beforeSend: function () {
					showLoad();
				},
				data : {
					orgId : deleteOrgId
				},
				success : function(data) {
					closeLoad();
					if(data.code == "0"){
						sureDeleteOrg(deleteOrgId);
					}else{
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
		});
	}
}
//检查无误后删除组织机构
function sureDeleteOrg(deleteOrgId){
	$.ajax({
		type : 'post',
		url : rootPath + '/SystemManage/deleteOrg',
		beforeSend: function () {
			showLoad();
		},
		data : {
			orgId : deleteOrgId
		},
		success : function(data) {
			closeLoad();
			initAll();
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