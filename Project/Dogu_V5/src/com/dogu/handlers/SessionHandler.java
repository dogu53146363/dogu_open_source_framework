package com.dogu.handlers;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.dogu.constants.Constant;
import com.dogu.utils.GetConFromDB;
import com.jfinal.handler.Handler;
import com.jfinal.kit.HandlerKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

/**
 * Session拦截器
 * @author Dogu 2016-12-17 20:28
 */
public class SessionHandler extends Handler {

	/**
	 * 全局页面拦截器:防止出现用户登陆后 可以在地址栏中直接访问未赋权页面的情况
	 */
	@SuppressWarnings("unchecked")
	public void handle(String target, HttpServletRequest request,
			HttpServletResponse response, boolean[] isHandled) {
		HttpSession session = request.getSession();
		Map<String, String> sessionStorage = (Map<String, String>) session
				.getAttribute(Constant.SESSION_STORAGE);
		//排除资源文件如:js和css
		if(target.indexOf(".") > 0) {
			next.handle(target, request, response, isHandled);
		}else {
			if(null == sessionStorage) {//在没有登录的时候
				Record rec = Db
						.findFirst(
								"SELECT 1 FROM SYS_MENU_DCT WHERE F_FUNC_PATH = ? AND F_INTERCEPT = ? AND F_USE = ?",
								target, Constant.CAPITAL_STR_N, Constant.CAPITAL_STR_Y);
				if (null == rec) {
					HandlerKit.renderError403(request, response, isHandled);
				} else {
					next.handle(target, request, response, isHandled);
				}
			}else {//登录之后的判断
				//单端登录检查
				String SessionOnePointId = sessionStorage.get("F_ONE_POINT_ID");
				String Account = sessionStorage.get("F_ACCOUNT");
				Record UserOnePointIdRcd = Db.findFirst("SELECT F_ONE_POINT_ID FROM SYS_USER WHERE F_ACCOUNT = ?",Account);
				String currentOnePointId = UserOnePointIdRcd.getStr("F_ONE_POINT_ID");
				String OnePointStatus = GetConFromDB.GetCIFromDB("OPEN_ONE_POINT");
				if("true".equals(OnePointStatus) && !SessionOnePointId.equals(currentOnePointId)) {//打开单端登录且ID不同时销毁session
					session.invalidate();
				}else {
					//是否实时更新session信息
					String ALWAYS_KEEP_NEW_SESSION = GetConFromDB.GetCIFromDB("ALWAYS_KEEP_NEW_SESSION");
					if("true".equals(ALWAYS_KEEP_NEW_SESSION)) {
						Record rec = Db.findFirst("SELECT * FROM SYS_USER WHERE F_ACCOUNT = ?",Account);
						String columnKey[] = rec.getColumnNames();
						Map<String, String> sessionMap = new HashMap<String, String>();
						for(int i=0;i<columnKey.length;i++){
							if(!"F_PASSWORD".equals(columnKey[i])) {
								sessionMap.put(columnKey[i], rec.getStr(columnKey[i]));
							}
						}
						session.setAttribute(Constant.SESSION_STORAGE,sessionMap);
					}
					String roleId = sessionStorage.get("F_ROLE");
					String orgId = sessionStorage.get("F_ORG");
					String LimitType = GetConFromDB.GetCIFromDB("LIMIT_TYPE");
					if(Constant.CAPITAL_STR_ORG.equals(LimitType)) {
						Record rec = Db
								.findFirst(
										"SELECT 1 FROM SYS_MENU_LIMIT MENU_LIMIT,SYS_MENU_DCT MENU_DCT"
												+ " WHERE ((MENU_LIMIT.F_ACCOUNT = ? AND MENU_LIMIT.F_TYPE = ? ) OR"
												+ " (MENU_LIMIT.F_ACCOUNT = ? AND MENU_LIMIT.F_TYPE = ? ))"
												+ " AND MENU_DCT.F_FUNC_PATH = ? AND MENU_LIMIT.F_MENUID = MENU_DCT.F_ID AND MENU_DCT.F_USE = ? ",
												Account, Constant.CAPITAL_STR_USER, orgId, Constant.CAPITAL_STR_ORG, target, Constant.CAPITAL_STR_Y);
						// 当用户登录后且没给当前功能授权的时候
						if (null == rec) {
							rec = Db.findFirst(
									"SELECT 1 FROM SYS_MENU_DCT WHERE F_FUNC_PATH = ? AND F_INTERCEPT = ? AND F_USE = ?",
									target, Constant.CAPITAL_STR_N, Constant.CAPITAL_STR_Y);
							if (null == rec) {
								HandlerKit.renderError403(request, response, isHandled);
							} else {
								next.handle(target, request, response, isHandled);
							}
						} else {
							next.handle(target, request, response, isHandled);
						}
					}else if(Constant.CAPITAL_STR_ROLE.equals(LimitType)){
						Record rec = Db
								.findFirst(
										"SELECT 1 FROM SYS_MENU_LIMIT MENU_LIMIT,SYS_MENU_DCT MENU_DCT"
												+ " WHERE ((MENU_LIMIT.F_ACCOUNT = ? AND MENU_LIMIT.F_TYPE = ? ) OR"
												+ " (MENU_LIMIT.F_ACCOUNT = ? AND MENU_LIMIT.F_TYPE = ? ))"
												+ " AND MENU_DCT.F_FUNC_PATH = ? AND MENU_LIMIT.F_MENUID = MENU_DCT.F_ID AND MENU_DCT.F_USE = ? ",
												Account, Constant.CAPITAL_STR_USER, roleId, Constant.CAPITAL_STR_ROLE, target, Constant.CAPITAL_STR_Y);
						// 当用户登录后且没给当前访问页面赋权的时候,判断是否可访问该页面
						if (null == rec) {
							rec = Db.findFirst(
									"SELECT 1 FROM SYS_MENU_DCT WHERE F_FUNC_PATH = ? AND F_INTERCEPT = ? AND F_USE = ?",
									target, Constant.CAPITAL_STR_N, Constant.CAPITAL_STR_Y);
							if (null == rec) {
								HandlerKit.renderError403(request, response, isHandled);
							} else {
								next.handle(target, request, response, isHandled);
							}
						} else {
							next.handle(target, request, response, isHandled);
						}
					}else {
						Record rec = Db
								.findFirst(
										"SELECT 1 FROM SYS_MENU_LIMIT MENU_LIMIT,SYS_MENU_DCT MENU_DCT"
												+ " WHERE ((MENU_LIMIT.F_ACCOUNT = ? AND MENU_LIMIT.F_TYPE = ? ) OR"
												+ " (MENU_LIMIT.F_ACCOUNT = ? AND MENU_LIMIT.F_TYPE = ? ) OR (MENU_LIMIT.F_ACCOUNT = ? AND MENU_LIMIT.F_TYPE = ? ))"
												+ " AND MENU_DCT.F_FUNC_PATH = ? AND MENU_LIMIT.F_MENUID = MENU_DCT.F_ID AND MENU_DCT.F_USE = ? ",
												Account, Constant.CAPITAL_STR_USER, orgId, Constant.CAPITAL_STR_ORG, Constant.CAPITAL_STR_ROLE,
										roleId, target, Constant.CAPITAL_STR_Y);
						// 当用户登录后且没给当前访问页面赋权的时候,判断是否可访问该页面
						if (null == rec) {
							rec = Db.findFirst(
									"SELECT 1 FROM SYS_MENU_DCT WHERE F_FUNC_PATH = ? AND F_INTERCEPT = ? AND F_USE = ?",
									target, Constant.CAPITAL_STR_N, Constant.CAPITAL_STR_Y);
							if (null == rec) {
								rec = Db
										.findFirst(
												"SELECT 1 FROM SYS_MENU_LIMIT MENU_LIMIT,SYS_MENU_DCT MENU_DCT"
														+ " WHERE ((MENU_LIMIT.F_ACCOUNT = ? AND MENU_LIMIT.F_TYPE = ? ) OR"
														+ " (MENU_LIMIT.F_ACCOUNT = ? AND MENU_LIMIT.F_TYPE = ? ))"
														+ " AND MENU_DCT.F_FUNC_PATH = ? AND MENU_LIMIT.F_MENUID = MENU_DCT.F_ID AND MENU_DCT.F_USE = ? ",
														Account, Constant.CAPITAL_STR_USER, roleId, Constant.CAPITAL_STR_ROLE, target, Constant.CAPITAL_STR_Y);
								// 当用户登录后且没给当前访问页面赋权的时候,判断是否可访问该页面
								if (null == rec) {
									rec = Db.findFirst(
											"SELECT 1 FROM SYS_MENU_DCT WHERE F_FUNC_PATH = ? AND F_INTERCEPT = ? AND F_USE = ?",
											target, Constant.CAPITAL_STR_N, Constant.CAPITAL_STR_Y);
									if (null == rec) {
										HandlerKit.renderError403(request, response, isHandled);
									} else {
										next.handle(target, request, response, isHandled);
									}
								} else {
									next.handle(target, request, response, isHandled);
								}
								
							} else {
								next.handle(target, request, response, isHandled);
							}
						} else {
							next.handle(target, request, response, isHandled);
						}
					}
				}
			}
		}
	}
}
