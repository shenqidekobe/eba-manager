[#escape x as x?html]
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
<title>${message("admin.profile.edit")} - Powered By DreamForYou</title>
<meta name="author" content="UTLZ Team" />
<meta name="copyright" content="UTLZ" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/admin1.0/css/public.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/admin1.0/js/jquery.min.js"></script>
<style type="text/css">
	html{width:100%;height:100%;}
	body{background:#f9f9f9;width:100%;height:100%;}
	.div{
		width:300px;
		margin:0 auto;
		text-align: center;
		color:#333;
	}
	p{text-align: center;}
	img{margin-top:40px;width:150px ;height:150px;}
	/*.reson{text-align: left;}*/
	button{margin:20px 0;}
</style>
</head>
<body>
	<div class="div">
		
		<img src="${base}/resources/admin1.0/images/rzsb_icon.png" alt="" />
		
		<p style="line-height: 50px;">您的企业认证未通过!</p>
		<div class="reson">
			原因:<span>
			[#if supplier.reasons ==""]
				无
			[#else]
				${supplier.reasons}
			[/#if]
			
			</span></div>
		<div>
			<button class="tab_button bgred">重新填写提交</button>
		</div>
	</div>
	
	<script>
		$(".bgred").on("click",function(){
			window.location = "index.jhtml";
		})
	</script>
	
</body>
</html>
[/#escape]