package com.dogu.config;

import com.dogu.route.SystemRoute;
import com.jfinal.config.Routes;

import example.route.ExampleRoute;

/**
 * 路由配置
 * @author Dogu
 */
public class RouteConfig {
	
    public RouteConfig(Routes me) {
		new SystemRoute(me);// 系统路由
		new ExampleRoute(me);// 示例路由
	}
}
