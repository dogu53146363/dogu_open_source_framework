$(document).ready(function() {
	getTableData();
});
//获取表格数据
function getTableData(){
	$.ajax({
		type : 'post',
		url : rootPath + '/Example/getTableData',
		data : {
			
		},
		success : function(data) {
			DrawTable(data);
		},
		error : function(data, error) {
			parent.layer.alert(error,{icon: 7});
		}
	});
}
//绘制表格
function DrawTable(data){
	document.getElementById("tablediv").innerHTML = "";
	var innertable = "<table class='tablecls'><tr><td>姓名</td><td>年龄</td><td>性别</td><td>爱好</td></tr>";
	for(var i=0;i<data.length;i++){
		innertable += "<tr><td>"+data[i].F_NAME+"</td><td>"+data[i].F_AGE+"</td><td>"+data[i].F_SEX+"</td><td>"+data[i].F_HOBBY+"</td></tr>"
	}
	innertable += "</table>";
	document.getElementById("tablediv").innerHTML = innertable;
}
//上传excel
function uploadfile() {
	$.ajaxFileUpload({
			url : rootPath + '/Example/ExcelUpload', //提交的路径
			secureuri : false, // 是否启用安全提交，默认为false
			fileElementId : 'ExcelFile', // file控件id
			dataType : 'json',
			data : {
				
			},
			success : function(data, status) {
				getTableData();
				parent.layer.alert(data.message,{icon: 1});
			},
			error : function(data, status) {
				parent.layer.alert("导入失败!",{icon: 1});
			}
		});
}
//导出Excel
function ExportExcel(){
	window.location.href = rootPath + "/Example/ExportExcel";
}
//删除数据
function deleteData(){
	$.ajax({
		type : 'post',
		url : rootPath + '/Example/deleteExcelData',
		data : {
			
		},
		success : function(data) {
			getTableData();
			parent.layer.alert(data.message,{icon: 1});
		},
		error : function(data, error) {
			parent.layer.alert(error,{icon: 7});
		}
	});
}
//下载模板
function downLoadTemplet(){
	window.location.href = rootPath + "/Example/downLoadExcel";
}