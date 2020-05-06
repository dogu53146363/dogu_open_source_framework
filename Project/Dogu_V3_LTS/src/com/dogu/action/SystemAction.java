package com.dogu.action;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.quartz.Job;

import com.dogu.authorization.Authorization;
import com.dogu.constants.Constant;
import com.dogu.timmer.QuartzManager;
import com.dogu.utils.CommonUtils;
import com.dogu.utils.GetConFromDB;
import com.dogu.utils.GetMechineInfo;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

/**
 * 系统Action 包括所有的系统设置菜单
 * 
 * @author Dogu
 */
public class SystemAction extends Controller {

	/**
	 * 登录后获取菜单
	 */
	public void getMenuInfo() {
		
		String LimitType = GetConFromDB.GetCIFromDB("LIMIT_TYPE");
		Map<String, Object> returnMap = new HashMap<String, Object>();
		if (Constant.CAPITAL_STR_ORG.equals(LimitType)) {
			String account = this.getSessionAttr("F_ACCOUNT");
			String orgId = this.getSessionAttr("F_ORG");
			ArrayList<Object> returnList = new ArrayList<>();
			Map<String, Object> menuDataMap = null;
			List<Record> parentList = Db.find("SELECT DISTINCT(result.F_MENUID),result.F_MENUNAME,result.F_ICON FROM"
					+ " (SELECT MENU_DCT.F_MENUID,MENU_DCT.F_MENUNAME,MENU_DCT.F_ICON FROM SYS_MENU_LIMIT menu_limit,"
					+ "SYS_MENU_DCT MENU_DCT WHERE MENU_DCT.F_MENUID = menu_limit.F_MENUID AND MENU_DCT.F_JS = ?"
					+ " AND MENU_DCT.F_USE = ? AND MENU_DCT.F_SHOW = ?"
					+ " AND ((menu_limit.F_ACCOUNT = ? AND menu_limit.F_TYPE = ? ) OR (menu_limit.F_ACCOUNT = ?"
					+ " AND menu_limit.F_TYPE = ? )))" + " result ORDER BY F_MENUID", Constant.STR_ONE,
					Constant.CAPITAL_STR_Y, Constant.CAPITAL_STR_Y, account, Constant.CAPITAL_STR_USER, orgId,
					Constant.CAPITAL_STR_ORG);
			List<Record> childList = null;
			for (Record rec : parentList) {
				menuDataMap = new HashMap<String, Object>();
				menuDataMap.put("title", rec.get("F_MENUNAME"));
				menuDataMap.put("icon", rec.get("F_ICON"));
				menuDataMap.put("spread", false);
				childList = Db.find(
						"SELECT DISTINCT(result.F_MENUID),result.F_MENUNAME AS title,result.F_PATH AS href,F_ICON AS icon FROM"
								+ " (SELECT MENU_DCT.F_MENUID,MENU_DCT.F_MENUNAME,MENU_DCT.F_PATH,MENU_DCT.F_ICON FROM SYS_MENU_LIMIT menu_limit,"
								+ "SYS_MENU_DCT MENU_DCT WHERE MENU_DCT.F_MENUID = menu_limit.F_MENUID AND MENU_DCT.F_JS = ?"
								+ " AND MENU_DCT.F_USE = ? AND MENU_DCT.F_SHOW = ? AND menu_limit.F_MENUID LIKE ? "
								+ " AND ((menu_limit.F_ACCOUNT = ? AND menu_limit.F_TYPE = ? ) OR (menu_limit.F_ACCOUNT = ?"
								+ " AND menu_limit.F_TYPE = ? )))" + " result ORDER BY F_MENUID",
						Constant.STR_TWO, Constant.CAPITAL_STR_Y, Constant.CAPITAL_STR_Y, rec.get("F_MENUID") + "%",
						account, Constant.CAPITAL_STR_USER, orgId, Constant.CAPITAL_STR_ORG);
				menuDataMap.put("children", childList);
				returnList.add(menuDataMap);
			}
			returnMap.put("MENU_DATA", returnList);
			returnMap.put("USER_NAME", this.getSessionAttr("F_USERNAME"));
		} else if (Constant.CAPITAL_STR_ROLE.equals(LimitType)) {
			String account = this.getSessionAttr("F_ACCOUNT");
			String role = this.getSessionAttr("F_ROLE");
			ArrayList<Object> returnList = new ArrayList<>();
			Map<String, Object> menuDataMap = null;
			List<Record> parentList = Db.find("SELECT DISTINCT(result.F_MENUID),result.F_MENUNAME,result.F_ICON FROM"
					+ " (SELECT MENU_DCT.F_MENUID,MENU_DCT.F_MENUNAME,MENU_DCT.F_ICON FROM SYS_MENU_LIMIT menu_limit,"
					+ "SYS_MENU_DCT MENU_DCT WHERE MENU_DCT.F_MENUID = menu_limit.F_MENUID AND MENU_DCT.F_JS = ?"
					+ " AND MENU_DCT.F_USE = ? AND MENU_DCT.F_SHOW = ? AND"
					+ " ((menu_limit.F_ACCOUNT = ? AND menu_limit.F_TYPE = ? ) OR (menu_limit.F_ACCOUNT = ?"
					+ " AND menu_limit.F_TYPE = ? )))" + " result ORDER BY F_MENUID", Constant.STR_ONE,
					Constant.CAPITAL_STR_Y, Constant.CAPITAL_STR_Y, account, Constant.CAPITAL_STR_USER, role,
					Constant.CAPITAL_STR_ROLE);
			List<Record> childList = null;
			for (Record rec : parentList) {
				menuDataMap = new HashMap<String, Object>();
				menuDataMap.put("title", rec.get("F_MENUNAME"));
				menuDataMap.put("icon", rec.get("F_ICON"));
				menuDataMap.put("spread", false);
				childList = Db.find(
						"SELECT DISTINCT(result.F_MENUID),result.F_MENUNAME AS title,result.F_PATH AS href,F_ICON AS icon FROM"
								+ " (SELECT MENU_DCT.F_MENUID,MENU_DCT.F_MENUNAME,MENU_DCT.F_PATH,MENU_DCT.F_ICON FROM SYS_MENU_LIMIT menu_limit,"
								+ "SYS_MENU_DCT MENU_DCT WHERE MENU_DCT.F_MENUID = menu_limit.F_MENUID AND MENU_DCT.F_JS = '"
								+ Constant.STR_TWO + "' AND MENU_DCT.F_USE = '" + Constant.CAPITAL_STR_Y
								+ "' AND MENU_DCT.F_SHOW = '" + Constant.CAPITAL_STR_Y
								+ "' AND menu_limit.F_MENUID LIKE ? "
								+ " AND ((menu_limit.F_ACCOUNT = ? AND menu_limit.F_TYPE = ? ) OR (menu_limit.F_ACCOUNT = ?"
								+ " AND menu_limit.F_TYPE = ? )))" + " result ORDER BY F_MENUID",
						rec.get("F_MENUID") + "%", account, Constant.CAPITAL_STR_USER, role, Constant.CAPITAL_STR_ROLE);
				menuDataMap.put("children", childList);
				returnList.add(menuDataMap);
			}
			returnMap.put("MENU_DATA", returnList);
			returnMap.put("USER_NAME", this.getSessionAttr("F_USERNAME"));
		} else {
			String account = this.getSessionAttr("F_ACCOUNT");
			String role = this.getSessionAttr("F_ROLE");
			String orgId = this.getSessionAttr("F_ORG");
			ArrayList<Object> returnList = new ArrayList<>();
			Map<String, Object> menuDataMap = null;
			List<Record> parentList = Db.find("SELECT DISTINCT(result.F_MENUID),result.F_MENUNAME,result.F_ICON FROM"
					+ " (SELECT MENU_DCT.F_MENUID,MENU_DCT.F_MENUNAME,MENU_DCT.F_ICON FROM SYS_MENU_LIMIT menu_limit,"
					+ "SYS_MENU_DCT MENU_DCT WHERE MENU_DCT.F_MENUID = menu_limit.F_MENUID AND MENU_DCT.F_JS = ?"
					+ " AND MENU_DCT.F_USE = ? AND MENU_DCT.F_SHOW = ? AND ((menu_limit.F_ACCOUNT = ? AND menu_limit.F_TYPE = ? ) OR"
					+ " (menu_limit.F_ACCOUNT = ? AND menu_limit.F_TYPE = ? ) OR"
					+ " (menu_limit.F_ACCOUNT = ? AND menu_limit.F_TYPE = ? )))" + " result ORDER BY F_MENUID",
					Constant.STR_ONE, Constant.CAPITAL_STR_Y, Constant.CAPITAL_STR_Y, account,
					Constant.CAPITAL_STR_USER, role, Constant.CAPITAL_STR_ROLE, orgId, Constant.CAPITAL_STR_ORG);
			List<Record> childList = null;
			for (Record rec : parentList) {
				menuDataMap = new HashMap<String, Object>();
				menuDataMap.put("title", rec.get("F_MENUNAME"));
				menuDataMap.put("icon", rec.get("F_ICON"));
				menuDataMap.put("spread", false);
				childList = Db.find(
						"SELECT DISTINCT(result.F_MENUID),result.F_MENUNAME AS title,result.F_PATH AS href,F_ICON AS icon FROM"
								+ " (SELECT MENU_DCT.F_MENUID,MENU_DCT.F_MENUNAME,MENU_DCT.F_PATH,MENU_DCT.F_ICON FROM SYS_MENU_LIMIT menu_limit,"
								+ "SYS_MENU_DCT MENU_DCT WHERE MENU_DCT.F_MENUID = menu_limit.F_MENUID AND MENU_DCT.F_JS = ?"
								+ " AND MENU_DCT.F_USE = ? AND MENU_DCT.F_SHOW = ? AND menu_limit.F_MENUID LIKE ? "
								+ " AND ((menu_limit.F_ACCOUNT = ? AND menu_limit.F_TYPE = ? ) OR"
								+ " (menu_limit.F_ACCOUNT= ? AND menu_limit.F_TYPE = ? ) OR"
								+ " (menu_limit.F_ACCOUNT = ? AND menu_limit.F_TYPE = ? )))"
								+ " result ORDER BY F_MENUID",
						Constant.STR_TWO, Constant.CAPITAL_STR_Y, Constant.CAPITAL_STR_Y, rec.get("F_MENUID") + "%",
						account, Constant.CAPITAL_STR_USER, role, Constant.CAPITAL_STR_ROLE, orgId,
						Constant.CAPITAL_STR_ORG);
				menuDataMap.put("children", childList);
				returnList.add(menuDataMap);
			}
			returnMap.put("MENU_DATA", returnList);
			returnMap.put("USER_NAME", this.getSessionAttr("F_USERNAME"));
		}
		InetAddress InetAddress = null;
		try {
			InetAddress = GetMechineInfo.getInetAddress();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String IP = InetAddress.getHostAddress();
		String Mac = null;
		try {
			Mac = GetMechineInfo.getMacAddress(InetAddress);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String Head = "未授权，请将【";
		String And = "】和【";
		String End = "】发送至邮箱：lzq_jn@qq.com来获取授权！";
		if (Authorization.authorization()) {
			returnMap.put("AT_MESSAGE", "");
		} else {
			returnMap.put("AT_MESSAGE", Head + IP + And + Mac + End);
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
	 * 用户管理-获取角色字典
	 */
	public void userManegeGetRoleInfo() {
		String use = this.getPara("use");
		List<Record> list = Db.find(
				"SELECT F_ROLENUM,F_ROLENAME,F_USE FROM SYS_ROLE_DCT " + "WHERE F_USE LIKE ? ORDER BY F_ROLENUM",
				"%" + use + "%");
		this.renderJson(list);
	}

	/**
	 * 用户管理-获取组织机构字典
	 */
	public void userManegeGetOrgInfo() {
		String use = this.getPara("use");
		List<Record> list = Db.find(
				"SELECT F_ORG_ID,F_ORG_NAME FROM SYS_ORGANIZATION_DCT " + "WHERE F_USE LIKE ? ORDER BY F_ORG_ID",
				"%" + use + "%");
		this.renderJson(list);
	}

	/**
	 * 获取用户信息
	 */
	public void getUserInfo() {
		// 计算页数
		String limit = this.getPara("limit");
		String pageNum = this.getPara("page");
		int intLimit = Integer.valueOf(limit);
		int intPageNum = Integer.valueOf(pageNum);
		int startRow = (intPageNum - 1) * intLimit;// 从0开始
		int endRow = startRow + intLimit;// 不包含

		String account = this.getPara("account");
		String rolenum = this.getPara("rolenum");
		String username = this.getPara("username");
		try {
			username = URLDecoder.decode(username, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		String orgname = this.getPara("orgname");
		try {
			orgname = URLDecoder.decode(orgname, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		Record totalRc = Db.findFirst("SELECT COUNT(1) AS F_NUM FROM SYS_USER,SYS_ROLE_DCT,SYS_ORGANIZATION_DCT"
				+ " WHERE F_ROLENUM LIKE ? AND F_USERNAME LIKE ?" + " AND F_ACCOUNT LIKE ? AND SYS_USER.F_ROLE ="
				+ " SYS_ROLE_DCT.F_ROLENUM AND SYS_USER.F_ORG = SYS_ORGANIZATION_DCT.F_ORG_ID "
				+ " AND F_ORG_NAME LIKE ? ", "%" + rolenum + "%", "%" + username + "%", "%" + account + "%",
				"%" + orgname + "%");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code", "0");
		map.put("msg", "");
		int totalCount = 0;
		String strTotalCount = "0";
		List<Map<String, Object>> dataList = new ArrayList<>();
		Map<String, Object> dataMap = new HashMap<String, Object>();
		strTotalCount = totalRc.getStr("F_NUM");
		totalCount = Integer.valueOf(strTotalCount);
		map.put("count", totalCount);
		List<Record> list = Db.find(
				"SELECT F_ACCOUNT,F_USERNAME,F_ORG_NAME,F_ROLENAME,"
						+ "F_SEX,F_TEL,F_ADDRESS,F_E_MAIL,F_QQ FROM SYS_USER,SYS_ROLE_DCT,SYS_ORGANIZATION_DCT"
						+ " WHERE F_ROLENUM LIKE ? AND F_USERNAME LIKE ? AND F_ACCOUNT LIKE ?"
						+ " AND SYS_USER.F_ROLE = SYS_ROLE_DCT.F_ROLENUM"
						+ " AND SYS_USER.F_ORG = SYS_ORGANIZATION_DCT.F_ORG_ID AND F_ORG_NAME LIKE ?"
						+ " ORDER BY F_ORG,F_ROLE,F_ACCOUNT",
				"%" + rolenum + "%", "%" + username + "%", "%" + account + "%", "%" + orgname + "%");
		Record rc = null;
		if (endRow <= totalCount) {
			for (int i = startRow; i < endRow; i++) {
				dataMap = new HashMap<String, Object>();
				rc = list.get(i);
				dataMap.put("id", i + 1);
				dataMap.put("f_account", rc.getStr("F_ACCOUNT"));
				dataMap.put("f_username", rc.getStr("F_USERNAME"));
				dataMap.put("f_rolename", rc.getStr("F_ROLENAME"));
				dataMap.put("f_org_name", rc.getStr("F_ORG_NAME"));
				dataMap.put("f_sex", rc.getStr("F_SEX"));
				dataMap.put("f_tel", rc.getStr("F_TEL"));
				dataMap.put("f_e_mail", rc.getStr("F_E_MAIL"));
				dataMap.put("f_qq", rc.getStr("F_QQ"));
				dataMap.put("f_address", rc.getStr("F_ADDRESS"));
				dataList.add(dataMap);
			}
		} else {
			for (int i = startRow; i < totalCount; i++) {
				dataMap = new HashMap<String, Object>();
				rc = list.get(i);
				dataMap.put("id", i + 1);
				dataMap.put("f_account", rc.getStr("F_ACCOUNT"));
				dataMap.put("f_username", rc.getStr("F_USERNAME"));
				dataMap.put("f_rolename", rc.getStr("F_ROLENAME"));
				dataMap.put("f_org_name", rc.getStr("F_ORG_NAME"));
				dataMap.put("f_sex", rc.getStr("F_SEX"));
				dataMap.put("f_tel", rc.getStr("F_TEL"));
				dataMap.put("f_e_mail", rc.getStr("F_E_MAIL"));
				dataMap.put("f_qq", rc.getStr("F_QQ"));
				dataMap.put("f_address", rc.getStr("F_ADDRESS"));
				dataList.add(dataMap);
			}
		}
		map.put("data", dataList);
		this.renderJson(map);
	}

	/**
	 * 重置密码
	 */
	public void restPassword() {
		String account = this.getPara("account");
		String defaultpsw = Constant.DEFAULT_PSW;
		Map<String, Object> map = new HashMap<String, Object>();
		Db.update("UPDATE SYS_USER SET F_PASSWORD = ? WHERE F_ACCOUNT = ?", defaultpsw, account);
		map.put("message", "操作成功!");
		this.renderJson(map);
	}

	/**
	 * 删除用户
	 */
	public void deleteUser() {
		String account = this.getPara("account");
		String[] accountLs = account.split(",");
		Map<String, Object> map = new HashMap<String, Object>();
		for (int i = 0; i < accountLs.length; i++) {
			Db.update("DELETE FROM SYS_USER WHERE F_ACCOUNT = ?", accountLs[i]);
			Db.update("DELETE FROM SYS_MENU_LIMIT WHERE F_ACCOUNT = ?", accountLs[i]);
		}
		map.put("message", "操作成功!");
		this.renderJson(map);
	}

	/**
	 * 添加用户
	 */
	public void AddUser() {
		String account = this.getPara("account");
		String password = this.getPara("password");
		String role = this.getPara("role");
		String org = this.getPara("org");
		String name = this.getPara("name");
		try {
			name = URLDecoder.decode(name, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sex = this.getPara("sex");
		String tel = this.getPara("tel");
		String address = this.getPara("address");
		try {
			address = URLDecoder.decode(address, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		String email = this.getPara("email");
		String qq = this.getPara("qq");
		Map<String, Object> map = new HashMap<String, Object>();
		if (CommonUtils.isLetterDigit(account)) {
			Record rec = Db.findFirst("SELECT COUNT(1) AS NUM FROM SYS_USER WHERE F_ACCOUNT = ?", account);
			String num = rec.get("NUM").toString();
			int intnum = Integer.parseInt(num);
			if (intnum > 0) {
				map.put("code", "1");
				map.put("message", "该帐号已存在!");
			} else {
				Db.update(
						"INSERT INTO SYS_USER (F_ACCOUNT,F_PASSWORD,F_ROLE,F_ORG,F_USERNAME,F_SEX,F_TEL,F_ADDRESS,F_E_MAIL,F_QQ) VALUES (?,?,?,?,?,?,?,?,?,?)",
						account, password, role, org, name, sex, tel, address, email, qq);
				map.put("code", "0");
				map.put("message", "操作成功!");
			}
		} else {
			map.put("code", "1");
			map.put("message", "帐号只能为字母和数字！");
		}
		this.renderJson(map);
	}

	/**
	 * 改变用户信息的时候先查询用户信息
	 */
	public void getOneUserInfoBeforeChange() {
		String account = this.getPara("account");
		Map<String, Object> map = new HashMap<String, Object>();
		Record rec = Db.findFirst(
				"SELECT F_ACCOUNT,F_PASSWORD,F_ROLE,F_ORG,F_USERNAME,F_SEX,F_TEL,F_ADDRESS,F_E_MAIL,F_QQ FROM SYS_USER WHERE F_ACCOUNT = ?",
				account);
		if (rec == null) {
			map.put("code", "1");
			map.put("message", "未查询到相关用户信息，请刷新重试！");
		} else {
			map.put("account", rec.get("F_ACCOUNT").toString());
			map.put("password", rec.get("F_PASSWORD").toString());
			map.put("role", rec.get("F_ROLE").toString());
			map.put("org", rec.get("F_ORG").toString());
			map.put("name", rec.get("F_USERNAME").toString());
			map.put("sex", rec.get("F_SEX").toString());
			map.put("tel", rec.get("F_TEL").toString());
			map.put("address", rec.get("F_ADDRESS").toString());
			map.put("email", rec.get("F_E_MAIL").toString());
			map.put("qq", rec.get("F_QQ").toString());
			map.put("code", "0");
		}
		this.renderJson(map);
	}

	/**
	 * 更改用户信息
	 */
	public void ChangeUserInfo() {
		String account = this.getPara("account");
		String password = this.getPara("password");
		String role = this.getPara("role");
		String org = this.getPara("org");
		String name = this.getPara("name");
		try {
			name = URLDecoder.decode(name, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sex = this.getPara("sex");
		String tel = this.getPara("tel");
		String address = this.getPara("address");
		try {
			address = URLDecoder.decode(address, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		String email = this.getPara("email");
		String qq = this.getPara("qq");
		Map<String, Object> map = new HashMap<String, Object>();
		Db.update(
				"UPDATE SYS_USER SET F_PASSWORD = ?,F_ROLE = ?,F_ORG = ?,F_USERNAME = ?,F_SEX = ?,F_TEL = ?,F_ADDRESS = ?,F_E_MAIL = ?,F_QQ = ? WHERE F_ACCOUNT = '"
						+ account + "'",
				password, role, org, name, sex, tel, address, email, qq);
		map.put("message", "操作成功!");
		this.renderJson(map);
	}

	/**
	 * 获取菜单信息的ztree
	 */
	public void QueryMenuTree() {
		// 获取一级菜单
		String backstr = "[{id:1, pId:-1, name:\"菜单管理\" ,menuid : \"MENU\", \"icon\":" + Constant.MENU_TREE_P_ICON_PATH
				+ "},";
		List<Record> list = Db.find(
				"SELECT F_MENUID,F_MENUNAME FROM SYS_MENU_DCT WHERE F_JS = ?" + " ORDER BY F_MENUID", Constant.STR_ONE);
		for (Record rec : list) {
			backstr += "{id:" + rec.getStr("F_MENUID") + ", pId:1, name:\"" + rec.getStr("F_MENUNAME") + "\", menuid:\""
					+ rec.getStr("F_MENUID") + "\", \"icon\":" + Constant.MENU_TREE_P_ICON_PATH + "},";
		}
		// 获取第二级菜单的pid
		List<Record> listgetdpid = Db.find("SELECT F_MENUID FROM SYS_MENU_DCT WHERE F_JS = ?" + " ORDER BY F_MENUID",
				Constant.STR_ONE);
		List<String> listsavepid = new ArrayList<String>();
		for (Record rec1 : listgetdpid) {
			listsavepid.add(rec1.getStr("F_MENUID"));
		}
		// 生成二级菜单
		for (int i = 0; i < listsavepid.size(); i++) {
			List<Record> secondlist = Db.find("SELECT F_MENUID,F_MENUNAME FROM SYS_MENU_DCT WHERE F_JS = ?"
					+ " AND F_MENUID LIKE ? ORDER BY F_MENUID", Constant.STR_TWO, listsavepid.get(i) + "%");
			for (Record rec : secondlist) {
				backstr += "{id:" + rec.getStr("F_MENUID") + ", pId:" + listsavepid.get(i) + ", name:\""
						+ rec.getStr("F_MENUNAME") + "\", menuid:\"" + rec.getStr("F_MENUID") + "\"," + " \"icon\":"
						+ Constant.MENU_TREE_C_ICON_PATH + "},";
			}
		}
		backstr += "]";
		this.renderJson(backstr);
	}

	/**
	 * 新增菜单中的保存菜单action
	 */
	public void AddMenu() {
		String menuid = this.getPara("menuid");
		String menuname = this.getPara("menuname");
		String pagepath = this.getPara("pagepath");
		String menujb = this.getPara("menujb");
		String use = this.getPara("ifuse");
		String show = this.getPara("ifshow");
		String intercept = this.getPara("ifintercept");
		String icon = this.getPara("icon");
		try {
			menuname = URLDecoder.decode(menuname, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		Map<String, Object> map = new HashMap<String, Object>();
		if (CommonUtils.isNumber(menuid)) {
			Record rec = Db.findFirst("SELECT COUNT(1) AS NUM FROM SYS_MENU_DCT WHERE F_MENUID = ?", menuid);
			String num = rec.get("NUM").toString();
			int intnum = Integer.parseInt(num);
			if (intnum > 0) {
				map.put("code", "1");
				map.put("message", "已经存在的菜单编号!");
			} else {
				Db.update(
						"INSERT INTO SYS_MENU_DCT (F_MENUID,F_MENUNAME,F_JS,F_PATH,F_ICON,F_USE,F_SHOW,F_INTERCEPT) VALUES (?,?,?,?,?,?,?,?)",
						menuid, menuname, menujb, pagepath, icon, use, show, intercept);
				map.put("code", "0");
				map.put("message", "操作成功!");
			}
		} else {
			map.put("code", "1");
			map.put("message", "菜单编号只能为数字!");
		}
		this.renderJson(map);
	}

	/**
	 * 获取左边tree选中的菜单信息
	 */
	public void getOneMenuInfo() {
		String menuid = this.getPara("menuid");
		Map<String, Object> map = new HashMap<String, Object>();
		Record rec = Db.findFirst(
				"SELECT F_MENUID,F_MENUNAME,F_JS,F_PATH,F_ICON,F_USE,F_SHOW,F_INTERCEPT FROM SYS_MENU_DCT WHERE F_MENUID = ?",
				menuid);
		if (null != rec) {
			map.put("menu", rec.get("F_MENUID").toString());
			map.put("menuname", rec.get("F_MENUNAME").toString());
			if (null == rec.get("F_PATH")) {
				map.put("pagepath", "");
			} else {
				map.put("pagepath", rec.get("F_PATH").toString());
			}
			map.put("menujb", rec.get("F_JS").toString());
			map.put("use", rec.get("F_USE").toString());
			map.put("show", rec.get("F_SHOW").toString());
			map.put("intercept", rec.get("F_INTERCEPT").toString());
			if (null == rec.get("F_ICON")) {
				map.put("icon", "");
			} else {
				map.put("icon", rec.get("F_ICON").toString());
			}
			map.put("code", "0");
		} else {
			map.put("code", "1");
			map.put("message", "无相应的菜单信息请删除后重新添加！");
		}
		this.renderJson(map);
	}

	/**
	 * 修改菜单内容
	 */
	public void UpdateMenu() {
		String menuid = this.getPara("menuid");
		String oldmenuid = this.getPara("oldmenuid");
		String menuname = this.getPara("menuname");
		String pagepath = this.getPara("pagepath");
		String menujb = this.getPara("menujb");
		String icon = this.getPara("icon");
		String use = this.getPara("ifuse");
		String show = this.getPara("ifshow");
		String intercept = this.getPara("ifintercept");
		try {
			menuname = URLDecoder.decode(menuname, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		Map<String, Object> map = new HashMap<String, Object>();
		// 判断如果菜单编号没改变，只改变的下面的内容则进oldmenuid.equals(menuid)
		if (CommonUtils.isNumber(menuid)) {
			if (oldmenuid.equals(menuid)) {
				Db.update(
						"UPDATE SYS_MENU_DCT SET F_MENUNAME = ?,F_JS = ?,F_PATH = ?,F_ICON = ?,F_USE = ?,F_SHOW = ?,F_INTERCEPT = ?  WHERE F_MENUID = '"
								+ menuid + "'",
						menuname, menujb, pagepath, icon, use, show, intercept);
				map.put("code", "0");
				map.put("message", "操作成功!");
			} else {
				// 如果菜单编号改变了则先查询，改变了的菜单编号是否在库中唯一，如果不是则提示
				String sql = "SELECT COUNT(1) AS NUM FROM SYS_MENU_DCT WHERE F_MENUID = '" + menuid + "'";
				Record rec = Db.findFirst(sql);
				String num = rec.get("NUM").toString();
				int intnum = Integer.parseInt(num);
				if (intnum > 0) {
					map.put("code", "1");
					map.put("message", "已经存在的菜单编号!");
				} else {
					Db.update(
							"UPDATE SYS_MENU_DCT SET F_MENUID = ?,F_MENUNAME = ?,F_JS = ?,F_PATH = ?,F_ICON = ?,F_USE = ?,F_SHOW = ?,F_INTERCEPT = ? WHERE F_MENUID = '"
									+ oldmenuid + "'",
							menuid, menuname, menujb, pagepath, icon, use, show, intercept);
					Db.update("UPDATE SYS_MENU_LIMIT SET F_MENUID = ? WHERE F_MENUID = ? ", menuid, oldmenuid);
					map.put("code", "0");
					map.put("message", "操作成功!");
				}
			}
		} else {
			map.put("code", "1");
			map.put("message", "菜单编号只能为数字!");
		}
		this.renderJson(map);
	}

	/**
	 * 删除菜单
	 */
	public void DeleteMenu() {
		Map<String, Object> map = new HashMap<String, Object>();
		String menuid = this.getPara("menuid");
		String[] menuidArray = menuid.split(",");
		for (int i = 0; i < menuidArray.length; i++) {
			Db.update("DELETE FROM SYS_MENU_DCT WHERE F_MENUID LIKE ?", menuidArray[i] + "%");
			Db.update("DELETE FROM SYS_MENU_LIMIT WHERE F_MENUID LIKE ?", menuidArray[i] + "%");
		}
		map.put("message", "操作成功!");
		this.renderJson(map);
	}

	/**
	 * 获取角色字典
	 */
	public void getRoleInfo() {
		// 计算页数
		String limit = this.getPara("limit");
		String pageNum = this.getPara("page");
		int intLimit = Integer.valueOf(limit);
		int intPageNum = Integer.valueOf(pageNum);
		int startRow = (intPageNum - 1) * intLimit;// 从0开始
		int endRow = startRow + intLimit;// 不包含

		String use = this.getPara("use");
		Record totalRc = Db.findFirst("SELECT COUNT(1) AS F_NUM FROM" + " SYS_ROLE_DCT WHERE F_USE LIKE ? ",
				"%" + use + "%");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code", "0");
		map.put("msg", "");
		int totalCount = 0;
		String strTotalCount = "0";
		List<Map<String, Object>> dataList = new ArrayList<>();
		Map<String, Object> dataMap = new HashMap<String, Object>();
		strTotalCount = totalRc.getStr("F_NUM");
		totalCount = Integer.valueOf(strTotalCount);
		map.put("count", totalCount);
		List<Record> list = Db.find(
				"SELECT F_ROLENUM,F_ROLENAME,F_USE FROM " + " SYS_ROLE_DCT WHERE F_USE LIKE ? ORDER BY F_ROLENUM",
				"%" + use + "%");
		Record rc = null;
		if (endRow <= totalCount) {
			for (int i = startRow; i < endRow; i++) {
				dataMap = new HashMap<String, Object>();
				rc = list.get(i);
				dataMap.put("id", i + 1);
				dataMap.put("f_rolenum", rc.getStr("F_ROLENUM"));
				dataMap.put("f_rolename", rc.getStr("F_ROLENAME"));
				dataMap.put("f_use", rc.getStr("F_USE"));
				dataList.add(dataMap);
			}
		} else {
			for (int i = startRow; i < totalCount; i++) {
				dataMap = new HashMap<String, Object>();
				rc = list.get(i);
				dataMap.put("id", i + 1);
				dataMap.put("f_rolenum", rc.getStr("F_ROLENUM"));
				dataMap.put("f_rolename", rc.getStr("F_ROLENAME"));
				dataMap.put("f_use", rc.getStr("F_USE"));
				dataList.add(dataMap);
			}
		}
		map.put("data", dataList);
		this.renderJson(map);
	}

	/**
	 * 添加角色
	 */
	public void AddRole() {
		String roleId = this.getPara("roleid");
		String roleName = this.getPara("rolename");
		String use = this.getPara("use");
		try {
			roleName = URLDecoder.decode(roleName, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		Map<String, Object> map = new HashMap<String, Object>();
		if (CommonUtils.isLetterDigit(roleId)) {
			Record rec = Db.findFirst("SELECT COUNT(1) AS NUM FROM SYS_ROLE_DCT WHERE F_ROLENUM = ?", roleId);
			String num = rec.get("NUM").toString();
			int intnum = Integer.parseInt(num);
			if (intnum > 0) {
				map.put("code", "1");
				map.put("message", "该角色已存在!");
			} else {
				Db.update("INSERT INTO SYS_ROLE_DCT (F_ROLENUM,F_ROLENAME,F_USE) VALUES (?,?,?)", roleId, roleName,
						use);
				map.put("code", "0");
				map.put("message", "操作成功!");
			}
		} else {
			map.put("code", "1");
			map.put("message", "角色编号只能为字母和数字!");
		}
		this.renderJson(map);
	}

	/**
	 * 修改角色的查询单个角色信息
	 */
	public void getOneRoleInfo() {
		String roleid = this.getPara("roleid");
		Map<String, Object> map = new HashMap<String, Object>();
		Record rec = Db.findFirst("SELECT F_ROLENUM,F_ROLENAME,F_USE FROM SYS_ROLE_DCT WHERE F_ROLENUM = ?", roleid);
		if (null != rec) {
			map.put("roleid", rec.get("F_ROLENUM").toString());
			map.put("rolename", rec.get("F_ROLENAME").toString());
			map.put("use", rec.get("F_USE").toString());
			map.put("code", "0");
		} else {
			map.put("code", "1");
			map.put("message", "不存在的角色信息，请删除后重新添加！");
		}
		this.renderJson(map);
	}

	/**
	 * 修改角色
	 */
	public void UpdateRole() {
		String roleid = this.getPara("roleid");
		String rolename = this.getPara("rolename");
		String use = this.getPara("use");
		try {
			rolename = URLDecoder.decode(rolename, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		Map<String, Object> map = new HashMap<String, Object>();
		Db.update("UPDATE SYS_ROLE_DCT SET F_ROLENAME = ?,F_USE = ? WHERE F_ROLENUM = ?", rolename, use, roleid);
		map.put("message", "操作成功!");
		this.renderJson(map);
	}

	/**
	 * 删除角色
	 */
	public void DeleteRole() {
		String roleid = this.getPara("roleid");
		String roleLs[] = roleid.split(",");
		Map<String, Object> map = new HashMap<String, Object>();
		for (int i = 0; i < roleLs.length; i++) {
			Db.update("DELETE FROM SYS_ROLE_DCT WHERE F_ROLENUM = ?", roleLs[i]);
			Db.update("DELETE FROM SYS_MENU_LIMIT WHERE F_ACCOUNT = ?", roleLs[i]);
		}
		map.put("message", "操作成功!");
		this.renderJson(map);
	}

	/**
	 * 用户权限字典获取用户信息
	 */
	public void LimitgetUserInfo() {
		String account = this.getPara("account");
		String username = this.getPara("username");
		List<Record> list = Db.find(
				"SELECT F_ACCOUNT,F_USERNAME FROM SYS_USER"
						+ " WHERE F_ACCOUNT LIKE ? AND F_USERNAME LIKE ? ORDER BY F_ACCOUNT",
				"%" + account + "%", "%" + username + "%");
		this.renderJson(list);
	}

	/**
	 * 用户/角色/组织机构行权限获取菜单树
	 */
	public void LimitgetMenuInfo() {
		// 生成一级树
		String backstr = "[";
		List<Record> list = Db.find(
				"SELECT F_MENUID,F_MENUNAME FROM SYS_MENU_DCT WHERE F_JS = ?" + " AND F_USE = ? ORDER BY F_MENUID",
				Constant.STR_ONE, Constant.CAPITAL_STR_Y);
		for (Record rec : list) {
			backstr += "{id:" + rec.getStr("F_MENUID") + ", pId:-1, name:\"" + rec.getStr("F_MENUNAME")
					+ "\", menuid:\"" + rec.getStr("F_MENUID") + "\", \"icon\":" + Constant.MENU_TREE_P_ICON_PATH
					+ "},";
		}
		// 获取第二级树的pid
		List<Record> listgetdpid = Db.find(
				"SELECT F_MENUID FROM SYS_MENU_DCT WHERE F_JS = ?" + " AND F_USE = ? ORDER BY F_MENUID",
				Constant.STR_ONE, Constant.CAPITAL_STR_Y);
		List<String> listsavepid = new ArrayList<String>();
		for (Record rec1 : listgetdpid) {
			listsavepid.add(rec1.getStr("F_MENUID"));
		}
		// 生成二级树
		for (int i = 0; i < listsavepid.size(); i++) {
			List<Record> secondlist = Db.find(
					"SELECT F_MENUID,F_MENUNAME FROM SYS_MENU_DCT"
							+ " WHERE F_JS = ? AND F_USE = ?  AND F_MENUID LIKE ? ORDER BY F_MENUID",
					Constant.STR_TWO, Constant.CAPITAL_STR_Y, listsavepid.get(i) + "%");
			for (Record rec : secondlist) {
				backstr += "{id:" + rec.getStr("F_MENUID") + ", pId:" + listsavepid.get(i) + ", name:\""
						+ rec.getStr("F_MENUNAME") + "\", menuid:\"" + rec.getStr("F_MENUID") + "\", " + "\"icon\":"
						+ Constant.MENU_TREE_C_ICON_PATH + "},";
			}
		}
		backstr += "]";
		this.renderJson(backstr);
	}

	/**
	 * 获取选中的树节点
	 */
	public void getSelectTreeNode() {
		String account = this.getPara("account");
		String type = this.getPara("type");
		Map<String, Object> map = new HashMap<String, Object>();
		List<Record> list = Db.find(
				"SELECT F_MENUID FROM SYS_MENU_LIMIT WHERE F_ACCOUNT = ? AND F_TYPE = ? ORDER BY F_MENUID", account,
				type);
		map.put("list", list);
		this.renderJson(map);
	}

	/**
	 * 保存用户行权限
	 */
	public void SaveUserLimit() {
		Map<String, Object> map = new HashMap<String, Object>();
		String userid = this.getPara("userid");
		String menuid = this.getPara("menuid");
		String type = this.getPara("type");
		String[] menuidArray = menuid.split(",");
		Db.update("DELETE FROM SYS_MENU_LIMIT WHERE F_ACCOUNT = ? AND F_TYPE = ?", userid, type);
		if (!Constant.EMPTYSTR.equals(menuidArray[0])) {
			for (int i = 0; i < menuidArray.length; i++) {
				Db.update("INSERT INTO SYS_MENU_LIMIT (F_ACCOUNT,F_MENUID,F_TYPE) VALUES (?,?,?)", userid,
						menuidArray[i], type);
			}
		}
		map.put("message", "操作成功!");
		this.renderJson(map);
	}

	/**
	 * 角色行权限获取角色信息
	 */
	public void roleLimitGetInfo() {

		String rolename = this.getPara("rolename");
		try {
			rolename = URLDecoder.decode(rolename, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		List<Record> list = Db.find("SELECT F_ROLENUM,F_ROLENAME FROM SYS_ROLE_DCT" + " WHERE F_ROLENAME LIKE ? ",
				"%" + rolename + "%");
		this.renderJson(list);
	}

	/**
	 * 保存角色行权限
	 */
	public void SaveRoleLimit() {
		Map<String, Object> map = new HashMap<String, Object>();
		String roleid = this.getPara("roleid");
		String menuid = this.getPara("menuid");
		String type = this.getPara("type");
		String[] menuidArray = menuid.split(",");
		Db.update("DELETE FROM SYS_MENU_LIMIT WHERE F_ACCOUNT = ? AND F_TYPE = ?", roleid, type);
		if (!Constant.EMPTYSTR.equals(menuidArray[0])) {
			for (int i = 0; i < menuidArray.length; i++) {
				Db.update("INSERT INTO SYS_MENU_LIMIT (F_ACCOUNT,F_MENUID,F_TYPE) VALUES (?,?,?)", roleid,
						menuidArray[i], type);
			}
		}
		map.put("message", "操作成功!");
		this.renderJson(map);
	}

	/**
	 * 保存角色行权限
	 */
	public void SaveOrgLimit() {
		Map<String, Object> map = new HashMap<String, Object>();
		String orgid = this.getPara("orgid");
		String menuid = this.getPara("menuid");
		String type = this.getPara("type");
		String[] menuidArray = menuid.split(",");
		Db.update("DELETE FROM SYS_MENU_LIMIT WHERE F_ACCOUNT = ? AND F_TYPE = ?", orgid, type);
		if (!Constant.EMPTYSTR.equals(menuidArray[0])) {
			for (int i = 0; i < menuidArray.length; i++) {
				Db.update("INSERT INTO SYS_MENU_LIMIT (F_ACCOUNT,F_MENUID,F_TYPE) VALUES (?,?,?)", orgid,
						menuidArray[i], type);
			}
		}
		map.put("message", "操作成功!");
		this.renderJson(map);
	}

	/**
	 * 定时任务查询
	 */
	public void getTimmerInfo() {
		// 计算页数
		String limit = this.getPara("limit");
		String pageNum = this.getPara("page");
		int intLimit = Integer.valueOf(limit);
		int intPageNum = Integer.valueOf(pageNum);
		int startRow = (intPageNum - 1) * intLimit;// 从0开始
		int endRow = startRow + intLimit;// 不包含

		String zt = this.getPara("zt");

		Record totalRc = Db.findFirst("SELECT COUNT(1) AS F_NUM FROM SYS_TIMMER" + " WHERE F_STATUS LIKE ?",
				"%" + zt + "%");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code", "0");
		map.put("msg", "");
		int totalCount = 0;
		String strTotalCount = "0";
		List<Map<String, Object>> dataList = new ArrayList<>();
		Map<String, Object> dataMap = new HashMap<String, Object>();
		strTotalCount = totalRc.getStr("F_NUM");
		totalCount = Integer.valueOf(strTotalCount);
		map.put("count", totalCount);
		List<Record> list = Db.find("SELECT F_TIMMERID,F_STATUS,F_CLZ,F_CRON,F_NOTE FROM SYS_TIMMER"
				+ " WHERE F_STATUS LIKE ? ORDER BY F_TIMMERID ", "%" + zt + "%");
		Record rc = null;
		if (endRow <= totalCount) {
			for (int i = startRow; i < endRow; i++) {
				dataMap = new HashMap<String, Object>();
				rc = list.get(i);
				dataMap.put("id", i + 1);
				dataMap.put("f_timmerid", rc.getStr("F_TIMMERID"));
				dataMap.put("f_status", rc.getStr("F_STATUS"));
				dataMap.put("f_clz", rc.getStr("F_CLZ"));
				dataMap.put("f_cron", rc.getStr("F_CRON"));
				dataMap.put("f_note", rc.getStr("F_NOTE"));
				dataList.add(dataMap);
			}
		} else {
			for (int i = startRow; i < totalCount; i++) {
				dataMap = new HashMap<String, Object>();
				rc = list.get(i);
				dataMap.put("id", i + 1);
				dataMap.put("f_timmerid", rc.getStr("F_TIMMERID"));
				dataMap.put("f_status", rc.getStr("F_STATUS"));
				dataMap.put("f_clz", rc.getStr("F_CLZ"));
				dataMap.put("f_cron", rc.getStr("F_CRON"));
				dataMap.put("f_note", rc.getStr("F_NOTE"));
				dataList.add(dataMap);
			}
		}
		map.put("data", dataList);
		this.renderJson(map);
	}

	/**
	 * 获取单条定时任务信息
	 */
	public void getOneTimmerInfo() {
		Map<String, Object> map = new HashMap<String, Object>();
		String timmerId = this.getPara("timmerid");
		Record rec = Db.findFirst("SELECT F_TIMMERID,F_STATUS,F_CLZ,F_CRON,F_NOTE FROM SYS_TIMMER WHERE F_TIMMERID = ?",
				timmerId);
		if (rec == null) {
			map.put("code", "1");
			map.put("message", "未查询到该菜单信息，请刷新后重试！");
		} else {
			String f_timmerid = rec.get("F_TIMMERID").toString();
			String f_status = rec.get("F_STATUS").toString();
			String f_clz = rec.get("F_CLZ").toString();
			String f_cron = rec.get("F_CRON").toString();
			String f_note = rec.get("F_NOTE").toString();
			map.put("f_timmerid", f_timmerid);
			map.put("f_status", f_status);
			map.put("f_clz", f_clz);
			map.put("f_cron", f_cron);
			map.put("f_note", f_note);
			map.put("code", "0");
		}
		this.renderJson(map);
	}

	/**
	 * 定时任务启用
	 */
	public void StartTimmer() {
		Map<String, Object> map = new HashMap<String, Object>();
		String timmerid = this.getPara("timmerid");
		// 根据timmerid获取必要的参数
		Record rec = Db.findFirst("SELECT F_CLZ,F_CRON FROM SYS_TIMMER WHERE F_TIMMERID = ?", timmerid);
		String clazz = rec.get("F_CLZ").toString();
		String f_cron = rec.get("F_CRON").toString();
		String Identity = timmerid;

		Class<? extends Job> jobClazz = null;
		try {
			jobClazz = Class.forName(clazz).asSubclass(Job.class);
			QuartzManager qm = new QuartzManager();
			try {
				// 先remove一下,防止冲突
				qm.removeJob(timmerid, Identity);
				// 添加定时任务
				qm.addJob(timmerid, Identity, jobClazz, f_cron);
				Db.update("UPDATE SYS_TIMMER SET F_STATUS = ? WHERE F_TIMMERID = ?", Constant.CAPITAL_STR_Y, timmerid);
				map.put("code", "0");
				map.put("message", "操作成功!");
			} catch (Exception e) {
				map.put("code", "1");
				map.put("message", e.toString());
			}
		} catch (Exception e) {
			map.put("code", "1");
			map.put("message", "[" + e.toString() + "]");
		}
		this.renderJson(map);
	}

	/**
	 * 定时任务停用
	 */
	public void StopTimmer() {
		Map<String, Object> map = new HashMap<String, Object>();
		String timmerid = this.getPara("timmerid");
		String Identity = timmerid;
		QuartzManager qm = new QuartzManager();
		try {
			qm.removeJob(timmerid, Identity);
			Db.update("UPDATE SYS_TIMMER SET F_STATUS = ? WHERE F_TIMMERID = ?", Constant.CAPITAL_STR_N, timmerid);
			map.put("message", "操作成功!");
		} catch (Exception e) {
			map.put("message", e.toString());
		}
		this.renderJson(map);
	}

	/**
	 * 删除定时任务
	 */
	public void DeleteTimmer() {
		Map<String, Object> map = new HashMap<String, Object>();
		String timmerid = this.getPara("timmerid");
		String Identity = timmerid;
		String timmerLs[] = timmerid.split(",");
		QuartzManager qm = null;
		for (int i = 0; i < timmerLs.length; i++) {
			qm = new QuartzManager();
			try {
				qm.removeJob(timmerid, Identity);
				Db.update("DELETE FROM SYS_TIMMER WHERE F_TIMMERID = ?", timmerLs[i]);
				map.put("message", "操作成功!");
			} catch (Exception e) {
				map.put("message", e.toString());
			}
		}
		this.renderJson(map);
	}

	/**
	 * 添加定时任务
	 */
	public void AddTimmer() {
		String timmerid = this.getPara("timmerid");
		String timmerzt = this.getPara("timmerzt");
		String timmerclz = this.getPara("timmerclz");
		timmerclz = timmerclz.trim();
		String timmercron = this.getPara("timmercron");
		String timmernote = this.getPara("timmernote");
		String Identity = timmerid;

		Map<String, Object> map = new HashMap<String, Object>();
		if (CommonUtils.isLetterDigit(timmerid)) {
			try {
				timmernote = URLDecoder.decode(timmernote, "UTF-8");
				timmerclz = URLDecoder.decode(timmerclz, "UTF-8");
				timmercron = URLDecoder.decode(timmercron, "UTF-8");
			} catch (Exception e) {
				e.printStackTrace();
			}
			Record rec = Db.findFirst("SELECT COUNT(1) AS NUM FROM SYS_TIMMER WHERE F_TIMMERID = ? OR F_CLZ = ?",
					timmerid, timmerclz);
			String num = rec.get("NUM").toString();
			int intnum = Integer.parseInt(num);
			if (intnum > 0) {
				map.put("code", "1");
				map.put("message", "该定时器或执行的类已存在!");
			} else {
				if (Constant.CAPITAL_STR_Y.equals(timmerzt)) {
					Class<? extends Job> jobClazz = null;
					try {
						jobClazz = Class.forName(timmerclz).asSubclass(Job.class);
						QuartzManager qm = new QuartzManager();
						try {
							// 添加定时任务
							qm.addJob(timmerid, Identity, jobClazz, timmercron);
							Db.update(
									"INSERT INTO SYS_TIMMER (F_TIMMERID,F_STATUS,F_CLZ,F_CRON,F_NOTE) VALUES (?,?,?,?,?)",
									timmerid, timmerzt, timmerclz, timmercron, timmernote);
							map.put("code", "0");
							map.put("message", "操作成功!");
						} catch (Exception e) {
							map.put("code", "1");
							map.put("message", e.toString());
						}
					} catch (Exception e) {
						map.put("code", "1");
						map.put("message", "[" + e.toString() + "]");
					}
				} else {
					QuartzManager qm = new QuartzManager();
					qm.removeJob(timmerid, Identity);
					Db.update("INSERT INTO SYS_TIMMER (F_TIMMERID,F_STATUS,F_CLZ,F_CRON,F_NOTE) VALUES (?,?,?,?,?)",
							timmerid, timmerzt, timmerclz, timmercron, timmernote);
					map.put("code", "0");
					map.put("message", "操作成功!");
				}
			}
		} else {
			map.put("code", "1");
			map.put("message", "定时任务编号只能由数字和字母组成!");
		}
		this.renderJson(map);
	}

	/**
	 * 修改定时任务
	 */
	public void ModifyTimmer() {
		String timmerid = this.getPara("timmerid");
		String timmerzt = this.getPara("timmerzt");
		String timmerclz = this.getPara("timmerclz");
		timmerclz = timmerclz.trim();
		String timmercron = this.getPara("timmercron");
		String timmernote = this.getPara("timmernote");
		String Identity = timmerid;

		Map<String, Object> map = new HashMap<String, Object>();
		if (CommonUtils.isLetterDigit(timmerid)) {
			try {
				timmernote = URLDecoder.decode(timmernote, "UTF-8");
				timmerclz = URLDecoder.decode(timmerclz, "UTF-8");
				timmercron = URLDecoder.decode(timmercron, "UTF-8");
			} catch (Exception e) {
				e.printStackTrace();
			}
			Record rec = Db.findFirst("SELECT COUNT(1) AS NUM FROM SYS_TIMMER WHERE F_TIMMERID = ?", timmerid);
			String num = rec.get("NUM").toString();
			int intnum = Integer.parseInt(num);
			if (intnum <= 0) {
				map.put("code", "1");
				map.put("message", "该定时器不存在，清删除后重新添加!");
			} else {
				if (Constant.CAPITAL_STR_Y.equals(timmerzt)) {
					Class<? extends Job> jobClazz = null;
					try {
						jobClazz = Class.forName(timmerclz).asSubclass(Job.class);
						QuartzManager qm = new QuartzManager();
						try {
							// 先remove一下,防止冲突
							qm.removeJob(timmerid, Identity);
							Db.update(
									"UPDATE SYS_TIMMER SET F_STATUS = ?,F_CLZ = ?,F_CRON = ?,F_NOTE = ? WHERE F_TIMMERID = ?",
									timmerzt, timmerclz, timmercron, timmernote, timmerid);
							// 添加定时任务
							qm.addJob(timmerid, Identity, jobClazz, timmercron);
							map.put("code", "0");
							map.put("message", "操作成功!");
						} catch (Exception e) {
							map.put("code", "1");
							map.put("message", e.toString());
						}
					} catch (Exception e) {
						map.put("code", "1");
						map.put("message", "[" + e.toString() + "]");
					}
				} else {
					QuartzManager qm = new QuartzManager();
					qm.removeJob(timmerid, Identity);
					Db.update("UPDATE SYS_TIMMER SET F_STATUS = ?,F_CLZ = ?,F_CRON = ?,F_NOTE = ? WHERE F_TIMMERID = ?",
							timmerzt, timmerclz, timmercron, timmernote, timmerid);
					map.put("code", "0");
					map.put("message", "操作成功!");
				}
			}
		} else {
			map.put("code", "1");
			map.put("message", "定时任务编号只能由数字和字母组成!");
		}
		this.renderJson(map);
	}

	/**
	 * 获取配置设置信息
	 */
	public void getConfInfo() {
		// 计算页数
		String limit = this.getPara("limit");
		String pageNum = this.getPara("page");
		int intLimit = Integer.valueOf(limit);
		int intPageNum = Integer.valueOf(pageNum);
		int startRow = (intPageNum - 1) * intLimit;// 从0开始
		int endRow = startRow + intLimit;// 不包含

		String confDescription = this.getPara("confDescription");
		try {
			confDescription = URLDecoder.decode(confDescription, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}

		Record totalRc = Db.findFirst(
				"SELECT COUNT(1) AS F_NUM FROM SYS_CONFIG" + " WHERE F_KEY LIKE ? OR F_VALUE LIKE ? OR F_NOTE LIKE ?",
				"%" + confDescription + "%", "%" + confDescription + "%", "%" + confDescription + "%");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code", "0");
		map.put("msg", "");
		int totalCount = 0;
		String strTotalCount = "0";
		List<Map<String, Object>> dataList = new ArrayList<>();
		Map<String, Object> dataMap = new HashMap<String, Object>();
		strTotalCount = totalRc.getStr("F_NUM");
		totalCount = Integer.valueOf(strTotalCount);
		map.put("count", totalCount);
		List<Record> list = Db.find(
				"SELECT F_KEY,F_VALUE,F_NOTE FROM SYS_CONFIG" + " WHERE F_KEY LIKE ?"
						+ " OR F_VALUE LIKE ? OR F_NOTE LIKE ? ORDER BY F_KEY ",
				"%" + confDescription + "%", "%" + confDescription + "%", "%" + confDescription + "%");
		Record rc = null;
		if (endRow <= totalCount) {
			for (int i = startRow; i < endRow; i++) {
				dataMap = new HashMap<String, Object>();
				rc = list.get(i);
				dataMap.put("id", i + 1);
				dataMap.put("f_key", rc.getStr("F_KEY"));
				dataMap.put("f_value", rc.getStr("F_VALUE"));
				dataMap.put("f_note", rc.getStr("F_NOTE"));
				dataList.add(dataMap);
			}
		} else {
			for (int i = startRow; i < totalCount; i++) {
				dataMap = new HashMap<String, Object>();
				rc = list.get(i);
				dataMap.put("id", i + 1);
				dataMap.put("f_key", rc.getStr("F_KEY"));
				dataMap.put("f_value", rc.getStr("F_VALUE"));
				dataMap.put("f_note", rc.getStr("F_NOTE"));
				dataList.add(dataMap);
			}
		}
		map.put("data", dataList);
		this.renderJson(map);
	}

	/**
	 * 添加系统配置
	 */
	public void Addconf() {
		String confkey = this.getPara("confkey");
		String confvalue = this.getPara("confvalue");
		String confnote = this.getPara("confnote");
		try {
			confvalue = URLDecoder.decode(confvalue, "UTF-8");
			confnote = URLDecoder.decode(confnote, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		Map<String, Object> map = new HashMap<String, Object>();
		Record rec = Db.findFirst("SELECT COUNT(1) AS NUM FROM SYS_CONFIG WHERE F_KEY = ?", confkey);
		String num = rec.get("NUM").toString();
		int intnum = Integer.parseInt(num);
		if (intnum > 0) {
			map.put("code", "1");
			map.put("message", "该配置编号已存在!");
		} else {
			Db.update("INSERT INTO SYS_CONFIG (F_KEY,F_VALUE,F_NOTE) VALUES (?,?,?)", confkey, confvalue, confnote);
			map.put("code", "0");
			map.put("message", "操作成功!");
		}
		this.renderJson(map);
	}

	/**
	 * 修改配置信息前先获取一条配置信息的内容
	 */
	public void getOneConfInfo() {
		String confkey = this.getPara("confkey");
		Map<String, Object> map = new HashMap<String, Object>();
		Record rec = Db.findFirst("SELECT F_KEY,F_VALUE,F_NOTE FROM SYS_CONFIG WHERE F_KEY = ?", confkey);
		if (null != rec) {
			map.put("key", rec.get("F_KEY").toString());
			map.put("value", rec.get("F_VALUE").toString());
			map.put("note", rec.get("F_NOTE").toString());
			map.put("code", "0");
		} else {
			map.put("code", "1");
			map.put("message", "不存在的角色信息，请删除后重新添加！");
		}
		this.renderJson(map);
	}

	/**
	 * 更改配置信息
	 */
	public void UpdateConf() {
		String confkey = this.getPara("confkey");
		String confvalue = this.getPara("confvalue");
		String confnote = this.getPara("confnote");
		try {
			confvalue = URLDecoder.decode(confvalue, "UTF-8");
			confnote = URLDecoder.decode(confnote, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		Map<String, Object> map = new HashMap<String, Object>();
		Db.update("UPDATE SYS_CONFIG SET F_VALUE = ? ,F_NOTE = ? WHERE F_KEY = ? ", confvalue, confnote, confkey);
		map.put("message", "操作成功!");
		this.renderJson(map);
	}

	/**
	 * 删除配置信息
	 */
	public void Deleteconf() {
		String confkey = this.getPara("confkey");
		String confkeyLs[] = confkey.split(",");
		Map<String, Object> map = new HashMap<String, Object>();
		for (int i = 0; i < confkeyLs.length; i++) {
			Db.update("DELETE FROM SYS_CONFIG WHERE F_KEY = ?", confkeyLs[i]);
		}
		map.put("message", "操作成功!");
		this.renderJson(map);
	}

	/**
	 * 首页获取用户信息
	 */
	public void getIndexChangeUserInfo() {
		String account = this.getSessionAttr("F_ACCOUNT");
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
	public void indexChangeUser() {
		String account = this.getSessionAttr("F_ACCOUNT");
		String name = this.getPara("name");
		try {
			name = URLDecoder.decode(name, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sex = this.getPara("sex");
		String tel = this.getPara("tel");
		String address = this.getPara("address");
		try {
			address = URLDecoder.decode(address, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		String email = this.getPara("email");
		String qq = this.getPara("qq");
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
	public void validatePassword() {
		String account = this.getSessionAttr("F_ACCOUNT");
		String oldpassword = this.getPara("oldpassword");
		String newpassword = this.getPara("newpassword");
		Record rec = Db.findFirst("SELECT COUNT(1) AS NUM FROM SYS_USER WHERE F_ACCOUNT = ? AND F_PASSWORD = ?",
				account, oldpassword);
		String num = rec.get("NUM").toString();
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

	/**
	 * 获取系统日志
	 */
	public void getSysLog() {
		// 计算页数
		String limit = this.getPara("limit");
		String pageNum = this.getPara("page");
		int intLimit = Integer.valueOf(limit);
		int intPageNum = Integer.valueOf(pageNum);
		int startRow = (intPageNum - 1) * intLimit;// 从0开始
		int endRow = startRow + intLimit;// 不包含

		String account = this.getPara("account");
		String startTime = this.getPara("startTime");
		startTime = startTime.replaceAll(" ", "");
		startTime = startTime.replaceAll("-", "");
		startTime = startTime.replaceAll(":", "");
		String endTime = this.getPara("endTime");
		endTime = endTime.replaceAll(" ", "");
		endTime = endTime.replaceAll("-", "");
		endTime = endTime.replaceAll(":", "");

		Record totalRc = Db.findFirst("SELECT COUNT(1) AS F_NUM FROM SYS_LOG"
				+ " WHERE F_ACCOUNT LIKE ? AND F_TIME >= ?" + " AND F_TIME <= ?", "%" + account + "%", startTime,
				endTime);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code", "0");
		map.put("msg", "");
		int totalCount = 0;
		String strTotalCount = "0";
		List<Map<String, Object>> dataList = new ArrayList<>();
		Map<String, Object> dataMap = new HashMap<String, Object>();
		strTotalCount = totalRc.getStr("F_NUM");
		totalCount = Integer.valueOf(strTotalCount);
		map.put("count", totalCount);
		List<Record> list = Db.find(
				"SELECT F_ACCOUNT,F_IP,F_REQUEST_TYPE," + "F_CTRL,F_METHOD,F_PARAMS,F_TIME FROM SYS_LOG"
						+ " WHERE F_ACCOUNT LIKE ? AND F_TIME >= ?" + " AND F_TIME <= ? ORDER BY F_TIME,F_ACCOUNT",
				"%" + account + "%", startTime, endTime);
		Record rc = null;
		if (endRow <= totalCount) {
			for (int i = startRow; i < endRow; i++) {
				dataMap = new HashMap<String, Object>();
				rc = list.get(i);
				dataMap.put("id", i + 1);
				dataMap.put("f_account", rc.getStr("F_ACCOUNT"));
				dataMap.put("f_ip", rc.getStr("F_IP"));
				dataMap.put("f_request_type", rc.getStr("F_REQUEST_TYPE"));
				dataMap.put("f_ctrl", rc.getStr("F_CTRL"));
				dataMap.put("f_method", rc.getStr("F_METHOD"));
				dataMap.put("f_params", rc.getStr("F_PARAMS"));
				dataMap.put("f_time", rc.getStr("F_TIME"));
				dataList.add(dataMap);
			}
		} else {
			for (int i = startRow; i < totalCount; i++) {
				dataMap = new HashMap<String, Object>();
				rc = list.get(i);
				dataMap.put("id", i + 1);
				dataMap.put("f_account", rc.getStr("F_ACCOUNT"));
				dataMap.put("f_ip", rc.getStr("F_IP"));
				dataMap.put("f_request_type", rc.getStr("F_REQUEST_TYPE"));
				dataMap.put("f_ctrl", rc.getStr("F_CTRL"));
				dataMap.put("f_method", rc.getStr("F_METHOD"));
				dataMap.put("f_params", rc.getStr("F_PARAMS"));
				dataMap.put("f_time", rc.getStr("F_TIME"));
				dataList.add(dataMap);
			}
		}
		map.put("data", dataList);
		this.renderJson(map);
	}

	/**
	 * 清理日志
	 */
	public void deleteSysLog() {
		String account = this.getPara("account");
		String startTime = this.getPara("startTime");
		startTime = startTime.replaceAll(" ", "");
		startTime = startTime.replaceAll("-", "");
		startTime = startTime.replaceAll(":", "");
		String endTime = this.getPara("endTime");
		endTime = endTime.replaceAll(" ", "");
		endTime = endTime.replaceAll("-", "");
		endTime = endTime.replaceAll(":", "");
		Db.update("DELETE FROM SYS_LOG WHERE" + " F_ACCOUNT LIKE ? AND F_TIME >= ? AND F_TIME <= ?",
				"%" + account + "%", startTime, endTime);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("message", "操作成功！");
		this.renderJson(map);
	}

	/**
	 * 获取组织机构树
	 */
	public void getOrgInfo() {
		List<Object> returnLs = new ArrayList<>();
		Map<String, String> returnMap = new HashMap<String, String>();
		List<Record> list = Db
				.find("SELECT F_ORG_ID, F_ORG_NAME, F_ORG_PID FROM SYS_ORGANIZATION_DCT" + " ORDER BY F_ORG_ID ");
		for (Record rec : list) {
			returnMap = new HashMap<String, String>();
			returnMap.put("f_org_id", rec.getStr("F_ORG_ID"));
			returnMap.put("name", rec.getStr("F_ORG_NAME"));
			returnMap.put("f_org_pid", rec.getStr("F_ORG_PID"));
			Record ipRc = Db.findFirst("SELECT F_ORG_PID FROM SYS_ORGANIZATION_DCT WHERE F_ORG_PID = ? ",
					rec.getStr("F_ORG_ID"));
			if (ipRc != null) {
				returnMap.put("icon", Constant.ORG_TREE_P_ICON_PATH);
			} else {
				returnMap.put("icon", Constant.ORG_TREE_C_ICON_PATH);
			}
			returnLs.add(returnMap);
		}
		this.renderJson(returnLs);
	}

	/**
	 * 获取组织机构表
	 */
	public void getOrgTable() {
		// 计算页数
		String limit = this.getPara("limit");
		String pageNum = this.getPara("page");
		int intLimit = Integer.valueOf(limit);
		int intPageNum = Integer.valueOf(pageNum);
		int startRow = (intPageNum - 1) * intLimit;// 从0开始
		int endRow = startRow + intLimit;// 不包含

		String orgPID = this.getPara("orgID");

		Record totalRc = Db.findFirst("SELECT COUNT(1) AS F_NUM FROM SYS_ORGANIZATION_DCT" + " WHERE F_ORG_PID = ? ",
				orgPID);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code", "0");
		map.put("msg", "");
		int totalCount = 0;
		String strTotalCount = "0";
		List<Map<String, Object>> dataList = new ArrayList<>();
		Map<String, Object> dataMap = new HashMap<String, Object>();
		strTotalCount = totalRc.getStr("F_NUM");
		totalCount = Integer.valueOf(strTotalCount);
		map.put("count", totalCount);
		map.put("count", totalCount);
		List<Record> list = Db.find("SELECT F_ORG_ID,F_ORG_NAME,F_ORG_PID,F_USE,F_NOTE"
				+ " FROM SYS_ORGANIZATION_DCT WHERE F_ORG_PID = ? " + " ORDER BY F_ORG_ID", orgPID);
		Record rc = null;
		if (endRow <= totalCount) {
			for (int i = startRow; i < endRow; i++) {
				dataMap = new HashMap<String, Object>();
				rc = list.get(i);
				dataMap.put("id", i + 1);
				dataMap.put("f_org_id", rc.getStr("F_ORG_ID"));
				dataMap.put("f_org_name", rc.getStr("F_ORG_NAME"));
				dataMap.put("f_org_pid", rc.getStr("F_ORG_PID"));
				dataMap.put("f_use", rc.getStr("F_USE"));
				dataMap.put("f_note", rc.getStr("F_NOTE"));
				dataList.add(dataMap);
			}
		} else {
			for (int i = startRow; i < totalCount; i++) {
				dataMap = new HashMap<String, Object>();
				rc = list.get(i);
				dataMap.put("id", i + 1);
				dataMap.put("f_org_id", rc.getStr("F_ORG_ID"));
				dataMap.put("f_org_name", rc.getStr("F_ORG_NAME"));
				dataMap.put("f_org_pid", rc.getStr("F_ORG_PID"));
				dataMap.put("f_use", rc.getStr("F_USE"));
				dataMap.put("f_note", rc.getStr("F_NOTE"));
				dataList.add(dataMap);
			}
		}
		map.put("data", dataList);
		this.renderJson(map);
	}

	/**
	 * 添加组织机构
	 */
	public void AddOrg() {
		String orgId = this.getPara("orgId");
		String orgName = this.getPara("orgName");
		try {
			orgName = URLDecoder.decode(orgName, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		String pOrgId = this.getPara("pOrgId");
		String Note = this.getPara("Note");
		try {
			Note = URLDecoder.decode(Note, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		String use = this.getPara("use");

		Map<String, Object> map = new HashMap<String, Object>();
		String code = "1";
		String msg = "";
		// 先查询是否存在pid父节点
		Record rcd = Db.findFirst("SELECT F_ORG_ID FROM SYS_ORGANIZATION_DCT WHERE F_ORG_ID = ? ", pOrgId);
		if (rcd == null && !"-1".equals(pOrgId)) {
			msg = "未找到添加组织机构的父节点，请检查！";
		} else {
			// 检验是否重复
			rcd = Db.findFirst("SELECT F_ORG_ID FROM SYS_ORGANIZATION_DCT WHERE F_ORG_ID = ? ", orgId);
			if (rcd != null) {
				msg = "已存在编号为【" + orgId + "】的组织机构，请检查！";
			} else {
				Db.update("INSERT INTO SYS_ORGANIZATION_DCT (F_ORG_ID,F_ORG_NAME,F_ORG_PID,F_USE,F_NOTE)"
						+ " VALUES(?,?,?,?,?)", orgId, orgName, pOrgId, use, Note);
				code = "0";
				msg = "操作成功！";
			}
		}
		map.put("code", code);
		map.put("message", msg);
		this.renderJson(map);
	}

	/**
	 * 修改组织机构之前查询单个组织机构信息
	 */
	public void getOneOrgInfo() {
		String orgId = this.getPara("orgId");
		Map<String, Object> map = new HashMap<String, Object>();
		Record rec = Db.findFirst(
				"SELECT F_ORG_ID,F_ORG_NAME,F_ORG_PID,F_USE,F_NOTE FROM SYS_ORGANIZATION_DCT WHERE F_ORG_ID = ?",
				orgId);
		if (null != rec) {
			map.put("orgId", rec.get("F_ORG_ID").toString());
			map.put("orgName", rec.get("F_ORG_NAME").toString());
			map.put("orgPId", rec.get("F_ORG_PID").toString());
			map.put("use", rec.get("F_USE").toString());
			map.put("note", rec.get("F_NOTE").toString());
			map.put("code", "0");
		} else {
			map.put("code", "1");
			map.put("message", "不存在的组织机构信息，请删除后重新添加！");
		}
		this.renderJson(map);
	}

	/**
	 * 修改组织机构
	 */
	public void ChangeOrg() {
		String orgId = this.getPara("orgId");
		String orgName = this.getPara("orgName");
		String pOrgId = this.getPara("pOrgId");
		String use = this.getPara("use");
		String Note = this.getPara("Note");
		try {
			orgName = URLDecoder.decode(orgName, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			Note = URLDecoder.decode(Note, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		String msg = "";
		String code = "1";
		Record rcd = Db.findFirst("SELECT F_ORG_ID FROM SYS_ORGANIZATION_DCT WHERE F_ORG_ID = ? ", pOrgId);
		if (rcd == null) {
			msg = "未找到添加组织机构的父节点，请检查！";
		} else {
			Db.update("UPDATE SYS_ORGANIZATION_DCT SET F_ORG_NAME = ?, F_ORG_PID = ?,"
					+ " F_USE = ?, F_NOTE = ? WHERE F_ORG_ID = ? ", orgName, pOrgId, use, Note, orgId);
			code = "0";
			msg = "操作成功！";
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code", code);
		map.put("message", msg);
		this.renderJson(map);
	}

	/**
	 * 删除组织机构
	 */
	public void DeleteOrg() {
		String orgId = this.getPara("orgId");
		String[] orgIdLs = orgId.split(",");
		Map<String, Object> map = new HashMap<String, Object>();
		for (int i = 0; i < orgIdLs.length; i++) {
			Db.update("DELETE FROM SYS_ORGANIZATION_DCT WHERE F_ORG_ID = ? OR F_ORG_PID = ? ", orgIdLs[i], orgIdLs[i]);
		}
		map.put("message", "操作成功!");
		this.renderJson(map);
	}

	/**
	 * 组织机构行权限获取组织机构树
	 */
	public void LimitGetOrgInfo() {
		List<Object> returnLs = new ArrayList<>();
		Map<String, String> returnMap = new HashMap<String, String>();
		List<Record> list = Db.find("SELECT F_ORG_ID, F_ORG_NAME, F_ORG_PID FROM SYS_ORGANIZATION_DCT"
				+ " WHERE F_USE = ? ORDER BY F_ORG_ID ", Constant.CAPITAL_STR_Y);
		for (Record rec : list) {
			returnMap = new HashMap<String, String>();
			returnMap.put("f_org_id", rec.getStr("F_ORG_ID"));
			returnMap.put("name", rec.getStr("F_ORG_NAME"));
			returnMap.put("f_org_pid", rec.getStr("F_ORG_PID"));
			Record ipRc = Db.findFirst("SELECT F_ORG_PID FROM SYS_ORGANIZATION_DCT WHERE F_ORG_PID = ? ",
					rec.getStr("F_ORG_ID"));
			if (ipRc != null) {
				returnMap.put("icon", "../../../SYSTEM/lib/zTree/css/zTreeStyle/img/diy/1_open.png");
			} else {
				returnMap.put("icon", "../../../SYSTEM/lib/zTree/css/zTreeStyle/img/diy/1_close.png");
			}
			returnLs.add(returnMap);
		}
		this.renderJson(returnLs);
	}

	/**
	 * 获取控制权限的类型
	 */
	public void getLimitType() {
		Map<String, Object> map = new HashMap<String, Object>();
		String LimitType = GetConFromDB.GetCIFromDB("LIMIT_TYPE");
		map.put("type", LimitType);
		this.renderJson(map);
	}
}
