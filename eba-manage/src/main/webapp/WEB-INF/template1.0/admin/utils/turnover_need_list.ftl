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
			
		<form id="listForm" action="turnOverNeedList.jhtml" method="get">
			<input type="hidden" name="numberTimes" id="numberTimes" value="${numberTimes}" />
			<input type="hidden" name="supplierId" id="supplierId" value="${supplierId}"/>
			<div class="ch_condition">
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
						<input type="text" id="tel" name="tel" value="${tel}" maxlength="200" placeholder="请输入联系电话" />
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
							<th width="4%"><input class="all_checked" type="checkbox" id="selectAll"></th>
							<th width="20%">联系人</th>
							<th width="15%">联系电话</th>
							<th width="35%">收货地址</th>
						</tr>
					</thead>
				</table>
				<div class="list_t_tbody" id="listTable">
					<table class="table table-border table-hover table_width">
						<thead>
							<tr class="text-l">
								<th width="4%" style="">
									<div class="th_div" style="">
										<input class="all_checked" type="checkbox">
									</div>
								</th>
								<th width="20%"><div class="th_div">联系人</div></th>
								<th width="15%"><div class="th_div">联系电话</div></th>
								<th width="35%"><div class="th_div">收货地址</div></th>
							</tr>
						</thead>
						<tbody>
						[#list page.content as need]
							<tr class="text-l">
								<td>
									<input type="checkbox" name="ids" value="${need.id}" tempValue='{"id":${need.id} , "userName":"${need.userName?html}" , "tel":"${need.tel}" , 
									"areaId":"${need.area.id}" , 
									"address":"${need.area.fullName?html} ${need.address?html}" ,
									"addresses":"${need.address?html}" ,
									 "treePath":"${need.area.treePath?html}" , "orderNum":"${need.isOver()}"}' onclick="javascript:selectNeed(this , {'id':${need.id} , 'userName':'${need.userName?html}' , 'tel':'${need.tel}' ,'areaId':'${need.area.id}', 'address':'${need.area.fullName?html} ${need.address!""?html}','addresses':'${need.address!""?html}' ,'treePath':'${need.area.treePath?html}', 'orderNum':'${need.isOver()}' , 'numberTimes':'${numberTimes}'});" />
								</td>
								<td>${need.userName}</td>
								<td>${need.tel}</td>
								<td>${need.area.fullName} ${need.address}</td>
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
			var heightObj = $(document.body).height() - 150;
			$(".list_t_tbody").css("height",heightObj);
			
			function selectNeed(now, obj) {
		        var $id = $(now);

		        if ($id.attr("checked")) {
		            window.parent.addNeed(obj);
		        }
		        if (!$id.attr("checked")) {
		            window.parent.delNeed(obj);
		        }
		    }


		    $().ready(function () {
		    	var numberTimes = $("#numberTimes").val();
		        var cacheNeeds = window.parent.getNeedCacheFromParent();

		        $("input[name='ids']").each(function () {//遍历根据name属性取到的所有值
		        	
		            var id = $(this).val();
		            if (id in cacheNeeds) {
		                $(this).attr("checked", true);
		                $(this).attr("disabled", true);
		            }
		        });

				[@flash_message /]

		        //全选事件
		        $("#selectAll").click(function () {
		            $("input[name='ids']").each(function () {//遍历根据name属性取到的所有值
		                var obj = $(this).attr("tempValue");
		                selectNeed(this, JSON.parse(obj));
		            });
		        });
				
				$("input[name='ids']").each(function (){
					var obj = $(this).attr("tempValue");
					var date = JSON.parse(obj);
					var orderNum = parseInt(date.orderNum);
					if(orderNum >= numberTimes) {
						$(this).attr("disabled","disabled");
					}
				});

		    });
			
		</script>
	</body>
</html>
[/#escape]