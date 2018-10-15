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
	var needProducts = {};
	var needProductsLen = 0 ;
	//子页面调用 增加关联商品
    /**
	 *
     * @param obj
     */
	function addNeedProduct(goodId , products){
        needProducts[goodId] = products;
        needProductsLen++ ;
	}
	//删除关联商品
	function delNeedProduct(goodId){
		delete needProducts[goodId];
        needProductsLen--;
	}

    function getCacheFromChild(){
        return needProducts ;
    }

$().ready(function() {

	var $inputForm = $("#inputForm");
	
	[@flash_message /]

    $("#description").editor();
    // 地区选择
    $("#areaId").lSelect({
        url: "${base}/admin/common/area.jhtml"
    });
	
	// 表单验证
	$inputForm.validate({
		rules: {
            supplier: {
				required: true
			},
            need: {
				required: true
			}/*,
			tel: {
				required: true,
                pattern: /^\d{3,4}-?\d{7,9}$/
			}*/
		},
		messages: {
			tel: {
				pattern: "格式不正确"
			}
		}
	});

	$("#submitButton").click(function(){
        if(!$inputForm.valid()){
            return false ;
        }
        if(needProductsLen == 0){
			$.message({'type':'error' , 'content':'请选择商品'});
			return false ;
		}
        var saveProducts = new Array();
        var tempSaveJson = {};
		var i = 0;
        $.each(needProducts,function(key,value) {
            $.each(value,function(key1,value1) {
                tempSaveJson['needProductList['+i+'].products.id'] = value1['products'] ;
                tempSaveJson['needProductList['+i+'].supplyPrice'] = value1["supplyPrice"] ;
				i++ ;
            });
        });
        tempSaveJson['need.id'] = $("#need").val();
		/*var tempSaveJson = {};
        tempSaveJson.needProducts=saveProducts;*/
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
                        location.href="list.jhtml";
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
		<a href="${base}/admin/common/index.jhtml">${message("admin.breadcrumb.home")}</a> &raquo; ${message("admin.need.add")}
	</div>
	<form id="inputForm" action="save.jhtml" method="post">

		<table class="input tabContent">
			<tr>
				<th>
					<span class="requiredField">*</span>选择企业:
				</th>
				<td>
                    <select id="supplier" name="supplier">
                        <option value="">${message("admin.common.choose")}</option>
						[#list suppliers as supplier]
							[#if supplier != currSupplier]
                                <option value="${supplier.id}">${supplier.name}</option>
							[/#if]
						[/#list]
                    </select>
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>选择收货点:
				</th>
				<td>
					<select id="need" name="need">
                        <option value="">${message("admin.common.choose")}</option>
					</select>
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>店长姓名:
				</th>
				<td>
					<input type="text" id="userName" name="userName" class="text" readonly maxlength="20" autocomplete="off" />
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>店长手机号:
				</th>
				<td>
					<input type="text" id="tel" name="tel" class="text" readonly maxlength="200" />
				</td>
			</tr>

            <tr id="selectPro">
                <th>
                    选择供应商品:
                </th>
                <td>
                    <div style="width:1024px;height:auto;">
                        <iframe marginheight="80px" width="100%" frameborder="no" border="0" height="500px" src="selectList.jhtml?goodListStatus=add"></iframe>
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
<script>

	var needCache = {};
    $().ready(function() {
		$("#supplier").change(function(){
            $("#tel").val("");
            $("#userName").val("");
			var supplierId = $(this).val();
			if("" == supplierId){

			}else{
                if (supplierId in needCache) {
                    $("#need option").slice(1).remove();
                    $.each(needCache[supplierId], function (i, option) {
                        $("#need").append("<option value='"+option.id+"'>"+option.name+"</option");
                    });
                } else {
                    $.ajax({
                        type: "GET",
                        url: "../need/getNeeds.jhtml",
                        data: {"supplierId":supplierId},
                        dataType: "json",
                        beforeSend: function () {

                        },
                        success: function (data) {
                            $("#need option").slice(1).remove();
                            $.each(data, function (i, option) {
                                $("#need").append("<option value='"+option.id+"'>"+option.name+"</option");
                            });

                            needCache[supplierId] = data ;
                        },
                        error: function (data) {
                        }
                    });
                }
			}
		});
		
        $("#need").change(function(){
			var supplierId = $("#supplier").val();
			var needId = $(this).val();
            $("#tel").val("");
            $("#userName").val("");

			if("" == needId){
				return false ;
			}

			$("#tel").val(formatPhone(needCache[supplierId][needId].tel));
			$("#userName").val(needCache[supplierId][needId].userName);

		});

	});

    function formatPhone(phone) {
        return phone.replace(/(\d{3})\d{4}(\d{4})/, "$1****$2");
    }
</script>
[/#escape]