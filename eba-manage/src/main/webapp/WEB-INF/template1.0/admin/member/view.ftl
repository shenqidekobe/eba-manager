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
<title>${message("admin.admin.edit")} - Powered By DreamForYou</title>
</head>
<body>
	<div class="child_page"><!--内容外面的大框-->	
		<div class="cus_nav">
			<ul>
				<li><a id="goHome"	href="../homePage/index.jhtml">${message("admin.breadcrumb.home")}</a></li>
        		<li><a href="list.jhtml">用户列表</a></li>
				<li>用户详情</li>
			</ul>
		</div>
		<div class="form_box">
			<form id="inputForm" action="update.jhtml" method="post" class="form form-horizontal">
				<input type="hidden" name="id" value="${member.id}" />
				<div id="tab-system" class="HuiTab">
				    <h3 class="form_title" style="margin:20px 0 0 20px;">个人基础数据</h3>
					<div >
						<div class="pag_div">
							<div class="row cl">
								<label class="form-label col-xs-2 col-sm-2">
									昵称
								</label>
								<div class="formControls col-xs-4 col-sm-4">
									<span class="input_no_span">${member.nickName}</span>
								</div>
							
								<label class="form-label col-xs-2 col-sm-2">
									openID
								</label>
								<div class="formControls col-xs-4 col-sm-4">
									<span class="input_no_span">${member.smOpenId}</span>
								</div>
							</div>
							<div class="row cl">
								<label class="form-label col-xs-2 col-sm-2">
									联系电话
								</label>
								<div class="formControls col-xs-4 col-sm-4">
								    <span class="input_no_span">${member.phone}</span>
								</div>
								<label class="form-label col-xs-2 col-sm-2">
								           他的订单
								</label>
								<div class="formControls col-xs-4 col-sm-4">
								    <a class="input_no_span" href="/admin/order/list.jhtml?smOpenId=${member.smOpenId}&sr=member" target="_blank">点击查看</a>
								</div>
							</div>
                            <!--
                            <div class="row cl">
                                <label class="form-label col-xs-2 col-sm-2">
                                                                                                                                  头像
                                </label>
                                <div class="formControls col-xs-8 col-sm-8">
                                   <span class="input_no_span"><img src="${member.headImgUrl}" width="250px" height="150px"></span>
                                </div>
                            </div>-->
                            [#if member.parent]
                            <div class="row cl">
								<label class="form-label col-xs-2 col-sm-2">
									上级昵称
								</label>
								<div class="formControls col-xs-4 col-sm-4">
									<span class="input_no_span">${member.parent.nickName}</span>
								</div>
							
								<label class="form-label col-xs-2 col-sm-2">
									上级openID
								</label>
								<div class="formControls col-xs-4 col-sm-4">
									<span class="input_no_span">${member.parent.smOpenId}</span>
								</div>
							</div>
								[#if member.parent.parent]
	                            <div class="row cl">
									<label class="form-label col-xs-2 col-sm-2">
										上上级昵称
									</label>
									<div class="formControls col-xs-4 col-sm-4">
										<span class="input_no_span">${member.parent.parent.nickName}</span>
									</div>
								
									<label class="form-label col-xs-2 col-sm-2">
										上上级openID
									</label>
									<div class="formControls col-xs-4 col-sm-4">
										<span class="input_no_span">${member.parent.parent.smOpenId}</span>
									</div>
								</div>
	                            [/#if]
                            [/#if]
							<div class="row cl">
								<label class="form-label col-xs-2 col-sm-2">
									是否会员
								</label>
								<div class="formControls col-xs-4 col-sm-4">
								    <span class="input_no_span">[#if member.isShoper]是[#else]否[/#if]</span>
								</div>
								<label class="form-label col-xs-2 col-sm-2">
									会员等级
								</label>
								<div class="formControls col-xs-4 col-sm-4">
								    <span class="input_no_span">${member.rank.label}</span>
								</div>
							</div>
							[#if member.isShoper]
							<div class="row cl">
								<label class="form-label col-xs-2 col-sm-2">
									账户余额
								</label>
								<div class="formControls col-xs-4 col-sm-4">
									<span class="input_no_span">${member.balance}</span>
								</div>
								
								<label class="form-label col-xs-2 col-sm-2">
									累计收益
								</label>
								<div class="formControls col-xs-4 col-sm-4">
								<span class="input_no_span">${member.income}</span>
								</div>
							</div>
							[/#if]
							<div class="row cl">
								<label class="form-label col-xs-2 col-sm-2">
									自购总订单量
								</label>
								<div class="formControls col-xs-4 col-sm-4">
									<span class="input_no_span">${member.buyNum}</span>
								</div>
								
								<label class="form-label col-xs-2 col-sm-2">
									自购总订单金额
								</label>
								<div class="formControls col-xs-4 col-sm-4">
								<span class="input_no_span">${member.buyAmount}</span>
								</div>
							</div>
							<div class="row cl">
								<label class="form-label col-xs-2 col-sm-2">
									下级总订单量
								</label>
								<div class="formControls col-xs-4 col-sm-4">
									<span class="input_no_span">${member.subBuyNum}</span>
								</div>
								
								<label class="form-label col-xs-2 col-sm-2">
									下级总订单金额
								</label>
								<div class="formControls col-xs-4 col-sm-4">
								<span class="input_no_span">${member.subBuyAmount}</span>
								</div>
							</div>
							<div class="row cl">
								<label class="form-label col-xs-2 col-sm-2">
									下下级总订单量
								</label>
								<div class="formControls col-xs-4 col-sm-4">
									<span class="input_no_span">${member.subSubBuyNum}</span>
								</div>
								
								<label class="form-label col-xs-2 col-sm-2">
									下下级总订单金额
								</label>
								<div class="formControls col-xs-4 col-sm-4">
								<span class="input_no_span">${member.subSubBuyAmount}</span>
								</div>
							</div>
							<div class="row cl">
								<label class="form-label col-xs-2 col-sm-2">
									累计总订单量
								</label>
								<div class="formControls col-xs-4 col-sm-4">
									<span class="input_no_span">${member.totalBuyNum}</span>
								</div>
								
								<label class="form-label col-xs-2 col-sm-2">
									累计总订单金额
								</label>
								<div class="formControls col-xs-4 col-sm-4">
								<span class="input_no_span">${member.totalBuyAmount}</span>
								</div>
							</div>
							[#if member.platinaTime]
							<div class="row cl">
								<label class="form-label col-xs-2 col-sm-2">
									成为白金时间
								</label>
								<div class="formControls col-xs-4 col-sm-4">
									<span class="input_no_span">${member.platinaTime?string("yyyy-MM-dd HH:mm:ss")}</span>
								</div>
							</div>
							[/#if]
							[#if member.platinumTime]	
							<div class="row cl">
								<label class="form-label col-xs-2 col-sm-2">
									成为铂金时间
								</label>
								<div class="formControls col-xs-4 col-sm-4">
								<span class="input_no_span">${member.platinumTime?string("yyyy-MM-dd HH:mm:ss")}</span>
								</div>
							</div>
							[/#if]
							[#if member.blackplatinumTime]
							<div class="row cl">
								<label class="form-label col-xs-2 col-sm-2">
									成为白金时间
								</label>
								<div class="formControls col-xs-4 col-sm-4">
									<span class="input_no_span">${member.blackplatinumTime?string("yyyy-MM-dd HH:mm:ss")}</span>
								</div>
							</div>
							[/#if]
							<div class="row cl">
							    <label class="form-label col-xs-2 col-sm-2">
									注册时间
								</label>
								<div class="formControls col-xs-4 col-sm-4">
								<span class="input_no_span">${member.createDate?string("yyyy-MM-dd HH:mm:ss")}</span>
								</div>
						   </div>
						
					</div>
				</div>
				<div class="form_spec">
                <h3 class="form_title" style="margin:20px 0 0 20px;">地址数据列表</h3>
                    <div  class="spec_form" style="margin-bottom:10px;">
                	<div class="table_box">
	                    <table id="productTable" class="table table-border table-hover table_width">
	                        <thead>
								<tr class="text-l">
									<th width="16%"><div class="th_div">收货人</div></th>
									<th width="16%"><div class="th_div">手机号</div></th>
									<th width="16%"><div class="th_div">区域</div></th>
									<th width="16%"><div class="th_div">地址</div></th>
								</tr>
							</thead>
							<tbody>
							    [#list addresss.content as ads]
								<tr class="text-l">
									<td>${ads.consignee}</td>
									<td>${ads.phone}</td>
									<td>${ads.areaName}</td>
									<td>${ads.address}</td>
								</tr>
								[/#list]
							</tbody>
						</table>
					</div>
				</div>
				<div class="footer_submit">
					<input class="btn radius cancel_B" type="button" value="返回" onclick="history.back(); return false;"/>
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