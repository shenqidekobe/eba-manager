package com.microBusiness.manage.util;

import java.util.HashMap;

import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;

import org.apache.commons.codec.digest.Md5Crypt;



/**
 * 实现微信支付用到的方法类
 * 
 * @author xhs
 * 
 */
public class PayUtil {


	/**
	 * 微信签名算法sign
	 * 
	 * @param characterEncoding
	 *            编码
	 * @param key
	 *            签名算法的秘钥
	 * @param parameters
	 *            签名算法的参数
	 * @return 签名sign字符串
	 */
	@SuppressWarnings("rawtypes")
	public static String getSign(String characterEncoding, String key, SortedMap<String, Object> parameters) {
		StringBuffer sb = new StringBuffer();
		Set es = parameters.entrySet();// 所有参与传参的参数按照accsii排序（升序）
		Iterator it = es.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String k = (String) entry.getKey();
			Object v = entry.getValue();
			if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
				sb.append(k + "=" + v + "&");
			}
		}
		sb.append("key=" + key);
		System.out.println("连接key字符串:" + sb.toString());
		String sign = MD5Util.MD5Encode(sb.toString(), characterEncoding).toUpperCase();
		return sign;
	}

	/**
	 * 随机字符串
	 * 
	 * @return 随机字符串
	 */
	public static String getNonceStr() {
		Random random = new Random();
		return MD5Util.MD5Encode(String.valueOf(random.nextInt(10000)), "GBK");
	}

	/**
	 * 得到统一下单接口返回的prepay_id参数值Map结果
	 * 
	 * @param url
	 *            接口url
	 * @param xmlParam
	 *            提交的xml
	 * @param characterEncoding
	 *            编码
	 * @return 统一下单接口返回的Map数值结果
	 */
	@SuppressWarnings("rawtypes")
	public static Map<String, String> getPrepayId(String url, String xmlParam, String characterEncoding) {
		String prepay_id = "";
		Map<String, String> returnMap = new HashMap<String, String>();
		try {
			String jsonStr = WebUtils.post(url, xmlParam);
			System.out.println("统一下单返回结果:" + jsonStr);
			if (jsonStr.indexOf("FAIL") != -1) {
				Map map = XmlUtil.doXMLParse(jsonStr);
				prepay_id = (String) map.get("prepay_id");
				String return_code = (String) map.get("return_code");
				String err_code_des = (String) map.get("err_code_des");
				returnMap.put("result", "fail");
				returnMap.put("prepay_id", prepay_id);
				returnMap.put("return_code", return_code);
				returnMap.put("err_code_des", err_code_des);
			} else {
				Map map = XmlUtil.doXMLParse(jsonStr);
				prepay_id = (String) map.get("prepay_id");
				returnMap.put("result", "success");
				returnMap.put("prepay_id", prepay_id);
				Object obj = map.get("code_url");
				if (obj != null) {
					returnMap.put("code_url", obj.toString());
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returnMap;
	}
	
	
	@SuppressWarnings("rawtypes")
	public static Map<String, String> pay2Customer(String url, String xmlParam, String characterEncoding, String p12Path) {
		String prepay_id = "";
		Map<String, String> returnMap = new HashMap<String, String>();
		try {
			WebUtils.setWeixinCertP12(p12Path);//加载证书
			String jsonStr = WebUtils.post(url, xmlParam);
			System.out.println("统一下单返回结果:" + jsonStr);
			if (jsonStr.indexOf("FAIL") != -1) {
				Map map = XmlUtil.doXMLParse(jsonStr);
				prepay_id = (String) map.get("prepay_id");
				String return_code = (String) map.get("return_code");
				String err_code_des = (String) map.get("err_code_des");
				returnMap.put("result", "fail");
				returnMap.put("prepay_id", prepay_id);
				returnMap.put("return_code", return_code);
				returnMap.put("err_code_des", err_code_des);
			} else {
				Map map = XmlUtil.doXMLParse(jsonStr);
				prepay_id = (String) map.get("prepay_id");
				returnMap.put("result", "success");
				returnMap.put("prepay_id", prepay_id);
				Object obj = map.get("code_url");
				if (obj != null) {
					returnMap.put("code_url", obj.toString());
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returnMap;
	}
	
	
	public static void main(String[] args) {
		String str = "appid=wxd2d5a5831e423617&body=xx&mch_id=1505283021&nonce_str=3c5be6328b&notify_url=http://shcxnc.com/api/small/payment/notifyFun.jhtml&openid=oi5qo5cocMFPerMyBdUm9LnwXXKM&out_trade_no=2018062114250010351&spbill_create_ip=101.224.10.206&total_fee=61600&trade_type=JSAPI&key=chunxiaonongchang123qazwsxedc321";
		String sign = MD5Util.MD5Encode(str, "UTF-8").toUpperCase();
		System.out.println(sign);
		//D59F7922D91EE31B0DDA50CD60558ECC
	}

}
