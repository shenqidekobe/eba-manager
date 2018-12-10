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
			.page_con .content,.con_center{height:400px;min-height:400px;}
			.int_info .title_h3{
				background:url(${base}/resources/shop/common/images/wenzu-6.png) no-repeat center;
				background-size:250px 60px;
			}
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
		                <a class="nav-bar-active nav-type" href="javascript:void(0);" target="_self" title="关于我们">关于我们</a>
		
		            </div>
		        </div>
		    </div>
			<div class="con_center">
				<div class="nav">
					<ul class="nav_ul">
						<li class="li_hover nav_selected"><a href="">关于我们</a></li>
					</ul>
				</div>
				<div class="content">
					<form action="">
					<div class="conList">
						<div class="content_box">
								
							<div class="int_info">
								<div class="title">
									<h3 class="title_h3">
										关于我们
									</h3>
								</div>
								<div class="con">上海云食信息科技有限公司成立于2017年，是一家标准的新兴互联网公司。其团队核心成员均来自于惠普，阿里，上海钢银等大型知名上市公司。在互联网各个领域拥有深厚的底蕴。旗下目前主要运营微信小程序项目。
     微信小程序是国内领先的餐饮食品采购供应链服务平台，以连锁餐饮为切入点，提供前端B2B贸易信息撮合，订单SAAS服务流转，物流跟踪，在线金融等一系列企业服务。我们做的不仅是一个第三方电商平台，更是为企业用户提供一篮子商业解决方案。     
</div>
							</div>
								
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
