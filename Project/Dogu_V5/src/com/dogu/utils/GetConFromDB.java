package com.dogu.utils;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

public class GetConFromDB {
	
	//通过数据库后去配置文件
	public static String GetCIFromDB(String key) {
		String returenCI = "";;
		Record rec = null;
		if(null != key && !"".equals(key)) {
			rec = Db.findFirst("SELECT F_VALUE FROM SYS_CONFIG WHERE F_KEY = ? ",key);
		}
		if(null != rec ) {
			returenCI = rec.getStr("F_VALUE");
		}
		return returenCI;
	}
}
