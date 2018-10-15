package com.microBusiness.manage.controller.api.small;

import java.io.BufferedReader;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.microBusiness.manage.controller.api.BaseController;
import com.microBusiness.manage.entity.JsonEntity;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.Order;
import com.microBusiness.manage.service.ChildMemberService;
import com.microBusiness.manage.service.LogService;
import com.microBusiness.manage.service.MemberService;
import com.microBusiness.manage.service.OrderService;
import com.microBusiness.manage.service.PaymentLogService;
import com.microBusiness.manage.service.PaymentService;
import com.microBusiness.manage.service.WechatPayService;
import com.microBusiness.manage.util.Code;
import com.microBusiness.manage.util.Constant.PAYMENT_PLUGIN;
import com.microBusiness.manage.util.DateUtils;
import com.microBusiness.manage.util.DateformatEnum;
import com.microBusiness.manage.util.IpUtil;
import com.microBusiness.manage.util.XmlUtils;

/**
 * 微信支付Controller
 * 
 * @author xhs
 *
 */
@Controller("wxpaymentController")
@RequestMapping("/api/small/payment")
public class WxPaymentController extends BaseController {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Resource
	private OrderService orderService;

	@Resource
	private MemberService memberService;

	@Resource
	private PaymentService paymentService;

	@Resource
	private PaymentLogService paymentLogService;

	@Resource
	private LogService logService;

	@Resource
	WechatPayService wechatPayService;
	
	@Resource
	private ChildMemberService childMemberService;


	/**
	 * 获取微信支付统一下单参数
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 */
	@RequestMapping(value = "/getWxPayParams")
	@ResponseBody
	public JsonEntity getWxPayParams(String smOpenId, String orderId, 
			HttpServletRequest request, HttpServletResponse response) {
		try {
			//setResponseUtf8(response);
			Map<String, Object> returnMap = new HashMap<String, Object>();
			Member member = childMemberService.findBySmOpenId(smOpenId).getMember();
			// 获取订单信息
			Order order = (orderId != null && !"".equals(orderId)) ? orderService.find(Long.valueOf(orderId))
					: null;

			// 订单存在的情况下发起支付流程
			if (order != null && order.getAmount() != null && order.getAmount().compareTo(BigDecimal.ZERO) > 0) {

				int amountInt = order.getAmount().scaleByPowerOfTen(2).intValueExact();
				int total_fee = amountInt;
				String date = DateUtils.formatDateToString(new Date(), DateformatEnum.HHmmss);
				String paySn = convertSn2PaySn(order.getSn(), date); // 将订单编号->支付订单编号
				String body = paySn;// "订单号："+orderBo.getSn()+"订单金额:"+orderBo.getAmount()+"已付金额:"+orderBo.getAmountPaid();//"商品或支付单简要描述";
				String out_trade_no = paySn;// "20151211091062_052636";
				String ip = request.getRemoteHost();//返回发出请求的IP地址
				
				// 将pay_sn记录到订单表中
				order.setPaySn(paySn);
				orderService.updatePaySn(member, order);
				
				//生成支付记录和日志
				
				

				// 得到统一下单prepay_id
				Map<String, String> prepayIdMap = wechatPayService.getPrepayIdResult(smOpenId, total_fee, body,
						out_trade_no, ip);
				String prepayIdResult = prepayIdMap.get("result");

				if ("success".equals(prepayIdResult)) {// 判断返回的结果
					String prepay_id = prepayIdMap.get("prepay_id");

					// 得到网页端调起支付API的参数
					returnMap = wechatPayService.getH5Params(prepay_id);

					

					return JsonEntity.successMessage(returnMap);
				} else {

					returnMap.put("prepay_id", returnMap.get("prepay_id"));
					returnMap.put("return_code", returnMap.get("return_code"));
					returnMap.put("err_code_des", returnMap.get("err_code_des"));
					
					return JsonEntity.error(Code.code_payment_error, returnMap.get("return_code"));
				}

			} else {
				return JsonEntity.error(Code.code8998, Code.code8998.getDesc());
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return JsonEntity.error(Code.code8999, Code.code8999.getDesc());
		}

	}

	

	/**
	 * 微信支付异步回调
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 */
	@RequestMapping(value = "/notifyFun")
	public void notifyFun(HttpServletRequest request, HttpServletResponse response) {
		try {
			System.out.println("notifyFun:");
			// 设置Response
			response.setContentType("text/xml; charset=UTF-8");
			response.setHeader("Cache-Control", "no-cache");
			response.setHeader("Pragma", "no-cache");
			response.setDateHeader("Expires", 0);

			// 获取用户信息
			String operator = null;// member.getNickname();//用户
			logger.info("[notifyFun()]: wechatUser=" + operator + "'s payment notifyFun start...");

			// 获取收到的报文
			BufferedReader reader = request.getReader();
			String line = "";
			StringBuffer inputString = new StringBuffer();
			while ((line = reader.readLine()) != null) {
				inputString.append(line);
			}
			request.getReader().close();
			Map<String, String> map = XmlUtils.doXMLParse(inputString.toString());
			logger.info("[notifyFun()]: receive inputString=" + inputString.toString());

			// 支付完成后订单的处理
			if (map.get("result_code").equalsIgnoreCase("SUCCESS")) {
				String out_trade_no = map.get("out_trade_no");// 商户订单号
				String transaction_id = map.get("transaction_id");// 微信支付单号
				String total_fee = map.get("total_fee");// 总金额，单位为分
				String cash_fee = map.get("cash_fee");// 支付金额，单位为分
				String sn = convertPaySn2Sn(out_trade_no);// 将支付订单编号->订单编号

				logger.info("[notifyFun()]: receive out_trade_no=" + out_trade_no + ",sn=" + sn + ",transaction_id="
						+ transaction_id + ",total_fee=" + total_fee + ",cash_fee=" + cash_fee);

				// 防止超时回调操作数据库多次
				if (!paymentService.isExistTransactionId(transaction_id)) {
					int cashFee = (cash_fee != null && !"".equals(cash_fee)) ? Integer.valueOf(cash_fee) : 0;
					int totalFee = (total_fee != null && !"".equals(total_fee)) ? Integer.valueOf(total_fee) : 0;

					BigDecimal amountPaid = new BigDecimal((cashFee)).divide(new BigDecimal(100));// 已付金额,转化成元
					BigDecimal amountTotal = new BigDecimal((totalFee)).divide(new BigDecimal(100));// 总金额,转化成元

					Order order = orderService.findBySn(sn);
					
					logger.info("order status : " + order.getStatus());
					
					if(order.getStatus().equals(Order.Status.pendingReview)){
						order.setStatus(Order.Status.pendingPayment);
					}

					if (order != null && amountTotal.compareTo(order.getAmount()) >= 0 && !order.hasExpired()
							&& Order.Status.pendingPayment.equals(order.getStatus())) {
						Member member = order.getMember();
						if (member != null) {
							operator = member.getNickname();
						}

						//更改订单状态，更改支付信息，计算返利
						orderService.payment(order, amountPaid, BigDecimal.ZERO, transaction_id, operator, map);

						// 记录唯一值，微信支付单号
						request.getSession().setAttribute("transaction_id", transaction_id);

					}

					// 告诉微信服务器，不要在调用回调，告诉微信服务器，收到信息了，不要在调用回调action了
					response.getWriter().write(XmlUtils.setXML("SUCCESS", "OK"));

				} else {
					// 记录操作日志
					logService.createLog("订单支付", operator, "订单支付失败",
							"订单支付失败，返回转换成json的数据：" + new Gson().toJson(map).toString(), IpUtil.getHostIp());

					response.getWriter().write(XmlUtils.setXML("FAIL", "报文为空"));
				}
			}

			logger.info("[notifyFun()]: wechatUser=" + operator + "'s payment 微信支付回调接口完成 finish...");

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}
	
	
	
	
	/**
	 * 将订单编号->支付订单编号
	 * @param sn
	 * @return
	 */
	public String convertSn2PaySn(String sn, String date){
		return sn + "" + date;
	}
	
	/**
	 * 将支付订单编号->订单编号
	 * @param paySn
	 * @return
	 */
	public String convertPaySn2Sn(String paySn){		
		return paySn.substring(0,paySn.length()-6);
	}

}