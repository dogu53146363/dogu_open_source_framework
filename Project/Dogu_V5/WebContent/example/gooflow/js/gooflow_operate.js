var demo = null;
$(document).ready(function() {
	getData();
});

function getData() {
	// 获取数据
	$.ajax({
		type : 'post',
		url : rootPath + '/Example/getFlowData',
		data : {
			
		},
		success : function(data) {
			initFlowData(data);
		},
		error : function(data, error) {
			parent.layer.alert(error,{icon: 7});
		}
	});
}
var out;
// 初始化
function initFlowData(jsondata) {
	demo=$.createGooFlow($("#demo"),property);
	demo.setNodeRemarks(remark);
	demo.onItemDel=function(id,type){
		if(confirm("确定要删除该单元吗?")){
      this.blurItem();
      return true;
		}else{
      return false;
		}
	};
	demo.loadData(jsondata);
	demo.onItemFocus=function(id,model){
      var obj;
      $("#ele_model").val(model);
      $("#ele_id").val(id);
      if(model=="line"){
        obj=this.$lineData[id];
        $("#ele_type").val(obj.M);
        $("#ele_left").val("");
        $("#ele_top").val("");
        $("#ele_width").val("");
        $("#ele_height").val("");
        $("#ele_from").val(obj.from);
        $("#ele_to").val(obj.to);
      }else if(model=="node"){
        obj=this.$nodeData[id];
        $("#ele_type").val(obj.type);
        $("#ele_left").val(obj.left);
        $("#ele_top").val(obj.top);
        $("#ele_width").val(obj.width);
        $("#ele_height").val(obj.height);
        $("#ele_from").val("");
        $("#ele_to").val("");
      }
      $("#ele_name").val(obj.name);
      return true;
	};
	demo.onItemBlur=function(id,model){
    document.getElementById("propertyForm").reset();
    return true;
	};
}

//更改页面缓存内容
function changeValue() {
	var oldId = demo.$focus;
	var newId = $("#ele_id").val();
	var type = $("#ele_model").val();
	var name = $("#ele_name").val();
	var partId = $("#ele_part").val();
	var partName = $("#ele_partname").val();
	if (type == "node") {
		demo.setName(oldId, name, type);
		parent.layer.alert("修改成功！",{icon: 1});
	}else if(type == "line"){
		demo.setName(oldId, name, type);
		parent.layer.alert("修改成功！",{icon: 1});
	}else {
		parent.layer.alert("请选择节点或连线之后再操作！",{icon: 1});
	}
}

var out;
function Export(){
    document.getElementById("result").value=JSON.stringify(demo.exportData());
}

//真正的保存流程信息
function SaveFlowInfo(){
	var result = JSON.stringify(demo.exportData());
	result = encodeURI(result);
	$.ajax({
		type : 'post',
		url : rootPath + '/Example/saveFlowData',
		data : {
			FlowData : result
		},
		success : function(data) {
			if(demo != null){
				demo.destrory();
			}
			getData();
			parent.layer.alert(data.message,{icon: 1});
		},
		error : function(data, error) {
			parent.layer.alert(error,{icon: 7});
		}
	});
}