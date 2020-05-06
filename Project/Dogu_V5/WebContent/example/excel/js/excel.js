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
	var upFileDom = document.getElementById("FileInput");//获取上传文件的input的Dom对象
	var allowSuffix = document.getElementById("FileInput").accept;//获取允许上传文件的类型
	var unlegal = 0;
	//循环遍历每个文件的后缀名
	for(var i=0;i<upFileDom.files.length;i++){
		var upFileName = upFileDom.files[i].name;
		var index1 = upFileName.lastIndexOf(".");
		var index2 = upFileName.length;
		var suffix = upFileName.substring(index1+1,index2).toLowerCase();//后缀名
		if(allowSuffix.indexOf(suffix) == -1 && allowSuffix != ""){
			unlegal++;
		}
	}
	if(unlegal == 0){
		showLoad();
		var form = $("#uploadFile");
		var options  = {
			url: rootPath + "/Example/ExcelUpload",
			type:'post',
			data:{
				
			},
			success : function(data, status) {
				getTableData();
				parent.layer.alert(data.message,{icon: 1});
			},
			error : function(data, status) {
				parent.layer.alert("导入失败!",{icon: 1});
			}
		}; 
		form.ajaxSubmit(options);
		closeLoad();
	}else{
		parent.layer.alert("上传失败，非法的文件类型！",{icon: 7});
	}
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