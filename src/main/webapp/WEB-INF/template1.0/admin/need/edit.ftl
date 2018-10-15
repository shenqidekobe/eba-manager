[#escape x as x?html]
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>${message("admin.admin.add")} - Powered By DreamForYou</title>
	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
	<link rel="stylesheet" type="text/css" href="${base}/resources/admin1.0/H-ui/static/h-ui/css/H-ui.min.css" />
	<link rel="stylesheet" type="text/css" href="${base}/resources/admin1.0/H-ui/static/h-ui.admin/css/H-ui.admin.css" />
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
		.footer_submit{
			width:100%;
		}
	</style>
</head>
<body >
<form id="inputForm" action="update.jhtml" method="post" class="form form-horizontal">
<input type="hidden" name="oldTel" id="oldTel" value="${oldTel}"/>
<input type="hidden" name="id" id="id" value="${need.id}" />
<input type="hidden" name="shopType" id="shopType" value="${need.shopType}">


	<div class="child_page"><!--内容外面的大框-->
		<div class="cus_nav">
			<ul>
				<li><a id="goHome"	href="../homePage/index.jhtml">${message("admin.breadcrumb.home")}</a></li>
				<li><a href="list.jhtml">${message("admin.need.list")}</a></li>
				<li>编辑门店</li>
			</ul>
		</div>
		<div class="form_box">

			<div class='form_baseInfo'>
				<h3 class="form_title" style="margin:20px 0 0 20px;">基本信息</h3>
				<div class="pag_div">
					<div class="row cl">
						<label class="form-label col-xs-4 col-sm-3">
							<img src="${base}/resources/admin1.0/images/bitian_icon.svg" alt="" />${message("Need.clientName")}</label>
						<div class="formControls col-xs-8 col-sm-7">
                                <span class="fieldSet">
                                    <input type="text" class="input-text radius" id="name" name="name" value="${need.name}" />
                                </span>
						</div>
					</div>
					<div class="row cl">
						<label class="form-label col-xs-4 col-sm-3">
							<img src="${base}/resources/admin1.0/images/bitian_icon.svg" alt="" />${message("Need.userName")}</label>
						<div class="formControls col-xs-8 col-sm-7">
                                <span class="fieldSet">
                                    <input type="text" id="userName" name="userName" class="input-text radius" value="${need.userName}"/>
                                </span>
						</div>
					</div>
					<div class="row cl">
						<label class="form-label col-xs-4 col-sm-3">
							<img src="${base}/resources/admin1.0/images/bitian_icon.svg" alt="" />${message("Need.area")}</label>
						<div class="formControls col-xs-8 col-sm-7">
                                <span class="fieldSet">
                                    <input type="hidden" id="areaId" name="areaId" value="${need.area.id}" treePath="${need.area.treePath}" />
                                </span>
						</div>
					</div>
					
				</div>
				<div class="pag_div">
					<div class="row cl">
						<label class="form-label col-xs-4 col-sm-3">${message("Need.clientNum")}</label>
						<div class="formControls col-xs-8 col-sm-7">
							<input type="text" id="clientNum" name="clientNum" class="input-text radius" value="${need.clientNum}"/>
						</div>
					</div>
					[#if need.shopType == 'direct']
					<div class="row cl receiverCl">
						<input type="hidden" name="tel" value="${need.tel}" />
                        <label class="form-label col-xs-4 col-sm-3">
                            <img src="${base}/resources/admin1.0/images/bitian_icon.svg" alt="" />联系方式</label>
                        <div class="formControls col-xs-8 col-sm-7">
                                <span class="fieldSet">
                                    <input type="text" class="input-text radius" id="receiverTel" name="receiverTel" placeholder="收货人手机号" value="${need.receiverTel}" />
                                </span>
                        </div>
                    </div>
                    [#else]
                    <div class="row cl">
						<label class="form-label col-xs-4 col-sm-3">
							<img src="${base}/resources/admin1.0/images/bitian_icon.svg" alt="" />联系方式</label>
						<div class="formControls col-xs-8 col-sm-7">
                            <input type="text" class="input-text radius" name="tel" placeholder="小程序登陆账号" id="acount" value="${need.tel}"/>
						</div>
					</div>
                    [/#if]
					<div class="row cl">
						<label class="form-label col-xs-4 col-sm-3">
							<img src="${base}/resources/admin1.0/images/bitian_icon.svg" alt="" />${message("Need.address")}</label>
						<div class="formControls col-xs-8 col-sm-7">
                                <span class="fieldSet">
                                    <input type="text" id="address" name="address" class="input-text radius" value="${need.address}"/>
                                </span>
						</div>
					</div>
				</div>
				<h3 class="form_title" style="margin:20px 0 0 20px;">联系人信息</h3>
				<div class="pag_div">
					<div class="row cl">
						<label class="form-label col-xs-4 col-sm-3">${message("Need.contacts")}</label>
						<div class="formControls col-xs-8 col-sm-7">
							<input type="text" id="contacts" name="contacts" class="input-text radius" value="${need.contacts}"/>
						</div>
					</div>
					<div class="row cl">
						<label class="form-label col-xs-4 col-sm-3">${message("Need.email")}</label>
						<div class="formControls col-xs-8 col-sm-7">
							<input type="text" id="email" name="email" class="input-text radius" value="${need.email}"/>
						</div>
					</div>
				</div>
				<div class="pag_div">
					<div class="row cl">
						<label class="form-label col-xs-4 col-sm-3">${message("Need.contactsTel")}</label>
						<div class="formControls col-xs-8 col-sm-7">
							<input type="text" id="contactsTel" name="contactsTel" class="input-text radius" value="${need.contactsTel}"/>
						</div>
					</div>
				</div>
				<h3 class="form_title" style="margin:20px 0 0 20px;">财务信息</h3>
				<div class="pag_divs">
					<div class="pag_div">
						<div class="row cl">
							<label class="form-label col-xs-4 col-sm-3">${message("Need.accountName")}</label>
							<div class="formControls col-xs-8 col-sm-7">
								<input type="text" id="accountName" name="accountName" class="input-text radius" value="${need.accountName}"/>
							</div>
						</div>
						<div class="row cl">
							<label class="form-label col-xs-4 col-sm-3">${message("Need.bank")}</label>
							<div class="formControls col-xs-8 col-sm-7">
								<input type="text" id="bank" name="bank" class="input-text radius" value="${need.bank}"/>
							</div>
						</div>
						<div class="row cl">
							<label class="form-label col-xs-4 col-sm-3">${message("Need.identifier")}</label>
							<div class="formControls col-xs-8 col-sm-7">
								<input type="text" id="identifier" name="identifier" class="input-text radius" value="${need.identifier}"/>
							</div>
						</div>
					</div>
					<div class="pag_div">
						<div class="row cl">
							<label class="form-label col-xs-4 col-sm-3">${message("Need.invoice")}</label>
							<div class="formControls col-xs-8 col-sm-7">
								<input type="text" id="invoice" name="invoice" class="input-text radius" value="${need.invoice}"/>
							</div>
						</div>
						<div class="row cl">
							<label class="form-label col-xs-4 col-sm-3">${message("Need.bankAccountNum")}</label>
							<div class="formControls col-xs-8 col-sm-7">
								<input type="text" id="bankAccountNum" name="bankAccountNum" class="input-text radius" value="${need.bankAccountNum}"/>
							</div>
						</div>
					</div>
				</div>
				<div class="form_title" style="margin:20px 0 0 20px;width:110px;">
					所属业务员
					<span class="explainSpan" data="请先选择部门，再选择该部门所属业务员；若无部门／业务员可选，请先到系统部门／员工，添加部门员工信息"></span>
				</div>
				<div class="pag_divs2" [#if need.shopType=='direct']style="padding:0;"[/#if] >
					<div class="pag_div">
						<div class="row cl">
							<label class="form-label col-xs-4 col-sm-3">部门</label>
							<div class="formControls col-xs-8 col-sm-7">
								<input type="text" class="input-text radius down_list" readonly placeholder="[#if departmentTree.size() == 0]暂无部门信息[#else]请选择[/#if]">
								<input type="text" class="downList_val" name="departmentId" id="departmentId" value="${need.admin.department.id}" />
                                <ul class="downList_con" id="departmentUl">
                                        [#list departmentTree as department]
                                            <li val="${department.id}" [#if department.id == need.admin.department.id] class="li_bag"[/#if]>[#if department.grade != 0][#list 1..department.grade as i]&nbsp;&nbsp;[/#list][/#if]${department.name}</li>
                                        [/#list]
                                </ul>
							</div>
						</div>

					</div>
					<div class="pag_div">
						<div class="row cl">
							<label class="form-label col-xs-4 col-sm-3">业务员</label>
							<div class="formControls col-xs-8 col-sm-7 adminDiv">
								<input type="text" id="prompt" class="input-text radius down_list" readonly placeholder="请先选择部门">
								<input type="text" name="adminId" value="${need.admin.id}" class="downList_val" />
								<ul class="downList_con" id="adminUl">
								</ul>
							</div>
						</div>
					</div>
				</div>
				[#if need.shopType == 'direct']
				<div id="trusteeship-div">
                    <div class="form_title" style="margin:20px 0 0 20px;width:110px;">
                        托管店员
                    </div>
                    
                    <div class="pag_divs2">
                    	[#if hostingShops.size()>0]
                    		<input type="hidden" name="hostShop" id="hostShop" value="1"/>
                    	[#list hostingShops as hos]
                    		<div class="manyAssustant">
		                        <div class="pag_div">
		                            <div class="row cl">
		                                <label class="form-label col-xs-4 col-sm-3">店员姓名</label>
		                                <div class="formControls col-xs-8 col-sm-7">
		                                    <input type="text" id="shopAssistantName_${hos_index}" data-index="${hos_index}" name="shopAssistantDtoList[${hos_index}].shopAssistantName" value="${hos.memberMember.name}" class="input-text radius assistantNameValid" />
		                                </div>
		                            </div>
		
		                        </div>
		                        <div class="pag_div">
		                            <div class="row cl">
		                                <label class="form-label col-xs-4 col-sm-3">店员手机号</label>
		                                <div class="formControls col-xs-8 col-sm-7">
		                                    <input type="text" id="shopAssistantTel_${hos_index}" data-index="${hos_index}" name="shopAssistantDtoList[${hos_index}].shopAssistantTel" value="${hos.memberMember.byMember.username}" class="input-text radius assistantNameValid" />
		                                </div>
		                            </div>
		                        </div>
	                        </div>
	                    [/#list]
                       [#else]
                       		<div class="pag_div">
                       		<input type="hidden" name="hostShop" id="hostShop" value="0"/>
                            <div class="row cl">
                                <label class="form-label col-xs-4 col-sm-3">店员姓名</label>
                                <div class="formControls col-xs-8 col-sm-7">
                                    <input type="text" id="shopAssistantName" name="shopAssistantName" id="shopAssistantName" class="input-text radius" />
                                </div>
                            </div>

                        </div>
                        <div class="pag_div">
                            <div class="row cl">
                                <label class="form-label col-xs-4 col-sm-3">店员手机号</label>
                                <div class="formControls col-xs-8 col-sm-7">
                                    <input type="text" id="shopAssistantTel" name="shopAssistantTel" class="input-text radius" />
                                    <input type="checkbox" class="input-text radius" id="shopAssistantChk" />&nbsp;<span id="input-spans">无手机号</span>
                                </div>
                            </div>
                        </div>
                       [/#if]
                    </div>
                    
                </div>
                [/#if]
                
               
               
                
				
			</div>

		</div>
	</div>
	<div class="footer_submit">
		<input class="btn radius confir_S" type="button" id="yes" value="${message("admin.common.submit")}">
		<input class="btn radius cancel_B" type="button" value="${message("admin.common.back")}" onclick="history.back();return false;">
	</div>
</form>
<script src="${base}/resources/admin1.0/js/jquery.min.js"></script>

<script type="text/javascript" src="${base}/resources/admin1.0/js/validate/jquery.validate.js"></script>
<script type="text/javascript" src="${base}/resources/admin1.0/js/validate/jquery.validate.min.js"></script>
<script type="text/javascript" src="${base}/resources/admin1.0/js/jquery.lSelect.js"></script>
<script type="text/javascript" src="${base}/resources/admin1.0/js/webuploader.js"></script>
<script type="text/javascript" src="${base}/resources/admin1.0/ueditor/ueditor.js"></script>
<script type="text/javascript" src="${base}/resources/admin1.0/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/admin1.0/js/input.js"></script>
<script type="text/javascript">

    $(function(){

        var $inputForm = $("#inputForm");
        var $admin = $("#adminUl");
        var adminId="${need.admin.id}";
        var $adminDiv = $(".adminDiv");
        var departmentId="";

        [@flash_message /]

        $("#description").editor();
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

        function loadAdmin(){
        	departmentId=$("#departmentId").val();
            $adminDiv.find("input").val('');
            $.get("${base}/admin/admin/getListByDepartment.jhtml",{"departmentId":departmentId},function(o){
                var data=o.data;
                var lis="";
                if (departmentId == '') {
                    $("#prompt").attr("placeholder","请先选择部门");
                }else if (data.length==0) {
                    $("#prompt").attr("placeholder","暂无业务员信息");
                }else if (data.length>0) {
                    $("#prompt").attr("placeholder","请选择");
                }
                for (var i = 0; i < data.length; i++) {
                    if (adminId == data[i].id) {
                        lis+="<li val='"+data[i].id+"' class='li_bag'> "+data[i].name+"</li>";
                    }else{
                        lis+="<li val='"+data[i].id+"'> "+data[i].name+"</li>";
                    }
                }
                $admin.html(lis);
                select();
            });
        }
        //加载业务员
        loadAdmin();
        //默认选择
        select();
        function select(){
            $(".downList_con").each(function(){
                var curr = $(this).find("li.li_bag");
                var firstText ;
                var firstVal ;
                firstText = curr.text();
                firstVal = curr.attr("val");
                $(this).siblings(".down_list").val(firstText);
                $(this).siblings(".downList_val").val(firstVal);
            });
        }
        
        

        $(".downList_con li").click(function(){
            $(this).parent().siblings(".down_list").attr("value",$(this).text());
          	$(this).parent().siblings(".downList_val").val($(this).attr("val"));
            $(this).parent().siblings(".downList_val").change();
            $(this).addClass("li_bag").siblings().removeClass("li_bag");
        });

        $("#departmentUl li").click(function(){
        	if (departmentId == $(this).attr("val")) {
                return;
            }
            loadAdmin();
        });

        $(".downList_con").delegate("li","click",function(){
			$(this).parent().siblings(".down_list").attr("value",$(this).text());
            $(this).parent().siblings(".downList_val").val($(this).attr("val"));

            $(this).parent().siblings(".downList_val").change();

            $(this).addClass("li_bag").siblings().removeClass("li_bag");
            $(this).parent().hide();
        });

        // 表单验证
        $inputForm.validate({
                rules: {
                    name: {
                        required: true
                    },
                    userName: {
                        required: true
                    },
                    areaId: {
                        required: true
                    },
                    address:{
                        required:true
                    },
                    tel: {
                        required: true,
                        pattern: /^[1][3|4|5|7|8]\d{9}|[2]\d\d{9}$/,
                        remote: {
                            url: "checkTel.jhtml",
                            data:{
                            	oldTel:$("#oldTel").val()
                            },
                            cache: false
                        }

                    },
                },
                messages: {
                    username: {
                    pattern: "${message("admin.validate.illegal")}",
                    remote: "${message("admin.validate.exist")}"
	            },
		        address:{
		            required:"必填"
		        },
		        tel: {
		            remote: "填写号码已存在"
		        },
		        
	
	    }
    });

        var formHeight=$(document.body).height()-100;
        $(".form_box").css("height",formHeight);

        /*引入复文本框*/
        $("#description").editor();

        $("#yes").on("click",function(){
        	var shopType = $("#shopType").val();
        	var hostShop = $("#hostShop").val();
        	if(shopType == 'direct' && hostShop == '0') {
        	 var tel = $("#shopAssistantTel").val().trim();
   			 var name = $("#shopAssistantName").val().trim();
   			 var chk = $("#shopAssistantChk").prop("checked");
   			 if(chk && name.length == 0) {
   				 validateName();
   			 }else if(name.length > 0 && !chk && tel.length == 0) {
   				 validateATel();
   			 }else if(name.length == 0 && !chk && tel.length > 0) {
   				 validateName();
   				 validateATel();
   			 }else if(name.length > 0 && !chk && tel.length > 0) {
   				 validateATel();
   			 }else if(name.length == 0 && !chk && tel.length == 0) {
   				 deleteValidateName();
   				 deleteValidateTel();
   			 }
          }
			 
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
        
        $("#shopAssistantChk").on("click",function(){
        	var shopAName = $("#shopAssistantName").val().trim();
        	var shopATel = $("#shopAssistantTel").val().trim();
        	var th = $(this);
        	var chk = th.prop("checked");
        	if(chk) {
        		validateAssistantTel();
        		if(shopAName.length == 0) {
        			validateName();
        		}
        	}else {
        		if(shopAName.length > 0) {
        			validateATel();
        		}else {
        			deleteValidateName();
        		}
        		$("#shopAssistantTel").attr("disabled",false);
            	$("#shopAssistantTel").removeClass("inputDisabled");
        	}
        });
        
        
        $("div.manyAssustant input").blur(function(){
        	var id = $(this).data("index");
        	var inputTel = $("#shopAssistantTel_"+id);
        	var inputName = $("#shopAssistantName_"+id);
        	if(inputName.val().length > 0 || inputTel.val().length > 0) {
        		inputName.rules("remove");
        		inputName.rules("add",
            			{
                            required: true
                        }
            	);
        		inputName.attr("disabled",false);
        		inputName.removeClass("inputDisabled");
            	
            	inputTel.rules("remove");
            	inputTel.rules("add",
            			{
                            required: true,
                            pattern: /^\d{11}$/
                        }
            	);
            	inputTel.attr("disabled",false);
            	inputTel.removeClass("inputDisabled");
        	}else {
        		inputName.rules("remove");
        		inputName.removeClass("error");
                $("#shopAssistantName_"+id+"-error").hide();
                
                inputTel.rules("remove");
                inputTel.removeClass("error");
                $("#shopAssistantTel_"+id+"-error").hide();
        	}
        	
        	
        }); 
        
        var shopType = $("#shopType").val();
        if(shopType == 'direct') {
        	$("#receiverTel").rules("remove");
        	$("#receiverTel").rules("add",
        			{
                        required: true
                    }
        	);
        	$("#receiverTel").attr("disabled",false);
        	$("#receiverTel").removeClass("inputDisabled");
        }

    });
    
    function validateAssistantTel(){
    	$("#shopAssistantTel").rules("remove");
    	$("#shopAssistantTel").addClass("inputDisabled").attr("disabled",true).removeClass("error");
        $("#shopAssistantTel-error").hide();
        validateName();
    }
    
    function validateATel(){
    	$("#shopAssistantTel").rules("remove");
    	$("#shopAssistantTel").rules("add",
    			{
                    required: true,
                    pattern: /^1[3|4|5|7|8]\d{9}$/,
                }
    	);
    	$("#shopAssistantTel").attr("disabled",false);
    	$("#shopAssistantTel").removeClass("inputDisabled");

    }
    
    //给店员名称添加验证
    function validateName(){
    	$("#shopAssistantName").rules("remove");
    	$("#shopAssistantName").rules("add",
    			{
                    required: true
                }
    	);
    	$("#shopAssistantName").attr("disabled",false);
    	$("#shopAssistantName").removeClass("inputDisabled");
    }
    
    function deleteValidateName() {
    	$("#shopAssistantName").rules("remove");
    	$("#shopAssistantName").removeClass("error");
        $("#shopAssistantName-error").hide();
    }
    
    function deleteValidateTel() {
    	$("#shopAssistantTel").rules("remove");
    	$("#shopAssistantTel").removeClass("error");
        $("#shopAssistantTel-error").hide();
    }

</script>

</body>
</html>
[/#escape]