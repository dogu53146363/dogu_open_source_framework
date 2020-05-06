function ChangeUploadFile() {
	var upFileDom = document.getElementById("fileText");//获取上传文件的input的Dom对象
	var allowSuffix = document.getElementById("fileText").accept;//获取允许上传文件的类型
	var unlegal = 0;
	//循环遍历每个文件的后缀名
	for(var i=0;i<upFileDom.files.length;i++){
		var upFileName = upFileDom.files[i].name;
		var index1 = upFileName.lastIndexOf(".");
		var index2 = upFileName.length;
		var suffix = upFileName.substring(index1+1,index2).toLowerCase();//后缀名
		if(allowSuffix.indexOf(suffix) == -1 && allowSuffix != ""){
			unlegal++;
		}
	}
	if(unlegal == 0){
		showLoad();
		var form = $("#uploadFile");
		var options  = {
			url: rootPath + "/Example/fileUpload",
			type:'post',
			data:{
				
			},
			success:function(data){
				if(typeof(data)=='string'){//判断一下，renderForIE的时候会出现返回json字符串的情况
					data = JSON.parse(data);
				}
		    	if(data.error == "0"){
		    		parent.layer.alert(data.message,{icon: 1});
		    		document.getElementById("showBigPic").innerHTML = "";
					document.getElementById("showBigPic").innerHTML = "<img  class='piccls' src='../"
							+ data.src + "' style='width:200px; height: 200px;' onclick='fangda(\"" + data.src + "\")'>";
		    	}else if(data.error == "NO"){
		    		//由于浏览器内核原因
		    		//在上传完一个文件之后有可能直接
		    		//修改上传弹出框的值得时候会触发
		    		//onchange事件，并且无操作，也无文件，此时
		    		//给它一个特殊的值，做一下不提示的处理
		    		//parent.layer.alert(data.msg,{icon: 7});
		    	}else{
		    		parent.layer.alert(data.msg,{icon: 7});
		    	}
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				parent.layer.alert(JSON.stringify(XMLHttpRequest), {icon: 7});
			}
		}; 
		form.ajaxSubmit(options);
		closeLoad();
	}else{
		parent.layer.alert("上传失败，非法的文件类型！",{icon: 7});
	}
}
$(document).keyup(function(event) {
	switch (event.keyCode) {
	case 27:
		document.getElementById("fdpic").style.display = "none";
	case 96:
		document.getElementById("fdpic").style.display = "none";
	}
});
function fangda(src) {
	document.getElementById("fdpic").style.display = "block";
	document.getElementById("picfdck").innerHTML = "<img src='../"+src+"'>";
}
function hidefangdapic() {
	document.getElementById("fdpic").style.display = "none";
}