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
			a{text-decoration:none}
		</style>
	</head>
	<body class="reg10">
		[#include "/shop/common/head.ftl"]
		<div class="page_con">
			<div class="con_center">
				[#include "/shop/member/inc.ftl"]
				<div class="content">
					<div class="button_box release_goods">
						<div class="img"></div>
						<a href="javascript:void(0);" class='rel_B' onclick="check();">发布新产品</a>
					</div>
					<div class="button_box dhm_goods">
						<div class="img"></div>
						<a href="javascript:void(0);" class='rel_B' onclick="checkDing();">发布微信小程序产品</a>
					</div>
				</div>
			</div>
		</div>
		[#include "/shop/common/foot.ftl"]
		
		<script type="text/javascript" src="${base}/resources/shop/common/js/jquery.min.js"></script>
		<script type="text/javascript" src="${base}/resources/shop/common/js/public.js"></script>
		<script type="text/javascript" src="${base}/resources/shop/common/js/common.js"></script>
		
		<script type="text/javascript">
			function check(){
				if("${supplier.status != 'verified'}"){
					$.message("warn","请先认证企业！");
					return false;
				}
				window.location.href = "add.jhtml";
			}
			function checkDing(){
				if("${supplier.status != 'verified'}"){
					$.message("warn","请先认证企业！");
					return false;
				}
				window.location.href = "dinghuomeList.jhtml";
			}
		
		</script>
	</body>
</html>
