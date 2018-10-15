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
    <title>编辑企业客户</title>
</head>
<body>
<form id="inputForm" action="update.jhtml" method="post" class="form form-horizontal">
    <input type="hidden" name="id" id="id" value="${customerRelation.id}" />
    <div class="customer-add">
        <div class="customer-goods">
            <div class="goods-title">商品信息</div>
            <ul class="info_ul">
                <li class="li_info li_bot">
                    <span class="label">邀请码</span>
                    <div class="input">
                        <input id="inviteCode" name="inviteCode" type="text" value="${customerRelation.inviteCode}" readonly="readonly"  class="info">
                    </div>
                </li>
                <li class="li_info li_bot">
                    <span class="label">客户名称</span>
                    <div class="input">
                        <input type="text" id="clientName" name="clientName" value="${customerRelation.clientName}" readonly="readonly"   class="info">
                    </div>
                </li>
                <li class="li_info li_bot">
                    <span class="label">客户编号</span>
                    <div class="input">
                        <input type="text" id="clientNum" name="clientNum" value="${customerRelation.clientNum}"  placeholder="请输入客户编号" class="info">
                    </div>
                </li>
                <li class="li_info li_jiantou">
                    <span class="label">客户地区</span>
                    <div class="input">
                        <input type="text" class="goodsType_in" value="${customerRelation.area.fullName}" id="mobileSelect" class="info" placeholder="请选择客户所在地区" unselectable="on" onfocus="this.blur()" readonly="readonly">
                        <input type="hidden" id="areaId" name="areaId" value="${customerRelation.area.id}"  treePath="">
                    </div>
                </li>
                <li class="li_info">
                    <span class="label">详细地址</span>
                    <div class="input">
                        <input type="text" id="address" name="address" value="${customerRelation.address}"  placeholder="请输入详细地址">
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
                        <input type="text" id="userName" name="userName" value="${customerRelation.userName}" placeholder="请输入联系人" class="info">
                    </div>
                </li>
                <li class="li_info li_bot">
                    <span class="label">联系方式</span>
                    <div class="input">
                        <input type="text" id="tel" name="tel" value="${customerRelation.tel}" placeholder="请输入联系方式" class="info">
                    </div>
                </li>
                <li class="li_info">
                    <span class="label">电子邮箱</span>
                    <div class="input">
                        <input type="text" id="email" name="email" value="${customerRelation.email}" placeholder="请输入电子邮箱" class="info">
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
                        <input id="accountName" name="accountName" value="${customerRelation.accountName}" type="text" placeholder="请输入开户名称" class="info">
                    </div>
                </li>
                <li class="li_info li_bot">
                    <span class="label">发票抬头</span>
                    <div class="input">
                        <input id="invoice" name="invoice" value="${customerRelation.invoice}" type="text" placeholder="请输入发票抬头" class="info">
                    </div>
                </li>
                <li class="li_info li_bot">
                    <span class="label">开户银行</span>
                    <div class="input">
                        <input id="bank" name="bank" value="${customerRelation.bank}" type="text" placeholder="请输入开户银行" class="info">
                    </div>
                </li>
                <li class="li_info li_bot">
                    <span class="label">银行账号</span>
                    <div class="input">
                        <input id="bankAccountNum" name="bankAccountNum" value="${customerRelation.bankAccountNum}" type="text" placeholder="请输入银行账号" class="info">
                    </div>
                </li>
                <li class="li_info">
                    <span class="label">纳税人号</span>
                    <div class="input">
                        <input id="identifier" name="identifier" value="${customerRelation.identifier}" type="text" placeholder="请输入纳税人识别码" class="info">
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
                        <input type="text" id="department" class="input-text radius down_list" unselectable="on" onfocus="this.blur()" readonly placeholder="[#if departmentTree.size() == 0]暂无部门信息[#else]请选择[/#if]">
                        <input type="hidden" id="departmentId" class="downList_val" name="departmentId" value="${customerRelation.admin.department.id}" />
                        <ul class="downList_con" id="departmentUl">
                            [#list departmentTree as department]
                            <li val="${department.id}" [#if department.id == customerRelation.admin.department.id] class="li_bag"[/#if]>[#if department.grade != 0][#list 1..department.grade as i]&nbsp;&nbsp;[/#list][/#if]${department.name}</li>
                            [/#list]
                        </ul>
                        <!--<input type="text" readonly="readonly" id="department" name="department" value="" placeholder="暂无部门信息" class="info">-->
                    </div>
                </li>
                <li class="li_info li_jiantou">
                    <span class="label">业务员</span>
                    <div class="input">
                        <!--<input type="text" readonly="readonly" id="" name="tel" value="" placeholder="请先选择部门" class="info">-->
                        <input type="text" id="prompt" class="input-text radius down_list" unselectable="on" onfocus="this.blur()" readonly placeholder="请先选择部门">
                        <input type="hidden" id="adminId" name="adminId" value="${customerRelation.admin.id}" class="downList_val" />
                    </div>
                </li>
            </ul>
        </div>
    </div>
    <div class="addSpace"></div>
    <input type="button" id="yes" class="input_s input_B" value="保存">
</form>

<script src="${base}/resources/mobile/js/validate/jquery.validate.min.js"></script>
<script src="${base}/resources/mobile/js/common.js"></script>
<script src="${base}/resources/mobile/js/picker.min.js"></script>
<script src="${base}/resources/mobile/js/city.js"></script>
<script src="${base}/resources/mobile/js/mobileSelect.js"></script>
<script>
    $(function () {
        var $inputForm = $("#inputForm");
        var $admin = $("#adminUl");
        var adminId="${customerRelation.admin.id}";
        var $adminDiv = $(".adminDiv");
        var departmentId="";
        var $areaId = $("#areaId");
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
//                        if(adminId == data[i].id){
//                            $('#prompt').val(data[i].name);
//                        }
                        var itemArray ={};
                        itemArray.id = data[i].id;
                        itemArray.value = data[i].name;
                        teArray.push(itemArray);
                    }
                    adminSelect1.updateWheel(0,teArray);
                });
            }
        });






        /*表单验证*/
        $("#inputForm").validate({
            rules:{
                clientNum:{
                    maxlength:20
                },
                areaId:{
                    required:true
                },
                email:{
                    email:true
                },
                tel:{
                    pattern: /^1[3|4|5|7|8]\d{9}$/
                }
            },
            messages:{
                clientNum:{
                    maxlength:"最多支持20个字符"
                },
                areaId:{
                    required:"客户地区不能为空"
                },
                email:{
                    email:"请输入正确格式的电子邮件"
                },
                tel:{
                    pattern:"手机格式不正确"
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