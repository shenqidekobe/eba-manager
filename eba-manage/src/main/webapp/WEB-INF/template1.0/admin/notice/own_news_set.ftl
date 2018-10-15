[#escape x as x?html]
<!DOCTYPE HTML>
<html>
<head>
    <meta charset="utf-8">
    <meta name="renderer" content="webkit|ie-comp|ie-stand">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width,initial-scale=1,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no"/>
    <meta http-equiv="Cache-Control" content="no-siteapp"/>

    <link rel="stylesheet" type="text/css" href="${base}/resources/admin1.0/H-ui/static/h-ui/css/H-ui.min.css"/>
    <link rel="stylesheet" type="text/css" href="${base}/resources/admin1.0/H-ui/static/h-ui.admin/css/H-ui.admin.css"/>
    <link rel="stylesheet" href="${base}/resources/admin1.0/css/public.css"/>
    <link rel="stylesheet" href="${base}/resources/admin1.0/css/admin.css"/>

    <style>
        body {
            background: #f9f9f9;
        }

        .pag_div {
            width: 90%;
            float: left;
            padding-left:80px;
        }
		.form_box {
		    overflow: auto;
		    overflow-x: hidden;
		}
		.ch_search{border:1px solid #cadbf3;}
		.table_width th{border-top:1px solid #cadbf3;border-top:1px so}
		.check-box, .radio-box{padding-right:40px;padding-bottom:5px;}
		.require_search{border:1px solid #cadbf3;}
    </style>
    <script src="${base}/resources/admin1.0/js/jquery.min.js"></script>
	<script type="text/javascript" src="${base}/resources/admin1.0/js/validate/jquery.validate.min.js"></script>
	<script type="text/javascript" src="${base}/resources/admin1.0/js/common.js"></script>
	<script type="text/javascript">

        var cacheNeeds = {} ;
			[#if needs?has_content]
				[#list needs as need]
                cacheNeeds[${need.id}] = {
                    'id': ${need.id}
                };
				[/#list]
			[/#if]


        function getCache(){
            return cacheNeeds ;
        }

        function addToCache(needId){
            cacheNeeds[needId] = {'id': needId};
        }

        function delFromCache(needId){
            delete cacheNeeds[needId];
        }

	    $(function () {
	        /*通过js获取页面高度，来定义表单的高度*/
	        var formHeight = $(document.body).height() - 100;
	        $(".form_box").css("height", formHeight);
			
	        $(".tabCon").css("height",formHeight - 40);
			
			/*通过js获取页面高度，来定义表单的高度*/
			var formHeight=$(document.body).height()-100;
			$(".form_box").css("height",formHeight);
	
			/*var formList = $(document.body).height() - 240;
			$(".selectList").css("height",formList);*/
			
			
	       /*搜索条件*/
	    	$(".require_search li").on("click",function(){
	    		$(this).parent().siblings(".search").html($(this).html());
	    		$(this).addClass("li_bag").siblings().removeClass("li_bag");
	    		$(".check").css("display","none");
	    	});
	    	$(".require_search").mouseover(function(){
				$(this).find("ul").css("display","block");
			});
			$(".require_search").mouseout(function(){
				$(this).find("ul").css("display","none");
			});
	
			
			$("#iframeList").load(function () {
				var mainheight = $(this).contents().find("body>div").height() + 40;
				if(mainheight<300){
					mainheight = 300;
				};
			 	$(this).height(mainheight);
			});

            $("#savePurchaseNotice").validate({
                rules: {
                    /*types: {
                        required: true
                    }*/
                },
                submitHandler: function(form) {

                    /*if ($.isEmptyObject(cacheNeeds)) {
                        $.message("warn", "请选择收货点");
                        return false;
                    }*/

                    $.each(cacheNeeds , function(key , item){
                        $(form).append('<input type="hidden" name="needIds" value="'+key+'" \/>');
                    });

                    $(form).find("input:submit").prop("disabled", true);
                    form.submit();
                }
            });
            
            $("#goHome").on("click",function(){
				var nav = window.top.$(".index_nav_one");
        			nav.find("li li").removeClass('clickTo');
					nav.find("i").removeClass('click_border');
			})
	
	    });
	    
	</script>
    <title>查看订单</title>
</head>
<body>

<form class="form form-horizontal" id="savePurchaseNotice" action="savePurchaseNotice.jhtml" method="post" >
    <input type="hidden" name="noticeUserId" value="${noticeUserId}" />
	<div class="child_page"><!--内容外面的大框-->
	    <div class="cus_nav">
	        <ul>
	            <li><a id="goHome"	href="../homePage/index.jhtml">${message("admin.breadcrumb.home")}</a></li>
	        	<li><a href="list.jhtml">消息通知</a></li>
	        	<li>采购单消息通知设置 </li>
	        </ul>
	    </div>
	    
	    <div class="form_box">
	       
        	<div class="notice_con">
        		<h3 class="form_title" style="margin:20px 0 0 20px;">通知内容</h3>
        		<div class="pag_div" style="padding-top:10px;width:36%;">
        			<div class="row cl">
						[#list types as type]
                            <div class="check-box">
                                <input type="checkbox" class="input-text radius" name="types" value="${type}" [#if setTypes?has_content][#if setTypes?seq_contains(type)]checked[/#if][/#if] />
                                <span>${type.desc}</span>
                            </div>
						[/#list]

                    </div>
        			
        		</div>
        	</div>
        	<div class="footer_submit">
		        <input class="btn radius confir_S" type="submit" value="确定" id="submitButton">
		        <input class="btn radius cancel_B" type="button" value="取消" onclick="history.back();return false;">
		    </div>
        	<div class="notice_con">
        		<h3 class="form_title" style="margin:20px 0 0 20px;">通知范围</h3>
        		<div class="pag_div" style="padding-top:10px;">
        			
        			<div class="" style="width:100%;">
						<iframe src="../utils/needList.jhtml" id="iframeList" name="iframeList"  frameborder="0" width="100%" height="" scrolling="no" >
					</div>
        			
        		</div>
        	</div>
        	
        </div>	
        
	</div>
	
</form>


</body>
</html>
[/#escape]