[#escape x as x?html]
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title></title>
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
			.pag_div1{width:45%;float:left;}
			.col-sm-7{width:72%;}
			.require_search,.ch_search,.update_B{border:1px solid #CADBF3;}
			.table-border th{border-top:1px solid #f0f0f0;}
			#iframeList{overflow: hidden;}
			.form_box{overflow: auto;}
			#endDate-error{margin-left:36px;float:right;line-height:30px}
			.ch_time .chooseTime{width:280px;}
			.ch_time .ta_date .date_title{width:280px;}
			.ch_time .ta_date{width:320px;}
			.col_div{padding-top:8px;font-size:14px;color:#333;}
			.col_div span{margin-right:15px;}
			#noticeDay{width:50px;height:25px;padding-left:8px}
			.col_div i{color:#999;margin-left:30px;font-size:12px}
			.xxDialog {
				top: 40px
			}

			#addProductImage {
				margin: 10px 30px;
			}

			.xxDialog .dialogBottom {
				width: 100%;
				position: absolute;
				bottom: 0;
				background: #fff
			}

			.dialogContent {
				height: calc(100% - 80px);
				overflow: auto;
				overflow-x: hidden;
			}
			iframe {
				height: calc(100% - 5px);
				width: 100%;
			}
		</style>
		<script src="${base}/resources/admin1.0/js/jquery.min.js"></script>
		<script type="text/javascript" src="${base}/resources/admin1.0/js/validate/jquery.validate.min.js"></script>
		<script type="text/javascript" src="${base}/resources/admin1.0/js/common.js"></script>
		<script type="text/javascript">
			var goods = {};
			
			function getCacheFromChild(){
				return goods ;
			}
			
			//子页面调用 增加关联商品
			function addGoods(goodId){
				var goodIds={};
				goodIds.goodId=parseInt(goodId);
				goods[parseInt(goodId)]=goodIds;
			}
			//删除关联商品
			function delGoods(goodId) {
			  delete goods[parseInt(goodId)];
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
				$(this).parent().siblings("#id").val($(this).attr("dis"));
				
				if($(this).html() != ''&& $(this).html() != "请选择..."){
					$("#supplier-error").css("display","none");
				}
				
				$(this).addClass("li_bag").siblings().removeClass("li_bag");
			});

                $(".downList_con").each(function () {
                    $(this).find("li:eq(0)").addClass("li_bag");
                    var firstText = $(this).find("li:eq(0)").text();
                    var firstVal = $(this).find("li:eq(0)").attr("val");
                    var firstId = $(this).find("li:eq(0)").attr("dis");
                    $(this).siblings(".down_list").val(firstText);
                    $(this).siblings(".downList_val").val(firstVal);
                    $(this).siblings("#id").val(firstId);
                });
			
			
			
			/*当input获得焦点时，外面的边框显示蓝色*/
			$(".focus_border").focus(function(){
				$(this).parent().addClass("add_border");
			})
			$(".focus_border").blur(function(){
				$(this).parent().removeClass("add_border");
			})
			
			
			//获得年月日      得到日期oTime  
	        function getMyDate(str){  
	            var oDate = new Date(str),  
	            oYear = oDate.getFullYear(),  
	            oMonth = oDate.getMonth()+1,  
	            oDay = oDate.getDate(),  
	            oHour = oDate.getHours(),  
	            oMin = oDate.getMinutes(),  
	            oSen = oDate.getSeconds(),  
	            oTime = oYear +'-'+ getzf(oMonth) +'-'+ getzf(oDay) +' '+ getzf(oHour) +':'+ getzf(oMin) +':'+getzf(oSen);//最后拼接时间  
	            return oTime;  
	        };
	      	//补0操作  
	        function getzf(num){  
	            if(parseInt(num) < 10){  
	                num = '0'+num;  
	            }  
	            return num;  
	        }
		
			var id = $("#id").val();
			if(id != "") {
				$.ajax({
					url: "querySupplierSupplier.jhtml",
					type: "GET",
					async: true,
					data: {"id":id},
					dataType: "json",
					success: function(data) {
						if(data.code == '0') {
							$(".input_no_span").html(getMyDate(data.data.startDate)+"~"+getMyDate(data.data.endDate));
							var goodIdList = data.data.goodIdList;
							 $.each(goodIdList,function(i,value) {
							 	addGoods(value);
					        });
							
						}
					}
				});
				
				$("#iframeList").attr("src","distributionGoodList.jhtml?supplierSupplierId="+id);
				
			}
			
			
			$(".downList_con li").click(function(){
				var supplierSupplierId = $("#id").val();
				goods = {};
				$(".input_no_span").html("");
				$.ajax({
					url: "querySupplierSupplier.jhtml",
					type: "GET",
					async: true,
					data: {"id":supplierSupplierId},
					dataType: "json",
					success: function(data) {
						if(data.code == '0') {
							$(".input_no_span").html(getMyDate(data.data.startDate)+"~"+getMyDate(data.data.endDate));
							var goodIdList = data.data.goodIdList;
							$.each(goodIdList,function(i,value) {
								 addGoods(value);
					        });
						}
					}
				});
				var src = "distributionGoodList.jhtml?supplierSupplierId="+supplierSupplierId;
				$("#iframeList").attr("src",src);
				
			});
			
			$("#submitButton").click(function(){
		        if(goods.length == 0){
					$.message({'type':'error' , 'content':'请选择商品'});
					return false ;
				}
		        var goodsList = new Array();
		        var tempSaveJson = {};
		        $.each(goods,function(i,value) {
		        		goodsList.push(value.goodId);
		        });
		        tempSaveJson['goodsList']=goodsList;
		        tempSaveJson['supplierSupplierId']=$("#id").val();
		        console.log(tempSaveJson);
		        $.ajax({
		            type: "POST",
		            url: "copySupplierGoods.jhtml",
		            data: tempSaveJson,
		            dataType: "json",
		            beforeSend: function () {},
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
			$("#iframeList").load(function () {
				var mainheight = $(this).contents().find("body>div").height() + 40;
				if(mainheight<300){
					mainheight = 300;
				};
			 	$(this).height(mainheight);
			});





			
           });
            function goodsView(id){

                $.dialog({
                    title:"查看",
                    width:$(".bodyObj").width()-20,
                    height:$(".bodyObj").height()-40,
                    content:'<iframe id="sonFrame" frameborder="0" width="" height="" src="distributionView.jhtml?id='+id+'"></iframe>',
                    onShow:function(){

					}
                });


            }
           	
		</script>
	</head>
	<body class="bodyObj">
		<div class="child_page"><!--内容外面的大框-->	
			<div class="cus_nav">
				<ul>
					<li><a href="">${message("admin.breadcrumb.home")}</a></li>
					<li><a href="list.jhtml">商品列表 </a></li>
					<li>分销商品</li>
				</ul>
			</div>
			<div class="form_box">
				<form id="inputForm" action="save.jhtml" method="post" class="form form-horizontal">
					<div class='form_baseInfo'>
						<h3 class="list_title">供应商</h3>
						<div class="pag_div">
							<div class="row cl" style="float:left;">
								<label class="form-label col-xs-4 col-sm-3">
									<img src="${base}/resources/admin1.0/images/bitian_icon.svg" alt="" />供应商</label>
								<div class="formControls col-xs-8 col-sm-7" style="position:relative;"> 
									<input type="text" class="input-text radius down_list" readonly placeholder="请选择">
									<input type="text" class="downList_val" name="supplier" id="supplier"/>
									<input type="hidden" id="id">
									<ul class="downList_con">
										[#list page.content as supplierSupplier]
											[#if supplierSupplier.supplier != currSupplier]
												<li val="${supplierSupplier.supplier.id}" dis="${supplierSupplier.id}">${supplierSupplier.supplier.name}</li>
											[/#if]
										
										[/#list]
									</ul>
								</div>
							</div>
							<div class="row cl" style="float:left;">
								<label class="form-label col-xs-4 col-sm-3">
									<img src="${base}/resources/admin1.0/images/bitian_icon.svg" alt="" />
									供应时间
								</label>
								<div class="formControls col-xs-8 col-sm-7"> 
									<span class="input_no_span" style="color:#333"></span>
								</div>
							</div>
                            
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
						<iframe src="" id="iframeList" name="iframeList"  frameborder="0" width="100%" height="" scrolling="no">
					</div>
					
					
				</form>
			</div>	
		</div>
	</body>
</html>
[/#escape]
