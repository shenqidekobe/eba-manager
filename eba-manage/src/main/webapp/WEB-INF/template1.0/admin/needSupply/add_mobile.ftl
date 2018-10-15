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
    <link rel="stylesheet" href="${base}/resources/mobile/css/mobileSelect.css" />
    <title>添加个体供应</title>
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
                <span class="label">个体客户</span>
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
            <li class="li_info li_jiantou">
                <span class="label">供应模式</span>
                <div class="input">
                    <input type="text" id="listTitle" class="info" placeholder="请选择供应模式 必选" unselectable="on" onfocus="this.blur()" readonly="readonly">
                    <input type="hidden" name="assignedModel" value="" id="assignedModel" />
                </div>
            </li>
            <li class="li_info">
                <span class="label tellInfo">订单消息提醒</span>
                <div class="tell">
                    <img class="choose" id="news" src="${base}/resources/mobile/images/xuanze-a.png" alt="图片">
                    <input type="hidden" name="noticeDay" value="5" id="noticeDay" />
                </div>
            </li>
        </ul>
    </div>
    <div class="prompt">说明：开启消息提醒后，默认个体客户最后一次下单后5天进行微信消息提醒；若更改消息提醒时间，请登录www.dinghuo.me/admin，进行设置。</div>
    <input type="button" id="yes" class="input_s input_B" value="保存">
</form>

<script src="${base}/resources/mobile/js/validate/jquery.validate.min.js"></script>
<script src="${base}/resources/mobile/js/LCalendar.js"></script>
<script src="${base}/resources/mobile/js/common.js"></script>
<script src="${base}/resources/mobile/js/mobileSelect.js"></script>
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

    $('#startDate').on('input propertychange',function () {
        var minArr = $("#startDate").val().split('-');
        endDate.minY = ~~minArr[0];
        endDate.minM = ~~minArr[1];
        endDate.minD = ~~minArr[2];
        storage.setItem("needStartDate",$("#startDate").val());
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
        storage.setItem("needEndDate",$("#endDate").val());
    });

    $('#endDate').on('input propertychange',function () {
        storage.setItem("needEndDate",$("#endDate").val());
    });

    $('#news').on('click',function () {
        if(!$(this).hasClass('selected')){
            $(this).attr({'src':'${base}/resources/mobile/images/xuanze-b.png'}).addClass('selected');
            storage.setItem("news",true);
        }else{
            $(this).attr({'src':'${base}/resources/mobile/images/xuanze-a.png'}).removeClass('selected');
            storage.setItem("news",false);
        }
    });

    //供应模式下拉
    var isDistributionModel='${currSupplier.systemSetting.isDistributionModel}';
    var array = [{id:'STRAIGHT',value:'直销模式'}];
    if (isDistributionModel == 'true') {
        array.push({
            id:'BRANCH',
            value:'分销模式'
        });
    }
    var adminSelect = new MobileSelect({
        trigger: '#listTitle',
        title: '选择供应模式',
        wheels: [
            {data:array}
        ],
        position:[0], //初始化定位
        callback:function(indexArr, data){
            var listTitle={};
            listTitle.id=data[0].id;
            listTitle.value=data[0].value;
            listTitle.indexArr=indexArr;
            storage.setItem("listTitle",JSON.stringify(listTitle));
            $("#assignedModel").val(data[0].id);
        }
    });

    //表单提交
    $('#submits').on('click',function () {
        $("#updateItemForm").submit();
    });

    $('#bysupplierId').on('click',function () {
        window.location.href="needsMobile.jhtml";
    });

    $('#goods').on('click',function () {
        window.location.href="selectList.jhtml?goodListStatus=add";
    });

    var isSelectNeed=false;
    var isSelectProduct=false;

    //从本地获取数据
    var startDateVal=storage.getItem("needStartDate");
    var endDateVal=storage.getItem("needEndDate");
    var needIds=eval('(' + storage.getItem("needIds")+ ')');
    var products=eval('(' + storage.getItem("needProducts")+ ')');
    var listTitle=eval('(' + storage.getItem("listTitle")+ ')');
    var news=storage.getItem("news");
    if (startDateVal != null) {
        $("#startDate").val(startDateVal);
    }
    if (endDateVal != null) {
        $("#endDate").val(endDateVal);
    }
    if (needIds != null && needIds.length > 0) {
        isSelectNeed=true;
        $("#bysupplierId").attr("placeholder","已选择"+needIds.length+"个客户");
    }
    if (products != null && products.length > 0) {
        isSelectProduct=true;
        $("#goods").attr("placeholder","已选择"+products.length+"种商品");
    }
    if (listTitle != null) {
        $("#listTitle").val(listTitle.value);
        $("#assignedModel").val(listTitle.id);
        //下拉选中当前值
        adminSelect.locatePostion(0,listTitle.indexArr);
    }
    if (news != null) {
        if(news == 'true'){
            $("#news").attr({'src':'${base}/resources/mobile/images/xuanze-b.png'}).addClass('selected');
        }else{
            $("#news").attr({'src':'${base}/resources/mobile/images/xuanze-a.png'}).removeClass('selected');
        }
    }
    //保存
    $("#yes").on("click",function(){
        $(this).addClass("disabled");
        var startDateValue=$("#startDate").val();
        var endDateValue=$("#endDate").val();
        var assignedModel=$("#assignedModel").val();
        var openNotice=false;
        if ($("#news").hasClass('selected')) {
            $(this).removeClass("disabled");
            openNotice=true;
        }
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
        if (assignedModel == "") {
            errorInfoFun("请选择供应模式");
            $(this).removeClass("disabled");
            return;
        }

        //时间段供应关系验证
        var isTrue=true;
        var params={"startDate":startDateValue,"endDate":endDateValue,"needIds":needIds};
        $.ajax({
            type: "GET",
            url: "batchVerification.jhtml",
            async: false,
            data: params,
            dataType: "json",
            success: function (o) {
                var data=o.data;
                if (data.length > 0) {
                    isTrue=false;
                    var errMsg="";
                    for (var i = 0; i < data.length; i++) {
                        if (i==data.length-1) {
                            errMsg+=data[i];
                        }else{
                            errMsg+=data[i]+"、";
                        }
                    }
                    errMsg+="客户该时间段已有供应关系！";
                    errorInfoFun(errMsg);
                }
            }
        });
        if(!isTrue){
            tempBool = false;
            $(this).removeClass("disabled");
            return;
        }
        var tempSaveJson = {};
        $.each(products,function(index,value) {
            tempSaveJson['needProductList['+index+'].products.id'] = value.id;
            tempSaveJson['needProductList['+index+'].supplyPrice'] = value.supplyPrice;
            tempSaveJson['needProductList['+index+'].minOrderQuantity'] = value.minOrderQuantity;
        });

        tempSaveJson['startDate']=startDateValue;
        tempSaveJson['endDate']=endDateValue;
        tempSaveJson['needIds']=needIds;
        tempSaveJson['openNotice']=openNotice;
        tempSaveJson['noticeDay']=$("#noticeDay").val();
        tempSaveJson['assignedModel']=assignedModel;
        console.log(tempSaveJson);
        $.ajax({
            type: "POST",
            url: "batchSave.jhtml",
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
