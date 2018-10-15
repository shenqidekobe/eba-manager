<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, minimum-scale=1, user-scalable=no">
    <meta content="telephone=no,email=no" name="format-detection">
    <title>添加备注</title>
    <link rel="stylesheet" href="${base}/resources/mobile/css/public.css" />
    <link rel="stylesheet" href="${base}/resources/mobile/css/order.css" />
    <style>
        html,body{background:#fff;}
        #description-error{color:red;}
    </style>
</head>

<body>
    <form action="" id="orderRemark">
        <div class="addRemark">
            <textarea class="textarea" id="description" name="description" maxlength="100" placeholder="最多输入100个字"></textarea>

            <div class="addArea">
                <!--<div class='imgBox'>
                    <img src='${base}/resources/mobile/images/img.jpg' />
                    <span class='delImg'></span>
                </div>-->


                <div class="image">
                    <span  id="picker">
                        <div class="webuploader-pick"></div>
                    </span>
                </div>
            </div>
        </div>
        <button type="button" class="submit_to">添加</button>
        <p class="pageRemind"></p>
    </form>

    <div class="addModel" >
        <img src="${base}/resources/mobile/images/loading.gif" />
    </div>

    <script type="text/javascript" src="${base}/resources/mobile/js/jquery.min.js"></script>
    <script type="text/javascript" src="${base}/resources/mobile/js/adaptive.js"></script>
    <script type="text/javascript" src="${base}/resources/admin1.0/js/validate/jquery.validate.min.js"></script>
    <script type="text/javascript" src="${base}/resources/admin1.0/js/webuploader.js"></script>
    <script type="text/javascript" src="${base}/resources/mobile/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/admin1.0/js/common.js"></script>

    <script>
        $(function(){
            var orderId = GetQueryString("id");
            /*主图上传图片*/
            var $picker = $("#picker");
            var fileUrl = {};
            $picker.uploader({
                maxSize:6,
                before:function(file){
                    console.log(file);
                },
                complete:function(file,data){
                    console.log(data);
                    console.log(file.file);
                    if(data.state == 'SUCCESS'){

                        var addImg = '<div class="imgBox" id="'+ file.file.id +'">' +
                            '<img src="'+ data.url +'" />' +
                            '<span class="delImg"></span>' +
                            '</div>';

                        $(".image").before(addImg);

                        fileUrl[file.file.id] = {'url':data.url , "fileName":file.file.name , "suffix":file.file.ext , "size": file.file.size}

                    }

                }
            });
            $(".submit_to").on("click",function(){
                var $description = $("#description").val();
                var formDate = {} ;
                var index = 0 ;
                $.each(fileUrl,function(key,value) {
                    formDate['annex[' + index +'].url'] = value.url ;
                    formDate['annex[' + index +'].fileName'] = value.fileName ;
                    formDate['annex[' + index +'].suffix'] = value.suffix ;
                    formDate['annex[' + index +'].size'] = value.size ;
                    index ++ ;
                });
                formDate['orderId'] = orderId ;
                formDate['description'] = $description ;
                if(!$("#orderRemark").valid()){
                    return false ;
                }
                console.log(formDate);
                $(".addModel").addClass('modelDisplay');
                $.ajax({
                    url: "addRemarks.jhtml",
                    type: "POST",
                    data: formDate,
                    dataType: "json",
                    cache: false,
                    success: function (message) {
                        console.log(message)
                        if (message.type == "success") {
                            errorInfoFun('添加成功');
                            window.location.href="remarksMobile.jhtml?id="+orderId;
//                            window.history.go(-1);

                        }else{

                        }
                    }
                });

            });

            /*删除图片*/
            $(".addArea").delegate('.imgBox .delImg',"click",function(){
                var fileId = $(this).parent().attr("id");
                delete fileUrl[fileId];
                $(this).parent().remove();
            });



            $("#orderRemark").validate({
                rules: {
                    description:{
                        required:true,
                        maxlength:100
                    }
                }
            });




        });


    </script>
</body>
</html>
