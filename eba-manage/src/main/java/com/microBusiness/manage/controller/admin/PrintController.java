/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.controller.admin;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.microBusiness.manage.Message;
import com.microBusiness.manage.Setting;
import com.microBusiness.manage.entity.DeliveryCenter;
import com.microBusiness.manage.entity.DeliveryTemplate;
import com.microBusiness.manage.entity.Order;
import com.microBusiness.manage.entity.Shipping;
import com.microBusiness.manage.service.*;

import com.microBusiness.manage.entity.DeliveryTemplate;
import com.microBusiness.manage.service.OrderService;
import com.microBusiness.manage.util.ApiSmallUtils;
import com.microBusiness.manage.util.Constant;
import com.microBusiness.manage.util.QRCodeUtil;
import com.microBusiness.manage.util.SystemUtils;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.zxing.WriterException;

@Controller("adminPrintController")
@RequestMapping("/admin/print")
public class PrintController extends BaseController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name = "orderServiceImpl")
	private OrderService orderService;
	@Resource(name = "deliveryTemplateServiceImpl")
	private DeliveryTemplateService deliveryTemplateService;
	@Resource(name = "deliveryCenterServiceImpl")
	private DeliveryCenterService deliveryCenterService;

	@Resource
	private ShippingService shippingService ;

	@Resource
	private WeChatService weChatService;
	
	/*
	 * 打印订货单
	 */
	@RequestMapping(value = "/order", method = RequestMethod.GET)
	public String order(Long id, ModelMap model) {
		model.addAttribute("supplier", getCurrentSupplier());
		model.addAttribute("order", orderService.find(id));
		return "/admin/print/order";
	}
	
	/*
	 * 打印分销单
	 */
	@RequestMapping(value = "/distributionOrder", method = RequestMethod.GET)
	public String distributionOrder(Long id, ModelMap model) {
		model.addAttribute("order", orderService.find(id));
		return "/admin/print/order";
	}
	
	/*
	 * 打印采购单
	 */
	@RequestMapping(value = "/purchaseorder", method = RequestMethod.GET)
	public String purchaseorder(Long id,ModelMap model) {
		model.addAttribute("supplier", getCurrentSupplier());
		model.addAttribute("order", orderService.find(id));
		return "/admin/print/purchaseorder";
	}

	@RequestMapping(value = "/product", method = RequestMethod.GET)
	public String product(Long id, ModelMap model) {
		model.addAttribute("order", orderService.find(id));
		return "/admin/print/product";
	}

	@RequestMapping(value = "/shipping", method = RequestMethod.GET)
	public String shipping(Long id, ModelMap model) {

		Shipping shipping = shippingService.find(id) ;
		model.addAttribute("order", shipping.getOrder());
		model.addAttribute("shipping", shipping);

		return "/admin/print/shipping";
	}

	@RequestMapping(value = "/delivery", method = RequestMethod.GET)
	public String delivery(Long orderId, Long deliveryTemplateId, Long deliveryCenterId, ModelMap model) {
		DeliveryTemplate deliveryTemplate = deliveryTemplateService.find(deliveryTemplateId);
		DeliveryCenter deliveryCenter = deliveryCenterService.find(deliveryCenterId);
		if (deliveryTemplate == null) {
			deliveryTemplate = deliveryTemplateService.findDefault();
		}
		if (deliveryCenter == null) {
			deliveryCenter = deliveryCenterService.findDefault();
		}
		model.addAttribute("deliveryTemplates", deliveryTemplateService.findAll());
		model.addAttribute("deliveryCenters", deliveryCenterService.findAll());
		model.addAttribute("order", orderService.find(orderId));
		model.addAttribute("deliveryTemplate", deliveryTemplate);
		model.addAttribute("deliveryCenter", deliveryCenter);
		return "/admin/print/delivery";
	}
	
	/**
	 * 采购单发货信息打印
	 */
	@RequestMapping(value = "/deliveryInfor", method = RequestMethod.GET)
	public String deliveryInfor(Long id,String shippingId, ModelMap model) {
		model.addAttribute("order", orderService.find(id));
		model.addAttribute("shipping", shippingService.find(Long.parseLong(shippingId)));
		return "/admin/print/deliveryInfor";
	}
	
	/**
	 * 订货单发货信息打印
	 */
	@RequestMapping(value = "/orderShippingInfo", method = RequestMethod.GET)
	public String orderShippingInfo(Long id,String shippingId, ModelMap model) {
		model.addAttribute("order", orderService.find(id));
		model.addAttribute("supplier", getCurrentSupplier());
		model.addAttribute("shipping", shippingService.find(Long.parseLong(shippingId)));
		return "/admin/print/orderShippingInfo";
	}
	
	/**
	 * 判断订单发货信息是否为空
	 * @return
	 */
	@RequestMapping(value = "/verificationDeliveryInfo" , method = RequestMethod.POST)
	public @ResponseBody Message verificationOrderStart(Long[] orderIds) {
		if(orderIds == null) {
			return ERROR_MESSAGE;
		}
		for(int i=0 ; i<orderIds.length ; i++) {
			Order order = orderService.find(orderIds[i]);
			if(order.getShippings().size() == 0) {
				return Message.warn("请选择有发货信息的订单进行批量打印！");
			}
		}
		return SUCCESS_MESSAGE;
	}
	
	/**
	 * 订货单批量打印
	 * @param orderIds 订单id
	 * @return
	 */
	@RequestMapping(value = "/orderBatchPrint" , method = RequestMethod.GET)
	public String orderBatchPrint(Long[] orderIds , ModelMap model) {
		if(orderIds == null) {
			return ERROR_VIEW;
		}
		List<Order> orders = new ArrayList<>();
		for(int i= 0 ; i < orderIds.length ; i++) {
			Order order = orderService.find(orderIds[i]);
			orders.add(order);
		}
		model.addAttribute("supplier", getCurrentSupplier());
		model.addAttribute("orders", orders);
		return "/admin/print/orderBatchPrint";
	}
	
	
	/**
	 * 采购单批量打印
	 * @param orderIds
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/purchaseOrderBatchPrint" , method = RequestMethod.GET)
	public String purchaseOrderBatchPrint(Long[] orderIds , ModelMap model) {
		if(orderIds == null) {
			return ERROR_VIEW;
		}
		List<Order> orders = new ArrayList<>();
		for(int i= 0 ; i < orderIds.length ; i++) {
			Order order = orderService.find(orderIds[i]);
			orders.add(order);
		}
		model.addAttribute("supplier", getCurrentSupplier());
		model.addAttribute("orders", orders);
		return "/admin/print/purchaseOrderBatchPrint";
	}
	
	/**
	 * 发货单二维码
	 * @param request
	 * @param response
	 * @param shippingId 发货信息id
	 */
	@RequestMapping(value = "/getQRCode" , method = {RequestMethod.GET , RequestMethod.POST})
	public void getQRCode(HttpServletRequest request , HttpServletResponse response , Long shippingId) {
//		response.setHeader("Pragma", "no-cache");
//		response.setHeader("Cache-Control", "no-cache");
//		response.setHeader("Cache-Control", "no-store");
//		response.setDateHeader("Expires", 0);
//		response.setContentType("image/jpeg");
//		try {
//			Setting setting = SystemUtils.getSetting();
//			//生成图像
//			int width = 300; // 图像宽度
//			int height = 300; // 图像高度
//			String format = "jpg";// 图像类型
//			String url = getUrl(shippingId);
//			logger.info("发货单二维码  getQRCode()", url);
//
//			BufferedImage bufferedImage = QRCodeUtil.encode(width, height, format, url);
//			OutputStream stream = response.getOutputStream();
//			ImageIO.write(bufferedImage, format, stream);
//			stream.flush();
//			stream.close();
//		} catch (WriterException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Cache-Control", "no-store");
		response.setDateHeader("Expires", 0);
		response.setContentType("image/jpeg");

        CloseableHttpResponse httpResponse = null;
		try {
			String accessToken = weChatService.getSmallGlobalToken();

			httpResponse = ApiSmallUtils.getInputStream("page/takeGood/takeGood?shippingId="+shippingId, accessToken, request, response);

			InputStream inputStream = null;
			
			HttpEntity httpEntity = httpResponse.getEntity();
			if (httpEntity != null) {
				inputStream = httpEntity.getContent();
			}
			
			OutputStream stream = response.getOutputStream();
			ImageIO.write(ImageIO.read(inputStream), "jpg", stream);
			stream.flush();
			stream.close();
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally{
			try {
				httpResponse.close();
			} catch (IOException e) {
			}
		}
	}
	
	
	private String getUrl(Long shippingId) {
		Setting setting = SystemUtils.getSetting();
		String url = setting.getSiteUrl() + Constant.LOGIN_URL_PRE + String.format(Constant.INVOICE_URL_QRCODE,shippingId);
		return url;
	}

}