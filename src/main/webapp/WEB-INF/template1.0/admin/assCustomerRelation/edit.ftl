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
	<input type="hidden" name="id" id="id" value="${customerRelation.id}" />
	<input type="hidden" name="oldClientName" id="oldClientName" value="${oldClientName}" />
    <div class="child_page">
        <div class="cus_nav">
            <ul>
            	<li><a id="goHome"	href="../homePage/index.jhtml">${message("admin.breadcrumb.home")}</a></li>
            	<li><a href="list.jhtml">${message("admin.customerRelation.list")}</a></li>
                <li>${message("admin.assCustomerRelation.edit")}</li>
            </ul>
        </div>
        <div class="form_box">

            <div class='form_baseInfo'>
                <h3 class="form_title" style="margin:20px 0 0 20px;">基本信息</h3>
                <div class="pag_div">
                	 <div class="row cl">
                        <label class="form-label col-xs-4 col-sm-3">
                        	<img src="${base}/resources/admin1.0/images/bitian_icon.svg" alt="" />${message("CustomerRelation.clientName")}</label>
                        <div class="formControls col-xs-8 col-sm-7">
                            <input type="text" class="input-text radius" id="clientName" name="clientName" value="${customerRelation.clientName}" />
                        </div>
                    </div>
                    <div class="row cl">
                        <label class="form-label col-xs-4 col-sm-3">
                        	<img src="${base}/resources/admin1.0/images/bitian_icon.svg" alt="" />${message("CustomerRelation.area")}</label>
                        <div class="formControls col-xs-8 col-sm-7"> 
								<span class="fieldSet">
									<input type="hidden" id="areaId" name="areaId" value="${customerRelation.area.id}" treePath="${customerRelation.area.treePath}" />
								</span>
						</div>
                    </div>
                </div>
                <div class="pag_div">
                    <div class="row cl">
                        <label class="form-label col-xs-4 col-sm-3">${message("CustomerRelation.clientNum")}</label>
                        <div class="formControls col-xs-8 col-sm-7">
                            <input type="text" id="clientNum" name="clientNum" value="${customerRelation.clientNum}" class="input-text radius" />
                        </div>
                    </div>
                    <div class="row cl">
                        <label class="form-label col-xs-4 col-sm-3">
                        	<img src="${base}/resources/admin1.0/images/bitian_icon.svg" alt="" />${message("CustomerRelation.address")}</label>
                        <div class="formControls col-xs-8 col-sm-7">
                            <input type="text" id="address" name="address" value="${customerRelation.address}" class="input-text radius" />
                        </div>
                    </div>
                </div>
                <h3 class="form_title" style="margin:20px 0 0 20px;">联系人信息</h3>
                <div class="pag_div">
                    <div class="row cl">
                        <label class="form-label col-xs-4 col-sm-3">${message("CustomerRelation.userName")}</label>
                        <div class="formControls col-xs-8 col-sm-7">
                            <input type="text" id="userName" name="userName" value="${customerRelation.userName}" class="input-text radius" />
                        </div>
                    </div>
                    <div class="row cl">
                        <label class="form-label col-xs-4 col-sm-3">${message("CustomerRelation.email")}</label>
                        <div class="formControls col-xs-8 col-sm-7">
                            <input type="text" id="email" name="email" value="${customerRelation.email}" class="input-text radius" />
                        </div>
                    </div>
                </div>
                <div class="pag_div">
                    <div class="row cl">
                        <label class="form-label col-xs-4 col-sm-3">${message("CustomerRelation.tel")}</label>
                        <div class="formControls col-xs-8 col-sm-7">
                            <input type="text" id="tel" name="tel" value="${customerRelation.tel}" class="input-text radius" />
                        </div>
                    </div>
                </div>
                <h3 class="form_title" style="margin:20px 0 0 20px;">财务信息</h3>
                <div class="pag_div">
                    <div class="row cl">
                        <label class="form-label col-xs-4 col-sm-3">${message("CustomerRelation.accountName")}</label>
                        <div class="formControls col-xs-8 col-sm-7">
                            <input type="text" id="accountName" name="accountName" value="${customerRelation.accountName}" class="input-text radius" />
                        </div>
                    </div>
                    <div class="row cl">
                        <label class="form-label col-xs-4 col-sm-3">${message("CustomerRelation.bank")}</label>
                        <div class="formControls col-xs-8 col-sm-7">
                            <input type="text" id="bank" name="bank" value="${customerRelation.bank}"  class="input-text radius" />
                        </div>
                    </div>
                </div>
                
                
                <div class="pag_div">
                    <div class="row cl">
                        <label class="form-label col-xs-4 col-sm-3">${message("CustomerRelation.invoice")}</label>
                        <div class="formControls col-xs-8 col-sm-7">
                            <input type="text" id="invoice" name="invoice" value="${customerRelation.invoice}" class="input-text radius" />
                        </div>
                    </div>
                    <div class="row cl">
                        <label class="form-label col-xs-4 col-sm-3">${message("CustomerRelation.bankAccountNum")}</label>
                        <div class="formControls col-xs-8 col-sm-7">
                            <input type="text" id="bankAccountNum" name="bankAccountNum" value="${customerRelation.bankAccountNum}" class="input-text radius" />
                        </div>
                    </div>

                </div>
            </div>
    
        </div>
    </div>
    <div class="footer_submit">
        <input class="btn radius confir_S" type="button" id="yes" value="${message("admin.common.submit")}">
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

		/*下拉框的样式*/
        $(".input_box .box_right ul li").on("click",function(){
            $(this).parent().siblings(".weight_unit").val($(this).html());
            $(this).parent().siblings(".downList_val").val($(this).attr("val"));

            $(this).parent().siblings(".downList_val").change();
  
            $(this).parent().css("display","none");
            $(this).addClass("li_bag").siblings().removeClass("li_bag");
        });
        $(".input_box .box_right").mouseover(function(){
            $(this).find("ul").css("display","block");
        });
        $(".input_box .box_right").mouseout(function(){
            $(this).find("ul").css("display","none");
        });

        $(".down_list").click(function(){
            $(this).siblings(".downList_con").toggle();
        });

        $("*").click(function (event) {
            if (!$(this).hasClass("down_list")&&!$(this).hasClass("downList_con")){
                $(".downList_con").hide();
            }
            event.stopPropagation();
        });
        
        $(".downList_con").each(function(){

            var curr = $(this).find("li.li_bag");
            var firstText ;
            var firstVal ;
            if(curr.length == 0){
                firstText = $(this).find("li:eq(0)").text();
                firstVal = $(this).find("li:eq(0)").attr("val");
                $(this).find("li:eq(0)").addClass("li_bag");
            }else{
                firstText = curr.text();
                firstVal = curr.attr("val");

            }

        	$(this).siblings(".down_list").val(firstText);
        	$(this).siblings(".downList_val").val(firstVal);
        });
        

        $(".downList_con li").click(function(){
            $(this).parent().siblings(".down_list").attr("value",$(this).text());
          	$(this).parent().siblings(".downList_val").val($(this).attr("val"));
            $(this).parent().siblings(".downList_val").change();
            $(this).addClass("li_bag").siblings().removeClass("li_bag");
        });
      
        /*表单验证*/
		$("#inputForm").validate({
			rules:{
				clientName:{
                    remote: {
                    	required: true,
                        url: "checkName.jhtml",
                        data:{
                        	oldClientName:$("#oldClientName").val()
                        },
                        cache: false
                    }
				},
				clientNum:{
					maxlength:20
				},
				areaId:{
					required:true
				},
				email:{
					email:true
				},
				tel:{
					pattern: /^1[3|4|5|7|8]\d{9}$/
				}
			},
			messages:{
				clientName: {
			        remote: "填写供应商已存在"
			    },
				clientNum:{
					maxlength:"最多支持20个字符"
				},
				areaId:{
					required:"供应商地区不能为空"
				},
				email:{
					email:"请输入正确格式的电子邮件"
				},
				tel:{
					pattern:"手机格式不正确"
				}
			}
		});
        
		$("#yes").on("click",function(){
			 if(!$("#inputForm").valid()){
		         return false ;
		     }
			 $("#inputForm").submit();
		});
		
		
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
