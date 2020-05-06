package com.dogu.config;

import com.dogu.handlers.CsrfHandler;
import com.dogu.handlers.SysHandler;
import com.jfinal.config.Handlers;

import example.handlers.WebSocketHandler;

/**
 * Handlers配置
 * @author Dogu
 */
public class HandlerConfig {
	public HandlerConfig(Handlers me) {
		me.add(new CsrfHandler());// CSRF攻击Handler
		me.add(new SysHandler());// 权限Handler
		me.add(new WebSocketHandler("^/websocket", true));//websockt Handler
	}
}
