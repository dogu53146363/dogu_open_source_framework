<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String scheme = request.getScheme();
	String serverName = request.getServerName();
	int port = request.getServerPort();
	String contextPath = request.getContextPath();
	String rootPath = "";
	if(port == 80){
		rootPath = scheme+"://"+serverName+contextPath;
	}else{
		rootPath = scheme+"://"+serverName+":"+port+contextPath;
	}
	
	Object userAccount = session.getAttribute("F_ACCOUNT");//用户账号
	Object userName = session.getAttribute("F_USERNAME");//用户姓名
	Object roleID = session.getAttribute("F_ROLE");//角色ID
	Object orgID = session.getAttribute("F_ORG");//组织机构ID
	String UserAccount = "";
	String UserName = "";
	String RoleID = "";
	String Org = "";
	if(null != userAccount){
		UserAccount = userAccount.toString();
	}
	if(null != userName){
		UserName = userName.toString();
	}
	if(null != roleID){
		RoleID = roleID.toString();
	}
	if(null != orgID){
		Org = orgID.toString();
	}
%>
<!-- jsp使用的版本号 -->
<c:set var="version" value="<%=System.currentTimeMillis()%>"/>
<!-- jsp使用的项目绝对路径 -->
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!-- js使用的绝对路径 -->
<script type="text/javascript">var rootPath = "<%= rootPath%>";</script>
<!-- layui css -->
<link rel="stylesheet" type="text/css" href="${ctx}/common/lib/layui/css/layui.css?version=${version}" />
<!-- 通用css -->
<link rel="stylesheet" type="text/css" href="${ctx}/common/css/common.css?version=${version}"/>
<!-- js引用的版本号 -->
<script type="text/javascript">var version=${version};</script>
<!-- jquery -->
<script type="text/javascript" src="${ctx}/common/lib/jquery/3.4.1/jquery-3.4.1.min.js?version=${version}"></script>
<!-- layui -->
<script type="text/javascript" src="${ctx}/common/lib/layui/layui.js?version=${version}"></script>
<!-- 通用js -->
<script type="text/javascript" src="${ctx}/common/js/common.js?version=${version}"></script>
