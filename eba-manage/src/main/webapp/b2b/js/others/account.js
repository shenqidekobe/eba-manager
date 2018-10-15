


$(function(){
	
	function accountCreateList(obj){
		$('<li><img src="images/mr_icon.png" alt=""><ol class="detail"><p class="left"><span class="goodsName"></span><span class="goodsTip"></span></p><p class="right"><span class="sum">×<span></span></span></p></ol><div class="clearfix"></div></li>').appendTo(obj);
	}
	
	
	
	$.ajax({
		type:"get",
		url:baseUrl+"/api/order/preOrder.jhtml",
		data:{
			supplyType:getSupplyType(),
			supplierId:getSupplierId()
		},
		success:function(obj){
			//console.info(obj);
			var gotoGoods = obj.data.need;
			$(".receiving_info .sroreName").val(gotoGoods.needName);
			$(".receiving_info .takeGoodsAddress").val(gotoGoods.address);
			$(".receiving_info .contactName").val(gotoGoods.userName);
			$(".receiving_info .phone").val(gotoGoods.mobile);

			for(var i=0; i<obj.data.supplierList.length; i++){
				if(obj.data.supplierList[i].supplierId != getSupplierId()){
					continue ;
				}
				var goodsToNum = obj.data.supplierList[i].goods;
				var index = 0;
				for(var j=0;j<goodsToNum.length; j++){
					//console.info(1);
					accountCreateList(".order_info");
					var orderListLi = $(".order_info").children("li").eq(j);
					var imgSrc = goodsToNum[j].img?goodsToNum[j].img:defaultImg;
					orderListLi.find("img").attr("src",imgSrc);
					orderListLi.find(".detail .goodsName").html(goodsToNum[j].goodsName);
					orderListLi.find(".detail .right .sum span").html(goodsToNum[j].quantity);
					index += goodsToNum[j].quantity;
					
					if(goodsToNum[j].specs.length>0){
						var specsObj = '';
						for(var k=0; k<goodsToNum[j].specs.length; k++){
							specsObj += goodsToNum[j].specs[k];
						}
						orderListLi.find(".goodsTip").append("<span>"+specsObj+"</span>");
					}
					
					
				}
				$(".orderAllInfo .total i").html(index);
			}
		},
		error:function(){}
	})
             	
	
	
	$(".bottom2 .confirm").on("click",function(){
		var userName = $(".receiving_info .contactName").val();
		var mobile = $(".receiving_info .phone").val();
		var reDate = $(".receiving_info #appDate").val();
		var orderMess = $(".receiving_info .orderMess textarea").val();  /*给供应商留言*/
		
		if(orderMess == "给供应商留言"){
			orderMess = ""
		};
		
		if(userName == ''){
			errorInfoFun(errInfo.userNameError);
			return false;
		}
		
		if(!(/^1[34578]\d{9}$/.test(mobile))){ 
	        errorInfoFun(errInfo.mobileError);
	        return false;
    	}
		
		if(reDate == ''){
			errorInfoFun(errInfo.reDateError);
			return false;
		}
		
		$.ajax({
			type:"get",
			url:baseUrl+"/api/order/create.jhtml?",
			data:{
				supplyType:getSupplyType(),
				supplierId:getSupplierId(),
				userName:userName,
				tel:mobile,
				reDate:reDate,
				memo:orderMess
			},
			success:function(obj){
				if(obj.code == "0"){
					//清空本地缓存
					var goodsNumObj = JSON.stringify({allNum:0});
					localStorage.setItem("goodsNumObjObj",goodsNumObj);
				}
				window.location = "success.html"
			},
			error:function(){}
		})
		
	})
	
	
	
})













