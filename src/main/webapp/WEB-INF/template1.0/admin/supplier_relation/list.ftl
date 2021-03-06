[#assign shiro = JspTaglibs["/WEB-INF/tld/shiro.tld"] /]
[#escape x as x?html]
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>${message("admin.order.list")} - Powered By DreamForYou</title>
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
			.dialogContent {
            height: calc(100% - 80px);
            overflow: auto;
            overflow-x: hidden;
        }
		</style>
	</head>
	<body >
		<div class="child_page"><!--内容外面的大框-->	
			<div class="cus_nav">
				<ul>
					<li><a id="goHome"	href="../homePage/index.jhtml">${message("admin.breadcrumb.home")}</a></li>
					<li>供应商管理列表<span>(${message("admin.page.total", page.total)})</span></li>
				</ul>
			</div>
		<form id="listForm" action="list.jhtml" method="get">
			<input type="hidden" id="type" name="type" value="${type}" />
			
			<div class="ch_condition">
				
				[#--<div class="require_search" id="filterMenu">
					<span class="search">${message("admin.order.filter")}</span>
					<ul class="check">
						<li name="clientType" val="">全部类型</li>
						<li name="clientType"[#if "enterprise" == clientType] class="checked"[/#if] val="enterprise">企业</li>
                        <li name="clientType"[#if "individual" == clientType] class="checked"[/#if] val="individual">个体</li>
					</ul>
				</div>--]
				
				<div class="ch_search">
					<img class="search_img" src="${base}/resources/admin1.0/images/sousuo_icon.svg" alt="" />
					<div class="search_input">
						<input type="text" id="searchName" name="searchName" value="${searchName}" maxlength="200" placeholder="输入供应商名称/编号/联系人" />

					</div>
				</div>
				<div>
					<input class="timeType" type="text" readonly="" value="创建时间" />
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
					[#--<button type="button" class="op_button add_B" onclick="add();">${message("admin.common.add")}</button>
					<button type="button" class="op_button del_B disabled" id="deleteButton">${message("admin.common.delete")}<button>--]
					<button type="button" class="op_button update_B" id="refreshButtons" onclick="javascript:window.location.href='list.jhtml'">${message("admin.common.refresh")}</button>
				</div>
			</div>
			
			<div class="table_con">
				<table class="table table-border table-hover table_width">
					<thead>
						<tr class="text-l">
							[#--<th width="4%"><input class="selectAll" type="checkbox" id="selectAll" id="selectAll"></th>--]
							<th width="15%">${message("CustomerRelation.supplierName")}</th>
							<th width="8%">${message("CustomerRelation.supplierCode")}</th>
							<th width="8%">${message("CustomerRelation.supplierArea")}</th>
							<th width="10%">${message("CustomerRelation.supplierUserName")}</th>
							<th width="10%">${message("CustomerRelation.supplierTel")}</th>
							<th width="10%">${message("CustomerRelation.createDate")}</th>
							<th width="8%">${message("admin.common.action")}</th>
						</tr>
					</thead>
				</table>
				<div class="list_t_tbody" id="listTable">
					<table class="table table-border table-hover table_width">
						<thead>
                        <tr class="text-l">
						[#--<th width="4%" style="">
                            <div class="th_div" style="">
                                <input class="selectAll" type="checkbox" id="selectAll">
                            </div>
                        </th>--]
							<th width="15%"><div class="th_div"></div></th>
                            <th width="8%"><div class="th_div"></div></th>
                            <th width="8%"><div class="th_div"></div></th>
                            <th width="10%"><div class="th_div"></div></th>
                            <th width="10%"><div class="th_div"></div></th>
                            <th width="10%"><div class="th_div"></div></th>
                            <th width="8%"><div class="th_div"></div></th>
                        </tr>
						</thead>
						<tbody>
						[#list page.content as supplierRelation]
							<tr class="text-l">
								[#--<td><input type="checkbox" name="ids" value="${supplierRelation.id}"></td>--]
								<td>${supplierRelation.supplier.name}</td>
								<td>${supplierRelation.supplierCode!"-"}</td>
								<td>${supplierRelation.supplierArea.fullName!supplierRelation.supplier.area.fullName}</td>
								<td>${supplierRelation.supplierUserName!supplierRelation.supplier.userName}</td>
								<td>${supplierRelation.supplierTel!supplierRelation.supplier.tel}</td>
								<td><span title="${supplierRelation.createDate?string("yyyy-MM-dd HH:mm:ss")}">${supplierRelation.createDate?string("yyyy-MM-dd HH:mm:ss")}</span></td>
								<td class="td-manage">
									[@shiro.hasPermission name = "admin:supplierRelation:edit"]
										<a title="${message("admin.common.edit")}" href="edit.jhtml?id=${supplierRelation.id}" class="ml-5" style="text-decoration:none"><i class="operation_icon icon_bianji"></i></a>
									[/@shiro.hasPermission]
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
	
			
				/*表格的高度，，随着电脑分倍率的变化而变化*/
				var heightObj = $(document.body).height() - 170;
				$(".list_t_tbody").css("height",heightObj);
				
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
		
			
			
			$("#goHome").on("click",function(){
				var nav = window.top.$(".index_nav_one");
        			nav.find("li li").removeClass('clickTo');
					nav.find("i").removeClass('click_border');
			})
	      });
	        
	        function add() {
        		window.location.href="add.jhtml";
        	}
		</script>
	</body>
</html>
[/#escape]