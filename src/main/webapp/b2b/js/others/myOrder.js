

$(function(){
	
	$("#nav li").on("click",function(){
		$("#nav li").removeClass("selected");
		$(this).addClass("selected");
	})
	
	
	var status = "",searchValue = "";
	supplierListCreate(status,searchValue);
	
	
	$("#menu #nav li").on("click",function(){
		
		var statusTo = $(this).attr("status");
		if(statusTo == 1){
			statusTo = "";
		}
		supplierListCreate(statusTo);
		
		
	})
	
	
	//当列表的数据为0的时候显示您还没有订单页面
	function orderNot(){
		var OrderLength = $("#menu_con .tag ul").length;
		if(OrderLength == 0){
			$(".orderNumNot").addClass("orderNumActive");
		}else{
			$(".orderNumNot").removeClass("orderNumActive");
		}
	}
	
})











