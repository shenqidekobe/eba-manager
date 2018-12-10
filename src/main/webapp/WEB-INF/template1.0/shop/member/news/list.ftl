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
			.messageCon{
				width:380px;
				overflow: hidden;
				text-overflow:ellipsis;
				white-space: nowrap;
			}
			.table>tbody>tr>td{padding:12px 8px;}
		</style>
	</head>
	<body class="reg10">
		[#include "/shop/common/head.ftl"]
		<div class="page_con">
			<div class="con_center">
				[#include "/shop/member/inc.ftl"]
				<div class="content">
					<form action="list.jhtml" method="get">
					<div class="conList">
						<div class="table_div">
							<table class="table table-hover textLeft">
	                            <thead>
	                                <tr>
	                                    <th width="25%">消息标题</th>
	                                    <th width="40%">消息内容</th>
	                                    <th width="25%">发布日期</th>
	                                    <th width="10%">操作</th>
	                                </tr>
	                            </thead>
                                <tbody>
                                [#list page.content as msg]
	                                <tr>
	                                   <td style="text-indent: 15px;"><i 
	                                        [#if msg.receiverRead == false]
	                                        	class="red_spot"
	                                        [/#if]
	                                   ></i>${msg.newMessage.title}</td>
	                                   <td><div class='messageCon'>${msg.newMessage.content}</div></td>
	                                   <td>${msg.newMessage.createDate?string("yyyy-MM-dd HH:mm:ss")}</td>
	                                   <td class="blue" style="cursor:pointer;" onclick="location='view.jhtml?id=${msg.id}';">查看</td>
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
		<script>
			
			$(function(){
				
				var heightObj = $(".page_con .nav").height()-8;
				var table_height = $(".table_div").height()<heightObj?heightObj:"auto";
				$(".table_div").css("height",table_height);
				
			})
			
		</script>
		
		
		
		
		
	</body>
</html>
