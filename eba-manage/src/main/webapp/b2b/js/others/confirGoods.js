

$(function(){
	
	function getGoodsList(obj){
		$('<li><img src="images/mr_icon.png" alt=""><div class="detail"><p class="left"><span class="goodsName"></span><span class="goodsTip"></span></p><p class="right"><span class="sum">×<span></span></span></p></div><p class="pSum"><span>实收数量</span><input type="text" disabled style="background:#fff;"></p></li>').appendTo(obj);
	};
	
	
	var shippingId = GetQueryString("id");
	
	$.ajax({
    	type:"get",
    	url:baseUrl+"/api/shipping/get.jhtml",
    	data:{
    		id:shippingId
    	},
    	async:false,
    	success:function(obj){
    		//console.log(obj);
    		var conInfo = obj.data.items;
    		for(var i=0; i<conInfo.length; i++){
    			getGoodsList(".order_info");
    			logInfoUl = $(".order_info").children("li").eq(i);
    			logInfoUl.attr("");
    			logInfoUl.find(".goodsName").html(conInfo[i].productName);
    			logInfoUl.find("img").attr("src", conInfo[i].img);
    			logInfoUl.find(".sum span").html(conInfo[i].quantity);
    			logInfoUl.find(".pSum input").val(conInfo[i].realQuantity);
    			if(conInfo[i].specs.length > 0){
					var specsObj = '';
					for(var k=0; k<conInfo[i].specs.length; k++){
						specsObj += conInfo[i].specs[k];
					}
					logInfoUl.find(".goodsTip").append("<span>"+specsObj+"</span>");
				}
    		};
    	},
    	error:function(){}
	})
	
	
	/*点击确认按钮*/
	$(".totalPrice span").click(function(){
		var passed = $(this).attr("passed"),
		    code = $('.goodsCode input').val();
			
			if(code == ""){
				errorInfoFun("送货码不能为空");
				return ;
			};
			
		$.ajax({
	    	type:"post",
	    	url:baseUrl+"/api/shipping/senderCheck.jhtml",
	    	data:{
	    		id:shippingId,
	    		code:code,
	    		passed:passed
	    	},
	    	async:false,
	    	success:function(obj){
	    		//console.log(obj);
	    		if(obj.code == "0"){
	    			localStorage.setItem("pass",passed);
					window.location = "confirResult.html";
	    		}else{
	    			errorInfoFun("送货码输入错误，请重新输入！");
	    		}
	    	},
			error:function(){}
		});
	});
	
	
	/*送货码的效果js*/
	$('.goodsCode input').on('input', function (e){
		var numLen = 6;
		var pw = $('.goodsCode input').val();
		var list = $('li');
		for(var i=0; i<numLen; i++){
			if(pw[i]){
				$(list[i]).text(pw[i]);
			}else{
				$(list[i]).text('');
			}
		}
	});
	
	
})










