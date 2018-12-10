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
			.form-group{width:1000px;}
			.form-control{display:inline-block;margin-right:10px;}
			.con_form{padding-left:0;}
		</style>
	</head>
	<body class="reg10">
		[#include "/shop/common/head.ftl"]
		<div class="page_con">
			<div class="con_center">
				[#include "/shop/member/inc.ftl"]
				<div class="content">
					<div class="con_form">
						<div class="bind_list"> 
							<span class="bind_logo">
								<img src="${base}/resources/shop/common/images/qq.svg" alt="" />
							</span>
							<div class="bind_con">
								<p>绑定客服人员qq，成为企业的客服，可与其他企业在线沟通</p>
								<button class="bind_B" type="button">立即绑定</button>
							</div>
						</div>
						<div class="bind_list"> 
							<span class="bind_logo">
								<img src="${base}/resources/shop/common/images/qq.svg" alt="" />
							</span>
							<div class="bind_con">
								<p>QQ111111111已经绑定</p>
								<button class="unbind_B" type="button">解除绑定</button>
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

		</script>

	</body>
</html>
