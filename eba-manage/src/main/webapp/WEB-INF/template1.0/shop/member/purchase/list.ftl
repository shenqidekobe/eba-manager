<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge" />
		<title>华奕优选</title>
		<link rel="stylesheet" href="${base}/resources/shop/common/config/bootstrap/css/bootstrap.min.css" />
		<link rel="stylesheet" href="${base}/resources/shop/common/css/public.css" />
		<link rel="stylesheet" href="${base}/resources/shop/common/css/index.css" />
		<link rel="stylesheet" href="${base}/resources/shop/common/css/member.css" />
		<style>
			.table_div{background:#fff;border:1px solid #eee;margin-top:10px;}
			.xxDialog .dialogwarnIcon{
				margin:0;
				padding:50px 10px;
				font-size:16px;
			}
			.xxDialog .dialogBottom{
				height:44px;
				padding-left:10px;
			}
			.table>tbody>tr>td{padding:12px 8px;}
			.td_operate{
				top:5px;
				padding-top:7px;
				background: url(${base}/resources/shop/common/images/caidanjt-a.svg) no-repeat 75% 10px;
				background-size:14px;
			}
			.operate_border{
				background:#fff url(${base}/resources/shop/common/images/caidanjt-b.svg) no-repeat 75% 10px;
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
					<form id="listForm" action="/shop/member/supply/purchaseList.jhtml" method="get">
					<div class="conList">
						<div class="table_div">
							<table class="table table-hover textLeft">
	                            <thead>
	                                <tr>
	                                    <th width="25%">产品名称</th>
	                                    <th width="15%">产品分类</th>
	                                    <th width="10%">采购数</th>
	                                    <th width="10%">货源类型</th>
	                                    <th width="20%">更新日期</th>
	                                    <th width="10%">状态</th>
	                                    <th width="10%" style="text-align: center;">操作</th>
	                                </tr>
	                            </thead>
                                <tbody>
                                	[#list page.content as goods]
                                    <tr>
                                        <td>${abbreviate(goods.name, 80, "...")}</td>
                                        <td>${goods.category.name}</td>
                                        <td>
                                        
                                        	[#if goods.needNum == "-1"]
			                                    	面议
			                                [#else]
			                                    	${goods.needNum}
			                                [/#if]
                                        </td>
                                        <td>
                                        	[#if goods.sourceType ]
                                        	${goods.sourceType.getName()}
                                        	[/#if]
                                        </td>
                                        <td>${goods.createDate?string("yyyy-MM-dd HH:mm:ss")}</td>
                                        <td>${goods.status.getName()}</td>
                                        <td>
                                        	<div class="td_operate blue">
                                        		<a class="show_S" href="/shop/companyGoods/companyGoodsLook.jhtml?pubType=pub_need&id=${goods.id}">查看</a>
                                        		<a class="hidden_S" href="editPur.jhtml?id=${goods.id}">编辑</a>
                                        		<a href="jacascript:;" class="hidden_S del_list" val="${goods.id}">删除</a>
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
		
		<script src="${base}/resources/shop/common/js/jquery.min.js"></script>
		<script src="${base}/resources/shop/common/js/common.js"></script>
		<script type="text/javascript" src="${base}/resources/shop/common/js/public.js"></script>
		<script src="${base}/resources/shop/common/js/list.js"></script>
		
		<script>
			
			$(function(){
				
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
								url: "removePur.jhtml",
								type: "POST",
								data: {id: $this.attr("val")},
								dataType: "json",
								cache: false,
								success: function(message) {
									$.message(message);
									if (message.type == "success") {
										$this.closest("tr").remove();
									}
									setTimeout(function(){
										location.reload();
									},1000)
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
