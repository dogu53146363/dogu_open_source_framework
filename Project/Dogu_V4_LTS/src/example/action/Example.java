package example.action;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.alibaba.fastjson.JSONObject;
import com.dogu.utils.DoguCaptchaRender;
import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.jfinal.core.NotAction;
import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.render.JsonRender;
import com.jfinal.upload.UploadFile;

import example.utils.CommonUtils;
import example.utils.FileService;
import example.utils.HttpRequest;
import example.utils.Md5;

/**
 * 框架的示例Action
 * (建议不要在该action中进行开发)
 * @author Dogu
 */
public class Example extends Controller {

	/**
	 * 文件上传
	 */
	public void fileUpload() {
		UploadFile uploadFile = this.getFile();
		JSONObject json = new JSONObject();
		String fileName = uploadFile.getOriginalFileName();
		String extension = fileName.substring(fileName.lastIndexOf("."));
		File file = uploadFile.getFile();
		if (file.length() > 30 * 1024 * 1024) {
			json.put("error", 1);
			json.put("message", "文件大小超出限制！");
		} else if (".png".equals(extension) || ".jpg".equals(extension)
				|| ".gif".equals(extension) || ".jpeg".equals(extension)
				|| ".bmp".equals(extension)) {
			FileService fs = new FileService();
			String savePath = PathKit.getWebRootPath() + "/upload/img/";// 此savepath是放在工程路径下的适合保存图片,然后在页面上显示出来
			String returnName = UUID.randomUUID().toString();
			String Savefilename = returnName + extension;
			// String savePath =
			// "D:\\file\\"+UUID.randomUUID().toString()+extension;//此savepath是放在服务器硬盘目录上的,适合存文件如果使用该路径则需要先建好相应的文件夹路径
			File floder = new File(savePath);
			if (!floder.exists()) {
				floder.mkdir();
			}
			File f = new File(savePath + Savefilename);
			try {
				f.createNewFile();
				json.put("error", 0);
				json.put("message", "文件:" + fileName + "上传成功！"); // 相对地址，显示图片用
				json.put("src", "upload/img/" + Savefilename); // 如果是图片的话则打开此句,则可回传图片路径在页面上显示图片,注：路径必须是在工程的WebRoot内
			} catch (IOException e) {
				e.printStackTrace();
				json.put("error", 1);
				json.put("message", e);
			}
			fs.fileChannelCopy(file, f);
			file.delete();
		} else {
			json.put("error", 1);
			json.put("message", "只允许上传png,jpg,jpeg,gif,bmp类型的图片文件！");
		}
		setAttr("success", true);
		render(new JsonRender(json).forIE());
	}

	/**
	 * 不被拦截器拦截的方法示例
	 */
	@Clear
	public void NoInterceptor() {
		String backstr = "不被拦截器拦截的Action";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("message", backstr);
		this.renderJson(map);
	}

	/**
	 * 查询用户查询的地区数量,如果是一个则继续往下查询,如果不是则提示
	 */
	public void getAreaNUm() {
		String area = this.getPara("area");
		try {
			area = URLDecoder.decode(area, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		area = area.replace("'", "");
		area = area.replace("\"", "");
		Map<String, Object> map = new HashMap<String, Object>();
		String sql = "SELECT COUNT(1) AS NUM FROM (SELECT DISTINCT(env.F_AREA) FROM DOGU_ENVIRONMENT env WHERE env.F_AREA LIKE '%"
				+ area + "%') result";
		Record rec = Db.findFirst(sql);
		String num = rec.get("NUM").toString();
		if ("1".equals(num)) {
			map.put("code", 0);
		} else {
			map.put("code", 1);
			map.put("message", "没有该区域或查询的为多个区域！");
		}
		this.renderJson(map);
	}

	/**
	 * Echarts查询
	 */
	public void getEchartsdata() {
		String area = this.getPara("area");
		try {
			area = URLDecoder.decode(area, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		area = area.replace("'", "");
		area = area.replace("\"", "");
		String object = this.getPara("object");
		Map<String, Object> map = new HashMap<String, Object>();
		List<Record> timelist = null;
		String time = "";
		String value = "";
		List<Record> valueist = null;
		String sql = "SELECT F_CT FROM DOGU_ENVIRONMENT WHERE F_AREA LIKE '%"
				+ area + "%' ORDER BY F_CT";
		timelist = Db.find(sql);
		for (Record rec : timelist) {
			time += CommonUtils.dateFormat(rec.getStr("F_CT")) + ",";
		}
		sql = "SELECT " + object
				+ " FROM DOGU_ENVIRONMENT WHERE F_AREA LIKE '%" + area
				+ "%' ORDER BY F_CT";
		valueist = Db.find(sql);
		for (Record rec : valueist) {
			value += rec.getStr(object) + ",";
		}
		map.put("time", time);
		map.put("value", value);
		this.renderJson(map);
	}

	/**
	 * 获取ueditor中的数据
	 */
	public void getUeditordata() {
		String account = this.getSessionAttr("F_ACCOUNT");
		Map<String, Object> map = new HashMap<String, Object>();
		Record rec = Db.findFirst(
				"SELECT F_CLOB FROM DOGU_CLOB WHERE F_ACCOUNT = ?", account);
		if (rec != null) {
			String clob = rec.get("F_CLOB").toString();
			map.put("message", clob);
		} else {
			map.put("message", "");
		}
		this.renderJson(map);
	}

	/**
	 * 往库里面的clob字段中保存ueditor内容
	 */
	@Clear(com.dogu.interceptor.XSSInterceptor.class)
	public void saveUeditor() {
		String account = this.getSessionAttr("F_ACCOUNT");
		String data = this.getPara("data");
		try {
			data = URLDecoder.decode(data, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		Map<String, Object> map = new HashMap<String, Object>();
		Record rec = Db.findFirst(
				"SELECT F_ACCOUNT FROM DOGU_CLOB WHERE F_ACCOUNT = ?", account);
		if (rec != null) {
			Db.update("UPDATE DOGU_CLOB SET F_CLOB = ? WHERE F_ACCOUNT = ?",
					data, account);
		} else {
			Db.update("INSERT INTO DOGU_CLOB (F_ACCOUNT,F_CLOB) VALUES (?,?)",
					account, data);
		}
		map.put("message", "保存成功！");
		this.renderJson(map);
	}

	/**
	 * jqgride获取数据
	 */
	public void getJqGride() {
		String jsondata = "{\"page\":\"1\","
				+ "      \"total\":2,"
				+ "      \"records\":\"13\","
				+ "      \"rows\":"
				+ "          ["
				+ "            {"
				+ "              \"id\":\"13\","
				+ "              \"cell\":"
				+ "                  [\"13\",\"2007-10-06\",\"Client 3\",\"1000.00\",\"0.00\",\"1000.00\",null]"
				+ "            },"
				+ "            {"
				+ "              \"id\":\"12\","
				+ "              \"cell\":"
				+ "                  [\"12\",\"2007-10-06\",\"Client 2\",\"700.00\",\"140.00\",\"840.00\",null]"
				+ "            },"
				+ "            {"
				+ "              \"id\":\"11\","
				+ "              \"cell\":"
				+ "                  [\"11\",\"2007-10-06\",\"Client 1\",\"600.00\",\"120.00\",\"720.00\",null]"
				+ "            },"
				+ "            {"
				+ "              \"id\":\"10\","
				+ "              \"cell\":"
				+ "                  [\"10\",\"2007-10-06\",\"Client 2\",\"100.00\",\"20.00\",\"120.00\",null]"
				+ "            },"
				+ "            {"
				+ "              \"id\":\"9\","
				+ "              \"cell\":"
				+ "                  [\"9\",\"2007-10-06\",\"Client 1\",\"200.00\",\"40.00\",\"240.00\",null]"
				+ "            },"
				+ "            {"
				+ "              \"id\":\"8\","
				+ "              \"cell\":"
				+ "                  [\"8\",\"2007-10-06\",\"Client 3\",\"200.00\",\"0.00\",\"200.00\",null]"
				+ "            },"
				+ "            {"
				+ "              \"id\":\"7\","
				+ "              \"cell\":"
				+ "                  [\"7\",\"2007-10-05\",\"Client 2\",\"120.00\",\"12.00\",\"134.00\",null]"
				+ "            },"
				+ "            {"
				+ "              \"id\":\"6\","
				+ "              \"cell\":"
				+ "                  [\"6\",\"2007-10-05\",\"Client 1\",\"50.00\",\"10.00\",\"60.00\",\"\"]"
				+ "            },"
				+ "            {"
				+ "              \"id\":\"5\","
				+ "              \"cell\":"
				+ "                  [\"5\",\"2007-10-05\",\"Client 3\",\"100.00\",\"0.00\",\"100.00\",\"no tax at all\"]"
				+ "            },"
				+ "            {"
				+ "              \"id\":\"4\","
				+ "              \"cell\":"
				+ "                  [\"4\",\"2007-10-04\",\"Client 3\",\"150.00\",\"0.00\",\"150.00\",\"no tax\"]"
				+ "            }"
				+ "          ],"
				+ "      \"userdata\":{\"amount\":3220,\"tax\":342,\"total\":3564,\"name\":\"Totals:\"}"
				+ "    }";
		this.renderJson(jsondata);
	}

	/**
	 * 获取Excel的数据
	 */
	public void getTableData() {
		String sql = "SELECT F_NAME,F_AGE,F_SEX,F_HOBBY FROM DOGU_EXCEL";
		List<Record> list = Db.find(sql);
		this.renderJson(list);
	}

	/**
	 * Excel上传
	 */
	public void ExcelUpload() {
		UploadFile uploadFile = this.getFile();
		JSONObject json = new JSONObject();
		String fileName = uploadFile.getOriginalFileName();
		String extension = fileName.substring(fileName.lastIndexOf("."));
		File file = uploadFile.getFile();
		if (file.length() > 30 * 1024 * 1024) {
			json.put("error", 1);
			json.put("message", "文件大小超出限制！");
		} else if (".xls".equals(extension) || ".xlsx".equals(extension)) {
			List<String> list = null;
			try {
				list = getExcelOneRow(file);
			} catch (EncryptedDocumentException e1) {
				e1.printStackTrace();
			} catch (InvalidFormatException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			for (int i = 0; i < list.size(); i++) {
				String sql = "INSERT INTO DOGU_EXCEL (F_NAME,F_AGE,F_SEX,F_HOBBY,F_UUID) VALUES ("
						+ list.get(i) + ")";
				try {
					Db.update(sql);
				} catch (Exception e) {
					json.put("error", 1);
					json.put("message", e.toString());
					break;
				}
				json.put("error", 0);
				json.put("message", "导入成功！");
			}
		} else {
			json.put("error", 1);
			json.put("message", "只允许上传.xls或.xlsx类型的文件！");
		}
		file.delete();// 删除文件
		setAttr("success", true);
		render(new JsonRender(json).forIE());
	}

	/**
	 * 获取Excel的一行，并将改行的数据以list的方式返回
	 */
	@NotAction
	@SuppressWarnings("deprecation")
	public List<String> getExcelOneRow(File file)
			throws EncryptedDocumentException, InvalidFormatException,
			IOException {
		InputStream ins = null;
		Workbook wb = null;
		ins = new FileInputStream(file);
		wb = WorkbookFactory.create(ins);
		ins.close();
		// 3.得到Excel工作表对象
		Sheet sheet = wb.getSheetAt(0);
		// 总行数这个是最后一行的下标值,比实际行数少一
		int trLength = sheet.getLastRowNum();
		// 4.得到Excel工作表的第一行(为了取列数)
		Row row = sheet.getRow(0);
		// 总列数这个是最后一列的列数(不是下标数是实际的列数)
		int tdLength = row.getLastCellNum();
		List<String> list = new ArrayList<String>();
		String col = "";
		for (int i = 0; i < trLength + 1; i++) {
			col = "";
			// 得到Excel工作表的行
			Row row1 = sheet.getRow(i);
			// 第几列
			for (int j = 0; j < tdLength; j++) {
				// 得到Excel工作表指定行的单元格
				Cell cell = row1.getCell(j);
				/**
				 * 为了处理：Excel异常Cannot get a text value from a numeric cell
				 * 将所有列中的内容都设置成String类型格式
				 */
				if (cell != null) {
					cell.setCellType(Cell.CELL_TYPE_STRING);
				}
				// 获得每一列中的值
				if ("".equals(cell + "")) {
					col += "'"+"0"+"',";
				} else {
					col += "'" + cell + "',";
				}
			}
			list.add(col + "'" + CommonUtils.creatUUID() + "'");
		}
		return list;
	}
	
	/**
	 * 下载excel模板
	 */
	
	public void downLoadExcel() {
		String filePath = PathKit.getWebRootPath() + "\\example\\excel\\ExcelTemplet\\ExcelTemplet.xls";
		File fl = new File(filePath);
		this.renderFile(fl);
	}

	/**
	 * 从数据库里面获取数据并导出Excel
	 */
	@SuppressWarnings("resource")
	public void ExportExcel() {
		List<String> UUIDlist = new ArrayList<String>();
		String sql = "SELECT F_UUID FROM DOGU_EXCEL ORDER BY F_NAME";
		List<Record> list = Db.find(sql);
		for (Record rec : list) {
			UUIDlist.add(rec.getStr("F_UUID"));
		}
		// 创建HSSFWorkbook对象(excel的文档对象)
		HSSFWorkbook wb = new HSSFWorkbook();
		// 建立新的sheet对象（excel的表单）
		HSSFSheet sheet = wb.createSheet("人员信息");
		// 设置列大小第2列
		sheet.setColumnWidth(1, 25 * 256);
		// 设置列大小第3列
		sheet.setColumnWidth(2, 15 * 256);
		// 设置列大小第4列
		sheet.setColumnWidth(3, 15 * 256);

		HSSFRow rowtitle = sheet.createRow(0);

		HSSFCell cell = rowtitle.createCell(0);
		cell.setCellValue("姓名");

		cell = rowtitle.createCell(1);
		cell.setCellValue("年龄");

		cell = rowtitle.createCell(2);
		cell.setCellValue("性别");

		cell = rowtitle.createCell(3);
		cell.setCellValue("爱好");

		ArrayList<String> Innerlist = new ArrayList<String>();
		for (int i = 0; i < UUIDlist.size(); i++) {
			sql = "SELECT F_NAME,F_AGE,F_SEX,F_HOBBY FROM DOGU_EXCEL WHERE F_UUID = '"
					+ UUIDlist.get(i) + "'";
			Record rec = Db.findFirst(sql);
			Innerlist = new ArrayList<String>();
			Innerlist.add(rec.get("F_NAME").toString());
			Innerlist.add(rec.get("F_AGE").toString());
			Innerlist.add(rec.get("F_SEX").toString());
			Innerlist.add(rec.get("F_HOBBY").toString());
			HSSFRow row = sheet.createRow(i + 1);
			for (int j = 0; j < 4; j++) {
				cell = row.createCell(j);
				cell.setCellValue(Innerlist.get(j));
			}
		}
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		// 将wb写入到byte[]中
		try {
			wb.write(os);
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] xls = os.toByteArray();
		String tempfilepath = PathKit.getWebRootPath()
				+ "/upload/ExcelDownloadTemp/";
		tempfilepath = tempfilepath.replace("\\", "\\\\");
		File TempFolder = new File(tempfilepath);
		if (!TempFolder.exists()) {
			TempFolder.mkdirs();
		}
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");// 可以方便地修改日期格式
		String TimeFilename = dateFormat.format(date);
		File TempOutFile = new File(tempfilepath + TimeFilename + ".xls");
		if (!TempOutFile.exists()) {
			try {
				TempOutFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// 输出
		OutputStream out = null;
		try {
			out = new FileOutputStream(TempOutFile);
			try {
				out.write(xls);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} finally {
			try {
				out.flush();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		this.renderFile(TempOutFile);
	}

	/**
	 * 删除Excel表中的所有数据
	 */
	public void deleteExcelData() {
		String sql = "DELETE FROM DOGU_EXCEL";
		Db.update(sql);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("message", "删除成功！");
		this.renderJson(map);
	}

	/**
	 * 验证码(返回图片)示例
	 */
	public void VerificationCode() {
		render(new DoguCaptchaRender(60, 22, 4, true));
	}

	/**
	 * 验证验证码是否正确
	 */
	public void CheckVerificationCode() {
		String InputVerificationCode = this.getPara("VerificationCode");
		boolean validate = DoguCaptchaRender
				.validate(this, InputVerificationCode);
		Map<String, Object> map = new HashMap<String, Object>();
		if (validate == true) {
			map.put("message", "验证码正确!");
		} else {
			map.put("message", "验证码错误!");
		}
		this.renderJson(map);
	}

	/**
	 * 发送短信
	 */
	public void SendMsg() {

		Map<String, Object> map = new HashMap<String, Object>();
		String returnmsg = "";
		
		String phonenumber = this.getPara("phonenumber");
		String prefix = this.getPara("prefix");
		try {
			prefix = URLDecoder.decode(prefix, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		String msg = this.getPara("msg");
		try {
			msg = URLDecoder.decode(msg, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		
		String method = this.getPara("method");
		if("FACILITATOR".equals(method)){
			String password = "123456";
			try {
				password = Md5.BigMd5(password);
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			// 短信内容做一个utf-8编码
			String content = null;
			try {
				content = URLEncoder.encode("【" + prefix + "】" + msg, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			// 拼接参数
			String postData = "type=send&username=dugu&password=" + password
					+ "&gwid=a9da689&mobile=" + phonenumber + "&message=" + content
					+ "";
			String url = "http://112.126.81.205:533//smsUTF8.aspx";
			String result = HttpRequest.sendPost(url, postData);
			System.out.println(result);
			Document document = null;
			try {
				document = DocumentHelper.parseText(result);
			} catch (DocumentException e) {
				e.printStackTrace();
			}
			Element root = document.getRootElement();
			Element codelem = root.element("code");
			if ("0".equals(codelem.getStringValue())) {
				returnmsg = "发送成功！";
			} else {
				System.out.println(result);
				returnmsg = "短信服务器错误，错误代码：" + codelem.getStringValue();
			}
			map.put("message", returnmsg);
		}else if("SELFDEVICE".equals(method)){
			String result = "";
	        BufferedReader in = null;
	        String url = "http://192.168.1.188/";
	        phonenumber = CommonUtils.toUnocode(phonenumber);
	        prefix = CommonUtils.toUnocode("【"+prefix+"】");
	        msg = CommonUtils.toUnocode(msg);
	        String param = "tel="+phonenumber+"&msg="+prefix+msg;
	        try {
	        	url = url + "?" + param;
				URL realUrl = new URL(url);
				System.out.println("--------------------------------------------------------------------------------");
				System.out.println("HTTP SEND : "+url);
				System.out.println("--------------------------------------------------------------------------------");
				// 打开和URL之间的连接
				URLConnection connection = realUrl.openConnection();
				// 设置通用的请求属性
				connection.setRequestProperty("accept", "*/*");
				connection.setRequestProperty("connection", "Keep-Alive");
				connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
				// 建立实际的连接
				connection.connect();
				// 获取所有响应头字段
				Map<String, List<String>> sendMap = connection.getHeaderFields();
				// 遍历所有的响应头字段
				for (String key : sendMap.keySet()) {
					System.out.println(key + "--->" + sendMap.get(key));
				}
				// 定义 BufferedReader输入流来读取URL的响应
				in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String line;
				while ((line = in.readLine()) != null) {
					result += line;
				}
				returnmsg = result;
				map.put("message", returnmsg);
			} catch (Exception e) {
				e.printStackTrace();
				map.put("message", "发送短信出现异常！"+e);
			}
			// 使用finally块来关闭输入流
			finally {
				try {
					if (in != null) {
						in.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		this.renderJson(map);
	}
	
	/**
	 * 获取doguFlow数据
	 */
	public void getFlowData() {
		
		String backData = "";
		//绘制头信息
		backData = "{\"title\":\"工作流\",";
		//拼node
		String nodeSql = "SELECT * FROM DOGU_FLOW WHERE F_MODEL = 'node'";
		List<Record> list = Db.find(nodeSql);
		if(list.size() >0){
			backData += "\"nodes\":{";
			for (Record rec : list) {
				backData += "\""+rec.getStr("F_ID")+"\":{";//拼ID
				backData += "\"name\":\""+rec.getStr("F_NAME")+"\",";
				backData += "\"left\":\""+rec.getStr("F_LEFT")+"\",";
				backData += "\"top\":\""+rec.getStr("F_TOP")+"\",";
				backData += "\"type\":\""+rec.getStr("F_TYPE")+"\",";
				backData += "\"width\":\""+rec.getStr("F_WIDTH")+"\",";
				backData += "\"height\":\""+rec.getStr("F_HEIGHT")+"\"},";
			}
			backData = backData.substring(0,backData.length()-1);//去掉最后一个多余的逗号
			backData += "},";
			//拼line
			String lineSql = "SELECT * FROM DOGU_FLOW WHERE F_MODEL = 'line'";
			list = Db.find(lineSql);
			if(list.size()>0){
				backData += "\"lines\":{";
				for (Record rec : list) {
					backData += "\""+rec.getStr("F_ID")+"\":{";//拼ID
					backData += "\"type\":\""+rec.getStr("F_TYPE")+"\",";
					backData += "\"from\":\""+rec.getStr("F_FROM")+"\",";
					backData += "\"to\":\""+rec.getStr("F_TO")+"\",";
					backData += "\"name\":\""+rec.getStr("F_NAME")+"\",";
					backData += "\"dash\":\""+rec.getStr("F_DASH")+"\",";
					backData += "\"alt\":\""+rec.getStr("F_ALT")+"\"},";
				}
				backData = backData.substring(0,backData.length()-1);//去掉最后一个多余的逗号
				backData += "},";
			}
			backData += "\"areas\":{}";
			backData += "}";
		}else{
			//处理没有的时候的情况
			backData = "{}";
		}
		this.renderJson(backData);
	}
	
	public void saveFlowData() {

		String jsonData = this.getPara("FlowData");
		if(null != jsonData){
			try {
				jsonData = URLDecoder.decode(jsonData, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		org.json.JSONObject json = new org.json.JSONObject(jsonData);
		String msg = "操作成功！";
		try{
			// 处理node
			HashMap<String, String> nodeMap = new HashMap<String, String>();
			org.json.JSONObject nodeJson = json.getJSONObject("nodes");
			Iterator<?> nodeIterator = nodeJson.keys();
			while (nodeIterator.hasNext()) {
				String key = (String) nodeIterator.next();
				nodeMap.put(key, (nodeJson.get(key)).toString());
			}
			Iterator<Entry<String, String>> nodEntries = nodeMap.entrySet().iterator();
			org.json.JSONObject nodeChileJson = null;
			Db.update("DELETE FROM DOGU_FLOW");
			while (nodEntries.hasNext()) {
				Entry<String, String> entry = nodEntries.next();
				nodeChileJson = new org.json.JSONObject(entry.getValue());
				Db.update(
						"INSERT INTO DOGU_FLOW (F_ID,F_NAME,F_LEFT,F_TOP,F_TYPE,F_WIDTH,F_HEIGHT,F_MODEL) VALUES (?,?,?,?,?,?,?,?)",
						entry.getKey(), nodeChileJson.get("name"),
						nodeChileJson.get("left"), nodeChileJson.get("top"),
						nodeChileJson.get("type"), nodeChileJson.get("width"),
						nodeChileJson.get("height"), "node");
			}
			//处理line
			HashMap<String, String> lineMap = new HashMap<String, String>();
			org.json.JSONObject lineJson = json.getJSONObject("lines");
			Iterator<?> lineIterator = lineJson.keys();
			while (lineIterator.hasNext()) {
				String key = (String) lineIterator.next();
				lineMap.put(key, (lineJson.get(key)).toString());
			}
			Iterator<Entry<String, String>> linEntries = lineMap.entrySet().iterator();
			org.json.JSONObject lineChileJson = null;
			while (linEntries.hasNext()) {
				Entry<String, String> LinEntry = linEntries.next();
				lineChileJson = new org.json.JSONObject(LinEntry.getValue());
				Db.update(
						"INSERT INTO DOGU_FLOW (F_ID,F_NAME,F_TYPE,F_FROM,F_TO,F_MODEL,F_ALT,F_DASH) VALUES (?,?,?,?,?,?,?,?)",
						LinEntry.getKey(), "", lineChileJson.get("type"),
						lineChileJson.get("from"), lineChileJson.get("to"),
						"line","true","false");
			}
		}catch (Exception e) {
			e.printStackTrace();
			int name = e.toString().indexOf("name");
			int left = e.toString().indexOf("left");
			int top = e.toString().indexOf("top");
			int type = e.toString().indexOf("type");
			int width = e.toString().indexOf("width");
			int height = e.toString().indexOf("height");
			if(name > 0){
				msg = "保存失败，没有指定节点名称！";
			}else if(left > 0){
				msg = "保存失败，没有指定节点左方距离！";
			}else if(top > 0){
				msg = "保存失败，没有指定节点上方距离！";
			}else if(type > 0){
				msg = "保存失败，没有指定节点类型！";
			}else if(width > 0){
				msg = "保存失败，没有指定节点宽度！";
			}else if(height > 0){
				msg = "保存失败，没有指定节点高度！";
			}else {
				msg = e.toString();
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("message", msg);
		this.renderJson(map);
	}
	
	/**
	 * noVNC获取主机列表
	 */
	public void getnoVNCServerListInfo() {
	    String serverName = this.getPara("serverName");
	    try {
			serverName = URLDecoder.decode(serverName,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
        this.renderJson("{flag:true}");
	}
	
	/**
	 * websocket连接前先查询名字是否被占用了
	 */
	public void QueryOnlinePerson() {
	    String UserName = this.getPara("USERNAME");
	    try {
			UserName = URLDecoder.decode(UserName,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	    Record rc = Db.findFirst("SELECT COUNT(1) AS F_NUM FROM DOGU_SOCKET WHERE F_USERNAME = ?",UserName);
        this.renderJson(rc);
	}
}
