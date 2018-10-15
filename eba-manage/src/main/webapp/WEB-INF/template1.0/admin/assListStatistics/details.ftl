[#escape x as x?html]
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title></title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <link rel="stylesheet" type="text/css" href="${base}/resources/admin1.0/H-ui/static/h-ui/css/H-ui.min.css" />
    <link rel="stylesheet" href="${base}/resources/admin1.0/css/public.css" />
    <link rel="stylesheet" href="${base}/resources/admin1.0/css/admin.css" />
    <style>
        body{background:#f9f9f9;}

        .col-sm-7{width:72%;}
        .form_box{overflow: auto;overflow-x: hidden;}
        .input_box ul{right:0;}
    </style>
</head>
<body>
<form id="inputForm" action="" method="post" class="form form-horizontal">
    <div class="child_page">
        <div class="cus_nav">
            <ul>
                <li><a href="">${message("admin.breadcrumb.home")}</a></li>
                <li><a href="list.jhtml">清单统计</a></li>
                <li>清单详情</li>
            </ul>
        </div>
        <div class="form_box orderCountDetail">
            <div class='form_baseInfo'>
                <h3 class="form_title" style="margin:20px 0 0 20px;">基本信息</h3>
                <div class="pag_div">
                    <div class="row cl">
                        <label class="form-label col-xs-4 col-sm-3">编号</label>
                        <div class="formControls col-xs-8 col-sm-7">
                            <span class="input_no_span">${assList.sn}</span>
                        </div>
                    </div>
                    <div class="row cl">
                        <label class="form-label col-xs-4 col-sm-3">下单人</label>
                        <div class="formControls col-xs-8 col-sm-7">
                            <span class="input_no_span">${assList.singlePerson}</span>
                        </div>
                    </div>
                    <div class="row cl">
                        <label class="form-label col-xs-4 col-sm-3">采购方</label>
                        <div class="formControls col-xs-8 col-sm-7">
                            <span class="input_no_span">${assList.addressName}</span>
                        </div>
                    </div>
                </div>
                <div class="pag_div">
                    <div class="row cl">
                        <label class="form-label col-xs-4 col-sm-3">主题</label>
                        <div class="formControls col-xs-8 col-sm-7">
                            <span class="input_no_span">${theme!}</span>
                        </div>
                    </div>
                    <div class="row cl">
                        <label class="form-label col-xs-4 col-sm-3">收货人</label>
                        <div class="formControls col-xs-8 col-sm-7">
                            <span class="input_no_span">${assList.name}</span>
                        </div>
                    </div>
                    <div class="row cl">
                        <label class="form-label col-xs-4 col-sm-3">收货地址</label>
                        <div class="formControls col-xs-8 col-sm-7">
                            <span class="input_no_span">${assList.address}</span>
                        </div>
                    </div>
                </div>
                <div class="pag_div">
                    <div class="row cl">
                        <label class="form-label col-xs-4 col-sm-3">员工</label>
                        <div class="formControls col-xs-8 col-sm-7">
                            <span class="input_no_span">${adminName}</span>
                        </div>
                    </div>
                    <div class="row cl">
                        <label class="form-label col-xs-4 col-sm-3">联系方式</label>
                        <div class="formControls col-xs-8 col-sm-7">
                            <span class="input_no_span">${assList.tel}</span>
                        </div>
                    </div>
                    <div class="row cl">
                        <label class="form-label col-xs-4 col-sm-3">创建时间</label>
                        <div class="formControls col-xs-8 col-sm-7">
                            <span class="input_no_span">${(assList.createDate?string('yyyy-MM-dd HH:mm:ss'))!}</span>
                        </div>
                    </div>
                </div>
            </div>
            <div class='form_baseInfo'>
                <h3 class="form_title" style="margin:20px 0 0 20px;">商品信息</h3>

                <div class="goodInfo">

                    <table class="table table-border table-hover table_width boo">
                        <thead>
                        <tr class="text-l">
                            <th width="">商品编号</th>
                            <th width="">商品名称</th>
                            <th width="">商品规格</th>
                            <th width="">基本单位</th>
                            <th width="">采购数量</th>
                        </tr>
                        </thead>
                        <tbody>
                        [#list assListItems as good]
                        <tr class="text-l">
                            <td>${good.sn}</td>
                            <td>${good.name}</td>
                            <td>${good.specification}</td>
                            <td>
                                [#if good.unit??]
                                    [#assign unit=good.unit]
                                    ${message("AssGoods.Unit."+unit)}
                                [/#if]
                            </td>
                            <td>${good.quantity}</td>
                        </tr>
                        [/#list]
                        </tbody>
                    </table>

                </div>


            </div>
            <div class='form_baseInfo'>
                <h3 class="form_title" style="margin:20px 0 0 20px;">参与人信息</h3>
                <div class="goodInfo">
                    <table class="table table-border table-hover table_width boo">
                        <thead>
                        <tr class="text-l">
                            <th width="60%">参与人</th>
                            <th width="">时间</th>
                        </tr>
                        </thead>
                        <tbody>
                        [#list assListRelations as relations]
                        <tr class="text-l">
                            <td class="tdHeadImg">
                                <img src="${relations.assChildMember.headImgUrl}" alt="">
                                ${relations.assChildMember.nickName}
                            </td>
                            <td>
                                ${(relations.createDate?string('yyyy-MM-dd HH:mm:ss'))!}
                            </td>
                        </tr>
                        [/#list]
                        </tbody>
                    </table>

                </div>
            </div>
            <div class='form_baseInfo'>
                <h3 class="form_title" style="margin:20px 0 0 20px;">备注信息</h3>
                <div class="goodInfo">
                    <table class="table table-border table-hover table_width boo">
                        <thead>
                        <tr class="text-l">
                            <th width="">备注人</th>
                            <th width="">备注信息</th>
                            <th width="">备注附件</th>
                            <th width="">时间</th>
                        </tr>
                        </thead>
                        <tbody>
                        [#list assListRemarks as remarks]
                        <tr class="text-l">
                            <td class="tdHeadImg">
                                <img src="${remarks.assChildMember.headImgUrl}" alt="">
                                ${remarks.assChildMember.nickName}
                            </td>
                            <td>${remarks.description}</td>
                            <td class="tdGoodImg">
                                [#list remarks.annex as images]
                                <a href="${images.url}" target="_blank"><img src="${images.url}" alt="" /></a>
                                [/#list]
                            </td>
                            <td>
                                ${(remarks.createDate?string('yyyy-MM-dd HH:mm:ss'))!}
                            </td>
                        </tr>
                        [/#list]
                        </tbody>
                    </table>

                </div>
            </div>
            <div style="height:100px;width:100%;"></div>
        </div>
    </div>
    <div class="footer_submit">
        <input class="btn radius cancel_B" type="button" value="${message("admin.common.back")}" onclick="history.back();return false;">
    </div>
</form>


<script src="${base}/resources/admin1.0/js/jquery.min.js"></script>
<script type="text/javascript" src="${base}/resources/admin1.0/js/jquery.tools.js"></script>
<script src="${base}/resources/admin1.0/js/validate/jquery.validate.min.js"></script>
<script type="text/javascript" src="${base}/resources/admin1.0/H-ui/lib/layer/2.4/layer.js"></script>
<script type="text/javascript" src="${base}/resources/admin1.0/js/webuploader.js"></script>
<script type="text/javascript" src="${base}/resources/admin1.0/js/common.js"></script>
<script type="text/javascript">
    $().ready(function() {

        /*获取页面的高度*/
        var formHeight = $(document.body).height() - 100;
        $(".form_box").css("height", formHeight);

    });
</script>

</body>
</html>
[/#escape]
