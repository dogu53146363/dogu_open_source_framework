package com.dogu.interceptor;

import java.util.Map;

import com.dogu.constants.Constant;
import com.dogu.utils.GetConFromDB;
import com.dogu.utils.insertLogIntoDb;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;

/**
 * 日志拦截器
 * @author Dogu
 */
public class LogInterceptor implements Interceptor {

	@SuppressWarnings("unchecked")
	@Override
	public void intercept(Invocation inv) {
		//往数据库里面插日志
		Controller controller = inv.getController();
		Map<String, String> sessionStorage = (Map<String, String>) controller.getSession()
				.getAttribute(Constant.SESSION_STORAGE);
		String loginUser = "";
		if(null != sessionStorage) {
			loginUser = sessionStorage.get("F_ACCOUNT");
		}
		String openLogIntoDb = GetConFromDB.GetCIFromDB("OPEN_LOG_INTO_DB");
		if(openLogIntoDb != null && "true".equals(openLogIntoDb)) {
			if(null == loginUser) {
				loginUser = controller.getPara("account", "");//如果是登录则需要从request中获取account参数
			}
			insertLogIntoDb.doInsert(loginUser, controller, inv);
		}
		inv.invoke();
	}
}
