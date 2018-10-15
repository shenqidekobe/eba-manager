/*倒计时*/
function clickButton(obj1){
    var obj = obj1;
    obj.attr("disabled","disabled");/*按钮倒计时*/
    var time = 60;
    var set=setInterval(function(){
        obj.val(--time+"s后重新获取");
    }, 1000);/*等待时间*/
    setTimeout(function(){
        obj.attr("disabled",false).val("重新获取验证码");/*倒计时*/
        clearInterval(set);
    }, 60000);
}

/*当输入的格式不正确的时候，显示提示信息*/
function errorInfoFun(obj){//obj-->提示的信息内容
    if($(".pageRemind").length){
        $(".pageRemind").html(obj).css({"display":"block"});
    }else{
        $("body").append('<p class="pageRemind">'+obj+'</p>');
        $(".pageRemind").css({"display":"block"});
    }
    setInterval(function(){
        $(".pageRemind").fadeOut(2000);
    },2000)
}
function GetQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if (r != null)
        return unescape(r[2]);
    return null;
}







var utlz = {
    base: "",
    locale: "zh_CN",
    theme: "default"
};

var setting = {
    priceScale: 2,
    priceRoundType: "roundHalfUp",
    currencySign: "￥",
    currencyUnit: "元",
    uploadMaxSize: 10,
    uploadImageExtension: "jpg,jpeg,bmp,gif,png",
    uploadMediaExtension: "swf,flv,mp3,wav,avi,rm,rmvb",
    uploadFileExtension: "zip,rar,7z,doc,docx,xls,xlsx,ppt,pptx,pdf"
};

var messages = {
    "admin.message.success": "操作成功",
    "admin.message.error": "操作错误",
    "admin.dialog.ok": "确定",
    "admin.dialog.cancel": "取消",
    "admin.dialog.deleteConfirm": "您确定要删除吗？",
    "admin.dialog.clearConfirm": "您确定要清空吗？"
};

// 添加Cookie
function addCookie(name, value, options) {
    if (arguments.length > 1 && name != null) {
        if (options == null) {
            options = {};
        }
        if (value == null) {
            options.expires = -1;
        }
        if (typeof options.expires == "number") {
            var time = options.expires;
            var expires = options.expires = new Date();
            expires.setTime(expires.getTime() + time * 1000);
        }
        if (options.path == null) {
            options.path = "/";
        }
        if (options.domain == null) {
            options.domain = "";
        }
        document.cookie = encodeURIComponent(String(name)) + "=" + encodeURIComponent(String(value)) + (options.expires != null ? "; expires=" + options.expires.toUTCString() : "") + (options.path != "" ? "; path=" + options.path : "") + (options.domain != "" ? "; domain=" + options.domain : "") + (options.secure != null ? "; secure" : "");
    }
}

// 获取Cookie
function getCookie(name) {
    if (name != null) {
        var value = new RegExp("(?:^|; )" + encodeURIComponent(String(name)) + "=([^;]*)").exec(document.cookie);
        return value ? decodeURIComponent(value[1]) : null;
    }
}

// 移除Cookie
function removeCookie(name, options) {
    addCookie(name, null, options);
}

// Html转义
function escapeHtml(str) {
    return str.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/"/g, '&quot;');
}

// 字符串缩略
function abbreviate(str, width, ellipsis) {
    if ($.trim(str) == "" || width == null) {
        return str;
    }
    var i = 0;
    for (var strWidth = 0; i < str.length; i++) {
        strWidth = /^[\u4e00-\u9fa5\ufe30-\uffa0]$/.test(str.charAt(i)) ? strWidth + 2 : strWidth + 1;
        if (strWidth >= width) {
            break;
        }
    }
    return ellipsis != null && i < str.length - 1 ? str.substring(0, i) + ellipsis : str.substring(0, i);
}

// 货币格式化
function currency(value, showSign, showUnit) {
    if (value != null) {
        var price = (Math.round(value * Math.pow(10, 2)) / Math.pow(10, 2)).toFixed(2);
        if (showSign) {
            price = '￥' + price;
        }
        if (showUnit) {
            price += '元';
        }
        return price;
    }
}

// 多语言
function message(code) {
    if (code != null) {
        var content = messages[code] != null ? messages[code] : code;
        if (arguments.length == 1) {
            return content;
        } else {
            if ($.isArray(arguments[1])) {
                $.each(arguments[1], function(i, n) {
                    content = content.replace(new RegExp("\\{" + i + "\\}", "g"), n);
                });
                return content;
            } else {
                $.each(Array.prototype.slice.apply(arguments).slice(1), function(i, n) {
                    content = content.replace(new RegExp("\\{" + i + "\\}", "g"), n);
                });
                return content;
            }
        }
    }
}



(function($) {

    var zIndex = 100;

    // 消息框
    var $message;
    var messageTimer;
    $.message = function() {
        var message = {};
        if ($.isPlainObject(arguments[0])) {
            message = arguments[0];
        } else if (typeof arguments[0] === "string" && typeof arguments[1] === "string") {
            message.type = arguments[0];
            message.content = arguments[1];
        } else {
            return false;
        }

        if (message.type == null || message.content == null) {
            return false;
        }

        if ($message == null) {
            $message = $('<div class="xxMessage"><div class="messageContent message' + escapeHtml(message.type) + 'Icon"><\/div><\/div>');
            if (!window.XMLHttpRequest) {
                $message.append('<iframe class="messageIframe"><\/iframe>');
            }
            $message.appendTo("body");
        }

        $message.children("div").removeClass("messagewarnIcon messageerrorIcon messagesuccessIcon").addClass("message" + message.type + "Icon").html(message.content);
        $message.css({"margin-left": - parseInt($message.outerWidth() / 2), "z-index": zIndex ++}).show();

        clearTimeout(messageTimer);
        messageTimer = setTimeout(function() {
            $message.hide();
        }, 3000);
        return $message;
    };



    $.fn.extend({
        // 文件上传
        uploader: function(options) {
            var settings = {
                url: '/admin/file/upload.jhtml',
                fileType: "image",
                fileName: "file",
                data: {},
                maxSize: 10,
                extensions: null,
                before: null,
                complete: null
            };
            $.extend(settings, options);

            if (settings.extensions == null) {
                switch(settings.fileType) {
                    case "media":
                        settings.extensions = 'swf,flv,mp3,wav,avi,rm,rmvb';
                        break;
                    case "file":
                        settings.extensions = 'zip,rar,7z,doc,docx,xls,xlsx,ppt,pptx,pdf';
                        break;
                    default:
                        settings.extensions = 'jpg,jpeg,bmp,gif,png';
                }
            }

            var $progressBar = $('<div class="progressBar"><\/div>').appendTo("body");
            return this.each(function() {
                var element = this;
                var $element = $(element);

                var webUploader = WebUploader.create({
                    swf: '/resources/admin1.0/flash/webuploader.swf',
                    server: settings.url + (settings.url.indexOf('?') < 0 ? '?' : '&') + 'fileType=' + settings.fileType + '&token=' + getCookie("token"),
                    pick: {
                        id: element,
                        multiple: false
                    },
                    fileVal: settings.fileName,
                    formData: settings.data,
                    fileSingleSizeLimit: settings.maxSize * 1024 * 1024,
                    accept: {
                        extensions: settings.extensions
                    },
                    fileNumLimit: 1,
                    auto: true
                }).on('beforeFileQueued', function(file) {
                    if ($.isFunction(settings.before) && settings.before.call(element, file) === false) {
                        return false;
                    }
                    if ($.trim(settings.extensions) == '') {
                        this.trigger('error', 'Q_TYPE_DENIED');
                        return false;
                    }
                    this.reset();
                    $progressBar.show();
                }).on('uploadProgress', function(file, percentage) {
                    $progressBar.width(percentage * 100 + '%');
                }).on('uploadAccept', function(file, data) {
                    $progressBar.fadeOut("slow", function() {
                        $progressBar.width(0);
                    });
                    if (data.message.type != 'success') {
                        $.message(data.message);
                        return false;
                    }
                    $element.prev("input:text").val(data.url);
                    if ($.isFunction(settings.complete)) {
                        settings.complete.call(element, file, data);
                    }
                }).on('error', function(type) {
                    switch(type) {
                        case "F_EXCEED_SIZE":
                            $.message("warn", "上传文件大小超出限制");
                            break;
                        case "Q_TYPE_DENIED":
                            $.message("warn", "上传文件格式不正确");
                            break;
                        default:
                            $.message("warn", "上传文件出现错误");
                    }
                });

                $element.mouseover(function() {
                    webUploader.refresh();
                });
            });
        }

    });

})(jQuery);

$().ready(function() {

    // AJAX全局设置
    $.ajaxSetup({
        traditional: true
    });

    // 令牌
    $(document).ajaxSend(function(event, request, settings) {
        if (!settings.crossDomain && settings.type != null && settings.type.toLowerCase() == "post") {
            var token = getCookie("token");
            if (token != null) {
                request.setRequestHeader("token", token);
            }
        }
    });

    // 令牌
    $("form").submit(function() {
        var $this = $(this);
        if ($this.attr("method") != null && $this.attr("method").toLowerCase() == "post" && $this.find("input[name='token']").size() == 0) {
            var token = getCookie("token");
            if (token != null) {
                $this.append('<input type="hidden" name="token" value="' + token + '" \/>');
            }
        }
    });

    // 状态
    $(document).ajaxComplete(function(event, request, settings) {
        var tokenStatus = request.getResponseHeader("tokenStatus");
        var validateStatus = request.getResponseHeader("validateStatus");
        var loginStatus = request.getResponseHeader("loginStatus");
        if (tokenStatus == "accessDenied") {
            var token = getCookie("token");
            if (token != null) {
                $.extend(settings, {
                    global: false,
                    headers: {token: token}
                });
                $.ajax(settings);
            }
        } else if (validateStatus == "accessDenied") {
            $.message("warn", "非法字符");
        } else if (loginStatus == "accessDenied") {
            $.message("warn", "登录超时，请重新登录");
            setTimeout(function() {
                location.reload(true);
            }, 2000);
        } else if (loginStatus == "unauthorized") {
            $.message("warn", "对不起，您无此操作权限！");
        }
    });

    //客户与供应列表与搜索编辑点击事件
    //编辑显示隐藏
    $("body").delegate('.customer-operate','click',function (e) {
        var $item = $(this).next('.operate-list');
        if($.trim($item.html()) == ''){
            $item.hide().parents('.customer-li').siblings().find('.operate-list').hide();
            $.message("warn", "无权限！");
            return false;
        }else if($item.is(':visible')){
            $item.hide();
        }else{
            $item.show().parents('.customer-li').siblings().find('.operate-list').hide();
        }
        e.stopPropagation();
    });
    //点击全局环境隐藏
    $(document).on('click',function () {
        $('.operate-list').hide();
    });


});








