package com.dogu.action;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dogu.constants.Constant;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

public class OrgManageAction extends Controller {
	
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
	 * 获取组织机构树
	 */
	public void getOrgTreeInfo() {
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
				returnMap.put("icon", this.getRequest().getContextPath()+"/common/lib/zTree/css/zTreeStyle/img/diy/1_open.png");
			} else {
				returnMap.put("icon", this.getRequest().getContextPath()+"/common/lib/zTree/css/zTreeStyle/img/diy/1_close.png");
			}
			returnLs.add(returnMap);
		}
		this.renderJson(returnLs);
	}
	
	/**
	 * 获取组织机构表
	 */
	public void getOrgTable() {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		try {
			// 计算页数
			String limit = this.getPara("limit", "10");
			String pageNum = this.getPara("page", "1");
			int pageSize = Integer.valueOf(limit);
			int pageNumber = Integer.valueOf(pageNum);
			String orgPID = this.getPara("orgID", "");
			
			StringBuffer select = new StringBuffer();
			select.append("SELECT F_ORG_ID,F_ORG_NAME,F_ORG_PID,F_USE,F_NOTE");
			StringBuffer sqlExceptSelect = new StringBuffer();
			sqlExceptSelect.append("FROM SYS_ORGANIZATION_DCT WHERE F_ORG_PID = ? ORDER BY F_ORG_ID");
			List<Object> paras = new ArrayList<Object>();
			paras.add(orgPID);
			
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
	public void updateOrg() {
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
}
