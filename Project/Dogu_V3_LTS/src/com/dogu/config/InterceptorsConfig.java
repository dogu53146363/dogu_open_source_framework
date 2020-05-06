package com.dogu.config;

import com.dogu.interceptor.LogInterceptor;
import com.dogu.interceptor.LoginInterceptor;
import com.dogu.interceptor.MultipartRequestInterceptor;
import com.dogu.interceptor.XSSInterceptor;
import com.jfinal.config.Interceptors;

/**
 * Interceptor配置
 * @author Dogu
 */
public class InterceptorsConfig {

	public InterceptorsConfig(Interceptors me) {
		me.add(new LogInterceptor());// 日志拦截器
		me.add(new XSSInterceptor());// XSS拦截器
		me.add(new LoginInterceptor());// 登录拦截器
		me.add(new MultipartRequestInterceptor());// 解决POST提交,无法上传不带Multipart-data问题的拦截器
	}
}
