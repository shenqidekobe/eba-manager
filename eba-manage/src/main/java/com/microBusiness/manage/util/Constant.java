package com.microBusiness.manage.util;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class Constant {

    public static final int MODE_NORMAL = 0;
    public static final int MODE_DEBUG = 1;
    public static final String PREFIX = "/";
    public static final int NO_VALUE_ALTERNATIVE = -(1 << 24) - 1;


    //商品url前缀
    public static final String HTTP = "http://localhost:8080/goods/content/";
    //商品url后缀
    public static final String HTML = ".html";



    public static int getModeDebug() {
        return MODE_DEBUG;
    }

    public static int getModeNormal() {
        return MODE_NORMAL;
    }

    public static final int UNALARMED = 0;
    public static final int ALARMED = 1;

    public static final int STOCK_CHUQUAN = 0;
    public static final int STOCK_BEFORE_FUQUAN = 1;
    public static final int STOCK_AFTER_FUQUAN = 2;

    //短信

    public static final int SMS_BIND_PHONE = 1;//绑定手机
    public static final int SMS_MODIFY_PHONE = 2;//修改手机

    public static final String SMS_CODE = "shanlin_sms_code";//验证码缓存key
    public static final String SMS_PHONE = "shanlin_sms_phone";//手机号缓存key

    //订单状态
    public static final int ORDER_STATUS_PENDINGPAYMENT = 0;//待支付
    public static final int ORDER_STATUS_PENDINGREVIEW = 1;//已支付
    public static final int ORDER_STATUS_STOCKING = 2;//备货中
    public static final int ORDER_STATUS_PENDINGSHIPMENT = 3;//备货完成
    public static final int ORDER_STATUS_SHIPPED = 4;//配送中
    public static final int ORDER_STATUS_COMPLETED = 5;//已完成
    public static final int ORDER_STATUS_DELETED = 6;//已删除
    public static final int ORDER_STATUS_CANCELED_BUYER = 7;//已取消 买家
    public static final int ORDER_STATUS_CANCELED_SELLER = 8;//已取消 卖家
    public static final int ORDER_STATUS_CANCELED_TIMEOUT = 9;//已取消 超时

    //订单日志类型
    public static final int ORDERLOG_TYPE_CREATE = 0;//订单创建
    public static final int ORDERLOG_TYPE_PAYMENT = 1;//订单收款
    public static final int ORDERLOG_TYPE_STOCKING = 2;//备货中
    public static final int ORDERLOG_TYPE_STOCKED = 3;//备货完成
    public static final int ORDERLOG_TYPE_SHIPPING = 4;//订单配送中
    public static final int ORDERLOG_TYPE_COMPLETE = 5;//订单完成
    public static final int ORDERLOG_TYPE_DELETE = 6;//订单删除
    public static final int ORDERLOG_TYPE_CANCEL_BUYER = 7;//订单取消买家
    public static final int ORDERLOG_TYPE_CANCEL_SELLER = 8;//订单取消卖家
    public static final int ORDERLOG_TYPE_CANCEL_TIMEOUT = 9;//订单取消超时

//	//订单状态                        
//	public static final int ORDER_STATUS_PENDINGPAYMENT=0;//待支付
//	public static final int ORDER_STATUS_PENDINGREVIEW=1;//已支付
//	public static final int ORDER_STATUS_STOCKING=2;//备货中
//	public static final int ORDER_STATUS_PENDINGSHIPMENT=3;//备货完成
//	public static final int ORDER_STATUS_SHIPPED=4;//配送中
//	public static final int ORDER_STATUS_RECEIVED=5;//已收货
//	public static final int ORDER_STATUS_COMPLETED=6;//已完成
//	public static final int ORDER_STATUS_FAILED=7;//已失败
//	public static final int ORDER_STATUS_CANCELED=8;//已取消
//	public static final int ORDER_STATUS_DENIED=9;//已拒绝
//	public static final int ORDER_STATUS_DELETED=10;//已删除
//	//订单日志类型
//	public static final int ORDERLOG_TYPE_CREATE=0;//订单创建
//	public static final int ORDERLOG_TYPE_UPDATE=1;//订单更新
//	public static final int ORDERLOG_TYPE_CANCEL=2;//订单取消
//	public static final int ORDERLOG_TYPE_PAYMENT=3;//订单收款
//	public static final int ORDERLOG_TYPE_STOCKING=4;//备货中
//	public static final int ORDERLOG_TYPE_STOCKED=5;//备货完成
//	public static final int ORDERLOG_TYPE_REFUNDS=6;//订单退款
//	public static final int ORDERLOG_TYPE_SHIPPING=7;//订单配送中
//	public static final int ORDERLOG_TYPE_RETURNS=8;//订单退货
//	public static final int ORDERLOG_TYPE_RECEIVE=9;//订单收货
//	public static final int ORDERLOG_TYPE_COMPLETE=10;//订单完成
//	public static final int ORDERLOG_TYPE_FAIL=11;//订单失败
//	public static final int ORDERLOG_TYPE_DELETE=12;//订单删除
//	public static final int ORDERLOG_TYPE_CANCEL_TIMEOUT=13;//订单取消超时
//	public static final int ORDERLOG_TYPE_CANCEL_BUYER=14;//订单取消买家
//	public static final int ORDERLOG_TYPE_CANCEL_SELLER=15;//订单取消卖家

    public static final int SHIPPING_METHOD_SHIP = 1;//1：配送，2：自提
    public static final int SHIPPING_METHOD_OWN = 2;//1：配送，2：自提

    //微信配置
    //微信公众号的app_id，唯一
    //本地测试环境
    //public static final String APP_ID = "wx53472cc747411baf";
    //线上测试环境
    //public static final String APP_ID = "wxf76903378edbbf26";
    //正式环境
    public static final String APP_ID_OLD="";
    //微信公众号的secret，唯一
    //本地测试环境
    //public static final String SECRET = "5dbce27e2e7035ae6012ea698bcac001";
    //线上测试环境
    //public static final String SECRET = "a40e7c032136ea842f406af83dca529d";
    //正式环境
    public static final String SECRET_OLD="";
    
    
    //正式公众号账号
    public static final String APP_ID = "wxe8dde426b3876eb0";
    public static final String SECRET = "cf00d6d3bd2d34bd60d4d98a6ff5a7fb";
    
    
    //小程序密匙
    public static final String SMALL_APPID = "wxe8dde426b3876eb0";
    public static final String SMALL_SECRET = "cf00d6d3bd2d34bd60d4d98a6ff5a7fb";
    
    public static final String SMALL_TEMPLATE_ID = "C-c1RLZrb1RXsjCSsTd_L9mZ5l25WCd28aG9tj-qiL8";
    
    
    
    //江庆
    //public static final String SMALL_APPID = "wxa421555fba2cd68b";
    //public static final String SMALL_SECRET = "9ca2a9ad34738bfaf7b7e19699a57c55";
    
    // 消息通知，订单详情
    public static final String PAGE_PATH = "/pages/index/my/order-show/index?orderId=%d";
    
    //微信公众号获取code的返回类型，固定
    public static final String RESPONSE_TYPE = "code";
    //应用授权作用域,静默授权方式
    public static final String SCOPE_TYPE_BASE = "snsapi_base";
    //应用授权作用域，手动确认方式
    public static final String SCOPE_TYPE_USERINFO = "snsapi_userinfo";
    //可不写，重定向后会带上state参数，开发者可以填写a-zA-Z0-9的参数值
    public static final String STATE = "STATE";
    //必填，固定值
    public static final String GRANT_TYPE = "authorization_code";
    //
    public static final String ACCEPT = "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8";
    //
    public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:34.0) Gecko/20100101 Firefox/34.0";
    //
    public static final String CONNECTION = "keep-alive";
    //通过code获取access_token和openid的微信请求前缀
    public static final String GET_BY_CODE = "https://api.weixin.qq.com/sns/oauth2/access_token?";
    //获取code的微信请求前缀
    public static final String GET_CODE = "https://open.weixin.qq.com/connect/oauth2/authorize?";
    //从微信公众平台接口获取用户基本信息（网页授权）前缀
    public static final String GET_USERINFO_WEB = "https://api.weixin.qq.com/sns/userinfo?";
    //获取微信用户信息UnionID机制前缀
    public static final String GET_USERINFO_UNIONID = "https://api.weixin.qq.com/cgi-bin/user/info?";
    //获取access_token的微信请求前缀
    public static final String GET_ACCESS_TOKEN = "https://api.weixin.qq.com/cgi-bin/token?";
    //返回国家地区语言版本
    public static final String WEIXIN_LANG = "zh_CN";
    //定时任务间隔时间 （小时）
    public static final int TIMED_TASK_SPACE = 1;
    //获取全局access_token的微信请求前缀
    public static final String GET_GLOBAL_ACCESS_TOKEN = "https://api.weixin.qq.com/cgi-bin/token?";
    //全局
    public static final String GLOBAL_GRANT_TYPE = "client_credential";
    //截取网页url的参数map的key
    public static final String MAP_KEY = "code";
    //用户信息存入缓存的key值
    public static final String REDIS_CLIENT_KEY = "token";

    public static final String KEY_TOKEN = "accessToken";

    public static final String KEY_JSAPITICKET = "jsapiTicket";

    //首单减免是否开启
    public static final boolean IS_FIRST_ORDER_START = true;//首单减免是否开启
    //首单减免值
    public static final BigDecimal FIRST_REDUCE_VALUE = new BigDecimal(5);//首单减免5元
    public static final Byte ORDER_TYPE_FIRST = 0;//首单

    public static final int USERCOUPON_STATUS_UNUSED = 1;//用户优惠券状态：1 未使用
    public static final int USERCOUPON_STATUS_USED = 2;//用户优惠券状态：2 已使用
    public static final int USERCOUPON_STATUS_INVALID = 3;//3：已过期（可存可不存）

    //用户优惠券日志操作类（使用/退回/领取/赠送）
    public static final int COUPONLOG_OPERATORTYPE_USED = 0;//使用/退回/领取/赠送
    public static final int USERCOUPON_STATUS_RETURN = 1;//使用/退回/领取/赠送
    public static final int USERCOUPON_STATUS_RECEIVE = 2;//使用/退回/领取/赠送
    public static final int USERCOUPON_STATUS_GIVE = 3;//使用/退回/领取/赠送

    //优惠券类型
    public interface COUPON_TYPE {
        public static final Integer CONSUME = 2;//消费赠送
    }

    //积分类型
    public static final int POINTLOG_TYPE_USED = 0;//使用
    public static final int POINTLOG_TYPE_RETURN = 1;//退还
    public static final int POINTLOG_TYPE_GIFT = 2;//促销活动赠送
    public static final int POINTLOG_TYPE_PAID = 3;//订单应付金额赠送

    //选择商品范围
    public interface PRODUCT_SCOPE {
        public static final Integer ALL = 0;//全部商品
        public static final Integer PARTY = 1;//部分商品
    }


    //促销类型
    public interface SALE_TYPE {
        public static final Integer MONEY = 0;//减现金
        public static final Integer POINT = 1;//送积分
        public static final Integer COUPON = 2;//送优惠券
        public static final Integer SALE = 3;//打折
    }

    // 达达对接
    // 开发者中心申请的应用key
    public static final String DADA_APP_KEY = "dadaf3792889ecbea39c";
    // 授权作用域(固定值)
    public static final String SCOPE = "dada_base";
    // 测试地址
    public static final String SUPER_URL = "http://public.ga.qa.imdada.cn";
    // 获取grant_code的url
    public static final String GET_GRANT_CODE_URL = "/oauth/authorize/?";
    // 固定值，获取access_token填写authorization_code
    public static final String DADA_GRANT_TYPE = "authorization_code";

    // 固定值，刷新access_token填写refresh_token
    public static final String REFRESH_GRANT_TYPE = "refresh_token";

    // 获取access_token的url
    public static final String GET_ACCESS_TOKEN_URL = "/oauth/access_token/?";
    // 刷新token的url
    public static final String REFRESH_TOKEN_URL = "/oauth/refresh_token/?";
    // 常量字符串dada 为signature加密做准备
    public static final String DADA = "dada";
    // 添加订单地址
    public static final String ADD_ORDER_URL = "/v1_0/addOrder/?";
    // 取消订单地址(测试环境)
    public static final String CANCEL_ORDER_URL = "/v1_0/cancelOrder/?";
    // 追加订单地址
    public static final String NEW_ADD_ORDER_URL = "/v1_0/appointNewOrder/?";
    // 获取城市列表信息地址
    public static final String GET_CITY_INFO_URL = "/v1_0/getCity/?";
    // 获取订单状态信息地址
    public static final String GET_ORDER_STATUS_URL = "/v1_0/getOrderInfo/?";
    // 接受订单地址（只为测试用）
    public static final String DADA_ACCEPT_ORDER_URL = "/v1_0/acceptOrder/?";

    /**
     * 短信网关配置
     */
    public static final String SMS_SN = "SDK-WSS-010-09574";
    public static final String SMS_PWD = "70882c9@";

    public static final String COOKIE_OPEN_ID_NAME="openId";

    public static final String COOKIE_UNION_ID_NAME="unionId";
    /**
     * 供应商二维码地址
     */
    public static final String LOGIN_URL_PRE = "/api/member/login.jhtml?url=";
    public static final String BACK_URL_QRCODE = "/b2b/goodsList.html?needSupplierId=%d" ;
    //public static final String BACK_URL_FORMAL_QRCODE = "/b2b/goodsList.html?needSupplierId=%d" ;
    
    /**
     * 发货单二维码地址
     */
    public static final String INVOICE_URL_QRCODE = "/b2b/getGoods.html?shippingId=%d";


    /**
     *
     */

    public static final String SMALL_TEMPLATE_SEND_URL = "https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?access_token=%s" ;

    
    
    /**
     * 手机号规则前缀
     */
    public static final String tel_pre_num1 = "1";
    public static final String tel_pre_num2 = "2";
    
    public interface PAYMENT_PLUGIN {
		//微信
		public interface WENCHAT {
			public static final String PLUGIN_ID = "wenchatPaymentPlugin";
			public static final String PLUGIN_NAME = "微信支付";
		}
		//银联
		public interface UNIONPAY {
			public static final String PLUGIN_ID = "unionpayPaymentPlugin";
			public static final String PLUGIN_NAME = "银联支付";
		}
		//支付宝
		public interface ALIPAY {
			public static final String PLUGIN_ID = "alipayPaymentPlugin";
			public static final String PLUGIN_NAME = "支付宝支付";
		}
	}
    
    
    public interface ORDER_LOG_CONTENT {
		public static final String CREATE = "订单提交成功";
		public static final String PAYMENT = "订单已支付，等待商家发货";
		public static final String SHIPPING_COMMON = "商家已发货，等待顾客接收";
		public static final String SHIPPING_SERV = "商家已发货，等待服务点接收";
		public static final String RECEIVE_SERV = "服务点已收货，等待配送/自提";
		public static final String COMPLETE = "顾客已收货，订单已完成";
		public static final String CANCEL = "订单已取消";
	}
    
    
    
}
