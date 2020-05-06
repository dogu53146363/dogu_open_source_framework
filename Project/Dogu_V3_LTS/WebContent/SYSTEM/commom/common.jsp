<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="version" value="<%=System.currentTimeMillis()%>"/>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!-- 通用css开始 -->
<link rel="stylesheet" type="text/css" href="${ctx}/SYSTEM/lib/layui/css/layui.css?version=${version}" />
<link rel="stylesheet" href="${ctx}/SYSTEM/lib/font-awesome/css/font-awesome.min.css?version=${version}">
<!-- 通用css结束 -->
<!-- 通用JS引用开始 -->
<script type="text/javascript" src="${ctx}/SYSTEM/lib/jquery/2.1.4/jquery-2.1.4.min.js?version=${version}"></script>
<script type="text/javascript" src="${ctx}/SYSTEM/lib/layui/layui.js?version=${version}"></script>
<script type="text/javascript" src="${ctx}/SYSTEM/commom/common.js?version=${version}"></script>
<!-- 通用JS引用结束 -->