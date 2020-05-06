package com.dogu.action;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dogu.constants.Constant;
import com.dogu.utils.GetConFromDB;
import com.dogu.utils.MenuTreeUtil;
import com.dogu.utils.Tree;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

public class MainAction extends Controller {
	
	/**
	 * main根路径
	 */
	public void index() {
		this.render(Constant.MAINPAGEPATH);
	}
	
	/**
	 * 我的桌面
	 */
	public void desktop() {
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
	 * 更换UI
	 */
	public void changeUI() {
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
	 * 便签
	 */
	public void note() {
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
	 * 消息
	 */
	public void message() {
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
	 * 登录后获取菜单
	 */
	@SuppressWarnings("unchecked")
	public void getMenuInfo() {
		
		String LimitType = GetConFromDB.GetCIFromDB("LIMIT_TYPE");
		Map<String, Object> returnMap = new HashMap<String, Object>();
		Map<String, String> sessionStorage = (Map<String, String>) this.getSession()
				.getAttribute(Constant.SESSION_STORAGE);
		String account = sessionStorage.get("F_ACCOUNT");
		String orgId = sessionStorage.get("F_ORG");
		String role = sessionStorage.get("F_ROLE");
		String ContextPath = this.getRequest().getContextPath();
		if (Constant.CAPITAL_STR_ORG.equals(LimitType)) {//组织机构行权限
			List<Record> DataList =  Db.find("SELECT DISTINCT(F_ID) AS ID,F_PID AS PID,"
					+ "F_NAME AS NAME,F_FUNC_PATH AS URL,F_ICON AS ICON"
					+ " FROM SYS_MENU_DCT dct,SYS_MENU_LIMIT lmt"
					+ " WHERE dct.F_ID = lmt.F_MENUID AND ((lmt.F_TYPE = ? AND F_ACCOUNT = ?)"
					+ " OR (lmt.F_TYPE = ? AND F_ACCOUNT = ?)) AND F_SHOW = ? AND F_USE = ?"
					+ " AND dct.F_TYPE = ? ORDER BY F_ID",
						Constant.CAPITAL_STR_ORG,orgId,Constant.CAPITAL_STR_USER,account,
						Constant.CAPITAL_STR_Y,Constant.CAPITAL_STR_Y,Constant.CAPITAL_STR_MENU);
			if(DataList.size() == 0) {
				returnMap.put("code", "400");
				returnMap.put("msg", "未配置该用户菜单，请联系管理员！");
			}else {
				returnMap.put("code", "200");
				returnMap.put("msg", "");
				//组织菜单
				List<Tree> list = new ArrayList<Tree>();
				Tree tree = new Tree();
				for(int i=0;i<DataList.size();i++) {
					tree = new Tree();
					tree.setId(DataList.get(i).getStr("ID"));
					tree.setpId(DataList.get(i).getStr("PID"));
					tree.setName(DataList.get(i).getStr("NAME"));
					if("javascript:;".equals(DataList.get(i).getStr("URL"))) {
						tree.setUrl(DataList.get(i).getStr("URL"));
					}else {
						tree.setUrl(ContextPath+DataList.get(i).getStr("URL"));
					}
					tree.setIcon(DataList.get(i).getStr("ICON"));
					list.add(tree);
				}
				MenuTreeUtil menuTree = new MenuTreeUtil();
				List<Object> menuList = menuTree.menuList(list);
				returnMap.put("data",menuList);
			}
		} else if (Constant.CAPITAL_STR_ROLE.equals(LimitType)) {//角色行权限
			List<Record> DataList =  Db.find("SELECT DISTINCT(F_ID) AS ID,F_PID AS PID,"
					+ "F_NAME AS NAME,F_FUNC_PATH AS URL,F_ICON AS ICON"
					+ " FROM SYS_MENU_DCT dct,SYS_MENU_LIMIT lmt"
					+ " WHERE dct.F_ID = lmt.F_MENUID AND ((lmt.F_TYPE = ? AND F_ACCOUNT = ?)"
					+ " OR (lmt.F_TYPE = ? AND F_ACCOUNT = ?)) AND F_SHOW = ? AND F_USE = ?"
					+ " AND dct.F_TYPE = ? ORDER BY F_ID",
						Constant.CAPITAL_STR_ROLE,role,Constant.CAPITAL_STR_USER,account,
						Constant.CAPITAL_STR_Y,Constant.CAPITAL_STR_Y,Constant.CAPITAL_STR_MENU);
			if(DataList.size() == 0) {
				returnMap.put("code", "400");
				returnMap.put("msg", "未配置该用户菜单，请联系管理员！");
			}else {
				returnMap.put("code", "200");
				returnMap.put("msg", "");
				//组织菜单
				List<Tree> list = new ArrayList<Tree>();
				Tree tree = new Tree();
				for(int i=0;i<DataList.size();i++) {
					tree = new Tree();
					tree.setId(DataList.get(i).getStr("ID"));
					tree.setpId(DataList.get(i).getStr("PID"));
					tree.setName(DataList.get(i).getStr("NAME"));
					if("javascript:;".equals(DataList.get(i).getStr("URL"))) {
						tree.setUrl(DataList.get(i).getStr("URL"));
					}else {
						tree.setUrl(ContextPath+DataList.get(i).getStr("URL"));
					}
					tree.setIcon(DataList.get(i).getStr("ICON"));
					list.add(tree);
				}
				MenuTreeUtil menuTree = new MenuTreeUtil();
				List<Object> menuList = menuTree.menuList(list);
				returnMap.put("data",menuList);
			}
		} else if(Constant.CAPITAL_STR_ALL.equals(LimitType)){//所有权限模式
			List<Record> DataList =  Db.find("SELECT DISTINCT(F_ID) AS ID,F_PID AS PID,"
					+ "F_NAME AS NAME,F_FUNC_PATH AS URL,F_ICON AS ICON"
					+ " FROM SYS_MENU_DCT dct,SYS_MENU_LIMIT lmt"
					+ " WHERE dct.F_ID = lmt.F_MENUID AND ((lmt.F_TYPE = ? AND F_ACCOUNT = ?)"
					+ " OR (lmt.F_TYPE = ? AND F_ACCOUNT = ?) OR (lmt.F_TYPE = ? AND F_ACCOUNT = ? ))"
					+ " AND F_SHOW = ? AND F_USE = ? AND dct.F_TYPE = ?"
					+ " ORDER BY F_ID",Constant.CAPITAL_STR_USER,account,
						Constant.CAPITAL_STR_ROLE,role,Constant.CAPITAL_STR_ORG,orgId,
						Constant.CAPITAL_STR_Y,Constant.CAPITAL_STR_Y,Constant.CAPITAL_STR_MENU);
			if(DataList.size() == 0) {
				returnMap.put("code", "400");
				returnMap.put("msg", "未配置该用户菜单，请联系管理员！");
			}else {
				returnMap.put("code", "200");
				returnMap.put("msg", "");
				//组织菜单
				List<Tree> list = new ArrayList<Tree>();
				Tree tree = new Tree();
				for(int i=0;i<DataList.size();i++) {
					tree = new Tree();
					tree.setId(DataList.get(i).getStr("ID"));
					tree.setpId(DataList.get(i).getStr("PID"));
					tree.setName(DataList.get(i).getStr("NAME"));
					if("javascript:;".equals(DataList.get(i).getStr("URL"))) {
						tree.setUrl(DataList.get(i).getStr("URL"));
					}else {
						tree.setUrl(ContextPath+DataList.get(i).getStr("URL"));
					}
					tree.setIcon(DataList.get(i).getStr("ICON"));
					list.add(tree);
				}
				MenuTreeUtil menuTree = new MenuTreeUtil();
				List<Object> menuList = menuTree.menuList(list);
				returnMap.put("data",menuList);
			}
		}
		this.renderJson(returnMap);
	}

	/**
	 * 获取MydeskTop页面的路径
	 */
	public void getDeskTopPath() {
		Map<String, String> returnMap = new HashMap<String, String>();
		String path = GetConFromDB.GetCIFromDB("DESKTOP_PATH");
		returnMap.put("PATH", path);
		this.renderJson(returnMap);
	}
	
	/**
	 * 首页获取用户信息
	 */
	@SuppressWarnings("unchecked")
	public void getIndexChangeUserInfo() {
		Map<String, String> sessionStorage = (Map<String, String>) this.getSession()
				.getAttribute(Constant.SESSION_STORAGE);
		String account = sessionStorage.get("F_ACCOUNT");
		Record rec = Db.findFirst(
				"SELECT F_ACCOUNT,F_USERNAME,F_SEX,F_TEL,F_ADDRESS,F_E_MAIL,F_QQ FROM SYS_USER WHERE F_ACCOUNT = ?",
				account);
		String username = rec.getStr("F_USERNAME");
		String sex = rec.getStr("F_SEX");
		String tel = rec.getStr("F_TEL");
		String address = rec.getStr("F_ADDRESS");
		String email = rec.getStr("F_E_MAIL");
		String qq = rec.getStr("F_QQ");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("account", account);
		map.put("username", username);
		map.put("sex", sex);
		map.put("tel", tel);
		map.put("address", address);
		map.put("email", email);
		map.put("qq", qq);
		this.renderJson(map);
	}
	
	/**
	 * 首页改变用户信息
	 */
	@SuppressWarnings("unchecked")
	public void indexChangeUser() {
		Map<String, String> sessionStorage = (Map<String, String>) this.getSession()
				.getAttribute(Constant.SESSION_STORAGE);
		String account = sessionStorage.get("F_ACCOUNT");
		String name = this.getPara("name", "");
		try {
			name = URLDecoder.decode(name, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sex = this.getPara("sex", "");
		String tel = this.getPara("tel", "");
		String address = this.getPara("address", "");
		try {
			address = URLDecoder.decode(address, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		String email = this.getPara("email", "");
		String qq = this.getPara("qq", "");
		Map<String, Object> map = new HashMap<String, Object>();
		Db.update(
				"UPDATE SYS_USER SET F_USERNAME = ?,F_SEX = ?,F_TEL = ?,F_ADDRESS = ?,F_E_MAIL = ?,F_QQ = ? WHERE F_ACCOUNT = ?",
				name, sex, tel, address, email, qq, account);
		map.put("code", "0");
		map.put("message", "修改成功!");
		this.renderJson(map);
	}

	/**
	 * 验证并更改密码
	 */
	@SuppressWarnings("unchecked")
	public void validatePassword() {
		Map<String, String> sessionStorage = (Map<String, String>) this.getSession()
				.getAttribute(Constant.SESSION_STORAGE);
		String account = sessionStorage.get("F_ACCOUNT");
		String oldpassword = this.getPara("oldpassword", "");
		String newpassword = this.getPara("newpassword", "");
		Record rec = Db.findFirst("SELECT COUNT(1) AS NUM FROM SYS_USER WHERE F_ACCOUNT = ? AND F_PASSWORD = ?",
				account, oldpassword);
		String num = rec.getStr("NUM");
		int intnum = Integer.parseInt(num);
		Map<String, Object> map = new HashMap<String, Object>();
		if (intnum > 0) {
			Db.update("UPDATE SYS_USER SET F_PASSWORD = ? WHERE F_ACCOUNT = ?", newpassword, account);
			map.put("code", "0");
			map.put("message", "修改成功!");
		} else {
			map.put("code", "1");
			map.put("message", "修改失败，旧密码不正确！");
		}
		this.renderJson(map);
	}
}
