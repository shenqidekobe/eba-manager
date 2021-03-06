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

		</style>
	</head>
	<body >
		<div class="child_page"><!--内容外面的大框-->	
			<div class="cus_nav">
				<ul>
					<li>推荐企业<span>(${message("admin.page.total", page.total)})</span></li>
				</ul>
			</div>
		<form id="listForm" action="recommendlist.jhtml" method="get">
			<input type="hidden" id="status" name="status" value="${status}" />
			<div class="ch_condition">

				<div class="require_search" id="filterMenu">
					<span class="search">认证状态</span>
					<ul class="check">
					<li name="status" val="" value="">所有企业</li>
					 [#list statusEnum as statusEnum]
					  <li name="status" val="${statusEnum}" [#if status == "${statusEnum}"]class="checked" [/#if] value="${statusEnum}">${message("Supplier.Status."+statusEnum)}</li>
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
						<input type="text" id="searchValue" name="searchValue" value="${page.searchValue}" maxlength="200" placeholder="请输入供应商" />
						<input type="hidden" name="searchProperty" value="name">
					</div>
				</div>
				
				<button type="submit" class="search_button">查询</button>
				<div class="ch_operate">
					<button type="button" class="op_button update_B" id="refreshButton">${message("admin.common.refresh")}</button>
				</div>
			</div>
			
			<div class="table_con">
				<table class="table table-border table-hover table_width">
					<thead>
						<tr class="text-l">
							<th width="5%"><input class="all_checked" type="checkbox" id="selectAll"></th>
							<th width="15%">供应商</th>
							<th width="25%">地址</th>
							<th width="10%">联系人</th>
							<th width="15%">手机号</th>
							<th width="20%">${message("admin.common.createDate")}</th>
							<th width="10%">认证状态</th>
						</tr>
					</thead>
				</table>
				<div class="list_t_tbody" id="listTable">
					<table class="table table-border table-hover table_width">
						<thead>
							<th width="5%" style="">
									<div class="th_div" style="">
										<input class="all_checked" type="checkbox" value="">
									</div>
								</th>
								<th width="15%"><div class="th_div">供应商</div></th>
								<th width="25%"><div class="th_div">地址</div></th>
								<th width="10%"><div class="th_div">联系人</div></th>
								<th width="15%"><div class="th_div">手机号</div></th>
								<th width="20%"><div class="th_div">${message("admin.common.createDate")}</div></th>
								<th width="10%"><div class="th_div">认证状态</div></th>
						</thead>
						<tbody>
						[#list page.content as supplier]
							<tr class="text-l">
								<td><input type="checkbox" value="${supplier.id}" [#if supplier.recommendFlag == true] checked="checked" [/#if] name="ids"></td>
								<td>${supplier.name}</td>
								<td>${supplier.area.fullName} ${supplier.address}</td>
								<td>${supplier.userName}</td>
								<td>${supplier.tel}</td>
								<td>
									<span title="${supplier.createDate?string("yyyy-MM-dd HH:mm:ss")}">${supplier.createDate}</span>
								</td>
								<td>
									[#if supplier.status == 'notCertified' ]
										<span style="color:red">企业未认证</span>
									[/#if]
									[#if supplier.status == 'authenticationFailed' ]
									 	<span  style="color:red">认证失败</span>
									[/#if]
									[#if supplier.status == 'certification' ]
									 	<span  style="color:blue">认证中</span>
									[/#if]
									[#if supplier.status == 'verified' ]
									 	<span  style="color:green">企业已认证</span>
									[/#if]
								</td>
							</tr>
						[/#list]
						</tbody>
					</table>
				</div>
			</div>
			<input id="save" class="btn radius confir_S" type="button" value="保存" style="margin-top:10px"/>
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
	        	
				$("#save").click(function(){
					var check_value = [];
					$('input[name="ids"]:checked').each(function(){ 
						check_value.push($(this).val()); 
					});
					if(check_value.length == 0){
						check_value.push(-1); 
					}
					
					var check_value_no = [];
					$('input[name="ids"]:not(":checked")').each(function(){ 
						check_value_no.push($(this).val()); 
					});
					if(check_value_no.length == 0){
						check_value_no.push(-1); 
					}
					
					$.ajax({
		                url: "/admin/adSupplier/recommend.jhtml",
		                type: "POST",
		                async: false,
		                data: {ids: check_value,idsNo:check_value_no},
		                dataType: "json",
		                cache: false,
		                success: function(message) {
		                   
		                   if(message.type == 'success'){
		                	   $.message("info", "保存成功！");
		                	   setTimeout(function () {
		                		   $("#listForm").submit();
		                       },500);
		                   }else{
		                	   setTimeout(function () {
		                		   $.message("error", "保存失败！");
		                		   $("#listForm").submit();
		                       },500);
		                   }
		                }
		            });
					
				});
	        });
		</script>
	</body>
</html>
[/#escape]