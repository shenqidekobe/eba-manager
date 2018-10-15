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
			.stratImportCon{width:100%;}
			.I_template{margin:20px 0 0 30px;width:calc(100% - 60px);}
			.I_template header{background:#f9f9f9;}
			.I_rule p{width:70%;}
			.xxDialog{top:10%;}
			.dialogBottom{display:none;}
			.dialogContent{
				position:relative;
				height: calc(100% - 40px);
				overflow: auto;
				overflow-x: hidden;
			}
			.table_box th{
				border-top:1px solid #f0f0f0;
			}
		</style>
	</head>
	<body >
		<div class="child_page"><!--内容外面的大框-->	
			<div class="cus_nav">
				<ul>
					<li><a id="goHome"	href="../homePage/index.jhtml">${message("admin.breadcrumb.home")}</a></li>
					<li><a href="list.jhtml">商品列表</a></li>
					<li>导入商品图片</li>
				</ul>
			</div>
			<div class="form_box" style="overflow: auto;">
				<input type="hidden" class="batchInput" value="${batch}" />
				<form id="inputForm" action="save.jhtml" method="post" class="form form-horizontal">
					<div class="stratImportCon">
						<div class="I_template">
							<header>上传图片说明</header>
							<div class="I_rule">
								<p>1、本次上传图片的商品，如商品已有图片，则替换原有图片</p>
								<p>2、商品图片命名规则：商品编号或商品编号+(数字),()只支持英文输入法半角。例：商品编号：201712212659，图片命名：201712212659.jpg、201712212659(1).jpg、201712212659(2).jpg；</p>
								<p>3、单一商品，一次最多可上传3张图片，以编号命名代表主图，以编号+(数字) 命名代表展示图片，建议每张图片尺寸800*800px，大小不超过6M，仅支持JPG、PNG、JPEG</p>
							</div>
							<buttton class="batchImgButtom">批量上传商品图片</buttton>
						</div>

						<!--<div class="batchImgs">
							<div class="batchImg">
								<p class="progress"><span></span></p>
								<img src="${base}/resources/admin1.0/images/denglubj_icon.png" alt="">
								<div class="info">1232321.png</div>
							</div>
						</div>-->
						<div class="table_box" style="margin:20px 0;">
							<table class="table table-border table-hover table_width">
								<thead>
									<tr class="text-l">
										<th width="25%">商品编号</th>
										<th width="25%">商品名称</th>
										<th width="50%">图片</th>
									</tr>
								</thead>
								<tbody class="goodImgTbody">
									<!--<tr>
										<td>1232323123</td>
										<td>西红柿</td>
										<td class="updateImgTd">
											<div class="themeImg">
												<img src="${base}/resources/admin1.0/images/denglubj_icon.png" alt="" />
												主图
											</div>
											<div class="detailImgs">
												<img src="${base}/resources/admin1.0/images/denglubj_icon.png" alt="" />
												<img src="${base}/resources/admin1.0/images/denglubj_icon.png" alt="" />
											</div>

										</td>
									</tr>-->

								</tbody>
							</table>
						</div>

					</div>
				
					<div class="footer_submit">
						<input class="btn radius confir_S" type="button" onclick="javascript:nextstep();" value="确定" />
						<input class="btn radius cancel_B" type="button" value="${message("admin.common.back")}" onclick="history.back()" />
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
		<div class="xxModel">
			<div class="close"></div>
			<div class="title">信息提示</div>
			<div class="content">确定退出上传？</div>
			<div class="bottom">
				<input type="button" class="define" value="确定" />
				<input type="button" class="cancel" value="取消" />
			</div>
		</div>
		
		<script src="${base}/resources/admin1.0/js/jquery.min.js"></script>
		<script type="text/javascript" src="${base}/resources/admin1.0/H-ui/static/h-ui/js/H-ui.min.js"></script> 
		<script type="text/javascript" src="${base}/resources/admin1.0/js/validate/jquery.validate.min.js"></script>
		<script type="text/javascript" src="${base}/resources/admin1.0/js/jquery.lSelect.js"></script>
		<script type="text/javascript" src="${base}/resources/admin1.0/js/js/webuploader.min.js"></script>
		<script type="text/javascript" src="${base}/resources/admin1.0/js/common.js"></script>
		<script type="text/javascript" src="${base}/resources/admin1.0/js/input.js"></script>
		<script type="text/javascript">
			
	
            function nextstep(){

            	location.href = "/admin/goodsCenter/saveMoreImage.jhtml?batch="+batch;
            }


			$(function(){
						
				var formHeight=$(document.body).height()-100;
				$(".form_box").css("height",formHeight);
				
			});

            var batch = $(".batchInput").val();
            $(".batchImgButtom").on("click",function(){

                $.dialog({
                    title: "批量上传文件",
                    width: 600,
                    height: 400,
                    content:
					'<div class="choiceImg"><div id="picker" class="batchUpdateImg">选择文件</div><span>允许上传图片类型（jpg,png,jpeg），最大不能超过 6MB</span></div>'+
					'<div class="batchImgs"></div>',
                    modal: true,
                    onShow: function () {
                        updateImgFun();
                        $(".xxModel .close").on("click",function(){
                            $(".xxModel").css("display","none");
						})
                        $(".xxModel .cancel").on("click",function(){
                            $(".xxModel").css("display","none");
                        })
                        $(".xxModel .define").on("click",function(){
                            $(".xxModel").css("display","none");
                            $(".xxDialog").remove();
                            $(".dialogOverlay").remove();
                        })

                    },
                    onClose:function(){
                        var imgsDiv = $(".batchImgs .batchImg");
                        if(imgsDiv.length>0){
							$(".xxModel").css("display","block");
                            return false;
						}
					}
                })
			});

			function updateImgFun(){

				var fileUrl = {};
				var $ = jQuery,
					$list = $(".batchImgs"),
					imgIndex = 0,
				// 优化retina, 在retina下这个值是2

				ratio = window.devicePixelRatio || 1,

				// 缩略图大小
				thumbnailWidth = 120 * ratio,
				thumbnailHeight = 120 * ratio,
				updateImgBool = true;
				uploadUrl = '/admin/goodsCenter/upload.jhtml';

				var uploader = WebUploader.create({
					// swf文件路径
					swf: '/resources/admin1.0/flash/webuploader.swf',
					// 文件接收服务端。
					server:uploadUrl + (uploadUrl.indexOf('?') < 0 ? '?' : '&') + 'fileType=image' +
					'&token=' + getCookie("token")+'&batch='+batch,
					// 选择文件的按钮。可选。
					// 内部根据当前运行是创建，可能是input元素，也可能是flash.
					pick: '#picker',
					fileSingleSizeLimit: 1024 * 1024 * 6,
					accept: {
						extensions: 'jpg,jpeg,png',
					},
					//验证文件总数量, 超出则不允许加入队列
					fileNumLimit: 300,
					//auto {Boolean} [可选] [默认值：false] 设置为 true 后，不需要手动调用上传，有文件选择即开始上传
					auto: true,
					// 不压缩image, 默认如果是jpeg，文件上传前会压缩一把再上传！
					resize: false,
					fileVal : 'file'
				});
				uploader.on('fileQueued', function(file) {
                    $(".choiceImg").css("display",'none');
					fileQueued(file);

				}).on( 'uploadProgress', function( file, percentage ) {

					var $li = $( '#'+file.id ),
						$percent = $li.find('.progress span');

					// 避免重复创建
					if ( !$percent.length ) {
						$percent = $('<p class="progress"><span></span></p>')
							.appendTo( $li )
							.find('span');
					}
                    percentage = percentage.toFixed(2);
					$percent.css( 'width', percentage * 100 + '%' );
					$percent.html(percentage * 100 + '%');
				}).on('uploadAccept', function(file, data) {

					console.log(file);
					 console.log(data);

				}).on("uploadSuccess",function(file , response){
					console.log(file);
					console.log(response);

					if(response.state == "SUCCESS"){

                        $.ajax({
                            type: "post",
                            url: "/admin/goods/importImageList.jhtml?batch="+batch,
                            success: function (data) {
								console.log(data);
                                mateGood(data);
                            },
							error:function(data){
                                console.log(data);
							}
                        });

						$( '#'+file.id ).remove();

						if($(".batchImgs .batchImg").length == 0){

							$(".xxDialog").remove();
							$(".dialogOverlay").remove();

						}

					}else{
                        $( '#'+file.id ).find('.progress span').css("background",'red').html(response.message);
                    }

				}).on('error', function(type,file) {
					//console.log(type);
                    $(".choiceImg").css("display",'none');
					fileQueued(file);
					$li = $( '#'+file.id );
					$percent = $li.find('.progress span');
					if ( !$percent.length ) {
						$percent = $('<p class="progress"><span></span></p>')
							.appendTo( $li )
							.find('span');
					}
					$percent.css( 'width','100%' );
					errorText = '';

					switch(type) {
						case "F_EXCEED_SIZE":
							errorText = '上传文件大小超出限制';
							//$.message("warn", "上传文件大小超出限制");
							break;
						case "Q_TYPE_DENIED":
							errorText = '上传文件格式不正确';
							//$.message("warn", "上传文件格式不正确");
							break;
						case "F_DUPLICATE":
							errorText = '文件重复上传';
							//$.message("warn", "文件重复上传");
							break;
						case "Q_EXCEED_NUM_LIMIT":
							errorText = '只能上传一个表格';
							//$.message("warn", "只能上传一个表格");
							break;
						default:
							errorText = '上传文件出现错误';
							//$.message("warn", "上传文件出现错误");
					}
					$percent.css("background",'red').html(errorText);

				});

				/*但图片添加进来时*/
				function fileQueued(file){
					var img;
					var $li = $(
						'<div id="' + file.id + '" class="batchImg">' +
						'<div class="imgDiv"></div>'+
						'<div class="info">' + file.name + '</div>' +
						'</div>'
					);
                    console.log(file);
                    console.log(isNaN(file));

					//$img = $li.find('img');
					$list.append( $li );
					var imgDiv = $li.find( '.imgDiv' );

					if(isNaN(file)){
                        if(file.type.indexOf('image') != -1&&file.size<1024*1024*6){
                            uploader.makeThumb(file, function( error, src ) {
                                if ( error ) {
                                    imgDiv.text( '不能预览' );
                                    return;
                                }
                                img = $('<img src="'+src+'">');
                                imgDiv.empty().append( img );
                                //$img.attr( 'src', src );
                            }, thumbnailWidth, thumbnailHeight );
                        }
					}


				}
            }

            /*匹配的商品*/
            function mateGood(data){
                var table = $(".goodImgTbody");
                table.html('');

                for(var i=0; i<data.length; i++){
                    var tr = '<tr><td>'+ data[i].sn +'</td><td>'+ data[i].name +'</td><td class="updateImgTd">'+
						'<div class="themeImg">';
                    if(data[i].image){
                        tr += '<img src="'+data[i].image+'" alt="" />主图';
					}
					tr += '</div><div class="detailImgs">';
                    if(data[i].images.length){
                        for(var j=0; j<data[i].images.length; j++){
                            tr += '<img src="'+data[i].images[j].value+'" alt="" />';
						}
					}
					tr += '</div></td></tr>'
                    table.append(tr);
				}

			}


			/*弹出框，上传的图片*/


			$("#goHome").on("click",function(){
				var nav = window.top.$(".index_nav_one");
        			nav.find("li li").removeClass('clickTo');
					nav.find("i").removeClass('click_border');
			})
			
			
		</script>
		
	</body>
</html>
[/#escape]