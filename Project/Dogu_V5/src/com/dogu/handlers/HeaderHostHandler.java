package com.dogu.handlers;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.dogu.constants.Constant;
import com.dogu.utils.Properties2List;
import com.jfinal.handler.Handler;
import com.jfinal.kit.HandlerKit;

/**
 * Csr漏洞拦截
 * @author Dogu
 */
public class HeaderHostHandler extends Handler {

	@Override
	public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled) {
		String host = request.getHeader("Host");
		if(null == host) {
			next.handle(target, request, response, isHandled);
		} else {
			try {
				//获取配置文件List
				List<String> ConfigFileList = 
						Properties2List.getList(Constant.HTTPRQ_HEADER_CONFIG_FILE_PATH);
				if(ConfigFileList.size() > 0) {
					int Permission = 0;
					Properties properties = new Properties();
					InputStream in = null;
					// 读取配置文件的配置属性
					String IP =  null;
					for (int i = 0; i < ConfigFileList.size(); i++) {
						in = new BufferedInputStream(new FileInputStream(ConfigFileList.get(i)));
						properties.load(in);
						// 读取配置文件的配置属性
						IP = properties.getProperty("IP");//IP
						if(null != IP) {
							if(host.trim().indexOf(IP)>-1) {
								Permission ++;
							}
						}
						in.close();
						properties.clear();
					}
					if(Permission >0) {
						next.handle(target, request, response, isHandled);
					}else {
						HandlerKit.renderError404(request, response, isHandled);
					}
				}else {
					HandlerKit.renderError404(request, response, isHandled);
					throw new Exception("Unable To Load Http Request Header Property File!");
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
