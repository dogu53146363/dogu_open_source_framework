function uploadfile(savefilefolder) {
	upload(savefilefolder); //函数参数为上传的文件的本机地址
}
function upload(savefilefolder) {
	var url = window.location.href;
	$.ajaxFileUpload({
			url : rootPath + '/Example/fileUpload', //提交的路径
			secureuri : false, // 是否启用安全提交，默认为false
			fileElementId : savefilefolder + 'File', // file控件id
			dataType : 'json',
			data : {
				
			},
			success : function(data, status) {
				if (data.error == 0) {
					parent.layer.alert(data.message,{icon: 1});
					//下面是上传之后显示图片的
					var src = data.src;
					document.getElementById(savefilefolder + "showpic").innerHTML = "";
					document.getElementById(savefilefolder + "showpic").innerHTML = "<img  class='piccls' src='../../../"
							+ src + "' style='width:200px; height: 200px;' onclick='fangda(\"" + src + "\")'>";
				} else {
					document.getElementById(savefilefolder+"File").value = "";
					document.getElementById(savefilefolder + "showpic").innerHTML = "";
					parent.layer.alert(data.message,{icon: 7});
				}
			},
			error : function(data, status) {
				document.getElementById(savefilefolder+"File").value = "";
				document.getElementById(savefilefolder + "showpic").innerHTML = "";
				parent.layer.alert("上传失败!",{icon: 7});
			}
		});
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
	document.getElementById("picfdck").innerHTML = "<img src='../../../"+src+"'>";
}
function hidefangdapic() {
	document.getElementById("fdpic").style.display = "none";
}