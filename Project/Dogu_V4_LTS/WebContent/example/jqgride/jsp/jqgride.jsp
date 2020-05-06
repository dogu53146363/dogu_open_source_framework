<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Jqgride示例</title>
<%@ include file="../../../SYSTEM/common/jsp/common.jsp"%>
<script type="text/javascript" src="../js/jqgride.js?version=${version}"></script>
<script type="text/javascript" src="../js/jquery.jqGrid.src.js?version=${version}"></script>
<script type="text/javascript" src="../js/i18n/grid.locale-cn.js?version=${version}"></script>
<link rel="stylesheet" href="../css/jquery-ui-1.8.12.custom/jquery-ui-1.8.12.custom.css?version=${version}"/>
<link rel="stylesheet" href="../css/ui.jqgrid.css?version=${version}"/>
</head>
<body>
	<table id="list"></table> 
	<div id="pager"></div>
</body>
</html>