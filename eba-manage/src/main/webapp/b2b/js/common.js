var zb = {
	base: "",
	locale: "zh_CN",
	theme: "default",
	setting: setting,
	messages: messages,
	xmrDomain: "http://192.168.88.141:8080/",
	listSize:8
};

/**  
 * StringBuffer Class, to join two string is the most use  
 */  
function StringBuffer()   
{   
    this._strings = [];   
    if(arguments.length==1)   
    {   
        this._strings.push(arguments[0]);   
    }   
}   
  
StringBuffer.prototype.append = function(str)   
{   
    this._strings.push(str);   
    return this;   
}   
  
StringBuffer.prototype.toString = function()   
{   
    return this._strings.join("");   
}   
  
/* 返回长度 */  
StringBuffer.prototype.length = function()   
{   
    var str = this._strings.join("");   
    return str.length;   
}   
  
/* 删除后几位字符 */  
StringBuffer.prototype.del = function(num)   
{   
    var len = this.length();   
    var str = this.toString();   
    str     = str.slice(0,len-num);   
    this._strings = [];   
    this._strings.push(str);   
}  


var setting = {
	priceScale: 2,
	priceRoundType: "roundHalfUp",
	currencySign: "￥",
	currencyUnit: "元",
	uploadMaxSize: 10,
	uploadImageExtension: "jpg,jpeg,bmp,gif,png",
	uploadMediaExtension: "swf,flv,mp3,wav,avi,rm,rmvb",
	uploadFileExtension: "zip,rar,7z,doc,docx,xls,xlsx,ppt,pptx"
};

var messages = {
	"shop.message.success": "操作成功",
	"shop.message.error": "操作错误",
	"shop.dialog.ok": "确 定",
	"shop.dialog.cancel": "取 消",
	"shop.dialog.deleteConfirm": "您确定要删除吗？",
	"shop.dialog.clearConfirm": "您确定要清空吗？"
};

/**
 * 工具类
 */
zb.util = {
	cookie: {
		addCookie: addCookie,
		getCookie: getCookie,
		removeCookie: removeCookie
	},
	format: {
		escapeHtml: escapeHtml,
		abbreviate: abbreviate,
		currency: currency
	},
	plat:{
		isPC: isPC
	}
};

var LocalStorage = {
	    set: function (key, value) {
	        localStorage.setItem(key, value);
	    },
	    get: function (key) {
	        return localStorage.getItem(key);
	    },
	    remove: function (key) {
	        return localStorage.removeItem(key);
	    },
	    update: function (key, value) {
	        localStorage.removeItem(key);
	        localStorage.setItem(key, value);
	    },
	    clear: function () {
	        return localStorage.clear();
	    }
};

var SessionCache = {
	    set: function (key, value) {
	        sessionStorage.setItem(key, value);
	    },
	    get: function (key) {
	        return sessionStorage.getItem(key);
	    },
	    remove: function (key) {
	        return sessionStorage.removeItem(key);
	    },
	    update: function (key, value) {
	        sessionStorage.removeItem(key);
	        sessionStorage.setItem(key, value);
	    },
	    clear: function () {
	        return sessionStorage.clear();
	    }
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
//		alert(encodeURIComponent(String(name)) + "=" + encodeURIComponent(String(value)) + (options.expires != null ? "; expires=" + options.expires.toUTCString() : "") + (options.path != "" ? "; path=" + options.path : "") + (options.domain != "" ? "; domain=" + options.domain : "") + (options.secure != null ? "; secure" : ""));
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

function isPC() {
    var userAgentInfo = navigator.userAgent;
    var Agents = ["Android", "iPhone",
                "SymbianOS", "Windows Phone",
                "iPad", "iPod"];
    var flag = true;
    for (var v = 0; v < Agents.length; v++) {
        if (userAgentInfo.indexOf(Agents[v]) > 0) {
            flag = false;
            break;
        }
    }
    return flag;
}

/**
 * 获取压缩图片
 * realPath  图片地址
 * type  "-m":中图  "-s":小图
 */
function getCustomPic(realPath,type){
	if(!realPath){
		return;
	}
	var index=realPath.lastIndexOf(".");
	var sPath=realPath.substring(0,index);
	var ePath=realPath.substring(index+1);
	var path=sPath+type+"."+ePath;
	return path;
}

//-------------------------工具类end


/**
 * event
 */
var events = {
	touchEvents: {
		touchstart: "touchstart",
        touchmove: "touchmove",
        touchend: "touchend",
        /**
         * @desc:判断是否pc设备，若是pc，需要更改touch事件为鼠标事件，否则默认触摸事件
         */
        initTouchEvents: function () {
            if (zb.util.plat.isPC()) {
                this.touchstart = "mousedown";
                this.touchmove = "mousemove";
                this.touchend = "mouseup";
            }
        }
	}
};

zb.e = events;
zb.e.touchEvents.initTouchEvents();

//---------------------------------------------
function isWeiXin(){
    var ua = window.navigator.userAgent.toLowerCase();
    if(ua.match(/MicroMessenger/i) == 'micromessenger'){
        return true;
    }else{
        return false;
    }
}
function to(url){
	window.location.href=url;
}
/**
 * default load
 */
var isFirst = false;
var isIndex = false;
var isdefalt = false;
$().ready(function() {
	
	if(isWeiXin()){
	}else{
		var url = "nosupport.html";
		//to(url);
	}

	var postion = SessionCache.get("dwPostion");
	if(!postion ){
		if(!isdefalt){
			//getLocation();
		}
		
	}
	$(".r1").click(function(event){
		if($("#han").is(":hidden")){
			$("#han").show();
		}else{
			$("#han").hide();
		}
		event.stopPropagation();
	});
	$(document).click(function(){ 
	 	$("#han").hide();
	 });
	$(".zTop a").click(function (){
		//self.location=document.referrer;
		history.back(); 
	});
	
	var objLi = $(".alt ul li");
	if(objLi){
		var url = "";
		if(objLi.length == 4){
			$(".alt ul li:eq(0)").click(function (){
				url = "index.html";
				to(url);
			});
			$(".alt ul li:eq(1)").click(function (){
				url = "cart.html";
				to(url);
			});
			$(".alt ul li:eq(2)").click(function (){
				url = "search.html";
				to(url);
			});
			$(".alt ul li:eq(3)").click(function (){
				toPersonOnly();
			});
		}
	}
	
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
	
//	$(document).ajaxError(function(event,xhr,options,exc){
//	     //console.log(event);
//	});
	
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
	
	$.MsgBox = {
	        Alert: function (msg) {
	           GenerateHtml(msg);
	           btnOk(); 
	           btnNo();
	        },
	        Confirm: function (msg,func) {
	        	GenerateConfirmHtml(msg,func);
	            btnOk(func);
	            btnNo();
	        }
	    }
	    //生成Html
	    var GenerateHtml = function (msg) {
	    	$("#alert").hide();
	    	$("#alert").html('<div class="coverO"></div>' +
	    			'<div class="cover">' +
	    		    '<div class="popup">' + 
	    		            '<p class="hint">提示</p>' +
	    		            '<p class="content">'+msg+'</p>' +
	    		        '<div class="buttonL" id="bthOK">' +
	    		            '<botton class="confirms">确  定</botton>' +
	    		        '</div>' +
	    		    '</div>' +
	    		'</div>');
	        //必须先将_html添加到body，再设置Css样式
	        $("#alert").show();
	    }
	
		//生成Html
    	var GenerateConfirmHtml = function (msg,func) {
	    	$("#alert").hide();
	    	$("#alert").html('<div class="coverO"></div><div class="contentO"><div class="titleO">' +
				'提示</div><div class="promptScriptO">'+msg+'</div><div class="buttonO"><p class="left"  id="bthOK">确定</p>'+
					'<p class="right" id="btnNo">取消</p><div class="clearfix"></div></div></div>');
	        //必须先将_html添加到body，再设置Css样式
	        $("#alert").show();
    	}
	   
	    //确定按钮事件
	    var btnOk = function (callback) {
	        $("#bthOK").click(function () {
	        	$("#alert").hide();
	            if (typeof (callback) == 'function') {
	                callback();
	            }
	        });
	    }
	    //取消按钮事件
	    var btnNo = function () {
	        $("#btnNo").click(function () {
	        	$("#alert").hide();
	        });
	    }
	    
	    alertinfo = function(msg){
	    	$.MsgBox.Alert(msg);
	    }
	    alertBox = function (msg, func){
	    	$.MsgBox.Confirm(msg,func);
	    }

});

function toPersonOnly(){
	$.ajax({
		type: "POST",
		url:  "/mobile/member/getCurrentMember.jhtml",
		dataType: "json", //jsonp
		success: function(data,status){
			var result = JSON.parse(data);
			var member = result.member;
			if(!member.mobile){
				window.location.href = "login.html";
				return;
			}
			if(member.memberType == 0){
				window.location.href = "personc.html";
				return; 
			}
			window.location.href = "personb.html";
		},
		error:function(data,status){
			alertinfo("获取用户信息异常");
		}
	});
}

/**
 * 获取url中参数
 * zc
 */
UrlParm = function() { // url参数    
	   var data, index;    
	   (function init() {    
	     data = [];    
	     index = {};    
	     var u = window.location.search.substr(1);    
	     if (u != '') {    
	       var parms = decodeURIComponent(u).split('&');    
	       for (var i = 0, len = parms.length; i < len; i++) {    
	         if (parms[i] != '') {    
	           var p = parms[i].split("=");    
	           if (p.length == 1 || (p.length == 2 && p[1] == '')) {// p | p=    
	             data.push(['']);    
	             index[p[0]] = data.length - 1;    
	           } else if (typeof(p[0]) == 'undefined' || p[0] == '') { // =c | =    
	             data[0] = [p[1]];    
	           } else if (typeof(index[p[0]]) == 'undefined') { // c=aaa    
	             data.push([p[1]]);    
	             index[p[0]] = data.length - 1;    
	           } else {// c=aaa    
	             data[index[p[0]]].push(p[1]);    
	           }    
	         }    
	       }    
	     }    
	   })();    
	   return {    
	     // 获得参数,类似request.getParameter()    
	     parm : function(o) { // o: 参数名或者参数次序    
	       try {    
	         return (typeof(o) == 'number' ? data[o][0] : data[index[o]][0]);    
	       } catch (e) {    
	       }    
	     },    
	     //获得参数组, 类似request.getParameterValues()    
	     parmValues : function(o) { //  o: 参数名或者参数次序    
	       try {    
	         return (typeof(o) == 'number' ? data[o] : data[index[o]]);    
	       } catch (e) {}    
	     },    
	     //是否含有parmName参数    
	     hasParm : function(parmName) {    
	       return typeof(parmName) == 'string' ? typeof(index[parmName]) != 'undefined' : false;    
	     },    
	     // 获得参数Map ,类似request.getParameterMap()    
	     parmMap : function() {    
	       var map = {};    
	       try {    
	         for (var p in index) {  map[p] = data[index[p]];  }    
	       } catch (e) {}    
	       return map;    
	     }    
	   }    
	 }(); 
zb.url = UrlParm;
zb.a = {
	page: pageAnimation
}

function pageAnimation(){
	$(".ani").animsition({
	    inClass: 'fade-in-up',
	    outClass: 'fade-out-up',
	    inDuration: 1300,
	    outDuration: 800,
	    linkElement: '.animsition-link',
	    // e.g. linkElement: 'a:not([target="_blank"]):not([href^=#])'
	    loading: true,
	    loadingParentElement: 'body', //animsition wrapper element
	    loadingClass: 'animsition-loading',
	    loadingInner: '', // e.g '<img src="loading.svg" />'
	    timeout: false,
	    timeoutCountdown: 2000,
	    onLoadEvent: true,
	    browser: [ 'animation-duration', '-webkit-animation-duration'],
	    // "browser" option allows you to disable the "animsition" in case the css property in the array is not supported by your browser.
	    // The default setting is to disable the "animsition" in a browser that does not support "animation-duration".
	    overlay : false,
	    overlayClass : 'animsition-overlay-slide',
	    overlayParentElement : 'body',
	    transition: function(url){ window.location.href = url; }
	  });
}



//格式化手机号码
function formatMobile(mobile){
	if("" == mobile || mobile == null || mobile == "undefined"){
		return mobile;
	}else if(mobile.length == 11){
		return mobile.substring(0,3) + "******" + mobile.substring(8,mobile.length);
	}else{
		return mobile;
	}
}
function onlyNum() {
    if(!(event.keyCode==46)&&!(event.keyCode==8)&&!(event.keyCode==37)&&!(event.keyCode==39))
    if(!((event.keyCode>=48&&event.keyCode<=57)||(event.keyCode>=96&&event.keyCode<=105)))
    event.returnValue=false;
}
//判断是否是正整数
function IsNum(s)
{
    if(s!=null){
        var r,re;
       re = /^[1-9]+[0-9]*]*$/;
        r = s.match(re);
        return (r==s)?true:false;
    }
    return false;
}
function clearWait(){
	var rotary =  $(".rotary:visible");
	if(rotary){
		$(".rotary").hide();
	}
}

function ani(){
	$(".animsition").animsition({
	    inClass: 'zoom-in-sm',
	    outClass: 'zoom-out-sm',
	    inDuration: 800,
	    outDuration: 800,
	    linkElement: '.animsition-link',
	    // e.g. linkElement: 'a:not([target="_blank"]):not([href^=#])'
	    loading: true,
	    loadingParentElement: 'body', //animsition wrapper element
	    loadingClass: 'animsition-loading',
	    loadingInner: '', // e.g '<img src="loading.svg" />'
	    timeout: false,
	    timeoutCountdown: 5000,
	    onLoadEvent: true,
	    browser: [ 'animation-duration', '-webkit-animation-duration'],
	    // "browser" option allows you to disable the "animsition" in case the css property in the array is not supported by your browser.
	    // The default setting is to disable the "animsition" in a browser that does not support "animation-duration".
	    overlay : false,
	    overlayClass : 'animsition-overlay-slide',
	    overlayParentElement : 'body',
	    transition: function(url){ window.location.href = url; }
	 });
}

//!function(){
//	zb.util.cookie.addCookie("openId", "7e2d92798c4de93f7cd76fd8c597446c", null);
//	
////	zb.util.cookie.addCookie("openId", "o_Enesw5wqglWLApBYfAzEPv7Wg8", {expires: 24 * 60 * 60 , domain:"192.168.0.111:8088" , path:"/"});
//
//}();


