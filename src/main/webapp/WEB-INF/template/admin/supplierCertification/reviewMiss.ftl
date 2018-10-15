[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>${message("admin.profile.edit")} - Powered By DreamForYou</title>
<meta name="author" content="UTLZ Team" />
<meta name="copyright" content="UTLZ" />
<link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
<style type="text/css">
	.div{
		width:500px;
		margin:100px auto;
	}
</style>
</head>
<body>
	<div class="div">
		<h1>
			您的企业认证未通过!
		</h1>
		<br/>
		<br/>
		<h1>原因:${supplier.reasons}</h1>
		<br/>
		<br/>
		<br/>
		<br/>
		<div>
			<a href="index.jhtml" class="button">重新填写提交</a>
		</div>
	</div>
</body>
</html>
[/#escape]