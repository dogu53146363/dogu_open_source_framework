<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>系统日志</title>
	<%@ include file="../../../SYSTEM/common/jsp/common.jsp"%>
	<script type="text/javascript" src="../js/systemlog.js?version=${version}"></script>
</head>
<body>
	<table style="margin-left: 20px;margin-top: 10px;">
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
				&nbsp;&nbsp;&nbsp;&nbsp;<button class="layui-btn icon-btn" onclick="initTable()"><i class="layui-icon">&#xe615;</i>查询</button>
			</td>
			<td>
				&nbsp;&nbsp;&nbsp;&nbsp;<button class="layui-btn icon-btn" onclick="deleteLog()"><i class="layui-icon">&#xe639;</i>清除日志</button>
			</td>
		</tr>
	</table>
	<!-- 页面加载loading -->
	<div class="page-loading">
	    <div class="ball-loader">
	        <span></span><span></span><span></span><span></span>
	    </div>
	</div>
	<table class="layui-hide" id="dataTable" lay-filter="dataTable"></table>
    <script id="opreateTime" type="text/html">
		{{ formdate(d.f_time)}}
    </script>
</body>
</html>