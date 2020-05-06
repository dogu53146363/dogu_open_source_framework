package com.dogu.config;

import com.dogu.afterStart.AfterStart;
import com.dogu.beforeStop.BeforeStop;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.template.Engine;

/**
 * jfinal配置
 * @author Dogu
 */
public class SystemConfig extends JFinalConfig {
	
	// 配置常量
	@Override
	public void configConstant(Constants me) {
		new ConstantConfig(me);
	}
	
	// 配置访问路由
	@Override
	public void configRoute(Routes me) {
		new RouteConfig(me);// 路由配置单独抽取出来
	}
	
	// 配置引擎模板
	@Override
	public void configEngine(Engine me) {
		new EngineConfig(me);
	}
	
	// 数据库配置
	@Override
	public void configPlugin(Plugins me) {
		new DataBaseConfig(me);
	}
	
	// 配置全局拦截器
	@Override
	public void configInterceptor(Interceptors me) {
		new InterceptorsConfig(me);
	}
	
	// 配置Handler
	@Override
	public void configHandler(Handlers me) {
		new HandlerConfig(me);
	}
	
	// 应用启动完成之后调用
	@Override
	public void onStart() {
		new AfterStart();
	}
	
	// 应用停止前调用
	@Override
	public void onStop() {
		new BeforeStop();
	}
}
