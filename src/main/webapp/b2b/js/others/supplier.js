


$(function(){
	
	function supplierCreate(obj){
		$('<li><div class="left"><div class="img"></div></div><div class="right"></div></li>').appendTo(obj);
	}
	
	
	$(".supHeader .formal").addClass("supHeaderActive");
	//选择供应商，添加效果，和获取各供应商
	$(".supHeader>div").on("click",function(){
		$(this).find("div").addClass("supHeaderActive");
		$(this).siblings().find("div").removeClass("supHeaderActive");	
	})
	
	var supplyType = "formal";
	localStorage.setItem("supplyType",supplyType);
	//点击供应商类型
	$(".supHeader>div").click(function(){
		$(".supplier").html("");
		var type = $(this).attr("supplyType");
		
		supplerFun(type);
		
		if(type == "selectSupplier"){
			supplyType ="temporary";
		}else{
			supplyType = type;
		};
		localStorage.setItem("supplyType",supplyType);
		
	})
	
	//默认显示正式供应商的列表
	supplerFun("formal");
	function supplerFun(type){
		$.ajax({
			type:"get",
			url:baseUrl+"/api/supplier/"+type+".jhtml",
			success:function(obj){
				//console.info(obj);
				for(var i=0; i<obj.data.length; i++){
					supplierCreate(".supplier");
					var supperObj = $(".supplier").children("li").eq(i);
					var Data = obj.data[i];
					supperObj.find(".right").html(Data.name);
					supperObj.attr("supplierId",Data.id);
				};
			},
			error:function(){}
		});
	};
	
	
	$(".supplier").delegate("li","click",function(){
		var supplierId = $(this).attr("supplierId");
		//console.info(supplierId);
		var supplyType = getSupplyType();
		setSupplierId(supplierId);
		takeGoodsNum();
		
		//console.info(goodsNumObj);
		//return false;
		window.location = "goodsList.html";
	})
})





