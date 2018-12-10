[#escape x as x?html]
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
			.xxDialog {top: 120px;}
	        .xxDialog .dialogBottom {
	            width: 100%;
	            position: absolute;
	            bottom: 0;
	        }
	        .dialogContent,.xxDialog .dialogwarnIcon {
	            height: calc(100% - 80px);
	            overflow: auto;
	            overflow-x: hidden;
	        }
	        .xxDialog .dialogBottom{height:42px;}
	        .xxDialog .dialogwarnIcon{
	        	margin:0;
	        	line-height:150px;
	        }
		</style>
	</head>
	<body class="reg10">
		[#include "/shop/common/head.ftl"]
		<div class="page_con">
			<div class="con_center">
				[#include "/shop/member/inc.ftl"]
				<div class="content">
					<form id="unbundledForm" action="delete.jhtml" method="post">
					    <input type="hidden" name="id" value="${supplier.id}"/>
					</form>
					<div class="con_form">
						[#if exist == 0]
						<div class="bind_list"> 
							<span class="bind_logo">
								<img src="${base}/resources/shop/common/images/qq.svg" alt="" />
							</span>
							<div class="bind_con">
								<p>绑定客服人员qq，成为企业的客服可在线沟通。</p>
								<button class="bind_B" type="button">立即绑定</button>
							</div>
						</div>
						[#else]
						<div class="bind_list"> 
							<span class="bind_logo">
								<img src="${base}/resources/shop/common/images/qq.svg" alt="" />
							</span>
							<div class="bind_con">
								<p>${supplier.qqCustomerService}已经绑定</p>
								<button class="unbind_B" type="button">解除绑定</button>
							</div>
						</div>
						[/#if]
					</div>
				</div>
			</div>
		</div>
		[#include "/shop/common/foot.ftl"]
		
		
		<script type="text/javascript" src="${base}/resources/shop/common/js/jquery.min.js"></script>
		<script type="text/javascript" src="${base}/resources/shop/common/js/common.js"></script>
		<script type="text/javascript" src="${base}/resources/shop/common/js/public.js"></script>
		<script type="text/javascript" src="${base}/resources/shop/common/js/validate/jquery.validate.min.js"></script>
		<script type="text/javascript">
			$(".bind_B").click(function(){
		    	$.dialog({
	                title: "绑定QQ号",
	                width:600,
	                height:250,
	                content: [@compress single_line = true]
                	'<form id="bindqq" class="form form-horizontal" action="" method="">
                		<div class="modelCon" style="padding:50px 80px 20px 80px;">
                			
                			<div class="form-group" style="margin-bottom:30px">
							    <label class="col-sm-4 control-label"><img src="${base}/resources/shop/common/images/bitian_icon.svg" alt="" />QQ号<\/label>
							    <div class="col-sm-7">
							     	<input class="form-control" type="text" name="qqCustomerService" id="qqCustomerService" \/>
							    <\/div>
							<\/div>
                		<\/div>
                    <\/form>'
                	[/@compress],
	                onOk: function() {
	                	if(!$("#bindqq").valid()){
	                        return false ;
	                    }
	                	$.ajax({
                			url: "update.jhtml",
    	    				type: "POST",
    	    				async: false,
    	    				data: {"qqCustomerService":$("#qqCustomerService").val()},
    	    				dataType: "json",
    	    				success: function(data) {
    	    					if(data.code == "0"){
    	    						window.location.href="index.jhtml";
	                            }else{
	                                $.message("error", data.msg);
	                            }
    	    				}
                		});
	                },
	                onShow:function(){
	                	/*验证*/
	    				$("#bindqq").validate({
	    					rules:{
	    						qqCustomerService: {
	    							required: true,
	    			                pattern: /^[1-9][0-9]{4,14}$/
	    						}
	    					},
	    					messages:{
	    						qqCustomerService:{
	    							required:"qq号不能为空",
	    							pattern:"格式错误"
	    						}
	    					}
	    					
	    				});
	                	
	                },
	            });   
		    });
		    
		    $(".unbind_B").click(function(){
				var $this = $(this);
				$.dialog({
					type: "warn",
					width:400,
					height:250,
					content: "确定要解除绑定么？",
					onOk: function() {
						$("#unbundledForm").submit();
					}
				});
				return false;
			})
		    
		    
		    
		</script>

	</body>
</html>
[/#escape]