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
		.table_width th{border-top:1px solid #cadbf3;}
		.check-box, .radio-box{width:160px;padding-right:40px;padding-bottom:5px;}
    </style>
    
    <script src="${base}/resources/admin1.0/js/jquery.min.js"></script>
	<script type="text/javascript" src="${base}/resources/admin1.0/js/validate/jquery.validate.min.js"></script>
	<script type="text/javascript" src="${base}/resources/admin1.0/js/common.js"></script>
	<script type="text/javascript">
		var cacheCustomers = {} ;
		[#if suppliers?has_content]
			[#list suppliers as supplier]
            cacheCustomers[${supplier.id}] = {
            	    'id': ${supplier.id}
				};
			[/#list]
		[/#if]


        var cacheNeeds = {} ;
			[#if needs?has_content]
				[#list needs as need]
                cacheNeeds[${need.id}] = {
                    'id': ${need.id}
                };
				[/#list]
			[/#if]


		function getCache(){
		    return cacheCustomers ;
		};

		function addToCache(supplierId){
            cacheCustomers[supplierId] = {'id': supplierId};
		};

		function delFromCache(supplierId){
            delete cacheCustomers[supplierId];
		};


        function getNeedCache(){
            return cacheNeeds ;
        };

        function addToNeedCache(needId){
            cacheNeeds[needId] = {'id': needId};
        };

        function delFromNeedCache(needId){
            delete cacheNeeds[needId];
        };


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
			
			
			$("#iframeList").load(function () {
				var mainheight = $(this).contents().find("body>div").height() + 40;
				if(mainheight<300){
					mainheight = 300;
				};
			 	$(this).height(mainheight);
			});

            $("#saveOrderNoticeForm").validate({
                rules: {
                    /*types: {
                        required: true
                    }*/
                },
                submitHandler: function(form) {
                    /*if ($.isEmptyObject(cacheCustomers)) {
                        $.message("warn", "请选择客户");
                        return false;
                    }

                    if ($.isEmptyObject(cacheNeeds)) {
                        $.message("warn", "请选择个体客户");
                        return false;
                    }*/

                    $.each(cacheCustomers , function(key , item){
                        $(form).append('<input type="hidden" name="supplierIds" value="'+key+'" \/>');
					});

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
			});


            /*企业客户--个体客户*/
            var enterpriseUrl= "../utils/customerRelList.jhtml";
            var individualUrl= "../utils/orderNeedList.jhtml";
            $(".CustomerType div").on("click",function(){
                $(".CustomerType div").removeClass("divChecked");
                if($(this).hasClass("CustomerIndividual")){
                    $("#iframeList").prop("src",individualUrl);
                    $(this).addClass("divChecked");
				}else{
                    $("#iframeList").prop("src",enterpriseUrl);
                    $(this).addClass("divChecked");
				}
			})

	    });
	    
	</script>
    
    <title>查看订单</title>
</head>
<body>
<form class="form form-horizontal" id="saveOrderNoticeForm" action="saveOrderNotice.jhtml" method="post">
	<input type="hidden" name="noticeUserId" value="${noticeUserId}" />
	<div class="child_page"><!--内容外面的大框-->
	    <div class="cus_nav">
	        <ul>
	            <li><a id="goHome"	href="../homePage/index.jhtml">${message("admin.breadcrumb.home")}</a></li>
	        	<li><a href="list.jhtml">消息通知</a></li>
	        	<li>订货单消息通知设置 </li>
	        </ul>
	    </div>
	    
	    <div class="form_box">
	       
        	<div class="notice_con">
        		<h3 class="form_title" style="margin:20px 0 0 20px;">通知内容</h3>
        		<div class="pag_div" style="padding-top:10px;width:60%">
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
				<div class="modelInfo">
					<div class="CustomerType">
						<div class="CustomerEnterprise divChecked">
							企业客户
						</div>
						<div class="CustomerIndividual">
							门店
						</div>
					</div>
				</div>
        		<div class="pag_div" style="padding:10px 0 0 30px;">

        			<div class="selectList" style="width:100%;">
                        <iframe src="../utils/customerRelList.jhtml" id="iframeList" name="iframeList"  frameborder="0" width="100%" height="" scrolling="no" >
					</div>	
        			
        		</div>
        	</div>
        	
        </div>	
        
	</div>
	
</form>


</body>
</html>
[/#escape]