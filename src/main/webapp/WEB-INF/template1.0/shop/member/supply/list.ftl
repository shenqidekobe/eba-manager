<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge" />
		<title>微商平台</title>
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
			.boxImg{
				display:table;
				width:65%;
				height:60px;
				float:left;
			}
			.tdImg img{
				float:left;
			}
			.goodsName{
				vertical-align:middle;    
	          	display:table-cell;        
	          	width:60px; 
			}
		</style>
	</head>
	<body class="reg10">
		[#include "/shop/common/head.ftl"]
		<div class="page_con" id="app">
			<div class="con_center">
				[#include "/shop/member/inc.ftl"]
				<div class="content">
					<form id="listForm" action="/shop/member/supply/list.jhtml" method="get">
					<div class="conList">
						<div class="table_div">
							<table class="table table-hover textLeft">
	                            <thead>
	                                <tr>
	                                    <th width="28%">产品名称</th>
	                                    <th width="14%">产品分类</th>
	                                    <th width="12%">参考价格</th>
	                                    <th width="10%">货源类型</th>
	                                    <th width="18%">更新日期</th>
	                                    <th width="8%">状态</th>
	                                    <th width="10%" style="text-align: center;">操作</th>
	                                </tr>
                                </thead>
                                <tbody>
                                	[#list page.content as goods]
                                    <tr>
                                        <td class="tdImg">
                                        	[#if goods.image??]
                                        	<img src="${goods.image}" alt="" />
                                        	[#else]
                                        	<img src="${base}/resources/shop/common/images/moren.svg" class="imgMoren" alt="" />
                                        	[/#if]
                                        	<div class="boxImg">
                                        		<span class="goodsName">
	                                        		${abbreviate(goods.name, 80, "...")}
	                                        	</span>
                                        	</div>
                                        	
                                        </td>
                                        <td>
                                        	${goods.category.name}
                                        </td>
                                        <td>
                                        	[#if goods.marketPrice == "-1"]
			                                    	面议
			                                [#else]
			                                    	￥${goods.marketPrice}
			                                [/#if]
                                        </td>
                                        <td>[#if goods.sourceType]
                                        	${goods.sourceType.getName()}
                                        	[/#if]
                                        </td>
                                        <td>${goods.createDate?string("yyyy-MM-dd HH:mm:ss")}</td>
                                        <td>${goods.status.getName()}</td>
                                        <td style="text-align: center;">
                                        	<div class="td_operate blue">
                                        		<a class="show_S" href="/shop/companyGoods/companyGoodsLook.jhtml?pubType=pub_supply&id=${goods.id}">查看</a>
                                        		<a class="hidden_S" href="edit.jhtml?id=${goods.id}">编辑</a>
                                        		<a class="hidden_S del_list" val="${goods.id}">删除</a>
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
		<script src="${base}/resources/shop/common/js/list.js"></script>
		<script src="${base}/resources/shop/common/js/public.js"></script>
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
								url: "remove.jhtml",
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
				
				
				$("img").on("error", function(){  //加入相应的图片类名
			        $(this).prop("src", "${base}/resources/shop/common/images/moren.svg");
			    });
				
	
			})
			

		</script>
		
		
		
		
		
	</body>
</html>
