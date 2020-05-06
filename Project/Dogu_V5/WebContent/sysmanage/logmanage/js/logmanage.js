var wholeStartTime = getTime(7);
var wholeEndTIme = getTime(0);
var deleteLogIndex = 0;
$(document).ready(function() {
	initDatePicker();
});
//初始化日期选择器
function initDatePicker(){
	layui.use('laydate', function(){
		var laydate = layui.laydate;
		laydate.render({
			elem: '#startTime' //指定元素
			,value: wholeStartTime
			,type: 'datetime'
			,btns: ['now', 'confirm']//'clear', 'now', 'confirm'
		});
		laydate.render({
			elem: '#endTime' //指定元素
			,value: wholeEndTIme
			,type: 'datetime'
			,btns: ['now', 'confirm']//'clear', 'now', 'confirm'
		});
	});
	initTable();
}
//初始化日志表
function initTable(){
	var account = $("#accountid").val();
	var startTime = $("#startTime").val();
	if(startTime == null || startTime == ""){
		startTime = wholeStartTime;
	}
	var endTime = $("#endTime").val();
	if(endTime == null || endTime == ""){
		endTime = wholeEndTIme;
	}
	layui.use(['layer', 'table', 'element'], function(){
		var table = layui.table //表格
		,element = layui.element //元素操作
		//执行一个 table 实例
		table.render({
			elem: '#dataTable'
			,height: 'full-70'
			,url: rootPath + '/logmanage/getSysLog' //数据接口
			,method: "post"
			,where: {
				account : account,
				startTime : startTime,
				endTime : endTime
			}
			,title: '用户表'
			,page: true //开启分页
			,limit: 10
			,toolbar: 'false' //开启工具栏，此处显示默认图标，可以自定义模板，详见文档
			,totalRow: false //开启合计行
			,cols: [[ //表头
				{type: 'numbers', title: '序号', width: 60, fixed: 'left', align: 'center'}
				,{field: 'F_ACCOUNT', title: '账号', width: 150, sort: true, fixed: 'left'}
				,{field: 'F_IP', title: 'IP地址', width: 150, sort: true}
				,{field: 'F_REQUEST_TYPE', title: '请求类型', width: 120, sort: true}
				,{field: 'F_CTRL', title: '调用的类', width: 260, sort: true} 
				,{field: 'F_METHOD', title: '调用的方法', width: 200, sort: true}
				,{field: 'F_PARAMS', title: '参数', width: 280, sort: true, sort: true}
				,{field: 'F_TIME', title: '时间', width: 190, sort: true, templet: '#opreateTime'}
			]]
		});
		
		//监听行工具事件
		table.on('tool(dataTable)', function(obj){ //注：tool 是工具条事件名，test 是 table 原始容器的属性 lay-filter="对应的值"
			
		});
		
		//监听头工具栏事件
		table.on('toolbar(dataTable)', function(obj){
			
		});
	});
}
//清空日志
function deleteLog(){
	var deleteLogContext = 
		"<table class='layui-table'>"+
		"<tr>"+
		"<td style='text-align: center;font-size:20px;' colspan='2'>清除日志</td>"+
		"</tr>"+
		"<tr>"+
		"<td style='text-align: right;'>用户名：</td>"+
		"<td><input id='del_acount' class='layui-input' placeholder='*请输入用户名' type='text' style='width: 250px;'/></td>"+
		"</tr>"+
		"<tr>"+
		"<td style='text-align: right;'>起始时间：</td>"+
		"<td><input id='del_startTime' class='layui-input' placeholder='*请选择开始时间' type='text' style='width: 250px;'/></td>"+
		"</tr>"+
		"<tr>"+
		"<td style='text-align: right;'>结束时间：</td>"+
		"<td><input id='del_endTime' class='layui-input' placeholder='*请选择结束时间' type='text' style='width: 250px;'/></td>"+
		"</tr>"+
		"<tr>"+
		"<td style='text-align: center;' colspan='2'>"+
		"<input id='sureBtn' type='button' class='layui-btn' value='确定' onclick='sureDelete()'/>"+
		"<input id='cancelBtn' type='button' value='取消' class='layui-btn' onclick='cancelDelete()'/>"+
		"</td>"+
		"</tr>"+
		"</table>";
	deleteLogIndex = layer.open({
		type: 1,
		area: ['400px','350px'],
		fix: true, //不固定
		maxmin: false,
		shade:0.4,
		title: '清除日志',
		content: deleteLogContext
	});
	layui.use('laydate', function(){
		var laydate = layui.laydate;
		laydate.render({
			elem: '#del_startTime' //指定元素
			,type: 'datetime'
			,btns: ['clear', 'now', 'confirm']//'clear', 'now', 'confirm'
		});
		laydate.render({
			elem: '#del_endTime' //指定元素
			,type: 'datetime'
			,btns: ['clear', 'now', 'confirm']//'clear', 'now', 'confirm'
		});
	});
}
//确认删除日志
function sureDelete(){
	var account = $("#del_acount").val();
	var startTime = $("#del_startTime").val();
	if(startTime == null || startTime == ""){
		parent.layer.alert("请选择起始时间！",{icon: 7});
		return;
	}
	var endTime = $("#del_endTime").val();
	if(endTime == null || endTime == ""){
		parent.layer.alert("请选择结束间！",{icon: 7});
		return;
	}
	var confirmMsg = "确定要清空所有从"+startTime+"到"+endTime+"的操作日志吗？清空之后无法找回！";
	if(account != null && account != ""){
		confirmMsg = "确定要清空用户【"+account+"】从"+startTime+"到"+endTime+"的操作日志吗？清空之后无法找回！";
	}
	parent.layer.confirm(confirmMsg, {
		icon : 3,
		title : '提示'
	}, function(index) {
		parent.layer.close(index);
		$.ajax({
			url : rootPath + '/logmanage/deleteSysLog',
			beforeSend: function () {
				showLoad();
			},
			type : 'post',
			data : {
				account : account,
				startTime : startTime,
				endTime : endTime
			},
			success : function(data) {
				$("#endTime").val(getTime(0));
				cancelDelete();
				initTable();
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
//取消删除日志
function cancelDelete(){
	layer.close(deleteLogIndex);
}
//格式化时间
function formdate(datetime){
    var year = datetime.substr(0,4);
    var month = datetime.substr(4,2);
    var date = datetime.substr(6,2);
    var hour = datetime.substr(8,2);
    var minutes = datetime.substr(10,2);
    var second = datetime.substr(12,2);
    return year+"-"+month+"-"+date+" "+hour+":"+minutes+":"+second;
}
//获取当前时间    
function getTime(reduceDay){ 
    var now = new Date();
    now.setDate(now.getDate()-reduceDay);//获取AddDayCount天后的日期 
    var year = now.getFullYear();//年
    var month = now.getMonth() + 1;//月
    var day = now.getDate();//日
    var hh = now.getHours();//时
    var mm = now.getMinutes();//分
    var ss = now.getSeconds();//秒
    var clock = year + "-";
    if(month < 10){
        clock += "0";
    }
    clock += month + "-";
    if(day < 10){
    	clock += "0";
    }
    clock += day + " ";
    if(hh < 10){
    	clock += "0";
    }
    clock += hh + ":";
    if (mm < 10) clock += '0';
    clock += mm + ":";
    if (ss < 10) clock += '0';
    clock += ss;
    return(clock);
}