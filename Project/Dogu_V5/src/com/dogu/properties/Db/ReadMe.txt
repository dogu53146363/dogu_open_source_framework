一个properties文件是一个数据库链接
可以添加多个properties(数据库连接配置)文件来配置多个数据库链接
支持多种数据源同时连接

使用方法如下:
1.若DbLinkKey为空则可以直接使用:Db.find(SQL)
2.若DbLinkKey有值则使用:Db.use(DbLinkKey).find(SQL);

获取Connection:
1.若DbLinkKey为空则可以直接使用:
Connection con = DbKit.getConfig().getDataSource().getConnection();
2.若DbLinkKey有值则使用:
Connection con = DbKit.getConfig(DbLinkKey).getDataSource().getConnection();

动态添加数据库连接：
String url = "jdbc:mysql://localhost:3306/dogu?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=GMT%2B8";
String username = "root";
String password = "root";
String drive = "com.mysql.cj.jdbc.Driver";
Map<String, String> map = new HashMap<>();
map.put(DruidDataSourceFactory.PROP_DRIVERCLASSNAME, drive);
map.put(DruidDataSourceFactory.PROP_URL, url);
map.put(DruidDataSourceFactory.PROP_USERNAME, username);
map.put(DruidDataSourceFactory.PROP_PASSWORD, password);

Dialect dialect = new MysqlDialect();
boolean showSql = true;
boolean devMode = false;
int transactionLevel = 0;
ICache cache = new EhCache();
Config config = new Config("NEW_DB_KEY", DruidDataSourceFactory.createDataSource(map), dialect, showSql, devMode, transactionLevel, IContainerFactory.defaultContainerFactory, cache);
DbKit.addConfig(config);