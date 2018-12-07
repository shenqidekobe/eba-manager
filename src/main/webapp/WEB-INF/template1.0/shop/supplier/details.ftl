<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <title>华奕优选</title>
    <link rel="stylesheet" href="${base}/resources/shop/common/config/bootstrap/css/bootstrap.css">
    <link rel="stylesheet" href="${base}/resources/shop/common/css/public.css">
    <link rel="stylesheet" href="${base}/resources/shop/common/css/index.css">
    <link rel="stylesheet" href="${base}/resources/shop/common/css/companyDetail.css">

    <script src="${base}/resources/shop/common/js/jquery.min.js"></script>
    <script src="${base}/resources/shop/common/config/bootstrap/js/bootstrap.min.js"></script>

</head>
<body class="reg10">
[#include "/shop/common/head.ftl"]
<div class="dh-company-wrapper">
    <div class="dh-nav-bar-selected">
        <div class="nav-bar-con">
            <div class="nav-bar">
                <a class="nav-bar-active" href="/shop/index.jhtml" target="_self" title="首页">首页</a>
                <i>/</i>
                <a class="nav-bar-active" href="/shop/supplie/jumpSupplierList.jhtml" target="_self" title="企业展示">企业展示</a>
                <i>/</i>
                <a href="javascript:void(0);" target="_self" title="${supplier.name }">${supplier.name }</a>
            </div>
        </div>
    </div>
    <div class="dh-company-box">
        <div class="company-con">
            <div class="company-have">
                <div class="company-box-img"></div>
                <div class="company-detail">
                    <div class="company-logo">
                        <a href="javascript:void(0);" target="_self" title="">
                            <img src="${supplier.imagelogo }" alt="">
                        </a>
                    </div>
                    <div class="company-title">
                        <h5>${supplier.name }</h5>
                        <span class="dh-conf">
                        	 [#if supplier.status == "notCertified"]
                                 未认证
                             [#elseif supplier.status == "authenticationFailed"]
                                 未认证
                             [#elseif supplier.status == "certification"]
                                 未认证
                             [#elseif supplier.status == "verified"]
                                 已认证
                             [#else]

                             [/#if]
                        </span>
                    </div>
                    <div class="company-info company-sh">
                        <input class="supplierId" type="hidden" value="${supplier.id}" />
                        <p>
                            <i>联系人：</i>
                            <span class="company-contact">${supplier.userName } <i>|</i></span>
                        </p>
                        <p>
                            <i>联系电话：</i>
                            <span class="company-contact">${supplier.tel }<i>|</i></span>
                        </p>
                        <p>
                            <i>入驻时间：</i>
                            <span class="company-contact">${supplier.createDate }</span>
                        </p>
                        <div class="company-set">
                            [#if supplier.qqCustomerService == null]
                                 <a class="dh-qq-connect">
                                     <span class="company-weixin consult-no" >
                                        <img src="${base}/resources/shop/common/images/zixun-a.svg" alt="" />
                                     </span>
                                 </a>
                            [#else]
                                <a class="dh-qq-connect" href="http://wpa.qq.com/msgrd?v=3&uin=${supplier.qqCustomerService}&site=qq&menu=yes" target="_blank">
                                    <span class="company-weixin consult" onclick=advice(${supplier.qqCustomerService});>
                                        <img src="${base}/resources/shop/common/images/zixun.svg" alt="" />
                                     </span>
                                </a>
                            [/#if]
                            [#assign favorCompanyFlag=false]
                            [#if favorCompanyL == null]
                                <span class="company-collect">
                                    <img class="select${supplier.id}" src="${base}/resources/shop/common/images/shoucang-a.svg" alt="" title="收藏企业" />
                                </span>
                            [#else]
                                [#list favorCompanyL as favorCompany]
                                    [#if supplier.id == favorCompany.supplierId]
                                        [#assign favorCompanyFlag=true]
                                        [#break]
                                    [/#if]
                                [/#list]
                                [#if favorCompanyFlag]
                                     <span class="company-collect">
                                         <img class="select${supplier.id}" src="${base}/resources/shop/common/images/shoucang-b.svg" alt="" title="取消收藏" />
                                     </span>
                                [#else]
                                    <span class="company-collect">
                                        <img class="select${supplier.id}" src="${base}/resources/shop/common/images/shoucang-a.svg" alt="" title="收藏企业" />
                                    </span>
                                [/#if]
                            [/#if]
                        </div>
                    </div>
                    <div class="company-summary">
                        <div class="company-le">
                            <span>公司简介</span>
                        </div>
                        <div class="company-ce">
                            <div class="over-h">
                               ${supplier.companyProfile }
                            </div>
                        </div>
                        <div class="summary-more">
                            <span>更多<i class="glyphicon glyphicon-chevron-down"></i></span>
                        </div>
                    </div>
                </div>
            </div>
            <div class="company-supply">
                <div class="supply-title">
                    <h5 class="supply-name">
                        <span>供应信息</span>
                    </h5>
                    <a class="dh-more" href="javascript:void(0);" target="_self" onclick=queryGoodsBySupplier("pub_supply","${supplierId }")>
                        <span>查看更多</span>
                        <img src="${base}/resources/shop/common/images/gengduo-a.svg" alt=""/>
                    </a>
                </div>
                <div class="supply-box">
                    <div class="container">
                        <div class="row">
                        [#list supplyCompanyGoodsList.content as supplyCompanyGoods]
                            <div class="col-md-2" onclick=queryDetails("pub_supply","${supplyCompanyGoods.id }");>
                                <div class="supply-item">
                                    <div class="supply-pa">
                                        <a class="img-pa" href="javascript:void(0);" target="_self" title="">
                                            <img class="foods-img" src="${supplyCompanyGoods.image}" alt="">
                                        </a>
                                        <p class="goods-title">
                                            <a href="javascript:void(0);" target="_self" title="">
                                                ${supplyCompanyGoods.name }
                                            </a>
                                        </p>
                                        <p class="goods-price">
                                        <i class="goods-par">参考价格：</i>
                                        <span>
                                         [#if supplyCompanyGoods.marketPrice == "-1"]
                                    		面议
			                             [#else]
			                                   <i>¥ </i>${supplyCompanyGoods.marketPrice }
			                             [/#if]
                                        </span>
                                        </p>
                                    </div>
                                </div>
                            </div>
                            [/#list]
                    </div>
                </div>
            </div>
            <div class="company-need">
                <div class="need-title">
                    <h5 class="need-name">
                        <span>采购信息</span>
                    </h5>
                    <a class="dh-more" href="javascript:void(0);" target="_self" onclick=queryGoodsBySupplier("pub_need","${supplierId }");>
                        <span>查看更多</span>
                        <img src="${base}/resources/shop/common/images/gengduo-a.svg" alt=""/>
                    </a>
                </div>
                <div class="need-box">
                    <div class="container">
                        <div class="row">
                        	[#list needCompanyGoodsList.content as needCompanyGoods]
                            <div class="col-md-2" onclick=queryDetails("pub_need","${needCompanyGoods.id }");>
                                <div class="need-item">
                                    <div class="need-pa">
                                        <a class="img-pa" href="javascript:void(0);" target="_self" title="">
                                            <img class="foods-img" src="${needCompanyGoods.image}" alt="">
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
			                             [/#if]
                                        </span></p>
                                    </div>
                                </div>
                            </div>
                             [/#list]
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
<script src="${base}/resources/shop/common/js/companyDetail.js"></script>
<script src="${base}/resources/shop/common/js/common.js"></script>
<script src="${base}/resources/shop/common/js/list.js"></script>
</html>