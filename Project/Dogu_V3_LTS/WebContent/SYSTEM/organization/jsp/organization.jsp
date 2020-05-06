<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>组织机构管理</title>
<%@ include file="../../../SYSTEM/commom/common.jsp"%>
<link rel="stylesheet" href="../../../SYSTEM/lib/zTree/css/zTreeStyle/zTreeStyle.css?version=${version}" type="text/css"/>
<script type="text/javascript" src="../../../SYSTEM/lib/zTree/js/jquery.ztree.core.min.js?version=${version}"></script>
<script type="text/javascript" src="../../../SYSTEM/lib/zTree/js/jquery.ztree.excheck.min.js?version=${version}"></script>
<script type="text/javascript" src="../js/organization.js?version=${version}"></script>
</head>
<body>
	<table style="margin-left: 22px;">
		<tr>
			<td>
				&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" class="layui-btn" value="查询" onclick="initAll()"/>
			</td>
			<td>
				&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" class="layui-btn" value="新增" onclick="addOrg()"/>
			</td>
			<td>
				&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" class="layui-btn" value="删除" onclick="titleDelete()"/>
			</td>
			<td>
				&nbsp;&nbsp;&nbsp;&nbsp;<a style="color: red;font-size: 16px;">注：根节点的所属组织机构编号为"-1"，当前组织机构编号为：所属组织机构编号_组织机构编号</a>
			</td>
		</tr>
	</table>
	<div style="height:40px;width:312px;background-color:#009688;margin-left: 38px;margin-top:10px;
		border-radius:4px 4px 0px 0px;color:#FFF;font-size:16px;text-align:center;line-height:40px">
		组织机构树
	</div>
	<div id="ztree" class="ztree" style="margin-top:0px !important;width:300px;
		background-color: #FFF;margin-left: 38px; overflow-y: scroll; border:1px solid #009688;"></div>
	<div id="dataTableDiv" style="margin-left: 370px;">
		<table class="layui-hide" id="dataTable" lay-filter="dataTable"></table>
	</div>
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