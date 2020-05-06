package com.dogu.authorization;

import java.net.InetAddress;

import com.dogu.constants.Constant;
import com.dogu.utils.GetMechineInfo;
import com.dogu.utils.GetProperties;
import com.dogu.utils.Md5;

public class Authorization {
	public static boolean authorization() {
		// 是否打开授权
		boolean Authorization = AuthorizationStatus.authorizationStatus();
		boolean returnAuthorization = false;
		// 授权文件匹配
		if (Authorization) {
			try {
				InetAddress InetAddress = GetMechineInfo.getInetAddress();
				String iPAddress = InetAddress.getHostAddress();
				iPAddress = iPAddress.trim();
				iPAddress = iPAddress.replaceAll("\\.", "");
				String filepath = "/com/dogu/authorization/authorizationFile/IP" + iPAddress + "";
				String MacAddress = GetMechineInfo.getMacAddress(InetAddress);
				String key = GetProperties.readValue(filepath, MacAddress);
				String localvalue = Md5.SmallMd5(MacAddress + "-" + Constant.CAPITAL_DOGU);
				if (key.equals(localvalue)) {
					returnAuthorization = true;
				}
			} catch (Exception e) {
				returnAuthorization = false;
			}
		} else {
			returnAuthorization = true;
		}
		return returnAuthorization;
	}
}