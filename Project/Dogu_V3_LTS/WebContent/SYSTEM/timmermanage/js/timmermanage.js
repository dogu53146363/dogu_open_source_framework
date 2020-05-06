var AddTimmerIndex = 0;
var ChangeTimmerIndex = 0;
$(document).ready(function() {
	gettimmerInfo();
});
function gettimmerInfo(){
	var zt = document.getElementById("useid").value;
	layui.use(['layer', 'table', 'element'], function(){
		var table = layui.table //表格
		,element = layui.element //元素操作
		//执行一个 table 实例
		table.render({
			elem: '#dataTable'
			,height: 'full-70'
			,url: rootPath + '/SystemManage/getTimmerInfo' //数据接口
			,method: "post"
			,where: {
				zt : zt
			}
			,title: '用户表'
			,page: true //开启分页
			,limit: 10
			,toolbar: 'default' //开启工具栏，此处显示默认图标，可以自定义模板，详见文档
			,totalRow: false //开启合计行
			,cols: [[ //表头
				 {type: 'checkbox', fixed: 'left'}
				,{field: 'id', title: '序号', width: 60, fixed: 'left', align: 'center'}
				,{field: 'f_timmerid', title: '定时器编号', width: 200, sort: true, fixed: 'left'}
				,{field: 'f_status', title: '定时器状态', width: 150, sort: true, templet: '#statusMask', align: 'center'}
				,{field: 'f_clz', title: '执行的类', width: 300, sort: true}
				,{field: 'f_cron', title: '表达式', width: 300, sort: true}
				,{field: 'f_note', title: '定时器名称	', width: 300, sort: true}
				,{fixed: 'right', title: '操作', width: 265, align:'center', toolbar: '#dataTableBar'}
			]]
		});
		
		//监听行工具事件
		table.on('tool(dataTable)', function(obj){ //注：tool 是工具条事件名，test 是 table 原始容器的属性 lay-filter="对应的值"
			var data = obj.data;//获得当前行数据
			var layEvent = obj.event;//获得 lay-event 对应的值
			if(layEvent == 'edit'){
				changeTimmer(data.f_timmerid);
			} else if(layEvent == 'delete'){
				Delete(data.f_timmerid);
			} else if(layEvent == 'start'){
				Start(data.f_timmerid);
			} else if(layEvent == 'stop'){
				Stop(data.f_timmerid);
			}
		});
		
		//监听头工具栏事件
		table.on('toolbar(dataTable)', function(obj){
			var checkStatus = table.checkStatus(obj.config.id);
			var data = checkStatus.data; //获取选中的数据
			switch(obj.event){
				case 'add':
					addTimmer();
				break;
				case 'update':
				if(data.length == 0){
					layer.msg('请选择一行！');
				} else if(data.length > 1){
					layer.msg('只能编辑一行！');
				} else {
					changeTimmer(data[0].f_timmerid);
				}
				break;
				case 'delete':
				if(data.length == 0){
					layer.msg('至少选择一行！');
				} else {
					var timmerLs = "";
					for(var i=0;i<data.length;i++){
						timmerLs += data[i].f_timmerid+","
					}
					Delete(timmerLs);
				}
				break;
			};
		});
	});
}
//启用计时器
function Start(timmerid){
	$.ajax({
		type : 'post',
		url : rootPath + '/SystemManage/StartTimmer',
		beforeSend: function () {
			showLoad();
		},
		data : {
			timmerid : timmerid
		},
		success : function(data) {
			closeLoad();
			if(data.code == "0"){
				parent.layer.alert(data.message,{icon: 1});
				gettimmerInfo();
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
}
//停用计时器
function Stop(timmerid){
	$.ajax({
		type : 'post',
		url : rootPath + '/SystemManage/StopTimmer',
		beforeSend: function () {
			showLoad();
		},
		data : {
			timmerid : timmerid
		},
		success : function(data) {
			closeLoad();
			parent.layer.alert(data.message,{icon: 1});
			gettimmerInfo();
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
//删除定时任务
function Delete(timmerid){
	parent.layer.confirm('确定删除定时任务？删除之后定时任务将不能使用！', {
		icon : 3,
		title : '提示'
	}, function(index) {
		parent.layer.close(index);
		$.ajax({
			type : 'post',
			url : rootPath + '/SystemManage/DeleteTimmer',
			beforeSend: function () {
				showLoad();
			},
			data : {
				timmerid : timmerid
			},
			success : function(data) {
				closeLoad();
				parent.layer.alert(data.message,{icon: 1});
				gettimmerInfo();
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
//添加定时任务
function addTimmer(){
	var AddTimmerContent = 
		"<table class='layui-table'>"+
		"<tr>"+
		"<td style='text-align: right;'>任务编号：</td>"+
		"<td style='text-align: left;'><input id='timmerid' class='layui-input' placeholder='*请输入定时任务编号' type='text' style='width: 280px;'/></td>"+
		"</tr>"+
		"<tr>"+
		"<td style='text-align: right;'>调用的类：</td>"+
		"<td style='text-align: left;'><input id='clznameid' class='layui-input' placeholder='*请输入调用的类，如：com.dogu.timmer' type='text' style='width: 280px;'/></td>"+
		"</tr>"+
		"<tr>"+
		"<td style='text-align: right;'>计时公式：</td>"+
		"<td style='text-align: left;'><input id='cronid' class='layui-input' placeholder='*请输入计时公式' type='text' onclick='showModelessDialog()' style='width: 280px;'/></td>"+
		"</tr>"+
		"<tr>"+
		"<td style='text-align: right;'>备　　注：</td>"+
		"<td style='text-align: left;'><input id='timmernote' class='layui-input' placeholder='*请输入定时任务备注' type='text' style='width: 280px;'/></td>"+
		"</tr>"+
		"<tr>"+
		"<td style='text-align: right;'>是否启用：</td>"+
		"<td>"+
		"&nbsp;<input id='use' type='checkbox'/>"+
		"</td>"+
		"</tr>"+
		"<tr>"+
		"<td colspan='2' style='text-align:center;'>"+
		"<input id='sureBtn' type='button' value='确定' class='layui-btn'/>"+
		"<input id='cancelBtn' type='button' value='取消' class='layui-btn'/>"+
		"</td>"+
		"</tr>"+
		"</table>";
	AddTimmerIndex = layer.open({
		type: 1,
		area: ['430px','400px'],
		fix: true, //不固定
		maxmin: false,
		shade:0.4,
		title: '新增定时任务',
		content: AddTimmerContent
	});
	document.getElementById("sureBtn").onclick = function(){sureAddTimmer()};
	document.getElementById("cancelBtn").onclick = function(){cancelAddTimmer()};
}
//确定新增
function sureAddTimmer(){
	var timmerid = document.getElementById("timmerid").value;
	var timmerclz = document.getElementById("clznameid").value;
	var timmercron = document.getElementById("cronid").value;
	var timmernote = document.getElementById("timmernote").value;
	var usecheckbox = document.getElementById("use");
	var timmerzt = "N";
	if(usecheckbox.checked){
		timmerzt = "Y";
	}
	if($.trim(timmerid) == "" || timmerid == null){
		parent.layer.alert("请输入定时任务编号！",{icon: 7});
		return;
	}else if($.trim(timmerclz) == "" || timmerclz == null){
		parent.layer.alert("请输入定时任务执行的类！",{icon: 7});
		return;
	}else if($.trim(timmercron) == "" || timmercron == null){
		parent.layer.alert("请输入计时表达式！",{icon: 7});
		return;
	}else if($.trim(timmernote) == "" || timmernote == null){
		parent.layer.alert("请输入备注！",{icon: 7});
		return;
	}else if(timmerzt == "N"){
		parent.layer.confirm('注意,添加的定时任务将不会被启用！', {
			icon : 3,
			title : '提示'
		}, function(index) {
			parent.layer.close(index);
			timmerclz = encodeURI(timmerclz);
			timmercron = encodeURI(timmercron);
			timmernote = encodeURI(timmernote);
			$.ajax({
				type : 'post',
				url : rootPath + '/SystemManage/AddTimmer',
				beforeSend: function () {
					showLoad();
				},
				data : {
					timmerid : timmerid,
					timmerclz : timmerclz,
					timmercron : timmercron,
					timmernote : timmernote,
					timmerzt : timmerzt
				},
				success : function(data) {
					closeLoad();
					if (data.code == "0") {
						cancelAddTimmer();
						gettimmerInfo();
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
		timmernote = encodeURI(timmernote);
		$.ajax({
			type : 'post',
			url : rootPath + '/SystemManage/AddTimmer',
			beforeSend: function () {
				showLoad();
			},
			data : {
				timmerid : timmerid,
				timmerclz : timmerclz,
				timmercron : timmercron,
				timmernote : timmernote,
				timmerzt : timmerzt
			},
			success : function(data) {
				closeLoad();
				if (data.code == "0") {
					cancelAddTimmer();
					gettimmerInfo();
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
//隐藏添加定时任务
function cancelAddTimmer(){
	layer.close(AddTimmerIndex);
}
//修改之前先获取timmer信息
function changeTimmer(timmerid){
	var ChangeTimmerContent = 
		"<table class='layui-table'>"+
		"<tr>"+
		"<td style='text-align: right;'>任务编号：</td>"+
		"<td style='text-align: left;'><input id='timmerid' disabled='disabled' class='layui-input' placeholder='*请输入定时任务编号' type='text' style='width: 280px;'/></td>"+
		"</tr>"+
		"<tr>"+
		"<td style='text-align: right;'>调用的类：</td>"+
		"<td style='text-align: left;'><input id='clznameid' class='layui-input' placeholder='*请输入调用的类，如：com.dogu.timmer' type='text' style='width: 280px;'/></td>"+
		"</tr>"+
		"<tr>"+
		"<td style='text-align: right;'>计时公式：</td>"+
		"<td style='text-align: left;'><input id='cronid' class='layui-input' placeholder='*请输入计时公式' type='text' onclick='showModelessDialog()' style='width: 280px;'/></td>"+
		"</tr>"+
		"<tr>"+
		"<td style='text-align: right;'>备　　注：</td>"+
		"<td style='text-align: left;'><input id='timmernote' class='layui-input' placeholder='*请输入定时任务备注' type='text' style='width: 280px;'/></td>"+
		"</tr>"+
		"<tr>"+
		"<td style='text-align: right;'>是否启用：</td>"+
		"<td>"+
		"&nbsp;<input id='use' type='checkbox'/>"+
		"</td>"+
		"</tr>"+
		"<tr>"+
		"<td colspan='2' style='text-align:center;'>"+
		"<input id='sureBtn' type='button' value='确定' class='layui-btn'/>"+
		"<input id='cancelBtn' type='button' value='取消' class='layui-btn'/>"+
		"</td>"+
		"</tr>"+
		"</table>";
	ChangeTimmerIndex = layer.open({
		type: 1,
		area: ['430px','400px'],
		fix: true, //不固定
		maxmin: false,
		shade:0.4,
		title: '修改定时任务',
		content: ChangeTimmerContent
	});
	document.getElementById("sureBtn").onclick = function(){sureChangeTimmer(timmerid)};
	document.getElementById("cancelBtn").onclick = function(){cancelChangeTimmer()};
	$.ajax({
		type : 'post',
		url : rootPath + '/SystemManage/getOneTimmerInfo',
		beforeSend: function () {
			showLoad();
		},
		data : {
			timmerid : timmerid
		},
		success : function(data) {
			closeLoad();
			if(data.code == "0"){
				document.getElementById("timmerid").value = data.f_timmerid;
				document.getElementById("clznameid").value = data.f_clz;
				document.getElementById("cronid").value = data.f_cron;
				document.getElementById("timmernote").value = data.f_note;
				if(data.f_status == "Y"){
					document.getElementById("use").checked = true;
				}
			}else{
				cancelChangeTimmer();
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
//确定修改
function sureChangeTimmer(timmerid){
	var timmerclz = document.getElementById("clznameid").value;
	var timmercron = document.getElementById("cronid").value;
	var timmernote = document.getElementById("timmernote").value;
	var usecheckbox = document.getElementById("use");
	var timmerzt = "N";
	if(usecheckbox.checked){
		timmerzt = "Y";
	}
	if($.trim(timmerclz) == "" || timmerclz == null){
		parent.layer.alert("请输入定时任务执行的类！",{icon: 7});
		return;
	}else if($.trim(timmercron) == "" || timmercron == null){
		parent.layer.alert("请输入计时表达式！",{icon: 7});
		return;
	}else if($.trim(timmernote) == "" || timmernote == null){
		parent.layer.alert("请输入备注！",{icon: 7});
		return;
	}else if(timmerzt == "N"){
		parent.layer.confirm('注意,添加的定时任务将不会被启用！', {
			icon : 3,
			title : '提示'
		}, function(index) {
			parent.layer.close(index);
			timmerclz = encodeURI(timmerclz);
			timmercron = encodeURI(timmercron);
			timmernote = encodeURI(timmernote);
			$.ajax({
				type : 'post',
				url : rootPath + '/SystemManage/ModifyTimmer',
				beforeSend: function () {
					showLoad();
				},
				data : {
					timmerid : timmerid,
					timmerclz : timmerclz,
					timmercron : timmercron,
					timmernote : timmernote,
					timmerzt : timmerzt
				},
				success : function(data) {
					closeLoad();
					if (data.code == "0") {
						cancelChangeTimmer();
						gettimmerInfo();
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
		timmernote = encodeURI(timmernote);
		$.ajax({
			type : 'post',
			url : rootPath + '/SystemManage/ModifyTimmer',
			beforeSend: function () {
				showLoad();
			},
			data : {
				timmerid : timmerid,
				timmerclz : timmerclz,
				timmercron : timmercron,
				timmernote : timmernote,
				timmerzt : timmerzt
			},
			success : function(data) {
				closeLoad();
				if (data.code == "0") {
					cancelChangeTimmer();
					gettimmerInfo();
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
//隐藏修改定时任务
function cancelChangeTimmer(){
	layer.close(ChangeTimmerIndex);
}
//弹出定时任务选择页面
function showModelessDialog(){
	var url = rootPath+"/SYSTEM/lib/cron/cron.html";//转向网页的地址; 
    var name = 'Cron表达式';//网页名称,可为空;
    var iWidth = 850;//弹出窗口的宽度;
    var iHeight = 600;//弹出窗口的高度;
    //获得窗口的垂直位置 
    var iTop = (window.screen.availHeight - 30 - iHeight) / 2; 
    //获得窗口的水平位置 
    var iLeft = (window.screen.availWidth - 10 - iWidth) / 2; 
	window.open(url,name,"height="+iHeight+",width="+iWidth+",top="+iTop+",left="+iLeft+",toolbar=no,menubar=no,scrollbars=no,resizable=no,location=no,status=no,modal=yes");
}