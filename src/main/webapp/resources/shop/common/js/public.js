/**
 * Created by Administrator on 2017/7/27 0027.
 */

$(function () {
	$("title").html("微商平台商流系统");
	var oneCategory = $("#oneCategory").val();
	var twoCategory = $("#twoCategory").val();
	var threeCategory = $("#threeCategory").val();
	if (oneCategory != null && oneCategory!='') {
		$(".oneCategory").css("display","block");
	}
	if (twoCategory != null && twoCategory!='') {
		$(".twoCategory").css("display","block");
	}
	if (threeCategory != null && threeCategory!='') {
		$(".threeCategory").css("display","block");
	}
	
    var $pageType = $('#typeId').val();//页面选中状态
    	//往上滚动
    $(document).on('click','#sidebarTop',function () {
        $('html,body').animate({scrollTop: '0px'}, 800);
    });
    $("#sidebarTop").hide();
    
	$(document).scroll(function(){
		if(document.documentElement.scrollTop == 0){
			if(document.body.scrollTop<50){
				$("#sidebarTop").hide(1000);
			}else{
				$("#sidebarTop").show(1000);
			}
		}else{
			if(document.documentElement.scrollTop<50){
				$("#sidebarTop").hide(1000);
			}else{
				$("#sidebarTop").show(1000);
			}
		}
		
	});
	
    if($pageType == "index"){
        $('.dh-nav .item1').addClass("item-active").siblings().removeClass("item-active");
        $('.goods-list-box').show();
        $('#searchWords').val('').focus();
        $('#showDetail').text($('#supplier').text());
        $('#searchDetail').val('supplier');
    }else if($pageType == "pub_supply"){
        $('.dh-nav .item2').addClass("item-active").siblings().removeClass("item-active");
        $('#showDetail').text($('#pubSupply').text());
        $('#searchDetail').val('pub_supply');
    }else if($pageType == "pub_need"){
        $('.dh-nav .item3').addClass("item-active").siblings().removeClass("item-active");
        $('#showDetail').text($('#pubNeed').text());
        $('#searchDetail').val('pub_need');
    }else if($pageType == "supplier"){
        $('.dh-nav .item4').addClass("item-active").siblings().removeClass("item-active");
        $('#showDetail').text($('#supplier').text());
        $('#searchDetail').val('supplier');
    }else{
        // $('.dh-nav .item1').addClass("item-active").siblings().removeClass("item-active");
        $('#searchDetail').val('supplier');
    }

    //其他页面菜单栏导航浮动显示
    $('.classify-index').stop().hover(function () {
        if($pageType == "index"){
            return false;
        }else{
            $('.goods-list-box').slideDown(400);
        }
    },function () {
        if($pageType == "index"){
            return false;
        }else{
            $('.goods-list-box').slideUp(400);
        }
    });

    //tab切换
    $('.goods-list .goods-item').each(function (i) {
        $(this).hover(function () {
            $(this).find('.goods-list-detail').css({"top":-50*i+"px"}).stop(true,true).fadeIn(400);
        },function () {
            $(this).find('.goods-list-detail').css({"top":-50*i+"px"}).stop(true,true).fadeOut(400);
        });
    });

    //导航栏鼠标浮动展示
    $('.dh-toolbar .dh-bar-node-box').each(function (i) {
        $(this).hover(function () {
            $(this).addClass("dh-bar-hover").find('.dh-down-box').stop(true,true).slideDown(200);
        },function () {
            $(this).removeClass("dh-bar-hover").find('.dh-down-box').stop(true,true).slideUp(200);
        })
    });

    //查询表单左边选择项鼠标浮动展示
    $('.dh-search #infoSearch').hover(function () {
        $(this).find('div.d-search-item').stop().slideDown(200);
    },function () {
        $(this).find('div.d-search-item').stop().slideUp(200);
    });

    //查询表单左边选择项选定
    $(document).on('click','.d-search .d-search-detail',function (e) {
        var $selectInfo = $(e.target).text();
        var $selectType = $(e.target).attr("name");
        $('#showDetail').text($selectInfo);
        $('#searchDetail').val($selectType);
    });

    //搜索关键字
    $("#searchSubmit").click(function(){
    	var type = $("#searchDetail").val();
    	var name = $("#searchWords").val();
    	if (type == 'pub_supply' || type == 'pub_need') {
    		window.location.href='/shop/companyGoods/getCompanyGoodsList.jhtml?pubType='+type+'&name='+name;
    	}
    	if (type == 'supplier') {
    		window.location.href='/shop/supplie/jumpSupplierList.jhtml?name='+name;
    	}
    });

    //点击enter键搜索
    $(document).keydown(function (e) {
        if(e.keyCode == 13){
            $("#searchSubmit").click();
        }
    });

    //企业咨询
    $('.dh-qq-connect').on('click',function (e) {
        if($(this).find('.company-weixin').hasClass('consult-no')){
            $.message("warn","该企业未绑定客服,无法咨询！");
        }
        // else if($(this).hasClass('consult')){
        //     $.message("warn","咨询！");
        // }
        e.stopPropagation();
    });

    //企业展示-企业收藏
    $('.company-collect').on('click',function (e) {
        var $id = $(this).parents('.company-sh').find('.supplierId').val();
        var _this = $(this);
        var $loginV = $('#dh-login').val();
        var url = window.location.pathname;
  		var param = getUrlParam(window.location.href);
        if($loginV == 'false'){
        	window.location = '/shop?redirectUrl='+encodeURIComponent(url+param);
        }else{
            $.ajax({
                url: "/shop/member/adminManage/favorCompany.jhtml",
                type: "POST",
                async: false,
                data: {supplierId: $id},
                dataType: "json",
                cache: false,
                success: function(data) {
                    if(data == null){
                        return false;
                    }else{
                        if (data.type == "success") {
                            if(_this.find('img.select'+$id).attr("src") == '/resources/shop/common/images/shoucang-b.svg'){
                                $('img.select'+$id).each(function (i,item) {
                                    $(item).attr("src","/resources/shop/common/images/shoucang-a.svg").prop("title","收藏企业");
                                });
                                
                            }else{
                                $('img.select'+$id).each(function (i,item) {
                                    $(item).attr("src","/resources/shop/common/images/shoucang-b.svg").prop("title","取消收藏");
                                });
                            }
                        }
                    }
                }
            });
        }

        e.stopPropagation();
    });

    //产品收藏
    $('.goods-collect').on('click',function (e) {
        var $id = $(this).parents('.company-sh').find('.goodsId').val();
        var _this = $(this);
        var $loginV = $('#dh-login').val();
        var url = window.location.pathname;
  		var param = getUrlParam(window.location.href);
        if($loginV == 'false'){
      		window.location = '/shop?redirectUrl='+encodeURIComponent(url+param);
        }else{
            $.ajax({
                url: "/shop/member/adminManage/favorCompanyGoods.jhtml",
                type: "POST",
                async: false,
                data: {supplierId: $id},
                dataType: "json",
                cache: false,
                success: function(data) {
                    if(data == null){
                        return false;
                    }else{
                        if (data.type == "success") {
                            if(_this.find('img').attr("src") == '/resources/shop/common/images/shoucang-b.svg'){
                                _this.find('img').attr("src","/resources/shop/common/images/shoucang-a.svg");
                                _this.find("span").html("收藏产品");
                            }else{
                                _this.find('img').attr("src","/resources/shop/common/images/shoucang-b.svg");
                                _this.find("span").html("取消收藏");
                            }
                        }
                    }
                }
            });
        }

        e.stopPropagation();
    });
});

function queryDetails(type,id){
	if (type!=null && id!=null && type!='' && id!='') {
		window.location.href='/shop/companyGoods/companyGoodsLook.jhtml?pubType='+type+'&id='+id;
	}else{
		alert("请稍后重试");
	}
}

function queryCategory(type, id, oneId, twoId, threeId, flag, supplierId){
	
	if(flag){
        var categoryId = $("#categoryTwoId").val();
		if(categoryId!=null && categoryId!=""){
            var categoryId = $("#categoryOneId").val();
            id = categoryId;
            oneId = categoryId
		}
        var categoryIdNew = $("#categoryThreeId").val();
		if(categoryIdNew!=null&&categoryIdNew!=""){
            var categoryId = $("#categoryTwoId").val();
            id = categoryId;
            twoId = categoryId;
		}

	}

	var name = "";
    if (id==null) {
        id = "";
    }else {
    	var name = $("#searchWords").val();
	}
    
	if (oneId==null) {
		oneId = "";
	}
	if (twoId==null) {
		twoId = "";
	}
	if (threeId==null) {
		threeId = "";
	}
	if (supplierId==null) {
		supplierId = "";
	}
	window.location.href='/shop/companyGoods/getCompanyGoodsList.jhtml?name='+name+'&pubType='+type+'&categoryId='+id+'&categoryOneId='+oneId+'&categoryTwoId='+twoId+'&categoryThreeId='+threeId+'&supplierId='+supplierId;
}

function querySupplierDetails(id){
	window.location.href='/shop/supplie/supplierLook.jhtml?id='+id;
}

function queryGoodsBySupplier(type, id){
	window.location.href='/shop/companyGoods/getCompanyGoodsList.jhtml?pubType='+type+'&supplierId='+id;
}

function getUrlParam(url) {
	var index = url.indexOf("?");
    if(index != -1){
    	return url.substring(index,url.length);
    } else {
    	return "";
    }
}
