package com.dogu.action;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dogu.constants.Constant;
import com.dogu.utils.CommonUtils;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

public class MenuManageAction extends Controller {

	public void index() {
		String RequestURI = this.getRequest().getRequestURI();
		String ContextPath = this.getRequest().getContextPath();
		String functionPath = RequestURI.replaceAll(ContextPath, "");
		Record renderRcd = Db.findFirst("SELECT F_PATH FROM SYS_MENU_DCT WHERE F_FUNC_PATH = ?",
				functionPath);
		if(null == renderRcd) {
			this.render(Constant.PAGE404PATH);
		}else {
			this.render(renderRcd.getStr("F_PATH"));
		}
	}
	/**
	 * 获取菜单信息的
	 */
	public void queryMenuTree() {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("code", 0);
		returnMap.put("msg", "");
		List<Record> dataLs = Db.find("SELECT F_ID,F_NAME,F_PID,F_FUNC_PATH,"
				+ "F_PATH,F_ICON,F_USE,F_SHOW,F_INTERCEPT,F_TYPE"
				+ " FROM SYS_MENU_DCT ORDER BY F_ID");
		returnMap.put("count", dataLs.size());
		returnMap.put("data", dataLs);
		this.renderJson(returnMap);
	}
	
	/**
	 * 新增菜单
	 */
	public void addMenu() {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		String id = this.getPara("id", "");
		String name = this.getPara("name", "");
		String pid = this.getPara("pid", "");
		String path = this.getPara("path", "");
		String icon = this.getPara("icon", "");
		String use = this.getPara("use", "");
		String show = this.getPara("show", "");
		String interceptor = this.getPara("interceptor", "");
		String fpath = this.getPara("fpath", "");
		String type = this.getPara("type", "");
		
		try {
			name = URLDecoder.decode(name, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			path = URLDecoder.decode(path, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			fpath = URLDecoder.decode(fpath, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if("".equals(path)) {
			path = "javascript:;";
		}
		if("".equals(fpath)) {
			fpath = "javascript:;";
		}
		//检查是否为纯数字
		if (CommonUtils.isNumber(id) && CommonUtils.isNumber(pid.replaceAll("-", ""))) {
			//检查父ID是否存在
			if("-1".equals(pid)) {
				//检查ID是否已存在
				Record rec = Db.findFirst("SELECT F_ID FROM SYS_MENU_DCT WHERE F_ID = ?", id);
				if(null != rec) {
					returnMap.put("code", "1");
					returnMap.put("message", "已经存在的菜单编号!");
				}else {
					Db.update(
							"INSERT INTO SYS_MENU_DCT (F_ID,F_NAME,F_PID,F_PATH,F_FUNC_PATH,F_ICON,F_USE,F_SHOW,F_INTERCEPT,F_TYPE) VALUES (?,?,?,?,?,?,?,?,?,?)",
							id, name, pid, path, fpath, icon, use, show, interceptor, type);
					returnMap.put("code", "0");
					returnMap.put("message", "操作成功!");
				}
			}else {
				//检查PID是否存在
				Record CheckRec = Db.findFirst("SELECT F_ID FROM SYS_MENU_DCT WHERE F_ID = ?",pid);
				if(null == CheckRec) {
					returnMap.put("code", "1");
					returnMap.put("message", "菜单父ID不存在!");
				}else {
					//检查ID是否已存在
					Record rec = Db.findFirst("SELECT F_ID FROM SYS_MENU_DCT WHERE F_ID = ?", id);
					if(null != rec) {
						returnMap.put("code", "1");
						returnMap.put("message", "已经存在的菜单编号!");
					}else {
						Db.update(
								"INSERT INTO SYS_MENU_DCT (F_ID,F_NAME,F_PID,F_PATH,F_FUNC_PATH,F_ICON,F_USE,F_SHOW,F_INTERCEPT,F_TYPE) VALUES (?,?,?,?,?,?,?,?,?,?)",
								id, name, pid, path, fpath, icon, use, show, interceptor, type);
						returnMap.put("code", "0");
						returnMap.put("message", "操作成功!");
					}
				}
			}
		} else {
			returnMap.put("code", "1");
			returnMap.put("message", "菜单(父)编号只能为数字!");
		}
		this.renderJson(returnMap);
	}
	
	/**
	 * 获取单个菜单信息
	 */
	public void getOneMenuInfo() {
		String menuid = this.getPara("menuid","");
		Map<String, Object> map = new HashMap<String, Object>();
		Record rec = Db.findFirst(
				"SELECT * FROM SYS_MENU_DCT WHERE F_ID = ?",
				menuid);
		if (null != rec) {
			map.put("Id", rec.getStr("F_ID"));
			map.put("Pid", rec.getStr("F_PID"));
			map.put("Name", rec.getStr("F_NAME"));
			map.put("FunPath", rec.getStr("F_FUNC_PATH"));
			map.put("Path", rec.getStr("F_PATH"));
			map.put("Icon", rec.getStr("F_ICON"));
			map.put("Use", rec.getStr("F_USE"));
			map.put("Show", rec.getStr("F_SHOW"));
			map.put("Intercept", rec.getStr("F_INTERCEPT"));
			map.put("Type", rec.getStr("F_TYPE"));
			map.put("code", "0");
		} else {
			map.put("code", "1");
			map.put("message", "不存在的菜单信息，请重新操作！");
		}
		this.renderJson(map);
	}
	
	/**
	 * 修改菜单内容
	 */
	public void updateMenu() {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		String id = this.getPara("id", "");
		String name = this.getPara("name", "");
		String pid = this.getPara("pid", "");
		String path = this.getPara("path", "");
		String fpath = this.getPara("fpath", "");
		String icon = this.getPara("icon", "");
		String use = this.getPara("use", "");
		String show = this.getPara("show", "");
		String type = this.getPara("type", "");
		String interceptor = this.getPara("interceptor", "");
		try {
			name = URLDecoder.decode(name, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			path = URLDecoder.decode(path, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			fpath = URLDecoder.decode(fpath, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if("".equals(path)) {
			path = "javascript:;";
		}
		if("".equals(fpath)) {
			fpath = "javascript:;";
		}
		//检查是否为纯数字
		if (CommonUtils.isNumber(id) && CommonUtils.isNumber(pid.replaceAll("-", ""))) {
			//检查父ID是否存在
			if("-1".equals(pid)) {
				//检查ID是否已存在
				Record rec = Db.findFirst("SELECT F_ID FROM SYS_MENU_DCT WHERE F_ID = ?", id);
				if(null == rec) {
					returnMap.put("code", "1");
					returnMap.put("message", "不存在的菜单编号!");
				}else {
					Db.update(
							"UPDATE SYS_MENU_DCT SET F_NAME = ?,F_PID = ?,F_PATH = ?,F_FUNC_PATH = ?,"
							+ "F_ICON = ?,F_USE = ?,F_SHOW = ?,F_INTERCEPT = ?,F_TYPE = ? WHERE F_ID = ?",
							name, pid, path, fpath, icon, use, show, interceptor, type,id);
					returnMap.put("code", "0");
					returnMap.put("message", "操作成功!");
				}
			}else {
				//检查PID是否存在
				Record CheckRec = Db.findFirst("SELECT F_ID FROM SYS_MENU_DCT WHERE F_ID = ?",pid);
				if(null == CheckRec) {
					returnMap.put("code", "1");
					returnMap.put("message", "菜单父ID不存在!");
				}else {
					//检查ID是否已存在
					Record rec = Db.findFirst("SELECT F_ID FROM SYS_MENU_DCT WHERE F_ID = ?", id);
					if(null == rec) {
						returnMap.put("code", "1");
						returnMap.put("message", "不存在的菜单编号!");
					}else {
						Db.update(
								"UPDATE SYS_MENU_DCT SET F_NAME = ?,F_PID = ?,F_PATH = ?,F_FUNC_PATH = ?,"
								+ "F_ICON = ?,F_USE = ?,F_SHOW = ?,F_INTERCEPT = ?,F_TYPE = ? WHERE F_ID = ?",
								name, pid, path, fpath, icon, use, show, interceptor, type,id);
						returnMap.put("code", "0");
						returnMap.put("message", "操作成功!");
					}
				}
			}
		} else {
			returnMap.put("code", "1");
			returnMap.put("message", "菜单(父)编号只能为数字!");
		}
		this.renderJson(returnMap);
	}
	
	/**
	 * 删除菜单
	 */
	public void deleteMenu() {
		Map<String, Object> map = new HashMap<String, Object>();
		String menuid = this.getPara("menuid", "");
		Db.update("DELETE FROM SYS_MENU_DCT WHERE F_ID = ?", menuid );//删除当前菜单
		Db.update("DELETE FROM SYS_MENU_LIMIT WHERE F_MENUID = ?", menuid );//删除权限表的内容
		
		List<Record> delteChildMenuLs = Db.find("SELECT F_ID FROM SYS_MENU_DCT WHERE F_PID = ?",menuid);//查询出来为删除子菜单权限表用
		Db.update("DELETE FROM SYS_MENU_DCT WHERE F_PID = ?", menuid );//删除当前菜单的子菜单(必须先查再删)
		
		for(Record rec: delteChildMenuLs) {
			Db.update("DELETE FROM SYS_MENU_LIMIT WHERE F_MENUID = ?", rec.getStr("F_ID"));
		}
		map.put("message", "操作成功!");
		this.renderJson(map);
	}
	
	/**
	 * 查询功能列表
	 */
	public void queryFuncList() {
		String menuId = this.getPara("menuId", "");
		this.renderJson(Db.find("SELECT F_ID,F_NAME,F_FUNC_PATH,F_USE FROM SYS_MENU_DCT WHERE F_PID = ? AND F_TYPE = ? ",menuId,Constant.CAPITAL_STR_FUNC));
	}
}
