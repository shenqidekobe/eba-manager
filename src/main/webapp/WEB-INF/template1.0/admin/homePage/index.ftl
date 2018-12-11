[#escape x as x?html]
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>${message("admin.order.list")} - Powered By DreamForYou</title>
		<meta http-equiv="X-UA-Compatible" content="IE=edge" />
		<link rel="stylesheet" type="text/css" href="${base}/resources/admin1.0/H-ui/static/h-ui/css/H-ui.min.css" />
		<link rel="stylesheet" href="${base}/resources/admin1.0/js/date/dateRange.css" />
		<link rel="stylesheet" type="text/css" href="${base}/resources/admin1.0/js/kkpager/kkpager_blue.css" />
		<link rel="stylesheet" href="${base}/resources/admin1.0/css/public.css" />
		<link rel="stylesheet" href="${base}/resources/admin1.0/css/admin.css" />
		<style>
			body{background:#f9f9f9;}
			.child_page{padding:0;width:100%;height:100%;}
			.cus_nav{padding:10px;}
		</style>
	</head>
	<body >
		<div class="child_page"><!--内容外面的大框-->	
			<div class="cus_nav">
				<ul>
					<li><a href="">${message("admin.breadcrumb.home")}</a></li>
				</ul>
			</div>
			<form id="listForm" action="" method="get">
				<input type="hidden" name="status" id="status" />
				<input type="hidden" name="startDate" id="startDate"/>
				<input type="hidden" name="endDate" id="endDate"/>
				<input type="hidden" name="timeSearch" id="timeSearch"/>
				<div class="index_order">
					<h3 class="index_title">订货单概况</h3>
					<div class="i_content">
						<div class="i_box box_left">
							<div class="i_title">待处理订货单</div>
							<div class="i_content">
								<div class="data_li">
									<div class="order_num">
									[#if moderated > 0]
										<a class="blue supplyB" href="javascript:void(0);" id="moderated">${moderated}</a>
									[#else]
										${moderated}
									[/#if]
									</div>
									<h3 class="order_p">等待付款</h3>
								</div>
								<div class="data_li">
									<div class="order_num">
									[#if waitForDelivery > 0]
										<a class="blue supplyB" href="javascript:void(0);" id="waitForDelivery">${waitForDelivery}</a>
									[#else]
										${waitForDelivery}
									[/#if]
									
									</div>
									<h3 class="order_p">等待发货</h3>
								</div>
								<div class="data_li">
									<div class="order_num">
										[#if InDelivery > 0]
											<a class="blue supplyB" href="javascript:void(0);" id="InDelivery">${InDelivery}</a>
										[#else]
											${InDelivery}
										[/#if]
									</div>
									<h3 class="order_p">发货中</h3>
								</div>
								<div class="data_li">
									<div class="order_num">
									[#if cancel > 0]
										<a class="blue supplyB" href="javascript:void(0);" id="cancel">${cancel}</a>
									[#else]
										${cancel}
									[/#if]
									</div>
									<h3 class="order_p">已取消</h3>
								</div>
							</div>
						</div>
						<div class="i_box box_right">
							<div class="i_title">本日订货单数</div>
							<div class="i_content">
								<div class="right_li">
									<div class="order_num">
									[#if todayOrderForm.orderTotal > 0]
										<a class="blue supplyB" href="javascript:void(0);" id="todayOrder">${todayOrderForm.orderTotal}</a>
									[#else]
										${todayOrderForm.orderTotal}
									[/#if]
									</div>
									<h3 class="order_p">订货单数</h3>
								</div>
								<div class="right_li">
									<div class="order_num">
									[#if todayOrderForm.totalAmount??]
										${currency(todayOrderForm.totalAmount, true)}
									[#else]
										0
									[/#if]
									</div>
									<h3 class="order_p">订货单金额</h3>
								</div>
							</div>
						</div>
						<div class="i_box box_left">
							<div class="i_title">订货单走势</div>
							<div class="i_content">
								<div class="time_slot">
									<div class="switch_button">
										<input type="button" class="thisWeek select_color" id="orderFormThisWeek" value="按本周" />
										<input type="button" class="thisMonth" id="orderFormThisMonth" value="按本月" />
										<i class="select_B"></i>
									</div>
								</div>
								<div id="main" style="width:100%;height:400px;"></div>
							</div>
						</div>
						<div class="i_box box_right">
							<div class="i_title">订货单相关</div>
							<div class="i_content">
								<div class="right_li">
									<div class="order_num">${orderRelated.goodTotal}</div>
									<h3 class="order_p">订货商品SKU</h3>
								</div>
								<!--<div class="right_li">
									<div class="order_num">${orderRelated.supplierTotal}</div>
									<h3 class="order_p">订货客户数</h3>
								</div>-->
							</div>
						</div>
					</div>
				</div>
		
<!--				<div class="index_order">
					<h3 class="index_title">采购单概况</h3>
					<div class="i_content">
						<div class="i_box box_left">
							<div class="i_title">待处理采购单</div>
							<div class="i_content">
								<div class="data_li">
									<div class="order_num">
									[#if moderateds > 0]
										<a class="blue purchaseB" href="javascript:void(0);" id="moderateds">${moderateds}</a>
									[#else]
										${moderateds}
									[/#if]
									</div>
									<h3 class="order_p">等待审核</h3>
								</div>
								<div class="data_li">
									<div class="order_num">
									[#if waitForDeliverys > 0]
										<a class="blue purchaseB" href="javascript:void(0);" id="waitForDeliverys">${waitForDeliverys}</a>
									[#else]
										${waitForDeliverys}
									[/#if]
									</div>
									<h3 class="order_p">等待发货</h3>
								</div>
								<div class="data_li">
									<div class="order_num">
									[#if InDeliverys > 0]
										<a class="blue purchaseB" href="javascript:void(0);" id="InDeliverys">${InDeliverys}</a>
									[#else]
										${InDeliverys}
									[/#if]
									</div>
									<h3 class="order_p">发货中</h3>
								</div>
								<div class="data_li">
									<div class="order_num">
									[#if cancels > 0]
										<a class="blue purchaseB" href="javascript:void(0);" id="cancels">${cancels}</a>
									[#else]
										${cancels}
									[/#if]
									</div>
									<h3 class="order_p">已取消</h3>
								</div>
							</div>
						</div>
						<div class="i_box box_right">
							<div class="i_title">本日采购单数</div>
							<div class="i_content">
								<div class="right_li">
									<div class="order_num">
									[#if todayPurchaseOrder.orderTotal > 0]
										<a class="blue purchaseB" href="javascript:void(0);" id="todayPurchaseOrder">${todayPurchaseOrder.orderTotal}</a>
									[#else]
										${todayPurchaseOrder.orderTotal}
									[/#if]
									</div>
									<h3 class="order_p">采购单数</h3>
								</div>
								<div class="right_li">
									<div class="order_num">
									[#if todayPurchaseOrder.totalAmount??]
										${currency(todayPurchaseOrder.totalAmount, true)}
									[#else]
										0
									[/#if]
									</div>
									<h3 class="order_p">采购单金额</h3>
								</div>
							</div>
						</div>
						<div class="i_box box_left">
							<div class="i_title">采购单走势</div>
							<div class="i_content">
								<div class="time_slot">
									<div class="switch_button">
										<input type="button" class="thisWeek select_color" id="PurchaseOrderWeek" value="按本周" />
										<input type="button" class="thisMonth " id="PurchaseOrderMonth" value="按本月" />
										<i class="select_B"></i>
									</div>
								</div>
								<div id="own_main" style="width:100%;height:400px;"></div>
							</div>
						</div>
						<div class="i_box box_right">
							<div class="i_title">采购单相关</div>
							<div class="i_content">
								<div class="right_li">
									<div class="order_num">${purchaseOrderRelated.goodTotal}</div>
									<h3 class="order_p">采购商品SKU</h3>
								</div>
								<div class="right_li">
									<div class="order_num">${purchaseOrderRelated.supplierTotal}</div>
									<h3 class="order_p">采购供应商数</h3>
								</div>
							</div>
						</div>
					</div>
				</div>
			-->	
			</form>
		</div>
		<script src="${base}/resources/admin1.0/js/jquery.min.js"></script>
		<script type="text/javascript" src="${base}/resources/admin1.0/js/common.js"></script>
        <script type="text/javascript" src="${base}/resources/admin1.0/js/list.js"></script>
        <script type="text/javascript" src="${base}/resources/admin1.0/js/echarts.js"></script>
        
        <script type="text/javascript">
	       
	        $(function(){
	        	
	        	$(".index_order a").on("click",function(){
		        	
		        	var nav = window.top.$(".index_nav_one");
		        		nav.find("li li").removeClass('clickTo');
		        	console.log($(this).hasClass("supplyB"));
					if($(this).hasClass("supplyB")){
		        		nav.find(".orderLi li.supply_B").addClass("clickTo");
		        	}else{
		        		nav.find(".orderLi li.purchase_B").addClass("clickTo");
		        	}
					
					nav.find("i").removeClass('click_border');
					nav.find(".orderLi i").addClass("click_border");
				});
	        	
	        	
	        	loadData();
	        	
	        	function loadData() {
	        		orderFrom("thisWeek");
	        		//purchaseOrder("thisWeek");
	        	}
	        	
	        	$("#orderFormThisMonth").on("click",function(){
	        		orderFrom("thisMonth");
	        	});
	        	
	        	$("#orderFormThisWeek").on("click",function(){
	        		orderFrom("thisWeek");
	        	});
	        	
	        	$("#PurchaseOrderMonth").on("click",function(){
	        		//purchaseOrder("thisMonth");
	        	});
	        	
	        	$("#PurchaseOrderWeek").on("click",function(){
	        		//purchaseOrder("thisWeek");
	        	});
	        	
	        	//等待审核
				$("#moderated").on("click",function(){
					if($(this).html() == 0){
						return;
					}
					$("#status").val("pendingReview");
					$("#listForm").attr("action", "../order/list.jhtml");
			        $("#listForm").submit();
				});
	        	//等待发货
				$("#waitForDelivery").on("click",function(){
					if($(this).html() == 0){
						return;
					}
					$("#status").val("pendingShipment");
					$("#listForm").attr("action", "../order/list.jhtml");
			        $("#listForm").submit();
				});
				//发货中
				$("#InDelivery").on("click",function(){
					if($(this).html() == 0){
						return;
					}
					$("#status").val("inShipment");
					$("#listForm").attr("action", "../order/list.jhtml");
			        $("#listForm").submit();
				});
				//申请取消
				$("#cancel").on("click",function(){
					if($(this).html() == 0){
						return;
					}
					$("#status").val("applyCancel");
					$("#listForm").attr("action", "../order/list.jhtml");
			        $("#listForm").submit();
				});
				
				//今日订货单
				$("#todayOrder").on("click",function(){
					if($(this).html() == 0){
						return;
					}
					$("#status").val("");
					$("#startDate").val(getMyDate(new Date()));
					$("#endDate").val(getMyDate(new Date()));
					$("#timeSearch").val("createTime");
					$("#listForm").attr("action", "../order/list.jhtml");
			        $("#listForm").submit();
				});
				
				//采购单
				
				//等待审核
				$("#moderateds").on("click",function(){
					if($(this).html() == 0){
						return;
					}
					$("#status").val("pendingReview");
					$("#listForm").attr("action", "../ownOrder/list.jhtml");
			        $("#listForm").submit();
				});
	        	//等待发货
				$("#waitForDeliverys").on("click",function(){
					if($(this).html() == 0){
						return;
					}
					$("#status").val("pendingShipment");
					$("#listForm").attr("action", "../ownOrder/list.jhtml");
			        $("#listForm").submit();
				});
				//发货中
				$("#InDeliverys").on("click",function(){
					if($(this).html() == 0){
						return;
					}
					$("#status").val("inShipment");
					$("#listForm").attr("action", "../ownOrder/list.jhtml");
			        $("#listForm").submit();
				});
				//申请取消
				$("#cancels").on("click",function(){
					if($(this).html() == 0){
						return;
					}
					$("#status").val("applyCancel");
					$("#listForm").attr("action", "../ownOrder/list.jhtml");
			        $("#listForm").submit();
				});
				
				//今日订货单
				$("#todayPurchaseOrder").on("click",function(){
					if($(this).html() == 0){
						return;
					}
					$("#status").val("");
					$("#startDate").val(getMyDate(new Date()));
					$("#endDate").val(getMyDate(new Date()));
					$("#timeSearch").val("createTime");
					$("#listForm").attr("action", "../ownOrder/list.jhtml");
			        $("#listForm").submit();
				});
	        	
	        	function orderFrom(type) {
	        		$.ajax({
						url: "orderharts.jhtml",
						type: "GET",
						data: {"type":type},
						dataType: "json",
						success: function(data) {
							var xData = [];
							var yData = [];
							var dto= data.orderStatisticsDtos;
							for(var i=0;i<dto.length;i++) {
								xData.push(dto[i].createDate);
								yData.push(dto[i].orderTotal);
							}
							var myChart = echarts.init(document.getElementById("main"));
							optionTest(xData,yData);
							myChart.setOption(option);
						}
					});
	        	}
	        	
	        	function purchaseOrder(type) {
	        		$.ajax({
						url: "purchaseOrderCharts.jhtml",
						type: "GET",
						data: {"type":type},
						dataType: "json",
						success: function(data) {
							var xData = [];
							var yData = [];
							var dto= data.dto;
							for(var i=0;i<dto.length;i++) {
								xData.push(dto[i].createDate);
								yData.push(dto[i].orderTotal);
							}
							var myChart = echarts.init(document.getElementById("own_main"));
							optionTest(xData,yData);
							myChart.setOption(option);
						}
					});
	        	}
	        	
	       
				/*表格的高度，，随着电脑分倍率的变化而变化*/
				var heightObj = $(document.body).height() - 170;
				$(".list_t_tbody").css("height",heightObj);
				
				$(".switch_button input").on("click",function(){
				
					$(this).siblings(".select_B").animate({left:$(this).css("left")},"slow");
					$(this).addClass("select_color").siblings().removeClass("select_color");
	
				})
				
				
				/*订货单*/
				function optionTest(xData,yData){
					option = {
					    title: {
					        text: '',
					        subtext: '(单位：笔)'
					    },
					    tooltip: {
					        trigger: 'axis'
					    },
					    xAxis:  {
					        type: 'category',
					        boundaryGap: false,
					        data: xData
					    },
					    yAxis: {
					        type: 'value',
					        axisLabel: {
					            formatter: '{value}'
					        }
					    },
					    series: [
					        {
					            name:'',
					            type:'line',
					            data:yData,
					            itemStyle : {  
	                                normal : {  
	                                	color:'#4DA1FF',
	                                    lineStyle:{  
	                                        color:"#4DA1FF"  
	                                    }  
	                                }  
	                            }
					        }
					    ]
						};
					return option;
				}
				
				//获得年月日      得到日期oTime  
		        function getMyDate(str){  
		            var oDate = new Date(str),  
		            oYear = oDate.getFullYear(),  
		            oMonth = oDate.getMonth()+1,  
		            oDay = oDate.getDate(),  
		            oHour = oDate.getHours(),  
		            oMin = oDate.getMinutes(),  
		            oSen = oDate.getSeconds(),  
		            oTime = oYear +'-'+ getzf(oMonth) +'-'+ getzf(oDay);// +' '+ getzf(oHour) +':'+ getzf(oMin) +':'+getzf(oSen);//最后拼接时间  
		            return oTime;  
		        };  
		        //补0操作  
		        function getzf(num){  
		            if(parseInt(num) < 10){  
		                num = '0'+num;  
		            }  
		            return num;  
		        }



                $("body").click(function(){
                    window.top.$(".show_news").removeClass("show");
                })
		        
		        
		        
		        
		        
		        
		        
			
	        })
	      
		</script>
	</body>
</html>
[/#escape]