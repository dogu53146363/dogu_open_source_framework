package com.dogu.config;

import com.dogu.interceptor.LogInterceptor;
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
	}
}
