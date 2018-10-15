

$(function(){
	
	var supplierIdTo = GetQueryString("needSupplierId");
	var supplierId;
	
	if(supplierIdTo){
		supplierId = supplierIdTo;
		setSupplierId(supplierId);
	}else{
		supplierId = getSupplierId();
	}
	//供应商的类型
	var supplyType = getSupplyType();

	
	var desc = 'desc';
	goodsListFun(supplierId,desc);
	function goodsListFun(supplierId,desc){
		$(".company").html("");
		$.ajax({
			type:"get",
			url:baseUrl+"/api/product/goodsList.jhtml",
			data:{
				supplierId:getSupplierId(),
				orderProperty:"sales",
				orderDirection:desc,
				supplyType:getSupplyType()	
			},
			success:function(obj){
				//console.info(obj);
				CreateGoodsList(obj);
				var goodsNumObj = JSON.parse(localStorage.getItem("goodsNumObjObj"));
				var goodsSum = goodsNumObj["allNum"] > 99?"99+":goodsNumObj["allNum"];
				$(".totalPrice .num").html(goodsSum);
				//onloadNum();
			},
			error:function(){}
		})
	}
	var salesObj = true;
	
	/*$(".twoSearchBox .priceChoose").on("click",function(){
		desc = 'desc';//asc	
		if(salesObj){
			goodsListFun(supplierId,desc);
			salesObj = false;
		}
	})*/
	
	
	$(".goodsPrice ul").delegate("li", "click",function(){
		
		/*选中的分类加上背景颜色*/
		$(this).parent().children().removeClass("activeBage");
		$(this).addClass("activeBage");
		$(".twoSearch div").removeClass("bage");
		$(".twoSearchBox .twoBottom").removeClass("display");
		
		/*点击的内容显示在分类上*/
		var index = $(this).parent().parent().index()-1;
		$(".twoSearch div").eq(index).html($(this).html());
		
		desc = $(this).attr("idNum");
		
		goodsListFun(supplierId,desc);

	})
	/*点击遮罩关掉分类*/
	$(".twoBottom").click(function(){  
		$(".twoSearch div").removeClass("bage");
		$(this).removeClass("display");
	})
	
	/*阻止冒泡事件，，点击遮罩关闭弹出框，别的不会*/
	$(".twoBottom ul").click(function(ev){  
		var ev = ev || event;
		ev.stopPropagation();
	})

	//显示购物车商品的数量
	//takeGoodsNum();
	var GoodsNumObj = JSON.parse(localStorage.getItem("goodsNumObjObj"));
	$(".totalPrice .icon span.num").html(GoodsNumObj["allNum"]);
	//console.log(GoodsNumObj["allNum"]);
	
	
	//点击加号
	$(".company").delegate("li .goodsnum button.plus","click",function(){
		var productId = $(this).parent().parent().parent().parent().attr("productId");
		
		var nums = $(this).prev().val()*1+1;
		$(this).prev().val(nums);
		if(nums != 0){
			$(this).siblings().css("display","block");
		}
		
		var goodsNum = getCartNum(productId) + 1 ;
		addMinus(productId,goodsNum);
		
	})
	//点击减号
	$(".company").delegate("li .goodsnum button.minus","click",function(){
		var productId = $(this).parent().parent().parent().parent().attr("productId");
		var nums = $(this).next().val()*1-1;
		
		$(this).next().val(nums);
		if(nums <= 0){
			$(this).next().css("display","none");
			$(this).css("display","none");
		}
		/*if(goodsNumObj[productId]){
			nums = goodsNumObj[productId] - nums;
		}
		
		var goodsNum = nums;*/
		var goodsNum = getCartNum(productId) - 1 ;
		
		addMinus(productId,goodsNum);
	})
	
	
//	
//	//点击分类和商品价格
	$(".twoSearch div").on("click",function(){
		if($(this).hasClass("bage")){
			$(".twoSearch div").removeClass("bage");
			$(".twoSearchBox .twoBottom").removeClass("display");
		}else{
			$(".twoSearch div").removeClass("bage");
			$(this).addClass("bage");
			$(".twoSearchBox .twoBottom").removeClass("display");
			$(".twoSearchBox .twoBottom").eq($(this).index()).addClass("display");
		}
	})
	$(".goodsType ul").delegate("li", "click",function(){
		/*选中的分类加上背景颜色*/
		$(this).parent().children().removeClass("activeBage");
		$(this).addClass("activeBage");
		$(".twoSearch div").removeClass("bage");
		$(".twoSearchBox .twoBottom").removeClass("display");
		
		/*点击的内容显示在分类上*/
		var index = $(this).parent().parent().index()-1;
		$(".twoSearch div").eq(index).html($(this).html());
		
		var id = $(this).attr("idNum");
		
		$(".company").html("");
		$.ajax({
			type:"get",
			url:baseUrl+"/api/product/goodsList.jhtml",
			data:{
				supplierId:getSupplierId(),
				supplyType:getSupplyType(),
				searchProperty:"productCategory",
				searchValue:id
			},
			success:function(obj){
				//console.info(obj);
				CreateGoodsList(obj);
			},
			error:function(){}
		})
		
		
		
	})


	//点击结算，当购物车有商品的时候，跳转到结算页面
	$("a.account").on("click",function(){
		if($(".totalPrice .num").html()*1 != 0){
			$(this).attr("href","account.html");
		}

	})
	
	
	//分类的点击
	$.ajax({
		type:"get",
		url:baseUrl+"/api/category/get.jhtml",
		data:{
			supplyType:getSupplyType(),
			supplierId:getSupplierId()
		},
		success:function(obj){
			//console.log(obj);
			var ulObj = $(".twoSearchBox .goodsType ul");
			
			if(obj.code == "0"){
				var data = obj.data;
				ulObj.append('<li idNum="" class="activeBage">全部分类</li>');
				for(var i=0; i<data.length; i++){
					ulObj.append('<li idNum='+data[i].id+'>'+data[i].name+'</li>');
				}
			}
			
		},
		error:function(){}
	})
	
	
	


})







