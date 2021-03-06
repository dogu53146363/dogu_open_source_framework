<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>角色管理</title>
<%@ include file="../../../SYSTEM/commom/common.jsp"%>
<script type="text/javascript" src="../js/rolemanage.js?version=${version}"></script>
</head>
<body>
	<table style="margin-left: 20px;">
		<tr>
			<td>是否启用：</td>
			<td>
				<select class="layui-select" id="useid" style="width: 150px;" onchange="getRoleInfo()">
					<option value="" selected="selected">全部</option>
					<option value="Y">启用</option>
					<option value="N">停用</option>
				</select>
			</td>
			<td>
				&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" class="layui-btn" value="查询" onclick="getRoleInfo()"/>
			</td>
			<td>
				&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" class="layui-btn" value="新增" onclick="addRole()"/>
			</td>
		</tr>
	</table>
	<table class="layui-hide" id="dataTable" lay-filter="dataTable"></table>
	<script type="text/html" id="dataTableBar">
		<a class="layui-btn layui-btn-xs" lay-event="edit">编辑</a>
		<a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="delete">删除</a>
	</script>
	<script type="text/html" id="statusMask">
		{{#  if(d.f_use == "Y"){ }}
			<a style='color:green;'>启用</a>
		{{#  } else if(d.f_use == "N") { }}
			<a style='color:red;'>停用</a>
		{{#  } }}
	</script>
</body>
</html>