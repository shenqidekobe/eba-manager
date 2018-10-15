

$(function(){
	
	function accountCreateList(obj){
		$('<li><img src="images/mr_icon.png" alt=""><ol class="detail"><p class="left"><span class="goodsName"></span><span class="goodsTip"></span></p><p class="right"><span class="sum">×<span></span></span></p></ol><div class="clearfix"></div></li>').appendTo(obj);
	}
	
	/*物流信息*/
	function logInfoFun(obj){
		$('<ul><li><span>物流公司</span><span class="logCompany">上海优递莱思</span></li><li><span>物流单号</span><span class="logCode">23234423423</span></li><li class="conButton"><button type="button"></button></li></ul>').appendTo(obj);
	}
	
	function twoCodeFun(obj){
		obj.before('<li class="twoCode"><div class="content"><div class="img"><img src="images/mr_icon.png" alt="" /></div><p>向司机展示此二维码，扫描确认收货信息</p><span class="left"></span><span class="right"></span></div></li>');
	};
	
	function refuseFun(obj){
		$('<li class="refuse"><span class="name">拒绝理由：</span><span class="reason"></span></li>').appendTo(obj);
	};

	function fujianFun(obj){
		$('<li><a href="javascript:;"><div class="fileImg"><img src="images/PDF_icon.png" alt="" /></div><div class="right"><p class="fileName"></p><span></span></div></a></li>').appendTo(obj);
	}
	
	



	var outOrderId = GetQueryString("goodsOrderId"),
		orderId;
		
	if(outOrderId){
		orderId = outOrderId;
		localStorage.setItem("goodsOrderId",orderId);
	}else{
		orderId = localStorage.getItem('goodsOrderId');
	};
	
	//var orderId = localStorage.getItem("goodsOrderId");

	//console.info(orderId);
	
	/*获取订单详情的数据展示*/
	$.ajax({
		type:"get",
		url:baseUrl+"/api/order/view.jhtml",
		data:{
			orderId:orderId
		},
		success:function(obj){
			console.info(obj);
			$(".order_num .num span.no").html(obj.data.sn);
			var orderDate = new Date(obj.data.orderDate).format("yyyy-MM-dd hh:mm");
			$(".order_num .time span.date").html(orderDate);
			$(".order_num .state").html(statusObj[obj.data.status]);
			
			$(".bottom .cancelOrder").attr("orderId",obj.data.orderId);
			
			//把供应商类型和供应商放在再来一单的按钮里面
			$(".bottom .again").attr("supplyType",obj.data.supplyType);
			$(".bottom .again").attr("supplierId",obj.data.supplierId);
			
			var orderSrate = obj.data.status;
			
			/*当订单状态为，等待审核，等待发货的时候，，才显示取消订单的按钮*/
			if(orderSrate == 1 || orderSrate == 2){
				$(".bottom .cancelOrder").css("display","block");
			};
			/*判断订单处于什么状态，，显示在页面中*/
			if(orderSrate == 0 || orderSrate == 1 || orderSrate == 2|| orderSrate == 9 || orderSrate == 11){
				$(".order_num .state").addClass("state1");
			}else if(orderSrate == 3){
				$(".order_num .state").addClass("state4");
			}else if(orderSrate == 4 || orderSrate == 5){
				$(".order_num .state").addClass("state2");
			}else if(orderSrate == 6 || orderSrate == 7 || orderSrate == 10){
				$(".order_num .state").addClass("state3");
			}else if( orderSrate == 8){
				$(".order_num .state").addClass("state3");
				refuseFun(".order_num");
				$(".order_num .reason").html(obj.data.deniedReason);
			};
			
			/*展示收货信息*/
			$(".takeGoodsInfo .sroreName").html(obj.data.needName);
			$(".takeGoodsInfo .address").html(obj.data.address);
			$(".takeGoodsInfo .contactName").html(obj.data.userName);
			$(".takeGoodsInfo .phone").html(obj.data.mobile);
			$(".takeGoodsInfo .message").html(obj.data.memo);
			var reDate = new Date(obj.data.reDate).format("yyyy-MM-dd");
			$(".takeGoodsInfo .appDate").html(reDate);
			
			/*$(".takeGoodsInfo .takeGoodsCode").html(obj.data.receiveCode);*/
			
			var logInfo = obj.data.shippings;
			/*当有物流信息的时候，，显示物流列表*/
			if(logInfo.length > 0){
				$(".logisiticsInfo").addClass("logInfoType");
				for(var k=0; k<logInfo.length; k++){
					logInfoFun(".logisiticsInfo");
					var logInfoUl = $(".logisiticsInfo").children("ul").eq(k);
						logInfoUl.attr("shippingId", logInfo[k].shippingId);
					logInfoUl.find("li .logCompany").html(logInfo[k].shippingName);
					
					if(logInfo[k].status==logObj[0] || logInfo[k].status==logObj[1]){
						logInfoUl.find(".conButton button").addClass("shouhuo");
					}else if(logInfo[k].status==logObj[2] || logInfo[k].status==logObj[3]){
						logInfoUl.find(".conButton button").addClass("waitTake");
						twoCodeFun(logInfoUl.find(".conButton"));
						//console.log(logInfo[k].shippingId);
						logInfoUl.find(".twoCode img").attr("src",baseUrl+"/api/shipping/getQRCode.jhtml?id="+logInfo[k].shippingId);
						
					}else if(logInfo[k].status==logObj[4]){
						logInfoUl.find(".conButton button").addClass("already");
					}
					
					logInfoUl.find("li.conButton button").html(logType[logInfo[k].status]);
					logInfoUl.find("li.conButton button").attr("status",logInfo[k].status);
					if(logInfo[k].status == "waitCustomerCheck" || logInfo[k].status == "senderDenied"){
						logInfoUl.find("li.conButton").css("display","none");
					};
					
					if(logInfo[k].trackingNo){
						logInfoUl.find("li .logCode").html(logInfo[k].trackingNo);
					}else{
						logInfoUl.find("li .logCode").html("无");
					}
				}
			}else{
				$(".logisiticsInfo").css("display","none");
			};
			
			
			var goodsAllNum = 0;
			/*订单的商品列表展示*/
			for(var i=0; i<obj.data.orderItems.length; i++){
				accountCreateList(".orderAllInfo .order_info");
				var orderDetailLi = $(".orderAllInfo .order_info").children("li").eq(i);
				var dataOrderGoods = obj.data.orderItems[i];
				var orderDetailImg = dataOrderGoods.img?dataOrderGoods.img:defaultImg;
				orderDetailLi.find("img").attr("src",orderDetailImg);
				orderDetailLi.find(".goodsName").html(dataOrderGoods.productName);
				orderDetailLi.find(".detail .right .sum span").html(dataOrderGoods.quantity);
				goodsAllNum += dataOrderGoods.quantity;
				
				if(dataOrderGoods.specs.length>0){
					var specsObj = '';
					for(var k=0; k<dataOrderGoods.specs.length; k++){
						specsObj += dataOrderGoods.specs[k];
					}
					orderDetailLi.find(".goodsTip").append("<span>"+specsObj+"</span>");
				}	
			}
			
			$(".orderAllInfo .total i").html(goodsAllNum);
			
			//商品的附件
			if(obj.data.orderFiles.length>0){
				$(".attachment").addClass("logInfoType");
				var orderData = obj.data.orderFiles;
				for(var j=0; j<orderData.length; j++){
					if(orderData[j].size == 0){continue;}
					fujianFun(".attachment ul");
					var ulLi = $(".attachment ul").children("li").eq(j);
					ulLi.find(".fileName").html(orderData[j].title);
					var size =orderData[j].size/1024/1024;
						size = size.toFixed(2)+"M";
					ulLi.find(".right span").html(size);
					if(orderData[j].title.indexOf("pdf")<0){
						ulLi.find(".fileImg img").attr("src",orderData[j].source);
					}else{
						ulLi.find("a").attr("href",orderData[j].source);
						//ulLi.find("a").attr("href","js/aaa.pdf");
					}
				}		
			}			
		},
		error:function(){}
	});
	
	//点击再来一单，，跳转商品列表页面
	$(".bottom .again").on("click",function(){
		var supplierId = $(this).attr("supplierId");
		var supplyType = $(this).attr("supplyType");
		setSupplierId(supplierId);
		setSupplyType(supplyType);
		window.location = "goodsList.html";
	})
	
	
	
	
	var imgs = ["images/gwc_icon2.png","images/PDF_icon.png","images/gysl_icon.png"];
	var titles = ["gouwuche.png", "lansede.pdf", "hksdjfdkds.jpg"];
	//test();
	function test(){
		for(var i=0; i<3; i++){
			fujianFun(".attachment ul");
			var ulLi = $(".attachment ul").children("li").eq(i);
			ulLi.find("img").attr("src",imgs[i]);
			ulLi.find(".fileName").html(titles[i]);
		}
	}
	/*点击附件，，当是图片的时候，弹出弹出框*/
	$(".attachment ul").delegate("li","click",function(){
		var title = $(this).find(".fileName").html(),
		 	img = $(this).find("img").attr("src");
		if(title.indexOf("pdf")>0){
			//$(this).find("a").attr("href","js/aaa.pdf");
		}else{
			$(".mutailImg").css("display","block");
			$('.mutailImg .title p').html(title);
			$('.mutailImg .img img').attr("src",img);
		}
	})
	/*关闭查看图片的弹窗*/
	$(".mutailImg span").on("click",function(){
		$(".mutailImg").css("display","none");
	})
	
	
	
	/*点击去收货按钮，跳转到客户收货的页面*/
	$(".logisiticsInfo").delegate("ul .conButton button","click",function(){
		
		var shippingId = $(this).parent().parent().attr("shippingId"),
			status = $(this).attr("status");
		
		//console.log(status);
		/*只有是客户未收货，，和送货员拒绝收货时，，才跳转到客户收货页面*/
		if(status == "waitCustomerCheck" || status == "senderDenied"){
			sessionStorage.setItem("shippingId",shippingId);
			//console.log(shippingId);
			window.location = "getGoods.html";
		}
		
	});
	
	
	
	/*$(".bottom .confirm").on("click",function(){
		
		var status = $(".order_num .state").attr("status");
		$.ajax({
			type:"get",
			url:baseUrl+"/api/order/updateStatus.jhtml",
			data:{
				orderId:orderId,
				status:4
			},
			success:function(){
				alert("dfdf");
			},
			error:function(){}
		})
	})*/
	
	
	
	/*把时间毫秒数转化成时间格式*/
	Date.prototype.format = function(format) {
       var date = {
              "M+": this.getMonth() + 1,
              "d+": this.getDate(),
              "h+": this.getHours(),
              "m+": this.getMinutes(),
              "s+": this.getSeconds(),
              "q+": Math.floor((this.getMonth() + 3) / 3),
              "S+": this.getMilliseconds()
       };
       if (/(y+)/i.test(format)) {
              format = format.replace(RegExp.$1, (this.getFullYear() + '').substr(4 - RegExp.$1.length));
       }
       for (var k in date) {
              if (new RegExp("(" + k + ")").test(format)) {
                     format = format.replace(RegExp.$1, RegExp.$1.length == 1
                            ? date[k] : ("00" + date[k]).substr(("" + date[k]).length));
              }
       }
       return format;
}
	
	var time = new Date().format("hh:mm");
	//console.info(time);
	
	
	
	
    //弹出确认框,是否确认取消订单
	$(".cancelOrder").on("click",function(){
		$(".mutail").css("display","block");
		var orderId = $(this).attr("orderId");
		$(".mutail .delSure").attr("orderId",orderId);
		//console.log(orderId);
	})
	
	//取消按钮
	$(".mutail .cancel").on("click",function(){
		$(".mutail").css("display","none");
	})
	
	//确认按钮，，取消订单
	$(".mutail .delSure").on("click",function(){
		var orderId = $(this).attr("orderId");
		$.ajax({
			type:"post",
			url:baseUrl+"/api/order/applyCancel.jhtml",
			data:{
				orderId:orderId
			},
			success:function(obj){
				console.info(obj);
				if(obj.code == "0"){
					//$(".mutail").css("display","none");
					location.reload();
				}
			},
			error:function(){}
		})
	})
	
	
	$(".remark").on("click",function(){
		
		var sum = $(this).find("span").html();
		
		if(sum > 0){
			window.location = "remarkInfo.html";
		};
		
	});
	
	$(".goodInfoList .orderImages").on("click",function(){
		
		window.location = "goodsInfo.html";
		
	});
	
	$(".detailLog").on("click",function(){
		
		var sum = $(this).find("span").html();
		
		if(sum > 0){
			window.location = "orderDate.html";
		};
		
	});
	
	
	$(".detailAttach").on("click",function(){
		
		var sum = $(this).find("span").html();
		
		if(sum > 0){
			window.location = "orderAttach.html";
		}
		
	})
	
	
})












