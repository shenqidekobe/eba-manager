[#assign shiro = JspTaglibs["/WEB-INF/tld/shiro.tld"] /]
[#escape x as x?html]
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>订单详情</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta http-equiv="X-UA-Compatible" content="IE=7; IE=EDGE">
    <script src="${base}/resources/mobile/js/jquery.min.js"></script>
    <script src="${base}/resources/mobile/js/adaptive.js"></script>
    <link rel="stylesheet" href="${base}/resources/mobile/css/public.css" />
    <link rel="stylesheet" href="${base}/resources/mobile/css/order.css" />
    <style>
        html,body{background:#f5f5f5;}
    </style>
</head>
<body>
<!--用户申请取消-->
<form id="applyCancelForm" action="applyCancel.jhtml" method="post">
    <input type="hidden" name="id" value="${order.id}"/>
    <input type="hidden" id="passed" name="passed"/>
</form>
<!--订单取消-->
<form id="cancelForm" action="cancel.jhtml" method="post">
    <input type="hidden" name="id" value="${order.id}"/>
</form>
<!--订单完成-->
<form id="completeForm" action="complete.jhtml" method="post">
    <input type="hidden" name="id" value="${order.id}"/>
</form>
<!--订单审核-->
<form id="reviewForm" action="review.jhtml" method="post">
    <input type="hidden" name="id" value="${order.id}"/>
    <input type="hidden" name="passed"/>
</form>
<!--修改订单-->
<form id="editView" [#if order.supplyType == 'temporary'] action="editView.jhtml" [#else] action="editViewByFormal.jhtml" [/#if]  method="get">
<input type="hidden" name="id" value="${order.id}"/>
</form>
<div class="info-box">
    <div class="info-top">
        <div class="info-status">
            <span class="${order.status}s"></span>
            ${message("Order.Status." + order.status)}
        </div>
        <div class="info-code">
            <div class="code-name">订单编号</div>
            <div class="code">${order.sn}</div>
        </div>
        <div class="info-time">
            <div class="time-name">下单时间</div>
            <div class="time">${order.createDate?string("yyyy-MM-dd HH:mm:ss")}</div>
        </div>
    </div>
    <div class="info-content">
        <div class="take-info">收货信息</div>
        <div class="take-na">
            <div class="take-name">收货点名称</div>
            <div class="take-content name">${order.need.name}</div>
        </div>
        <div class="take-address">
            <div class="take-name">地址信息</div>
            <div class="take-content address">
                <p>${order.consignee} ${order.need.tel}</p>
                ${order.areaName} ${order.address}
            </div>
        </div>
        <!--<div class="take-people">
            <div class="take-name">联系人</div>
            <div class="take-content people"></div>
        </div>
        <div class="take-phone">
            <div class="take-name">手机号</div>
            <div class="take-content phone"></div>
        </div>-->
        <div class="take-ti">
            <div class="take-name">收货时间</div>
            <div class="take-content take-time">${order.reDate?string("yyyy-MM-dd")}</div>
        </div>
    </div>
    <div class="info-other">
        <ul class="info_ul">
            <li class="li_info li_jiantou">
                <span class="label">备注信息</span>
                <div class="input">
                    <a class="info-a" href="remarksList.jhtml?id=${order.id}">
                        [#if orderRemarksSize>0]
                        <span class="number">${orderRemarksSize}</span>
                        [/#if]
                    </a>
                </div>
            </li>
            <li class="li_info li_jiantou">
                <span class="label">商品信息</span>
                <div class="input">
                    <a class="info-a" href="productInfo.jhtml?id=${order.id}">
                        [#if quantitySize>0]
                        <span class="goodNumber">共${quantitySize}件商品</span>
                        [/#if]
                    </a>
                </div>
            </li>
            <li class="li_info li_jiantou">
                <span class="label">物流信息</span>
                <div class="input">
                    <a class="info-a" href="logisticsInfo.jhtml?id=${order.id}">
                        [#if shippingSize>0]
                        <span class="number">${shippingSize}</span>
                        [/#if]
                    </a>
                </div>
            </li>
            <li class="li_info li_jiantou">
                <span class="label">订单附件</span>
                <div class="input">
                    <a class="info-a" href="orderAttachmentList.jhtml?id=${order.id}">
                        [#if orderFilesSize>0]
                        <span class="number">${orderFilesSize}</span>
                        [/#if]
                    </a>
                </div>
            </li>
            <li class="li_info li_jiantou">
                <span class="label">订单日志</span>
                <div class="input">
                    <a class="info-a" href="ownLog.jhtml?id=${order.id}">
                        [#if orderLogSize>0]
                        <span class="number">${orderLogSize}</span>
                        [/#if]
                    </a>
                </div>
            </li>
        </ul>
    </div>
    [#if order.status == "pendingReview" || ( !order.hasExpired() && order.status == "applyCancel")]
    <div class="info-bottom">
        <ul class="ul-bo">
            [#if order.status == "pendingReview"]
            <li data-status="review"  class="bagBlue">审核</li>
            <li data-status="cancel">取消</li>
            [/#if]
            [#if !order.hasExpired() && order.status == "applyCancel"]
                 <li data-status="applyCancel">用户申请</li>
            [/#if]
        </ul>
    </div>
    [/#if]
</div>
<div class="mutail">
    <div class="mutailContent">
        <div class="content_M" >
            <div class="check-box" id="magDiv">

            </div>
        </div>
        <span class="cancel">取消</span>
        <span class="delSure">确定</span>
    </div>
</div>

<script src="${base}/resources/mobile/js/common.js"></script>
<script>
    $(function () {
        //审核弹窗
        //        $(".review").on("click",function(){
        //            errorInfoFun("可见风使舵将发售");
        //        });

        //权限
        var canReview=false;
        var canCancel=false;
        var applyCancel=false;
        [@shiro.hasPermission name = "admin:distributionOrder:review"]
        canReview=true;
        [/@shiro.hasPermission]

        [@shiro.hasPermission name = "admin:distributionOrder:cancel"]
        canCancel=true;
        [/@shiro.hasPermission]

        [@shiro.hasPermission name = "admin:distributionOrder:applyCancel"]
        applyCancel=true;
        [/@shiro.hasPermission]

        var itemAdminUpdate=${order.itemAdminUpdate};
        var status;
        $(".ul-bo li").on("click",function(){
            console.log(123);
            status=$(this).data("status");
            if(status=="cancel"){
                if(!canCancel){
                    errorInfoFun("无权限！");
                    return;
                }
                $("#magDiv").html("是否确认取消该订单？");
            }else if(status=="review"){
                if(!canReview){
                    errorInfoFun("无权限！");
                    return;
                }
                var htmlTxt='<input type="radio" name="passed" value="true" checked="checked" class="passedClass" /><span>通过</span><input type="radio" name="passed" value="false" class="passedClass" /><span>不通过<span>';
                $("#magDiv").html(htmlTxt);
            }else if(status=="applyCancel"){
                if(!applyCancel){
                    errorInfoFun("无权限！");
                    return;
                }
                var htmlTxt='<input type="radio" name="passed" value="true" checked="checked" class="passedClass" /><span>通过</span><input type="radio" name="passed" value="false" class="passedClass" /><span>不通过<span>';
                $("#magDiv").html(htmlTxt);
            }

            $(".mutail").css("display","block");
        });

        $(".mutail .cancel").on("click",function(){
            $(".mutail").css("display","none");
        });

        $(".mutail .delSure").on("click",function(){
            $(".mutail").css("display","none");
            if(status=="cancel"){
                $("#cancelForm").submit();
            }else if(status=="review"){
                var passed=$('input:radio[name="passed"]:checked').val();
                $("#reviewForm").find("input[name=passed]").val(passed);
                $("#reviewForm").submit();
            }else if(status=="applyCancel"){
                var passed=$('input:radio[name="passed"]:checked').val();
                $("#applyCancelForm").find("input[name=passed]").val(passed);
                $("#applyCancelForm").submit();
            }
        });



    });

    $(function(){
        pushHistory();
        window.addEventListener("popstate", function(e) {
            window.location.href = 'list.jhtml';
        }, false);
        function pushHistory() {
            var state = {
                title: "title",
                url: "#"
            };
            window.history.pushState(state, "title", "#");
        }
    });
</script>
</body>
</html>
[/#escape]