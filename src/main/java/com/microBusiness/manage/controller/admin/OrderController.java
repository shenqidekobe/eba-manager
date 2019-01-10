package com.microBusiness.manage.controller.admin;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mobile.device.DeviceUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.microBusiness.manage.Message;
import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.Setting;
import com.microBusiness.manage.entity.Admin;
import com.microBusiness.manage.entity.Area;
import com.microBusiness.manage.entity.ChildMember;
import com.microBusiness.manage.entity.Invoice;
import com.microBusiness.manage.entity.JsonEntity;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.Need;
import com.microBusiness.manage.entity.Order;
import com.microBusiness.manage.entity.OrderFile;
import com.microBusiness.manage.entity.OrderItem;
import com.microBusiness.manage.entity.OrderRemarks;
import com.microBusiness.manage.entity.OrderRemarks.MsgType;
import com.microBusiness.manage.entity.Payment;
import com.microBusiness.manage.entity.PaymentMethod;
import com.microBusiness.manage.entity.Product;
import com.microBusiness.manage.entity.ProductCategory;
import com.microBusiness.manage.entity.ProxyUser;
import com.microBusiness.manage.entity.Refunds;
import com.microBusiness.manage.entity.Returns;
import com.microBusiness.manage.entity.ReturnsItem;
import com.microBusiness.manage.entity.Shipping;
import com.microBusiness.manage.entity.ShippingItem;
import com.microBusiness.manage.entity.ShippingMethod;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.entity.SupplierSupplier;
import com.microBusiness.manage.entity.SupplyNeed;
import com.microBusiness.manage.entity.SupplyType;
import com.microBusiness.manage.form.OrderItemUpdateForm;
import com.microBusiness.manage.service.AdminService;
import com.microBusiness.manage.service.AreaService;
import com.microBusiness.manage.service.ChildMemberService;
import com.microBusiness.manage.service.DeliveryCorpService;
import com.microBusiness.manage.service.MemberService;
import com.microBusiness.manage.service.NeedService;
import com.microBusiness.manage.service.OrderRemarksService;
import com.microBusiness.manage.service.OrderService;
import com.microBusiness.manage.service.PaymentMethodService;
import com.microBusiness.manage.service.ProductCategoryService;
import com.microBusiness.manage.service.ProductService;
import com.microBusiness.manage.service.ProxyUserService;
import com.microBusiness.manage.service.ShippingMethodService;
import com.microBusiness.manage.service.ShippingService;
import com.microBusiness.manage.service.SupplierSupplierService;
import com.microBusiness.manage.service.WeChatService;
import com.microBusiness.manage.util.DateUtils;
import com.microBusiness.manage.util.SystemUtils;

@Controller("adminOrderController")
@RequestMapping("/admin/order")
public class OrderController extends BaseController {

	@Resource(name = "adminServiceImpl")
	private AdminService adminService;
	@Resource(name = "areaServiceImpl")
	private AreaService areaService;
	@Resource(name = "orderServiceImpl")
	private OrderService orderService;
	@Resource(name = "shippingMethodServiceImpl")
	private ShippingMethodService shippingMethodService;
	@Resource(name = "paymentMethodServiceImpl")
	private PaymentMethodService paymentMethodService;
	@Resource(name = "deliveryCorpServiceImpl")
	private DeliveryCorpService deliveryCorpService;
	@Resource(name = "shippingServiceImpl")
	private ShippingService shippingService;
	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	@Resource(name = "supplierSupplierServiceImpl")
	private SupplierSupplierService supplierSupplierService;
	@Resource(name = "productCategoryServiceImpl")
	private ProductCategoryService productCategoryService;
	@Resource(name = "productServiceImpl")
	private ProductService productService;

	@Resource
	private ChildMemberService childMemberService;
	@Resource
	private WeChatService weChatService ;

	@Resource
	private OrderRemarksService orderRemarksService;

	@Value("${orderItem.updateTotal.admin}")
	//后台修改的次数
	private Integer itemAdminUpdateTotal ;
	@Resource
	private NeedService needService;

	@Resource
	private ProxyUserService proxyUserService;

	@RequestMapping(value = "/check_lock", method = RequestMethod.POST)
	public @ResponseBody
	Message checkLock(Long id) {
		Order order = orderService.find(id);
		if (order == null) {
			return ERROR_MESSAGE;
		}
		Admin admin = adminService.getCurrent();
		if (orderService.isLocked(order, admin, true)) {
			return Message.warn("admin.order.locked");
		}
		return SUCCESS_MESSAGE;
	}

	@RequestMapping(value = "/calculate", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> calculate(Long id, BigDecimal freight, BigDecimal tax, BigDecimal offsetAmount) {
		Map<String, Object> data = new HashMap<String, Object>();
		Order order = orderService.find(id);
		if (order == null) {
			data.put("message", ERROR_MESSAGE);
			return data;
		}
		data.put("message", SUCCESS_MESSAGE);
		data.put("amount", orderService.calculateAmount(order.getPrice(), order.getFee(), freight, tax, order.getPromotionDiscount(), order.getCouponDiscount(), offsetAmount));
		return data;
	}

	@RequestMapping(value = "/transit_step", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Object> transitStep(Long shippingId) {
		Map<String, Object> data = new HashMap<String, Object>();
		Shipping shipping = shippingService.find(shippingId);
		if (shipping == null) {
			data.put("message", ERROR_MESSAGE);
			return data;
		}
		Setting setting = SystemUtils.getSetting();
		if (StringUtils.isEmpty(setting.getKuaidi100Key()) || StringUtils.isEmpty(shipping.getDeliveryCorpCode()) || StringUtils.isEmpty(shipping.getTrackingNo())) {
			data.put("message", ERROR_MESSAGE);
			return data;
		}
		data.put("message", SUCCESS_MESSAGE);
		data.put("transitSteps", shippingService.getTransitSteps(shipping));
		return data;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id, ModelMap model) {
		Order order = orderService.find(id);
		if (order == null || order.hasExpired() || (!Order.Status.pendingPayment.equals(order.getStatus()) && !Order.Status.pendingReview.equals(order.getStatus()))) {
			return ERROR_VIEW;
		}
		model.addAttribute("paymentMethods", paymentMethodService.findAll());
		model.addAttribute("shippingMethods", shippingMethodService.findAll());
		model.addAttribute("order", order);
		return "/admin/order/edit";
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(Long id, Long areaId, Long paymentMethodId, Long shippingMethodId, BigDecimal freight, BigDecimal tax, BigDecimal offsetAmount, Long rewardPoint, String consignee, String address, String zipCode, String phone, String invoiceTitle, String memo,
			RedirectAttributes redirectAttributes) {
		Area area = areaService.find(areaId);
		PaymentMethod paymentMethod = paymentMethodService.find(paymentMethodId);
		ShippingMethod shippingMethod = shippingMethodService.find(shippingMethodId);

		Order order = orderService.find(id);
		if (order == null || order.hasExpired() || (!Order.Status.pendingPayment.equals(order.getStatus()) && !Order.Status.pendingReview.equals(order.getStatus()))) {
			return ERROR_VIEW;
		}
		Invoice invoice = StringUtils.isNotEmpty(invoiceTitle) ? new Invoice(invoiceTitle, null) : null;
		order.setTax(invoice != null ? tax : BigDecimal.ZERO);
		order.setOffsetAmount(offsetAmount);
		order.setRewardPoint(rewardPoint);
		order.setMemo(memo);
		order.setInvoice(invoice);
		order.setPaymentMethod(paymentMethod);
		if (order.getIsDelivery()) {
			order.setFreight(freight);
			order.setConsignee(consignee);
			order.setAddress(address);
			order.setZipCode(zipCode);
			order.setPhone(phone);
			order.setArea(area);
			order.setShippingMethod(shippingMethod);
			if (!isValid(order, Order.Delivery.class)) {
				return ERROR_VIEW;
			}
		} else {
			order.setFreight(BigDecimal.ZERO);
			order.setConsignee(null);
			order.setAreaName(null);
			order.setAddress(null);
			order.setZipCode(null);
			order.setPhone(null);
			order.setShippingMethodName(null);
			order.setArea(null);
			order.setShippingMethod(null);
			if (!isValid(order)) {
				return ERROR_VIEW;
			}
		}

		Admin admin = adminService.getCurrent();
		if (orderService.isLocked(order, admin, true)) {
			return ERROR_VIEW;
		}
		orderService.update(order, admin);

		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public String view(Long id, ModelMap model , HttpServletRequest request) {
		Setting setting = SystemUtils.getSetting();
		model.addAttribute("methods", Payment.Method.values());
		model.addAttribute("refundsMethods", Refunds.Method.values());
		model.addAttribute("paymentMethods", paymentMethodService.findAll());
		model.addAttribute("shippingMethods", shippingMethodService.findAll());
		model.addAttribute("deliveryCorps", deliveryCorpService.findAll());
		model.addAttribute("isKuaidi100Enabled", StringUtils.isNotEmpty(setting.getKuaidi100Key()));
		Order order = orderService.find(id) ;
		Need need = order.getNeed();
		order.setNeed(need);
		model.addAttribute("order", order);
		Supplier supplier=this.getCurrentSupplier();
		model.addAttribute("supplierId", supplier.getId());
		if (DeviceUtils.getCurrentDevice(request).isMobile()) {
			//订单物流动态数量
			model.addAttribute("shippingSize" , order.getShippings().size()) ;
			//订单附件数量
			model.addAttribute("orderFilesSize" , order.getOrderFiles().size()) ;
			//订单日志数量
			model.addAttribute("orderLogSize", order.getOrderLogs().size());
			//订单备注数量
			Integer remarksSize=0;
			if (order.getSupplyType().equals(SupplyType.temporary)) {
				for (OrderRemarks orderRemarks : order.getOrderRemarks()) {
					if (orderRemarks.getMsgType().equals(MsgType.btoc) && order.getToSupplier() == null) {
						remarksSize++;
					}else if (order.getToSupplier().equals(supplier) && orderRemarks.getMsgType().equals(MsgType.btoc)) {
						remarksSize++;
					}else if (order.getSupplier().equals(supplier) && orderRemarks.getMsgType().equals(MsgType.btob)) {
						remarksSize++;
					}
				}
			}else {
				remarksSize=order.getOrderRemarks().size();
			}
			model.addAttribute("orderRemarksSize", remarksSize);
			//订单商品数量
			model.addAttribute("quantitySize", order.getQuantity());
		}

		model.addAttribute("isOverUpdate" , order.getItemAdminUpdate().compareTo(itemAdminUpdateTotal) >=0 ? true : false) ;

		return "/admin/order/view";
	}

	public static class ReviewForm{
		private Date reDate ;
		private String deniedReason ;
		private List<OrderItem> orderItems ;

		public Date getReDate() {
			return reDate;
		}

		public void setReDate(Date reDate) {
			this.reDate = reDate;
		}

		public String getDeniedReason() {
			return deniedReason;
		}

		public void setDeniedReason(String deniedReason) {
			this.deniedReason = deniedReason;
		}

		public List<OrderItem> getOrderItems() {
			return orderItems;
		}

		public void setOrderItems(List<OrderItem> orderItems) {
			this.orderItems = orderItems;
		}
	}

	@RequestMapping(value = "/review", method = RequestMethod.POST)
	public String review(Long id, Boolean passed, RedirectAttributes redirectAttributes , String deniedReason) {

		Order order = orderService.find(id);
		if (order == null || order.hasExpired() || !Order.Status.pendingReview.equals(order.getStatus())) {
			return ERROR_VIEW;
		}
		Admin admin = adminService.getCurrent();
		if (orderService.isLocked(order, admin, true)) {
			return ERROR_VIEW;
		}
		orderService.review(order, passed, admin , deniedReason);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);



		return "redirect:view.jhtml?id=" + id;
	}

	//支付通知
	@RequestMapping(value = "/payment", method = RequestMethod.POST)
	public String payment(Payment payment, Long orderId, Long paymentMethodId, RedirectAttributes redirectAttributes) {
		Order order = orderService.find(orderId);
		if (order == null) {
			return ERROR_VIEW;
		}
		payment.setOrder(order);
		payment.setPaymentMethod(paymentMethodService.find(paymentMethodId));
		if (!isValid(payment)) {
			return ERROR_VIEW;
		}
		Admin admin = adminService.getCurrent();
		if (orderService.isLocked(order, admin, true)) {
			return ERROR_VIEW;
		}
		Member member = order.getMember();
		if (Payment.Method.deposit.equals(payment.getMethod()) && payment.getAmount().compareTo(member.getBalance()) > 0) {
			return ERROR_VIEW;
		}
		payment.setFee(BigDecimal.ZERO);
		payment.setOperator(admin);
		orderService.payment(order, payment, admin);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:view.jhtml?id=" + orderId;
	}

	//退款
	@RequestMapping(value = "/refunds", method = RequestMethod.POST)
	public String refunds(Refunds refunds, Long orderId, Long paymentMethodId, RedirectAttributes redirectAttributes) {
		Order order = orderService.find(orderId);
		if (order == null || order.getRefundableAmount().compareTo(BigDecimal.ZERO) <= 0) {
			return ERROR_VIEW;
		}
		refunds.setOrder(order);
		refunds.setPaymentMethod(paymentMethodService.find(paymentMethodId));
		if (!isValid(refunds)) {
			return ERROR_VIEW;
		}
		Admin admin = adminService.getCurrent();
		if (orderService.isLocked(order, admin, true)) {
			return ERROR_VIEW;
		}
		refunds.setOperator(admin);
		orderService.refunds(order, refunds, admin);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:view.jhtml?id=" + orderId;
	}

	@Value("${order.template.common.templateId}")
	private String commonTemplateId;


	public static class OrderFilesForm {

		private List<OrderFile> orderFiles = new ArrayList<>() ;

		public List<OrderFile> getOrderFiles() {
			return orderFiles;
		}

		public void setOrderFiles(List<OrderFile> orderFiles) {
			this.orderFiles = orderFiles;
		}
	}

	//发货
	@RequestMapping(value = "/shipping", method = RequestMethod.POST)
	public String shipping(Shipping shipping, Long orderId, Long shippingMethodId, Long deliveryCorpId, Long areaId, RedirectAttributes redirectAttributes , OrderFilesForm orderFilesForm ) {
		Order order = orderService.find(orderId);
		if (order == null || order.getShippableQuantity() <= 0) {
			return ERROR_VIEW;
		}
		boolean isDelivery = false;
		for (Iterator<ShippingItem> iterator = shipping.getShippingItems().iterator(); iterator.hasNext();) {
			ShippingItem shippingItem = iterator.next();
			if (shippingItem == null || StringUtils.isEmpty(shippingItem.getSn()) || shippingItem.getQuantity() == null || shippingItem.getQuantity() <= 0) {
				iterator.remove();
				continue;
			}
			OrderItem orderItem = order.getOrderItem(shippingItem.getSn());
			if (orderItem == null || shippingItem.getQuantity() > orderItem.getShippableQuantity()) {
				return ERROR_VIEW;
			}
			Product product = orderItem.getProduct();
			if (product != null && shippingItem.getQuantity() > product.getStock()) {
				return ERROR_VIEW;
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
		shipping.setOrder(order);
		shipping.setShippingMethod(shippingMethodService.find(shippingMethodId));
		shipping.setDeliveryCorp(deliveryCorpService.find(deliveryCorpId));
		shipping.setArea(areaService.find(areaId));
		shipping.setConsignee(order.getConsignee());
		if (isDelivery) {
			if (!isValid(shipping, Shipping.Delivery.class)) {
				return ERROR_VIEW;
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
				return ERROR_VIEW;
			}
		}

		Admin admin = adminService.getCurrent();
		if (orderService.isLocked(order, admin, true)) {
			return ERROR_VIEW;
		}
		shipping.setOperator(admin);
		orderService.shipping(order, shipping, admin , orderFilesForm.getOrderFiles());
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);

		/*// TODO: 2017/2/14 发送模版消息
		weChatService.sendTemplateMessage(order , commonTemplateId , weChatService.getGlobalToken() , Order.OrderStatus.shipped) ;

		//采购单企业接受员通知
		weChatService.sendTemplateMessageToNoticeUserPurchase(order , Order.OrderStatus.shipped , commonTemplateId , weChatService.getGlobalToken() , NoticeTypePurchase.Type.order_shipped , "");*/

		return "redirect:view.jhtml?id=" + orderId;
	}

	//
	@RequestMapping(value = "/returns", method = RequestMethod.POST)
	public String returns(Returns returns, Long orderId, Long shippingMethodId,
			Long deliveryCorpId, Long areaId, RedirectAttributes redirectAttributes) {
		Order order = orderService.find(orderId);
		if (order == null || order.getReturnableQuantity() <= 0) {
			return ERROR_VIEW;
		}
		for (Iterator<ReturnsItem> iterator = returns.getReturnsItems().iterator(); iterator.hasNext();) {
			ReturnsItem returnsItem = iterator.next();
			if (returnsItem == null || StringUtils.isEmpty(returnsItem.getSn()) || returnsItem.getQuantity() == null || returnsItem.getQuantity() <= 0) {
				iterator.remove();
				continue;
			}
			OrderItem orderItem = order.getOrderItem(returnsItem.getSn());
			if (orderItem == null || returnsItem.getQuantity() > orderItem.getReturnableQuantity()) {
				return ERROR_VIEW;
			}
			returnsItem.setName(orderItem.getName());
			returnsItem.setReturns(returns);
			returnsItem.setSpecifications(orderItem.getSpecifications());
		}
		returns.setOrder(order);
		returns.setShippingMethod(shippingMethodService.find(shippingMethodId));
		returns.setDeliveryCorp(deliveryCorpService.find(deliveryCorpId));
		returns.setArea(areaService.find(areaId));
		if (!isValid(returns)) {
			return ERROR_VIEW;
		}
		Admin admin = adminService.getCurrent();
		if (orderService.isLocked(order, admin, true)) {
			return ERROR_VIEW;
		}
		returns.setOperator(admin);
		orderService.returns(order, returns, admin);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:view.jhtml?id=" + orderId;
	}

	//后台确认收货
	@RequestMapping(value = "/receive", method = RequestMethod.POST)
	public String receive(Long id , RedirectAttributes redirectAttributes) {
		Order order = orderService.find(id);
		if (order == null || order.hasExpired() || !Order.Status.shipped.equals(order.getStatus())) {
			return ERROR_VIEW;
		}
		Admin admin = adminService.getCurrent();
		if (orderService.isLocked(order, admin, true)) {
			return ERROR_VIEW;
		}
		orderService.receive(order, admin);

		//收货就标示完成
		orderService.complete(order, admin);

		// TODO: 2017/2/14 发送模版消息
		//weChatService.sendTemplateMessage(order , commonTemplateId , weChatService.getGlobalToken() , Order.OrderStatus.completed) ;

		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:view.jhtml?id=" + id;
	}

	//订单完成
	@RequestMapping(value = "/complete", method = RequestMethod.POST)
	public String complete(Long id, RedirectAttributes redirectAttributes) {
		Order order = orderService.find(id);
		if (order == null || order.hasExpired() /*|| !Order.Status.received.equals(order.getStatus())*/) {
			return ERROR_VIEW;
		}
		Admin admin = adminService.getCurrent();
		if (orderService.isLocked(order, admin, true)) {
			return ERROR_VIEW;
		}
		orderService.complete(order, admin);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);

		return "redirect:view.jhtml?id=" + id;
	}


	//取消订单
	@RequestMapping(value = "/cancel", method = RequestMethod.POST)
	public String cancel(Long id, RedirectAttributes redirectAttributes) {
		Order order = orderService.find(id);
		if (order == null || order.hasExpired()/* || !Order.Status.received.equals(order.getStatus())*/) {
			return ERROR_VIEW;
		}
		Admin admin = adminService.getCurrent();
		if (orderService.isLocked(order, admin, true)) {
			return ERROR_VIEW;
		}
		orderService.cancel(order , admin);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:view.jhtml?id=" + id;
	}

	//订单已失败
	@RequestMapping(value = "/fail", method = RequestMethod.POST)
	public String fail(Long id, RedirectAttributes redirectAttributes) {
		Order order = orderService.find(id);
		if (order == null || order.hasExpired() || (!Order.Status.pendingShipment.equals(order.getStatus()) && !Order.Status.shipped.equals(order.getStatus()) && !Order.Status.received.equals(order.getStatus()))) {
			return ERROR_VIEW;
		}
		Admin admin = adminService.getCurrent();
		if (orderService.isLocked(order, admin, true)) {
			return ERROR_VIEW;
		}
		orderService.fail(order, admin);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:view.jhtml?id=" + id;
	}

	/**
	 * 订单列表查询
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Order.Type type, Order.Status status,Order.Status[] statuses,
			Boolean isPendingReceive, Boolean isPendingRefunds, Boolean isAllocatedStock, 
			Boolean hasExpired, Pageable pageable, ModelMap model ,String smOpenId,String sr,
			Date startDate , Date endDate , String searchName , String timeSearch, Long proxyUserId) {
		
		List<ProxyUser> list = proxyUserService.findTree(super.getCurrentSupplier(), null);
		model.addAttribute("proxyUserTree", list);
		model.addAttribute("proxyUserId", proxyUserId);
		ChildMember childMember = null;
		if(proxyUserId != null && proxyUserId.longValue() != 0){
			ProxyUser proxyUser = proxyUserService.find(proxyUserId);
			childMember = proxyUser.getChildMember();
		}
		if(StringUtils.isNotEmpty(smOpenId)){
			childMember=this.childMemberService.findBySmOpenId(smOpenId);
		}
		
		model.addAttribute("types", Order.Type.values());
		model.addAttribute("statuses", Order.Status.values());
		model.addAttribute("type", type);
		model.addAttribute("smOpenId", smOpenId);
		model.addAttribute("sr", sr);//进入订单列表的来源
		model.addAttribute("status", status);
		model.addAttribute("isPendingReceive", isPendingReceive);
		model.addAttribute("isPendingRefunds", isPendingRefunds);
		model.addAttribute("isAllocatedStock", isAllocatedStock);
		model.addAttribute("hasExpired", hasExpired);
		model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("searchName", searchName);
        model.addAttribute("timeSearch", timeSearch);
        
        if(startDate != null) {
        	startDate = DateUtils.specifyDateZero(startDate);
        }
        if(endDate != null) {
        	endDate = DateUtils.specifyDatetWentyour(endDate);
        }
		Supplier supplier = super.getCurrentSupplier();
		model.addAttribute("useExpired",""+supplier.isExpired());
		model.addAttribute("whetherCertify", ""+supplier.isProbation());
		model.addAttribute("supplierStatic", supplier.getStatus());
		model.addAttribute("supplierId", supplier.getId());
		model.addAttribute("page", orderService.findPage(null, status,statuses, null, null, 
				isPendingReceive, isPendingRefunds, null, null, isAllocatedStock, hasExpired, 
				pageable , supplier , startDate ,  endDate , searchName , timeSearch, childMember));
		model.addAttribute("isDistributionModel", supplier.getSystemSetting().getIsDistributionModel());
		//统计页面跳转带多个状态
		if (statuses!=null) {
			for (Order.Status status2 : statuses) {
				model.addAttribute(status2.toString(), status2.toString());
			}
		}
		return "/admin/order/list";
	}
	/**
	 * 异步请求订单列表
	 */
	@RequestMapping(value = "/asyncList", method = RequestMethod.GET)
	@ResponseBody
	public JsonEntity asyncList(Order.Type type, Order.Status status,Order.Status[] statuses, String memberUsername, Boolean isPendingReceive, Boolean isPendingRefunds, Boolean isAllocatedStock, Boolean hasExpired, Pageable pageable, ModelMap model , Date startDate , Date endDate , String searchName , String timeSearch) {

        if(startDate != null) {
        	startDate = DateUtils.specifyDateZero(startDate);
        }
        if(endDate != null) {
        	endDate = DateUtils.specifyDatetWentyour(endDate);
        }
		Supplier supplier = super.getCurrentSupplier();

		Member member = memberService.findByUsername(memberUsername);
		Page<Order> page;
		if (StringUtils.isNotEmpty(memberUsername) && member == null) {
			page=Page.emptyPage(pageable);
		} else {
			page=orderService.findPage(type, status,statuses, member, null, isPendingReceive,
					isPendingRefunds, null, null, isAllocatedStock, hasExpired, pageable ,
					supplier , startDate ,  endDate , searchName , timeSearch, null);
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Map<String, Object>> orderList=new ArrayList<>();
		for (Order order : page.getContent()) {
			Map<String, Object> map=new HashMap<>();
			map.put("orderId", order.getId());
			map.put("sn", order.getSn());
			map.put("status", order.getStatus().ordinal());
			map.put("statusString", order.getStatus());
			map.put("needName", order.getNeed().getName());
			map.put("quantity", order.getQuantity());
			map.put("type", order.getType());
			map.put("supplierId", supplier.getId());
			Supplier pSupplier = order.getToSupplier();
			map.put("toSupplierId", pSupplier != null ? pSupplier.getId() : null);
			orderList.add(map);
		}
		resultMap.put("orderList", orderList);
		resultMap.put("totalPages", page.getTotalPages());
		return JsonEntity.successMessage(resultMap);
	}

	//删除订单
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	Message delete(Long[] ids) {
		if (ids != null) {
			Admin admin = adminService.getCurrent();
			for (Long id : ids) {
				Order order = orderService.find(id);
				if (order != null && orderService.isLocked(order, admin, true)) {
					return Message.error("admin.order.deleteLockedNotAllowed", order.getSn());
				}
			}
			orderService.delete(ids);
		}
		return SUCCESS_MESSAGE;
	}


	//申请取消订单
	@RequestMapping(value = "/applyCancel", method = RequestMethod.POST)
	public String applyCancel(Long id, Boolean passed , RedirectAttributes redirectAttributes) {

		Order order = orderService.find(id);
		if (order == null || order.hasExpired()) {
			return ERROR_VIEW;
		}
		Admin admin = adminService.getCurrent();
		if (orderService.isLocked(order, admin, true)) {
			return ERROR_VIEW;
		}

		orderService.dealAppyCancel(order , passed , admin);

		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);

		return "redirect:view.jhtml?id=" + id;
	}

	@RequestMapping(value = "/getOutDownload", method = RequestMethod.GET)
	public ModelAndView download(Order.Type type, Order.Status status, String memberUsername, Boolean isPendingReceive, Boolean isPendingRefunds, Boolean isAllocatedStock, Boolean hasExpired, Pageable pageable, ModelMap model , Date startDate , Date endDate , String searchName , String timeSearch) {

		Supplier supplier = super.getCurrentSupplier();

		return new ModelAndView(orderService.downOrderCustomer(type, status, memberUsername, null, isPendingReceive, isPendingRefunds, null, null, isAllocatedStock, hasExpired, pageable , supplier , startDate , endDate , searchName , timeSearch), model);
	}

	/**
	 * 修改商品数量和收货时间
	 * @param id
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "/updateItems", method = RequestMethod.POST)
	public String updateItems(Long id, RedirectAttributes redirectAttributes , OrderItemUpdateForm orderItemUpdateForm , ModelMap modelMap) {
		Order order = orderService.find(id);
		if (order == null || order.hasExpired()/* || !Order.Status.received.equals(order.getStatus())*/) {
			return ERROR_VIEW;
		}
		if(!Order.Status.pendingShipment.equals(order.getStatus())){
			modelMap.addAttribute("errorMessage" , "状态错误");
			return ERROR_VIEW;
		}
		if(itemAdminUpdateTotal.compareTo(order.getItemAdminUpdate()) <= 0){
			modelMap.addAttribute("errorMessage" , "超过次数");
			return ERROR_VIEW;
		}
		Admin admin = adminService.getCurrent();
		if (orderService.isLocked(order, admin, true)) {
			modelMap.addAttribute("errorMessage" , "订单被锁定");
			return ERROR_VIEW;
		}
		orderService.updateItems(order , orderItemUpdateForm , admin);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:view.jhtml?id=" + id;
	}

	/**
	 * 发货作废
	 */
	@RequestMapping(value = "/cancelShipped", method = RequestMethod.POST)
	public String cancelShipped(Long orderId, Long shippingId , RedirectAttributes redirectAttributes , ModelMap modelMap) {
		Order order = orderService.find(orderId);

		if (order == null || order.hasExpired()) {
			return ERROR_VIEW;
		}

		Shipping shipping = shippingService.find(shippingId);

		if (null == shipping) {
			return ERROR_VIEW;
		}
		//收货，不能取消
		if(Shipping.Status.senderChecked.equals(shipping.getStatus())){
			return ERROR_VIEW;
		}

		Admin admin = adminService.getCurrent() ;

		orderService.cancelShipped(order , shipping , admin);
		return "redirect:view.jhtml?id=" + orderId;
	}

	/**
	 * 添加备注
	 * @param orderId
	 * @param orderRemarks
	 * @return
	 */
	@RequestMapping(value = "/addRemarks",method = RequestMethod.POST)
	public @ResponseBody Message addRemarks(Long orderId,OrderRemarks orderRemarks,RedirectAttributes redirectAttributes,HttpServletRequest request) {
		Order order = orderService.find(orderId);
		
		
		if(order == null) {
			return ERROR_MESSAGE;
		}
		Admin admin = adminService.getCurrent();
		Supplier supplier = admin.getSupplier() ;
		//备注人所在供应商
		String supplierName = supplier.getName();
		if (orderService.isLocked(order, admin, true)) {
			return Message.error("订单被锁定，请稍后再操作");
		}
		orderRemarksService.saveOrderRemarks(order, admin,orderRemarks,supplierName);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		//发送模版消息
		if(StringUtils.isNotEmpty(orderRemarks.getDescription())){
			weChatService.sendTemplateMessageByNotice(order , weChatService.getGlobalToken() , null , this.commonTemplateId , null,orderRemarks.getDescription(),orderRemarks.getMsgType(),orderRemarks.getLogType(),supplier);
		}

		return SUCCESS_MESSAGE;
	}

	/**
	 * 批量审核
	 */
	@RequestMapping(value = "/batchReview" , method = RequestMethod.POST)
	public String batchReview(Long[] orderIds , Boolean passed, RedirectAttributes redirectAttributes , String deniedReason) {
		if(orderIds == null) {
			return ERROR_VIEW;
		}
		Admin admin = adminService.getCurrent();

		for(int i=0 ; i<orderIds.length ; i++) {
			Order order = orderService.find(orderIds[i]);
			if(order.hasExpired() || !Order.Status.pendingReview.equals(order.getStatus())) {
				return ERROR_VIEW;
			}
			if(orderService.isLocked(order, admin, true)) {
				return ERROR_VIEW;
			}
			orderService.review(order, passed, admin, deniedReason);
		}
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml?";
	}

	/**
	 * 批量审核验证，验证是订单状态，只有是等待审核状态才可以选择
	 * @param orderIds
	 * @return
	 */
	@RequestMapping(value = "/checkBatchReview" , method = RequestMethod.POST)
	public @ResponseBody Message checkBatchReview(Long[] orderIds) {
		if(orderIds == null) {
			return ERROR_MESSAGE;
		}
		Admin admin = adminService.getCurrent();
		for(int i=0 ; i<orderIds.length ; i++) {
			Order order = orderService.find(orderIds[i]);
			if(orderService.isLocked(order, admin, true)) {
				return Message.warn("admin.order.locked");
			}
			if(order.getStatus() != Order.Status.pendingReview) {
				return Message.warn("请选择状态为“等待审核”的订单！");
			}
			if (order.getType().equals(Order.Type.billDistribution)) {
				if (order.getToSupplier().getId().equals(getCurrentSupplier().getId())) {
					return Message.warn("请选择不包含分销商品的订单！");
				}
			}
		}
		return SUCCESS_MESSAGE;
	}
	
	/**
	 * 导出选中的订单
	 * @return
	 */
	@RequestMapping(value = "/getOutExportSelectedOrder", method = RequestMethod.GET)
    public ModelAndView exportSelectedOrder(Long[] ids , ModelMap model ) {
		Supplier supplier = super.getCurrentSupplier();
		return new ModelAndView(orderService.exportSelectedOrderForm(ids , supplier), model);
    }
	
	/**
	 */
	@RequestMapping(value = "/getOutReportDownload", method = RequestMethod.GET)
	public void reportDownload(Order.Type type, Order.Status status, String memberUsername, Boolean isPendingReceive, Boolean isPendingRefunds, Boolean isAllocatedStock, Boolean hasExpired, Pageable pageable, ModelMap model , Date startDate , Date endDate , String searchName , String timeSearch , HttpServletRequest request , HttpServletResponse response) {
		Supplier supplier = super.getCurrentSupplier();
		if(startDate != null) {
        	startDate = DateUtils.specifyDateZero(startDate);
        }
        if(endDate != null) {
        	endDate = DateUtils.specifyDatetWentyour(endDate);
        }
		orderService.reportDownload(type, status, memberUsername, null, isPendingReceive, isPendingRefunds, null, null, isAllocatedStock, hasExpired, pageable , supplier , startDate , endDate , searchName , timeSearch , request , response);
	}
	
	/**
	 */
	@RequestMapping(value = "/getOutSelectedReport", method = RequestMethod.GET)
	public void selectedReport(Long[] ids ,Pageable pageable, ModelMap model , HttpServletRequest request , HttpServletResponse response) {
		Supplier supplier = super.getCurrentSupplier();
		orderService.selectedReport(ids, supplier, request, response);
	}
	
	/**
	 */
	@RequestMapping(value = "/getOutSplitOut", method = RequestMethod.GET)
	public void splitOut(Order.Type type, Order.Status status, String memberUsername, Boolean isPendingReceive, Boolean isPendingRefunds, Boolean isAllocatedStock, Boolean hasExpired, Pageable pageable, ModelMap model , Date startDate , Date endDate , String searchName , String timeSearch , HttpServletRequest request , HttpServletResponse response) {

		Supplier supplier = super.getCurrentSupplier();
		orderService.splitOut(type, status, memberUsername, null, isPendingReceive, isPendingRefunds, null, null, isAllocatedStock, hasExpired, pageable, supplier, startDate, endDate, searchName, timeSearch, request, response);
	}
	
	/**
	 * 选中拆分导出
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/getOutSelectSplitExport", method = RequestMethod.GET)
	public void selectSplitExport(Long[] ids , HttpServletRequest request , HttpServletResponse response) {

		Supplier supplier = super.getCurrentSupplier();
		orderService.selectSplitExport(ids, supplier, request, response);
	}
	
	@RequestMapping(value = "/editView", method = RequestMethod.GET)
	public String editView(Long id, ModelMap model) {
		Order order = orderService.find(id) ;
		Supplier currentSupplier=getCurrentSupplier();
		Supplier supplier=null;//当前登录如果是供应商就不为null
		Supplier distributionSupplier=null;//分销商
		Boolean hasDistribution=true;
		if (order.getType().equals(Order.Type.billDistribution)) {
			if (currentSupplier.getId().equals(order.getSupplier().getId())) {
				supplier=order.getSupplier();
			}
			distributionSupplier=order.getToSupplier();
		}else if(order.getType().equals(Order.Type.billGeneral)){
			distributionSupplier=order.getSupplier();
		}else if (order.getType().equals(Order.Type.general)) {
			distributionSupplier=order.getSupplier();
			hasDistribution=false;
		}
		SupplyNeed supplyNeed=order.getSupplyNeed();

		List<Product> list=new ArrayList<>();
		if (supplyNeed!=null) {
			if (supplyNeed.getAssignedModel().equals(SupplyNeed.AssignedModel.STRAIGHT)) {
				hasDistribution=false;
			}else {
				hasDistribution=true;
			}
			if (supplyNeed.getStatus() != SupplyNeed.Status.SUPPLY ){
				model.addAttribute("hasSupplierSupplier", false);
			}else {
				list = productService.getGoodList(supplyNeed,supplier,distributionSupplier,hasDistribution, null, null, null);
				model.addAttribute("hasSupplierSupplier", true);
			}
		}else {
			list = productService.getGoodList(supplyNeed,supplier,distributionSupplier,hasDistribution, null, null, null);
			model.addAttribute("hasSupplierSupplier", true);
		}
		
		model.addAttribute("order", order);
		model.addAttribute("products", list);
		return "/admin/order/edit";
	}

	@RequestMapping(value = "/getGoodList", method = RequestMethod.GET)
	public String getGoodList(long orderId,Long productCategoryId,String goodName,Pageable pageable,ModelMap model){
		ProductCategory productCategory = productCategoryService.find(productCategoryId);
		Order order = orderService.find(orderId);
		Supplier currentSupplier=getCurrentSupplier();
		Supplier supplier=null;//当前登录如果是供应商就不为null
		Supplier distributionSupplier=null;//分销商
		Boolean hasDistribution=true;
		if (order.getType().equals(Order.Type.billDistribution)) {
			if (currentSupplier.getId().equals(order.getSupplier().getId())) {
				supplier=order.getSupplier();
			}
			distributionSupplier=order.getToSupplier();
			hasDistribution=false;
		}else if(order.getType().equals(Order.Type.billGeneral)){
			distributionSupplier=order.getSupplier();
			hasDistribution=true;
		}else if (order.getType().equals(Order.Type.general)) {
			distributionSupplier=order.getSupplier();
			hasDistribution=false;
		}
		SupplyNeed supplyNeed=order.getSupplyNeed();
		model.addAttribute("orderId", orderId);
		model.addAttribute("goodName", goodName);
		model.addAttribute("productCategoryId", productCategoryId);
		model.addAttribute("productCategoryTree",productCategoryService.findBySupplyNeed(supplyNeed,supplier,distributionSupplier,hasDistribution));
		Page<Product> page=productService.getGoodListPage(supplyNeed,supplier,distributionSupplier,hasDistribution, productCategory, goodName, pageable,null);
		model.addAttribute("page",page);
		return "/admin/order/goodList";
	}

	@RequestMapping(value = "/editViewByFormal", method = RequestMethod.GET)
	public String editViewByFormal(Long id, ModelMap model) {
		Order order = orderService.find(id) ;
		SupplierSupplier supplierSupplier=order.getSupplierSupplier();
		List<Product> list=new ArrayList<>();
		if (supplierSupplier == null){
			model.addAttribute("hasSupplierSupplier", false);
		}else {
			if (supplierSupplier.getStatus().equals(SupplierSupplier.Status.inTheSupply) || supplierSupplier.getStatus().equals(SupplierSupplier.Status.suspendSupply)) {
				list=productService.getGoodListByFormal(supplierSupplier,null);
				model.addAttribute("hasSupplierSupplier", true);
			}else {
				model.addAttribute("hasSupplierSupplier", false);
			}
		}

		model.addAttribute("order", order);
		model.addAttribute("products", list);
		return "/admin/order/edit";
	}

	@RequestMapping(value = "/getGoodListByFormal", method = RequestMethod.GET)
	public String getGoodListByFormal(long orderId,Long productCategoryId,String goodName,Pageable pageable,ModelMap model){
		ProductCategory productCategory = productCategoryService.find(productCategoryId);
		Order order=orderService.find(orderId);
		SupplierSupplier supplierSupplier=order.getSupplierSupplier();

		if (supplierSupplier == null){
			return ERROR_VIEW;
		}else {
			if (!supplierSupplier.getStatus().equals(SupplierSupplier.Status.inTheSupply) && !supplierSupplier.getStatus().equals(SupplierSupplier.Status.suspendSupply)) {
				return ERROR_VIEW;
			}
		}

		model.addAttribute("orderId", orderId);
		model.addAttribute("goodName", goodName);
		model.addAttribute("productCategoryId", productCategoryId);
		model.addAttribute("productCategoryTree",productCategoryService.findByGradeAndNeedByFormal(supplierSupplier,null));
		Page<Product> page=productService.getGoodListPageByFormal(supplierSupplier,null, productCategory, goodName, pageable,null);
		model.addAttribute("page",page);
		return "/admin/order/goodList";
	}

	/**
	 */
	@RequestMapping(value = "/remarksMobile", method = RequestMethod.GET)
	public String remarksMobile(ModelMap model , Long id) {
		Order order = orderService.find(id);
		model.addAttribute("order", order);
		model.addAttribute("supplierId",getCurrentSupplier().getId());
		return "/admin/order/remarks_mobile";
	}
	
	/**
	 * 商品信息
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/productMobile", method = RequestMethod.GET)
	public String productMobile(ModelMap model , Long id) {
		Order order = orderService.find(id);
		model.addAttribute("order", order);
		return "/admin/order/product_mobile";
	}
	
	/**
	 * 物流信息
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/logisticsMobile", method = RequestMethod.GET)
	public String logisticsMobile(ModelMap model , Long id) {
		Order order = orderService.find(id);
		model.addAttribute("order", order);
		return "/admin/order/logistics_mobile";
	}
	
	/**
	 * 发货
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/sendMobile", method = RequestMethod.GET)
	public String sendMobile(ModelMap model , Long id) {
		Order order = orderService.find(id);
		model.addAttribute("order", order);
		model.addAttribute("methods", Payment.Method.values());
		model.addAttribute("refundsMethods", Refunds.Method.values());
		model.addAttribute("paymentMethods", paymentMethodService.findAll());
		model.addAttribute("shippingMethods", shippingMethodService.findAll());
		model.addAttribute("deliveryCorps", deliveryCorpService.findAll());
		return "/admin/order/send_mobile";
	}
	
	/**
	 * 订单日志
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/logMobile", method = RequestMethod.GET)
	public String logMobile(ModelMap model , Long id) {
		Order order = orderService.find(id);
		model.addAttribute("order", order);
		return "/admin/order/log_mobile";
	}
	
	/**
	 * 订单附件
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/annexMobile", method = RequestMethod.GET)
	public String annexMobile(ModelMap model, Long id) {
		Order order = orderService.find(id);
		model.addAttribute("order", order);
		return "/admin/order/annex_mobile";
	}
	
	/**
	 * 添加备注
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/addRemarksMobile", method = RequestMethod.GET)
	public String addRemarksMobile(ModelMap model) {
		return "/admin/order/addRemarks_mobile";
	}
	
	/**
	 * 手机版后台订货单搜索页面
	 * @return
	 */
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public String search() {
		return "/admin/order/list_search_mobile";
	}
}