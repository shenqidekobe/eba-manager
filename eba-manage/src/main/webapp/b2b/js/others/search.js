


$(function(){
		
	var searchArr = [];
	if(getCookie("searchArrName")){
		searchArr = $.unique(getCookie("searchArrName").split(","));
		if(searchArr.length >10){
			searchArr.length = 10;
		}
		for(var i=0; i<searchArr.length; i++){
			$(".searchHostory .searchHistory").append("<li>"+searchArr[i]+"</li>");
		}
	}
	//removeCookie("searchArrName",null);
	
	
	
	$(".searchHostory .searchHistory").delegate("li","click",function(){
		var searchCon = $(this).html();
		$(".search2 .searchOut input").val(searchCon);
		$.ajax({
			type:"get",
			url:baseUrl+"/api/product/goodsList.jhtml",
			data:{
				supplyType:getSupplyType(),
				supplierId:getSupplierId(),
				keyword:searchCon
			},
			success:function(obj){
				$(".company").html("");
				CreateGoodsList(obj);
				searchGoodsNum();
				if(!$(".company li").length){
					$(".searchHostory").css("display","none");
					$(".notToSearch").css("display","block");
				}else{
					$(".notToSearch").css("display","none");
				}
			},
			error:function(){
				
			}
		})
	})
		
	searchGoodsNum();
	//当搜索商品的数量不为0的时候，显示结算标签和隐藏历史搜索
	function searchGoodsNum(){
		if($(".company li").length>0){
			$(".searchHostory").css("display","none");
			$(".totalPrice").css("display","block");
		}else{
			$(".searchHostory").css("display","block");
			$(".totalPrice").css("display","none");
		}
	}
	
	var supplierId = localStorage.getItem('needSupplierId');
	var keyword = $(this).val();
	if(keyword == ""){
		$(".company").html("");
		searchGoodsNum();
	}
	
	
	//点击搜索按钮，搜索出来的商品列表
	$(".search2 .button").on("click",function(){
		var keyword = $(".search2 .searchOut input").val();
		
		if(keyword == ""){
			$(".company").html("");
			searchGoodsNum();
			return false;			
		}
		$.ajax({
			type:"get",
			url:baseUrl+"/api/product/goodsList.jhtml",
			data:{
				supplyType:getSupplyType(),
				supplierId:getSupplierId(),
				keyword:keyword
			},
			success:function(obj){
				$(".company").html("");
				//console.info(obj);
				CreateGoodsList(obj);
				searchGoodsNum();
				//搜索成功的商品名，保存历史中
				if(!$(".company li").length){
					$(".searchHostory").css("display","none");
					$(".notToSearch").css("display","block");	
				}else{
					$(".notToSearch").css("display","none");
				}
				searchArr.unshift(keyword);
				addCookie("searchArrName",searchArr,null);
				
			},
			error:function(){
				
			}
		})
		
	})
	
	
	//显示购物车商品的数量
	takeGoodsNum();
	
	//点击加号
	$(".company").delegate("li .goodsnum button.plus","click",function(){
		var nums = $(this).prev().val()*1+1;
		$(this).prev().val(nums);
		if(nums != 0){
			$(this).siblings().css("display","block");
		}
		var productId = $(this).parent().parent().parent().parent().attr("productId");
		//var goodsNum = nums;
		var goodsNum = getCartNum(productId) + 1 ;
		addMinus(productId,goodsNum);
		
	})
	//点击减号
	$(".company").delegate("li .goodsnum button.minus","click",function(){
		var nums = $(this).next().val()*1-1;
		 $(this).next().val(nums);
		if(nums <= 0){
			$(this).next().css("display","none");
			$(this).css("display","none");
		}
		var productId = $(this).parent().parent().parent().parent().attr("productId");
		//var goodsNum = nums;
		var goodsNum = getCartNum(productId) - 1 ;
		addMinus(productId,goodsNum);
		
	})


	//点击结算，当购物车有商品的时候，跳转到结算页面
	$("a.account").on("click",function(){
		if($(".totalPrice .num").html()*1 != 0){
			$(this).attr("href","account.html");
		}

	})
	
})










