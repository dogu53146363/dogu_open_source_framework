<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>系统日志</title>
<%@ include file="../../../SYSTEM/commom/common.jsp"%>
<script type="text/javascript" src="../js/systemlog.js?version=${version}"></script>
</head>
<body>
	<table style="margin-left: 20px;">
		<tr>
			<td>&nbsp;&nbsp;&nbsp;&nbsp;帐号：</td>
			<td>
				<input id="accountid" class="layui-input" type="text" value = "" style="width: 150px;"/>
			</td>
			<td>&nbsp;&nbsp;&nbsp;&nbsp;从：</td>
			<td>
				<input id="startTime" class="layui-input" type="text" value = "" style="width: 180px;"/>
			</td>
			<td>&nbsp;&nbsp;&nbsp;&nbsp;至&nbsp;&nbsp;&nbsp;&nbsp;</td>
			<td>
				<input id="endTime" class="layui-input" type="text" value = "" style="width: 180px;"/>
			</td>
			<td>
				&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" class="layui-btn" value="查询" onclick="initTable()"/>
			</td>
			<td>
				&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" class="layui-btn" value="清空日志" onclick="deleteLog()"/>
			</td>
		</tr>
	</table>
	<table class="layui-hide" id="dataTable" lay-filter="dataTable"></table>
    <script id="opreateTime" type="text/html">
		{{ formdate(d.f_time)}}
    </script>
</body>
</html>