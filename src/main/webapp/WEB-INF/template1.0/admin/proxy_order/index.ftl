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
        .form_box{padding:40px 0;}
        .xxDialog{top:12%;overflow: visible;}
        .dialogContent{height:calc(100% - 100px);}
        .form-horizontal .form-label{width:70px;}
        .xxDialog .dialogBottom{padding-top:5px;}
    </style>
</head>
<body>
<form id="inputForm" action="update.jhtml" method="post" class="form form-horizontal">

    <div class="child_page">
        <div class="cus_nav">
            <ul>
                <li><a href="">${message("admin.breadcrumb.home")}</a></li>
                <li>代下单</li>
            </ul>
        </div>
        <div class="form_box">

            <div class="proxy_li individualAdd">
                <!--<a href="individualAdd.jhtml">-->
                    <img src="${base}/resources/admin1.0/images/geti-kehu.svg" alt="">
                    <span class="proxy_text">直营门店代下单</span>
                <!--</a>-->
            </div>
            <div class="proxy_li multipleAdd">
                <!--<a href="multipleAdd.jhtml">-->
                    <img src="${base}/resources/admin1.0/images/liushui-kehu.svg" alt="">
                    <span class="proxy_text">流水门店代下单</span>
                    <!--<div class="liushui_div">
                        <div class="liu_con">
                            请先到供应-流水供应设置商品供货价和起订量，若无设置，不能下单
                        </div>
                    </div>-->
                <!--</a>-->
            </div>
        </div>
    </div>
</form>


<script src="${base}/resources/admin1.0/js/jquery.min.js"></script>
<script type="text/javascript" src="${base}/resources/admin1.0/js/common.js"></script>
<script type="text/javascript">
    [@flash_message /]
    $().ready(function() {

        /*获取页面的高度*/
        var formHeight = $(document.body).height() - 130;
        $(".form_box").css("height", formHeight);

        $(".individualAdd").on("click",function(){
            $.dialog({
                title:"直营门店代下单",
                width:500,
                height:200,
                content:[@compress single_line = true]
                '<form id="reviewForm" class="form form-horizontal" action="individualAdd.jhtml" method="get">
                    <div class="pag_div1" style="margin-top:20px">
                        <div class="row cl">
                            <label class="form-label col-xs-4 col-sm-2"><\/label>
                            <div class="formControls col-xs-8 col-sm-9">
                                <div class="check-box">
                                    <input type="radio" name="type" value="formal" checked="checked" class="passedClass" \/>
                                    <span style="margin-right:60px;">向供应商下单<\/span>
                                    <input type="radio" name="type" value="temporary" class="passedClass" \/>
                                    <span>向本企业下单<\/span>
                                <\/div>
                            <\/div>
                        <\/div>
                    <\/div>
                <\/form>'
                [/@compress],
                modal: true,
                onShow:function(){

                },
                onOk: function() {
                    $("#reviewForm").submit();
                }
            });
        });
        $(".multipleAdd").on("click",function(){
            $.dialog({
                title:"流水客户代下单",
                width:500,
                height:210,
                content:[@compress single_line = true]
                '<form id="reviewForm" class="form form-horizontal" action="multipleAdd.jhtml" method="get">
                    <div class="pag_div1" style="margin-top:20px">
                        <div class="row cl">
                            <label class="form-label col-xs-4 col-sm-2"><\/label>
                            <div class="formControls col-xs-8 col-sm-9">
                                <div class="check-box">
                                    <input type="radio" name="type" value="formal" checked="checked" class="passedClass" \/>
                                    <span style="margin-right:60px;">向供应商下单<\/span>
                                    <input type="radio" name="type" value="temporary" class="passedClass" \/>
                                    <span>向本企业下单<\/span>
                                <\/div>
                                <div class="liushui_div">
                                        <div class="liu_con">
                                        请先到供应-流水供应设置商品供货价和起订量，若无设置，不能下单
                                    <\/div>
                                <\/div>
                            <\/div>
                        <\/div>
                    <\/div>
                <\/form>'
                [/@compress],
                modal: true,
                onShow:function(){

                },
                onOk: function() {
                    $("#reviewForm").submit();
                }
            });
        });

    });
</script>

</body>
</html>
[/#escape]
