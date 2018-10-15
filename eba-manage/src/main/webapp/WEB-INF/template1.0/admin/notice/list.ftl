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
			.td_span{margin-right:10px;cursor: pointer;}
		</style>
	</head>
	<body >
		<div class="child_page"><!--内容外面的大框-->	
			<div class="cus_nav">
				<ul>
					<li><a id="goHome"	href="../homePage/index.jhtml">${message("admin.breadcrumb.home")}</a></li>
					<li>消息通知 <span>(${message("admin.page.total", page.total)})</span></li>
				</ul>
			</div>
		<form id="listForm" action="list.jhtml" method="get">
			<div class="ch_condition">

                <div class="ch_search">
					<img class="search_img" src="${base}/resources/admin1.0/images/sousuo_icon.svg" alt="" />
					<div class="search_input">
						<input type="text" id="searchName" name="searchName" value="${searchName}" maxlength="200" placeholder="请输入接收员昵称" />
					</div>
				</div>
				
				<button type="submit" class="search_button">查询</button>

                <div class="ch_operate">
                	[@shiro.hasPermission name = "admin:notice:add"]
						<button type="button" class="op_button add_B" id="addButton">${message("admin.common.add")}</button>
					[/@shiro.hasPermission]
					[@shiro.hasPermission name = "admin:notice:delete"]
						<button type="button" class="op_button del_B disabled" id="deleteButton">${message("admin.common.delete")}<button>
					[/@shiro.hasPermission]
					<button type="button" class="op_button update_B" id="refreshButtons" onclick="javascript:window.location.href='list.jhtml'">${message("admin.common.refresh")}</button>
				</div>
			</div>
			
			<div class="table_con">
				<table class="table table-border table-hover table_width">
					<thead>
						<tr class="text-l">
							<th width="4%"><input class="all_checked" type="checkbox" id="selectAll"></th>
							<th width="16%">接收员昵称</th>
							<th width="25%">接收消息类型</th>
							<th width="20%">绑定时间</th>
							<th width="13%">${message("admin.common.action")}</th>
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
								<th width="16%"><div class="th_div">接收员昵称</div></th>
								<th width="25%"><div class="th_div">接收消息类型</div></th>
								<th width="20%"><div class="th_div">绑定时间</div></th>
								<th width="13%"><div class="th_div">${message("admin.common.action")}</div></th>
							</tr>
						</thead>
						<tbody>
							[#list page.content as noticeUser]
							<tr class="text-l">
								<td><input type="checkbox" value="${noticeUser.id}" name="ids"></td>
								<td>${noticeUser.nickName}</td>
								<td>
									[#if noticeUser.noticeTypes?has_content]
								        订货单
								    [/#if]

									[#if noticeUser.noticeTypePurchases?has_content]
                                        采购单
									[/#if]

								</td>
								<td><span title="${noticeUser.bindDate?string("yyyy-MM-dd HH:mm:ss")}">${noticeUser.bindDate?string("yyyy-MM-dd HH:mm:ss")}</span></td>
								<td class="td-manage">
									
									[#--<a title="${message("admin.common.edit")}" href="javascript:;" onclick="javascript:editNotice(${noticeUser.id});" class="ml-5" style="text-decoration:none"><i class="operation_icon icon_bianji"></i></a>--]
									[@shiro.hasPermission name = "admin:notice:orderNotice"]
										<span class="td_span" onclick="javascript:location.href='orderNotice.jhtml?id=${noticeUser.id}'">订货单通知</span>
									[/@shiro.hasPermission]
									[@shiro.hasPermission name = "admin:notice:purchaseNotice"]
										<span class="td_span" onclick="javascript:location.href='purchaseNotice.jhtml?id=${noticeUser.id}'">采购单通知</span>
									[/@shiro.hasPermission]
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
			
			function editNotice(id){
	            $.dialog({
	                title: "选择接收的消息",
	                [@compress single_line = true]
	                    content: '<div style="text-align:center ; padding-top:20px;">
	                        [#list types as type]
	                       	<div style="height:40px;">
	                            <input style="margin-right:10px;" id="${type}" type="checkbox" name="types" value="${type}">${type.desc} <br\/>
	                        </div>
	                        [/#list]

	                    <\/div>',
	                [/@compress]
	                width: 300,
	                modal: true,
	                ok: "${message("admin.dialog.ok")}",
	                cancel: "${message("admin.dialog.cancel")}",

	                onShow:function(){
	                    $.ajax({
	                        url: "getTypes.jhtml",
	                        type: "GET",
	                        dataType: "json",
	                        cache: false,
	                        data:{id:id},
	                        success: function(message) {
	                            if(message.code == "0"){
	                                var data = message.data ;
	                                $.each(data , function(key , item){
	                                    $("#" + item).attr("checked","true") ;
	                                });
	                            }else{

	                            }
	                        }
	                    });

	                },

	                onOk:function(){

	                    var types = $("input[name='types']:checked") ;
	                   /* if(types.length == 0){
	                        location.reload(true);
	                        return false ;
	                    }*/
	                    /*var datas = {} ;
	                    datas["id"] = id;
	                    types.each(function(){
	                        datas["types"]=$(this).val();
	                    });*/
	                    var results = types.serializeArray();
	                    results.push({name:"id" , value:id});
	                    $.ajax({
	                        url: "update.jhtml",
	                        type: "POST",
	                        dataType: "json",
	                        cache: false,
	                        data:results,
	                        success: function(message) {
	                            if (message.type == "success") {
	                                location.reload(true);
	                            } else {
	                                $.message(message);
	                            }
	                        }
	                    });
	                }

	            });
	        }


	        $().ready(function() {

				[@flash_message /]

	            $("#addButton").click(function() {
	                $.dialog({
	                    title: "添加消息接收员",
	                    [@compress single_line = true]
	                        content: '<img src="add.jhtml" alt=""\/>',
	                    [/@compress]
	                    width: 300,
	                    modal: true,
	                    ok: "${message("admin.dialog.ok")}",
	                    cancel: "${message("admin.dialog.cancel")}",
	                    onOk:function(){
	                        location.reload(true);
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