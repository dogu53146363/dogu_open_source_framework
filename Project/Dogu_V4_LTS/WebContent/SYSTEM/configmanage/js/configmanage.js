var AddConfigIndex = 0;
var ChangeConfigIndex = 0;
$(document).ready(function() {
	getConfInfo();
});
//配置字典信息
function getConfInfo(){
	var confDescription = document.getElementById("confDescription").value;
	confDescription = encodeURI(confDescription);
	layui.use(['layer', 'table', 'element'], function(){
		var table = layui.table //表格
		,element = layui.element //元素操作
		//执行一个 table 实例
		table.render({
			elem: '#dataTable'
			,height: 'full-70'
			,url: rootPath + '/SystemManage/getConfInfo' //数据接口
			,method: "post"
			,where: {
				confDescription : confDescription
			}
			,title: '用户表'
			,page: true //开启分页
			,limit: 10
			,toolbar: 'default' //开启工具栏，此处显示默认图标，可以自定义模板，详见文档
			,totalRow: false //开启合计行
			,cols: [[ //表头
				 {type: 'checkbox', fixed: 'left'}
				,{field: 'id', title: '序号', width: 60, fixed: 'left', align: 'center'}
				,{field: 'f_key', title: '配置编号', width: 300, sort: true, fixed: 'left'}
				,{field: 'f_value', title: '配置内容', width: 400, sort: true}
				,{field: 'f_note', title: '配置描述', width: 300, sort: true}
				,{fixed: 'right', title: '操作', width: 165, align:'center', toolbar: '#dataTableBar'}
			]]
		});
		
		//监听行工具事件
		table.on('tool(dataTable)', function(obj){ //注：tool 是工具条事件名，test 是 table 原始容器的属性 lay-filter="对应的值"
			var data = obj.data;//获得当前行数据
			var layEvent = obj.event;//获得 lay-event 对应的值
			if(layEvent == 'edit'){
				changeConf(data.f_key);
			} else if(layEvent == 'delete'){
				delConf(data.f_key);
			}
		});
		
		//监听头工具栏事件
		table.on('toolbar(dataTable)', function(obj){
			var checkStatus = table.checkStatus(obj.config.id);
			var data = checkStatus.data; //获取选中的数据
			switch(obj.event){
				case 'add':
					addConf();
				break;
				case 'update':
				if(data.length == 0){
					layer.msg('请选择一行！');
				} else if(data.length > 1){
					layer.msg('只能编辑一行！');
				} else {
					changeConf(data[0].f_key);
				}
				break;
				case 'delete':
				if(data.length == 0){
					layer.msg('至少选择一行！');
				} else {
					var configLs = "";
					for(var i=0;i<data.length;i++){
						configLs += data[i].f_key+","
					}
					delConf(configLs);
				}
				break;
			};
		});
	});
	
}
//添加配置信息
function addConf(){
	var AddConfigContext = 
		"<table class='layui-table'>"+
		"<tr>"+
		"<td style='text-align: right;'>配置编号：</td>"+
		"<td><input id='confkey' class='layui-input' placeholder='*请输入配置编号' type='text'"+
		"style='width: 250px;'/></td>"+
		"</tr>"+
		"<tr>"+
		"<td style='text-align: right;'>配置内容：</td>"+
		"<td><input id='confvalue' class='layui-input' placeholder='*请输入配置内容' type='text'"+
		"style='width: 250px;' /></td>"+
		"</tr>"+
		"<tr>"+
		"<td style='text-align: right;'>配置描述：</td>"+
		"<td><input id='confnote' class='layui-input' placeholder='*请输入配置描述' type='text'"+
		"style='width: 250px;'/></td>"+
		"</tr>"+
		"<tr>"+
		"<td style='text-align: center;' colspan='2'>"+
		"<input id='sureBtn' type='button' class='layui-btn' value='确定' onclick='sureAddConf()'/>"+
		"<input id='cancelBtn' type='button' class='layui-btn' value='取消' onclick='cancelAddConf()'/></td>"+
		"</tr>"+
		"</table>";
	AddConfigIndex = layer.open({
		type: 1,
		area: ['400px','320px'],
		fix: true, //不固定
		maxmin: false,
		shade:0.4,
		title: '添加配置',
		content: AddConfigContext
	});
}
//点击确定按钮方法
function sureAddConf(){
	var confkey = document.getElementById("confkey").value;
	var confvalue = document.getElementById("confvalue").value;
	var confnote = document.getElementById("confnote").value;
	if($.trim(confkey) == "" || confkey == null){
		parent.layer.alert("请输入配置编号！",{icon: 7});
		return;
	}else if($.trim(confvalue) == "" || confvalue == null){
		parent.layer.alert("请输入配置内容！",{icon: 7});
		return;
	}else if(confnote == ""){
		parent.layer.alert("请输入配置描述！",{icon: 7});
		return;
	}else {
		confvalue = encodeURI(confvalue);
		confnote = encodeURI(confnote);
		$.ajax({
			type : 'post',
			url : rootPath + '/SystemManage/addConf',
			beforeSend: function () {
		        showLoad();
		    },
			data : {
				confkey : confkey,
				confvalue : confvalue,
				confnote : confnote
			},
			success : function(data) {
				closeLoad();
				if (data.code == "0") {
					cancelAddConf()
					getConfInfo();
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
//隐藏添加配置框
function cancelAddConf(){
	layer.close(AddConfigIndex);
}
//修改配置信息之前的获取一条配置信息的信息
function changeConf(confkey){
	var ChangeConfigContext = 
		"<table class='layui-table'>"+
		"<tr>"+
		"<td style='text-align: right;'>配置编号：</td>"+
		"<td><input id='confkey' class='layui-input' disabled='disabled' placeholder='*请输入配置编号' type='text'"+
		"style='width: 250px;'/></td>"+
		"</tr>"+
		"<tr>"+
		"<td style='text-align: right;'>配置内容：</td>"+
		"<td><input id='confvalue' class='layui-input' placeholder='*请输入配置内容' type='text'"+
		"style='width: 250px;' /></td>"+
		"</tr>"+
		"<tr>"+
		"<td style='text-align: right;'>配置描述：</td>"+
		"<td><input id='confnote' class='layui-input' placeholder='*请输入配置描述' type='text'"+
		"style='width: 250px;'/></td>"+
		"</tr>"+
		"<tr>"+
		"<td style='text-align: center;' colspan='2'>"+
		"<input id='sureBtn' type='button' class='layui-btn' value='确定' onclick='sureChangeConf()'/>"+
		"<input id='cancelBtn' type='button' class='layui-btn' value='取消' onclick='cancelChangeConf()'/></td>"+
		"</tr>"+
		"</table>";
	ChangeConfigIndex = layer.open({
		type: 1,
		area: ['400px','320px'],
		fix: true, //不固定
		maxmin: false,
		shade:0.4,
		title: '修改配置',
		content: ChangeConfigContext
	});

	$.ajax({
		type : 'post',
		url : rootPath + '/SystemManage/getOneConfInfo',
		beforeSend: function () {
			showLoad();
	    },
		data : {
			confkey : confkey
		},
		success : function(data) {
			closeLoad();
			if(data.code == "0"){
				document.getElementById("confkey").value = data.key;
				document.getElementById("confvalue").value = data.value;
				document.getElementById("confnote").value = data.note;
			}else{
				cancelChangeConf();
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
//修改配置信息
function sureChangeConf(){
	var confkey = document.getElementById("confkey").value;
	var confvalue = document.getElementById("confvalue").value;
	var confnote = document.getElementById("confnote").value;
	if($.trim(confkey) == "" || confkey == null){
		parent.layer.alert("请输入配置编号！",{icon: 7});
		return;
	}else if($.trim(confvalue) == "" || confvalue == null){
		parent.layer.alert("请输入配置内容！",{icon: 7});
		return;
	}else if(confnote == ""){
		parent.layer.alert("请输入配置描述！",{icon: 7});
		return;
	}else {
		confvalue = encodeURI(confvalue);
		confnote = encodeURI(confnote);
		$.ajax({
			type : 'post',
			url : rootPath + '/SystemManage/updateConf',
			beforeSend: function () {
		        showLoad();
		    },
			data : {
				confkey : confkey,
				confvalue : confvalue,
				confnote : confnote
			},
			success : function(data) {
				closeLoad();
				cancelChangeConf();
				getConfInfo();
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
//隐藏修改配置框
function cancelChangeConf(){
	layer.close(ChangeConfigIndex);
}
//删除配置信息
function delConf(confkey){
	parent.layer.confirm('确定删除配置信息？', {
		icon : 3,
		title : '提示'
	}, function(index) {
		parent.layer.close(index);
		$.ajax({
			type : 'post',
			url : rootPath + '/SystemManage/deleteConf',
			beforeSend: function () {
		        showLoad();
		    },
			data : {
				confkey : confkey
			},
			success : function(data) {
				closeLoad();
				getConfInfo();
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