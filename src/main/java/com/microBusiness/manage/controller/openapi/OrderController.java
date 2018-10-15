package com.microBusiness.manage.controller.openapi;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.microBusiness.manage.entity.Admin;
import com.microBusiness.manage.entity.Order;
import com.microBusiness.manage.entity.OrderFile;
import com.microBusiness.manage.entity.OrderItem;
import com.microBusiness.manage.entity.OrderRemarks;
import com.microBusiness.manage.entity.OutApiJsonEntity;
import com.microBusiness.manage.entity.Product;
import com.microBusiness.manage.entity.RemarksFile;
import com.microBusiness.manage.entity.Shipping;
import com.microBusiness.manage.entity.Shipping.DeliverType;
import com.microBusiness.manage.entity.ShippingItem;
import com.microBusiness.manage.entity.SpecificationValue;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.service.OrderService;
import com.microBusiness.manage.util.Code;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by afei.
 * User: mingbai
 * Date: 2018/1/30 下午2:18
 * Describe:
 * Update:
 */
@Controller("openApiOrderController")
@RequestMapping("/openapi/order")
public class OrderController extends BaseController {

	private final static Logger LOGGER = LoggerFactory.getLogger(OrderController.class);
	
	@Resource(name = "orderServiceImpl")
	private OrderService orderService;
	
    @RequestMapping(value = "/get" , method = RequestMethod.GET)
    @ResponseBody
    public OutApiJsonEntity get(Long startDate , Long endDate, HttpServletRequest request , HttpServletResponse response){
    	if (startDate == null) {
    		return OutApiJsonEntity.error(Code.code100004);
		}
//    	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
    	
    	Date staDate = null;
    	Date enDate = null;
		if(startDate != null) {
//			staDate = simpleDateFormat.parse(String.valueOf(new Date(startDate)));
			staDate = new Date(startDate);
		}
		if(endDate != null) {
//			enDate = simpleDateFormat.parse(String.valueOf(new Date(startDate)));
			enDate = new Date(endDate);
		}
        
        Supplier supplier = (Supplier) request.getAttribute("supplier");
 		List<Order> order = orderService.find(supplier, staDate, enDate);
 		List<Map<String, Object>> lis = new ArrayList<Map<String,Object>>();
 		for (Order en : order) {
 			Map map = new HashMap<>();
 			Map orderMap = new HashMap<String, String>();
 			orderMap.put("sn", en.getSn());
 			orderMap.put("amount", en.getAmount());
 			orderMap.put("customerName", en.getNeed().getName());
 			orderMap.put("shoppingCustomer", en.getConsignee());
 			orderMap.put("receivingAddress",en.getAreaName()+en.getAddress());
 			orderMap.put("status", en.getStatus());
 			orderMap.put("cusNo", en.getNeed().getClientNum());
 			orderMap.put("phone", en.getPhone());
 			if (en.getChildMember() != null) {
 				orderMap.put("nickName", en.getChildMember().getNickName());
			}else {
				orderMap.put("nickName", "");
			}
 			orderMap.put("createDate", en.getCreateDate());
 			
 			List<Map<String, Object>> shippingList = new ArrayList<Map<String,Object>>();
 			for (Shipping shipping: en.getShippings()) {
 				Map shippingMap = new HashMap<String, String>();
 				shippingMap.put("consignee", shipping.getConsignee());
 				shippingMap.put("phone", shipping.getPhone());
 				shippingMap.put("deliveryDate", shipping.getCreateDate());
 				shippingMap.put("address", shipping.getArea()+shipping.getAddress());
 				shippingMap.put("deliveryCode", shipping.getDeliveryCode());
 				
 				shippingList.add(shippingMap);
			}
 			orderMap.put("shipping", shippingList);
 			
 			// 备注
 			List<Map<String, Object>> remarkList = new ArrayList<Map<String,Object>>();
 			for (OrderRemarks orderRemarks : en.getOrderRemarks()) {
 				Map remarkMap = new HashMap<String, String>();
 				remarkMap.put("description", orderRemarks.getDescription());
 				
 				List<Map<String, Object>> remarkFileList = new ArrayList<Map<String,Object>>();
 				for (RemarksFile remarksFile : orderRemarks.getAnnex()) {
 					Map remarkFileMap = new HashMap<String, String>();
 					remarkFileMap.put("url", remarksFile.getUrl());
 					remarkFileMap.put("name", remarksFile.getFileName());
 					
 					remarkFileList.add(remarkFileMap);
				}
 				
 				remarkMap.put("remarkFileList", remarkFileList);
 				
 				remarkList.add(remarkMap);
			}
 			orderMap.put("remark", remarkList);
 			
 			List<OrderItem> orderitems = en.getOrderItems();
 			List<Map<String, Object>> goodsList = new ArrayList<Map<String,Object>>();
 			for (OrderItem orderItem : orderitems) {
 				Map mapG = new HashMap<String, String>();
 				mapG.put("sn", orderItem.getSn());
 				mapG.put("name", orderItem.getName());
 				mapG.put("unit", "");
 				
 				String str = "";
 				List<SpecificationValue> svl = orderItem.getProduct().getSpecificationValues();
 				for (SpecificationValue specificationValue : svl) {
 					str += specificationValue.getValue()+";";
				}
 				
 				mapG.put("product", str);
 				mapG.put("price", orderItem.getPrice());
 				mapG.put("quantity", orderItem.getQuantity());
 				mapG.put("shippedQuantity", orderItem.getShippedQuantity());
 				mapG.put("realProductQuantity", en.getRealProductQuantity(orderItem.getProduct().getId()));
 				goodsList.add(mapG);
 			}
 			
// 			List<Map<String, Object>> goodsRecord = new ArrayList<Map<String,Object>>();
// 			List<OrderItemLog> orderLogs = en.getOrderItemLogs();
// 			for (OrderItemLog orderLog : orderLogs) {
// 				Map mapLog = new HashMap<String, String>();
// 				mapLog.put("operatorName", orderLog.getOperatorName());
// 				mapLog.put("type", orderLog.getType());
// 				mapLog.put("createDate", orderLog.getCreateDate());
// 				List<OrderItemInfo> orderItemInfos = orderLog.getOrderItemInfos();
// 				List<Map<String, Object>> orderItemInfoMaps = new ArrayList<Map<String,Object>>();
// 				for (OrderItemInfo orderItemInfo : orderItemInfos) {
// 					Map orderItemInfoMap = new HashMap<String, String>();
// 					orderItemInfoMap.put("sn", orderItemInfo.getProduct().getSn());
// 					orderItemInfoMap.put("name", orderItemInfo.getProduct().getGoods().getName());
// 					orderItemInfoMap.put("product", orderItemInfo.getProduct().getSpecificationValues());
// 					orderItemInfoMap.put("unit","");
// 					orderItemInfoMap.put("beforeQuantity", orderItemInfo.getBeforeQuantity());
// 					orderItemInfoMap.put("afterQuantity", orderItemInfo.getAfterQuantity());
// 					orderItemInfoMaps.add(orderItemInfoMap);
// 				}
// 				mapLog.put("orderItemInfos", orderItemInfoMaps);
// 				
// 				goodsRecord.add(mapLog);
// 			}
 			
 			map.put("orderDetail", orderMap);
 			map.put("orderItemInfos", goodsList);
 			lis.add(map);
 		}
 		
 		return OutApiJsonEntity.successMessage(lis);
    }
   
    @RequestMapping(value = "/send" , method = RequestMethod.POST)
    @ResponseBody
    public OutApiJsonEntity send(Shipping shipping, String sn, HttpServletRequest request , HttpServletResponse response){
    	
    	Order order = orderService.findBySn(sn);
    	if (order == null) {
			return OutApiJsonEntity.error(Code.code8323);
		}
		if (order.getShippableQuantity() <= 0) {
			return OutApiJsonEntity.error(Code.code200002);
		}
		if (!StringUtils.isNotEmpty(shipping.getDeliveryCorp())) {
			return OutApiJsonEntity.error(Code.code200003);
		}
		
		// 收货人如果为空，则选用下单时的收货人
		if (!StringUtils.isNotEmpty(shipping.getConsignee())) {
			shipping.setConsignee(order.getConsignee());
		}
		
		// 收货人电话如果为空，则选用下单时的电话
		if (!StringUtils.isNotEmpty(shipping.getPhone())) {
			shipping.setPhone(order.getPhone());
		}
		
		// 省市区如果为空，则选用则选用下单时选择的省市区
		if (!StringUtils.isNotEmpty(shipping.getArea())) {
			shipping.setArea(order.getArea());
		}
		
		// 详细地址如果为空，选用下单是使用的详细地址
		if (!StringUtils.isNotEmpty(shipping.getAddress())) {
			shipping.setAddress(order.getAddress());
		}
		
		boolean isDelivery = false;
		for (Iterator<ShippingItem> iterator = shipping.getShippingItems().iterator(); iterator.hasNext();) {
			ShippingItem shippingItem = iterator.next();
			if (shippingItem == null || StringUtils.isEmpty(shippingItem.getSn()) || shippingItem.getQuantity() == null || shippingItem.getQuantity() <= 0) {
				iterator.remove();
				continue;
			}
			OrderItem orderItem = order.getOrderItem(shippingItem.getSn());
			if (orderItem == null) {
				return OutApiJsonEntity.error(Code.code8324, Code.code8324.getDesc() + ":" + shippingItem.getSn());
			}
			if (shippingItem.getQuantity() > orderItem.getShippableQuantity()) {
				return OutApiJsonEntity.error(Code.code200005);
			}
			Product product = orderItem.getProduct();
			if (product == null) {
				return OutApiJsonEntity.error(Code.code200006);
			}
			if (shippingItem.getQuantity() > product.getStock()) {
				return OutApiJsonEntity.error(Code.code200007);
			}
			shippingItem.setName(orderItem.getName());
			shippingItem.setIsDelivery(orderItem.getIsDelivery());
			shippingItem.setProduct(product);
			shippingItem.setShipping(shipping);
			shippingItem.setSpecifications(orderItem.getSpecifications());
			if (orderItem.getIsDelivery()) {
				isDelivery = true;
			}
		}
		shipping.setZipCode("200000");
		shipping.setOrder(order);
		shipping.setDeliverType(DeliverType.Own);
		if (isDelivery) {
			if (!isValid(shipping, Shipping.Delivery.class)) {
				return OutApiJsonEntity.error(Code.code200009);
			}
		} else {
			shipping.setShippingMethod((String) null);
			shipping.setDeliveryCorp((String) null);
			shipping.setDeliveryCorpUrl(null);
			shipping.setDeliveryCorpCode(null);
			shipping.setTrackingNo(null);
			shipping.setFreight(null);
			shipping.setConsignee(null);
			shipping.setArea((String) null);
			shipping.setAddress(null);
			shipping.setZipCode(null);
			shipping.setPhone(null);
			if (!isValid(shipping)) {
				return OutApiJsonEntity.error(Code.code200008);
			}
		}

		Admin admin = new Admin();
		Supplier supplier = (Supplier) request.getAttribute("supplier");
		admin.setUsername(supplier.getName());
		admin.setSupplier(supplier);
		/*if (orderService.isLocked(order, admin, true)) {
			return ERROR_VIEW;
		}*/
		shipping.setOperator(admin);
		orderService.shipping(order, shipping, admin , new OrderFilesForm().getOrderFiles());

		return OutApiJsonEntity.successMessage();
    }
    
    @RequestMapping(value = "/cancel" , method = RequestMethod.POST)
    @ResponseBody
    public OutApiJsonEntity cancel(String sn, HttpServletRequest request , HttpServletResponse response){
    	
    	Order order = orderService.findBySn(sn);
		if (order == null || order.hasExpired()) {
			return OutApiJsonEntity.error(Code.code100004);
		}
		Admin admin = new Admin();
		Supplier supplier = (Supplier) request.getAttribute("supplier");
		admin.setUsername(supplier.getName());
		admin.setSupplier(supplier);
	  /*if (orderService.isLocked(order, admin, true)) {
			return ERROR_VIEW;
		}*/
		orderService.cancel(order , admin);
    	
		return OutApiJsonEntity.successMessage();
    }
 
    public static class OrderFilesForm {

		private List<OrderFile> orderFiles = new ArrayList<>() ;

		public List<OrderFile> getOrderFiles() {
			return orderFiles;
		}

		public void setOrderFiles(List<OrderFile> orderFiles) {
			this.orderFiles = orderFiles;
		}
	}

}
