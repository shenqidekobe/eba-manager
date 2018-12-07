<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <title>华奕优选</title>
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
                <a class="nav-bar-active nav-type" href="javascript:void(0);" onclick=queryCategory("pub_need"); target="_self" title="采购信息">采购信息</a>
            </div>
        </div>
    </div>
    <input id="selectType" type="hidden" value="pub_need">
    <div class="dh-search-ite">
        <div class="item-list item-list1">
            <h5 class="item-list-title">一级分类</h5>
            <div class="item-list-con">
           [@category_List]
                           <ul class="item">
                           <li class="li1"><a class="item1 item-type-c" href="javascript:void(0);" target="_self" title="全部" onclick=queryCategory("pub_need","");>全部</a></li>
                           [#list categorys as category]
                               <li class="li1">
                                   <a class="item1" href="javascript:void(0);" target="_self" data-id="${category.id}" title="${category.name}" onclick=queryCategory("pub_need","${category.id}","${category.id}");>${category.name}</a>
                                   <div class="item-list item-list2" style="display:none;">
                                       <h5 class="item-list-title">二级分类</h5>
                                       <div class="item-list-con">
                                           <ul class="item">
                                           <li class="li2"><a class="item2 item-type-c" class="total2" href="javascript:void(0);" target="_self" title="全部" onclick=queryCategory("pub_need",${category.id },${category.id });>全部</a></li>
                                           [#list category.children as categoryOne]
                                               <li class="li2">
                                                   <a class="item2" href="javascript:void(0);" target="_self" data-id="${categoryOne.id}" title="${categoryOne.name}" onclick=queryCategory("pub_need","${categoryOne.id}","${category.id}","${categoryOne.id}");>${categoryOne.name}</a>
                                                   <div class="item-list item-list3" style="display:none;">
                                                       <h5 class="item-list-title">三级分类</h5>
                                                       <div class="item-list-con">
                                                           <ul class="item">
                                                           <li class="li3"><a class="item3 item-type-c" href="javascript:void(0);" target="_self" title="全部" onclick=queryCategory("pub_need",${categoryOne.id },${category.id},${categoryOne.id});>全部</a></li>
                                                           [#list categoryOne.children as categoryTwo]
                                                               <li class="li3">
                                                                   <a class="item3" href="javascript:void(0);" target="_self" data-id="${categoryTwo.id}" title="${categoryTwo.name}" onclick=queryCategory("pub_need","${categoryTwo.id}","${category.id}","${categoryOne.id}","${categoryTwo.id}");>${categoryTwo.name}</a>
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
                <a id="dh-packagesNum" href="javascript:void(0);" target="_self" title="采购数">
                    采购数
                    <img class="dh-packagesNum" src="${base}/resources/shop/common/images/shaixuan-a.svg" alt="" />
                </a>
                <a id="dh-popularity" href="javascript:void(0);" target="_self" title="人气">
                    人气
                    <img class="dh-popularity" src="${base}/resources/shop/common/images/shaixuan-a.svg" alt="" />
                </a>
            </div>
        </div>
    </div>
    <div class="all-data">
        <div class="dh-data-box">
            <div class="data-info">
                <h5>采购信息数据</h5>
                <div class="data-con">共计<span class="data-total">${page.total}</span>条记录</div>
            </div>
        </div>
    </div>
    <form id="listForm" action="getCompanyGoodsList.jhtml" method="get">
    <input type="hidden" value="pub_need" name="pubType"/>
    <input id="name" type="hidden"  name="name" value="${name }"/>
    <input id="categoryId" type="hidden"  name="categoryId" value="${categoryId}"/>
    <input id="categoryOneId" type="hidden"  name="categoryOneId" value="${categoryOneId }"/>
    <input id="categoryTwoId" type="hidden"  name="categoryTwoId" value="${categoryTwoId }"/>
    <input id="categoryThreeId" type="hidden" name="categoryThreeId"  value="${categoryThreeId }"/>

    <input id="supplierId" type="hidden" name="supplierId"  value="${supplierId }"/>

    <input id="popularityBoolean" type="hidden" name="popularityBoolean"  value="${(popularityBoolean?string("true", "false"))!}"/>
    <input id="packagesNumBoolean" type="hidden" name="packagesNumBoolean"  value="${(packagesNumBoolean?string("true", "false"))!}"/>
    <div class="dh-search-result">
        <div class="result-box">
            <div class="result-content">
            [#list page.content as needCompanyGoods]
                <div class="filter-result">
                	<input class="goodsId" type="hidden" value="${needCompanyGoods.id }"/>
                    <div class="result-left" onclick=queryDetails("pub_need","${needCompanyGoods.id }");>
                        <div class="goods-detail dh-need">
                            <h5>${needCompanyGoods.name }</h5>
                            <span class="goods-ntime"><i>更新日期：</i>${needCompanyGoods.modifyDate }</span>
                            <div class="goods-info">
                                <p class="price-tag"><i class="goods-par">采购数：</i>
                                <span class="goods-price">
                                [#if needCompanyGoods.needNum == "-1"]
                                    面议
                                [#else]
                                     ${needCompanyGoods.needNum }
                                [/#if]
                                </span>
                                [#if needCompanyGoods.sourceType == "source_type_has"]
					              <i class="goods-cur">                           
					                                        有货
                                  </i>
                                [#elseif needCompanyGoods.sourceType == "source_type_now"]
							             <i class="goods-cur">                          
							                                        现货
                                         </i>
                                [#else]
                                [/#if]
                                </p>
                                <div class="goods-classify">
                                    <div class="goods-di">
                                        <i class="goods-par">产品分类：</i><span class="goods-cont">${needCompanyGoods.category.name }</span>
                                    </div>
                                    <div class="goods-di">
                                         <i class="goods-par goods-par-second">保存条件：</i>
                                         <span class="goods-cont">
                                         [#if needCompanyGoods.storageConditions == "roomTemperature"]
                                             常温
                                         [#elseif needCompanyGoods.storageConditions == "refrigeration"]
                                                                                                                    冷藏
                                         [#elseif needCompanyGoods.storageConditions == "frozen"]
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
                                            [#if needCompanyGoods.unit == "box"]
                                            箱
                                            [#elseif needCompanyGoods.unit == "bottle"]
                                             瓶
                                            [#elseif needCompanyGoods.unit == "bag"]
                                             袋
                                            [#elseif needCompanyGoods.unit == "frame"]
                                                                                                             盒
                                            [#elseif needCompanyGoods.unit == "pack"]
                                                                                                            包
                                            [#else]
                                            [/#if]
                                        </span>
                                    </div>
                                    <div class="goods-di">
                                        <i class="goods-par goods-par-second">产品规格：</i><span class="goods-cont">${needCompanyGoods.goodsSpec }</span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="result-right" onclick=querySupplierDetails(${needCompanyGoods.supplier.id });>
                        <div class="company-img-box">
                            <a href="javascript:void(0);" target="_self">
                                <img src="${needCompanyGoods.supplier.imagelogo }" alt="">
                            </a>
                        </div>
                        <div class="company-detail company-sh">
                            <h5>${needCompanyGoods.supplier.name }</h5>
                            <input class="supplierId" type="hidden" value="${needCompanyGoods.supplier.id}" />
                            <div class="company-info">
                                <p>联系人：<span class="company-contact">${needCompanyGoods.supplier.userName }</span></p>
                                <p>联系电话：<span class="company-phone">${needCompanyGoods.supplier.tel }</span></p>
                            </div>
                            <div class="company-set">
                                [#if needCompanyGoods.supplier.qqCustomerService == null]
                                     <a class="dh-qq-connect">
                                         <span class="company-weixin consult-no"  title="咨询">
                                            <img src="${base}/resources/shop/common/images/zixun-a.svg" alt="" />
                                         </span>
                                     </a>
                                [#else]
                                    <a class="dh-qq-connect" href="http://wpa.qq.com/msgrd?v=3&uin=${needCompanyGoods.supplier.qqCustomerService}&site=qq&menu=yes" target="_blank">
                                        <span class="company-weixin consult" title="咨询" onclick=advice(${needCompanyGoods.supplier.qqCustomerService});>
                                            <img src="${base}/resources/shop/common/images/zixun.svg" alt="" />
                                         </span>
                                    </a>
                                [/#if]
                                [#assign favorCompanyFlag=false]
                                [#if favorCompanyL == null]
                                    <span class="company-collect">
                                        <img class="select${needCompanyGoods.supplier.id}" src="${base}/resources/shop/common/images/shoucang-a.svg" alt="" title="收藏企业"/>
                                    </span>
                                [#else]
                                    [#list favorCompanyL as favorCompany]
                                        [#if needCompanyGoods.supplier.id == favorCompany.supplierId]
                                            [#assign favorCompanyFlag=true]
                                            [#break]
                                        [/#if]
                                    [/#list]
                                    [#if favorCompanyFlag]
                                         <span class="company-collect">
                                             <img class="select${needCompanyGoods.supplier.id}" src="${base}/resources/shop/common/images/shoucang-b.svg" alt="" title="取消收藏" />
                                         </span>
                                    [#else]
                                        <span class="company-collect">
                                            <img class="select${needCompanyGoods.supplier.id}" src="${base}/resources/shop/common/images/shoucang-a.svg" alt="" title="收藏企业" />
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

	 // 采购数
     $("#dh-packagesNum").click(function(){
         if(packagesNumBoolean == "false"){
             window.location.href='/shop/companyGoods/getCompanyGoodsList.jhtml?name='+name+'&pubType=pub_need&packagesNumBoolean=true&categoryId='+id+'&categoryOneId='+oneId+'&categoryTwoId='+twoId+'&categoryThreeId='+threeId+'&supplierId='+supplierId;
         }else if(packagesNumBoolean == "true"){
             window.location.href='/shop/companyGoods/getCompanyGoodsList.jhtml?name='+name+'&pubType=pub_need&packagesNumBoolean=false&categoryId='+id+'&categoryOneId='+oneId+'&categoryTwoId='+twoId+'&categoryThreeId='+threeId+'&supplierId='+supplierId;
         }else{
             window.location.href='/shop/companyGoods/getCompanyGoodsList.jhtml?name='+name+'&pubType=pub_need&packagesNumBoolean=true&categoryId='+id+'&categoryOneId='+oneId+'&categoryTwoId='+twoId+'&categoryThreeId='+threeId+'&supplierId='+supplierId;
         }

     });

	 // 人气
     $("#dh-popularity").click(function(){
         if(popularityBoolean == "false"){
             window.location.href='/shop/companyGoods/getCompanyGoodsList.jhtml?name='+name+'&pubType=pub_need&popularityBoolean=true&categoryId='+id+'&categoryOneId='+oneId+'&categoryTwoId='+twoId+'&categoryThreeId='+threeId+'&supplierId='+supplierId;
         }else if(popularityBoolean == "true"){
             window.location.href='/shop/companyGoods/getCompanyGoodsList.jhtml?name='+name+'&pubType=pub_need&popularityBoolean=false&categoryId='+id+'&categoryOneId='+oneId+'&categoryTwoId='+twoId+'&categoryThreeId='+threeId+'&supplierId='+supplierId;
         }else{
             window.location.href='/shop/companyGoods/getCompanyGoodsList.jhtml?name='+name+'&pubType=pub_need&popularityBoolean=true&categoryId='+id+'&categoryOneId='+oneId+'&categoryTwoId='+twoId+'&categoryThreeId='+threeId+'&supplierId='+supplierId;
         }
     });
});
</script>
</html>