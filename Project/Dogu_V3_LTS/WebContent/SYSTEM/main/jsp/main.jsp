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
<link rel="shortcut icon" href="SYSTEM/index/ico/favicon.ico" /> 
<%@ include file="/SYSTEM/commom/common.jsp"%>
<link rel="stylesheet" href="${ctx}/SYSTEM/main/css/main.css?version=${version}" media="all">
<script type="text/javascript" src="${ctx}/SYSTEM/main/js/main.js?version=${version}"></script>
<script type="text/javascript" src="${ctx}/SYSTEM/lib/md5/md5.js?version=${version}"></script>
</head>

<body>
	<div id="wholediv" class="layui-layout layui-layout-admin">
		<!-- 头部导航 start -->
		<div class="layui-header header header-demo">
			<div class="layui-main">
				<div class="admin-login-box">
					<img style="height:50px;width:50px;z-index:99;margin-top: 5px;" src="${ctx}/SYSTEM/main/img/logo.png">
					<a class="logo" style="right: 0px;width:150px;" href=""> <span
						style="font-size: 22px;font-weight:bold;">Dogu开源框架</span>
					</a>
					<div class="admin-side-toggle">
						<i class="fa fa-bars" aria-hidden="true"></i>
					</div>
					<div class="admin-side-full">
						<i class="fa fa-life-bouy" aria-hidden="true"></i>
					</div>
				</div>
				<ul class="layui-nav layui-layout-right">
					<!-- li class="layui-nav-item" id="donate"><a href="javascript:;">捐赠我</a></li!-->
					<li class="layui-nav-item">
						<a href="javascript:;">
							<span id="UserName" class="userNameSpan"></span>
						</a>
						<dl class="layui-nav-child">
							<dd id="userInfo">
								<a href="javascript:;"><i class="fa fa-user-circle"
									aria-hidden="true"></i> 个人信息</a>
							</dd>
							<dd id="setPas">
								<a href="javascript:;"><i class="fa fa-gear"
									aria-hidden="true"></i> 修改密码</a>
							</dd>
							<dd id="lock">
								<a href="javascript:;"> <i class="fa fa-lock"
									aria-hidden="true"></i> 锁屏
								</a>
							</dd>
							<dd id="clearCached">
								<a href="javascript:;"><i class="fa fa-user-circle"
									aria-hidden="true"></i> 清除缓存</a>
							</dd>
							<dd id="logOut">
								<a href="javascript:;"><i class="fa fa-sign-out"
									aria-hidden="true"></i> 退出</a>
							</dd>
						</dl>
					</li>
				</ul>
				<ul class="layui-nav admin-header-item-mobile">
					<li class="layui-nav-item"><a href="login.html"><i
							class="fa fa-sign-out" aria-hidden="true"></i> 注销</a></li>
				</ul>
			</div>
		</div>
		<!-- 头部导航 start -->
		<!-- 菜单 start -->
		<div class="layui-side layui-bg-black" id="admin-side">
			<div class="layui-side-scroll" id="admin-navbar-side" lay-filter="side"></div>
		</div>
		<!-- 菜单 end -->
		<!-- body start -->
		<div class="layui-body"
			style="bottom: 0; border-left: solid 2px #1AA094;" id="admin-body">
			<div class="layui-tab admin-nav-card layui-tab-brief" lay-filter="admin-tab">
				<ul class="layui-tab-title"></ul>
				<div class="layui-tab-content" style="min-height: 150px; padding: 5px 0 0 0;"></div>
			</div>
		</div>
		<!-- body end -->
		<!-- footer start -->
		<div class="layui-footer footer footer-demo" id="admin-footer">
			<div class="layui-main">
				<p>
					<a href="javascript:;">Dogu开源框架</a>2018 &copy;
				</p>
			</div>
		</div>
		<!-- footer end -->
		<!-- 手机 start -->
		<div class="site-tree-mobile layui-hide">
			<i class="layui-icon">&#xe602;</i>
		</div>
		<div class="site-mobile-shade"></div>
		<!-- 手机 end -->
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
		<!--锁屏模板 end -->
	</div>
</body>
</html>