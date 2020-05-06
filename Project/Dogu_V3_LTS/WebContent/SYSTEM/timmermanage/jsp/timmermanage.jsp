<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>定时任务管理</title>
<%@ include file="../../../SYSTEM/commom/common.jsp"%>
<script type="text/javascript" src="../js/timmermanage.js?version=${version}"></script>
</head>
<body>
	<table style="margin-left: 15px;">
		<tr>
			<td>是否启用：</td>
			<td>
				<select id="useid" class="layui-select" style="width: 150px;" onchange="gettimmerInfo()">
					<option value="" selected="selected">全部</option>
					<option value="Y">启用</option>
					<option value="N">停用</option>
				</select>
			</td>
			<td>
				&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" value="查询" class="layui-btn" onclick="gettimmerInfo()"/>
			</td>
			<td>
				&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" value="新增" class="layui-btn" onclick="addTimmer()"/>
			</td>
		</tr>
	</table>
	<table class="layui-hide" id="dataTable" lay-filter="dataTable"></table>
	<script type="text/html" id="dataTableBar">
		<a class="layui-btn layui-btn-xs" lay-event="start">启用</a>
		<a class="layui-btn layui-btn-xs" lay-event="stop">停用</a>
		<a class="layui-btn layui-btn-xs" lay-event="edit">编辑</a>
		<a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="delete">删除</a>
	</script>
	<script type="text/html" id="statusMask">
		{{#  if(d.f_status == "Y"){ }}
			<a style='color:green;'>启用</a>
		{{#  } else if(d.f_status == "N") { }}
			<a style='color:red;'>停用</a>
		{{#  } }}
	</script>
</body>
</html>