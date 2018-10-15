/**
 * Created by Administrator on 2017/7/24 0024.
 */
$(function () {
    
    //悬浮显示左右按钮
    // $('.dh-banner').hover(function () {
    //     $('.dh-banner .btn').fadeIn(400);
    // },function () {
    //     $('.dh-banner .btn').fadeOut(400);
    // });
    //图片轮播
    var curr = 0;
    var $imgCount = $('.dh-ul li').length;
    for(var i=0;i<$imgCount;i++){
        var $pageItem = null;
        if(i == 0){
            $pageItem = $('<a href="javascript:;" class="page-item imgSelected">'+(i+1)+'</a>');
        }else{
            $pageItem = $('<a href="javascript:;" class="page-item">'+(i+1)+'</a>');
        }
        $('.banner-nav').append($pageItem);
    }
    $(".dh-banner a.page-item").each(function(i){
        $(this).click(function(){
            curr = i;
            $(".dh-banner .dh-ul li").eq(i).fadeIn("fast").siblings().fadeOut("fast");
            $(this).addClass("imgSelected").siblings().removeClass("imgSelected");
        });
    });
    var timer = setInterval(function(){
        var go = (curr + 1) % $imgCount;
        $(".dh-banner a.page-item").eq(go).click();
    },5000);
    $(".dh-banner .image-set,.dh-banner .btn-right,.dh-banner .btn-left").hover(function(){
        clearInterval(timer);
    },function(){
        timer = setInterval(function(){
            var go = (curr + 1) % $imgCount;
            $(".dh-banner a.page-item").eq(go).click();
        },5000);
    });
    $(".dh-banner .btn-right").click(function(){
        if(curr == $imgCount-1){
            var go = 0;
        }else{
            var go = (curr + 1) % $imgCount;
        }
        $(".dh-banner a.page-item").eq(go).click();
    });
    $(".dh-banner .btn-left").click(function(){
        if(curr == 0){
            var go = $imgCount-1;
        }else{
            var go = (curr - 1) % $imgCount;
        }
        $(".dh-banner a.page-item").eq(go).click();
    });

    //表格内容为空的数据展示
    if($.trim($('#supply-table-content tbody').html()) == ''){
        $('#supply-table-content .no-data').show();
    }else{
        $('#supply-table-content .no-data').hide();
    }

    if($.trim($('#purchase-table-content tbody').html()) == ''){
        $('#purchase-table-content .no-data').show();
    }else{
        $('#purchase-table-content .no-data').hide();
    }

    //更多图片悬浮切换
    $('.dh-floor .dh-more').hover(function () {
        $(this).find('img').attr({"src":"/resources/shop/common/images/gengduo-b.svg"});
    },function () {
        $(this).find('img').attr({"src":"/resources/shop/common/images/gengduo-a.svg"});
    });

    //企业展示轮播
    var rowLeft = 0,//left开始值
        showNum=6;//页面展示的个数
    if($('.company-logo-box .col-md-2').length <= 6){
        return;
    }
    var $cHtml = $('.company-logo-box .col-md-2:lt('+showNum+')').clone(true);
    $('.company-logo-box .row').append($cHtml);
    var $compangCount = $('.company-logo-box .col-md-2').length;
    $('.company-logo-box .container').css({"width":200*$compangCount+"px"});
    var $cWidth = $('.company-logo-box .container').width() - 200*showNum;
    var companyTimer = setInterval(companyLogo,30);

    //鼠标悬浮暂停
    $('.company-logo-box .container').hover(function () {
        clearInterval(companyTimer);
    },function () {
        companyTimer=setInterval(companyLogo,30);
    });

    //企业logo轮播展示
    function companyLogo() {
        rowLeft = rowLeft+2;
        $('.company-logo-box .row').css({"left":-rowLeft+"px"});
        if((rowLeft-30) == $cWidth){
            rowLeft=0;
        }
    }

});

