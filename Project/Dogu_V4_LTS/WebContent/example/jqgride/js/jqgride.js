$(function(){
	//页面加载完成之后执行
	pageInit();
});

function pageInit(){
	  jQuery("#list").jqGrid(
	      {
	    	url : rootPath + '/Example/getJqGride',//组件创建完成之后请求数据的url
	        datatype : "json",
	        colNames : [ 'Inv No', 'Date', 'Client', 'Amount', 'Tax','Total', 'Notes' ],
	        colModel : [ 
	                     {name : 'id',index : 'id',width : 55}, 
	                     {name : 'invdate',index : 'invdate',width : 90}, 
	                     {name : 'name',index : 'name asc, invdate',width : 100}, 
	                     {name : 'amount',index : 'amount',width : 80,align : "right"}, 
	                     {name : 'tax',index : 'tax',width : 80,align : "right"}, 
	                     {name : 'total',index : 'total',width : 80,align : "right"}, 
	                     {name : 'note',index : 'note',width : 150,sortable : false} 
	                   ],
	        rowNum : 10,
	        rowList : [ 10, 20, 30 ],
	        pager : '#pager',
	        sortname : 'id',
	        mtype : "post",
	        viewrecords : true,
	        sortorder : "desc",
	        caption : "JQGride 实例"
	      });
	  jQuery("#list").jqGrid('navGrid', '#pager', {edit : false,add : false,del : false});
	}