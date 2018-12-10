
[#escape x as x?html]
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, minimum-scale=1, user-scalable=no">
    <meta content="telephone=no,email=no" name="format-detection">
    <link rel="stylesheet" href="${base}/resources/mobile/css/public.css" />
    <link rel="stylesheet" href="${base}/resources/mobile/css/goods.css" />
    <link rel="stylesheet" href="${base}/resources/mobile/js/mobiscroll/mobiscroll.css" />
    <title>添加商品</title>
    <style>
        body{background:#fbfbfb;}
    </style>
</head>
<body>

    <div class="addGoods">
        <div class="uploadImg">
            <div class="img">
                上传商品图片
                <img src="${base}/resources/mobile/images/img.jpg" alt="">
            </div>
            <div class="caozuo">
                <div class="upload_B">
                    <div id="picker"></div>
                </div>
                <span class="delImg"></span>
            </div>
        </div>
        <ul class="info_ul">
            <li class="li_info li_jiantou">
                <span class="label">商品分类</span>
                <div class="input">
                    <input type="hidden" class="goodsType_in">
                    <ul id="goodsCation" value="hahhha" style="display:none">
                        <li data-val="汽车,2">汽车
                            <ul>
                                <li data-val="别克,21">别克
                                    <ul>
                                        <li data-val="威朗,211">威朗</li>
                                        <li data-val="君威,212">君威</li>
                                        <li data-val="君越,213">君越</li>
                                    </ul>
                                </li>
                                <li data-val="本田,23">本田
                                    <ul>
                                        <li data-val="冠道,231">冠道</li>
                                        <li data-val="雅阁,232">雅阁</li>
                                        <li data-val="本田X-RV,233">本田X-RV</li>
                                        <li data-val="本田,234">本田</li>
                                    </ul>
                                </li>
                            </ul>
                        </li>
                    </ul>
                </div>
            </li>
            <li class="li_info">
                <span class="label">商品名称</span>
                <div class="input">
                    <input type="text" placeholder="请输入商品名称(必填)" class="info">
                </div>
            </li>
            <li class="li_info">
                <span class="label">商品编号</span>
                <div class="input">
                    <input type="text" placeholder="请输入商品编号" class="info">
                </div>
            </li>
            <li class="li_info">
                <span class="label">保质期</span>
                <div class="input">
                    <input type="text" placeholder="请输入保质期天数" class="info">
                </div>
            </li>
            <li class="li_info li_jiantou">
                <span class="label">基本单位</span>
                <div class="input">
                    <input type="hidden" class="compony_in">
                    <ul id="compony" style="display:none">
                        [#list units as unit]
                        <li data-val="${message("Goods.unit."+unit)},${unit}">${message("Goods.unit."+unit)}</li>
                        [/#list]
                    </ul>
                </div>
            </li>
            <li class="li_info li_jiantou">
                <span class="label">保存条件</span>
                <div class="input">
                    <input type="hidden" class="saveTrem_in">
                    <ul id="saveTrem" style="display:none">
                        [#list storageConditions as storageCondition]
                        <li data-val='${message("Goods.storageConditions."+storageCondition)},${storageCondition}'>${message("Goods.storageConditions."+storageCondition)}</li>
                        [/#list]
                    </ul>
                </div>
            </li>
            <li class="li_info">
                <span class="label">销售价</span>
                <div class="input">
                    <input type="text" placeholder="请输入销售价格" class="info">
                </div>
            </li>
        </ul>
        <p class="p_tishi">多规格商品请至微信小程序pc后台进行管理</p>
    </div>
    <input type="button" class="input_s input_B" value="保存">



    <script src="${base}/resources/mobile/js/jquery.min.js"></script>
    <script src="${base}/resources/mobile/js/common.js"></script>
    <script src="${base}/resources/mobile/js/webuploader.js"></script>
    <script src="${base}/resources/mobile/js/mobiscroll/mobiscroll.js"></script>
    <script src="${base}/resources/mobile/js/adaptive.js"></script>
    <script>

        $(function() {



            $('#goodsCation').mobiscroll().treelist({
                theme: 'ios',
                lang: 'zh',
                display: 'bottom',
                placeholder: '请选择商品分类(必选)',
                onSet: function (event, inst) {
                    var arr = event.valueText.split(" ");
                    var text = '',valueCode=0;
                    for(var value of arr){
                        text+=value.split(",")[0]+" ";
                        valueCode = value.split(",")[1];
                    }
                    $("#goodsCation_dummy").val(text);
                    $(".goodsType_in").val(valueCode);
                }

            });


            $('#compony').mobiscroll().treelist({
                theme: 'ios',
                lang: 'zh',
                display: 'bottom',
                placeholder: '请选择基本条件',
                defaultValue:["袋,bag"],
                onSet: function (event, inst) {
                    var text = event.valueText.split(",");
                    $(".compony_in").val(text[1]);
                    $("#compony_dummy").val(text[0]);
                }

            });
            $('#saveTrem').mobiscroll().treelist({
                theme: 'ios',
                lang: 'zh',
                display: 'bottom',
                placeholder: '请选择保存条件',
                defaultValue:["冷藏,refrigeration"],
                onSet: function (event, inst) {
                    var text = event.valueText.split(",");
                    $(".saveTrem_in").val(text[1]);
                    $("#saveTrem_dummy").val(text[0]);
                }

            });



            var uploadUrl = "/admin/file/upload.jhtml";
            var uploader = WebUploader.create({
                swf: '/resources/mobile/js/Uploader.swf',
                server:uploadUrl,
                pick: '#picker',
                fileSingleSizeLimit: 1024 * 1024 * 30,
                accept: {
                    extensions: 'jpg,jpeg,bmp,gif,png,pdf'
                },
                fileNumLimit: 10,
                //auto不需要手动调用上传，有文件选择即开始上传
                auto: true,
                // 不压缩image, 默认如果是jpeg，文件上传前会压缩一把再上传！
                resize: false
            }).on('uploadAccept', function(file, data) {
                console.log(file);
                console.log(data);

            }).on('error', function(type) {
                console.log(type);
                switch(type) {
                    case "F_EXCEED_SIZE":
                        errorInfoFun("上传文件大小超出限制");
                        break;
                    case "Q_TYPE_DENIED":
                        errorInfoFun("上传文件格式不正确");
                        break;
                    case "F_DUPLICATE":
                        errorInfoFun("文件重复上传");
                        break;
                    default:
                        errorInfoFun("上传文件出现错误");
                }
            }).on('fileQueued', function(file) {
                console.log(file);
            });
        })
    </script>
</body>
</html>
[/#escape]


















