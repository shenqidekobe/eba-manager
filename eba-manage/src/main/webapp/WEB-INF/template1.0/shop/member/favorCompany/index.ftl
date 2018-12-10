[#escape x as x?html]
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge" />
		<title>微信小程序</title>
		<link rel="stylesheet" href="${base}/resources/shop/common/config/bootstrap/css/bootstrap.min.css" />
		<link rel="stylesheet" href="${base}/resources/shop/common/css/public.css" />
		<link rel="stylesheet" href="${base}/resources/shop/common/css/index.css" />
		<link rel="stylesheet" href="${base}/resources/shop/common/css/member.css" />
		<style>
			.table_div{background:#fff;border:1px solid #eee;margin-top:10px;}
			.xxDialog .dialogwarnIcon{
				margin:0;
				padding:60px 10px;
				text-align: left;
				font-size:16px;
			}
			.xxDialog .dialogBottom{
				border:0;
				height:50px;
				padding-left:10px;
				text-align: left;
			}
			.table>tbody>tr>td,.table>tbody>tr>th{padding:12px 8px;}
			.td_operate{
				top:5px;
				padding-top:5px;
				background: url(${base}/resources/shop/common/images/caidanjt-a.svg) no-repeat 75% 8px;
				background-size:14px;
			}
			.operate_border{
				background:#fff url(${base}/resources/shop/common/images/caidanjt-b.svg) no-repeat 75% 8px;
				background-size:14px;
			}
		</style>
	</head>
	<body class="reg10">
		[#include "/shop/common/head.ftl"]
		<div class="page_con">
			<div class="con_center">
				[#include "/shop/member/inc.ftl"]
				<div class="content">
					<form id="listForm" action="index.jhtml" method="get">
					<div class="conList">
						<div class="table_div">
							<table class="table table-hover textLeft">
	                            <thead>
	                                <tr>
	                                    <th width="25%">企业名称</th>
	                                    <th width="18%">认证状态</th>
	                                    <th width="15%">联系人</th>
	                                    <th width="17%">联系电话</th>
	                                    <th width="15%">入驻时间</th>
	                                    <th width="10%" style='text-align: center;'>操作</th>
	                                </tr>
	                            </thead>
	                       
                                <tbody>
                                	[#list page.content as supplier]
                                    <tr>
                                        <td>${supplier.name}</td>
                                        <td>
                                        	[#if supplier.status == 'notCertified' ]
												<span>企业未认证</span>
											[/#if]
											[#if supplier.status == 'authenticationFailed' ]
											 	<span>企业未认证</span>
											[/#if]
											[#if supplier.status == 'certification' ]
											 	<span>企业未认证</span>
											[/#if]
											[#if supplier.status == 'verified' ]
											 	<span>企业已认证</span>
											[/#if]
                                        </td>
                                        <td>${supplier.userName}</td>
                                        <td>${supplier.tel}</td>
                                        <td><span title="${supplier.createDate}">${supplier.createDate}</span></td>
                                        <td>
                                        	<div class="td_operate blue">
                                        		<a href="/shop/supplie/supplierLook.jhtml?id=${supplier.id}" class="show_S">查看</a>
                                        		<a href="javascript:;" listId='${supplier.id}' class="hidden_S del_list">删除</a>
                                        	</div>
                                        </td>
                                    </tr>
                                    [/#list]
                             
                                </tbody>
                            </table>
						</div>
						
					</div>
						[@pagination pageNumber = page.pageNumber totalPages = page.totalPages]
						[#include "/shop/include/pagination.ftl"]
						[/@pagination]
					</form>	
				</div>
			</div>
		</div>
		[#include "/shop/common/foot.ftl"]
		
		
		<script type="text/javascript" src="${base}/resources/shop/common/js/jquery.min.js"></script>
		<script type="text/javascript" src="${base}/resources/shop/common/js/common.js"></script>
        <script type="text/javascript" src="${base}/resources/shop/common/js/list.js"></script>
		<script type="text/javascript" src="${base}/resources/shop/common/js/public.js"></script>
		<script>
			
			$(function(){
				[@flash_message /]
				/*操作的效果*/
				$(".td_operate .show_S").on("mouseover",function(){
					$(this).siblings().css("display","block");
					$(this).parent().addClass("operate_border");
				})
				
				$(".td_operate").on("mouseleave",function(){
					$(this).children(".hidden_S").css("display","none");
					$(this).removeClass("operate_border");
				})
				
				
				var heightObj = $(".page_con .nav").height()-8;
				var table_height = $(".table_div").height()<heightObj?heightObj:"auto";
				$(".table_div").css("height",table_height);
				
				
				$(".del_list").click(function(){
					var $this = $(this);
					$.dialog({
						type: "warn",
						width:400,
						content: "${message("admin.dialog.deleteConfirm")}",
						onOk: function() {
							$.ajax({
								url: "delete.jhtml",
								type: "POST",
								data: {id: $this.attr("listId")},
								dataType: "json",
								cache: false,
								success: function(message) {
									$.message(message);
									if (message.type == "success") {
										$this.closest("tr").remove();
									}
								}
							});
						}
					});
					return false;
				})
				
				
				
				
				
				
				
			})
			
		</script>
	</body>
</html>
[/#escape]