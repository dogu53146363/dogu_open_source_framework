<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Ueditor示例</title>
<%@ include file="../../../SYSTEM/commom/common.jsp"%>
<script type="text/javascript" src="../common/ueditor.config.js?version=${version}"></script>
<script type="text/javascript" src="../common/ueditor.all.min.js?version=${version}"></script>
<script type="text/javascript" src="../js/ueditor.js?version=${version}"></script>
</head>
<body>
<div><input onclick="saveInnerUeditor()" value="保存" type="button" class="commonSureBtnCls" style="height: 25px;margin-left: 15px;"/></div>
	<div style="margin-top: 5px;margin-left: 15px;">
		<script id="editor" type="text/plain" style="width:100%;height:500px;"></script>
	</div>
</body>
</html>