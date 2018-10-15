[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <title>${message("admin.admin.list")} - Powered By DreamForYou</title>
    <meta name="author" content="UTLZ Team" />
    <meta name="copyright" content="UTLZ" />
    <link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/list.js"></script>
    <script type="text/javascript">
        function editNotice(id){


            $.dialog({
                title: "选择接收的消息",
                [@compress single_line = true]
                    content: '<div style="text-align:center ; padding:10px 0">
                        [#list types as type]
                            <input id="${type}" type="checkbox" name="types" value="${type}">${type.desc} <br\/>
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

        });
    </script>
</head>
<body>
<div class="breadcrumb">
    <a href="${base}/admin/common/index.jhtml">${message("admin.breadcrumb.home")}</a> &raquo; 消息通知 <span>(${message("admin.page.total", page.total)})</span>
</div>
<form id="listForm" action="list.jhtml" method="get">
    <div class="bar">
        <a href="javascript:;" id="addButton" class="iconButton">
            <span class="addIcon">&nbsp;</span>${message("admin.common.add")}
        </a>
        <div class="buttonGroup">
            <a href="javascript:;" id="deleteButton" class="iconButton disabled">
                <span class="deleteIcon">&nbsp;</span>${message("admin.common.delete")}
            </a>
            <a href="javascript:;" id="refreshButton" class="iconButton">
                <span class="refreshIcon">&nbsp;</span>${message("admin.common.refresh")}
            </a>
            <div id="pageSizeMenu" class="dropdownMenu">
                <a href="javascript:;" class="button">
				${message("admin.page.pageSize")}<span class="arrow">&nbsp;</span>
                </a>
                <ul>
                    <li[#if page.pageSize == 10] class="current"[/#if] val="10">10</li>
                    <li[#if page.pageSize == 20] class="current"[/#if] val="20">20</li>
                    <li[#if page.pageSize == 50] class="current"[/#if] val="50">50</li>
                    <li[#if page.pageSize == 100] class="current"[/#if] val="100">100</li>
                </ul>
            </div>
        </div>
    </div>
    <table id="listTable" class="list">
        <tr>
            <th class="check">
                <input type="checkbox" id="selectAll" />
            </th>
            <th>
                <a href="javascript:;">接受员昵称</a>
            </th>
            <th>
                <a href="javascript:;">接受消息类型</a>
            </th>
            <th>
                <a href="javascript:;" >绑定时间</a>
            </th>
            <th>
                <span>${message("admin.common.action")}</span>
            </th>
        </tr>
		[#list page.content as noticeUser]
            <tr>
                <td>
                    <input type="checkbox" name="ids" value="${noticeUser.id}" />
                </td>
                <td>
				${noticeUser.nickName}
                </td>
                <td>
				    [#if noticeUser.noticeTypes?has_content]
				        [#list noticeUser.noticeTypes as noticeType]
				            ${noticeType.type.desc}
				        [/#list]
				    [/#if]
                </td>
                <td>
                    <span title="${noticeUser.bindDate?string("yyyy-MM-dd HH:mm:ss")}">${noticeUser.bindDate?string("yyyy-MM-dd HH:mm:ss")}</span>
                </td>
                <td>
                    <a href="javascript:;" onclick="javascript:editNotice(${noticeUser.id});">[${message("admin.common.edit")}]</a>
                </td>
            </tr>
		[/#list]
    </table>
	[@pagination pageNumber = page.pageNumber totalPages = page.totalPages]
		[#include "/admin/include/pagination.ftl"]
	[/@pagination]
</form>
</body>
</html>
[/#escape]