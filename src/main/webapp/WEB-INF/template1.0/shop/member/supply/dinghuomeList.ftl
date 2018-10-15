[#escape x as x?html]
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
		<link rel="stylesheet" href="${base}/resources/shop/common/js/element-ui/lib/theme-default/index.css">
		<style>
			.table_div{background:#fff;border:1px solid #eee;margin-top:10px;}
			.el-input__inner{border:1px solid #eee;}
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
			.el-cascader-menu__item{color:333;}
		</style>
	</head>
	<body class="reg10">
		[#include "/shop/common/head.ftl"]
		<div class="page_con">
			<input id="productCategoryIdStr" type="hidden" value="${productCategoryIdStr } "/>
			<div class="con_center">
				[#include "/shop/member/inc.ftl"]
				<div class="content">
					<form id="listForm" action="dinghuomeList.jhtml" method="get">
					<div class="conList">
						<div class="search_div">
							<span style="float:left;line-height:40px;">微商平台产品分类</span>
							<div class="search_type">
								<input type="hidden" :value="searchVal | arrLast" name="productCategoryId" />
								<input type="hidden" :value="searchVal" name="productCategoryIdStr" />
								<el-cascader
								  	:options="options"
								  	v-model="searchVal"
								  	@change="handleChange"
								  	change-on-select>
								</el-cascader>
							</div>
							<input type="hidden" class="input_S" name="searchProperty" value="name"/>
							<input type="text" class="input_S" name="searchValue" value="${searchValue}"/>
							<button type="button" class="toSearch" id="searchBtn">搜索</button>
						</div>
						<div class="table_div" id="app">
							<table class="table table-hover table_width textLeft">
	                            <thead>
	                                <tr>
	                                    <th width="3%">
	                                    	<input type="checkbox" class="allCheck" />
	                                    </th>
	                                    <th width="24%">产品名称</th>
	                                    <th width="15%">微商平台产品分类</th>
	                                    <th width="28%">商流系统分类</th>
	                                    <th width="10%">基本单位</th>
	                                    <th width="10%">产品规格</th>
	                                    <th width="10%">参考价格</th>
	                                </tr>
                                </thead>
                                <tbody>
                                <input type="hidden" name="productIds" id="productIds" />
                                <input type="hidden" name="categoryIds" id="categoryIds" />
                                	[#list page.content as product]
                                    <tr>
                                        <td class="middle">
                                        	<input type="checkbox" class="check" name="productId" value="${product.id}"/>
                                        </td>
                                        <td class="tdImg">
                                        	[#if product.goods.image]
                                        	<img src="${product.goods.image}" alt="" />
                                        	[#else]
                                        	<img src="${base}/resources/shop/common/images/moren.svg" class="imgMoren" alt="" />
                                        	[/#if]
                                        	<div class="boxImg">
                                        		<span class="goodsName">
	                                        		${abbreviate(product.goods.name, 80, "...")}
	                                        	</span>
                                        	</div>
                                        	
                                        </td>
                                        <td>${product.goods.productCategory.name}</td>
                                        <td>
										  	<el-cascader
										    	:options="options"
										    	v-model="selectedOptions${product.id}"
										    	@change="handleChange">
										  	</el-cascader>
										  	<input type="hidden" class="hiddenIn" :value="selectedOptions${product.id} | arrLast" name="categoryId${product.id}"/>
                                        </td>
                                        <td>
                                        	[#if product.goods.unit?? ]
                                        		${message("Goods.unit."+product.goods.unit)}
                                        	[/#if]
                                        </td>
                                        <td>
                                        	[#list product.specificationValues as spec]
                                        		${spec.value}&nbsp;
                                        	[/#list]
                                        </td>
                                        <td>￥${product.goods.marketPrice}</td>
                                    </tr>
                                    [/#list]
                                </tbody>
                            </table>
						</div>
						[@pagination pageNumber = page.pageNumber totalPages = page.totalPages]
							[#include "/shop/include/pagination.ftl"]
						[/@pagination]
						
						
						
						<div class="footer_submit">
							<input type="button" class="input_B submit_In" value="确定" id="subBtn"/>
							<input type="button"  class="input_B cancel_In " value="取消" onclick="location='addIndex.jhtml';"/>
						</div>	
						
					</div>
					</form>	
				</div>
			</div>
		</div>
		[#include "/shop/common/foot.ftl"]
		
		<script type="text/javascript" src="${base}/resources/shop/common/js/jquery.min.js"></script>
		<script type="text/javascript" src="${base}/resources/shop/common/js/validate/jquery.validate.min.js"></script>
		<script type="text/javascript" src="${base}/resources/shop/common/js/common.js"></script>
		<script type="text/javascript" src="${base}/resources/shop/common/js/list.js"></script>
		<script type="text/javascript" src="${base}/resources/shop/common/js/public.js"></script>
		<script type="text/javascript" src="${base}/resources/shop/common/js/vue.min.js"></script>
  		<script type="text/javascript" src="${base}/resources/shop/common/js/element-ui/lib/index.js"></script>
  
        
		<script>
			
			$(function(){
				
				
				var heightObj = $(".page_con .nav").height()-58;
				
				var table_height = $(".table_div").height()<heightObj?heightObj:"auto";
				$(".table_div").css("height",table_height);
			
				
				$(".check").on("click",function(){
					if($(this).prop("checked")){
						$(this).closest("tr").find(".hiddenIn").addClass("cateClass");
					}else{
						$(this).closest("tr").find(".hiddenIn").removeClass("cateClass");
						$(this).closest("tr").find("label.error").css("display","none");
					}
				})
				$(".allCheck").on("click",function(){
					if($(this).prop("checked")){
						$(".hiddenIn").addClass("cateClass");
					}else{
						$(".hiddenIn").removeClass("cateClass");
						$("label.error").css("display","none");
					}
				})
				
				
				
				$.validator.addClassRules({
					//表单共有的class
                    cateClass: {
                        required:true
                    }
                });
                
				$("#listForm").validate({});
			
				$("#subBtn").on("click", function(){
					if(!$("#listForm").valid()){
                        return false ;
                    }

					var productIds = "";
					var categoryIds = "";
					$('input:checkbox[name=productId]:checked').each(function(i){
				       	var productId = $(this).val();
				       	var categoryId = $('input[name=categoryId' + productId + ']').val();
				       	
				       	if(0 == i){
				         	productIds = productId;
				         	categoryIds = categoryId;
				       	}else{
				         	productIds += (","+productId);
				         	categoryIds += (","+categoryId);
				       	}
				    });
				    if(!productIds){
				    	$.message("warn","请选择商品");
				    	return false;
				    }
				    $("#categoryIds").val(categoryIds);
					$("#productIds").val(productIds);
					$.ajax({
		                url: "/shop/member/supply/saveFromDhm.jhtml",
		                type: "POST",
		                async: false,
		                data: {
		                	productIds: productIds,
		                	categoryIds:categoryIds
		                },
		                dataType: "json",
		                cache: false,
		                success: function(data) {
		                	if(data.type == "success"){
		                		 setTimeout(function () {
		                             window.location.href="list.jhtml";
		                         },500);
		                	}
		                }
		            });

					return false;
				});

				$("#searchBtn").on("click", function(){
					$("#listForm").attr("method", "get");
					$("#listForm").attr("action", "dinghuomeList.jhtml");
					$("#listForm").submit();
				});
				

				/*全选或全不选*/
                $(".allCheck").on("click",function(){
                    $(".check").prop("checked",$(this).prop("checked"));    
                })
                
                $(".check").on("click",function(){
                    $(".allCheck").prop("checked",$(".check").length==$(".check:checked").length?true:false);
                })
				
			
			})
			
			
			var trees = [];

	        var categoryStr = [#noescape]'${categoryRoots?json_string}'[/#noescape];
	
	        trees = JSON.parse(categoryStr) ;
	        
     		var returnData = {};
     		
        	returnData.options = trees;
        	
        	[#list page.content as goods]
        		returnData["selectedOptions${goods.id}"] = [];
     		[/#list]
     		returnData.searchVal = [];
     		
     		
     		new Vue({
	  			el:"#app",
			    data:function() {
			      	return returnData;
				},
				methods: {
					handleChange(value) {
						$(".el-cascader").siblings("label").css("display","none");
						$(".el-input__inner").removeClass("borderFocus");
					}
				},
				filters: {
				    arrLast: function (value) {
				      return value[value.length-1];
				    }
				}
			});
			

     		var tree = [];

	        var productCategory = [#noescape]'${productCategory?json_string}'[/#noescape];
	        tree = JSON.parse(productCategory) ;
     		var searchData = {};
     		
     		searchData.options = tree;
     		searchData.searchVal= [];
     		var productCategoryIdStr = $("#productCategoryIdStr").val();
     		if(productCategoryIdStr != null && productCategoryIdStr != ''){
     			var productCategoryIds = productCategoryIdStr.split(',');
     			for(var i=0; i < productCategoryIds.length;i++){
     				searchData.searchVal.push(Number(productCategoryIds[i]));
     			}
     		}
        	

        	searchData.searchVal=[];

			new Vue({
	  			el:".search_type",
			    data:function() {
			      	return searchData;
				},
				methods: {
					handleChange(value) {
						//console.log(value);
				    	$(".el-input__inner").removeClass("borderFocus");
					}
				},
				filters: {
				    arrLast: function (value) {
				      return value[value.length-1];
				    }
				}
			});
			
			
			$(".el-cascader").on("click",function(event){
				$(this).find(".el-input__inner").addClass("borderFocus");
				event.stopPropagation();
			});
			$(document).on("click",function(){
				$(".el-input__inner").removeClass("borderFocus");
			})
			
			$("img").on("error", function(){  //加入相应的图片类名
		        $(this).prop("src", "${base}/resources/shop/common/images/moren.svg").addClass("imgMoren");
		    });
			
		</script>

	</body>
</html>
[/#escape]
