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
			.child_page{background:#ffffff;height:auto;padding-bottom:10px;}
			.search_button{margin-left:36px;}
			.dialogContent {
				height: calc(100% - 80px);
				overflow: auto;
			}
		</style>
	</head>
	<body >
		<div class="child_page oneCustomer"><!--内容外面的大框-->
		<h3 class="form_title" style="margin:10px 0 0 20px;">选择门店</h3>
		<form id="listForm" action="getNeedList.jhtml" method="get">
			<div class="ch_condition">
				<div class="ch_search">
					<img class="search_img" src="${base}/resources/admin1.0/images/sousuo_icon.svg" alt="" />
					<div class="search_input">
						<input type="text" id="searchName" name="searchName" value="${searchName}" maxlength="200" placeholder="输入门店名称/门店编号/收货人" />
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
			</div>
			
			<div class="table_con">
				<table class="table table-border table-hover table_width boo">
					<thead>
					<tr class="text-l">
						<th width="4%"><input class="all_checked" type="checkbox" id="selectAll" id="selectAll"></th>
						<th width="15%">${message("Need.clientName")}</th>
						<th width="11%">${message("Need.clientNum")}</th>
						<th width="15%">${message("Need.area")}</th>
						<th width="10%">${message("Need.userName")}</th>
						<th width="10%">${message("Need.tel")}</th>
						<th width="15%">${message("Need.createDate")}</th>
					</tr>
					</thead>
				</table>
				<div class="list_t_tbody" id="listTable">
					<table class="table table-border table-hover table_width">
						<thead>
						<tr class="text-l">
							<th width="4%"></th>
							<th width="15%"></th>
							<th width="11%"></th>
							<th width="15%"></th>
							<th width="10%"></th>
							<th width="10%"></th>
							<th width="15%"></th>
						</tr>
					</thead>
						<tbody>
						[#list page.content as need]
						<tr class="text-l">
							<td><input type="checkbox" name="ids" id="${need.id}" value="${need.id}" onclick="selectNeeds(${need.id})"></td>
							<td>${need.name}</td>
							<td>${need.clientNum}</td>
							<td>${need.area.fullName}</td>
							<td>${need.userName}</td>
							<td>${need.tel}</td>
							<td><span title="${need.createDate?string("yyyy-MM-dd HH:mm:ss")}">${need.createDate?string("yyyy-MM-dd HH:mm:ss")}</span></td>
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
		<script type="text/javascript" src="${base}/resources/admin1.0/js/common.js"></script>
        <script type="text/javascript" src="${base}/resources/admin1.0/js/list.js"></script>
        <script type="text/javascript">
			
			/*表格的高度，，随着电脑分倍率的变化而变化*/
			var heightObj = $(document.body).height() - 70;
			//$(".list_t_tbody").css("height",heightObj);
			$(".table_width").css("width", $(".list_t_tbody").css("width")); 
			window.parent.setPageNumber(parseInt('${page.pageNumber}'));
			function selectNeeds(id){
			        //alert($("#"+id).val());
			        var $id = $("#"+id);
			        if($id.attr("checked")){
						var need={};
	           			need.id=parseInt(id); 
			            window.parent.addNeeds(need);
			        }
			        if(!$id.attr("checked")){
			            window.parent.delNeeds(parseInt(id));
			        }
		    }

            $().ready(function() {
            	var cacheNeeds = window.parent.getNeeds();

			    $("input[name='ids']").each(function () {//遍历根据name属性取到的所有值
			        var id = $(this).val();
			        for (var i = 0; i < cacheNeeds.length; i++) {
			        	if (id == cacheNeeds[i].id) {
			        		$(this).attr("checked", true);
			        		break;
			        	}
			        }
			    });


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
				});

				//全选事件
				$("#selectAll").click(function(){
			        $("input[name='ids']").each(function () {//遍历根据name属性取到的所有值
			            var id = $(this).val();
			            selectNeeds(id);
			        });
				});

            });

		</script>
	</body>
</html>
[/#escape]