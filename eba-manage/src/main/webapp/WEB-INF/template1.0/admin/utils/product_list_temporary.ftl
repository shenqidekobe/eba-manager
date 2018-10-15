[#escape x as x?html]
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title></title>
	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <link rel="stylesheet" type="text/css" href="${base}/resources/admin1.0/H-ui/static/h-ui/css/H-ui.min.css"/>
    <link rel="stylesheet" type="text/css" href="${base}/resources/admin1.0/H-ui/static/h-ui.admin/css/H-ui.admin.css"/>

    <link rel="stylesheet" href="${base}/resources/admin1.0/css/public.css"/>
    <link rel="stylesheet" href="${base}/resources/admin1.0/css/admin.css"/>
    <style>
        body {
            background: #f9f9f9;
        }

        .child_page {
            width: calc(100% - 20px);
            height: calc(100% - 20px);
        }
        .trColor{
            background-color:#f9f9f9;
        }
        .table-hover tbody tr.trColor:hover td{background-color:#fff;color:#999;}
        .table-hover tbody tr.trColor td{background-color:#fff;color:#999;}
    </style>
</head>
<body>
<div class="child_page"><!--内容外面的大框-->
    <form id="listForm" action="proList.jhtml" method="get">
        <input type="hidden" name="supplierId" value="${supplierId}"/>
        <input type="hidden" name="needId" value="${needId}"/>
        <input type="hidden" id="productCategoryId" name="productCategoryId" value="${productCategoryId}" />
        <div class="ch_condition">
        	<div class="require_search" id="filterMenu">
				<span class="search">商品筛选</span>
				<ul class="check">
                    <li name="productCategoryId" val="">所有分类</li>
                    [#list productCategoryTree as productCategory]
                        <li name="productCategoryId" val="${productCategory.id}" value="${productCategory.id}" [#if productCategory.id == productCategoryId] class="checked"[/#if]>[#if productCategory.grade != 0]
                            [#list 1..productCategory.grade as i]
                                &nbsp;&nbsp;
                            [/#list]
                        [/#if]${productCategory.name}</li>
                    [/#list]
					
				</ul>
			</div>
            <div class="ch_search">
                <img class="search_img" src="${base}/resources/admin1.0/images/sousuo_icon.svg" alt=""/>
                <!--<div class="ch_sear_type">
                    <div class="setUp">
                        <p class="bianhao">编号</p>
                        <span></span>
                        <p class="canzuo">操作员</p>
                    </div>
                </div>-->
                <div class="search_input">
                    <input type="text" name="searchName" placeholder="请输入编号/商品名搜索" value="${searchName}"/>
                </div>
            </div>
            <button type="submit" class="search_button">查询</button>
            <div class="ch_operate">
                <button type="button" id="refreshButton" class="op_button update_B">刷新</button>
            </div>
        </div>

        <div class="table_con">
            <table id="listTable" class="table table-border table-hover table_width boo">
                <thead>
                <tr class="text-l">
                    <th width="5%"><input class="all_checked" type="checkbox" value="" id="selectAlls"/></th>
                    <th width="20%">id</th>
                    <th width="20%">编号</th>
                    <th width="20%">${message("Goods.name")}</th>
                    <th width="20%">${message("Goods.productCategory")}</th>
                    <th width="15%">规格</th>
                </tr>
                </thead>
                <tbody>
                    [#list page.content as product]
                    <tr [#if product.source??] [#if product.goods.supplierSupplier.status != "inTheSupply"] class="text-l trColor" [#else] class="text-l" [/#if] [#else] class="text-l" [/#if]  >
                        <td><input type="checkbox"
                                   tempValue='{"productId":"${product.id}" , "goodsName":"${product.goods.name?html}" , "specificationValues":"[#list product.specificationValues as specificationValue]${specificationValue.value?html} [/#list]","productCategory":"${product.goods.productCategory.name?html}", "minOrderQuantity" : "${product.minOrderQuantity}"}'
                                   name="ids" id="${product.id}" class="selectGoods" value="${product.id}"
                                   [#if product.source??] [#if product.goods.supplierSupplier.status != "inTheSupply"] onclick="return false;" title="分销商品的企业供应已过期" [#else] onclick="selectProduct(this , {'productId':'${product.id}' , 'goodsName':'${product.goods.name?html}' , 'specificationValues':'[#list product.specificationValues as specificationValue]${specificationValue.value?html} [/#list]' , 'productCategory':'${product.goods.productCategory.name?html}', 'minOrderQuantity' : '${product.minOrderQuantity}'})" [/#if] [#else] onclick="selectProduct(this , {'productId':'${product.id}' , 'goodsName':'${product.goods.name?html}' , 'specificationValues':'[#list product.specificationValues as specificationValue]${specificationValue.value?html} [/#list]' , 'productCategory':'${product.goods.productCategory.name?html}', 'minOrderQuantity' : '${product.minOrderQuantity}'})" [/#if]
                                   />
                        </td>
                        <td>${product.id}</td>
                        <td>${product.sn}</td>
                        <td>${product.goods.name}</td>
                        <td>${product.goods.productCategory.name}</td>
                        <td>[#list product.specificationValues as specificationValue]
                        ${specificationValue.value}&nbsp;
                        [/#list]</td>
                    </tr>
                    [/#list]
                </tbody>
            </table>
        </div>
        [@pagination pageNumber = page.pageNumber totalPages = page.totalPages]
            [#include "/admin/include/pagination.ftl"]
        [/@pagination]
    </form>
</div>

<script src="${base}/resources/admin1.0/js/jquery.min.js"></script>
<script type="text/javascript" src="${base}/resources/admin1.0/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/admin1.0/js/list.js"></script>
<script type="text/javascript">

    function selectProduct(now , obj){
        var $id = $(now);

        if($id.attr("checked")){
            window.parent.addProduct(obj);
        }
        if(!$id.attr("checked")){
            window.parent.delProduct(obj);
        }
    }


    $().ready(function() {


        var cacheProducts = window.parent.getCacheFromParent();

        $("input[name='ids']").each(function () {//遍历根据name属性取到的所有值
            var id = $(this).val();
            if(id in cacheProducts){
                $(this).attr("checked" , true);
                // $(this).attr("disabled" , true);
            }
        });

        var $listForm = $("#listForm");
        var $filterMenu = $("#filterMenu");
        var $filterMenuItem = $("#filterMenu li");
        var $moreButton = $("#moreButton");

        [@flash_message /]

        // 筛选菜单
        $filterMenu.hover(
                function() {
                    $(this).children("ul").show();
                }, function() {
                    $(this).children("ul").hide();
                }
        );

        // 筛选
        $filterMenuItem.click(function() {
            var $this = $(this);
            var $dest = $("#" + $this.attr("name"));
            if ($this.hasClass("checked")) {
                $dest.val("");
            } else {
                $dest.val($this.attr("val"));
            }
            $listForm.submit();
        });


        //全选事件
        $("#selectAlls").click(function(){
            $("input[name='ids']").each(function () {//遍历根据name属性取到的所有值
                if ($(this).parent().parent().hasClass("trColor")) {
                    return true;
                }
                if ($("#selectAlls").is(":checked")) {
                    $(this).attr("checked",true);
                }else{
                    $(this).attr("checked",false);
                }
                var obj = $(this).attr("tempValue");
                selectProduct(this , JSON.parse(obj));
            });
        });
        
        
        /*搜索条件*/
    	$(".require_search li").on("click",function(){
    		$(this).parent().siblings(".search").html($(this).html());
    		//$(this).addClass("li_bag").siblings().removeClass("li_bag");
    		$(".check").css("display","none");
    	});
    	$(".require_search").mouseover(function(){
			$(this).find("ul").css("display","block");
		});
		$(".require_search").mouseout(function(){
			$(this).find("ul").css("display","none");
		});
        
        
        
        $("#goHome").on("click",function(){
			var nav = window.top.$(".index_nav_one");
    			nav.find("li li").removeClass('clickTo');
				nav.find("i").removeClass('click_border');
		})

    });


</script>

</body>
</html>
[/#escape]