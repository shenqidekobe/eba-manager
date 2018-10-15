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
		<title>订单报表</title>
	</head>
	<body class="bodyObj">
	<div class="child_page"><!--内容外面的大框-->
		<div class="cus_nav">
			<ul>
				<li><a href="">${message("admin.breadcrumb.home")}</a></li>
                <li><a href="index.jhtml">订单报表</a></li>
			</ul>
		</div>
		<div class="form_box reportForm">
				<div id="tab-system" class="HuiTab">
					<div class="tabBar cl">
						<a href="index.jhtml"><span>订货单</span></a>
						<span>采购单</span>
					</div>
					<div class="tabCon">
						
					</div>
					<div class="tabCon">
						<div class="queryTerm">
							<form id="downLoadForm" action="exportOrderReport.jhtml" method="get">
								<input type="hidden" id="queyType" name="queyType" value="purchaseForm">
								<input type="hidden" name="timeSearch" value="createTime">
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
											<div class="ch_operate">
												<button type="button" class="op_button daochu_B" id="downButton">导出</button>
											</div>
										</div>
									</div>
									<div class="query_div2">
										<span style="margin-right:20px;">订单状态</span>
										<div class="check-box">
											<input type="checkbox" class="input-text radius" checked="checked" name="statuses" value="pendingReview"/>
											<span>等待审核</span>
										</div>
										<div class="check-box">
											<input type="checkbox" class="input-text radius" checked="checked" name="statuses" value="pendingShipment"/>
											<span>等待发货</span>
										</div>
										<div class="check-box">
											<input type="checkbox" class="input-text radius" checked="checked" name="statuses" value="inShipment"/>
											<span>发货中</span>
										</div>
										<div class="check-box">
											<input type="checkbox" class="input-text radius" checked="checked" name="statuses" value="shipped"/>
											<span>已发货</span>
										</div>
										<div class="check-box">
											<input type="checkbox" class="input-text radius" checked="checked" name="statuses" value="completed"/>
											<span>已完成</span>
										</div>
										<div class="check-box">
											<input type="checkbox" class="input-text radius" checked="checked" name="statuses" value="applyCancel"/>
											<span>申请取消</span>
										</div>
									</div>
								</div>
							</form>
						<div class="i_box orderForm">
							<!--<div class="i_title">待处理订货单</div>-->
							<div class='i_content'>
								<div class="data_li">
									<div class="order_num divSum" >0</div>
									<h3 class="order_p">采购单数</h3>
								</div>
								<div class="data_li">
									<div class="order_num" id="customersSum">0</div>
									<h3 class="order_p">采购供应商数</h3>
								</div>
								<div class="data_li">
									<div class="order_num" id="amountSum">0</div>
									<h3 class="order_p">采购单金额</h3>
								</div>
							</div>
						</div>
						
						
						<div class="i_box orderForm">
							<div class="i_title">采购单走势</div>
							<div class='i_content'>
								<div id="main" style="width:100%;height:400px;"></div>
							</div>
						</div>
						<div class="orderTable">
							<table class="table table-border table-hover table_width">
								<thead>
									<tr class="text-l">
										<th width="25%">日期</th>
										<th width="25%">采购单数</th>
										<th width="25%">采购供应商数</th>
										<th width="25%">采购单金额</th>
									</tr>
								</thead>
								<tbody id="tableData">
								</tbody>
							</table>
						</div>
 					</div>
				</div>
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
			
			$.Huitab("#tab-system .tabBar span","#tab-system .tabCon","current","click","1");
			
			/*采购单*/
			
			$(".query_switch input").on("click",function(){
				$(this).siblings(".select_B").animate({left:$(this).css("left")},"slow");
				$(this).addClass("select_color").siblings().removeClass("select_color");
			});
			//tab致灰
            function tabGray(){
                $(".select_color").siblings(".select_B").animate({left:-200},"slow");
                $(".query_switch input").removeClass("select_color");
            }
			//时间控件的选中事件
			function checkDateBox(startDate,endDate){
                $("#div_date_demo3").remove();
                $("#endDate").after("<div class=\"ta_date\" id=\"div_date_demo3\">" +
                        "<span class=\"date_title\" id=\"date_demo3\"></span>" +
                        "<a class=\"opt_sel\" id=\"input_trigger_demo3\" href=\"#\"></a>" +
                        "</div>");
				new pickerDateRange('date_demo3', {
	                aRecent7Days: 'aRecent7DaysDemo3', //最近7天
	                isTodayValid: true,
	                startDate : startDate,
	                endDate : endDate,
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
			}
            
			//默认加载本周数据
			loadData("thisWeek");
			$("#search").on("click",function(){
				var startDate=$("#startDate").val();
				var endDate=$("#endDate").val();
				if(dateDiff(startDate,endDate) > 60){
					information();
					return;
				}
                tabGray();
				loadData();
			});
			
			$("input[name=partitionSearch]").on("click",function(){
				var type=$(this).data("type");
				loadData(type);
			});
			
			$("input[name=statuses]").on("click",function(){
				loadData();
			});
			$("#downButton").on("click",function(){
				export1();
			});
			$(".divSum").delegate("#orderSum","click",function(){
				if($(this).html() == 0){
					return;
				}
				
				var nav = window.top.$(".index_nav_one");
		        	nav.find("li li").removeClass('clickTo');
		        	nav.find(".orderLi li.purchase_B").addClass("clickTo");
					nav.find("i").removeClass('click_border');
					nav.find(".orderLi i").addClass("click_border");
				
				$("#downLoadForm").attr("action", "../ownOrder/list.jhtml");
		        $("#downLoadForm").submit();
			});
			var addOnClick=function(){
				$("a[name=tablbOrderNum]").on("click",function(){
					//获取点击的订单量的日期
					var reportDate=$(this).data("reportdate");
					//设置参数开始结束日期为点击的日期
					$("#startDate").val(reportDate);
					$("#endDate").val(reportDate);
					//提交form，跳转到订单页面

                    var nav = window.top.$(".index_nav_one");
                    nav.find("li li").removeClass('clickTo');
                    nav.find(".orderLi li.purchase_B").addClass("clickTo");
                    nav.find("i").removeClass('click_border');
                    nav.find(".orderLi i").addClass("click_border");

					$("#downLoadForm").attr("action", "../ownOrder/list.jhtml");
			        $("#downLoadForm").submit();
				});
			}
			
			function loadData(type){
				var statuses=[];
				$('input[name="statuses"]:checked').each(function(){ 
					statuses.push($(this).val()); 
				}); 
				$.get("purchaseList.jhtml",{"statuses":statuses,"startDate":$("#startDate").val(),"endDate":$("#endDate").val(),"type":type,"queyType":$("#queyType").val()},function(data){
					var startDate=data.startDate;
					var endDate=data.endDate;
					checkDateBox(startDate,endDate);
					$(".chooseTime1").html( startDate + "～" + endDate);
					$(".startTime").val(startDate);
                    $(".endTime").val(endDate);
					
					$("#tableData").html("");
					var list=data.list;
					var xData=[];
					var values=[];
					var customersSum=data.customersSum;
					var orderSum=0;
					var amountSum=0;
					for(var i = 0;i < list.length ;i++){
						var o=list[i];
						xData.push(o.reportDate);
						values.push(o.orderNumber);
						orderSum+=o.orderNumber;
						amountSum+=o.amount;
						o=list[list.length-i-1];
                        var htmls="";
                        htmls+="<tr><td>"+o.reportDate+"</td>";
                        if(o.orderNumber > 0){
                            htmls+="<td><a class='blue' href='javascript:void(0);' name='tablbOrderNum' data-reportdate='"+o.reportDate+"' >"+o.orderNumber+"</a></td>";
                        }else{
                            htmls+="<td>"+o.orderNumber+"</td>";
                        }
                        htmls+="<td>"+o.customersNumber+"</td><td>￥"+o.amount+"</td></tr>";
                        $("#tableData").append(htmls);
					}
					//添加点击事件
					addOnClick();
					//总量
					if(orderSum>0){
						$(".divSum").html('<a class="blue" href="javascript:void(0);" id="orderSum">'+orderSum+'</a>');
					}else{
						$(".divSum").html(orderSum);
					}
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
			
			function information(){
				$.dialog({
		            title:"提示信息",
		            width:450,
		            height:230,
		            content:[@compress single_line = true]
					'<form id="reviewForm" class="form form-horizontal" action="" method="post">
						<div class="pag_div1" style="margin:50px 110px;">
							采购单统计只支持两个月数据统计，超过两个月的数据请导出查看
						<\/div>
					<\/form>'
					[/@compress],
					modal: true,
		            onShow:function(){},
		            onOk: function() {
		            	$("#downLoadForm").attr("action", "exportOrderReport.jhtml");
		            	$("#downLoadForm").submit();
		            }
		        });	
			}
			
			function export1(){
				$.dialog({
		            title:"提示信息",
		            width:450,
		            height:230,
		            content:[@compress single_line = true]
					'<form id="reviewForm" class="form form-horizontal" action="" method="post">
						<div class="pag_div1" style="margin:50px 110px;">
							确认导出采购单报表吗？
						<\/div>
					<\/form>'
					[/@compress],
					modal: true,
		            onShow:function(){},
		            onOk: function() {
		            	$("#downLoadForm").attr("action", "exportOrderReport.jhtml");
		            	$("#downLoadForm").submit();
		            }
		        });	
			}
			
			//计算天数差的函数，通用  
   			function  dateDiff(sDate1,  sDate2){    //sDate1和sDate2是2006-12-18格式  
			       var  aDate,  oDate1,  oDate2,  iDays;
			       aDate  =  sDate1.split("-");
			       oDate1  =  new  Date(aDate[1]  +  '-'  +  aDate[2]  +  '-'  +  aDate[0]);   //转换为12-18-2006格式  
			       aDate  =  sDate2.split("-");
			       oDate2  =  new  Date(aDate[1]  +  '-'  +  aDate[2]  +  '-'  +  aDate[0]);  
			       iDays  =  parseInt(Math.abs(oDate1  -  oDate2)  /  1000  /  60  /  60  /24);    //把相差的毫秒数转换为天数  
			       return  iDays;
  			}    
			
		});
	
	</script>


</body>
</html>
[/#escape]