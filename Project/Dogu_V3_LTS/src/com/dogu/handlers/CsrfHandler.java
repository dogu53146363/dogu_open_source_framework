package com.dogu.handlers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jfinal.handler.Handler;
import com.jfinal.kit.HandlerKit;

public class CsrfHandler extends Handler {

	@Override
	public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled) {
		String referer = request.getHeader("Referer");
		if(null == referer) {
			next.handle(target, request, response, isHandled);
		} else {
			if(referer.trim().indexOf("localhost")>-1 || 
					referer.trim().indexOf("127.0.0.1")>-1) {//这个地方根据实际的地址进行更改或者是添加来判断是不是进行了跨域
				next.handle(target, request, response, isHandled);
			}else {
				HandlerKit.renderError404(request, response, isHandled);
			}
		}
	}
}
