[#assign shiro = JspTaglibs["/WEB-INF/tld/shiro.tld"] /]
<div class="dh-toolbar">
            <div class="dh-toolbar-con wrapper">
                <div class="dh-toolbar-left">
                    <div class="lazy-bar-box">
                        <a class="dh-bar-node dh-bar-node-site" href="javascript:void(0);">
                            <span>欢迎使用微商平台商流平台</span>
                        </a>
                    </div>
                    [@shiro.user]
						<div class="lazy-bar-box">
	                        <a class="dh-bar-node" href="javascript:void(0);">
	                           <span>
								[@shiro.principal/]
							</span>
	                        </a>
	                    </div>
	                    <div class="lazy-bar-box">
	                        <input id="dh-login" type="hidden" value="true" />
                            <a class="dh-bar-node dh-bar-node-exit adminOut" href="javascript:void(0);" onclick="logout()">
                                <span>退出</span>
                            </a>
                        </div>
                    [/@shiro.user]
                    [@shiro.guest]
					<div class="lazy-bar-box">
					    <input id="dh-login" type="hidden" value="false" />
                        <a class="dh-bar-node dh-bar-node-exit" href="javascript:void(0);" onclick="login()">
                            <span class="login-span">登录</span>
                        </a>
                        <a class="dh-bar-node dh-bar-node-exit" href="javascript:void(0);" onclick="register()">
                            <span class="login-span">注册</span>
                        </a>
                    </div>
                    [/@shiro.guest]
                </div>
                <div class="dh-toolbar-right">
                    <div id="ord-bar-node" class="dh-bar-node ord-bar-node">
                        <a class="reg-touch reg-join-node" href="/admin" target="_blank">
                            <span>进入订单系统</span>
                        </a>
                    </div>
                    [@msg]
                    <div class="dh-bar-node-box my-info">
                    	[#if msgPage == null]
                        [#else]
                           [#if msgPage.total == 0]
                           [#else]
                                 <span class="red_news"></span>
                           [/#if]
                        [/#if]
                        <a class="dh-bar-node reg-touch" href="javascript:void(0);">
                            <span>消息中心</span>
                            <!--<i class="glyphicon glyphicon-chevron-down"></i>-->
                            <img src="${base}/resources/shop/common/images/caidanjt-a.svg" alt="" />
                        </a>
                        <div class="dh-down-box dh-d-box" style="display: none;">
                                [#if msgPage == null]
                                [#else]
                                	[#if msgPage.total == 0]
	                                [#else]
	                                    <span class="news_sum">${msgPage.total }</span>
	                                [/#if]
                                [/#if]
                        	<a href="/shop/member/msg/list.jhtml" target="_self">系统消息</a>
                        </div>
                    </div>
                    [/@msg]
                    <div class="dh-bar-node-box my-commercial-trend">
                        <a class="dh-bar-node reg-touch" href="javascript:void(0);">
                            <span>我的商流</span>
                            <img src="${base}/resources/shop/common/images/caidanjt-a.svg" alt="" />
                        </a>
                        <div class="dh-down-box dh-d-box my-flow" style="display: none;">
                            <a class="dh-my-info" href="javascript:void(0);" style="cursor: default" target="_self"><strong>我的供应</strong></a>
                            <a class="dh-my-info" href="javascript:void(0);" style="cursor: default" target="_self"><strong>我的采购</strong></a>
                            <a href="/shop/member/supply/addIndex.jhtml" target="_self">发布供应信息</a>
                            <a href="/shop/member/supply/addPur.jhtml" target="_self">发布采购信息</a>
                            <a href="/shop/member/supply/list.jhtml" target="_self">已发布信息</a>
                            <a href="/shop/member/supply/purchaseList.jhtml" target="_self">已发布信息</a>
                        </div>
                    </div>
                    <div class="dh-bar-node-box my-collection">
                        <a class="dh-bar-node reg-touch" href="javascript:void(0);">
                            <span>我的收藏</span>
                            <img src="${base}/resources/shop/common/images/caidanjt-a.svg" alt="" />
                        </a>
                        <div class="dh-down-box dh-d-box" style="display: none;">
                            <a href="/shop/member/favorCompanyGoods/index.jhtml" target="_self">收藏产品</a>
                            <a href="/shop/member/favorCompany/index.jhtml" target="_self">收藏企业</a>
                        </div>
                    </div>
                    <div class="dh-bar-node-box my-service">
                        <a class="dh-bar-node reg-touch" href="javascript:void(0);">
                            <span>客户服务</span>
                            <img src="${base}/resources/shop/common/images/caidanjt-a.svg" alt="" />
                        </a>
                        <div class="dh-down-box dh-d-box dh-server" style="display: none;">
                            <p>客服电话：</p>
                            <p>400-8869-219</p>
                            <p>周一至周五：</p>
                            <p>9:00-18:00</p>
                        </div>
                    </div>
                    <div class="dh-bar-node-box my-weixin">
                          <a class="dh-bar-node reg-touch" href="javascript:void(0);">
                              <span>关注微商平台</span>
                              <img src="${base}/resources/shop/common/images/caidanjt-a.svg" alt="" />
                          </a>
                          <div class="dh-down-box dh-d-box dh-wei" style="display: none;">
                                <div class="my-qr-code">
                                    <img src="/resources/shop/common/images/weixin.jpg" alt="二维码">
                                </div>
                                <div class="dh-about-wei">扫码关注微商平台</div>
                          </div>
                    </div>
                </div>
            </div>
        </div>
    <div class="index-header">
        <div class="dh-container">
        </div>
        <div class="dh-header-con">
            <div class="dh-header-logo">
                <a class="logo-set" href="/shop/index.jhtml" title="微商平台">
                    <img class="logo1" src="${base}/resources/shop/common/images/logo.svg" alt="微商平台">
                    <img class="logo2" src="${base}/resources/shop/common/images/slogan.svg" alt="微商平台">
                </a>
            </div>
            <div class="dh-search">
                <div class="d-search">
                    <div id="infoSearch" class="d-info-search">
                        <span id="showDetail">供应商</span>
                        <input id="searchDetail" type="hidden" value="supplier">
                        <img class="search-down" src="${base}/resources/shop/common/images/biaodanjt-b.svg" alt="">
                        <div class="d-search-item" style="display: none;">
                            <a id="pubSupply" class="d-search-detail" href="javascript:void(0);" name="pub_supply">产品</a>
                            <a id="supplier" class="d-search-detail" href="javascript:void(0);" name="supplier">供应商</a>
                            <a id="pubNeed" class="d-search-detail" href="javascript:void(0);" name="pub_need">采购</a>
                        </div>
                    </div>
                    <div class="search-keyword-con">
                    	<input id="searchWords" class="search-keyword" type="text" placeholder="请输入关键词查询..." value="${name}">
                    </div>
                    <input id="searchSubmit" class="search-submit" value="搜索" type="submit">
                </div>
            </div>
            <div class="dh-s-n">
                <div class="dh-supply-t" onclick="location='/shop/member/supply/addIndex.jhtml';">发布供应信息</div>
                <span class="dh-fen">/</span>
                <div class="dh-need-t" onclick="location='/shop/member/supply/addPur.jhtml';">发布采购信息</div>
            </div>
        </div>
    </div>
    <div class="dh-nav-bar">
        <div class="classify-index">
            <a class="classify-box" href="javascript:void(0);" target="_self">
                <span>全部商品分类</span>
                <i class="glyphicon glyphicon-th-list"></i>
            </a>
            <div class="goods-list">
                <div class="goods-list-box" style="display:none;">
                    <div class="goods-cover">
                    [@category_List]
                        <ul class="goods-list-ul">
                            [#list categorys as category]
                                <li class="goods-item">
                                    <a class="category-name" href="javascript:void(0);" target="_self" onclick=queryCategory("pub_supply","${category.id}","${category.id}");>${category.name}</a>
                                    <div class="goods-list-detail">
                                        <div class="item-list">
                                            [#list category.children as categoryOne]
                                                <dl>
                                                    <dt>
                                                        <a href="javascript:void(0);" target="_self" title="${categoryOne.name}" onclick=queryCategory("pub_supply","${categoryOne.id}","${category.id}","${categoryOne.id}");>${categoryOne.name}</a>
                                                    </dt>
                                                    <dd>
                                                        [#list categoryOne.children as categoryTwo]
                                                            <a href="javascript:void(0);" target="_self" title="${categoryTwo.name}" onclick=queryCategory("pub_supply","${categoryTwo.id}","${category.id}","${categoryOne.id}","${categoryTwo.id}");>${categoryTwo.name}</a>
                                                        [/#list]
                                                    </dd>
                                                </dl>
                                            [/#list]
                                        </div>
                                    </div>
                                </li>
                            [/#list]
                        </ul>
                    [/@category_List]
                    </div>
                </div>
            </div>
        </div>
        <input type="hidden" value="${type }" id="typeId"/>
        <div class="dh-nav-index">
            <ul class="dh-nav">
                <li class="item item1">
                    <a href="/shop/index.jhtml" target="_self">首页</a>
                </li>
                <li class="item item2">
                    <a href="/shop/companyGoods/getCompanyGoodsList.jhtml?pubType=pub_supply" target="_self">供应信息</a>
                </li>
                <li class="item item3">
                    <a href="/shop/companyGoods/getCompanyGoodsList.jhtml?pubType=pub_need" target="_self">采购信息</a>
                </li>
                <li class="item item4">
                    <a href="/shop/supplie/jumpSupplierList.jhtml" target="_self">企业展示</a>
                </li>
            </ul>
        </div>
    </div>
    <div class="clear"></div>
    <div class="dh-sidebar">
        <div id="sidebarTop" class="dh-sidebar-top">
            <i class="glyphicon glyphicon-arrow-up"></i>
            <p>TOP</p>
        </div>
    </div>
    
    
  <script>
  	function logout(){
  		var url = window.location.pathname;
  		var param =  getUrlParam(window.location.href);
  		window.location = '/shop/login/logout.jhtml?redirectUrl='+encodeURIComponent(url+param);
  	}
  	
  	function login(){
  		var url = window.location.pathname;
  		var param = getUrlParam(window.location.href);
  		window.location = '/shop?redirectUrl='+encodeURIComponent(url+param);
  	}

  	function getUrlParam(url) {
  		var index = url.indexOf("?");
  	    if(index != -1){
  	    	return url.substring(index,url.length);
  	    } else {
  	    	return "";
  	    }
  	}
  </script>