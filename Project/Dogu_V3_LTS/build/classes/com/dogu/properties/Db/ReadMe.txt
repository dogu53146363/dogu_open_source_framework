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