package com.dogu.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.regex.Pattern;

public class CommonUtils {

	/**
	 * 格式化时间到小时
	 */
	public static String dateFormat(String date) {
		String year = null;
		String month = null;
		String day = null;
		String hour = null;
		String returndate = null;
		if (date.length() >= 10) {
			year = date.substring(0, 4);
			month = date.substring(4, 6);
			day = date.substring(6, 8);
			hour = date.substring(8, 10);
			returndate = year + "-" + month + "-" + day + " " + hour;
		} else {
			System.out.println("需要格式化的日期位数不够！");
			Date dt = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH");
			return sdf.format(dt);
		}
		return returndate;
	}

	/**
	 * 生成UUID
	 */
	public static String creatUUID() {
		return UUID.randomUUID().toString().replace("-", "");
	}
	
	/**
	 * 验证是否只为数字和字母
	 */
	public static boolean isLetterDigit(String str) {
		String regex = "[0-9A-Za-z]*";
		return str.matches(regex);
	}

	/**
	 * 验证是否只为数字
	 */
	public static boolean isNumber(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(str).matches();
	}
	
	/**
	 * unocode转string
	 */
	public static String toUnocode(String gbString) {
		char[] utfBytes = gbString.toCharArray();
		String unicodeBytes = "";
		for (int byteIndex = 0; byteIndex < utfBytes.length; byteIndex++) {
			String hexB = Integer.toHexString(utfBytes[byteIndex]);// 转换为16进制整型字符串
			if (hexB.length() <= 2) {
				hexB = "00" + hexB;
			}
			unicodeBytes = unicodeBytes + hexB;
		}
		return unicodeBytes.toUpperCase();
	}
}
