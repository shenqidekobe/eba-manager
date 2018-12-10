<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge" />
		<title>微信小程序</title>
		<link rel="stylesheet" href="${base}/resources/shop/common/config/bootstrap/css/bootstrap.min.css" />
		<link rel="stylesheet" href="${base}/resources/shop/common/css/public.css" />
		<link rel="stylesheet" href="${base}/resources/shop/common/css/index.css" />
		<link rel="stylesheet" href="${base}/resources/shop/common/css/member.css" />
		<style>
			.content_box{
				margin-top:0;
			}
			.content_box img{
				width:100%;
			}
			.page_con{overflow-x: hidden;}
		</style>
	</head>
	<body class="reg10">
		[#include "/shop/common/head.ftl"]
		<div class="page_con">
			<div class="dh-nav-bar-selected">
		        <div class="nav-bar-con">
		            <div class="nav-bar">
		                <a class="nav-bar-active" href="/shop/index.jhtml" target="_self" title="首页">首页</a>
		                <i>/</i>
		                <a class="nav-bar-active nav-type" href="javascript:void(0);" target="_self" title="如何发布采购">如何发布采购</a>
		            </div>
		        </div>
		    </div>
			<div class="con_center">
				<div class="nav">
					<ul class="nav_ul">
						<li class="font_bold">帮助中心</li>
						<li class="li_hover"><a href="/shop/jumpPubType.jhtml?pubType=pub_supply">如何发布供应</a></li>
						<li class="li_hover nav_selected"><a href="/shop/jumpPubType.jhtml?pubType=pub_need">如何发布采购</a></li>
					</ul>
				</div>
				<div class="content">
					<form action="">
					<div class="conList">
						<div class="content_box">
								
							<img src="${base}/resources/shop/common/images/caigou.jpg" alt="" />
								
						</div>
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
				
			})
			
		</script>
		
		
		
		
		
	</body>
</html>
