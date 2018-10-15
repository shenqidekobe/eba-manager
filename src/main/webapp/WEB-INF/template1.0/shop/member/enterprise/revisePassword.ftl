<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge" />
		<title>微商平台</title>
		<link rel="stylesheet" href="${base}/resources/shop/common/config/bootstrap/css/bootstrap.min.css" />
		<link rel="stylesheet" href="${base}/resources/shop/common/css/public.css" />
		<link rel="stylesheet" href="${base}/resources/shop/common/css/index.css" />
		<link rel="stylesheet" href="${base}/resources/shop/common/css/member.css" />
		<style>
			.form-group{width:650px;}
			.form-control{display:inline-block;margin-right:10px;}
			.xxDialog {top: 120px;}
	        .xxDialog .dialogBottom {
	            width: 100%;
	            position: absolute;
	            bottom: 0;
	        }
	        .dialogContent {
	            height: calc(100% - 80px);
	            overflow: auto;
	            overflow-x: hidden;
	        }
	        .xxDialog .dialogBottom{height:42px;}
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
							修改密码
						</div>
					
						<div class="con_form">
							<form class="form-horizontal">
								<div class="form-group">
								    <label class="col-sm-4 control-label">旧密码</label>
								    <div class="col-sm-7">
								     	<input class="form-control" type="text" />
								    </div>
								</div>
								<div class="form-group">
								    <label class="col-sm-4 control-label">新密码</label>
								    <div class="col-sm-7">
								     	<input class="form-control" type="text" />
								    </div>
								</div>
								
								<div class="form-group">
								    <label class="col-sm-4 control-label">确认密码</label>
								    <div class="col-sm-7">
								     	<input class="form-control" type="text" />
								    </div>
								</div>
								<div class="form-group" style="width:900px">
								    <label class="col-sm-4 control-label"></label>
								    <div class="col-sm-7" style="width:700px">
								     	<button type="button" class="hold_B">保存</button>
								    </div>
								</div>
							</form>
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
			var heightObj = $(".page_con .nav").height()-9;
			$(".content_box").css("height",heightObj);
		</script>

	</body>
</html>
