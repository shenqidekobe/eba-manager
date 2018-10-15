[#escape x as x?html]
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>${message("admin.admin.edit")} - Powered By DreamForYou</title>
		<meta http-equiv="X-UA-Compatible" content="IE=edge" />
		<link rel="stylesheet" type="text/css" href="${base}/resources/admin1.0/H-ui/static/h-ui/css/H-ui.min.css" />
		<link rel="stylesheet" type="text/css" href="${base}/resources/admin1.0/H-ui/static/h-ui.admin/css/H-ui.admin.css" />
		<link rel="stylesheet" href="${base}/resources/admin1.0/js/date/dateRange.css" />
		<link rel="stylesheet" href="${base}/resources/admin1.0/css/public.css" />
		<link rel="stylesheet" href="${base}/resources/admin1.0/css/admin.css" />
		<style>
			body{background:#f9f9f9;}
			.pag_div{width:95%; float:left;}
			.pag_div .row{width:45%;}
			.col-sm-7{width:72%;}
			.require_search,.ch_search,.update_B{border:1px solid #f0f0f0;}
			.table-border th{border-top:1px solid #eaeefb;}
			#iframeList{overflow: hidden;}
			.form_box{overflow: auto;}
			.ch_time .chooseTime{width:280px;}
			.ch_time .ta_date .date_title{width:280px;}
			.ch_time .ta_date{width:320px;}

            .col_div{padding-top:8px;font-size:14px;color:#333;}
            .col_div span{margin-right:15px;}
            #noticeDay{width:50px;height:25px;padding-left:8px}
            .col_div i{color:#999;margin-left:30px;font-size:12px}
			.form_baseInfo::after{
				content: '';
				display: block;
				width:100%;
				height:0;
				clear: both;
			}
		</style>
		<script src="${base}/resources/admin1.0/js/jquery.min.js"></script>
		<script src="${base}/resources/admin1.0/js/date/dateRange.js"></script><!--时间控件-->
		<script type="text/javascript" src="${base}/resources/admin1.0/H-ui/lib/layer/2.4/layer.js"></script>
		<script type="text/javascript" src="${base}/resources/admin1.0/H-ui/static/h-ui/js/H-ui.min.js"></script>
		<script type="text/javascript" src="${base}/resources/admin1.0/H-ui/static/h-ui.admin/js/H-ui.admin.js"></script>
		<script type="text/javascript" src="${base}/resources/admin1.0/H-ui/lib/laypage/1.2/laypage.js"></script>
		<script type="text/javascript" src="${base}/resources/admin1.0/js/validate/jquery.validate.min.js"></script>
		<script type="text/javascript" src="${base}/resources/admin1.0/js/common.js"></script>
		<script type="text/javascript">
			var needProducts = {};
			var needProductsLen = 0;
			
			var productList =[#noescape]'${result?json_string}'[/#noescape];
			var ids=JSON.parse(productList);
			$.each(ids, function (i, vue) {
				addNeedProduct(i,vue);
   			});
			
			//子页面调用 增加关联商品
		    /**
			 *
		     * @param obj
		     */
			function addNeedProduct(goodId , products){
		        needProducts[goodId] = products;
		        needProductsLen++ ;
			}
			//删除关联商品
			function delNeedProduct(goodId){
				delete needProducts[goodId];
		        needProductsLen--;
			}
	
		    function getCacheFromChild(){
		        return needProducts ;
		    }
	
	        function updatePrice(productId , updPrice){
	            needProducts[productId][productId].supplyPrice = updPrice ;
	        }
	        function updateOrderQuantity(productId , orderQuantity){
	            needProducts[productId][productId].minOrderQuantity = orderQuantity ;
	        }
		
		
			$(function(){
				var $inputForm = $("#inputForm");

				[@flash_message /]

				/*通过js获取页面高度，来定义表单的高度*/
				var formHeight=$(document.body).height()-100;
				$(".form_box").css("height",formHeight);

				/*下拉框的效果*/
				$(".down_list").click(function(){
		    		$(this).siblings(".downList_con").toggle();
		    	})

		        $("*").click(function (event) {
		            if (!$(this).hasClass("down_list")&&!$(this).hasClass("downList_con")){
		                $(".downList_con").hide();
		            }
		            event.stopPropagation();
		        });
				$(".downList_con li").click(function(){
					$(this).parent().siblings(".down_list").val($(this).html());
					$(this).parent().siblings(".downList_val").val($(this).attr("val"));
					$(this).addClass("li_bag").siblings().removeClass("li_bag");
				});

				/*当input获得焦点时，外面的边框显示蓝色*/
				$(".focus_border").focus(function(){
					$(this).parent().addClass("add_border");
				})
				$(".focus_border").blur(function(){
					$(this).parent().removeClass("add_border");
				})

				/*搜索条件*/
	        	$(".require_search li").on("click",function(){
	        		$(this).parent().siblings(".search").html($(this).html());
	        		$(this).addClass("li_bag").siblings().removeClass("li_bag");
	        		$(".check").css("display","none");
	        	});
	        	$(".require_search").mouseover(function(){
					$(this).find("ul").css("display","block");
				});
				$(".require_search").mouseout(function(){
					$(this).find("ul").css("display","none");
				});

		        /* $("#submitButton").click(function(){
		            if(!$inputForm.valid()){
		                return false ;
		            }
		            var parsms = $inputForm.serializeArray() ;
		            $.ajax({
		                type: "GET",
		                url: "validDate.jhtml",
		                async: false,
		                data: parsms,
		                dataType: "json",
		                success: function (data) {
		                    if(!data.isTrue){
		                        isTrue=false;
		                        $.message({'type':'error' , 'content':'选择的企业该时间段已有供应关系！'});
		                    }else{
		                        $inputForm.submit();
							}
		                }
		            });
				}); */
		        
		        
		        
		        $("#submitButton").click(function(){
					var flag = true;
					 
			        if(needProductsLen == 0){
						$.message({'type':'error' , 'content':'请选择商品'});
						return false ;
					}
					
					var id=$("#id").val();
					var noticeDay = $("#noticeDay").val();
	                var openNotice = $("input[name='openNotice']:checked").val();
					
					var isDistributionModel = $("input[name=assignedModel]:checked").val();
					var isTrue=true;
										
			        var saveProducts = new Array();
			        var tempSaveJson = {};
					var i = 0;
			        $.each(needProducts,function(key,value) {
			            $.each(value,function(key1,value1) {
			            	var id = value1['products'];
			            	$("iframe").contents().find("#min_"+id).html("");
			            	var min = Number(value1["min"]);
			            	var minOrderQuantity = Number(value1["minOrderQuantity"]);
			            	
			            	if(minOrderQuantity <  min){
			            		$("iframe").contents().find("#min_"+id).html("不允许小于"+min);
			            		flag = false;
			            	} 
			                tempSaveJson['needProductList['+i+'].products.id'] = value1['products'] ;
			                tempSaveJson['needProductList['+i+'].supplyPrice'] = value1["supplyPrice"] ;
	                        tempSaveJson['needProductList['+i+'].minOrderQuantity'] = value1["minOrderQuantity"] ;
							i++ ;
			            });
			        });

			        if(!flag){
			        	return false;
			        }

			        $(this).addClass("in_no_click");
			        $(this).attr("disabled", true);
			        
			        tempSaveJson['id']=id;
	                tempSaveJson['openNotice']=openNotice;
	                tempSaveJson['noticeDay']=noticeDay;
	                tempSaveJson['assignedModel']=isDistributionModel;

			        $.ajax({
			            type: "POST",
			            url: "update.jhtml",
			            data: tempSaveJson,
			            dataType: "json",
			            //contentType:'application/json;charset=utf-8', //设置请求头信息
			            beforeSend: function () {

			            },
			            success: function (message) {
			                $.message(message);
			                if (message.type == "success") {
			                    setTimeout(function () {
			                        location.href="list.jhtml";
			                    }, 3000);
			                }
			            },
			            error: function (data) {
			                $.message(data);
			                $(this).removeClass("in_no_click");
					        $(this).attr("disabled", false);
			            }
			        });
				});
		        
		        
		        
		        

				$("#iframeList").load(function () {
					var mainheight = $(this).contents().find("body>div").height() + 40;
					if(mainheight<300){
						mainheight = 300;
					};
				 	$(this).height(mainheight);
				});

           });
		</script>
	</head>
	<body >
		<div class="child_page"><!--内容外面的大框-->
			<div class="cus_nav">
				<ul>
					<li><a href="">${message("admin.breadcrumb.home")}</a></li>
					<li><a href="list.jhtml">门店供应列表 </a></li>
					<li>修改门店供应</li>
				</ul>
			</div>
			<div class="form_box">
				<form id="inputForm" action="update.jhtml" method="post" class="form form-horizontal">

					<div style="width:100%;height:40px;padding-top:20px;">
						<h3 class="list_title">供应模式</h3>
						<div style="height:34px;">

							<div class="radio-box">
								<input type="radio" id="" name="assignedModel" [#if supplyNeed.assignedModel == "STRAIGHT"] checked="checked"[/#if] value="STRAIGHT" ／>
								<label for="radio-1">直销模式</label>
							</div>
							[#if currSupplier.systemSetting.isDistributionModel]
							<div class="radio-box" >
								<input type="radio" id="" name="assignedModel" [#if supplyNeed.assignedModel == "BRANCH"] checked="checked"[/#if] value="BRANCH" />
								<label for="radio-2">分销模式</label>
							</div>
							[/#if]
						</div>
					</div>

					<input type="hidden" name="id" id="id" value="${supplyNeed.id}"/>
					<div class='form_baseInfo'>
						<h3 class="list_title">基本信息</h3>
						<div class="pag_div">
							<div class="row cl" style="float:left;">
								<label class="form-label col-xs-4 col-sm-3">门店</label>
								<div class="formControls col-xs-8 col-sm-7"">
									<span class="input_no_span" style="color:#333">${supplyNeed.need.name}</span>
								</div>
							</div>
							<div class="row cl" style="float:left; width:80%">
								<label class="form-label col-xs-4 col-sm-3">
									<img src="${base}/resources/admin1.0/images/bitian_icon.svg" alt="" />订单消息提醒</label>
								<div class="formControls col-xs-8 col-sm-7">
									<div class="col_div">
										<input type="radio" name="openNotice" value="false" checked="checked" [#if !(supplyNeed.openNotice)] checked="checked"[/#if]/><span>不开启</span>
									</div>
									<div class="col_div">
										<input type="radio" name="openNotice" value="true" [#if supplyNeed.openNotice == "true"] checked="checked"[/#if]/><span>开启</span>个体客户最后一次下单后<input name="noticeDay" value="${supplyNeed.noticeDay!5}" id="noticeDay" class="input-text radius"/> 天进行微信消息提醒 <i>系统默认为5天,当天的9:00进行提醒</i>
									</div>
								</div>
							</div>

							<div class="clear"></div>
						</div>
					</div>
					<div class="hang_list">
						<h3 class="list_title">商品信息</h3>

					</div>

					<div class="footer_submit">
						<input class="btn radius confir_S" type="button" id="submitButton" value="${message("admin.common.submit")}"/>
						<input class="btn radius cancel_B" type="button" value="${message("admin.common.back")}" onclick="history.back(); return false;"/>
					</div>

					<div class="selectList" style="width:100%;height:100%">
						<iframe src="selectList.jhtml?goodListStatus=edit" id="iframeList" name="iframeList"  frameborder="0" width="100%" height="" scrolling="no">
					</div>

				</form>
			</div>
		</div>
	</body>
</html>
[/#escape]
