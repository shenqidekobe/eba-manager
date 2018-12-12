package com.microBusiness.manage.controller.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.microBusiness.manage.dao.NoticeUserDao;
import com.microBusiness.manage.entity.Cart;
import com.microBusiness.manage.entity.CartItem;
import com.microBusiness.manage.entity.ChildMember;
import com.microBusiness.manage.entity.Goods;
import com.microBusiness.manage.entity.JsonEntity;
import com.microBusiness.manage.entity.LogType;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.Need;
import com.microBusiness.manage.entity.Order;
import com.microBusiness.manage.entity.OrderItem;
import com.microBusiness.manage.entity.OrderItemInfo;
import com.microBusiness.manage.entity.OrderItemLog;
import com.microBusiness.manage.entity.OrderLog;
import com.microBusiness.manage.entity.OrderRemarks;
import com.microBusiness.manage.entity.OrderRemarks.MsgType;
import com.microBusiness.manage.entity.OrderSetting;
import com.microBusiness.manage.entity.Product;
import com.microBusiness.manage.entity.Receiver;
import com.microBusiness.manage.entity.Shipping;
import com.microBusiness.manage.entity.ShippingItem;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.entity.SupplyType;
import com.microBusiness.manage.form.OrderItemUpdateForm;
import com.microBusiness.manage.service.AreaService;
import com.microBusiness.manage.service.CartService;
import com.microBusiness.manage.service.FileService;
import com.microBusiness.manage.service.MemberService;
import com.microBusiness.manage.service.NeedService;
import com.microBusiness.manage.service.OrderRemarksService;
import com.microBusiness.manage.service.OrderService;
import com.microBusiness.manage.service.OrderSettingService;
import com.microBusiness.manage.service.PaymentMethodService;
import com.microBusiness.manage.service.ProductService;
import com.microBusiness.manage.service.ReceiverService;
import com.microBusiness.manage.service.ShippingMethodService;
import com.microBusiness.manage.service.ShippingService;
import com.microBusiness.manage.service.SupplierService;
import com.microBusiness.manage.service.WeChatService;
import com.microBusiness.manage.util.Code;
import com.microBusiness.manage.util.DateUtils;

@Controller("shopOrderController")
@RequestMapping("/api/order")
public class OrderController extends BaseController {

	@Resource(name = "productServiceImpl")
	private ProductService productService;
	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	@Resource(name = "areaServiceImpl")
	private AreaService areaService;
	@Resource(name = "receiverServiceImpl")
	private ReceiverService receiverService;
	@Resource(name = "cartServiceImpl")
	private CartService cartService;
	@Resource(name = "shippingMethodServiceImpl")
	private ShippingMethodService shippingMethodService;
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
	private FileService fileService;
	
	@Resource
	private OrderSettingService orderSettingService;
	@Resource
	private SupplierService supplierService;

	@Resource
	private NoticeUserDao noticeUserDao ;
	
	@Value("${ordderitem.updateTotal.customer}")
	//前台修改次数
	private Integer itemCustomerUpdateTotal ;
	@Resource
	private NeedService needService;

	/**
	 * 预备下单，填写收货地址信息，展示购物车商品列表
	 * @param request
	 * @param response
	 * @param supplierId
	 * @param supplyType
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/preOrder")
	public @ResponseBody
	JsonEntity preOrder(HttpServletRequest request, HttpServletResponse response , Long supplierId , SupplyType supplyType,Long relationId) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> needMap = new HashMap<String, Object>();
		Member member = this.getUserInfo(request);
		//获取个体客户关系
		Supplier sup = supplierService.find(supplierId);
		Need need = needService.findNeedByMemberSupplier(sup, member);
		if(need != null){
			needMap.put("needId", need.getId());
			needMap.put("needName", need.getName());
			needMap.put("address", need.getAddress());
			needMap.put("userName", need.getUserName());
			needMap.put("mobile", need.getTel());
		}
		resultMap.put("need", needMap);
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
        int count = orderService.countNumByOrder(need , startDate, endDate , supplierService.find(supplierId));//得到当天的下单次数
        if(count >= orderSetting.getNumberTimes()) {
        	return new JsonEntity("13002" , "每个收货点当天只能下"+orderSetting.getNumberTimes()+"笔订单（不包括已拒绝和已取消订单）");
        }
        resultMap.put("timeReceipt", orderSetting.getTimeReceipt());
		Cart cart = null;
		try {
			cart = member.getCart();
			if(cart == null){
				return JsonEntity.error(Code.code11105, Code.code11105.getDesc()); 
			}
		} catch (Exception e) {
			return JsonEntity.error(Code.code11101, Code.code11101.getDesc());
		}
		Set<CartItem> cartItemList = null;
		try {
			cartItemList = cart.getCartItems(supplierId , supplyType,relationId);
		} catch (Exception e) {
			return JsonEntity.error(Code.code11103, Code.code11103.getDesc());
		}
		List<Map<String, Object>> supplierList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> goodsList = null;
		Map<String, Object> supplierKeyMap = new HashMap<String, Object>();
		Map<String, Object> goodsKeyMap = new HashMap<String, Object>();
		Map<String, Object> supplierMap = null;
		Map<String, Object> goodsMap = null;
		for (CartItem cartItem : cartItemList) {
			Product product = cartItem.getProduct();
			Goods goods = product.getGoods();
			Supplier supplier = goods.getSupplier();
			if(supplierKeyMap.get(String.valueOf(supplier.getId())) == null){
				supplierMap = new HashMap<String, Object>();
				supplierMap.put("supplierId", supplier.getId());
				supplierMap.put("supplierName", supplier.getName());
				supplierMap.put("cartId", cart.getId());
				supplierList.add(supplierMap);
			}else{
				supplierMap = (Map<String, Object>)supplierKeyMap.get(String.valueOf(supplier.getId()));
			}
			if(goodsKeyMap.get(String.valueOf(supplier.getId())) == null){
				goodsList = new ArrayList<Map<String, Object>>();
			}else{
				goodsList = (List<Map<String, Object>>)goodsKeyMap.get(String.valueOf(supplier.getId()));
			}
			goodsMap = new HashMap<String, Object>();
			goodsMap.put("goodsId", goods.getId());
			goodsMap.put("goodsName", goods.getName());
			goodsMap.put("productId", product.getId());
			goodsMap.put("quantity", cartItem.getQuantity());
			goodsMap.put("cartItemId", cartItem.getId());
			goodsMap.put("img", goods.getImage());
			goodsMap.put("specs", product.getSpecifications());
			goodsList.add(goodsMap);
			
			supplierMap.put("goods", goodsList);
			supplierKeyMap.put(String.valueOf(supplier.getId()), supplierMap);
			goodsKeyMap.put(String.valueOf(supplier.getId()), goodsList);
		}
		resultMap.put("supplierList", supplierList);
		return JsonEntity.successMessage(resultMap);
	}

	@Value("${order.template.common.templateId}")
	private String commonTemplateId;

	@RequestMapping(value = "/create")
	public @ResponseBody
	JsonEntity create(Need need, String reDate,
			HttpServletRequest request, HttpServletResponse response , Long supplierId , SupplyType supplyType , String memo,Long relationId) {
		//Map<String, Object> data = new HashMap<String, Object>();
		Member member = this.getUserInfo(request);
		//ChildMember childMember = super.getCurrChildMem(request) ;
		Cart cart = null;
		try {
			cart = member.getCart();
			if(cart == null){
				return JsonEntity.error(Code.code11105, Code.code11105.getDesc()); 
			}
		} catch (Exception e) {
			return JsonEntity.error(Code.code11101, Code.code11101.getDesc());
		}
		
		//保存收货地址
		Set<Receiver> receivers = member.getReceivers();
		Receiver receiver = null;
		if(receivers == null || receivers.isEmpty()){
			receiver = new Receiver();
		}else{
			receiver = new ArrayList<Receiver>(receivers).get(0);
		}
		receiver.setConsignee(need.getUserName());
		receiver.setPhone(need.getTel());
		
		//获取个体客户关系
		Supplier sup = supplierService.find(supplierId);
		Need needEntity = needService.findNeedByMemberSupplier(sup, member);
		receiver.setAddress(needEntity.getAddress());
		receiver.setMember(member);
		receiver.setIsDefault(true);
		receiver.setArea(needEntity.getArea());
		receiver.setAreaName(needEntity.getArea().getFullName());
		receiver.setZipCode("200000");
		if(receiver.getId() == null){
			receiverService.save(receiver);
		}else{
			receiverService.update(receiver);
		}
		//ShippingMethod shippingMethod = shippingMethodService.find(1l);
		//PaymentMethod paymentMethod = paymentMethodService.find(3l);
		//Date date = DateUtils.formatStringToDate(reDate, DateformatEnum.yyyyMMdd2);
		// FIXME: 2017/3/21 创建订单的时候需要知道是那个公众号下的单
		//ChildMember childMember = super.getCurrChildMem(request);

//		Order order = orderService.create( cart, receiver, needEntity,
//				paymentMethod, shippingMethod, null, null, null, memo, date , supplierId , supplyType , childMember,relationId);
//		data.put("sn", order.getSn());

		return JsonEntity.successMessage();
	}
	
	
	@RequestMapping(value = "/orderDetail")
	public @ResponseBody
	JsonEntity orderDetail(Long orderId,
			HttpServletRequest request, HttpServletResponse response) {

		super.getUserInfo(request) ;

		Order order = orderService.find(orderId);

		if(null == orderId || null == order){
			return new JsonEntity("010502" , "参数错误");
		}

		Map<String, Object> orderMap = this.getCommonOrderDetail(order) ;

		return JsonEntity.successMessage(orderMap);
	}


	@RequestMapping(value = "/updateStatus")
	public @ResponseBody
	JsonEntity updateStatus(Long orderId, Integer status,
			HttpServletRequest request, HttpServletResponse response) {
		Order order = orderService.find(orderId);
		order.setStatus(Order.convertIntegerToOrderStatus(status));
		orderService.update(order);
		return JsonEntity.successMessage();
	}




	@RequestMapping(value = "/applyCancel")
	public @ResponseBody
	JsonEntity applyCancel(Long orderId, Integer status, HttpServletRequest request, HttpServletResponse response) {

		super.getUserInfo(request) ;
		ChildMember childMember = super.getCurrChildMem(request) ;

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
		
		Supplier supplier = order.getToSupplier() != null ? order.getToSupplier() : order.getSupplier();

		//orderService.applyCancel(order , childMember.getNickName() , member.getNeed().getSupplier());
		orderService.applyCancel(order , childMember.getNickName() , supplier);

        //向供应商的接收员发送模版消息
        //weChatService.sendTemplateMessageToNoticeUser(order.getSupplier() , order , Order.OrderStatus.applyCancel , commonTemplateId , weChatService.getGlobalToken() ) ;

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
	JsonEntity view(Long orderId , HttpServletRequest request, HttpServletResponse response) {

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
	public JsonEntity updateItems(Long id , OrderItemUpdateForm orderItemUpdateForm , HttpServletRequest request){
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
		ChildMember childMember = this.getCurrChildMem(request);
		if (orderService.isLocked(order, this.getUserInfo(request), true)) {
			return JsonEntity.error(Code.code_order_011802);
		}
		
		/**orderService.updateItems(order , orderItemUpdateForm , childMember.getNickName() ,
				OrderItemLog.OperatorType.update , OrderItemLog.Type.custom , 
				super.getUserInfo(request).getNeed().getSupplier().getName());*/
		
		Supplier supplier = order.getToSupplier() != null ? order.getToSupplier() : order.getSupplier();
		
		orderService.updateItems(order , orderItemUpdateForm , childMember.getNickName() ,
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
	public JsonEntity orderRemarksDetail(Long orderId , HttpServletRequest request) {
		this.getCurrChildMem(request);
		final Order order = orderService.find(orderId);
		Map<String, Object> orderMap = new HashMap<String, Object>();
		//处理备注信息
		Set<OrderRemarks> orderRemarks = order.getOrderRemarks();
		List<Map<String, Object>> orderRemarksList = new ArrayList<Map<String, Object>>();
		for(final OrderRemarks orderRemark : orderRemarks) {
			if (!orderRemark.getMsgType().equals(MsgType.btoc)) {
				continue;
			}
			orderRemarksList.add(new HashMap<String, Object>(){{
				this.put("description" , orderRemark.getDescription());
				if (orderRemark.getLogType().equals(LogType.distributor)) {//分销商
					if (order.getToSupplier() == null) {
						this.put("remarksPeople" , order.getSupplier().getName()) ;
					}else {
						this.put("remarksPeople" , order.getToSupplier().getName()) ;
					}
				}else {
					this.put("remarksPeople" , orderRemark.getName()) ;
				}
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
	public JsonEntity orderFileDetail(Long orderId , HttpServletRequest request) {
		this.getCurrChildMem(request);
		Order order = orderService.find(orderId);
		Map<String, Object> orderMap = new HashMap<String, Object>();
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
	public JsonEntity orderLogDetail(Long orderId , HttpServletRequest request) {
		this.getCurrChildMem(request);
		final Order order = orderService.find(orderId);
		Map<String, Object> orderMap = new HashMap<String, Object>();
		//处理订单日志
		Set<OrderLog> orderLogs = order.getOrderLogs();
		List<Map<String, Object>> orderLogList = new ArrayList<Map<String, Object>>();
		for(final OrderLog orderLog : orderLogs) {
			orderLogList.add(new HashMap<String, Object>(){{
				this.put("suppliperName" , orderLog.getSupplierName());
				if (orderLog.getLogType().equals(LogType.member)) {
					this.put("operator" , orderLog.getOperator());
				}else {
					if (order.getToSupplier() == null) {
						this.put("operator" , order.getSupplier().getName());
					}else {
						this.put("operator" , order.getToSupplier().getName());
					}
				}
				this.put("operator" , orderLog.getOperator()) ;
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
	public JsonEntity logisticsInfo(Long shippingId , HttpServletRequest request) {
		this.getCurrChildMem(request);
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
				this.put("productPicture", shippingItem.getProduct().getGoods().getImage());
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
	public JsonEntity addOrderRemarks(Long orderId , OrderRemarks orderRemarks , HttpServletRequest request) {
		ChildMember childMember = this.getCurrChildMem(request);
		//企业名称
		//String suppliperName = childMember.getMember().getNeed().getSupplier().getName();
		
		Order order = orderService.find(orderId);
		
		Supplier supplier = order.getToSupplier() != null ? order.getToSupplier() : order.getSupplier();
		
		String suppliperName = supplier.getName();
		
		orderRemarks.setOrder(order);
		orderRemarks.setName(childMember.getNickName());
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
	@SuppressWarnings("serial")
	@RequestMapping(value = "/getOrderItems" , method = RequestMethod.GET)
	@ResponseBody
	public JsonEntity getOrderItems(Long id , HttpServletRequest request){

		super.getCurrChildMem(request);

		Order order = orderService.find(id);
		if (order == null || order.hasExpired()) {
			return JsonEntity.error(Code.code_order_011801);
		}
		List<OrderItem> orderItems = order.getOrderItems();
		List<Map<String , Object>> result = new ArrayList<>(orderItems.size());
		for(final OrderItem orderItem : orderItems){
			result.add(new HashMap<String, Object>(){{
				this.put("itemId" , orderItem.getId());
				this.put("goodsId", orderItem.getProduct().getGoods().getId());
				this.put("productId", orderItem.getProduct().getId());
				this.put("productName", orderItem.getName());
				this.put("img", orderItem.getProduct().getImage());
				this.put("quantity", orderItem.getQuantity());
			}});
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
	public JsonEntity getItemsAndLog(Long id , HttpServletRequest request){

		super.getCurrChildMem(request);

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
					this.put("beforeQuantity" , orderItemInfo.getBeforeQuantity());
					this.put("afterQuantity" , orderItemInfo.getAfterQuantity());
					this.put("img" , product.getImage());
				}});
			}

			returnLogs.add(new HashMap<String, Object>(){{
				this.put("infos" , logInfos);
				if (orderItemLog.getLogType().equals(LogType.member)) {
					this.put("operatorName" , orderItemLog.getOperatorName());
				}else {
					if (order.getToSupplier() == null) {
						this.put("operatorName" , order.getSupplier().getName());
					}else {
						this.put("operatorName" , order.getToSupplier().getName());
					}
				}
				this.put("supplierName" , orderItemLog.getSupplierName());
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
		orderMap.put("status", order.getStatus().ordinal());
		//orderMap.put("needName", order.getMember().getNeed().getName());
		Need need = order.getNeed();
		orderMap.put("needName", need.getName());
		
		orderMap.put("address", order.getAddress());
		orderMap.put("userName", order.getConsignee());
		orderMap.put("mobile", order.getPhone());
		orderMap.put("reDate", order.getReDate());
		orderMap.put("receiveCode" , order.getReCode()) ;
		orderMap.put("itemCustomerUpdate" , order.getItemCustomerUpdate());

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
		for (OrderRemarks orderRemarks : order.getOrderRemarks()) {
			if (orderRemarks.getMsgType().equals(MsgType.btoc)) {
				remarksSize++;
			}
		}
		orderMap.put("orderRemarksSize", remarksSize);

		orderMap.put("deniedReason" , order.getDeniedReason()) ;
		orderMap.put("supplyType" , order.getSupplyType());
		orderMap.put("memo" , order.getMemo());
		orderMap.put("supplierId" , order.getSupplier().getId());

		List<OrderItem> orderItemList = order.getOrderItems();
		List<Map<String, Object>> orderItemMapList = new ArrayList<Map<String, Object>>();
		if(orderItemList != null){
			for (OrderItem orderItem : orderItemList) {
				Map<String, Object> orderItemMap = new HashMap<String, Object>();
				orderItemMap.put("goodsId", orderItem.getProduct().getGoods().getId());
				orderItemMap.put("productId", orderItem.getProduct().getId());
				orderItemMap.put("productName", orderItem.getName());
				orderItemMap.put("img", orderItem.getThumbnail());
				orderItemMap.put("quantity", orderItem.getQuantity());
				//新增规格项显示
				orderItemMap.put("specs", orderItem.getProduct().getSpecifications());
				orderItemMapList.add(orderItemMap);
			}
			orderMap.put("orderItems", orderItemMapList);
		}

		return orderMap ;
	}

}