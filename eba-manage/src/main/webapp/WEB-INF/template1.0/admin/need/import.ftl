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
			.webuploader-pick{
				    position: relative;
				    display: inline-block;
				    cursor: pointer;
				    background: #4DA1FF;
				    padding: 3px 15px;
				    color: #fff;
				    text-align: center;
				    border-radius: 16px;
				    overflow: hidden;
			}
			.webuploader-element-invisible{opacity: 0;}
			#picker{padding-left:20px;}
		</style>
	</head>
	<body >
		<div class="child_page"><!--内容外面的大框-->	
			<div class="cus_nav">
				<ul>
					<li><a id="goHome"	href="../homePage/index.jhtml">${message("admin.breadcrumb.home")}</a></li>
					<li><a href="list.jhtml">${message("admin.need.list")}</a></li>
					<li>导入</li>
				</ul>
			</div>
			<div class="form_box" style="overflow: auto;">
				<form id="inputForm" action="save.jhtml" method="post" class="form form-horizontal">
					<div class="imprtStep">
						<div class="stratImport">
							<span></span>
							<i></i>
							<i></i>
							<i></i>
							<i></i>
							<i></i>
						</div>
						<div class="seeImport">
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
					
					<div class="stratImportCon">
						<div class="I_template">
							<header>下载数据模板</header>
							<div class="I_rule">
								<p>模板使用说明</p>
								<p>1、表格格式必须为 xls、xlsx及csv格式</p>
								<p>2、收货人姓名、门店名称、门店地区、详细地址为必填项</p>
								<p>3、若门店类型为加盟，登录账号请填写真实手机号；不填时系统自动生成登录账号</p>
								<p>4、若导入门店为直营，无店员时，无需填写店员信息；有店员时，若填写店员手机号，店员可通过填写手机号登录，</br>若不填写店员手机号，系统自动生成店员登录账号</p>
								<p>5、一次最多可导入2000条数据</p>
							</div>
							<a class="btn radius dl_template" style="bottom:60px;" href="${base}/template/加盟门店信息导入模板.xlsm">下载加盟门店添加模板</a>
							<a class="btn radius dl_template" href="${base}/template/直营门店信息导入模板.xlsm">下载直营门店添加模板</a>
						</div>
						<div class="I_template">
							<header>上传导入数据</header>
							<div class="I_rule">
								<div class='I_table'>
									<div id="picker">上传表格</div>
									<i>(选择excel文件，大小不超过20M)</i>
								</div>
								<div class="I_tableList">
									<!--<div class="item">
					        			<span class="wjImg"></span>
					        			<h4 class="info">123434324.jpg</h4>
					        			<span class="delFil"></span>
					    			</div>-->
								</div>
								
							</div>
						</div>
					</div>
				
					<div class="footer_submit">
						<input class="btn radius confir_S" type="button" onclick="javascript:nextstep();" value="下一步" />
						<input class="btn radius cancel_B" type="button" value="${message("admin.common.back")}" onclick="history.back(); return false;" />
					</div>
				</form>
			</div>		
		</div>
		<div class="modalBox">
			<div class="lodding">
				<img src="../../../../resources/admin1.0/images/Loading-b.gif" alt="" />
				<p>导入中...</p>
			</div>
			
		</div>
		
		<script src="${base}/resources/admin1.0/js/jquery.min.js"></script>
		<script type="text/javascript" src="${base}/resources/admin1.0/H-ui/static/h-ui/js/H-ui.min.js"></script> 
		<script type="text/javascript" src="${base}/resources/admin1.0/js/validate/jquery.validate.min.js"></script>
		<script type="text/javascript" src="${base}/resources/admin1.0/js/jquery.lSelect.js"></script>
		<script type="text/javascript" src="${base}/resources/admin1.0/js/webuploader.js"></script>
		<script type="text/javascript" src="${base}/resources/admin1.0/ueditor/ueditor.js"></script>
		<script type="text/javascript" src="${base}/resources/admin1.0/js/common.js"></script>
		<script type="text/javascript" src="${base}/resources/admin1.0/js/input.js"></script>
		<script type="text/javascript">
			
	
            function nextstep(){
				//异步上传文件名 multipartFile
				if(!$(".I_tableList div").length){
					$.message("warn", "请先上传表格!");
					return false;
				}
				$(".modalBox").css("display","block");
				uploader.upload();
                
            }
			
			$(function(){
						
				var formHeight=$(document.body).height()-100;
				$(".form_box").css("height",formHeight);
				
			});
			
			var uploadUrl = "viewMore.jhtml" ;
			var fileUrl = {};
				
			var uploader = WebUploader.create({
			    // swf文件路径
			   	swf: '/resources/admin1.0/flash/webuploader.swf',
			    // 文件接收服务端。
			    server:uploadUrl + (uploadUrl.indexOf('?') < 0 ? '?' : '&') + 'token=' + getCookie("token"),
			    // 选择文件的按钮。可选。
			    // 内部根据当前运行是创建，可能是input元素，也可能是flash.
			    pick: '#picker',
			    fileSingleSizeLimit: 1024 * 1024 * 30,
			    accept: {
			    	extensions: 'xls,xlsx,xlsm,csv'
				},
				//验证文件总数量, 超出则不允许加入队列
				fileNumLimit: 1,
				//auto {Boolean} [可选] [默认值：false] 设置为 true 后，不需要手动调用上传，有文件选择即开始上传
				auto: false,
			    // 不压缩image, 默认如果是jpeg，文件上传前会压缩一把再上传！
			    resize: false,
                fileVal : 'multipartFile'
			}).on('uploadAccept', function(file, data) {
				

			}).on('fileQueued', function(file) {
				var  $list = $(".I_tableList");
				$list.append( '<div id="' + file.id + '" class="item">' +
			        '<span class="wjImg"></span>'+'<h4 class="info">' + file.name + '</h4>' + '<span class="delFil"></span>'+
			    '</div>' );
				$(".delFil").off("click").on("click",function(){
			    	var fileId = $(this).parent().attr("id");
			    	uploader.removeFile( fileId,true);
			    	$(this).parent().remove();
			    })
			}).on("uploadSuccess",function(file , response){
				if(response.code == "0"){
					//console.log(file);
					//异步请求后返回批次id
                	window.location.href="importList.jhtml?logId="+response.data;
				}else{
					$(".modalBox").css("display","none");
					$.message("error", response.msg);
				}

			}).on('error', function(type) {
				switch(type) {
					case "F_EXCEED_SIZE":
						$.message("warn", "上传文件大小超出限制");
						break;
					case "Q_TYPE_DENIED":
						$.message("warn", "上传文件格式不正确");
						break;
					case "F_DUPLICATE":
						$.message("warn", "文件重复上传");
						break;
					case "Q_EXCEED_NUM_LIMIT":
						$.message("warn", "只能上传一个表格");
						break;
					default:
						$.message("warn", "上传文件出现错误");
				}
			});
			
			$("#goHome").on("click",function(){
				var nav = window.top.$(".index_nav_one");
        			nav.find("li li").removeClass('clickTo');
					nav.find("i").removeClass('click_border');
			})
			
			
		</script>
		
	</body>
</html>
[/#escape]