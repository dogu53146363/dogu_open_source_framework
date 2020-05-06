$(document).ready(function() {
	$.ajax({
		type : 'post',
		url : rootPath + '/Example/NoInterceptor',
		data : {
			
		},
		success : function(data) {
			parent.layer.alert(data.message,{icon: 1});
		},
		error : function(data, error) {
			parent.layer.alert(error,{icon: 7});
		}
	});
});