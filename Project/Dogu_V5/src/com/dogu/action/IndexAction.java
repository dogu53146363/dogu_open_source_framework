package com.dogu.action;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.mail.MessagingException;
import org.dom4j.DocumentException;
import com.dogu.constants.Constant;
import com.dogu.utils.CommonUtils;
import com.dogu.utils.DoguCaptchaRender;
import com.dogu.utils.GetConFromDB;
import com.dogu.utils.Md5;
import com.dogu.utils.SendEmail;
import com.dogu.utils.SendMsg;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

/**
 * 系统首页的Action
 * 登录之后产生session
 * 全局根据session做拦截
 * @author Dogu
 */

public class IndexAction extends Controller {
	
	@SuppressWarnings("unchecked")
	public void index() {
		Map<String, String> sessionStorage = (Map<String, String>) this.getSession()
				.getAttribute(Constant.SESSION_STORAGE);
		if(null == sessionStorage) {
			this.render("/"+Constant.INDEXJSP);
		}else {
			this.render(Constant.MAINPAGEPATH);
		}
	}
	
	/**
	 * 是否打开用户注册和找回密码以及验证码验证功能
	 */
	public void checkOpenUserRegAndForgetPswAndvalidateCode() {
		String UserRegOpened = GetConFromDB.GetCIFromDB("OPEN_USERREG");
		String ForgetPswOpened = GetConFromDB.GetCIFromDB("OPEN_RETRIEVE_PSW");
		String validateCode = GetConFromDB.GetCIFromDB("OPEN_VALIDATECODE");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("REG",UserRegOpened);
		map.put("PSW",ForgetPswOpened);
		map.put("VCODE",validateCode);
		this.renderJson(map);
	}
	
	/**
	 * 首页产生验证码
	 */
    public void getValidateImg(){
        //产生验证码
		String ValidateCodeLength = GetConFromDB.GetCIFromDB("VALIDATE_CODE_LENGTH");
		int length = 6;//默认长度是6位可以在系统配置的"VALIDATE_CODE_LENGTH"中配置
		try {
			length = Integer.parseInt(ValidateCodeLength);
		}catch (Exception e) {
			length = 6;
		}
		render(new DoguCaptchaRender(90,22,length,true));
	}
	
	/**
	 * 用户注册
	 */
	public void UseReg() {
		String account = this.getPara("account");
		String password = this.getPara("password");
		String name = this.getPara("name");
		try {
			name = URLDecoder.decode(name, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String sex = this.getPara("sex");
		String tel = this.getPara("tel");
		String address = this.getPara("address");
		try {
			address = URLDecoder.decode(address, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String email = this.getPara("email");
		String qq = this.getPara("qq");
		Map<String, Object> map = new HashMap<String, Object>();
		if (CommonUtils.isLetterDigit(account)) {
			Record rec = Db
					.findFirst(
							"SELECT 1 FROM SYS_USER WHERE F_ACCOUNT = ?",
							account);
			if (rec != null) {
				map.put("code", "1");
				map.put("message", "该帐号已存在!");
			} else {
				Db.update(
						"INSERT INTO SYS_USER (F_ACCOUNT,F_PASSWORD,F_ROLE,F_USERNAME,F_SEX,F_TEL,F_ADDRESS,F_E_MAIL,F_QQ) VALUES (?,?,?,?,?,?,?,?,?)",
						account, password, Constant.REGSTATUE, name, sex, tel, address,
						email, qq);
				map.put("code", "0");
				map.put("message", "注册成功，请等待管理员审核！");
			}
		} else {
			map.put("code", "1");
			map.put("message", "帐号只能为字母和数字！");
		}
		this.renderJson(map);
	}
	
	/**
	 * 找回密码
	 */
	public void forgetPSW() {
		String account = this.getPara("account");
		String tel = this.getPara("tel");
		String email = this.getPara("email");
		String method = this.getPara("method");
		Record rec = Db.findFirst("SELECT 1 FROM SYS_USER WHERE F_ACCOUNT = ? AND F_TEL = ? AND F_E_MAIL = ? ",account,tel,email);
		Map<String, Object> map = new HashMap<String, Object>();
		if (null != rec) {
			//通过短信发送
			if(Constant.CAPITAL_MSG.equals(method)){
				int newPassword = (int)((Math.random()*9+1)*100000);
				SendMsg sendmsg = new SendMsg();
				String returnmsg = null;
				try {
					returnmsg = sendmsg.sendMsg(tel,"找回密码","您的帐号密码为：["+newPassword+"]，请登录系统后及时修改！",Constant.FACILITATOR);
				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				} catch (DocumentException e) {
					e.printStackTrace();
				}
				if(Constant.STR_ZERO.equals(returnmsg)||Constant.CAPITAL_OK.equals(returnmsg)){
					map.put("code", "0");
					map.put("message", "重置密码成功，请注意接收手机短信!");
					try {
						Db.update("UPDATE SYS_USER SET F_PASSWORD = ? WHERE F_ACCOUNT = ? ",Md5.SmallMd5(newPassword+""),account);
					} catch (NoSuchAlgorithmException e) {
						e.printStackTrace();
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}else{
					map.put("code", "1");
					map.put("message", "重置密码失败，请尝试其他找回方式！");
				}
			}else if(Constant.CAPITAL_MAIL.equals(method)){
				int newPassword = (int)((Math.random()*9+1)*100000);
				SendEmail sendEmail = new SendEmail();
				try {
					sendEmail.sendmail(GetConFromDB.GetCIFromDB("MAIL_ACCOUNT"),GetConFromDB.GetCIFromDB("MAIL_TOKEN"),email,"找回密码","您的账号密码为：["+newPassword+"]请登录系统后及时修改！");
				} catch (MessagingException e) {
					e.printStackTrace();
				}
				map.put("code", "0");
				map.put("message", "重置密码成功，请注意查收邮件!");
				try {
					Db.update("UPDATE SYS_USER SET F_PASSWORD = ? WHERE F_ACCOUNT = ? ",Md5.SmallMd5(newPassword+""),account);
				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}else{
				map.put("code", "1");
				map.put("message", "请选择找回方式！");
			}
			
		}else{
			map.put("code", "1");
			map.put("message", "重置密码失败，邮箱或手机号不正确!");
		}
		this.renderJson(map);
	}
	
	/**
	 * 登录验证
	 */
	public void logincheck() {
		String account = this.getPara("account");
		String password = this.getPara("password");
		String icode = this.getPara("icode");
		Map<String, Object> map = new HashMap<String, Object>();
		if (null == account || Constant.EMPTYSTR.equals(account)) {
			map.put("id", "fail");
			map.put("message", "请输入账号!");
		} else {
			Record rec = Db.findFirst("SELECT 1 FROM SYS_USER WHERE F_ACCOUNT = ? AND F_PASSWORD = ?",account,password);
			if (null != rec) {
				//判断是否已打开了验证码功能
				String validateCodeStatus = GetConFromDB.GetCIFromDB("OPEN_VALIDATECODE");
				if(Constant.LOWERCASE_TRUE.equals(validateCodeStatus)) {
					//判断验证码
					boolean validate = DoguCaptchaRender.validate(this, icode);
					if(!validate) {
						map.put("id", "fail");
			        	map.put("message", "验证码错误!");
					}else {
						UUID onePointID = UUID.randomUUID();
						this.setSessionAttr("ONE_POINT_ID", onePointID.toString());
						Db.update("UPDATE SYS_USER SET F_ONE_POINT_ID = ? WHERE F_ACCOUNT = ?",
								onePointID.toString(),account);
						rec = Db.findFirst("SELECT * FROM SYS_USER WHERE F_ACCOUNT = ?",account);
						String columnKey[] = rec.getColumnNames();
						Map<String, String> sessionMap = new HashMap<String, String>();
						for(int i=0;i<columnKey.length;i++){
							if(!"F_PASSWORD".equals(columnKey[i])) {
								sessionMap.put(columnKey[i], rec.getStr(columnKey[i]));
							}
						}
						this.setSessionAttr(Constant.SESSION_STORAGE,sessionMap);
						map.put("id", "success");
						map.put("message", "登陆成功!");
					}
				}else {
					UUID onePointID = UUID.randomUUID();
					Db.update("UPDATE SYS_USER SET F_ONE_POINT_ID = ? WHERE F_ACCOUNT = ?",
							onePointID.toString(),account);
					rec = Db.findFirst("SELECT * FROM SYS_USER WHERE F_ACCOUNT = ?",account);
					String columnKey[] = rec.getColumnNames();
					Map<String, String> sessionMap = new HashMap<String, String>();
					for(int i=0;i<columnKey.length;i++){
						if(!"F_PASSWORD".equals(columnKey[i])) {
							sessionMap.put(columnKey[i], rec.getStr(columnKey[i]));
						}
					}
					this.setSessionAttr(Constant.SESSION_STORAGE,sessionMap);
					map.put("id", "success");
					map.put("message", "登陆成功!");
					map.put("id", "success");
					map.put("message", "登陆成功!");
				}
			} else {
				map.put("id", "fail");
				map.put("message", "账号或密码错误!");
			}
		}
		renderJson(map);
	}
	
	/**
	 * 登出
	 */
	public void loginout() {
		this.getSession().invalidate();
		this.renderText("success");
	}
}
