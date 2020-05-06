<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>用户管理</title>
	<%@ include file="/common/jsp/common.jsp"%>
	<script type="text/javascript" src="${ctx}/sysmanage/usermanage/js/usermanage.js?version=${version}"></script>
	<script type="text/javascript" src="${ctx}/common/lib/md5/md5.js?version=${version}"></script>
</head>
<body>
	<table style="margin-left: 20px;margin-top: 10px;">
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
				&nbsp;&nbsp;&nbsp;&nbsp;<button class="layui-btn icon-btn" onclick="getUserInfo()"><i class="layui-icon">&#xe615;</i>查询</button>
			</td>
			<td>
				&nbsp;&nbsp;&nbsp;&nbsp;<button class="layui-btn icon-btn" onclick="addtUser()"><i class="layui-icon">&#xe654;</i>新增</button>
			</td>
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
		<a class="layui-btn layui-btn-xs" lay-event="resetPsw">重置密码</a>
		<a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="delete">删除</a>
	</script>
	<script type="text/html" id="sexMask">
		{{#  if(d.F_SEX == "F"){ }}
			男
		{{#  } else if(d.F_SEX == "M") { }}
			女
		{{#  } }}
	</script>
</body>
</html>