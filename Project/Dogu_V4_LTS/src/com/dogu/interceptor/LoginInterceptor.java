package com.dogu.interceptor;

import com.dogu.constants.Constant;
import com.dogu.utils.GetConFromDB;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

/**
 * 登录拦截器
 */
public class LoginInterceptor implements Interceptor {

	public void intercept(Invocation Ivc) {
		Controller controller = Ivc.getController();
		String loginUser = controller.getSessionAttr("F_ACCOUNT");
		if (null != loginUser && !Constant.EMPTYSTR.equals(loginUser)) {
			String openOnePointIDStatus = GetConFromDB.GetCIFromDB("OPEN_ONE_POINT");
			if("true".equals(openOnePointIDStatus)) {
				String sessionOnePointID = controller.getSessionAttr("ONE_POINT_ID");
				Record rc = Db.findFirst("SELECT F_ONE_POINT_ID FROM SYS_USER WHERE F_ACCOUNT = ?",loginUser);
				if(rc != null) {
					String queryOnePointID = rc.getStr("F_ONE_POINT_ID");
					if(queryOnePointID.equals(sessionOnePointID)) {
						//做完操作之后统一更新session
						Record updateSessionRec = Db.findFirst("SELECT * FROM SYS_USER WHERE F_ACCOUNT = ?",loginUser);
						String columnKey[] = updateSessionRec.getColumnNames();
						for(int i=0;i<columnKey.length;i++){
							if(!"F_PASSWORD".equals(columnKey[i])) {
								controller.setSessionAttr(columnKey[i], updateSessionRec.get(columnKey[i]));
							}
						}
						Ivc.invoke();
						return;
					}else {
						String ajax = Ivc.getController().getRequest()
								.getHeader("X-Requested-With");
						if ("XMLHttpRequest".equals(ajax)) {
							controller.renderError(403);
						} else {
							controller.renderJsp("/"+Constant.INDEXJSP);
						}
						Record rec = Db.findFirst("SELECT * FROM SYS_USER WHERE F_ACCOUNT = ?",loginUser);
						if(rec != null) {
							String columnKey[] = rec.getColumnNames();
							for(int i=0;i<columnKey.length;i++) {
								controller.removeSessionAttr(columnKey[i]);
							}
						}
						controller.removeSessionAttr("ONE_POINT_ID");
						return;
					}
				}else {
					String ajax = Ivc.getController().getRequest()
							.getHeader("X-Requested-With");
					if ("XMLHttpRequest".equals(ajax)) {
						controller.renderError(403);
					} else {
						controller.renderJsp("/"+Constant.INDEXJSP);
					}
					return;
				}
			}else {
				//做完操作之后统一更新session
				Record updateSessionRec = Db.findFirst("SELECT * FROM SYS_USER WHERE F_ACCOUNT = ?",loginUser);
				String columnKey[] = updateSessionRec.getColumnNames();
				for(int i=0;i<columnKey.length;i++){
					if(!"F_PASSWORD".equals(columnKey[i])) {
						controller.setSessionAttr(columnKey[i], updateSessionRec.get(columnKey[i]));
					}
				}
				Ivc.invoke();
				return;
			}
		} else {
			String ajax = Ivc.getController().getRequest()
					.getHeader("X-Requested-With");
			if ("XMLHttpRequest".equals(ajax)) {
				controller.renderError(403);
			} else {
				controller.renderJsp("/"+Constant.INDEXJSP);
			}
			if(null != loginUser && !Constant.EMPTYSTR.equals(loginUser)) {
				Record rec = Db.findFirst("SELECT * FROM SYS_USER WHERE F_ACCOUNT = ?",loginUser);
				if(rec != null) {
					String columnKey[] = rec.getColumnNames();
					for(int i=0;i<columnKey.length;i++) {
						controller.removeSessionAttr(columnKey[i]);
					}
				}
				controller.removeSessionAttr("ONE_POINT_ID");
			}
			return;
		}
	}
}