var AddMenuIndex = 0;
var EditMenuIndex = 0;
$(document).ready(function() {
	getMenuTreeInfo();
});
//获取菜单信息
function getMenuTreeInfo(){
	layui.config({
	    version: true,
	    base: rootPath + '/common/lib/layui-third-party/'
	}).use(['layer', 'form', 'table', 'admin', 'treetable'], function () {
        var $ = layui.jquery;
        var layer = layui.layer;
        var form = layui.form;
        var table = layui.table;
        var admin = layui.admin;
        var treetable = layui.treetable;

        // 渲染表格
        function renderTable() {
            treetable.render({
                treeColIndex: 1,
                treeSpid: -1,
                treeIdName: 'F_ID',
                treePidName: 'F_PID',
                treeDefaultClose: true,//是否默认折叠
                treeLinkage: false,//父级展开时是否自动展开所有子级
                elem: '#orgTableTree',
                url: rootPath + '/menumanage/queryMenuTree',
                cellMinWidth: 80,
                height: 'full-100',
                cols: [[
                    {type: 'numbers', title: '排序'},
                    {field: 'F_ID', title: 'ID', minWidth: 150},
                    {field: 'F_NAME', title: '名称', minWidth: 160},
                    {field: 'F_PID', title: '父ID', minWidth: 100},
                    {field: 'F_FUNC_PATH', title: '功能路径', minWidth: 120},
                    {field: 'F_PATH', title: '页面路径', minWidth: 120},
                    {field: 'F_ICON', title: '图标', minWidth: 120},
                    {
                        title: '类型', templet: function (d) {
                        	var status = "";
                        	if(d.F_TYPE == "MENU"){
                        		status = "<a>菜单";
                        	}else if(d.F_TYPE == "FUNC"){
                        		status = "<a>功能";
                        	}
                        	status += "</a>"
                            return status;
                        }
                    },
                    {
                        title: '状态', templet: function (d) {
                        	var status = "";
                        	if(d.F_USE == "Y"){
                        		status = "<a style='color:green;'>启用";
                        	}else{
                        		status = "<a style='color:red;'>停用";
                        	}
                        	status += "</a>"
                            return status;
                        }, align: 'center'
                    	, minWidth: 100
                    },
                    {
                        title: '展示否', templet: function (d) {
                        	var ifShow = "";
                        	if(d.F_SHOW == "Y"){
                        		ifShow = "<a style='color:green;'>是";
                        	}else{
                        		ifShow = "<a style='color:red;'>否";
                        	}
                        	ifShow += "</a>"
                            return ifShow;
                        }, align: 'center'
                    },
                    {
                        title: '权限拦截', templet: function (d) {
                        	var ifInterceptor = "";
                        	if(d.F_INTERCEPT == "Y"){
                        		ifInterceptor = "<a style='color:green;'>是";
                        	}else{
                        		ifInterceptor = "<a style='color:red;'>否";
                        	}
                        	ifInterceptor += "</a>"
                            return ifInterceptor;
                        }, align: 'center'
                    },
                    {templet: '#tableBarMask', title: '操作', align: 'center', minWidth: 150}
                ]]
            });
        }
        renderTable();
		
		//添加菜单
		$('#addMenu').unbind("click");//解除绑定
        $('#addMenu').click(function () {
            addMenu();
        });
		
        // 工具条点击事件
        table.on('tool(orgTableTree)', function (obj) {
            var data = obj.data;
            var layEvent = obj.event;
            if (layEvent === 'edit') { //修改
                editMenu(data.F_ID);
            } else if (layEvent === 'del') { //删除
            	deleteMenu(data.F_ID, data.F_NAME);
            }
        });
		// 搜索按钮点击事件
		$('#btnSearchAuth').unbind("click");//解除绑定
        $('#btnSearchAuth').click(function () {
            $('#edtSearchAuth').removeClass('layui-form-danger');
            var keyword = $('#edtSearchAuth').val();
            var $tds = $('#orgTableTree').next('.treeTable').find('.layui-table-body tbody tr td');
            $tds.css('background-color', 'transparent');
            if (!keyword) {
                layer.msg("请输入关键字", {icon: 5, anim: 6});
                $('#edtSearchAuth').addClass('layui-form-danger');
                $('#edtSearchAuth').focus();
                return;
            }
            var searchCount = 0;
            $tds.each(function () {
                if ($(this).text().indexOf(keyword) >= 0) {
                    $(this).css('background-color', '#FAE6A0');
                    if (searchCount == 0) {
                        $('body,html').stop(true);
                        $('body,html').animate({scrollTop: $(this).offset().top - 150}, 500);
                    }
                    searchCount++;
                }
            });
            if (searchCount == 0) {
                layer.msg("没有匹配结果", {icon: 5, anim: 6});
            } else {
                treetable.expandAll('#orgTableTree');
            }
        });
        // 搜索按钮点击事件
		$('#refreshMenu').unbind("click");//解除绑定
        $('#refreshMenu').click(function () {
        	location.reload();
        });
		//全部展开
		$('#btnExpandAuth').unbind("click");//解除绑定
        $('#btnExpandAuth').click(function () {
            treetable.expandAll('#orgTableTree');
        });
		//全部折叠
		$('#btnFoldAuth').unbind("click");//解除绑定
        $('#btnFoldAuth').click(function () {
            treetable.foldAll('#orgTableTree');
        });
    });
}
//添加定时任务
function addMenu(){
	var AddMenuContent = 
		"<table class='layui-table'>"+
		"<tr>"+
		"<td style='text-align: right;'>ID：</td>"+
		"<td style='text-align: left;'><input id='id_Input' class='layui-input' placeholder='*请输入菜单/功能ID' type='text' style='width: 280px;'/></td>"+
		"</tr>"+
		"<tr>"+
		"<td style='text-align: right;'>名称：</td>"+
		"<td style='text-align: left;'><input id='name_Input' class='layui-input' placeholder='*请输入菜单/功能名称' type='text' style='width: 280px;'/></td>"+
		"</tr>"+
		"<tr>"+
		"<td style='text-align: right;'>父ID：</td>"+
		"<td style='text-align: left;'><input id='pid_Input' class='layui-input' placeholder='*请输入菜单/功能父ID' type='text' style='width: 280px;'/></td>"+
		"</tr>"+
		"<tr>"+
		"<td style='text-align: right;'>功能路径：</td>"+
		"<td style='text-align: left;'><input id='fpath_Input' class='layui-input' placeholder='*请输入功能路径' type='text' style='width: 280px;'/></td>"+
		"</tr>"+
		"<tr>"+
		"<td style='text-align: right;'>页面路径：</td>"+
		"<td style='text-align: left;'><input id='path_Input' class='layui-input' placeholder='*请输入页面路径' type='text' style='width: 280px;'/></td>"+
		"</tr>"+
		"<tr>"+
		"<td style='text-align: right;'>类型：</td>"+
		"<td style='text-align: left;'>" +
		"<select id='type_Input' class='layui-select' style='width:280px;'><option value='MENU'>菜单</option><option value='FUNC'>功能</option></select>" +
		"</td>"+
		"</tr>"+
		"<tr>"+
		"<td style='text-align: right;'>菜单图标：</td>"+
		"<td style='text-align: left;'><input id='icon_Input' class='layui-input' placeholder='*请输入菜单图标' type='text' style='width: 280px;'/></td>"+
		"</tr>"+
		"<tr>"+
		"<td style='text-align: right;'>是否启用：</td>"+
		"<td>"+
		"&nbsp;<input id='use_Check' type='checkbox'/>"+
		"</td>"+
		"</tr>"+
		"<tr>"+
		"<td style='text-align: right;'>是否显示：</td>"+
		"<td>"+
		"&nbsp;<input id='show_Check' type='checkbox'/>"+
		"</td>"+
		"</tr>"+
		"<tr>"+
		"<td style='text-align: right;'>权限拦截：</td>"+
		"<td>"+
		"&nbsp;<input id='interceptor_Check' type='checkbox'/>"+
		"</td>"+
		"</tr>"+
		"<tr>"+
		"<td colspan='2' style='text-align:center;'>"+
		"<input id='sureBtn' type='button' value='确定' onclick='sureAdd()' class='layui-btn'/>"+
		"<input id='cancelBtn' type='button' value='取消' onclick='cancelAdd()' class='layui-btn'/>"+
		"</td>"+
		"</tr>"+
		"</table>";
	AddMenuIndex = layer.open({
		type: 1,
		area: ['430px','650px'],
		fix: true, //不固定
		maxmin: false,
		shade:0.4,
		title: '新增菜单/功能',
		content: AddMenuContent
	});
}
//确定添加菜单
function sureAdd(){
	var id = document.getElementById("id_Input").value;
	var name = document.getElementById("name_Input").value;
	var pid = document.getElementById("pid_Input").value;
	var path = document.getElementById("path_Input").value;
	var icon = document.getElementById("icon_Input").value;
	var fpath = document.getElementById("fpath_Input").value;
	var type = document.getElementById("type_Input").value;
	var use = "N";
	if(document.getElementById("use_Check").checked){
		use = "Y";
	}
	var show = "N";
	if(document.getElementById("show_Check").checked){
		show = "Y";
	}
	var interceptor = "N";
	if(document.getElementById("interceptor_Check").checked){
		interceptor = "Y";
	}
	
	if($.trim(id) == "" || id == null){
		parent.layer.alert("请输入菜单/功能ID！",{icon: 7});
		return;
	}else if($.trim(name) == "" || name == null){
		parent.layer.alert("请输入菜单/功能名称！",{icon: 7});
		return;
	}else if($.trim(pid) == "" || pid == null){
		parent.layer.alert("请输入菜单/功能父ID！",{icon: 7});
		return;
	}else{
		name = encodeURI(name);
		path = encodeURI(path);
		fpath = encodeURI(fpath);
		$.ajax({
			type : 'post',
			url : rootPath + '/menumanage/addMenu',
			beforeSend: function () {
				showLoad();
			},
			data : {
				id: id,
				name: name,
				pid: pid,
				path: path,
				icon: icon,
				use: use,
				show: show,
				interceptor: interceptor,
				fpath: fpath,
				type: type
			},
			success : function(data) {
				if (data.code == "0") {
					getMenuTreeInfo();
					cancelAdd();
					parent.layer.alert(data.message,{icon: 1});
				} else {
					parent.layer.alert(data.message,{icon: 7});
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
}
//取消添加菜单
function cancelAdd(){
	layer.close(AddMenuIndex);
}
//修改菜单
function editMenu(menuid){
	$.ajax({
		type : 'post',
		url : rootPath + '/menumanage/getOneMenuInfo',
		beforeSend: function () {
			showLoad();
		},
		data : {
			menuid : menuid
		},
		success : function(data) {
			if(data.code == "0"){
				var UseChecked = "";
				if(data.Use == "Y"){
					UseChecked = "checked='checdek'";
				}
				
				var ShowChecked = "";
				if(data.Show == "Y"){
					ShowChecked = "checked='checked'"
				}
				
				var InterceptorChecked = "";
				if(data.Intercept == "Y"){
					InterceptorChecked = "checked='checked'"
				}
				
				var TypeMenuSelected = "";
				if(data.Type == "MENU"){
					TypeMenuSelected = "selected='selected'";
				}
				var TypeFuncSelected = "";
				if(data.Type == "FUNC"){
					TypeFuncSelected = "selected='selected'";
				}
				
				var EditMenuContent = 
					"<table class='layui-table'>"+
					"<tr>"+
					"<td style='text-align: right;'>ID：</td>"+
					"<td style='text-align: left;'><input id='id_Input' class='layui-input layui-disabled' placeholder='*请输入菜单/功能ID' disabled='disabled' value='"+data.Id+"' type='text' style='width: 280px;'/></td>"+
					"</tr>"+
					"<tr>"+
					"<td style='text-align: right;'>名称：</td>"+
					"<td style='text-align: left;'><input id='name_Input' class='layui-input' placeholder='*请输入菜单/功能名称' value='"+data.Name+"' type='text' style='width: 280px;'/></td>"+
					"</tr>"+
					"<tr>"+
					"<td style='text-align: right;'>父ID：</td>"+
					"<td style='text-align: left;'><input id='pid_Input' class='layui-input' placeholder='*请输入菜单/功能父ID' value='"+data.Pid+"' type='text' style='width: 280px;'/></td>"+
					"</tr>"+
					"<tr>"+
					"<td style='text-align: right;'>功能路径：</td>"+
					"<td style='text-align: left;'><input id='fpath_Input' class='layui-input' placeholder='*请输入功能路径' value='"+data.FunPath+"' type='text' style='width: 280px;'/></td>"+
					"</tr>"+
					"<tr>"+
					"<td style='text-align: right;'>页面路径：</td>"+
					"<td style='text-align: left;'><input id='path_Input' class='layui-input' placeholder='*请输入页面路径' value='"+data.Path+"' type='text' style='width: 280px;'/></td>"+
					"</tr>"+
					"<tr>"+
					"<td style='text-align: right;'>类型：</td>"+
					"<td style='text-align: left;'>" +
					"<select id='type_Input' class='layui-select' style='width:280px;'><option value='MENU' "+TypeMenuSelected+">菜单</option><option value='FUNC' "+TypeFuncSelected+">功能</option></select>" +
					"</td>"+
					"</tr>"+
					"<tr>"+
					"<td style='text-align: right;'>菜单图标：</td>"+
					"<td style='text-align: left;'><input id='icon_Input' class='layui-input' placeholder='*请输入菜单图标' value='"+data.Icon+"' type='text' style='width: 280px;'/></td>"+
					"</tr>"+
					"<tr>"+
					"<td style='text-align: right;'>是否启用：</td>"+
					"<td>"+
					"&nbsp;<input id='use_Check' type='checkbox' "+UseChecked+" />"+
					"</td>"+
					"</tr>"+
					"<tr>"+
					"<td style='text-align: right;'>是否显示：</td>"+
					"<td>"+
					"&nbsp;<input id='show_Check' type='checkbox' "+ShowChecked+" />"+
					"</td>"+
					"</tr>"+
					"<tr>"+
					"<td style='text-align: right;'>权限拦截：</td>"+
					"<td>"+
					"&nbsp;<input id='interceptor_Check' type='checkbox' "+InterceptorChecked+" />"+
					"</td>"+
					"</tr>"+
					"<tr>"+
					"<td colspan='2' style='text-align:center;'>"+
					"<input id='sureBtn' type='button' value='确定' onclick='sureUpdate()' class='layui-btn'/>"+
					"<input id='cancelBtn' type='button' value='取消' onclick='cancelUpdate()' class='layui-btn'/>"+
					"</td>"+
					"</tr>"+
					"</table>";
				EditMenuIndex = layer.open({
					type: 1,
					area: ['430px','650px'],
					fix: true, //不固定
					maxmin: false,
					shade:0.4,
					title: '修改菜单/功能-'+data.Name,
					content: EditMenuContent
				});
				
			}else{
				parent.layer.alert(data.message,{icon: 7});
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
//确定修改菜单
function sureUpdate(){
	var id = document.getElementById("id_Input").value;
	var name = document.getElementById("name_Input").value;
	var pid = document.getElementById("pid_Input").value;
	var path = document.getElementById("path_Input").value;
	var fpath = document.getElementById("fpath_Input").value;
	var icon = document.getElementById("icon_Input").value;
	var type = document.getElementById("type_Input").value;
	var use = "N";
	if(document.getElementById("use_Check").checked){
		use = "Y";
	}
	var show = "N";
	if(document.getElementById("show_Check").checked){
		show = "Y";
	}
	var interceptor = "N";
	if(document.getElementById("interceptor_Check").checked){
		interceptor = "Y";
	}
	
	if($.trim(id) == "" || id == null){
		parent.layer.alert("请输入菜单/功能ID！",{icon: 7});
	}else if($.trim(name) == "" || name == null){
		parent.layer.alert("请输入菜单/功能名称！",{icon: 7});
	}else if($.trim(pid) == "" || pid == null){
		parent.layer.alert("请输入菜单/功能父ID！",{icon: 7});
	}else{
		name = encodeURI(name);
		path = encodeURI(path);
		fpath = encodeURI(fpath);
		$.ajax({
			type : 'post',
			url : rootPath + '/menumanage/updateMenu',
			beforeSend: function () {
				showLoad();
			},
			data : {
				id: id,
				name: name,
				pid: pid,
				path: path,
				fpath: fpath,
				icon: icon,
				use: use,
				show: show,
				interceptor: interceptor,
				type: type
			},
			success : function(data) {
				if (data.code == "0") {
					getMenuTreeInfo();
					cancelUpdate();
					parent.layer.alert(data.message,{icon: 1});
				} else {
					parent.layer.alert(data.message,{icon: 7});
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
}
//取消修改菜单
function cancelUpdate(){
	layer.close(EditMenuIndex);
}
//删除菜单
function deleteMenu(id,name){
	parent.layer.confirm('确认删除【'+name+'】？如果删除的[菜单/功能]包含[子菜单/功能]则相应的[子菜单/功能]也会被删除！', {
		icon : 3,
		title : '提示'
	}, function(index) {
		parent.layer.close(index);
		$.ajax({
			type : 'post',
			url : rootPath + '/menumanage/deleteMenu',
			beforeSend: function () {
				showLoad();
			},
			data : {
				menuid : id
			},
			success : function(data) {
				getMenuTreeInfo();
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
	});
}