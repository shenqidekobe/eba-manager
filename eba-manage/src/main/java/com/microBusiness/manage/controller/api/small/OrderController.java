package com.microBusiness.manage.controller.api.small;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.controller.api.BaseController;
import com.microBusiness.manage.entity.Admin;
import com.microBusiness.manage.entity.Cart;
import com.microBusiness.manage.entity.CartItem;
import com.microBusiness.manage.entity.ChildMember;
import com.microBusiness.manage.entity.Goods;
import com.microBusiness.manage.entity.HostingShop;
import com.microBusiness.manage.entity.JsonEntity;
import com.microBusiness.manage.entity.LocalOrderSharingStatus;
import com.microBusiness.manage.entity.LogType;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.Need;
import com.microBusiness.manage.entity.Order;
import com.microBusiness.manage.entity.OrderForm;
import com.microBusiness.manage.entity.OrderItem;
import com.microBusiness.manage.entity.OrderItemInfo;
import com.microBusiness.manage.entity.OrderItemLog;
import com.microBusiness.manage.entity.OrderLog;
import com.microBusiness.manage.entity.OrderRelation;
import com.microBusiness.manage.entity.OrderRemarks;
import com.microBusiness.manage.entity.OrderRemarks.MsgType;
import com.microBusiness.manage.entity.OrderSetting;
import com.microBusiness.manage.entity.PaymentMethod;
import com.microBusiness.manage.entity.Product;
import com.microBusiness.manage.entity.ProxyUser;
import com.microBusiness.manage.entity.Receiver;
import com.microBusiness.manage.entity.ShareNotes;
import com.microBusiness.manage.entity.Shipping;
import com.microBusiness.manage.entity.ShippingItem;
import com.microBusiness.manage.entity.ShippingMethod;
import com.microBusiness.manage.entity.Shop;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.entity.SupplierType;
import com.microBusiness.manage.entity.SupplyNeed;
import com.microBusiness.manage.entity.SupplyType;
import com.microBusiness.manage.entity.Types;
import com.microBusiness.manage.form.OrderItemUpdateForm;
import com.microBusiness.manage.service.AdminService;
import com.microBusiness.manage.service.CartItemService;
import com.microBusiness.manage.service.ChildMemberService;
import com.microBusiness.manage.service.HostingShopService;
import com.microBusiness.manage.service.OrderFormService;
import com.microBusiness.manage.service.OrderRelationService;
import com.microBusiness.manage.service.OrderRemarksService;
import com.microBusiness.manage.service.OrderService;
import com.microBusiness.manage.service.OrderSettingService;
import com.microBusiness.manage.service.PaymentMethodService;
import com.microBusiness.manage.service.ProductService;
import com.microBusiness.manage.service.ProxyUserService;
import com.microBusiness.manage.service.ReceiverService;
import com.microBusiness.manage.service.ReturnsService;
import com.microBusiness.manage.service.ShareNotesService;
import com.microBusiness.manage.service.ShippingMethodService;
import com.microBusiness.manage.service.ShippingService;
import com.microBusiness.manage.service.ShopService;
import com.microBusiness.manage.service.SupplierService;
import com.microBusiness.manage.service.SupplyNeedService;
import com.microBusiness.manage.service.WeChatService;
import com.microBusiness.manage.util.Code;
import com.microBusiness.manage.util.DateUtils;

/**
 * 我的订单处理接口
 * */
@Controller("smallOrderController")
@RequestMapping("/api/small/order")
public class OrderController extends BaseController {

	@Resource(name = "shippingMethodServiceImpl")
	private ShippingMethodService shippingMethodService;
	@Resource
    private ChildMemberService childMemberService;
	@Resource
	private PaymentMethodService paymentMethodService;
	@Resource(name = "orderServiceImpl")
	private OrderService orderService;
	@Resource
	private WeChatService weChatService ;
	@Resource
	private OrderRemarksService orderRemarksService;
	@Resource
	private ShippingService shippingService ;
	@Resource
	private OrderSettingService orderSettingService;
	@Resource
	private SupplierService supplierService;
	@Value("${ordderitem.updateTotal.customer}")
	//前台修改次数
	private Integer itemCustomerUpdateTotal;
	@Resource
   	private SupplyNeedService supplyNeedService;
	@Resource
	private ShopService shopService;
	@Resource(name = "cartItemServiceImpl")
	private CartItemService cartItemService;
	@Resource(name = "productServiceImpl")
	private ProductService productService;
	@Resource
	private ShareNotesService shareNotesService;
	@Resource
	private OrderRelationService orderRelationService;
	@Resource
	private HostingShopService hostingShopService;
	@Resource
	private OrderFormService orderFormService;
	@Value("${small.template.common.microBusiness.templateId}")
	private String smallCommonTemplateId;
	@Resource(name = "adminServiceImpl")
	private AdminService adminService;
	@Resource
	private ProxyUserService proxyUserService;
	@Resource
	private ReceiverService receiverService;
	@Resource
	private ReturnsService returnsService;

	/**
	 * 平台订单
	 * @param unionId
	 * @param pageable
	 * @param status
	 * @param shopId
	 * @return
	 */
	@RequestMapping(value = "/orderList", method = RequestMethod.GET)
	public @ResponseBody
	JsonEntity orderList(String unionId, String smOpenId, Long proxyUserId, 
			Pageable pageable, Integer status, Date startDate, Date endDate, String searchName, String ts) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Map<String, Object>> orderList = new ArrayList<Map<String, Object>>();
		//Member member = childMemberService.findByUnionId(unionId).getMember();
		ChildMember childMember = null;
		if(proxyUserId != null){
			ProxyUser proxyUser = proxyUserService.find(proxyUserId);
			childMember = proxyUser.getChildMember();
		}else{
			childMember = childMemberService.findBySmOpenId(smOpenId);
		}
		Member member = childMember.getMember();
		Order.Status orderStatus = Order.convertIntegerToOrderStatus(status);
//		List<Shop> shops=new ArrayList<>();
//		if (shopId == null){
//			shops= shopService.findList(member);
//		}else {
//			shops.add(shopService.find(shopId));
//		}
		if(proxyUserId != null && proxyUserId.longValue() != 0){
			ProxyUser proxyUser = proxyUserService.find(proxyUserId);
			childMember = proxyUser.getChildMember();
		}
		Long suppierId = 1l;
		Supplier supplier = supplierService.find(suppierId);
		if(null != startDate) {
			startDate = DateUtils.specifyDateZero(startDate);
		}
		if(null != endDate) {
			endDate = DateUtils.specifyDatetWentyour(endDate);
		}
		if(null != ts) {
			if(ts.equalsIgnoreCase("thisWeek")) {
				startDate = DateUtils.startThisWeek();
				endDate = DateUtils.endOfTheWeek();
			};
			if(ts.equalsIgnoreCase("lastWeek")) {
				startDate = DateUtils.lastWeekStartTime();
				endDate = DateUtils.lastWeekEndTime();
			};
			if(ts.equalsIgnoreCase("lastMonth")) {
				startDate = DateUtils.lastMonthStartTime();
				endDate = DateUtils.lastMonthEndTime();
			};
			if(ts.equalsIgnoreCase("thisMonth")) {
				startDate = DateUtils.startThisMonth();
				endDate = DateUtils.theEndOfTheMonth();
			};
		}
//		if(null == startDate || null == endDate) {
//			startDate = DateUtils.startThisWeek();
//			endDate = DateUtils.endOfTheWeek();
//		}
		
		
		Boolean isAllocatedStock = null;
		Boolean isPendingRefunds = null;
		Boolean isPendingReceive = null;
		Boolean hasExpired = null;
		Page<Order> page = orderService.findPage(null, orderStatus, null, member, null,
				isPendingReceive, isPendingRefunds, null, null, isAllocatedStock, 
				hasExpired, pageable, supplier, startDate, endDate, searchName, null, childMember);
		List<Map<String, Object>> orderItemList;
		for (Order order : page.getContent()) {
			orderItemList = new ArrayList<Map<String, Object>>();
			Map<String, Object> orderMap = new HashMap<String, Object>();
			orderMap.put("orderId", order.getId());
			orderMap.put("supplierName", order.getSupplier().getName());
//			if (order.getSupplyType() == SupplyType.temporary && order.getSupplierType() != SupplierType.FOUR) {
//				if (order.getToSupplier() != null) {
//					orderMap.put("supplierName", order.getToSupplier().getName());
//				}else {
//					orderMap.put("supplierName", order.getSupplier().getName());
//				}
//			}else {
//				orderMap.put("supplierName", order.getSupplier().getName());
//			}
			
			// 判断订单是否进行过入库操作
			if (order.getStorageFormLogs() != null && order.getStorageFormLogs().size() > 0) {
				orderMap.put("storageFlag", true);
			}else {
				orderMap.put("storageFlag", false);
			}
			
			orderMap.put("sn", order.getSn());
			orderMap.put("status", order.getStatus().ordinal());
			orderMap.put("supplierId" , order.getSupplier().getId()) ;
//			if (order.getShop() != null)
//				orderMap.put("shopName" , order.getShop().getName()) ;
//			else
//				orderMap.put("shopName" , "无店铺---老数据") ;
			List<OrderItem> itemsList = order.getOrderItems();
			for (OrderItem orderItem : itemsList) {
				Map<String, Object> orderItemMap = new HashMap<String, Object>();
				orderItemMap.put("goodsName", orderItem.getName());
				orderItemMap.put("img", orderItem.getProduct().getImage());
				orderItemMap.put("amount", orderItem.getPrice());
				//orderMap.put("goodsId", orderItem.getProduct().getGoods().getId());
				orderItemList.add(orderItemMap);
			}
			orderMap.put("amount" , order.getAmount());
			orderMap.put("quantity" , order.getQuantity());
			orderMap.put("orderItems", orderItemList);
			orderMap.put("supplyType" , order.getSupplyType());
			orderMap.put("price", order.getPrice());
			orderList.add(orderMap);
		}
		resultMap.put("orders", orderList);
		resultMap.put("pageNumber", page.getPageNumber());
		resultMap.put("totalPages", page.getTotalPages());
		return JsonEntity.successMessage(resultMap);

	}


	/**
	 *
	 * @Title: localOrderList
	 * @author: yuezhiwei
	 * @date: 2018年3月16日下午3:24:50
	 * @Description: 本地订单列表
	 * @return: JsonEntity
	 */
	@ResponseBody
	@RequestMapping(value = "/localOrderList" , method = RequestMethod.GET)
	public JsonEntity localOrderList(String unionId, String sharingStatus, Long shopId, Pageable pageable) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Map<String, Object>> orderList = new ArrayList<Map<String, Object>>();
		ChildMember childMember= childMemberService.findByUnionId(unionId);
		Member member = childMemberService.findByUnionId(unionId).getMember();
		Page<Order> page;
		//店铺里面的订单列表
		if (shopId != null){
			Shop shop = shopService.find(shopId);
			page = orderService.findPageOrderLocal(sharingStatus,null,shop,childMember, pageable);
		}else {
			//总订单列表
			List<Shop> shops= shopService.findList(member);
			page = orderService.findPageOrderLocal(sharingStatus,shops,null,childMember, pageable);
		}
		for (Order order : page.getContent()) {
			Map<String, Object> orderMap = new HashMap<>();
			Shop shop=order.getShop();
			boolean isMyOrder=false;
			orderMap.put("orderId", order.getId());
			orderMap.put("supplierName", order.getSupplier().getName());
			orderMap.put("sn", order.getSn());
			orderMap.put("status", order.getStatus().ordinal());
			orderMap.put("shopName", shop.getName());
			
			// 判断订单是否进行过入库操作
			if (order.getStorageFormLogs() != null && order.getStorageFormLogs().size() > 0) {
				orderMap.put("storageFlag", true);
			}else {
				orderMap.put("storageFlag", false);
			}
			
			if (shop.getMember().equals(member)){
				isMyOrder=true;
			}
			if (member.isHostingShop(shop)){
				isMyOrder=true;
			}
			if (isMyOrder){
				orderMap.put("sharingStatus",order.getSharingStatus());
			}else {
				orderMap.put("sharingStatus","participate");
			}
			orderList.add(orderMap);
		}
		resultMap.put("orders", orderList);
		resultMap.put("pageNumber", page.getPageNumber());
		resultMap.put("totalPages", page.getTotalPages());
		return JsonEntity.successMessage(resultMap);
	}

	/**
	 * 预备下单，填写收货地址信息，展示购物车商品列表
	 * itemIds:需下单的购物车项ID
	 * @param supplierId
	 * @return
	 */
	@RequestMapping(value = "/preOrder")
	public @ResponseBody
	JsonEntity preOrder(String unionId, String smOpenId, Long supplierId , Long shopId,
			Long relationId, SupplierType supplierType,Types types,String itemIds) {
		Map<String, Object> resultMap = new HashMap<>();
		Map<String, Object> shopMap = new HashMap<>();
		if(StringUtils.isEmpty(itemIds)) {
			return new JsonEntity("13002" , "请选择您要下单的商品");
		}
		//ChildMember childMember = childMemberService.findByUnionId(unionId);
		ChildMember childMember = childMemberService.findBySmOpenId(smOpenId);
		Member member = childMember.getMember();
		Supplier supplier=supplierService.find(supplierId);
		Shop shop=shopService.find(shopId);
		if(shop != null){
			shopMap.put("shopId", shop.getId());
			shopMap.put("name", shop.getName());
			shopMap.put("address", shop.getArea().getFullName()+shop.getAddress());
			if (shop.getMember().equals(member)) {
				shopMap.put("userName", shop.getUserName());
			}else {
				HostingShop hostingShop=hostingShopService.findByShopAndByMember(shop,member);
				shopMap.put("userName", hostingShop.getMemberMember().getName());
			}
			shopMap.put("tel", shop.getReceiverTel());
		}
		resultMap.put("shop", shopMap);

		Need need=null;
		SupplyNeed supplyNeed=null;
		if (SupplierType.ONE.equals(supplierType) || SupplierType.THREE.equals(supplierType)){
			supplyNeed=supplyNeedService.find(relationId);
			need=supplyNeed.getNeed();
		}else if (SupplierType.TWO.equals(supplierType)){
			need=shop.getNeeds().iterator().next();
		}
		//平台订单才会判断下单规则
		if (Types.platform.equals(types)){
			//根据supplier查询供应商的规则设置
			OrderSetting orderSetting = orderSettingService.findBySupplier(supplierId);
			if(orderSetting == null || orderSetting.getEndTime() == null || orderSetting.getStartTime() == null) {
				orderSetting = orderSettingService.setDefaultValue();
			}
			//判断下单时间
			if(!orderSettingService.compareTime(orderSetting)) {
				return new JsonEntity("13001" , "此供应商只能在当天"+orderSetting.getStartTime()+"~"+orderSetting.getEndTime()+"之间下单");
			}
			Date startDate = DateUtils.currentStartTime();
			Date endDate = DateUtils.currentEndTime();
			int count = orderService.countNumByOrder(need , startDate, endDate , supplier);//得到当天的下单次数
			if(count >= orderSetting.getNumberTimes()) {
				return new JsonEntity("13002" , "每个店铺当天只能下"+orderSetting.getNumberTimes()+"笔订单");
			}
			resultMap.put("timeReceipt", orderSetting.getTimeReceipt());
		}
		Cart cart;
		try {
			cart = member.getCart();
			if(cart == null){
				return JsonEntity.error(Code.code11105, Code.code11105.getDesc()); 
			}
		} catch (Exception e) {
			return JsonEntity.error(Code.code11101, Code.code11101.getDesc());
		}
		Set<CartItem> cartItemList;
		try {
			cartItemList = cartItemService.getCartItems(cart,shop,supplierId,relationId,supplierType,types);
			if(cartItemList.isEmpty()){
				return JsonEntity.error(Code.code11108, Code.code11108.getDesc());
			}
		} catch (Exception e) {
			return JsonEntity.error(Code.code11103, Code.code11103.getDesc());
		}

		String[] ids=StringUtils.split(itemIds,",");
		List<Map<String, Object>> resultList = new ArrayList<>();
		for (CartItem cartItem : cartItemList) {
			//不包含的项不加入
			if(!ArrayUtils.contains(ids, cartItem.getId().toString())) {
				continue;
			}
			if (!cartItem.getValid()) {
				continue;
			}
			Product product=cartItem.getProduct();
			Map<String,Object> map=new HashMap<>();
			map.put("cartItemId",cartItem.getId());
			map.put("productId",product.getId());
			map.put("name",product.getGoods().getName());
			map.put("image",product.getGoods().getImage());
			map.put("specifications",product.getSpecifications());
			map.put("quantity",cartItem.getQuantity());
			map.put("price",cartItem.getPrice());
			resultList.add(map);
		}
		resultMap.put("resultList", resultList);

		boolean canRemark=true;
//		if (SupplierType.ONE.equals(supplierType) || SupplierType.THREE.equals(supplierType)){
//			if (supplyNeed != null && supplyNeed.getAssignedModel().equals(SupplyNeed.AssignedModel.BRANCH)) {
//				canRemark=false;
//			}
//		}
		resultMap.put("canRemark", canRemark);
		List<Receiver>  reList = receiverService.find(member, true);
		if(reList == null || reList.isEmpty()){
			reList = receiverService.find(member, false);
		}
		Receiver receiver = new Receiver();
		Map<String, Object> defaultAddressMap = new HashMap<String,Object>();
		if(reList != null && !reList.isEmpty()){
			receiver = reList.get(0);
			defaultAddressMap.put("id", receiver.getId());
			defaultAddressMap.put("address", receiver.getAddress());
			defaultAddressMap.put("addressName", receiver.getAreaName());
			defaultAddressMap.put("area", receiver.getArea().getFullName());
			defaultAddressMap.put("isDefault", receiver.getIsDefault());
			defaultAddressMap.put("phone", receiver.getPhone());
			defaultAddressMap.put("name", receiver.getConsignee());
		}
		resultMap.put("address", defaultAddressMap);
		return JsonEntity.successMessage(resultMap);
	}
	
	
	@RequestMapping(value = "/preOrderPur")
	public @ResponseBody
	JsonEntity preOrderPur(String unionId, String smOpenId, Long supplierId , Long productId,
					Integer quantity) {
		if(supplierId == null){
			supplierId = 1l;
		}
		Map<String, Object> resultMap = new HashMap<>();
		//ChildMember childMember = childMemberService.findByUnionId(unionId);
		ChildMember childMember = childMemberService.findBySmOpenId(smOpenId);
		Member member = childMember.getMember();
		//Supplier supplier = supplierService.find(supplierId);
		List<Map<String, Object>> resultList = new ArrayList<>();
		Product product = productService.find(productId);
		Map<String,Object> map = new HashMap<String,Object> ();
		map.put("productId", product.getId());
		map.put("name", product.getGoods().getName());
		map.put("image", product.getGoods().getImage());
		map.put("specifications",product.getSpecifications());
		map.put("quantity", quantity);
		map.put("price",product.getPrice());
		resultList.add(map);
		resultMap.put("resultList", resultList);

		boolean canRemark = true;
		resultMap.put("canRemark", canRemark);
		List<Receiver>  reList = receiverService.find(member, true);
		if(reList == null || reList.isEmpty()){
			reList = receiverService.find(member, false);
		}
		Receiver receiver = new Receiver();
		Map<String, Object> defaultAddressMap = new HashMap<String,Object>();
		if(reList != null && !reList.isEmpty()){
			receiver = reList.get(0);
			defaultAddressMap.put("id", receiver.getId());
			defaultAddressMap.put("address", receiver.getAddress());
			defaultAddressMap.put("addressName", receiver.getAreaName());
			defaultAddressMap.put("area", receiver.getArea().getFullName());
			defaultAddressMap.put("isDefault", receiver.getIsDefault());
			defaultAddressMap.put("phone", receiver.getPhone());
			defaultAddressMap.put("name", receiver.getConsignee());
		}
		resultMap.put("address", defaultAddressMap);
		return JsonEntity.successMessage(resultMap);
	}

	@Value("${order.template.common.templateId}")
	private String commonTemplateId;

	/**
	 * itemIds：购物车项
	 * */
	@RequestMapping(value = "/create")
	public @ResponseBody
	JsonEntity create(String unionId, String smOpenId, String reDate,
			Long supplierId , SupplyType supplyType , String memo,Long relationId, 
			Long shopId, SupplierType supplierType, Types types, Long receiverId,
			String itemIds) {
		if(StringUtils.isEmpty(itemIds)) {
			return new JsonEntity("13002" , "请选择您要下单的商品");
		}
		
		Map<String, Object> data = new HashMap<String, Object>();
		ChildMember childMember = childMemberService.findBySmOpenId(smOpenId);
		Member member = childMember.getMember();
		
		Receiver receiver = receiverService.find(receiverId);

		//ChildMember childMember = childMemberService.findByUnionId(unionId);
		
		Cart cart;
		try {
			cart = member.getCart();
			if(cart == null){
				return JsonEntity.error(Code.code11105, Code.code11105.getDesc()); 
			}
		} catch (Exception e) {
			return JsonEntity.error(Code.code11101, Code.code11101.getDesc());
		}

		ShippingMethod shippingMethod = shippingMethodService.find(1l);
		PaymentMethod paymentMethod = paymentMethodService.find(3l);
		Date date = new Date();//DateUtils.formatStringToDate(reDate, DateformatEnum.yyyyMMdd2);
		// FIXME: 2017/3/21 创建订单的时候需要知道是那个公众号下的单
		//ChildMember childMember = super.getCurrChildMem(request);
		Set<CartItem> cartItemList = cartItemService.getCartItems(cart,null,supplierId,relationId,supplierType,types);
		Order order = orderService.create(cart, null, supplierType, types, null, cartItemList,itemIds,
				paymentMethod, shippingMethod, null, null, null, memo, date , supplierId , 
				supplyType , childMember,relationId, receiver);
		data.put("sn", order.getSn());
		data.put("orderId", order.getId());
		
		return JsonEntity.successMessage(data);
	}

	
	@RequestMapping(value = "/createforPur")
	public @ResponseBody
	JsonEntity createforPur(String unionId, String smOpenId, String reDate,
			Long supplierId, SupplyType supplyType , String memo,
			Long productId, Integer quantity, SupplierType supplierType, Types types, Long receiverId) {
		
		Map<String, Object> data = new HashMap<String, Object>();
		ChildMember childMember = childMemberService.findBySmOpenId(smOpenId);
		
		Receiver receiver = receiverService.find(receiverId);

		//ChildMember childMember = childMemberService.findByUnionId(unionId);
		ShippingMethod shippingMethod = shippingMethodService.find(1l);
		PaymentMethod paymentMethod = paymentMethodService.find(3l);
		Date date = new Date();//DateUtils.formatStringToDate(reDate, DateformatEnum.yyyyMMdd2);
		// FIXME: 2017/3/21 创建订单的时候需要知道是那个公众号下的单
		//ChildMember childMember = super.getCurrChildMem(request);
		Order order = orderService.create(productId,  quantity,
				  paymentMethod,  shippingMethod,  null,
				  null,  null,  memo,  date , 
				  supplierId ,  supplyType ,  childMember,  receiver);
		data.put("sn", order.getSn());
		data.put("orderId", order.getId());
		
		return JsonEntity.successMessage(data);
	}
	
	
	@RequestMapping(value = "/orderDetail")
	public @ResponseBody
	JsonEntity orderDetail(String unionId, String smOpenId, Long orderId,
			HttpServletRequest request, HttpServletResponse response) {

		//childMemberService.findByUnionId(unionId).getMember();

		Order order = orderService.find(orderId);

		if(null == orderId || null == order){
			return new JsonEntity("010502" , "参数错误");
		}

		Map<String, Object> orderMap = this.getCommonOrderDetail(order) ;

		return JsonEntity.successMessage(orderMap);
	}


	@RequestMapping(value = "/updateStatus")
	public @ResponseBody
	JsonEntity updateStatus(Long orderId, Integer status) {
		Order order = orderService.find(orderId);
		order.setStatus(Order.convertIntegerToOrderStatus(status));
		orderService.update(order);
		return JsonEntity.successMessage();
	}




	@RequestMapping(value = "/applyCancel")
	public @ResponseBody
	JsonEntity applyCancel(String unionId, String smOpenId, Long orderId) {

		ChildMember childMember = childMemberService.findBySmOpenId(smOpenId);
		//ChildMember childMember = childMemberService.findByUnionId(unionId);

		if(null == orderId){
			return new JsonEntity("010501" , "参数错误");
		}

		Order order = orderService.find(orderId) ;

		if(null == order){
			return new JsonEntity("010502" , "参数错误");
		}
		
		if(!(order.getStatus() == Order.Status.pendingReview || order.getStatus() == Order.Status.pendingShipment)) {
			return new JsonEntity("010503" , "状态错误");
    	}
		
		Supplier supplier = order.getSupplier();
		//Supplier supplier = order.getToSupplier() != null ? order.getToSupplier() : order.getSupplier();
//		Shop shop=order.getShop();
		String logName = childMember.getNickName();
		//操作账号
//		if (shop.getMember().equals(childMember.getMember())) {
//			logName="超级管理员";
//		}else {
//			HostingShop hostingShop=hostingShopService.findByShopAndByMember(shop,childMember.getMember());
//			logName=hostingShop.getMemberMember().getName();
//		}
		orderService.applyCancel(order , logName , supplier);
		
        //向供应商的接收员发送模版消息
        //weChatService.sendTemplateMessageToNoticeUser(order.getSupplier() , order , Order.OrderStatus.applyCancel , commonTemplateId , weChatService.getGlobalToken() ) ;

		return JsonEntity.successMessage();
	}

	@RequestMapping(value = "/delete")
	@ResponseBody
	public JsonEntity delete(String unionId,Long orderId){
		//ChildMember childMember = childMemberService.findByUnionId(unionId);
		Order order=orderService.find(orderId);
		if (order.getType() != Order.Type.local){
			return new JsonEntity("010533" , "只能删除本地订单");
		}
		orderService.deleted(order);
		return JsonEntity.successMessage();
	}

	/**
	 * 下发短信时提供的订单详情查看接口
	 * @param orderId
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/view")
	public @ResponseBody
	JsonEntity view(Long orderId) {

		Order order = orderService.find(orderId);

		if(null == orderId || null == order){
			return new JsonEntity("010502" , "参数错误");
		}

		Map<String, Object> orderMap = this.getCommonOrderDetail(order) ;

		return JsonEntity.successMessage(orderMap);
	}

	/**
	 * 微信用户修改商品数量
	 * @param id 订单id
	 * @param orderItemUpdateForm
	 * @return
	 */
	@RequestMapping(value = "/updateItems")
	@ResponseBody
	public JsonEntity updateItems(String unionId, Long id , OrderItemUpdateForm orderItemUpdateForm){
		Order order = orderService.find(id);
		if (order == null || order.hasExpired()) {
			return JsonEntity.error(Code.code_order_011801);
		}
		if(!Order.Status.pendingReview.equals(order.getStatus())){
			return JsonEntity.error(Code.code_order_011803);
		}
		if(itemCustomerUpdateTotal.compareTo(order.getItemCustomerUpdate()) <= 0){
			return JsonEntity.error(Code.code_order_011804);
		}
		ChildMember childMember = childMemberService.findByUnionId(unionId);
		Member member=childMember.getMember();
		if (orderService.isLocked(order, childMember.getMember(), true)) {
			return JsonEntity.error(Code.code_order_011802);
		}
		

		Supplier supplier = order.getToSupplier() != null ? order.getToSupplier() : order.getSupplier();
		Shop shop=order.getShop();
		String logName="";
		//操作账号
		if (shop.getMember().equals(member)) {
			logName="超级管理员";
		}else {
			HostingShop hostingShop=hostingShopService.findByShopAndByMember(shop,member);
			logName=hostingShop.getMemberMember().getName();
		}
		
		orderService.updateItems(order , orderItemUpdateForm , logName ,
				OrderItemLog.OperatorType.update , OrderItemLog.Type.custom , 
				supplier.getName());

		return JsonEntity.successMessage();
	}

	/**
	 * 订单备注详情
	 * @param orderId 订单id
	 * @return
	 */
	@SuppressWarnings("serial")
	@ResponseBody
	@RequestMapping(value = "/orderRemarksDetail")
	public JsonEntity orderRemarksDetail(String unionId, Long orderId , HttpServletRequest request) {
		childMemberService.findByUnionId(unionId);
		final Order order = orderService.find(orderId);
		Map<String, Object> orderMap = new HashMap<String, Object>();
		//处理备注信息
		Set<OrderRemarks> orderRemarks = order.getOrderRemarks();
		List<Map<String, Object>> orderRemarksList = new ArrayList<Map<String, Object>>();
		for(final OrderRemarks orderRemark : orderRemarks) {
			if (order.getSupplyType().equals(SupplyType.temporary)) {
				if (!orderRemark.getMsgType().equals(MsgType.btoc)) {
					continue;
				}
			}
			orderRemarksList.add(new HashMap<String, Object>(){{
				this.put("description" , orderRemark.getDescription());
				this.put("remarksPeople" , orderRemark.getName()) ;
				this.put("createDate" , orderRemark.getCreateDate());
				this.put("suppliperName" , orderRemark.getSuppliper());
				this.put("annex" , orderRemark.getAnnex());
				this.put("name", orderRemark.getName());
			}});
		}
		orderMap.put("orderRemarks", orderRemarksList);
		
		return JsonEntity.successMessage(orderMap);
	}
	
	
	/**
	 * 订单附件
	 * @param orderId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/orderFileDetail")
	public JsonEntity orderFileDetail(String unionId, Long orderId , HttpServletRequest request) {
		childMemberService.findByUnionId(unionId);
		Order order = orderService.find(orderId);
		Map<String, Object> orderMap = new HashMap<String, Object>();
		//List<OrderFile> orderRemarks = order.getOrderFiles();
		orderMap.put("orderFile", order.getOrderFiles());
		return JsonEntity.successMessage(orderMap);
	}
	
	
	/**
	 * 订单日志详情
	 * @param orderId
	 * @return
	 */
	@SuppressWarnings("serial")
	@ResponseBody
	@RequestMapping(value = "/orderLogDetail")
	public JsonEntity orderLogDetail(String unionId, Long orderId , HttpServletRequest request) {
		childMemberService.findByUnionId(unionId);
		final Order order = orderService.find(orderId);
		Map<String, Object> orderMap = new HashMap<String, Object>();
		//处理订单日志
		Set<OrderLog> orderLogs = order.getOrderLogs();
		List<Map<String, Object>> orderLogList = new ArrayList<Map<String, Object>>();
		for(final OrderLog orderLog : orderLogs) {
			orderLogList.add(new HashMap<String, Object>(){{
				if (order.getSupplyType().equals(SupplyType.temporary)) {
					if (orderLog.getLogType().equals(LogType.supplier)) {
						this.put("suppliperName" , "******");
					}else {
						this.put("suppliperName" , orderLog.getSupplierName());
					}
				}else {
						this.put("suppliperName" , orderLog.getSupplierName());
				}
				this.put("operator" , orderLog.getOperator());
				this.put("createDate" , orderLog.getCreateDate());
				this.put("type" , orderLog.getType());
				this.put("content", orderLog.getContent());
			}});
		}
		orderMap.put("orderLog", orderLogList);
		return JsonEntity.successMessage(orderMap);
	}
	
	/**
	 * 物流信息(每次发货的物流详情，包含发货数量和实收数量)
	 * @param shippingId 物流id
	 * @param request
	 * @return
	 */
	@SuppressWarnings("serial")
	@ResponseBody
	@RequestMapping(value = "/logisticsInfo")
	public JsonEntity logisticsInfo(String unionId, Long shippingId , HttpServletRequest request) {
		childMemberService.findByUnionId(unionId);
		Map<String, Object> orderMap = new HashMap<String, Object>();
		Shipping shipping = shippingService.find(shippingId);
		if(shipping == null) {
			return JsonEntity.error(Code.code_shipping_012001);
		}
		List<ShippingItem> shippingItems = shipping.getShippingItems();
		List<Map<String, Object>> shippingItemList = new ArrayList<Map<String, Object>>();
		if(shippingItems == null) {
			return JsonEntity.error(Code.code_shipping_012002);
		}
		for(final ShippingItem shippingItem : shippingItems) {
			shippingItemList.add(new HashMap<String, Object>(){{
				this.put("name" , shippingItem.getName());
				this.put("sn" , shippingItem.getSn()) ;
				this.put("quantity" , shippingItem.getQuantity());
				this.put("realQuantity" , shippingItem.getTrueRealQuantity());
				
				// 入库需要的实际收货数量
				this.put("realQuantityStorage" , shippingItem.getRealQuantity());
				this.put("productId" , shippingItem.getProduct().getId());
				
				this.put("productPicture", shippingItem.getProduct().getGoods().getImage());
				List<String> list = shippingItem.getSpecifications();
				String str = "";
				for(String aa : list) {
					if(str.equals("")) {
						str = aa;
					}else {
						str +=","+aa;
					}
				}
				this.put("specifications", str);
			}});
		}
		orderMap.put("shippingItem", shippingItemList);
		return JsonEntity.successMessage(orderMap);
	}

	/**
	 * 添加订单备注
	 * @param orderId 订单id
	 * @param orderRemarks 订单备注实体类
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/addOrderRemarks")
	public JsonEntity addOrderRemarks(String unionId, String smOpenId, Long orderId , OrderRemarks orderRemarks , HttpServletRequest request) {
		//ChildMember childMember = childMemberService.findByUnionId(unionId);
		
		ChildMember childMember = childMemberService.findBySmOpenId(smOpenId);
		Order order = orderService.find(orderId);
		//企业名称
		Supplier supplier = order.getSupplier();
//		Supplier supplier = order.getToSupplier() != null ? order.getToSupplier() : order.getSupplier();
		
//		Shop shop=order.getShop();
		String logName = childMember.getNickName();
		//操作账号
//		if (shop.getMember().equals(childMember.getMember())) {
//			logName="超级管理员";
//		}else {
//			HostingShop hostingShop=hostingShopService.findByShopAndByMember(shop,childMember.getMember());
//			logName=hostingShop.getMemberMember().getName();
//		}
		
		String suppliperName = supplier.getName();
		orderRemarks.setOrder(order);
		orderRemarks.setName(logName);
		orderRemarks.setSource(OrderRemarks.Source.customer);
		orderRemarks.setSuppliper(suppliperName);
		orderRemarks.setLogType(LogType.member);
		orderRemarks.setMsgType(MsgType.btoc);
		orderRemarksService.saveOrderRemarks(orderRemarks);

		if(StringUtils.isNotEmpty(orderRemarks.getDescription())){
			//发送模版消息
			weChatService.sendTemplateMessageByNotice(order , weChatService.getGlobalToken() , null , this.commonTemplateId , null,orderRemarks.getDescription(),orderRemarks.getMsgType(),orderRemarks.getLogType(),null);
		}


		return JsonEntity.successMessage();
	}


	/**
	 * 获取订单商品信息
	 * @param id 订单id
	 * @return
	 */
	@RequestMapping(value = "/getOrderItems" , method = RequestMethod.GET)
	@ResponseBody
	public JsonEntity getOrderItems(String unionId, Long id , final Long supplierId, final Long needId , HttpServletRequest request){
		//childMemberService.findByUnionId(unionId);
		Order order = orderService.find(id);
		if (order == null || order.hasExpired()) {
			return JsonEntity.error(Code.code_order_011801);
		}
		List<OrderItem> orderItems = order.getOrderItems();
		List<Map<String , Object>> result = new ArrayList<>(orderItems.size());
		SupplierType supplierType=order.getSupplierType();
		for(final OrderItem orderItem : orderItems){
			Map<String, Object> map=new HashMap<>();
			map.put("itemId" , orderItem.getId());
			map.put("specification" , orderItem.getSpecifications());
			map.put("goodsId", orderItem.getProduct().getGoods().getId());
			map.put("productId", orderItem.getProduct().getId());
			map.put("productName", orderItem.getName());
			map.put("img", orderItem.getProduct().getImage());
			map.put("quantity", orderItem.getQuantity());
			Long relationId=null;
			if (SupplierType.ONE.equals(supplierType) || SupplierType.THREE.equals(supplierType)) {
				relationId=order.getSupplyNeed().getId();
			}else if (SupplierType.TWO.equals(supplierType)) {
				relationId=order.getSupplierSupplier().getId();
			}
			Integer minOrderQuantity=productService.getMinOrderQuantity(order.getShop(),supplierId,order.getSupplierType(),relationId, orderItem.getProduct());
			if (minOrderQuantity == -1){
				minOrderQuantity=1;
			}
			map.put("minOrderQuantity", minOrderQuantity);
			result.add(map);
		}

		return JsonEntity.successMessage(result);
	}

	/**
	 * 获取订单商品信息 和 变更记录
	 * @param id 订单id
	 * @return
	 */
	@SuppressWarnings("serial")
	@RequestMapping(value = "/getItemsAndLog" , method = RequestMethod.GET)
	@ResponseBody
	public JsonEntity getItemsAndLog(String unionId, Long id, HttpServletRequest request){
		childMemberService.findByUnionId(unionId);
		final Order order = orderService.find(id);
		Map<String , Object> result = new HashMap<>(2);
		if (order == null || order.hasExpired()) {
			return JsonEntity.error(Code.code_order_011801);
		}
		List<OrderItem> orderItems = order.getOrderItems();
		List<Map<String , Object>> itemList = new ArrayList<>(orderItems.size());
		for(final OrderItem orderItem : orderItems){
			itemList.add(new HashMap<String, Object>(){{
				this.put("itemId" , orderItem.getId());
				this.put("goodsId", orderItem.getProduct().getGoods().getId());
				this.put("productId", orderItem.getProduct().getId());
				this.put("productName", orderItem.getName());
				this.put("img", orderItem.getProduct().getImage());
				this.put("quantity", orderItem.getQuantity());
				this.put("specs", orderItem.getSpecifications());
				this.put("price", orderItem.getPrice());
			}});
		}

		result.put("orderItems" , itemList);

		List<OrderItemLog> orderItemLogs = order.getOrderItemLogs();

		List<Map<String , Object>> returnLogs = new ArrayList<>(orderItemLogs.size());

		for (final OrderItemLog orderItemLog : orderItemLogs){

			final List<Map<String , Object>> logInfos = new ArrayList<>(orderItemLog.getOrderItemInfos().size());

			for(final OrderItemInfo orderItemInfo : orderItemLog.getOrderItemInfos()){
				final Product product = orderItemInfo.getProduct() ;
				logInfos.add(new HashMap<String, Object>(){{
					this.put("productName" , product.getName());
					this.put("specification" , product.getSpecifications());
					this.put("beforeQuantity" , orderItemInfo.getBeforeQuantity());
					this.put("afterQuantity" , orderItemInfo.getAfterQuantity());
					this.put("img" , product.getImage());
					this.put("price", product.getPrice());
				}});
			}

			returnLogs.add(new HashMap<String, Object>(){{
				this.put("infos" , logInfos);
//				if (order.getSupplyType().equals(SupplyType.temporary)) {
//					if (orderItemLog.getLogType().equals(LogType.supplier)) {
//						this.put("suppliperName" , "******");
//					}else {
//						this.put("suppliperName" , orderItemLog.getSupplierName());
//					}
//				}else {
//						this.put("suppliperName" , orderItemLog.getSupplierName());
//				}
				this.put("suppliperName" , orderItemLog.getSupplierName());
				this.put("operatorName" , orderItemLog.getOperatorName());
				this.put("createDate" , orderItemLog.getCreateDate());
				this.put("operatorType" , orderItemLog.getOperatorType());
			}});
		}

		result.put("itemLogs" , returnLogs);

		return JsonEntity.successMessage(result);
	}

	/**
	 * 消息接受员查看订单详情
	 * @param orderId
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/noticeUserView")
	public @ResponseBody
	JsonEntity noticeUserView(Long orderId, HttpServletRequest request, HttpServletResponse response) {
		Order order = orderService.find(orderId);
		if(null == orderId || null == order){
			return new JsonEntity("010502" , "参数错误");
		}
		Map<String, Object> orderMap = this.getCommonOrderDetail(order) ;
		return JsonEntity.successMessage(orderMap);
	}


	@SuppressWarnings("serial")
	private Map<String, Object> getCommonOrderDetail(Order order){
		Map<String, Object> orderMap = new HashMap<String, Object>();
		orderMap.put("orderId", order.getId());
		orderMap.put("sn", order.getSn());
		orderMap.put("orderDate", order.getCreateDate());
		orderMap.put("applyReturnsDate", order.getApplyReturnsDate());
		orderMap.put("confirmReturnsDate", order.getConfirmReturnsDate());
		orderMap.put("status", order.getStatus().ordinal());
		orderMap.put("price", order.getPrice());
		orderMap.put("freight", order.getFreight());
		orderMap.put("promotionDiscount", order.getPromotionDiscount());
		orderMap.put("couponDiscount", order.getCouponDiscount());
		orderMap.put("amountPaid", order.getAmountPaid());
		orderMap.put("refundAmount", order.getRefundAmount());
		orderMap.put("returnsNum", order.getReturnsNum());
		//orderMap.put("needName", order.getMember().getNeed().getName());
//		Need need = order.getNeed();
//		orderMap.put("needName", need.getName());
//		orderMap.put("needId", need.getId());
//		orderMap.put("shopId", order.getShop().getId());
		
		// 判断订单是否进行过入库操作
		if (order.getStorageFormLogs() != null && order.getStorageFormLogs().size() > 0) {
			orderMap.put("storageFlag", true);
			orderMap.put("storageSize", order.getStorageFormLogsCreate().size());
		}else {
			orderMap.put("storageFlag", false);
		}
		
		orderMap.put("address", order.getAddress());
		orderMap.put("areaName", order.getArea().getFullName());
		orderMap.put("userName", order.getConsignee());
		orderMap.put("mobile", order.getPhone());
		orderMap.put("reDate", order.getReDate());
		orderMap.put("receiveCode" , order.getReCode()) ;
		orderMap.put("itemCustomerUpdate" , order.getItemCustomerUpdate());
		orderMap.put("type" , order.getType()) ;
		if (order.getType().equals(Order.Type.distribution)) {
			orderMap.put("canRemark" , false);
		}else {
			orderMap.put("canRemark" , true);
		}
		
		
		//处理物流信息
		Set<Shipping> shippings = order.getShippings();
		List<Map<String , Object>> shippingList = new ArrayList<Map<String, Object>>();
		for (final Shipping shipping : shippings){
			//final String qrPath = shippingService.getQrPath(shipping);
			shippingList.add(new HashMap<String, Object>(){{
				this.put("shippingName" , shipping.getDeliveryCorp());
				this.put("trackingNo" , shipping.getTrackingNo()) ;
				this.put("shippingId" , shipping.getId());
				this.put("status" , shipping.getStatus());
				//this.put("qrPath" , qrPath) ;
			}});
		}
		orderMap.put("shippings" , shippingList) ;
		//订单附件数量
		orderMap.put("orderFilesSize" , order.getOrderFiles().size()) ;
		//订单详情数量
		orderMap.put("orderLogSize", order.getOrderLogs().size());
		//订单备注数量
		Integer remarksSize=0;
		/*if (order.getSupplyType().equals(SupplyType.temporary)) {
			for (OrderRemarks orderRemarks : order.getOrderRemarks()) {
				if (orderRemarks.getMsgType().equals(MsgType.btoc)) {
					remarksSize++;
				}
			}
		}else {
			remarksSize=order.getOrderRemarks().size();
		}*/
		orderMap.put("orderRemarksSize", remarksSize);

		orderMap.put("deniedReason" , order.getDeniedReason()) ;
		orderMap.put("supplyType" , order.getSupplyType());
		orderMap.put("memo" , order.getMemo());
		orderMap.put("supplierId" , order.getSupplier().getId());
		orderMap.put("customerTel" , order.getSupplier().getCustomerServiceTel());

		List<OrderItem> orderItemList = order.getOrderItems();
		List<Map<String, Object>> orderItemMapList = new ArrayList<Map<String, Object>>();
		if(orderItemList != null){
			for (OrderItem orderItem : orderItemList) {
				Map<String, Object> orderItemMap = new HashMap<String, Object>();
				orderItemMap.put("goodsId", orderItem.getProduct().getGoods().getId());
				orderItemMap.put("productId", orderItem.getProduct().getId());
				orderItemMap.put("name", orderItem.getName());
				orderItemMap.put("image", orderItem.getThumbnail());
				orderItemMap.put("price", orderItem.getPrice());
				orderItemMap.put("quantity", orderItem.getQuantity());
				//新增规格项显示
				orderItemMap.put("specifications", orderItem.getProduct().getSpecifications());
				orderItemMapList.add(orderItemMap);
			}
			orderMap.put("orderItems", orderItemMapList);
		}

		return orderMap ;
	}
	
	/**
	 * 
	 * @Title: updateStatus
	 * @author: yuezhiwei
	 * @date: 2018年3月19日下午3:08:27
	 * @Description: 分享、参与、终结、退出
	 * @param unionId
	 * @param orderId 订单id
	 * @param status
	 * @return: JsonEntity
	 */
	@ResponseBody
	@RequestMapping(value = "/updateStatus",method = RequestMethod.POST)
	public JsonEntity updateStatus(String unionId,Long orderId,LocalOrderSharingStatus status) {
		ChildMember childMember = childMemberService.findByUnionId(unionId);
		if(null == orderId) {
			return new JsonEntity(Code.code019998, Code.code019998.getDesc());
		}
		Order order = orderService.find(orderId);
		if(null == order || null == status) {
			return new JsonEntity(Code.code019998, Code.code019998.getDesc());
		}
		if(!status.equals(LocalOrderSharingStatus.share)&&!status.equals(LocalOrderSharingStatus.end)&&!status.equals(LocalOrderSharingStatus.participate)&&!status.equals(LocalOrderSharingStatus.noparticipate)) {
			return new JsonEntity(Code.code_order_11110, Code.code_order_11110.getDesc());
		}
		/*//分享和终结订单判断该清单是否是登录用户的清单
		if (status.equals(AssListMemberStatus.end) || status.equals(AssListMemberStatus.share)) {
			if (assList.getType().equals(AssList.Type.self) && !assChildMember.equals(assList.getAssChildMember())) {
				return new JsonEntity(Code.code_asslist_11111, Code.code_asslist_11111.getDesc());
			}
			if (assList.getType().equals(AssList.Type.supplier) && assChildMember.getMember() != null && !assChildMember.getMember().equals(assList.getMember())) {
				return new JsonEntity(Code.code_asslist_11111, Code.code_asslist_11111.getDesc());
			}
		}*/
		//只能在清单状态为分享的时候才能参与
		if(status.equals(LocalOrderSharingStatus.participate) && !order.getSharingStatus().equals(Order.SharingStatus.share)) {
			return new JsonEntity(Code.code_order_11112, Code.code_order_11112.getDesc());
		}
		//只能在清单状态为分享和终结的时候才能退出参与
		if(status.equals(LocalOrderSharingStatus.noparticipate) && (!order.getSharingStatus().equals(Order.SharingStatus.share) && !order.getSharingStatus().equals(Order.SharingStatus.end))) {
			return new JsonEntity(Code.code_order_11112, Code.code_order_11112.getDesc());
		}
		orderService.updateStatus(order, status, childMember);
		return JsonEntity.successMessage();
	}
	
	/**
	 * 
	 * @Title: getShareNotes
	 * @author: yuezhiwei
	 * @date: 2018年3月19日下午5:15:38
	 * @Description: 获取分享留言列表
	 * @return: JsonEntity
	 */
	@ResponseBody
	@RequestMapping(value = "/getShareNotes" , method = RequestMethod.GET)
	public JsonEntity getShareNotes(String unionId,Long orderId,Long shareNotesId, Pageable pageable) {
		//ChildMember childMember = childMemberService.findByUnionId(unionId);
		Order order = orderService.find(orderId);
		if(null == orderId || null == order) {
			return new JsonEntity(Code.code019998, Code.code019998.getDesc());
		}
		Page<ShareNotes> page = shareNotesService.findPage(pageable, order, shareNotesId);
		List<Map<String, Object>> shareNotesList = this.dealOrderShareNotes(page.getContent());
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("shareNotesList", shareNotesList);
		resultMap.put("totalPages", page.getTotalPages());
		return JsonEntity.successMessage(resultMap);
	}
	
	/**
	 * 
	 * @Title: getOrderRelations
	 * @author: yuezhiwei
	 * @date: 2018年3月20日上午11:31:34
	 * @Description: 获取分享参与列表
	 * @return: JsonEntity
	 */
	@ResponseBody
	@RequestMapping(value = "/getOrderRelations" , method = RequestMethod.GET)
	public JsonEntity getOrderRelations(String unionId,Long orderId,Long orderRelationId, Pageable pageable) {
		Order order = orderService.find(orderId);
		if(null == orderId || null == order || unionId == null){
			return new JsonEntity(Code.code019998, Code.code019998.getDesc());
		}
		Page<OrderRelation> page = orderRelationService.findPage(pageable, order, orderRelationId);
		List<Map<String, Object>> orderRelations = this.dealOrderRelation(page.getContent());
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("orderRelations", orderRelations);
		resultMap.put("totalPages", page.getTotalPages());
		return JsonEntity.successMessage(resultMap);
	}
	
	/**
	 * 
	 * @Title: addShareNotes
	 * @author: yuezhiwei
	 * @date: 2018年3月20日上午11:38:13
	 * @Description: 添加分享备注
	 * @return: JsonEntity
	 */
	@ResponseBody
	@RequestMapping(value = "/addShareNotes",method = RequestMethod.POST)
	public JsonEntity addShareNotes(String unionId,Long orderId,ShareNotes shareNotes) {
		Order order = orderService.find(orderId);
		if (null == orderId || null == order || unionId == null) {
			return new JsonEntity(Code.code019998, Code.code019998.getDesc());
		}
		if (order.getSharingStatus().equals(Order.SharingStatus.end)) {
			return new JsonEntity(Code.code_order_11112, Code.code_order_11112.getDesc());
		}
		ChildMember childMember = childMemberService.findByUnionId(unionId);
		shareNotes.setOrder(order);
		shareNotes.setChildMember(childMember);
		shareNotes.setName(childMember.getNickName());
		shareNotesService.save(shareNotes);
		
		//发送模版消息
		if(StringUtils.isNotBlank(shareNotes.getDescription())) {
			for(OrderRelation orderRelation : order.getOrderRelations()) {
				ChildMember aChildMember = orderRelation.getChildMember();
				//不给当前登陆用户发
				if(!childMember.equals(aChildMember)) {
					OrderForm orderForm = aChildMember.getOrderFormOne();
					Date now=new Date();
					if(orderForm != null) {
						//formId是否过期
						if(DateUtils.daysBetween(orderForm.getCreateDate(), now) < 7) {
							weChatService.sendSmallTemplateMessageToInitiator(order, aChildMember, weChatService.getSmallGlobalToken(), smallCommonTemplateId, shareNotes.getDescription(), orderForm.getFormId());
						}
						//删除formId
						orderFormService.clearExpired();
					}
				}
			}
		}
		
		return JsonEntity.successMessage();
	}
	
	/**
	 * 
	 * @Title: getOrderShareDetail
	 * @author: yuezhiwei
	 * @date: 2018年3月24日下午3:40:12
	 * @Description: 订单分享详情
	 * @return: JsonEntity
	 */
	@ResponseBody
	@RequestMapping(value = "/getOrderShareDetail",method = RequestMethod.GET)
	public JsonEntity getOrderShareDetail(String unionId,Long orderId) {
		Order order = orderService.find(orderId);
		if(null == unionId || null == order || null == orderId ){
			return new JsonEntity(Code.code019998, Code.code019998.getDesc());
		}
		if (order.isDeleted()) {
			return new JsonEntity(Code.code_order_11113, Code.code_order_11113.getDesc());
		}
		ChildMember childMember = childMemberService.findByUnionId(unionId);
		Member member = childMember.getMember();
		Map<String, Object> map = getCommonOrderDetail(order, childMember, member);
		return JsonEntity.successMessage(map);
	}
	
	/**
	 * 
	 * @Title: getGoodsList
	 * @author: yuezhiwei
	 * @date: 2018年3月24日下午3:40:53
	 * @Description: 订单分享中的商品列表
	 * @return: JsonEntity
	 */
	@ResponseBody
	@RequestMapping(value = "/getShareGoodsList",method = RequestMethod.GET)
	public JsonEntity getShareGoodsList(String unionId,Long orderId) {
		if(null == unionId || null == orderId) {
			return new JsonEntity(Code.code019998, Code.code019998.getDesc());
		}
		//ChildMember childMember = childMemberService.findByUnionId(unionId);
		Order order = orderService.find(orderId);
		List<Map<String, Object>> resultMap = new ArrayList<Map<String,Object>>();
		if(order != null) {
			List<OrderItem> orderItems = order.getOrderItems();
			for(OrderItem orderItem : orderItems) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("name", orderItem.getName());
				Product product = orderItem.getProduct();
				map.put("id", product == null ? null : product.getId());
				map.put("specification", product == null ? null : product.getSpecificationValues());
				Goods goods = product.getGoods();
				map.put("image", goods == null ? null : goods.getImage());
				map.put("quantity", orderItem.getQuantity());
				resultMap.add(map);
			}
		}
		return JsonEntity.successMessage(resultMap);
	}
	
	private List<Map<String, Object>> dealOrderShareNotes(List<ShareNotes> list) {
		List<Map<String, Object>> sharenotesList = new ArrayList<Map<String,Object>>();
		for(ShareNotes shareNotes : list) {
			Map<String, Object> map=new HashMap<String, Object>();
			map.put("shareNotesId", shareNotes.getId());
			map.put("name", shareNotes.getName());
			map.put("annex" , shareNotes.getAnnex());
			map.put("headImgUrl", shareNotes.getChildMember().getHeadImgUrl());
			map.put("createDate" , shareNotes.getCreateDate());
			map.put("description" , shareNotes.getDescription());
			sharenotesList.add(map);
		}
		return sharenotesList;
	}
	
	private List<Map<String, Object>> dealOrderRelation(List<OrderRelation> list) {
		List<Map<String, Object>> orderRelations = new ArrayList<Map<String,Object>>();
		for(OrderRelation orderRelation : list) {
			Map<String, Object> map=new HashMap<String, Object>();
			map.put("orderRelationId", orderRelation.getId());
			map.put("headImgUrl", orderRelation.getChildMember().getHeadImgUrl());
			map.put("name", orderRelation.getChildMember().getNickName());
			map.put("type", orderRelation.getType());
			map.put("orderRelationDate", orderRelation.getCreateDate());
			orderRelations.add(map);
		}
		return orderRelations;
	}
	
	@SuppressWarnings("serial")
	private Map<String, Object> getCommonOrderDetail(Order order , ChildMember childMember , Member member) {
		Map<String, Object> map = new HashMap<String, Object>();
		//订单信息
		map.put("orderId", order.getId());
		map.put("sn", order.getSn());
		map.put("areaName", order.getAreaName());
		map.put("address", order.getAddress());
		map.put("createDate", order.getCreateDate());
		map.put("sharingStatus", order.getSharingStatus());
		//门店名称
		map.put("shopName", order.getShop().getName());
		map.put("shopId", order.getShop().getId());
		//收货时间
		map.put("reDate", order.getReDate());
		//下单人
		map.put("consignee", order.getConsignee());
		
		map.put("phone", order.getPhone());
		map.put("supplierId", order.getSupplier().getId());
		
		// 判断订单是否进行过入库操作
		if (order.getStorageFormLogs() != null && order.getStorageFormLogs().size() > 0) {
			map.put("storageFlag", true);
			map.put("storageSize", order.getStorageFormLogsCreate().size());
		}else {
			map.put("storageFlag", false);
		}
		
		//当前用户相当于订单的角色  创建人  参与人  未参与人
		LocalOrderSharingStatus orderSharingStatus;
		Member pMember = order.getShop().getMember();
		if(member != null) {
			if(member.isHostingShop(order.getShop()) || member.equals(pMember)){
				orderSharingStatus = LocalOrderSharingStatus.self;
			}else{
				if(order.participate(childMember)) {
					orderSharingStatus=LocalOrderSharingStatus.participate;
				}else {
					orderSharingStatus=LocalOrderSharingStatus.noparticipate;
				}
			}
		}else{
			if(order.participate(childMember)) {
				orderSharingStatus=LocalOrderSharingStatus.participate;
			}else {
				orderSharingStatus=LocalOrderSharingStatus.noparticipate;
			}
			
		}
		map.put("orderSharingStatus", orderSharingStatus);
		//商品数量
		map.put("quantity", order.getQuantity());
		//参与人
		List<Map<String, Object>> orderRelations = new ArrayList<Map<String,Object>>();
		List<OrderRelation> relations = order.getOrderRelations();
		for(final OrderRelation orderRelation : relations) {
			orderRelations.add(new HashMap<String, Object>(){{
				this.put("headImgUrl", orderRelation.getChildMember().getHeadImgUrl());
			}});
		}
		map.put("orderRelations", orderRelations);
		return map;
	}
	
	//用户点击确认收货
	@RequestMapping(value = "/receive", method = RequestMethod.GET)
	public @ResponseBody JsonEntity receive(String unionId, String smOpenId,Long orderId ) {
		Map<String, Object> resultMap = new HashMap<>();
		Order order = orderService.find(orderId);
		if (order == null || order.hasExpired() || !Order.Status.shipped.equals(order.getStatus())) {
			return JsonEntity.error(Code.code_order_011803);
		}
		/*ChildMember childMember = childMemberService.findBySmOpenId(smOpenId);
		if (orderService.isLocked(order, childMember.getMember(), true)) {
			return JsonEntity.error(Code.code_order_011803);
		}*/
		Admin admin=this.adminService.find(1L);
		orderService.receive(order, admin);
		//收货就标示完成
		orderService.complete(order, admin);
		// TODO: 2017/2/14 发送模版消息
		weChatService.sendTemplateMessage(order , commonTemplateId , weChatService.getGlobalToken() , Order.OrderStatus.completed) ;
		return JsonEntity.successMessage(resultMap);
	}

	//用户点击退货--已发货后才能退
	@RequestMapping(value = "/returns", method = RequestMethod.GET)
	public @ResponseBody JsonEntity returns(String unionId, String smOpenId,Long orderId,
			String returnsNum,String returnsReason) {
		Map<String, Object> resultMap = new HashMap<>();
		Order order = orderService.find(orderId);
		if (order == null || order.hasExpired() || !Order.Status.shipped.equals(order.getStatus())) {
			return JsonEntity.error(Code.code_order_011803);
		}
		/*ChildMember childMember = childMemberService.findBySmOpenId(smOpenId);
		if (orderService.isLocked(order, childMember.getMember(), true)) {
			return JsonEntity.error(Code.code_order_011803);
		}*/
		order.setStatus(Order.Status.applyReturns);
		order.setApplyReturnsDate(new Date());
		order.setReturnsNum(returnsNum);
		order.setReturnsReason(returnsReason);
		orderService.update(order);
		
		// TODO: 2017/2/14 发送模版消息
		weChatService.sendTemplateMessage(order , commonTemplateId , weChatService.getGlobalToken() , Order.OrderStatus.apply_returns) ;
		return JsonEntity.successMessage(resultMap);
	}
}