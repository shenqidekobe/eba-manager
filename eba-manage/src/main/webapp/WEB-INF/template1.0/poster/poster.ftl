[#escape x as x?html]
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>Document</title>
	<link rel="stylesheet" href="${base}/resources/poster/css/poster.css" />
</head>
<body>
	<div class="pageView">
		<div class="conView">
			<div class="header">
				<div class="title">${data.clientName}</div>
	        	<div class="theme">${data.theme}</div>
			</div>
	 		<div class="textView">
	 			[#list data.profiles as profile]
	 				<p>${profile}</p>
	 			[/#list]
	 		</div>
	 		<!-- <div class="textView" style="word-break:break-all">
	 			${assCustomerRelation.profiles}

	 		</div> -->
	 		<div class="goodsView">
			[#list data.goods as good]
				[#if good_index < 8 ]
					<div class="goodsLi">
						<div class="goodsImg">
							[#if good.image == null || good.image.size() == 0]
								<img src="${base}/resources/poster/images/wu.png" alt="">
							[#else]
								[#list good.image as images]
									[#if images_index == 0]
										<img src="${images}" alt="">
									[/#if]
								[/#list]
							[/#if]
						</div>
						<p class="goodsName">${good.name}</p>
					</div>
				[/#if]
				[#if good_index == 8 && data.goods.size()==9 ]
					<div class="goodsLi">
						<div class="goodsImg">
							[#if good.image == null || good.image.size() == 0]
								<img src="${base}/resources/poster/images/wu.png" alt="">
							[#else]
								[#list good.image as images]
									[#if images_index == 0]
										<img src="${images}" alt="">
									[/#if]
								[/#list]
							[/#if]
						</div>
						<p class="goodsName">${good.name}</p>
					</div>
				[/#if]
			[/#list]
			[#if data.goods.size() > 9]
				<div class="goodsLi liNine">
					<div class="gengduoDiv">
						<div class="jiange"></div>
						<img src="${base}/resources/poster/images/gengduo.png" alt="">
						<p class="gengduoText">共<span>${data.goods.size()}</span>件商品</p>
					</div>
				</div>
			[/#if]
	 		</div>


	 		<div class="codeDiv">
	 			<p class="codeText">更多商品信息 </p>
	 			<p class="codeText">请长按识别小程序码</p>
				<p class="codeText timeP">${.now?string("yyyy年MM月dd日 HH:mm")}</p>
	 			<img src="${base}/ass/common/generateTwoCode.jhtml?sn=${data.sn}" alt="">
	 		</div>
	 		[#if data.exist]
		 		<div class="cardDiv">
		 			<div class="infoDiv">
		 				<span class='name'>${data.name}</span>
		 				<span class='position'>${data.position}</span>
		 			</div>	
		 			<div class="weixin">${data.wxNum}</div>
		 			<div class="phone">${data.phone}</div>
					<img src="${base}/resources/poster/images/qinglianxi.png" alt="">
		 		</div>
	 		[/#if]
	 		<div class="footer">
	 			——快来生成属于您的订货助理——
	 		</div>
 		</div>
	</div>
</body>
</html>
[/#escape]





