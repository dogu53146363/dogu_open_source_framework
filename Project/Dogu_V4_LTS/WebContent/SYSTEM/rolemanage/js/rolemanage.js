var AddRoleIndex = 0;
var ChangeRoleIndex = 0;
$(document).ready(function() {
	getRoleInfo();
});
//获取角色字典
function getRoleInfo(){
	var use = document.getElementById("useid").value;
	layui.use(['layer', 'table', 'element'], function(){
		var table = layui.table //表格
		,element = layui.element //元素操作
		//执行一个 table 实例
		table.render({
			elem: '#dataTable'
			,height: 'full-70'
			,url: rootPath + '/SystemManage/getRoleInfo' //数据接口
			,method: "post"
			,where: {
				use : use
			}
			,title: '用户表'
			,page: true //开启分页
			,limit: 10
			,toolbar: 'default' //开启工具栏，此处显示默认图标，可以自定义模板，详见文档
			,totalRow: false //开启合计行
			,cols: [[ //表头
				 {type: 'checkbox', fixed: 'left'}
				,{field: 'id', title: '序号', width: 60, fixed: 'left', align: 'center'}
				,{field: 'f_rolenum', title: '角色编号', width: 200, sort: true, fixed: 'left'}
				,{field: 'f_rolename', title: '角色名称	', width: 250, sort: true}
				,{field: 'f_use', title: '角色状态', width: 150, sort: true, templet: '#statusMask'}
				,{fixed: 'right', title: '操作', width: 160, align:'center', toolbar: '#dataTableBar'}
			]]
		});
		
		//监听行工具事件
		table.on('tool(dataTable)', function(obj){ //注：tool 是工具条事件名，test 是 table 原始容器的属性 lay-filter="对应的值"
			var data = obj.data;//获得当前行数据
			var layEvent = obj.event;//获得 lay-event 对应的值
			if(layEvent == 'edit'){
				ChangeRole(data.f_rolenum);
			} else if(layEvent == 'delete'){
				delRole(data.f_rolenum);
			}
		});
		
		//监听头工具栏事件
		table.on('toolbar(dataTable)', function(obj){
			var checkStatus = table.checkStatus(obj.config.id);
			var data = checkStatus.data; //获取选中的数据
			switch(obj.event){
				case 'add':
					addRole();
				break;
				case 'update':
				if(data.length == 0){
					layer.msg('请选择一行！');
				} else if(data.length > 1){
					layer.msg('只能编辑一行！');
				} else {
					ChangeRole(data[0].f_rolenum);
				}
				break;
				case 'delete':
				if(data.length == 0){
					layer.msg('至少选择一行！');
				} else {
					var roleLs = "";
					for(var i=0;i<data.length;i++){
						roleLs += data[i].f_rolenum+","
					}
					delRole(roleLs);
				}
				break;
			};
		});
	});
}
//添加角色
function addRole(){
	var addRoleContext = 
		"<table class='layui-table'>"+
		"<tr>"+
		"<td style='text-align: center;font-size:20px;' colspan='2'>角色信息</td>"+
		"</tr>"+
		"<tr>"+
		"<td style='text-align: right;'>角色编号：</td>"+
		"<td><input id='roleid' class='layui-input' placeholder='*请输入角色编号' type='text' style='width: 250px;'/></td>"+
		"</tr>"+
		"<tr>"+
		"<td style='text-align: right;'>角色名：</td>"+
		"<td><input id='rolename' class='layui-input' placeholder='*请输入角色名' type='text' style='width: 250px;'/></td>"+
		"</tr>"+
		"<tr>"+
		"<td style='text-align: right;'>是否启用：</td>"+
		"<td>"+
		"&nbsp;<input id='use' type='checkbox'/>"+
		"</td>"+
		"</tr>"+
		"<tr>"+
		"<td style='text-align: center;' colspan='2'>"+
		"<input id='sureBtn' type='button' class='layui-btn' value='确定' onclick='sureAddRole()'/>"+
		"<input id='cancelBtn' type='button' value='取消' class='layui-btn' onclick='cancelAddRole()'/>"+
		"</td>"+
		"</tr>"+
		"</table>";
	AddRoleIndex = layer.open({
		type: 1,
		area: ['400px','320px'],
		fix: true, //不固定
		maxmin: false,
		shade:0.4,
		title: '新增角色',
		content: addRoleContext
	});
}
//点击确定按钮方法
function sureAddRole(){
	var roleId = document.getElementById("roleid").value;
	var roleName = document.getElementById("rolename").value;
	var usecheckbox = document.getElementById("use");
	var use = "N";
	if(usecheckbox.checked){
		use = "Y";
	}
	if($.trim(roleId) == "" || roleId == null){
		parent.layer.alert("请输入角色编号！",{icon: 7});
		return;
	}else if($.trim(roleName) == "" || roleName == null){
		parent.layer.alert("请输入角色名！",{icon: 7});
		return;
	}else if(use == "N"){
		parent.layer.confirm('注意,添加的此角色将不会被启用！', {
			icon : 3,
			title : '提示'
		}, function(index) {
			parent.layer.close(index);
			roleName = encodeURI(roleName);
			$.ajax({
				type : 'post',
				url : rootPath + '/SystemManage/addRole',
				beforeSend: function () {
					showLoad();
				},
				data : {
					roleid : roleId,
					rolename : roleName,
					use : use
				},
				success : function(data) {
					closeLoad();
					if (data.code == "0") {
						cancelAddRole();
						getRoleInfo();
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
		roleName = encodeURI(roleName);
		$.ajax({
			type : 'post',
			url : rootPath + '/SystemManage/addRole',
			beforeSend: function () {
				showLoad();
			},
			data : {
				roleid : roleId,
				rolename : roleName,
				use : use
			},
			success : function(data) {
				closeLoad();
				if (data.code == "0") {
					cancelAddRole();
					getRoleInfo();
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
//隐藏添加角色框
function cancelAddRole(){
	layer.close(AddRoleIndex);
}
//修改角色信息之前的获取角色信息
function ChangeRole(roleID){
	var changeRoleContext = 
		"<table class='layui-table'>"+
		"<tr>"+
		"<td style='text-align: center;font-size:20px;' colspan='2'>角色信息</td>"+
		"</tr>"+
		"<tr>"+
		"<td style='text-align: right;'>角色编号：</td>"+
		"<td><input id='roleid' class='layui-input' disabled='disabled' placeholder='*请输入角色编号' type='text' style='width: 250px;'/></td>"+
		"</tr>"+
		"<tr>"+
		"<td style='text-align: right;'>角色名：</td>"+
		"<td><input id='rolename' class='layui-input' placeholder='*请输入角色名' type='text' style='width: 250px;'/></td>"+
		"</tr>"+
		"<tr>"+
		"<td style='text-align: right;'>是否启用：</td>"+
		"<td>"+
		"<input id='use' type='checkbox'/>"+
		"</td>"+
		"</tr>"+
		"<tr>"+
		"<td style='text-align: center;' colspan='2'>"+
		"<input id='sureBtn' type='button' class='layui-btn' value='确定' onclick='sureChangeRole()'/>"+
		"<input id='cancelBtn' type='button' value='取消' class='layui-btn' onclick='cancelChangeRole()'/>"+
		"</td>"+
		"</tr>"+
		"</table>";
	ChangeRoleIndex = layer.open({
		type: 1,
		area: ['400px','320px'],
		fix: true, //不固定
		maxmin: false,
		shade:0.4,
		title: '修改角色',
		content: changeRoleContext
	});
	$.ajax({
		type : 'post',
		url : rootPath + '/SystemManage/getOneRoleInfo',
		beforeSend: function () {
			showLoad();
		},
		data : {
			roleid : roleID
		},
		success : function(data) {
			closeLoad();
			if(data.code == "0"){
				document.getElementById("roleid").value = data.roleid;
				document.getElementById("rolename").value = data.rolename;
				if(data.use == "Y"){
					document.getElementById("use").checked = true;
				}else{
					document.getElementById("use").checked = false;
				}
			}else{
				cancelChangeRole();
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
//修改角色信息
function sureChangeRole(){
	var roleid = document.getElementById("roleid").value;
	var rolename = document.getElementById("rolename").value;
	var usecheckbox = document.getElementById("use");
	var use = "N";
	if(usecheckbox.checked){
		use = "Y";
	}
	if($.trim(roleid) == "" || roleid == null){
		parent.layer.alert("请输入角色编号！",{icon: 7});
		return;
	}else if($.trim(rolename) == "" || rolename == null){
		parent.layer.alert("请输入角色名",{icon: 7});
		return;
	}else if(use == "N"){
		parent.layer.confirm('注意,此角色将不会被启用！', {
			icon : 3,
			title : '提示'
		}, function(index) {
			parent.layer.close(index);
			rolename = encodeURI(rolename);
			$.ajax({
				type : 'post',
				url : rootPath + '/SystemManage/updateRole',
				beforeSend: function () {
					showLoad();
				},
				data : {
					roleid : roleid,
					rolename : rolename,
					use : use
				},
				success : function(data) {
					closeLoad();
					cancelChangeRole();
					getRoleInfo();
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
	}else{
		rolename = encodeURI(rolename);
		$.ajax({
			type : 'post',
			url : rootPath + '/SystemManage/updateRole',
			beforeSend: function () {
				showLoad();
			},
			data : {
				roleid : roleid,
				rolename : rolename,
				use : use
			},
			success : function(data) {
				closeLoad();
				cancelChangeRole();
				getRoleInfo();
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
//隐藏修改角色框
function cancelChangeRole(){
	layer.close(ChangeRoleIndex);
}
//删除角色
function delRole(roleid){
	parent.layer.confirm('确定删除角色？', {
		icon : 3,
		title : '提示'
	}, function(index) {
		parent.layer.close(index);
		//删除组织机构之前的查询(查询是否还有用户属于该组织机构)
		$.ajax({
			type : 'post',
			url : rootPath + '/SystemManage/beforeDelRoleQuery',
			beforeSend: function () {
				showLoad();
			},
			data : {
				roleid : roleid
			},
			success : function(data) {
				closeLoad();
				if(data.code == "0"){
					suerDeleteRole(roleid);
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

//检查无误后删除角色
function suerDeleteRole(roleid){
	$.ajax({
		type : 'post',
		url : rootPath + '/SystemManage/deleteRole',
		beforeSend: function () {
			showLoad();
		},
		data : {
			roleid : roleid
		},
		success : function(data) {
			closeLoad();
			getRoleInfo();
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