<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>系统配置表管理</title>
<%@ include file="../../../SYSTEM/commom/common.jsp"%>
<script type="text/javascript" src="../js/configmanage.js?version=${version}"></script>
</head>
<body>
	<table style="margin-left: 20px;">
		<tr>
			<td>配置编号/内容/描述：</td>
			<td><input id="confDescription" class="layui-input" value="" /></td>
			<td>&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" class="layui-btn" value="查询" onclick="getConfInfo()" /></td>
			<td>&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" class="layui-btn" value="新增" onclick="addConf()" /></td>
		</tr>
	</table>
	<table class="layui-hide" id="dataTable" lay-filter="dataTable"></table>
	<script type="text/html" id="dataTableBar">
		<a class="layui-btn layui-btn-xs" lay-event="edit">编辑</a>
		<a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="delete">删除</a>
	</script>
</body>
</html>