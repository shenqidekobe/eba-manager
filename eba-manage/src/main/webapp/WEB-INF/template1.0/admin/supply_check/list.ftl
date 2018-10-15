[#escape x as x?html]
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>${message("admin.goods.list")} - Powered By DreamForYou</title>
		<meta http-equiv="X-UA-Compatible" content="IE=edge" />
		<link rel="stylesheet" type="text/css" href="${base}/resources/admin1.0/H-ui/static/h-ui/css/H-ui.min.css" />
		<link rel="stylesheet" type="text/css" href="${base}/resources/admin1.0/H-ui/static/h-ui.admin/css/H-ui.admin.css" />
		<link rel="stylesheet" href="${base}/resources/admin1.0/js/date/dateRange.css" />
		<link rel="stylesheet" type="text/css" href="${base}/resources/admin1.0/js/kkpager/kkpager_blue.css" />
		<link rel="stylesheet" href="${base}/resources/admin1.0/css/public.css" />
		<link rel="stylesheet" href="${base}/resources/admin1.0/css/admin.css" />
		<style>
			body{background:#f9f9f9;}
		</style>
	</head>
	<body >
		<div class="child_page"><!--内容外面的大框-->	
			<div class="cus_nav">
				<ul>
					<li><a id="goHome"	href="../homePage/index.jhtml">${message("admin.breadcrumb.home")}</a></li>
					<li>供应审核 <span>(${message("admin.page.total", page.total)})</span></li>
				</ul>
			</div>
		<form id="listForm" action="list.jhtml" method="get">
			<input type="hidden" id="status" name="status" value="${status}" />
			<input type="hidden" id="categoryId" name="categoryId" value="${categoryId}" />

			<div class="ch_condition">
				<div class="require_search" id="filterStatus">
					<span class="search">所有状态</span>
					<ul class="check">
						<li name="status" val="">所有状态</li>
						[#list statuses as statusShow]
                            <li name="status"[#if statusShow == status] class="checked"[/#if] val="${statusShow}">${statusShow.getName()}</li>
						[/#list]
					</ul>
				</div>

                <div class="require_search" id="filterCategory">
                    <span class="search">所有分类</span>
                    <ul class="check">
                        <li name="categoryId" val="">所有分类</li>
						[#list categoryTree as category]
                            <li name="categoryId" val="${category.id}" [#if category.id == categoryId] class="checked"[/#if]>[#if category.grade != 0]
								[#list 1..category.grade as i]
                                    &nbsp;&nbsp;
								[/#list]
							[/#if]${category.name}</li>
						[/#list]
                    </ul>
                </div>
				
				<div class="ch_search">
					<img class="search_img" src="${base}/resources/admin1.0/images/sousuo_icon.svg" alt="" />
					<!--<div class="ch_sear_type">
						<div class="setUp">	
							<p class="bianhao">编号</p>
							<span></span>
							<p class="canzuo">操作员</p>
						</div>
					</div>-->
					<div class="search_input">
						<input type="text" id="searchValue" name="searchValue" value="${searchValue}" maxlength="200" placeholder="请输入产品名称／企业名称" />
					</div>
				</div>
				<button type="submit" class="search_button">查询</button>
				<div class="ch_operate">
					<button type="button" class="op_button shenhe_B" id="checkButton">审核</button>
					<button type="button" class="op_button del_B disabled" id="deleteButton">${message("admin.common.delete")}<button>
					<button type="button" class="op_button update_B" id="refreshButtons" onclick="javascript:window.location.href='list.jhtml'">${message("admin.common.refresh")}</button>

					<button type="button" class="op_button daochu_B" id="exportButton">导出</button>

				</div>
			</div>
			
			<div class="table_con">
				<table class="table table-border table-hover table_width" id="box">
					<thead>
						<tr class="text-l">
							<th width="4%"><input class="all_checked" type="checkbox" id="selectAll"></th>
							<th width="20%">产品名称</th>
							<th width="12%">产品分类</th>
							<th width="15%">企业名称</th>
							<th width="10%">参考价格</th>
							<th width="10%">货源类型</th>
							<th width="10%">更新日期</th>
                            <th width="10%">状态</th>
                            <th width="10%">操作</th>
						</tr>
					</thead>
				</table>
				<div id="listTable" class="list_t_tbody">
					<table class="table table-border table-hover table_width">
						<thead>
							<tr class="text-l">
								<th width="4%" style="">
									<div class="th_div" style="">
										<input class="all_checked" type="checkbox" value="">
									</div>
								</th>
								<th width="20%"><div class="th_div">产品名称</div></th>
                                <th width="12%"><div class="th_div">产品分类</div></th>
                                <th width="15%"><div class="th_div">企业名称</div></th>
                                <th width="10%"><div class="th_div">参考价格</div></th>
                                <th width="10%"><div class="th_div">货源类型</div></th>
                                <th width="10%"><div class="th_div">更新日期</div></th>
                                <th width="10%"><div class="th_div">状态</div></th>
                                <th width="10%"><div class="th_div">操作</div></th>
							</tr>
						</thead>
						<tbody>
							[#list page.content as goods]
							<tr class="text-l">
								<td><input type="checkbox" value="${goods.id}" name="ids"></td>
								<td>
									${goods.name}
								</td>
								<td>
									${goods.category.name}
								</td>
								<td>${goods.supplier.name}</td>
								<td>
									[#if goods.marketPrice == -1]
								    	面议
										[#else]${currency(goods.marketPrice, true)}
									[/#if]
								</td>
								<td>
									[#if goods.sourceType??]${goods.sourceType.getName()}[/#if]
								<td>
									<span title="${goods.modifyDate?string("yyyy-MM-dd HH:mm:ss")}">${goods.modifyDate}</span>
								</td>
                                <td>
								${message("admin.companyGoos.status." + goods.status)}
                                </td>
								<td class="td-manage">
									<a title="${message("admin.common.view")}" href="/shop/companyGoods/companyGoodsLook.jhtml?pubType=pub_supply&id=${goods.id}" class="ml-5" style="text-decoration:none" target="_blank"><i class="operation_icon icon_see"></i></a>

                                    <a title="${message("admin.common.edit")}" href="edit.jhtml?id=${goods.id}" class="ml-5" style="text-decoration:none"><i class="operation_icon icon_bianji"></i></a>

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
			
			$().ready(function() {
				var $listForm = $("#listForm");
				var $searchSelect = $(".ch_condition .require_search li");
				// 筛选
                $searchSelect.click(function() {
					var $this = $(this);
					var $dest = $("#" + $this.attr("name"));
					if ($this.hasClass("checked")) {
						$dest.val("");
					} else {
						$dest.val($this.attr("val"));
					}
					$listForm.submit();
				});

				$("#exportButton").click(function(){
                    $listForm.attr("action", "export.jhtml");

                    $listForm.submit();

                    $listForm.attr("action", "list.jhtml");

                    return true;
				});

				/*var */

                $(".ch_condition .require_search ul").each(function(){
                    var checkedDom =  $(this).find("li.checked");
                    var firstDom;
                    var firstText;
                    if(checkedDom.length == 0){
                        firstDom = $(this).find("li:eq(0)");
                        firstText = firstDom.html();
                        firstDom.addClass("checked");
                    }else{
                        firstText = checkedDom.html();
                    }
                    $(this).siblings(".search").html(firstText);
                });


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

                $("#checkButton").click(function(){
                    var $checkedIds = $("#listTable input[name='ids']:enabled:checked");
                    if($checkedIds.length == 0){
                        $.message({'type':'error' , 'content':'请选择至少1条供应信息'});
                        return false ;
                    }

                    $.dialog({
                        title:"批量审核",
						content:'审核：<input type="radio" name="statusCheck" value="status_ok"  \/>通过 <input name="statusCheck" type="radio" value="status_rej"  \/>不通过 ',
                        type: "warn",
                        ok: message("admin.dialog.ok"),
                        cancel: message("admin.dialog.cancel"),
                        onOk: function() {
                            var paramsData = $checkedIds.serializeArray() ;
                            paramsData.push({name:"status" , value:$("input[name='statusCheck']:checked").val()});

                            $.ajax({
                                url: "check.jhtml",
                                type: "POST",
                                data: paramsData,
                                dataType: "json",
                                cache: false,
                                success: function(message) {
                                    $.message(message);
                                    if (message.type == "success") {
                                        location.reload(true);

                                    }
                                }
                            });
                        }
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