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
    <script type="text/javascript" src="${base}/resources/admin/datePicker/WdatePicker.js"></script>
<style type="text/css">
.roles label {
	width: 150px;
	display: block;
	float: left;
	padding-right: 6px;
}
</style>
<script type="text/javascript">

    var products = {};
    var productsLen = 0 ;

    var tempProducts = {} ;

    //子页面调用 增加关联商品
    /**
     *
     * @param obj
     */
    function addProduct(product){

        var proId = product.productId ;
        if(proId in products){
            return false ;
        }
        products[proId] = product;
        tempProducts[proId] = product;

        // var $productTable = $("#productTable");
        //$productTable.append([@compress single_line = true]
                '<tr id="remove_'+product.productId+'"><td>'+product.goodsName+'<\/td><td>'+product.productCategory+'<\/td><td>'+product.specificationValues+'<\/td><td><input type="number" name="" value="1" min="1"/ ><\/td><td><a href="javascript:void(0);" onclick="javascript:delProductChild(this , '+product.productId+');" >删除<\/a><\/td></tr>'
        [/@compress]);

    }
    //删除关联商品
    function delProduct(product){
        var proId = product.productId ;
        delete products[proId];
        delete tempProducts[proId];
    }

    //删除关联商品
    function delProductChild(now , productId){
        delete products[productId];
        $(now).closest("tr").remove();

    }

    function getCacheFromParent(){
        return products ;
    }


    function cleanCache(){
        products = {};
        tempProducts = {} ;
        $("#productTable tr").slice(1).remove();
    }

$().ready(function() {

    var dealCache = function () {
        $.each(tempProducts , function(key , product){
            delete products[key];
        });
    };

    var $productTable = $("#productTable");

    var needs = {} ;
    [#if needs?has_content]
        [#list needs as need]
            needs[${need.id}] = {
                id: "${need.id}",
                name: "${need.name}",
                tel: "${need.tel}",
                userName: "${need.userName}",
                address: "${need.area.fullName} ${need.address}"
            };
        [/#list]
    [/#if]

    $("#need").change(function(){
        var nowVal = $(this).val() ;
        var need = needs[nowVal];
        $("#supplier option").slice(1).remove();
        cleanCache();

        $("#address").val("");
        $("#userName").val("");
        $("#tel").val("");

        if("" != nowVal){
            $("#address").val(need.address);
            $("#userName").val(need.userName);
            $("#tel").val(need.tel);

            $.ajax({
                type: "GET",
                url: "getSuppliers.jhtml",
                data: {"needId":nowVal},
                dataType: "json",
                beforeSend: function () {

                },
                success: function (data) {
                    $.each(data, function (key, option) {
                        $("#supplier").append("<option value='"+key+"'>"+option.name+"</option");
                    });
                },
                error: function (data) {
                }
            });
        }
    });

    $("#supplier").change(function(){
        cleanCache();
    });

    var productLen = 0 ;

    $("#addPro").click(function(){
        if(!$inputForm.valid()){
            return false ;
        }
        var supplierId = $("#supplier").val() ;
        var needId = $("#need").val()
        var contentPath = "../utils/proList.jhtml?supplierId=" + supplierId +"&needId=" + needId;

        $.dialog({
            title: "选择商品",
            [@compress single_line=true]
                content: '<iframe id="sonFrame" marginheight="80px" scrolling="yes" width="100%" height="310px" src="'+contentPath+'"><\/iframe>',
            [/@compress]
            width: 1024,
            ok: "${message("admin.dialog.ok")}",
            cancel: "${message("admin.dialog.cancel")}",
            onOk: function(){
                $.each(tempProducts , function(key , product){
                    $productTable.append([@compress single_line = true]
                        '<tr id="remove_'+product.productId+'"><td>'+product.goodsName+'<\/td><td>'+product.productCategory+'<\/td><td>'+product.specificationValues+'<\/td><td><input type="number" name="ownOrderItems['+productLen+'].quantity" value="1" min="1" class="quantityClass"/ ><\/td><td><a href="javascript:void(0);" onclick="javascript:delProductChild(this , '+product.productId+');" >删除<\/a><\/td><input type="hidden" name="ownOrderItems['+productLen+'].productId" value="'+product.productId+'"></tr>'
                    [/@compress]);
                    productLen++;
                });
                tempProducts = {} ;

            },
            onClose:function(){
                dealCache();
            },
            onCancel:function(){
                dealCache();
            },
            onShow:function(){
                //$(".dialogContent").height("300px");
                //$(".dialogContent").css("overflow" , "auto");
            }
        });

    });

	var $inputForm = $("#inputForm");
	
	[@flash_message /]

    $("#description").editor();
    // 地区选择
    $("#areaId").lSelect({
        url: "${base}/admin/common/area.jhtml"
    });

    $.validator.addClassRules({
        quantityClass:{
            required : true ,
            digits : true
        }
    });
	
	// 表单验证
	$inputForm.validate({
		rules: {
			supplier: {
				required: true
			},
			need: {
				required: true
			},
            reDate: {
                required: true
            }
		},
		messages: {
			username: {
				pattern: "${message("admin.validate.illegal")}",
				remote: "${message("admin.validate.exist")}"
			},
            tel: {
                remote: "手机号已存在"
            }

		},
        submitHandler: function(form) {
            if($.isEmptyObject(products)){
                $.message("warn", "请选择商品");
                return false;
            }
            form.submit();
        }
	});

});
</script>
</head>
<body>
	<div class="breadcrumb">
		<a href="${base}/admin/common/index.jhtml">${message("admin.breadcrumb.home")}</a> &raquo; 收货点代下单
	</div>
	<form id="inputForm" action="create.jhtml" method="post">

		<table class="input tabContent">
            <tr>
                <th>
                    <h1 style="text-align: left ; font-size: 16px">收货信息</h1>
                </th>
                <td colspan="3">
                </td>

            </tr>
            <tr>
                <th>
                    <span class="requiredField">*</span>选择收货点:
                </th>
                <td>
                    <select id="need" name="need">
                        <option value="">${message("admin.common.choose")}</option>
                        [#list needs as need]
                            <option value="${need.id}">${need.name}</option>
                        [/#list]
                    </select>
                </td>
            </tr>


			<tr>
				<th>
					<span class="requiredField">*</span>选择供应商:
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
                    <span class="requiredField">*</span>收货点地址:
                </th>
                <td>
                    <input type="text" id="address" name="address" class="text" readonly maxlength="20" autocomplete="off" />
                </td>
            </tr>


            <tr>
                <th>
                    <span class="requiredField">*</span>联系人:
                </th>
                <td>
                    <input type="text" id="userName" name="userName" class="text" readonly maxlength="20" autocomplete="off" />
                </td>
            </tr>
            <tr>
                <th>
                    <span class="requiredField">*</span>联系电话:
                </th>
                <td>
                    <input type="text" id="tel" name="tel" class="text" readonly maxlength="200" />
                </td>
            </tr>
            <tr>
                <th>
                    <span class="requiredField">*</span>收货时间:
                </th>
                <td>
                    <input type="text" id="reDate" name="reDate" class="text Wdate" onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd',readOnly:true});" />
                </td>
            </tr>

            <tr>
                <th>
                    备注:
                </th>
                <td>
                    <textarea name="memo" class="text"></textarea>
                </td>
            </tr>

		</table>
        <table class="input tabContent">
            <tr>
                <th>
                    <h1 style="text-align: left ; font-size: 16px">商品信息</h1>
                </th>
                <td colspan="3">
                </td>
            </tr>
        </table>
        <div class="tabContent">
            <table id="specificationTable" class="productTable input">
                <tr>
                    <th>
                        &nbsp;
                    </th>
                    <td>
                        <a href="javascript:;" id="addPro" class="button">添加商品</a>
                    </td>
                </tr>
            </table>
            <table id="productTable" class="productTable item">
                <tr>
                    <th>
                        <a href="javascript:;">${message("Goods.productCategory")}</a>
                    </th>
                    <th>
                        <a href="javascript:;">${message("Goods.name")}</a>
                    </th>

                    <th>
                        <a href="javascript:;">规格</a>
                    </th>
                    <th>
                        <a href="javascript:;">商品数量</a>
                    </th>
                    <th>
                        <a href="javascript:;">操作</a>
                    </th>
                </tr>
            </table>
        </div>
        [#--<table class="list">
            <tr>
                <th>
                    <a href="javascript:;">序号</a>
                </th>
                <th>
                    <a href="javascript:;">${message("Goods.productCategory")}</a>
                </th>
                <th>
                    <a href="javascript:;">${message("Goods.name")}</a>
                </th>

                <th>
                    <a href="javascript:;">规格</a>
                </th>
            </tr>
            <tr>
                <td>
                    <a href="javascript:;">序号</a>
                </td>
                <td>
                    <a href="javascript:;">${message("Goods.productCategory")}</a>
                </td>
                <td>
                    <a href="javascript:;">${message("Goods.name")}</a>
                </td>

                <td>
                    <a href="javascript:;">规格</a>
                </td>
            </tr>
        </table>--]
		<table class="input">
			<tr>
				<th>
					&nbsp;
				</th>
				<td>
					<input type="submit" class="button" value="${message("admin.common.submit")}" />
					<input type="button" class="button" value="${message("admin.common.back")}" onclick="history.back(); return false;" />
				</td>
			</tr>
		</table>
	</form>
</body>
</html>
[/#escape]