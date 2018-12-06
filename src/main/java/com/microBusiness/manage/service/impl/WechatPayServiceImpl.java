package com.microBusiness.manage.service.impl;


import java.util.Date;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.microBusiness.manage.service.WechatPayService;
import com.microBusiness.manage.util.DateUtil;
import com.microBusiness.manage.util.PayUtil;
import com.microBusiness.manage.util.PropertiesUtil;
import com.microBusiness.manage.util.SystemUtil;
import com.microBusiness.manage.util.XmlUtil;

/**
 * 支付接口实现类
 * 
 */
@Service
public class WechatPayServiceImpl implements WechatPayService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public Map<String, String> getPrepayIdResult(String openId, int total_fee, String body, String out_trade_no, String ip) {
		// TODO Auto-generated method stub
		// 得到统一下单的参数值
		String characterEncoding = "UTF-8";
		String key = PropertiesUtil.readValue("key");// 秘钥
		String appid = PropertiesUtil.readValue("appid");// 小程序账号ID
		String mch_id = PropertiesUtil.readValue("mch_id");// 商户号
		String nonce_str = PayUtil.getNonceStr();// 随机字符串
		String device_info = PropertiesUtil.readValue("device_info");// 终端设备号(门店号或收银设备ID)，注意：PC网页或公众号内支付请传"WEB"
		String spbill_create_ip = SystemUtil.getHostIp();// PropertiesUtil.readValue("spbill_create_ip");//
		// 交易结束时间
		String time_start = DateUtil.getCurrTime();// 交易起始时间
		String time_space = PropertiesUtil.readValue("time_space");// 交易结束时间间隔
		Long timeSpace = new Long(time_space);
		String time_expire = DateUtil.getAddTime(time_start, timeSpace);// 交易结束时间,time_expire时间过短，刷卡至少1分钟，其他5分钟
		String notify_url = PropertiesUtil.readValue("notify_url");// 通知地址
		String trade_type = PropertiesUtil.readValue("trade_type");// 交易类型
		SortedMap<String, Object> packageParams = new TreeMap<String, Object>();
		packageParams.put("appid", appid);
		packageParams.put("mch_id", mch_id);
		packageParams.put("nonce_str", nonce_str.substring(0, 10));
		packageParams.put("device_info", device_info);
		packageParams.put("body", body);
		packageParams.put("out_trade_no", out_trade_no);
		packageParams.put("total_fee", total_fee);
		packageParams.put("spbill_create_ip", spbill_create_ip);
		packageParams.put("time_start", time_start);
		packageParams.put("time_expire", time_expire);
		packageParams.put("notify_url", notify_url);
		packageParams.put("trade_type", trade_type);
		packageParams.put("openid", openId);
		
		// 得到签名
		String sign = PayUtil.getSign(characterEncoding, key, packageParams);
		packageParams.put("sign", sign);
		// 得到提交的数据
		String xmlParam = XmlUtil.getXml(packageParams);
		// 调用接口得到返回结果
		String url = PropertiesUtil.readValue("pay_interface");// 統一下单接口
		Map<String, String> prepay_idMap = PayUtil.getPrepayId(url, xmlParam, characterEncoding);
		// 返回结果
		return prepay_idMap;
	}

	@Override
	public Map<String, String> getCodeUrlResult(String openId, int total_fee, String body, String out_trade_no,
			String product_id, String spbill_create_ip) {
		// TODO Auto-generated method stub
		// 得到统一下单的参数值
		String characterEncoding = "UTF-8";
		String key = PropertiesUtil.readValue("key");// 秘钥
		String appid = PropertiesUtil.readValue("appid");// 公众账号ID
		String mch_id = PropertiesUtil.readValue("mch_id");// 商户号
		String nonce_str = PayUtil.getNonceStr();// 随机字符串
		// String sign = PropertiesUtil.readValue("sign");// 签名
		String device_info = PropertiesUtil.readValue("device_info");// 终端设备号(门店号或收银设备ID)，注意：PC网页或公众号内支付请传"WEB"
		// String body = PropertiesUtil.readValue("body");// 商品描述
		// String out_trade_no PropertiesUtil.readValue("out_trade_no");// 商户订单号
		// String total_fee = PropertiesUtil.readValue("total_fee");// 总金额
		// String spbill_create_ip =SystemUtil.getHostIp();//
		// PropertiesUtil.readValue("spbill_create_ip");// 终端IP
		// String time_start = PropertiesUtil.readValue("time_start");// 交易起始时间
		// String time_expire = PropertiesUtil.readValue("time_expire");//
		// 交易结束时间
		String time_start = DateUtil.getCurrTime();// 交易起始时间
		String time_space = PropertiesUtil.readValue("time_space");// 交易结束时间间隔
		Long timeSpace = new Long(time_space);
		String time_expire = DateUtil.getAddTime(time_start, timeSpace);// 交易结束时间,time_expire时间过短，刷卡至少1分钟，其他5分钟
		String notify_url = PropertiesUtil.readValue("notify_url");// 通知地址
		String trade_type = PropertiesUtil.readValue("trade_type_native");// 交易类型
		SortedMap<String, Object> packageParams = new TreeMap<String, Object>();
		packageParams.put("appid", appid);
		packageParams.put("mch_id", mch_id);
		packageParams.put("nonce_str", nonce_str);
		packageParams.put("device_info", device_info);
		packageParams.put("body", body);
		packageParams.put("out_trade_no", out_trade_no);
		packageParams.put("total_fee", total_fee);
		packageParams.put("spbill_create_ip", spbill_create_ip);
		packageParams.put("time_start", time_start);
		packageParams.put("time_expire", time_expire);
		packageParams.put("notify_url", notify_url);
		packageParams.put("trade_type", trade_type);
		packageParams.put("openid", openId);
		packageParams.put("product_id", product_id);

		// 得到签名
		String sign = PayUtil.getSign(characterEncoding, key, packageParams);
		packageParams.put("sign", sign);
		// 得到提交的数据
		String xmlParam = XmlUtil.getXml(packageParams);
		// 调用接口得到返回结果
		String url = PropertiesUtil.readValue("pay_interface");// 統一下单接口
		Map<String, String> prepay_idMap = PayUtil.getPrepayId(url, xmlParam, characterEncoding);
		// 返回结果
		return prepay_idMap;
	}

	@Override
	public Map<String, Object> getH5Params(String prepay_id) {
		// TODO Auto-generated method stub
		String characterEncoding = "UTF-8";
		String key = PropertiesUtil.readValue("key");// 秘钥
		String appId = PropertiesUtil.readValue("appid");// 公众账号ID
		Long timeStamp = DateUtil.getUnixTime(new Date());// 当前的时间戳
		String nonceStr = PayUtil.getNonceStr();// 随机字符串
		String packages = "prepay_id=" + prepay_id;// 统一下单接口返回的prepay_id参数值，提交格式如：prepay_id=***
		String signType = "MD5";
		SortedMap<String, Object> packageParams = new TreeMap<String, Object>();
		packageParams.put("appId", appId);
		packageParams.put("timeStamp", timeStamp.toString());
		packageParams.put("nonceStr", nonceStr);
		packageParams.put("package", packages);
		packageParams.put("signType", signType);
		// 得到签名
		String paySign = PayUtil.getSign(characterEncoding, key, packageParams);
		packageParams.put("paySign", paySign);

		//System.out.println(paySign);
		return packageParams;
	}

	@Override
	public Map<String, String> getAppPrepayIdResult(int total_fee, String body, String out_trade_no,
			String spbill_create_ip,String device_info) {
		// TODO Auto-generated method stub
		// 得到统一下单的参数值
		String characterEncoding = "UTF-8";
		String key = PropertiesUtil.readValue("app_key");// 秘钥
		String appid = PropertiesUtil.readValue("app_id");// 微信开放平台审核通过的应用APPID
		String mch_id = PropertiesUtil.readValue("app_mch_id");// 微信支付分配的商户号
		String nonce_str = PayUtil.getNonceStr();// 随机字符串
		String trade_type = PropertiesUtil.readValue("trade_type_app");// 交易类型
		
		// 交易结束时间
		String time_start = DateUtil.getCurrTime();// 交易起始时间
		String time_space = PropertiesUtil.readValue("time_space");// 交易结束时间间隔
		Long timeSpace = new Long(time_space);
		String time_expire = DateUtil.getAddTime(time_start, timeSpace);// 交易结束时间,time_expire时间过短，刷卡至少1分钟，其他5分钟
		String notify_url = PropertiesUtil.readValue("app_notify_url");// 通知地址
		SortedMap<String, Object> packageParams = new TreeMap<String, Object>();
		packageParams.put("appid", appid);
		packageParams.put("mch_id", mch_id);
		packageParams.put("nonce_str", nonce_str);
		packageParams.put("device_info", device_info);
		packageParams.put("body", body);
		packageParams.put("out_trade_no", out_trade_no);
		packageParams.put("total_fee", total_fee);
		packageParams.put("spbill_create_ip", spbill_create_ip);
		packageParams.put("time_start", time_start);
		packageParams.put("time_expire", time_expire);
		packageParams.put("notify_url", notify_url);
		packageParams.put("trade_type", trade_type);
		// 得到签名
		String sign = PayUtil.getSign(characterEncoding, key, packageParams);
		packageParams.put("sign", sign);
		// 得到提交的数据
		String xmlParam = XmlUtil.getXml(packageParams);
		// 调用接口得到返回结果
		String url = PropertiesUtil.readValue("pay_interface");// 統一下单接口
		Map<String, String> prepay_idMap = PayUtil.getPrepayId(url, xmlParam, characterEncoding);
		// 返回结果
		return prepay_idMap;
	}

	@Override
	public Map<String, Object> getAppParams(String prepay_id) {
		// TODO Auto-generated method stub
		String characterEncoding = "UTF-8";
		String key = PropertiesUtil.readValue("app_key");// 秘钥
		String appId = PropertiesUtil.readValue("app_id");// 微信开放平台审核通过的应用APPID
		String partner_id = PropertiesUtil.readValue("app_mch_id");// 微信支付分配的商户号
		Long timeStamp = DateUtil.getUnixTime(new Date());// 当前的时间戳
		String nonceStr = PayUtil.getNonceStr();// 随机字符串
		String packages = "Sign=WXPay";// 暂填写固定值Sign=WXPay
		SortedMap<String, Object> packageParams = new TreeMap<String, Object>();
		packageParams.put("appid", appId);
		packageParams.put("prepayid", prepay_id);
		packageParams.put("partnerid", partner_id);
		packageParams.put("timestamp", timeStamp.toString());
		packageParams.put("noncestr", nonceStr);
		
		packageParams.put("package", packages);
		//packageParams.put("extData", "app data");
		// 得到签名
		String paySign = PayUtil.getSign(characterEncoding, key, packageParams);
		packageParams.put("sign", paySign);

		System.out.println(paySign);
		return packageParams;
	}
	
	
	/**
	 * 企业付款到用户微信零钱
	 * @param openId
	 * @param total_fee
	 * @param body
	 * @param out_trade_no
	 * @param ip
	 * @return
	 */
	@Override
	public Map<String, String> pay2Customer(String openId, int amount, String desc, 
				String partner_trade_no, String ip, String check_name, String re_user_name) {
		// TODO Auto-generated method stub
		// 得到统一下单的参数值
		String characterEncoding = "UTF-8";
		String key = PropertiesUtil.readValue("key");// 秘钥
		String appid = PropertiesUtil.readValue("appid");// 小程序账号ID
		String mch_id = PropertiesUtil.readValue("mch_id");// 商户号
		String nonce_str = PayUtil.getNonceStr();// 随机字符串
		String device_info = PropertiesUtil.readValue("device_info");// 终端设备号(门店号或收银设备ID)，注意：PC网页或公众号内支付请传"WEB"
		String spbill_create_ip = SystemUtil.getHostIp();
		SortedMap<String, Object> packageParams = new TreeMap<String, Object>();
		packageParams.put("mch_appid", appid);
		packageParams.put("mchid", mch_id);
		packageParams.put("nonce_str", nonce_str.substring(0, 10));
		packageParams.put("device_info", device_info);
		packageParams.put("partner_trade_no", partner_trade_no);
		packageParams.put("amount", amount);
		packageParams.put("spbill_create_ip", spbill_create_ip);
		packageParams.put("check_name", "NO_CHECK");
		packageParams.put("re_user_name", re_user_name);
		packageParams.put("desc", desc);
		packageParams.put("openid", openId);
		
		// 得到签名
		String sign = PayUtil.getSign(characterEncoding, key, packageParams);
		packageParams.put("sign", sign);
		// 得到提交的数据
		String xmlParam = XmlUtil.getXml(packageParams);
		// 调用接口得到返回结果
		String url = PropertiesUtil.readValue("pay_transfers_url");// 企业付款给微信零钱接口
		
		String p12Path = PropertiesUtil.readValue("p12Path");// p12微信证书
		p12Path = "";
		Map<String, String> result = PayUtil.pay2Customer(url, xmlParam, characterEncoding, p12Path);
		// 返回结果
		return result;
	}
	
	public static void main(String[] args) {
		new WechatPayServiceImpl().pay2Customer("oi5qo5cocMFPerMyBdUm9LnwXXKM", 10, "测试", "123",
				"", "小明", "小明");
	}
}
