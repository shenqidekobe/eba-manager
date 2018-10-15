

$(function(){
	
	function clickButton(){
	    var obj = $(".yzm_djs");
	    obj.attr("disabled","disabled");/*按钮倒计时*/
	    var time = 60;
	    var set=setInterval(function(){
	    obj.val(--time+"s后重新获取");
	    }, 1000);/*等待时间*/
	    setTimeout(function(){
	    obj.attr("disabled",false).val("重新获取验证码");/*倒计时*/
	    clearInterval(set);
	    }, 60000);
	}
	
	var loginObj = false;
	$(".yzm_djs").on("click",function(){
		var phone = $(".blingding .phone").val();
		if(!(/^1[34578]\d{9}$/.test(phone))){ 
	        errorInfoFun(errInfo.mobileError); 
	        return false; 
    	}
		$.ajax({
			type:"get",
			url:baseUrl+"/api/member/sendSms.jhtml",
//			xhrFields: {withCredentials: true},
			data:{
				tel:phone
			},
			xhrFields: {
				withCredentials: true
			},
			crossDomain: true,
			success:function(obj){
				//console.info(obj);
				if(obj.code == "0"){
					errorInfoFun(errInfo.yanzhengma);
					clickButton();
					loginObj = true;
				}else{
					errorInfoFun(obj.msg);
				}				
			},
			error:function(){ 
				errorInfoFun(errInfo.yanzhengmaError);
		        return false;
			}
		})
	})
	
	
	var backToUrl = GetQueryString("backurl");
	
	$(".blingRight").on("click",function(){
		
		var phone = $(".blingding .phone").val();
		var Short = $(".blingding .yzm_name").val();

		if(!(/^1[34578]\d{9}$/.test(phone))){ 
	        errorInfoFun(errInfo.mobileError); 
	        return false; 
    	} 
		
		if(loginObj == false){
			
			if(!(/^1[34578]\d{9}$/.test(phone))){ 
		        errorInfoFun(errInfo.mobileError); 
		        return false; 
	    	}else{
	    		errorInfoFun(errInfo.placeToYz); 
				return false;
	    	}
	
		}
		
		if(Short == ""){
			errorInfoFun(errInfo.placeToYz); 
			return false;
		}
		
		$.ajax({
			type:"get",
			url:baseUrl+"/api/member/checkSms.jhtml",
			data:{
				tel:phone,
				code:Short
			},
			xhrFields: {
				withCredentials: true
			},
			crossDomain: true,
			beforeSend:function(xhr){
			},
			success:function(obj){
				//console.info(obj);
				if(obj.code == "0"){
					if(backToUrl){
						window.location = backToUrl;
					}else{
						window.location = "supplier.html";
					}
				}else{
					errorInfoFun(obj.msg); 
				}				
			},
			error:function(){
				errorInfoFun(errInfo.ToError); 
				
		        return false;
			}
		})
	})

})

















