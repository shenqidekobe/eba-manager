
//var baseUrl = "http://192.168.0.111:8088";
	
	var baseUrl = "";
	
	
	//默认图片
	var defaultImg = "images/mr_icon.png";
	//创建商品列表的标签的建立，li
	function goodsListCreate(obj){
		$('<li class="product"><div class="img lf"><img src="images/mr_icon.png" alt=""></div><div class="detail lf"><p class="title2"></p><p class="size"></p><div class="price_num"></div></div></li>').appendTo(obj);
	}
	
	//当商品没有规格可以选的时候，显示加减号，，添加和减少商品的数量
	function goodsAddMinCreate(obj){
		$('<div class="goodsnum num2 goodsSum"><button type="button" class="minus">-</button><input type="text" value="0";/><button type="button" class="plus">+</button></div>').appendTo(obj);
	}
	
	//当商品有规格可以选的时候，显示可以选择规格按钮
	function goodsTypeCreate(obj){
		$('<a class="choose1 cd-popup-trigger2" href="javascript:0;">选规格</a>').appendTo(obj);
	}
	
	//商品规格的选择li创建
	function goodsTypeTo(obj){
		$('<li></li>').appendTo(obj);
	}
	
	//供应商的supplierId
	function setSupplierId(obj){
		localStorage.setItem("needSupplierId",obj);
	};
	function getSupplierId(){
		return localStorage.getItem("needSupplierId");
	};
	
	//供应商的类型
	function setSupplyType(obj){
		localStorage.setItem("supplyType",obj);
	};
	function getSupplyType(){
		return localStorage.getItem("supplyType");
	};
	
	//供应商的类型
	//var supplyType = getSupplyType();
	//供应商的supplierId
	// supplierId = getSupplierId();
	
	//onloadNum();
	//当商品的数量为0的时候，隐减号和数量
	function onloadNum(){
		var nums = $(".goodsnum input").val();
		if(nums == 0){
			$(".goodsnum input").css("display","none");
			$(".goodsnum button.minus").css("display","none");
		}else{
			$(".goodsnum input").css("display","block");
			$(".goodsnum button.minus").css("display","block");
		}
	}

//	var strFullPath = window.document.location.href;
//	console.info(strFullPath);
	
	//选中地址栏中的地址，截取需要的name部分
	function GetQueryString(name) {
	    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
	    var r = window.location.search.substr(1).match(reg);
	    if (r != null)
	        return unescape(r[2]);
	    return null;
	}

	//商品列表和搜索列表的创建列表标签
	function CreateGoodsList(obj){
		if(obj.code != "成功"){
			return false;
		}
		for(var i=0; i<obj.data.length; i++){
			goodsListCreate(".company");
			var goodsListNum = $(".company").children("li").eq(i);
			var goodsData = obj.data[i];
			var goodsImgSrc = goodsData.image?goodsData.image:defaultImg;
			goodsListNum.find(".img img").attr("src",goodsImgSrc);
			goodsListNum.find(".detail .title2").html(goodsData.name);
			goodsListNum.find(".detail .jieshao").html(goodsData.caption);
			if(goodsData.hasSpecification){
				goodsTypeCreate(goodsListNum.find(".price_num"));
			}else{
				goodsAddMinCreate(goodsListNum.find(".price_num"));
				var goodsTypeMinAdd = goodsListNum.find(".price_num .goodsnum");
				if(goodsTypeMinAdd.find("input").val() == 0){
					goodsTypeMinAdd.find("input").css("display","none");
					goodsTypeMinAdd.find(".minus").css("display","none");
				}
			}
			goodsListNum.attr("productId",goodsData.productId);	
			goodsListNum.attr("goodsId",goodsData.goodsId);	
		}
	}
	
	//显示购物车商品数量
	function takeGoodsNum(){
		var goodsNumObj={allNum:0};
		$.ajax({
			type:"get",
			url:baseUrl+"/api/cart/list.jhtml",
			data:{
				supplyType:getSupplyType(),
				supplierId:getSupplierId()
			},
			async:false,
			success:function(obj){
				//console.info(obj);
				if(obj.data.length == 0){
					$(".totalPrice .icon span.num").html(0);
				}
				for(var i=0; i<obj.data.length; i++){
					if(obj.data[i].supplierId == getSupplierId()){
						var index = 0;
						for(var j=0; j<obj.data[i].goods.length; j++){
							var goodsData = obj.data[i].goods[j];
							goodsNumObj[goodsData.productId] = goodsData.quantity;
							index += obj.data[i].goods[j].quantity;
						}
						var indexSum = index > 99?"99+":index;
						$(".totalPrice .icon span.num").html(indexSum);
						goodsNumObj["allNum"] = index;
					}
				}
				goodsNumObj = JSON.stringify(goodsNumObj);
				localStorage.setItem("goodsNumObjObj",goodsNumObj);
				
			}
		})
	}
	
	
	
	
	
	//点击加减号，，增加和减少商品数量
	function addMinus(productId,goodsNum){
		$.ajax({
			type:"get",
			url:baseUrl+"/api/cart/add.jhtml",
			data:{
				supplyType:getSupplyType(),
				supplierId:getSupplierId(),
				productId:productId,
				quantity:goodsNum
			},
			success:function(){
				//takeGoodsNum();
				//alert("请求成功");
				updateCartCache(productId , goodsNum);
			},
			error:function(){
				//alert("请求失败");
			}
		})
	}
	function updateCartCache(productId , goodsNum){
		var goodsNumObj = JSON.parse(localStorage.getItem("goodsNumObjObj"));
		var beforeNum = goodsNumObj[productId]?goodsNumObj[productId]:0;
		var totalNum = goodsNumObj["allNum"]?goodsNumObj["allNum"]:0;
		totalNum = totalNum - beforeNum + goodsNum*1 ;
		goodsNumObj[productId] = goodsNum*1 ;
		goodsNumObj["allNum"] = totalNum ;
		goodsNumObj = JSON.stringify(goodsNumObj);
		localStorage.setItem("goodsNumObjObj",goodsNumObj);
		
		var allSum = totalNum > 99?"99+":totalNum;
		$(".totalPrice .num").html(allSum);

	}
	
	function addCartCache(productId , totalNum){
		//购物车的商品数量
		var goodsNumObj = JSON.parse(localStorage.getItem("goodsNumObjObj"));
		//判断是否存在，如果存在的话 ， allNum = allNum - 之前 + 现在
		//不存在，allNum = allNum + goodsNum;
		
		goodsNumObj[productId] = goodsNum ;
		
		//修改总数量
		//console.info(goodsNumObj);
	}
	
	function subCartCache(productId , totalNum){
		
	}
	
	//点击删除商品，本地购物车的数量变化
	function delCartCache(productId){
		var goodsNumObj = JSON.parse(localStorage.getItem("goodsNumObjObj"));
		var goodsNum = goodsNumObj[productId] ;
		var totalNum = goodsNumObj['allNum'] - goodsNum ;
		delete goodsNumObj[productId];
		goodsNumObj['allNum'] = totalNum ;

		goodsNumObj = JSON.stringify(goodsNumObj);
		localStorage.setItem("goodsNumObjObj",goodsNumObj);
		$(".totalPrice .num").html(totalNum);

	}
	
	function getCartNum(productId){
		//购物车的商品数量
		var goodsNumObj = JSON.parse(localStorage.getItem("goodsNumObjObj"));
		
		return goodsNumObj[productId]?goodsNumObj[productId]:0 ;
	}
	
	//点击规格表示选中的那个
	$(".modelType ul").delegate("li","click",function(){
		$(".modelType ul li").removeClass("modelActive");
		$(this).addClass("modelActive");
	})
	
	
	//选择规格里面的加减商品数量
    //点击加号
    $(".add .goodsnum .plus").on("click",function(){
   		var nums = $(this).prev().val()*1+1;
   		$(this).prev().val(nums);
   	})
    //点击减号
   	$(".add .goodsnum .minus").on("click",function(){
   		var nums = $(this).next().val()*1-1;
   		if(nums == 0){
   			nums = 1;
   		}
   		$(this).next().val(nums);
   	})
	//规格数量的选择
	$(".goodsnum input").on("keyup",function(){
		var val = $(this).val();
		$(this).val(val.replace(/[^\d]/g,''));
		if(val >9999){
			$(this).val("9999");
		}else if(val === "0"){
			$(this).val(1);
		}
	})
	
	$(".goodsnum input").on("blur",function(){
		if($(this).val() == ""){
			$(this).val("1");
		}
	})
	
	
	
	//打开窗口,选择商品规格
    $(".company").delegate("li .price_num .cd-popup-trigger2",'click', function(event){
        event.preventDefault();
        $('.cd-popup2').addClass('is-visible2');
        
        //onloadNum();
        var goodsId = $(this).parent().parent().parent().attr("goodsId");
        //console.info(goodsId);
        var parentsObj = $(this).parent().parent().parent();
        var imgSrc = parentsObj.find("img").attr("src");
        var title = parentsObj.find(".title2").html();
        var jieshao = parentsObj.find(".jieshao").html();
        	$(".add .goodsnum input").val("1");
        
        $.ajax({
        	type:"get",
        	url:baseUrl+"/api/product/productSpecifications.jhtml",
        	data:{
        		id:goodsId,
        		supplierId:getSupplierId(),
        		supplyType:getSupplyType()
        	},
        	success:function(obj){
        		//console.info(obj);
        		$(".modelTypeUl").html("");
        		for(var i=0; i<obj.data.length; i++){
        			goodsTypeTo(".modelTypeUl");
        			var modelCon = '';
        			var modelValue = obj.data[i].specificationValues;
        			for(var j=0; j<modelValue.length; j++){
        				modelCon += modelValue[j].value;
        			}
        			var modelTypeLi = $(".modelTypeUl").children('li').eq(i);
        			modelTypeLi.html(modelCon);
        			modelTypeLi.attr("productId",obj.data[i].productId);
        		}
				$(".modelTypeUl").children('li').eq(0).addClass("modelActive");
        		var popup = $(".cd-popup-container2");
        		popup.find(".left img").attr("src",imgSrc);
        		popup.find(".product_name2").html(title);
        		popup.find(".product_intro2").html(jieshao);
        	},
        	error:function(){
        		
        	}
        }) 
    });
    
    
    //选择规格后加入购物车
    $(".cd-popup-container2 .addCart").on("click",function(){
    	
    	var productId = $(".modelType ul li.modelActive").attr("productId");
    	var goodsSum = $(".add .goodsnum input").val()*1;
    	
    	//console.info(productId,goodsSum);
    	if(productId == undefined){
    		errorInfoFun(errInfo.goodstype);
    		return false;
    	}

		goodsSum = getCartNum(productId) + goodsSum ;

    	addMinus(productId,goodsSum);
    	
    	$(".cd-popup2").removeClass("is-visible2");
    })
    
    // 点击结算，当购物车有商品的时候，才跳转到结算页面。
    $(".totalPrice .account").on("click",function(){
   		var goodsAllNum = $(".totalPrice .icon span").html()*1;
   		if(goodsAllNum > 0){
   			$(this).attr("href","account.html");
   		}
    })
    
    //关闭窗口
    $('.cd-popup2').on('click', function(event){
        if( $(event.target).is('.cd-popup-close') || $(event.target).is('.cd-popup2') ) {
            event.preventDefault();
            $(this).removeClass('is-visible2');
        }
    });
	
	
	
	//当输入的格式不正确的时候，显示提示信息
	function errorInfoFun(obj){
		$(".pageRemind").html(obj).css({"display":"block"});
		setInterval(function(){
			$(".pageRemind").fadeOut(2000);
		},2000) 
	}
	
	//错误提示信息
	var errInfo = {
		mobileError:"手机号码格式不正确,请重新填写！",
		reDateError:"请选择收货时间",
		userNameError:"联系人不能为空！",
		goodsNumOne:"商品的数量不能再减少了！",
		goodstype:"请选择商品的规格！",
		yanzhengma:"验证码获取成功！",
		yanzhengmaError:"验证码获取失败！",
		placeToYz:"请获取短信验证码并输入",
		ToError:"输入的信息不对"
	};

	//商品的状态
	var statusObj = {
		0:"等待付款",
		1:"等待审核",
		2:"等待发货",
		3:"已发货",
		4:"已收货",
		5:"已完成",
		6:"已失败",
		7:"已取消",
		8:"已拒绝",
		9:"申请取消",
		10:"通过取消",
		11:"拒绝取消"
	};
	
	var logType = {
		waitCustomerCheck: "收货",
		customerChecked: "待司机确认",
		waitSenderCheck: "待司机确认",
		senderChecked: "已收货",
		senderDenied: "收货"
	};
	var logObj = {
		0:"waitCustomerCheck",
		1:"senderDenied",
		2:"customerChecked",
		3:"waitSenderCheck",
		4:"senderChecked"
	}


	//商品列表，，没有规格选择，，假如购物车的数量的操作
	//商品的数量，的设置
	$(".company").delegate("li .goodsnum input","keyup",function(){
		var val = $(this).val();
		$(this).val(val.replace(/[^\d]/g,''));
		if(val >9999){
			$(this).val("9999");
		}else if(val === "0"){
			$(this).val(1);
		}
	})
	
	//自动输入商品的数量
	$(".company").delegate("li .goodsnum input","blur",function(){
		if($(this).val() == ""){
			$(this).val("1");
		}
		var productId = $(this).parent().parent().parent().parent().attr("productId");
		var goodsSum = $(this).val() * 1;
	
	    addMinus(productId,goodsSum);
		
	})


	


	










