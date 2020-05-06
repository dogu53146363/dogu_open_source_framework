//验证验证码
function CheckVerificationCode(){
	var VerificationCode = document.getElementById("VerificationCodeId").value;
	$.ajax({
		type : 'post',
		url : rootPath + '/Example/CheckVerificationCode',
		data : {
			VerificationCode : VerificationCode
		},
		success : function(data) {
			parent.layer.alert(data.message,{icon: 1});
		},
		error : function(data, error) {
			parent.layer.alert(error,{icon: 7});
		}
	});
}