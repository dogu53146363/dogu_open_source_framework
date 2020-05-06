<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>打开新的tab页</title>
<%@ include file="/common/jsp/common.jsp"%>
<script type="text/javascript">
</script>
</head>
<body>
	<div>
        <button id="add" class="layui-btn">向选项卡添加一个tab</button>
	</div>
	<br>
	<div>
        <button id="remove" class="layui-btn" onclick="closeCurrentTab()">移除当前选项卡</button>
    </div>
    <br>
	<div>
        <button id="directOpen" class="layui-btn" onclick="openNewTab('测试标题','http://www.baidu.com')">直接打开</button>
    </div>
</body>
</html>