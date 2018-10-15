[#assign shiro = JspTaglibs["/WEB-INF/tld/shiro.tld"] /]
[#escape x as x?html]
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>${message("admin.admin.list")} - Powered By DreamForYou</title>
		<meta http-equiv="X-UA-Compatible" content="IE=edge" />
		<link rel="stylesheet" type="text/css" href="${base}/resources/admin1.0/H-ui/static/h-ui/css/H-ui.min.css" />
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
			}
			.ch_search{width:260px;}
			.ch_search .search_input{width:220px;}
			.ch_search .search_input input{width:220px;}
		</style>
	</head>
	<body >
		<div class="child_page"><!--内容外面的大框-->	
			<div class="cus_nav">
				<ul>
					<li><a id="goHome" href="../homePage/index.jhtml">${message("admin.breadcrumb.home")}</a></li>
					<li>${message("admin.need.list")} <span>(${message("admin.page.total", page.total)})</span></li>
				</ul>
			</div>
		<form id="listForm" action="list.jhtml" method="get">
			<div class="ch_condition">
				<div class="ch_search">
					<img class="search_img" src="${base}/resources/admin1.0/images/sousuo_icon.svg" alt="" />
					<div class="search_input">
						<input type="text" id="searchName" name="searchName" value="${searchName}" maxlength="200" placeholder="输入门店名称/门店编号/收货人/业务员" />
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
					[@shiro.hasPermission name = "admin:need:add"]
						<button type="button" class="op_button add_B" onclick="add();">${message("admin.common.add")}</button>
					[/@shiro.hasPermission]
					[@shiro.hasPermission name = "admin:need:importMore"]
						<button type="button" class="op_button daoru_B" onclick="importData()">导入</button>
					[/@shiro.hasPermission]
					<button type="button" class="op_button update_B" id="refreshButtons" onclick="javascript:window.location.href='list.jhtml'">${message("admin.common.refresh")}</button>
					[@shiro.hasPermission name = "admin:need:delete"]
					<button type="button" class="op_button del_B disabled" id="deleteButton">${message("admin.common.delete")}<button>
						[/@shiro.hasPermission]
				</div>
			</div>
			
			<div class="table_con">
				<table class="table table-border table-hover table_width">
					<thead>
						<tr class="text-l">
							<th width="4%"><input class="selectAll" type="checkbox" id="selectAll" id="selectAll"></th>
							<th width="8%">${message("Need.clientName")}</th>
							<th width="5%">${message("Need.type")}</th>
							<th width="7%">${message("Need.clientNum")}</th>
							<th width="5%">业务员</th>
							<th width="10%">${message("Need.area")}</th>
							<th width="8%">${message("Need.userName")}</th>
							<th width="8%">联系方式</th>
							<th width="8%">${message("Need.createDate")}</th>
							<th width="5%">${message("admin.common.action")}</th>
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
							<th width="8%"><div class="th_div">${message("Need.clientName")}</div></th>
							<th width="5%"><div class="th_div">${message("Need.type")}</div></th>
							<th width="7%"><div class="th_div">${message("Need.clientNum")}</div></th>
							<th width="5%"><div class="th_div">业务员</div></th>
							<th width="10%"><div class="th_div">${message("Need.area")}</div></th>
							<th width="8%"><div class="th_div">${message("Need.userName")}</div></th>
							<th width="8%"><div class="th_div">${message("Need.tel")}</div></th>
							<th width="8%"><div class="th_div">${message("Need.createDate")}</div></th>
							<th width="5%"><div class="th_div">${message("admin.common.action")}</div></th>
						</tr>
						</thead>
						<tbody>
						[#list page.content as need]
						<tr class="text-l">
							<td><input type="checkbox" name="ids" value="${need.id}"></td>
							<td>${need.name}</td>
							<td>${message("Need.shopType."+need.shopType)}</td>
							<td>${need.clientNum}</td>
							<td>${need.admin.name}</td>
							<td>${need.area.fullName}</td>
							<td>${need.userName}</td>
							[#if need.shopType == 'direct']
							<td>${need.receiverTel}</td>
							[#else]
							<td>${need.tel}</td>
							[/#if]
							<td><span title="${need.createDate?string("yyyy-MM-dd HH:mm:ss")}">${need.createDate?string("yyyy-MM-dd HH:mm:ss")}</span></td>
							<td class="td-manage">
								<!-- [#if need.needStatus == "available"]
									<a title="暂停" href="javascript:;" onclick="javascript:updateneedStatus('suspend', ${need.id});" class="ml-5" style="text-decoration:none"><i class="operation_icon icon_stop"></i></a>
								[#else]
									<a title="启用" href="javascript:;" onclick="javascript:updateneedStatus('available', ${need.id});" class="ml-5" style="text-decoration:none"><i class="operation_icon icon_start"></i></a>
								[/#if] -->
								[@shiro.hasPermission name = "admin:need:edit"]
								<a title="${message("admin.common.edit")}" href="edit.jhtml?id=${need.id}&oldTel=${need.tel}" class="ml-5" style="text-decoration:none"><i class="operation_icon icon_bianji"></i></a>
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
			
			/*表格的高度，，随着电脑分倍率的变化而变化*/
			var heightObj = $(document.body).height() - 170;
			$(".list_t_tbody").css("height",heightObj);
			$(".table_width").css("width", $(".list_t_tbody").css("width")); 
			
			function add() {
        		window.location.href="add.jhtml";
        	}
			
			function importData(){
				window.location.href="importMore.jhtml";
			}
			
			function updateneedStatus(type , id){
			    $.dialog({
			        type: "warn",
			        content: "是否要修改客户状态",
			        onOk: function() {
			            $.ajax({
			                url: "updateneedStatus.jhtml",
			                type: "GET",
			                data: {needStatus:type , id:id},
			                dataType: "json",
			                cache: false,
			                success: function(message) {
			                    $.message(message);
			                    if (message.type == "success") {
			                        setTimeout(function () {
			                            location.reload(true);
			                        }, 2000);
			                    }
			                }
			            });
			        }
			    });
			}

            $().ready(function() {

            	[@flash_message /]


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
			
		</script>
	</body>
</html>
[/#escape]