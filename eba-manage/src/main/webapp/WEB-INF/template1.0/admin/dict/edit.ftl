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
		.pag_div{width:45%;float:left;}
		.tabBar{background:#f9f9f9;border:0;}
		.tabBar span{padding:5px 24px;background:#f9f9f9;color:#c3cfe5;}
		.tabBar span.current{background:#fff;color:#333;}
		.beizhu{height:80px;}
		.check-box{width:150px;height:28px;overflow: hidden;}
		#tab-system{position:relative;}
		.opera_butt{position:absolute;top:0;right:0; margin-top:5px;}
		.col-sm-7{width:80%;}
		.tabCon,#tab-system{overflow: auto;}
	</style>
<title>编辑数据配置</title>
</head>
<body>
	<div class="child_page"><!--内容外面的大框-->	
		<div class="cus_nav">
			<ul>
				<li><a id="goHome"	href="../homePage/index.jhtml">${message("admin.breadcrumb.home")}</a></li>
				<li>数据配置</li>
			</ul>
		</div>
		<div class="form_box">
			<form id="inputForm" action="update.jhtml" method="post" class="form form-horizontal">
				<input type="hidden" name="id" value="${id}" />
				<div id="tab-system" class="HuiTab">
					<div class="">
						<div class="pag_div">
							<div class="row cl">
								<label class="form-label col-xs-4 col-sm-4">
									几级分销
								</label>
								<div class="formControls col-xs-8 col-sm-8">
								    <div class="input_box">
									   <input type="number" min="0" max="2" class="input-text radius" placeholder="2" name="levelDist" value="${obj.levelDist}" maxlength="10"/>
								       <div class="box_right" style="text-align:right;">级</div>
								    </div>
								</div>
							</div>
							<hr/><br/>
							<div class="row cl">
								<label class="form-label col-xs-4 col-sm-4">
									满N金额成为铂金
								</label>
								<div class="formControls col-xs-8 col-sm-8">
								    <div class="input_box">
										<input type="text" class="input-text radius" name="platinum_to" value="${obj.platinum_to}" maxlength="10"/>
									    <div class="box_right" style="text-align:right;">元</div>
								    </div>
								</div>
							</div>
							<div class="row cl">
								<label class="form-label col-xs-4 col-sm-4">
									铂金一级提成
								</label>
								<div class="formControls col-xs-8 col-sm-8">
									<input type="text" class="input-text radius" name="platinum_rate1" value="${obj.platinum_rate1}" maxlength="10"/>
								</div>
							</div>
							<div class="row cl">
								<label class="form-label col-xs-4 col-sm-4">
									铂金二级提成
								</label>
								<div class="formControls col-xs-8 col-sm-8">
									<input type="text" class="input-text radius" name="platinum_rate2" value="${obj.platinum_rate2}" maxlength="10"/>
								</div>
							</div>
							<div class="row cl">
								<label class="form-label col-xs-4 col-sm-4">
									铂金自购单单满N
								</label>
								<div class="formControls col-xs-8 col-sm-8">
								    <div class="input_box">
										<input type="text" class="input-text radius" name="platinum_buy_amount" value="${obj.platinum_buy_amount}" maxlength="10"/>
									    <div class="box_right" style="text-align:right;">元</div>
								    </div>
								</div>
							</div>
							<div class="row cl">
								<label class="form-label col-xs-4 col-sm-4">
									铂金自购提成
								</label>
								<div class="formControls col-xs-8 col-sm-8">
									<input type="text" class="input-text radius" name="platinum_buy_rate" value="${obj.platinum_buy_rate}" maxlength="10"/>
								</div>
							</div>
							<div class="row cl">
								<label class="form-label col-xs-4 col-sm-4">
									自购上上级返佣
								</label>
								<div class="formControls col-xs-8 col-sm-8">
								    <input type="text" class="input-text radius down_list" readonly placeholder="请选择" />
                                    <input type="text" class="downList_val" name="buySSRakeBack" value="${obj.buySSRakeBack}"/>
                                    <ul class="downList_con">
                                        <li val="1" [#if obj.buySSRakeBack]class="li_bag"[/#if]>返佣</li>
                                        <li val="0" [#if !obj.buySSRakeBack]class="li_bag"[/#if]>不返</li>
                                    </ul>
								</div>
							</div>
							</div>
							
							<div class="pag_div">
							<div class="row cl">
								<label class="form-label col-xs-4 col-sm-4">
									间隔几天发红包
								</label>
								<div class="formControls col-xs-4 col-sm-4">
								    <div class="input_box">
										<input type="number" class="input-text radius" name="intervalDayCommision" placeholder="15" value="${obj.intervalDayCommision}" maxlength="10"/>
									    <div class="box_right" style="text-align:right;">天</div>
								    </div>
								</div>
							</div>
							<hr/><br/>
							<div class="row cl">
								<label class="form-label col-xs-4 col-sm-4">
									满N金额成为黑金
								</label>
								<div class="formControls col-xs-8 col-sm-8">
								    <div class="input_box">
										<input type="text" class="input-text radius" name="blackplatinum_to" value="${obj.blackplatinum_to}" maxlength="10"/>
									    <div class="box_right" style="text-align:right;">元</div>
								    </div>
								</div>
							</div>
							<div class="row cl">
								<label class="form-label col-xs-4 col-sm-4">
									黑金一级提成
								</label>
								<div class="formControls col-xs-8 col-sm-8">
									<input type="text" class="input-text radius" name="blackplatinum_rate1" value="${obj.blackplatinum_rate1}" maxlength="10"/>
								</div>
							</div>
							<div class="row cl">
								<label class="form-label col-xs-4 col-sm-4">
									黑金二级提成
								</label>
								<div class="formControls col-xs-8 col-sm-8">
									<input type="text" class="input-text radius" name="blackplatinum_rate2" value="${obj.blackplatinum_rate2}" maxlength="10"/>
								</div>
							</div>
							<div class="row cl">
								<label class="form-label col-xs-4 col-sm-4">
									黑金自购单单满N
								</label>
								<div class="formControls col-xs-8 col-sm-8">
								    <div class="input_box">
										<input type="text" class="input-text radius" name="blackplatinum_buy_amount" value="${obj.blackplatinum_buy_amount}" maxlength="10"/>
									    <div class="box_right" style="text-align:right;">元</div>
								    </div>
								</div>
							</div>
							<div class="row cl">
								<label class="form-label col-xs-4 col-sm-4">
									黑金自购提成
								</label>
								<div class="formControls col-xs-8 col-sm-8">
									<input type="text" class="input-text radius" name="blackplatinum_buy_rate" value="${obj.blackplatinum_buy_rate}" maxlength="10"/>
								</div>
							</div>
							<div class="row cl">
								<label class="form-label col-xs-4 col-sm-4">
									黑金不满自购
								</label>
								<div class="formControls col-xs-8 col-sm-8">
								    <input type="text" class="input-text radius down_list" readonly placeholder="请选择" />
                                    <input type="text" class="downList_val" name="blackplatinumBuyNoAs" value="${obj.blackplatinumBuyNoAs}"/>
                                    <ul class="downList_con">
                                        <li val="1" [#if obj.blackplatinumBuyNoAs]class="li_bag"[/#if]>达标铂金自购则按铂金返佣</li>
                                        <li val="0" [#if !obj.blackplatinumBuyNoAs]class="li_bag"[/#if]>不返</li>
                                    </ul>
								</div>
							</div>
							</div>
						</div>
						<div class="pag_div1">
							<div class="row cl">
							    <label class="form-label2 col-xs-12 col-sm-12">
									<span style="color:red;">温馨提示：</span>
									<span style="color:red;">1、【间隔几天发红包】 表示当订单完成后间隔N天给会员提成红包</span></br>
									<span style="color:red;margin-left:74px;">2、【满N金额成为铂金】表示自购-下级购-下下级购买的订单累计金额满N金额才成为铂金会员</span></br>
									<span style="color:red;margin-left:74px;">3、【自购单单满N】表示自购的单次订单满N金额才返佣</span></br>
									<span style="color:red;margin-left:74px;">4、【自购上上级返佣】表示自购达标得到返佣后，下单人的上上级是否还享受返佣</span></br>
									<span style="color:red;margin-left:74px;">5、【黑金不满自购】表示黑金自购订单不满自购金额标准但满足了铂金自购标准，是否返佣</span></br>
									<span style="color:red;margin-left:74px;">6、    各项配置谨慎修改，请计算好比例再进行更新保存操作！</span></br>
								</label>
							</div>
						</div>
					</div>
				</div>
				
				<div class="footer_submit">
					<input class="btn radius confir_S" type="submit" value="更新保存"/>
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
		$(".tabCon").css("height",formHeight - 40);
		
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