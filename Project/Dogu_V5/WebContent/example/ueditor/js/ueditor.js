$(document).ready(function() {
	getUeditordata();
});
//活的当前用户保存到ueditor中的内容
function getUeditordata(){
	$.ajax({
		type : 'post',
		url : rootPath + '/Example/getUeditordata',
		data : {
			
		},
		success : function(data) {
			InitUeditor(data);
		},
		error : function(data, error) {
			parent.layer.alert(error,{icon: 7});
		}
	});
}
function InitUeditor(data){
	//实例化编辑器
    //建议使用工厂方法getEditor创建和引用编辑器实例，如果在某个闭包下引用该编辑器，直接调用UE.getEditor('editor')就能拿到相关的实例
    var ue = UE.getEditor('editor');
    ue.ready(function() {
    	UE.getEditor('editor').setContent(data.message, false);
	});
}
//保存ueditor中的内容(BLOB或者是CLOB形式)
function saveInnerUeditor(){
	var data = UE.getEditor('editor').getContent();
	data = encodeURI(data);
	$.ajax({
		type : 'post',
		url : rootPath + '/Example/saveUeditor',
		data : {
			data : data
		},
		success : function(data) {
			getUeditordata();
			parent.layer.alert(data.message,{icon: 1});
		},
		error : function(data, error) {
			parent.layer.alert(error,{icon: 7});
		}
	});
}