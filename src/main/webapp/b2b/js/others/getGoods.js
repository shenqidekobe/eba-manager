


$(function(){
	
	
	function getGoodsList(obj){
		$('<li><img src="images/mr_icon.png" alt=""><div class="detail"><p class="left"><span class="goodsName"></span><span class="goodsTip"></span></p><p class="right"><span class="sum">×<span></span></span></p></div><p class="pSum"><span>实收数量</span><input type="text" value="" /><input type="text" value="" /></p></li>').appendTo(obj);
	};
	
	
	var shippingId = sessionStorage.getItem("shippingId");
	$(".order_info .inId").val(shippingId);
	//console.log(shippingId);
	$.ajax({
    	type:"get",
    	url:baseUrl+"/api/shipping/get.jhtml",
    	data:{
    		id:shippingId
    	},
    	success:function(obj){
    		console.log(obj.data.items);
    		var logInfo = obj.data.items;
    		for(var i=0; i<logInfo.length; i++){
    			getGoodsList(".order_info");
    			logInfoUl = $(".order_info").children("li").eq(i);
    			logInfoUl.attr("");
    			logInfoUl.find(".goodsName").html(logInfo[i].productName);
    			logInfoUl.find("img").attr("src", logInfo[i].img);
    			logInfoUl.find("input:eq(0)").attr("name", "shippingItems["+i+"].realQuantity");
    			logInfoUl.find("input:eq(1)").attr("name", "shippingItems["+i+"].id");
    			logInfoUl.find("input:eq(1)").val(logInfo[i].itemId);
    			logInfoUl.find(".sum span").html(logInfo[i].quantity);
    			if(logInfo[i].specs.length > 0){
					var specsObj = '';
					for(var k=0; k<logInfo[i].specs.length; k++){
						specsObj += logInfo[i].specs[k];
					}
					logInfoUl.find(".goodsTip").append("<span>"+specsObj+"</span>");
				}
    		};
    	},
    	error:function(){
    		
    	}
   });
   
   
   $(".totalPrice .conSum").click(function(){
   		var dataObj = $("#checkForm").serializeArray();
   		console.log(dataObj);
   		for(var i=0; i<dataObj.length; i++){
   			if(dataObj[i].value == ""){
   				return;
   			}
   		};
   		$.ajax({
	    	type:"post",
	    	url:baseUrl+"/api/shipping/customerCheck.jhtml",
	    	data:$("#checkForm").serializeArray(),
	    	success:function(obj){
	    		//console.log(obj);
	    		window.location = "orderdetails.html";
	    	},
	    	error:function(){}
	   });
   		
   	
   });
   
   
   	/*只能输入数字*/
   $(".order_info").delegate("input","keyup",function(){
   	
   		$(this).val($(this).val().replace(/[^0-9.]/g,''));  
   	
   })
   
   
/*   data:{
   		shippingItems[0].id: 34,
   		shippingItems[0]. realQuantity:3,
   		shippingItems[1].id: 34,
   		shippingItems[1]. realQuantity:3,
   }*/
   
   
   
   
})











