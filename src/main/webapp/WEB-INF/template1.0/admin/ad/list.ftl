[#escape x as x?html]
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${message("admin.admin.list")} - Powered By DreamForYou</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <link rel="stylesheet" type="text/css" href="${base}/resources/admin1.0/H-ui/static/h-ui/css/H-ui.min.css" />
    <link rel="stylesheet" type="text/css" href="${base}/resources/admin1.0/H-ui/static/h-ui.admin/css/H-ui.admin.css" />
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
            <li><a id="goHome" href="../homePage/index.jhtml">${message("admin.breadcrumb.home")}</a></li>
            <li>${message("admin.ad.list")} <span>(${message("admin.page.total", page.total)})</span></li>
        </ul>
    </div>
    <form id="listForm" action="list.jhtml" method="get">
        <div class="ch_condition">
		[#--<div class="ch_search">
            <img class="search_img" src="${base}/resources/admin1.0/images/sousuo_icon.svg" alt="" />
            <div class="search_input">
                <input type="text" id="searchName" name="searchName" value="${searchName}" maxlength="200" placeholder="请输入名称/地址/姓名/手机号" />
            </div>
        </div>--]

            <div class="ch_operate">
                <button type="button" class="op_button add_B" onclick="add();">${message("admin.common.add")}</button>
                <button type="button" class="op_button del_B disabled" id="deleteButton">${message("admin.common.delete")}<button>
                    <button type="button" class="op_button update_B" id="refreshButtons" onclick="javascript:window.location.href='list.jhtml'">${message("admin.common.refresh")}</button>
            </div>
        </div>

        <div class="table_con">
            <table class="table table-border table-hover table_width">
                <thead>
                <tr class="text-l">
                    <th width="4%"><input class="selectAll" type="checkbox" id="selectAll" value=""></th>
                    <th width="15%">${message("Ad.title")}</th>
                    <th width="25%">${message("Ad.adPosition")}</th>
                    <th width="10%">${message("Ad.type")}</th>
                    <th width="10%">${message("Ad.beginDate")}</th>
                    <th width="10%">${message("Ad.endDate")}</th>
                    <th width="10%">${message("admin.common.order")}</th>
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
                                <input class="selectAll" type="checkbox" id="selectAll">
                            </div>
                        </th>
						<th width="15%"><div class="th_div">${message("Ad.title")}</div></th>
                        <th width="25%"><div class="th_div">${message("Ad.adPosition")}</div></th>
                        <th width="10%"><div class="th_div">${message("Ad.type")}</div></th>
                        <th width="10%"><div class="th_div">${message("Ad.beginDate")}</div></th>
                        <th width="10%"><div class="th_div">${message("Ad.endDate")}</div></th>
                        <th width="10%"><div class="th_div">${message("admin.common.order")}</div></th>
                        <th width="8%"><div class="th_div">${message("admin.common.action")}</div></th>
                    </tr>
                    </thead>
                    <tbody>
						[#list page.content as ad]
                        <tr class="text-l">
                            <td>
                                <input type="checkbox" name="ids" value="${ad.id}" />
                            </td>
                            <td>
                                <span title="${ad.title}">${abbreviate(ad.title, 50, "...")}</span>
                            </td>
                            <td>
							${ad.adPosition.name}
                            </td>
                            <td>
							${message("Ad.Type." + ad.type)}
                            </td>
                            <td>
								[#if ad.beginDate??]
                                    <span title="${ad.beginDate?string("yyyy-MM-dd HH:mm:ss")}">${ad.beginDate}</span>
								[#else]
                                    -
								[/#if]
                            </td>
                            <td>
								[#if ad.endDate??]
                                    <span title="${ad.endDate?string("yyyy-MM-dd HH:mm:ss")}">${ad.endDate}</span>
								[#else]
                                    -
								[/#if]
                            </td>
                            <td>
							${ad.order}
                            </td>
                            <td>
                                <a href="edit.jhtml?id=${ad.id}" class="operation_icon icon_bianji"></a>
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

    /*表格的高度，，随着电脑分倍率的变化而变化*/
    var heightObj = $(document.body).height() - 170;
    $(".list_t_tbody").css("height",heightObj);
    $(".table_width").css("width", $(".list_t_tbody").css("width"));

    function add() {
        window.location.href="add.jhtml";
    }

    $().ready(function() {

		[@flash_message /]
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