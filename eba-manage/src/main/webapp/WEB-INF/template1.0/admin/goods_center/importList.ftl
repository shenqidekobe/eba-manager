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
			.pag_div{width:calc(100% - 30px);}
			label.error{left:300px;}
			#address+label.error{left:300px;top:50px;}
			th{background:#f9f9f9;}
			.pagination{
				margin-top:0;
				margin-bottom:20px;
			}
			.dropdownMenu{
				margin-right:30px;
			}
			.dropdownMenu a{
				margin-top:0;
				margin-bottom:20px;
				background: #fff url(../../../../resources/admin1.0/images/caidanjtx_icon.svg) no-repeat 60px center;
			}
			.dropdownMenu a:hover{
				background: #fff url(../../../../resources/admin1.0/images/caidanjtx_icon.svg) no-repeat 60px center;
			}
			.dropdownMenu ul{
				top:-100px;
			}
			.table_width  td.I_color{color:red;}
		</style>
	</head>
	<body >
		<div class="child_page"><!--内容外面的大框-->	
			<div class="cus_nav">
				<ul>
					<li><a id="goHome"	href="../homePage/index.jhtml">${message("admin.breadcrumb.home")}</a></li>
					<li><a href="list.jhtml">商品列表</a></li>
					<li>导入</li>
				</ul>
			</div>
			<div class="form_box" style="overflow: auto;">
				<form id="saveMoreForm" action="saveMore.jhtml" method="post">
                    <input type="hidden" name="logId" value="${goodsImportLog.id}">
				</form>


				<form id="listForm" action="importList.jhtml" method="get" class="form form-horizontal">
					<input type="hidden" name="logId" value="${goodsImportLog.id}">
					<div class="imprtStep">
						<div class="stratImport">
							<span></span>
							<i></i>
							<i></i>
							<i></i>
							<i></i>
							<i></i>
						</div>
						<div class="seeImport seeImporting">
							<i></i>
							<i></i>
							<i></i>
							<i></i>
							<i></i>
							<span></span>
							<i></i>
							<i></i>
							<i></i>
							<i></i>
							<i></i>
						</div>
						<div class="completeImport">
							<i></i>
							<i></i>
							<i></i>
							<i></i>
							<i></i>
							<span></span>
						</div>
					</div>
					
					<div class="importTable">
						<p class="I_result">导入情况：本次共导入<span class="I_color">${goodsImportLog.total}</span>条商品数据，<span class="I_color">${goodsImportLog.successNum}</span>条正确，<span class="I_color">${goodsImportLog.errorNum}</span>条错误，只能够导入数据格式正确的商品信息</p>
						<div class="" id="">
							<table id="productTable" class="table table-border table-hover table_width boo">
				                <tr class="text-c">
				                    <th width="">验证</th>
				                    <th width="">商品编号</th>
				                    <th width="">商品名称</th>
				                    <th width="">一级分类</th>
				                    <th width="">错误信息</th>
				                </tr>
								[#list page.content as info]
				                <tr class="text-c">
				                    [#if info.valid]
										<td>正确</td>
				                        [#else]<td class="I_color">错误</td>
				                    [/#if]
				                    <td>${info.sn}</td>
				                    <td>${info.name}</td>
				                    <td>${info.oneProductCategoryImport.name }</td>
                                    <td>
										[#list info.errors as error]
											[#if error_has_next]
												${error.errorInfo}、
												[#else]${error.errorInfo}
											[/#if]

										[/#list]
									</td>
				                </tr>
								[/#list]
				            </table>
						</div>
					</div>

					[@pagination pageNumber = page.pageNumber totalPages = page.totalPages]
						[#include "/admin/include/pagination.ftl"]
					[/@pagination]

					<div class="footer_submit">
						<input class="btn radius confir_S" type="button" onclick="javascript:saveMore();" value="下一步" />
						<input class="btn radius cancel_B" type="button" value="${message("admin.common.back")}" onclick="javascript:window.location.href='list.jhtml'" />
					</div>
				</form>
			</div>	
		</div>
		<script src="${base}/resources/admin1.0/js/jquery.min.js"></script>
		<script type="text/javascript" src="${base}/resources/admin1.0/H-ui/static/h-ui/js/H-ui.min.js"></script> 
		<script type="text/javascript" src="${base}/resources/admin1.0/H-ui/static/h-ui.admin/js/H-ui.admin.js"></script>
		<script type="text/javascript" src="${base}/resources/admin1.0/js/validate/jquery.validate.js"></script>
		<script type="text/javascript" src="${base}/resources/admin1.0/js/validate/jquery.validate.min.js"></script>
		<script type="text/javascript" src="${base}/resources/admin1.0/js/jquery.lSelect.js"></script>
		<script type="text/javascript" src="${base}/resources/admin1.0/js/webuploader.js"></script>
		<script type="text/javascript" src="${base}/resources/admin1.0/ueditor/ueditor.js"></script>
		<script type="text/javascript" src="${base}/resources/admin1.0/js/common.js"></script>
		<script type="text/javascript" src="${base}/resources/admin1.0/js/input.js"></script>
        <script type="text/javascript" src="${base}/resources/admin1.0/js/list.js"></script>
		<script type="text/javascript">
			function saveMore() {
				$(".confir_S").addClass("in_no_click");
		        $(".confir_S").attr("disabled", true);
				$("#saveMoreForm").submit();
            }

			
			$(function(){
						
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