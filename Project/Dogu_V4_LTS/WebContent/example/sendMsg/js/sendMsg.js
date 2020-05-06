$(document).ready(function() {
	
});

function sendMsg(){
	var phonenumber = document.getElementById("phonenumber").value;
	var prefix = document.getElementById("prefix").value;
	prefix = encodeURI(prefix);
	var msg = document.getElementById("msg").value;
	msg = encodeURI(msg);
	if(phonenumber == ""){
		parent.layer.alert("请填写手机号！",{icon: 7});
	}else if(prefix == ""){
		parent.layer.alert("请填写前缀！",{icon: 7});
	}else if(msg == ""){
		parent.layer.alert("请填写所要发送的内容！",{icon: 7});
	}else{
		$.ajax({
			type : 'post',
			url : rootPath + '/Example/SendMsg',
			data : {
				method : "FACILITATOR",
				phonenumber : phonenumber,
				prefix : prefix,
				msg : msg
			},
			success : function(data) {
				parent.layer.alert(data.message,{icon: 1});
			},
			error : function(data, error) {
				parent.layer.alert(error,{icon: 7});
			}
		});
	}
}

//自己的硬件设备发送短信
function sendMsgByselfDevice(){
	var phonenumber = document.getElementById("selfDecicePhonenumber").value;
	var prefix = document.getElementById("selfDecicePrefix").value;
	prefix = encodeURI(prefix);
	var msg = document.getElementById("selfDeciceMsg").value;
	msg = encodeURI(msg);
	if(phonenumber == ""){
		parent.layer.alert("请填写手机号！",{icon: 7});
	}else if(prefix == ""){
		parent.layer.alert("请填写前缀！",{icon: 7});
	}else if(msg == ""){
		parent.layer.alert("请填写所要发送的内容！",{icon: 7});
	}else{
		$.ajax({
			type : 'post',
			url : rootPath + '/Example/SendMsg',
			data : {
				method : "SELFDEVICE",
				phonenumber : phonenumber,
				prefix : prefix,
				msg : msg
			},
			success : function(data) {
				parent.layer.alert(data.message,{icon: 1});
			},
			error : function(data, error) {
				parent.layer.alert(error,{icon: 7});
			}
		});
	}
}