


	var searchOrderArr = [];
	if(getCookie("searchOrderName")){
		searchOrderArr = $.unique(getCookie("searchOrderName").split(","));
		if(searchOrderArr.length >10){
			searchOrderArr.length = 10;
		}
		for(var i=0; i<searchOrderArr.length; i++){
			$(".searchHostory .searchHistory").append("<li>"+searchOrderArr[i]+"</li>");
		}
	}

	//创建订单供应商的列表
	function supplierNumCreate(obj){
		$('<ul class="orderdetail"><li class="provide"><span class="provideName"></span><span class="content2"></span></li><li class="orderNumber">订单编号：<span></span></li><li class="orderImages"></li><li class="price"><span>共<i></i>件商品</span></li><li class="button"><a class="again" href="javascript:;">再来一单</a></li></ul>').appendTo(obj);
	}
	
	function goodsImgNum(obj){
		$('<img src="" alt="">').appendTo(obj);
	}
	
	//取消订单按钮
	function cancelFun(obj){
		$('<button class="cancelOrder">取消订单</button>').appendTo(obj);
	}

	function supplierAjax(obj){
		if(obj.data.orders.length == 0){
			$(".orderNumNot").addClass("orderNumActive");
		}else{
			$(".orderNumNot").removeClass("orderNumActive");
		}
		for(var i=0; i<obj.data.orders.length; i++){
			supplierNumCreate("#menu_con .tag");
			var ordersUl = $("#menu_con .tag").children("ul").eq(i);
			var orderInfo = obj.data.orders[i];
			
			//把供应商类型和供应商放在再来一单的按钮里面保存
			ordersUl.find(".button .again").attr("supplyType",orderInfo.supplyType);
			ordersUl.find(".button .again").attr("supplierId",orderInfo.supplierId);
			
			
			
			ordersUl.attr("orderId",orderInfo.orderId);
			ordersUl.find(".provideName").html(orderInfo.supplierName);
			
			ordersUl.find(".provide .content2").html(statusObj[orderInfo.status]);
			
			//当商品的状态是，等待审核或者等待发货时，显示取消订单按钮
			if(orderInfo.status ==1 || orderInfo.status == 2 ){
				cancelFun("#menu_con .tag ul:eq("+i+")  .button");
			};
			
			ordersUl.find(".orderNumber span").html(orderInfo.sn);
			//ordersUl.find(".button a").attr("href" , "goodsList.html?needSupplierId=" + orderInfo.supplierId);
			for(var j=0; j<orderInfo.orderItems.length; j++){
				goodsImgNum(ordersUl.find(".orderImages"));
				var imgSrc = orderInfo.orderItems[j].img?orderInfo.orderItems[j].img:defaultImg;
				ordersUl.find(".orderImages").children("img").eq(j).attr("src",imgSrc);
				
			}
			ordersUl.find(".price span i").html(orderInfo.quantity);
			ordersUl.find(".orderImages").attr("orderId",orderInfo.orderId);
			
		}
	}
	
	//点击再来一单，，跳转到商品列表页面，保存供应商类型
	$("#menu_con .tag").delegate("ul li.button .again","click",function(){
		var supplierId = $(this).attr("supplierId");
		var supplyType = $(this).attr("supplyType");
		setSupplierId(supplierId);
		setSupplyType(supplyType);
		window.location = "goodsList.html";
	})
	
	
	
	function supplierListCreate(status,searchValue){
		$.ajax({
			type:"get",
			url:baseUrl+"/api/member/orderList.jhtml",
			async:false,
			data:{
				status:status,
				searchProperty:"sn",
				searchValue:searchValue
			},
			success:function(obj){
				//console.info(obj.data);
				$("#menu_con .tag").html("");
				supplierAjax(obj);
			},
			error:function(){}
		})
	}

	//点击商品，保存商品的Id，，跳转到商品详情页面
	$("#menu_con .tag").delegate("ul .orderImages","click",function(){
		
		var orderId = $(this).attr("orderId");
		//console.info(orderId);
		localStorage.setItem("goodsOrderId",orderId);
		window.location = "orderdetails.html";
		
	});
	
	
	
	 //弹出确认框,是否确认取消订单
	$("#menu_con .tag").delegate("ul .cancelOrder","click",function(){
		$(".mutail").css("display","block");
		var orderId = $(this).parent().parent().attr("orderId");
		//console.log(orderId);
		$(".mutail .delSure").attr("orderId",orderId);
		$(".mutail .delSure").attr("index",$(this).parent().parent().index());
		
	})
	
	//取消按钮
	$(".mutail .cancel").on("click",function(){
		$(".mutail").css("display","none");
	})
	
	//确认按钮，，取消订单
	$(".mutail .delSure").on("click",function(){
		
		var orderId = $(this).attr("orderId");
		var index = $(this).attr("index");
		//console.log(orderId);
		$.ajax({
			type:"post",
			url:baseUrl+"/api/order/applyCancel.jhtml",
			data:{
				orderId:orderId
			},
			success:function(obj){
				console.info(obj);
				if(obj.code == "0"){
					$("#menu_con ul").eq(index).find(".content2").html(statusObj[9]);
					$("#menu_con ul").eq(index).find(".cancelOrder").css("display","none");
					$(".mutail").css("display","none");
				}
			},
			error:function(){}
		})
		
		
	})

