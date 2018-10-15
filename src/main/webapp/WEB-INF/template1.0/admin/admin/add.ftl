[#escape x as x?html]
<!DOCTYPE HTML>
<html>
<head>
<meta charset="utf-8">
<meta name="renderer" content="webkit|ie-comp|ie-stand">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
<meta http-equiv="Cache-Control" content="no-siteapp" />

<link rel="stylesheet" type="text/css" href="${base}/resources/admin1.0/H-ui/static/h-ui/css/H-ui.min.css" />
<link rel="stylesheet" type="text/css" href="${base}/resources/admin1.0/H-ui/static/h-ui.admin/css/H-ui.admin.css" />
<link rel="stylesheet" href="${base}/resources/admin1.0/css/public.css" />
<link rel="stylesheet" href="${base}/resources/admin1.0/css/admin.css" />

	<style>
		body{background:#f9f9f9;}
		.pag_div{width:95%;float:left;}
		.tabBar{background:#f9f9f9;border:0;}
		.tabBar span{padding:5px 24px;background:#f9f9f9;color:#c3cfe5;}
		.tabBar span.current{background:#fff;color:#333;}
		.beizhu{height:80px;}
		.col-sm-7{width:80%;}
		/*.juese{max-width:85%;position: relative;}
		.juese #roleIds-error{position:absolute;right:0;width:50px;}*/
		#tab-system{position:relative;}
		.opera_butt{position:absolute;top:0;right:0; margin-top:5px;}
	</style>
<title>${message("admin.admin.add")} - Powered By DreamForYou</title>
</head>
<body>
	<div class="child_page"><!--内容外面的大框-->	
		<div class="cus_nav">
			<ul>
				<li><a id="goHome"	href="../homePage/index.jhtml">${message("admin.breadcrumb.home")}</a></li>
        		<li><a href="list.jhtml">${message("admin.admin.list")}</a></li>
				<li>${message("admin.admin.add")}</li>
			</ul>
		</div>
		<div class="form_box">
			<form id="inputForm" action="save.jhtml" method="post" class="form form-horizontal">
				<div id="tab-system" class="HuiTab">
					<div>
						
						<div class="pag_div">
							<div class="row cl">
								<label class="form-label col-xs-4 col-sm-3">
									<img src="${base}/resources/admin1.0/images/bitian_icon.svg" alt="" />
									${message("Admin.username")}
								</label>
								<div class="formControls col-xs-8 col-sm-7">
									<input type="text" class="input-text radius" name="username" placeholder="6-30位字符，只能由数字和字母组成"  maxlength="20"/>
								</div>
							</div>
                            <div class="row cl">
                                <label class="form-label col-xs-4 col-sm-3">
                                    <img src="${base}/resources/admin1.0/images/bitian_icon.svg" alt="" />
								手机号
                                </label>
                                <div class="formControls col-xs-8 col-sm-7">
                                    <input type="text" class="input-text radius" name="bindPhoneNum" placeholder="11位数字" maxlength="20"/>
                                </div>
                            </div>
							<div class="row cl">
								<label class="form-label col-xs-4 col-sm-3">
									<img src="${base}/resources/admin1.0/images/bitian_icon.svg" alt="" />
									${message("Admin.password")}
								</label>
								<div class="formControls col-xs-8 col-sm-7">
									<input type="password" class="input-text radius" name="password" id="password" placeholder="6-20位，由字母、数字或特殊字符组成" maxlength="20" autocomplete="off"/>
								</div>
							</div>
							<div class="row cl">
								<label class="form-label col-xs-4 col-sm-3">
									<img src="${base}/resources/admin1.0/images/bitian_icon.svg" alt="" />
									${message("admin.admin.rePassword")}
								</label>
								<div class="formControls col-xs-8 col-sm-7">
									<input type="password" class="input-text radius" name="rePassword" placeholder="6-20位，由字母、数字或特殊字符组成" maxlength="20" autocomplete="off"/>
								</div>
							</div>
							<div class="row cl">
								<label class="form-label col-xs-4 col-sm-3">
									<img src="${base}/resources/admin1.0/images/bitian_icon.svg" alt="" />
									${message("Admin.email")}
								</label>
								<div class="formControls col-xs-8 col-sm-7">
									<input type="text" class="input-text radius" name="email" placeholder="电子邮箱" maxlength="200"/>
								</div>
							</div>
                            <div class="row cl">
                                <label class="form-label col-xs-4 col-sm-3">
                                    <img src="${base}/resources/admin1.0/images/bitian_icon.svg" alt="" />
									${message("Admin.department")}
                                </label>
                                <div class="formControls col-xs-8 col-sm-7">
                                    <input type="text" class="input-text radius down_list" readonly placeholder="请选择" />
                                    <input type="text" class="downList_val" name="departmentId" />
                                    <ul class="downList_con">
										[#list departmentTree as department]
                                            <li val="${department.id}">[#if department.grade != 0][#list 1..department.grade as i]&nbsp;&nbsp;[/#list][/#if]${department.name}</li>
										[/#list]
                                    </ul>
                                </div>
                            </div>
                            <div class="row cl">
                                <label class="form-label col-xs-4 col-sm-3">
                                	<img src="${base}/resources/admin1.0/images/bitian_icon.svg" alt="" />
									${message("Admin.name")}
                                </label>
                                <div class="formControls col-xs-8 col-sm-7">

                                    <input type="text" class="input-text radius" name="name" maxlength="200"/>

                                </div>
                            </div>
							<div class="row cl">
								<label class="form-label col-xs-4 col-sm-3">
								<img src="${base}/resources/admin1.0/images/bitian_icon.svg" alt="" />
								${message("Admin.roles")}
								</label>
								<div class="formControls col-xs-8 col-sm-7 juese">
									<span class="fieldSet">
									[#list roles as role]
									<div class="check-box">
											<input type="checkbox" class="input-text radius" name="roleIds" value="${role.id}"/>
											<span style="color:#333">${role.name}</span>
										</div>
									[/#list]
									<label id="roleMsg" style="color:red;font-size: 12px"></label>
									</span>
								</div>
							</div>
							<div class="row cl">
								<label class="form-label col-xs-4 col-sm-3">${message("admin.common.setting")}</label>
								<div class="formControls col-xs-8 col-sm-7">
									<div class="check-box">
										<input type="checkbox" class="input-text radius" name="isEnabled" value="true" checked="checked"/>
										<span style="color:#333">${message("Admin.isEnabled")}</span>
										<input type="hidden" class="input-text radius" name="_isEnabled" value="false"/>
									</div>
								</div>
							</div>
						</div>
						
					</div>

				</div>
				
				
				<div class="footer_submit">
					<input class="btn radius confir_S" type="button" value="${message("admin.common.submit")}" onclick="return submitForm();"/>
					<input class="btn radius cancel_B" type="button" value="${message("admin.common.back")}" onclick="history.back(); return false;"/>
				</div>
			</form>
		</div>
	</div>
<!--_footer 作为公共模版分离出去-->
<script src="${base}/resources/admin1.0/js/jquery.min.js"></script>
<script type="text/javascript" src="${base}/resources/admin1.0/H-ui/lib/layer/2.4/layer.js"></script>
<script type="text/javascript" src="${base}/resources/admin1.0/H-ui/static/h-ui/js/H-ui.min.js"></script>
<script type="text/javascript" src="${base}/resources/admin1.0/H-ui/static/h-ui.admin/js/H-ui.admin.js"></script>
<script type="text/javascript" src="${base}/resources/admin1.0/js/validate/jquery.validate.min.js"></script>
<script type="text/javascript" src="${base}/resources/admin1.0/js/validate/messages_zh.min.js"></script>
<script type="text/javascript" src="${base}/resources/admin1.0/js/common.js"></script>
<script type="text/javascript">
	$(function(){
		
		var $inputForm = $("#inputForm");
		
		[@flash_message /]
		
		/*通过js获取页面高度，来定义表单的高度*/
		var formHeight=$(document.body).height()-100;
		$(".form_box").css("height",formHeight);
		
		/**/
		var heightObj = $(document.body).height() - 185;
		$(".list_t_tbody").css("height",heightObj);
		


        //下拉列表
        $(".down_list").click(function(){
            $(this).siblings(".downList_con").toggle();
        });

        $("*").click(function (event) {
            if (!$(this).hasClass("down_list")&&!$(this).hasClass("downList_con")){
                $(".downList_con").hide();
            }
            event.stopPropagation();
        });


        $(".downList_con li").click(function(){
            $(this).parent().siblings(".down_list").attr("value",$(this).text());
            $(this).parent().siblings(".downList_val").val($(this).attr("val"));
            $(this).addClass("li_bag").siblings().removeClass("li_bag");
        });

		// 表单验证
		$inputForm.validate({
			rules: {
				username: {
					required: true,
					pattern: /^(?![0-9]+$)[0-9a-zA-Z_]+$/,
					minlength: 6,
					maxlength: 30,
					remote: {
						url: "check_username.jhtml",
						cache: false
					}
				},
				password: {
					required: true,
                    pattern:/^(?![0-9]+$)/,
                    minlength: 6,
                    maxlength: 20
				},
				rePassword: {
					required: true,
					equalTo: "#password"
				},
				email: {
					required: true,
					email: true
				},
				roleIds: "required" ,
				name: {
					required: true,
					maxlength: 20,
					onkeyup: function(element, event) {
		                //去除左侧空格
		                var value = $(element).val().replace(/^\s+/g, "");
		                $(element).val(value);
            		}
				},
                bindPhoneNum:{
				    required : true ,
                    pattern: /^1[0-9]{10}$/,
                    remote: {
                        url: "telIsBind.jhtml"
                    }
				},
                departmentId:"required"
			},
			messages: {
				username: {
					pattern: "${message("admin.validate.illegal")}",
					remote: "${message("admin.validate.exist")}"
				},
				bindPhoneNum : {
				    remote : "手机号已存在"
				}
			}
		});
		
		
		$("#goHome").on("click",function(){
			var nav = window.top.$(".index_nav_one");
    			nav.find("li li").removeClass('clickTo');
				nav.find("i").removeClass('click_border');
		})
		
	});
    function submitForm() {
        if($("input[name=roleIds]").length <=0 ){
            $("#roleMsg").html("请先添加角色！");
            return false;
        }else {
            $("#inputForm").submit();
        }
    }
</script>
</body>
</html>
[/#escape]