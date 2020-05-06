<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>我的桌面</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="Dogu开源框架">
<meta http-equiv="description" content="Dogu开源框架">
<%@ include file="../../commom/common.jsp"%>
<script type="text/javascript" src="../js/myDesktop.js?version=${version}"></script>
</head>
<body style="height:100%;width: 100%;">
	<div>
		<h1>&nbsp;&nbsp;Dogu开源框架<span>v3.5</span></h1>
	</div>
	<div style="float: left;margin-left: 20px;">
		作者:Dogu<br>
		作者QQ:53146363<br>
		作者邮箱:lzq_jn@qq.com<br>
		-------------------------------------<br>
		框架Git地址：<br>
		https://gitee.com/dogu/<br>
		dogu_open_source_framework.git<br>
		---------------版本1.0---------------<br>
		1、发布上线<br>
		时间:2016年11月5号23点56分<br>
		--------------版本1.1----------------<br>
		1、修复若干BUG<br>
		时间:2016年11月8号21点36分<br>
		--------------版本1.2----------------<br>
		1、添加定时器功能，将程序员<br>
		的主要工作转移到业务代码上<br>
		可以一键配置定时器<br>
		时间:2016年11月19号11点11分<br>
		--------------版本1.3----------------<br>
		1、添加接口请求数据并存入库<br>
		内并生成json文件的示例<br>
		2、添加Echarts示例<br>
		4、添加BLOB和CLOB(Oracle)<br>
		插入示例<br>
		4、修复系统功能:SQL注入问题<br>
		--------------版本1.4----------------<br>
		1、添加Ueditor示例<br>
		时间:2016年11月23号0点12分<br>
		--------------版本1.5----------------<br>
		1、提高框架菜单稳定性<br>
		2、提高示例稳定性<br>
		时间:2016年11月23号10点23分<br>
	</div>
	<div style="float: left;margin-left: 20px;">
		--------------版本1.6----------------<br>
		1、增加JQGride示例<br>
		2、增加线程示例(详情见程序<br>
		里面的:com.dogu.main)<br>
		时间:2016年12月06号21点46分<br>
		--------------版本1.7----------------<br>
		1、增加Excel导入、导出示例<br>
		时间:2016年12月08号13点53分<br>
		--------------版本1.8----------------<br>
		1、增加对Mysql数据库的支持！<br>
		时间:2016年12月12号23点01分<br>
		--------------版本1.9----------------<br>
		1、解决用户登录之后可以直接<br>
		在浏览器的地址栏中输入Url访<br>
		问没有赋权的页面的BUG<br>
		时间:2016年12月17号20点23分<br>
		--------------版本2.0----------------<br>
		1、优化左侧菜单视觉体验<br>
		2、增加验证码示例<br>
		3、增加js和css文件打版本功能<br>
		(防止出现浏览器缓存问题)<br>
		时间:2017年02月21号11点25分<br>
		--------------版本3.0----------------<br>
		1、将jfinal内核升级至3.0<br>
		2、增加在线视频示例<br>
		时间:2017年04月06号09点39分<br>
		--------------版本3.1----------------<br>
		1、"菜单管理"增加"是否显示"<br>
		功能来实现不在左方菜单导航<br>
		栏内显示但给用户/角色加上<br>
		该菜单权限之后又不被拦截器<br>
		拦截返回404页面的功能<br>
		2、增加通过自有设备发短信示例<br>
		为以后的物联网兼容做准备<br>
		时间:2017年10月14号14点46分<br>
	</div>
	<div style="float: left;margin-left: 20px;">
		--------------版本3.2----------------<br>
		1、性能优化(拦截器、handle)<br>
		2、增加Gooflow流程图示例<br>
		时间:2017年10月17号09点16分<br>
		--------------版本3.3----------------<br>
		1、更换前端框架(H-UI-3.1.3)<br>
		2、整合框架功能，使框架工程<br>
		目录更清晰<br>
		3、提升框架稳定性<br>
		4、修改了一些系统管理的UI<br>
		5、修正示例中ueditor上方<br>
		插件404的错误<br>
		7、支持jdk1.6-jdk1.8<br>
		8、优化对elcipse和myeclipse<br>
		的兼容<br>
		9、登录界面增加用户注册、<br>
		找回密码功能<br>
		10、登录界面优化<br>
		11、增加非管理员人员可修改<br>
		个人密码/个人信息的功能<br>
		时间:2017年10月17号09点16分<br>
		--------------版本3.4----------------<br>
		1、更换jfinal包(3.3)<br>
		2、增强定时器功能，工程不重<br>
		启的情况下，添加/删除/修改<br>
		定时任务不再是梦！详细请见：<br>
		com.dogu.timmer.readme.docx<br>
		3、统一编码方式，将工程的所<br>
		有文件编码方式改为：UTF-8<br>
		时间:2017年12月27号09点11分<br>
		--------------版本3.4.1--------------<br>
		1、更换jfinal包(3.4)<br>
		2、增加pdfJS示例<br>
		时间:2018年07月27号21点07分<br>
	</div>
	<div style="float: left;margin-left: 20px;color:red;">
		--------------版本3.5--------------<br>
		1、更换jfinal包(3.5)<br>
		2、由于UI视感比较差，更换UI框架，<br>
		变更后的框架为：layui-v2.4.5<br>
		时间:2018年11月23号23点56分<br>
		3、"我的桌面"功能改为了动态<br>
		配置，在'系统管理'-->'系统配置<br>
		管理'选项中添加配置编号为:<br>
		DESKTOP_PATH的项，其值为：<br>
		SYSTEM/myDesktop/jsp/myDesktop.jsp<br>
		*注：<br>
		(1)、此版本改动较大，注意手动<br>
		填充和修改SYS_MENU_DCT<br>
		表里的F_ICON字段，保证该<br>
		字段不是null或空字符串，<br>
		否则会影响菜单正常<br>
		使用 <br>
		时间:2018年11月23号23点56分<br>
		-------------------------------------<br>
	</div>
</body>
</html>
