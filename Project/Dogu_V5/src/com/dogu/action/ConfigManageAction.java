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

public class ConfigManageAction extends Controller {
	
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
	 * 获取配置设置信息
	 */
	public void getConfInfo() {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		try {
			// 计算页数
			String limit = this.getPara("limit", "10");
			String pageNum = this.getPara("page", "1");
			int pageSize = Integer.valueOf(limit);
			int pageNumber = Integer.valueOf(pageNum);

			String confDescription = this.getPara("confDescription", "");
			confDescription = URLDecoder.decode(confDescription, "UTF-8");
			
			StringBuffer select = new StringBuffer();
			select.append("SELECT F_KEY,F_VALUE,F_NOTE");
			StringBuffer sqlExceptSelect = new StringBuffer();
			sqlExceptSelect.append("FROM SYS_CONFIG WHERE F_KEY LIKE ? OR F_VALUE LIKE ? OR F_NOTE LIKE ? ORDER BY F_KEY");
			List<Object> paras = new ArrayList<Object>();
			paras.add("%" + confDescription + "%");
			paras.add("%" + confDescription + "%");
			paras.add("%" + confDescription + "%");
			
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
}
