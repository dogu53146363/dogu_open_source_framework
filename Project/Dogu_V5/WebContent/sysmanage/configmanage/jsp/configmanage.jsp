<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>系统配置表管理</title>
	<%@ include file="/common/jsp/common.jsp"%>
	<script type="text/javascript" src="${ctx}/sysmanage/configmanage/js/configmanage.js?version=${version}"></script>
</head>
<body>
	<table style="margin-left: 20px;margin-top: 10px;">
		<tr>
			<td>配置编号/内容/描述：</td>
			<td><input id="confDescription" class="layui-input" value="" /></td>
			<td>&nbsp;&nbsp;&nbsp;&nbsp;<button class="layui-btn icon-btn" onclick="getConfInfo()"><i class="layui-icon">&#xe615;</i>查询</button></td>
			<td>&nbsp;&nbsp;&nbsp;&nbsp;<button class="layui-btn icon-btn" onclick="addConf()"><i class="layui-icon">&#xe654;</i>新增</button></td>
		</tr>
	</table>
	<table class="layui-hide" id="dataTable" lay-filter="dataTable"></table>
	<!-- 页面加载loading -->
	<div class="page-loading">
	    <div class="ball-loader">
	        <span></span><span></span><span></span><span></span>
	    </div>
	</div>
	<script type="text/html" id="dataTableBar">
		<a class="layui-btn layui-btn-xs" lay-event="edit">编辑</a>
		<a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="delete">删除</a>
	</script>
</body>
</html>