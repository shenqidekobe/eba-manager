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
		<link rel="stylesheet" href="${base}/resources/admin1.0/js/date/dateRange.css" />
		<link rel="stylesheet" href="${base}/resources/admin1.0/css/public.css" />
		<link rel="stylesheet" href="${base}/resources/admin1.0/css/admin.css" />
		<style>
	        body {background: #f9f9f9;}
	  		
	  		
		</style>
		<title>查看订单</title>
	</head>
	<body class="bodyObj">
	<div class="child_page"><!--内容外面的大框-->
		<div class="cus_nav">
			<ul>
				<li><a href="">${message("admin.breadcrumb.home")}</a></li>
				<li><a href="list.jhtml">订货单</a></li>
			</ul>
		</div>
		<div class="form_box reportForm">
			<form class="form form-horizontal" id="form-article-add">
				<div id="tab-system" class="HuiTab">
					<div class="tabBar cl">
						<span>订货单</span>
						<!--<span>采购单</span>-->
					</div>
					<div class="tabCon">
						<div class="queryTerm">
							<div class="query_div1">
								<div class="query_switch">
									<input type="button" class="thisWeek select_color" value="本周" data-type="thisWeek" name="partitionSearch" />
									<input type="button" class="lastWeek" value="上周" data-type="lastWeek" name="partitionSearch"/>
									<input type="button" class="lastMonth " value="上月" data-type="lastMonth" name="partitionSearch"/>
									<input type="button" class="thisMonth " value="本月" data-type="thisMonth" name="partitionSearch"/>
									<i class="select_B"></i>
								</div>
									<div class="query_search">
										<span class="title">查询时段</span>
										<div class="ch_time">
											<span class="chooseTime chooseTime1">${(startDate?string("yyyy-MM-dd"))!}~${(endDate?string("yyyy-MM-dd"))!}</span>
											<input type="hidden" class="startTime" id="startDate" name="startDate" value="${(startDate?string("yyyy-MM-dd"))!}"/>
											<input type="hidden" class="endTime" id="endDate" name="endDate" value="${(endDate?string("yyyy-MM-dd"))!}"/>
											<div class="ta_date" id="div_date_demo3">
									            <span class="date_title" id="date_demo3"></span>
									            <a class="opt_sel" id="input_trigger_demo3" href="#"></a>
									        </div>
										</div>
										<button type="button" class="search_B" id="search">查询</button>
									</div>
								</div>
								<div class="query_div2">
									<span style="margin-right:20px;">订单状态</span>
									<div class="check-box">
										<input type="checkbox" class="input-text radius" checked="checked" name="status" value="pendingReview"/>
										<span>等待审核</span>
									</div>
									<div class="check-box">
										<input type="checkbox" class="input-text radius" checked="checked" name="status" value="pendingShipment"/>
										<span>等待发货</span>
									</div>
									<div class="check-box">
										<input type="checkbox" class="input-text radius" checked="checked" name="status" value="inShipment"/>
										<span>发货中</span>
									</div>
									<div class="check-box">
										<input type="checkbox" class="input-text radius" checked="checked" name="status" value="shipped"/>
										<span>已发货</span>
									</div>
									<div class="check-box">
										<input type="checkbox" class="input-text radius" checked="checked" name="status" value="completed"/>
										<span>已完成</span>
									</div>
									<div class="check-box">
										<input type="checkbox" class="input-text radius" checked="checked" name="status" value="applyCancel"/>
										<span>申请取消</span>
									</div>
								</div>
							</div>
						<div class="i_box orderForm">
							<div class="i_title">待处理订货单</div>
							<div class='i_content'>
								<div class="data_li">
									<h3 class="order_num" id="orderSum">0</h3>
									<h3 class="order_p">订货单数</h3>
								</div>
								<div class="data_li">
									<h3 class="order_num blue" id="customersSum">0</h3>
									<h3 class="order_p">订货客户数</h3>
								</div>
								<div class="data_li">
									<h3 class="order_num" id="amountSum">0</h3>
									<h3 class="order_p">订货单金额</h3>
								</div>
							</div>
						</div>
						
						
						<div class="i_box orderForm">
							<div class="i_title">订货单走势</div>
							<div class='i_content'>
								<div class="time_slot">
									<div class="switch_button">
										<input type="button" class="thisWeek select_color" value="按本周" data-type="thisWeek" name="partitionSearch"/>
										<input type="button" class="thisMonth " value="按本月" data-type="thisMonth" name="partitionSearch"/>
										<i class="select_B"></i>
									</div>
								</div>
								<div id="main" style="width:100%;height:400px;"></div>
							</div>
						</div>
						<div class="orderTable">
							<table class="table table-border table-hover table_width">
								<thead>
									<tr class="text-l">
										<th width="25%">日期</th>
										<th width="25%">订货单数</th>
										<th width="25%">订货客户数</th>
										<th width="25%">订货单金额</th>
									</tr>
								</thead>
								<tbody id="tableData">
								</tbody>
							</table>
						</div>
						
						
						
					</div>
					<div class="tabCon">
						<div class="queryTerm ">
							<div class="query_div1">
								<div class="query_switch">
									<input type="button" class="thisWeek select_color" value="本周" />
									<input type="button" class="lastWeek" value="上周" />
									<input type="button" class="lastMonth " value="上月" />
									<input type="button" class="thisMonth " value="本月" />
									<i class="select_B"></i>
								</div>
								<div class="query_search">
									<span class="title">查询时段</span>
									<div class="ch_time">
										<span class="chooseTime chooseTime2">${(startDate?string("yyyy-MM-dd"))!}~${(endDate?string("yyyy-MM-dd"))!}</span>
										<input type="hidden" class="startTime2" id="startDate2" name="startDate2" value="${(startDate?string("yyyy-MM-dd"))!}"/>
										<input type="hidden" class="endTime2" id="endDate2" name="endDate2" value="${(endDate?string("yyyy-MM-dd"))!}"/>
										<div class="ta_date" id="div_date_demo2">
								            <span class="date_title" id="date_demo2"></span>
								            <a class="opt_sel" id="input_trigger_demo2" href="#"></a>
								        </div>
									</div>
									<button type="button" class="search_B">查询</button>
								</div>
							</div>
							<div class="query_div2">
								<span style="margin-right:20px;">订单状态</span>
								<div class="check-box">
									<input type="checkbox" class="input-text radius" name="" value=""/>
									<span>等待审核</span>
								</div>
								<div class="check-box">
									<input type="checkbox" class="input-text radius" name="" value=""/>
									<span>等待发货</span>
								</div>
								<div class="check-box">
									<input type="checkbox" class="input-text radius" name="" value=""/>
									<span>发货中</span>
								</div>
								<div class="check-box">
									<input type="checkbox" class="input-text radius" name="" value=""/>
									<span>已发货</span>
								</div>
								<div class="check-box">
									<input type="checkbox" class="input-text radius" name="" value=""/>
									<span>已完成</span>
								</div>
								<div class="check-box">
									<input type="checkbox" class="input-text radius" name="" value=""/>
									<span>申请取消</span>
								</div>
							</div>
						</div>
						
						<div class="i_box orderForm">
							<div class="i_title">待处理订货单</div>
							<div class='i_content'>
								<div class="data_li">
									<h3 class="order_num">0</h3>
									<h3 class="order_p">订货单数</h3>
								</div>
								<div class="data_li">
									<h3 class="order_num blue">0</h3>
									<h3 class="order_p">订货客户数</h3>
								</div>
								<div class="data_li">
									<h3 class="order_num">0</h3>
									<h3 class="order_p">订货单金额</h3>
								</div>
							</div>
						</div>
						
						
						<div class="i_box orderForm">
							<div class="i_title">订货单走势</div>
							<div class='i_content wh'>
								<div class="time_slot">
									<div class="switch_button">
										<input type="button" class="thisWeek select_color" value="按本周" />
										<input type="button" class="thisMonth " value="按本周" />
										<i class="select_B"></i>
									</div>
								</div>
								<div id="own_main" style="width:100%;height:400px;"></div>
							</div>
						</div>
						<div class="orderTable">
							<table class="table table-border table-hover table_width">
								<thead>
									<tr class="text-l">
										<th width="25%">日期</th>
										<th width="25%">订货单数</th>
										<th width="25%">订货客户数</th>
										<th width="25%">订货单金额</th>
									</tr>
								</thead>
								<tbody>
									<tr>
										<td>2017-12-12 23:12:23</td>
										<td>23</td>
										<td>23</td>
										<td>￥2344</td>
									</tr>
								</tbody>
							</table>
						</div>
					</div>
 
				</div>
			</form>
		</div>
	</div>
	<script src="${base}/resources/admin1.0/js/jquery.min.js"></script>
	<script type="text/javascript" src="${base}/resources/admin1.0/H-ui/static/h-ui/js/H-ui.min.js"></script>
	<script src="${base}/resources/admin1.0/js/date/dateRange.js"></script><!--时间控件-->
	<script type="text/javascript" src="${base}/resources/admin1.0/js/common.js"></script>
	<script type="text/javascript" src="${base}/resources/admin1.0/js/echarts.js"></script>

	<script type="text/javascript">
		$(function(){

			/*通过js获取页面高度，来定义表单的高度*/
			var formHeight=$(document.body).height();
			
			/*采购单*/
			jQuery.Huitab =function(tabBar,tabCon,class_name,tabEvent,i){
				var $tab_menu=$(tabBar);
				// 初始化操作
				$tab_menu.removeClass(class_name);
				$(tabBar).eq(i).addClass(class_name);
				$(tabCon).hide();
				$(tabCon).eq(i).show();
				  
				$tab_menu.bind(tabEvent,function(){
				  	$tab_menu.removeClass(class_name);
				    $(this).addClass(class_name);
				    var index=$tab_menu.index(this);
				    $(tabCon).hide();
				    $(tabCon).eq(index).show();
				    
				    
				    var myChart2 = echarts.init(document.getElementById("own_main"));
					var option2 = {
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
					        data: ['周一','周二','周三','周四','周五','周六','周日']
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
					            data:[110, 0, 150, 300, 200, 500, 100],
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
					myChart2.setOption(option2);
				})
			}
			$.Huitab("#tab-system .tabBar span","#tab-system .tabCon","current","click","0");
			
			/*采购单*/
			var dateRange2 = new pickerDateRange('date_demo2', {
                aRecent7Days: 'aRecent7DaysDemo3', //最近7天
                isTodayValid: true,
                startDate : '${(startDate?string("yyyy-MM-dd"))!}',
                endDate : '${(endDate?string("yyyy-MM-dd"))!}',
                shortOpr : true,
                stopToday:false,
                defaultText: ' 至 ',
                inputTrigger: 'input_trigger_demo2',
                theme: 'ta',
                success: function (obj) {
                    $(".chooseTime2").html( obj.startDate + "～" + obj.endDate);
                    $(".startTime2").val(obj.startDate);
                    $(".endTime2").val(obj.endDate);
                }
            });
			
			
			
			
			$(".query_switch input").on("click",function(){
				$(this).siblings(".select_B").animate({left:$(this).css("left")},"slow");
				$(this).addClass("select_color").siblings().removeClass("select_color");
			});
			$(".switch_button input").on("click",function(){	
				$(this).siblings(".select_B").animate({left:$(this).css("left")},"slow");
				$(this).addClass("select_color").siblings().removeClass("select_color");
			});
			
			/*订货单*/
			var dateRange = new pickerDateRange('date_demo3', {
                aRecent7Days: 'aRecent7DaysDemo3', //最近7天
                isTodayValid: true,
                startDate : '${(startDate?string("yyyy-MM-dd"))!}',
                endDate : '${(endDate?string("yyyy-MM-dd"))!}',
                /*needCompare : true,
                   isSingleDay : false,*/
                shortOpr : true,
                stopToday:false,
                defaultText: ' 至 ',
                inputTrigger: 'input_trigger_demo3',
                theme: 'ta',
                success: function (obj) {
                    $(".chooseTime1").html( obj.startDate + "～" + obj.endDate);
                    $(".startTime").val(obj.startDate);
                    $(".endTime").val(obj.endDate);
                }
            });
			
			
			loadData("thisMonth");
			$("#search").on("click",function(){
				loadData();
			});
			$("input[name=partitionSearch]").on("click",function(){
				var type=$(this).data("type");
				loadData(type);
			});
			
			function loadData(type){
				var status=[];
				$('input[name="status"]:checked').each(function(){ 
					status.push($(this).val()); 
				}); 
				$.get("list.jhtml",{"status":status,"startDate":$("#startDate").val(),"endDate":$("#endDate").val(),"type":type},function(data){
					var startDate=data.startDate;
					var endDate=data.endDate;
					$(".chooseTime1").html( startDate + "～" + endDate);
					$(".startTime").val(startDate);
                    $(".endTime").val(endDate);
					
					$("#tableData").html("");
					var list=data.list;
					var xData=[];
					var values=[];
					var orderSum=0;
					var customersSum=0;
					var amountSum=0;
					$.each(list,function(i,o){
						xData.push(o.reportDate);
						values.push(o.orderNumber);
						orderSum+=o.orderNumber;
						customersSum+=o.customersNumber;
						amountSum+=o.amount;
						$("#tableData").append("<tr><td>"+o.reportDate+"</td><td>"+o.orderNumber+"</td><td>"+o.customersNumber+"</td><td>￥"+o.amount+"</td></tr>");
					});
					//总量
					$("#orderSum").html(orderSum);
					$("#customersSum").html(customersSum);
					$("#amountSum").html(amountSum);
					//图表
					var myChart = echarts.init(document.getElementById("main"));
					var option=getOption(xData,values);
					myChart.setOption(option);
				});
			}
			
			function getOption(xData,values){
				var option = {
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
				            data:values,
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
			
		});
	
	</script>


</body>
</html>
[/#escape]