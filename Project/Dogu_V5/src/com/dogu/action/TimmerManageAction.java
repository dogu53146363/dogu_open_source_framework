package com.dogu.action;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.quartz.Job;

import com.dogu.constants.Constant;
import com.dogu.timmer.QuartzManager;
import com.dogu.utils.CommonUtils;
import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

public class TimmerManageAction extends Controller {
	
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
	 * 定时任务查询
	 */
	public void getTimmerInfo() {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		try {
			// 计算页数
			String limit = this.getPara("limit", "10");
			String pageNum = this.getPara("page", "1");
			int pageSize = Integer.valueOf(limit);
			int pageNumber = Integer.valueOf(pageNum);
			String zt = this.getPara("zt", "");
			
			StringBuffer select = new StringBuffer();
			select.append("SELECT F_TIMMERID,F_STATUS,F_CLZ,F_CRON,F_NOTE");
			StringBuffer sqlExceptSelect = new StringBuffer();
			sqlExceptSelect.append("FROM SYS_TIMMER WHERE F_STATUS LIKE ? ORDER BY F_TIMMERID");
			List<Object> paras = new ArrayList<Object>();
			paras.add("%" + zt + "%");
			
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
	@Clear(com.dogu.interceptor.XSSInterceptor.class)
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
	 * 修改定时任务
	 */
	@Clear(com.dogu.interceptor.XSSInterceptor.class)
	public void updateTimmer() {
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
	
	public void cronPage() {
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
}
