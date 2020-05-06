package com.dogu.config;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.wall.WallFilter;
import com.dogu.constants.Constant;
import com.dogu.utils.Properties2List;
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

	String dbLinkKye = "";
	/**
	 * 数据库注册
	 * @param me
	 */
	public DataBaseConfig(Plugins plugins) {
		// 数据库配置文件目录
		try {
			//获取配置文件List
			List<String> ConfigFileList = 
					Properties2List.getList(Constant.DB_CONFIG_FILE_PATH);
			// 读取数据库配置文件
			if(ConfigFileList.size() > 0) {
				Properties properties = null;
				InputStream in = null;
				// 读取配置文件的配置属性
				String JdbcUrl =  null;
				String User =  null;
				String Pswd =  null;
				String DbLinkKey =  null;
				String DbType =  null;
				int initialSize = 5;
				int minIdle = 5;
				int maxActive = 10;
				boolean ShowSQL = false;
				for (int i = 0; i < ConfigFileList.size(); i++) {
					properties = new Properties();
					in = new BufferedInputStream(new FileInputStream(ConfigFileList.get(i)));
					properties.load(in);
					// 读取配置文件的配置属性
					JdbcUrl = properties.getProperty("JdbcUrl");//JdbcUrl
					User = properties.getProperty("User");//账号
					Pswd = properties.getProperty("Pswd");//密码
					DbLinkKey = properties.getProperty("DbLinkKey");//key
					dbLinkKye = DbLinkKey;
					DbType = properties.getProperty("DbType");//数据库类型
					ShowSQL = Boolean.parseBoolean(properties.getProperty("ShowSQL"));//是否打印sql
					initialSize = Integer.parseInt(properties.getProperty("InitialSize"));
					minIdle = Integer.parseInt(properties.getProperty("MinIdle"));
					maxActive = Integer.parseInt(properties.getProperty("MaxActive"));
					DruidPlugin druidPlugin = new DruidPlugin(JdbcUrl, User, Pswd);
					druidPlugin.addFilter(new StatFilter());
					WallFilter wall = new WallFilter();
					wall.setDbType(DbType.toLowerCase().trim().toString());
					druidPlugin.addFilter(wall);
					druidPlugin.setInitialSize(initialSize);//初始化连接数
					druidPlugin.setMinIdle(minIdle);//最小活跃连接数
					druidPlugin.setMaxActive(maxActive);//最大连接数
					plugins.add(druidPlugin);
					
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
					plugins.add(arp);
					
					in.close();
					properties.clear();
				}
			}else {
				System.err.println("------Can not find any dataBase property------");
			}
		} catch (Exception e) {
			System.err.println("------load ["+dbLinkKye+"] dataBase property file filed!------");
		}
	}
}
