[#escape x as x?html]
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge" />
		<title>华奕优选</title>
		<link rel="stylesheet" href="${base}/resources/shop/common/config/bootstrap/css/bootstrap.min.css" />
		<link rel="stylesheet" href="${base}/resources/shop/common/css/public.css" />
		<link rel="stylesheet" href="${base}/resources/shop/common/css/index.css" />
		<link rel="stylesheet" href="${base}/resources/shop/common/css/member.css" />
		<style>
			.form-group{width:1000px;}
			.form-control{display:inline-block;margin-right:10px;}
			.auth_imgs li{
				float:left;
				width:150px;
				height:150px;
				overflow: hidden;
				border-radius: 5px;
				margin-right:10px;
			}
			.auth_imgs li img{
				width:150px;
				height:150px;
			}
		</style>
	</head>
	<body class="reg10">
		[#include "/shop/common/head.ftl"]
		<div class="page_con">
			<div class="con_center">
				[#include "/shop/member/inc.ftl"]
				<div class="content">
					<div class="content_box">
						<div class="con_title">
							[#if supplier.status == 'verified']
							<span>认证已通过</span>
							[#elseif supplier.status == 'certification']
							<span>认证中</span>
							[/#if]
						</div>
						<div class="con_form">
							<div class="form-group">
							    <label class="col-sm-4 control-label">工商证件</label>
							    <div class="col-sm-9">
							    	<input type="hidden" id="businessCard" value="${supplier.businessCard}">
							     	<ul class="auth_imgs">
							     		
							     	</ul>
							    </div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		[#include "/shop/common/foot.ftl"]
		
		
		<script type="text/javascript" src="${base}/resources/shop/common/js/jquery.min.js"></script>
		<script type="text/javascript" src="${base}/resources/shop/common/js/common.js"></script>
		<script type="text/javascript" src="${base}/resources/shop/common/js/public.js"></script>
		<script type="text/javascript">
			$().ready(function() {
		        
		      	//显示工商证件
			  	 if($("#businessCard").val() != ''){
			 		var businessCardImg=eval('(' + $("#businessCard").val() + ')');
			 		for(var o in businessCardImg){  
			 	         $(".auth_imgs").append("<li><a href='"+businessCardImg[o].imgUrl+"' target='view_window'><img src='"+businessCardImg[o].imgUrl+"' /></a></li>");
			 	      } 
			 	}else{
			 		$(".auth_imgs").append("<p style='line-height:36px;color:#586897;'>无</p>");
			 	}
			});
		
		
			var heightObj = $(".page_con .nav").height()-8;
			$(".content_box").css("height",heightObj);
			
			
			
		</script>

	</body>
</html>
[/#escape]