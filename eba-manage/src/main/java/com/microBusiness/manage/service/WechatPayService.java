package com.microBusiness.manage.service;

import java.util.Map;

/**
 * 支付接口
 * 
 * @author xhs
 *
 */
public interface WechatPayService {

	// 得到统一下单prepay_id
	/**
	 * JSAPI方式得到统一下单prepay_id返回的结果
	 * 
	 * @param openId
	 *            用户openId
	 * @param total_fee
	 *            订单应支付的费用
	 * @param body
	 *            订单描述
	 * @param out_trade_no
	 *            订单号
	 * @return
	 */
	Map<String, String> getPrepayIdResult(String openId, int total_fee, String body, String out_trade_no, String ip);

	/**
	 * NATIVE方式 得到统一下单code_url返回的结果
	 * 
	 * @param openId
	 *            用户openId
	 * @param total_fee
	 *            订单应支付的费用
	 * @param body
	 *            订单描述
	 * @param out_trade_no
	 *            订单号
	 * @param product_id
	 *            trade_type=NATIVE，此参数必传。此id为二维码中包含的商品ID，商户自行定义。
	 * @param spbill_create_ip
	 *            APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP。
	 * @return
	 */
	public Map<String, String> getCodeUrlResult(String openId, int total_fee, String body, String out_trade_no,
			String product_id, String spbill_create_ip);

	/**
	 * 得到网页端调起支付API的参数
	 * 
	 * @param prepay_id
	 *            统一下单prepay_id
	 * @return 微信H5支付的map参数
	 */
	Map<String, Object> getH5Params(String prepay_id);

	/**
	 * APP方式 方式得到统一下单prepay_id返回的结果
	 * 
	 * @param total_fee
	 *            订单应支付的费用
	 * @param body
	 *            订单描述
	 * @param out_trade_no
	 *            订单号
	 * @param spbill_create_ip
	 *            App用户端实际ip
	 * @param device_info
	 *            终端设备号(门店号或收银设备ID)，注意：PC网页或公众号内支付请传"WEB"
	 * @return
	 */
	Map<String, String> getAppPrepayIdResult( int total_fee, String body, String out_trade_no,
			String spbill_create_ip,String device_info);
	
	/**
	 * 得到app调起支付API的参数
	 * @param prepay_id
	 * @return app支付的map参数
	 */
	Map<String, Object> getAppParams(String prepay_id) ;
	
	public Map<String, String> pay2Customer(String openId, int amount, String desc, 
			String partner_trade_no, String ip, String check_name, String re_user_name);
}
