package com.dogu.utils;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.dogu.constants.Constant;

/**
 * 发送邮件工具类
 * @author Dogu
 *
 */
public class SendEmail {
	
	/**
	 * @param FROM 发送者
	 * @param PASSWORD 发送者的授权码/密码
	 * @param TO 邮件接收方
	 * @param SUBJECT 标题
	 * @param CONTENT 内容
	 * @throws MessagingException
	 */
	public void sendmail(String FROM,String PASSWORD, String TO, String SUBJECT, String CONTENT) throws MessagingException{
		// 创建Properties 类用于记录邮箱的一些属性
		final Properties props = new Properties();
		// 表示SMTP发送邮件，必须进行身份验证
		props.put("mail.smtp.auth", Constant.LOWERCASE_TRUE);
		// SMTP服务器
		props.put("mail.smtp.host", GetConFromDB.GetCIFromDB("QQ_SMTP_SERVER_ADDR"));
		// 端口号
		props.put("mail.smtp.port", GetConFromDB.GetCIFromDB("QQ_SMTP_SERVER_PORT"));
		// QQ邮箱账号
		props.put("mail.user", FROM);
		// 授权码
		props.put("mail.password", PASSWORD);
		// 构建授权信息，用于进行SMTP进行身份验证
		Authenticator authenticator = new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				// 用户名、密码
				String userName = props.getProperty("mail.user");
				String password = props.getProperty("mail.password");
				return new PasswordAuthentication(userName, password);
			}
		};
		// 使用环境属性和授权信息，创建邮件会话
		Session mailSession = Session.getInstance(props, authenticator);
		// 创建邮件消息
		MimeMessage message = new MimeMessage(mailSession);
		// 设置发件人
		InternetAddress form = new InternetAddress(props.getProperty("mail.user"));
		message.setFrom(form);
		// 设置收件人的邮箱
		InternetAddress to = new InternetAddress(TO);
		message.setRecipient(RecipientType.TO, to);
		// 设置邮件标题
		message.setSubject(SUBJECT);
		// 设置邮件的内容体
		message.setContent(CONTENT, "text/html;charset=UTF-8");
		// 最后当然就是发送邮件啦
		Transport.send(message);
	}
}
