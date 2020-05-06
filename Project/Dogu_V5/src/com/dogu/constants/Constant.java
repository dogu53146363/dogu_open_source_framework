package com.dogu.constants;

public interface Constant {
	
	//常用String格式字符串
	String EMPTYSTR = "";
	String STR_LOWERCASE_NULL = "null";
	String STR_CAPITAL_NULL = "NULL";
	String STR_ZERO = "0";
	String STR_ONE = "1";
	String STR_TWO = "2";
	String STR_THREE = "3";
	String STR_FOUR = "4";
	String STR_FIVE = "5";
	String STR_SIX = "6";
	String STR_SEVEN = "7";
	String STR_EIGHT = "8";
	String STR_NONE = "9";
	//Y OR N
	String CAPITAL_STR_Y = "Y";
	String CAPITAL_STR_N = "N";
	//功能行权限类型
	String CAPITAL_STR_USER = "USER";
	String CAPITAL_STR_ROLE = "ROLE";
	String CAPITAL_STR_ORG = "ORG";
	String CAPITAL_STR_ALL = "ALL";
	//首页
	String INDEXJSP = "index.jsp";
	//短信发送(接口方式/自持设备)
	String FACILITATOR = "FACILITATOR";
	String SELFDEVICE = "SELFDEVICE";
	//页面类型后缀名
	String DOJSP = ".jsp";
	String DOJSPX = ".jspx";
	String DOHTM = ".htm";
	String DOHTML = ".html";
	String DOSHTM = ".shtm";
	String DOSHTML = ".shtml";
	String JSP = "jsp";
	String HTM = "htm";
	String HTML = "html";
	String SHML = "shtm";
	String SHTML = "shtml";
	//Excel文件后缀名
	String DOXLS = ".xls";
	String DOXLSX = ".xlsx";
	String XLS = "xls";
	String XLSX = "xlsx";
	//图片文件后缀名
	String DOPNG = ".png";
	String DOJPG = ".jpg";
	String DOGIF = ".gif";
	String DOJPEG = ".jpeg";
	String DOBMP = ".bmp";
	String PNG = "png";
	String JPG = "jpg";
	String GIF = "gif";
	String JPEG = "jpeg";
	String BMP = "bmp";
	//常用整形数字
	int INT_ONE = 1;
	int INT_TWO = 2;
	int INT_THREE = 3;
	int INT_FOUR = 4;
	int INT_FIVE = 5;
	int INT_SIX = 6;
	int INT_SEVEN = 7;
	int INT_EIGHT = 8;
	int INT_NINE = 9;
	//序数常量
	String FIRST = "FIRST";
	String SECOND = "SECOND";
	String THIRD = "THIRD";
	String FOURTH = "FOURTH";
	String FIFTH = "FIFTH";
	String SIXTH = "SIXTH";
	String SEVENTH = "SEVENTH";
	String EIGHTH = "EIGHTH";
	String NINTH = "NINTH";
	//main页面路径
	String MAINPAGEPATH = "/sysmanage/main/jsp/main.jsp";
	//404页面路径
	String PAGE404PATH = "/sysmanage/404page/jsp/404.jsp";
	//本地语言
	String DEFAULTLOCALE = "zh_CN";
	String EMPTY_MD5 = "d41d8cd98f00b204e9800998ecf8427e";
	String REGSTATUE = "REG";
	String CAPITAL_OK = "OK";
	String LOWERCASE_OK = "ok";
	String CAPITAL_MSG = "MSG";
	String CAPITAL_MAIL = "MAIL";
	String LOWERCASE_TRUE = "true";
	String CAPITAL_TRUE = "TRUE";
	String LOWERCASE_FALSE = "false";
	String CAPITAL_FALSE = "FALSE";
	String LOWERCASE_DOGU = "dogu";
	String CAPITAL_DOGU = "DOGU";
	//数据库配置文件存放路径
	String DB_CONFIG_FILE_PATH = "/com/dogu/properties/Db";
	//CSRF配置文件存放路径
	String CSRF_CONFIG_FILE_PATH = "/com/dogu/properties/Csrf";
	//Http request header配置文件存放路径
	String HTTPRQ_HEADER_CONFIG_FILE_PATH = "/com/dogu/properties/RequestHeader";
	//数据库类型
	String MYSQL_STR = "MYSQL";
	String ORACLE_STR = "ORACLE";
	String SQLSERVER_STR = "SQLSERVER";
	String SQLITE3_STR = "SQLITE3";
	//默认密码
	String DEFAULT_PSW = "e10adc3949ba59abbe56e057f20f883e";
	//菜单/功能配置
	String CAPITAL_STR_MENU = "MENU";
	String CAPITAL_STR_FUNC = "FUNC";
	//Session存储key
	String SESSION_STORAGE = "SESSION_STORAGE";
}
