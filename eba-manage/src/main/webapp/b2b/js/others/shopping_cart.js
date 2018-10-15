
$(function(){
	
	
	function GoodsListCreate(obj){
		$('<li class="product"><div class="img lf"><img src="images/mr_icon.png" alt=""></div><div class="detail lf"><p class="jieshao"></p><span class="delate"></span><p class="size"><span></span></p><div class="price_num"><div class="goodsSum num cartSum"><button type="button" class="minus">-</button><input type="text" value="0";/><button type="button" class="plus">+</button></div></div></div></li>').appendTo(obj);
	}
	var goodsNumObj={"allNum":0};
	//var supplierId = localStorage.getItem('needSupplierId');
	$.ajax({
		type:"get",
		url:baseUrl+"/api/cart/list.jhtml",
		data:{
			supplierId:getSupplierId(),
			supplyType:getSupplyType()
		},
		success:function(obj){
			console.info(obj);
			
			for(var i=0; i<obj.data.length; i++){
				//console.info(supplierId);
				if(obj.data[i].supplierId == getSupplierId()){
					var indexNum = 0;
					for(var j=0; j<obj.data[i].goods.length; j++){
						GoodsListCreate(".company");
						var objData = obj.data[i].goods[j];
						var liObj = $(".company").children("li").last();
						liObj.find(".img img").attr("src",objData.img);
						liObj.find(".jieshao").html(objData.goodsName);
						liObj.find(".price_num .num input").val(objData.quantity);
						var specsCon="";
						for(var k=0; k<objData.specs.length; k++){
							specsCon += objData.specs[k];
						}
						liObj.find(".size span").html(specsCon);//规格
						liObj.attr("cartItemId",objData.cartItemId);
						liObj.attr("productId",objData.productId);
						
						goodsNumObj[objData.productId] = objData.quantity;
						indexNum += objData.quantity;
					}
					goodsNumObj["allNum"] = indexNum;
				}
			}
			
			goodsNumObj = JSON.stringify(goodsNumObj);
			localStorage.setItem("goodsNumObjObj",goodsNumObj);
			
			goodsNumNot();
		},
		error:function(){
			
		}
	})


	//点击删除按钮是否要删除该商品
	//打开窗口
	$(".company").delegate("li .delate","click",function(){
		$(".mutail").addClass("isToClass");
		var cartItemId = $(this).parent().parent().attr("cartItemId");
		var productId = $(this).parent().parent().attr("productId");
		var index = $(this).parent().parent().index();
		$(".mutail .delSure").attr("cartItemId",cartItemId);
		$(".mutail .delSure").attr("productId",productId);
		$(".mutail .delSure").attr("index",index);
	})
	
	//关闭窗口
	$(".mutail").on("click",function(event){
		//console.info(event.target);
		if($(event.target).is(".mutail") || $(event.target).is(".cancel")){
			event.preventDefault();
			$(".mutail").removeClass("isToClass");	
		}else if($(event.target).is(".delSure")){
			var cartItemId = $(this).find(".delSure").attr("cartItemId");
			var productId = $(this).find(".delSure").attr("productId");
			var index = $(this).find(".delSure").attr("index");
			
			$.ajax({
				type:"get",
				url:baseUrl+"/api/cart/deleteCartItem.jhtml",
				data:{
					cartItemId:cartItemId
				},
				success:function(){
					//alert("请求成功");
					delCartCache(productId);
					$(".goodsListUl").children('li').eq(index).remove();
					$(".mutail").removeClass("isToClass");
					goodsNumNot();
					//location.reload();
				},
				error:function(){
					//alert("请求失败");
				}
			})
		}
		
	})
	
	
	
	
	//点击减号
	$(".company").delegate("li .minus","click",function(){
		var goodsNum = $(this).next().val();
		if(goodsNum == 1){
			$(this).next().val(1);
			errorInfoFun(errInfo.goodsNumOne);
			return false;
		}else{
			$(this).next().val(goodsNum-1);
		}
		
		var productId = $(this).parent().parent().parent().parent().attr("productId");
		var goodsNum = $(this).parent().find("input").val();
		//console.info(goodsNum);
		addMinus(productId,goodsNum);	
	})
	
    //点击加号
	$(".company").delegate("li .plus","click",function(){
		var goodsNum = $(this).prev().val()*1;
		
		$(this).prev().val(goodsNum+1);

		var productId = $(this).parent().parent().parent().parent().attr("productId");
		var goodsNum = $(this).parent().find("input").val();
		
		addMinus(productId,goodsNum);
		
	})
	
	//商品的数量，的设置
	$(".company").delegate("li .cartSum input","keyup",function(){
		var val = $(this).val();
		$(this).val(val.replace(/[^\d]/g,''));
		if(val >9999){
			$(this).val("9999");
		}else if(val === "0"){
			$(this).val(1);
		}
	})
	
	//自动输入商品的数量
	$(".company").delegate("li .cartSum input","blur",function(){
		if($(this).val() == ""){
			$(this).val("1");
		}
		var productId = $(this).parent().parent().parent().parent().attr("productId");
		var goodsSum = $(this).val() * 1;
		
		addMinus(productId,goodsSum);
//		goodsSum = getCartNum(productId) + goodsSum ;
//	
//	    addMinus(productId,goodsSum);
		
	})
	
	
	
	
	//购物车为空时，，显示为空
	function goodsNumNot(){
		var goodsUlNum = $(".main .company").children().length;
		if(goodsUlNum == 0){
			$(".pay").css("display","none");
			$(".goodsNumOne").addClass("goodsActive");
		}else{
			$(".pay").css("display","block");
			$(".goodsNumOne").removeClass("goodsActive");
		}
		//console.info(goodsUlNum);
	}
	
	//点击结算，当购物车有商品的时候，跳转到结算页面
	$("a.account").on("click",function(){
		if($(".main .company").children().length > 0){
			$(this).attr("href","account.html");
		}
	})


});