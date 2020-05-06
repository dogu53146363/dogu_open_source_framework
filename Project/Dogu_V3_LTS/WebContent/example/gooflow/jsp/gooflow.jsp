<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Gooflow示例</title>
<!--[if lt IE 9]>
<?import namespace="v" implementation="#default#VML" ?>
<![endif]-->
<link rel="stylesheet" type="text/css" href="../css/GooFlow.css?version=${version}"/>
<style>
.myForm{display:block;margin:0px;padding:0px;line-height:1.5;border:#ccc 1px solid;font: 12px Arial, Helvetica, sans-serif;margin:5px 5px 0px 0px;border-radius:4px;}
.myForm .form_title{background:#428bca;padding:4px;color:#fff;border-radius:3px 3px 0px 0px;}
.myForm .form_content{padding:4px;background:#fff;}
.myForm .form_content table{border:0px}
.myForm .form_content table td{border:0px}
.myForm .form_content table .th{text-align:right;font-weight:bold}
.myForm .form_btn_div{text-align:center;border-top:#ccc 1px solid;background:#f5f5f5;padding:4px;border-radius:0px 0px 3px 3px;} 
#propertyForm{float:left;width:260px;margin-left:10px;}
</style>
<%@ include file="../../../SYSTEM/commom/common.jsp"%>
<script type="text/javascript" src="../js/GooFunc.js?version=${version}"></script>
<script type="text/javascript" src="../js/json2.js?version=${version}"></script>
<link rel="stylesheet" type="text/css" href="../css/default.css?version=${version}"/>
<script type="text/javascript" src="../js/GooFlow.js?version=${version}"></script>
<script type="text/javascript" src="../js/GooFlow.color.js?version=${version}"></script>
<script type="text/javascript" src="../js/property.js?version=${version}"></script>
<script type="text/javascript" src="../js/gooflow_operate.js?version=${version}"></script>
</head>
<body style="background:#FFF;">
<div id="demo" style="margin-left:25px;float:left"></div>
<form class="myForm" id="propertyForm">
<div class="form_title">属性设置</div>
<div class="form_content">
	<table>
		<tr><td class="th">Id：</td><td><input type="text" style="width:120px" id="ele_id" disabled="disabled"/></td></tr>
		<tr><td class="th">Name：</td><td><input type="text" style="width:120px" id="ele_name"/></td></tr>
		<tr><td class="th">Type：</td><td><input type="text" style="width:120px" id="ele_type" disabled="disabled"/></td></tr>
		<tr><td class="th">Model：</td><td><input type="text" style="width:120px" id="ele_model" disabled="disabled"/></td></tr>
		<tr><td class="th">Left-r：</td><td><input type="text" style="width:120px" id="ele_left" disabled="disabled"/></td></tr>
		<tr><td class="th">Top-r：</td><td><input type="text" style="width:120px" id="ele_top" disabled="disabled"></td></tr>
		<tr><td class="th">Width：</td><td><input type="text" style="width:120px" id="ele_width" disabled="disabled"/></td></tr>
		<tr><td class="th">Height：</td><td><input type="text" style="width:120px" id="ele_height" disabled="disabled"/></td></tr>
		<tr><td class="th">From：</td><td><input type="text" style="width:120px" id="ele_from" disabled="disabled"/></td></tr>
		<tr><td class="th">To：</td><td><input type="text" style="width:120px" id="ele_to" disabled="disabled"/></td></tr>
	</table>
</div>
<div class="form_btn_div">
	<input type="button" class="commonSureBtnCls" style="height: 25px;" onclick="changeValue()" value="修改"/>
	<input type="button" class="commonSureBtnCls" style="height: 25px;" value="保存" onclick="SaveFlowInfo()"/>
</div>
</form>
</body>
</html>
