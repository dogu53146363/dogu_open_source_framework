<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>菜单管理</title>
<%@ include file="../../../SYSTEM/commom/common.jsp"%>
<link rel="stylesheet" href="../../../SYSTEM/lib/zTree/css/zTreeStyle/zTreeStyle.css?version=${version}" type="text/css"/>
<script type="text/javascript" src="../js/menumanage.js?version=${version}"></script>
<script type="text/javascript" src="../../../SYSTEM/lib/zTree/js/jquery.ztree.core.min.js?version=${version}"></script>
<script type="text/javascript" src="../../../SYSTEM/lib/zTree/js/jquery.ztree.excheck.min.js?version=${version}"></script>
</head>
<body>
	<table style="margin-left: 40px;margin-top: 5px;">
		<tr>
			<td><input type="button" class="layui-btn" value="刷新" onclick="refresh()" /></td>
			<td>&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" class="layui-btn" value="新增菜单" onclick="showaddMenudiv()" /></td>
			<td>&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" class="layui-btn" value="删除菜单" onclick="deleteMenu()" /></td>
		</tr>
	</table>
	<div style="height:40px;width:312px;background-color:#009688;margin-left: 40px;margin-top:10px;
		border-radius:4px 4px 0px 0px;color:#FFF;font-size:16px;text-align:center;line-height:40px">
		菜单字典
	</div>
	<div id="ztree" class="ztree" style="margin-top:0px !important;width:300px;
		background-color: #FFF;margin-left: 40px; overflow-y: scroll; border:1px solid #009688;"></div>
	<div id="addmenudivid" style="margin-left: 385px;display: none;width: 600px;">
		<div style="height:40px;width:600px;background-color:#009688;margin-top:10px;
			border-radius:4px 4px 0px 0px;color:#FFF;font-size:16px;text-align:center;line-height:40px">
			菜单明细
		</div>
		<input id="oldmenu" style="display:none;" type="text" disabled="disabled"/>
		<div id="mxTableDIvId" style="overflow-y:auto;border:1px solid #009688;">
			<table class="layui-table" style="margin-top: 0px;margin-bottom: 0px;">
				<tr style="height:20px;">
					<td style="text-align: right;">菜单编号：</td>
					<td><input id="menu" class="layui-input" placeholder="*请输入菜单ID" onchange="this.value=this.value.replace(/\D/g,'')" type="text"
						style="width: 360px;" onkeyup="setMenuJSandIsNum(this.value)"/>
					</td>
				</tr>
				<tr>
					<td style="text-align: right;">菜单名称：</td>
					<td><input id="menuname" class="layui-input" placeholder="*请输入菜单名称"
						 type="text" style="width: 360px;" />
					</td>
				</tr>
				<tr>
					<td style="text-align: right;">菜单级别：</td>
					<td>
						<select id="menujb" class="layui-select" style="width: 360px;">
							<option value="" selected="selected">请选择</option>
							<option value="1">1级(母菜单)</option>
							<option value="2">2级(子菜单)</option>
						</select>
					</td>
				</tr>
				<tr>
					<td style="text-align: right;">页面路径：</td>
					<td><input id="pagepath" class="layui-input" placeholder="*请输入页面路径" type="text"
						style="width: 360px;" />
					</td>
				</tr>
				<tr>
					<td style="text-align: right;">菜单图标：</td>
					<td><input id="icon" class="layui-input" placeholder="*请输入图标代号" type="text"
						style="width: 360px;" />
					</td>
				</tr>
				<tr>
					<td style="text-align: right;">是否启用：</td>
					<td>
						<input id="use" type="checkbox"/>
					</td>
				</tr>
				<tr>
					<td style="text-align: right;">是否显示：</td>
					<td>
						<input id="show" type="checkbox"/>
					</td>
				</tr>
				<tr>
					<td style="text-align: right;">权限拦截：</td>
					<td>
						<input id="intercept" type="checkbox"/>
					</td>
				</tr>
				<tr>
					<td colspan="2" style="width: 100%;text-align: center;">
						<input id="surebtn" class="layui-btn" type="button" value="确定" />
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<input id="canclebtn" type="button" class="layui-btn" value="取消" onclick="hideaddmenutablediv()" />
					</td>
				</tr>
				<tr>
					<td colspan="2" style="font-size: 14px;color: #FF0000">*注：1、"菜单编号"：一级菜单是四位数字，二级菜单为七位数字</td>
				</tr>
				<tr>
					<td colspan="2" style="font-size: 14px;color: #FF0000">　　&nbsp;　　如：一级菜单编号:1001，1则二级为：1001001</td>
				</tr>
				<tr>
					<td colspan="2" style="font-size: 14px;color: #FF0000">　　&nbsp;2、"是否显示"：若不选中则左侧菜单内不显示该菜单</td>
				</tr>
				<tr>
					<td colspan="2" style="font-size: 14px;color: #FF0000">　　&nbsp;3、"权限拦截"：选中时，在没给当前已登录用户赋权或用户未登录的情况下，</td>
				</tr>
				<tr>
					<td colspan="2" style="font-size: 14px;color: #FF0000">　　&nbsp;　　直接通过浏览器的地址栏访问该页面，会返回404页面；未选中时，可在任</td>
				</tr>
				<tr>
					<td colspan="2" style="font-size: 14px;color: #FF0000">　　&nbsp;　　何情况下直接通过浏览器的地址栏访问该页面。</td>
				</tr>
			</table>
		</div>
	</div>
</body>
</html>