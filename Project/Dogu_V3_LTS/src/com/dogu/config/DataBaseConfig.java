package com.dogu.config;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import com.dogu.constants.Constant;
import com.jfinal.config.Plugins;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.dialect.MysqlDialect;
import com.jfinal.plugin.activerecord.dialect.OracleDialect;
import com.jfinal.plugin.activerecord.dialect.SqlServerDialect;
import com.jfinal.plugin.activerecord.dialect.Sqlite3Dialect;
import com.jfinal.plugin.druid.DruidPlugin;

/**
 * 数据库配置
 * 
 * @author Dogu Tip：数据库连接可以继续累加
 */
public class DataBaseConfig {

	/**
	 * 数据库注册
	 * @param me
	 */
	public DataBaseConfig(Plugins me) {
		// 数据库配置文件目录
		String DbConfigFloder_Path = this.getClass().getResource("/").getPath()
				.replaceFirst("/", "") + Constant.DB_CONFIG_FILE_PATH;
		try {
			//将20%转换成空格(jdk的bug)
			DbConfigFloder_Path = java.net.URLDecoder.decode(DbConfigFloder_Path,"UTF-8");
			File DbFloder = new File(DbConfigFloder_Path);
			List<String> ConfigFileList = new ArrayList<String>();
			// 遍configFile目录下的properties文件放入ConfigFileList中
			if (DbFloder.isDirectory()) {
				File[] fileArray = DbFloder.listFiles();
				for (int i = 0; i < fileArray.length; i++) {
					if (!fileArray[i].isDirectory()
							&& (fileArray[i].getName().substring(fileArray[i].getName().lastIndexOf(".") + 1)
									.toLowerCase()).equals("properties")) {
						ConfigFileList.add(fileArray[i].getPath());
					}
				}
			}else {
				throw new Exception("Unable To Load DataBase Property File");
			}
			// 读取数据库配置文件
			if(ConfigFileList.size() > 0) {
				for (int i = 0; i < ConfigFileList.size(); i++) {
					Properties properties = new Properties();
					InputStream in = new BufferedInputStream(new FileInputStream(ConfigFileList.get(i)));
					properties.load(in);
					// 读取配置文件的配置属性
					String JdbcUrl = properties.getProperty("JdbcUrl");//JdbcUrl
					String User = properties.getProperty("User");//账号
					String Pswd = properties.getProperty("Pswd");//密码
					String DbLinkKey = properties.getProperty("DbLinkKey");//key
					String DbType = properties.getProperty("DbType");//数据库类型
					boolean ShowSQL = Boolean.parseBoolean(properties.getProperty("ShowSQL"));//是否打印sql
					
					DruidPlugin druidPlugin = new DruidPlugin(JdbcUrl, User, Pswd);
					me.add(druidPlugin);
					ActiveRecordPlugin arp = null;
					if(null == DbLinkKey || "".equals(DbLinkKey.trim())) {
						arp = new ActiveRecordPlugin(druidPlugin);
					}else {
						 arp = new ActiveRecordPlugin(DbLinkKey,druidPlugin);
					}
					//设置方言
					//mysql方言
					if(Constant.MYSQL_STR.equals(DbType.toUpperCase())) {
						arp.setDialect(new MysqlDialect());
					}
					//oracle方言
					if(Constant.ORACLE_STR.equals(DbType.toUpperCase())) {
						arp.setDialect(new OracleDialect());
					}
					//sqlserver方言
					if(Constant.SQLSERVER_STR.equals(DbType.toUpperCase())) {
						arp.setDialect(new SqlServerDialect());
					}
					//sql lite3方言
					if(Constant.SQLITE3_STR.equals(DbType.toUpperCase())) {
						arp.setDialect(new Sqlite3Dialect());
					}
					//是否打印sql语句
					arp.setShowSql(ShowSQL);
					me.add(arp);
					
					in.close();
					properties.clear();
				}
			}else {
				throw new Exception("Unable To Load DataBase Property File");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
