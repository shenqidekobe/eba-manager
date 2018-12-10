<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <title>微信小程序</title>
    <link rel="stylesheet" href="${base}/resources/shop/common/config/bootstrap/css/bootstrap.css">
    <link rel="stylesheet" href="${base}/resources/shop/common/css/public.css">
    <link rel="stylesheet" href="${base}/resources/shop/common/css/index.css">
    <link rel="stylesheet" href="${base}/resources/shop/common/css/purchase.css">

    <script src="${base}/resources/shop/common/js/jquery.min.js"></script>
    <script src="${base}/resources/shop/common/config/bootstrap/js/bootstrap.min.js"></script>

</head>
<body class="reg10">
[#include "/shop/common/head.ftl"]
<div class="dh-purchase-box">
    <div class="dh-nav-bar-selected">
        <div class="nav-bar-con">
            <div class="nav-bar">
                <a class="nav-bar-active" href="/shop/index.jhtml" target="_self" title="首页">首页</a>
                <i>/</i>
                <a class="nav-bar-active nav-type" href="javascript:void(0);" onclick=queryCategory("pub_supply"); target="_self" title="供应信息">供应信息</a>

            </div>
        </div>
    </div>
    <input id="selectType" type="hidden" value="pub_supply">
    <div class="dh-search-ite">
        <div class="item-list item-list1">
            <h5 class="item-list-title">一级分类</h5>
            <div class="item-list-con">
            [@category_List]
                <ul class="item">
                <li class="li1"><a class="item1 item-type-c" href="javascript:void(0);" target="_self" title="全部" onclick=queryCategory("pub_supply","");>全部</a></li>
                [#list categorys as category]
                    <li class="li1">
                        <a class="item1" href="javascript:void(0);" target="_self" data-id="${category.id}" title="${category.name}" onclick=queryCategory("pub_supply","${category.id}","${category.id}");>${category.name}</a>
                        <div class="item-list item-list2" style="display:none;">
                            <h5 class="item-list-title">二级分类</h5>
                            <div class="item-list-con">
                                <ul class="item">
                                <li class="li2"><a class="item2 item-type-c" class="total2" href="javascript:void(0);" target="_self" title="全部" onclick=queryCategory("pub_supply",${category.id },${category.id });>全部</a></li>
                                [#list category.children as categoryOne]
                                    <li class="li2">
                                        <a class="item2" href="javascript:void(0);" target="_self" data-id="${categoryOne.id}" title="${categoryOne.name}" onclick=queryCategory("pub_supply","${categoryOne.id}","${category.id}","${categoryOne.id}");>${categoryOne.name}</a>
                                        <div class="item-list item-list3" style="display:none;">
                                            <h5 class="item-list-title">三级分类</h5>
                                            <div class="item-list-con">
                                                <ul class="item">
                                                <li class="li3"><a class="item3 item-type-c" class="item3" href="javascript:void(0);" target="_self" title="全部" onclick=queryCategory("pub_supply",${categoryOne.id },${category.id},${categoryOne.id});>全部</a></li>
                                                [#list categoryOne.children as categoryTwo]
                                                    <li class="li3">
                                                        <a class="item3" href="javascript:void(0);" target="_self" data-id="${categoryTwo.id}" title="${categoryTwo.name}" onclick=queryCategory("pub_supply","${categoryTwo.id}","${category.id}","${categoryOne.id}","${categoryTwo.id}");>${categoryTwo.name}</a>
                                                    </li>
                                                [/#list]
                                                </ul>
                                            </div>
                                        </div>
                                    </li>
                                [/#list]
                                </ul>
                            </div>
                        </div>
                    </li>
                [/#list]
                </ul>
            [/@category_List]
            </div>
        </div>
        <div class="item-list item-c-list1" style="display:none;"></div>
        <div class="item-list item-c-list2" style="display:none;"></div>
    </div>
    <div class="dh-search-ite">
        <div class="item-list border-top-n">
            <h5 class="item-list-title">筛选</h5>
            <div class="data-filter">
                <a id="dh-popularity" href="javascript:void(0);" target="_self" title="人气">
                    人气
                    <img class="dh-popularity" src="${base}/resources/shop/common/images/shaixuan-a.svg" alt="" />
                </a>
                <a id="dh-price" href="javascript:void(0);" target="_self" title="价格">
                    价格
                    <img class="dh-price" src="${base}/resources/shop/common/images/shaixuan-a.svg" alt="" />
                </a>
            </div>
        </div>
    </div>
    <div class="all-data">
        <div class="dh-data-box">
            <div class="data-info">
                <h5>供应信息数据</h5>
                <div class="data-con">共计<span class="data-total">${page.total}</span>条记录</div>
            </div>
        </div>
    </div>
    <form id="listForm" action="/shop/companyGoods/getCompanyGoodsList.jhtml" method="get">
    <input type="hidden" value="pub_supply" name="pubType"/>
    <input id="name" type="hidden"  name="name" value="${name }"/>
    <input id="categoryId" type="hidden"  name="categoryId" value="${categoryId }"/>
    <input id="categoryOneId" type="hidden"  name="categoryOneId" value="${categoryOneId }"/>
    <input id="categoryTwoId" type="hidden"  name="categoryTwoId" value="${categoryTwoId }"/>
    <input id="categoryThreeId" type="hidden" name="categoryThreeId"  value="${categoryThreeId }"/>
    
    <input id="supplierId" type="hidden" name="supplierId"  value="${supplierId }"/>
    
    <input id="priceBoolean" type="hidden" name="priceBoolean"  value="${(priceBoolean?string("true", "false"))!}"/>
    <input id="popularityBoolean" type="hidden" name="popularityBoolean"  value="${(popularityBoolean?string("true", "false"))!}"/>
    <div class="dh-search-result">
        <div class="result-box">
            <div class="result-content">
             [#list page.content as supplyCompanyGoods]
                <div class="filter-result">
                	<input class="goodsId" type="hidden" value="${supplyCompanyGoods.id }"/>
                     <div class="result-left" onclick=queryDetails("pub_supply","${supplyCompanyGoods.id }");>
                        <div class="goods-img-box">
                            <a class="goods-url" href="javascript:void(0);" target="_self">
                                <img src="${supplyCompanyGoods.image}" alt="">
                            </a>
                        </div>
                        <div class="goods-detail">
                            <h5>${supplyCompanyGoods.name }</h5>
                            <span class="goods-stime"><i>更新日期：</i>${supplyCompanyGoods.modifyDate }</span>
                            <div class="goods-info">
                                <p class="price-tag"><i class="goods-par">参考价格：</i><span class="goods-price">
                                [#if supplyCompanyGoods.marketPrice == "-1"]
                                    面议
                                [#else]
                                    ￥${supplyCompanyGoods.marketPrice }
                                [/#if]
                                </span>
                                
                                [#if supplyCompanyGoods.sourceType == "source_type_has"]
					             <i class="goods-cur">                          
					                                        有货
                                 </i>
                                [#elseif supplyCompanyGoods.sourceType == "source_type_now"]
						              <i class="goods-cur">                             
						                                        现货
                                      </i>
                                [#else]
                                [/#if]
                                </p>
                                <div class="goods-classify">
                                    <div class="goods-di">
                                        <i class="goods-par">产品分类：</i><span class="goods-cont">${supplyCompanyGoods.category.name }</span>
                                    </div>
                                    <div class="goods-di">
                                        <i class="goods-par goods-par-second">保存条件：</i>
                                        <span class="goods-cont">
                                        [#if supplyCompanyGoods.storageConditions == "roomTemperature"]
                                            常温
                                        [#elseif supplyCompanyGoods.storageConditions == "refrigeration"]
                                                                                                                   冷藏
                                        [#elseif supplyCompanyGoods.storageConditions == "frozen"]
                                                                                                                   冰冻
                                        [#else]
                                        [/#if]
                                        </span>
                                    </div>
                                </div>
                                <div class="goods-format">
                                    <div class="goods-di">
                                         <i class="goods-par">基本单位：</i>
                                         <span class="goods-cont">
                                             [#if supplyCompanyGoods.unit == "box"]
                                             箱
                                             [#elseif supplyCompanyGoods.unit == "bottle"]
                                              瓶
                                             [#elseif supplyCompanyGoods.unit == "bag"]
                                              袋
                                             [#elseif supplyCompanyGoods.unit == "frame"]
                                                                                                              盒
                                             [#elseif supplyCompanyGoods.unit == "pack"]
                                                                                                             包
                                             [#else]
                                             [/#if]
                                         </span>
                                    </div>
                                    <div class="goods-di">
                                        <i class="goods-par goods-par-second">产品规格：</i><span class="goods-cont">${supplyCompanyGoods.goodsSpec }</span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="result-right" onclick=querySupplierDetails(${supplyCompanyGoods.supplier.id });>
                        <div class="company-img-box">
                            <a href="javascript:void(0);" target="_self">
                                <img src="${supplyCompanyGoods.supplier.imagelogo }" alt="">
                            </a>
                        </div>
                        <div class="company-detail company-sh">
                        	<input class="supplierId" type="hidden" value="${supplyCompanyGoods.supplier.id }">
                            <h5>${supplyCompanyGoods.supplier.name }</h5>
                            <div class="company-info">
                                <p>联系人：<span class="company-contact">${supplyCompanyGoods.supplier.userName }</span></p>
                                <p>联系电话：<span class="company-phone">${supplyCompanyGoods.supplier.tel }</span></p>
                            </div>
                            <div class="company-set">
                                [#if supplyCompanyGoods.supplier.qqCustomerService == null]
                                     <a class="dh-qq-connect">
                                         <span class="company-weixin consult-no" title="咨询">
                                            <img src="${base}/resources/shop/common/images/zixun-a.svg" alt="" />
                                         </span>
                                     </a>
                                [#else]
                                    <a class="dh-qq-connect" href="http://wpa.qq.com/msgrd?v=3&uin=${supplyCompanyGoods.supplier.qqCustomerService}&site=qq&menu=yes" target="_blank">
                                        <span class="company-weixin consult"  title="咨询" onclick=advice(${supplyCompanyGoods.supplier.qqCustomerService});>
                                            <img src="${base}/resources/shop/common/images/zixun.svg" alt="" />
                                         </span>
                                    </a>
                                [/#if]
                                [#assign favorCompanyFlag=false]
                                [#if favorCompanyL == null]
                                    <span class="company-collect">
                                        <img class="select${supplyCompanyGoods.supplier.id}" src="${base}/resources/shop/common/images/shoucang-a.svg" alt="" title="收藏企业" />
                                    </span>
                                [#else]
                                    [#list favorCompanyL as favorCompany]
                                        [#if supplyCompanyGoods.supplier.id == favorCompany.supplierId]
                                            [#assign favorCompanyFlag=true]
                                            [#break]
                                        [/#if]
                                    [/#list]
                                    [#if favorCompanyFlag]
                                         <span class="company-collect">
                                             <img class="select${supplyCompanyGoods.supplier.id}" src="${base}/resources/shop/common/images/shoucang-b.svg" alt="" title="取消收藏" />
                                         </span>
                                    [#else]
                                        <span class="company-collect">
                                            <img class="select${supplyCompanyGoods.supplier.id}" src="${base}/resources/shop/common/images/shoucang-a.svg" alt="" title="收藏企业" />
                                        </span>
                                    [/#if]
                                [/#if]
                            </div>
                        </div>
                    </div>
                </div>
                 [/#list]
            </div>
            [@pagination pageNumber = page.pageNumber totalPages = page.totalPages]
				[#include "/shop/include/pagination.ftl"]
			[/@pagination]
        </div>
    </div>
    </form>
</div>
[#include "/shop/common/foot.ftl"]
</body>
<script src="${base}/resources/shop/common/js/public.js"></script>
<script src="${base}/resources/shop/common/js/goodsList.js"></script>
<script src="${base}/resources/shop/common/js/common.js"></script>
<script src="${base}/resources/shop/common/js/list.js"></script>
<script>
$(function(){
     var name = $("#searchWords").val();

     var id = $("#categoryId").val();
     var oneId = $("#categoryOneId").val();
     var twoId = $("#categoryTwoId").val();
     var threeId = $("#categoryThreeId").val();

     var supplierId = $("#supplierId").val();

     var packagesNumBoolean = $('#packagesNumBoolean').val();
     var popularityBoolean = $('#popularityBoolean').val();
     var priceBoolean = $('#priceBoolean').val();

     // 价格
     $("#dh-price").click(function(){
         if(priceBoolean == "false"){
             window.location.href='/shop/companyGoods/getCompanyGoodsList.jhtml?name='+name+'&pubType=pub_supply&priceBoolean=true&categoryId='+id+'&categoryOneId='+oneId+'&categoryTwoId='+twoId+'&categoryThreeId='+threeId+'&supplierId='+supplierId;
         }else if(priceBoolean == "true"){
             window.location.href='/shop/companyGoods/getCompanyGoodsList.jhtml?name='+name+'&pubType=pub_supply&priceBoolean=false&categoryId='+id+'&categoryOneId='+oneId+'&categoryTwoId='+twoId+'&categoryThreeId='+threeId+'&supplierId='+supplierId;
         }else{
             window.location.href='/shop/companyGoods/getCompanyGoodsList.jhtml?name='+name+'&pubType=pub_supply&priceBoolean=true&categoryId='+id+'&categoryOneId='+oneId+'&categoryTwoId='+twoId+'&categoryThreeId='+threeId+'&supplierId='+supplierId;
         }

     });


     // 人气
     $("#dh-popularity").click(function(){
         if(popularityBoolean == "false"){
             window.location.href='/shop/companyGoods/getCompanyGoodsList.jhtml?name='+name+'&pubType=pub_supply&popularityBoolean=true&categoryId='+id+'&categoryOneId='+oneId+'&categoryTwoId='+twoId+'&categoryThreeId='+threeId+'&supplierId='+supplierId;
         }else if(popularityBoolean == "true"){
             window.location.href='/shop/companyGoods/getCompanyGoodsList.jhtml?name='+name+'&pubType=pub_supply&popularityBoolean=false&categoryId='+id+'&categoryOneId='+oneId+'&categoryTwoId='+twoId+'&categoryThreeId='+threeId+'&supplierId='+supplierId;
         }else{
             window.location.href='/shop/companyGoods/getCompanyGoodsList.jhtml?name='+name+'&pubType=pub_supply&popularityBoolean=true&categoryId='+id+'&categoryOneId='+oneId+'&categoryTwoId='+twoId+'&categoryThreeId='+threeId+'&supplierId='+supplierId;
         }
     });
});
</script>
</html>