var domWidth = 710;
var domHeight = 490;
if(screen.width >= 1920){
	domWidth = 1310;
}
if(screen.height >= 1080){
	domHeight = 800;
}

var property={
	width:domWidth,
	height:domHeight,
	toolBtns:["start round","end round","task round","node","chat","state","plug","join","fork","complex"],
	haveHead:true,
	headBtns:["new","open","save","undo","redo","reload"],//如果haveHead=true，则定义HEAD区的按钮
	haveTool:true,
	haveGroup:true,
	useOperStack:true
};
var remark={
	"cursor":"选择指针",
	"direct":"结点连线",
	"start":"入口结点",
	"end":"结束结点",
	"task":"任务结点",
	"node":"自动结点",
	"chat":"决策结点",
	"state":"状态结点",
	"plug":"附加插件",
	"fork":"分支结点",
	"join":"联合结点",
	"complex":"复合结点",
	"group":"组织划分框编辑开关"
};