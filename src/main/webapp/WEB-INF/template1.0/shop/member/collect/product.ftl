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
		</style>
	</head>
	<body class="reg10">
		[#include "/shop/common/head.ftl"]
		<div class="page_con">
			<div class="con_center">
				[#include "/shop/member/inc.ftl"]
				<div class="content">
					<form action="">
					<div class="conList">
						<div class="table_div">
							<table class="table table-hover">
	                            <thead>
	                                <tr>
	                                    <th width="20%">产品名称</th>
	                                    <th width="10%">产品分类</th>
	                                    <th width="10%">参考价格</th>
	                                    <th width="10%">货源类型</th>
	                                    <th width="20%">发布企业</th>
	                                    <th width="15%">更新日期</th>
	                                    <th width="10%">操作</th>
	                                </tr>
	                            </thead>
	                        </table>
                            <div class="list_tbody">
	                            <table class="table table-hover">
	                                <thead>
	                                    <tr>
		                                    <th width="20%"></th>
		                                    <th width="10%"></th>
		                                    <th width="10%"></th>
		                                    <th width="10%"></th>
		                                    <th width="20%"></th>
		                                    <th width="15%"></th>
		                                    <th width="10%"></th>
		                                </tr>
	                                </thead>
	                                <tbody>
	                                    <tr>
	                                        <td>柠檬味奶茶原料</td>
	                                        <td>水果</td>
	                                        <td>400</td>
	                                        <td>现货</td>
	                                        <td>上海同乐实业有限公司</td>
	                                        <td>2017-12-21 23:12:20</td>
	                                        <td>
	                                        	<div class="td_operate blue">
	                                        		<a href="jacascript:;" class="show_S">查看</a>
	                                        		<a href="jacascript:;" class="hidden_S">编辑</a>
	                                        		<a href="jacascript:;" class="hidden_S">删除</a>
	                                        	</div>
	                                        </td>
	                                    </tr>
	                                    
	                                   
	                                    
	                             
	                                </tbody>
	                            </table>
	                        </div>
						</div>
						
						
						
						
						
					</div>
					</form>	
				</div>
			</div>
		</div>
		[#include "/shop/common/foot.ftl"]
		
		<script src="${base}/resources/shop/common/js/jquery.min.js"></script>
		<script src="${base}/resources/shop/common/js/common.js"></script>
		<script type="text/javascript" src="${base}/resources/shop/common/js/public.js"></script>
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
				
				
				var heightObj = $(".page_con .nav").height()-47;
				console.log(heightObj);
				$(".list_tbody").css("height",heightObj);
				
			})
			
		</script>
		
		
		
		
		
	</body>
</html>
