[#assign shiro = JspTaglibs["/WEB-INF/tld/shiro.tld"] /]
[#escape x as x?html]
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${message("admin.admin.list")} - Powered By DreamForYou</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <link rel="stylesheet" type="text/css" href="${base}/resources/admin1.0/H-ui/static/h-ui/css/H-ui.min.css"/>
    <link rel="stylesheet" type="text/css" href="${base}/resources/admin1.0/H-ui/static/h-ui.admin/css/H-ui.admin.css"/>
    <link rel="stylesheet" href="${base}/resources/admin1.0/css/public.css"/>
    <link rel="stylesheet" href="${base}/resources/admin1.0/css/admin.css"/>
    <style>
        body {
            background: #f9f9f9;
        }
    </style>

</head>
<body>
<div class="child_page"><!--内容外面的大框-->
    <div class="cus_nav">
        <ul>
            <li><a id="goHome" href="../homePage/index.jhtml">${message("admin.breadcrumb.home")}</a></li>
        [#--<li>${message("admin.ad.list")} <span>(${message("admin.page.total", page.total)})</span></li>--]
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
                [@shiro.hasPermission name = "admin:ver:add"]
                <button type="button" class="op_button add_B" onclick="add();">${message("admin.common.add")}</button>
                [/@shiro.hasPermission]
                <button type="button" class="op_button update_B" id="refreshButtons"
                        onclick="javascript:window.location.href='list.jhtml'">${message("admin.common.refresh")}</button>
                [@shiro.hasPermission name = "admin:ver:impl"]
                <button type="button" class="op_button print_B" onclick="createTxt();">创建TXT</button>
               [/@shiro.hasPermission]
            </div>
        </div>

        <div class="table_con">
            <table class="table table-border table-hover table_width">
                <thead>
                <tr class="text-l">
                    <th width="15%">
                        <div class="th_div">ID</div>
                    </th>
                    <th width="8%">
                        <div class="th_div">批次</div>
                    </th>
                    <th width="25%">
                        <div class="th_div">标识</div>
                    </th>
                    <th width="20%">
                        <div class="th_div">创建时间</div>
                    </th>
                    <th width="20%">
                        <div class="th_div">验证时间</div>
                    </th>
                </tr>
                </thead>
            </table>
            <div class="list_t_tbody" id="listTable">
                <table class="table table-border table-hover table_width">
                    <thead>
                    <tr class="text-l">
                        <th width="15%">
                            <div class="th_div">ID</div>
                        </th>
                        <th width="8%">
                            <div class="th_div">批次</div>
                        </th>
                        <th width="25%">
                            <div class="th_div">标识</div>
                        </th>
                        <th width="20%">
                            <div class="th_div">创建时间</div>
                        </th>
                        <th width="20%">
                            <div class="th_div">验证时间</div>
                        </th>
                    </tr>
                    </thead>
                    <tbody>
                        [#list page.content as ver]
                        <tr class="text-l">
                            <td>${ver.id}</td>
                            <td>${ver.batchNo}</td>
                            <td>${ver.tag}</td>
                            <td>
                                [#if ver.createDate??]
                                    <span title="${ver.createDate?string("yyyy-MM-dd HH:mm:ss")}">${ver.createDate}</span>
                                [#else]
                                    -
                                [/#if]
                            </td>
                            <td>
                                [#if ver.proofTime??]
                                    <span title="${ver.proofTime?string("yyyy-MM-dd HH:mm:ss")}">${ver.proofTime}</span>
                                [#else]
                                    -
                                [/#if]
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
    $(".list_t_tbody").css("height", heightObj);
    $(".table_width").css("width", $(".list_t_tbody").css("width"));

    function add() {
        window.location.href = "add.jhtml";
    }

    function createTxt() {
       // window.location.href = "impl.jhtml";
        $.dialog({
            type: "success",
            height:190,
            width:350,
            content: "输入批次号：<input type='text' id='batchNo' style='width:90px' min='0'>",
            onOk: function() {
                var batchNo=$("#batchNo").val();
                if(batchNo=='')return false;
                window.location.href = "impl2.jhtml?batchNo="+batchNo;
            },
            onShow:function(){
            	$(".xxDialog").css("top","150px");
            }
        });
    }

    $().ready(function () {

        [@flash_message /]
        $("#goHome").on("click", function () {
            var nav = window.top.$(".index_nav_one");
            nav.find("li li").removeClass('clickTo');
            nav.find("i").removeClass('click_border');
        })

    });
</script>
</body>
</html>
[/#escape]