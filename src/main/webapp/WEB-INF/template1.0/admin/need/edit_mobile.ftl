[#assign shiro = JspTaglibs["/WEB-INF/tld/shiro.tld"] /]
[#escape x as x?html]
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, minimum-scale=1, user-scalable=no">
    <meta content="telephone=no,email=no" name="format-detection">
    <script src="${base}/resources/mobile/js/jquery.min.js"></script>
    <script src="${base}/resources/mobile/js/adaptive.js"></script>
    <link rel="stylesheet" href="${base}/resources/mobile/css/public.css" />
    <link rel="stylesheet" href="${base}/resources/mobile/css/mobileSelect.css" />
    <link rel="stylesheet" href="${base}/resources/mobile/css/customer.css" />
    <title>编辑个体客户</title>
</head>
<body>
<form id="inputForm" action="update.jhtml" method="post" class="form form-horizontal">
    <input type="hidden" name="oldTel" id="oldTel" value="${oldTel}"/>
    <input type="hidden" name="id" id="id" value="${need.id}" />
    <div class="need-add">
        <div class="customer-goods">
            <div class="goods-title">基本信息</div>
            <ul class="info_ul">
                <li class="li_info li_bot">
                    <span class="label">客户名称</span>
                    <div class="input">
                        <input type="text" id="name" value="${need.name}" placeholder="请填入客户名称 必填" name="name" class="info" />
                    </div>
                </li>
                <li class="li_info li_bot">
                    <span class="label">联系方式</span>
                    <div class="input">
                        <input type="text" id="tel" name="tel" value="${need.tel}" placeholder="此电话作为微商平台登录账号 必填" class="info">
                    </div>
                </li>
                <li class="li_info li_bot">
                    <span class="label">收货人</span>
                    <div class="input">
                        <input type="text" id="userName" value="${need.userName}" placeholder="请填入收货人姓名 必填" name="userName" class="info" />
                    </div>
                </li>
                <li class="li_info li_jiantou">
                    <span class="label">客户地区</span>
                    <div class="input">
                        <input type="text" class="goodsType_in" value="${need.area.fullName}" id="mobileSelect" class="info" unselectable="on" onfocus="this.blur()" placeholder="请选择客户所在地区 必选" readonly="readonly">
                        <input type="hidden" id="areaId" value="${need.area.id}" name="areaId" treePath="" />
                    </div>
                </li>
                <li class="li_info li_bot">
                    <span class="label">详细地址</span>
                    <div class="input">
                        <input type="text" id="address" name="address" value="${need.address}"  placeholder="请填写详细地址 必填">
                    </div>
                </li>

                <li class="li_info">
                    <span class="label">客户编号</span>
                    <div class="input">
                        <input type="text" id="clientNum" name="clientNum" value="${need.clientNum}"  placeholder="请填写客户编号 非必填" class="info">
                    </div>
                </li>
            </ul>
        </div>
        <div class="customer-info">
            <div class="info-title">联系人信息</div>
            <ul class="info_ul">
                <li class="li_info li_bot">
                    <span class="label">联系人</span>
                    <div class="input">
                        <input type="text" id="contacts" name="contacts" value="${need.contacts}" placeholder="请填写联系人姓名 非必填" class="info">
                    </div>
                </li>
                <li class="li_info li_bot">
                    <span class="label">联系方式</span>
                    <div class="input">
                        <input type="text" id="contactsTel" name="contactsTel" value="${need.contactsTel}" placeholder="请填写联系人电话 非必填" class="info">
                    </div>
                </li>
                <li class="li_info">
                    <span class="label">电子邮箱</span>
                    <div class="input">
                        <input type="text" id="email" name="email" value="${need.email}" placeholder="请填写联系人邮箱 非必填" class="info">
                    </div>
                </li>
            </ul>
        </div>
        <div class="finance-info">
            <div class="info-title">财务信息</div>
            <ul class="info_ul">
                <li class="li_info li_bot">
                    <span class="label">开户名称</span>
                    <div class="input">
                        <input id="accountName" name="accountName" value="${need.accountName}" type="text" placeholder="请填写开户名称 非必填" class="info">
                    </div>
                </li>
                <li class="li_info li_bot">
                    <span class="label">发票抬头</span>
                    <div class="input">
                        <input id="invoice" name="invoice" value="${need.invoice}" type="text" placeholder="请填写发票抬头 非必填" class="info">
                    </div>
                </li>
                <li class="li_info li_bot">
                    <span class="label">开户银行</span>
                    <div class="input">
                        <input id="bank" name="bank" value="${need.bank}" type="text" placeholder="请填写开户银行 非必填" class="info">
                    </div>
                </li>
                <li class="li_info li_bot">
                    <span class="label">银行账号</span>
                    <div class="input">
                        <input id="bankAccountNum" name="bankAccountNum" value="${need.bankAccountNum}" type="text" placeholder="请填写银行账号 非必填" class="info">
                    </div>
                </li>
                <li class="li_info">
                    <span class="label">纳税人号</span>
                    <div class="input">
                        <input id="identifier" name="identifier" value="${need.identifier}" type="text" placeholder="请填写纳税人识别码 非必填" class="info">
                    </div>
                </li>
            </ul>
        </div>
        <div class="saleman-info">
            <div class="info-title">所属业务员</div>
            <ul class="info_ul">
                <li class="li_info li_jiantou">
                    <span class="label">部门</span>
                    <div class="input">
                        <input type="text" class="info" id="department" unselectable="on" onfocus="this.blur()" readonly placeholder="[#if departmentTree.size() == 0]暂无部门信息[#else]请选择[/#if]">
                        <input type="hidden" class="downList_val" name="departmentId" id="departmentId" value="${need.admin.department.id}" />
                        <ul class="downList_con" id="departmentUl">
                            [#list departmentTree as department]
                            <li val="${department.id}" [#if department.id == need.admin.department.id] class="li_bag"[/#if]>[#if department.grade != 0][#list 1..department.grade as i]&nbsp;&nbsp;[/#list][/#if]${department.name}</li>
                            [/#list]
                        </ul>
                    </div>
                </li>
                <li class="li_info li_jiantou">
                    <span class="label">业务员</span>
                    <div class="input">
                        <input type="text" id="prompt" class="input-text radius down_list" unselectable="on" onfocus="this.blur()" readonly placeholder="请先选择部门">
                        <input type="hidden" id="adminId" name="adminId" value="${need.admin.id}" class="downList_val" />
                        <ul class="downList_con" id="adminUl">
                        </ul>
                    </div>
                </li>
            </ul>
        </div>
    </div>
    <div class="addSpace"></div>
    <input type="button" class="input_s input_B" id="yes" value="保存">
</form>
<script src="${base}/resources/mobile/js/validate/jquery.validate.min.js"></script>
<script src="${base}/resources/mobile/js/common.js"></script>
<script src="${base}/resources/mobile/js/picker.min.js"></script>
<script src="${base}/resources/mobile/js/city.js"></script>
<script src="${base}/resources/mobile/js/mobileSelect.js"></script>
<script>
    $(function(){

        var $inputForm = $("#inputForm");
        var $admin = $("#adminUl");
        var adminId="${need.admin.id}";
        var $adminDiv = $(".adminDiv");
        var departmentId="";

        [@flash_message /]


        //部门选择
        $('#department').val($('.li_bag').text());
        var departmentArray =[];
        var adminArray = [{value:'请先选择部门'}];
        //业务员选择
        var adminSelect1 = new MobileSelect({
            trigger: '#prompt',
            wheels: [
                {data:adminArray}
            ],
            position:[0], //初始化定位
            callback:function(indexArr, data){
                console.log(data[0].id); //返回选中的json数据
                $('#adminId').val(data[0].id);
            }
        });

        var departmentId=$("#departmentId").val();
        $adminDiv.find("input").val('');
        var teArray =[];
        $.get("${base}/admin/admin/getListByDepartment.jhtml",{"departmentId":departmentId},function(o){
            var data=o.data;
            $("#prompt").val('');
            if (departmentId == '') {
                $("#prompt").attr("placeholder","请先选择部门");
            }else if (data.length==0) {
                $("#prompt").attr("placeholder","暂无业务员信息");
            }else if (data.length>0) {
                $("#prompt").attr("placeholder","请选择");
            }

            for (var i = 0; i < data.length; i++) {
                if(adminId == data[i].id){
                    $('#prompt').val(data[i].name);
                }
                var itemArray ={};
                itemArray.id = data[i].id;
                itemArray.value = data[i].name;
                teArray.push(itemArray);
            }
//            console.log(teArray);
            adminSelect1.updateWheel(0,teArray);
        });

        $('#departmentUl li ').each(function () {
            var itemArray ={};
            itemArray.id = $(this).attr('val');
            itemArray.value = $(this).text();
            departmentArray.push(itemArray);
        });
        //底部滚动
        var mobileSelect1 = new MobileSelect({
            trigger: '#department',
            wheels: [
                {data:departmentArray}
            ],
            position:[0], //初始化定位
            callback:function(indexArr, data){
                //console.log(data[0].id); //返回选中的json数据
                $('#departmentId').val(data[0].id);
                var departmentId=$("#departmentId").val();
                $adminDiv.find("input").val('');
                var teArray =[];
                $.get("${base}/admin/admin/getListByDepartment.jhtml",{"departmentId":departmentId},function(o){
                    var data=o.data;
                    $("#prompt").val('');
                    $("#adminId").val('');
                    if (departmentId == '') {
                        $("#prompt").attr("placeholder","请先选择部门");
                    }else if (data.length==0) {
                        $("#prompt").attr("placeholder","暂无业务员信息");
                    }else if (data.length>0) {
                        $("#prompt").attr("placeholder","请选择");
                    }

                    for (var i = 0; i < data.length; i++) {
                        var itemArray ={};
                        itemArray.id = data[i].id;
                        itemArray.value = data[i].name;
                        teArray.push(itemArray);
                    }
                    adminSelect1.updateWheel(0,teArray);
                });
            }
        });



        // 表单验证
        $inputForm.validate({
                rules: {
                    name: {
                        required: true
                    },
                    userName: {
                        required: true
                    },
                    clientName: {
                        required: true
                    },
                    tel: {
                        required: true,
                        pattern: /^1[3|4|5|7|8]\d{9}$/

                    },
                    areaId: {
                        required: true
                    },
//                    areaName: {
//                        required: true
//                    },
                    address:{
                        required:true
                    }
                },
                messages: {
                    name:'必填',
                    userName:'必填',
                    clientName:'必填',
//                    areaName:'必选',
                    tel:{
                        pattern:"手机格式不正确",
                        required:'必填'
                    },
                    address:{
                        required:"必填"
                    }

                }
            });


        $("#yes").on("click",function(){
            if(!$("#inputForm").valid()){
                return false ;
            }
            $("#inputForm").submit();
        });

    });

    //省市县区域选择

    //地区下拉框初始化
    var defaultData=[{"id":"",value:""}];
    //判断 第几个轮子滑动
    var selectIndexArr=[0,0,0];
    var mobileSelect = new MobileSelect({
        trigger: '#mobileSelect',
        wheels: [
            {data: [{}]},
            {data: [{}]},
            {data: defaultData}
        ],
        transitionEnd:function(indexArr, data){
            //判断是哪个滑动的哪个轮子
            for (var i = 0; i < selectIndexArr.length-1; i++) {
                if (selectIndexArr[i] != indexArr[i]) {
                    selectIndexArr=indexArr;
                    loadArea(i+1,data[i].id);
                    mobileSelect.locatePostion(i+1,0);
                    break;
                }
            }
        },
        callback:function(indexArr, data){
            for (var i = data.length - 1; i >= 0; i--) {
                if (data[i].id != "") {
                    $("#areaId").val(data[i].id);
                    $inputForm.valid();
                    break;
                }
            }
        }
    });
    //加载第一层
    loadArea(0);
    function loadArea(index,parentId){
        $.ajax({
            type: "GET",
            url: "../common/area.jhtml",
            async: false,
            data: {"parentId":parentId},
            success: function (o) {
                var data=[];
                for (var i =0; i< o.length ; i++) {
                    data.push({
                        "id":o[i].value,
                        "value":o[i].name
                    });
                }
                if (data.length > 0) {
                    mobileSelect.updateWheel(index,data);
                    loadArea(index+1,data[0].id);
                }else{
                    //如果下一层没有数据，则把后面所有层级置空
                    for (var j = index; j < mobileSelect.wheelsData.length; j++) {
                        mobileSelect.updateWheel(j,defaultData);
                    }
                }
            }
        });
    }

</script>
</body>
</html>
[/#escape]