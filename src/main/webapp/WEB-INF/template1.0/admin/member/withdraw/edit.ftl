[#escape x as x?html]
<!DOCTYPE HTML>
<html>
<head>
<meta charset="utf-8">
<meta name="renderer" content="webkit|ie-comp|ie-stand">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
<meta http-equiv="Cache-Control" content="no-siteapp" />

<link rel="stylesheet" type="text/css" href="${base}/resources/admin1.0/H-ui/static/h-ui/css/H-ui.min.css" />
<link rel="stylesheet" type="text/css" href="${base}/resources/admin1.0/H-ui/static/h-ui.admin/css/H-ui.admin.css" />
<link rel="stylesheet" href="${base}/resources/admin1.0/css/public.css" />
<link rel="stylesheet" href="${base}/resources/admin1.0/css/admin.css" />

	<style>
		body{background:#f9f9f9;}
		.pag_div{width:96%;float:left;}
		.tabBar{background:#f9f9f9;border:0;}
		.tabBar span{padding:5px 24px;background:#f9f9f9;color:#c3cfe5;}
		.tabBar span.current{background:#fff;color:#333;}
		.beizhu{height:80px;}
		.check-box{width:150px;height:28px;overflow: hidden;}
		#tab-system{position:relative;}
		.opera_butt{position:absolute;top:0;right:0; margin-top:5px;}
		.col-sm-7{width:80%;}
	</style>
<title>编辑提现</title>
</head>
<body>
	<div class="child_page"><!--内容外面的大框-->	
		<div class="cus_nav">
			<ul>
				<li><a id="goHome"	href="../homePage/index.jhtml">${message("admin.breadcrumb.home")}</a></li>
        		<li><a href="list.jhtml">提现列表</a></li>
				<li>编辑提现</li>
			</ul>
		</div>
		<div class="form_box">
			<form id="inputForm" action="update.jhtml" method="post" class="form form-horizontal">
				<input type="hidden" name="id" value="${obj.id}" />
				<input type="hidden" name="way" value="${obj.way}" />
				<div id="tab-system" class="HuiTab">
					<div >
						<div class="pag_div">
						    <div class="row cl">
						        <label class="form-label col-xs-2 col-sm-2">
									提现人昵称
								</label>
								<div class="formControls col-xs-4 col-sm-4">
									<span class="input_no_span">${obj.member.nickName}</span>
								</div>
								
								<label class="form-label col-xs-2 col-sm-2">
									提现人ID
								</label>
								<div class="formControls col-xs-4 col-sm-4">
									<span class="input_no_span">${obj.member.smOpenId}</span>
								</div>
                            </div>
							<div class="row cl">
							    <label class="form-label col-xs-2 col-sm-2">
                                                                                                                       提现方式
                                </label>
                                <div class="formControls col-xs-4 col-sm-4">
                                   <span class="input_no_span">${obj.wayName}</span>
                                </div>
                                
								<label class="form-label col-xs-2 col-sm-2">
									联系电话
								</label>
								<div class="formControls col-xs-4 col-sm-4">
									<span class="input_no_span">${obj.phone}</span>
								</div>
							</div>
                            <div class="row cl">
                                <label class="form-label col-xs-2 col-sm-2">
                                                                                                                       提现账户
                                </label>
                                <div class="formControls col-xs-4 col-sm-4">
                                   <span class="input_no_span">${obj.account}</span>
                                </div>
								<label class="form-label col-xs-2 col-sm-2">
									账户名称
								</label>
								<div class="formControls col-xs-4 col-sm-4">
								    <span class="input_no_span">${obj.accountName}</span>
								    
								</div>
							</div>
							<div class="row cl">
								<label class="form-label col-xs-2 col-sm-2">
									提现金额
								</label>
								<div class="formControls col-xs-4 col-sm-4">
									<span class="input_no_span">${obj.amount}</span>
								</div>
								
								<label class="form-label col-xs-2 col-sm-2">
									手续费
								</label>
								<div class="formControls col-xs-4 col-sm-4">
									<input type="text" class="input-text radius" name="fee" value="${obj.fee}" placeholder="手续费" maxlength="200"/>
								</div>
							</div>
							<div class="row cl">
								<label class="form-label col-xs-2 col-sm-2">
									申请时间
								</label>
								<div class="formControls col-xs-4 col-sm-4">
									<span class="input_no_span">${obj.createDate?string("yyyy-MM-dd HH:mm:ss")}</span>
								</div>
                                <label class="form-label col-xs-2 col-sm-2">
								             提现状态
                                </label>
                                <div class="formControls col-xs-4 col-sm-4">
                                    <input type="text" class="input-text radius down_list" readonly placeholder="请选择" />
                                    <input type="text" class="downList_val" name="status" value="${obj.status}"/>
                                    <ul class="downList_con">
										[#list statusList as st]
                                            <li val="${st}" [#if st == obj.status] class="li_bag"[/#if]>${st.label}</li>
										[/#list]
                                    </ul>
                                </div>
                            </div>
                            <div class="row cl">
								<label class="form-label col-xs-2 col-sm-2">
									打款凭证号
								</label>
								<div class="formControls col-xs-4 col-sm-4">
									<input type="text" class="input-text radius" name="voucherNum" value="${obj.voucherNum}" placeholder="打款凭证流水号" maxlength="200"/>
								</div>
                            </div>
                            <div class="row cl">
                                <label class="form-label col-xs-4 col-sm-3">
                                	备注说明
                                </label>
                                <div class="formControls col-xs-8 col-sm-7">
                                    <textarea id="remark" name="remark" rows="5" class="text_area" cols="90" placeholder="如审核请输入审核原因">${obj.remark}</textarea>
                                </div>
                            </div>
						</div>
						
					</div>
				</div>
				
				<div class="footer_submit">
				    [#if obj.status!='complete']
					   <input class="btn radius confir_S" type="submit" value="${message("admin.common.submit")}"/>
					[/#if]
					<input class="btn radius cancel_B" type="button" value="${message("admin.common.back")}" onclick="history.back(); return false;"/>
				</div>
			</form>
		</div>
	</div>

<script src="${base}/resources/admin1.0/js/jquery.min.js"></script>
<script type="text/javascript" src="${base}/resources/admin1.0/H-ui/lib/layer/2.4/layer.js"></script>
<script type="text/javascript" src="${base}/resources/admin1.0/H-ui/static/h-ui/js/H-ui.min.js"></script>
<script type="text/javascript" src="${base}/resources/admin1.0/H-ui/static/h-ui.admin/js/H-ui.admin.js"></script>
<script type="text/javascript" src="${base}/resources/admin1.0/js/validate/jquery.validate.min.js"></script>
<script type="text/javascript" src="${base}/resources/admin1.0/js/validate/messages_zh.min.js"></script>
<script type="text/javascript" src="${base}/resources/admin1.0/js/common.js"></script>
<script type="text/javascript">
	$(function(){
		
		var $inputForm = $("#inputForm");
		
		[@flash_message /]
		
		/*通过js获取页面高度，来定义表单的高度*/
		var formHeight=$(document.body).height()-100;
		$(".form_box").css("height",formHeight);
		
		/**/
		var heightObj = $(document.body).height() - 185;
		$(".list_t_tbody").css("height",heightObj);
		
		
        //下拉列表
        $(".down_list").click(function(){
            $(this).siblings(".downList_con").toggle();
        });

        $("*").click(function (event) {
            if (!$(this).hasClass("down_list")&&!$(this).hasClass("downList_con")){
                $(".downList_con").hide();
            }
            event.stopPropagation();
        });

        $(".downList_con").each(function(){

            var $selectDom = $(this).find("li.li_bag") ;

            var firstText = $selectDom.text();
            var firstVal = $selectDom.attr("val");

            $(this).siblings(".down_list").val(firstText);
            $(this).siblings(".downList_val").val(firstVal);
        });

        $(".downList_con li").click(function(){
            $(this).parent().siblings(".down_list").attr("value",$(this).text());
            $(this).parent().siblings(".downList_val").val($(this).attr("val"));
            $(this).addClass("li_bag").siblings().removeClass("li_bag");
        });

		// 表单验证
		$inputForm.validate({
			rules: {
			},
            messages: {
            }
		});
		
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