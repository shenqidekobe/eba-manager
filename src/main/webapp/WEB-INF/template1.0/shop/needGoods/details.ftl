<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <title>微信小程序</title>
    <link rel="stylesheet" href="${base}/resources/shop/common/config/bootstrap/css/bootstrap.css">
    <link rel="stylesheet" href="${base}/resources/shop/common/css/public.css">
    <link rel="stylesheet" href="${base}/resources/shop/common/css/index.css">
    <link rel="stylesheet" href="${base}/resources/shop/common/css/goodsDetail.css">

    <script src="${base}/resources/shop/common/js/jquery.min.js"></script>
    <script src="${base}/resources/shop/common/config/bootstrap/js/bootstrap.min.js"></script>

</head>
<body class="reg10">
[#include "/shop/common/head.ftl"]
<div class="dh-goods-wrapper">
    <div class="dh-nav-bar-selected">
        <div class="nav-bar-con">
            <div class="nav-bar">
               <a class="nav-bar-active" href="/shop/index.jhtml" target="_self" title="首页">首页</a>
                <i>/</i>
                <a class="nav-bar-active" href="/shop/companyGoods/getCompanyGoodsList.jhtml?pubType=pub_need" target="_self" title="采购信息">采购信息</a>
                <sapn style="display:none" class="oneCategory"> 
                <i>/</i>
                <a onclick=queryCategory("pub_need","${companyGoods.category.parent.parent.id }","${companyGoods.category.parent.parent.id }"); class="nav-bar-active" href="javascript:void(0);" target="_self" title="${companyGoods.category.parent.category.name }">${companyGoods.category.parent.parent.name }</a>
                </sapn>
                <sapn style="display:none" class="twoCategory"> 
                <i>/</i>
                <a onclick=queryCategory("pub_need","${companyGoods.category.parent.id }","${companyGoods.category.parent.parent.id }","${companyGoods.category.parent.id }"); class="nav-bar-active" href="javascript:void(0);" target="_self" title="${companyGoods.category.parent.name }">${companyGoods.category.parent.name }</a>
                </sapn>
                <sapn style="display:none" class="threeCategory"> 
                <i>/</i>
                <a onclick=queryCategory("pub_need","${companyGoods.category.id }","${companyGoods.category.parent.parent.id }","${companyGoods.category.parent.id }","${companyGoods.category.id }"); class="nav-bar-active" href="javascript:void(0);" target="_self" title="${companyGoods.category.name }">${companyGoods.category.name }</a>
                 </sapn>
                <i>/</i>
                <a href="javascript:void(0);" target="_self" title="${companyGoods.name }">${companyGoods.name }</a>
            </div>
        </div>
    </div>
    <input  id="oneCategory" type="hidden" value="${companyGoods.category.parent.parent.name }"/>
    <input  id="twoCategory" type="hidden" value="${companyGoods.category.parent.name }"/>
    <input  id="threeCategory" type="hidden" value="${companyGoods.category.name }"/>
    <div class="dh-goods-box">
        <div class="goods-content">
            <div class="goods-con">
                <div class="dh-goods-d dh-goods-need">
                    <div class="goods-detail dh-goods-ne">
                        <h5>${companyGoods.name }</h5>
                        <div class="dh-checking">
                              [#if companyGoods.status == "status_wait"]
                                    <span class="checking-detail checking-wait">未审核</span>
                              [/#if]
                              [#if companyGoods.status == "status_rej"]
                                    <span class="checking-detail checking-no">审核未通过</span>
                              [/#if]
                        </div>
                        <div class="goods-info">
                            <p class="price-tag"><i class="goods-par">采购数：</i>
                            <span class="goods-price">
                             [#if companyGoods.needNum == "-1"]
                                    	面议
                             [#else]
                                     ${companyGoods.needNum }
                             [/#if]
                            </span>
                            	[#if companyGoods.sourceType == "source_type_has"]
						             <i class="goods-cur">                         
						                                        有货
                                        </i>
                                [#elseif companyGoods.sourceType == "source_type_now"]
						             <i class="goods-cur">                          
						                                        现货
                                        </i>
                                [#else]
                                [/#if]
                            
                            </p>
                            <div class="goods-classify">
                            <div class="goods-di">
                                <i class="goods-par">产品分类：</i><span class="goods-cont">${companyGoods.category.name }</span>
                            </div>
                            <div>
                                <i class="goods-par goods-par-second">保存条件：</i>
                                <span class="goods-cont">
                                    [#if companyGoods.storageConditions == "roomTemperature"]
                                                                                                                常温
                                    [#elseif companyGoods.storageConditions == "refrigeration"]
                                                                                                               冷藏
                                    [#elseif companyGoods.storageConditions == "frozen"]
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
                                        [#if companyGoods.unit == "box"]
                                            箱
                                            [#elseif companyGoods.unit == "bottle"]
                                             瓶
                                            [#elseif companyGoods.unit == "bag"]
                                             袋
                                            [#elseif companyGoods.unit == "frame"]
                                              盒
                                            [#elseif companyGoods.unit == "pack"]
                                            包
                                            [#else]
                                            [/#if]
                                    </span>
                                </div>
                                <div class="goods-di">
                                    <i class="goods-par goods-par-second">产品规格：</i><span class="goods-cont">${companyGoods.goodsSpec }</span>
                                </div>
                            </div>
                            <!--<p class="goods-ntime">-->
                            <!--<span>2017-7-31  2:29 <i>更新</i></span>-->
                            <!--</p>-->
                        </div>
                    </div>
                    <div class="goods-contact">
                        <div class="goods-phone">
                            <i class="glyphicon glyphicon-earphone"></i>
                            <span>${companyGoods.supplier.tel }</span>
                        </div>
                        <div class="goods-consult">
                            [#if companyGoods.supplier.qqCustomerService == null]
                                 <a class="dh-qq-connect">
                                     <span class="company-weixin consult-no">
                                        <img src="${base}/resources/shop/common/images/zixun-a.svg" alt="" />
                                     </span>
                                     <span>咨询</span>
                                 </a>
                            [#else]
                                <a class="dh-qq-connect" href="http://wpa.qq.com/msgrd?v=3&uin=${companyGoods.supplier.qqCustomerService}&site=qq&menu=yes" target="_blank">
                                    <span class="company-weixin consult" onclick=advice(${companyGoods.supplier.qqCustomerService});>
                                        <img src="${base}/resources/shop/common/images/zixun.svg" alt="" />
                                     </span>
                                     <span>咨询</span>
                                </a>
                            [/#if]
                        </div>
                    </div>
                </div>
                <div class="dh-company" onclick=querySupplierDetails(${companyGoods.supplier.id});>
                    <div class="company-info company-sh">
                        <input class="supplierId" type="hidden" value="${companyGoods.supplier.id}" />
                        <div class="logo-box">
                            <img src="${companyGoods.supplier.imagelogo }" alt="">
                        </div>
                        <h5>${companyGoods.supplier.name }</h5>
                        	 [#if companyGoods.supplier.status == "notCertified"]
                                 <p class="company-qua">未认证</p>
                             [#elseif companyGoods.supplier.status == "authenticationFailed"]
                                 <p class="company-qua">未认证</p>
                             [#elseif companyGoods.supplier.status == "certification"]
                                 <p class="company-qua">未认证</p>
                             [#elseif companyGoods.supplier.status == "verified"]
                                 <p class="company-qua">已认证</p>
                             [#else]
                                 <p class="company-qua"></p>
                             [/#if]
                         <div class="company-detail">
                            <div class="goods-di"><i class="goods-par">联系人：</i><span class="company-contact">${companyGoods.supplier.userName}</span></div>
                             <div class="goods-di"><i class="goods-par">联系电话：</i><span class="company-phone">${companyGoods.supplier.tel}</span></div>
                             <div class="goods-di"><i class="goods-par">入驻时间：</i><span class="company-time">${companyGoods.supplier.createDate }</span></div>
                         </div>
                        <div class="company-set">
                            [#assign favorCompanyFlag=false]
                            [#if favorCompanyL == null]
                                <span class="company-collect">
                                    <img class="select${companyGoods.supplier.id}" src="${base}/resources/shop/common/images/shoucang-a.svg" alt="" title="收藏企业" />
                                </span>
                            [#else]
                                [#list favorCompanyL as favorCompany]
                                    [#if companyGoods.supplier.id == favorCompany.supplierId]
                                        [#assign favorCompanyFlag=true]
                                        [#break]
                                    [/#if]
                                [/#list]
                                [#if favorCompanyFlag]
                                     <span class="company-collect">
                                         <img class="select${companyGoods.supplier.id}" src="${base}/resources/shop/common/images/shoucang-b.svg" alt="" title="取消收藏" />
                                     </span>
                                [#else]
                                    <span class="company-collect">
                                        <img class="select${companyGoods.supplier.id}" src="${base}/resources/shop/common/images/shoucang-a.svg" alt="" title="收藏企业" />
                                    </span>
                                [/#if]
                            [/#if]
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="clear"></div>
    <div class="goods-show">
        <div class="goods-detail-box">
            <div class="goods-list-l">
                <div class="list-header">
                    <h5>该企业其他采购</h5>
                </div>
                <ul class="list-other">
                  [#list needCompanyGoodsList.content as needCompanyGoods]
                    <li onclick=queryDetails("pub_need","${needCompanyGoods.id }");>
                        <a class="dh-other" href="javascript:void(0);" target="_self" title="">
                            <img class="foods-img" src="${base}/resources/shop/common/images/moren.svg" alt="">
                        </a>
                        <p class="goods-title">
                            <a href="javascript:void(0);" target="_self" title="">
                                ${needCompanyGoods.name }
                            </a>
                        </p>
                        <p class="goods-price">
                        <i class="goods-par">采购数：</i>
                        <span>
                        [#if needCompanyGoods.needNum == "-1"]
                                   	 面议
                        [#else]
                                     ${needCompanyGoods.needNum }
                                [/#if]</span></p>
                    </li>
                   [/#list]
                </ul>
            </div>
            <div class="goods-list-r">
                <div class="goods-header">
                    <ul class="goods-tab">
                        <li>
                            <a class="tab" href="javascript:void(0);">商品详情</a>
                        </li>
                    </ul>
                </div>
                <div class="goods-detail-img">
                    <div class="goods-detail-con">
                        <div class="goods-image">
                           ${companyGoods.introduction }
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
[#include "/shop/common/foot.ftl"]
</div>
</body>
<script src="${base}/resources/shop/common/js/public.js"></script>
<script src="${base}/resources/shop/common/js/common.js"></script>
<script src="${base}/resources/shop/common/js/list.js"></script>
</html>