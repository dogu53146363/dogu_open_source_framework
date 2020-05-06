<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>菜单管理</title>
	<%@ include file="../../../SYSTEM/common/jsp/common.jsp"%>
	<script type="text/javascript" src="${ctx}/SYSTEM/menumanage/js/menumanage.js?version=${version}"></script>
</head>
<body>
	<!-- 正文开始 -->
	<div class="layui-fluid">
	    <div class="layui-card">
	        <div class="layui-card-body">
	            <div class="layui-form toolbar">
	                <div class="layui-form-item">
	                    <div class="layui-inline">
	                        <label class="layui-form-label w-auto">搜索：</label>
	                        <div class="layui-input-inline mr0">
	                            <input id="edtSearchAuth" class="layui-input" type="text" placeholder="输入关键字"/>
	                        </div>
	                    </div>
	                    <div class="layui-inline">
	                        <button id="btnSearchAuth" class="layui-btn icon-btn"><i class="layui-icon">&#xe615;</i>搜索</button>
	                        <button id="addMenu" class="layui-btn icon-btn"><i class="layui-icon">&#xe654;</i>添加</button>
	                        <button id="btnExpandAuth" class="layui-btn icon-btn"><i class="layui-icon">&#xe668;</i>全部展开</button>
	                        <button id="btnFoldAuth" class="layui-btn icon-btn"><i class="layui-icon">&#xe66b;</i>全部折叠</button>
	                    </div>
	                </div>
	            </div>
	
	            <table class="layui-table" id="orgTableTree" lay-filter="orgTableTree"></table>
	        </div>
	    </div>
	</div>
	<!-- 页面加载loading -->
	<div class="page-loading">
	    <div class="ball-loader">
	        <span></span><span></span><span></span><span></span>
	    </div>
	</div>
	<!-- 表格按钮掩码-->
	<script type="text/html" id="tableBarMask">
    	<a class="layui-btn layui-btn-primary layui-btn-xs" lay-event="edit">修改</a>
    	<a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="del">删除</a>
	</script>
</body>
</html>