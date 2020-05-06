package com.dogu.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dogu.constants.Constant;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

public class LogManageAction extends Controller {
	
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
	 * 获取系统日志
	 */
	public void getSysLog() {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		try {
			// 计算页数
			String limit = this.getPara("limit", "10");
			String pageNum = this.getPara("page", "1");
			int pageSize = Integer.valueOf(limit);
			int pageNumber = Integer.valueOf(pageNum);

			String account = this.getPara("account", "");
			String startTime = this.getPara("startTime", "");
			startTime = startTime.replaceAll(" ", "");
			startTime = startTime.replaceAll("-", "");
			startTime = startTime.replaceAll(":", "");
			String endTime = this.getPara("endTime", "");
			endTime = endTime.replaceAll(" ", "");
			endTime = endTime.replaceAll("-", "");
			endTime = endTime.replaceAll(":", "");
			
			StringBuffer select = new StringBuffer();
			select.append("SELECT F_ACCOUNT,F_IP,F_REQUEST_TYPE,F_CTRL,F_TIME,F_METHOD,F_PARAMS");
			StringBuffer sqlExceptSelect = new StringBuffer();
			sqlExceptSelect.append("FROM SYS_LOG WHERE F_ACCOUNT LIKE ? AND F_TIME >= ? AND F_TIME <= ? ORDER BY F_TIME,F_ACCOUNT");
			List<Object> paras = new ArrayList<Object>();
			paras.add("%" + account + "%");
			paras.add(startTime);
			paras.add(endTime);
			
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
}
