<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Dogu开源框架</title>
	<meta name="renderer" content="webkit">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
	<meta name="apple-mobile-web-app-status-bar-style" content="black">
	<meta name="apple-mobile-web-app-capable" content="yes">
	<meta name="format-detection" content="telephone=no">
	<%@ include file="/common/jsp/common.jsp"%>
	<link rel="shortcut icon" href="${ctx}/sysmanage/index/ico/favicon.ico" /> 
	<style type="text/css">
		.layui-nav-tree .layui-nav-item a{
			height: 35px;
			line-height: 35px;
		}
	</style>
	<!-- 图标库css -->
	<link rel="stylesheet" type="text/css" href="${ctx}/common/lib/font-awesome/css/font-awesome.min.css?version=${version}"/>
	<script type="text/javascript" src="${ctx}/sysmanage/main/js/main.js?version=${version}"></script>
	<script type="text/javascript" src="${ctx}/common/lib/md5/md5.js?version=${version}"></script>
</head>
	<body class="layui-layout-body">
		<div class="layui-layout layui-layout-admin">
		    <!-- 头部 -->
		    <div class="layui-header">
		        <div class="layui-logo">
		            <img src="${ctx}/sysmanage/main/img/logo.png"/>
		            &nbsp;&nbsp;
		            <cite>Dogu开源框架</cite>
		        </div>
		        <ul class="layui-nav layui-layout-left">
		            <li class="layui-nav-item" lay-unselect>
		                <a ew-event="flexible" title="侧边伸缩"><i class="layui-icon layui-icon-shrink-right"></i></a>
		            </li>
		            <li class="layui-nav-item" lay-unselect>
		                <a ew-event="refresh" title="刷新"><i class="layui-icon layui-icon-refresh-3"></i></a>
		            </li>
		        </ul>
		        <ul class="layui-nav layui-layout-right">
		            <li class="layui-nav-item" lay-unselect>
		                <a ew-event="message" title="消息">
		                    <i class="layui-icon layui-icon-notice"></i>
		                    <span class="layui-badge-dot"></span><!--小红点-->
		                </a>
		            </li>
		            <li class="layui-nav-item" lay-unselect>
		                <a ew-event="note" title="便签"><i class="layui-icon layui-icon-note"></i></a>
		            </li>
		            <li class="layui-nav-item layui-hide-xs" lay-unselect>
		                <a ew-event="fullScreen" title="全屏"><i class="layui-icon layui-icon-screen-full"></i></a>
		            </li>
		            <li class="layui-nav-item" lay-unselect>
		                <a>
		                    <img src="${ctx}/sysmanage/main/img/user.png" class="layui-nav-img">
		                    <cite><%=UserName%></cite>
		                </a>
		                <dl class="layui-nav-child">
		                    <dd lay-unselect>
		                        <!-- a ew-href="page/template/user-info.html">个人中心</a -->
		                        <a ew-event="info">个人中心</a>
		                    </dd>
		                    <dd lay-unselect>
		                        <a ew-event="psw">修改密码</a>
		                    </dd>
		                    <dd lay-unselect>
		                        <a ew-event="lock">锁屏</a>
		                    </dd>
		                    <dd lay-unselect>
		                        <a ew-event="logout">退出</a>
		                    </dd>
		                </dl>
		            </li>
		            <li class="layui-nav-item" lay-unselect>
		                <a ew-event="theme" title="主题"><i class="layui-icon layui-icon-more-vertical"></i></a>
		            </li>
		        </ul>
		    </div>
		    <!-- 侧边栏 -->
		    <div class="layui-side">
		        <div class="layui-side-scroll">
		            <ul class="layui-nav layui-nav-tree" lay-filter="admin-side-nav" style="margin: 15px 0;"></ul>
		        </div>
		    </div>
		    <!-- 主体部分 -->
		    <div class="layui-body"></div>
		    <!-- 底部 -->
		    <div class="layui-footer">
		        Copyright © 2019 dogu.site All Rights Reserved.&nbsp;&nbsp;Version 4.0.2
		    </div>
		</div>
		<!-- 加载动画 -->
		<div class="page-loading">
		    <div class="ball-loader">
		        <span></span><span></span><span></span><span></span>
		    </div>
		</div>
		<!-- 侧边栏渲染模板 -->
		<script id="sideNav" type="text/html">
			{{#  layui.each(d, function(index, item){ }}
			<li class="layui-nav-item">
				<a lay-href="{{item.url}}"><i class="{{item.icon}}"></i>&emsp;<cite>{{item.name}}</cite></a>
				{{# if(item.subMenus&&item.subMenus.length>0){ }}
				<dl class="layui-nav-child">
					{{# layui.each(item.subMenus, function(index, subItem){ }}
					<dd>
						<a lay-href="{{ subItem.url }}"><i class="{{subItem.icon}}"></i>&emsp;<cite>{{ subItem.name }}</a>
						{{# if(subItem.subMenus&&subItem.subMenus.length>0){ }}
							<dl class="layui-nav-child">
							{{# layui.each(subItem.subMenus, function(index, thrItem){ }}
								<dd><a lay-href="{{ thrItem.url }}"><i class="{{thrItem.icon}}"></i>&emsp;<cite>{{ thrItem.name }}</a></dd>
							{{# }); }}
							</dl>
						{{# } }}
					</dd>
					{{# }); }}
				</dl>
				{{# } }}
			</li>
			{{#  }); }}
		</script>
		<!--锁屏模板 start-->
		<script type="text/template" id="lock-input">
			<div class="admin-header-lock" id="lock-num-box">
				<div>请设置解锁码：<input id="lockNum" type="password" class="admin-header-lock-input" value="" /></div>
				<br>
				<div>
					<button class="layui-btn layui-btn-small" id="putLockNum">确定</button>
					<button class="layui-btn layui-btn-small" id="cancleLock">取消</button>
				</div>
			</div>
		</script>
		<script type="text/template" id="lock-temp">
			<div class="admin-header-lock" id="lock-box">
				<div>解锁码：<input id="lockPwd" type="password" class="admin-header-lock-input" value="" /></div>
				<br>
				<div><button class="layui-btn layui-btn-small" id="unlock">解锁</button></div>
			</div>
		</script>
	</body>
</html>