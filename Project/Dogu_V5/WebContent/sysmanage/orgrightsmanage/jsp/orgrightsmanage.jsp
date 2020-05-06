<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>组织机构管理</title>
	<%@ include file="/common/jsp/common.jsp"%>
	<link rel="stylesheet" href="${ctx}/common/lib/zTree/css/zTreeStyle/zTreeStyle.css?version=${version}" type="text/css"/>
	<script type="text/javascript" src="${ctx}/common/lib/zTree/js/jquery.ztree.core.min.js?version=${version}"></script>
	<script type="text/javascript" src="${ctx}/common/lib/zTree/js/jquery.ztree.excheck.min.js?version=${version}"></script>
	<script type="text/javascript" src="${ctx}/sysmanage/orgrightsmanage/js/orgrightsmanage.js?version=${version}"></script>
</head>
<body>
	<table style="margin-left: 30px;margin-top: 10px;">
		<tr>
			<td>&nbsp;&nbsp;&nbsp;&nbsp;<button class="layui-btn icon-btn" onclick="initAll()"><i class="layui-icon">&#xe666;</i>刷新</button></td>
			<td>&nbsp;&nbsp;&nbsp;&nbsp;<button class="layui-btn icon-btn" onclick="SaveOrgLimit()"><i class="layui-icon">&#xe63c;</i>保存</button></td>
		</tr>
	</table>
	<div style="height:40px;width:312px;background-color:#009688;margin-left: 38px;margin-top:10px;
		border-radius:4px 4px 0px 0px;color:#FFF;font-size:16px;text-align:center;line-height:40px">
		组织机构树
	</div>
	<div id="orgZtree" class="ztree" style="margin-top:0px !important;width:300px;
		background-color: #FFF;margin-left: 38px; overflow-y: scroll; border:1px solid #009688;">
	</div>
	
	<div id="menuDivId" style="margin-left: 380px;width: 312px;">
		<div style="height:40px;width:312px;background-color:#009688;margin-top:10px;
			border-radius:4px 4px 0px 0px;color:#FFF;font-size:16px;text-align:center;line-height:40px">
			菜单字典
		</div>
		<div id="menuZtree" class="ztree" style="width: 300px;background-color: #FFF; overflow-y: scroll;border:1px solid #009688;"></div>
	</div>
	<!-- 页面加载loading -->
	<div class="page-loading">
	    <div class="ball-loader">
	        <span></span><span></span><span></span><span></span>
	    </div>
	</div>
</body>
</html>