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
    <title>添加企业客户</title>
</head>
<body>
<form id="inputForm" action="save.jhtml" method="post" class="form form-horizontal">
<input type="hidden" name="supplierId" id="supplierId" />
<div class="customer-add">
        <div class="customer-goods">
            <div class="goods-title">基本信息</div>
            <ul class="info_ul">
                <li class="li_info li_bot">
                    <span class="label">邀请码</span>
                    <div class="input">
                        <input id="inviteCode" name="inviteCode" type="text" value=""  placeholder="请填入客户邀请码 必填"  class="info">
                    </div>
                </li>
                <li class="li_info li_bot">
                    <span class="label">客户名称</span>
                    <div class="input">
                        <input type="text" id="clientName" name="clientName"  placeholder="填写邀请码后系统自动填写"  unselectable="on" onfocus="this.blur()" readonly="readonly"  class="info">
                    </div>
                </li>
                <li class="li_info li_jiantou">
                    <span class="label">客户地区</span>
                    <div class="input">
                        <input type="text" class="goodsType_in" id="mobileSelect" class="info" value="${supplier.area.fullName}" placeholder="请选择客户所在地区 非必填" unselectable="on" onfocus="this.blur()" readonly="readonly">
                        <input type="hidden" id="areaId" value="" name="areaId" treePath="" />
                    </div>
                </li>
                <li class="li_info li_bot">
                    <span class="label">详细地址</span>
                    <div class="input">
                        <input type="text" id="address" name="address" value=""  placeholder="请填写详细地址 非必填">
                    </div>
                </li>
                <li class="li_info">
                    <span class="label">客户编号</span>
                    <div class="input">
                        <input type="text" id="clientNum" name="clientNum" value=""  placeholder="请填写客户编号 非必填" class="info">
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
                        <input type="text" id="userName" name="userName" value="" placeholder="请填写联系人姓名 非必填" class="info">
                    </div>
                </li>
                <li class="li_info li_bot">
                    <span class="label">联系方式</span>
                    <div class="input">
                        <input type="text" id="tel" name="tel" value="" placeholder="请填写联系人电话 非必填" class="info">
                    </div>
                </li>
                <li class="li_info">
                    <span class="label">电子邮箱</span>
                    <div class="input">
                        <input type="text" id="email" name="email" value="" placeholder="请填写联系人邮箱 非必填" class="info">
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
                        <input id="accountName" name="accountName" value="" type="text" placeholder="请填写开户名称 非必填" class="info">
                    </div>
                </li>
                <li class="li_info li_bot">
                    <span class="label">发票抬头</span>
                    <div class="input">
                        <input id="invoice" name="invoice" value="" type="text" placeholder="请填写发票抬头 非必填" class="info">
                    </div>
                </li>
                <li class="li_info li_bot">
                    <span class="label">开户银行</span>
                    <div class="input">
                        <input id="bank" name="bank" value="" type="text" placeholder="请填写开户银行 非必填" class="info">
                    </div>
                </li>
                <li class="li_info li_bot">
                    <span class="label">银行账号</span>
                    <div class="input">
                        <input id="bankAccountNum" name="bankAccountNum" value="" type="text" placeholder="请填写银行账号 非必填" class="info">
                    </div>
                </li>
                <li class="li_info">
                    <span class="label">纳税人号</span>
                    <div class="input">
                        <input id="identifier" name="identifier" value="" type="text" placeholder="请填写纳税人识别码 非必填" class="info">
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
                        <!--<input type="text" readonly="readonly" id="department" name="department" value="" placeholder="暂无部门信息" class="info">-->
                        <input type="text" id="department" class="info" unselectable="on" onfocus="this.blur()" readonly placeholder="[#if departmentTree.size() == 0]暂无部门信息[#else]请选择[/#if]">
                        <input type="hidden" class="downList_val" name="departmentId" id="departmentId" />
                        <ul class="downList_con" id="departmentUl">
                            [#list departmentTree as department]
                            <li val="${department.id}">[#if department.grade != 0][#list 1..department.grade as i]&nbsp;&nbsp;[/#list][/#if]${department.name}</li>
                            [/#list]
                        </ul>
                    </div>
                </li>
                <li class="li_info">
                    <span class="label">业务员</span>
                    <div class="input adminDiv">
                        <!--<input type="text" readonly="readonly" id="" name="tel" value="" placeholder="请先选择部门" class="info">-->
                        <input type="text" id="prompt" class="input-text radius down_list" unselectable="on" onfocus="this.blur()" readonly placeholder="请先选择部门" />
                        <input type="hidden" id="adminId" name="adminId" class="downList_val" />
                    </div>
                </li>
            </ul>
        </div>
    </div>
    <div class="addSpace"></div>
<input type="button" id="yes" class="input_s input_B" value="保存">
</form>

<script src="${base}/resources/mobile/js/validate/jquery.validate.min.js"></script>
<script src="${base}/resources/mobile/js/picker.min.js"></script>
<script src="${base}/resources/mobile/js/city.js"></script>
<script src="${base}/resources/mobile/js/common.js"></script>
<script src="${base}/resources/mobile/js/mobileSelect.js"></script>
<script>
$(function () {
    var $inputForm = $("#inputForm");
    var $clientName = $("#clientName");
    var $address = $("#address");
    var $admin = $("#adminUl");
    var $adminDiv = $(".adminDiv");
    var departmentId="";
    var $supplierId = $("#supplierId");
    var $areaId = $("#areaId");
    [@flash_message /]



    $inputForm.validate({
        rules:{
            inviteCode:{
                required:true,
                maxlength:6,
                remote:{
                    url:"findBySupplierToCode.jhtml",
                    type:"GET",
                    dataType:"json",
                    dataFilter:function(data) {

                        var suppler = JSON.parse(data);
                        if(suppler.exist == 0) {
                            errorInfoFun("邀请码不存在！");
                            return "false";
                        }
//                        $(".fieldSet select").remove();
                        $clientName.attr("value",suppler.name);
                        $areaId.attr("value",suppler.area);
                        $address.attr("value",suppler.address);
                        $areaId.attr("treePath",suppler.treePath);
                        $supplierId.attr("value",suppler.id);
                        $("#mobileSelect").val(suppler.areaName);
                        // 地区选择
                       // $("#areaId").lSelect({
                       //     url: "${base}/admin/common/area.jhtml"
                       // });
                        return "true";
                    }
                }
            },
            clientName:{
                required:true
            },
            clientNum:{
                maxlength:20
            },
            tel:{
                pattern: /^1[3|4|5|7|8]\d{9}$/
            }
        },
        messages:{
            inviteCode:{
                required:"必填",
                maxlength:"最多6个字符",
                remote:""
            },
            clientName:{
                required:""
            },
            clientNum:{
                maxlength:"最多20个字符"
            },
            email:{
                email:"请输入正确格式"
            },
            tel:{
                pattern:"手机格式不正确"
            }
        }
    })

    $("#yes").on("click",function(){
        if(!$("#inputForm").valid()){
            return false ;
        }
        $("#inputForm").submit();
    });

    //部门选择
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
//            console.log(data[0].id);
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
                console.log(teArray);
                adminSelect1.updateWheel(0,teArray);
            });

        }
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

$(function(){
    window.addEventListener("popstate", function(e) {
        window.location.href = 'list.jhtml';
    }, false);
});
</script>
</body>
</html>
[/#escape]