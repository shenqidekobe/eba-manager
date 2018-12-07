<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <title>华奕优选</title>
    <link rel="stylesheet" href="${base}/resources/shop/common/config/bootstrap/css/bootstrap.css">
    <link rel="stylesheet" href="${base}/resources/shop/common/css/public.css">
    <link rel="stylesheet" href="${base}/resources/shop/common/css/index.css">
    <link rel="stylesheet" href="${base}/resources/shop/common/css/showCompany.css">

    <script src="${base}/resources/shop/common/js/jquery.min.js"></script>
    <script src="${base}/resources/shop/common/config/bootstrap/js/bootstrap.min.js"></script>

</head>
    <body class="reg10">
        [#include "/shop/common/head.ftl"]
        <div class="dh-company-box">
            <div class="dh-nav-bar-selected">
                 <div class="nav-bar-con">
                     <div class="nav-bar">
                         <a class="nav-bar-active" href="javascript:void(0);" target="_self" title="首页">首页</a>
                         <i>/</i>
                         <a class="nav-bar-active" href="/shop/supplie/jumpSupplierList.jhtml" target="_self" title="企业展示">企业展示</a>
                         <i>/</i>
                         <a class="nav-item" href="javascript:void(0);" target="_self"></a>
                     </div>
                 </div>
            </div>
            <div class="dh-pick">
                <div class="dh-pick-box">
                    <h5 class="dh-pick-title">筛选</h5>
                    <div class="pick-con">
                        <div class="item">
                            <a class="se-ver1" href="javascript:void(0);" target="_self" title="全部" onclick=querySupplier("","");>全部</a>
                            <a class="se-ver2" href="javascript:void(0);" target="_self" title="认证" onclick=querySupplier("","verified");>认证</a>
                            <a class="se-ver3" href="javascript:void(0);" target="_self" title="未认证" onclick=querySupplier("","notCertified");>未认证</a>
                        </div>
                    </div>
                </div>
            </div>
            <div class="company-data">
                <div class="dh-data-box">
                    <div class="data-info">
                        <h5>企业信息数据</h5>
                        <div class="data-con">共计<span class="data-total">${page.total}</span>条记录</div>
                    </div>
                </div>
            </div>
            <form id="listForm" action="jumpSupplierList.jhtml" method="get">
            <input type="hidden" value="${name }" name="name"/>
            <input id="statusType" type="hidden" value="${status }" name="status" />
            <div class="dh-company-warp">
                <div class="company-con">
                    <div class="container">
                        <div class="row">
                            [#list page.content as supplier]
                            <div class="col-md-3">
                                <div class="company-box" onclick=querySupplierDetails(${supplier.id});>
                                    <div class="company-info company-sh" href="javascript:void(0);" target="_self">
                                    <input class="supplierId" type="hidden" value="${supplier.id}" />
                                        <div class="logo-box">
                                            <img src="${supplier.imagelogo}" alt="">
                                        </div>
                                        <h5>${supplier.name}</h5>
                                        [#if supplier.status == "notCertified"]
                                              <p class="company-qua">未认证</p>
                                        [#elseif supplier.status == "authenticationFailed"]
                                              <p class="company-qua">未认证</p>
                                        [#elseif supplier.status == "certification"]
                                              <p class="company-qua">未认证</p>
                                        [#elseif supplier.status == "verified"]
                                              <p class="company-qua">已认证</p>
                                        [#else]
                                              <p class="company-qua"></p>
                                        [/#if]

                                        <div class="company-detail">
                                            <p>联系人：<span class="company-contact">${supplier.userName}</span></p>
                                            <p>联系电话：<span class="company-phone">${supplier.tel}</span></p>
                                            <p>入驻时间：<span class="company-time">${supplier.createDate}</span></p>
                                        </div>
                                        <div class="company-set">
                                            [#if supplier.qqCustomerService == null]
                                            <a class="dh-qq-connect">
                                             <span class="company-weixin consult-no" title="咨询">
                                                <img src="${base}/resources/shop/common/images/zixun-a.svg" alt="" />
                                             </span>
                                             </a>
                                            [#else]
                                            <a class="dh-qq-connect" href="http://wpa.qq.com/msgrd?v=3&uin=${supplier.qqCustomerService}&site=qq&menu=yes" target="_blank">
                                                <span class="company-weixin consult" title="咨询"  onclick=advice(${supplier.qqCustomerService});>
                                                    <img src="${base}/resources/shop/common/images/zixun.svg" alt="" />
                                                 </span>
                                                <!--<div class="qq-consult">咨询 <i class="triangle-down"></i></div>-->
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
                                </div>
                            </div>
                            [/#list]
                        </div>
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
    <script src="${base}/resources/shop/common/js/supplierList.js"></script>
    
	<script src="${base}/resources/shop/common/js/common.js"></script>
	<script src="${base}/resources/shop/common/js/list.js"></script>
</html>