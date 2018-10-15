[#escape x as x?html]
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>${message("admin.admin.list")} - Powered By DreamForYou</title>
		<meta http-equiv="X-UA-Compatible" content="IE=edge" />
		<link rel="stylesheet" type="text/css" href="${base}/resources/admin1.0/H-ui/static/h-ui/css/H-ui.min.css" />
		<link rel="stylesheet" type="text/css" href="${base}/resources/admin1.0/H-ui/static/h-ui.admin/css/H-ui.admin.css" />
		<link rel="stylesheet" href="${base}/resources/admin1.0/js/date/dateRange.css" />
		<link rel="stylesheet" type="text/css" href="${base}/resources/admin1.0/js/kkpager/kkpager_blue.css" />
		<!--<link rel="stylesheet" href="${base}/resources/admin/css/common.css" type="text/css" />-->
		<link rel="stylesheet" href="${base}/resources/admin1.0/css/public.css" />
		<link rel="stylesheet" href="${base}/resources/admin1.0/css/admin.css" />
		
		<style>
			body{background:#f9f9f9;}
			.supplyType{display:block;width:50px;height:22px;background:red;text-align: center;line-height: 22px;border-radius: 5px;}
			.SUPPLY{background:#d2f0e6;color:#00b45a;}/*供应中*/
			.ygq{background:#dce6eb;color:#7882e0}/*已过期*/
			.STOP{background:#dce6eb;color:#7882e0}/*暂停中*/
			.yfp{background:#d2f0e6;color:#00b45a}/*已分配*/
			.wks{background:#faf0b4;color:#be965a}/*未开始*/
		</style>
	</head>
	<body >
		<div class="child_page"><!--内容外面的大框-->	
			<div class="cus_nav">
				<ul>
					<li><a id="goHome"	href="../homePage/index.jhtml">${message("admin.breadcrumb.home")}</a></li>
					<li>临时供应列表 <span>(${message("admin.page.total", page.total)})</span></li>
				</ul>
			</div>
		<form id="listForm" action="list.jhtml" method="get">
			<input type="hidden" id="status" name="status" value="${status}">
			<div class="ch_condition">
			
				<div class="require_search" id="filterMenu">
					<span class="search">${message("admin.order.filter")}</span>
					<ul class="check">
						<li name="status" val="">所有状态</li>
						<li name="status"[#if "SUPPLY" == status] class="checked"[/#if] val="SUPPLY">供应中</li>
                        <li name="status"[#if "STOP" == status] class="checked"[/#if] val="STOP">暂停中</li>
                        					
					</ul>
				</div>
				<div class="ch_search">
					<img class="search_img" src="${base}/resources/admin1.0/images/sousuo_icon.svg" alt="" />
					<div class="search_input">
						<input type="text" id="searchName" name="searchName" value="${searchName}" maxlength="200" placeholder="请输入客户名称/收货点" />

					</div>
				</div>
				<button type="submit" class="search_button">查询</button>
	
				<div class="ch_operate">
					<button type="button" class="op_button add_B" onclick="add();">${message("admin.common.add")}</button>
					<button type="button" class="op_button del_B disabled" id="deleteButton">${message("admin.common.delete")}<button>
					<button type="button" class="op_button update_B" id="refreshButtons" onclick="javascript:window.location.href='list.jhtml'">${message("admin.common.refresh")}</button>
				</div>
			</div>
			
			<div class="table_con">
				<table class="table table-border table-hover table_width">
					<thead>
						<tr class="text-l">
							<th width="5%"><input class="all_checked" type="checkbox" id="selectAll"></th>
							<th width="24%">客户名称</th>
							<th width="24%">收货点名称</th>
							<th width="18%">添加时间</th>
							<th width="19%">供应状态</th>
							<th width="10%">${message("admin.common.action")}</th>
						</tr>
					</thead>
				</table>
				<div class="list_t_tbody" id="listTable">
					<table class="table table-border table-hover table_width">
						<thead>
							<tr class="text-l">
								<th width="5%" style="">
									<div class="th_div" style="">
										<input class="all_checked" type="checkbox">
									</div>
								</th>
								<th width="24%"><div class="th_div">企业名称</div></th>
								<th width="24%"><div class="th_div">收货点</div></th>
								<th width="18%"><div class="th_div">添加时间</div></th>
								<th width="19%"><div class="th_div">供应状态</div></th>
								<th width="10%"><div class="th_div">${message("admin.common.action")}</div></th>
							</tr>
						</thead>
						<tbody>
							[#list page.content as supplyNeed]
							<tr class="text-l">
								<td><input type="checkbox" name="ids" value="${supplyNeed.id}"></td>
								<td>${supplyNeed.need.supplier.name}</td>
								<td>${supplyNeed.need.name}</td>
								<td>
									<span title="${supplyNeed.need.createDate?string("yyyy-MM-dd HH:mm:ss")}">${supplyNeed.need.createDate}</span>
								</td>
								<td>
									<span class="supplyType ${supplyNeed.status}">${message("SupplyNeed.Status." + supplyNeed.status)}</span>
									[#--<span class="supplyType ygq">${message("SupplyNeed.Status." + supplyNeed.status)}</span>
									<span class="supplyType ygq">${message("SupplyNeed.Status." + supplyNeed.status)}</span>
									<span class="supplyType ygq">${message("SupplyNeed.Status." + supplyNeed.status)}</span>--]
								
								</td>
								<td class="td-manage">
									[#if supplyNeed.status == "STOP"]
				                        <a title="开启供应" href="javascript:void(0);" onclick="javascript:updateStatus('SUPPLY' , ${supplyNeed.id});"><i class="operation_icon icon_start" style="margin-bottom:-3px;"></i></a>
										[#elseif supplyNeed.status == "SUPPLY"]
				                            <a title="暂停供应" href="javascript:void(0);" onclick="javascript:updateStatus('STOP', ${supplyNeed.id});"><i class="operation_icon icon_stop" style="margin-bottom:-3px;"></i></a>
									[/#if]
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
		<script type="text/javascript" src="${base}/resources/admin1.0/js/kkpager/kkpager.js"></script><!--分页-->
		<script type="text/javascript" src="${base}/resources/admin1.0/H-ui/lib/layer/2.4/layer.js"></script>
		<script type="text/javascript" src="${base}/resources/admin1.0/H-ui/static/h-ui/js/H-ui.min.js"></script> 
		<script type="text/javascript" src="${base}/resources/admin1.0/H-ui/static/h-ui.admin/js/H-ui.admin.js"></script>
		<script type="text/javascript" src="${base}/resources/admin1.0/H-ui/lib/datatables/1.10.0/jquery.dataTables.min.js"></script> 
		<script type="text/javascript" src="${base}/resources/admin1.0/H-ui/lib/laypage/1.2/laypage.js"></script>
		<script type="text/javascript" src="${base}/resources/admin1.0/js/common.js"></script>
		<script type="text/javascript" src="${base}/resources/admin1.0/js/list.js"></script>
		
		<script type="text/javascript">
			
			/*表格的高度，，随着电脑分倍率的变化而变化*/
			var heightObj = $(document.body).height() - 170;
			$(".list_t_tbody").css("height",heightObj);
			$(".table_width").css("width", $(".list_t_tbody").css("width")); 
			
			function add() {
				window.location.href="add.jhtml";
			}
		
			$().ready(function() {
				var $listForm = $("#listForm");
	        	var $filterMenu = $("#filterMenu");
	        	var $filterMenuItem = $("#filterMenu li");
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

			});
			function updateStatus(type , id){
			    $.dialog({
			        type: "warn",
			        content: "是否要修改供应状态",
			        onOk: function() {
			            $.ajax({
			                url: "updateStatus.jhtml",
			                type: "GET",
			                data: {status:type , id:id},
			                dataType: "json",
			                cache: false,
			                success: function(message) {
			                    $.message(message);
			                    if (message.type == "success") {
			                        setTimeout(function () {
			                            location.reload(true);
			                        }, 3000);
			                    }
			                }
			            });
			        }
			    });
			}
			
			
			$("#goHome").on("click",function(){
				var nav = window.top.$(".index_nav_one");
        			nav.find("li li").removeClass('clickTo');
					nav.find("i").removeClass('click_border');
			})
		</script>
	</body>
</html>
[/#escape]