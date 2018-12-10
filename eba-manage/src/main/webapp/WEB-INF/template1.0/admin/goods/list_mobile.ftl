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
    <link rel="stylesheet" href="${base}/resources/mobile/css/goods.css" />
    <title>微信小程序</title>
    <style>
        body{background:#fbfbfb;}
        .footer_nav .nav_goods {
            background: url(${base}/resources/mobile/images/shangpin-2.svg) no-repeat center 0.1rem;
            background-size: 0.44rem;
            color:#4DA1FF;
        }
    </style>
</head>
<body>
	<input type="hidden" name="total" id="total" value="${page.total}" />
	<input type="hidden" name="pageNumber" id="pageNumber" value="${page.pageable.pageNumber}" />
	<input type="hidden" name="pageSize" id="pageSize" value="${page.pageable.pageSize}" />
    <div class="headerSet">
        叮咚
        <a href="../wxSetting/index.jhtml" class="setting_a"></a>
    </div>
    <ul class="goodsList">
        [#list page.content as goods]
        <li class="list_li">
            <div class="left">
            	[#if goods.image != ""]
                    <img src="${goods.image}" />
                [#else]
                	<img src="${base}/resources/mobile/images/mr_icon.png" alt="">
                [/#if]
            </div>
            <div class="right">
                <p class="title">${abbreviate(goods.name, 80, "...")}</p>
                <div class="type">
                	[#if goods.hasSpecification() ]
	                	<span>多规格</span>
	                [/#if]
                </div>
                <div class="price">销售价：<span>${currency(goods.price, true)}</span></div>
                [@shiro.hasPermission name = "admin:goods:edit"]
                	<a href="javascript:;" goodsId="${goods.id}" class="cz_B edit">编辑</a>
                [/@shiro.hasPermission]
                [@shiro.hasPermission name = "admin:goods:delete"]
                	<a href="javascript:;" goodsId="${goods.id}" class="cz_B del">删除</a>
                [/@shiro.hasPermission]
            </div>
        </li>
        [/#list]
    </ul>
    <p class="p_loading">正在加载...</p>
	[@shiro.hasPermission name = "admin:goods:add"]
    	<a href="add.jhtml" class="addButton">添加</a>
    [/@shiro.hasPermission]


    <footer class="footer_nav">
        <a href="javascript:;" class="nav_li nav_index">首页</a>
        <a href="javascript:;" class="nav_li nav_order ">订单</a>
        <a href="javascript:;" class="nav_li nav_Customer">客户</a>
        <a href="javascript:;" class="nav_li nav_supply">供应</a>
        <a href="javascript:;" class="nav_li nav_setting">设置</a>
    </footer>


    <!--<input type="button" class="input_B" value="提交">-->
    <!--弹出确认框-->
    <div class="mutail">
        <div class="mutailContent">
            <div class="content_M">

            </div>
            <span class="cancel">取消</span>
            <span class="delSure">确定</span>
        </div>
    </div>


    <script src="${base}/resources/mobile/js/common.js"></script>

    <script>

        $(function(){
        	var editAuth = '';
        	var deleteAuth = '';
        	[@shiro.hasPermission name = "admin:goods:edit"]
	        	editAuth = 'true';
	        [/@shiro.hasPermission]
	        
	        [@shiro.hasPermission name = "admin:goods:delete"]
	        	deleteAuth = 'true';
	        [/@shiro.hasPermission]

            $(".input_B").on("click",function(){
                errorInfoFun("可见风使舵将发售");
            });

            var boolAjax = false;
            function loadingTest(){
            	var total = $("#total").val();
            	var pageNumber = $("#pageNumber").val();
            	var pageSize = $("#pageSize").val();
            	//总页数
            	var totalPages = total/pageSize;
            	if(pageNumber > totalPages) {
            		$(".p_loading").html("我已经是最后一页了");
            		return;
            	}
            	var next = ++pageNumber;
				$.ajax({
					url: "asynclist.jhtml",
					type: "GET",
					data: {"pageNumber":next},
					dataType: "json",
					success: function(data) {
						if(data.code == '0'){
							var data = data.data;
							var textHtml = '';
							for(var i=0 ; i<data.length ; i++) {
                                textHtml="";
								textHtml+='<li class="list_li"><div class="left">';
								if(data[i].image == "") {
									textHtml+='<img src="${base}/resources/mobile/images/mr_icon.png" alt="">';
								}else {
									textHtml+='<img src="'+data[i].image+'" />';
								}
								textHtml+='</div><div class="right"><p class="title">'+data[i].name+'</p><div class="type">';            
						        if(data[i].specification) {
						        	textHtml+='<span>多规格</span>';
						        }        
						        textHtml+='</div><div class="price">销售价：<span>'+fmoney(data[i].price, 2)+'</span></div>';        	
						        if(editAuth == 'true') {
						        	textHtml+='<button type="button" class="cz_B edit">编辑</button>';
						        }
						        if(deleteAuth == 'true') {
						        	textHtml+='<button type="button" class="cz_B del">删除</button>';
						        }
						        textHtml+='</div></li>';
						        $(".goodsList").append(textHtml);
							}
							$("#pageNumber").val(next);
                            boolAjax = false;
							
						}else {
							errorInfoFun(data.msg);
							
						}
					}
				});
			};


            $(window).scroll(function(){
                var top = $(".p_loading").offset().top;
                var divScroll = $(window).scrollTop();
                var divHeight = $(window).height();

                if(divHeight-(top-divScroll)>70){
                    if(boolAjax){return false;}
                        console.log(111);
                        boolAjax=true;
                        loadingTest();
                }

            });

            var goodsId = 0;
            var delLi = "";
            //ids=182&ids=181&ids=178
            $(".del").on("click",function(){
                goodsId = $(this).attr("goodsId");
                $(".mutail").css("display","block");
                delLi = $(this);
            });

            //取消按钮
            $(".mutail .cancel").on("click",function(){
                $(".mutail").css("display","none");
            });

            //确认按钮，，取消订单
            $(".mutail .delSure").on("click",function(){
                var orderId = $(this).attr("orderId");
                $(".mutail").css("display","none");

                var obj = {"ids":goodsId};
                delGoods(obj);
            });


            function delGoods(obj){
                $.ajax({
                    url: "delete.jhtml",
                    type: "POST",
                    data: obj,
                    dataType: "json",
                    cache: false,
                    success: function(message) {
                        if (message.type == "success") {
                            delLi.closest("li").remove();
                        }
                    }
                });
            }

            


        });
        
        function fmoney(s, n) {
            /*
             * 参数说明：
             * s：要格式化的数字
             * n：保留几位小数
             * */
            n = n > 0 && n <= 20 ? n : 2;
            s = parseFloat((s + "").replace(/[^\d\.-]/g, "")).toFixed(n) + "";
            var l = s.split(".")[0].split("").reverse(),
                r = s.split(".")[1];
            t = "";
            for (i = 0; i < l.length; i++) {
                t += l[i] + ((i + 1) % 3 == 0 && (i + 1) != l.length ? "" : "");
            }
            return '￥'+t.split("").reverse().join("") + "." + r;
        }

    </script>
</body>
</html>
[/#escape]