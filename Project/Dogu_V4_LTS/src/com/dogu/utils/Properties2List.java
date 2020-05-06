package com.dogu.utils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * 获取配置文件,并封装成List返回
 * @author Dogu
 *
 */
public class Properties2List {
	
	public static List<String> getList(String filePath) throws UnsupportedEncodingException {
		
		String classPath = Properties2List.class.getResource("").getPath();
		String ConfigFloder_Path = "";
		
		//windows环境
		if("\\".equals(File.separator)){
			ConfigFloder_Path  = classPath.substring(1,classPath.indexOf("/WEB-INF/classes")+16)+filePath;
			ConfigFloder_Path = ConfigFloder_Path.replace("/", "\\");
		}
		//linux环境
		if("/".equals(File.separator)){
			ConfigFloder_Path  = classPath.substring(1,classPath.indexOf("/WEB-INF/classes")+16)+filePath;
			ConfigFloder_Path = ConfigFloder_Path.replace("\\", "/");
		}
		//将20%转换成空格(jdk的bug)
		ConfigFloder_Path = java.net.URLDecoder.decode(ConfigFloder_Path,"UTF-8");
		File Floder = new File(ConfigFloder_Path);
		List<String> ConfigFileList = new ArrayList<String>();
		// 遍configFile目录下的properties文件放入ConfigFileList中
		if (Floder.isDirectory()) {
			File[] fileArray = Floder.listFiles();
			for (int i = 0; i < fileArray.length; i++) {
				if (!fileArray[i].isDirectory()
						&& (fileArray[i].getName().substring(fileArray[i].getName().lastIndexOf(".") + 1)
								.toLowerCase()).equals("properties")) {
					ConfigFileList.add(fileArray[i].getPath());
				}
			}
		}else {
			System.err.println("Unable To Load Property File ["+filePath+"]");
		}
		return ConfigFileList;
	}
}
