$(document).ready(function(){
	init();
});

//初始化
function init(){
	document.querySelector('table[grid-manager="demo-ajaxPageCode"]').GM({
		height: '90%'
		,supportAjaxPage:true
		,supportCheckbox: true
		,ajax_data: 'http://www.lovejavascript.com/learnLinkManager/getLearnLinkList'
		,ajax_type: 'POST'
		,query: {pluginId: 1}
		,pageSize: 20
		,emptyTemplate: '<div class="gm-emptyTemplate">无数据</div>'
		,disableCache:true
		,columnData: [{
			key: 'name',
			remind: '网站标题',
			width: '100px',
			text: '标题',
			sorting: 'up'
		},{
			key: 'info',
			remind: '网站介绍',
			text: '介绍',
			sorting: ''
		},{
			key: 'url',
			remind: '网站地址',
			text: '地址',
			template: function(oneObject, rowObject){  //operation:当前key所对应的单条数据；rowObject：单个一行完整数据
				return '<button onclick=tanchu("'+(rowObject.name)+'");>'+rowObject.name+'</button>';
			}
		}
		]
		,ajax_success: function () {
			console.log('test1 ajax_success');
		}
		,pagingBefore:function(query){
			console.log('Event: 分页前', query);
		}
		,pagingAfter: function(query){
			console.log('Event: 分页后', query);
		}
		,sortingBefore:function(query){
			console.log('Event: 排序前', query);
		}
		,sortingAfter: function(query){
			console.log('Event: 排序后', query);
		}
	});
}

function tanchu(text){
	alert(text);
}