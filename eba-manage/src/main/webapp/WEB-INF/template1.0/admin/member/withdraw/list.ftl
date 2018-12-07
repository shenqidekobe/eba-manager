[#assign shiro = JspTaglibs["/WEB-INF/tld/shiro.tld"] /]
[#escape x as x?html]
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>${message("admin.order.list")} - Powered By microBusiness</title>
		<meta http-equiv="X-UA-Compatible" content="IE=edge" />
		<link rel="stylesheet" type="text/css" href="${base}/resources/admin1.0/H-ui/static/h-ui/css/H-ui.min.css" />
		<link rel="stylesheet" type="text/css" href="${base}/resources/admin1.0/H-ui/static/h-ui.admin/css/H-ui.admin.css" />
		<link rel="stylesheet" href="${base}/resources/admin1.0/js/date/dateRange.css" />
		<link rel="stylesheet" type="text/css" href="${base}/resources/admin1.0/js/kkpager/kkpager_blue.css" />
		<link rel="stylesheet" href="${base}/resources/admin1.0/css/public.css" />
		<link rel="stylesheet" href="${base}/resources/admin1.0/css/admin.css" />
		<style>
			body{background:#f9f9f9;}
			.search_button{margin-left:36px;}
			.timeType[type="text"]{float:left;border:0;width:60px;height:30px;background:#f9f9f9;}
			.proxyUserType[type="text"]{float:left;border:0;width:60px;height:30px;background:#f9f9f9;}
			.dialogContent {
	            height: calc(100% - 80px);
	            overflow: auto;
	            overflow-x: hidden;
	        }
	        .ch_time .ta_date .date_title,.ch_time .ta_date .opt_sel,.ch_time .ta_date,.ch_time .chooseTime{height:30px;}
			.ch_time .chooseTime{line-height: 30px;}
			.drop_down:hover .check{
				display:block;
			}
			.daochuDown .check{
				left:-50px;
			}
			.form-horizontal .form-label{width:160px;}
			.check-box i{color:#999;font-size:12px;padding-left:20px;}
		</style>
	</head>
	<body >
		<div class="child_page"><!--内容外面的大框-->	
			<div class="cus_nav">
				<ul>
					<li><a id="goHome"	href="../homePage/index.jhtml">${message("admin.breadcrumb.home")}</a></li>
					<li>提现列表<span>(${message("admin.page.total", page.total)})</span></li>
				</ul>
			</div>
		<form id="listForm" action="list.jhtml" method="get">
			<input type="hidden" id="status" name="status" value="${status}" />
			<!-- 为了多统计页面多状态分页 -->
			<input type="checkbox" class="input-text radius hide" [#if await??] checked="checked" [/#if] name="statuses" value="await"/>
			<input type="checkbox" class="input-text radius hide" [#if complete??] checked="checked" [/#if] name="statuses" value="complete"/>
			<input type="checkbox" class="input-text radius hide" [#if fail??] checked="checked" [/#if] name="statuses" value="fail"/>
			<input type="checkbox" class="input-text radius hide" [#if repeal??] checked="checked" [/#if] name="statuses" value="repeal"/>
			
			<div class="ch_condition">
				
				<div class="require_search" id="filterMenu">
					<span class="search">${message("admin.order.filter")}</span>
					<ul class="check">
						<li name="status" val="">所有状态</li>
						<li name="status"[#if "await" == status] class="checked"[/#if] val="await">等待审核</li>
                        <li name="status"[#if "complete" == status] class="checked"[/#if] val="complete">已完成</li>
                        <li name="status"[#if "fail" == status] class="checked"[/#if] val="fail">操作失败</li>
                        <li name="status"[#if "repeal" == status] class="checked"[/#if] val="repeal">已取消</li>
					</ul>
				</div>
				
				<div class="ch_search">
					<img class="search_img" src="${base}/resources/admin1.0/images/sousuo_icon.svg" alt="" />
		
					<div class="search_input">
						<input type="text" id="searchName" name="searchName" value="${searchName}" maxlength="200" placeholder="请输入" />
					</div>
				</div>
				<div>
					<div class="drop_down" id="dropDown">
						<span class="timeType">提现时间</span>
						<ul class="check">
							<li name="timeSearch"[#if "createTime" == timeSearch] class="checked"[/#if] val="createTime">提现时间</li>
							<li name="timeSearch"[#if "processTime" == timeSearch] class="checked"[/#if] val="processTime">处理时间</li>
						</ul>
					</div>
					<div class="ch_time">
						<span class="chooseTime">${(startDate?string("yyyy-MM-dd"))!}~${(endDate?string("yyyy-MM-dd"))!}</span>
						<input type="hidden" class="startTime" id="startDate" name="startDate" value="${(startDate?string("yyyy-MM-dd"))!}"/>
						<input type="hidden" class="endTime" id="endDate" name="endDate" value="${(endDate?string("yyyy-MM-dd"))!}"/>
						<div class="ta_date" id="div_date_demo3">
				            <span class="date_title" id="date_demo3"></span>
				            <a class="opt_sel" id="input_trigger_demo3" href="#"></a>
				        </div>
					</div>		
				</div>
				
				
				
				
				
				<button type="submit" class="search_button">查询</button>
				<div class="ch_operate">
					<!--<button type="button" class="op_button del_B disabled" id="deleteButton">${message("admin.common.delete")}<button>-->
					<button type="button" class="op_button update_B" id="refreshButtons" onclick="javascript:window.location.href='list.jhtml'">${message("admin.common.refresh")}</button>
					<!--[@shiro.hasPermission name = "admin:withdraw:getOut"]
						<div class="daochuDown" id="export">
							<button type="button" class="op_button daochu_B" id="downButton">导出</button>
							<ul class="check">
								<li name="export" class="batch" val="batch">导出批量订单</li>
								<li name="export" class="selected" val="selected">导出选中订单</li>
							</ul>
						</div>
					[/@shiro.hasPermission]-->
				</div>
			</div>
			
			<div class="table_con">
				<table class="table table-border table-hover table_width">
					<thead>
						<tr class="text-l">
							<th width="4%"><input class="selectAll" type="checkbox" id="selectAll" id="selectAll"></th>
							<th width="15%">编号</th>
							<th width="11%">提现金额</th>
							<th width="13%">提现账户</th>
							<th width="14%">账户姓名</th>
							<th width="10%">状态</th>
							<th width="14%">申请时间</th>
							<th width="6%">${message("admin.common.action")}</th>
						</tr>
					</thead>
				</table>
				<div class="list_t_tbody" id="listTable">
					<table class="table table-border table-hover table_width">
						<thead>
							<tr class="text-l">
								<th width="4%" style="">
									<div class="th_div" style="">
										<input class="selectAll" type="checkbox" id="selectAll">
									</div>
								</th>
								<th width="15%"><div class="th_div">编号</div></th>
								<th width="11%"><div class="th_div">提现金额</div></th>
								<th width="13%"><div class="th_div">提现账户</div></th>
								<th width="14%"><div class="th_div">账户姓名</div></th>
								<th width="10%"><div class="th_div">状态</div></th>
								<th width="14%"><div class="th_div">申请时间</div></th>
								<th width="6%"><div class="th_div">${message("admin.common.action")}</div></th>
							</tr>
						</thead>
						<tbody>
						[#list page.content as order]
							<tr class="text-l">
								<td><input type="checkbox" name="ids" value="${order.id}"></td>
								<td>
									${order.sn}
								</td>
								<td>
									${currency(order.amount, true)}
								</td>
								<td>
									${order.account}
								</td>
								<td>${order.accountName}</td>
								<td>
									${order.status.label}
								</td>
								<td><span title="${order.createDate?string("yyyy-MM-dd HH:mm:ss")}">${order.createDate?string("yyyy-MM-dd HH:mm:ss")}</span></td>

								<td class="td-manage">
									<a title="${message("admin.common.view")}" href="edit.jhtml?id=${order.id}" class="ml-5" style="text-decoration:none"><i class="operation_icon icon_see"></i></a>
								</td>
							</tr>
						[/#list]
						</tbody>
					</table>
				</div>
			</div>
			[@pagination pageNumber = page.pageNumber totalPages = page.totalPages]
			[#include "/admin/include/pagination.ftl"]
			[/@pagination]
		</form>
		</div>
		<script src="${base}/resources/admin1.0/js/jquery.min.js"></script>
		<script src="${base}/resources/admin1.0/js/date/dateRange.js"></script><!--时间控件-->
		<script type="text/javascript" src="${base}/resources/admin1.0/js/common.js"></script>
        <script type="text/javascript" src="${base}/resources/admin1.0/js/list.js"></script>
        
        <script type="text/javascript">
	        $().ready(function() {
	        	
	        	var $listForm = $("#listForm");
	        	var $filterMenu = $("#filterMenu");
	        	var $filterMenuItem = $("#filterMenu li");
	        	var $moreButton = $("#moreButton");
	        	var $print = $("#listTable select[name='print']");
	        	
	        	[@flash_message /]
	        	
	        	// 筛选
	        	$filterMenuItem.click(function() {
	        		var $this = $(this);
	        		var $dest = $("#" + $this.attr("name"));
	        		if ($this.hasClass("checked")) {
	        			$dest.val("");
	        		} else {
	        			$dest.val($this.attr("val"));
	        		}
	        		$listForm.submit();
	        	});
	        	
	        	var checkedDom =  $("#filterMenu li.checked");
                var firstDom;
                var firstText;
				if(checkedDom.length == 0){
                    firstDom = $("#filterMenu").find("li:eq(0)");
                    firstText = firstDom.html();
                    firstDom.addClass("checked");
				}else{
                    firstText = checkedDom.html();
                }
                $(".search").html(firstText);
	        	
	        	/*表格的高度，，随着电脑分倍率的变化而变化*/
				var heightObj = $(document.body).height() - 170;
				$(".list_t_tbody").css("height",heightObj);
				$(".table_width").css("width", $(".list_t_tbody").css("width")); 
	        	
				/*  按时间搜索 */
                $("#dropDown li").click(function(){
                	var $this = $(this);
                	var $dest = $("#" +$this.attr("name"));
                	$dest.val($this.attr("val"));
                });
				var checkedDown = $("#dropDown li.checked");
				var firstDown;
				var firstDownTest;
				if(checkedDown.length == 0) {
					firstDown = $("#dropDown").find("li:eq(0)");
					firstDownTest = firstDown.html();
					firstDown.addClass("checked");
				}else{
					firstDownTest = checkedDown.html();
				}
				$(".timeType").html(firstDownTest); 
				
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
				
				
	        	/*搜索条件*/
	        	$(".require_search li").on("click",function(){
	        		$(this).parent().siblings(".search").html($(this).html());
	        		//$(this).addClass("li_bag").siblings().removeClass("li_bag");
	        		$(".check").css("display","none");
	        	});
	        	$(".require_search").mouseover(function(){
					$(this).find("ul").css("display","block");
				});
				$(".require_search").mouseout(function(){
					$(this).find("ul").css("display","none");
				});
				
				/*时间类型*/
    			$("#dropDown li").on("click",function(){
            		$(this).parent().siblings(".timeType").html($(this).html());
            		$(".check").css("display","none");
            	});
            	$("#dropDown").mouseover(function(){
    				$(this).find("ul").css("display","block");
    			});
    			$("#dropDown").mouseout(function(){
    				$(this).find("ul").css("display","none");
    			});
    			
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
	        	
	        	/*导出*/
    			$(".daochuDown li").on("click",function(){
            		$(this).parent().siblings(".timeType").html($(this).html());
            		$(".check").css("display","none");
            	});
            	$(".daochuDown").mouseover(function(){
    				$(this).find("ul").css("display","block");
    			});
    			$(".daochuDown").mouseout(function(){
    				$(this).find("ul").css("display","none");
    			});
	        	
	        	
	        	var $print = $("#listTable select[name='print']");
	        	
	        	[@flash_message /]
			
			/*表格的高度，，随着电脑分倍率的变化而变化*/
			var heightObj = $(document.body).height() - 170;
			$(".list_t_tbody").css("height",heightObj);
			
			/* $("#downButton").click(function () {
				$("#listForm").attr("action", "getOutDownload.jhtml");

				$("#listForm").submit();

				$("#listForm").attr("action", "list.jhtml");

		    }); */
			
			//导出
            $("#export li").click(function(){
				var $this = $(this);
				var $exp = $this.attr("val");
				var ids = [];
				$("input[name='ids']:checked").each(function(){
					ids.push($(this).val());
				});
				if($exp == "batch") {
					/*$listForm.attr("action", "download.jhtml");

                    $listForm.submit();

                    $listForm.attr("action", "list.jhtml");
                    return true;*/
				}
				if($exp == "selected") {
					/*if(ids.length == 0) {
						$.message({'type':'error' , 'content':'请选择至少一条记录'});
						return false;
					}
					$listForm.attr("action", "exportSelectedOrder.jhtml?ids="+ids);

                    $listForm.submit();

                    $listForm.attr("action", "list.jhtml");
                    return true;*/
				}
				if($exp == "report") {
					$listForm.attr("action", "getOutReportDownload.jhtml");

                    $listForm.submit();

                    $listForm.attr("action", "list.jhtml");
                    return true;
				}
				if($exp == "selectedReport") {
					if(ids.length == 0) {
						$.message({'type':'error' , 'content':'请选择至少一条记录'});
						return false;
					}
					$listForm.attr("action", "getOutSelectedReport.jhtml");

                    $listForm.submit();

                    $listForm.attr("action", "list.jhtml");
                    return true;
				}
				
			});
			
			
			// 打印选择
			$print.change(function() {
				var $this = $(this);
				if ($this.val() != "") {
					window.open($this.val());
				}
			});

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
                    $(".chooseTime").html( obj.startDate + "～" + obj.endDate);
                    $(".startTime").val(obj.startDate);
                    $(".endTime").val(obj.endDate);
                }
            });
			
			//批量审核
			$("#batchReview").click(function(){
				var check_value = [];
				$('input[name="ids"]:checked').each(function(){ 
					check_value.push($(this).val()); 
				});
				if(check_value.length == 0) {
					$.message("warn", "请选择至少一条记录！");
					return false;
				}
				var checkReview = true;
				$.ajax({
	                url: "checkBatchReview.jhtml",
	                type: "POST",
	                async: false,
	                data: {orderIds: check_value},
	                dataType: "json",
	                cache: false,
	                success: function(message) {
	                    if (message.type != "success") {
	                        $.message(message);
	                        checkReview = false;

	                    }
	                }
	            });
				if(checkReview == true) {
		        $.dialog({
		            title:"批量审核",
		            width:600,
		            height:300,
		            content:[@compress single_line = true]
					'<form id="reviewForm" class="form form-horizontal" action="batchReview.jhtml" method="post">
					    <input type="hidden" name="token" value="${token}" \/>
					    <div id="hideInput"><\/div>
						<div class="pag_div1" style="margin-top:20px;">
							<div class="row cl">
								<label class="form-label col-xs-4 col-sm-3">审核<\/label>
								<div class="formControls col-xs-8 col-sm-7">
									<div class="check-box">
				                         <input type="radio" name="passed" value="true" checked="checked" class="passedClass" \/><span style="margin-right:30px">通过<\/span>
										<input type="radio" name="passed" value="false" class="passedClass" \/><span>不通过<\/span>
				                    <\/div>
								<\/div>
							<\/div>
							<div id="passedTrue">

							<\/div>
							<div id="passedFalse" style="display:none">
								<div class="row cl">
									<label class="form-label col-xs-4 col-sm-3">原因<\/label>
									<div class="formControls col-xs-8 col-sm-7">
					                    <textarea name="deniedReason" class="text_area1"><\/textarea>
									<\/div>
								<\/div>
							<\/div>
						<\/div>
					<\/form>'
					[/@compress],
					modal: true,
		            onShow:function(){
		                $(".passedClass").click(function(){

		                    $("#passedTrue").hide();
		                    $("#passedFalse").hide();

		                    if($(this).val() == "true"){
		                        $("#passedTrue").show();
		                    }
		                    if($(this).val() == "false"){
		                        $("#passedFalse").show();
		                    }
		                });
		                
		                var index = 0;
		                $.each(check_value,function(key,value) {
		                    $("#hideInput").append('<input type="hidden" name="orderIds" value="'+value+'" \/>');
		                    index ++ ;
		                });

		            },
		            onOk: function() {
		            	var $reviewForm = $("#reviewForm");
		                $reviewForm.submit();
		                return false ;
		               
		            }
		        });
		        
				}
		    });
			
			//批量打印
		    $("#batchPrint").click(function() {
		    	var orderIds = [];
				$('input[name="ids"]:checked').each(function(){ 
					orderIds.push($(this).val()); 
				});
				if(orderIds.length == 0) {
					$.message("warn", "请选择至少一条记录！");
					return false;
				}
				var checkReview = true;
				$.ajax({
	                url: "../print/verificationDeliveryInfo.jhtml",
	                type: "POST",
	                async: false,
	                data: {orderIds: orderIds},
	                dataType: "json",
	                cache: false,
	                success: function(message) {
	                    if (message.type != "success") {
	                        $.message(message);
	                        checkReview = false;

	                    }
	                }
	            });
				if(checkReview == false) {
					return false;
				}
		    	var href="../print/orderBatchPrint.jhtml?orderIds="+orderIds;
		    	window.open(href); 
		    });
			
			/*批量导出*/
			$(".batch").on("click",function(){
				
				$.dialog({
		            title:"批量导出",
		            width:600,
		            height:280,
		            content:[@compress single_line = true]
					'<form id="reviewForm" class="form form-horizontal" action="" method="post">
						<div class="pag_div1" style="margin:40px 100px;">
							<div class="check-box" style="margin-bottom:20px;">
				                <input type="radio" name="batchExport" value="mergeExport" checked="checked" \/><span style="margin-right:30px">订货单合并导出<i>不同客户订单在同一个sheet中<\/i><\/span>
				            <\/div>
				            <div class="check-box">
				                <input type="radio" name="batchExport" value="splitOut" \/><span style="margin-right:30px">订货单拆分导出<i>不同客户订单在单独的sheet中<\/i><\/span>
				            <\/div>
						<\/div>
					<\/form>'
					[/@compress],
					modal: true,
		            onShow:function(){},
		            onOk: function() {
		            	var batchExport = $("input[name = 'batchExport']:checked").val();
		            	if(batchExport == "mergeExport") {
		            		$listForm.attr("action", "getOutDownload.jhtml");

		                    $listForm.submit();

		                    $listForm.attr("action", "list.jhtml");
		                    return;
		            	}else {
		            		$listForm.attr("action", "getOutSplitOut.jhtml");

		                    $listForm.submit();

		                    $listForm.attr("action", "list.jhtml");
		                    return;
		            	}
		            	
		            }
		        });	
			})
			/*导出选中订单*/
			$(".selected").on("click",function(){
				$.dialog({
		            title:"导出选中订单",
		            width:600,
		            height:280,
		            content:[@compress single_line = true]
					'<form id="reviewForm" class="form form-horizontal" action="batchReview.jhtml" method="post">
						<div class="pag_div1" style="margin:40px 100px;">
							<div class="check-box" style="margin-bottom:20px;">
				                <input type="radio" name="selectExport" value="mergeExport" checked="checked" \/><span style="margin-right:30px">订货单合并导出<i>不同客户订单在同一个sheet中<\/i><\/span>
				            <\/div>
				            <div class="check-box">
				                <input type="radio" name="selectExport" value="splitOut" \/><span style="margin-right:30px">订货单拆分导出<i>不同客户订单在单独的sheet中<\/i><\/span>
				            <\/div>
						<\/div>
					<\/form>'
					[/@compress],
					modal: true,
		            onShow:function(){},
		            onOk: function() {
		            	var ids = [];
						$("input[name='ids']:checked").each(function(){
							ids.push($(this).val());
						});
		            	if(ids.length == 0) {
							$.message({'type':'error' , 'content':'请选择至少一条记录'});
							return;
						}
		            	var batchExport = $("input[name = 'selectExport']:checked").val();
		            	if(batchExport == "mergeExport") {
		            		$listForm.attr("action", "getOutExportSelectedOrder.jhtml?ids="+ids);

		                    $listForm.submit();

		                    $listForm.attr("action", "list.jhtml");
		                    return;
		            	}else {
		            		$listForm.attr("action", "getOutSelectSplitExport.jhtml?ids="+ids);

		                    $listForm.submit();

		                    $listForm.attr("action", "list.jhtml");
		                    return;
		            	}
		            	
		            }
		        });	
			})
			
			var whetherCertify = $("#whetherCertify").val();
			var useExpired = $("#useExpired").val();
			var supplierStatic = $("#supplierStatic").val();
			if(whetherCertify == "true") {
				if(useExpired == "true") {
					$.dialog({
		                title:"企业试用过期提醒",
		                ok:"去认证",
		                height:190,
		                content: "企业已经超过试用期限，请做企业认证继续使用！",
		                onOk: function() {
		                	if(supplierStatic == "notCertified") {
		                		window.location.href="../certification/index.jhtml";
		                	}
		                	if(supplierStatic == "certification") {
		                		window.location.href="../certification/reviewing.jhtml";
		                	}
		                	if(supplierStatic == "authenticationFailed") {
		                		//window.location.href="../certification/reviewMiss.jhtml";
		                		window.location.href="../certification/index.jhtml";
		                	}
		                },
		                onCancel: function() {
		                	window.history.go(-1);
		                },
		                onShow:function(){
		                	$(".xxDialog").css("top","150px");
		                	$(".dialogContent").css({"line-height":"110px","text-align":"center"});
		                }
	            	});
				}
			}
			
			
			$("#goHome").on("click",function(){
				var nav = window.top.$(".index_nav_one");
        			nav.find("li li").removeClass('clickTo');
					nav.find("i").removeClass('click_border');
			})
                $("body").click(function(){
                    window.top.$(".show_news").removeClass("show");
                })
	      });
		</script>
	</body>
</html>
[/#escape]