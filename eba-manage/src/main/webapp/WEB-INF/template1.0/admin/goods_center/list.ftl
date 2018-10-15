[#escape x as x?html]
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>${message("admin.goods.list")} - Powered By DreamForYou</title>
		<meta http-equiv="X-UA-Compatible" content="IE=edge" />
		<link rel="stylesheet" type="text/css" href="${base}/resources/admin1.0/H-ui/static/h-ui/css/H-ui.min.css" />
		<link rel="stylesheet" href="${base}/resources/admin1.0/css/public.css" />
		<link rel="stylesheet" href="${base}/resources/admin1.0/css/admin.css" />
		<style>
			body{background:#f9f9f9;}

            /*导出图片*/
            .exportImgs{
                float:left;
                width:80px;
                height:40px;
                position:relative;
            }

            .exportImgUl{
                width:100px;
                position:absolute;
                left:10px;
                top:30px;
                background:#fff;
                box-shadow: 0px 0px 10px 3px #f9f9f9;
                display:none;
            }
            .exportImgs:hover .exportImgUl{
                display:block;
            }
            .exportImgUl li{
                cursor: pointer;
                padding:0 10px;
                line-height: 30px;
                border-bottom:1px solid #f0f0f0;
            }
            .exportImgUl li:hover{
                background:#4DA1FF;
                color:#fff;
            }

		</style>
	</head>
	<body >
		<div class="child_page"><!--内容外面的大框-->	
			<div class="cus_nav">
				<ul>
					<li><a href="">${message("admin.breadcrumb.home")}</a></li>
					<li>${message("admin.goods.list")} <span>(${message("admin.page.total", page.total)})</span></li>
				</ul>
			</div>
		<form id="listForm" action="list.jhtml" method="get">
			<input type="hidden" id="type" name="type" value="${type}" />
			<input type="hidden" id="productCategoryId" name="productCategoryId" value="${productCategoryId}" />
			<input type="hidden" id="brandId" name="brandId" value="${brandId}" />
			<input type="hidden" id="tagId" name="tagId" value="${tagId}" />
			<input type="hidden" id="promotionId" name="promotionId" value="${promotionId}" />
			<input type="hidden" id="isMarketable" name="isMarketable" value="${(isMarketable?string("true", "false"))!}" />
			<input type="hidden" id="isList" name="isList" value="${(isList?string("true", "false"))!}" />
			<input type="hidden" id="isTop" name="isTop" value="${(isTop?string("true", "false"))!}" />
			<input type="hidden" id="isOutOfStock" name="isOutOfStock" value="${(isOutOfStock?string("true", "false"))!}" />
			<input type="hidden" id="isStockAlert" name="isStockAlert" value="${(isStockAlert?string("true", "false"))!}" />
			<div class="ch_condition">
				<div class="require_search" id="filterMenu">
					<span class="search">${message("admin.goods.filter")}</span>
					<ul class="check">
						<li name="isMarketable" val="">${message("admin.goods.filter")}</li>
                        <li name="isMarketable"[#if isMarketable?? && isMarketable] class="checked"[/#if] val="true">${message("admin.goods.isMarketable")}</li>
						<li name="isMarketable"[#if isMarketable?? && !isMarketable] class="checked"[/#if] val="false">${message("admin.goods.notMarketable")}</li>
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
						<input type="text" id="searchValue" name=orSearchValue value="${page.orSearchValue}" maxlength="200" placeholder="请输入编号/商品名称搜索" />
						<input type="hidden" name="searchProperties" value="sn">
						<input type="hidden" name="searchProperties" value="name">
					</div>
				</div>
				<button type="submit" class="search_button">查询</button>
				<div class="ch_operate">
					<button type="button" class="op_button add_B" onclick="javascript:location.href='add.jhtml'">${message("admin.common.add")}</button>
					<button type="button" class="op_button del_B disabled" id="deleteButton">${message("admin.common.delete")}</button>

                    <div class="exportImgs">
                        <button type="button" class="op_button daoru_B">导入</button>
                        <ul class="exportImgUl">
                                <li onclick="importData()">导入商品</li>
                                <li onclick="importImg()">导入商品图片</li>
                        </ul>
                    </div>

					<button type="button" class="op_button update_B" id="refreshButtons" onclick="javascript:window.location.href='list.jhtml'">${message("admin.common.refresh")}</button>
				</div>
			</div>
			
			<div class="table_con">
				<table class="table table-border table-hover table_width" id="box">
					<thead>
						<tr class="text-l">
							<th width="4%"><input class="all_checked" type="checkbox" id="selectAll"></th>
							<th width="16%">${message("Goods.sn")}</th>
							<th width="25%">${message("Goods.name")}</th>
							<th width="12%">${message("Goods.productCategory")}</th>
							<th width="10%">${message("Goods.price")}</th>
							<!--<th>角色</th>-->
							<th width="10%">${message("Goods.isMarketable")}</th>
							<th width="10%">${message("admin.common.createDate")}</th>
							<th width="10%">${message("admin.common.action")}</th>
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
								<th width="16%"><div class="th_div">${message("Goods.sn")}</div></th>
								<th width="25%"><div class="th_div">${message("Goods.name")}</div></th>
								<th width="12%"><div class="th_div">${message("Goods.productCategory")}</div></th>
								<th width="10%"><div class="th_div">${message("Goods.price")}</div></th>
								<!--<th>角色</th>-->
								<th width="10%"><div class="th_div">${message("Goods.isMarketable")}</div></th>
								<th width="10%"><div class="th_div">${message("admin.common.createDate")}</div></th>
								<th width="10%"><div class="th_div">${message("admin.common.action")}</div></th>
							</tr>
						</thead>
						<tbody>
							[#list page.content as goods]
							<tr class="text-l">
								<td><input type="checkbox" value="${goods.id}" name="ids"></td>
								<td>
									<span[#if goods.isOutOfStock] class="red"[#elseif goods.isStockAlert] class="blue"[/#if]>
										${goods.sn}
									</span>
								</td>
								<td>
									<span title="${goods.name}">
										${abbreviate(goods.name, 80, "...")}
									</span>
									[#if goods.type != "general"]
										<span class="red">*</span>
									[/#if]
									[#list goods.validPromotions as promotion]
										<span class="promotion" title="${promotion.title}">${promotion.name}</span>
									[/#list]
								</td>
								<td>${goods.categoryCenter.name}</td>
								<td>${currency(goods.price, true)}</td>
								<!--<td>栏目编辑</td>-->
								<td class="td-status">
									[#if goods.isMarketable == false]
									<img src="${base}/resources/admin1.0/images/shangjiaf_icon.svg">
									[/#if]
									[#if goods.isMarketable == true]
									<img src="${base}/resources/admin1.0/images/shangjias_icon.svg">
									[/#if]
									</td>
								<td>
									<span title="${goods.createDate?string("yyyy-MM-dd HH:mm:ss")}">${goods.createDate}</span>
								</td>
								<td class="td-manage">
									<a title="${message("admin.common.edit")}" href="edit.jhtml?id=${goods.id}" class="ml-5" style="text-decoration:none"><i class="operation_icon icon_bianji"></i></a>
									[#--[#if goods.isMarketable]
									<a title="${message("admin.common.view")}" href="javascript:;" onclick="admin_edit('${message("admin.common.view")}','${goods.url}','1','800','500')" class="ml-5" style="text-decoration:none"><i class="operation_icon icon_see"></i></a>
									[#else]
										<span title="${message("admin.goods.notMarketable")}"><i class="operation_icon icon_see"></i></span>
									[/#if]--]
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
				var $filterMenuItem = $("#filterMenu li");
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
				
				
				//$(".cus_nav li::after").rotate(45);
				
			});

            function importData(){
                window.location.href="importMore.jhtml";
            }

            function importImg(){
                window.location.href="importImg.jhtml";
            }

        </script>
        
	</body>
</html>
[/#escape]