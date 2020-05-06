var myChart = null;
$(document).ready(function() {
	selectinputareanum();
});
// 查询输入地区的个数
function selectinputareanum() {
	var area = document.getElementById("area").value;
	if (area == "") {
		parent.layer.alert("请输入您想查的地区！",{icon: 7});
		return;
	} else {
		area = encodeURI(area);
		$.ajax({
			type : 'post',
			url : rootPath + '/Example/getAreaNUm',
			data : {
				area : area
			},
			success : function(data) {
				if (data.code == "1") {
					parent.layer.alert(data.message,{icon: 7});
				} else {
					getEchatsdata(data);
				}
			},
			error : function(data, error) {
				parent.layer.alert(error,{icon: 7});
			}
		});
	}
}
// 获取echarts数据
function getEchatsdata(data) {
	var area = document.getElementById("area").value;
	var object = document.getElementById("object").value;
	area = encodeURI(area);
	$.ajax({
		type : 'post',
		url : rootPath + '/Example/getEchartsdata',
		data : {
			area : area,
			object : object
		},
		success : function(data) {
			Drawhats(data, object);
		},
		error : function(data, error) {
			parent.layer.alert(error,{icon: 7});
		}
	});
}
function Drawhats(data, object) {
	var times = data.time.split(",");
	var values = data.value.split(",");
	var subtext = "";
	if (object == "F_PM2_5") {
		subtext = "pm2.5";
	} else if (object == "F_O3") {
		subtext = "臭氧";
	} else if (object == "F_SO2") {
		subtext = "二氧化硫";
	} else if (object == "F_CO") {
		subtext = "一氧化碳";
	} else if (object == "F_AQI") {
		subtext = "空气质量指数";
	} else if (object == "F_PM10") {
		subtext = "pm10";
	} else if (object == "F_O3_8H") {
		subtext = "臭氧8h平均";
	}
	// 基于准备好的dom，初始化echarts实例
	myChart = echarts.init(document.getElementById('mycharts'));
	var title = document.getElementById("area").value;
	myChart.setOption({
		title : {
			text : title,
			subtext : '项目：'+subtext
		},
		tooltip : {
			trigger : 'axis'
		},
		legend : {
			data : [ subtext ]
		},
		grid: {
            x: 80,
            y: 60,
            x2: 0,
            y2: 80
        },
		toolbox : {
			show : true,
			feature : {
				mark : {
					show : true
				},
				dataView : {
					show : true,
					readOnly : false
				},
				magicType : {
					show : true,
					type : [ 'line', 'bar' ]
				},
				restore : {
					show : true
				},
				saveAsImage : {
					show : true
				}
			}
		},
		calculable : true,
		xAxis : [ {
			type : 'category',
			axisLabel : {
				rotate: 40,
			},
			boundaryGap : false,
			data : times
		} ],
		yAxis : [ {
			type : 'value',
			axisLabel : {
				formatter : '{value}'
			}
		} ],
		series : [ {
			name : subtext,
			type : 'line',
			data : values,
			markLine : {
				data : [ {
					type : 'average',
					name : '平均值'
				} ]
			}
		} ]
	});
	// 处理点击事件
	myChart.on('click', function(params) {
		parent.layer.alert(params.name,{icon: 7});
	});
}