package com.dogu.action;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dogu.constants.Constant;
import com.dogu.utils.CommonUtils;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

public class RoleManageAction extends Controller {
	
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
		Map<String, Object> returnMap = new HashMap<String, Object>();
		try {
			// 计算页数
			String limit = this.getPara("limit","10");
			String pageNum = this.getPara("page","1");
			int pageSize = Integer.valueOf(limit);
			int pageNumber = Integer.valueOf(pageNum);
			String use = this.getPara("use", "");
			
			StringBuffer select = new StringBuffer();
			select.append("SELECT F_ROLENUM,F_ROLENAME,F_USE");
			StringBuffer sqlExceptSelect = new StringBuffer();
			sqlExceptSelect.append("FROM SYS_ROLE_DCT WHERE F_USE LIKE ? ORDER BY F_ROLENUM");
			List<Object> paras = new ArrayList<Object>();
			paras.add("%" + use + "%");
			
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
}
