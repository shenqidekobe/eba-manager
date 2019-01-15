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
        		<li><a href="list.jhtml">会员列表</a></li>
				<li>编辑会员</li>
			</ul>
		</div>
		<div class="form_box">
			<form id="inputForm" action="update.jhtml" method="post" class="form form-horizontal">
				<input type="hidden" name="id" value="${member.id}" />
				<input type="hidden" name="phone" value="${member.phone}" />
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
								    <input type="text" class="input-text radius" placeholder="" name="phone" value="${member.phone}" />
								</div>
								<label class="form-label col-xs-2 col-sm-2">
								           他的订单
								</label>
								<div class="formControls col-xs-4 col-sm-4">
								    <a class="input_no_span" href="/admin/order/list.jhtml?smOpenId=${member.smOpenId}&sr=member" target="_blank">点击查看</a>
								</div>
							</div>
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
								    <input type="text" class="input-text radius down_list" readonly placeholder="请选择" />
                                    <input type="text" class="downList_val" name="isShoper" value="${obj.isShoper}"/>
                                    <ul class="downList_con">
                                        <li val="0" [#if !obj.isShoper] class="li_bag"[/#if]>是</li>
                                        <li val="1" [#if obj.isShoper] class="li_bag"[/#if]>否</li>
                                    </ul>
								</div>
								<label class="form-label col-xs-2 col-sm-2">
								           会员等级
								</label>
								<div class="formControls col-xs-4 col-sm-4">
								    <input type="text" class="input-text radius down_list" readonly placeholder="请选择" />
                                    <input type="text" name="rank" class="downList_val" />
                                    <ul class="downList_con">
                                        [#list ranks as r]
                                            <li val="${r}" [#if r == member.rank]class="li_bag"[/#if]>${r.label}</li>
                                        [/#list]

                                    </ul>
								</div>
							</div>
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
							<div class="row cl">
								<label class="form-label col-xs-2 col-sm-2">
									自购总订单量
								</label>
								<div class="formControls col-xs-4 col-sm-4">
								    <input type="text" class="input-text radius" placeholder="自己购买订单量" name="buyNum" value="${member.buyNum}" />
								</div>
								
								<label class="form-label col-xs-2 col-sm-2">
									自购总订单金额
								</label>
								<div class="formControls col-xs-4 col-sm-4">
								    <input type="text" class="input-text radius" placeholder="自己购买订单总金额" name="buyAmount" value="${member.buyAmount}" />
								</div>
							</div>
							<div class="row cl">
								<label class="form-label col-xs-2 col-sm-2">
									下级总订单量
								</label>
								<div class="formControls col-xs-4 col-sm-4">
								    <input type="text" class="input-text radius" placeholder="下级购买订单量" name="subBuyNum" value="${member.subBuyNum}" />
								</div>
								
								<label class="form-label col-xs-2 col-sm-2">
									下级总订单金额
								</label>
								<div class="formControls col-xs-4 col-sm-4">
								     <input type="text" class="input-text radius" placeholder="下级购买订单总金额累计" name="subBuyAmount" value="${member.subBuyAmount}" />
								</div>
							</div>
							<div class="row cl">
								<label class="form-label col-xs-2 col-sm-2">
									下下级总订单量
								</label>
								<div class="formControls col-xs-4 col-sm-4">
								    <input type="text" class="input-text radius" placeholder="下下级购买订单量" name="subSubBuyNum" value="${member.subSubBuyNum}" />
								</div>
								
								<label class="form-label col-xs-2 col-sm-2">
									下下级总订单金额
								</label>
								<div class="formControls col-xs-4 col-sm-4">
								    <input type="text" class="input-text radius" placeholder="下下级购买订单总金额累计" name="subSubBuyAmount" value="${member.subSubBuyAmount}" />
								</div>
							</div>
							<div class="row cl">
								<label class="form-label col-xs-2 col-sm-2">
									累计总订单量
								</label>
								<div class="formControls col-xs-4 col-sm-4">
								    <input type="text" class="input-text radius" placeholder="累计销售订单量(自购-下级购-下下级购)" name="totalBuyNum" value="${member.totalBuyNum}" />
								</div>
								
								<label class="form-label col-xs-2 col-sm-2">
									累计总订单金额
								</label>
								<div class="formControls col-xs-4 col-sm-4">
								   <input type="text" class="input-text radius" placeholder="购买订单总金额累计(自购-下级购-下下级购)" name="totalBuyAmount" value="${member.totalBuyAmount}" />
								</div>
							</div>
							<div class="row cl">
                                <label class="form-label col-xs-4 col-sm-3">
                                	备注说明
                                </label>
                                <div class="formControls col-xs-8 col-sm-7">
                                    <textarea id="remark" name="remark" rows="5" class="text_area" cols="90" placeholder="会员个人信息备注">${obj.remark}</textarea>
                                </div>
                            </div>
						</div>
						
					</div>
				</div>
				<div class="footer_submit">
				    <input class="btn radius confir_S" type="submit" value="${message("admin.common.submit")}"/>
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