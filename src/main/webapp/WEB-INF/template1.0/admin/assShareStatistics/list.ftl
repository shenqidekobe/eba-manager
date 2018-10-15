[#assign shiro = JspTaglibs["/WEB-INF/tld/shiro.tld"] /]
[#escape x as x?html]
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${message("admin.goods.list")} - Powered By DreamForYou</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <link rel="stylesheet" type="text/css" href="${base}/resources/admin1.0/H-ui/static/h-ui/css/H-ui.min.css" />
    <link rel="stylesheet" href="${base}/resources/admin1.0/js/date/dateRange.css" />
    <link rel="stylesheet" href="${base}/resources/admin1.0/css/public.css" />
    <link rel="stylesheet" href="${base}/resources/admin1.0/css/admin.css" />
    <style>
        body{background:#f9f9f9;}
        .child_page {
            width: calc(100% - 20px);
            height: calc(100% - 20px);
        }
        .search_button{margin-left:36px;}
        .trColor{
            background-color:#f9f9f9;
        }
        .timeType[type="text"]{float:left;border:0;width:60px;height:30px;background:#f9f9f9;}
        .table-hover tbody tr.trColor:hover td{background-color:#fff;color:#999;}
        .table-hover tbody tr.trColor td{background-color:#fff;color:#999;}
    </style>
</head>
<body >
<div class="child_page"><!--内容外面的大框-->
    <div class="cus_nav">
        <ul>
            <li><a
                    id="goHome"	href="../homePage/index.jhtml">${message("admin.breadcrumb.home")}</a></li>
            <li>分享统计 <span>(${message("admin.page.total", page.total)})</span></li>
        </ul>
    </div>
    <form id="listForm" action="list.jhtml" method="get">
        <input type="hidden" name="orderBy" id="orderBy">
        <div class="ch_condition">
            <div class="ch_search">
                <img class="search_img" src="${base}/resources/admin1.0/images/sousuo_icon.svg" alt="" />
                <div class="search_input">
                    <input type="text" id="searchName" name="searchName" value="${searchName}" maxlength="200" placeholder="输入员工/主题" />

                </div>
            </div>
            <div>
                <input class="timeType" type="text" readonly="" value="分享时间" />
                <div class="ch_time">
                    <span class="chooseTime">${(startDate?string("yyyy-MM-dd"))!}~${(endDate?string("yyyy-MM-dd"))!}</span>
                    <input type="hidden" class="startTime" id="startDate" name="startDate" value="${(startDate?string('yyyy-MM-dd'))!}"/>
                    <input type="hidden" class="endTime" id="endDate" name="endDate" value="${(endDate?string('yyyy-MM-dd'))!}"/>
                    <div class="ta_date" id="div_date_demo3">
                        <span class="date_title" id="date_demo3"></span>
                        <a class="opt_sel" id="input_trigger_demo3" href="#"></a>
                    </div>
                </div>
            </div>

            <button type="submit" class="search_button">查询</button>
            <div class="ch_operate">
                <button type="button" class="op_button update_B" id="refreshButtons" onclick="javascript:window.location.href='list.jhtml'">${message("admin.common.refresh")}</button>
            </div>
            <div class="sortDiv">
                <div class="pageNum">
                    <span>按页面访问次数</span>
                    <div class="sanjiao">
                        <img class="pageGrayTop imgGray [#if orderBy == 'pageAsc'] imgNone [/#if]" data-orderby="pageAsc"
                             src="${base}/resources/admin1.0/images/paixu-c.svg" alt="">
                        <img class="pageBlueTop imgBlue [#if orderBy != 'pageAsc'] imgNone [/#if]" data-orderby="" src="${base}/resources/admin1.0/images/paixu-d.svg"
                             alt="">
                        <img class="pageGrayBottom imgGray [#if orderBy == 'pageDesc'] imgNone [/#if]" data-orderby="pageDesc" src="${base}/resources/admin1.0/images/paixu-a.svg" alt="">
                        <img class="pageBlueBottom imgBlue [#if orderBy != 'pageDesc'] imgNone [/#if]" data-orderby="" src="${base}/resources/admin1.0/images/paixu-b.svg" alt="">
                    </div>
                </div>
                <div class="goodsNum">
                    <span>按商品浏览次数</span>
                    <div class="sanjiao">
                        <img class="goodGrayTop imgGray  [#if orderBy == 'goodsAsc'] imgNone [/#if]" data-orderby="goodsAsc" src="${base}/resources/admin1.0/images/paixu-c.svg" alt="">
                        <img class="goodBlueTop imgBlue  [#if orderBy != 'goodsAsc'] imgNone [/#if]" data-orderby="" src="${base}/resources/admin1.0/images/paixu-d.svg" alt="">
                        <img class="goodGrayBottom imgGray  [#if orderBy == 'goodsDesc'] imgNone [/#if]" data-orderby="goodsDesc" src="${base}/resources/admin1.0/images/paixu-a.svg" alt="">
                        <img class="goodBlueBottom imgBlue  [#if orderBy != 'goodsDesc'] imgNone [/#if]" data-orderby="" src="${base}/resources/admin1.0/images/paixu-b.svg" alt="">
                    </div>
                </div>
            </div>


            <!--<div class="ch_operate">-->
                <!--[@shiro.hasPermission name = "admin:goods:distribution"]-->
                <!--[#if currSupplier.systemSetting.isDistributionModel]-->
                <!--<button type="button" class="op_button fenxiao_B" onclick="javascript:location.href='distribution.jhtml'">分销商品</button>-->
                <!--[/#if]-->
                <!--[/@shiro.hasPermission]-->
                <!--[@shiro.hasPermission name = "admin:goods:add"]-->
                <!--<button type="button" class="op_button add_B" onclick="javascript:location.href='add.jhtml'">${message("admin.common.add")}</button>-->
                <!--[/@shiro.hasPermission]-->
                <!--[@shiro.hasPermission name = "admin:goods:delete"]-->
                <!--<button type="button" class="op_button del_B " id="deleteButtons">${message("admin.common.delete")}<button>-->
                    <!--[/@shiro.hasPermission]-->

                    <!--<button type="button" class="op_button update_B" id="refreshButtons" onclick="javascript:window.location.href='list.jhtml'">${message("admin.common.refresh")}</button>-->
            <!--</div>-->
        </div>

        <div class="table_con">
            <table class="table table-border table-hover table_width" id="box">
                <thead>
                <tr class="text-l">
                    <th width="15%">员工</th>
                    <th width="20%">主题</th>
                    <th width="10%">分享类型</th>
                    <th width="10%">页面访问次数</th>
                    <th width="10%">商品浏览次数</th>
                    <th width="15%">分享时间</th>
                    <th width="10%">${message("admin.common.action")}</th>
                </tr>
                </thead>
            </table>
            <div id="listTable" class="list_t_tbody">
                <table class="table table-border table-hover table_width">
                    <thead>
                    <tr class="text-l">
                        <th width="15%"><div class="th_div">员工</div></th>
                        <th width="20%"><div class="th_div">主题</div></th>
                        <th width="10%"><div class="th_div">分享类型</div></th>
                        <th width="10%"><div class="th_div">页面访问次数</div></th>
                        <th width="10%"><div class="th_div">商品浏览次数</div></th>
                        <th width="15%"><div class="th_div">分享日期</div></th>
                        <th width="10%"><div class="th_div">${message("admin.common.action")}</div></th>
                    </tr>
                    </thead>
                    <tbody>
                        [#list page.content as shareStatistics]
                            <tr class="text-l">
                                <td>${shareStatistics.assCustomerRelation.adminName}</td>
                                <td>${shareStatistics.assCustomerRelation.theme}</td>
                                <td>
                                    [#if shareStatistics.assCustomerRelation.type??]
                                        [#assign type=shareStatistics.assCustomerRelation.type]
                                        ${message("AssCustomerRelation.type."+type)}
                                    [/#if]
                                </td>
                                <td>${shareStatistics.pageVisit}</td>
                                <td>${shareStatistics.goodsVisit}</td>
                                <td>${(shareStatistics.assCustomerRelation.createDate?string('yyyy-MM-dd HH:mm:ss'))!}
                                </td>
                                <td class="td-manage">
                                    <a title="查看" href="goodDetails.jhtml?id=${shareStatistics.assCustomerRelation.id}" class="ml-5" style="text-decoration:none"><i
                                            class="operation_icon icon_see"></i></a>
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

        [@flash_message /]

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
        })

        $("#pageGrayTop").on("click",function(){
            console.log(11);
        });

    });



    $(".sortDiv img").on("click",function(){
        $("#orderBy").val($(this).data("orderby"));
        $("#listForm").submit();
    });


</script>

</body>
</html>
[/#escape]