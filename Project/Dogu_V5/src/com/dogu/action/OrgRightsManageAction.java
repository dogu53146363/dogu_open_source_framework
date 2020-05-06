package com.dogu.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dogu.constants.Constant;
import com.dogu.utils.GetConFromDB;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

public class OrgRightsManageAction extends Controller {
	
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
	 * 组织机构行权限获取组织机构树
	 */
	public void getOrgInfo() {
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
				returnMap.put("icon", this.getRequest().getContextPath()+"/common/lib/zTree/css/zTreeStyle/img/diy/1_open.png");
			} else {
				returnMap.put("icon", this.getRequest().getContextPath()+"/common/lib/zTree/css/zTreeStyle/img/diy/1_close.png");
			}
			returnLs.add(returnMap);
		}
		this.renderJson(returnLs);
	}
	
	/**
	 * 用户/角色/组织机构行权限获取菜单树
	 */
	public void getMenuInfo() {
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
			dataMap.put("icon",this.getRequest().getContextPath()+"/common/lib/zTree/css/zTreeStyle/img/diy/2.png");
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
	 * 保存组织机构行权限
	 */
	public void saveOrgRights() {
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
	 * 获取控制权限的类型
	 */
	public void getRightsType() {
		Map<String, Object> map = new HashMap<String, Object>();
		String LimitType = GetConFromDB.GetCIFromDB("LIMIT_TYPE");
		map.put("type", LimitType);
		this.renderJson(map);
	}
}
