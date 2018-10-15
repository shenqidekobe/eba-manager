

$(function(){

	var status = "",searchValue = "";
	//supplierListCreate(status,searchValue);
	
	$(".search2 .ToSearch").on("click",function(){
	
		searchValue = $(".search2 .searchOut input").val();
		if(searchValue == ""){
			return false;
		}
		supplierListCreate(status,searchValue);
		searchOrderArr.unshift(searchValue);
		addCookie("searchOrderName",searchOrderArr,null);
		$(".searchHostory").css("display","none");
		console.log(!$("#menu_con .tag ul").length);
		if(!$("#menu_con .tag ul").length){
			$(".notToSearch").css("display","block");
		}else{
			$(".notToSearch").css("display","none");
		}
	})
	
	
	
	$(".searchHostory .searchHistory").delegate("li","click",function(){
		
		var searchCon = $(this).html();
		$(".search2 .searchOut input").val(searchCon);
		$.ajax({
			type:"get",
			url:baseUrl+"/api/member/orderList.jhtml",
			data:{
				status:status,
				searchProperty:"sn",
				searchValue:searchCon
			},
			success:function(obj){
				//console.info(obj.data);
				$("#menu_con .tag").html("");
				supplierAjax(obj);
				$(".searchHostory").css("display","none");
				if(!$("#menu_con .tag ul").length){
					$(".notToSearch").css("display","block");
				}else{
					$(".notToSearch").css("display","none");
				}
			},
			error:function(){}
		})
	
	})
	
	
})


















