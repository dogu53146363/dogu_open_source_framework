package com.dogu.action;

import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dogu.constants.Constant;
import com.dogu.utils.CommonUtils;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

public class UserManageAction extends Controller {
	
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
	 * 获取角色字典
	 */
	public void getRoleInfo() {
		String use = this.getPara("use", "");
		List<Record> list = Db.find(
				"SELECT F_ROLENUM,F_ROLENAME,F_USE FROM SYS_ROLE_DCT " + "WHERE F_USE LIKE ? ORDER BY F_ROLENUM",
				"%" + use + "%");
		this.renderJson(list);
	}
	
	/**
	 * 获取组织机构字典
	 */
	public void getOrgInfo() {
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
		Map<String, Object> returnMap = new HashMap<String, Object>();
		try {
			// 计算页数
			String limit = this.getPara("limit", "10");
			String pageNum = this.getPara("page", "1");
			int pageSize = Integer.valueOf(limit);
			int pageNumber = Integer.valueOf(pageNum);

			String account = this.getPara("account", "");
			String rolenum = this.getPara("rolenum", "");
			String username = this.getPara("username","");
			
			username = URLDecoder.decode(username, "UTF-8");
			String orgname = this.getPara("orgname", "");
			orgname = URLDecoder.decode(orgname, "UTF-8");
			
			StringBuffer select = new StringBuffer();
			select.append("SELECT F_ACCOUNT,F_USERNAME,F_ORG_NAME,F_ROLENAME,F_SEX,F_TEL,F_ADDRESS,F_E_MAIL,F_QQ");
			StringBuffer sqlExceptSelect = new StringBuffer();
			sqlExceptSelect.append("FROM SYS_USER,SYS_ROLE_DCT,SYS_ORGANIZATION_DCT");
			sqlExceptSelect.append(" WHERE F_ROLENUM LIKE ? AND F_USERNAME LIKE ? AND F_ACCOUNT LIKE ? AND SYS_USER.F_ROLE =");
			sqlExceptSelect.append(" SYS_ROLE_DCT.F_ROLENUM AND SYS_USER.F_ORG = SYS_ORGANIZATION_DCT.F_ORG_ID");
			sqlExceptSelect.append(" AND F_ORG_NAME LIKE ? ORDER BY F_CREATE_TIME");
			List<Object> paras = new ArrayList<Object>();
			paras.add("%" + rolenum + "%");
			paras.add("%" + username + "%");
			paras.add("%" + account + "%");
			paras.add("%" + orgname + "%");
			Page<Record> page = Db.paginate(pageNumber, pageSize, select.toString(), sqlExceptSelect.toString(), paras.toArray());
			returnMap.put("code", "0");
			returnMap.put("count", page.getTotalRow());
			returnMap.put("total", page.getTotalPage());
			returnMap.put("data", page.getList());
		}catch(Exception e) {
			e.printStackTrace();
			returnMap.put("code", "-1");
			returnMap.put("msg", e.toString());
		}
		this.renderJson(returnMap);
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
				SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
				Db.update(
						"INSERT INTO SYS_USER (F_ACCOUNT,F_PASSWORD,F_ROLE,F_ORG,F_USERNAME,F_SEX,F_TEL,F_ADDRESS,F_E_MAIL,F_QQ,F_CREATE_TIME)"
						+ " VALUES (?,?,?,?,?,?,?,?,?,?,?)",account, password, role, org, name, sex, tel, address, email, qq, df.format(new Date()).toString());
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
	 * 改变用户信息的时候先查询用户信息
	 */
	public void getOneUserInfo() {
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
	public void updateUserInfo() {
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
}
