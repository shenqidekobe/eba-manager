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
			.supplyType{display:block;width:50px;height:22px;text-align: center;line-height: 22px;border-radius: 5px;}
			.inTheSupply{background:#d2f0e6;color:#00b45a;}/*供应中*/
			.expired{background:#dce6eb;color:#7882e0}/*已过期*/
			.suspendSupply{background:#ffe5cd;color:#db5e03}/*暂停中*/
			.yfp{background:#d2f0e6;color:#00b45a}/* 已分配 */
			.rejected{background:#ffd6d6; color:#ff5454;}/*已拒绝*/
			.toBeConfirmed{background:#c6f5ff; color:#41bcd6;}/*待确认*/
			.willSupply{background:#faf0b4;color:#be965a}/*未开始*/
			.xxDialog{top:40px}
			.xxDialog .dialogBottom{width:100%;position:absolute;bottom:0;border:0;}
			.xxDialog .dialogBottom{border-top:1px solid #eee;}
			.dialogContent{height:calc(100% - 80px); overflow: auto; overflow-x: hidden;}
			iframe{height:calc(100% - 5px);width:100%;}
		</style>
	</head>
	<body class="bodyObj">
		<div class="child_page"><!--内容外面的大框-->	
			<div class="cus_nav">
				<ul>
					<li><a id="goHome"	href="../homePage/index.jhtml">${message("admin.breadcrumb.home")}</a></li>
					<li><a href="list.jhtml">供应分配列表</a></li>
					<li>${message("admin.need.list")} <span>(${message("admin.page.total", page.total)})</span></li>
				</ul>
			</div>
		<form id="listForm" action="distributionList.jhtml" method="get">
			<input type="hidden" name="id" value="${id}">
			<input type="hidden" name="status" id="status" value="${status}">
			<div class="ch_condition">
				<div class="require_search" id="filterMenu">
					<span class="search">${message("admin.order.filter")}</span>
					<ul class="check">
						<li name="status" val="">所有状态</li>
						<li name="status"[#if "on" == status] class="checked"[/#if] val="on">已分配</li>
                        <li name="status"[#if "off" == status] class="checked"[/#if] val="off">未分配</li>
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
						<input type="text" id="searchValue" name="searchValue" value="${page.searchValue}" maxlength="200" placeholder="请输入收货点名称" />
						<input type="hidden" name="searchProperty" value="name">
					</div>
				</div>
				
				<button type="submit" class="search_button">查询</button>
				<div class="ch_operate">
					<button type="button" class="op_button fenpei_B" id="batchistribution">批量分配</button>
					<button type="button" class="op_button update_B" id="refreshButtons" onclick="javascript:window.location.href='list.jhtml'">${message("admin.common.refresh")}</button>
				</div>
			</div>
			
			<div class="table_con">
				<table class="table table-border table-hover table_width">
					<thead>
						<tr class="text-l">
							<th width="4%"><input class="selectAll" type="checkbox" id="selectAll"></th>
							<th width="10%">${message("admin.need.name")}</th>
							<th width="13%">${message("admin.need.address")}</th>
							<th width="5%">${message("admin.need.userName")}</th>
							<th width="10%">${message("admin.need.tel")}</th>
							<th width="8%">分配状态</th>
							<th width="8%">${message("admin.common.action")}</th>
						</tr>
					</thead>
				</table>
				<div class="list_t_tbody" id="listTable">
					<table class="table table-border table-hover table_width">
						<thead>
							<tr class="text-l">
								<th width="4%" style="">
									<div class="th_div" style="">
										<input class="selectAll" type="checkbox">
									</div>
								</th>
								<th width="10%"><div class="th_div">${message("admin.need.name")}</div></th>
								<th width="13%"><div class="th_div">${message("admin.need.address")}</div></th>
								<th width="5%"><div class="th_div">${message("admin.need.userName")}</div></th>
								<th width="10%"><div class="th_div">${message("admin.need.tel")}</div></th>
								<th width="8%"><div class="th_div">分配状态</div></th>
								<th width="8%"><div class="th_div">${message("admin.common.action")}</div></th>
							</tr>
						</thead>
						<tbody>
						[#list page.content as need]
							<tr class="text-l">
								<td><input type="checkbox" value="${need.id}" name="ids" id="ids"></td>
								<td>${need.name}</td>
								<td>${need.area.fullName} ${need.address}</td>
								<td>${need.userName}</td>
								<td>${need.tel}</td>
								<td>
								
									[#if need.status == 'on']
										<span class="supplyType yfp">已分配</span>
									[#elseif need.status == 'off']
										<span class="supplyType willSupply">未分配</span>
									[/#if]
								</td>
								<td class="td-manage">
									<a title="商品分配" href="distributionView.jhtml?id=${id}&needId=${need.id}" class="ml-5" style="text-decoration:none"><i class="operation_icon icon_fenpei"></i></a> 
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
	        var needProducts = new Array();
	    	
			/* var needProductsStr =[#noescape]'${needProductIds?json_string}'[/#noescape];
			var ids=JSON.parse(needProductsStr).ids;
			$.each(ids, function (i, id) {
				addNeedProduct(id,0);
			});
			console.log(needProducts); */
			function getCacheFromChild(){
				return needProducts ;
			}
			//子页面调用 增加关联商品
		    /**
			 *
		     * @param obj
		     */
			function addNeedProduct(products,supplyPrice){
		        needProducts.push({
		        	'products':products,
		        	'supplyPrice':supplyPrice
		        });
			}
			//删除关联商品
			function removeByValue(products) {
			  for(var i=0; i<needProducts.length; i++) {
			    if(needProducts[i].products == products) {
			      needProducts.splice(i, 1);
			      break;
			    }
			  }
			}
			
			function updatePrice(productId , updPrice){
		        needProducts[updatePrice][productId].supplyPrice = updPrice ;
			}
        
			/*表格的高度，，随着电脑分倍率的变化而变化*/
			var heightObj = $(document.body).height() - 170;
			$(".list_t_tbody").css("height",heightObj);
			$(".table_width").css("width", $(".list_t_tbody").css("width"));
			
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
				
				
				//批量分配
		        $("#batchistribution").click(function(){
		        	var ids = [];
					$('input[name="ids"]:checked').each(function(){ 
						ids.push($(this).val()); 
					}); 
		        	if(ids.length == 0) {
		        		$.message({'type':'error' , 'content':'请选择收货点'});
						return false ;
		        	}
		        	//收货点数量
		        	var needNum = ids.length;
		        	var contentPath = "batchdistributionGoods.jhtml?id=${id}&needNum=" + needNum;
		            $.dialog({
		                title: "批量商品分配",
		                [@compress single_line=true]
		                    content: '<iframe id="sonFrame" frameborder="0" width="" height="" src="'+contentPath+'"><\/iframe>',
		                [/@compress]
		                width: $(".bodyObj").width()-20,
		                height:$(".bodyObj").height()-40,
		                ok: "${message("admin.dialog.ok")}",
		                cancel: "${message("admin.dialog.cancel")}",
		                onOk: function(){
		                	if(needProducts.length == 0){
								$.message({'type':'error' , 'content':'请选择至少一条记录！'});
								return false ;
							}
		                	var saveProducts = new Array();
					        var tempSaveJson = {};
					        $.each(needProducts,function(i,value) {
					               tempSaveJson['supplierNeedProductList['+i+'].products.id'] = value['products'] ;
					               tempSaveJson['supplierNeedProductList['+i+'].supplyPrice'] = value["supplyPrice"] ;
					        });
					        tempSaveJson['needId'] = ids;
					        tempSaveJson['supplierSupplierId'] = ${id};
					        $.ajax({
					            type: "POST",
					            url: "batchDistributionSave.jhtml",
					            data: tempSaveJson,
					            dataType: "json",
					            success: function (message) {
					                $.message(message);
					                if (message.type == "success") {
					                    setTimeout(function () {
					                        location.href="distributionList.jhtml?id=${id}";
					                    }, 3000);
					                }
					            },
					            error: function (data) {
					                $.message(data);
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