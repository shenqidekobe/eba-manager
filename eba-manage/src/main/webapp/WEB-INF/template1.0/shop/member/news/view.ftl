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
			.news_con{min-height: 200px;padding:30px 0;}
			.hold_B{margin-top:10px;}
			.con_form{padding:30px 60px;}
			.content_box{position:relative;}
			.content_box .time{
				position:absolute;
				bottom:40px;
				left:60px;
			}
		</style>
	</head>
	<body class="reg10">
		[#include "/shop/common/head.ftl"]
		<div class="page_con">
			<div class="con_center">
				[#include "/shop/member/inc.ftl"]
				<div class="content">
					<form action="">
					<div class="conList">
						<div class="content_box">
							<div class="con_title">
								${newMessageCompamy.newMessage.title}
							</div>
							<div class="con_form">
								<div class="news_con">
									${newMessageCompamy.newMessage.content}
								</div>
							</div>
							
							<span class="time">${newMessageCompamy.newMessage.createDate?string("yyyy-MM-dd HH:mm:ss")}</span>
							
						</div>
						<button type="button" class="hold_B" id="cancelBtn" onclick="location='list.jhtml';">返回</button>
					</div>
					</form>	
				</div>
			</div>
		</div>
		[#include "/shop/common/foot.ftl"]
		
		<script src="${base}/resources/shop/common/js/jquery.min.js"></script>
		<script src="${base}/resources/shop/common/js/common.js"></script>
		<script type="text/javascript" src="${base}/resources/shop/common/js/public.js"></script>
		<script>
			
			$(function(){
				
				var heightObj = $(".page_con .nav").height()-8;
				$(".content_box").css("height",heightObj);
				
				
				
			})
			
		</script>
		
		
		
		
		
	</body>
</html>
