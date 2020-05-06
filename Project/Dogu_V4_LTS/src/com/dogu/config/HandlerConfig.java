package com.dogu.config;

import com.dogu.handlers.CsrfHandler;
import com.dogu.handlers.HeaderHostHandler;
import com.dogu.handlers.SessionHandler;
import com.jfinal.config.Handlers;
import com.jfinal.plugin.druid.DruidStatViewHandler;

import example.handlers.WebSocketHandler;

/**
 * Handlers配置
 * @author Dogu
 */
public class HandlerConfig {
	public HandlerConfig(Handlers me) {
		me.add(new HeaderHostHandler());// 请求头Host攻击Handler
		me.add(new CsrfHandler());// CSRF攻击Handler
		me.add(new DruidStatViewHandler("/druid"));//Druid Handler
		me.add(new SessionHandler());// 权限Handler
		me.add(new WebSocketHandler("^/websocket", true));//websockt Handler
	}
}
