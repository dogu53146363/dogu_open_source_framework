package com.dogu.handlers;

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
	public void handle(String target, HttpServletRequest request,
			HttpServletResponse response, boolean[] isHandled) {
		HttpSession session = request.getSession();
		String username = (String) session.getAttribute("F_ACCOUNT");
		String role = (String) session.getAttribute("F_ROLE");
		String orgId = (String) session.getAttribute("F_ORG");
		String path = target.substring(1, target.length());
		// 拦截所有的页面(jsp/htm/html/shtm/shtml)
		if (path.toLowerCase().endsWith(Constant.DOJSP) || path.toLowerCase().endsWith(Constant.DOJSPX)
				|| path.toLowerCase().endsWith(Constant.DOHTM) || path.toLowerCase().endsWith(Constant.DOHTML)
				|| path.toLowerCase().endsWith(Constant.DOSHTM) || path.toLowerCase().endsWith(Constant.DOSHTML) ) {
			// 当没用户登录的时候(session中的username为空)
			if (null == username || (Constant.STR_LOWERCASE_NULL.equals(username))
					|| Constant.EMPTYSTR.equals(username)) {
				Record rec = Db
						.findFirst(
								"SELECT COUNT(1) AS NUM FROM SYS_MENU_DCT WHERE F_PATH = ? AND F_INTERCEPT = ? AND F_USE = ?",
								path, Constant.CAPITAL_STR_N, Constant.CAPITAL_STR_Y);
				String num = rec.get("NUM").toString();
				if (Constant.STR_ZERO.equals(num)) {
					HandlerKit.renderError404(request, response, isHandled);
				} else {
					next.handle(target, request, response, isHandled);
				}
			} else {
				// 当用户登录后
				String LimitType = GetConFromDB.GetCIFromDB("LIMIT_TYPE");
				if(Constant.CAPITAL_STR_ORG.equals(LimitType)) {
					Record rec = Db
							.findFirst(
									"SELECT COUNT(1) AS NUM FROM SYS_MENU_LIMIT MENU_LIMIT,SYS_MENU_DCT MENU_DCT"
											+ " WHERE ((MENU_LIMIT.F_ACCOUNT = ? AND F_TYPE = ? ) OR"
											+ " (MENU_LIMIT.F_ACCOUNT = ? AND F_TYPE = ? ))"
											+ " AND MENU_DCT.F_PATH = ? AND MENU_LIMIT.F_MENUID = MENU_DCT.F_ID AND MENU_DCT.F_USE = ? ",
									username, Constant.CAPITAL_STR_USER, orgId, Constant.CAPITAL_STR_ORG, path, Constant.CAPITAL_STR_Y);
					String num = rec.get("NUM").toString();
					// 当用户登录后且没给当前访问页面赋权的时候,判断是否可访问该页面
					if (Constant.STR_ZERO.equals(num)) {
						rec = Db.findFirst(
								"SELECT COUNT(1) AS NUM FROM SYS_MENU_DCT WHERE F_PATH = ? AND F_INTERCEPT = ? AND F_USE = ?",
								path, Constant.CAPITAL_STR_N, Constant.CAPITAL_STR_Y);
						num = rec.get("NUM").toString();
						if (Constant.STR_ZERO.equals(num)) {
							HandlerKit.renderError404(request, response, isHandled);
						} else {
							next.handle(target, request, response, isHandled);
						}
					} else {
						next.handle(target, request, response, isHandled);
					}
				}else if(Constant.CAPITAL_STR_ROLE.equals(LimitType)){
					Record rec = Db
							.findFirst(
									"SELECT COUNT(1) AS NUM FROM SYS_MENU_LIMIT MENU_LIMIT,SYS_MENU_DCT MENU_DCT"
											+ " WHERE ((MENU_LIMIT.F_ACCOUNT = ? AND F_TYPE = ? ) OR"
											+ " (MENU_LIMIT.F_ACCOUNT = ? AND F_TYPE = ? ))"
											+ " AND MENU_DCT.F_PATH = ? AND MENU_LIMIT.F_MENUID = MENU_DCT.F_ID AND MENU_DCT.F_USE = ? ",
									username, Constant.CAPITAL_STR_USER, role, Constant.CAPITAL_STR_ROLE, path, Constant.CAPITAL_STR_Y);
					String num = rec.get("NUM").toString();
					// 当用户登录后且没给当前访问页面赋权的时候,判断是否可访问该页面
					if (Constant.STR_ZERO.equals(num)) {
						rec = Db.findFirst(
								"SELECT COUNT(1) AS NUM FROM SYS_MENU_DCT WHERE F_PATH = ? AND F_INTERCEPT = ? AND F_USE = ?",
								path, Constant.CAPITAL_STR_N, Constant.CAPITAL_STR_Y);
						num = rec.get("NUM").toString();
						if (Constant.STR_ZERO.equals(num)) {
							HandlerKit.renderError404(request, response, isHandled);
						} else {
							next.handle(target, request, response, isHandled);
						}
					} else {
						next.handle(target, request, response, isHandled);
					}
				}else {
					Record rec = Db
							.findFirst(
									"SELECT COUNT(1) AS NUM FROM SYS_MENU_LIMIT MENU_LIMIT,SYS_MENU_DCT MENU_DCT"
											+ " WHERE ((MENU_LIMIT.F_ACCOUNT = ? AND F_TYPE = ? ) OR"
											+ " (MENU_LIMIT.F_ACCOUNT = ? AND F_TYPE = ? ) OR (MENU_LIMIT.F_ACCOUNT = ? AND F_TYPE = ? ))"
											+ " AND MENU_DCT.F_PATH = ? AND MENU_LIMIT.F_MENUID = MENU_DCT.F_ID AND MENU_DCT.F_USE = ? ",
									username, Constant.CAPITAL_STR_USER, orgId, Constant.CAPITAL_STR_ORG, Constant.CAPITAL_STR_ROLE,
									role, path, Constant.CAPITAL_STR_Y);
					String num = rec.get("NUM").toString();
					// 当用户登录后且没给当前访问页面赋权的时候,判断是否可访问该页面
					if (Constant.STR_ZERO.equals(num)) {
						rec = Db.findFirst(
								"SELECT COUNT(1) AS NUM FROM SYS_MENU_DCT WHERE F_PATH = ? AND F_INTERCEPT = ? AND F_USE = ?",
								path, Constant.CAPITAL_STR_N, Constant.CAPITAL_STR_Y);
						num = rec.get("NUM").toString();
						if (Constant.STR_ZERO.equals(num)) {
							
							rec = Db
									.findFirst(
											"SELECT COUNT(1) AS NUM FROM SYS_MENU_LIMIT MENU_LIMIT,SYS_MENU_DCT MENU_DCT"
													+ " WHERE ((MENU_LIMIT.F_ACCOUNT = ? AND F_TYPE = ? ) OR"
													+ " (MENU_LIMIT.F_ACCOUNT = ? AND F_TYPE = ? ))"
													+ " AND MENU_DCT.F_PATH = ? AND MENU_LIMIT.F_MENUID = MENU_DCT.F_ID AND MENU_DCT.F_USE = ? ",
											username, Constant.CAPITAL_STR_USER, role, Constant.CAPITAL_STR_ROLE, path, Constant.CAPITAL_STR_Y);
							num = rec.get("NUM").toString();
							// 当用户登录后且没给当前访问页面赋权的时候,判断是否可访问该页面
							if (Constant.STR_ZERO.equals(num)) {
								rec = Db.findFirst(
										"SELECT COUNT(1) AS NUM FROM SYS_MENU_DCT WHERE F_PATH = ? AND F_INTERCEPT = ? AND F_USE = ?",
										path, Constant.CAPITAL_STR_N, Constant.CAPITAL_STR_Y);
								num = rec.get("NUM").toString();
								if (Constant.STR_ZERO.equals(num)) {
									HandlerKit.renderError404(request, response, isHandled);
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
		} else {
			next.handle(target, request, response, isHandled);
		}
	}
}
