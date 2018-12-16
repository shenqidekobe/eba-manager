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
	  		.daochu_B{
	  			float:right;
	  			height:32px;
	  			border:1px solid #f0f0f0;
	  		}
	  		.daochu_B:hover{
	  			border:1px solid #4Da1ff;
	  		}
	  		.i_content{
	  			position:relative;
	  		}
	  		.nodataCss{
	  			position:absolute;
	  			width:100%;
	  			top:50px;
	  			text-align: center;
	  			line-height:250px;
	  			z-index: 9;
	  			display:none;
	  		}
	  		.proxyUserType[type="text"]{float:left;border:0;width:60px;height:30px;background:#f9f9f9;}
	  		.drop_down:hover .check{
				display:block;
			}
	  		
		</style>
		<title>查看订单</title>
	</head>
	<body class="bodyObj">
	<div class="child_page"><!--内容外面的大框-->
		<div class="cus_nav">
			<ul>
				<li><a id="goHome" href="../homePage/index.jhtml">首页</a></li>
				<li><a href="">商品报表</a></li>
			</ul>
		</div>
		<div class="form_box reportForm">
			<form action="" method="GET" class="form form-horizontal" id="listForm">
				<input type="hidden" name="ts" id="ts" />
				<input type="hidden" name="search" id="search"/>
				<input type="hidden" name="exportType" id="exportType"/>
				<input type="hidden" id="proxyUserId" name="proxyUserId" value="${proxyUserId}"/>
				<div id="tab-system" class="HuiTab">
					<div class="tabBar cl">
						<a href="index.jhtml"><span>订货商品</span></a>
						<!--<a href="supplyIndex.jhtml"><span>采购商品</span></a>-->
					</div>
					<div class="tabCon">
						<div class="queryTerm">
							<div class="query_div1">
								<div class="query_switch">
									<input type="button" class="thisWeek select_color" value="本周" ts="thisWeek" />
									<input type="button" class="lastWeek" value="上周" ts="lastWeek" />
									<input type="button" class="lastMonth " value="上月" ts="lastMonth" />
									<input type="button" class="thisMonth " value="本月" ts="thisMonth" />
									<i class="select_B"></i>
								</div>
								<div class="query_search">
									<span class="title">查询时段</span>
									<div class="ch_time">
										<span class="chooseTime chooseTime1"><label id="showDate"></label></span>
										<input type="hidden" class="startTime" id="startDate" name="startDate" value=""/>
										<input type="hidden" class="endTime" id="endDate" name="endDate" value=""/>
										<div class="ta_date" id="div_date_demo3">
								            <span class="date_title" id="date_demo3"></span>
								            <a class="opt_sel" id="input_trigger_demo3" href="#"></a>
								        </div>
									</div>
									<!--<div class="drop_down" id="dropDown2" style="margin-left:50px;width:160px;">
										<span class="proxyUserType">所有代理</span>
										<ul class="check">
											<li name="proxyUserId" val="0">所有代理</li>
											 [#list proxyUserTree as proxyUser]
					                            <li name="proxyUserId" [#if proxyUser.id == proxyUserId] class="checked"[/#if] val="${proxyUser.id}">[#if proxyUser.grade != 0][#list 1..proxyUser.grade as i]&nbsp;&nbsp;[/#list][/#if]${proxyUser.name}</li>
					                        [/#list]
										</ul>
									</div>-->
									<button type="button" class="search_B">查询</button>
									<!--<button type="button" class="op_button daochu_B" id="downButton">导出</button>-->
								</div>
							</div>
							<div class="query_div2">
								<span style="margin-right:20px;">订单状态</span>
								<div class="check-box">
									<input type="checkbox" checked="checked" class="input-text radius" name="status" value="pendingReview"/>
									<span>等待付款</span>
								</div>
								<div class="check-box">
									<input type="checkbox" checked="checked" class="input-text radius" name="status" value="pendingShipment"/>
									<span>等待发货</span>
								</div>
								<div class="check-box">
									<input type="checkbox" checked="checked" class="input-text radius" name="status" value="inShipment"/>
									<span>发货中</span>
								</div>
								<div class="check-box">
									<input type="checkbox" checked="checked" class="input-text radius" name="status" value="shipped"/>
									<span>已发货</span>
								</div>
								<div class="check-box">
									<input type="checkbox" checked="checked" class="input-text radius" name="status" value="completed"/>
									<span>已完成</span>
								</div>
								<div class="check-box">
									<input type="checkbox" checked="checked" class="input-text radius" name="status" value="canceled"/>
									<span>已取消</span>
								</div>
							</div>
						</div>
						
						<div class="i_box orderForm">
							<div class="i_title">订货商品相关</div>
							<div class='i_content'>
								<div class="data_li">
									<div class="order_num"><div id="numberOfGoods">0</div></div>
									<h3 class="order_p">订货商品SKU</h3>
								</div>
								<div class="data_li">
									<div class="order_num"><div id="total">0</div></div>
									<h3 class="order_p">订货商品数</h3>
								</div>
								<div class="data_li">
									<div class="order_num"><div id="totalAmount">0</div></div>
									<h3 class="order_p">订货商品金额</h3>
								</div>
							</div>
						</div>
						
						
						<div class="i_box orderForm">
							<div class="i_title">订货商品构成</div>
							<div class='i_content'>
								<div class="time_slot">
									<div class="switch_button">
										<input type="button" class="thisWeek select_color" value="按商品" id="byCommodity" val="good" />
										<input type="button" class="thisMonth " value="按分类" id="byClassification" val="classify" />
										<i class="select_B"></i>
									</div>
								</div>
								<div id="main" style="width:100%;height:300px;"></div>
								<p class="nodataCss">暂无数据！</p>
							</div>
						</div>
						<div class="orderTable">
							<table class="table table-border table-hover table_width">
								<thead>
									<tr class="text-l">
										<th width="20%" id="productSn">商品编号</th>
										<th width="25%"><span id="goodName">商品名称</span><i id="goodDate"></i></th>
										<th width="15%">订货单数</th>
										<th width="15%">订货商品数</th>
										<!--<th width="10%">订货客户数</th>-->
										<th width="15%">订单金额</th>
									</tr>
								</thead>
								<tbody>
									
								</tbody>
							</table>
						</div>

					</div>
					<div class="tabCon">
						
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
			
			var $from = $("#form-article-add");
			var $ts = $("#ts");		
			
			loadData();
			
			
			
			/* 按代理搜索 */
				$("#dropDown2 li").click(function(){
                	var $this = $(this);
                	var $dest = $("#" +$this.attr("name"));
                	$dest.val($this.attr("val"));
                });
				var checkedDown2 = $("#dropDown2 li.checked");
				var firstDown2;
				var firstDownTest2;
				if(checkedDown2.length == 0) {
					firstDown2 = $("#dropDown2").find("li:eq(0)");
					firstDownTest2 = firstDown2.html();
					firstDown2.addClass("checked");
				}else{
					firstDownTest2 = checkedDown2.html();
				}
				$(".proxyUserType").html(firstDownTest2); 
				
				
			
    			$("#dropDown2 li").on("click",function(){
            		$(this).parent().siblings(".proxyUserType").html($(this).html());
            		$(".check").css("display","none");
            	});
    			$("#dropDown2").mouseover(function(){
    				$(this).find("ul").css("display","block");
    			});
    			$("#dropDown2").mouseout(function(){
    				$(this).find("ul").css("display","none");
    			});
				
				
			
			$('.query_switch input').on("click",function(){
				
				$("#search").val("");
				var ts = $(this).attr('ts');
				$ts.val(ts);
				loadData();
			});
			
			$("input[name = 'status']").on("click",function(){
				$("#search").val("");
				$("#ts").val("");
				loadData();
			});
			
			$(".search_B").on("click",function(){
				$("#ts").val("");
				$("#search").val("query");
				var startDate=$("#startDate").val();
				var endDate=$("#endDate").val();
				//超过60天导出
				if(dateDiff(startDate,endDate) > 60){
					information();
					return;
				}
				tabGray();
				loadData();
			});
			
			function loadData() {
				var status =[]; 
				$('input[name="status"]:checked').each(function(){ 
					status.push($(this).val()); 
				});  
				var params=[];
	    		params.push(
	    			{name:"ts",value:$("#ts").val()},
	    			{name:"search",value: $("#search").val()},
	    			{name:"startDate",value:$("#startDate").val()},
	    			{name:"endDate",value:$("#endDate").val()},
	    			{name:"status",value: status},
	    			{name:"exportType",value: $("#exportType").val()},
	    			{name:"proxyUserId",value: $("#proxyUserId").val()}
	    		);
				$.ajax({
					url: "orderForm.jhtml",
					type: "GET",
					data: params,
					dataType: "json",
					success: function(data) {
						var datas = data.commodityReportDtos;
						var count = data.statisticsDto;
						var aa = data.startDate;
						var startDate = getMyDate(data.startDate);
						var endDate = getMyDate(data.endDate);
						var quantity = 0;
						$("#startDate").val(startDate);
						$("#endDate").val(endDate);
						$("#showDate").html(startDate+'~'+endDate);
						timePlugin(startDate,endDate);
						$(".orderTable tbody").html("");
						$("#goodDate").html("");
						$("#exportType").val(data.exportType);
						if(data.exportType == 'classify') {
								$("#productSn").hide();
							if(datas) {
								$("#goodDate").html("("+startDate+"~"+endDate+")");
								$("#goodName").html("商品分类");
								for(var i=0;i<datas.length;i++) {
									quantity+=datas[i].orderQuantity;
									var fenxiao = '';
									if(datas[i].source != null && datas[i].source != ''){
										fenxiao='<span class="distribut_label">分销</span>';
									}
									$(".orderTable tbody").append('<tr>'+
											'<td class="hide"></td>'+
											'<td><label id="productName">'+datas[i].name+'</label>'+fenxiao+'</td>'+
											'<td><label id="orderNumber">'+datas[i].orderNumber+'</label></td>'+
											'<td><label id="orderQuantity">'+datas[i].orderQuantity+'</label></td>'+
											<!--'<td><label id="customersNum">'+datas[i].customersNum+'</label></td>'+-->
											'<td><label id="orderAmount">'+"￥"+datas[i].goodAmount+'</label></td>'+
										'</tr>');
								}
							}
							//大饼图数据
				    		var pieChart = [];
							var legendDat = [];
				    		$.each(data.commodityReportDtos,function(i, n){ 
				    			var name=n.name;
				    			if (n.specification != "[]") {
				    				name+=n.specification;
				    			}
				    			if (n.source != null && n.source != '') {
				    				name+="(分销)";
				    			}
				    			pieChart.push({name:name,value:n.orderQuantity});
				    			legendDat.push(name);
				    		});
				    		if(pieChart.length){
								$(".nodataCss").css("display","none");
							}else{
								$(".nodataCss").css("display","block");
							}
			    			var myChart = echarts.init(document.getElementById("main"));
			    			var color = color1;
							if(pieChart.length<10){color = color2;}
							chartsForm(legendDat,pieChart,quantity,color);
							myChart.setOption(option);
						}else {
							$("#productSn").show();
							if(datas) {
								$("#goodDate").html("("+startDate+"~"+endDate+")");
								for(var i=0;i<datas.length;i++) {
									quantity+=datas[i].orderQuantity;
									var fenxiao = '';
									if(datas[i].source != null && datas[i].source != ''){
										fenxiao='<span class="distribut_label">分销</span>';
									}
									
									
									if(datas[i].specification == "[]") {
										$(".orderTable tbody").append('<tr>'+
												'<td><label id="productName">'+datas[i].productSn+'</label></td>'+
												'<td><label id="productName">'+datas[i].name+'</label>'+fenxiao+'</td>'+
												'<td><label id="orderNumber">'+datas[i].orderNumber+'</label></td>'+
												'<td><label id="orderQuantity">'+datas[i].orderQuantity+'</label></td>'+
												<!--'<td><label id="customersNum">'+datas[i].customersNum+'</label></td>'+-->
												'<td><label id="orderAmount">'+"￥"+datas[i].orderAmount+'</label></td>'+
											'</tr>');
									}else {
									$(".orderTable tbody").append('<tr>'+
										'<td><label id="productName">'+datas[i].productSn+'</label></td>'+
											'<td><label id="productName">'+datas[i].name+datas[i].specification+'</label>'+fenxiao+'</td>'+
											'<td><label id="orderNumber">'+datas[i].orderNumber+'</label></td>'+
											'<td><label id="orderQuantity">'+datas[i].orderQuantity+'</label></td>'+
											<!--'<td><label id="customersNum">'+datas[i].customersNum+'</label></td>'+-->
											'<td><label id="orderAmount">'+"￥"+datas[i].orderAmount+'</label></td>'+
										'</tr>');
									}
									
								}
							}
							//大饼图数据
				    		var pieChart = [];
							var legendDat = [];
				    		$.each(data.commodityReportDtos,function(i, n){ 
				    			var name=n.name;
				    			if (n.specification != "[]") {
				    				name+=n.specification;
				    			}
				    			if (n.source != null && n.source != '') {
				    				name+="(分销)";
				    			}
				    			pieChart.push({name:name,value:n.orderQuantity});
				    			legendDat.push(name);
				    		});
							if(pieChart.length){
								$(".nodataCss").css("display","none");
							}else{
								$(".nodataCss").css("display","block");
							}
			    			var myChart = echarts.init(document.getElementById("main"));
			    			var color = color1;
							if(pieChart.length<10){color = color2;}
							chartsForm(legendDat,pieChart,quantity,color);
							myChart.setOption(option);
						}
						$("#numberOfGoods").html(count.numberOfGoods);

						if(count.total == null) {
							$("#total").html(0);
						}else {
							$("#total").html(count.total);
						}
						if(count.totalAmount == null) {
							$("#totalAmount").html(0);
						}else {
							$("#totalAmount").html("￥"+count.totalAmount);
						}
						
					}
				});
			}
			//按分类
			$("#byClassification").on("click",function(){
				$("#exportType").val($(this).attr('val'));
				var status =[]; 
				$('input[name="status"]:checked').each(function(){ 
					status.push($(this).val()); 
				});  
				var params=[];
	    		params.push(
	    			{name:"ts",value:$("#ts").val()},
	    			{name:"startDate",value:$("#startDate").val()},
	    			{name:"endDate",value:$("#endDate").val()},
	    			{name:"status",value: status}
	    		);
				$.ajax({
					url: "categoryQuery.jhtml",
					type: "GET",
					data: params,
					dataType: "json",
					success: function(data) {
						var datas = data.commodityReportDtos;
						var startDate = getMyDate(data.startDate);
						var endDate = getMyDate(data.endDate);
						var quantity = 0;
						$(".orderTable tbody").html("");
						$("#goodDate").html("");
						if(datas) {
							$("#productSn").hide();
							$("#goodDate").html("("+startDate+"~"+endDate+")");
							$("#goodName").html("商品分类");
							for(var i=0;i<datas.length;i++) {
								quantity+=datas[i].orderQuantity;
								var fenxiao = '';
								if(datas[i].source != null && datas[i].source != ''){
									fenxiao='<span class="distribut_label">分销</span>';
								}
								$(".orderTable tbody").append('<tr>'+
										'<td class="hide"></td>'+
										'<td><label id="productName">'+datas[i].name+'</label>'+fenxiao+'</td>'+
										'<td><label id="orderNumber">'+datas[i].orderNumber+'</label></td>'+
										'<td><label id="orderQuantity">'+datas[i].orderQuantity+'</label></td>'+
										<!--'<td><label id="customersNum">'+datas[i].customersNum+'</label></td>'+-->
										'<td><label id="orderAmount">'+"￥"+datas[i].goodAmount+'</label></td>'+
									'</tr>');
							}
						}
						
						//大饼图数据
			    		var pieChart = [];
			    		var legendDat = [];
			    		$.each(data.commodityReportDtos,function(i, n){ 
			    			var name=n.name;
			    			if (n.specification != "[]") {
			    				name+=n.specification;
			    			}
			    			if (n.source != null && n.source != '') {
			    				name+="(分销)";
			    			}
			    			pieChart.push({name:name,value:n.orderQuantity});
			    			legendDat.push(name);
			    		});
			    		if(pieChart.length){
							$(".nodataCss").css("display","none");
						}else{
							$(".nodataCss").css("display","block");
						}
		    			var myChart = echarts.init(document.getElementById("main"));
		    			var color = color1;
						if(pieChart.length<10){color = color2;}
						chartsForm(legendDat,pieChart,quantity,color);
						myChart.setOption(option);

					}
				});
			});
			
			//按商品
			$("#byCommodity").on("click",function(){
				$("#exportType").val($(this).attr('val'));
				var status =[]; 
				$('input[name="status"]:checked').each(function(){ 
					status.push($(this).val()); 
				});  
				var params=[];
	    		params.push(
	    			{name:"ts",value:$("#ts").val()},
	    			{name:"startDate",value:$("#startDate").val()},
	    			{name:"endDate",value:$("#endDate").val()},
	    			{name:"status",value: status}
	    		);
				$.ajax({
					url: "orderForm.jhtml",
					type: "GET",
					data: params,
					dataType: "json",
					success: function(data) {
						var datas = data.commodityReportDtos;

						var startDate = getMyDate(data.startDate);
						var endDate = getMyDate(data.endDate);
						var quantity = 0;
						$(".orderTable tbody").html("");
						$("#goodDate").html("");
						if(datas) {
							$("#productSn").show();
							$("#goodDate").html("("+startDate+"~"+endDate+")");
							$("#goodName").html("商品名称");
							for(var i=0;i<datas.length;i++) {
								quantity+=datas[i].orderQuantity;
								var fenxiao = '';
								if(datas[i].source != null && datas[i].source != ''){
									fenxiao='<span class="distribut_label">分销</span>';
								}
								if(datas[i].specification == "[]") {
									$(".orderTable tbody").append('<tr>'+
										'<td><label id="productSn">'+datas[i].productSn+'</label></td>'+
											'<td><label id="productName">'+datas[i].name+'</label>'+fenxiao+'</td>'+
											'<td><label id="orderNumber">'+datas[i].orderNumber+'</label></td>'+
											'<td><label id="orderQuantity">'+datas[i].orderQuantity+'</label></td>'+
											<!--'<td><label id="customersNum">'+datas[i].customersNum+'</label></td>'+-->
											'<td><label id="orderAmount">'+"￥"+datas[i].orderAmount+'</label></td>'+
										'</tr>');
								}else {
								$(".orderTable tbody").append('<tr>'+
									'<td><label id="productSn">'+datas[i].productSn+'</label></td>'+
										'<td><label id="productName">'+datas[i].name+datas[i].specification+'</label>'+fenxiao+'</td>'+
										'<td><label id="orderNumber">'+datas[i].orderNumber+'</label></td>'+
										'<td><label id="orderQuantity">'+datas[i].orderQuantity+'</label></td>'+
										<!--'<td><label id="customersNum">'+datas[i].customersNum+'</label></td>'+-->
										'<td><label id="orderAmount">'+"￥"+datas[i].orderAmount+'</label></td>'+
									'</tr>');
								}
								
							}
						}
						
						
						//大饼图数据
			    		var pieChart = [];
						var legendDat = [];
			    		$.each(data.commodityReportDtos,function(i, n){ 
			    			var name=n.name;
			    			if (n.specification != "[]") {
			    				name+=n.specification;
			    			}
			    			if (n.source != null && n.source != '') {
			    				name+="(分销)";
			    			}
			    			pieChart.push({name:name,value:n.orderQuantity});
			    			legendDat.push(name);
			    		});
			    		
			    		if(pieChart.length){
							$(".nodataCss").css("display","none");
						}else{
							$(".nodataCss").css("display","block");
						}
		    			var myChart = echarts.init(document.getElementById("main"));
		    			var color = color1;
						if(pieChart.length<10){color = color2;}
						chartsForm(legendDat,pieChart,quantity,color);
						myChart.setOption(option);
					}
				});
			});
			
			$("#downButton").click(function () {
				$("#listForm").attr("action", "orderFormExport.jhtml");
				$("#listForm").submit();
		    });
			
			function information(){
				$.dialog({
		            title:"提示信息",
		            width:450,
		            height:230,
		            content:[@compress single_line = true]
					'<form id="reviewForm" class="form form-horizontal" action="" method="post">
						<div class="pag_div1" style="margin:50px 110px;">
							订货单统计只支持两个月数据统计，超过两个月的数据请导出查看
						<\/div>
					<\/form>'
					[/@compress],
					modal: true,
		            onShow:function(){},
		            onOk: function() {
		            	$("#listForm").attr("action", "orderFormExport.jhtml");
		            	$("#listForm").submit();
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
			
			/*通过js获取页面高度，来定义表单的高度*/
			var formHeight=$(document.body).height();
			
			
			$.Huitab("#tab-system .tabBar span","#tab-system .tabCon","current","click","0");
			
			$(".query_switch input").on("click",function(){
				$(this).siblings(".select_B").animate({left:$(this).css("left")},"slow");
				$(this).addClass("select_color").siblings().removeClass("select_color");
			});
			$(".switch_button input").on("click",function(){	
				$(this).siblings(".select_B").animate({left:$(this).css("left")},"slow");
				$(this).addClass("select_color").siblings().removeClass("select_color");
			});
			
			function timePlugin(startDate,endDate) {
				$("#div_date_demo3").remove();
			
				$("#endDate").after('<div class="ta_date" id="div_date_demo3">'+
			            '<span class="date_title" id="date_demo3"></span>'+
			            '<a class="opt_sel" id="input_trigger_demo3" href="#"></a>'+
			        '</div>');
				
				var dateRange = new pickerDateRange('date_demo3', {
	                aRecent7Days: 'aRecent7DaysDemo3', //最近7天
	                isTodayValid: true,
	                startDate : startDate,
	                endDate : endDate,
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
			
			
	        var color1 = ['#FE5F55','#E63946','#CA4236','#D96459','#ED7F2A','#F2A700','#FEC43F','#F2E394','#CBEAA6','#C0D684','#55BA7D','#94C9A9','#A8DADC','#8ABDDE','#457B9D','#006494','#314766','#814092','#A55BA4','#9897C7','#A5A7B7'];
	        var color2 = ['#FE5F55','#CA4236','#ED7F2A','#FEC43F','#C0D684','#94C9A9','#8ABDDE','#006494','#814092','#9897C7'];

			
			function chartsForm(legendDat,seriesData,quantity,color){
				option = {
				    tooltip: {
				        trigger: 'item',
				        formatter: "{a} <br/>{b}: {c} ({d}%)"
				    },
				    legend: {
				        orient: 'vertical',
				        x: '50%',
				        y: "10%",
				        data:legendDat,
				        formatter:function(name){
				            var value=0;
                            $.each(seriesData,function (i,item) {
                             	if(name == item.name){
                                    value=parseFloat((parseFloat((item.value/quantity).toFixed(4))*100).toFixed(2));
                             	}
                            });
                            return name+"  "+value+"%";
                        }
				    },
				    series: [
				        {
				            name:'所占百分比',
				            type:'pie',
				            radius: ['40%', '80%'],
				            center: ['30%', '50%'],
				            avoidLabelOverlap: false,
				            label: {
				                normal: {
				                    show: false,
				                    position: 'center'
				                },
				                emphasis: {
				                    show: false,
				                    textStyle: {
				                        fontSize: '20',
				                        fontWeight: 'bold'
				                    }
				                }
				            },
				            labelLine: {
				                normal: {
				                    show: false
				                }
				            },
				            data:seriesData
				        }
				    ], 
				    color:color
				    
				};
			}
			
			
			
			$("#goHome").on("click",function(){
				var nav = window.top.$(".index_nav_one");
        			nav.find("li li").removeClass('clickTo');
					nav.find("i").removeClass('click_border');
			})
			
			//tab致灰
            function tabGray(){
                $(".query_div1").find(".select_B").animate({"left":"-200px"},"slow");
                $(".query_switch input").removeClass("select_color");
            }
		});
	
	</script>

</body>
</html>
[/#escape]