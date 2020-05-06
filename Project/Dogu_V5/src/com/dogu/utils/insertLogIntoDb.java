package com.dogu.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;

public class insertLogIntoDb {
	
	public static void doInsert(String loginUser, Controller controller, Invocation Ivc) {
		//请求类型
		String requestType = controller.getRequest().getMethod();
		//调用的controller类
		String clzName = Ivc.getTarget().getClass().getName();
		//获取调用的方法名
		String MethodName = Ivc.getMethod().getName();
		//获取时间
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = df.format(new Date()).toString();
		time = time.replaceAll(" ", "");
		time = time.replaceAll("-", "");
		time = time.replaceAll(":", "");
		//获取request请求
		HttpServletRequest request = controller.getRequest();
		//获取请求的地址
		String ip = request.getHeader("x-forwarded-for");
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
	        ip = request.getHeader("Proxy-Client-IP");
	    }
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
	        ip = request.getHeader("WL-Proxy-Client-IP");
	    }
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
	        ip = request.getRemoteAddr();
	    }
	    //获取mac地址
		//获取请求的所有参数
		Enumeration<String> e = request.getParameterNames();
		String Parameter = "";
		if (e.hasMoreElements()) {
			while (e.hasMoreElements()) {
				String name = e.nextElement();
				String[] values = request.getParameterValues(name);
				if(values != null) {
					if (values.length == 1) {
						Parameter += name+"=";
						Parameter += values[0];
					} else {
						Parameter += name+"[]={";
						for (int i=0; i<values.length; i++) {
							if (i > 0)
								Parameter += ",";
							Parameter += values[i];
						}
						Parameter += "}";
					}
				}
				Parameter += "  ";
			}
			Parameter += "\n";
		}
		if(loginUser != null && !"".equals(loginUser) && !"getSysLog".equals(MethodName) && !"deleteSysLog".equals(MethodName)) {
			Db.update("INSERT INTO SYS_LOG (F_ACCOUNT, F_IP, F_REQUEST_TYPE, F_CTRL, F_METHOD, F_PARAMS, F_TIME)"
					+ " VALUES (?,?,?,?,?,?,?)",loginUser,ip,requestType,clzName,MethodName,Parameter,time);
		}
	}
}
