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
        .pag_div{
        	width: 45%;
            float: left;
        }
        label.error{left:300px;}
        #address+label.error{left:300px;top:50px;}
    </style>
</head>
<body >
<div class="child_page"><!--内容外面的大框-->
    <div class="cus_nav">
        <ul>
            <li><a id="goHome" href="../homePage/index.jhtml">${message("admin.breadcrumb.home")}</a></li>
            <li><a href="list.jhtml">代理申请列表</a></li>
            <li>代理申请审核</li>
        </ul>
    </div>
    <div class="form_box" style="overflow: auto;">
        <form id="inputForm" action="detailCheck.jhtml" method="post" class="form form-horizontal">
            <input type="hidden" name="id" value="${proxyCheck.id}" />
            <div class="pag_div">
                <h3 class="form_title">申请信息</h3>
                <div class="row cl">
                    <label class="form-label col-xs-4 col-sm-3">
					姓名:
                    </label>
                    <div class="formControls col-xs-8 col-sm-7">
                        <input type="text" class="input-text radius" name="name" maxlength="200" value="${proxyCheck.name}" readonly>
                    </div>
                </div>
                <div class="row cl">
                    <label class="form-label col-xs-4 col-sm-3">
					联系方式：
                    </label>
                    <div class="formControls col-xs-8 col-sm-7">
                        <input type="text" class="input-text radius" name="tel" maxlength="9" value="${proxyCheck.tel}" readonly>
                    </div>
                </div>
                <div class="row cl">
                    <label class="form-label col-xs-4 col-sm-3">
					联系地址：
                    </label>
                    <div class="formControls col-xs-8 col-sm-7">
                        <input type="text" class="input-text radius" name="address" maxlength="9" value="${proxyCheck.address}" readonly/>
                    </div>
                </div>
                <div class="row cl">
                    <label class="form-label col-xs-4 col-sm-3">
                    性别:
                    </label>
                    <div class="formControls col-xs-8 col-sm-7">
                    	<input type="text" class="input-text radius" name="gender" maxlength="9" value="${proxyCheck.gender.getName()}" readonly/>
                    </div>
                </div>
                <div class="row cl">
                    <label class="form-label col-xs-4 col-sm-3">
                    微信号:
                    </label>
                    <div class="formControls col-xs-8 col-sm-7">
                    	<input type="text" class="input-text radius" name="webchat" maxlength="9" value="${proxyCheck.webchat}" readonly/>
                    </div>
                </div>
                <div class="row cl">
                    <label class="form-label col-xs-4 col-sm-3">
                    省市区:
                    </label>
                    <div class="formControls col-xs-8 col-sm-7">
                    	<input type="text" class="input-text radius" name="area" maxlength="9" value="${proxyCheck.area.fullName}" readonly/>
                    </div>
                </div>
                <div class="row cl">
                    <label class="form-label col-xs-4 col-sm-3">
                    意向等级:
                    </label>
                    <div class="formControls col-xs-8 col-sm-7">
                    	<input type="text" class="input-text radius" name="level" maxlength="9" value="${proxyCheck.level.getName()}" readonly/>
                    </div>
                </div>
                
                <div class="row cl">
                    <label class="form-label col-xs-4 col-sm-3">
                    成为谁的下级:
                    </label>
                    <div class="formControls col-xs-8 col-sm-7">
                    	<input type="text" class="input-text radius" name="parentProxyUser" maxlength="9" value="${proxyCheck.parentProxyUser.name}" readonly/>
                    </div>
                </div>


            </div>
            
            <div class="pag_div">
                <h3 class="form_title">详细信息</h3>
                <div class="row cl">
                    <label class="form-label col-xs-4 col-sm-3">
                    从事微商时间:
                    </label>
                    <div class="formControls col-xs-8 col-sm-7">
                    	<input type="text" class="input-text radius" name="workTime" maxlength="9" value="${proxyCheck.workTime}" readonly/>
                    </div>
                </div>
                <div class="row cl">
                    <label class="form-label col-xs-4 col-sm-3">
                    月销售额:
                    </label>
                    <div class="formControls col-xs-8 col-sm-7">
                    	<input type="text" class="input-text radius" name="monthMoney" maxlength="9" value="${proxyCheck.monthMoney}" readonly/>
                    </div>
                </div>
                <div class="row cl">
                    <label class="form-label col-xs-8 col-sm-7">
                    目前经营的品牌品类:
                    </label>
                    <div class="formControls col-xs-8 col-sm-7">
                    	<input type="text" class="input-text radius" name="nowManageCategory" maxlength="9" value="${proxyCheck.nowManageCategory}" readonly/>
                    </div>
                </div>
                <div class="row cl">
                    <label class="form-label col-xs-4 col-sm-3">
                    了解途径:
                    </label>
                    <div class="formControls col-xs-8 col-sm-7">
                    	<input type="text" class="input-text radius" name="sourceType" maxlength="9" value="${proxyCheck.sourceType}" readonly/>
                    </div>
                </div>
                
                <div class="row cl">
                    <label class="form-label col-xs-4 col-sm-3">
					申请理由：
                    </label>
                    <div class="formControls col-xs-8 col-sm-7">
                        <!-- <textarea id="description" name="description" class="editor" style="width: 100%;"></textarea> -->
                        <textarea rows="5" class="text_area" cols="50" rows="5" name="reason" placeholder="" readonly>${proxyCheck.reason}</textarea>
                    </div>
                </div>

            </div>
            
            <div class="footer_submit">
            	[#if proxyCheck.proxyCheckStatus == 'wait'] 
                	<input class="btn radius confir_S" type="submit" value="审核" />
                [#else] 
                	<input class="btn radius confir_S" type="submit" value="已审核" disabled/>
                [/#if]
                <input class="btn radius cancel_B" type="button" value="返回" onclick="history.back(); return false;" />
            </div>
        </form>
    </div>
</div>
<script src="${base}/resources/admin1.0/js/jquery.min.js"></script>
<script type="text/javascript" src="${base}/resources/admin1.0/H-ui/static/h-ui/js/H-ui.min.js"></script>
<script type="text/javascript" src="${base}/resources/admin1.0/H-ui/static/h-ui.admin/js/H-ui.admin.js"></script>
<script type="text/javascript" src="${base}/resources/admin1.0/js/validate/jquery.validate.js"></script>
<script type="text/javascript" src="${base}/resources/admin1.0/js/validate/jquery.validate.min.js"></script>
<script type="text/javascript" src="${base}/resources/admin1.0/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/admin1.0/js/input.js"></script>
<script type="text/javascript">

    $(function(){

        var $inputForm = $("#inputForm");

		[@flash_message /]

      

        var formHeight=$(document.body).height()-100;
        $(".form_box").css("height",formHeight);
		
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