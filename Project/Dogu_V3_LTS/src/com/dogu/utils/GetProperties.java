package com.dogu.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import com.mchange.v2.util.PropertiesUtils;

public class GetProperties {
	public static String readValue(String filePath, String Key) {
		Properties props = new Properties();
		String value = null;
		BufferedReader bfrd = null;
		InputStream ips = null;
		try {
			ips = PropertiesUtils.class.getResourceAsStream(filePath);
			bfrd = new BufferedReader(new InputStreamReader(ips));
			props.load(bfrd);
			value = props.getProperty(Key);
		} catch (FileNotFoundException e) {
			value = e.toString();
		} catch (IOException e) {
			value = e.toString();
		} finally {
			try {
				if(bfrd != null) {
					bfrd.close();
				}
				if(ips != null) {
					ips.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return value;
	}
}
