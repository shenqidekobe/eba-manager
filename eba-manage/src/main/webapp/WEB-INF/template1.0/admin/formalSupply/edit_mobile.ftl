[#escape x as x?html]
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, minimum-scale=1, user-scalable=no">
    <meta content="telephone=no,email=no" name="format-detection">
    <script src="${base}/resources/mobile/js/jquery.min.js"></script>
    <script src="${base}/resources/mobile/js/adaptive.js"></script>
    <link rel="stylesheet" href="${base}/resources/mobile/css/public.css" />
    <link rel="stylesheet" href="${base}/resources/mobile/css/LCalendar.css" />
    <link rel="stylesheet" href="${base}/resources/mobile/css/supply.css" />
    <title>编辑企业供应</title>
    <style>
        .chooseDate{
            border:none;
        }
    </style>
</head>
<body >
<input type="hidden" id="supplierId" name="clientType" value="${supplierSupplier.id}" />
<input type="hidden" name="totalPages" id="totalPages" value="" />
<input type="hidden" name="pageNumber" id="pageNumber" value="1" />
<input type="hidden" name="pageSize" id="pageSize" value="" />
<form id="inputForm" action="update.jhtml" method="post" class="form form-horizontal">
<div class="choose-customer">
    <input type="hidden" name="id" value="${supplierSupplier.id}" />
    <div class="info-top">
        <div class="info-time li_jiantou">
            <div class="start-name">开始时间</div>
            <input type="text" id="startDate" name="startDate" value="${supplierSupplier.startDate}" class="chooseDate time" placeholder="请选择开始时间 必选" unselectable="on" onfocus="this.blur()" readonly="readonly">
        </div>
        <div class="info-time li_jiantou">
            <div class="end-name">结束时间</div>
            <input type="text" id="endDate" name="endDate" value="${supplierSupplier.endDate}" class="chooseDate time" placeholder="请选择结束时间 必选" unselectable="on" onfocus="this.blur()" readonly="readonly">
        </div>
        <div class="info-code li_bot">
            <div class="code-name">企业客户</div>
            <div class="code">${supplierSupplier.bySupplier.name}</div>
        </div>
    </div>
    <div class="goods">
        <div class="title">商品信息</div>
        <ul class="goods-list" id="customerList">

        </ul>
    </div>
    <div class="confirm-box">
        <!--<button class="refuse">拒绝</button>-->
        <div class="confirm">确定</div>
    </div>
    <p class="p_loading">正在加载...</p>
</div>
</form>

<script src="${base}/resources/mobile/js/LCalendar.js"></script>
<script src="${base}/resources/mobile/js/common.js"></script>
<script type="text/javascript">
    $(function () {
        var storage=window.localStorage;
        var startDate = new LCalendar();
        var endDate = new LCalendar();
        var $inputForm = $('#inputForm');
        var changeDate = '';
        startDate.init({
            'trigger': '#startDate', //标签id
            'type': 'date', //date 调出日期选择 datetime 调出日期时间选择 time 调出时间选择 ym 调出年月选择,
            'minDate': '1970-01-01', //最小日期
            'maxDate': '2100-01-01' //最大日期
        });
        endDate.init({
            'trigger': '#endDate', //标签id
            'type': 'date', //date 调出日期选择 datetime 调出日期时间选择 time 调出时间选择 ym 调出年月选择,
            'minDate': '1970-01-01', //最小日期
            'maxDate': '2100-01-01' //最大日期
        });

        $('#startDate').on('input propertychange',function () {
            var minArr = $("#startDate").val().split('-');
            endDate.minY = ~~minArr[0];
            endDate.minM = ~~minArr[1];
            endDate.minD = ~~minArr[2];
            storage.setItem("startDate",$("#startDate").val());
            if ($("#endDate").val() == "") {
                return;
            }
            var endDateArr = $("#endDate").val().split('-');
            //如果开始时间大于结束时间则把结束时间设置为开始时间
            if (~~minArr[0] > ~~endDateArr[0]) {
                $("#endDate").val($("#startDate").val());
            }else{
                if (~~minArr[1] > ~~endDateArr[1]) {
                    $("#endDate").val($("#startDate").val());
                }else{
                    if (~~minArr[2] > ~~endDateArr[2]) {
                        $("#endDate").val($("#startDate").val());
                    }
                }
            }
            storage.setItem("endDate",$("#endDate").val());
        });

        $('#endDate').on('input propertychange',function () {
            storage.setItem("endDate",$("#endDate").val());
        });

        //更改提交
        $('.confirm').on('click',function () {
            if($('#startDate').val() == ''){
                $.message('请选择开始时间');
                return false;
            }
            if($('#endDate').val() == ''){
                $.message('请选择结束时间');
                return false;
            }
            var parsms = $inputForm.serializeArray() ;
            $.ajax({
                type: "GET",
                url: "validDate.jhtml",
                async: false,
                data: parsms,
                dataType: "json",
                success: function (data) {
                    if(!data.isTrue){
                        isTrue=false;
                         errorInfoFun('选择的企业该时间段已有供应关系');
                    }else{
                        $inputForm.submit();
                    }
                }
            });
        });


        var boolAjax = false;
        //加载数据
        function loadData(){
            var pageNumber = $("#pageNumber").val();
            var pageSize = $("#pageSize").val();
            var totalPages = $("#totalPages").val();
            var supplierId = $("#supplierId").val();
            console.log(pageNumber+'----'+totalPages);
            if(parseInt(pageNumber) > parseInt(totalPages)) {
                $(".p_loading").html("");
                return;
            }
            var param={
                "pageNumber":pageNumber,
                "supplierSupplierId":supplierId,
                "pageSize":20
            };
            var next = ++pageNumber;
            $.get("asyncViewGoods.jhtml",param,function(o){
                if (o.code=='0') {
                    console.log(o);
                    var orders = o.data.list;
                    if(orders.length<4){
                        $(".p_loading").html("");
                    }
                    var textHtml='';
                    $.each(orders,function(i,n){
                        var name = '';
                        textHtml+='<li class="customer-li">';
                        textHtml+='<div class="good-item">';
                        if(n.image != ''){
                            textHtml+='<img src="'+n.image+'" alt="">';
                        }else{
                            textHtml+='<img src="${base}/resources/admin1.0/images/mr_icon.png" alt="">';
                        }
                        textHtml+='<div class="detail">';
                        textHtml+='<p class="goods-name">'+n.name+'</p>';
                        textHtml+='<p class="goodsTip">';
                        $.each(n.specificationValues,function (j,m) {
                            textHtml+='<span>'+m.value+'</span>';
                        });
                        textHtml+='</p></div></div>';
                        textHtml+='<div class="order-info">';
                        textHtml+='<div class="order-count">';
                        textHtml+='<span class="name">起订量</span>';
                        textHtml+='<input type="number" class="count" value="'+n.minOrderQuantity+'" unselectable="on" onfocus="this.blur()" readonly />';
                        textHtml+='</div>';
                        textHtml+='<div class="order-price">';
                        textHtml+='<span class="name">供货价(￥)</span>';
                        textHtml+='<input type="number" class="price" value="'+n.supplyPrice+'" unselectable="on" onfocus="this.blur()" readonly />';
                        textHtml+='</div>';
                        textHtml+='</div></li>';
                    });
                    $("#customerList").append(textHtml);
                    $("#pageSize").val(o.data.pageSize);
                    $("#totalPages").val(o.data.totalPages);
                    $("#pageNumber").val(next);
                    boolAjax = false;
                }else {
                    errorInfoFun(o.msg);
                }
            });
        }

        $(window).scroll(function(){
            var top = $(".p_loading").offset().top;
            var divScroll = $(window).scrollTop();
            var divHeight = $(window).height();
            if(divHeight-(top-divScroll)>70){
                if(boolAjax){return false;}
                boolAjax=true;
                loadData();
            }
        });
        loadData();
    });


</script>
</body>
</html>
[/#escape]
