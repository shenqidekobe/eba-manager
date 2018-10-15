[#escape x as x?html]
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title></title>
	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <link rel="stylesheet" type="text/css" href="${base}/resources/admin1.0/H-ui/static/h-ui/css/H-ui.min.css" />
    <link rel="stylesheet" type="text/css" href="${base}/resources/admin1.0/H-ui/static/h-ui.admin/css/H-ui.admin.css" />
    <link rel="stylesheet" type="text/css" href="${base}/resources/admin1.0/H-ui/static/h-ui.admin/skin/default/skin.css" id="skin" />
    <link rel="stylesheet" type="text/css" href="${base}/resources/admin1.0/H-ui/static/h-ui.admin/css/style.css" />
    <link rel="stylesheet" href="${base}/resources/admin1.0/js/date/dateRange.css" />
    <link rel="stylesheet" type="text/css" href="${base}/resources/admin1.0/js/kkpager/kkpager_blue.css" />
    <link rel="stylesheet" href="${base}/resources/admin1.0/css/public.css" />
    <link rel="stylesheet" href="${base}/resources/admin1.0/css/admin.css" />
    <style>
        body{background:#f9f9f9;}
        .pag_div{width:45%;float:left;}
		.col-sm-7{width:72%;}
        th{border-top:1px solid #eaeefb;}
		.form_box{
			overflow: auto;
			overflow-x: hidden;
		}
    </style>
</head>
<body>
<form id="inputForm" action="update.jhtml" method="post" class="form form-horizontal">
	<input type="hidden" name="id" id="id" value="${supplierRelation.id}" />
    <div class="child_page">
        <div class="cus_nav">
            <ul>
            	<li><a id="goHome"	href="../homePage/index.jhtml">${message("admin.breadcrumb.home")}</a></li>
            	<li><a href="list.jhtml">供应商管理列表</a></li>
                <li>编辑</li>
            </ul>
        </div>
        <div class="form_box">

            <div class='form_baseInfo'>
                <h3 class="form_title" style="margin:20px 0 0 20px;">基本信息</h3>
                <div class="pag_div">
                	<div class="row cl">
                        <label class="form-label col-xs-4 col-sm-3">
                        	<img src="${base}/resources/admin1.0/images/bitian_icon.svg" alt="" />${message("CustomerRelation.supplierName")}</label>
                        <div class="formControls col-xs-8 col-sm-7">
                            <span class="input_no_span">${supplierRelation.supplier.name}</span>
                        </div>
                    </div>
                    <div class="row cl">
                        <label class="form-label col-xs-4 col-sm-3">${message("CustomerRelation.supplierArea")}</label>
                        <div class="formControls col-xs-8 col-sm-7">
                           <span class="fieldSet">
									<input type="hidden" id="areaId" name="areaId" value="${supplierRelation.supplierArea.id!supplierRelation.supplier.area.id}" treePath="${supplierRelation.supplierArea.treePath!supplierRelation.supplier.area.treePath}" />
								</span>
                        </div>
                    </div>
                </div>
                <div class="pag_div">
                    <div class="row cl">
                        <label class="form-label col-xs-4 col-sm-3">
                        	${message("CustomerRelation.supplierCode")}</label>
                        <div class="formControls col-xs-8 col-sm-7">
                            <input type="text" class="input-text radius" id="supplierCode" name="supplierCode" value="${supplierRelation.supplierCode}" />
                        </div>
                    </div>

                    <div class="row cl">
                        <label class="form-label col-xs-4 col-sm-3">${message("CustomerRelation.supplierAddress")}</label>
                        <div class="formControls col-xs-8 col-sm-7">
                            <input type="text" id="supplierAddress" name="supplierAddress" value="${supplierRelation.supplierAddress}" class="input-text radius" />
                        </div>
                    </div>
                </div>
                <h3 class="form_title" style="margin:20px 0 0 20px;">联系人信息</h3>
				<div class="pag_div">
                    <div class="row cl">
                        <label class="form-label col-xs-4 col-sm-3">
                        	${message("CustomerRelation.supplierUserName")}</label>
                        <div class="formControls col-xs-8 col-sm-7">
                            <input type="text" id="supplierUserName" name="supplierUserName" value="${supplierRelation.supplierUserName!supplierRelation.supplier.userName}" class="input-text radius" />
						</div>
                    </div>
                    <div class="row cl">
                        <label class="form-label col-xs-4 col-sm-3">${message("CustomerRelation.supplierEmail")}</label>
                        <div class="formControls col-xs-8 col-sm-7">
                            <input type="text" id="supplierEmail" name="supplierEmail" value="${supplierRelation.supplierEmail!supplierRelation.supplier.email}" class="input-text radius" />
                        </div>
                    </div>
				</div>
				<div class="pag_div">
                    <div class="row cl">
                        <label class="form-label col-xs-4 col-sm-3">${message("CustomerRelation.supplierTel")}</label>
                        <div class="formControls col-xs-8 col-sm-7">
                            <input type="text" id="supplierTel" name="supplierTel" value="${supplierRelation.supplierTel!supplierRelation.supplier.tel}" class="input-text radius" />
                        </div>
                    </div>
                </div>
                <h3 class="form_title" style="margin:20px 0 0 20px;">财务信息</h3>
				<div class="pag_div">
                    <div class="row cl">
                        <label class="form-label col-xs-4 col-sm-3">${message("CustomerRelation.supAccountName")}</label>
                        <div class="formControls col-xs-8 col-sm-7">
                            <input type="text" id="supAccountName" name="supAccountName" value="${supplierRelation.supAccountName}" class="input-text radius" />
                        </div>
                    </div>
                    <div class="row cl">
                        <label class="form-label col-xs-4 col-sm-3">${message("CustomerRelation.bank")}</label>
                        <div class="formControls col-xs-8 col-sm-7">
                            <input type="text" id="supplierBank" name="supplierBank" value="${supplierRelation.supplierBank}"  class="input-text radius" />
                        </div>
                    </div>
                </div>

                
				
				<div class="pag_div">
                    <div class="row cl">
                        <label class="form-label col-xs-4 col-sm-3">${message("CustomerRelation.supplierInvoice")}</label>
                        <div class="formControls col-xs-8 col-sm-7">
                            <input type="text" id="supplierInvoice" name="supplierInvoice" value="${supplierRelation.supplierInvoice}" class="input-text radius" />
                        </div>
                    </div>
                    <div class="row cl">
                        <label class="form-label col-xs-4 col-sm-3">${message("CustomerRelation.supBankAccountNum")}</label>
                        <div class="formControls col-xs-8 col-sm-7">
                            <input type="text" id="supBankAccountNum" name="supBankAccountNum" value="${supplierRelation.supBankAccountNum}" class="input-text radius" />
                        </div>
                    </div>
                </div>
            </div>
    
        </div>
    </div>
    <div class="footer_submit">
        <input class="btn radius confir_S" type="submit" value="${message("admin.common.submit")}">
        <input class="btn radius cancel_B" type="button" value="${message("admin.common.back")}" onclick="history.back();return false;">
    </div>
</form>

<script src="${base}/resources/admin1.0/js/jquery.min.js"></script>
<script type="text/javascript" src="${base}/resources/admin1.0/js/validate/jquery.validate.min.js"></script>
<script type="text/javascript" src="${base}/resources/admin1.0/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/admin1.0/datePicker/WdatePicker.js"></script>
<script type="text/javascript" src="${base}/resources/admin1.0/js/jquery.lSelect.js"></script>

<script type="text/javascript">
    $().ready(function() {
        var $inputForm = $("#inputForm");
        [@flash_message /]

     	// 地区选择
	    $("#areaId").lSelect({
	        url: "${base}/admin/common/area.jhtml"
	    });

		/*获取页面的高度*/
        var formHeight = $(document.body).height() - 100;
        $(".form_box").css("height", formHeight);


        /*表单验证*/
		$("#inputForm").validate({
			rules:{
                supplierCode:{
					maxlength:20
				},
				areaId:{
					required:true
				},
				supplierEmail:{
					email:true
				},
                supplierTel:{
					pattern: /^1[3|4|5|7|8]\d{9}$/
				}
			},
			messages:{
                supplierCode:{
					maxlength:"最多支持20个字符"
				},
				areaId:{
					required:"客户地区不能为空"
				},
				supplierEmail:{
					email:"请输入正确格式的电子邮件"
				},
                supplierTel:{
					pattern:"手机格式不正确"
				}
			}
		})
		
		
		$("#goHome").on("click",function(){
			var nav = window.top.$(".index_nav_one");
    			nav.find("li li").removeClass('clickTo');
				nav.find("i").removeClass('click_border');
		})
    });
</script>

</body>
</html>
[/#escape]
