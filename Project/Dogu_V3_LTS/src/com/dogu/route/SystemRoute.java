package com.dogu.route;

import com.dogu.action.LoginAction;
import com.dogu.action.SystemAction;
import com.jfinal.config.Routes;

/**
 * 系统路由配置
 * @author Dogu
 *
 */
public class SystemRoute {
	
    public SystemRoute(Routes me) {
    	me.add("/", LoginAction.class);// 全局登录路由
    	me.add("/SystemManage", SystemAction.class);// 系统管理路由
	}
}