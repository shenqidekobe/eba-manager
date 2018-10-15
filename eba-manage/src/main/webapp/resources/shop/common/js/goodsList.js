/**
 * Created by Administrator on 2017/8/1 0001.
 */
$(function () {
    //获取隐藏域中的值
    var categoryOneId = $('#categoryOneId').val();
    var categoryTwoId = $('#categoryTwoId').val();
    var categoryThreeId = $('#categoryThreeId').val();
    var $selectType = $('#selectType').val();
    var packagesNumBoolean = $('#packagesNumBoolean').val();
    var popularityBoolean = $('#popularityBoolean').val();
    var priceBoolean = $('#priceBoolean').val();

    var $first = $('<i>/</i><a class="first-total" href="javascript:void(0);" target="_self" title="全部">全部</a>');
    $('.nav-type').after($first);

    //上部nav条最后无法点击
    if(categoryOneId != "" && categoryTwoId != ""){
        $('.first-total').attr({'onclick':'queryCategory("pub_supply","","","","",true);'})
    }

    //有货、现货样式展示
    $('.goods-info .goods-cur').each(function () {
        if($.trim($(this).text()) == '有货'){
            $(this).addClass('uconfirm-c');
        }else if($.trim($(this).text()) == '现货'){
            $(this).addClass('confirm-c');
        }else {
            $(this).addClass('uconfirm-c');
        }
    });

    //人气、价格\采购数刷选样式默认展示
    if(packagesNumBoolean == "true"){
        $('#dh-packagesNum').css({"background-color":"#4DA2FF","color":"#fff"});
        $('.dh-packagesNum').attr({"src":"/resources/shop/common/images/shaixuan-c.svg"});
    }else if(packagesNumBoolean == "false"){
        $('#dh-packagesNum').css({"background-color":"#4DA2FF","color":"#fff"});
        $('.dh-packagesNum').attr({"src":"/resources/shop/common/images/shaixuan-b.svg"});
    }
    if(popularityBoolean == "true"){
        $('#dh-popularity').css({"background-color":"#4DA2FF","color":"#fff"});
        $('.dh-popularity').attr({"src":"/resources/shop/common/images/shaixuan-c.svg"});
    }else if(popularityBoolean == "false"){
        $('#dh-popularity').css({"background-color":"#4DA2FF","color":"#fff"});
        $('.dh-popularity').attr({"src":"/resources/shop/common/images/shaixuan-b.svg"});
    }
    if(priceBoolean == "true"){
        $('#dh-price').css({"background-color":"#4DA2FF","color":"#fff"});
        $('.dh-price').attr({"src":"/resources/shop/common/images/shaixuan-c.svg"});
    }else if(priceBoolean == "false"){
        $('#dh-price').css({"background-color":"#4DA2FF","color":"#fff"});
        $('.dh-price').attr({"src":"/resources/shop/common/images/shaixuan-b.svg"});
    }


    //二级展示，三级隐藏
    $('.dh-search-ite .item a.item1').each(function (i,item) {
        if($(this).attr('data-id') == categoryOneId){
            $(this).parents('.item-list1').addClass('border-bot-none').parents('.dh-search-ite').find('.item-c-list1').show();
            $(this).next('.item-list2').show().end().parents('.li1').siblings().find('.item-list2').hide();
            $(this).addClass('item-type-c').parents('.li1').siblings().find('.item1').removeClass('item-type-c');
            $('.first-total').text($(this).text()).attr({"title":$(this).text()});
        }
    });

    //三级展示
    $('.dh-search-ite .item a.item2').each(function (i,item) {
        if($(this).attr('data-id') == categoryTwoId){
            $(this).parents('.item-list2').addClass('border-bot-none').parents('.dh-search-ite').find('.item-c-list2').show();
            $(this).next('.item-list3').show().end().parents('.li2').siblings().find('.item-list3').hide();
            $(this).addClass('item-type-c').parents('.li2').siblings().find('.item2').removeClass('item-type-c');
            var $secondType = $('<i>/</i><a class="second-total" href="javascript:void(0);" target="_self" title="'+$(this).text()+'">'+$(this).text()+'</a>')
            $('.first-total').addClass('nav-bar-active').after($secondType);
            if(categoryTwoId != "" && categoryThreeId != ""){
                $('.second-total').attr({'onclick':'queryCategory("'+$selectType+'","","","","",true);'})
            }
        }
    });

    //三级选中状态
    $('.dh-search-ite .item a.item3').each(function (i,item) {
        if($(this).attr('data-id') == categoryThreeId){
            $(this).addClass('item-type-c').parents('.li3').siblings().find('.item3').removeClass('item-type-c');
            var $thirdType = $('<i>/</i><a class="third-total" href="javascript:void(0);" target="_self" title="'+$(this).text()+'">'+$(this).text()+'</a>')
            $('.second-total').addClass('nav-bar-active').after($thirdType);
        }
    });


});


