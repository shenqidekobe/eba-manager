[#escape x as x?html]
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title></title>
		<meta http-equiv="X-UA-Compatible" content="IE=edge" />
		<link rel="stylesheet" type="text/css" href="${base}/resources/admin1.0/H-ui/static/h-ui/css/H-ui.min.css" />
		<link rel="stylesheet" type="text/css" href="${base}/resources/admin1.0/H-ui/static/h-ui.admin/css/H-ui.admin.css" />
		<link rel="stylesheet" href="${base}/resources/admin1.0/css/public.css" />
		<link rel="stylesheet" href="${base}/resources/admin1.0/css/admin.css" />
		<style>
			body{background:#f9f9f9;}
			.pag_div{width:95%; float:left;}
			.col-sm-7{width:70%;}
			.require_search,.ch_search,.update_B{border:1px solid #CADBF3;}
			.table-border th{border-top:1px solid #eaeefb;}
			#iframeList{height:calc(100%);overflow: hidden;}
			.form .row{width:45%;}
			.form_box{overflow: auto;}
		</style>
		<script src="${base}/resources/admin1.0/js/jquery.min.js"></script>
		<script type="text/javascript" src="${base}/resources/admin1.0/H-ui/static/h-ui/js/H-ui.min.js"></script>
		<script type="text/javascript" src="${base}/resources/admin1.0/H-ui/static/h-ui.admin/js/H-ui.admin.js"></script>

        <script src="${base}/resources/admin1.0/js/validate/jquery.validate.min.js"></script>
        <script src="${base}/resources/admin1.0/js/common.js"></script>
		<script>
            var needCache = {};
            var needProducts = {};
            var needProductsLen = 0 ;
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


            function updatePrice(goodsId , productId , updPrice){
                needProducts[goodsId][productId].supplyPrice = updPrice ;
            }

            function updateOrderQuantity(goodsId , productId , orderQuantity){
                needProducts[goodsId][productId].minOrderQuantity = orderQuantity ;
            }

			$(function(){
                var $inputForm = $("#inputForm");

                // 表单验证
                $inputForm.validate({
                    rules: {
                        supplier: {
                            required: true
                        },
                        need: {
                            required: true
                        }
                    },
                    messages: {
                        tel: {
                            pattern: "格式不正确"
                        }
                    }
                });

                $("#submitButton").click(function(){
                    if(!$inputForm.valid()){
                        return false ;
                    }

                    if(needProductsLen == 0){
                        $.message({'type':'error' , 'content':'请选择商品'});
                        return false ;
                    }
                    var saveProducts = new Array();
                    var tempSaveJson = {};
                    var i = 0;
                    $.each(needProducts,function(key,value) {
                        $.each(value,function(key1,value1) {
                            tempSaveJson['needProductList['+i+'].products.id'] = value1['products'] ;
                            tempSaveJson['needProductList['+i+'].supplyPrice'] = value1["supplyPrice"] ;
                            tempSaveJson['needProductList['+i+'].minOrderQuantity'] = value1["minOrderQuantity"] ;
                            i++ ;
                        });
                    });
                    tempSaveJson['need.id'] = $("#needId").val();
                    /*var tempSaveJson = {};
                    tempSaveJson.needProducts=saveProducts;*/
                    $.ajax({
                        type: "POST",
                        url: "save.jhtml",
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
                        }
                    });
                });

				/*通过js获取页面高度，来定义表单的高度*/
				var formHeight=$(document.body).height()-100;
				$(".form_box").css("height",formHeight);
	
				var formList = $(document.body).height() - 280;
				$(".selectList").css("height",formList);
			
				/*下拉框的效果*/
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
		        	$(this).find("li:eq(0)").addClass("li_bag");
		        	var firstText = $(this).find("li:eq(0)").text();
		        	var firstVal = $(this).find("li:eq(0)").attr("val");
		        	$(this).siblings(".down_list").val(firstText);
		        	$(this).siblings(".downList_val").val(firstVal);
		        });
	
		        $(".downList_con li").click(function(){
		            $(this).parent().siblings(".down_list").attr("value",$(this).text());
		          	$(this).parent().siblings(".downList_val").val($(this).attr("val"));
		            $(this).parent().siblings(".downList_val").change();
		            $(this).addClass("li_bag").siblings().removeClass("li_bag");
		        });
				
				
				
				/*当input获得焦点时，外面的边框显示蓝色*/
				$(".focus_border").focus(function(){
					$(this).parent().addClass("add_border");
				})
				$(".focus_border").blur(function(){
					$(this).parent().removeClass("add_border");
				});
				
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
			
				
				/*选择收获点*/

				$(".downList_con").delegate('li',"click",function(){
					var supplierId = $("#supplierId").val();
					var needId = $(this).attr("val");
                    $("#tel").val("");
                    $("#userName").val("");

					if("" == needId){
						return false ;
					}


					$("#tel").val(needCache[supplierId][needId].tel);
					$("#userName").val(needCache[supplierId][needId].userName);
					
		            $("#need").attr("value",$(this).text());
		          	$("#needId").val($(this).attr("val"));
		            $(this).addClass("li_bag").siblings().removeClass("li_bag");
		            $(this).parent().css("display","none")
				});

                var firstDom = $(".need").find("li:eq(0)");
                var firstText = firstDom.text();
                var firstVal = firstDom.attr("val");

				$(".supplierId").change(function(){

                    $("#tel").val("");
                    $("#userName").val("");
                    $(".need li").slice(1).remove();

                    firstDom.addClass("li_bag");
                    $(".need").siblings(".down_list").val(firstText);
                    $(".need").siblings(".downList_val").val(firstVal);


                    var supplierId = $(this).val();
                    if("" == supplierId){

                    }else{
                        if (supplierId in needCache) {
                            $.each(needCache[supplierId], function (i, option) {
                                $(".need").append("<li val='"+option.id+"'>"+option.name+"</li>");
                            });
                        } else {
                            $.ajax({
                                type: "GET",
                                url: "../need/getNeeds.jhtml",
                                data: {"supplierId":supplierId},
                                dataType: "json",
                                beforeSend: function () {

                                },
                                success: function (data) {
                                    $.each(data, function (i, option) {
                                        $(".need").append("<li val='"+option.id+"'>"+option.name+"</li>");
                                    });

                                    needCache[supplierId] = data ;
                                },
                                error: function (data) {
                                }
                            });
                        }
                    }




					/*var index = $(this).val();
					$(".need").html("");
					$.ajax({
						url:"add.json",
						success:function(obj){
							needCache = obj;
							$.each(obj[index],function(i, option){
								$(".need").append("<li val='"+option.id+"'>"+option.name+"</li>");
							});

							$("#need").val($(".need li:eq(0)").text());
							$(".needId").val($(".need li:eq(0)").attr("val"));
							$(".need li:eq(0)").addClass("li_bag");
						}
					})*/
				});
			
				
				$("#iframeList").load(function () {
					var mainheight = $(this).contents().find("body>div").height() + 40;
				 	$(this).height(mainheight);
				});
			
			
			$("#goHome").on("click",function(){
				var nav = window.top.$(".index_nav_one");
        			nav.find("li li").removeClass('clickTo');
					nav.find("i").removeClass('click_border');
			})
           });
		</script>
	</head>
	<body >
		<div class="child_page"><!--内容外面的大框-->	
			<div class="cus_nav">
				<ul>
					<li><a href="">${message("admin.breadcrumb.home")}</a></li>
					<li><a href="list.jhtml">临时供应列表</a></li>
					<li>添加临时供应</li>
				</ul>
			</div>
			<div class="form_box">
				<form id="inputForm" action="save.jhtml" method="post" class="form form-horizontal">
					<div class='form_baseInfo'>
						<h3 class="list_title">企业信息</h3>
						<div class="pag_div">
							<div class="row cl" style="float:left;">
								<label class="form-label col-xs-4 col-sm-3">
									<img src="${base}/resources/admin1.0/images/bitian_icon.svg" alt="" />
									选择客户
								</label>
								<div class="formControls col-xs-8 col-sm-7" style="position:relative;"> 
									<input type="text" class="input-text radius down_list" readonly placeholder="请选择" id="supplier" />
									<input type="hidden" class="downList_val supplierId" id="supplierId" name="supplier"/>
									<ul class="downList_con supplier">
										<li val="">${message("admin.common.choose")}</li>
										[#list suppliers as supplier]
											[#if supplier != currSupplier]
                                                <li val="${supplier.id}">${supplier.name}</li>
											[/#if]
										[/#list]

									</ul>
								</div>
							</div>
							<div class="row cl" style="float:left;">
								<label class="form-label col-xs-4 col-sm-3">
									<img src="${base}/resources/admin1.0/images/bitian_icon.svg" alt="" />
									选择收货点
								</label>
								<div class="formControls col-xs-8 col-sm-7"> 
									<input type="text" class="input-text radius down_list" readonly placeholder="请选择" id="need">
									<input type="hidden" class="downList_val" id="needId" name="need"/>
									<ul class="downList_con need">
                                        <li val="">${message("admin.common.choose")}</li>
									</ul>
								</div>
							</div>
							<div class="row cl" style="float:left;">
								<label class="form-label col-xs-4 col-sm-3">
									<img src="${base}/resources/admin1.0/images/bitian_icon.svg" alt="" />
									店长姓名
								</label>
								<div class="formControls col-xs-8 col-sm-7"> 
									<input type="text" class="input-text radius" readonly placeholder="" name="userName" id="userName">
								</div>
							</div>
							<div class="row cl" style="float:left;">
								<label class="form-label col-xs-4 col-sm-3">
									<img src="${base}/resources/admin1.0/images/bitian_icon.svg" alt="" />
									店长手机号
								</label>
								<div class="formControls col-xs-8 col-sm-7"> 
									<input type="text" class="input-text radius" readonly placeholder="" name="tel" id="tel">
								</div>
							</div>
							<div class="clear"></div>
						</div>
					</div>
					<div class="hang_list">
						<h3 class="list_title">商品信息</h3>
						
					</div>
					
					<div class="footer_submit">
						<input class="btn radius confir_S" type="button" value="确定" id="submitButton">
						<input class="btn radius cancel_B" type="button" value="取消" onclick="history.back();return false;">
					</div>
					
					<div class="selectList" style="width:100%;">
						<iframe src="selectList.jhtml?goodListStatus=add" id="iframeList" name="iframeList"  frameborder="0" width="100%" height="">
					</div>
					
					
				</form>
			</div>	
		</div>
		
		
		
		
		
		
	</body>
</html>
[/#escape]
