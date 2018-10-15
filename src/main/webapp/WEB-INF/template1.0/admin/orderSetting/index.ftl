[#escape x as x?html]
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>订单设置</title>
		<meta http-equiv="X-UA-Compatible" content="IE=edge" />
		<link rel="stylesheet" type="text/css" href="${base}/resources/admin1.0/H-ui/static/h-ui/css/H-ui.min.css" />
		<link rel="stylesheet" type="text/css" href="${base}/resources/admin1.0/H-ui/static/h-ui.admin/css/H-ui.admin.css" />
		<link rel="stylesheet" href="${base}/resources/admin1.0/css/public.css" />
		<link rel="stylesheet" href="${base}/resources/admin1.0/css/admin.css" />
		
		<style>
			body{background:#f9f9f9;}
			.col-sm-7{width:90%}
			body{color:#333;}
			 input:focus { outline: none; }
			input[type=text]{width:120px;}
			.input-text.focus, .textarea.focus{border-color:none;box-shadow:none}
		</style>
	</head>
	<body >
		<div class="child_page"><!--内容外面的大框-->	
			<div class="cus_nav">
				<ul>
					<li><a id="goHome"	href="../homePage/index.jhtml">${message("admin.breadcrumb.home")}</a></li>
					<li>订单设置</li>
				</ul>
			</div>
			<div class="form_box">
				<form action="update.jhtml" class="form form-horizontal" method="post">
					<div class="pag_div">
						<input type="hidden" id="id" name="id" value="${orderSetting.id}" />
						<h3 class="form_title">收货设置</h3>
						<br/>
						说明：该功能主要适用对象为拥有门店的企业
						<div class="info_text">
							<div class="row cl">
								
								<div class="formControls col-xs-8 col-sm-7"> 
									<img src="${base}/resources/admin1.0/images/bitian_icon.svg" alt="" />
									下单时间：  每天 
									 <input type="text" readonly="readonly" id="startTime" name="startTime" value="${orderSetting.startTime}" class=" text input-text radius reDate"
	                               onfocus="WdatePicker({dateFmt: 'HH:mm:ss',readOnly:true});"/>
									至
									<input type="text" readonly="readonly" id="endTime" name="endTime" value="${orderSetting.endTime}" class=" text input-text radius reDate"
	                               onfocus="WdatePicker({dateFmt: 'HH:mm:ss',readOnly:true});"/>
									之间允许门店下单
								</div>
							</div>
							<div class="row cl">
								
								<div class="formControls col-xs-8 col-sm-7"> 
									<img src="${base}/resources/admin1.0/images/bitian_icon.svg" alt="" />
									收货时间：  门店下单至少
									<input type="text" class="input-text radius" name="timeReceipt" id="timeReceipt" value="${orderSetting.timeReceipt}"  maxlength="20" />
									天后可以选择收货，默认为0，即下单后可以选择当天收货
								</div>
							</div>
							
							<div class="row cl">
								<div class="formControls col-xs-8 col-sm-7"> 
									<img src="${base}/resources/admin1.0/images/bitian_icon.svg" alt="" />
									下单次数： 每个门店每天最多可下单
									<input type="text" class="input-text radius" name="numberTimes" id="numberTimes" value="${orderSetting.numberTimes}"/>
									次，不包含已拒绝和已取消的订单
								</div>
							</div>
							
						</div>
					</div>
					<div class="footer_submit">
						<input class="btn radius confir_S" type="submit" value="${message("admin.common.submit")}" />
						<input class="btn radius cancel_B" type="button" value="${message("admin.common.back")}" onclick="history.back(); return false;" />
					</div>	
				</form>
			</div>
		</div>
		<script src="${base}/resources/admin1.0/js/jquery.min.js"></script>
		<script type="text/javascript" src="${base}/resources/admin1.0/H-ui/static/h-ui/js/H-ui.min.js"></script> 
		<script type="text/javascript" src="${base}/resources/admin1.0/H-ui/static/h-ui.admin/js/H-ui.admin.js"></script> 
		<script type="text/javascript" src="${base}/resources/admin1.0/js/jquery.lSelect.js"></script>
		<script type="text/javascript" src="${base}/resources/admin1.0/js/validate/jquery.validate.min.js"></script>
		<script type="text/javascript" src="${base}/resources/admin1.0/js/webuploader.js"></script>
		<script type="text/javascript" src="${base}/resources/admin1.0/ueditor/ueditor.js"></script>
		<script type="text/javascript" src="${base}/resources/admin1.0/js/common.js"></script>
		<script src="${base}/resources/admin1.0/datePicker/WdatePicker.js"></script>
		<script type="text/javascript">
		[@flash_message /]
			$(document).ready(function(){
			  $id = $("#id");
			  $startTime = $("#startTime");
			  $endTime = $("#endTime");
			  $timeReceipt = $("#timeReceipt");
			  $numberTimes = $("#numberTimes");
			  if($id.val() == "") {
				  $startTime.val("00:00:00");
				  $endTime.val("23:59:59");
				  $timeReceipt.val(0);
				  $numberTimes.val(10);
			  }
			  
			  
			var formHeight = $(document.body).height() - 100;
       	 	$(".form_box").css("height", formHeight);
			  
			  
			  $("#goHome").on("click",function(){
					var nav = window.top.$(".index_nav_one");
	        			nav.find("li li").removeClass('clickTo');
						nav.find("i").removeClass('click_border');
				})
			  
			  
			});
		</script>
	</body>
</html>
[/#escape]