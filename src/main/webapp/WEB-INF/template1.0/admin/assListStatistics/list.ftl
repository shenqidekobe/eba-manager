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
        .daochuDown .check{
                left:-50px;
            }
    </style>
</head>
<body >
<div class="child_page"><!--内容外面的大框-->
    <div class="cus_nav">
        <ul>
            <li><a id="goHome"	href="../homePage/index.jhtml">${message("admin.breadcrumb.home")}</a></li>
            <li>清单统计</li>
        </ul>
    </div>
    <form id="listForm" action="list.jhtml" method="get">

        <div class="ch_condition">
            <div class="ch_search">
                <img class="search_img" src="${base}/resources/admin1.0/images/sousuo_icon.svg" alt="" />
                <div class="search_input">
                    <input type="text" id="searchValue" name="searchValue" value="${searchValue}" maxlength="200"
                           placeholder="请输入编号/员工/收货人" />

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

                <div class="ch_operate">
                    <button type="button" class="op_button update_B" id="refreshButtons" onclick="javascript:window.location.href='list.jhtml'">${message("admin.common.refresh")}</button>
                    <div class="daochuDown" id="export">
                    <button type="button" class="op_button daochu_B" id="downButton">导出</button>
                    <ul class="check">
                        <li name="export" val="report">导出批量清单</li>
                        <li name="export" val="selectedReport">导出选中清单</li>
                    </ul>
                    </div>
                </div>
        </div>

        <div class="table_con">
            <table class="table table-border table-hover table_width" id="box">
                <thead>
                <tr class="text-l">
                    <th width="4%"><input class="selectAll" type="checkbox" id="selectAll" id="selectAll"></th>
                    <th width="10%">清单编号</th>
                    <th width="8%">员工</th>
                    <th width="10%">下单人</th>
                    <th width="16%">主题</th>
                    <th width="17%">采购方</th>
                    <th width="8%">收货人</th>
                    <th width="6%">状态</th>
                    <th width="15%">创建日期</th>
                    <th width="6%">${message("admin.common.action")}</th>
                </tr>
                </thead>
            </table>
            <div id="listTable" class="list_t_tbody">
                <table class="table table-border table-hover table_width">
                    <thead>
                    <tr class="text-l">
                        <th width="4%" style="">
                            <div class="th_div" style=""></div>
                        </th>
                        <th width="10%"><div class="th_div">清单编号</div></th>
                        <th width="8%"><div class="th_div">员工</div></th>
                        <th width="10%"><div class="th_div">下单人</div></th>
                        <th width="16%"><div class="th_div">主题</div></th>
                        <th width="17%"><div class="th_div">采购方</div></th>
                        <th width="8%"><div class="th_div">收货人</div></th>
                        <th width="6%"><div class="th_div">状态</div></th>
                        <th width="15%"><div class="th_div">创建日期</div></th>
                        <th width="6%"><div class="th_div">${message("admin.common.action")}</div></th>
                    </tr>
                    </thead>
                    <tbody>
                    [#list page.content as data]
                        <tr class="text-l">
                            <td><input type="checkbox" name="ids" value="${data.assList.id}"></td>
                            <td>${data.assList.sn}</td>
                            <td>${data.adminName}</td>
                            <td>${data.assList.singlePerson}</td>
                            <td>${data.pTheme}</td>
                            <td>${data.assList.addressName}</td>
                            <td>${data.assList.name}</td>
                            <td>
                                [#if data.assList.status??]
                                    [#assign status=data.assList.status]
                                    ${message("AssList.Status."+status)}
                                [/#if]
                            </td>
                            <td>${(data.assList.createDate?string('yyyy-MM-dd HH:mm:ss'))!}</td>
                            <td class="td-manage">
                                <a title="查看" href="details.jhtml?id=${data.assList.id}" class="ml-5" style="text-decoration:none"><i
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
        /*导出*/
        $(".daochuDown li").on("click",function(){
            $(this).parent().siblings(".timeType").html($(this).html());
            $(".check").css("display","none");
        });
        $(".daochuDown").mouseover(function(){
            $(this).find("ul").css("display","block");
        });
        $(".daochuDown").mouseout(function(){
            $(this).find("ul").css("display","none");
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

        //导出
        $("#export li").click(function(){
            var $this = $(this);
            var $exp = $this.attr("val");
            var ids = [];
            $("input[name='ids']:checked").each(function(){
                ids.push($(this).val());
            });
            if($exp == "report") {
                $listForm.attr("action", "getOutReportDownload.jhtml");

                $listForm.submit();

                $listForm.attr("action", "list.jhtml");
                return true;
            }
            if($exp == "selectedReport") {
                if(ids.length == 0) {
                    $.message({'type':'error' , 'content':'请选择至少一条记录'});
                    return false;
                }
                $listForm.attr("action", "getOutSelectedReport.jhtml");

                $listForm.submit();

                $listForm.attr("action", "list.jhtml");
                return true;
            }
            
        });

    });



</script>

</body>
</html>
[/#escape]