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
			.messageCon{
				width:250px;
				overflow: hidden;
				text-overflow:ellipsis;
				white-space: nowrap;
			}
		</style>
	</head>
	<body >
		<div class="child_page"><!--内容外面的大框-->	
			<div class="cus_nav">
				<ul>
					<li><a id="goHome"	href="../homePage/index.jhtml">首页</a></li>
					<li><a href="">消息列表共有${page.total }条消息</a></li>
				</ul>
			</div>
		<form id="listForm" action="list.jhtml" method="get">
			<div class="ch_condition">

				<div class="ch_search">
					<img class="search_img" src="${base}/resources/admin1.0/images/sousuo_icon.svg" alt="" />
					<div class="search_input">
						<input type="text" id="title" name="title" value="${newMessage.title}" maxlength="200" placeholder="请输入标题搜索" />
					</div>
				</div>
				<button type="submit" class="search_button">查询</button>
				<div class="ch_operate">
					<button type="button" class="op_button add_B" onclick="javascript:location.href='edit.jhtml'">发消息</button>
					<button type="button" class="op_button update_B" id="refreshButtons" onclick="javascript:window.location.href='list.jhtml'">更新</button>
				</div>
			</div>
			
			<div class="table_con">
				<table class="table table-border table-hover table_width" id="box">
					<thead>
						<tr class="text-l">
							<th width="5%"><input class="all_checked" type="checkbox" id="selectAll"></th>
							<th width="25%">标题</th>
							<th width="25%">内容</th>
							<th width="25%">创建日期</th>
							<th width="20%">操作</th>
						</tr>
					</thead>
				</table>
				<div id="listTable" class="list_t_tbody">
					<table class="table table-border table-hover table_width">
						<thead>
							<tr class="text-l">
								<th width="5%" style="">
									<div class="th_div" style="">
										<input class="all_checked" type="checkbox" value="">
									</div>
								</th>
								<th width="25%"></th>
								<th width="25%"></th>
								<th width="25%"></th>
								<th width="20%"></th>
							</tr>
						</thead>
						<tbody>
							[#list page.content as newMessage]
							<tr class="text-l">
								<td><input type="checkbox" value="${newMessage.id}" name="ids"></td>
								<td>
									${newMessage.title }
								</td>
								<td>
									<div class="messageCon">${newMessage.content }</div>
									
								</td>
								<td>
									${newMessage.createDate }
								</td>
									
								<td class="td-manage">
									<a title="查看"  onclick="queryById(${newMessage.id});" style="text-decoration:none"><i class="operation_icon icon_see"></i></a>
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
		<script type="text/javascript" src="${base}/resources/admin1.0/js/common.js"></script>
		<script type="text/javascript" src="${base}/resources/admin1.0/js/list.js"></script>
		<script type="text/javascript">
		
			function queryById(id){
				window.location.href="/admin/messageManage/queryById.jhtml?id="+id;
			}
		
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