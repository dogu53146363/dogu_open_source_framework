<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户管理</title>
<%@ include file="../../../SYSTEM/commom/common.jsp"%>
<script type="text/javascript" src="../js/usermanage.js?version=${version}"></script>
<script type="text/javascript" src="../../../SYSTEM/lib/md5/md5.js?version=${version}"></script>
</head>
<body>
	<table style="margin-left: 20px;">
		<tr>
			<td>用户角色：</td>
			<td>
				<select id="roleid" class="layui-select" style="width: 150px;" onchange="getUserInfo()"></select>
			</td>
			<td>&nbsp;&nbsp;&nbsp;&nbsp;帐号：</td>
			<td>
				<input id="accountid" class="layui-input" type="text" value = "" style="width: 150px;"/>
			</td>
			<td>&nbsp;&nbsp;&nbsp;&nbsp;用户姓名：</td>
			<td>
				<input id="usernameid" class="layui-input" type="text" value = "" style="width: 150px;"/>
			</td>
			<td>&nbsp;&nbsp;&nbsp;&nbsp;组织机构：</td>
			<td>
				<input id="orgid" class="layui-input" type="text" value = "" style="width: 150px;"/>
			</td>
			<td>
				&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" class="layui-btn" value="查询" onclick="getUserInfo()"/>
			</td>
			<td>
				&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" class="layui-btn" value="新增" onclick="addtUser()"/>
			</td>
		</tr>
	</table>
	<table class="layui-hide" id="dataTable" lay-filter="dataTable"></table>
	<script type="text/html" id="dataTableBar">
		<a class="layui-btn layui-btn-xs" lay-event="edit">编辑</a>
		<a class="layui-btn layui-btn-xs" lay-event="resetPsw">重置密码</a>
		<a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="delete">删除</a>
	</script>
	<script type="text/html" id="sexMask">
		{{#  if(d.f_sex == "F"){ }}
			男
		{{#  } else if(d.f_sex == "M") { }}
			女
		{{#  } }}
	</script>
</body>
</html>