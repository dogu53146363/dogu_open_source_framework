<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>文件上传示例</title>
<%@ include file="../../../SYSTEM/commom/common.jsp"%>
<script type="text/javascript" src="../js/fileupload.js?version=${version}"></script>
<script type="text/javascript" src="../js/ajaxfileupload.js?version=${version}"></script>
<style type="text/css">
.piccls {
	cursor:pointer;
}
.fdpiczzccls{
	position: absolute;
	width: 100%;
	height: 100%;
	left: 0px;
	top: 0px;
	background-color: #AAA;
}
</style>
</head>
<body>
	<div id="fdpic" class="fdpiczzccls" style="display: none;">
	   <div style="text-align: center;margin-top: 50px;">
		    <div id="picfdck" class="picfdls"></div>
		    <input type="button" value="退出预览模式" onclick="hidefangdapic()" />
		    <div style="font-size: 20px;">注意：按ESC键可退出预览模式</div>
	    </div>
    </div>
    <table>
		<tr id="picture">
			<td align="right">测试上传图片：</td>
			<td align="left">
				<input type="file" name="image" id="TESTFile" onchange="uploadfile('TEST')">
				<div id="TESTshowpic"></div>
			</td>
		</tr>
	</table>
</body>
</html>