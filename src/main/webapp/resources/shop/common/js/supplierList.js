/**
 * Created by Administrator on 2017/7/28 0028.
 */
$(function () {
	var $statusType = $('#statusType').val();
	if($statusType == 'verified'){
		$('.pick-con .se-ver2').addClass('item-type-c').siblings().removeClass('item-type-c');
        $('.nav-item').text('认证');
	}else if($statusType == 'notCertified'){
        $('.pick-con .se-ver3').addClass('item-type-c').siblings().removeClass('item-type-c');
        $('.nav-item').text('未认证');
	}else{
        $('.pick-con .se-ver1').addClass('item-type-c').siblings().removeClass('item-type-c');
        $('.nav-item').text('全部');
	}

	//认证、未认证样式判定
    $('.company-info .company-qua').each(function () {
        if($(this).text() == '未认证'){
            $(this).addClass('uconfirm-c');
        }else if($(this).text() == '已认证'){
            $(this).addClass('confirm-c');
        }else {
            $(this).addClass('uconfirm-c');
        }
    });

    //刷选：认证/未认证
    $('.dh-pick label.pick-item').off('click').on('click',function () {
    	var name = $("#searchWords").val();
        if($(this).find('i.radio-item').hasClass('radio-selected')){
        	return false;
        }else{
            $(this).find('i.radio-item').addClass('radio-selected')
                .end().siblings().find('i.radio-item').removeClass('radio-selected');
            $(this).find('input[name="pick-or"]').attr({"checked":true})
                .end().siblings().find('input[name="pick-or"]').attr({"checked":false});
        }
    });

});

function querySupplier(name,type){
	if (type == 'verified') {
		window.location.href='/shop/supplie/jumpSupplierList.jhtml?status='+type;
	}
	if (type == 'notCertified') {
		window.location.href='/shop/supplie/jumpSupplierList.jhtml?status='+type;
	}
	if (type == '' || type == null) {
		window.location.href='/shop/supplie/jumpSupplierList.jhtml';
	}
}