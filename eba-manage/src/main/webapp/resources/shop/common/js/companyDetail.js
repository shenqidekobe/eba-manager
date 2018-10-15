/**
 * Created by Administrator on 2017/8/9 0009.
 */
$(function () {
    var $length = $('.over-h').text();
    //限制字符个数
    $('.over-h').each(function(){
        var maxwidth=200;
        if($(this).text().length>maxwidth){
            $(this).text($(this).text().substring(0,maxwidth));
            $(this).html($(this).html()+'...');
        }
    });

    //点击更多展示
    $('.summary-more').toggle(function () {
        if($('.summary-more span i').hasClass('glyphicon-chevron-down')){
            $('.summary-more span i').removeClass('glyphicon-chevron-down').addClass('glyphicon-chevron-up');
        }
        $('.over-h').each(function(i,item){
            $(this).text($length);
        });
    },function () {
        if($('.summary-more span i').hasClass('glyphicon-chevron-up')){
            $('.summary-more span i').removeClass('glyphicon-chevron-up').addClass('glyphicon-chevron-down');
        }
        $('.over-h').each(function(){
            var maxwidth=200;
            if($(this).text().length>maxwidth){
                $(this).text($(this).text().substring(0,maxwidth));
                $(this).html($(this).html()+'...');
            }
        });
    });

    //认证、未认证样式判定
    $('.dh-conf').each(function () {
        if($.trim($(this).text()) == '未认证'){
            $(this).addClass('uconfirm-c');
        }else if($.trim($(this).text()) == '已认证'){
            $(this).addClass('confirm-c');
        }else {
            $(this).addClass('uconfirm-c');
        }
    });

    //更多图片悬浮切换
    $('.company-supply .dh-more').hover(function () {
        $(this).find('img').attr({"src":"/resources/shop/common/images/gengduo-b.svg"});
    },function () {
        $(this).find('img').attr({"src":"/resources/shop/common/images/gengduo-a.svg"});
    });
});