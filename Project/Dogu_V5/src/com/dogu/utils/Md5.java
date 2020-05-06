package com.dogu.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class Md5 {
	
	/**
	 * 大写的MD5加密
	 * @param str
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	public static String BigMd5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException{
		String result = "";
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		md5.update((str).getBytes("UTF-8"));  
		byte b[] = md5.digest();
		int i;  
		StringBuffer buf = new StringBuffer("");	  
		for(int offset=0; offset<b.length; offset++){  
		    i = b[offset];  
		    if(i<0){  
		        i+=256;  
		    }  
		    if(i<16){  
		        buf.append("0");  
		    }  
		    buf.append(Integer.toHexString(i));  
		}  
		result = buf.toString();
		result = buf.toString().toUpperCase();
		return result;  
	}
	
	/**
	 * 小写的MD5加密
	 * @param str
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	public static String SmallMd5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException{
		String result = "";
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		md5.update((str).getBytes("UTF-8"));  
		byte b[] = md5.digest();
		int i;  
		StringBuffer buf = new StringBuffer("");	  
		for(int offset=0; offset<b.length; offset++){  
		    i = b[offset];  
		    if(i<0){  
		        i+=256;  
		    }  
		    if(i<16){  
		        buf.append("0");  
		    }  
		    buf.append(Integer.toHexString(i));  
		}  
		result = buf.toString();
		result = buf.toString();
		return result;  
	}
}
