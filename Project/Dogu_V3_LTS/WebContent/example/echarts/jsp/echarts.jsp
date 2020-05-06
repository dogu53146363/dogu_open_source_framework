<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>echarts示例</title>
<%@ include file="../../../SYSTEM/commom/common.jsp"%>
<script type="text/javascript" src="../js/echartsrequire.js?version=${version}"></script>
<script type="text/javascript" src="../js/echarts.min.js?version=${version}"></script>
</head>
<body>
	<input id="area" value="济南" />
	<select id="object">
		<option value="F_PM2_5">pm2.5</option>
		<option value="F_O3">臭氧</option>
		<option value="F_SO2">二氧化硫</option>
		<option value="F_CO">一氧化碳</option>
		<option value="F_AQI">空气质量指数</option>
		<option value="F_PM10">pm10</option>
		<option value="F_O3_8H">臭氧8h平均</option>
	</select>
	<input type="button" class="commonSureBtnCls" style="height: 25px;" value="查询" onclick="selectinputareanum()"/>
	<div id="mycharts" style="width: 70%;height:400px;margin-left: 10px;margin-top: 30px;"></div>
</body>
</html>