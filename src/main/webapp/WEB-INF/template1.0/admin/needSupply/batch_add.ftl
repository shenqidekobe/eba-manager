[#escape x as x?html]
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>${message("admin.admin.add")} - Powered By DreamForYou</title>
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
			#endDate-error{margin-left:36px;float:right;line-height:30px}
			.ch_time .chooseTime{width:280px;}
			.ch_time .ta_date .date_title{width:280px;}
			.ch_time .ta_date{width:310px;}
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
		<script type="text/javascript" src="${base}/resources/admin1.0/js/validate/jquery.validate.min.js"></script>
		<script type="text/javascript" src="${base}/resources/admin1.0/js/common.js"></script>
		<script type="text/javascript">
			var needIds=eval('${needIds}');
			var pageNumber='${pageNumber}';
		    var needProducts = {};
			var needProductsLen = 0;
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

			var formList = $(document.body).height() - 240;
			$(".selectList").css("height",formList);
		
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
				
				if($(this).html() != ''&& $(this).html() != "请选择..."){
					$("#supplier-error").css("display","none");
				}
				
				$(this).addClass("li_bag").siblings().removeClass("li_bag");
			});

                $(".downList_con").each(function () {
                    $(this).find("li:eq(0)").addClass("li_bag");
                    var firstText = $(this).find("li:eq(0)").text();
                    var firstVal = $(this).find("li:eq(0)").attr("val");
                    $(this).siblings(".down_list").val(firstText);
                    $(this).siblings(".downList_val").val(firstVal);
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
			
			// 表单验证
			$inputForm.validate({
				rules: {
					need: {
						required: true
					},
					noticeDay:{
                        digits:true ,
                        required:{
                            required:true,
                            depends: function (element) {
                                return $("input[name='openNotice']:checked").val() == "true";
                            }
						},
						min:1


                    }
				},
				messages: {
					tel: {
						pattern: "格式不正确"
					}
				}
			});

			$("#back").on("click",function(){
 				var $backForm=$("#backForm");
	            var input="";
	            for (var i = 0; i < needIds.length; i++) {
	                input+="<input type='hidden' name='needIds' value='"+needIds[i]+"'>";
	            }
	            input+="<input type='hidden' name='pageNumber' value='"+pageNumber+"'>";
	            $backForm.html(input);
	            $backForm.submit();
            });
			
			$("#submitButton").click(function(){
				var flag = true;
				 
		        if(!$inputForm.valid()){
		            return false ;
		        }
		        if(needProductsLen == 0){
					$.message({'type':'error' , 'content':'请选择商品'});
					return false ;
				}
				//时间段供应关系验证
				//var startDate=$("#startDate").val();
				//var endDate=$("#endDate").val();
				var noticeDay = $("#noticeDay").val();
                var openNotice = $("input[name='openNotice']:checked").val();
				var params={"needIds":needIds};
				var isDistributionModel = $("input[name=assignedModel]:checked").val();
				var isTrue=true;
				$.ajax({
					type: "GET",
		            url: "batchVerification.jhtml",
		            async: false,
		            data: params,
		            dataType: "json",
		            success: function (o) {
		                var data=o.data;
		                if (data.length > 0) {
		                	isTrue=false;
		                	var errMsg="";
		                	for (var i = 0; i < data.length; i++) {
		                		if (i==data.length-1) {
		                			errMsg+=data[i];
		                		}else{
		                			errMsg+=data[i]+"、";
		                		}
		                	}
		                	errMsg+="门店已有供应关系！";
		                	$.message({'type':'error' , 'content':errMsg});
		                }
		            }
				});
				if(!isTrue){
					return;
				}
				
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
		        
		        //tempSaveJson['startDate']=startDate;
		        //tempSaveJson['endDate']=endDate;
		        tempSaveJson['needIds']=needIds;
                tempSaveJson['openNotice']=openNotice;
                tempSaveJson['noticeDay']=noticeDay;
                tempSaveJson['assignedModel']=isDistributionModel;

		        $.ajax({
		            type: "POST",
		            url: "batchSave.jhtml",
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
					<li>添加门店供应</li>
				</ul>
			</div>
			<div class="form_box">
				<form id="backForm" action="batchAddIndex.jhtml" method="get">

    			</form>
				<form id="inputForm" action="" method="post" class="form form-horizontal">
					<div class='form_baseInfo'>
						<h3 class="list_title">基本信息</h3>
						<div class="pag_div">
							<div class="row cl" style="float:left;">
								<label class="form-label col-xs-4 col-sm-3">
									<img src="${base}/resources/admin1.0/images/bitian_icon.svg" alt="" />供应模式</label>
								<div class="formControls col-xs-8 col-sm-7" style="position:relative;"> 
									[#if currSupplier.systemSetting.isDistributionModel]
									<div class="radio-box">
										<input type="radio" id="" name="assignedModel"  value="STRAIGHT">
										<label for="radio-1">直销模式</label>
									</div>
									<div class="radio-box" >
										<input type="radio" id="" name="assignedModel" checked="checked" value="BRANCH">
										<label for="radio-2">分销模式</label>
									</div>
									[#else]
									<div class="radio-box">
										<input type="radio" id="" name="assignedModel" checked="checked" value="STRAIGHT">
										<label for="radio-1">直销模式</label>
									</div>
									[/#if]
								</div>
							</div>
							
                            <div class="row cl" style="float:left; width:80%">
                                <label class="form-label col-xs-4 col-sm-3">
                                    <img src="${base}/resources/admin1.0/images/bitian_icon.svg" alt="" />订单消息提醒</label>
                                <div class="formControls col-xs-8 col-sm-7">
                                    <div class="col_div">
                                    	<input type="radio" name="openNotice" value="false" checked="checked"/><span>不开启</span>
                                    </div>
									<div class="col_div">
										<input type="radio" name="openNotice" value="true" /><span>开启</span>个体客户最后一次下单后<input name="noticeDay" value="5" id="noticeDay" class="input-text radius" /> 天进行微信消息提醒 <i>系统默认为5天,当天的9:00进行提醒</i>
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
						<input class="btn radius cancel_B" id="back" type="button" value="上一步" />
						<input class="btn radius confir_S" type="button" id="submitButton" value="${message("admin.common.submit")}"/>
					</div>
					
					<div class="selectList" style="width:100%;height:100%">
						<iframe src="selectList.jhtml?goodListStatus=add" id="iframeList" name="iframeList"  frameborder="0" width="100%" height="" scrolling="no">
					</div>
					
				</form>
			</div>	
		</div>
	</body>
</html>
[/#escape]
