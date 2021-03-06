[#assign shiro = JspTaglibs["/WEB-INF/tld/shiro.tld"] /]
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
					<li>用户列表<span>(${message("admin.page.total", page.total)})</span></li>
				</ul>
			</div>
		<form id="listForm" action="list.jhtml" method="get">
		    <input type="hidden" id="isShoper" name="isShoper" value="${(isShoper?string("true", "false"))!}" />
		    <input type="hidden" id="rank" name="rank" value="${rank}" />
			<div class="ch_condition">
				<div class="require_search" id="filterMenu">
					<span class="search">用户筛选</span>
					<ul class="check">
						<li name="isShoper" val="">用户筛选</li>
                        <li name="isShoper"[#if isShoper?? && isShoper] class="checked"[/#if] val="true">会员</li>
						<li name="isShoper"[#if isShoper?? && !isShoper] class="checked"[/#if] val="false">普通用户</li>
					</ul>
				</div>
				<div class="require_search" id="filterMenu2">
					<span class="search" id="search2">会员等级</span>
					<ul class="check">
						<li name="rank" val="">全部等级</li>
                        <li name="rank"[#if "platina" == rank] class="checked"[/#if] val="platina">白金</li>
                        <li name="rank"[#if "platinum" == rank] class="checked"[/#if] val="platinum">铂金</li>
                        <li name="rank"[#if "blackplatinum" == rank] class="checked"[/#if] val="blackplatinum">黑金</li>
						
					</ul>
				</div>
				<div class="ch_search">
					<img class="search_img" src="${base}/resources/admin1.0/images/sousuo_icon.svg" alt="" />
					<div class="search_input">
						<input type="text" id="searchValue" name="searchValue" value="${page.searchValue}" maxlength="200" placeholder="请输入昵称" />
						<input type="hidden" name="searchProperty" value="nickName">
					</div>
				</div>
				
				<div>
					<div class="drop_down" id="dropDown">
						<span class="timeType">注册时间</span>
						<ul class="check">
							<li name="timeSearch" class="checked" val="createTime">注册时间</li>
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
					<button type="button" class="op_button update_B" id="refreshButton" onclick="javascript:window.location.href='list.jhtml'">${message("admin.common.refresh")}</button>
				</div>
			</div>
			
			<div class="table_con">
				<table class="table table-border table-hover table_width">
					<thead>
						<tr class="text-l">
						    <th width="5%">ID</th>
							<th width="10%">昵称</th>
							<th width="18%">openID</th>
							<th width="10%">余额</th>
							<th width="10%">累计收益</th>
							<th width="10%">是否会员</th>
							<th width="10%">会员等级</th>
							<th width="15%">注册时间</th>
							<th width="10%">${message("admin.common.action")}</th>
						</tr>
					</thead>
				</table>
				<div class="list_t_tbody" id="listTable">
					<table class="table table-border table-hover table_width">
						<thead>
							<tr class="text-l">
							    <th width="5%"><div class="th_div">ID</div></th>
								<th width="10%"><div class="th_div">昵称</div></th>
								<th width="18%"><div class="th_div">openID</div></th>
								<th width="10%"><div class="th_div">余额</div></th>
								<th width="10%"><div class="th_div">累计收益</div></th>
								<th width="10%"><div class="th_div">是否会员</div></th>
								<th width="10%"><div class="th_div">会员等级</div></th>
								<th width="15%"><div class="th_div">${message("admin.common.createDate")}</div></th>
								<th width="10%"><div class="th_div">${message("admin.common.action")}</div></th>
							</tr>
						</thead>
						<tbody>
						[#list page.content as member]
							<tr class="text-l">
							    <td>${member.id}</td>
								<td>${member.nickName}</td>
								<td>${member.smOpenId}</td>
								<td>${member.member.balance}</td>
								<td>${member.member.income}</td>
								<td>
								[#if member.isShoper]是[#else]否[/#if]
								</td>
								<td>${member.rank.label}</td>
								<td>
									<span title="${member.createDate?string("yyyy-MM-dd HH:mm:ss")}">${member.createDate?string("yyyy-MM-dd HH:mm")}</span>
								</td>
								<td class="td-manage">
								   <a title="${message("admin.common.view")}" href="view.jhtml?id=${member.id}" class="ml-5" style="text-decoration:none"><i class="operation_icon icon_see"></i></a>
								   [@shiro.hasPermission name = "admin:member:edit"]
								      <a title="${message("admin.common.edit")}" href="edit.jhtml?id=${member.id}" class="ml-5" style="text-decoration:none"><i class="operation_icon icon_bianji"></i></a>
								   [/@shiro.hasPermission]
								   [#if member.isShoper]
								      <a title="收益记录" href="income/list.jhtml?memberId=${member.id}" class="ml-5" style="text-decoration:none"><i class="operation_icon icon_fenpei"></i></a>
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
	        $().ready(function() {
	        	var $filterMenu = $("#filterMenu");
	        	var $filterMenuItem = $("#filterMenu li");
	        	var $listForm = $("#listForm");
	        	
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
	        	
	        	var $filterMenuItem2 = $("#filterMenu2 li");
				// 筛选
				$filterMenuItem2.click(function() {
					var $this = $(this);
					var $dest = $("#" + $this.attr("name"));
					if ($this.hasClass("checked")) {
						$dest.val("");
					} else {
						$dest.val($this.attr("val"));
					}
					$listForm.submit();
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
                
                var checkedDom2 =  $("#filterMenu2 li.checked");
                var firstDom2;
                var firstText2;
				if(checkedDom2.length == 0){
                    firstDom2 = $("#filterMenu2").find("li:eq(0)");
                    firstText2 = firstDom2.html();
                    firstDom2.addClass("checked");
				}else{
                    firstText2 = checkedDom2.html();
                }
                $("#search2").html(firstText2);

	        	[@flash_message /]
	        	
	        	
	        	/*表格的高度，，随着电脑分倍率的变化而变化*/
				var heightObj = $(document.body).height() - 170;
				$(".list_t_tbody").css("height",heightObj);
				$(".table_width").css("width", $(".list_t_tbody").css("width")); 
	        	
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
	        	
	        });
		</script>
	</body>
</html>
[/#escape]