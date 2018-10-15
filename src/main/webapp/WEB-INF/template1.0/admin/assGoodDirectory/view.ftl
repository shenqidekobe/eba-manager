[#escape x as x?html]
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>${message("admin.admin.edit")} - Powered By DreamForYou</title>
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <link rel="stylesheet" type="text/css" href="${base}/resources/admin1.0/H-ui/static/h-ui/css/H-ui.min.css" />
        <link rel="stylesheet" type="text/css" href="${base}/resources/admin1.0/H-ui/static/h-ui.admin/css/H-ui.admin.css" />
        <link rel="stylesheet" href="${base}/resources/admin1.0/js/date/dateRange.css" />
        <link rel="stylesheet" href="${base}/resources/admin1.0/css/public.css" />
        <link rel="stylesheet" href="${base}/resources/admin1.0/css/admin.css" />
        <style>
            body{background:#f9f9f9;}
            .pag_div{width:95%; float:left;}
            .col-sm-7{width:40%;}
            .require_search,.ch_search,.update_B{border:1px solid #f0f0f0;}
            .table-border th{border-top:1px solid #f0f0f0;}
            #iframeList{overflow: hidden;}
            .form_box{overflow: auto;}
        </style>
        <script src="${base}/resources/admin1.0/js/jquery.min.js"></script>
        <script src="${base}/resources/admin1.0/js/date/dateRange.js"></script><!--时间控件-->
        <script type="text/javascript" src="${base}/resources/admin1.0/H-ui/lib/layer/2.4/layer.js"></script>
        <script type="text/javascript" src="${base}/resources/admin1.0/H-ui/static/h-ui/js/H-ui.min.js"></script> 
        <script type="text/javascript" src="${base}/resources/admin1.0/H-ui/static/h-ui.admin/js/H-ui.admin.js"></script>
        <script type="text/javascript" src="${base}/resources/admin1.0/H-ui/lib/laypage/1.2/laypage.js"></script>
        <script type="text/javascript" src="${base}/resources/admin1.0/js/validate/jquery.validate.min.js"></script>
        <script type="text/javascript" src="${base}/resources/admin1.0/js/common.js"></script>
        <script type="text/javascript">
            $(function(){
                
                /*通过js获取页面高度，来定义表单的高度*/
                var formHeight=$(document.body).height()-100;
                $(".form_box").css("height",formHeight);
    
                var formList = $(document.body).height() - 240;
                $(".selectList").css("height",formList);
            
                /*下拉框的效果*/
                $(".down_list").click(function(){
                    $(this).siblings(".downList_con").toggle();
                })
                
                $("*").click(function (event) {
                    if (!$(this).hasClass("down_list")&&!$(this).hasClass("downList_con")){
                        $(".downList_con").hide();
                    }
                    event.stopPropagation();   
                });
                $(".downList_con li").click(function(){
                    $(this).parent().siblings(".down_list").val($(this).html());
                    $(this).parent().siblings(".downList_val").val($(this).attr("val"));
                    $(this).addClass("li_bag").siblings().removeClass("li_bag");
                });
                
                
                
                /*当input获得焦点时，外面的边框显示蓝色*/
                $(".focus_border").focus(function(){
                    $(this).parent().addClass("add_border");
                })
                $(".focus_border").blur(function(){
                    $(this).parent().removeClass("add_border");
                })
                
                 $("#iframeList").load(function () {
                    var mainheight = $(this).contents().find("body>div").height() + 40;
                    if(mainheight<300){
                        mainheight = 300;
                    };
                    $(this).height(mainheight);
                });
           });
           
        </script>
    </head>
    <body >
        <div class="child_page"><!--内容外面的大框-->  
            <div class="cus_nav">
                <ul>
                    <li><a href="">${message("admin.breadcrumb.home")}</a></li>
                    <li><a href="list.jhtml">商品目录列表 </a></li>
                    <li>商品目录详情</li>
                </ul>
            </div>
            <div class="form_box">
                <form id="inputForm" action="" method="post" class="form form-horizontal">
                    <div class='form_baseInfo'>
                        <h3 class="form_title" style="margin:20px 0 0 20px;">基本信息</h3>
                        <div class="pag_div">
                            <div class="row cl">
                                <label class="form-label col-xs-4 col-sm-3">
                                    <img src="${base}/resources/admin1.0/images/bitian_icon.svg" alt=""/>主题名</label>
                                <div class="formControls col-xs-8 col-sm-7">
                                    <span class="input_no_span">${assGoodDirectory.theme}</span>
                                </div>
                            </div>
                            <div class="row cl">
                                <label class="form-label col-xs-4 col-sm-3">
                                    简介</label>
                                <div class="formControls col-xs-8 col-sm-7">
                                    <span class="fieldSet">
                                        <span class="textareaSpan">${assGoodDirectory.profiles}</span>
                                    </span>
                                </div>
                            </div>
                        </div>
                    </div>
                    
                    <div class="footer_submit">
                        <input class="btn radius cancel_B" type="button" value="返回" onclick="history.back(); return false;"/>
                    </div>
                    
                    <div class="selectList" style="width:100%;">
                        <h3 class="form_title" style="margin:20px 0 0 20px;">基本信息</h3>
                        <iframe src="getViewGoodList.jhtml?assGoodDirectoryId=${assGoodDirectory.id}" id="iframeList" name="iframeList"  frameborder="0" width="100%" height="" scrolling="no">
                    </div>
                </form>
            </div>  
        </div>
    </body>
</html>
[/#escape]
