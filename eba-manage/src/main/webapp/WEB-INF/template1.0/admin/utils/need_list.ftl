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
			/*body{background:#f3f8fe;}*/
			.ch_search{border:1px solid #eee;}
			.iframe_page:after{
				content:"";
				display: block;
				height:0;
				clear: both;
			}
			.require_search{border:1px solid #eee;}
		</style>
	</head>
	<body >
		<div class="iframe_page" style="width:100%;"><!--内容外面的大框-->	
			[#--<div class="cus_nav">
				<ul>
					<li><a id="goHome"	href="../homePage/index.jhtml">${message("admin.breadcrumb.home")}</a></li>
					<li>${message("admin.need.list")} <span>(${message("admin.page.total", page.total)})</span></li>
				</ul>
			</div>--]
		<form id="listForm" action="needList.jhtml" method="get">
            <input type="hidden" id="searchType" name="searchType" value="${searchType}" />
			<div class="ch_condition">

                <div class="require_search" id="filterMenu">
                    <span class="search">${message("admin.order.filter")}</span>
                    <ul class="check">
                        <li name="searchType" val="">全部类型</li>
						[#list needTypes as needType]
                            <li name="searchType"[#if needType == searchType] class="checked"[/#if] val="${needType}">${needType.desc}</li>
						[/#list]
                    </ul>
                </div>

				<div class="ch_search">
					<img class="search_img" src="${base}/resources/admin1.0/images/sousuo_icon.svg" alt="" />
					<div class="search_input">
						<input type="text" id="searchName" name="searchName" value="${searchName}" maxlength="200" placeholder="请输入名称/地址/姓名/手机号" />
					</div>
				</div>
				
				<button type="submit" class="search_button">查询</button>
				[#--<div class="ch_operate">
					<button type="button" class="op_button add_B" onclick="add();">${message("admin.common.add")}</button>
					<button type="button" class="op_button update_B" id="refreshButtons" onclick="javascript:window.location.href='list.jhtml'">${message("admin.common.refresh")}</button>
					<button type="button" class="op_button daoru_B" onclick="importData()">导入</button>
				</div>--]
			</div>
			
			<div class="table_con">
				<table id="listTable" class="table table-border table-hover table_width">
					<thead>
						<tr class="text-l">
							<th width="4%"><input class="selectAll" type="checkbox" id="selectAll" value=""></th>
							<th width="15%">${message("admin.need.name")}</th>
							<th width="25%">${message("admin.need.address")}</th>
							<th width="10%">${message("admin.need.userName")}</th>
							<th width="10%">${message("admin.need.tel")}</th>
							<td width="10%">${message("admin.common.createDate")}</td>
							[#--<th width="8%">${message("admin.common.action")}</th>--]
						</tr>
					</thead>
				
					<tbody>
						[#list page.content as need]
						<tr class="text-l">
							<td><input type="checkbox" value="${need.id}" name="ids" onclick="javascript:selectNeed(this);"></td>
							<td>${need.name}</td>
							<td>${need.area.fullName} ${need.address}</td>
							<td>${need.userName}</td>
							<td>${need.tel}</td>
							<td><span title="${need.createDate?string("yyyy-MM-dd HH:mm:ss")}">${need.createDate}</span></td>
							[#--<td class="td-manage">
								<a title="${message("admin.common.edit")}" href="edit.jhtml?id=${need.id}" class="ml-5" style="text-decoration:none"><i class="operation_icon icon_bianji"></i></a> 
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

            function selectNeed(currThis){
                var $this = $(currThis);
                var needId = $this.val();

                if($this.attr("checked")){
                    window.parent.addToCache(needId);
                }
                if(!$this.attr("checked")){
                    window.parent.delFromCache(needId);
                }
            }
			
            $().ready(function() {
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


            	[@flash_message /]

                var cacheNeeds = window.parent.getCache();

                $("input[name='ids']").each(function () {//遍历根据name属性取到的所有值
                    var id = $(this).val();
                    if(id in cacheNeeds){
                        $(this).attr("checked" , true);
                        //$(this).attr("disabled" , true);
                    }
                });

                //全选事件
                $("#selectAll").click(function(){
                    $("input[name='ids']").each(function () {//遍历根据name属性取到的所有值
                        selectNeed(this);
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