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
        body{background:#fbfbfb;}
    </style>
</head>

<body>
<div class="addRemark">
    <textarea class="textarea" maxlength="100" placeholder="最多输入100个字"></textarea>

    <div class="addArea">
        <div class='imgBox'>
            <img src='' />
            <span class='delImg'></span>
        </div>


        <div class="image">
            <span  id="picker">
                <div class="webuploader-pick">上传附件</div>
                <!--<div id="rt_rt_1br8vshg9f4v19adpdd622u6n1" style="position: absolute; top: 0px; left: 0px; width: 48px; height: 48px; overflow: hidden; bottom: auto; right: auto;"><input name="file" class="webuploader-element-invisible" multiple="multiple" accept="" type="file"><label style="opacity: 0; width: 100%; height: 100%; display: block; cursor: pointer; background: rgb(255, 255, 255) none repeat scroll 0% 0%;"></label></div>-->
            </span>
        </div>
    </div>
</div>
<button type="button" class="submit_to">添加</button>
<p class="pageRemind"></p>

<script src="${base}/resources/mobile/js/jquery.min.js"></script>
<script src="${base}/resources/mobile/js/adaptive.js"></script>

<script>

</script>
</body>
</html>
