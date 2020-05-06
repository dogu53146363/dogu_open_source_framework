package com.dogu.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

public class GetMechineInfo {
	/**
	 * 获取IP地址
	 * @return
	 * @throws UnknownHostException
	 */
	public static InetAddress getInetAddress() throws UnknownHostException {
		InetAddress address = InetAddress.getLocalHost();
		return address;
	}

	/**
	 * 获取mac地址
	 * @param ia
	 * @return
	 * @throws SocketException
	 */
	public static String getMacAddress(InetAddress ia) throws SocketException {
		byte[] mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();
		StringBuffer sb = new StringBuffer("");
		for (int i = 0; i < mac.length; i++) {
			if (i != 0) {
				sb.append("-");
			}
			// 字节转换为整数
			int temp = mac[i] & 0xff;
			String str = Integer.toHexString(temp);
			if (str.length() == 1) {
				sb.append("0" + str);
			} else {
				sb.append(str);
			}
		}
		return sb.toString().toUpperCase();
	}
}
