<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户行权限</title>
<%@ include file="../../../SYSTEM/commom/common.jsp"%>
<link rel="stylesheet" type="text/css" href="../../../SYSTEM/lib/zTree/css/zTreeStyle/zTreeStyle.css?version=${version}"/>
<script type="text/javascript" src="../../../SYSTEM/lib/zTree/js/jquery.ztree.core.min.js??version=${version}"></script>
<script type="text/javascript" src="../../../SYSTEM/lib/zTree/js/jquery.ztree.excheck.min.js?version=${version}"></script>
<script type="text/javascript" src="../js/userlimitmanage.js?version=${version}"></script>
</head>
<body>
	<table style="margin-left: 40px;margin-top: 5px;">
		<tr>
			<td>帐号：</td>
			<td>
				<input id="accountid" class="layui-input" type="text" value="" style="width: 160px;" />
			</td>
			<td>&nbsp;&nbsp;&nbsp;&nbsp;用户姓名：</td>
			<td>
				<input id="usernameid" class="layui-input" type="text" value="" style="width: 160px;" />
			</td>
			<td>&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" value="查询" class="layui-btn" onclick="getUserInfo()" /></td>
			<td>&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" value="保存" class="layui-btn" onclick="SaveUserLimit()" /></td>
		</tr>
	</table>
	<div style="height:40px;width:302px;background-color:#009688;margin-left: 40px;margin-top:10px;
		border-radius:4px 4px 0px 0px;color:#FFF;font-size:16px;text-align:center;line-height:40px">
		角色字典
	</div>
	<div id="userTableId" style="width:300px;margin-left: 40px;overflow-y:scroll;border:1px solid #009688;"></div>
	<div id="menuDivId" style="margin-left: 380px;width: 312px;">
		<div style="height:40px;width:312px;background-color:#009688;margin-top:10px;
			border-radius:4px 4px 0px 0px;color:#FFF;font-size:16px;text-align:center;line-height:40px">
			菜单字典
		</div>
		<div id="ztree" class="ztree" style="width: 300px;background-color: #FFF; overflow-y: scroll;border:1px solid #009688;"></div>
	</div>
</body>
</html>