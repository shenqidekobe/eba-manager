<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <title>华奕优选</title>
    <link rel="stylesheet" href="${base}/resources/shop/common/config/bootstrap/css/bootstrap.css">
    <link rel="stylesheet" href="${base}/resources/shop/common/css/public.css">
    <link rel="stylesheet" href="${base}/resources/shop/common/css/index.css">

    <script src="${base}/resources/shop/common/js/jquery.min.js"></script>
    <script src="${base}/resources/shop/common/config/bootstrap/js/bootstrap.min.js"></script>

</head>
<body class="reg10">
    [#include "/shop/common/head.ftl"]
    <div class="dh-images-change">
        <div class="dh-banner-wrapper">
            <div class="dh-banner">
                <ul class="dh-ul">
                [@ad_List id=1]
                	[#list adList as ad]
                    <li style="background: #${ad.backgroundColor} none repeat scroll 0% 0%;">
                        <a class="image-set" [#if ad.url == null] href="javascript:void(0);" [/#if][#if ad.url != null] href="${ad.url }" [/#if] target="_self" title="">
                            <img src="${ad.path }" alt="">
                        </a>
                    </li>
                    [/#list]
                [/@ad_List]
                </ul>
                <a class="btn btn-left" href="javascript:void(0);" style="display:none;">
                    <i class="glyphicon glyphicon-chevron-left"></i>
                </a>
                <a class="btn btn-right" href="javascript:void(0);" style="display:none;">
                    <i class="glyphicon glyphicon-chevron-right"></i>
                </a>
                <div class="dh-banner-box">
                    <div class="banner-nav">

                    </div>
                </div>
            </div>
        </div>
        <div class="dh-user">
            <div class="user-info">
                <!--<div class="user-img">-->
                    <!--<a href="javascript:void(0);">-->
                        <!--<img class="la-img" src="" />-->
                        <!--<span class="user-hover"></span>-->
                    <!--</a>-->
                <!--</div>-->
                <!--<p class="user-infor">Hi,你好</p>-->
                <div class="dh-user-con">
                    [@shiro.guest]
                    <div class="user-set">
                        <input id="dh-login" type="hidden" value="false" />
                        <a class="user-login" href="javascript:void(0);" onclick="login()">
                            登录
                        </a>
                        <a class="user-register" href="javascript:void(0);" onclick="register()">
                            注册
                        </a>
                    </div>
                    [/@shiro.guest]
                    [@shiro.user]
                    <div class="login-suc">
                        <input id="dh-login" type="hidden" value="true" />
                        <div class="welcome">Hi! [@shiro.principal/]</div>
                        <div class="user-exit" onclick="logout()">退出</div>
                    </div>
                    [/@shiro.user]
                </div>
                <div class="btn btn-set" onclick="location='/shop/member/supply/addIndex.jhtml';">发布供应信息</div>
                <div class="btn btn-set" onclick="location='/shop/member/supply/addPur.jhtml';">发布采购信息</div>
            </div>
            <div class="user-qr-code">
                <img src="${base}/resources/shop/common/images/weixin.jpg" alt="二维码">
            </div>
        </div>
    </div>
    <div class="dh-floor">
        <div class="supply-content">
            <div class="content">
                <div class="supply-title">
                    <h5 class="supply-name">
                        <span>供应专区</span>
                    </h5>
                    <a class="dh-more" href="/shop/companyGoods/getCompanyGoodsList.jhtml?pubType=pub_supply" target="_self">
                        <span>查看更多</span>
                        <img src="${base}/resources/shop/common/images/gengduo-a.svg" alt=""/>
                    </a>
                </div>
                <div class="su-box">
                    <div class="supply-tab">
                        <a class="supply-img-com" href="javascript:void(0);" target="_self">
                        	[@ad_List id=2]
			                	[#list adList as ad]
			                    <img src="${ad.path }" alt="">
			                    [/#list]
			                [/@ad_List]
                        </a>
                    </div>
                    <div class="dh-table">
                        <table id="supply-table-header" class="table table-border table-hover table_width">
                            <thead>
                                <tr class="text-l">
                                    <th width="18%" style="max-width:18%;">产品名称</th>
                                    <th width="10%" style="max-width:10%;">产品分类</th>
                                    <th width="18%" style="max-width:18%;">企业名称</th>
                                    <th width="10%" style="max-width:10%;">参考价格</th>
                                    <th width="14%" style="max-width:14%;">货源类型</th>
                                    <th width="16%" style="max-width:16%;">发布日期</th>
                                    <th width="14%" style="max-width:14%;">操作</th>
                                </tr>
                            </thead>
                        </table>
                        <div id="supply-table-content" class="dh-body">
                            <table class="table table-border table-hover table_width">
                                <thead>
                                    <tr class="text-l">
                                    <th width="18%"><div class="th-div">产品名称</div></th>
                                    <th width="10%"><div class="th-div">产品分类</div></th>
                                    <th width="18%"><div class="th-div">企业名称</div></th>
                                    <th width="10%"><div class="th-div">参考价格</div></th>
                                    <th width="14%"><div class="th-div">货源类型</div></th>
                                    <th width="16%"><div class="th-div">发布日期</div></th>
                                    <th width="14%"><div class="th-div">操作</div></th>
                                </tr>
                                </thead>
                                <tbody>
                                    [#list supplyCompanyGoodsList.content as supplyCompanyGoods]
                                         <tr class="text-l">
                                              <td style="max-width:18%;"><div class="dh-table-o" style="width:162px">${supplyCompanyGoods.name}</div></td>
                                              <td style="max-width:10%;"><div class="dh-table-o" style="width:90px">${supplyCompanyGoods.category.name}</div></td>
                                              <td style="max-width:18%;"><div class="dh-table-o" style="width:162px">${supplyCompanyGoods.supplier.name}</div></td>
                                              <td style="max-width:10%;"><div class="dh-table-o" style="width:90px">
                                              	[#if supplyCompanyGoods.marketPrice == "-1"]
				                                	面议
				                                [#else]
				                                  	  ￥${supplyCompanyGoods.marketPrice}
				                                [/#if]</div>
                                              </td>
                                              [#if supplyCompanyGoods.sourceType == "source_type_has"]
                                                      <td style="max-width:14%;"><div class="dh-table-o" style="width:126px">有货</div></td>
                                                [#elseif supplyCompanyGoods.sourceType == "source_type_now"]
                                                      <td style="max-width:14%;"><div class="dh-table-o" style="width:126px">现货</div></td>
                                                [#else]
                                                  <td style="max-width:14%;"><div class="dh-table-o" style="width:126px"></div></td>
                                                [/#if]
                                              <td style="max-width:16%;"><div class="dh-table-o" style="width:144px">${supplyCompanyGoods.modifyDate}</div></td>
                                              <td style="max-width:14%;" class="text-look" onclick=queryDetails("pub_supply","${supplyCompanyGoods.id }");><div class="dh-table-o" style="width:126px">查看</div></td>
                                         </tr>
                                    [/#list]
                                </tbody>
                            </table>
                            <div class="no-data">暂无数据</div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="purchase-content">
            <div class="content">
                <div class="purchase-title">
                    <h5 class="purchase-name">
                        <span>采购专区</span>
                    </h5>
                    <a class="dh-more" href="/shop/companyGoods/getCompanyGoodsList.jhtml?pubType=pub_need" target="_self">
                        <span>查看更多</span>
                        <img src="${base}/resources/shop/common/images/gengduo-a.svg" alt=""/>
                    </a>
                </div>
                <div class="pu-box">
                    <div class="purchase-tab">
                        <a class="purchase-img-com" href="javascript:void(0);" target="_self">
                            [@ad_List id=3]
			                	[#list adList as ad]
			                    <img src="${ad.path }" alt="">
			                    [/#list]
			                [/@ad_List]
                        </a>
                    </div>
                    <div class="dh-table">
                        <table id="purchase-table-header" class="table table-border table-hover table_width">
                            <thead>
                            <tr class="text-l">
                                <th width="18%" style="max-width:18%;">产品名称</th>
                                <th width="10%" style="max-width:10%;">产品分类</th>
                                <th width="18%" style="max-width:18%;">企业名称</th>
                                <th width="10%" style="max-width:10%;">采购数量</th>
                                <th width="14%" style="max-width:14%;">货源类型</th>
                                <th width="16%" style="max-width:16%;">发布日期</th>
                                <th width="14%" style="max-width:14%;">操作</th>
                            </tr>
                            </thead>
                        </table>
                        <div id="purchase-table-content" class="dh-body">
                            <table class="table table-border table-hover table_width">
                                <thead>
                                <tr class="text-l">
                                    <th width="18%"><div class="th-div">产品名称</div></th>
                                    <th width="10%"><div class="th-div">产品分类</div></th>
                                    <th width="18%"><div class="th-div">企业名称</div></th>
                                    <th width="10%"><div class="th-div">采购数量</div></th>
                                    <th width="14%"><div class="th-div">货源类型</div></th>
                                    <th width="16%"><div class="th-div">发布日期</div></th>
                                    <th width="14%"><div class="th-div">操作</div></th>
                                </tr>
                                </thead>
                                <tbody>
                                [#list needCompanyGoodsList.content as needCompanyGoods]
                                     <tr class="text-l">
                                          <td  style="max-width:18%;"><div class="dh-table-o" style="width:162px">${needCompanyGoods.name}</div></td>
                                          <td  style="max-width:10%;"><div class="dh-table-o" style="width:90px">${needCompanyGoods.category.name}</div></td>
                                          <td  style="max-width:18%;"><div class="dh-table-o" style="width:162px">${needCompanyGoods.supplier.name}</div></td>
                                          <td  style="max-width:10%;"><div class="dh-table-o" style="width:90px">
                                           [#if needCompanyGoods.needNum == "-1"]
			                                	面议
			                               [#else]
			                                	 ${needCompanyGoods.needNum }
			                               [/#if]</div>
                                          </td>
                                          [#if needCompanyGoods.sourceType == "source_type_has"]
                                                <td  style="max-width:14%;"><div class="dh-table-o" style="width:126px">有货</div></td>
                                          [#elseif needCompanyGoods.sourceType == "source_type_now"]
                                                <td  style="max-width:14%;"><div class="dh-table-o" style="width:126px">现货</div></td>
                                          [#else]
                                            <td  style="max-width:14%;"><div class="dh-table-o" style="width:126px"></div></td>
                                          [/#if]
                                          <td  style="max-width:16%;"><div class="dh-table-o" style="width:144px">${needCompanyGoods.modifyDate}</div></td>
                                          <td  style="max-width:14%;" class="text-look" onclick=queryDetails("pub_need","${needCompanyGoods.id }");><div class="dh-table-o" style="width:126px">查看</div></td>
                                     </tr>
                                 [/#list]
                                </tbody>
                            </table>
                            <div class="no-data">暂无数据</div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="company-content">
            <div class="content">
                <div class="company-title">
                    <h5 class="company-name">
                        <span>推荐企业</span>
                    </h5>
                </div>
                <div class="company-logo-box">
                    <div class="container">
                        <div class="row">
                            [#list recommendSupplierList as recommendSupplier]
                                <div class="col-md-2" onclick=querySupplierDetails(${recommendSupplier.id});>
                                    <a class="company-logo" href="javascript:void(0);" target="_self">
                                        <img src="${recommendSupplier.imagelogo}" alt="">
                                        <input class="company${recommendSupplier.id}" type="hidden" value="${recommendSupplier.id}">
                                    </a>
                                </div>
                            [/#list]
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    [#include "/shop/common/foot.ftl"]
</body>
<script src="${base}/resources/shop/common/js/public.js"></script>
<script src="${base}/resources/shop/common/js/index.js"></script>
<script>
	function register(){
		var url = window.location.pathname;
		var param = getUrlParam(window.location.href);
		window.location = '/admin/registered/index.jhtml?type=shop&redirectUrl='+encodeURIComponent(url+param);
	}
	
</script>
</html>