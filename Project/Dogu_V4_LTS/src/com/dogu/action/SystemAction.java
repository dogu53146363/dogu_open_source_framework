package com.dogu.action;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.quartz.Job;
import com.dogu.constants.Constant;
import com.dogu.timmer.QuartzManager;
import com.dogu.utils.CommonUtils;
import com.dogu.utils.GetConFromDB;
import com.dogu.utils.MenuTreeUtil;
import com.dogu.utils.Tree;
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
		String account = this.getSessionAttr("F_ACCOUNT");
		String orgId = this.getSessionAttr("F_ORG");
		String role = this.getSessionAttr("F_ROLE");
		if (Constant.CAPITAL_STR_ORG.equals(LimitType)) {//组织机构行权限
			List<Record> DataList =  Db.find("SELECT DISTINCT(F_ID) AS ID,F_PID AS PID,"
					+ "F_NAME AS NAME,F_PATH AS URL,F_ICON AS ICON"
					+ " FROM SYS_MENU_DCT dct,SYS_MENU_LIMIT lmt"
					+ " WHERE dct.F_ID = lmt.F_MENUID AND ((F_TYPE = ? AND F_ACCOUNT = ?)"
					+ " OR (F_TYPE = ? AND F_ACCOUNT = ?)) AND F_SHOW = ? AND F_USE = ? ORDER BY F_ID",
						Constant.CAPITAL_STR_ORG,orgId,Constant.CAPITAL_STR_USER,account,
						Constant.CAPITAL_STR_Y,Constant.CAPITAL_STR_Y);
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
					tree.setUrl(DataList.get(i).getStr("URL"));
					tree.setIcon(DataList.get(i).getStr("ICON"));
					list.add(tree);
				}
				MenuTreeUtil menuTree = new MenuTreeUtil();
				List<Object> menuList = menuTree.menuList(list);
				returnMap.put("data",menuList);
			}
		} else if (Constant.CAPITAL_STR_ROLE.equals(LimitType)) {//角色行权限
			List<Record> DataList =  Db.find("SELECT DISTINCT(F_ID) AS ID,F_PID AS PID,"
					+ "F_NAME AS NAME,F_PATH AS URL,F_ICON AS ICON"
					+ " FROM SYS_MENU_DCT dct,SYS_MENU_LIMIT lmt"
					+ " WHERE dct.F_ID = lmt.F_MENUID AND ((F_TYPE = ? AND F_ACCOUNT = ?)"
					+ " OR (F_TYPE = ? AND F_ACCOUNT = ?)) AND F_SHOW = ? AND F_USE = ? ORDER BY F_ID",
						Constant.CAPITAL_STR_ROLE,role,Constant.CAPITAL_STR_USER,account,
						Constant.CAPITAL_STR_Y,Constant.CAPITAL_STR_Y);
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
					tree.setUrl(DataList.get(i).getStr("URL"));
					tree.setIcon(DataList.get(i).getStr("ICON"));
					list.add(tree);
				}
				MenuTreeUtil menuTree = new MenuTreeUtil();
				List<Object> menuList = menuTree.menuList(list);
				returnMap.put("data",menuList);
			}
		} else if(Constant.CAPITAL_STR_ALL.equals(LimitType)){//所有权限模式
			List<Record> DataList =  Db.find("SELECT DISTINCT(F_ID) AS ID,F_PID AS PID,"
					+ "F_NAME AS NAME,F_PATH AS URL,F_ICON AS ICON"
					+ " FROM SYS_MENU_DCT dct,SYS_MENU_LIMIT lmt"
					+ " WHERE dct.F_ID = lmt.F_MENUID AND ((F_TYPE = ? AND F_ACCOUNT = ?)"
					+ " OR (F_TYPE = ? AND F_ACCOUNT = ?) OR (F_TYPE = ? AND F_ACCOUNT = ? ))"
					+ " AND F_SHOW = ? AND F_USE = ? ORDER BY F_ID",Constant.CAPITAL_STR_USER,account,
						Constant.CAPITAL_STR_ROLE,role,Constant.CAPITAL_STR_ORG,orgId,
						Constant.CAPITAL_STR_Y,Constant.CAPITAL_STR_Y);
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
					tree.setUrl(DataList.get(i).getStr("URL"));
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
	 * 用户管理-获取角色字典
	 */
	public void userManegeGetRoleInfo() {
		String use = this.getPara("use", "");
		List<Record> list = Db.find(
				"SELECT F_ROLENUM,F_ROLENAME,F_USE FROM SYS_ROLE_DCT " + "WHERE F_USE LIKE ? ORDER BY F_ROLENUM",
				"%" + use + "%");
		this.renderJson(list);
	}

	/**
	 * 用户管理-获取组织机构字典
	 */
	public void userManegeGetOrgInfo() {
		String use = this.getPara("use", "");
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
		String limit = this.getPara("limit", "10");
		String pageNum = this.getPara("page", "1");
		int intLimit = Integer.valueOf(limit);
		int intPageNum = Integer.valueOf(pageNum);
		int startRow = (intPageNum - 1) * intLimit;// 从0开始
		int endRow = startRow + intLimit;// 不包含

		String account = this.getPara("account", "");
		String rolenum = this.getPara("rolenum", "");
		String username = this.getPara("username","");
		try {
			username = URLDecoder.decode(username, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		String orgname = this.getPara("orgname", "");
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
		String account = this.getPara("account", "");
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
		String account = this.getPara("account", "");
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
	public void addUser() {
		String account = this.getPara("account", "");
		String password = this.getPara("password", "");
		String role = this.getPara("role", "");
		String org = this.getPara("org", "");
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
		if (CommonUtils.isLetterDigit(account)) {
			Record rec = Db.findFirst("SELECT COUNT(1) AS NUM FROM SYS_USER WHERE F_ACCOUNT = ?", account);
			String num = rec.getStr("NUM");
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
		String account = this.getPara("account", "");
		Map<String, Object> map = new HashMap<String, Object>();
		Record rec = Db.findFirst(
				"SELECT F_ACCOUNT,F_PASSWORD,F_ROLE,F_ORG,F_USERNAME,F_SEX,F_TEL,F_ADDRESS,F_E_MAIL,F_QQ FROM SYS_USER WHERE F_ACCOUNT = ?",
				account);
		if (rec == null) {
			map.put("code", "1");
			map.put("message", "未查询到相关用户信息，请刷新重试！");
		} else {
			map.put("account", rec.getStr("F_ACCOUNT"));
			map.put("password", rec.getStr("F_PASSWORD"));
			map.put("role", rec.getStr("F_ROLE"));
			map.put("org", rec.getStr("F_ORG"));
			map.put("name", rec.getStr("F_USERNAME"));
			map.put("sex", rec.getStr("F_SEX"));
			map.put("tel", rec.getStr("F_TEL"));
			map.put("address", rec.getStr("F_ADDRESS"));
			map.put("email", rec.getStr("F_E_MAIL"));
			map.put("qq", rec.getStr("F_QQ"));
			map.put("code", "0");
		}
		this.renderJson(map);
	}

	/**
	 * 更改用户信息
	 */
	public void changeUserInfo() {
		String account = this.getPara("account", "");
		String password = this.getPara("password", "");
		String role = this.getPara("role", "");
		String org = this.getPara("org", "");
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
				"UPDATE SYS_USER SET F_PASSWORD = ?,F_ROLE = ?,F_ORG = ?,F_USERNAME = ?,F_SEX = ?,F_TEL = ?,F_ADDRESS = ?,F_E_MAIL = ?,F_QQ = ? WHERE F_ACCOUNT = '"
						+ account + "'",
				password, role, org, name, sex, tel, address, email, qq);
		map.put("message", "操作成功!");
		this.renderJson(map);
	}

	/**
	 * 获取菜单信息的ztree
	 */
	public void queryMenuTree() {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		List<Map<String, Object>> returnLs = new ArrayList<Map<String, Object>>();
		returnMap.put("code", 0);
		returnMap.put("msg", "");
		List<Record> dataLs = Db.find("SELECT F_ID,F_NAME,F_PID,F_PATH,F_ICON,F_USE,F_SHOW,F_INTERCEPT"
				+ " FROM SYS_MENU_DCT ORDER BY F_ID");
		Map<String, Object> dataMap = new HashMap<String, Object>();
		for(Record rec: dataLs) {
			dataMap = new HashMap<String, Object>();
			dataMap.put("F_ID", rec.getStr("F_ID"));
			dataMap.put("F_NAME", rec.getStr("F_NAME"));
			dataMap.put("F_PID", rec.getStr("F_PID"));
			dataMap.put("F_PATH", rec.getStr("F_PATH"));
			dataMap.put("F_ICON", rec.getStr("F_ICON"));
			dataMap.put("F_USE", rec.getStr("F_USE"));
			dataMap.put("F_SHOW", rec.getStr("F_SHOW"));
			dataMap.put("F_INTERCEPT", rec.getStr("F_INTERCEPT"));
			returnLs.add(dataMap);
		}
		returnMap.put("count", returnLs.size());
		returnMap.put("data", returnLs);
		this.renderJson(returnMap);
	}

	/**
	 * 新增菜单中的保存菜单action
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
		if("".equals(path)) {
			path = "javascript:;";
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
							"INSERT INTO SYS_MENU_DCT (F_ID,F_NAME,F_PID,F_PATH,F_ICON,F_USE,F_SHOW,F_INTERCEPT) VALUES (?,?,?,?,?,?,?,?)",
							id, name, pid, path, icon, use, show, interceptor);
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
								"INSERT INTO SYS_MENU_DCT (F_ID,F_NAME,F_PID,F_PATH,F_ICON,F_USE,F_SHOW,F_INTERCEPT) VALUES (?,?,?,?,?,?,?,?)",
								id, name, pid, path, icon, use, show, interceptor);
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
	 * 修改菜单内容
	 */
	public void updateMenu() {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		String id = this.getPara("id", "");
		String name = this.getPara("name", "");
		String pid = this.getPara("pid", "");
		String path = this.getPara("path", "");
		String icon = this.getPara("icon", "");
		String use = this.getPara("use", "");
		String show = this.getPara("show", "");
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
		if("".equals(path)) {
			path = "javascript:;";
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
							"UPDATE SYS_MENU_DCT SET F_NAME = ?,F_PID = ?,F_PATH = ?,"
							+ "F_ICON = ?,F_USE = ?,F_SHOW = ?,F_INTERCEPT = ? WHERE F_ID = ?",
							name, pid, path, icon, use, show, interceptor,id);
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
								"UPDATE SYS_MENU_DCT SET F_NAME = ?,F_PID = ?,F_PATH = ?,"
								+ "F_ICON = ?,F_USE = ?,F_SHOW = ?,F_INTERCEPT = ? WHERE F_ID = ?",
								name, pid, path, icon, use, show, interceptor,id);
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
	 * 获取角色字典
	 */
	public void getRoleInfo() {
		// 计算页数
		String limit = this.getPara("limit","10");
		String pageNum = this.getPara("page","1");
		int intLimit = Integer.valueOf(limit);
		int intPageNum = Integer.valueOf(pageNum);
		int startRow = (intPageNum - 1) * intLimit;// 从0开始
		int endRow = startRow + intLimit;// 不包含

		String use = this.getPara("use", "");
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
	public void addRole() {
		String roleId = this.getPara("roleid", "");
		String roleName = this.getPara("rolename", "");
		String use = this.getPara("use", "");
		try {
			roleName = URLDecoder.decode(roleName, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		Map<String, Object> map = new HashMap<String, Object>();
		if (CommonUtils.isLetterDigit(roleId)) {
			Record rec = Db.findFirst("SELECT COUNT(1) AS NUM FROM SYS_ROLE_DCT WHERE F_ROLENUM = ?", roleId);
			String num = rec.getStr("NUM");
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
		String roleid = this.getPara("roleid", "");
		Map<String, Object> map = new HashMap<String, Object>();
		Record rec = Db.findFirst("SELECT F_ROLENUM,F_ROLENAME,F_USE FROM SYS_ROLE_DCT WHERE F_ROLENUM = ?", roleid);
		if (null != rec) {
			map.put("roleid", rec.getStr("F_ROLENUM"));
			map.put("rolename", rec.getStr("F_ROLENAME"));
			map.put("use", rec.getStr("F_USE"));
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
	public void updateRole() {
		String roleid = this.getPara("roleid", "");
		String rolename = this.getPara("rolename", "");
		String use = this.getPara("use", "");
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
	 * 删除组织机构之前查询是否有用户属于该组织机构
	 */
	public void beforeDelRoleQuery() {
		String roleid = this.getPara("roleid", "");
		String message = "删除失败，";
		int code = 0;
		String[] roleidLs = roleid.split(",");
		Map<String, Object> map = new HashMap<String, Object>();
		Record queryRec = null;
		for (int i = 0; i < roleidLs.length; i++) {
			queryRec = Db.findFirst("SELECT F_ACCOUNT,F_ROLE FROM SYS_USER WHERE F_ROLE = ?", roleidLs[i]);
			if(queryRec != null) {
				message += "用户["+queryRec.getStr("F_ACCOUNT")+"]绑定了角色["+queryRec.getStr("F_ROLE")+"]";
				code ++;
			}
		}
		map.put("code", ""+code);
		map.put("message", message);
		this.renderJson(map);
	}

	/**
	 * 删除角色
	 */
	public void deleteRole() {
		String roleid = this.getPara("roleid", "");
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
	public void limitgetUserInfo() {
		String account = this.getPara("account", "");
		String username = this.getPara("username", "");
		List<Record> list = Db.find(
				"SELECT F_ACCOUNT,F_USERNAME FROM SYS_USER"
						+ " WHERE F_ACCOUNT LIKE ? AND F_USERNAME LIKE ? ORDER BY F_ACCOUNT",
				"%" + account + "%", "%" + username + "%");
		this.renderJson(list);
	}

	/**
	 * 用户/角色/组织机构行权限获取菜单树
	 */
	public void limitgetMenuInfo() {
		// 生成一级树
		List<Record> dataList = Db.find("SELECT F_ID,F_PID,F_NAME FROM"
				+ " SYS_MENU_DCT WHERE F_USE = ? ORDER BY F_ID",
				Constant.CAPITAL_STR_Y);
		List<Map<String, Object>> returnLs = new ArrayList<Map<String, Object>> ();
		Map<String, Object> dataMap = new HashMap<String, Object>();
		for(Record rec: dataList) {
			dataMap = new HashMap<String, Object>();
			dataMap.put("id",rec.getStr("F_ID"));
			dataMap.put("pId",rec.getStr("F_PID"));
			dataMap.put("name",rec.getStr("F_NAME"));
			dataMap.put("icon",Constant.MENU_TREE_C_ICON_PATH);
			returnLs.add(dataMap);
		}
		this.renderJson(returnLs);
	}

	/**
	 * 获取选中的树节点
	 */
	public void getSelectTreeNode() {
		String account = this.getPara("account", "");
		String type = this.getPara("type", "");
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
	public void saveUserLimit() {
		Map<String, Object> map = new HashMap<String, Object>();
		String userid = this.getPara("userid", "");
		String menuid = this.getPara("menuid", "");
		String type = this.getPara("type", "");
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

		String rolename = this.getPara("rolename", "");
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
	public void saveRoleLimit() {
		Map<String, Object> map = new HashMap<String, Object>();
		String roleid = this.getPara("roleid", "");
		String menuid = this.getPara("menuid", "");
		String type = this.getPara("type", "");
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
	 * 保存组织机构行权限
	 */
	public void saveOrgLimit() {
		Map<String, Object> map = new HashMap<String, Object>();
		String orgid = this.getPara("orgid", "");
		String menuid = this.getPara("menuid", "");
		String type = this.getPara("type", "");
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
		String limit = this.getPara("limit", "10");
		String pageNum = this.getPara("page", "1");
		int intLimit = Integer.valueOf(limit);
		int intPageNum = Integer.valueOf(pageNum);
		int startRow = (intPageNum - 1) * intLimit;// 从0开始
		int endRow = startRow + intLimit;// 不包含

		String zt = this.getPara("zt", "");

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
		String timmerId = this.getPara("timmerid", "");
		Record rec = Db.findFirst("SELECT F_TIMMERID,F_STATUS,F_CLZ,F_CRON,F_NOTE FROM SYS_TIMMER WHERE F_TIMMERID = ?",
				timmerId);
		if (rec == null) {
			map.put("code", "1");
			map.put("message", "未查询到该菜单信息，请刷新后重试！");
		} else {
			String f_timmerid = rec.getStr("F_TIMMERID");
			String f_status = rec.getStr("F_STATUS");
			String f_clz = rec.getStr("F_CLZ");
			String f_cron = rec.getStr("F_CRON");
			String f_note = rec.getStr("F_NOTE");
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
	public void startTimmer() {
		Map<String, Object> map = new HashMap<String, Object>();
		String timmerid = this.getPara("timmerid", "");
		// 根据timmerid获取必要的参数
		Record rec = Db.findFirst("SELECT F_CLZ,F_CRON FROM SYS_TIMMER WHERE F_TIMMERID = ?", timmerid);
		String clazz = rec.getStr("F_CLZ");
		String f_cron = rec.getStr("F_CRON");
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
	public void stopTimmer() {
		Map<String, Object> map = new HashMap<String, Object>();
		String timmerid = this.getPara("timmerid", "");
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
	public void deleteTimmer() {
		Map<String, Object> map = new HashMap<String, Object>();
		String timmerid = this.getPara("timmerid", "");
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
	public void addTimmer() {
		String timmerid = this.getPara("timmerid", "");
		String timmerzt = this.getPara("timmerzt", "");
		String timmerclz = this.getPara("timmerclz", "");
		timmerclz = timmerclz.trim();
		String timmercron = this.getPara("timmercron", "");
		String timmernote = this.getPara("timmernote", "");
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
			String num = rec.getStr("NUM");
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
	public void modifyTimmer() {
		String timmerid = this.getPara("timmerid", "");
		String timmerzt = this.getPara("timmerzt", "");
		String timmerclz = this.getPara("timmerclz", "");
		timmerclz = timmerclz.trim();
		String timmercron = this.getPara("timmercron", "");
		String timmernote = this.getPara("timmernote", "");
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
			String num = rec.getStr("NUM");
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
		String limit = this.getPara("limit", "10");
		String pageNum = this.getPara("page", "1");
		int intLimit = Integer.valueOf(limit);
		int intPageNum = Integer.valueOf(pageNum);
		int startRow = (intPageNum - 1) * intLimit;// 从0开始
		int endRow = startRow + intLimit;// 不包含

		String confDescription = this.getPara("confDescription", "");
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
	public void addConf() {
		String confkey = this.getPara("confkey", "");
		String confvalue = this.getPara("confvalue", "");
		String confnote = this.getPara("confnote", "");
		try {
			confvalue = URLDecoder.decode(confvalue, "UTF-8");
			confnote = URLDecoder.decode(confnote, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		Map<String, Object> map = new HashMap<String, Object>();
		Record rec = Db.findFirst("SELECT COUNT(1) AS NUM FROM SYS_CONFIG WHERE F_KEY = ?", confkey);
		String num = rec.getStr("NUM");
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
		String confkey = this.getPara("confkey", "");
		Map<String, Object> map = new HashMap<String, Object>();
		Record rec = Db.findFirst("SELECT F_KEY,F_VALUE,F_NOTE FROM SYS_CONFIG WHERE F_KEY = ?", confkey);
		if (null != rec) {
			map.put("key", rec.getStr("F_KEY"));
			map.put("value", rec.getStr("F_VALUE"));
			map.put("note", rec.getStr("F_NOTE"));
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
	public void updateConf() {
		String confkey = this.getPara("confkey", "");
		String confvalue = this.getPara("confvalue", "");
		String confnote = this.getPara("confnote", "");
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
	public void deleteConf() {
		String confkey = this.getPara("confkey", "");
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
	public void validatePassword() {
		String account = this.getSessionAttr("F_ACCOUNT");
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

	/**
	 * 获取系统日志
	 */
	public void getSysLog() {
		// 计算页数
		String limit = this.getPara("limit", "10");
		String pageNum = this.getPara("page", "1");
		int intLimit = Integer.valueOf(limit);
		int intPageNum = Integer.valueOf(pageNum);
		int startRow = (intPageNum - 1) * intLimit;// 从0开始
		int endRow = startRow + intLimit;// 不包含

		String account = this.getPara("account", "");
		String startTime = this.getPara("startTime", "");
		startTime = startTime.replaceAll(" ", "");
		startTime = startTime.replaceAll("-", "");
		startTime = startTime.replaceAll(":", "");
		String endTime = this.getPara("endTime", "");
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
		String account = this.getPara("account", "");
		String startTime = this.getPara("startTime", "");
		startTime = startTime.replaceAll(" ", "");
		startTime = startTime.replaceAll("-", "");
		startTime = startTime.replaceAll(":", "");
		String endTime = this.getPara("endTime", "");
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
		String limit = this.getPara("limit", "10");
		String pageNum = this.getPara("page", "1");
		int intLimit = Integer.valueOf(limit);
		int intPageNum = Integer.valueOf(pageNum);
		int startRow = (intPageNum - 1) * intLimit;// 从0开始
		int endRow = startRow + intLimit;// 不包含

		String orgPID = this.getPara("orgID", "");

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
	public void addOrg() {
		String orgId = this.getPara("orgId", "");
		String orgName = this.getPara("orgName", "");
		try {
			orgName = URLDecoder.decode(orgName, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		String pOrgId = this.getPara("pOrgId", "");
		String Note = this.getPara("Note", "");
		try {
			Note = URLDecoder.decode(Note, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		String use = this.getPara("use", "");

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
		String orgId = this.getPara("orgId", "");
		Map<String, Object> map = new HashMap<String, Object>();
		Record rec = Db.findFirst(
				"SELECT F_ORG_ID,F_ORG_NAME,F_ORG_PID,F_USE,F_NOTE FROM SYS_ORGANIZATION_DCT WHERE F_ORG_ID = ?",
				orgId);
		if (null != rec) {
			map.put("orgId", rec.getStr("F_ORG_ID"));
			map.put("orgName", rec.getStr("F_ORG_NAME"));
			map.put("orgPId", rec.getStr("F_ORG_PID"));
			map.put("use", rec.getStr("F_USE"));
			map.put("note", rec.getStr("F_NOTE"));
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
	public void changeOrg() {
		String orgId = this.getPara("orgId", "");
		String orgName = this.getPara("orgName", "");
		String pOrgId = this.getPara("pOrgId", "");
		String use = this.getPara("use", "");
		String Note = this.getPara("Note", "");
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
	 * 删除组织机构之前查询是否有用户属于该组织机构
	 */
	public void beforeDelOrgQuery() {
		String orgId = this.getPara("orgId", "");
		String message = "删除失败，";
		int code = 0;
		String[] orgIdLs = orgId.split(",");
		Map<String, Object> map = new HashMap<String, Object>();
		Record queryRec = null;
		for (int i = 0; i < orgIdLs.length; i++) {
			queryRec = Db.findFirst("SELECT F_ACCOUNT,F_ORG FROM SYS_USER WHERE F_ORG = ?", orgIdLs[i]);
			if(queryRec != null) {
				message += "用户["+queryRec.getStr("F_ACCOUNT")+"]绑定了组织机构["+queryRec.getStr("F_ORG")+"]";
				code ++;
			}
		}
		map.put("code", ""+code);
		map.put("message", message);
		this.renderJson(map);
	}

	/**
	 * 删除组织机构
	 */
	public void deleteOrg() {
		String orgId = this.getPara("orgId", "");
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
	public void limitGetOrgInfo() {
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
