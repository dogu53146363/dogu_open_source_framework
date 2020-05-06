<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Excel示例</title>
<%@ include file="/common/jsp/common.jsp"%>
<script type="text/javascript" src="${ctx}/example/excel/js/excel.js?version=${version}"></script>
<script type="text/javascript" src="${ctx}/common/lib/jquery/jqueryform/jquery.form.min.js?version=${version}"></script>
<style type="text/css">
.tabledivcls{
	width: 98%;
	height: 300px;
	overflow-x:hidden;
	overflow-y:scroll;
	margin-left: 20px;
}

.tablecls{
	border-collapse: collapse;
	width: 100%;
}

.tablecls td{
	border: 1px solid #aac7cc;
	line-height:18px;
	color:#323232;
	font-size:12px;
	padding:2px;
	padding-left:4px;
}
</style>
</head>
<body>
	<div style="margin-left: 20px;">
		<input type="button" onclick="ExportExcel()" value="导出" class="commonSureBtnCls" style="height: 25px;"/>&nbsp;&nbsp;
		<input type="button" onclick="$('#FileInput').click()" value="导入" class="commonSureBtnCls" style="height: 25px;"/>&nbsp;&nbsp;
		<input type="button" onclick="downLoadTemplet()" value="导入模版下载" class="commonSureBtnCls" style="height: 25px;"/>&nbsp;&nbsp;
		<input type="button" onclick="deleteData()" value="删除数据" class="commonCancleBtnCls" style="height: 25px;"/>
	</div>
	<form id="uploadFile" enctype="multipart/form-data" style="display: none;">
        <input id="FileInput" name="FileInput" type="file" placeholder="请选择文件" 
		accept="xls,xlsx" onchange="uploadfile()"/>
    </form>
	<br>
	<div id="tablediv" class="tabledivcls"></div>
</body>
</html>