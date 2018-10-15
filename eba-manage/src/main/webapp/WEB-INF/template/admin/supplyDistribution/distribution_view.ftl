[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>${message("admin.admin.add")} - Powered By DreamForYou</title>
<meta name="author" content="UTLZ Team" />
<meta name="copyright" content="UTLZ" />
<link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/jquery.tools.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.lSelect.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/jquery.validate.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/webuploader.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/ueditor/ueditor.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/input.js"></script>
<style type="text/css">
.roles label {
	width: 150px;
	display: block;
	float: left;
	padding-right: 6px;
}
</style>
<script type="text/javascript">
	var needProducts = new Array();

	var needProductsStr =[#noescape]'${needProductIds?json_string}'[/#noescape];
	var ids=JSON.parse(needProductsStr).ids;
	$.each(ids, function (i, id) {
		addNeedProduct(id,0);
	});
	console.log(needProducts);
	function getCacheFromChild(){
		return needProducts ;
	}
	//子页面调用 增加关联商品
    /**
	 *
     * @param obj
     */
	function addNeedProduct(products,supplyPrice){
        needProducts.push({
        	'products':products,
        	'supplyPrice':supplyPrice
        });
	}
	//删除关联商品
	function removeByValue(products) {
	  for(var i=0; i<needProducts.length; i++) {
	    if(needProducts[i].products == products) {
	      needProducts.splice(i, 1);
	      break;
	    }
	  }
	}

$().ready(function() {

	var $inputForm = $("#inputForm");
	
	[@flash_message /]
	

	$("#submitButton").click(function(){
        if(!$inputForm.valid()){
            return false ;
        }
        if(needProducts.length == 0){
			$.message({'type':'error' , 'content':'请选择商品'});
			return false ;
		}
		
        var saveProducts = new Array();
        var tempSaveJson = {};
        $.each(needProducts,function(i,value) {
               tempSaveJson['supplierNeedProductList['+i+'].products.id'] = value['products'] ;
               tempSaveJson['supplierNeedProductList['+i+'].supplyPrice'] = value["supplyPrice"] ;
        });
        tempSaveJson['needId'] = $("#needId").val();
        tempSaveJson['supplierSupplierId'] = $("#id").val();
        console.log(tempSaveJson);
        $.ajax({
            type: "POST",
            url: "save.jhtml",
            data: tempSaveJson,
            dataType: "json",
            //contentType:'application/json;charset=utf-8', //设置请求头信息
            beforeSend: function () {

            },
            success: function (message) {
                $.message(message);
                if (message.type == "success") {
                    setTimeout(function () {
                        location.href="distributionList.jhtml?id=${id}";
                    }, 3000);
                }
            },
            error: function (data) {
                $.message(data);
            }
        });
	});


});
	function updatePrice(goodsId , productId , updPrice){
        needProducts[goodsId][productId].supplyPrice = updPrice ;
	}
</script>
</head>
<body>
	<div class="breadcrumb">
		<a href="${base}/admin/common/index.jhtml">${message("admin.breadcrumb.home")}</a> &raquo; 分配商品
	</div>
	<form id="inputForm" action="save.jhtml" method="post">
		<input type="hidden" id="id" name="id" value="${id}"/>
		<input type="hidden" id="needId" name="needId" value="${need.id}"/>
		<table class="input tabContent">
			<tr>
				<th>
					收货点名称:
				</th>
				<td>
					${need.name}
				</td>
			</tr>
			<tr>
				<th>
					店长名称：
				</th>
				<td>
					${need.userName}
				</td>
			</tr>
			<tr>
				<th>
					店长手机号:
				</th>
				<td>
					${need.tel}
				</td>
			</tr>

            <tr id="selectPro">
                <th>
                    	选择分配的商品:
                </th>
                <td>
                    <div style="width:1024px;height:auto;">
                        <iframe marginheight="80px" width="100%" frameborder="no" border="0" height="500px" src="distributionGoods.jhtml?supplierSupplierId=${id}&needId=${need.id}"></iframe>
                    </div>
                </td>
            </tr>

		</table>
		<table class="input">
			<tr>
				<th>
					&nbsp;
				</th>
				<td>
					<input type="button" class="button" id="submitButton" value="${message("admin.common.submit")}" />
					<input type="button" class="button" value="${message("admin.common.back")}" onclick="history.back(); return false;" />
				</td>
			</tr>
		</table>
		<div id="selectProducts" [#--style="display: none"--]>

		</div>
	</form>
</body>
</html>

[/#escape]