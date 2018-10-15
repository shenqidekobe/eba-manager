
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
</head>
<body >
    <div class="child_page modelPage">
        <form id="listForm" action="supply.jhtml" method="get">
        	<input type="hidden" name="orderTotal" id="orderTotal" value="${orderTotal}" />
        	<input type="hidden" name="purchaseTotal" id="purchaseTotal" value="${purchaseTotal}" />
            <div class="table_con">
                <div class="list_t_tbody" id="listTable">
                    <table class="table table-border table-hover table_width">
                        <tbody>
                        	[#list page.content as newsPush]
                            <tr class="text-l">
                                <td>
                                    <div class="infoText">
                                    	[#if newsPush.status == 'unread']
                                        <i class="noRead"></i>
                                        [/#if]
                                        [#if newsPush.order.type == 'distribution']
                                        <a href="javascript:void(0);" onclick="jump(${newsPush.order.id},${newsPush.id},1)">
                                        <p class="content">
                                        ${newsPush.send}-${newsPush.receive}${message("OrderNewsPush.OrderStatus." + newsPush.orderStatus)},订单号${newsPush.order.sn}
                                        </p>
                                        </a>
                                        [#else]
                                        	<a href="javascript:void(0);" onclick="jump(${newsPush.order.id},${newsPush.id},0)">
	                                        <p class="content">
	                                        ${newsPush.send}-${newsPush.receive}${message("OrderNewsPush.OrderStatus." + newsPush.orderStatus)},订单号${newsPush.order.sn}
	                                        </p>
	                                        </a>
                                        [/#if]
                                        <span class="time">
                                        	${newsPush.createDate?string("yyyy-MM-dd HH:mm:ss")}
                                        </span>
                                    </div>
                                </td>
                            </tr>
                            
							[/#list]
                        </tbody>
                    </table>
                </div>
                <span class="allRead">全部标记为已读</span>
                [@pagination pageNumber = page.pageNumber totalPages = page.totalPages]
				[#include "/admin/include/pagination.ftl"]
				[/@pagination]
            </div>
        </form>
    </div>
    <script src="${base}/resources/admin1.0/js/jquery.min.js"></script>
    <script type="text/javascript" src="${base}/resources/admin1.0/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/admin1.0/js/list.js"></script>
    <script type="text/javascript">
        /*表格的高度，，随着电脑分倍率的变化而变化*/
        var heightObj = $(document.body).height()-100;
        $(".list_t_tbody").css("height",heightObj);
        
        function jump(orderId,newsPushId,type) {
        	
        	//javascript:window.parent.goToOrder('/admin/order/view.jhtml?id=${newsPush.order.id}')
        	$.ajax({
				url: "updateStatus.jhtml",
				type: "POST",
				async: false,
				data: {"id":newsPushId},
				dataType: "json",
				success: function(data) {

				}
			});
        	if(type == 1) {
        		window.parent.goToOrder('/admin/distributionOrder/view.jhtml?id='+orderId);
        	}else {
        		window.parent.goToOrder('/admin/order/view.jhtml?id='+orderId);
        	}
        	
        }
		
        $().ready(function() {
        	$(".allRead").on("click",function(){
        		$("#listForm").attr("action", "allMarkAsReadByOrder.jhtml");
        		$("#listForm").attr("method","POST");
				$("#listForm").submit();
				
        	});
        	var orderTotal = $("#orderTotal").val();
        	var purchaseTotal = $("#purchaseTotal").val();
        	if(orderTotal == 0){
        		window.top.$(".model_typeB .supply_Bu .ListNum").css("display","none");
        	}
        	if(purchaseTotal == 0) {
        		window.top.$(".model_typeB .purshase_Bu .ListNum").css("display","none");
        	}
        	var num = window.top.$(".model_typeB .supply_Bu .ListNum").html(orderTotal);
        	window.top.$(".model_typeB .purshase_Bu .ListNum").html(purchaseTotal);
        	
        });
    </script>
</body>
</html>
[/#escape]
