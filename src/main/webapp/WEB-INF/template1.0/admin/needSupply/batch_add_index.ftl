[#escape x as x?html]
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${message("admin.admin.add")} - Powered By DreamForYou</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <link rel="stylesheet" type="text/css" href="${base}/resources/admin1.0/H-ui/static/h-ui/css/H-ui.min.css" />
    <link rel="stylesheet" type="text/css" href="${base}/resources/admin1.0/H-ui/static/h-ui.admin/css/H-ui.admin.css" />
    <link rel="stylesheet" href="${base}/resources/admin1.0/css/public.css" />
    <link rel="stylesheet" href="${base}/resources/admin1.0/css/admin.css" />
    <style>
        body{background:#f9f9f9;}
        .pag_div{width:45%;float:left;}
        .col-sm-7{width:72%;}
        th{border-top:1px solid #f0f0f0;}
        .form_box{
            overflow: auto;
            overflow-x: hidden;
        }

    </style>
    <script src="${base}/resources/admin1.0/js/jquery.min.js"></script>
    <script>
        $(function(){
            $("#iframeList").load(function () {
                var mainheight = $(this).contents().find("body>div").height() + 40;
                if(mainheight<400){
                    mainheight = 400;
                };
                $(this).height(mainheight);
            });
        })

    </script>
</head>
<body >
    <form id="inputForm" action="" class="form form-horizontal">
        <div class="child_page"><!--内容外面的大框-->
            <div class="cus_nav">
                <ul>
                    <li><a id="goHome"  href="../homePage/index.jhtml">${message("admin.breadcrumb.home")}</a></li>
                    <li><a href="list.jhtml">门店供应列表</a></li>
                    <li>批量添加门店供应</li>
                </ul>
            </div>
            <div class="form_box">
                <div class="selectList"  style="width:100%;height:100%">
                    <iframe src="getNeedList.jhtml?pageNumber=${pageNumber}" id="iframeList" name="iframeList"  frameborder="0" width="100%"
                            height=""  scrolling="no"></iframe>
                </div>
            </div>
            <div class="footer_submit">
                <input class="btn radius confir_S" type="button" id="next" value="下一步">
                <input class="btn radius cancel_B" type="button" value="${message("admin.common.back")}" onclick="javascript:window.location.href='list.jhtml'">
            </div>
        </div>
    </form>
    <form id="batchAddForm" action="batchAdd.jhtml" method="get">

    </form>

    <script type="text/javascript" src="${base}/resources/admin1.0/js/validate/jquery.validate.min.js"></script>
    <script type="text/javascript" src="${base}/resources/admin1.0/js/jquery.lSelect.js"></script>
    <script type="text/javascript" src="${base}/resources/admin1.0/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/admin1.0/js/input.js"></script>
    <script type="text/javascript">
        var needIds=eval('${needIds}');
        var pageNumber=1;
        var needs=[];

        function addNeeds(need){
            delNeeds(need.id);
            needs.push(need);
            // console.log(needs);
        }

        function delNeeds(id){
            for (var i = 0; i < needs.length; i++) {
                if(id == needs[i].id){
                    needs.splice(i,1);
                    break;
                }
            }
            // console.log(needs);
        }

        function getNeeds(){
            return needs ;
        }
        function setPageNumber(number){
            pageNumber=number;
        }
        $(function(){
            var formHeight=$(document.body).height()-100;
            $(".form_box").css("height",formHeight);

            $("#goHome").on("click",function(){
                var nav = window.top.$(".index_nav_one");
                    nav.find("li li").removeClass('clickTo');
                    nav.find("i").removeClass('click_border');
            });

            for (var i = 0; i < needIds.length; i++) {
                var need={};
                need.id=parseInt(needIds[i]); 
                addNeeds(need);
            }

        });

        $("#next").on("click",function(){
            if (needs.length <= 0) {
                $.message("warn","请选择客户");
                return false;
            }
            var $batchAddForm=$("#batchAddForm");
            var input="";
            for (var i = 0; i < needs.length; i++) {
                input+="<input type='hidden' name='needIds' value='"+needs[i].id+"'>";
            }
            input+="<input type='hidden' name='pageNumber' value='"+pageNumber+"'>";
            $batchAddForm.html(input);
            $batchAddForm.submit();

        });


    </script>
    </body>
</html>
[/#escape]