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
    <title>添加企业供应</title>
</head>
<body >
    <form id="inputForm" action="save.jhtml" method="post" class="form form-horizontal">
        <div class="supply-info">
            <ul class="info_ul">
                <li class="li_info li_jiantou">
                    <span class="label">开始时间</span>
                    <div class="input">
                        <input type="text" id="startDate" name="beginDate" class="info chooseDate" placeholder="请选择开始时间 必选" unselectable="on" onfocus="this.blur()" readonly="readonly">
                    </div>
                </li>
                <li class="li_info li_jiantou">
                    <span class="label">结束时间</span>
                    <div class="input">
                        <input type="text" id="endDate" name="endDate" class="info chooseDate" placeholder="请选择结束时间 必选" unselectable="on" onfocus="this.blur()" readonly="readonly">
                    </div>
                </li>
                <li class="li_info li_jiantou">
                    <span class="label">企业客户</span>
                    <div class="input">
                        <input type="text" id="bysupplierId" name="bysupplierId" class="info" placeholder="请选择客户 必选" unselectable="on" onfocus="this.blur()" readonly="readonly">
                    </div>
                </li>
                <li class="li_info li_jiantou">
                    <span class="label">商品信息</span>
                    <div class="input">
                        <input type="text"  id="goods" class="info" placeholder="请选择商品信息 必选" unselectable="on" onfocus="this.blur()" readonly="readonly">
                    </div>
                </li>
            </ul>
        </div>

        <input type="button" id="yes" class="input_s input_B" value="确定">
    </form>

<script src="${base}/resources/mobile/js/validate/jquery.validate.min.js"></script>
<script src="${base}/resources/mobile/js/LCalendar.js"></script>
<script src="${base}/resources/mobile/js/common.js"></script>
    <script type="text/javascript">
        var storage=window.localStorage;
        //时间插件
        var startDate = new LCalendar();
        var endDate = new LCalendar();
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

//        new Date().getFullYear() + '-' + (new Date().getMonth() + 1) + '-' + new Date().getDate()

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

        //表单提交
        $('#submits').on('click',function () {
            $("#updateItemForm").submit();
        });

        $('#bysupplierId').on('click',function () {
            window.location.href="consultationMobile.jhtml";
        });

        $('#goods').on('click',function () {
            window.location.href="selectList.jhtml?goodListStatus=add";
        });
        //从本地获取数据
        var startDateVal=storage.getItem("startDate");
        var endDateVal=storage.getItem("endDate");
        var bysupplierId=storage.getItem("bysupplierId");
        var products=eval('(' + storage.getItem("products")+ ')');
        var isSelectNeed=false;
        var isSelectProduct=false;
        if (startDateVal != null) {
            $("#startDate").val(startDateVal);
        }
        if (endDateVal != null) {
            $("#endDate").val(endDateVal);
        }
        if (bysupplierId != null) {
            isSelectNeed=true;
            $("#bysupplierId").attr("placeholder","已选择客户");
        }
        if (products != null && products.length > 0) {
            isSelectProduct=true;
            $("#goods").attr("placeholder","已选择"+products.length+"种商品");
        }
        //保存
        var tempBool = true;
        $("#yes").on("click",function(){
            $(this).addClass("disabled");
            var startDateValue=$("#startDate").val();
            var endDateValue=$("#endDate").val();
            if (startDateValue == "") {
                errorInfoFun("请选择开始时间");
                $(this).removeClass("disabled");
                return;
            }
            if (endDateValue == "") {
                errorInfoFun("请选择结束时间");
                $(this).removeClass("disabled");
                return;
            }
            if (!isSelectNeed) {
                errorInfoFun("请选择客户");
                $(this).removeClass("disabled");
                return;
            }
            if (!isSelectProduct) {
                errorInfoFun("请选择商品信息");
                $(this).removeClass("disabled");
                return;
            }
            //时间段供应关系验证
            var isTrue=true;
            var params={"startDate":startDateValue,"endDate":endDateValue,"bysupplierId":bysupplierId};
            $.ajax({
                type: "GET",
                url: "verification.jhtml",
                async: false,
                data: params,
                dataType: "json",
                success: function (data) {
                    if(!data.isTrue){
                        isTrue=false;
                    }
                }
            });
            if(!isTrue){
                errorInfoFun("选择的企业该时间段已有供应关系！");
                $(this).removeClass("disabled");
                return;
            }
            var tempSaveJson = {};
            $.each(products,function(index,value) {
                tempSaveJson['supplierProductList['+index+'].products.id'] = value.id;
                tempSaveJson['supplierProductList['+index+'].supplyPrice'] = value.supplyPrice;
                tempSaveJson['supplierProductList['+index+'].minOrderQuantity'] = value.minOrderQuantity;
            });

            tempSaveJson['startDate']=startDateValue;
            tempSaveJson['endDate']=endDateValue;
            tempSaveJson['bysupplierId']=bysupplierId;
            console.log(tempSaveJson);
            $.ajax({
                type: "POST",
                url: "save.jhtml",
                data: tempSaveJson,
                dataType: "json",
                //contentType:'application/json;charset=utf-8', //设置请求头信息
                beforeSend: function () {

                },
                success: function (message) {
                    errorInfoFun("添加成功");
                    if (message.type == "success") {
                        setTimeout(function () {
                            location.href="list.jhtml";
                        }, 1000);
                    }
                },
                error: function (data) {
                    errorInfoFun("添加失败");
                    $(this).removeClass("disabled");
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
