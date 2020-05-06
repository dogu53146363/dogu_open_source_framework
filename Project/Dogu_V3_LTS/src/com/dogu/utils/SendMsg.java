package com.dogu.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.dogu.constants.Constant;

public class SendMsg {
	
	/**
	 * 发送短信工具类
	 * phonenumber 手机号码
	 * prefix 标题
	 * msg 信息内容
	 * method 发送方法：服务商发送/自持设备发送
	 */
	public String sendMsg(String phonenumber, String prefix, String msg,
			String method) throws NoSuchAlgorithmException,
			UnsupportedEncodingException, DocumentException {
		
		String returnmsg = "";
		
		if(Constant.FACILITATOR.equals(method)){//服务商接口发送验证码
			String password = GetConFromDB.GetCIFromDB("MESSASG_SERVER_PASSWORD");
			password = Md5.BigMd5(password);
			// 短信内容做一个utf-8编码
			String content = URLEncoder.encode("【" + prefix + "】" + msg, "UTF-8");
			// 拼接参数
			String postData = "type=send&username=" + GetConFromDB.GetCIFromDB("MESSASG_SERVER_ACCOUNT") 
					+"&password=" + password
					+ "&gwid=a9da689&mobile=" + phonenumber + "&message=" + content
					+ "";
			String url = GetConFromDB.GetCIFromDB("MESSASG_SERVER_ADDR");
			String result = HttpRequest.sendPost(url, postData);
			System.out.println(result);
			Document document = DocumentHelper.parseText(result);
			Element root = document.getRootElement();
			Element codelem = root.element("code");
			if (Constant.STR_ZERO.equals(codelem.getStringValue())) {
				returnmsg = "OK";
			} else {
				System.out.println(result);
				returnmsg = "短信服务器错误，错误代码：" + codelem.getStringValue();
			}
		}else if(Constant.SELFDEVICE.equals(method)){//自持设备发送验证码
			String result = "";
	        BufferedReader in = null;
	        String url = GetConFromDB.GetCIFromDB("LOCALHOST_MSG_SVR_ADDR");
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
				while (null != (line = in.readLine())) {
					result += line;
				}
				returnmsg = result;
			} catch (Exception e) {
				e.printStackTrace();
				returnmsg = "发送短信出现异常！"+e;
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
		return returnmsg;
	}
}
