<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>发送短信示例</title>
<%@ include file="../../../SYSTEM/common/jsp/common.jsp"%>
<script type="text/javascript" src="../js/sendMsg.js?version=${version}"></script>
<style type="text/css">
.divcls {
	width: 500px;
	border: 1px solid #AAAAAA;
	text-align: center;
	height: 320px;
	margin-left: 20px;
	margin-top: 20px;
}
.tablecls{
	margin-top: -664px;
	margin-left: 540px;
}
</style>
</head>
<body>
	<div style="font-size: 30px;width: 100%;text-align: center;">短信发送，请节约资源！</div>
	<div class="divcls">
		<br>
		手机号：<input id="phonenumber" type="text" placeholder="请输入手机号码" style="width: 200px;" /> <br> <br>
		标　题：<input id="prefix" type="text" placeholder="请输入标题" style="width: 200px;" /> <br> <br>
		内 　容：<textarea rows="3" cols="20" id="msg" placeholder="请输入发送的内容" style="width: 200px;"></textarea>
		<br>
		<br>
		<input type="button" class="commonSureBtnCls" style="height: 25px;" value="使用服务商接口发送" onclick="sendMsg()" /> <br> <br>
		<div style="color: red">注:接收到的短信格式为:【标题】发送的内容</div>
		<br>
	</div>
	<div class="divcls">
		<br>
		手机号：<input id="selfDecicePhonenumber" type="text" placeholder="请输入手机号码" style="width: 200px;" /> <br> <br>
		标　题：<input id="selfDecicePrefix" type="text" placeholder="请输入标题" style="width: 200px;" /> <br> <br>
		内 　容：<textarea rows="3" cols="20" id="selfDeciceMsg" placeholder="请输入发送的内容" style="width: 200px;"></textarea>
		<br>
		<br>
		<input type="button" class="commonSureBtnCls" style="height: 25px;" value="使用自己的设备发送" onclick="sendMsgByselfDevice()" /> <br> <br>
		<div style="color: red">注:接收到的短信格式为:【标题】发送的内容</div>
		<br>
	</div>
	<table border="1" class="tablecls">
		<tbody>
			<tr style="font-size: 14px; font-weight: bold;">
				<td width="100" align="center" valign="middle" bgcolor="#FFCC66">
					返回码（code)</td>
				<td width="150" align="center" valign="middle" bgcolor="#FFCC66">
					错误信息（result)</td>
				<td width="250" align="center" valign="middle" bgcolor="#FFCC66">
					具体错误描述或解决方法（detail)</td>
			</tr>
			<tr>
				<td width="100" align="center" valign="middle">0</td>
				<td width="150" align="center" valign="middle">Success</td>
				<td width="250" align="center" valign="middle">调用接口成功</td>
			</tr>
			<tr>
				<td width="100" align="center" valign="middle">-1</td>
				<td width="150" align="center" valign="middle">账户名为空</td>
				<td width="250" align="center" valign="middle">补充必须传入的参数</td>
			</tr>
			<tr>
				<td width="100" align="center" valign="middle">-2</td>
				<td width="150" align="center" valign="middle">帐户密码错误</td>
				<td width="250" align="center" valign="middle">按照提示输入正确的账户密码</td>
			</tr>
			<tr>
				<td width="100" align="center" valign="middle">-3</td>
				<td width="150" align="center" valign="middle">账户不存在</td>
				<td width="250" align="center" valign="middle">请先开账户传参引用</td>
			</tr>
			<tr>
				<td width="100" align="center" valign="middle">-4</td>
				<td width="150" align="center" valign="middle">账户密码错误</td>
				<td width="250" align="center" valign="middle">密码错误或者未MD5加密
				</td>
			</tr>
			<tr>
				<td width="100" align="center" valign="middle">-5</td>
				<td width="150" align="center" valign="middle">发送手机号码为空</td>
				<td width="250" align="center" valign="middle">请输入要下发的手机号码</td>
			</tr>
			<tr>
				<td width="100" align="center" valign="middle">-6</td>
				<td width="150" align="center" valign="middle">发送短信内容为空</td>
				<td width="250" align="center" valign="middle">请输入要下发的短信内容</td>
			</tr>
			<tr>
				<td width="100" align="center" valign="middle">-7</td>
				<td width="150" align="center" valign="middle">短信签名为空</td>
				<td width="250" align="center" valign="middle">请登录平台—&gt;报备签名</td>
			</tr>
			<tr>
				<td width="100" align="center" valign="middle">-8</td>
				<td width="150" align="center" valign="middle">手机号码格式错误</td>
				<td width="250" align="center" valign="middle">
					请输入有效的11位号码，多个号码请用英文“,”间隔</td>
			</tr>
			<tr>
				<td width="100" align="center" valign="middle">-9</td>
				<td width="150" align="center" valign="middle">
					短信内容仅能包含一个【】这种符号，请用其它符号代替</td>
				<td width="250" align="center" valign="middle">按照提示修改短信内容</td>
			</tr>
			<tr>
				<td width="100" align="center" valign="middle">-10</td>
				<td width="150" align="center" valign="middle">指定网关ID错误</td>
				<td width="250" align="center" valign="middle">请登录平台查看网关ID</td>
			</tr>
			<tr>
				<td width="100" align="center" valign="middle">-11</td>
				<td width="150" align="center" valign="middle">账户余额不足</td>
				<td width="250" align="center" valign="middle">账户需要充值，请充值后重试</td>
			</tr>
			<tr>
				<td width="100" align="center" valign="middle">-12</td>
				<td width="150" align="center" valign="middle">账户没有充值</td>
				<td width="250" align="center" valign="middle">账户没有充值，请充值后重试</td>
			</tr>
			<tr>
				<td width="100" align="center" valign="middle">-13</td>
				<td width="150" align="center" valign="middle">
					106营销短信必须末尾加退订回T，供用户选择</td>
				<td width="250" align="center" valign="middle">按照提示修改短信内容</td>
			</tr>
			<tr>
				<td width="100" align="center" valign="middle">-14</td>
				<td width="150" align="center" valign="middle">
					短信内容包含屏蔽词，请登录平台检测</td>
				<td width="250" align="center" valign="middle">
					关键词屏蔽，登录平台-&gt;发送短信进行检测</td>
			</tr>
			<tr>
				<td width="100" align="center" valign="middle">-15</td>
				<td width="150" align="center" valign="middle">账户冻结</td>
				<td width="250" align="center" valign="middle">账户被冻结，禁止使用</td>
			</tr>
			<tr>
				<td width="100" align="center" valign="middle">-16</td>
				<td width="150" align="center" valign="middle">IP没有权限</td>
				<td width="250" align="center" valign="middle">
					访问IP不在白名单之内，可在后台 "系统设置-&gt;IP白名单设置"里添加该IP
				</td>
			</tr>
			<tr>
				<td width="100" align="center" valign="middle">-17</td>
				<td width="150" align="center" valign="middle">多号码格式错误</td>
				<td width="250" align="center" valign="middle">多个手机号码请用英文逗号间隔</td>
			</tr>
			<tr>
				<td width="100" align="center" valign="middle">-18</td>
				<td width="150" align="center" valign="middle">
					下发短信长度超限，最多是350个字符，其中空格标点符号都算作一个字符</td>
				<td width="250" align="center" valign="middle">按照提示修改短息内容长度</td>
			</tr>
			<tr>
				<td width="100" align="center" valign="middle">-19</td>
				<td width="150" align="center" valign="middle">网关ID为空</td>
				<td width="250" align="center" valign="middle">请先获取网关类别ID
				</td>
			</tr>
			<tr>
				<td width="100" align="center" valign="middle">-20</td>
				<td width="150" align="center" valign="middle">
					不存在应用签名或者签名为审核，请登录平台查看</td>
				<td width="250" align="center" valign="middle">登录平台，查看签名报备进度</td>
			</tr>
			<tr>
				<td width="100" align="center" valign="middle">-21</td>
				<td width="150" align="center" valign="middle">定时发送时间格式化错误</td>
				<td width="250" align="center" valign="middle">按照提示请输入有效的时间格式</td>
			</tr>
			<tr>
				<td width="100" align="center" valign="middle">-22</td>
				<td width="150" align="center" valign="middle">
					短信下发格式错误，正确格式：【签名】+短信内容，签名符号【】 只能出现一次！</td>
				<td width="250" align="center" valign="middle">
					按照提示请输入有效的短信内容格式</td>
			</tr>
			<tr>
				<td width="100" align="center" valign="middle">-30</td>
				<td width="150" align="center" valign="middle">定时时间必须大于当前系统时间！</td>
				<td width="250" align="center" valign="middle">修改dstime 参数的时间值
				</td>
			</tr>
			<tr>
				<td width="100" align="center" valign="middle">-97</td>
				<td width="150" align="center" valign="middle">提交短信失败</td>
				<td width="250" align="center" valign="middle">提交短信时系统出错</td>
			</tr>
			<tr>
				<td width="100" align="center" valign="middle">-98</td>
				<td width="150" align="center" valign="middle">系统繁忙</td>
				<td width="250" align="center" valign="middle">系统繁忙，请稍后重试</td>
			</tr>
			<tr>
				<td width="100" align="center" valign="middle">-99</td>
				<td width="150" align="center" valign="middle">未知异常</td>
				<td width="250" align="center" valign="middle">系统出现未知的异常情况</td>
			</tr>
		</tbody>
	</table>
</body>
</html>