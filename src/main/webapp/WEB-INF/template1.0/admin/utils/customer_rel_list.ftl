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
			/*body{background:#f3f8fe;}*/
			.search_button{margin-left:0px;}
			.timeType[type="text"]{float:left;border:0;width:60px;height:30px;background:#f3f8fe;}
			.iframe_page:after{
				content:"";
				display: block;
				height:0;
				clear: both;
			}
	        .ch_search{border:1px solid #f0f0f0}
			.iframe_page{

				padding-bottom:20px;
			}
		</style>
	</head>
	<body >
		<div class="iframe_page"><!--内容外面的大框-->	
			[#--<div class="cus_nav">
				<ul>
					<li><a id="goHome"	href="../homePage/index.jhtml">${message("admin.breadcrumb.home")}</a></li>
					<li>${message("admin.customerRelation.list")} <span>(${message("admin.page.total", page.total)})</span></li>
				</ul>
			</div>--]
		<form id="listForm" action="customerRelList.jhtml" method="get">

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
						<input type="text" id="searchName" name="searchName" value="${searchName}" maxlength="200" placeholder="输入客户名称/客户编号/联系人" />

					</div>
				</div>
				[#--<div>
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
				</div>--]
				
				<button type="submit" class="search_button" style="margin-left:0px;">查询</button>
				[#--<div class="ch_operate">
					<button type="button" class="op_button add_B" onclick="add();">${message("admin.common.add")}</button>
					<button type="button" class="op_button del_B disabled" id="deleteButton">${message("admin.common.delete")}<button>
					<button type="button" class="op_button update_B" id="refreshButtons" onclick="javascript:window.location.href='list.jhtml'">${message("admin.common.refresh")}</button>
				</div>--]
			</div>
			
			<div class="table_con">
				<table id="listTable" class="table table-border table-hover table_width">
					<thead>
						<tr class="text-l">
							<th width="4%"><input class="selectAll" type="checkbox" id="selectAll" id="selectAll"></th>
							<th width="16%">${message("CustomerRelation.clientName")}</th>
							<th width="10%">${message("CustomerRelation.clientNum")}</th>
							<th width="10%">${message("CustomerRelation.clientType")}</th>
							<th width="20%">${message("CustomerRelation.area")}</th>
							<th width="10%">${message("CustomerRelation.userName")}</th>
							<th width="15%">${message("CustomerRelation.tel")}</th>
							<th width="15%">${message("CustomerRelation.createDate")}</th>
							[#--<th width="8%">${message("admin.common.action")}</th>--]
						</tr>
					</thead>
					<tbody>
					[#list page.content as customerRelation]
						<tr class="text-l">
							<td><input type="checkbox" name="ids" value="${customerRelation.bySupplier.id}" onclick="javascript:selectCustomer(this);" /></td>
							<td>${customerRelation.clientName}</td>
							<td>${customerRelation.clientNum}</td>
							<td>${message("CustomerRelation.ClientType." + customerRelation.clientType)}</td>
							<td>${customerRelation.area.fullName}</td>
							<td>${customerRelation.userName}</td>
							<td>${customerRelation.tel}</td>
							<td><span title="${customerRelation.createDate?string("yyyy-MM-dd HH:mm:ss")}">${customerRelation.createDate?string("yyyy-MM-dd HH:mm:ss")}</span></td>
							[#--<td class="td-manage">
								<a title="${message("admin.common.edit")}" href="edit.jhtml?id=${customerRelation.id}" class="ml-5" style="text-decoration:none"><i class="operation_icon icon_bianji"></i></a>
							</td>--]
						</tr>
					[/#list]
					</tbody>
				</table>
			</div>
			[@pagination pageNumber = page.pageNumber totalPages = page.totalPages]
			[#include "/admin/include/pagination.ftl"]
			[/@pagination]
		</form>
		</div>
		<script src="${base}/resources/admin1.0/js/jquery.min.js"></script>
		<script type="text/javascript" src="${base}/resources/admin1.0/js/common.js"></script>
        <script type="text/javascript" src="${base}/resources/admin1.0/js/list.js"></script>
        
        <script type="text/javascript">

            function selectCustomer(currThis){
                var $this = $(currThis);
				var supplierId = $this.val();

                if($this.attr("checked")){
                    window.parent.addToCache(supplierId);
                }
                if(!$this.attr("checked")){
                    window.parent.delFromCache(supplierId);
                }
            }


	        $().ready(function() {
	        	
	        	var $listForm = $("#listForm");

                var cacheCustomers = window.parent.getCache();

                $("input[name='ids']").each(function () {//遍历根据name属性取到的所有值
                    var id = $(this).val();
                    if(id in cacheCustomers){
                        $(this).attr("checked" , true);
                        //$(this).attr("disabled" , true);
                    }
                });
	        	
	        	[@flash_message /]
	        	

				/*表格的高度，，随着电脑分倍率的变化而变化*/
				var heightObj = $(document.body).height() - 170;
				$(".list_t_tbody").css("height",heightObj);
				

                //全选事件
                $("#selectAll").click(function(){
                    $("input[name='ids']").each(function () {//遍历根据name属性取到的所有值
                        selectCustomer(this);
                    });
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