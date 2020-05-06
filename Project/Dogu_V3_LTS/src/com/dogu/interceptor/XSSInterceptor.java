package com.dogu.interceptor;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Enumeration;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;

/**
 * XXS漏洞拦截器
 * @author Dogu
 */
public class XSSInterceptor implements Interceptor{
	@Override
	public void intercept(Invocation inv) {
		//防止XSS攻击开始
		Controller controller = inv.getController();
		Enumeration<String> AttrLs = controller.getParaNames();
		String name = "";
		String value = "";
		int status = 0;
		while(AttrLs.hasMoreElements()){
			name = (String)AttrLs.nextElement();//调用nextElement方法获得元素
			value = controller.getPara(name);
			try {
				value = URLDecoder.decode(value,"UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			if(value != null) {
				if(!value.equals(Jsoup.clean(value, Whitelist.none()))) {
					status ++;
				}
			}
		}
		if(status == 0) {
			inv.invoke();
		}else {
			controller.renderError(405);
		}
		//防止XSS攻击结束
	}
}
