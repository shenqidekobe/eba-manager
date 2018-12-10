/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service.impl;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.poi.hssf.usermodel.HSSFBorderFormatting;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.Assert;

import com.google.gson.Gson;
import com.microBusiness.manage.ExcelView;
import com.microBusiness.manage.Filter;
import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.Setting;
import com.microBusiness.manage.dao.AreaDao;
import com.microBusiness.manage.dao.CartItemDao;
import com.microBusiness.manage.dao.ChildMemberDao;
import com.microBusiness.manage.dao.HostingShopDao;
import com.microBusiness.manage.dao.MemberDao;
import com.microBusiness.manage.dao.MemberIncomeDao;
import com.microBusiness.manage.dao.NeedDao;
import com.microBusiness.manage.dao.NeedProductDao;
import com.microBusiness.manage.dao.NeedShopProductDao;
import com.microBusiness.manage.dao.NoticeUserDao;
import com.microBusiness.manage.dao.OrderDao;
import com.microBusiness.manage.dao.OrderItemDao;
import com.microBusiness.manage.dao.OrderItemLogDao;
import com.microBusiness.manage.dao.OrderLogDao;
import com.microBusiness.manage.dao.OrderNewsPushDao;
import com.microBusiness.manage.dao.OrderRelationDao;
import com.microBusiness.manage.dao.OrderRemarksDao;
import com.microBusiness.manage.dao.OrderShareLogDao;
import com.microBusiness.manage.dao.PaymentDao;
import com.microBusiness.manage.dao.PaymentLogDao;
import com.microBusiness.manage.dao.PaymentMethodDao;
import com.microBusiness.manage.dao.ReceiverDao;
import com.microBusiness.manage.dao.RefundsDao;
import com.microBusiness.manage.dao.ReturnsDao;
import com.microBusiness.manage.dao.ShippingDao;
import com.microBusiness.manage.dao.SnDao;
import com.microBusiness.manage.dao.SupplierDao;
import com.microBusiness.manage.dao.SupplierProductDao;
import com.microBusiness.manage.dao.SupplierSupplierDao;
import com.microBusiness.manage.dto.AssListStatisticsDto;
import com.microBusiness.manage.dto.GoodNeedDto;
import com.microBusiness.manage.dto.GoodSupplierDto;
import com.microBusiness.manage.dto.OrderStatisticsDto;
import com.microBusiness.manage.entity.Admin;
import com.microBusiness.manage.entity.Area;
import com.microBusiness.manage.entity.Cart;
import com.microBusiness.manage.entity.CartItem;
import com.microBusiness.manage.entity.ChildMember;
import com.microBusiness.manage.entity.Coupon;
import com.microBusiness.manage.entity.CouponCode;
import com.microBusiness.manage.entity.DepositLog;
import com.microBusiness.manage.entity.Goods;
import com.microBusiness.manage.entity.HostingShop;
import com.microBusiness.manage.entity.Invoice;
import com.microBusiness.manage.entity.LocalOrderSharingStatus;
import com.microBusiness.manage.entity.LogType;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.MemberIncome;
import com.microBusiness.manage.entity.Need;
import com.microBusiness.manage.entity.NeedProduct;
import com.microBusiness.manage.entity.NoticeType;
import com.microBusiness.manage.entity.NoticeUser;
import com.microBusiness.manage.entity.Order;
import com.microBusiness.manage.entity.Order.BuyType;
import com.microBusiness.manage.entity.Order.Status;
import com.microBusiness.manage.entity.Order.Type;
import com.microBusiness.manage.entity.OrderFile;
import com.microBusiness.manage.entity.OrderItem;
import com.microBusiness.manage.entity.OrderItemInfo;
import com.microBusiness.manage.entity.OrderItemLog;
import com.microBusiness.manage.entity.OrderLog;
import com.microBusiness.manage.entity.OrderNeedsForm;
import com.microBusiness.manage.entity.OrderNewsPush;
import com.microBusiness.manage.entity.OrderProductForm;
import com.microBusiness.manage.entity.OrderRelation;
import com.microBusiness.manage.entity.OrderRemarks;
import com.microBusiness.manage.entity.OrderRemarks.MsgType;
import com.microBusiness.manage.entity.OrderShareLog;
import com.microBusiness.manage.entity.Payment;
import com.microBusiness.manage.entity.PaymentLog;
import com.microBusiness.manage.entity.PaymentMethod;
import com.microBusiness.manage.entity.PointLog;
import com.microBusiness.manage.entity.Product;
import com.microBusiness.manage.entity.Receiver;
import com.microBusiness.manage.entity.Refunds;
import com.microBusiness.manage.entity.Returns;
import com.microBusiness.manage.entity.ReturnsItem;
import com.microBusiness.manage.entity.Shipping;
import com.microBusiness.manage.entity.ShippingItem;
import com.microBusiness.manage.entity.ShippingMethod;
import com.microBusiness.manage.entity.Shop;
import com.microBusiness.manage.entity.Sn;
import com.microBusiness.manage.entity.StockLog;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.entity.SupplierProduct;
import com.microBusiness.manage.entity.SupplierSupplier;
import com.microBusiness.manage.entity.SupplierType;
import com.microBusiness.manage.entity.SupplyNeed;
import com.microBusiness.manage.entity.SupplyType;
import com.microBusiness.manage.entity.TemplateInfo;
import com.microBusiness.manage.entity.Types;
import com.microBusiness.manage.form.OrderItemUpdateForm;
import com.microBusiness.manage.service.CouponCodeService;
import com.microBusiness.manage.service.GoodsService;
import com.microBusiness.manage.service.LogService;
import com.microBusiness.manage.service.MailService;
import com.microBusiness.manage.service.MemberService;
import com.microBusiness.manage.service.OrderFileService;
import com.microBusiness.manage.service.OrderItemService;
import com.microBusiness.manage.service.OrderService;
import com.microBusiness.manage.service.ProductService;
import com.microBusiness.manage.service.ShippingMethodService;
import com.microBusiness.manage.service.SmsService;
import com.microBusiness.manage.service.SupplierService;
import com.microBusiness.manage.service.SupplyNeedService;
import com.microBusiness.manage.service.WeChatService;
import com.microBusiness.manage.util.Constant.ORDER_LOG_CONTENT;
import com.microBusiness.manage.util.Constant.PAYMENT_PLUGIN;
import com.microBusiness.manage.util.DateformatEnum;
import com.microBusiness.manage.util.IpUtil;
import com.microBusiness.manage.util.SpringUtils;
import com.microBusiness.manage.util.SystemUtils;

@Service("orderServiceImpl")
public class OrderServiceImpl extends BaseServiceImpl<Order, Long> implements OrderService {
	
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Resource(name = "orderDaoImpl")
	private OrderDao orderDao;
	@Resource(name = "orderLogDaoImpl")
	private OrderLogDao orderLogDao;
	@Resource(name = "snDaoImpl")
	private SnDao snDao;
	@Resource(name = "paymentDaoImpl")
	private PaymentDao paymentDao;
	@Resource(name = "refundsDaoImpl")
	private RefundsDao refundsDao;
	@Resource(name = "shippingDaoImpl")
	private ShippingDao shippingDao;
	@Resource(name = "returnsDaoImpl")
	private ReturnsDao returnsDao;
	@Resource(name = "memberIncomeDaoImpl")
	private MemberIncomeDao memberIncomeDao;
	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	@Resource(name = "couponCodeServiceImpl")
	private CouponCodeService couponCodeService;
	@Resource(name = "goodsServiceImpl")
	private GoodsService goodsService;
	@Resource(name = "productServiceImpl")
	private ProductService productService;
	@Resource(name = "shippingMethodServiceImpl")
	private ShippingMethodService shippingMethodService;
	@Resource(name = "mailServiceImpl")
	private MailService mailService;
	@Resource(name = "smsServiceImpl")
	private SmsService smsService;
	@Resource(name = "orderItemServiceImpl")
	private OrderItemService orderItemService;
	@Resource
	private SupplyNeedService supplyNeedService;
	@Resource
	private SupplierService supplierService;

	@Resource
	private WeChatService weChatService ;

	@Resource
	private OrderFileService orderFileService ;

	@Resource
	private SupplierDao supplierDao ;
	@Resource
	private NeedProductDao needProductDao ;
	@Resource
	private SupplierSupplierDao supplierSupplierDao ;
    @Resource
    private SupplierProductDao supplierProductDao ;
	@Resource
	private CartItemDao cartItemDao ;
	@Resource
	private NeedDao needDao ;
	@Resource
	private ReceiverDao receiverDao ;
	@Resource
	private PaymentMethodDao paymentMethodDao ;
	@Resource
	private OrderItemLogDao orderItemLogDao ;
	@Resource
	private OrderRemarksDao orderRemarksDao;
	@Resource
	private NoticeUserDao noticeUserDao ;
	@Resource
	private OrderNewsPushDao orderNewsPushDao;
	@Resource
	private NeedShopProductDao needShopProductDao;
	@Resource
	private AreaDao areaDao;
	@Resource
	private OrderShareLogDao orderShareLogDao;
	@Resource
	private OrderRelationDao orderRelationDao;
	@Resource
	private HostingShopDao hostingShopDao;
	@Value("${order.new.notice}")
	private String orderNotice ;
	@Value("${order.create.by.notice}")
	private String byOrderCreateNotice ;
	@Value("${order.detail.url.browser}")
	private String orderDetailBrowser ;
	@Resource
	private PaymentLogDao paymentLogDao;
	@Value("${distribution.rate1}")
	private Float distributionRate1;
	@Value("${distribution.rate2}")
	private Float distributionRate2;
	@Value("${distribution.rate3}")
	private Float distributionRate3;
	
	@Resource
	private PlatformTransactionManager transactionManager;
	
	@Resource
	private LogService logService;
	@Resource
	private ChildMemberDao childMemberDao;
	@Resource
	private MemberDao memberDao;
	@Resource
	private OrderItemDao orderItemDao;

	/*@Resource
	private BatchOrderLogDao batchOrderLogDao ;*/

	@Transactional(readOnly = true)
	public Order findBySn(String sn) {
		return orderDao.findBySn(sn);
	}

	@Transactional(readOnly = true)
	public List<Order> findList(Order.Type type, Order.Status status, Member member, Goods goods, Boolean isPendingReceive, Boolean isPendingRefunds, Boolean isUseCouponCode, Boolean isExchangePoint, Boolean isAllocatedStock, Boolean hasExpired, Integer count, List<Filter> filters,
                                List<com.microBusiness.manage.Order> orders) {
		return orderDao.findList(type, status, member, goods, isPendingReceive, isPendingRefunds, isUseCouponCode, isExchangePoint, isAllocatedStock, hasExpired, count, filters, orders);
	}

	@Transactional(readOnly = true)
	public Page<Order> findPage(Order.Type type, Order.Status status, Member member, Goods goods, Boolean isPendingReceive, Boolean isPendingRefunds, Boolean isUseCouponCode, Boolean isExchangePoint, Boolean isAllocatedStock, Boolean hasExpired, Pageable pageable) {
		return orderDao.findPage(type, status, member, goods, isPendingReceive, isPendingRefunds, isUseCouponCode, isExchangePoint, isAllocatedStock, hasExpired, pageable);
	}

	@Transactional(readOnly = true)
	public Long count(Order.Type type, Order.Status status, Member member, Goods goods, Boolean isPendingReceive, Boolean isPendingRefunds, Boolean isUseCouponCode, Boolean isExchangePoint, Boolean isAllocatedStock, Boolean hasExpired) {
		return orderDao.count(type, status, member, goods, isPendingReceive, isPendingRefunds, isUseCouponCode, isExchangePoint, isAllocatedStock, hasExpired);
	}

	@Transactional(readOnly = true)
	public BigDecimal calculateTax(BigDecimal price, BigDecimal promotionDiscount, BigDecimal couponDiscount, BigDecimal offsetAmount) {
		Assert.notNull(price);
		Assert.state(price.compareTo(BigDecimal.ZERO) >= 0);
		Assert.state(promotionDiscount == null || promotionDiscount.compareTo(BigDecimal.ZERO) >= 0);
		Assert.state(couponDiscount == null || couponDiscount.compareTo(BigDecimal.ZERO) >= 0);

		Setting setting = SystemUtils.getSetting();
		if (!setting.getIsTaxPriceEnabled()) {
			return BigDecimal.ZERO;
		}
		BigDecimal amount = price;
		if (promotionDiscount != null) {
			amount = amount.subtract(promotionDiscount);
		}
		if (couponDiscount != null) {
			amount = amount.subtract(couponDiscount);
		}
		if (offsetAmount != null) {
			amount = amount.add(offsetAmount);
		}
		BigDecimal tax = amount.multiply(new BigDecimal(String.valueOf(setting.getTaxRate())));
		return tax.compareTo(BigDecimal.ZERO) >= 0 ? setting.setScale(tax) : BigDecimal.ZERO;
	}

	@Transactional(readOnly = true)
	public BigDecimal calculateTax(Order order) {
		Assert.notNull(order);

		if (order.getInvoice() == null) {
			return BigDecimal.ZERO;
		}
		return calculateTax(order.getPrice(), order.getPromotionDiscount(), order.getCouponDiscount(), order.getOffsetAmount());
	}

	@Transactional(readOnly = true)
	public BigDecimal calculateAmount(BigDecimal price, BigDecimal fee, BigDecimal freight, BigDecimal tax, BigDecimal promotionDiscount, BigDecimal couponDiscount, BigDecimal offsetAmount) {
		Assert.notNull(price);
		Assert.state(price.compareTo(BigDecimal.ZERO) >= 0);
		Assert.state(fee == null || fee.compareTo(BigDecimal.ZERO) >= 0);
		Assert.state(freight == null || freight.compareTo(BigDecimal.ZERO) >= 0);
		Assert.state(tax == null || tax.compareTo(BigDecimal.ZERO) >= 0);
		Assert.state(promotionDiscount == null || promotionDiscount.compareTo(BigDecimal.ZERO) >= 0);
		Assert.state(couponDiscount == null || couponDiscount.compareTo(BigDecimal.ZERO) >= 0);

		Setting setting = SystemUtils.getSetting();
		BigDecimal amount = price;
		if (fee != null) {
			amount = amount.add(fee);
		}
		if (freight != null) {
			amount = amount.add(freight);
		}
		if (tax != null) {
			amount = amount.add(tax);
		}
		if (promotionDiscount != null) {
			amount = amount.subtract(promotionDiscount);
		}
		if (couponDiscount != null) {
			amount = amount.subtract(couponDiscount);
		}
		if (offsetAmount != null) {
			amount = amount.add(offsetAmount);
		}
		return amount.compareTo(BigDecimal.ZERO) >= 0 ? setting.setScale(amount) : BigDecimal.ZERO;
	}

	@Transactional(readOnly = true)
	public BigDecimal calculateAmount(Order order) {
		Assert.notNull(order);

		return calculateAmount(order.getPrice(), order.getFee(), order.getFreight(), order.getTax(), order.getPromotionDiscount(), order.getCouponDiscount(), order.getOffsetAmount());
	}

	public boolean isLocked(Order order, Admin admin, boolean autoLock) {
		Assert.notNull(order);
		Assert.notNull(admin);

		boolean isLocked = order.getLockExpire() != null && order.getLockExpire().after(new Date()) && StringUtils.isNotEmpty(order.getLockKey()) && !StringUtils.equals(order.getLockKey(), admin.getLockKey());
		if (autoLock && !isLocked && StringUtils.isNotEmpty(admin.getLockKey())) {
			order.setLockKey(admin.getLockKey());
			order.setLockExpire(DateUtils.addSeconds(new Date(), Order.LOCK_EXPIRE));
		}
		return isLocked;
	}

	public boolean isLocked(Order order, Member member, boolean autoLock) {
		Assert.notNull(order);
		Assert.notNull(member);

		boolean isLocked = order.getLockExpire() != null && order.getLockExpire().after(new Date()) && StringUtils.isNotEmpty(order.getLockKey()) && !StringUtils.equals(order.getLockKey(), member.getLockKey());
		if (autoLock && !isLocked && StringUtils.isNotEmpty(member.getLockKey())) {
			order.setLockKey(member.getLockKey());
			order.setLockExpire(DateUtils.addSeconds(new Date(), Order.LOCK_EXPIRE));
		}
		return isLocked;
	}

	public void lock(Order order, Admin admin) {
		Assert.notNull(order);
		Assert.notNull(admin);

		boolean isLocked = order.getLockExpire() != null && order.getLockExpire().after(new Date()) && StringUtils.isNotEmpty(order.getLockKey()) && !StringUtils.equals(order.getLockKey(), admin.getLockKey());
		if (!isLocked && StringUtils.isNotEmpty(admin.getLockKey())) {
			order.setLockKey(admin.getLockKey());
			order.setLockExpire(DateUtils.addSeconds(new Date(), Order.LOCK_EXPIRE));
		}
	}

	public void lock(Order order, Member member) {
		Assert.notNull(order);
		Assert.notNull(member);

		boolean isLocked = order.getLockExpire() != null && order.getLockExpire().after(new Date()) && StringUtils.isNotEmpty(order.getLockKey()) && !StringUtils.equals(order.getLockKey(), member.getLockKey());
		if (!isLocked && StringUtils.isNotEmpty(member.getLockKey())) {
			order.setLockKey(member.getLockKey());
			order.setLockExpire(DateUtils.addSeconds(new Date(), Order.LOCK_EXPIRE));
		}
	}

	public void undoExpiredUseCouponCode() {
		while (true) {
			List<Order> orders = orderDao.findList(null, null, null, null, null, null, true, null, null, true, 100, null, null);
			if (CollectionUtils.isNotEmpty(orders)) {
				for (Order order : orders) {
					undoUseCouponCode(order);
				}
				orderDao.flush();
				orderDao.clear();
			}
			if (orders.size() < 100) {
				break;
			}
		}
	}

	public void undoExpiredExchangePoint() {
		while (true) {
			List<Order> orders = orderDao.findList(null, null, null, null, null, null, null, true, null, true, 100, null, null);
			if (CollectionUtils.isNotEmpty(orders)) {
				for (Order order : orders) {
					undoExchangePoint(order);
				}
				orderDao.flush();
				orderDao.clear();
			}
			if (orders.size() < 100) {
				break;
			}
		}
	}

	public void releaseExpiredAllocatedStock() {
		while (true) {
			List<Order> orders = orderDao.findList(null, null, null, null, null, null, null, null, true, true, 100, null, null);
			if (CollectionUtils.isNotEmpty(orders)) {
				for (Order order : orders) {
					releaseAllocatedStock(order);
				}
				orderDao.flush();
				orderDao.clear();
			}
			if (orders.size() < 100) {
				break;
			}
		}
	}

	@Transactional(readOnly = true)
	public Order generate(Order.Type type, Cart cart, Receiver receiver, PaymentMethod paymentMethod, ShippingMethod shippingMethod, CouponCode couponCode, Invoice invoice, BigDecimal balance, String memo) {
		Assert.notNull(type);
		Assert.notNull(cart);
		Assert.notNull(cart.getMember());
		Assert.state(!cart.isEmpty());

		Setting setting = SystemUtils.getSetting();
		Member member = cart.getMember();

		Order order = new Order();
		order.setType(type);
		order.setPrice(cart.getPrice());
		order.setFee(BigDecimal.ZERO);
		order.setPromotionDiscount(cart.getDiscount());
		order.setOffsetAmount(BigDecimal.ZERO);
		order.setRefundAmount(BigDecimal.ZERO);
		order.setRewardPoint(cart.getEffectiveRewardPoint());
		order.setExchangePoint(cart.getExchangePoint());
		order.setWeight(cart.getWeight());
		order.setQuantity(cart.getQuantity());
		order.setShippedQuantity(0);
		order.setReturnedQuantity(0);
		order.setMemo(memo);
		order.setIsUseCouponCode(false);
		order.setIsExchangePoint(false);
		order.setIsAllocatedStock(false);
		order.setInvoice(setting.getIsInvoiceEnabled() ? invoice : null);
		order.setPaymentMethod(paymentMethod);
		order.setMember(member);
		order.setPromotionNames(cart.getPromotionNames());
		order.setCoupons(new ArrayList<Coupon>(cart.getCoupons()));

		if (shippingMethod != null && shippingMethod.isSupported(paymentMethod) && cart.getIsDelivery()) {
			order.setFreight(!cart.isFreeShipping() ? shippingMethodService.calculateFreight(shippingMethod, receiver, cart.getWeight()) : BigDecimal.ZERO);
			order.setShippingMethod(shippingMethod);
		} else {
			order.setFreight(BigDecimal.ZERO);
			order.setShippingMethod(null);
		}

		if (couponCode != null && cart.isCouponAllowed() && cart.isValid(couponCode)) {
			BigDecimal couponDiscount = cart.getEffectivePrice().subtract(couponCode.getCoupon().calculatePrice(cart.getEffectivePrice(), cart.getProductQuantity()));
			order.setCouponDiscount(couponDiscount.compareTo(BigDecimal.ZERO) >= 0 ? couponDiscount : BigDecimal.ZERO);
			order.setCouponCode(couponCode);
		} else {
			order.setCouponDiscount(BigDecimal.ZERO);
			order.setCouponCode(null);
		}

		order.setTax(calculateTax(order));
		order.setAmount(calculateAmount(order));

		if (balance != null && balance.compareTo(BigDecimal.ZERO) > 0 && balance.compareTo(member.getBalance()) <= 0 && balance.compareTo(order.getAmount()) <= 0) {
			order.setAmountPaid(balance);
		} else {
			order.setAmountPaid(BigDecimal.ZERO);
		}

		if (cart.getIsDelivery() && receiver != null) {
			order.setConsignee(receiver.getConsignee());
			order.setAreaName(receiver.getAreaName());
			order.setAddress(receiver.getAddress());
			order.setZipCode(receiver.getZipCode());
			order.setPhone(receiver.getPhone());
			order.setArea(receiver.getArea());
		}

		List<OrderItem> orderItems = order.getOrderItems();
		for (CartItem cartItem : cart.getCartItems()) {
			Product product = cartItem.getProduct();
			if (product != null) {
				OrderItem orderItem = new OrderItem();
				orderItem.setSn(product.getSn());
				orderItem.setName(product.getName());
				orderItem.setType(product.getType());
				orderItem.setPrice(cartItem.getPrice());
				orderItem.setWeight(product.getWeight());
				orderItem.setIsDelivery(product.getIsDelivery());
				orderItem.setThumbnail(product.getThumbnail());
				orderItem.setQuantity(cartItem.getQuantity());
				orderItem.setShippedQuantity(0);
				orderItem.setReturnedQuantity(0);
				orderItem.setProduct(cartItem.getProduct());
				orderItem.setOrder(order);
				orderItem.setSpecifications(product.getSpecifications());
				orderItems.add(orderItem);
			}
		}

		for (Product gift : cart.getGifts()) {
			OrderItem orderItem = new OrderItem();
			orderItem.setSn(gift.getSn());
			orderItem.setName(gift.getName());
			orderItem.setType(gift.getType());
			orderItem.setPrice(BigDecimal.ZERO);
			orderItem.setWeight(gift.getWeight());
			orderItem.setIsDelivery(gift.getIsDelivery());
			orderItem.setThumbnail(gift.getThumbnail());
			orderItem.setQuantity(1);
			orderItem.setShippedQuantity(0);
			orderItem.setReturnedQuantity(0);
			orderItem.setProduct(gift);
			orderItem.setOrder(order);
			orderItem.setSpecifications(gift.getSpecifications());
			orderItems.add(orderItem);
		}

		return order;
	}

	/**public Order create(Order.Type type, Cart cart, Receiver receiver, 
			PaymentMethod paymentMethod, ShippingMethod shippingMethod, 
			CouponCode couponCode, Invoice invoice, BigDecimal balance, String memo, Date reDate) {
		Assert.notNull(type);
		Assert.notNull(cart);
		Assert.notNull(cart.getMember());
		Assert.state(!cart.isEmpty());
		
		Member member = cart.getMember();
		Need need = member.getNeed();

		Map<String, BigDecimal> supplierMap = new HashMap<String, BigDecimal>();
		for (CartItem cartItem : cart.getCartItems()) {
			Product product = cartItem.getProduct();
			//if (product == null || !product.getIsMarketable() || cartItem.getQuantity() > product.getAvailableStock()) {
			//	throw new IllegalArgumentException();
			//}
			if(product != null){
				Supplier supplier = product.getGoods().getSupplier();
				SupplyNeed supplyNeedParams = new SupplyNeed();
				supplyNeedParams.setNeed(need);
				supplyNeedParams.setSupplier(supplier);
				SupplyNeed supplyNeed = supplyNeedService.findSupplyNeed(supplyNeedParams);
				if(supplyNeed == null){
					continue;
				}
				NeedProduct needProductParams = new NeedProduct();
				needProductParams.setSupplyNeed(supplyNeed);
				needProductParams.setProducts(product);
				NeedProduct needProduct = needProductService.findNeedProduct(needProductParams);
				if(needProduct == null){
					continue;
				}
				cartItem.setNewPrice(needProduct.getSupplyPrice());
				if(supplierMap.get(String.valueOf(supplier.getId())) == null){
					supplierMap.put(String.valueOf(supplier.getId()), needProduct.getSupplyPrice());
				}else{
					BigDecimal supplyPrice = (BigDecimal)supplierMap.get(String.valueOf(supplier.getId()));
					if(supplyPrice != null){
						supplierMap.put(String.valueOf(supplier.getId()), needProduct.getSupplyPrice().add(supplyPrice));
					}
				}
			}
		}

		for (Entry<String, BigDecimal> entry : supplierMap.entrySet()) {
			BigDecimal supplyPrice = entry.getValue();
			Supplier supplier = supplierService.find(Long.valueOf(entry.getKey()));
			createSingleSupplyOrder(member, supplier, supplyPrice, type, cart, receiver,
					paymentMethod, shippingMethod, couponCode, invoice, balance, memo, reDate);
		}
		if (!cart.isNew()) {
			cartDao.remove(cart);
		}
		return new Order();
	}
**/

	public Order createSingleSupplyOrder(Member member, Supplier supplier, BigDecimal supplyPrice, Order.Type type, Cart cart, Receiver receiver,
			PaymentMethod paymentMethod, ShippingMethod shippingMethod, 
			CouponCode couponCode, Invoice invoice, BigDecimal balance, String memo, Date reDate){
		Setting setting = SystemUtils.getSetting();
		Order order = new Order();
		do {
			order.setSn(snDao.generate(Sn.Type.order));
		} while (orderDao.findBySn(order.getSn()) != null);
		order.setType(type);
		order.setPrice(supplyPrice);
		order.setSupplier(supplier);
		order.setReDate(reDate);
		order.setReCode(RandomStringUtils.randomNumeric(6));
		order.setFee(BigDecimal.ZERO);
		order.setFreight(cart.getIsDelivery() && !cart.isFreeShipping() ?
				shippingMethodService.calculateFreight(shippingMethod, receiver, cart.getWeight()) : BigDecimal.ZERO);
		order.setPromotionDiscount(cart.getDiscount());
		order.setOffsetAmount(BigDecimal.ZERO);
		order.setAmountPaid(BigDecimal.ZERO);
		order.setRefundAmount(BigDecimal.ZERO);
		order.setRewardPoint(cart.getEffectiveRewardPoint());
		order.setExchangePoint(cart.getExchangePoint());
		order.setWeight(cart.getWeight());
		order.setQuantity(cart.getQuantity());
		order.setShippedQuantity(0);
		order.setReturnedQuantity(0);
		if (cart.getIsDelivery()) {
			order.setConsignee(receiver.getConsignee());
			order.setAreaName(receiver.getAreaName());
			order.setAddress(receiver.getAddress());
			order.setZipCode(receiver.getZipCode());
			order.setPhone(receiver.getPhone());
			order.setArea(receiver.getArea());
		}
		order.setMemo(memo);
		order.setIsUseCouponCode(false);
		order.setIsExchangePoint(false);
		order.setIsAllocatedStock(false);
		order.setInvoice(setting.getIsInvoiceEnabled() ? invoice : null);
		order.setShippingMethod(shippingMethod);
		order.setMember(member);
		order.setPromotionNames(cart.getPromotionNames());
		order.setCoupons(new ArrayList<Coupon>(cart.getCoupons()));

		if (couponCode != null) {
			if (!cart.isCouponAllowed() || !cart.isValid(couponCode)) {
				throw new IllegalArgumentException();
			}
			BigDecimal couponDiscount = cart.getEffectivePrice().subtract(couponCode.getCoupon().calculatePrice(cart.getEffectivePrice(), cart.getProductQuantity()));
			order.setCouponDiscount(couponDiscount.compareTo(BigDecimal.ZERO) >= 0 ? couponDiscount : BigDecimal.ZERO);
			order.setCouponCode(couponCode);
			useCouponCode(order);
		} else {
			order.setCouponDiscount(BigDecimal.ZERO);
		}

		order.setTax(calculateTax(order));
		order.setAmount(calculateAmount(order));

		if (balance != null && (balance.compareTo(BigDecimal.ZERO) < 0 || balance.compareTo(member.getBalance()) > 0 || balance.compareTo(order.getAmount()) > 0)) {
			throw new IllegalArgumentException();
		}
		BigDecimal amountPayable = balance != null ? order.getAmount().subtract(balance) : order.getAmount();
		if (amountPayable.compareTo(BigDecimal.ZERO) > 0) {
			if (paymentMethod == null) {
				throw new IllegalArgumentException();
			}
			order.setStatus(PaymentMethod.Type.deliveryAgainstPayment.equals(paymentMethod.getType()) ? Order.Status.pendingPayment : Order.Status.pendingReview);
			order.setPaymentMethod(paymentMethod);
			if (paymentMethod.getTimeout() != null && Order.Status.pendingPayment.equals(order.getStatus())) {
				order.setExpire(DateUtils.addMinutes(new Date(), paymentMethod.getTimeout()));
			}
			if (PaymentMethod.Method.online.equals(paymentMethod.getMethod())) {
				lock(order, member);
			}
		} else {
			order.setStatus(Order.Status.pendingReview);
			order.setPaymentMethod(null);
		}

		List<OrderItem> orderItems = order.getOrderItems();
		for (CartItem cartItem : cart.getCartItems()) {
			Product product = cartItem.getProduct();
			OrderItem orderItem = new OrderItem();
			orderItem.setSn(product.getSn());
			orderItem.setName(product.getName());
			orderItem.setType(product.getType());
			orderItem.setPrice(cartItem.getNewPrice());
			orderItem.setWeight(product.getWeight());
			orderItem.setIsDelivery(product.getIsDelivery());
			orderItem.setThumbnail(product.getThumbnail());
			orderItem.setQuantity(cartItem.getQuantity());
			orderItem.setShippedQuantity(0);
			orderItem.setReturnedQuantity(0);
			orderItem.setProduct(cartItem.getProduct());
			orderItem.setOrder(order);
			orderItem.setSpecifications(product.getSpecifications());
			orderItems.add(orderItem);
		}

		for (Product gift : cart.getGifts()) {
			OrderItem orderItem = new OrderItem();
			orderItem.setSn(gift.getSn());
			orderItem.setName(gift.getName());
			orderItem.setType(gift.getType());
			orderItem.setPrice(BigDecimal.ZERO);
			orderItem.setWeight(gift.getWeight());
			orderItem.setIsDelivery(gift.getIsDelivery());
			orderItem.setThumbnail(gift.getThumbnail());
			orderItem.setQuantity(1);
			orderItem.setShippedQuantity(0);
			orderItem.setReturnedQuantity(0);
			orderItem.setProduct(gift);
			orderItem.setOrder(order);
			orderItem.setSpecifications(gift.getSpecifications());
			orderItems.add(orderItem);
		}

		//生成送货码
		String deliveryCode="";
		for (int i = 0; i < 6; i++) {
			deliveryCode+=(int)(Math.random()*10);
		}
		order.setDeliveryCode(deliveryCode);
		
		orderDao.persist(order);

		OrderLog orderLog = new OrderLog();
		orderLog.setType(OrderLog.Type.create);
		orderLog.setOrder(order);
		orderLog.setLogType(LogType.member);
		orderLogDao.persist(orderLog);

		exchangePoint(order);
		if (Setting.StockAllocationTime.order.equals(setting.getStockAllocationTime())
				|| (Setting.StockAllocationTime.payment.equals(setting.getStockAllocationTime()) && (order.getAmountPaid().compareTo(BigDecimal.ZERO) > 0 || order.getExchangePoint() > 0 || order.getAmountPayable().compareTo(BigDecimal.ZERO) <= 0))) {
			allocateStock(order);
		}

		if (balance != null && balance.compareTo(BigDecimal.ZERO) > 0) {
			Payment payment = new Payment();
			payment.setMethod(Payment.Method.deposit);
			payment.setFee(BigDecimal.ZERO);
			payment.setAmount(balance);
			payment.setOrder(order);
			payment(order, payment, null);
		}
		//mailService.sendCreateOrderMail(order);
		smsService.sendCreateOrderSms(order);
		// TODO: 2017/2/14 发送模版消息
		weChatService.sendTemplateMessage(order , commonTemplateId , weChatService.getGlobalToken() , Order.OrderStatus.create) ;
		//订单创建成功,向供应商增加手机短信提醒
		smsService.sendContent(supplier.getTel() , orderNotice);
		return order;
	}

	@Value("${order.template.common.templateId}")
	private String commonTemplateId;
	
	
	@Value("${childMember.template.common.templateId}")
	private String memberTemplateId;
	
	
	

	/**
	 * public Order create(Order.Type type, Cart cart, Receiver receiver, 
			PaymentMethod paymentMethod, ShippingMethod shippingMethod, 
			CouponCode couponCode, Invoice invoice, BigDecimal balance, String memo) {
		Assert.notNull(type);
		Assert.notNull(cart);
		Assert.notNull(cart.getMember());
		Assert.state(!cart.isEmpty());
		if (cart.getIsDelivery()) {
			Assert.notNull(receiver);
			Assert.notNull(shippingMethod);
			Assert.state(shippingMethod.isSupported(paymentMethod));
		} else {
			Assert.isNull(receiver);
			Assert.isNull(shippingMethod);
		}

		for (CartItem cartItem : cart.getCartItems()) {
			Product product = cartItem.getProduct();
			if (product == null || !product.getIsMarketable() || cartItem.getQuantity() > product.getAvailableStock()) {
				throw new IllegalArgumentException();
			}
		}

		for (Product gift : cart.getGifts()) {
			if (!gift.getIsMarketable() || gift.getIsOutOfStock()) {
				throw new IllegalArgumentException();
			}
		}

		Setting setting = SystemUtils.getSetting();
		Member member = cart.getMember();

		Order order = new Order();
		order.setSn(snDao.generate(Sn.Type.order));
		order.setType(type);
		order.setPrice(cart.getPrice());
		order.setFee(BigDecimal.ZERO);
		order.setFreight(cart.getIsDelivery() && !cart.isFreeShipping() ? shippingMethodService.calculateFreight(shippingMethod, receiver, cart.getWeight()) : BigDecimal.ZERO);
		order.setPromotionDiscount(cart.getDiscount());
		order.setOffsetAmount(BigDecimal.ZERO);
		order.setAmountPaid(BigDecimal.ZERO);
		order.setRefundAmount(BigDecimal.ZERO);
		order.setRewardPoint(cart.getEffectiveRewardPoint());
		order.setExchangePoint(cart.getExchangePoint());
		order.setWeight(cart.getWeight());
		order.setQuantity(cart.getQuantity());
		order.setShippedQuantity(0);
		order.setReturnedQuantity(0);
		if (cart.getIsDelivery()) {
			order.setConsignee(receiver.getConsignee());
			order.setAreaName(receiver.getAreaName());
			order.setAddress(receiver.getAddress());
			order.setZipCode(receiver.getZipCode());
			order.setPhone(receiver.getPhone());
			order.setArea(receiver.getArea());
		}
		order.setMemo(memo);
		order.setIsUseCouponCode(false);
		order.setIsExchangePoint(false);
		order.setIsAllocatedStock(false);
		order.setInvoice(setting.getIsInvoiceEnabled() ? invoice : null);
		order.setShippingMethod(shippingMethod);
		order.setMember(member);
		order.setPromotionNames(cart.getPromotionNames());
		order.setCoupons(new ArrayList<Coupon>(cart.getCoupons()));

		if (couponCode != null) {
			if (!cart.isCouponAllowed() || !cart.isValid(couponCode)) {
				throw new IllegalArgumentException();
			}
			BigDecimal couponDiscount = cart.getEffectivePrice().subtract(couponCode.getCoupon().calculatePrice(cart.getEffectivePrice(), cart.getProductQuantity()));
			order.setCouponDiscount(couponDiscount.compareTo(BigDecimal.ZERO) >= 0 ? couponDiscount : BigDecimal.ZERO);
			order.setCouponCode(couponCode);
			useCouponCode(order);
		} else {
			order.setCouponDiscount(BigDecimal.ZERO);
		}

		order.setTax(calculateTax(order));
		order.setAmount(calculateAmount(order));

		if (balance != null && (balance.compareTo(BigDecimal.ZERO) < 0 || balance.compareTo(member.getBalance()) > 0 || balance.compareTo(order.getAmount()) > 0)) {
			throw new IllegalArgumentException();
		}
		BigDecimal amountPayable = balance != null ? order.getAmount().subtract(balance) : order.getAmount();
		if (amountPayable.compareTo(BigDecimal.ZERO) > 0) {
			if (paymentMethod == null) {
				throw new IllegalArgumentException();
			}
			order.setStatus(PaymentMethod.Type.deliveryAgainstPayment.equals(paymentMethod.getType()) ? Order.Status.pendingPayment : Order.Status.pendingReview);
			order.setPaymentMethod(paymentMethod);
			if (paymentMethod.getTimeout() != null && Order.Status.pendingPayment.equals(order.getStatus())) {
				order.setExpire(DateUtils.addMinutes(new Date(), paymentMethod.getTimeout()));
			}
			if (PaymentMethod.Method.online.equals(paymentMethod.getMethod())) {
				lock(order, member);
			}
		} else {
			order.setStatus(Order.Status.pendingReview);
			order.setPaymentMethod(null);
		}

		List<OrderItem> orderItems = order.getOrderItems();
		for (CartItem cartItem : cart.getCartItems()) {
			Product product = cartItem.getProduct();
			OrderItem orderItem = new OrderItem();
			orderItem.setSn(product.getSn());
			orderItem.setName(product.getName());
			orderItem.setType(product.getType());
			orderItem.setPrice(cartItem.getPrice());
			orderItem.setWeight(product.getWeight());
			orderItem.setIsDelivery(product.getIsDelivery());
			orderItem.setThumbnail(product.getThumbnail());
			orderItem.setQuantity(cartItem.getQuantity());
			orderItem.setShippedQuantity(0);
			orderItem.setReturnedQuantity(0);
			orderItem.setProduct(cartItem.getProduct());
			orderItem.setOrder(order);
			orderItem.setSpecifications(product.getSpecifications());
			orderItems.add(orderItem);
		}

		for (Product gift : cart.getGifts()) {
			OrderItem orderItem = new OrderItem();
			orderItem.setSn(gift.getSn());
			orderItem.setName(gift.getName());
			orderItem.setType(gift.getType());
			orderItem.setPrice(BigDecimal.ZERO);
			orderItem.setWeight(gift.getWeight());
			orderItem.setIsDelivery(gift.getIsDelivery());
			orderItem.setThumbnail(gift.getThumbnail());
			orderItem.setQuantity(1);
			orderItem.setShippedQuantity(0);
			orderItem.setReturnedQuantity(0);
			orderItem.setProduct(gift);
			orderItem.setOrder(order);
			orderItem.setSpecifications(gift.getSpecifications());
			orderItems.add(orderItem);
		}

		orderDao.persist(order);

		OrderLog orderLog = new OrderLog();
		orderLog.setType(OrderLog.Type.create);
		orderLog.setOrder(order);
		orderLogDao.persist(orderLog);

		exchangePoint(order);
		if (Setting.StockAllocationTime.order.equals(setting.getStockAllocationTime())
				|| (Setting.StockAllocationTime.payment.equals(setting.getStockAllocationTime()) && (order.getAmountPaid().compareTo(BigDecimal.ZERO) > 0 || order.getExchangePoint() > 0 || order.getAmountPayable().compareTo(BigDecimal.ZERO) <= 0))) {
			allocateStock(order);
		}

		if (balance != null && balance.compareTo(BigDecimal.ZERO) > 0) {
			Payment payment = new Payment();
			payment.setMethod(Payment.Method.deposit);
			payment.setFee(BigDecimal.ZERO);
			payment.setAmount(balance);
			payment.setOrder(order);
			payment(order, payment, null);
		}

		mailService.sendCreateOrderMail(order);
		smsService.sendCreateOrderSms(order);

		if (!cart.isNew()) {
			cartDao.remove(cart);
		}
		return order;
	}
	 */

	public void update(Order order, Admin operator) {
		Assert.notNull(order);
		Assert.isTrue(!order.isNew());
		Assert.state(!order.hasExpired() && (Order.Status.pendingPayment.equals(order.getStatus()) || Order.Status.pendingReview.equals(order.getStatus())));

		order.setAmount(calculateAmount(order));
		if (order.getAmountPayable().compareTo(BigDecimal.ZERO) <= 0) {
			order.setStatus(Order.Status.pendingReview);
			order.setExpire(null);
		} else {
			if (order.getPaymentMethod() != null && PaymentMethod.Type.deliveryAgainstPayment.equals(order.getPaymentMethod().getType())) {
				order.setStatus(Order.Status.pendingPayment);
			} else {
				order.setStatus(Order.Status.pendingReview);
				order.setExpire(null);
			}
		}

		OrderLog orderLog = new OrderLog();
		orderLog.setType(OrderLog.Type.update);
		orderLog.setOperator(operator);
		orderLog.setOrder(order);
		orderLogDao.persist(orderLog);

		mailService.sendUpdateOrderMail(order);
		smsService.sendUpdateOrderSms(order);
	}

	public void cancel(Order order , Admin admin) {
		Assert.notNull(order);
		Assert.isTrue(!order.isNew());
		Assert.state(!order.hasExpired() && !(Order.Status.denied.equals(order.getStatus()) || Order.Status.completed.equals(order.getStatus())));

		order.setStatus(Order.Status.canceled);
		order.setExpire(null);

		undoUseCouponCode(order);
		undoExchangePoint(order);
		releaseAllocatedStock(order);

		OrderLog orderLog = new OrderLog();
		orderLog.setType(OrderLog.Type.cancel);
		orderLog.setOrder(order);
		orderLog.setOperator(admin);
		//添加企业名称 
		orderLog.setSupplierName(admin.getSupplier().getName());
		if (order.getToSupplier()==null) {
			orderLog.setLogType(LogType.distributor);
		}else if (order.getToSupplier().getId().equals(admin.getSupplier().getId())) {
			orderLog.setLogType(LogType.distributor);
		}else if (order.getSupplier().getId().equals(admin.getSupplier().getId())) {
			orderLog.setLogType(LogType.supplier);
		}
		orderLogDao.persist(orderLog);
		
		//后台消息推送
		Supplier supplier = order.getSupplier();
		if(order.getType().equals(Order.Type.billDistribution)) {
			orderNewsPushDao.addOrderNewPush(supplier, order, OrderNewsPush.OrderStatus.cancel, order.getNeed(), supplier.getName(), "",OrderNewsPush.NoticeObject.order);
			orderNewsPushDao.addOrderNewPush(order.getToSupplier(), order, OrderNewsPush.OrderStatus.cancel, order.getNeed(), supplier.getName(), "",OrderNewsPush.NoticeObject.purchase);
			orderNewsPushDao.addOrderNewPush(order.getToSupplier(), order, OrderNewsPush.OrderStatus.cancel, order.getNeed(), supplier.getName(), "",OrderNewsPush.NoticeObject.purchase);
		}else if(order.getType() == Order.Type.formal) {
			orderNewsPushDao.addOrderNewPush(supplier, order, OrderNewsPush.OrderStatus.cancel, order.getNeed(), supplier.getName(), "",OrderNewsPush.NoticeObject.order);
			orderNewsPushDao.addOrderNewPush(order.getToSupplier(), order, OrderNewsPush.OrderStatus.cancel, order.getNeed(), supplier.getName(), "",OrderNewsPush.NoticeObject.purchase);
		}else {
			orderNewsPushDao.addOrderNewPush(supplier, order, OrderNewsPush.OrderStatus.cancel, order.getNeed(), supplier.getName(), "",OrderNewsPush.NoticeObject.order);
		}

		mailService.sendCancelOrderMail(order);
		smsService.sendCancelOrderSms(order);

		//增加消息接受员通知
		//weChatService.sendTemplateMessageToNoticeUserPurchase(order , Order.OrderStatus.canceled , commonTemplateId , weChatService.getGlobalToken() , NoticeTypePurchase.Type.order_cancel , "");

		weChatService.sendTemplateMessageByOrderStatus(order , Order.OrderStatus.canceled ,  weChatService.getGlobalToken() , null , commonTemplateId , null , null);
	}

	public void review(Order order, boolean passed, Admin operator) {
		Assert.notNull(order);
		Assert.isTrue(!order.isNew());
		Assert.state(!order.hasExpired() && Order.Status.pendingReview.equals(order.getStatus()));

		if (passed) {
			order.setStatus(Order.Status.pendingShipment);
		} else {
			order.setStatus(Order.Status.denied);

			undoUseCouponCode(order);
			undoExchangePoint(order);
			releaseAllocatedStock(order);
		}

		OrderLog orderLog = new OrderLog();
		orderLog.setType(OrderLog.Type.review);
		orderLog.setOperator(operator);
		orderLog.setOrder(order);
		if (order.getToSupplier()==null) {
			orderLog.setLogType(LogType.distributor);
		}else {
			orderLog.setLogType(LogType.supplier);
		}
		orderLogDao.persist(orderLog);

		mailService.sendReviewOrderMail(order);
		smsService.sendReviewOrderSms(order);
	}

	public void payment(Order order, Payment payment, Admin operator) {
		Assert.notNull(order);
		Assert.isTrue(!order.isNew());
		Assert.notNull(payment);
		Assert.isTrue(payment.isNew());
		Assert.notNull(payment.getAmount());
		Assert.state(payment.getAmount().compareTo(BigDecimal.ZERO) > 0);

		payment.setSn(snDao.generate(Sn.Type.payment));
		payment.setOrder(order);
		paymentDao.persist(payment);

		if (order.getMember() != null && Payment.Method.deposit.equals(payment.getMethod())) {
			memberService.addBalance(order.getMember(), payment.getEffectiveAmount().negate(), DepositLog.Type.payment, operator, null);
		}

		Setting setting = SystemUtils.getSetting();
		if (Setting.StockAllocationTime.payment.equals(setting.getStockAllocationTime())) {
			allocateStock(order);
		}

		order.setAmountPaid(order.getAmountPaid().add(payment.getEffectiveAmount()));
		order.setFee(order.getFee().add(payment.getFee()));
		if (!order.hasExpired() && Order.Status.pendingPayment.equals(order.getStatus()) && order.getAmountPayable().compareTo(BigDecimal.ZERO) <= 0) {
			order.setStatus(Order.Status.pendingReview);
			order.setExpire(null);
		}

		OrderLog orderLog = new OrderLog();
		orderLog.setType(OrderLog.Type.payment);
		orderLog.setOperator(operator);
		orderLog.setOrder(order);
		orderLogDao.persist(orderLog);

		mailService.sendPaymentOrderMail(order);
		smsService.sendPaymentOrderSms(order);
	}

	public void refunds(Order order, Refunds refunds, Admin operator) {
		Assert.notNull(order);
		Assert.isTrue(!order.isNew());
		Assert.state(order.getRefundableAmount().compareTo(BigDecimal.ZERO) > 0);
		Assert.notNull(refunds);
		Assert.isTrue(refunds.isNew());
		Assert.notNull(refunds.getAmount());
		Assert.state(refunds.getAmount().compareTo(BigDecimal.ZERO) > 0 && refunds.getAmount().compareTo(order.getRefundableAmount()) <= 0);

		refunds.setSn(snDao.generate(Sn.Type.refunds));
		refunds.setOrder(order);
		refundsDao.persist(refunds);

		if (Refunds.Method.deposit.equals(refunds.getMethod())) {
			memberService.addBalance(order.getMember(), refunds.getAmount(), DepositLog.Type.refunds, operator, null);
		}

		order.setAmountPaid(order.getAmountPaid().subtract(refunds.getAmount()));
		order.setRefundAmount(order.getRefundAmount().add(refunds.getAmount()));

		OrderLog orderLog = new OrderLog();
		orderLog.setType(OrderLog.Type.refunds);
		orderLog.setOperator(operator);
		orderLog.setOrder(order);
		orderLogDao.persist(orderLog);

		mailService.sendRefundsOrderMail(order);
		smsService.sendRefundsOrderSms(order);
	}

	public void shipping(Order order, Shipping shipping, Admin operator) {
		Assert.notNull(order);
		Assert.isTrue(!order.isNew());
		Assert.state(order.getShippableQuantity() > 0);
		Assert.notNull(shipping);
		Assert.isTrue(shipping.isNew());
		Assert.notEmpty(shipping.getShippingItems());

		shipping.setSn(snDao.generate(Sn.Type.shipping));
		shipping.setOrder(order);
		shippingDao.persist(shipping);

		Setting setting = SystemUtils.getSetting();
		if (Setting.StockAllocationTime.ship.equals(setting.getStockAllocationTime())) {
			allocateStock(order);
		}

		for (ShippingItem shippingItem : shipping.getShippingItems()) {
			OrderItem orderItem = order.getOrderItem(shippingItem.getSn());
			if (orderItem == null || shippingItem.getQuantity() > orderItem.getShippableQuantity()) {
				throw new IllegalArgumentException();
			}
			orderItem.setShippedQuantity(orderItem.getShippedQuantity() + shippingItem.getQuantity());
			Product product = shippingItem.getProduct();
			if (product != null) {
				if (shippingItem.getQuantity() > product.getStock()) {
					throw new IllegalArgumentException();
				}
				productService.addStock(product, -shippingItem.getQuantity(), StockLog.Type.stockOut, operator, null);
				if (BooleanUtils.isTrue(order.getIsAllocatedStock())) {
					productService.addAllocatedStock(product, -shippingItem.getQuantity());
				}
			}
		}

		// TODO: 2017/2/16 处理图片


		order.setShippedQuantity(order.getShippedQuantity() + shipping.getQuantity());
		if (order.getShippedQuantity() >= order.getQuantity()) {
			order.setStatus(Order.Status.shipped);
			order.setIsAllocatedStock(false);
		}

		OrderLog orderLog = new OrderLog();
		orderLog.setType(OrderLog.Type.shipping);
		orderLog.setOperator(operator);
		orderLog.setOrder(order);
		if (order.getToSupplier() == null) {
			orderLog.setLogType(LogType.distributor);
		}else {
			orderLog.setLogType(LogType.supplier);
		}
		orderLogDao.persist(orderLog);

		mailService.sendShippingOrderMail(order);
		smsService.sendShippingOrderSms(order);
	}

	public void returns(Order order, Returns returns, Admin operator) {
		Assert.notNull(order);
		Assert.isTrue(!order.isNew());
		Assert.state(order.getReturnableQuantity() > 0);
		Assert.notNull(returns);
		Assert.isTrue(returns.isNew());
		Assert.notEmpty(returns.getReturnsItems());

		returns.setSn(snDao.generate(Sn.Type.returns));
		returns.setOrder(order);
		returnsDao.persist(returns);

		for (ReturnsItem returnsItem : returns.getReturnsItems()) {
			OrderItem orderItem = order.getOrderItem(returnsItem.getSn());
			if (orderItem == null || returnsItem.getQuantity() > orderItem.getReturnableQuantity()) {
				throw new IllegalArgumentException();
			}
			orderItem.setReturnedQuantity(orderItem.getReturnedQuantity() + returnsItem.getQuantity());
		}

		order.setReturnedQuantity(order.getReturnedQuantity() + returns.getQuantity());

		OrderLog orderLog = new OrderLog();
		orderLog.setType(OrderLog.Type.returns);
		orderLog.setOperator(operator);
		orderLog.setOrder(order);
		orderLogDao.persist(orderLog);

		mailService.sendReturnsOrderMail(order);
		smsService.sendReturnsOrderSms(order);
	}

	public void receive(Order order, Admin operator) {
		Assert.notNull(order);
		Assert.isTrue(!order.isNew());
		Assert.state(!order.hasExpired() && Order.Status.shipped.equals(order.getStatus()));

		order.setStatus(Order.Status.received);

		OrderLog orderLog = new OrderLog();
		orderLog.setType(OrderLog.Type.receive);
		orderLog.setOperator(operator);
		orderLog.setOrder(order);
		orderLogDao.persist(orderLog);

		mailService.sendReceiveOrderMail(order);
		smsService.sendReceiveOrderSms(order);
	}

	public void complete(Order order, Admin operator) {
		Assert.notNull(order);
		Assert.isTrue(!order.isNew());
		Assert.state(!order.hasExpired()/* && Order.Status.received.equals(order.getStatus())*/);

		Member member = order.getMember();
		Boolean isShoper=false;
		//产品销量增加
		for (OrderItem orderItem : order.getOrderItems()) {
			Product product = orderItem.getProduct();
			isShoper=product.getGoods().getIs2Member();
			if (product != null && product.getGoods() != null) {
				goodsService.addSales(product.getGoods(), orderItem.getQuantity());
			}
		}
		member.setIsShoper(isShoper);
		//奖励积分
		if (order.getRewardPoint() > 0) {
			memberService.addPoint(member, order.getRewardPoint(), PointLog.Type.reward, operator, null);
		}
		//使用的优惠卷
		if (CollectionUtils.isNotEmpty(order.getCoupons())) {
			for (Coupon coupon : order.getCoupons()) {
				couponCodeService.generate(coupon, member);
			}
		}
		//已付金额累加
		if (order.getAmountPaid().compareTo(BigDecimal.ZERO) > 0) {
			memberService.addAmount(member, order.getAmountPaid());
		}
		//产品销量增加
		for (OrderItem orderItem : order.getOrderItems()) {
			Product product = orderItem.getProduct();
			if (product != null && product.getGoods() != null) {
				goodsService.addSales(product.getGoods(), orderItem.getQuantity());
			}
		}

		order.setStatus(Order.Status.completed);
		order.setCompleteDate(new Date());

		OrderLog orderLog = new OrderLog();
		orderLog.setType(OrderLog.Type.complete);
		orderLog.setOperator(operator);
		orderLog.setOrder(order);
		
		orderLog.setSupplierName(operator.getSupplier().getName());
		if (order.getToSupplier()==null) {
			orderLog.setLogType(LogType.distributor);
		}else if (order.getToSupplier().getId().equals(operator.getSupplier().getId())) {
			orderLog.setLogType(LogType.distributor);
		}else if (order.getSupplier().getId().equals(operator.getSupplier().getId())) {
			orderLog.setLogType(LogType.supplier);
		}
		
		orderLogDao.persist(orderLog);
		
		//分销结算
		distributionSettlement(order);
		
		//后台消息通知
		Supplier supplier = order.getSupplier();
		if(order.getType().equals(Order.Type.billDistribution)) {
			orderNewsPushDao.addOrderNewPush(supplier, order, OrderNewsPush.OrderStatus.complete, order.getNeed(), supplier.getName(), "",OrderNewsPush.NoticeObject.order);
			orderNewsPushDao.addOrderNewPush(order.getToSupplier(), order, OrderNewsPush.OrderStatus.complete, order.getNeed(), supplier.getName(), "",OrderNewsPush.NoticeObject.purchase);
			orderNewsPushDao.addOrderNewPush(order.getToSupplier(), order, OrderNewsPush.OrderStatus.complete, order.getNeed(), supplier.getName(), "",OrderNewsPush.NoticeObject.order);
		}else if(order.getType() ==Order.Type.formal) {
			orderNewsPushDao.addOrderNewPush(supplier, order, OrderNewsPush.OrderStatus.complete, order.getNeed(), supplier.getName(), "",OrderNewsPush.NoticeObject.order);
			orderNewsPushDao.addOrderNewPush(order.getToSupplier(), order, OrderNewsPush.OrderStatus.complete, order.getNeed(), supplier.getName(), "",OrderNewsPush.NoticeObject.purchase);
		}else {
			orderNewsPushDao.addOrderNewPush(supplier, order, OrderNewsPush.OrderStatus.complete, order.getNeed(), supplier.getName(), "",OrderNewsPush.NoticeObject.order);
		}

		mailService.sendCompleteOrderMail(order);
		smsService.sendCompleteOrderSms(order);

		//订货单消息接受员接受消息
		//weChatService.sendTemplateMessageToNoticeUser(order.getSupplier() , order , Order.OrderStatus.completed , commonTemplateId ,  weChatService.getGlobalToken()) ;

		//weChatService.sendTemplateMessageByOrderStatus(order , Order.OrderStatus.completed ,  weChatService.getGlobalToken() , null , commonTemplateId , null , null);
	}

	public void fail(Order order, Admin operator) {
		Assert.notNull(order);
		Assert.isTrue(!order.isNew());
		Assert.state(!order.hasExpired() && (Order.Status.pendingShipment.equals(order.getStatus()) || Order.Status.shipped.equals(order.getStatus()) || Order.Status.received.equals(order.getStatus())));

		order.setStatus(Order.Status.failed);

		undoUseCouponCode(order);
		undoExchangePoint(order);
		releaseAllocatedStock(order);

		OrderLog orderLog = new OrderLog();
		orderLog.setType(OrderLog.Type.fail);
		orderLog.setOperator(operator);
		orderLog.setOrder(order);
		orderLogDao.persist(orderLog);

		mailService.sendFailOrderMail(order);
		smsService.sendFailOrderSms(order);
	}

	@Override
	@Transactional
	public void delete(Order order) {
		if (order != null && !Order.Status.completed.equals(order.getStatus())) {
			undoUseCouponCode(order);
			undoExchangePoint(order);
			releaseAllocatedStock(order);
		}

		super.delete(order);
	}

	private void useCouponCode(Order order) {
		if (order == null || BooleanUtils.isNotFalse(order.getIsUseCouponCode()) || order.getCouponCode() == null) {
			return;
		}
		CouponCode couponCode = order.getCouponCode();
		couponCode.setIsUsed(true);
		couponCode.setUsedDate(new Date());
		order.setIsUseCouponCode(true);
	}

	private void undoUseCouponCode(Order order) {
		if (order == null || BooleanUtils.isNotTrue(order.getIsUseCouponCode()) || order.getCouponCode() == null) {
			return;
		}
		CouponCode couponCode = order.getCouponCode();
		couponCode.setIsUsed(false);
		couponCode.setUsedDate(null);
		order.setIsUseCouponCode(false);
		order.setCouponCode(null);
	}

	private void exchangePoint(Order order) {
		if (order == null || BooleanUtils.isNotFalse(order.getIsExchangePoint()) || order.getExchangePoint() <= 0 || order.getMember() == null) {
			return;
		}
		memberService.addPoint(order.getMember(), -order.getExchangePoint(), PointLog.Type.exchange, null, null);
		order.setIsExchangePoint(true);
	}

	private void undoExchangePoint(Order order) {
		if (order == null || BooleanUtils.isNotTrue(order.getIsExchangePoint()) || order.getExchangePoint() <= 0 || order.getMember() == null) {
			return;
		}
		memberService.addPoint(order.getMember(), order.getExchangePoint(), PointLog.Type.undoExchange, null, null);
		order.setIsExchangePoint(false);
	}

	private void allocateStock(Order order) {
		if (order == null || BooleanUtils.isNotFalse(order.getIsAllocatedStock())) {
			return;
		}
		if (order.getOrderItems() != null) {
			for (OrderItem orderItem : order.getOrderItems()) {
				Product product = orderItem.getProduct();
				if (product != null) {
					productService.addAllocatedStock(product, orderItem.getQuantity() - orderItem.getShippedQuantity());
				}
			}
		}
		order.setIsAllocatedStock(true);
	}

	private void releaseAllocatedStock(Order order) {
		if (order == null || BooleanUtils.isNotTrue(order.getIsAllocatedStock())) {
			return;
		}
		if (order.getOrderItems() != null) {
			for (OrderItem orderItem : order.getOrderItems()) {
				Product product = orderItem.getProduct();
				if (product != null) {
					productService.addAllocatedStock(product, -(orderItem.getQuantity() - orderItem.getShippedQuantity()));
				}
			}
		}
		order.setIsAllocatedStock(false);
	}

	@Override
	public Page<Order> findPage(Order.Type type, Order.Status status,Order.Status[] reportStatus,
			Member member, Goods goods, Boolean isPendingReceive, Boolean isPendingRefunds,
			Boolean isUseCouponCode, Boolean isExchangePoint, Boolean isAllocatedStock, Boolean hasExpired,
			Pageable pageable, Supplier supplier , Date startDate , Date endDate ,
			String searchName , String timeSearch, ChildMember childMember) {
		Page<Order> orders = orderDao.findPage(type, status,reportStatus, member, goods, 
				isPendingReceive, isPendingRefunds, isUseCouponCode, isExchangePoint,
				isAllocatedStock, hasExpired, pageable , supplier , startDate , endDate , 
				searchName , timeSearch, childMember);
		return orders;
	}
	
	@Override
	public Page<Order> findPageByMember(Order.Status status,List<Shop> shops,Pageable pageable) {
		Page<Order> orders;
		if (CollectionUtils.isEmpty(shops)) {
			orders=new Page<>();
		}else {
			orders=orderDao.findPageByMember( status,shops,pageable);
		}
		return orders;
	}

	@Override
	public Page<Order> findPageOrderLocal(String sharingStatus,List<Shop> shops,Shop shop, ChildMember childMember, Pageable pageable) {
		Page<Order> orders;
		if (shop == null && CollectionUtils.isEmpty(shops)) {
			orders=new Page<>();
		}else {
			orders= orderDao.findPageOrderLocal( sharingStatus,shops,shop,childMember,pageable);
		}
		return orders;
	}


	@Override
	public void shipping(Order order, Shipping shipping, Admin operator, List<OrderFile> orderFiles) {
		Assert.notNull(order);
		Assert.isTrue(!order.isNew());
		Assert.state(order.getShippableQuantity() > 0);
		Assert.notNull(shipping);
		Assert.isTrue(shipping.isNew());
		Assert.notEmpty(shipping.getShippingItems());

		shipping.setSn(snDao.generate(Sn.Type.shipping));

        //生成六位送货码
        shipping.setDeliveryCode(RandomStringUtils.randomNumeric(6));
        //设置物流状态
        shipping.setStatus(Shipping.Status.waitCustomerCheck);

		shipping.setOrder(order);
		shippingDao.persist(shipping);

		Setting setting = SystemUtils.getSetting();
		if (Setting.StockAllocationTime.ship.equals(setting.getStockAllocationTime())) {
			allocateStock(order);
		}

		for (ShippingItem shippingItem : shipping.getShippingItems()) {
			OrderItem orderItem = order.getOrderItem(shippingItem.getSn());
			if (orderItem == null || shippingItem.getQuantity() > orderItem.getShippableQuantity()) {
				throw new IllegalArgumentException();
			}
			orderItem.setShippedQuantity(orderItem.getShippedQuantity() + shippingItem.getQuantity());
			Product product = shippingItem.getProduct();
			if (product != null) {
				if (shippingItem.getQuantity() > product.getStock()) {
					throw new IllegalArgumentException();
				}
				productService.addStock(product, -shippingItem.getQuantity(), StockLog.Type.stockOut, operator, null);
				if (BooleanUtils.isTrue(order.getIsAllocatedStock())) {
					productService.addAllocatedStock(product, -shippingItem.getQuantity());
				}
			}
		}

		// TODO: 2017/2/16 处理图片
		orderFileService.generate(orderFiles , true);

		// FIXME: 2017/4/5 修改附件覆盖问题 ， addAll 效率不高？
		List<OrderFile> beforeOrderFiles = order.getOrderFiles();
		beforeOrderFiles.addAll(orderFiles) ;
		order.setOrderFiles(beforeOrderFiles);

		order.setShippedQuantity(order.getShippedQuantity() + shipping.getQuantity());
		if (order.getShippedQuantity() >= order.getQuantity()) {
			order.setStatus(Order.Status.shipped);
			order.setIsAllocatedStock(false);
		}else{
			//发货中
			order.setStatus(Order.Status.inShipment);
			order.setIsAllocatedStock(false);
		}

		OrderLog orderLog = new OrderLog();
		orderLog.setType(OrderLog.Type.shipping);
		orderLog.setOperator(operator);
		orderLog.setOrder(order);
		if (order.getToSupplier() == null) {
			orderLog.setLogType(LogType.distributor);
		}else {
			orderLog.setLogType(LogType.supplier);
		}
		orderLogDao.persist(orderLog);
		
		//后台消息推送
		Supplier supplier = order.getSupplier();
		if(order.getType().equals(Order.Type.billDistribution)) {
			orderNewsPushDao.addOrderNewPush(supplier, order, OrderNewsPush.OrderStatus.deliverGoods, order.getNeed(), supplier.getName(), "",OrderNewsPush.NoticeObject.order);
			orderNewsPushDao.addOrderNewPush(order.getToSupplier(), order, OrderNewsPush.OrderStatus.deliverGoods, order.getNeed(), supplier.getName(), "",OrderNewsPush.NoticeObject.purchase);
			orderNewsPushDao.addOrderNewPush(order.getToSupplier(), order, OrderNewsPush.OrderStatus.deliverGoods, order.getNeed(), supplier.getName(), "",OrderNewsPush.NoticeObject.order);
		}else if(order.getType() == Order.Type.formal) {
			orderNewsPushDao.addOrderNewPush(supplier, order, OrderNewsPush.OrderStatus.deliverGoods, order.getNeed(), supplier.getName(), "",OrderNewsPush.NoticeObject.order);
			orderNewsPushDao.addOrderNewPush(order.getToSupplier(), order, OrderNewsPush.OrderStatus.deliverGoods, order.getNeed(), supplier.getName(), "",OrderNewsPush.NoticeObject.purchase);
		}else {
			orderNewsPushDao.addOrderNewPush(supplier, order, OrderNewsPush.OrderStatus.deliverGoods, order.getNeed(), supplier.getName(), "",OrderNewsPush.NoticeObject.order);
		}

		/*mailService.sendShippingOrderMail(order);
		smsService.sendShippingOrderSms(order);*/

		//发送模版消息
		weChatService.sendTemplateMessageByOrderStatus(order , Order.OrderStatus.shipped ,  weChatService.getGlobalToken() , null , commonTemplateId , null , null);

	}


	@Override
	public void review(Order order, boolean passed, Admin operator, List<OrderItem> orderItems ,
			String deniedReason  , Date reDate) {
		Assert.notNull(order);
		Assert.isTrue(!order.isNew());
		Assert.state(!order.hasExpired() && Order.Status.pendingReview.equals(order.getStatus()));
		boolean isUpd = false ;
		boolean dataChange = false ;
		if (passed) {
			//处理订单相关数量已经金额等数据
			Map<Long , OrderItem> checkedOrderItem = new HashMap<>(orderItems.size());
			BigDecimal amount = new BigDecimal(0) ;

			for(OrderItem orderItem : orderItems){
				checkedOrderItem.put(orderItem.getId() , orderItem) ;
			}

			List<OrderItem> before = order.getOrderItems() ;

			Integer allQuantity =  0;
			for(OrderItem orderItem : before){
				Integer updQuantity = checkedOrderItem.get(orderItem.getId()).getCheckQuantity() ;
				Integer beforeQuantity = orderItem.getQuantity() ;
				if(updQuantity.compareTo(beforeQuantity) != 0){
					//如果数量有变动的话， 则进行商品库存分配修改
					productService.addAllocatedStock(orderItem.getProduct() , (updQuantity - beforeQuantity));
					isUpd = true ;
				}
				orderItem.setQuantity(updQuantity);
				allQuantity += updQuantity ;
			}

			BigDecimal price = this.getSupplyPrice(before);

			dataChange = reDate.compareTo(order.getReDate()) == 0 ? false : true ;

			order.setStatus(Order.Status.pendingShipment);
			order.setAmount(price);
			order.setPrice(price);
			order.setReDate(reDate);
			order.setQuantity(allQuantity);


		} else {
			order.setStatus(Order.Status.denied);
			order.setDeniedReason(deniedReason);

			undoUseCouponCode(order);
			undoExchangePoint(order);
			releaseAllocatedStock(order);
		}

		OrderLog orderLog = new OrderLog();
		orderLog.setType(OrderLog.Type.review);
		orderLog.setOperator(operator);
		orderLog.setOrder(order);
		if (order.getToSupplier() == null) {
			orderLog.setLogType(LogType.distributor);
		}else {
			orderLog.setLogType(LogType.supplier);
		}
		orderLogDao.persist(orderLog);

		mailService.sendReviewOrderMail(order);
		smsService.sendReviewOrderSms(order);

		if (!passed) {

			// TODO: 2017/2/14 发送模版消息
			weChatService.sendTemplateMessage(order , commonTemplateId , weChatService.getGlobalToken() , Order.OrderStatus.denied) ;

		}else{

			String remark = "您的订单已通过审核！" ;
			if(isUpd || dataChange){
				remark = "您的订单数据有变更，请查看订单详情！" ;
			}
			weChatService.sendTemplateMessage(order , commonTemplateId , weChatService.getGlobalToken() , Order.OrderStatus.passed , remark) ;

		}


	}


	private BigDecimal getSupplyPrice(List<OrderItem> orderItems){
		BigDecimal allPrice = new BigDecimal(0) ;
		for(OrderItem orderItem : orderItems){
            allPrice = allPrice.add(orderItem.getSubtotal());
		}
		return allPrice ;
	}

	private Integer getAllQuantity(List<OrderItem> orderItems){
		Integer allQuantity = 0 ;
		for(OrderItem orderItem : orderItems){
			allQuantity +=orderItem.getQuantity() ;
		}
		return allQuantity ;
	}


	@Transactional
	@Override
	public Order create(Cart cart, Shop shop, SupplierType supplierType, 
			Types types, Need need, Set<CartItem> cartItems,String itemIds, PaymentMethod paymentMethod, 
			ShippingMethod shippingMethod, CouponCode couponCode, Invoice invoice, 
			BigDecimal balance, String memo, Date reDate, Long supplierId, 
			SupplyType supplyType , ChildMember childMember, Long relationId, Receiver receiver) {

		Assert.notNull(cart);
		Assert.notNull(cart.getMember());
		Assert.state(!cart.isEmpty());

		Member buyMember = cart.getMember();
        BigDecimal totalPrice = BigDecimal.ZERO ;
        BigDecimal totalPriceB = BigDecimal.ZERO ;

        Supplier supplier = supplierDao.find(supplierId) ;//品牌商企业
//        Supplier bySupplier = need.getSupplier() ;
        Date now = new Date();
        SupplyNeed supplyNeed = null;
        SupplierSupplier supplierSupplier=null;
        // FIXME: 2017/3/21 这里的写法有问题，需要进行优化
        List<CartItem> cartItemList=new ArrayList<>();
		Integer sumQuantity=0;
		//处理购物车商品金额
		String[] ids=StringUtils.split(itemIds,",");
		for (CartItem cartItem : cartItems) {
			//不包含的项不加入
			if(!ArrayUtils.contains(ids, cartItem.getId().toString())) {
				continue;
			}
			if (!cartItem.getValid()) {
				continue;
			}
			Product product = cartItem.getProduct();
			Goods goods = product.getGoods();
			if(goods.getSales() == null){
				goods.setSales(0l);
			}
			if(product.getSales() == null){
				product.setSales(0l);
			}
			goodsService.update(goods);
			logger.info("update goods sales....");
			
			//更新product sku销量
//			product.setSales(product.getSales() + Long.valueOf(cartItem.getQuantity()));
			productService.update(product);
//			logger.info("update product sales....");
			
			
			BigDecimal proPrice = product.getPrice().multiply(new BigDecimal(cartItem.getQuantity()));
			cartItem.setNewPrice(proPrice);
			cartItem.setNewPriceUnit(product.getPrice());
			cartItem.setNewBToBPrice(proPrice);
			cartItem.setNewBToBPriceUnit(product.getPrice());
			totalPrice = totalPrice.add(proPrice);
			totalPriceB=totalPriceB.add(proPrice);
			cartItemList.add(cartItem);
			sumQuantity+=cartItem.getQuantity();
		}
		
		

       Order order= createSingleBaseOrder(buyMember, supplier, cartItemList, totalPrice, totalPriceB, cart,
                paymentMethod, shippingMethod, couponCode, invoice, balance, memo, 
                reDate , supplyType , childMember, receiver, supplierType, types, sumQuantity);

		/**
		 * 订单成功后，删除购物车中的购物项
		 */
		for (CartItem cartItem : cartItemList) {
			//不包含的项不加入
			if(!ArrayUtils.contains(ids, cartItem.getId().toString())) {
				continue;
			}
			cartItemDao.remove(cartItem);
		}
		return order;

	}
	
	
	private Order createSingleBaseOrder(Member buyMember, Supplier supplier, List<CartItem> cartItemList, 
			BigDecimal price, BigDecimal priceB, Cart cart,
			PaymentMethod paymentMethod, ShippingMethod shippingMethod,
            CouponCode couponCode, Invoice invoice, 
            BigDecimal balance, String memo, Date reDate , 
             SupplyType supplyType , ChildMember childMember, 
             Receiver receiver, SupplierType supplierType, Types types, Integer sumQuantity){
			String logName="";//操作人名称  如果为员工则为员工姓名   
			Setting setting = SystemUtils.getSetting();
			Order order = new Order();
			order.setChildMember(childMember);
			do {
			order.setSn(snDao.generate(Sn.Type.order));
			//本地订单编号加B
			if (types == Types.local) {
				order.setSn("B"+order.getSn());
			}
			} while (orderDao.findBySn(order.getSn()) != null);
			order.setPrice(price);
			order.setSupplier(supplier);
			order.setReDate(reDate);
			order.setReCode(RandomStringUtils.randomNumeric(6));
			order.setFee(BigDecimal.ZERO);
			order.setFreight(cart.getIsDelivery() && !cart.isFreeShipping() ?
			    shippingMethodService.calculateFreight(shippingMethod, receiver.getArea(), cart.getWeight()) : BigDecimal.ZERO);
			order.setPromotionDiscount(cart.getDiscount());
			order.setOffsetAmount(BigDecimal.ZERO);
			order.setAmountPaid(BigDecimal.ZERO);
			order.setRefundAmount(BigDecimal.ZERO);
			order.setRewardPoint(cart.getEffectiveRewardPoint());
			order.setExchangePoint(cart.getExchangePoint());
			order.setWeight(cart.getWeight());
			order.setQuantity(sumQuantity);
			order.setShippedQuantity(0);
			order.setReturnedQuantity(0);
			if (cart.getIsDelivery()) {
				order.setConsignee(receiver.getConsignee());
				order.setAreaName(receiver.getAreaName());
				order.setAddress(receiver.getAddress());
				order.setZipCode(receiver.getZipCode());
				order.setPhone(receiver.getPhone());
				order.setArea(receiver.getArea());
			}
			order.setAreaName(receiver.getArea().getFullName());
			order.setAddress(receiver.getAddress());
			order.setZipCode("200000");
			order.setMemo(memo);
			order.setIsUseCouponCode(false);
			order.setIsExchangePoint(false);
			order.setIsAllocatedStock(false);
			order.setInvoice(setting.getIsInvoiceEnabled() ? invoice : null);
			order.setShippingMethod(shippingMethod);
			order.setMember(buyMember);
			order.setBuyMember(buyMember);
			order.setPromotionNames(cart.getPromotionNames());
			order.setCoupons(new ArrayList<>(cart.getCoupons()));
			if (Types.local.equals(types)){
				order.setType(Type.local);
				order.setSharingStatus(Order.SharingStatus.noshare);
			}else {
				order.setType(Order.Type.general);
			}
			//正常下单
			order.setBuyType(Order.BuyType.general);
			
			if (couponCode != null) {
			if (!cart.isCouponAllowed() || !cart.isValid(couponCode)) {
			    throw new IllegalArgumentException();
			}
			BigDecimal couponDiscount = cart.getEffectivePrice().subtract(couponCode.getCoupon().calculatePrice(cart.getEffectivePrice(), cart.getProductQuantity()));
			order.setCouponDiscount(couponDiscount.compareTo(BigDecimal.ZERO) >= 0 ? couponDiscount : BigDecimal.ZERO);
			order.setCouponCode(couponCode);
			useCouponCode(order);
			} else {
			order.setCouponDiscount(BigDecimal.ZERO);
			}
			
			order.setTax(calculateTax(order));
			order.setAmount(price);
			order.setAmountToB(priceB);
			order.setAmount(calculateAmount(order));
			
			if (balance != null && (balance.compareTo(BigDecimal.ZERO) < 0 || balance.compareTo(buyMember.getBalance()) > 0 || balance.compareTo(order.getAmount()) > 0)) {
			throw new IllegalArgumentException();
			}
			BigDecimal amountPayable = balance != null ? order.getAmount().subtract(balance) : order.getAmount();
			if (amountPayable.compareTo(BigDecimal.ZERO) > 0) {
			if (paymentMethod == null) {
				throw new IllegalArgumentException();
			}
			order.setStatus(PaymentMethod.Type.deliveryAgainstPayment.equals(paymentMethod.getType()) ? Order.Status.pendingPayment : Order.Status.pendingReview);
			order.setPaymentMethod(paymentMethod);
			if (paymentMethod.getTimeout() != null && Order.Status.pendingPayment.equals(order.getStatus())) {
			    order.setExpire(DateUtils.addMinutes(new Date(), paymentMethod.getTimeout()));
			}
			if (PaymentMethod.Method.online.equals(paymentMethod.getMethod())) {
			    lock(order, buyMember);
			}
			} else {
			order.setStatus(Order.Status.pendingReview);
			order.setPaymentMethod(null);
			}
			
			List<OrderItem> orderItems = order.getOrderItems();
			for (CartItem cartItem : cartItemList) {
			Product product = cartItem.getProduct();
			OrderItem orderItem = new OrderItem();
			orderItem.setSn(product.getSn());
			orderItem.setName(product.getName());
			orderItem.setType(product.getType());
			orderItem.setPrice(cartItem.getNewPrice());
			orderItem.setPriceUnit(cartItem.getNewPriceUnit());
			orderItem.setPriceB(cartItem.getNewBToBPrice());
			orderItem.setPriceUnitB(cartItem.getNewBToBPriceUnit());
			orderItem.setWeight(product.getWeight());
			orderItem.setIsDelivery(product.getIsDelivery());
			orderItem.setThumbnail(product.getThumbnail());
			orderItem.setQuantity(cartItem.getQuantity());
			orderItem.setShippedQuantity(0);
			orderItem.setReturnedQuantity(0);
			orderItem.setProduct(cartItem.getProduct());
			orderItem.setOrder(order);
			orderItem.setSpecifications(product.getSpecifications());
			orderItems.add(orderItem);
			}
			
			for (Product gift : cart.getGifts()) {
			OrderItem orderItem = new OrderItem();
			orderItem.setSn(gift.getSn());
			orderItem.setName(gift.getName());
			orderItem.setType(gift.getType());
			orderItem.setPrice(BigDecimal.ZERO);
			orderItem.setWeight(gift.getWeight());
			orderItem.setIsDelivery(gift.getIsDelivery());
			orderItem.setThumbnail(gift.getThumbnail());
			orderItem.setQuantity(1);
			orderItem.setShippedQuantity(0);
			orderItem.setReturnedQuantity(0);
			orderItem.setProduct(gift);
			orderItem.setOrder(order);
			orderItem.setSpecifications(gift.getSpecifications());
			orderItems.add(orderItem);
			}
			
			/*//生成送货码
			String deliveryCode="";
			for (int i = 0; i < 6; i++) {
			deliveryCode+=(int)(Math.random()*10);
			}
			order.setDeliveryCode(deliveryCode);*/
			
			order.setSupplyType(supplyType);
			//order.setShop(shop);
			order.setSupplierType(supplierType);
			
			orderDao.persist(order);
			
			List<OrderItem> orderItemsAfter = order.getOrderItems();
			OrderItemLog orderItemLog = new OrderItemLog() ;
			
			List<OrderItemInfo> orderItemInfos = new ArrayList<>(orderItemsAfter.size());
			for(OrderItem orderItem : orderItemsAfter){
			OrderItemInfo orderItemInfo = new OrderItemInfo() ;
			orderItemInfo.setBeforeQuantity(orderItem.getQuantity());
			orderItemInfo.setAfterQuantity(orderItem.getQuantity());
			orderItemInfo.setBeforePrice((orderItem.getPrice()));
			orderItemInfo.setAfterPrice((orderItem.getPrice()));
			orderItemInfo.setBeforePriceB((orderItem.getPriceB()));
			orderItemInfo.setAfterPriceB((orderItem.getPriceB()));
			orderItemInfo.setProduct(orderItem.getProduct());
			orderItemInfo.setOrderItemLog(orderItemLog);
			orderItemInfos.add(orderItemInfo);
			}
			
			//下单企业
			Supplier bySupplier = supplier ;
//			if(need != null){
//				bySupplier = need.getSupplier() ;
//			}
			
			
			orderItemLog.setOrder(order);
			orderItemLog.setOperatorName(logName);
			orderItemLog.setOperatorType(OrderItemLog.OperatorType.create);
			orderItemLog.setType(OrderItemLog.Type.custom);
			orderItemLog.setOrderItemInfos(orderItemInfos);
			if (supplierType != SupplierType.FOUR){
			orderItemLog.setSupplierName(bySupplier.getName());
			}
			orderItemLog.setLogType(LogType.member);
			
			orderItemLogDao.persist(orderItemLog);
			
			
			OrderLog orderLog = new OrderLog();
			orderLog.setType(OrderLog.Type.create);
			orderLog.setOrder(order);
			orderLog.setOperator(logName);
			if (supplierType != SupplierType.FOUR){
				orderLog.setSupplierName(bySupplier.getName());
			}
			orderLog.setLogType(LogType.member);
			orderLogDao.persist(orderLog);
			
			//后台消息推送
			orderNewsPushDao.addOrderNewPush(order.getSupplier(), 
					order, OrderNewsPush.OrderStatus.placeAnOrder, 
					order.getNeed(), order.getSupplier().getName(),
					receiver.getConsignee(), OrderNewsPush.NoticeObject.order);
			
			//如果备注信信息不为空，则向备注息表中添加一条备注信息
			if(StringUtils.isNotEmpty(memo) && order.getType() != Type.local) {
			OrderRemarks orderRemarks = new OrderRemarks();
			orderRemarks.setDescription(memo);
			orderRemarks.setOrder(order);
			orderRemarks.setName(logName);
			orderRemarks.setSuppliper(bySupplier.getName());
			orderRemarks.setMsgType(MsgType.btoc);
			orderRemarks.setLogType(LogType.member);
			orderRemarksDao.persist(orderRemarks);
			
			//后台消息推送
			orderNewsPushDao.addOrderNewPush(order.getSupplier(), order, 
					OrderNewsPush.OrderStatus.leaveAMessage, order.getNeed(), order.getSupplier().getName(), 
					receiver.getConsignee(), OrderNewsPush.NoticeObject.order);
			
			}
			
				exchangePoint(order);
			if (Setting.StockAllocationTime.order.equals(setting.getStockAllocationTime())
			    || (Setting.StockAllocationTime.payment.equals(setting.getStockAllocationTime()) && (order.getAmountPaid().compareTo(BigDecimal.ZERO) > 0 || order.getExchangePoint() > 0 || order.getAmountPayable().compareTo(BigDecimal.ZERO) <= 0))) {
				allocateStock(order);
			}
			
			if (balance != null && balance.compareTo(BigDecimal.ZERO) > 0) {
				Payment payment = new Payment();
				payment.setMethod(Payment.Method.deposit);
				payment.setFee(BigDecimal.ZERO);
				payment.setAmount(balance);
				payment.setOrder(order);
				payment(order, payment, null);
			}
			
			/*//mailService.sendCreateOrderMail(order);
			smsService.sendCreateOrderSms(order);
			*/
			// TODO: 2017/2/14 发送模版消息
//			weChatService.sendTemplateMessage(order , commonTemplateId , weChatService.getGlobalToken() ,
//			 Order.OrderStatus.create) ;
			//订单创建成功,向供应商增加手机短信提醒
			/*smsService.sendContent(supplier.getTel() , orderNotice);
			//向供应商的接收员发送模版消息
			weChatService.sendTemplateMessageToNoticeUser(supplier , order , Order.OrderStatus.create , commonTemplateId , weChatService.getGlobalToken() ) ;
			
			//向采购方企业接受员发送模版消息
			weChatService.sendTemplateMessageToNoticeUserPurchase(supplier , bySupplier , need , order , Order.OrderStatus.create , commonTemplateId , weChatService.getGlobalToken() , NoticeTypePurchase.Type.order_create , "");*/
			
			//发送小程序通知给微信用户
//			OrderForm orderForm = order.getChildMember().getOrderFormOne();
//			if(orderForm != null){
//				weChatService.sendSmallTemplateMessageToInitiator(order, order.getChildMember(), weChatService.getGlobalToken(),
//						Constant.SMALL_TEMPLATE_ID, "", orderForm.getFormId());
//			}
			
			
			
			
//			if (order.getType() != Type.local){
//				//发送模版消息
//				weChatService.sendTemplateMessageByOrderStatus(order , Order.OrderStatus.create ,  
//						weChatService.getGlobalToken() , null , commonTemplateId , null , null);
//				
//				//订单创建成功,向供应商增加手机短信提醒
//				smsService.sendContent(supplier.getTel() , orderNotice);
//				
//				//订单备注时，增加消息提醒
//				if(StringUtils.isNotEmpty(memo)){
//				
//					weChatService.sendOrderRemarkNotice(order , buyMember , this.commonTemplateId , weChatService.getGlobalToken() , memo);
//				
//					List<NoticeUser> noticeUsers = noticeUserDao.findList(supplier , bySupplier , NoticeType.Type.user_order_remark);
//					weChatService.sendOrderRemarkNotice(order , buyMember , this.commonTemplateId , weChatService.getGlobalToken() , noticeUsers , memo);
//					//采购单消息接受员
//					//weChatService.sendOrderRemarkNoticeToNoticeUserPurchase(order , buyMember , commonTemplateId , weChatService.getGlobalToken() , memo , true);
//				
//				}
//			}
			
			//本地订单  发起人
//			OrderRelation orderRelation = new OrderRelation();
//			orderRelation.setType(OrderRelation.Type.sponsor);
//			orderRelation.setChildMember(childMember);
//			orderRelation.setOrder(order);
//			orderRelation.setMember(childMember.getMember());
//			orderRelationDao.persist(orderRelation);
			
			return order;
	}
	//立即购买
	private Order createSingleBaseOrderForPur(Member buyMember, Supplier supplier, 
			BigDecimal price, BigDecimal priceB, Product product,
			PaymentMethod paymentMethod, ShippingMethod shippingMethod,
            CouponCode couponCode, Invoice invoice, 
            BigDecimal balance, String memo, Date reDate , 
            SupplyType supplyType , ChildMember childMember, 
            Receiver receiver, SupplierType supplierType, Types types, Integer sumQuantity){
			String logName="";//操作人名称  如果为员工则为员工姓名   
			Setting setting = SystemUtils.getSetting();
			Order order = new Order();
			order.setChildMember(childMember);
			do {
			order.setSn(snDao.generate(Sn.Type.order));
			//本地订单编号加B
			if (types == Types.local) {
				order.setSn("B"+order.getSn());
			}
			} while (orderDao.findBySn(order.getSn()) != null);
			order.setPrice(price);
			order.setSupplier(supplier);
			order.setReDate(reDate);
			order.setReCode(RandomStringUtils.randomNumeric(6));
			order.setFee(BigDecimal.ZERO);
			if(product.getWeight() != null && shippingMethod != null && receiver.getArea() != null){
				order.setFreight(product.getIsDelivery() ?
					    shippingMethodService.calculateFreight(shippingMethod, receiver.getArea(), 
					    		product.getWeight()) : BigDecimal.ZERO);
			}else{
				order.setFreight(BigDecimal.ZERO);
			}
			order.setWeight(0);
			
			order.setPromotionDiscount(BigDecimal.ZERO);
			order.setOffsetAmount(BigDecimal.ZERO);
			order.setAmountPaid(BigDecimal.ZERO);
			order.setRefundAmount(BigDecimal.ZERO);
			order.setRewardPoint(0l);
			order.setExchangePoint(product.getExchangePoint());
			order.setWeight(product.getWeight());
			order.setQuantity(sumQuantity);
			order.setShippedQuantity(0);
			order.setReturnedQuantity(0);
			if (product.getIsDelivery()) {
				order.setConsignee(receiver.getConsignee());
				order.setAreaName(receiver.getAreaName());
				order.setAddress(receiver.getAddress());
				order.setZipCode(receiver.getZipCode());
				order.setPhone(receiver.getPhone());
				order.setArea(receiver.getArea());
			}
			order.setAreaName(receiver.getArea().getFullName());
			order.setAddress(receiver.getAddress());
			order.setZipCode("200000");
			order.setMemo(memo);
			order.setIsUseCouponCode(false);
			order.setIsExchangePoint(false);
			order.setIsAllocatedStock(false);
			order.setInvoice(setting.getIsInvoiceEnabled() ? invoice : null);
			order.setShippingMethod(shippingMethod);
			order.setMember(buyMember);
			order.setBuyMember(buyMember);
			order.setPromotionNames(new ArrayList());
			order.setCoupons(new ArrayList());
			if (Types.local.equals(types)){
				order.setType(Type.local);
				order.setSharingStatus(Order.SharingStatus.noshare);
			}else {
				order.setType(Order.Type.general);
			}
			//正常下单
			order.setBuyType(Order.BuyType.general);
			
			if (couponCode != null) {
				BigDecimal couponDiscount = BigDecimal.ZERO;
				order.setCouponDiscount(couponDiscount.compareTo(BigDecimal.ZERO) >= 0 ? couponDiscount : BigDecimal.ZERO);
				order.setCouponCode(couponCode);
				useCouponCode(order);
			} else {
			order.setCouponDiscount(BigDecimal.ZERO);
			}
			
			order.setTax(calculateTax(order));
			order.setAmount(price);
			order.setAmountToB(priceB);
			order.setAmount(calculateAmount(order));
			
			if (balance != null && (balance.compareTo(BigDecimal.ZERO) < 0 || balance.compareTo(buyMember.getBalance()) > 0 || balance.compareTo(order.getAmount()) > 0)) {
			throw new IllegalArgumentException();
			}
			BigDecimal amountPayable = balance != null ? order.getAmount().subtract(balance) : order.getAmount();
			if (amountPayable.compareTo(BigDecimal.ZERO) > 0) {
			if (paymentMethod == null) {
				throw new IllegalArgumentException();
			}
			order.setStatus(PaymentMethod.Type.deliveryAgainstPayment.equals(paymentMethod.getType()) ? Order.Status.pendingPayment : Order.Status.pendingReview);
			order.setPaymentMethod(paymentMethod);
			if (paymentMethod.getTimeout() != null && Order.Status.pendingPayment.equals(order.getStatus())) {
			    order.setExpire(DateUtils.addMinutes(new Date(), paymentMethod.getTimeout()));
			}
			if (PaymentMethod.Method.online.equals(paymentMethod.getMethod())) {
			    lock(order, buyMember);
			}
			} else {
			order.setStatus(Order.Status.pendingReview);
			order.setPaymentMethod(null);
			}
			
			List<OrderItem> orderItems = order.getOrderItems();
			OrderItem orderItem = new OrderItem();
			orderItem.setSn(product.getSn());
			orderItem.setName(product.getName());
			orderItem.setType(product.getType());
			orderItem.setPrice(price);
			orderItem.setPriceUnit(product.getPrice());
			orderItem.setPriceB(price);
			orderItem.setPriceUnitB(product.getPrice());
			orderItem.setWeight(product.getWeight());
			orderItem.setIsDelivery(product.getIsDelivery());
			orderItem.setThumbnail(product.getThumbnail());
			orderItem.setQuantity(sumQuantity);
			orderItem.setShippedQuantity(0);
			orderItem.setReturnedQuantity(0);
			orderItem.setProduct(product);
			orderItem.setOrder(order);
			orderItem.setSpecifications(product.getSpecifications());
			orderItems.add(orderItem);
			
			
			order.setSupplyType(supplyType);
			order.setSupplierType(supplierType);
			
			order.setWeight(0);
			orderDao.persist(order);
			
			List<OrderItem> orderItemsAfter = order.getOrderItems();
			OrderItemLog orderItemLog = new OrderItemLog() ;
			
			List<OrderItemInfo> orderItemInfos = new ArrayList<>(orderItemsAfter.size());
			for(OrderItem oitem : orderItemsAfter){
				OrderItemInfo orderItemInfo = new OrderItemInfo() ;
				orderItemInfo.setBeforeQuantity(oitem.getQuantity());
				orderItemInfo.setAfterQuantity(oitem.getQuantity());
				orderItemInfo.setBeforePrice((oitem.getPrice()));
				orderItemInfo.setAfterPrice((oitem.getPrice()));
				orderItemInfo.setBeforePriceB((oitem.getPriceB()));
				orderItemInfo.setAfterPriceB((oitem.getPriceB()));
				orderItemInfo.setProduct(oitem.getProduct());
				orderItemInfo.setOrderItemLog(orderItemLog);
				orderItemInfos.add(orderItemInfo);
			}
			
			//下单企业
			Supplier bySupplier = supplier ;
			
			
			orderItemLog.setOrder(order);
			orderItemLog.setOperatorName(logName);
			orderItemLog.setOperatorType(OrderItemLog.OperatorType.create);
			orderItemLog.setType(OrderItemLog.Type.custom);
			orderItemLog.setOrderItemInfos(orderItemInfos);
			if (supplierType != SupplierType.FOUR){
				orderItemLog.setSupplierName(bySupplier.getName());
			}
			orderItemLog.setLogType(LogType.member);
			
			orderItemLogDao.persist(orderItemLog);
			
			
			OrderLog orderLog = new OrderLog();
			orderLog.setType(OrderLog.Type.create);
			orderLog.setOrder(order);
			orderLog.setOperator(logName);
			if (supplierType != SupplierType.FOUR){
				orderLog.setSupplierName(bySupplier.getName());
			}
			orderLog.setLogType(LogType.member);
			orderLogDao.persist(orderLog);
			
			//后台消息推送
			orderNewsPushDao.addOrderNewPush(order.getSupplier(), 
					order, OrderNewsPush.OrderStatus.placeAnOrder, 
					order.getNeed(), order.getSupplier().getName(),
					receiver.getConsignee(), OrderNewsPush.NoticeObject.order);
			
			//如果备注信信息不为空，则向备注息表中添加一条备注信息
			if(StringUtils.isNotEmpty(memo) && order.getType() != Type.local) {
			OrderRemarks orderRemarks = new OrderRemarks();
			orderRemarks.setDescription(memo);
			orderRemarks.setOrder(order);
			orderRemarks.setName(logName);
			orderRemarks.setSuppliper(bySupplier.getName());
			orderRemarks.setMsgType(MsgType.btoc);
			orderRemarks.setLogType(LogType.member);
			orderRemarksDao.persist(orderRemarks);
			
			//后台消息推送
			orderNewsPushDao.addOrderNewPush(order.getSupplier(), order, 
					OrderNewsPush.OrderStatus.leaveAMessage, order.getNeed(), order.getSupplier().getName(), 
					receiver.getConsignee(), OrderNewsPush.NoticeObject.order);
			
			}
			
				exchangePoint(order);
			if (Setting.StockAllocationTime.order.equals(setting.getStockAllocationTime())
			    || (Setting.StockAllocationTime.payment.equals(setting.getStockAllocationTime()) && (order.getAmountPaid().compareTo(BigDecimal.ZERO) > 0 || order.getExchangePoint() > 0 || order.getAmountPayable().compareTo(BigDecimal.ZERO) <= 0))) {
				allocateStock(order);
			}
			
			if (balance != null && balance.compareTo(BigDecimal.ZERO) > 0) {
				Payment payment = new Payment();
				payment.setMethod(Payment.Method.deposit);
				payment.setFee(BigDecimal.ZERO);
				payment.setAmount(balance);
				payment.setOrder(order);
				payment(order, payment, null);
			}
			
			/*//mailService.sendCreateOrderMail(order);
			smsService.sendCreateOrderSms(order);*/
			// TODO: 2017/2/14 发送模版消息
//			weChatService.sendTemplateMessage(order , commonTemplateId ,
//			 weChatService.getGlobalToken() , Order.OrderStatus.create) ;
			//订单创建成功,向供应商增加手机短信提醒
		/*	smsService.sendContent(supplier.getTel() , orderNotice);
			//向供应商的接收员发送模版消息
			weChatService.sendTemplateMessageToNoticeUser(supplier , order , Order.OrderStatus.create , commonTemplateId , weChatService.getGlobalToken() ) ;
			
			//向采购方企业接受员发送模版消息
			weChatService.sendTemplateMessageToNoticeUserPurchase(supplier , bySupplier , need , order , Order.OrderStatus.create , commonTemplateId , weChatService.getGlobalToken() , NoticeTypePurchase.Type.order_create , "");*/
			
			//发送小程序通知给微信用户
//			OrderForm orderForm = order.getChildMember().getOrderFormOne();
//			if(orderForm != null){
//				weChatService.sendSmallTemplateMessageToInitiator(order, order.getChildMember(), weChatService.getGlobalToken(),
//						Constant.SMALL_TEMPLATE_ID, "", orderForm.getFormId());
//			}
			
			
			
			//本地订单  发起人
//			OrderRelation orderRelation = new OrderRelation();
//			orderRelation.setType(OrderRelation.Type.sponsor);
//			orderRelation.setChildMember(childMember);
//			orderRelation.setOrder(order);
//			orderRelation.setMember(childMember.getMember());
//			orderRelationDao.persist(orderRelation);
			
			return order;
	}



    private Order createSingleSupplyOrder(Member buyMember, Supplier supplier, List<CartItem> cartItemList, 
    					BigDecimal supplyPrice,BigDecimal supplyPriceB, Cart cart,
    					PaymentMethod paymentMethod, ShippingMethod shippingMethod,
                        CouponCode couponCode, Invoice invoice, 
                        BigDecimal balance, String memo, Date reDate , 
                         SupplyType supplyType , ChildMember childMember, 
                         Need need,SupplyNeed supplyNeed,SupplierSupplier supplierSupplier,
                         Long relationId,Shop shop,SupplierType supplierType,Types types,Integer sumQuantity){
        String logName="";//操作人名称  如果为员工则为员工姓名   
    	Setting setting = SystemUtils.getSetting();
        Order order = new Order();
        order.setChildMember(childMember);
        do {
			order.setSn(snDao.generate(Sn.Type.order));
			//本地订单编号加B
			if (types == Types.local) {
				order.setSn("B"+order.getSn());
			}
		} while (orderDao.findBySn(order.getSn()) != null);
        order.setPrice(supplyPrice);
        order.setSupplier(supplier);
        order.setNeed(need);
        order.setReDate(reDate);
        order.setReCode(RandomStringUtils.randomNumeric(6));
        order.setFee(BigDecimal.ZERO);
        order.setFreight(cart.getIsDelivery() && !cart.isFreeShipping() ?
                shippingMethodService.calculateFreight(shippingMethod, shop.getArea(), cart.getWeight()) : BigDecimal.ZERO);
        order.setPromotionDiscount(cart.getDiscount());
        order.setOffsetAmount(BigDecimal.ZERO);
        order.setAmountPaid(BigDecimal.ZERO);
        order.setRefundAmount(BigDecimal.ZERO);
        order.setRewardPoint(cart.getEffectiveRewardPoint());
        order.setExchangePoint(cart.getExchangePoint());
        order.setWeight(cart.getWeight());
        order.setQuantity(sumQuantity);
        order.setShippedQuantity(0);
        order.setReturnedQuantity(0);
        order.setSupplyNeed(supplyNeed);
        order.setSupplierSupplier(supplierSupplier);
//        if (cart.getIsDelivery()) {
//            order.setConsignee(receiver.getConsignee());
//            order.setAreaName(receiver.getAreaName());
//            order.setAddress(receiver.getAddress());
//            order.setZipCode(receiver.getZipCode());
//            order.setPhone(receiver.getPhone());
//            order.setArea(receiver.getArea());
//        }
		order.setAreaName(shop.getArea().getFullName());
		order.setAddress(shop.getAddress());
		order.setZipCode("200000");
		if (shop.getMember().equals(buyMember)) {
			order.setConsignee(shop.getUserName());
			logName="超级管理员";
		}else {
			HostingShop hostingShop=hostingShopDao.findByShopAndByMember(shop,buyMember);
			order.setConsignee(hostingShop.getMemberMember().getName());
			logName=hostingShop.getMemberMember().getName();
		}
		order.setPhone(shop.getReceiverTel());
		order.setArea(shop.getArea());
        order.setMemo(memo);
        order.setIsUseCouponCode(false);
        order.setIsExchangePoint(false);
        order.setIsAllocatedStock(false);
        order.setInvoice(setting.getIsInvoiceEnabled() ? invoice : null);
        order.setShippingMethod(shippingMethod);
        order.setMember(shop.getMember());
        order.setBuyMember(buyMember);
        order.setPromotionNames(cart.getPromotionNames());
        order.setCoupons(new ArrayList<>(cart.getCoupons()));
        if (Types.local.equals(types)){
			order.setType(Type.local);
			order.setSharingStatus(Order.SharingStatus.noshare);
		}else {
        	if (SupplierType.ONE.equals(supplierType) || SupplierType.THREE.equals(supplierType)){
				if (supplyNeed.getAssignedModel().equals(SupplyNeed.AssignedModel.STRAIGHT)) {
					order.setType(Order.Type.general);
				}else if (supplyNeed.getAssignedModel().equals(SupplyNeed.AssignedModel.BRANCH)) {
					order.setType(Order.Type.distribution);
				}
			}else if (SupplierType.TWO.equals(supplierType)){
				order.setType(Order.Type.formal);
				order.setToSupplier(need.getSupplier());
			}
		}
        //正常下单
        order.setBuyType(Order.BuyType.general);

        if (couponCode != null) {
            if (!cart.isCouponAllowed() || !cart.isValid(couponCode)) {
                throw new IllegalArgumentException();
            }
            BigDecimal couponDiscount = cart.getEffectivePrice().subtract(couponCode.getCoupon().calculatePrice(cart.getEffectivePrice(), cart.getProductQuantity()));
            order.setCouponDiscount(couponDiscount.compareTo(BigDecimal.ZERO) >= 0 ? couponDiscount : BigDecimal.ZERO);
            order.setCouponCode(couponCode);
            useCouponCode(order);
        } else {
            order.setCouponDiscount(BigDecimal.ZERO);
        }

        order.setTax(calculateTax(order));
        order.setAmount(supplyPrice);
        order.setAmountToB(supplyPriceB);
//        order.setAmount(calculateAmount(order));

        if (balance != null && (balance.compareTo(BigDecimal.ZERO) < 0 || balance.compareTo(buyMember.getBalance()) > 0 || balance.compareTo(order.getAmount()) > 0)) {
            throw new IllegalArgumentException();
        }
        BigDecimal amountPayable = balance != null ? order.getAmount().subtract(balance) : order.getAmount();
        if (amountPayable.compareTo(BigDecimal.ZERO) > 0) {
            if (paymentMethod == null) {
				throw new IllegalArgumentException();
			}
            order.setStatus(PaymentMethod.Type.deliveryAgainstPayment.equals(paymentMethod.getType()) ? Order.Status.pendingPayment : Order.Status.pendingReview);
            order.setPaymentMethod(paymentMethod);
            if (paymentMethod.getTimeout() != null && Order.Status.pendingPayment.equals(order.getStatus())) {
                order.setExpire(DateUtils.addMinutes(new Date(), paymentMethod.getTimeout()));
            }
            if (PaymentMethod.Method.online.equals(paymentMethod.getMethod())) {
                lock(order, buyMember);
            }
        } else {
            order.setStatus(Order.Status.pendingReview);
            order.setPaymentMethod(null);
        }
        
        List<OrderItem> orderItems = order.getOrderItems();
        for (CartItem cartItem : cartItemList) {
            Product product = cartItem.getProduct();
            OrderItem orderItem = new OrderItem();
            orderItem.setSn(product.getSn());
            orderItem.setName(product.getName());
            orderItem.setType(product.getType());
            orderItem.setPrice(cartItem.getNewPrice());
            orderItem.setPriceUnit(cartItem.getNewPriceUnit());
            orderItem.setPriceB(cartItem.getNewBToBPrice());
            orderItem.setPriceUnitB(cartItem.getNewBToBPriceUnit());
            orderItem.setWeight(product.getWeight());
            orderItem.setIsDelivery(product.getIsDelivery());
            orderItem.setThumbnail(product.getThumbnail());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setShippedQuantity(0);
            orderItem.setReturnedQuantity(0);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setOrder(order);
            orderItem.setSpecifications(product.getSpecifications());
            orderItems.add(orderItem);
        }

        for (Product gift : cart.getGifts()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setSn(gift.getSn());
            orderItem.setName(gift.getName());
            orderItem.setType(gift.getType());
            orderItem.setPrice(BigDecimal.ZERO);
            orderItem.setWeight(gift.getWeight());
            orderItem.setIsDelivery(gift.getIsDelivery());
            orderItem.setThumbnail(gift.getThumbnail());
            orderItem.setQuantity(1);
            orderItem.setShippedQuantity(0);
            orderItem.setReturnedQuantity(0);
            orderItem.setProduct(gift);
            orderItem.setOrder(order);
            orderItem.setSpecifications(gift.getSpecifications());
            orderItems.add(orderItem);
        }

        /*//生成送货码
        String deliveryCode="";
        for (int i = 0; i < 6; i++) {
            deliveryCode+=(int)(Math.random()*10);
        }
        order.setDeliveryCode(deliveryCode);*/

        order.setSupplyType(supplyType);
        order.setShop(shop);
        order.setSupplierType(supplierType);

        orderDao.persist(order);

        List<OrderItem> orderItemsAfter = order.getOrderItems();
		OrderItemLog orderItemLog = new OrderItemLog() ;

		List<OrderItemInfo> orderItemInfos = new ArrayList<>(orderItemsAfter.size());
		for(OrderItem orderItem : orderItemsAfter){
			OrderItemInfo orderItemInfo = new OrderItemInfo() ;
			orderItemInfo.setBeforeQuantity(orderItem.getQuantity());
			orderItemInfo.setAfterQuantity(orderItem.getQuantity());
			orderItemInfo.setBeforePrice((orderItem.getPrice()));
			orderItemInfo.setAfterPrice((orderItem.getPrice()));
			orderItemInfo.setBeforePriceB((orderItem.getPriceB()));
			orderItemInfo.setAfterPriceB((orderItem.getPriceB()));
			orderItemInfo.setProduct(orderItem.getProduct());
			orderItemInfo.setOrderItemLog(orderItemLog);
			orderItemInfos.add(orderItemInfo);
		}

		//下单企业
		Supplier bySupplier = null ;
		if(need != null){
			bySupplier = need.getSupplier() ;
		}


		orderItemLog.setOrder(order);
		orderItemLog.setOperatorName(logName);
		orderItemLog.setOperatorType(OrderItemLog.OperatorType.create);
		orderItemLog.setType(OrderItemLog.Type.custom);
		orderItemLog.setOrderItemInfos(orderItemInfos);
		if (supplierType != SupplierType.FOUR){
			orderItemLog.setSupplierName(bySupplier.getName());
		}
		orderItemLog.setLogType(LogType.member);

		orderItemLogDao.persist(orderItemLog);


        OrderLog orderLog = new OrderLog();
        orderLog.setType(OrderLog.Type.create);
        orderLog.setOrder(order);
        orderLog.setOperator(logName);
		if (supplierType != SupplierType.FOUR){
			orderLog.setSupplierName(bySupplier.getName());
		}
		orderLog.setLogType(LogType.member);
        orderLogDao.persist(orderLog);
        
        //后台消息推送
        if(order.getType() == Order.Type.formal) {
        	orderNewsPushDao.addOrderNewPush(order.getSupplier(), order, OrderNewsPush.OrderStatus.placeAnOrder, order.getNeed(), order.getToSupplier().getName(), need.getName(), OrderNewsPush.NoticeObject.order);
        	orderNewsPushDao.addOrderNewPush(order.getToSupplier(), order, OrderNewsPush.OrderStatus.placeAnOrder, order.getNeed(), order.getToSupplier().getName(), need.getName(),OrderNewsPush.NoticeObject.purchase);
        }else if (order.getType() != Order.Type.formal && order.getType() != Type.local) {
        	orderNewsPushDao.addOrderNewPush(order.getSupplier(), order, OrderNewsPush.OrderStatus.placeAnOrder, order.getNeed(), order.getSupplier().getName(), need.getName(),OrderNewsPush.NoticeObject.order);
        }
		
        //如果备注信信息不为空，则向备注息表中添加一条备注信息
        if(StringUtils.isNotEmpty(memo) && order.getType() != Type.local) {
        	OrderRemarks orderRemarks = new OrderRemarks();
        	orderRemarks.setDescription(memo);
        	orderRemarks.setOrder(order);
        	orderRemarks.setName(logName);
			orderRemarks.setSuppliper(bySupplier.getName());
        	orderRemarks.setMsgType(MsgType.btoc);
        	orderRemarks.setLogType(LogType.member);
        	orderRemarksDao.persist(orderRemarks);
        	
        	//后台消息推送
        	if(order.getType() == Order.Type.formal) {
        		orderNewsPushDao.addOrderNewPush(order.getSupplier(), order, OrderNewsPush.OrderStatus.leaveAMessage, order.getNeed(), order.getToSupplier().getName(), need.getName(),OrderNewsPush.NoticeObject.order);
        		orderNewsPushDao.addOrderNewPush(order.getToSupplier(), order, OrderNewsPush.OrderStatus.leaveAMessage, order.getNeed(), order.getToSupplier().getName(), need.getName(),OrderNewsPush.NoticeObject.purchase);
        	}else if (order.getType() != Order.Type.formal && order.getType() != Type.local){
        		orderNewsPushDao.addOrderNewPush(order.getSupplier(), order, OrderNewsPush.OrderStatus.leaveAMessage, order.getNeed(), order.getSupplier().getName(), need.getName(),OrderNewsPush.NoticeObject.order);
        	}
        	
        }

        exchangePoint(order);
        if (Setting.StockAllocationTime.order.equals(setting.getStockAllocationTime())
                || (Setting.StockAllocationTime.payment.equals(setting.getStockAllocationTime()) && (order.getAmountPaid().compareTo(BigDecimal.ZERO) > 0 || order.getExchangePoint() > 0 || order.getAmountPayable().compareTo(BigDecimal.ZERO) <= 0))) {
            allocateStock(order);
        }

        if (balance != null && balance.compareTo(BigDecimal.ZERO) > 0) {
            Payment payment = new Payment();
            payment.setMethod(Payment.Method.deposit);
            payment.setFee(BigDecimal.ZERO);
            payment.setAmount(balance);
            payment.setOrder(order);
            payment(order, payment, null);
        }

        /*//mailService.sendCreateOrderMail(order);
        smsService.sendCreateOrderSms(order);
        // TODO: 2017/2/14 发送模版消息
        weChatService.sendTemplateMessage(order , commonTemplateId , weChatService.getGlobalToken() , Order.OrderStatus.create) ;
        //订单创建成功,向供应商增加手机短信提醒
        smsService.sendContent(supplier.getTel() , orderNotice);
        //向供应商的接收员发送模版消息
        weChatService.sendTemplateMessageToNoticeUser(supplier , order , Order.OrderStatus.create , commonTemplateId , weChatService.getGlobalToken() ) ;

        //向采购方企业接受员发送模版消息
        weChatService.sendTemplateMessageToNoticeUserPurchase(supplier , bySupplier , need , order , Order.OrderStatus.create , commonTemplateId , weChatService.getGlobalToken() , NoticeTypePurchase.Type.order_create , "");*/

        if (order.getType() != Type.local){
			//发送模版消息
			weChatService.sendTemplateMessageByOrderStatus(order , Order.OrderStatus.create ,  weChatService.getGlobalToken() , null , commonTemplateId , null , null);

			//订单创建成功,向供应商增加手机短信提醒
			smsService.sendContent(supplier.getTel() , orderNotice);

			//订单备注时，增加消息提醒
			if(StringUtils.isNotEmpty(memo)){

				weChatService.sendOrderRemarkNotice(order , buyMember , this.commonTemplateId , weChatService.getGlobalToken() , memo);

				List<NoticeUser> noticeUsers = noticeUserDao.findList(supplier , bySupplier , NoticeType.Type.user_order_remark);
				weChatService.sendOrderRemarkNotice(order , buyMember , this.commonTemplateId , weChatService.getGlobalToken() , noticeUsers , memo);
				//采购单消息接受员
				weChatService.sendOrderRemarkNoticeToNoticeUserPurchase(order , buyMember , commonTemplateId , weChatService.getGlobalToken() , memo , true);

			}
		}

		//本地订单  发起人
		OrderRelation orderRelation = new OrderRelation();
		orderRelation.setType(OrderRelation.Type.sponsor);
		orderRelation.setChildMember(childMember);
		orderRelation.setOrder(order);
		orderRelation.setMember(childMember.getMember());
		orderRelationDao.persist(orderRelation);

        return order;
    }


	@Override
	public void applyCancel(Order order) {

		order.setBeforeStatus(order.getStatus());
		//orderDao.flush();
		order.setStatus(Order.Status.applyCancel);
		order.setExpire(null);


		OrderLog orderLog = new OrderLog();
		orderLog.setType(OrderLog.Type.applyCancel);
		orderLog.setOrder(order);
		orderLogDao.persist(orderLog);

	}

	/**
	 * 处理用户提交的取消申请操作
	 *
	 * @param order
	 * @param passed
	 */
	@Override
	public void dealAppyCancel(Order order, boolean passed , Admin operator) {

		OrderLog orderLog = new OrderLog();
		//OrderNewsPush orderNewsPush = new OrderNewsPush();
		OrderNewsPush.OrderStatus orderStatus = null;
		
		if(passed){

			order.setStatus(Order.Status.canceled);
			order.setExpire(null);

			orderLog.setType(OrderLog.Type.passedCancel);
			orderStatus = OrderNewsPush.OrderStatus.agreeApplicationCancel;

			undoUseCouponCode(order);
			undoExchangePoint(order);
			releaseAllocatedStock(order);

		}else{
			order.setStatus(order.getBeforeStatus());
			orderLog.setType(OrderLog.Type.deniedCancel);
			orderStatus = OrderNewsPush.OrderStatus.refuseApplicationCancel;
		}


		orderLog.setOperator(operator);
		orderLog.setOrder(order);
		if (order.getToSupplier() == null) {
			orderLog.setLogType(LogType.distributor);
		}else {
			orderLog.setLogType(LogType.supplier);
		}
		orderLogDao.persist(orderLog);
		
		//后台消息推送
		Supplier supplier = order.getSupplier();
		if(order.getType().equals(Order.Type.billDistribution)) {
			orderNewsPushDao.addOrderNewPush(supplier, order, orderStatus, order.getNeed(), supplier.getName(), "",OrderNewsPush.NoticeObject.order);
			orderNewsPushDao.addOrderNewPush(order.getToSupplier(), order, orderStatus, order.getNeed(), supplier.getName(), "",OrderNewsPush.NoticeObject.purchase);
			orderNewsPushDao.addOrderNewPush(order.getToSupplier(), order, orderStatus, order.getNeed(), supplier.getName(), "",OrderNewsPush.NoticeObject.order);
		}else if(order.getType() == Order.Type.formal) {
			orderNewsPushDao.addOrderNewPush(supplier, order, orderStatus, order.getNeed(), supplier.getName(), "",OrderNewsPush.NoticeObject.order);
			orderNewsPushDao.addOrderNewPush(order.getToSupplier(), order, orderStatus, order.getNeed(), supplier.getName(), "",OrderNewsPush.NoticeObject.purchase);
		}else{
			orderNewsPushDao.addOrderNewPush(supplier, order, orderStatus, order.getNeed(), supplier.getName(), "",OrderNewsPush.NoticeObject.order);
		}

		//发送模版消息
		weChatService.sendTemplateMessageByOrderStatus(order , passed? Order.OrderStatus.applyCancel_passed : Order.OrderStatus.applyCancel_denied  ,  weChatService.getGlobalToken() , null , commonTemplateId , null , null);


	}

	/**
	 * 查询我方订单
	 *
	 * @param type
	 * @param status
	 * @param member
	 * @param goods
	 * @param isPendingReceive
	 * @param isPendingRefunds
	 * @param isUseCouponCode
	 * @param isExchangePoint
	 * @param isAllocatedStock
	 * @param hasExpired
	 * @param pageable
	 * @param supplier
	 * @return
	 */
	@Override
	public Page<Order> findPageByOwn(Order.Type type, Order.Status status,Order.Status[] statuses, Member member, Goods goods, Boolean isPendingReceive, Boolean isPendingRefunds, Boolean isUseCouponCode, Boolean isExchangePoint, Boolean isAllocatedStock, Boolean hasExpired, Pageable pageable, Supplier supplier ,  Date startDate , Date endDate , String searchName , String timeSearch) {

		Page<Order> result = orderDao.findPage(type, status,statuses, member, goods, isPendingReceive, isPendingRefunds, isUseCouponCode, isExchangePoint, isAllocatedStock, hasExpired, pageable , null , startDate , endDate , searchName , timeSearch,supplier) ;
		return result;
	}

    /**
     * @param supplierId
     * @param needId
     * @param memo
     * @param reDate
     * @param orderProductForm
	 * @param operator
     * @return
     */
	@Transactional
    @Override

    public Order createByOwn(Long supplierId, Long needId, String memo, Date reDate, OrderProductForm orderProductForm , String operator , boolean isMore ,Admin admin ,SupplierType supplierType,Long areaId,String address,String consignee,String tel) {

		Supplier supplier = supplierDao.find(supplierId);
		Need need = needDao.find(needId) ;
		Member member = need.getMember();
		Shop shop=need.getShops().get(0);
		
		ShippingMethod shippingMethod = shippingMethodService.find(1L);
		PaymentMethod paymentMethod = paymentMethodDao.find(3L);

		Setting setting = SystemUtils.getSetting();
		Order order = new Order();

		order.setSupplyType(SupplyType.temporary);
		do {
			order.setSn(snDao.generate(Sn.Type.order));
		} while (orderDao.findBySn(order.getSn()) != null);
		
		SupplyNeed supplyNeed=new SupplyNeed();
		supplyNeed.setSupplier(supplier);
		supplyNeed.setNeed(need);
		supplyNeed=supplyNeedService.findSupplyNeedOnSupply(supplyNeed);
		//代下单
		order.setBuyType(Order.BuyType.substitute);
		if (supplyNeed.getAssignedModel().equals(SupplyNeed.AssignedModel.STRAIGHT)) {
			order.setType(Order.Type.general);
		}else {
			order.setType(Order.Type.distribution);
		}
		order.setSupplyNeed(supplyNeed);
		order.setNeed(need);
		order.setSupplier(supplier);
		order.setReDate(reDate);
		order.setReCode(RandomStringUtils.randomNumeric(6));
		order.setFee(BigDecimal.ZERO);


		order.setPromotionDiscount(BigDecimal.ZERO);
		order.setOffsetAmount(BigDecimal.ZERO);
		order.setAmountPaid(BigDecimal.ZERO);
		order.setRefundAmount(BigDecimal.ZERO);
		order.setRewardPoint(0L);
		order.setExchangePoint(0L);

		order.setShippedQuantity(0);
		order.setReturnedQuantity(0);

		//处理收货地址
		Area area=areaDao.find(areaId);
		order.setFreight(shippingMethodService.calculateFreight(shippingMethod, area, 0));
		order.setConsignee(consignee);
		order.setAreaName(area.getFullName());
		order.setAddress(address);
		order.setZipCode("200000");
		order.setPhone(tel);
		order.setArea(area);

		order.setMemo(memo);
		order.setIsUseCouponCode(false);
		order.setIsExchangePoint(false);
		order.setIsAllocatedStock(false);
		order.setInvoice(null);
		order.setShippingMethod(shippingMethod);
		order.setMember(member);
		order.setBuyMember(member);
		order.setShop(shop);
		order.setSupplierType(supplierType);

		order.setPromotionNames(null);
		order.setCoupons(null);

		order.setCouponDiscount(BigDecimal.ZERO);

		order.setTax(calculateTax(order));

		BigDecimal totalPrice = BigDecimal.ZERO ;
		BigDecimal totalPriceB = BigDecimal.ZERO ;
		Integer totalQuantity = 0;
		Integer totalWeight = 0;
		
		List<OrderItem> orderItems = order.getOrderItems();
		
		for (OrderProductForm.OwnOrderItem ownOrderItem : orderProductForm.getOwnOrderItems()) {
			Long productId = ownOrderItem.getProductId() ;
			Integer quantity = ownOrderItem.getQuantity() ;
			if(null == productId || null == quantity){
				continue;
			}
			Product product = productService.find(productId) ;

			NeedProduct needProduct=needProductDao.findByNeedSupplier(supplyNeed, product);
			BigDecimal price=needProduct.getSupplyPrice().multiply(new BigDecimal(quantity));
			totalPrice = totalPrice.add(price);
			totalQuantity+=quantity;
			totalWeight+=product.getWeight() == null ? 0:product.getWeight();


			OrderItem orderItem = new OrderItem();
			orderItem.setSn(product.getSn());
			orderItem.setName(product.getName());
			orderItem.setType(product.getType());
			orderItem.setPrice(price);
			orderItem.setPriceUnit(needProduct.getSupplyPrice());
			if (product.getGoods().getSource()!=null) {
				SupplierProduct supplierProduct=supplierProductDao.getSupplierProduct(product.getGoods().getSupplierSupplier(), product.getSource());
				BigDecimal priceB=supplierProduct.getSupplyPrice().multiply(new BigDecimal(quantity));
				totalPriceB = totalPriceB.add(priceB);
				orderItem.setPriceB(priceB);
				orderItem.setPriceUnitB(supplierProduct.getSupplyPrice());
			}
			orderItem.setWeight(product.getWeight());
			orderItem.setIsDelivery(product.getIsDelivery());
			orderItem.setThumbnail(product.getThumbnail());
			orderItem.setQuantity(quantity);
			orderItem.setShippedQuantity(0);
			orderItem.setReturnedQuantity(0);
			orderItem.setProduct(product);
			orderItem.setOrder(order);
			orderItem.setSpecifications(product.getSpecifications());
			orderItems.add(orderItem);

		}

		order.setAmount(totalPrice);
		order.setPrice(totalPrice);
		order.setAmountToB(totalPriceB);
		order.setQuantity(totalQuantity);
		order.setWeight(totalWeight);

//		order.setAmount(calculateAmount(order));
		
		BigDecimal amountPayable = order.getAmount();
		if (amountPayable.compareTo(BigDecimal.ZERO) > 0) {
			if (paymentMethod == null) {
				throw new IllegalArgumentException();
			}
			order.setStatus(PaymentMethod.Type.deliveryAgainstPayment.equals(paymentMethod.getType()) ? Order.Status.pendingPayment : Order.Status.pendingReview);
			order.setPaymentMethod(paymentMethod);
			if (paymentMethod.getTimeout() != null && Order.Status.pendingPayment.equals(order.getStatus())) {
				order.setExpire(DateUtils.addMinutes(new Date(), paymentMethod.getTimeout()));
			}
			if (PaymentMethod.Method.online.equals(paymentMethod.getMethod())) {
				lock(order, member);
			}
		} else {
			order.setStatus(Order.Status.pendingReview);
			order.setPaymentMethod(null);
		}


		orderDao.persist(order);

		List<OrderItem> orderItemsAfter = order.getOrderItems();
		OrderItemLog orderItemLog = new OrderItemLog() ;

		List<OrderItemInfo> orderItemInfos = new ArrayList<>(orderItemsAfter.size());
		for(OrderItem orderItem : orderItemsAfter){
			OrderItemInfo orderItemInfo = new OrderItemInfo() ;
			orderItemInfo.setBeforeQuantity(orderItem.getQuantity());
			orderItemInfo.setAfterQuantity(orderItem.getQuantity());
			orderItemInfo.setBeforePrice((orderItem.getPrice()));
			orderItemInfo.setAfterPrice((orderItem.getPrice()));
			orderItemInfo.setBeforePriceB(orderItem.getPriceB());
			orderItemInfo.setAfterPriceB(orderItem.getPriceB());
			orderItemInfo.setProduct(orderItem.getProduct());
			orderItemInfo.setOrderItemLog(orderItemLog);
			orderItemInfos.add(orderItemInfo);
		}

		orderItemLog.setOrder(order);
		orderItemLog.setOperatorName(operator);
		orderItemLog.setOperatorType(OrderItemLog.OperatorType.create);
		orderItemLog.setType(OrderItemLog.Type.admin);
		orderItemLog.setOrderItemInfos(orderItemInfos);
		orderItemLog.setSupplierName(admin.getSupplier().getName());
		orderItemLog.setLogType(LogType.distributor);
		orderItemLogDao.persist(orderItemLog);

		//后台消息推送
		orderNewsPushDao.addOrderNewPush(supplier, order, OrderNewsPush.OrderStatus.placeAnOrder, need, supplier.getName(), need.getName(),OrderNewsPush.NoticeObject.order);
		

		OrderLog orderLog = new OrderLog();
		orderLog.setType(OrderLog.Type.create);
		orderLog.setOrder(order);
		orderLog.setOperator(operator);
		orderLog.setSupplierName(admin.getSupplier().getName());
		orderLog.setLogType(LogType.distributor);
		orderLogDao.persist(orderLog);

		//如果备注不为空，则添加备注信息
		if(StringUtils.isNotEmpty(memo)) {
			OrderRemarks orderRemarks = new OrderRemarks();
			orderRemarks.setDescription(memo);
			orderRemarks.setOrder(order);
			orderRemarks.setName(operator);
			orderRemarks.setSuppliper(admin.getSupplier().getName());
			orderRemarks.setLogType(LogType.distributor);
			orderRemarks.setMsgType(MsgType.btoc);
			orderRemarksDao.persist(orderRemarks);
			//发送模版消息
			weChatService.sendTemplateMessageByNotice(order , weChatService.getGlobalToken() , null , this.commonTemplateId , null,memo,orderRemarks.getMsgType(),orderRemarks.getLogType(),admin.getSupplier());
			
			//后台消息推送
			orderNewsPushDao.addOrderNewPush(supplier, order, OrderNewsPush.OrderStatus.leaveAMessage, need, supplier.getName(), need.getName(),OrderNewsPush.NoticeObject.order);
		}
		
		//exchangePoint(order);

		if (Setting.StockAllocationTime.order.equals(setting.getStockAllocationTime())
				|| (Setting.StockAllocationTime.payment.equals(setting.getStockAllocationTime()) && (order.getAmountPaid().compareTo(BigDecimal.ZERO) > 0 || order.getExchangePoint() > 0 || order.getAmountPayable().compareTo(BigDecimal.ZERO) <= 0))) {
			allocateStock(order);
		}
		//非流水点带下单处理
		if(!isMore){
			if(CollectionUtils.isEmpty(member.getChildMembers())){

				String url = setting.getSiteUrl() + String.format(orderDetailBrowser, order.getId());

				String notice = String.format(byOrderCreateNotice, url) ;

//				smsService.sendContent(need.getTel() , notice);

			}else{
				//weChatService.sendTemplateMessage(order , commonTemplateId , weChatService.getGlobalToken() , Order.OrderStatus.create) ;
			}


		}

		//订单创建成功,向供应商增加手机短信提醒
		smsService.sendContent(supplier.getTel() , orderNotice);
		//向供应商的接收员发送模版消息
		//weChatService.sendTemplateMessageToNoticeUser(supplier , order , Order.OrderStatus.create , commonTemplateId , weChatService.getGlobalToken() ) ;


		weChatService.sendTemplateMessageByOrderStatus(order , Order.OrderStatus.create ,  weChatService.getGlobalToken() , null , commonTemplateId , null , null);
		//采购单消息接受员
//		weChatService.sendTemplateMessageToNoticeUserPurchase(supplier , bySupplier , need , order , Order.OrderStatus.create , commonTemplateId , weChatService.getGlobalToken() , NoticeTypePurchase.Type.order_create , "");


        return order;
    }
	
	/**
	 * 流水客户代下单,无客户关系
	 * @param adderss 
	 * @param areaId 
	 */
	@Transactional
    @Override
    public Order createByOwnMore(SupplyNeed.AssignedModel assignedModel,Supplier supplier, Long needId, String memo, Date reDate, OrderProductForm orderProductForm , String operator , boolean isMore ,Admin admin, Long areaId, String adderss ) {
		Need need = needDao.find(needId) ;
		Member member = need.getMember();
		
		ShippingMethod shippingMethod = shippingMethodService.find(1L);
		PaymentMethod paymentMethod = paymentMethodDao.find(3L);


		Setting setting = SystemUtils.getSetting();
		Order order = new Order();

		order.setSupplyType(SupplyType.temporary);
		do {
			order.setSn(snDao.generate(Sn.Type.order));
		} while (orderDao.findBySn(order.getSn()) != null);
		
		//代下单
		order.setBuyType(Order.BuyType.waterSubstitute);
		if (assignedModel.equals(SupplyNeed.AssignedModel.STRAIGHT)) {
			order.setType(Order.Type.general);
		}else {
			order.setType(Order.Type.distribution);
		}
		
		order.setNeed(need);
		order.setSupplier(supplier);
		order.setReDate(reDate);
		order.setReCode(RandomStringUtils.randomNumeric(6));
		order.setFee(BigDecimal.ZERO);
		order.setPromotionDiscount(BigDecimal.ZERO);
		order.setOffsetAmount(BigDecimal.ZERO);
		order.setAmountPaid(BigDecimal.ZERO);
		order.setRefundAmount(BigDecimal.ZERO);
		order.setRewardPoint(0L);
		order.setExchangePoint(0L);

		order.setShippedQuantity(0);
		order.setReturnedQuantity(0);

		//2018-2-2 修改  收货地址信息可以修改，通过前端传参
		Area area=areaDao.find(areaId);
		order.setFreight(shippingMethodService.calculateFreight(shippingMethod, area, 0));
		order.setConsignee(need.getUserName());
		order.setZipCode("200000");
		order.setPhone(need.getTel());
		order.setAreaName(area.getFullName());
		order.setAddress(adderss);
		order.setArea(area);

		order.setMemo(memo);
		order.setIsUseCouponCode(false);
		order.setIsExchangePoint(false);
		order.setIsAllocatedStock(false);
		order.setInvoice(null);
		order.setShippingMethod(shippingMethod);
		order.setMember(member);
		order.setBuyMember(member);

		order.setPromotionNames(null);
		order.setCoupons(null);

		order.setCouponDiscount(BigDecimal.ZERO);

		order.setTax(calculateTax(order));

		BigDecimal totalPrice = BigDecimal.ZERO ;
		BigDecimal totalPriceB = BigDecimal.ZERO ;
		Integer totalQuantity = 0;
		Integer totalWeight = 0;
		
		List<OrderItem> orderItems = order.getOrderItems();
		
		for (OrderProductForm.OwnOrderItem ownOrderItem : orderProductForm.getOwnOrderItems()) {
			Long productId = ownOrderItem.getProductId() ;
			Integer quantity = ownOrderItem.getQuantity() ;
			if(null == productId || null == quantity){
				continue;
			}
			Product product = productService.find(productId) ;

			BigDecimal price=product.getTurnoverSupplyPrice().multiply(new BigDecimal(quantity));
			totalPrice = totalPrice.add(price);
			totalQuantity+=quantity;
			totalWeight+=product.getWeight() == null ? 0:product.getWeight();

			OrderItem orderItem = new OrderItem();
			orderItem.setSn(product.getSn());
			orderItem.setName(product.getName());
			orderItem.setType(product.getType());
			orderItem.setPrice(price);
			orderItem.setPriceUnit(product.getTurnoverSupplyPrice());
			if (product.getGoods().getSource()!=null) {
				SupplierProduct supplierProduct=supplierProductDao.getSupplierProduct(product.getGoods().getSupplierSupplier(), product.getSource());
				BigDecimal priceB=supplierProduct.getSupplyPrice().multiply(new BigDecimal(quantity));
				totalPriceB = totalPriceB.add(priceB);
				orderItem.setPriceB(priceB);
				orderItem.setPriceUnitB(supplierProduct.getSupplyPrice());
			}
			orderItem.setWeight(product.getWeight());
			orderItem.setIsDelivery(product.getIsDelivery());
			orderItem.setThumbnail(product.getThumbnail());
			orderItem.setQuantity(quantity);
			orderItem.setShippedQuantity(0);
			orderItem.setReturnedQuantity(0);
			orderItem.setProduct(product);
			orderItem.setOrder(order);
			orderItem.setSpecifications(product.getSpecifications());
			orderItems.add(orderItem);

		}

		order.setAmount(totalPrice);
		order.setPrice(totalPrice);
		order.setAmountToB(totalPriceB);
		order.setQuantity(totalQuantity);
		order.setWeight(totalWeight);

//		order.setAmount(calculateAmount(order));
		BigDecimal amountPayable = order.getAmount();
		if (amountPayable.compareTo(BigDecimal.ZERO) > 0) {
			if (paymentMethod == null) {
				throw new IllegalArgumentException();
			}
			order.setStatus(PaymentMethod.Type.deliveryAgainstPayment.equals(paymentMethod.getType()) ? Order.Status.pendingPayment : Order.Status.pendingReview);
			order.setPaymentMethod(paymentMethod);
			if (paymentMethod.getTimeout() != null && Order.Status.pendingPayment.equals(order.getStatus())) {
				order.setExpire(DateUtils.addMinutes(new Date(), paymentMethod.getTimeout()));
			}
			if (PaymentMethod.Method.online.equals(paymentMethod.getMethod())) {
				lock(order, member);
			}
		} else {
			order.setStatus(Order.Status.pendingReview);
			order.setPaymentMethod(null);
		}


		orderDao.persist(order);

		List<OrderItem> orderItemsAfter = order.getOrderItems();
		OrderItemLog orderItemLog = new OrderItemLog() ;

		List<OrderItemInfo> orderItemInfos = new ArrayList<>(orderItemsAfter.size());
		for(OrderItem orderItem : orderItemsAfter){
			OrderItemInfo orderItemInfo = new OrderItemInfo() ;
			orderItemInfo.setBeforeQuantity(orderItem.getQuantity());
			orderItemInfo.setAfterQuantity(orderItem.getQuantity());
			orderItemInfo.setBeforePrice((orderItem.getPrice()));
			orderItemInfo.setAfterPrice((orderItem.getPrice()));
			orderItemInfo.setBeforePriceB(orderItem.getPriceB());
			orderItemInfo.setAfterPriceB(orderItem.getPriceB());
			orderItemInfo.setProduct(orderItem.getProduct());
			orderItemInfo.setOrderItemLog(orderItemLog);
			orderItemInfos.add(orderItemInfo);
		}

		orderItemLog.setOrder(order);
		orderItemLog.setOperatorName(operator);
		orderItemLog.setOperatorType(OrderItemLog.OperatorType.create);
		orderItemLog.setType(OrderItemLog.Type.admin);
		orderItemLog.setOrderItemInfos(orderItemInfos);
		orderItemLog.setSupplierName(admin.getSupplier().getName());
		orderItemLog.setLogType(LogType.distributor);
		orderItemLogDao.persist(orderItemLog);

		//后台消息推送
		orderNewsPushDao.addOrderNewPush(order.getSupplier(), order, OrderNewsPush.OrderStatus.placeAnOrder, need, order.getSupplier().getName(), "",OrderNewsPush.NoticeObject.order);

		OrderLog orderLog = new OrderLog();
		orderLog.setType(OrderLog.Type.create);
		orderLog.setOrder(order);
		orderLog.setOperator(operator);
		orderLog.setSupplierName(admin.getSupplier().getName());
		orderLog.setLogType(LogType.distributor);
		orderLogDao.persist(orderLog);

		//如果备注不为空，则添加备注信息
		if(StringUtils.isNotEmpty(memo)) {
			OrderRemarks orderRemarks = new OrderRemarks();
			orderRemarks.setDescription(memo);
			orderRemarks.setOrder(order);
			orderRemarks.setName(operator);
			orderRemarks.setSuppliper(admin.getSupplier().getName());
			orderRemarks.setLogType(LogType.distributor);
			orderRemarks.setMsgType(MsgType.btob);
			orderRemarksDao.persist(orderRemarks);
			
			//后台消息推送
			orderNewsPushDao.addOrderNewPush(order.getSupplier(), order, OrderNewsPush.OrderStatus.leaveAMessage, need, order.getSupplier().getName(), "",OrderNewsPush.NoticeObject.order);
		}
		
		//exchangePoint(order);

		if (Setting.StockAllocationTime.order.equals(setting.getStockAllocationTime())
				|| (Setting.StockAllocationTime.payment.equals(setting.getStockAllocationTime()) && (order.getAmountPaid().compareTo(BigDecimal.ZERO) > 0 || order.getExchangePoint() > 0 || order.getAmountPayable().compareTo(BigDecimal.ZERO) <= 0))) {
			allocateStock(order);
		}
		//非流水点带下单处理
		if(!isMore){
			if(CollectionUtils.isEmpty(member.getChildMembers())){

				String url = setting.getSiteUrl() + String.format(orderDetailBrowser, order.getId());

				String notice = String.format(byOrderCreateNotice, url) ;

//				smsService.sendContent(need.getTel() , notice);

			}else{
				weChatService.sendTemplateMessage(order , commonTemplateId , weChatService.getGlobalToken() , Order.OrderStatus.create) ;
			}

			if(StringUtils.isNotEmpty(memo)){

//				weChatService.sendOrderRemarkNotice(order , member , this.commonTemplateId , weChatService.getGlobalToken() , memo);
//
//				List<NoticeUser> noticeUsers = noticeUserDao.findList(supplier ,bySupplier , NoticeType.Type.user_order_remark);
//				weChatService.sendOrderRemarkNotice(order , member , this.commonTemplateId , weChatService.getGlobalToken() , noticeUsers , memo);

			}

		}

		//订单创建成功,向供应商增加手机短信提醒
		smsService.sendContent(supplier.getTel() , orderNotice);
		//向供应商的接收员发送模版消息
		weChatService.sendTemplateMessageToNoticeUser(supplier , order , Order.OrderStatus.create , commonTemplateId , weChatService.getGlobalToken() ) ;
		//采购单消息接受员
//		weChatService.sendTemplateMessageToNoticeUserPurchase(supplier , bySupplier , need , order , Order.OrderStatus.create , commonTemplateId , weChatService.getGlobalToken() , NoticeTypePurchase.Type.order_create , "");


        return order;
    }


	/**
	 * 多地址下单
	 *
	 * @param
	 * @param orderProductForm
	 * @param operator
	 * @param orderNeedsForm
	 * @return
	 */
	@Transactional
	@Override	public void createOwnMore(SupplyNeed.AssignedModel assignedModel,Supplier supplier, OrderProductForm orderProductForm, String operator, OrderNeedsForm orderNeedsForm , Admin admin) {

		List<OrderNeedsForm.OrderNeedsItem> orderNeedsItems = orderNeedsForm.getOrderNeedsItems() ;
		//List<Order> orders = new ArrayList<>();
		for(OrderNeedsForm.OrderNeedsItem orderNeedsItem : orderNeedsItems){
			Long needId = orderNeedsItem.getNeedId() ;
			String memo = orderNeedsItem.getMemo() ;
			Date reDate = orderNeedsItem.getReDate() ;
			Long areaId = orderNeedsItem.getAreaId();
			String adderss = orderNeedsItem.getAddress();

			if(null == needId || null == reDate){
				continue;
			}

			Order order = this.createByOwnMore(assignedModel,supplier ,  needId , memo , reDate , orderProductForm , operator , true, admin,areaId,adderss);

			//orders.add(order);
		}

		//处理多地址带下单消息通知处理

		//将批量下单的信息进行入库，为了微信端的查看
		// TODO: 2017/7/28 需求变更不做批量展示
		/*Order tempOrder = orders.get(0);
		List<OrderItem> orderItems = tempOrder.getOrderItems() ;
		BatchOrderLog batchOrderLog = new BatchOrderLog() ;
		List<BatchOrderOrder> batchOrderOrders = new ArrayList<>();

		batchOrderLogDao.persist(batchOrderLog);

		List<BatchOrderItem> batchOrderItems = new ArrayList<>();
		for(OrderItem orderItem : orderItems){

			BatchOrderItem batchOrderItem = new BatchOrderItem();

			batchOrderItem.setBatchOrderLog(batchOrderLog);
			batchOrderItem.setOrderItem(orderItem);

			batchOrderItems.add(batchOrderItem) ;
		}
		for(Order order : orders){
			BatchOrderOrder batchOrderOrder = new BatchOrderOrder() ;
			batchOrderOrder.setBatchOrderLog(batchOrderLog);
			batchOrderOrder.setOrder(order);

			batchOrderOrders.add(batchOrderOrder);
		}

		batchOrderLog.setBatchOrderItems(batchOrderItems);
		batchOrderLog.setBatchOrderOrders(batchOrderOrders);*/




	}
	
	
	/**直营店代下单
     * @param supplierId   供应商id
     * @param needId
     * @param memo
     * @param reDate
     * @param orderProductForm
	 * @param operator
     * @return
     */
	@Transactional
    @Override
    public Order createByOwnFormal(Long supplierId, Long needId, String memo, Date reDate, OrderProductForm orderProductForm , String operator , boolean isMore ,Admin admin,Long areaId, String address,String consignee,String tel,SupplierType supplierType) {
		Supplier supplier = supplierDao.find(supplierId);
		Need need = needDao.find(needId) ;
		Member member = need.getMember();
		Shop shop=null;
		if (!isMore){
			shop=need.getShops().get(0);//由于是直营店，所以只对应一个shop
		}

		BigDecimal totalPrice = BigDecimal.ZERO ;
		Integer totalQuantity = 0;
		Integer totalWeight = 0;


		Supplier bySupplier = need.getSupplier() ;

		ShippingMethod shippingMethod = shippingMethodService.find(1L);
		PaymentMethod paymentMethod = paymentMethodDao.find(3L);

		List<SupplierSupplier.Status> status=new ArrayList<>();
		status.add(SupplierSupplier.Status.inTheSupply);
		SupplierSupplier supplierSupplier=supplierSupplierDao.getSupplierSupplier(bySupplier, supplier, null, status);

		Setting setting = SystemUtils.getSetting();
		Order order = new Order();

		order.setSupplyType(SupplyType.formal);
		do {
			order.setSn(snDao.generate(Sn.Type.order));
		} while (orderDao.findBySn(order.getSn()) != null);
		order.setType(Order.Type.formal);
		order.setSupplier(supplier);
		order.setToSupplier(bySupplier);
		order.setNeed(need);
		if (isMore) {
			order.setBuyType(BuyType.waterSubstitute);
		}else {
			order.setBuyType(BuyType.substitute);
		}
		order.setSupplierSupplier(supplierSupplier);
		order.setReDate(reDate);
		order.setReCode(RandomStringUtils.randomNumeric(6));
		order.setFee(BigDecimal.ZERO);
		order.setPromotionDiscount(BigDecimal.ZERO);
		order.setOffsetAmount(BigDecimal.ZERO);
		order.setAmountPaid(BigDecimal.ZERO);
		order.setRefundAmount(BigDecimal.ZERO);
		order.setRewardPoint(0L);
		order.setExchangePoint(0L);

		order.setShippedQuantity(0);
		order.setReturnedQuantity(0);

		//处理收货地址
		Area area=areaDao.find(areaId);
		if (!isMore){
			order.setFreight(shippingMethodService.calculateFreight(shippingMethod, area, 0));
			order.setConsignee(consignee);
			order.setAreaName(area.getFullName());
			order.setAddress(address);
			order.setZipCode("200000");
			order.setPhone(tel);
			order.setArea(area);
		}else {
			order.setFreight(shippingMethodService.calculateFreight(shippingMethod,area, 0));
			order.setConsignee(need.getUserName());
			order.setZipCode("200000");
			order.setPhone(need.getTel());
			order.setAreaName(area.getFullName());
			order.setAddress(address);
			order.setArea(area);
		}

		order.setMemo(memo);
		order.setIsUseCouponCode(false);
		order.setIsExchangePoint(false);
		order.setIsAllocatedStock(false);
		order.setInvoice(null);
		order.setShippingMethod(shippingMethod);
		order.setMember(member);
		order.setBuyMember(member);
		order.setShop(shop);
		order.setSupplierType(supplierType);

		order.setPromotionNames(null);
		order.setCoupons(null);

		order.setCouponDiscount(BigDecimal.ZERO);

		order.setTax(calculateTax(order));

		List<OrderItem> orderItems = order.getOrderItems();


		for (OrderProductForm.OwnOrderItem ownOrderItem : orderProductForm.getOwnOrderItems()) {
			Long productId = ownOrderItem.getProductId() ;
			Integer quantity = ownOrderItem.getQuantity() ;
			if(null == productId || null == quantity){
				continue;
			}
			Product product = productService.find(productId) ;
			
			OrderItem orderItem = new OrderItem();
			orderItem.setSn(product.getSn());
			orderItem.setName(product.getName());
			orderItem.setType(product.getType());

			SupplierProduct supplierProduct = supplierProductDao.getProduct(productId, supplierId, bySupplier.getId(), SupplierSupplier.Status.inTheSupply, need.getId());
			totalPrice = totalPrice.add(supplierProduct.getSupplyPrice().multiply(new BigDecimal(quantity)));
			totalQuantity+=quantity;
			totalWeight+=product.getWeight() == null ? 0:product.getWeight();

			orderItem.setPrice(supplierProduct.getSupplyPrice().multiply(new BigDecimal(quantity)));
			orderItem.setPriceB(supplierProduct.getSupplyPrice().multiply(new BigDecimal(quantity)));
			orderItem.setPriceUnit(supplierProduct.getSupplyPrice());
			orderItem.setPriceUnitB(supplierProduct.getSupplyPrice());
			orderItem.setWeight(product.getWeight());
			orderItem.setIsDelivery(product.getIsDelivery());
			orderItem.setThumbnail(product.getThumbnail());
			orderItem.setQuantity(quantity);
			orderItem.setShippedQuantity(0);
			orderItem.setReturnedQuantity(0);
			orderItem.setProduct(product);
			orderItem.setOrder(order);
			orderItem.setSpecifications(product.getSpecifications());
			orderItems.add(orderItem);

		}

		order.setPrice(totalPrice);
		order.setAmount(totalPrice);
		order.setAmountToB(totalPrice);
		order.setQuantity(totalQuantity);
		order.setWeight(totalWeight);

		BigDecimal amountPayable = order.getAmount();
		if (amountPayable.compareTo(BigDecimal.ZERO) > 0) {
			if (paymentMethod == null) {
				throw new IllegalArgumentException();
			}
			order.setStatus(PaymentMethod.Type.deliveryAgainstPayment.equals(paymentMethod.getType()) ? Order.Status.pendingPayment : Order.Status.pendingReview);
			order.setPaymentMethod(paymentMethod);
			if (paymentMethod.getTimeout() != null && Order.Status.pendingPayment.equals(order.getStatus())) {
				order.setExpire(DateUtils.addMinutes(new Date(), paymentMethod.getTimeout()));
			}
			if (PaymentMethod.Method.online.equals(paymentMethod.getMethod())) {
				lock(order, member);
			}
		} else {
			order.setStatus(Order.Status.pendingReview);
			order.setPaymentMethod(null);
		}


		orderDao.persist(order);

		List<OrderItem> orderItemsAfter = order.getOrderItems();
		OrderItemLog orderItemLog = new OrderItemLog() ;

		List<OrderItemInfo> orderItemInfos = new ArrayList<>(orderItemsAfter.size());
		for(OrderItem orderItem : orderItemsAfter){
			OrderItemInfo orderItemInfo = new OrderItemInfo() ;
			orderItemInfo.setBeforeQuantity(orderItem.getQuantity());
			orderItemInfo.setAfterQuantity(orderItem.getQuantity());
			orderItemInfo.setBeforePrice((orderItem.getPrice()));
			orderItemInfo.setAfterPrice((orderItem.getPrice()));
			orderItemInfo.setBeforePriceB(orderItem.getPriceB());
			orderItemInfo.setAfterPriceB(orderItem.getPriceB());
			orderItemInfo.setProduct(orderItem.getProduct());
			orderItemInfo.setOrderItemLog(orderItemLog);
			orderItemInfos.add(orderItemInfo);
		}

		orderItemLog.setOrder(order);
		orderItemLog.setOperatorName(operator);
		orderItemLog.setOperatorType(OrderItemLog.OperatorType.create);
		orderItemLog.setType(OrderItemLog.Type.admin);
		orderItemLog.setOrderItemInfos(orderItemInfos);
		orderItemLog.setSupplierName(admin.getSupplier().getName());
		orderItemLog.setLogType(LogType.distributor);
		orderItemLogDao.persist(orderItemLog);

		//后台消息推送
		orderNewsPushDao.addOrderNewPush(order.getSupplier(), order, OrderNewsPush.OrderStatus.placeAnOrder, need, order.getToSupplier().getName(), "",OrderNewsPush.NoticeObject.order);
		orderNewsPushDao.addOrderNewPush(order.getToSupplier(), order, OrderNewsPush.OrderStatus.placeAnOrder, need, order.getToSupplier().getName(), need.getName(),OrderNewsPush.NoticeObject.purchase);

		OrderLog orderLog = new OrderLog();
		orderLog.setType(OrderLog.Type.create);
		orderLog.setOrder(order);
		orderLog.setOperator(operator);
		orderLog.setSupplierName(admin.getSupplier().getName());
		orderLog.setLogType(LogType.distributor);
		orderLogDao.persist(orderLog);

		//如果备注不为空，则添加备注信息
		if(StringUtils.isNotEmpty(memo)) {
			OrderRemarks orderRemarks = new OrderRemarks();
			orderRemarks.setDescription(memo);
			orderRemarks.setOrder(order);
			orderRemarks.setName(operator);
			orderRemarks.setSuppliper(admin.getSupplier().getName());
			orderRemarks.setLogType(LogType.distributor);
			orderRemarks.setMsgType(MsgType.btoc);
			orderRemarksDao.persist(orderRemarks);
			
			//后台消息推送
			orderNewsPushDao.addOrderNewPush(order.getSupplier(), order, OrderNewsPush.OrderStatus.leaveAMessage, need, order.getToSupplier().getName(), "",OrderNewsPush.NoticeObject.order);
			orderNewsPushDao.addOrderNewPush(order.getToSupplier(), order, OrderNewsPush.OrderStatus.leaveAMessage, need, order.getToSupplier().getName(), "",OrderNewsPush.NoticeObject.purchase);
		}
		
		//exchangePoint(order);

		if (Setting.StockAllocationTime.order.equals(setting.getStockAllocationTime())
				|| (Setting.StockAllocationTime.payment.equals(setting.getStockAllocationTime()) && (order.getAmountPaid().compareTo(BigDecimal.ZERO) > 0 || order.getExchangePoint() > 0 || order.getAmountPayable().compareTo(BigDecimal.ZERO) <= 0))) {
			allocateStock(order);
		}
		//非流水点带下单处理
		if(!isMore){
			if(CollectionUtils.isEmpty(member.getChildMembers())){

				String url = setting.getSiteUrl() + String.format(orderDetailBrowser, order.getId());

				String notice = String.format(byOrderCreateNotice, url) ;

//				smsService.sendContent(need.getTel() , notice);

			}else{
				weChatService.sendTemplateMessage(order , commonTemplateId , weChatService.getGlobalToken() , Order.OrderStatus.create) ;
			}

			if(StringUtils.isNotEmpty(memo)){

//				weChatService.sendOrderRemarkNotice(order , member , this.commonTemplateId , weChatService.getGlobalToken() , memo);
//
//				List<NoticeUser> noticeUsers = noticeUserDao.findList(supplier ,bySupplier , NoticeType.Type.user_order_remark);
//				weChatService.sendOrderRemarkNotice(order , member , this.commonTemplateId , weChatService.getGlobalToken() , noticeUsers , memo);

			}

		}

		//订单创建成功,向供应商增加手机短信提醒
		smsService.sendContent(supplier.getTel() , orderNotice);
		//向供应商的接收员发送模版消息
		weChatService.sendTemplateMessageToNoticeUser(supplier , order , Order.OrderStatus.create , commonTemplateId , weChatService.getGlobalToken() ) ;
		//采购单消息接受员
//		weChatService.sendTemplateMessageToNoticeUserPurchase(supplier , bySupplier , need , order , Order.OrderStatus.create , commonTemplateId , weChatService.getGlobalToken() , NoticeTypePurchase.Type.order_create , "");


        return order;
    }
	
	
	/**
	 * 正式供应多地址下单
	 *
	 * @param supplierId
	 * @param orderProductForm
	 * @param operator
	 * @param orderNeedsForm
	 * @return
	 */
	@Transactional
	@Override
	public void createOwnMoreFormal(Long supplierId, OrderProductForm orderProductForm, String operator, OrderNeedsForm orderNeedsForm , Admin admin) {
		List<OrderNeedsForm.OrderNeedsItem> orderNeedsItems = orderNeedsForm.getOrderNeedsItems() ;
		//List<Order> orders = new ArrayList<>();
		for(OrderNeedsForm.OrderNeedsItem orderNeedsItem : orderNeedsItems){
			Long needId = orderNeedsItem.getNeedId() ;
			String memo = orderNeedsItem.getMemo() ;
			Date reDate = orderNeedsItem.getReDate() ;
			Long areaId = orderNeedsItem.getAreaId();
			String address = orderNeedsItem.getAddress();

			if(null == needId || null == reDate){
				continue;
			}

			Order order = this.createByOwnFormal(supplierId ,  needId , memo , reDate , orderProductForm , operator , true, admin,areaId,address,null,null,SupplierType.TWO);

			//orders.add(order);
		}

		//处理多地址带下单消息通知处理

		//将批量下单的信息进行入库，为了微信端的查看
		// TODO: 2017/7/28 需求变更不做批量展示
		/*Order tempOrder = orders.get(0);
		List<OrderItem> orderItems = tempOrder.getOrderItems() ;
		BatchOrderLog batchOrderLog = new BatchOrderLog() ;
		List<BatchOrderOrder> batchOrderOrders = new ArrayList<>();

		batchOrderLogDao.persist(batchOrderLog);

		List<BatchOrderItem> batchOrderItems = new ArrayList<>();
		for(OrderItem orderItem : orderItems){

			BatchOrderItem batchOrderItem = new BatchOrderItem();

			batchOrderItem.setBatchOrderLog(batchOrderLog);
			batchOrderItem.setOrderItem(orderItem);

			batchOrderItems.add(batchOrderItem) ;
		}
		for(Order order : orders){
			BatchOrderOrder batchOrderOrder = new BatchOrderOrder() ;
			batchOrderOrder.setBatchOrderLog(batchOrderLog);
			batchOrderOrder.setOrder(order);

			batchOrderOrders.add(batchOrderOrder);
		}

		batchOrderLog.setBatchOrderItems(batchOrderItems);
		batchOrderLog.setBatchOrderOrders(batchOrderOrders);*/




	}


	/**
	 *
	 *
	 * @param order
	 * @param operatorName 操作人名称
	 */
	@Override
	public void completeByApi(Order order, String operatorName , String supplierName) {
		Assert.notNull(order);
		Assert.isTrue(!order.isNew());
		Assert.state(!order.hasExpired()/* && Order.Status.received.equals(order.getStatus())*/);

		Member member = order.getMember();
		if (order.getRewardPoint() > 0) {
			memberService.addPoint(member, order.getRewardPoint(), PointLog.Type.reward, null, null);
		}
		if (CollectionUtils.isNotEmpty(order.getCoupons())) {
			for (Coupon coupon : order.getCoupons()) {
				couponCodeService.generate(coupon, member);
			}
		}
		if (order.getAmountPaid().compareTo(BigDecimal.ZERO) > 0) {
			memberService.addAmount(member, order.getAmountPaid());
		}
		for (OrderItem orderItem : order.getOrderItems()) {
			Product product = orderItem.getProduct();
			if (product != null && product.getGoods() != null) {
				goodsService.addSales(product.getGoods(), orderItem.getQuantity());
			}
		}

		order.setStatus(Order.Status.completed);
		order.setCompleteDate(new Date());

		OrderLog orderLog = new OrderLog();
		orderLog.setType(OrderLog.Type.complete);
		orderLog.setOperator(operatorName);
		orderLog.setOrder(order);
		orderLog.setSupplierName(supplierName);
		orderLog.setLogType(LogType.member);
		orderLogDao.persist(orderLog);

		mailService.sendCompleteOrderMail(order);
		smsService.sendCompleteOrderSms(order);

		//订货单消息接受员接受消息
		//weChatService.sendTemplateMessageToNoticeUser(order.getSupplier() , order , Order.OrderStatus.completed , commonTemplateId ,  weChatService.getGlobalToken()) ;

		weChatService.sendTemplateMessageByOrderStatus(order , Order.OrderStatus.completed ,  weChatService.getGlobalToken() , null , commonTemplateId , null , null);

	}


	/**
	 * 取消申请
	 *
	 * @param order
	 * @param operatorName 申请人
	 */
	@Override
	public void applyCancel(Order order, String operatorName , Supplier supplier) {
		order.setBeforeStatus(order.getStatus());
		//orderDao.flush();
		order.setStatus(Order.Status.canceled);
		order.setExpire(null);

		OrderLog orderLog = new OrderLog();
		orderLog.setType(OrderLog.Type.applyCancel);
		orderLog.setOrder(order);
		orderLog.setOperator(operatorName);
		orderLog.setSupplierName(supplier.getName());
		orderLog.setLogType(LogType.member);
		orderLogDao.persist(orderLog);
		
		if(order.getType() == Order.Type.formal) {
			//orderNewsPushDao.addOrderNewPush(order.getSupplier(), order, OrderNewsPush.OrderStatus.applicationCancel, order.getNeed(), order.getToSupplier().getName(), order.getNeed().getName(), OrderNewsPush.NoticeObject.order);
			//orderNewsPushDao.addOrderNewPush(order.getToSupplier(), order, OrderNewsPush.OrderStatus.applicationCancel, order.getNeed(),order.getToSupplier().getName(), order.getNeed().getName(), OrderNewsPush.NoticeObject.purchase);
		}else {
			orderNewsPushDao.addOrderNewPush(order.getSupplier(), order, OrderNewsPush.OrderStatus.applicationCancel, order.getNeed(), order.getSupplier().getName(), null, OrderNewsPush.NoticeObject.order);
		}
		//发送模版消息
		//weChatService.sendTemplateMessageByOrderStatus(order , Order.OrderStatus.applyCancel ,  weChatService.getGlobalToken() , null , commonTemplateId , null , null);

	}


	/**
	 * @param type
	 * @param status
	 * @param memberUsername
	 * @param goods
	 * @param isPendingReceive
	 * @param isPendingRefunds
	 * @param isUseCouponCode
	 * @param isExchangePoint
	 * @param isAllocatedStock
	 * @param hasExpired
	 * @param pageable
	 * @param supplier
	 * @param startDate
	 * @param endDate
	 * @param searchName
	 * @return
	 */
	@Override
	public ExcelView downOrderOwn(Order.Type type, Order.Status status, String memberUsername, Goods goods, Boolean isPendingReceive, Boolean isPendingRefunds, Boolean isUseCouponCode, Boolean isExchangePoint, Boolean isAllocatedStock, Boolean hasExpired, Pageable pageable, Supplier supplier, Date startDate, Date endDate , String searchName , String timeSearch) {

		String filename = "订单列表" + DateFormatUtils.format(new Date(), "yyyy-MM-dd") + "-采购单.xls";
		String[] titles = new String[]{"订单编号" , "下单时间" , "订单状态" , "商品编号" , "商品名称" , "商品规格" , "基本单位" , "订货数量", "发货数量" , "实收数量" , "商品金额" , "供应商" , "收货点" , "收货人", "手机号", "收货地址" , "收货时间"};
		if(null == pageable){
			pageable = new Pageable(1, Integer.MAX_VALUE , true) ;
		}else {
			pageable.setPageNumber(1);
			pageable.setPageSize(Integer.MAX_VALUE , true);
		}

		List<Order> datas = null;
		Member member = memberService.findByUsername(memberUsername);
		if (null == supplier || CollectionUtils.isEmpty(supplier.getNeeds()) || (StringUtils.isNotEmpty(memberUsername) && member == null)) {
			datas = null;
		} else {
			datas = orderDao.findPage(type, status,null, member, null, isPendingReceive, isPendingRefunds, null, null, isAllocatedStock, hasExpired, pageable , null , startDate , endDate , searchName , timeSearch,supplier).getContent();
		}

		return this.downPurchaseOrder(filename , datas , titles) ;
	}

	/**
	 * @param type
	 * @param status
	 * @param memberUsername
	 * @param goods
	 * @param isPendingReceive
	 * @param isPendingRefunds
	 * @param isUseCouponCode
	 * @param isExchangePoint
	 * @param isAllocatedStock
	 * @param hasExpired
	 * @param pageable
	 * @param supplier
	 */
	@Override
	public ExcelView downOrderCustomer(Order.Type type, Order.Status status, String memberUsername, Goods goods, Boolean isPendingReceive, Boolean isPendingRefunds, Boolean isUseCouponCode, Boolean isExchangePoint, Boolean isAllocatedStock, Boolean hasExpired, Pageable pageable, Supplier supplier , Date startDate, Date endDate , String searchName , String timeSearch) {

		String filename = "订单列表" + DateFormatUtils.format(new Date(), "yyyy-MM-dd") + "-订货单.xls";
		String[] titles = null;
		if(supplier == null) {
			titles = new String[]{"订单编号" , "下单时间" , "订单状态" , "供应商" , "客户名称" , "商品编号" , "商品名称" , "商品规格" , "基本单位" , "订货数量","发货数量","实收数量","商品金额" , "收货点" , "收货人", "手机号", "收货地址" , "收货时间"};
		}else {
			titles = new String[]{"订单编号" , "下单时间" , "订单状态" , "商品编号" , "商品名称" , "商品规格" , "基本单位" , "订货数量","发货数量","实收数量","商品金额" ,"客户名称" , "收货点" , "收货人", "手机号", "收货地址" , "收货时间"};
		}
		
		if(null == pageable){
			pageable = new Pageable(1, Integer.MAX_VALUE , true) ;
		}else {
			pageable.setPageNumber(1);
			pageable.setPageSize(Integer.MAX_VALUE , true);
		}

		// TODO: 2017/5/8 这里使用分页方法 ，后期需要修改
		List<Order> datas = null;
		Member member = memberService.findByUsername(memberUsername);
		if (StringUtils.isNotEmpty(memberUsername) && member == null) {
			datas = null;
		} else {
			datas = orderDao.findPage(type, status,null, member, null, isPendingReceive, 
					isPendingRefunds, null, null, isAllocatedStock, hasExpired, pageable, supplier , 
					startDate , endDate , searchName , timeSearch, null).getContent();
		}
		ExcelView excelView = null;
		if(supplier == null) {
			excelView = this.downOrder(filename , datas , titles , supplier);
		}else {
			excelView = this.downOrder(filename , datas , titles);
		}

		return excelView ;
	}


	/**
	 * 供应商导出
	 */
	@Override
	public ExcelView downOrder(String fileName, List<Order> orders, String[] titles) {
		return new ExcelView(fileName, null, null, titles , null, null, orders, null){
			@Override
			public void buildExcelDocument(Map<String, Object> model, HSSFWorkbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
				//创建表头
				HSSFSheet sheet;
				if (StringUtils.isNotEmpty(this.getSheetName())) {
					sheet = workbook.createSheet(this.getSheetName());
				} else {
					sheet = workbook.createSheet();
				}

				int rowNumber = 0;
				String[] titles = this.getTitles() ;

				HSSFCellStyle cellStyle = workbook.createCellStyle();
				//填充色
				cellStyle.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
				cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

				cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
				cellStyle.setBorderTop(HSSFBorderFormatting.BORDER_THIN);
				cellStyle.setBorderBottom(HSSFBorderFormatting.BORDER_THIN);
				cellStyle.setBorderLeft(HSSFBorderFormatting.BORDER_THIN);
				cellStyle.setBorderRight(HSSFBorderFormatting.BORDER_THIN);

				HSSFFont font = workbook.createFont();

				font.setFontHeightInPoints((short) 11);
				font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

				cellStyle.setFont(font);

				HSSFCellStyle contentStyle = workbook.createCellStyle();
				contentStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);

				//处理表头
				if (titles != null && titles.length > 0) {
					HSSFRow header = sheet.createRow(rowNumber);
					header.setHeight((short) 400);
					for (int i = 0 , len = titles.length; i < len; i++) {
						HSSFCell cell = header.createCell(i);
						cell.setCellStyle(cellStyle);
						cell.setCellValue(titles[i]);

					}
					rowNumber++;
				}

				//处理内容
				List<Order> datas = (List<Order>)this.getData() ;

				if(CollectionUtils.isNotEmpty(datas)){
					for(Order order : datas){
						//获取商品数量
						List<OrderItem> orderItems = order.getOrderItems() ;

						Need need =order.getNeed();

						for(int i=0 ,len = orderItems.size() ; i < len ; i++){

							//创建行
							HSSFRow row = sheet.createRow(rowNumber);
							OrderItem orderItem = orderItems.get(i) ;

							Product product = orderItem.getProduct() ;
							Goods goods = product.getGoods() ;

							//sn
							row.createCell(0).setCellValue(order.getSn());
							//sheet.autoSizeColumn(0);

							row.createCell(1).setCellValue(com.microBusiness.manage.util.DateUtils.formatDateToString(order.getCreateDate(), DateformatEnum.yyyyMMddHHmmss2));
							//sheet.autoSizeColumn(1);

							row.createCell(2).setCellValue(SpringUtils.getMessage("Order.Status." + order.getStatus()));

							row.createCell(3).setCellValue(product.getSn());
							//sheet.autoSizeColumn(3);

							row.createCell(4).setCellValue(orderItem.getName());
							//sheet.autoSizeColumn(4);

							row.createCell(5).setCellValue(StringUtils.join(orderItem.getSpecifications(), ","));
							//sheet.autoSizeColumn(5);

							String basicUnit = SpringUtils.getMessage("Goods.unit." + goods.getUnit());
							if (goods.getUnit() == null) {
								basicUnit = "";
							}
							row.createCell(6).setCellValue(basicUnit);
							//sheet.autoSizeColumn(6);

							row.createCell(7).setCellValue(orderItem.getQuantity());
							//sheet.autoSizeColumn(7);

							row.createCell(8).setCellValue(orderItem.getShippedQuantity());
							
							row.createCell(9).setCellValue(order.getRealProductQuantity(product.getId()));
							
							row.createCell(10).setCellValue(orderItem.getPrice().doubleValue());
							//sheet.autoSizeColumn(8);
							
							row.createCell(11).setCellValue(need.getSupplier().getName());

							row.createCell(12).setCellValue(need.getName());
							//sheet.autoSizeColumn(9);

							row.createCell(13).setCellValue(order.getConsignee());
							//sheet.autoSizeColumn(10);

							row.createCell(14).setCellValue(order.getPhone());
							//sheet.autoSizeColumn(11);

							row.createCell(15).setCellValue(order.getAreaName() + " " + order.getAddress());
							//sheet.autoSizeColumn(12);

							row.createCell(16).setCellValue(com.microBusiness.manage.util.DateUtils.formatDateToString(order.getReDate(), DateformatEnum.yyyyMMdd2));
							//sheet.autoSizeColumn(13);

							rowNumber++;
						}
					}
				}

				response.setContentType("application/force-download");
				if (StringUtils.isNotEmpty(this.getFileName())) {
					/**
					 * 原来文件名的处理方式，火狐浏览器导出文件名出现乱码
					 * URLEncoder.encode(fileName, "UTF-8")
					 */
					String agent = request.getHeader("USER-AGENT").toLowerCase();
					if(agent.contains("firefox")) {
						response.setHeader("Content-disposition", "attachment; filename=" + new String(this.getFileName().getBytes(), "ISO8859-1"));
					}else{
						response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(this.getFileName(), "UTF-8"));
					}
					
				} else {
					response.setHeader("Content-disposition", "attachment");
				}

			}
		};
	}

	public void review(Order order, boolean passed, Admin operator , String deniedReason ) {
		Assert.notNull(order);
		Assert.isTrue(!order.isNew());
		Assert.state(!order.hasExpired() && Order.Status.pendingReview.equals(order.getStatus()));
		
		Need need = order.getNeed();
		OrderNewsPush orderNewsPush = new OrderNewsPush();
		if (passed) {
			order.setStatus(Order.Status.pendingShipment);

			//后台消息推送
			if(order.getType().equals(Order.Type.billDistribution)) {
				orderNewsPushDao.addOrderNewPush(operator.getSupplier(), order, OrderNewsPush.OrderStatus.reviewBy, need, operator.getSupplier().getName(), "",OrderNewsPush.NoticeObject.order);
				orderNewsPushDao.addOrderNewPush(order.getToSupplier(), order, OrderNewsPush.OrderStatus.reviewBy, need, operator.getSupplier().getName(), "",OrderNewsPush.NoticeObject.purchase);
				orderNewsPushDao.addOrderNewPush(order.getToSupplier(), order, OrderNewsPush.OrderStatus.reviewBy, need, operator.getSupplier().getName(), "",OrderNewsPush.NoticeObject.order);
			}else if(order.getType() == Order.Type.formal) {
				orderNewsPushDao.addOrderNewPush(operator.getSupplier(), order, OrderNewsPush.OrderStatus.reviewBy, need, operator.getSupplier().getName(), "",OrderNewsPush.NoticeObject.order);
				orderNewsPushDao.addOrderNewPush(order.getToSupplier(), order, OrderNewsPush.OrderStatus.reviewBy, need, operator.getSupplier().getName(), "",OrderNewsPush.NoticeObject.purchase);
			}else {
				orderNewsPushDao.addOrderNewPush(operator.getSupplier(), order, OrderNewsPush.OrderStatus.reviewBy, need, operator.getSupplier().getName(), "",OrderNewsPush.NoticeObject.order);
			}
			
		} else {
			order.setStatus(Order.Status.denied);
			order.setDeniedReason(deniedReason);

			undoUseCouponCode(order);
			undoExchangePoint(order);
			releaseAllocatedStock(order);
			
			//后台消息推送
			if(order.getType().equals(Order.Type.billDistribution)) {
				orderNewsPushDao.addOrderNewPush(operator.getSupplier(), order, OrderNewsPush.OrderStatus.refuse, need, operator.getSupplier().getName(), "",OrderNewsPush.NoticeObject.order);
				orderNewsPushDao.addOrderNewPush(order.getToSupplier(), order, OrderNewsPush.OrderStatus.refuse, need, operator.getSupplier().getName(), "",OrderNewsPush.NoticeObject.purchase);
			}else if(order.getType() == Order.Type.formal) {
				orderNewsPushDao.addOrderNewPush(operator.getSupplier(), order, OrderNewsPush.OrderStatus.refuse, need, operator.getSupplier().getName(), "",OrderNewsPush.NoticeObject.order);
				orderNewsPushDao.addOrderNewPush(order.getToSupplier(), order, OrderNewsPush.OrderStatus.refuse, need, operator.getSupplier().getName(), "",OrderNewsPush.NoticeObject.purchase);
			}else {
				orderNewsPushDao.addOrderNewPush(operator.getSupplier(), order, OrderNewsPush.OrderStatus.refuse, need, operator.getSupplier().getName(), "",OrderNewsPush.NoticeObject.order);
			}
			
		}

		OrderLog orderLog = new OrderLog();
		orderLog.setType(OrderLog.Type.review);
		orderLog.setOperator(operator);
		orderLog.setOrder(order);
		if (order.getToSupplier()==null) {
			orderLog.setLogType(LogType.distributor);
		}else {
			orderLog.setLogType(LogType.supplier);
		}
		orderLogDao.persist(orderLog);

		if (!passed) {
			/*weChatService.sendTemplateMessage(order , commonTemplateId , weChatService.getGlobalToken() , Order.OrderStatus.denied) ;
			//采购单企业接受员通知
			weChatService.sendTemplateMessageToNoticeUserPurchase(order , Order.OrderStatus.denied , commonTemplateId , weChatService.getGlobalToken() , NoticeTypePurchase.Type.order_review , "");*/

			String remark = "原因：" + deniedReason ;
			//发送模版消息
			weChatService.sendTemplateMessageByOrderStatus(order , Order.OrderStatus.denied ,  weChatService.getGlobalToken() , null , commonTemplateId , null , remark);

		}else{
			String remark = "您的订单已通过审核！" ;
			/*weChatService.sendTemplateMessage(order , commonTemplateId , weChatService.getGlobalToken() , Order.OrderStatus.passed , remark) ;
			//采购单企业接受员通知
			weChatService.sendTemplateMessageToNoticeUserPurchase(order , Order.OrderStatus.passed , commonTemplateId , weChatService.getGlobalToken() , NoticeTypePurchase.Type.order_review , remark);*/

			//发送模版消息
			weChatService.sendTemplateMessageByOrderStatus(order , Order.OrderStatus.passed ,  weChatService.getGlobalToken() , null , commonTemplateId , null , remark);


		}

		mailService.sendReviewOrderMail(order);
		smsService.sendReviewOrderSms(order);
	}

	@Override
	public void applicationCancel(Order order, Admin operator) {
		order.setBeforeStatus(order.getStatus());
		//orderDao.flush();
		order.setStatus(Order.Status.applyCancel);
		order.setExpire(null);


		OrderLog orderLog = new OrderLog();
		orderLog.setType(OrderLog.Type.applyCancel);
		orderLog.setOrder(order);
		orderLog.setOperator(operator);
		orderLogDao.persist(orderLog);

		weChatService.sendTemplateMessageByOrderStatus(order , Order.OrderStatus.applyCancel ,  weChatService.getGlobalToken() , null , commonTemplateId , null , null);
		
		orderNewsPushDao.addOrderNewPush(order.getSupplier(), order, OrderNewsPush.OrderStatus.applicationCancel, order.getNeed(), order.getToSupplier().getName(), "", OrderNewsPush.NoticeObject.order);
		orderNewsPushDao.addOrderNewPush(order.getToSupplier(), order, OrderNewsPush.OrderStatus.applicationCancel, order.getNeed(), order.getToSupplier().getName(), "", OrderNewsPush.NoticeObject.purchase);

	}


	/**
	 * 后台管理员修改商品数量和收货时间
	 *
	 * @param order
	 * @param orderItemUpdateForm
	 * @param admin
	 */
	@Override
	@Transactional
	public void updateItems(Order order, OrderItemUpdateForm orderItemUpdateForm, Admin admin) {
		boolean isUpd = false ;
		boolean dataChange = false ;

		Date reDate = orderItemUpdateForm.getReDate() ;

        List<OrderItem> orderItems = orderItemUpdateForm.getOrderItems() ;

        Integer allQuantity =  0;

        List<OrderItemInfo> orderItemInfos = new ArrayList<>();

        //获取最近一条日志信息
		OrderItemLog currItemLog = CollectionUtils.isEmpty(order.getOrderItemLogs()) ? null : order.getOrderItemLogs().get(0);

        OrderItemLog orderItemLog = new OrderItemLog() ;

        BigDecimal amount = BigDecimal.ZERO;
        BigDecimal amountToB = BigDecimal.ZERO;
        
        for (OrderItem orderItem : orderItems) {
			if (orderItem.getStatus()==OrderItem.Status.edit) {
				OrderItem oldOrderItem=orderItemService.find(orderItem.getId());
				Integer updQuantity = orderItem.getCheckQuantity();
				Integer beforeQuantity = oldOrderItem.getQuantity() ;
				BigDecimal afterPrice=oldOrderItem.getPriceUnit().multiply(new BigDecimal(updQuantity));
				BigDecimal afterPriceB=BigDecimal.ZERO;
				if (!order.getType().equals(Order.Type.formal)) {
					if (oldOrderItem.getType().equals(Goods.Type.distribution)) {
						afterPriceB=oldOrderItem.getPriceUnitB().multiply(new BigDecimal(updQuantity));
					}
				}else {
					afterPriceB=afterPrice;
				}
				if(updQuantity.compareTo(beforeQuantity) != 0){
					//如果数量有变动的话， 则进行商品库存分配修改
					productService.addAllocatedStock(oldOrderItem.getProduct() , (updQuantity - beforeQuantity));
					
					OrderItemInfo orderItemInfo = new OrderItemInfo() ;
					orderItemInfo.setBeforeQuantity(oldOrderItem.getQuantity());
					orderItemInfo.setAfterQuantity(updQuantity);
					orderItemInfo.setBeforePrice((oldOrderItem.getPrice()));
					orderItemInfo.setAfterPrice(afterPrice);
					orderItemInfo.setBeforePriceB(oldOrderItem.getPriceB());
					orderItemInfo.setAfterPriceB(afterPriceB);
					orderItemInfo.setProduct(oldOrderItem.getProduct());
					orderItemInfo.setOrderItemLog(orderItemLog);
					orderItemInfos.add(orderItemInfo);
					
					isUpd = true ;
				}
				oldOrderItem.setPrice(afterPrice);
				oldOrderItem.setPriceB(afterPriceB);
				oldOrderItem.setQuantity(updQuantity);
				amount=amount.add(afterPrice);
				amountToB=amountToB.add(afterPriceB);
				allQuantity += updQuantity ;
			}else if (orderItem.getStatus()==OrderItem.Status.delete) {
				OrderItem oldOrderItem=orderItemService.find(orderItem.getId());
				Integer updQuantity = 0;
				Integer beforeQuantity = oldOrderItem.getQuantity() ;
				//进行商品库存分配修改
				productService.addAllocatedStock(oldOrderItem.getProduct() , (updQuantity - beforeQuantity));

				OrderItemInfo orderItemInfo = new OrderItemInfo() ;
				orderItemInfo.setBeforeQuantity(oldOrderItem.getQuantity());
				orderItemInfo.setAfterQuantity(updQuantity);
				orderItemInfo.setBeforePrice((oldOrderItem.getPrice()));
				orderItemInfo.setAfterPrice(BigDecimal.ZERO);
				orderItemInfo.setBeforePriceB(oldOrderItem.getPriceB());
				orderItemInfo.setAfterPriceB(BigDecimal.ZERO);
				orderItemInfo.setProduct(oldOrderItem.getProduct());
				orderItemInfo.setOrderItemLog(orderItemLog);
				orderItemInfos.add(orderItemInfo);
				
				orderItemService.delete(oldOrderItem);
				
				isUpd = true ;
			}else if (orderItem.getStatus()==OrderItem.Status.add) {
				Product product=productService.find(orderItem.getProductId());
				Integer updQuantity = orderItem.getCheckQuantity();
				Integer beforeQuantity = 0;
				//进行商品库存分配修改
				productService.addAllocatedStock(product , (updQuantity - beforeQuantity));
				orderItem.setSn(product.getSn());
	            orderItem.setName(product.getName());
	            orderItem.setType(product.getType());
	            
	            if (!order.getType().equals(Order.Type.formal)) {
	            	//查找客户供应关系里面的价格
	            	if (order.getSupplyNeed() != null) {
	            		NeedProduct needProduct=needProductDao.findByNeedSupplier(order.getSupplyNeed(), product);
	            		orderItem.setPriceUnit(needProduct.getSupplyPrice());
	            		orderItem.setPrice(needProduct.getSupplyPrice().multiply(new BigDecimal(updQuantity)));
	            	}else {
	            		BigDecimal priceUnit=product.getTurnoverSupplyPrice();
	            		if (priceUnit == null) {
	            			priceUnit=BigDecimal.ZERO;
	            		}
	            		orderItem.setPriceUnit(priceUnit);
	            		orderItem.setPrice(priceUnit.multiply(new BigDecimal(updQuantity)));
	            	}
	            	amount= amount.add(orderItem.getPrice());
	            	//如果为分销商品的时候就计算企业与企业之间的商品价格
	            	if (product.getSource() != null) {
	            		SupplierSupplier supplierSupplier=product.getGoods().getSupplierSupplier();
	            		SupplierProduct supplierProduct=supplierProductDao.getSupplierProduct(supplierSupplier, product.getSource());
	            		orderItem.setPriceUnitB(supplierProduct.getSupplyPrice());
	            		orderItem.setPriceB(supplierProduct.getSupplyPrice().multiply(new BigDecimal(updQuantity)));
	            		amountToB=amountToB.add(orderItem.getPriceB());
	            	}
	            }else {
	            	SupplierSupplier supplierSupplier=order.getSupplierSupplier();
	            	SupplierProduct supplierProduct=supplierProductDao.getSupplierProduct(supplierSupplier, product);
	            	orderItem.setPriceUnit(supplierProduct.getSupplyPrice());
            		orderItem.setPrice(supplierProduct.getSupplyPrice().multiply(new BigDecimal(updQuantity)));
	            	orderItem.setPriceUnitB(supplierProduct.getSupplyPrice());
            		orderItem.setPriceB(supplierProduct.getSupplyPrice().multiply(new BigDecimal(updQuantity)));
            		amount= amount.add(orderItem.getPrice());
            		amountToB=amountToB.add(orderItem.getPriceB());
				}
	            
	            
	            orderItem.setWeight(product.getWeight());
	            orderItem.setIsDelivery(product.getIsDelivery());
	            orderItem.setThumbnail(product.getThumbnail());
	            orderItem.setQuantity(updQuantity);
	            orderItem.setShippedQuantity(0);
	            orderItem.setReturnedQuantity(0);
	            orderItem.setProduct(product);
	            orderItem.setOrder(order);
	            orderItem.setSpecifications(product.getSpecifications());
	            orderItemService.save(orderItem);
	            
	            OrderItemInfo orderItemInfo = new OrderItemInfo() ;
				orderItemInfo.setBeforeQuantity(0);
				orderItemInfo.setAfterQuantity(updQuantity);
				orderItemInfo.setBeforePrice(BigDecimal.ZERO);
				orderItemInfo.setAfterPrice(orderItem.getPrice());
				orderItemInfo.setBeforePriceB(BigDecimal.ZERO);
				orderItemInfo.setAfterPriceB(orderItem.getPriceB());
				orderItemInfo.setProduct(product);
				orderItemInfo.setOrderItemLog(orderItemLog);
				orderItemInfos.add(orderItemInfo);
	            
				isUpd = true ;
				allQuantity += updQuantity ;
			}else {
				OrderItem oldOrderItem=orderItemService.find(orderItem.getId());
				amount=amount.add(oldOrderItem.getPrice());
				amountToB=amountToB.add(oldOrderItem.getPriceB());
			}
		}
        //计算价格

		dataChange = reDate.compareTo(order.getReDate()) == 0 ? false : true ;

		if(isUpd || dataChange) {
			order.setItemAdminUpdate(order.getItemAdminUpdate() + 1);
		}

		if(isUpd){
			orderItemLog.setOrder(order);
			orderItemLog.setOperatorName(admin.getUsername());
			orderItemLog.setOperatorType(OrderItemLog.OperatorType.update);
			orderItemLog.setType(OrderItemLog.Type.admin);
			orderItemLog.setBeforeLog(currItemLog);
			orderItemLog.setOrderItemInfos(orderItemInfos);
			orderItemLog.setSupplierName(admin.getSupplier().getName());
			if (order.getToSupplier()==null) {
				orderItemLog.setLogType(LogType.distributor);
			}else {
				orderItemLog.setLogType(LogType.supplier);
			}
			orderItemLogDao.persist(orderItemLog);
		}

		order.setAmount(amount);
		order.setPrice(amount);
		order.setAmountToB(amountToB);
		order.setReDate(reDate);
		order.setQuantity(allQuantity);

		OrderLog orderLog = new OrderLog();
		orderLog.setType(OrderLog.Type.updateItems);
		orderLog.setOrder(order);
		orderLog.setOperator(admin);
		if (order.getToSupplier()==null) {
			orderLog.setLogType(LogType.distributor);
		}else {
			orderLog.setLogType(LogType.supplier);
		}
		orderLogDao.persist(orderLog);
		
		//后台消息推送
		Supplier supplier = order.getSupplier();
		if(order.getType().equals(Order.Type.billDistribution)) {
			orderNewsPushDao.addOrderNewPush(supplier, order, OrderNewsPush.OrderStatus.modify, order.getNeed(), supplier.getName(), "",OrderNewsPush.NoticeObject.order);
			orderNewsPushDao.addOrderNewPush(order.getToSupplier(), order, OrderNewsPush.OrderStatus.modify, order.getNeed(), supplier.getName(), "",OrderNewsPush.NoticeObject.purchase);
			orderNewsPushDao.addOrderNewPush(order.getToSupplier(), order, OrderNewsPush.OrderStatus.modify, order.getNeed(), supplier.getName(), "",OrderNewsPush.NoticeObject.order);
		}else if(order.getType() == Order.Type.formal) {
			orderNewsPushDao.addOrderNewPush(supplier, order, OrderNewsPush.OrderStatus.modify, order.getNeed(), supplier.getName(), "",OrderNewsPush.NoticeObject.order);
			orderNewsPushDao.addOrderNewPush(order.getToSupplier(), order, OrderNewsPush.OrderStatus.modify, order.getNeed(), supplier.getName(), "",OrderNewsPush.NoticeObject.purchase);
		}else {
			orderNewsPushDao.addOrderNewPush(supplier, order, OrderNewsPush.OrderStatus.modify, order.getNeed(), supplier.getName(), "",OrderNewsPush.NoticeObject.order);
		}

		if(isUpd || dataChange){
			String remark = "您的订单数据有变更，请查看订单详情！" ;
			/*weChatService.sendTemplateMessage(order , commonTemplateId , weChatService.getGlobalToken() , Order.OrderStatus.updateItems , remark) ;

			//采购单消息接受员接受消息
			weChatService.sendTemplateMessageToNoticeUserPurchase(order , Order.OrderStatus.updateItems , commonTemplateId , weChatService.getGlobalToken() , NoticeTypePurchase.Type.order_update , null) ;

			//订货单消息接受员接受消息
			weChatService.sendTemplateMessageToNoticeUser(order.getSupplier() , order , Order.OrderStatus.updateItems , commonTemplateId ,  weChatService.getGlobalToken()) ;*/

			//发送模版消息
			weChatService.sendTemplateMessageByOrderStatus(order , Order.OrderStatus.admin_updateItems ,  weChatService.getGlobalToken() , null , commonTemplateId , null , remark);




		}


	}
	



	/**
	 * 发货作废
	 *
	 * @param order
	 * @param shipping
	 * @param admin
	 */
	@Override
	public void cancelShipped(Order order, Shipping shipping, Admin admin) {
		List<ShippingItem> shippingItems = shipping.getShippingItems() ;

		for (ShippingItem shippingItem : shipping.getShippingItems()) {
			OrderItem orderItem = order.getOrderItem(shippingItem.getSn());
			if (orderItem == null) {
				throw new IllegalArgumentException();
			}
			orderItem.setShippedQuantity(orderItem.getShippedQuantity() - shippingItem.getQuantity());
			Product product = shippingItem.getProduct();
			if (product != null) {
				//库存回滚
				productService.addStock(product, shippingItem.getQuantity(), StockLog.Type.stockIn, admin, null);
				if (BooleanUtils.isTrue(order.getIsAllocatedStock())) {
					//释放商品库存
					productService.addAllocatedStock(product, shippingItem.getQuantity());
				}
			}
		}
		//修改订单发货数量
		order.setShippedQuantity(order.getShippedQuantity() - shipping.getQuantity());

		shippingDao.remove(shipping);

		shippingDao.flush();

		Set<Shipping> shippings = order.getShippings() ;
		if(CollectionUtils.isEmpty(shippings)){
			order.setStatus(Order.Status.pendingShipment);
		}else{
			order.setStatus(Order.Status.inShipment);
		}

		OrderLog orderLog = new OrderLog();
		orderLog.setType(OrderLog.Type.cancelShipped);
		orderLog.setOrder(order);
		orderLog.setOperator(admin);
		if (order.getToSupplier() == null) {
			orderLog.setLogType(LogType.distributor);
		}else {
			orderLog.setLogType(LogType.supplier);
		}
		orderLogDao.persist(orderLog);

		String remark = "订单发货取消！";
		weChatService.sendTemplateMessage(order, commonTemplateId, weChatService.getGlobalToken(), Order.OrderStatus.cancelShipped, remark);

	}

    /**
     * @param order
     * @param orderItemUpdateForm
     * @param operatorName        操作人名称
     * @param operatorType        操作类型
     * @param type                操作来源
	 * @param supplierName 		  企业名称
     */
    @Override
    public void updateItems(Order order, OrderItemUpdateForm orderItemUpdateForm, String operatorName, OrderItemLog.OperatorType operatorType, OrderItemLog.Type type , String supplierName) {

        boolean isUpd = false ;

        List<OrderItem> orderItems = orderItemUpdateForm.getOrderItems() ;

        Integer allQuantity =  0;

        List<OrderItemInfo> orderItemInfos = new ArrayList<>();

        //获取最近一条日志信息
		OrderItemLog currItemLog = CollectionUtils.isEmpty(order.getOrderItemLogs()) ? null : order.getOrderItemLogs().get(0);

        OrderItemLog orderItemLog = new OrderItemLog() ;

        BigDecimal amount = BigDecimal.ZERO;
        BigDecimal amountToB = BigDecimal.ZERO;
        
        for (OrderItem orderItem : orderItems) {
			if (orderItem.getStatus()==OrderItem.Status.edit) {
				OrderItem oldOrderItem=orderItemService.find(orderItem.getId());
				Integer updQuantity = orderItem.getCheckQuantity();
				Integer beforeQuantity = oldOrderItem.getQuantity() ;
				BigDecimal afterPrice=oldOrderItem.getPriceUnit().multiply(new BigDecimal(updQuantity));
				BigDecimal afterPriceB=BigDecimal.ZERO;
				if (!order.getType().equals(Order.Type.formal)) {
					if (oldOrderItem.getType().equals(Goods.Type.distribution)) {
						afterPriceB=oldOrderItem.getPriceUnitB().multiply(new BigDecimal(updQuantity));
					}
				}else {
					afterPriceB=afterPrice;
				}
				if(updQuantity.compareTo(beforeQuantity) != 0){
					//如果数量有变动的话， 则进行商品库存分配修改
					productService.addAllocatedStock(oldOrderItem.getProduct() , (updQuantity - beforeQuantity));
					
					OrderItemInfo orderItemInfo = new OrderItemInfo() ;
					orderItemInfo.setBeforeQuantity(oldOrderItem.getQuantity());
					orderItemInfo.setAfterQuantity(updQuantity);
					orderItemInfo.setBeforePrice((oldOrderItem.getPrice()));
					orderItemInfo.setAfterPrice(afterPrice);
					orderItemInfo.setBeforePriceB(oldOrderItem.getPriceB());
					orderItemInfo.setAfterPriceB(afterPriceB);
					orderItemInfo.setProduct(oldOrderItem.getProduct());
					orderItemInfo.setOrderItemLog(orderItemLog);
					orderItemInfos.add(orderItemInfo);
					
					isUpd = true ;
				}
				oldOrderItem.setPrice(afterPrice);
				oldOrderItem.setPriceB(afterPriceB);
				oldOrderItem.setQuantity(updQuantity);
				amount=amount.add(afterPrice);
				amountToB=amountToB.add(afterPriceB);
				allQuantity += updQuantity ;
			}else if (orderItem.getStatus()==OrderItem.Status.delete) {
				OrderItem oldOrderItem=orderItemService.find(orderItem.getId());
				Integer updQuantity = 0;
				Integer beforeQuantity = oldOrderItem.getQuantity() ;
				//进行商品库存分配修改
				productService.addAllocatedStock(oldOrderItem.getProduct() , (updQuantity - beforeQuantity));

				OrderItemInfo orderItemInfo = new OrderItemInfo() ;
				orderItemInfo.setBeforeQuantity(oldOrderItem.getQuantity());
				orderItemInfo.setAfterQuantity(updQuantity);
				orderItemInfo.setBeforePrice((oldOrderItem.getPrice()));
				orderItemInfo.setAfterPrice(BigDecimal.ZERO);
				orderItemInfo.setBeforePriceB(oldOrderItem.getPriceB());
				orderItemInfo.setAfterPriceB(BigDecimal.ZERO);
				orderItemInfo.setProduct(oldOrderItem.getProduct());
				orderItemInfo.setOrderItemLog(orderItemLog);
				orderItemInfos.add(orderItemInfo);
				
				isUpd = true ;
				orderItemService.delete(oldOrderItem);
			}else if (orderItem.getStatus()==OrderItem.Status.add) {
				Product product=productService.find(orderItem.getProductId());
				Integer updQuantity = orderItem.getCheckQuantity();
				Integer beforeQuantity = 0;
				//进行商品库存分配修改
				productService.addAllocatedStock(product , (updQuantity - beforeQuantity));
				orderItem.setSn(product.getSn());
	            orderItem.setName(product.getName());
	            orderItem.setType(product.getType());
	            
	            if (!order.getType().equals(Order.Type.formal)) {
	            	//查找客户供应关系里面的价格
	            	if (order.getSupplyNeed() != null) {
	            		NeedProduct needProduct=needProductDao.findByNeedSupplier(order.getSupplyNeed(), product);
	            		orderItem.setPriceUnit(needProduct.getSupplyPrice());
	            		orderItem.setPrice(needProduct.getSupplyPrice().multiply(new BigDecimal(updQuantity)));
	            	}else {
	            		BigDecimal priceUnit=product.getTurnoverSupplyPrice();
	            		if (priceUnit == null) {
	            			priceUnit=BigDecimal.ZERO;
	            		}
	            		orderItem.setPriceUnit(priceUnit);
	            		orderItem.setPrice(priceUnit.multiply(new BigDecimal(updQuantity)));
	            	}
	            	amount= amount.add(orderItem.getPrice());
	            	//如果为分销商品的时候就计算企业与企业之间的商品价格
	            	if (product.getSource() != null) {
	            		SupplierSupplier supplierSupplier=product.getGoods().getSupplierSupplier();
	            		SupplierProduct supplierProduct=supplierProductDao.getSupplierProduct(supplierSupplier, product.getSource());
	            		orderItem.setPriceUnitB(supplierProduct.getSupplyPrice());
	            		orderItem.setPriceB(supplierProduct.getSupplyPrice().multiply(new BigDecimal(updQuantity)));
	            		amountToB=amountToB.add(orderItem.getPriceB());
	            	}
	            }else {
					SupplierSupplier supplierSupplier=order.getSupplierSupplier();
					SupplierProduct supplierProduct=supplierProductDao.getSupplierProduct(supplierSupplier, product);
					orderItem.setPriceUnit(supplierProduct.getSupplyPrice());
					orderItem.setPrice(supplierProduct.getSupplyPrice().multiply(new BigDecimal(updQuantity)));
					orderItem.setPriceUnitB(supplierProduct.getSupplyPrice());
					orderItem.setPriceB(supplierProduct.getSupplyPrice().multiply(new BigDecimal(updQuantity)));
					amount= amount.add(orderItem.getPrice());
					amountToB=amountToB.add(orderItem.getPriceB());
				}
	            
	            
	            orderItem.setWeight(product.getWeight());
	            orderItem.setIsDelivery(product.getIsDelivery());
	            orderItem.setThumbnail(product.getThumbnail());
	            orderItem.setQuantity(updQuantity);
	            orderItem.setShippedQuantity(0);
	            orderItem.setReturnedQuantity(0);
	            orderItem.setProduct(product);
	            orderItem.setOrder(order);
	            orderItem.setSpecifications(product.getSpecifications());
	            orderItemService.save(orderItem);
	            
	            OrderItemInfo orderItemInfo = new OrderItemInfo() ;
				orderItemInfo.setBeforeQuantity(0);
				orderItemInfo.setAfterQuantity(updQuantity);
				orderItemInfo.setBeforePrice(BigDecimal.ZERO);
				orderItemInfo.setAfterPrice(orderItem.getPrice());
				orderItemInfo.setBeforePriceB(BigDecimal.ZERO);
				orderItemInfo.setAfterPriceB(orderItem.getPriceB());
				orderItemInfo.setProduct(product);
				orderItemInfo.setOrderItemLog(orderItemLog);
				orderItemInfos.add(orderItemInfo);
	            
				isUpd = true ;
				allQuantity += updQuantity ;
			}else {
				OrderItem oldOrderItem=orderItemService.find(orderItem.getId());
				amount=amount.add(oldOrderItem.getPrice());
				amountToB=amountToB.add(oldOrderItem.getPriceB());
			}
		}

        if(isUpd){
            order.setItemCustomerUpdate(order.getItemCustomerUpdate() + 1);

            orderItemLog.setOrder(order);
            orderItemLog.setOperatorName(operatorName);
            orderItemLog.setOperatorType(operatorType);
            orderItemLog.setType(type);
            orderItemLog.setBeforeLog(currItemLog);
            orderItemLog.setOrderItemInfos(orderItemInfos);
			orderItemLog.setSupplierName(supplierName);
			if (type.equals(OrderItemLog.Type.admin)) {
				orderItemLog.setLogType(LogType.distributor);
			}else {
				orderItemLog.setLogType(LogType.member);
			}
            orderItemLogDao.persist(orderItemLog);

        }

        order.setAmount(amount);
        order.setPrice(amount);
        order.setAmountToB(amountToB);
        order.setQuantity(allQuantity);

		OrderLog orderLog = new OrderLog();
		orderLog.setType(OrderLog.Type.updateItems);
		orderLog.setOrder(order);
		orderLog.setOperator(operatorName);
		orderLog.setSupplierName(supplierName);
		if (type.equals(OrderItemLog.Type.admin)) {
			orderLog.setLogType(LogType.distributor);
		}else {
			orderLog.setLogType(LogType.member);
		}
		orderLogDao.persist(orderLog);
		
		if (order.getType() != Order.Type.local) {
			if(order.getType() == Order.Type.billDistribution) {
				orderNewsPushDao.addOrderNewPush(order.getSupplier(), order, OrderNewsPush.OrderStatus.modify, order.getNeed(), order.getToSupplier().getName(), order.getNeed().getName(), OrderNewsPush.NoticeObject.order);
				orderNewsPushDao.addOrderNewPush(order.getToSupplier(), order, OrderNewsPush.OrderStatus.modify, order.getNeed(), order.getToSupplier().getName(), order.getNeed().getName(), OrderNewsPush.NoticeObject.purchase);
			}else if(order.getType() == Order.Type.formal) {
				orderNewsPushDao.addOrderNewPush(order.getSupplier(), order, OrderNewsPush.OrderStatus.modify, order.getNeed(), order.getToSupplier().getName(), order.getNeed().getName(), OrderNewsPush.NoticeObject.order);
				orderNewsPushDao.addOrderNewPush(order.getToSupplier(), order, OrderNewsPush.OrderStatus.modify, order.getNeed(), order.getToSupplier().getName(), order.getNeed().getName(), OrderNewsPush.NoticeObject.purchase);
			}else {
				orderNewsPushDao.addOrderNewPush(order.getSupplier(), order, OrderNewsPush.OrderStatus.modify, order.getNeed(),order.getSupplier().getName(), order.getNeed().getName(), OrderNewsPush.NoticeObject.order);
			}
			if(isUpd){
				String remark = "您的订单数据有变更，请查看订单详情！" ;
				/*weChatService.sendTemplateMessage(order , commonTemplateId , weChatService.getGlobalToken() , Order.OrderStatus.updateItems , remark) ;

			//采购单消息接受员接受消息
			weChatService.sendTemplateMessageToNoticeUserPurchase(order , Order.OrderStatus.updateItems , commonTemplateId , weChatService.getGlobalToken() , NoticeTypePurchase.Type.order_update , null) ;

			//订货单消息接受员接受消息
			weChatService.sendTemplateMessageToNoticeUser(order.getSupplier() , order , Order.OrderStatus.updateItems , commonTemplateId ,  weChatService.getGlobalToken()) ;*/
				
				//发送模版消息
				weChatService.sendTemplateMessageByOrderStatus(order , Order.OrderStatus.updateItems ,  weChatService.getGlobalToken() , null , commonTemplateId , null , remark);
				
			}
		}
    }

	/**
	 * 在供应中 的 正式供应 时间段内的下单
	 *
	 * @param startRow 开始行
	 * @param offset   偏移量
	 * @param compareDate
	 * @return
	 */
	@Override
	public List<Order> getOrderInSupply(int startRow, int offset , Date compareDate) {
		return orderDao.getOrderInSupply(startRow , offset , compareDate);
	}

	@Override
	public void sendNoOrderNotice(String noticeTemplateId) {
	//查出供应关系中用户下的单
		int startPage = 0;
		int startRow = 0;
		int offset = 100 ;

		while (true){
			startRow = startPage * offset ;
			final Date compareDate = new Date();

			List<Order> orders = this.getOrderInSupply(startRow , offset , compareDate);

			if (CollectionUtils.isEmpty(orders)) {
				return ;
			}
			try {

				TemplateInfo templateInfo = new TemplateInfo();
				templateInfo.setTemplateId(noticeTemplateId);
				for (Order order : orders) {
					Member member = order.getMember();
					Set<ChildMember> childMembers = member.getChildMembers();

					if (CollectionUtils.isEmpty(childMembers)) {
						continue;
					}

					final int noOrderDays = Days.daysBetween(LocalDate.fromDateFields(order.getCreateDate()), LocalDate.fromDateFields(compareDate)).getDays();

					templateInfo.setData(new HashMap<String, Map<String, String>>() {{

						this.put("first", new HashMap<String, String>() {{
							this.put("value", "您有" + noOrderDays + "天未订货了，请及时盘点商品库存进行订货，以免造成运营麻烦！");
						}});

						this.put("keyword1", new HashMap<String, String>() {{
							this.put("value", "盘点下单提醒");
						}});

						this.put("keyword2", new HashMap<String, String>() {{
							this.put("value", com.microBusiness.manage.util.DateUtils.formatDateToString(compareDate, DateformatEnum.yyyy年MM月dd日) + " 9:00");
						}});

						this.put("remark", new HashMap<String, String>() {{
							this.put("value", "微商小管理温馨提示！");
						}});

					}});


					for (ChildMember childMember : childMembers) {
						templateInfo.setToUser(childMember.getOpenId());
						weChatService.sendTemplateMessage(templateInfo, weChatService.getGlobalToken());
					}

				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			if(orders.size() < offset){
				break;
			}

			startRow ++;
		}
	}

	@Override
	public int countNumByOrder(Need need , Date startDate , Date endDate , Supplier supplier) {
		return orderDao.countNumByOrder(need , startDate , endDate , supplier);
	}

	@Override
	public List<Order> findListByIds(Long[] ids) {
		return orderDao.findListByIds(ids);
	}

	@Override
	public ExcelView exportSelectedOrder(Long[] ids) {
		String filename = "订单列表" + DateFormatUtils.format(new Date(), "yyyy-MM-dd") + "-采购单.xls";
		String[] titles = new String[]{"订单编号" , "下单时间" , "订单状态" , "商品编号" , "商品名称" , "商品规格" , "基本单位" , "订货数量", "发货数量", "实收数量" , "商品金额" , "供应商" , "收货点" , "收货人", "手机号", "收货地址" , "收货时间"};

		List<Order> datas = null;
		if(ids == null) {
			datas = null;
		}else {
			datas = orderDao.findListByIds(ids);
		}
		return this.downPurchaseOrder(filename , datas , titles) ;
	}
	
	/**
	 * 采购商导出
	 */
	@Override
	public ExcelView downPurchaseOrder(String fileName, List<Order> orders,
			String[] titles) {
		return new ExcelView(fileName, null, null, titles , null, null, orders, null){
			@Override
			public void buildExcelDocument(Map<String, Object> model, HSSFWorkbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
				//创建表头
				HSSFSheet sheet;
				if (StringUtils.isNotEmpty(this.getSheetName())) {
					sheet = workbook.createSheet(this.getSheetName());
				} else {
					sheet = workbook.createSheet();
				}

				int rowNumber = 0;
				String[] titles = this.getTitles() ;

				HSSFCellStyle cellStyle = workbook.createCellStyle();
				//填充色
				cellStyle.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
				cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

				cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
				cellStyle.setBorderTop(HSSFBorderFormatting.BORDER_THIN);
				cellStyle.setBorderBottom(HSSFBorderFormatting.BORDER_THIN);
				cellStyle.setBorderLeft(HSSFBorderFormatting.BORDER_THIN);
				cellStyle.setBorderRight(HSSFBorderFormatting.BORDER_THIN);

				HSSFFont font = workbook.createFont();

				font.setFontHeightInPoints((short) 11);
				font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

				cellStyle.setFont(font);

				HSSFCellStyle contentStyle = workbook.createCellStyle();
				contentStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);

				//处理表头
				if (titles != null && titles.length > 0) {
					HSSFRow header = sheet.createRow(rowNumber);
					header.setHeight((short) 400);
					for (int i = 0 , len = titles.length; i < len; i++) {
						HSSFCell cell = header.createCell(i);
						cell.setCellStyle(cellStyle);
						cell.setCellValue(titles[i]);

					}
					rowNumber++;
				}

				//处理内容
				List<Order> datas = (List<Order>)this.getData() ;

				if(CollectionUtils.isNotEmpty(datas)){
					for(Order order : datas){
						//获取商品数量
						List<OrderItem> orderItems = order.getOrderItems() ;

						Need need = order.getNeed();

						for(int i=0 ,len = orderItems.size() ; i < len ; i++){

							//创建行
							HSSFRow row = sheet.createRow(rowNumber);
							OrderItem orderItem = orderItems.get(i) ;

							Product product = orderItem.getProduct() ;
							Goods goods = product.getGoods() ;

							//sn
							row.createCell(0).setCellValue(order.getSn());
							//sheet.autoSizeColumn(0);

							row.createCell(1).setCellValue(com.microBusiness.manage.util.DateUtils.formatDateToString(order.getCreateDate(), DateformatEnum.yyyyMMddHHmmss2));
							//sheet.autoSizeColumn(1);

							row.createCell(2).setCellValue(SpringUtils.getMessage("Order.Status." + order.getStatus()));

							row.createCell(3).setCellValue(product.getSn());
							//sheet.autoSizeColumn(3);

							row.createCell(4).setCellValue(orderItem.getName());
							//sheet.autoSizeColumn(4);

							row.createCell(5).setCellValue(StringUtils.join(orderItem.getSpecifications(), ","));
							//sheet.autoSizeColumn(5);

							String basicUnit = SpringUtils.getMessage("Goods.unit." + goods.getUnit());
							if (goods.getUnit() == null) {
								basicUnit = "";
							}
							row.createCell(6).setCellValue(basicUnit);
							//sheet.autoSizeColumn(6);

							row.createCell(7).setCellValue(orderItem.getQuantity());
							//sheet.autoSizeColumn(7);

							row.createCell(8).setCellValue(orderItem.getShippedQuantity());
							
							row.createCell(9).setCellValue(order.getRealProductQuantity(product.getId()));
							
							row.createCell(10).setCellValue(orderItem.getPrice().doubleValue());
							//sheet.autoSizeColumn(8);
							
							row.createCell(11).setCellValue(order.getSupplier().getName());

							row.createCell(12).setCellValue(need.getName());
							//sheet.autoSizeColumn(9);

							row.createCell(13).setCellValue(order.getConsignee());
							//sheet.autoSizeColumn(10);

							row.createCell(14).setCellValue(order.getPhone());
							//sheet.autoSizeColumn(11);

							row.createCell(15).setCellValue(order.getAreaName() + " " + order.getAddress());
							//sheet.autoSizeColumn(12);

							row.createCell(16).setCellValue(com.microBusiness.manage.util.DateUtils.formatDateToString(order.getReDate(), DateformatEnum.yyyyMMdd2));
							//sheet.autoSizeColumn(13);

							rowNumber++;
						}
					}
				}

				response.setContentType("application/force-download");
				if (StringUtils.isNotEmpty(this.getFileName())) {
					/**
					 * 原来文件名的处理方式，火狐浏览器导出文件名出现乱码
					 * URLEncoder.encode(fileName, "UTF-8")
					 */
					String agent = request.getHeader("USER-AGENT").toLowerCase();
					if(agent.contains("firefox")) {
						response.setHeader("Content-disposition", "attachment; filename=" + new String(this.getFileName().getBytes(), "ISO8859-1"));
					}else{
						response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(this.getFileName(), "UTF-8"));
					}
					
				} else {
					response.setHeader("Content-disposition", "attachment");
				}

			}
		};
	}

	@Override
	public ExcelView exportSelectedOrderForm(Long[] ids , Supplier supplier) {
		String filename = "订单列表" + DateFormatUtils.format(new Date(), "yyyy-MM-dd") + "-订货单.xls";
		String[] titles = null;
		if(supplier == null) {
			titles = new String[]{"订单编号" , "下单时间" , "订单状态" , "供应商" , "客户名称" , "商品编号" , "商品名称" , "商品规格" , "基本单位" , "订货数量", "发货数量", "实收数量" , "商品金额" , "收货点" , "收货人", "手机号", "收货地址" , "收货时间"};
		}else {
			titles = new String[]{"订单编号" , "下单时间" , "订单状态" , "商品编号" , "商品名称" , "商品规格" , "基本单位" , "订货数量", "发货数量", "实收数量" , "商品金额" , "客户名称" , "收货点" , "收货人", "手机号", "收货地址" , "收货时间"};
		}

		List<Order> datas = null;
		if(ids == null) {
			datas = null;
		}else {
			datas = orderDao.findListByIds(ids);
		}
		ExcelView excelView = null;
		if(supplier == null) {
			excelView = this.downOrder(filename , datas , titles , supplier);
		}else {
			excelView = this.downOrder(filename , datas , titles);
		}
		
		return excelView ;
	}

	@Override
	public ExcelView downOrder(String fileName, List<Order> orders,
			String[] titles, Supplier supplier) {
		return new ExcelView(fileName, null, null, titles , null, null, orders, null){
			@Override
			public void buildExcelDocument(Map<String, Object> model, HSSFWorkbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
				//创建表头
				HSSFSheet sheet;
				if (StringUtils.isNotEmpty(this.getSheetName())) {
					sheet = workbook.createSheet(this.getSheetName());
				} else {
					sheet = workbook.createSheet();
				}

				int rowNumber = 0;
				String[] titles = this.getTitles() ;

				HSSFCellStyle cellStyle = workbook.createCellStyle();
				//填充色
				cellStyle.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
				cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

				cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
				cellStyle.setBorderTop(HSSFBorderFormatting.BORDER_THIN);
				cellStyle.setBorderBottom(HSSFBorderFormatting.BORDER_THIN);
				cellStyle.setBorderLeft(HSSFBorderFormatting.BORDER_THIN);
				cellStyle.setBorderRight(HSSFBorderFormatting.BORDER_THIN);

				HSSFFont font = workbook.createFont();

				font.setFontHeightInPoints((short) 11);
				font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

				cellStyle.setFont(font);

				HSSFCellStyle contentStyle = workbook.createCellStyle();
				contentStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);

				//处理表头
				if (titles != null && titles.length > 0) {
					HSSFRow header = sheet.createRow(rowNumber);
					header.setHeight((short) 400);
					for (int i = 0 , len = titles.length; i < len; i++) {
						HSSFCell cell = header.createCell(i);
						cell.setCellStyle(cellStyle);
						cell.setCellValue(titles[i]);

					}
					rowNumber++;
				}

				//处理内容
				List<Order> datas = (List<Order>)this.getData() ;

				if(CollectionUtils.isNotEmpty(datas)){
					for(Order order : datas){
						//获取商品数量
						List<OrderItem> orderItems = order.getOrderItems() ;
						
						Need need = order.getNeed();

						for(int i=0 ,len = orderItems.size() ; i < len ; i++){

							//创建行
							HSSFRow row = sheet.createRow(rowNumber);
							OrderItem orderItem = orderItems.get(i) ;

							Product product = orderItem.getProduct() ;
							Goods goods = product.getGoods() ;

							//sn
							row.createCell(0).setCellValue(order.getSn());
							//sheet.autoSizeColumn(0);

							row.createCell(1).setCellValue(com.microBusiness.manage.util.DateUtils.formatDateToString(order.getCreateDate(), DateformatEnum.yyyyMMddHHmmss2));
							//sheet.autoSizeColumn(1);

							row.createCell(2).setCellValue(SpringUtils.getMessage("Order.Status." + order.getStatus()));
							
							row.createCell(3).setCellValue(order.getSupplier().getName());

							//row.createCell(4).setCellValue(order.getMember().getNeed().getSupplier().getName());
							row.createCell(4).setCellValue(need.getSupplier().getName());
							
							row.createCell(5).setCellValue(product.getSn());
							//sheet.autoSizeColumn(3);

							row.createCell(6).setCellValue(orderItem.getName());
							//sheet.autoSizeColumn(4);

							row.createCell(7).setCellValue(StringUtils.join(orderItem.getSpecifications(), ","));
							//sheet.autoSizeColumn(5);

							String basicUnit = SpringUtils.getMessage("Goods.unit." + goods.getUnit());
							if (goods.getUnit() == null) {
								basicUnit = "";
							}
							row.createCell(8).setCellValue(basicUnit);
							//sheet.autoSizeColumn(6);

							row.createCell(9).setCellValue(orderItem.getQuantity());
							//sheet.autoSizeColumn(7);

							row.createCell(10).setCellValue(orderItem.getShippedQuantity());
							
							row.createCell(11).setCellValue(order.getRealProductQuantity(product.getId()));
							
							row.createCell(12).setCellValue(orderItem.getPrice().doubleValue());
							//sheet.autoSizeColumn(8);


							row.createCell(13).setCellValue(need.getName());
							//sheet.autoSizeColumn(9);

							row.createCell(14).setCellValue(order.getConsignee());
							//sheet.autoSizeColumn(10);

							row.createCell(15).setCellValue(order.getPhone());
							//sheet.autoSizeColumn(11);

							row.createCell(16).setCellValue(order.getAreaName() + " " + order.getAddress());
							//sheet.autoSizeColumn(12);

							row.createCell(17).setCellValue(com.microBusiness.manage.util.DateUtils.formatDateToString(order.getReDate(), DateformatEnum.yyyyMMdd2));
							//sheet.autoSizeColumn(13);

							rowNumber++;
						}
					}
				}

				response.setContentType("application/force-download");
				if (StringUtils.isNotEmpty(this.getFileName())) {
					/**
					 * 原来文件名的处理方式，火狐浏览器导出文件名出现乱码
					 * URLEncoder.encode(fileName, "UTF-8")
					 */
					String agent = request.getHeader("USER-AGENT").toLowerCase();
					if(agent.contains("firefox")) {
						response.setHeader("Content-disposition", "attachment; filename=" + new String(this.getFileName().getBytes(), "ISO8859-1"));
					}else{
						response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(this.getFileName(), "UTF-8"));
					}
					
				} else {
					response.setHeader("Content-disposition", "attachment");
				}

			}
		};
	}

	@Override
	public void reportDownload(Type type, Status status,
			String memberUsername, Goods goods, Boolean isPendingReceive,
			Boolean isPendingRefunds, Boolean isUseCouponCode,
			Boolean isExchangePoint, Boolean isAllocatedStock,
			Boolean hasExpired, Pageable pageable, Supplier supplier,
			Date startDate, Date endDate, String searchName, String timeSearch,
			HttpServletRequest request , HttpServletResponse response) {
		
		String filename = supplier.getName() + DateFormatUtils.format(new Date(), "yyyy-MM-dd") + "-订货食药监报告.xls";
		String[] productInfo = new String[]{"产品名称" , "产品代码" , "产品分类" , "产品规格" , "产品条码" , "保质期天数" , "生产厂商名称" , "产品描述"};
		String[] buyers = new String[]{"购货者名称" , "地址" , "联系电话" , "邮箱" , "法人名称"};
		String[] sales = new String[]{"产品名称" , "产品代码" , "销货数量" , "数量单位" , "销货日期" , "购货者名称" , "购货者地址" , "购货者联系方式" , "摊位号" , "经营者名称","生产厂商","生产日期","生产批次" , "产地证明编号" , "检验检疫证书编号", "质量安全检测", "产地"};
		
		//查询产品信息
		List<Product> products = orderDao.findByProduct(status, supplier, startDate, endDate, searchName, timeSearch);
		//查询购货者信息(采购商信息)
		List<Supplier> suppliers = orderDao.findBySupplier(status, supplier, startDate, endDate, searchName, timeSearch);
		//查询销货
		List<GoodNeedDto> goodNeedDtos = orderDao.findByGoodsAndNeed(status, supplier, startDate, endDate, searchName, timeSearch);
		this.exportExcel(productInfo, buyers, sales, products, suppliers, goodNeedDtos, filename, request, response);
		
	}
	
	public void exportExcel(String[] productInfo , String[] buyers , String[] sales , List<Product> products , List<Supplier> suppliers , List<GoodNeedDto> goodNeedDtos , String filename , HttpServletRequest request , HttpServletResponse response) {
		HSSFWorkbook workbook = new HSSFWorkbook();
		
		int rowNumber1 = 0;
		int rowNumber2 = 0;
		int rowNumber3 = 0;
		HSSFCellStyle cellStyle = workbook.createCellStyle();
		//填充色
		cellStyle.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
		cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

		cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		cellStyle.setBorderTop(HSSFBorderFormatting.BORDER_THIN);
		cellStyle.setBorderBottom(HSSFBorderFormatting.BORDER_THIN);
		cellStyle.setBorderLeft(HSSFBorderFormatting.BORDER_THIN);
		cellStyle.setBorderRight(HSSFBorderFormatting.BORDER_THIN);

		HSSFFont font = workbook.createFont();

		font.setFontHeightInPoints((short) 11);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

		cellStyle.setFont(font);

		HSSFCellStyle contentStyle = workbook.createCellStyle();
		contentStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		
		//处理表头
		HSSFSheet sheet = workbook.createSheet();
		workbook.setSheetName(0, "产品信息");
		sheet.setDefaultColumnWidth(15);
		sheet.createRow(0).setHeightInPoints(100);
		if (productInfo != null && productInfo.length > 0) {
			HSSFRow row1 = sheet.createRow(rowNumber1);
			row1.setHeight((short) 400);
			for (int i = 0 ; i < productInfo.length; i++) {
				HSSFCell cell = row1.createCell(i);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(productInfo[i]);
			}
			rowNumber1++;
		}
		if(CollectionUtils.isNotEmpty(products)){
			for(Product product : products) {
				Goods goods = product.getGoods();
				//创建行
				HSSFRow row = sheet.createRow(rowNumber1);
				//产品名称
				row.createCell(0).setCellValue(goods.getName());
				//产品编号
				row.createCell(1).setCellValue(goods.getSn());
				//产品分类
				row.createCell(2).setCellValue(goods.getProductCategory().getName());
				//产品规格
				row.createCell(3).setCellValue(StringUtils.join(product.getSpecifications(), ","));
				//产品条码
				row.createCell(4).setCellValue("");
				//保质期天数
				row.createCell(5).setCellValue("");
				//生产厂商名称
				row.createCell(6).setCellValue("");
				//产品描述
				row.createCell(7).setCellValue("");
				
				rowNumber1++;
			}
			
		}
		//第二个sheet
		//处理表头
		HSSFSheet sheet2 = workbook.createSheet();
		workbook.setSheetName(1, "购货者");
		sheet2.setDefaultColumnWidth(15);
		sheet2.createRow(0).setHeightInPoints(100);
		if (buyers != null && buyers.length > 0) {
			HSSFRow row2 = sheet2.createRow(rowNumber2);
			row2.setHeight((short) 400);
			for (int i = 0 , len = buyers.length; i < len; i++) {
				HSSFCell cell = row2.createCell(i);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(buyers[i]);
			}
			rowNumber2++;  //TODO	
		}
		if(CollectionUtils.isNotEmpty(suppliers)){
			for(Supplier supplier : suppliers){
				if (supplier != null) {
					//创建行
					HSSFRow row = sheet2.createRow(rowNumber2);
					//购货者名称
					row.createCell(0).setCellValue(supplier.getName());
					//地址
					Area area = supplier.getArea();
					row.createCell(1).setCellValue(null == area ? supplier.getAddress() : area.getFullName()+supplier.getAddress());
					//联系电话
					row.createCell(2).setCellValue(supplier.getTel());
					//邮箱
					row.createCell(3).setCellValue(supplier.getEmail());
					//法人名称
					row.createCell(4).setCellValue(supplier.getLegalPersonName());
					
					rowNumber2++;
				}
			}
		}
		//第三个sheet
		//处理表头
		HSSFSheet sheet3 = workbook.createSheet();
		workbook.setSheetName(2, "销货");
		sheet3.setDefaultColumnWidth(15);
		sheet3.createRow(0).setHeightInPoints(200);
		if (sales != null && sales.length > 0) {
			HSSFRow row3 = sheet3.createRow(rowNumber3);
			row3.setHeight((short) 400);
			for (int i = 0 , len = sales.length; i < len; i++) {
				HSSFCell cell = row3.createCell(i);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(sales[i]);
			}
			rowNumber3++;
		}		
		if(CollectionUtils.isNotEmpty(goodNeedDtos)){
			for(GoodNeedDto goodNeedDto : goodNeedDtos){
				Goods goods = goodNeedDto.getGoods();
				//创建行
				HSSFRow row = sheet3.createRow(rowNumber3);
				//产品名称
				row.createCell(0).setCellValue(goods.getName());
				//产品代码
				row.createCell(1).setCellValue(goods.getSn());
				//销货数量
				row.createCell(2).setCellValue(goodNeedDto.getQuantity());
				
				String basicUnit = SpringUtils.getMessage("Goods.unit." + goods.getUnit());
				if (goods.getUnit() == null) {
					basicUnit = "";
				}
				//数量单位
				row.createCell(3).setCellValue(basicUnit);
				//销货日期
				row.createCell(4).setCellValue(com.microBusiness.manage.util.DateUtils.formatDateToString(goodNeedDto.getCreateDate(), DateformatEnum.yyyyMMdd2));
				//购货者名称
				row.createCell(5).setCellValue(goodNeedDto.getNeedName());
				//购货者地址
				row.createCell(6).setCellValue(goodNeedDto.getFullName()+goodNeedDto.getAddress());
				//购货者联系方式
				row.createCell(7).setCellValue(goodNeedDto.getPhone());
				
				rowNumber3++;
			}
		}
		response.setContentType("application/force-download");
		if (StringUtils.isNotEmpty(filename)) {
			try {
				String agent = request.getHeader("USER-AGENT").toLowerCase();
				if(agent.contains("firefox")) {
					response.setHeader("Content-disposition", "attachment; filename=" + new String(filename.getBytes(), "ISO8859-1"));
				}else{
					response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(filename, "UTF-8"));
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		} else {
			response.setHeader("Content-disposition", "attachment");
		}
		
		try  
        {  
			OutputStream stream = response.getOutputStream();
			workbook.write(stream);
			stream.close();
        }  
        catch (Exception e)  
        {  
            e.printStackTrace();  
        }  
	
	}

	/**
	 * 采购单导出食药监报告
	 */
	@Override
	public void exportFoodMedicine(Type type, Status status,
			String memberUsername, Goods goods, Boolean isPendingReceive,
			Boolean isPendingRefunds, Boolean isUseCouponCode,
			Boolean isExchangePoint, Boolean isAllocatedStock,
			Boolean hasExpired, Pageable pageable, Supplier supplier,
			Date startDate, Date endDate, String searchName, String timeSearch,
			HttpServletRequest request, HttpServletResponse response) {
		
		String filename = supplier.getName() + DateFormatUtils.format(new Date(), "yyyy-MM-dd") + "-采购食药监报告.xls";
		String[] title1 = new String[]{"供货者名称" , "地址" , "联系电话" , "邮箱" , "法人姓名"};
		String[] title2 = new String[]{"产品名称" , "产品代码" , "采购数量" , "数量单位" , "进货日期" , "供货单位名称" , "供货单位地址" , "供货单位联系方式" , "摊位号" , "经营者名称","生产厂商","生产日期","生产批次" , "产地证明编号" , "检验检疫证书编号", "质量安全检测", "产地"};
		String[] title3 = new String[]{"产品名称" , "产品代码" , "产品分类" , "产品规格" , "产品条码" , "保质期天数" , "生产厂商名称" , "产品描述"};
		
		//查询供货者信息
		List<Supplier> suppliers = orderDao.findBySuppliers(status, supplier.getNeeds(), startDate, endDate, searchName, timeSearch);
		//查询进货
		List<GoodSupplierDto> goodSupplierDtos = orderDao.findByGoodsAndSupplier(status, supplier.getNeeds(), startDate, endDate, searchName, timeSearch);
		
		List<Product> products = orderDao.findBuyersByProduct(status, supplier.getNeeds(), startDate, endDate, searchName, timeSearch);
		
		this.exportExcels(title1, title2, title3, suppliers, goodSupplierDtos, products , filename, request, response);
		
	}
	
	public void exportExcels(String[] title1 , String[] title2 , String[] title3 , List<Supplier> suppliers , List<GoodSupplierDto> goodSupplierDtos , List<Product> products , String filename , HttpServletRequest request , HttpServletResponse response) {
		HSSFWorkbook workbook = new HSSFWorkbook();
		int rowNumber1 = 0;
		int rowNumber2 = 0;
		int rowNumber3 = 0;
		HSSFCellStyle cellStyle = workbook.createCellStyle();
		//填充色
		cellStyle.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
		cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

		cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		cellStyle.setBorderTop(HSSFBorderFormatting.BORDER_THIN);
		cellStyle.setBorderBottom(HSSFBorderFormatting.BORDER_THIN);
		cellStyle.setBorderLeft(HSSFBorderFormatting.BORDER_THIN);
		cellStyle.setBorderRight(HSSFBorderFormatting.BORDER_THIN);

		HSSFFont font = workbook.createFont();

		font.setFontHeightInPoints((short) 11);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

		cellStyle.setFont(font);

		HSSFCellStyle contentStyle = workbook.createCellStyle();
		contentStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		
		//第一个sheet
		HSSFSheet sheet1 = workbook.createSheet();
		workbook.setSheetName(0, "产品信息");
		sheet1.setDefaultColumnWidth(20);
		sheet1.createRow(0).setHeightInPoints(100);
		if (title3 != null && title3.length > 0) {
			HSSFRow row = sheet1.createRow(rowNumber3);
			row.setHeight((short) 400);
			for (int i = 0 ; i < title3.length; i++) {
				HSSFCell cell = row.createCell(i);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(title3[i]);
			}
			rowNumber3++;
		}
		if(CollectionUtils.isNotEmpty(products)){
			for(Product product : products){
				
				Goods goods = product.getGoods();
				//创建行
				HSSFRow row = sheet1.createRow(rowNumber3);
				//产品名称
				row.createCell(0).setCellValue(goods.getName());
				//产品编号
				row.createCell(1).setCellValue(goods.getSn());
				//产品分类
				row.createCell(2).setCellValue(goods.getProductCategory().getName());
				//产品规格
				row.createCell(3).setCellValue(StringUtils.join(product.getSpecifications(), ","));
				//产品条码
				row.createCell(4).setCellValue("");
				//保质期天数
				row.createCell(5).setCellValue("");
				//生产厂商名称
				row.createCell(6).setCellValue("");
				//产品描述
				row.createCell(7).setCellValue("");
				rowNumber3++;
			}
		}
		
		
		
		//第二个sheet
		HSSFSheet sheet2 = workbook.createSheet();
		workbook.setSheetName(1, "供货者");
		sheet2.setDefaultColumnWidth(20);
		sheet2.createRow(0).setHeightInPoints(100);
		if (title1 != null && title1.length > 0) {
			HSSFRow row1 = sheet2.createRow(rowNumber1);
			row1.setHeight((short) 400);
			for (int i = 0 ; i < title1.length; i++) {
				HSSFCell cell = row1.createCell(i);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(title1[i]);
			}
			rowNumber1++;
		}
		if(CollectionUtils.isNotEmpty(suppliers)){
			for(Supplier supplier : suppliers){
				//创建行
				HSSFRow row = sheet2.createRow(rowNumber1);
				//供货者名称
				row.createCell(0).setCellValue(supplier.getName());
				//地址
				Area area = supplier.getArea();
				row.createCell(1).setCellValue(null == area ? supplier.getAddress() : area.getFullName()+supplier.getAddress());
				//联系电话
				row.createCell(2).setCellValue(supplier.getTel());
				//邮箱
				row.createCell(3).setCellValue(supplier.getEmail());
				//法人名称
				row.createCell(4).setCellValue(supplier.getLegalPersonName());
				
				rowNumber1++;
			}
		}
		
		//第三个sheet
		HSSFSheet sheet3 = workbook.createSheet();
		workbook.setSheetName(2, "销货");
		sheet3.setDefaultColumnWidth(15);
		sheet3.createRow(0).setHeightInPoints(200);
		if (title2 != null && title2.length > 0) {
			HSSFRow row3 = sheet3.createRow(rowNumber2);
			row3.setHeight((short) 400);
			for (int i = 0; i < title2.length; i++) {
				HSSFCell cell = row3.createCell(i);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(title2[i]);
			}
			rowNumber2++;
		}		
		if(CollectionUtils.isNotEmpty(goodSupplierDtos)){
			for(GoodSupplierDto goodSupplierDto : goodSupplierDtos){
				Goods goods = goodSupplierDto.getGoods();
				Supplier supplier = goodSupplierDto.getSupplier();
				//创建行
				HSSFRow row = sheet3.createRow(rowNumber2);
				//产品名称
				row.createCell(0).setCellValue(goods.getName());
				//产品代码
				row.createCell(1).setCellValue(goods.getSn());
				//采购数量
				row.createCell(2).setCellValue(goodSupplierDto.getQuantity());
				
				String basicUnit = SpringUtils.getMessage("Goods.unit." + goods.getUnit());
				if (goods.getUnit() == null) {
					basicUnit = "";
				}
				//数量单位
				row.createCell(3).setCellValue(basicUnit);
				//进货日期
				row.createCell(4).setCellValue(com.microBusiness.manage.util.DateUtils.formatDateToString(goodSupplierDto.getCreateDate(), DateformatEnum.yyyyMMdd2));
				//供货单位名称
				row.createCell(5).setCellValue(goodSupplierDto.getSupplierName());
				//供货单位地址
				Area area = supplier.getArea();
				row.createCell(6).setCellValue(null == area ? supplier.getAddress() : area.getFullName()+" "+goodSupplierDto.getSupplier().getAddress());
				//供货单位联系方式
				row.createCell(7).setCellValue(supplier.getTel());
				
				rowNumber2++;
			}
		}
		response.setContentType("application/force-download");
		if (StringUtils.isNotEmpty(filename)) {
			try {
				String agent = request.getHeader("USER-AGENT").toLowerCase();
				if(agent.contains("firefox")) {
					response.setHeader("Content-disposition", "attachment; filename=" + new String(filename.getBytes(), "ISO8859-1"));
				}else{
					response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(filename, "UTF-8"));
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		} else {
			response.setHeader("Content-disposition", "attachment");
		}
		
		try  
        {  
			OutputStream stream = response.getOutputStream();
			workbook.write(stream);
			stream.close();
        }  
        catch (Exception e)  
        {  
            e.printStackTrace();  
        }  
	
	}

	@Override
	public void selectedReport(Long[] ids, Supplier supplier,
			HttpServletRequest request, HttpServletResponse response) {
		String filename = supplier.getName() + DateFormatUtils.format(new Date(), "yyyy-MM-dd") + "-订货食药监报告.xls";
		String[] title1 = new String[]{"产品名称" , "产品代码" , "产品分类" , "产品规格" , "产品条码" , "保质期天数" , "生产厂商名称" , "产品描述"};
		String[] title2 = new String[]{"购货者名称" , "地址" , "联系电话" , "邮箱" , "法人名称"};
		String[] title3 = new String[]{"产品名称" , "产品代码" , "销货数量" , "数量单位" , "销货日期" , "购货者名称" , "购货者地址" , "购货者联系方式" , "摊位号" , "经营者名称","生产厂商","生产日期","生产批次" , "产地证明编号" , "检验检疫证书编号", "质量安全检测", "产地"};
		
		//根据id查询产品信息
		List<Product> products = orderDao.findByids(ids);
		//根据id查询企业信息
		List<Supplier> suppliers = orderDao.queryBuyersByids(ids);
		//根据id查询销货信息
		List<GoodNeedDto> goodNeedDtos = orderDao.queryGoodsAndNeedByids(ids);
		
		this.exportExcel(title1, title2, title3, products, suppliers, goodNeedDtos, filename, request, response);
		
	}

	@Override
	public void selectedReports(Long[] ids, Supplier supplier,
			HttpServletRequest request, HttpServletResponse response) {
		String filename = supplier.getName() + DateFormatUtils.format(new Date(), "yyyy-MM-dd") + "-采购食药监报告.xls";
		
		String[] title1 = new String[]{"供货者名称" , "地址" , "联系电话" , "邮箱" , "法人姓名"};
		String[] title2 = new String[]{"产品名称" , "产品代码" , "采购数量" , "数量单位" , "进货日期" , "供货单位名称" , "供货单位地址" , "供货单位联系方式" , "摊位号" , "经营者名称","生产厂商","生产日期","生产批次" , "产地证明编号" , "检验检疫证书编号", "质量安全检测", "产地"};
		String[] title3 = new String[]{"产品名称" , "产品代码" , "产品分类" , "产品规格" , "产品条码" , "保质期天数" , "生产厂商名称" , "产品描述"};
		//根据订单id查询产品信息
		List<Product> products = orderDao.findByids(ids);
		//根据订单id查询供货者信息
		List<Supplier> suppliers = orderDao.querySupplierByids(ids);
		//根据订单id查询进货信息
		List<GoodSupplierDto> goodSupplierDtos = orderDao.queryGoodSupplierByids(ids);
		
		this.exportExcels(title1, title2, title3, suppliers, goodSupplierDtos, products , filename, request, response);
	}
	
	/**
	 * 订货单拆分导出
	 * @param filename
	 * @param map
	 * @param titles
	 * @param request
	 * @param response
	 */
	public void orderSplitExports(String filename, Map<String, List<Order>> map , String[] titles ,Supplier supplier, HttpServletRequest request , HttpServletResponse response) {
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFCellStyle cellStyle = workbook.createCellStyle();
		//填充色
		cellStyle.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
		cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

		cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		cellStyle.setBorderTop(HSSFBorderFormatting.BORDER_THIN);
		cellStyle.setBorderBottom(HSSFBorderFormatting.BORDER_THIN);
		cellStyle.setBorderLeft(HSSFBorderFormatting.BORDER_THIN);
		cellStyle.setBorderRight(HSSFBorderFormatting.BORDER_THIN);

		HSSFFont font = workbook.createFont();

		font.setFontHeightInPoints((short) 11);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

		cellStyle.setFont(font);

		HSSFCellStyle contentStyle = workbook.createCellStyle();
		contentStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		int sheetIx = 0;
		for(String key : map.keySet()) {
			int rowNumber = 0;
			HSSFSheet sheet = workbook.createSheet();
			workbook.setSheetName(sheetIx, key);
			sheet.setDefaultColumnWidth(10);
			sheet.createRow(0).setHeightInPoints(100);

			//标题
			HSSFRow titleRow = sheet.createRow(rowNumber);
			HSSFCellStyle titleStyle = workbook.createCellStyle();
			titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中  
			titleStyle.setFont(font);
			titleRow.setHeightInPoints(20);
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 16));
			HSSFCell cel = titleRow.createCell(0);
			cel.setCellValue(supplier.getName()+"--"+key);
			cel.setCellStyle(titleStyle);
			rowNumber++;
			if (titles != null && titles.length > 0) {
				HSSFRow row = sheet.createRow(rowNumber);
				row.setHeight((short) 400);
				for (int i = 0 ; i < titles.length; i++) {
					HSSFCell cell = row.createCell(i);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(titles[i]);
				}
				rowNumber++;
				sheetIx++;
			}
			if(CollectionUtils.isNotEmpty(map.get(key))){
				List<Order> orders = map.get(key);
				for(Order order : orders) {
					//获取商品数量
					List<OrderItem> orderItems = order.getOrderItems() ;

					Need need = order.getNeed();
					
					for(int i=0 ,len = orderItems.size() ; i < len ; i++){

						//创建行
						HSSFRow row = sheet.createRow(rowNumber);
						OrderItem orderItem = orderItems.get(i) ;
						Product product = orderItem.getProduct() ;
						Goods goods = product.getGoods() ;

						row.createCell(0).setCellValue(order.getSn());

						row.createCell(1).setCellValue(com.microBusiness.manage.util.DateUtils.formatDateToString(order.getCreateDate(), DateformatEnum.yyyyMMddHHmmss2));

						row.createCell(2).setCellValue(SpringUtils.getMessage("Order.Status." + order.getStatus()));

						row.createCell(3).setCellValue(product.getSn());

						row.createCell(4).setCellValue(orderItem.getName());

						row.createCell(5).setCellValue(StringUtils.join(orderItem.getSpecifications(), ","));

						String basicUnit = SpringUtils.getMessage("Goods.unit." + goods.getUnit());
						if (goods.getUnit() == null) {
							basicUnit = "";
						}
						row.createCell(6).setCellValue(basicUnit);

						row.createCell(7).setCellValue(orderItem.getQuantity());

						row.createCell(8).setCellValue(orderItem.getShippedQuantity());
						
						row.createCell(9).setCellValue(order.getRealProductQuantity(product.getId()));
						
						row.createCell(10).setCellValue(orderItem.getPrice().doubleValue());
						
						//row.createCell(11).setCellValue(order.getMember().getNeed().getSupplier().getName());
						row.createCell(11).setCellValue(need.getSupplier().getName());

						
						
						row.createCell(12).setCellValue(need.getName());

						row.createCell(13).setCellValue(order.getConsignee());

						row.createCell(14).setCellValue(order.getPhone());

						row.createCell(15).setCellValue(order.getAreaName() + " " + order.getAddress());

						row.createCell(16).setCellValue(com.microBusiness.manage.util.DateUtils.formatDateToString(order.getReDate(), DateformatEnum.yyyyMMdd2));

						rowNumber++;
					}
				}
			}
		}
		
		this.exportEndWork(filename, workbook, request, response);
	
	}

	/**
	 * 拆分导出订货单
	 */
	@Override
	public void splitOut(Type type, Status status, String memberUsername,
			Goods goods, Boolean isPendingReceive, Boolean isPendingRefunds,
			Boolean isUseCouponCode, Boolean isExchangePoint,
			Boolean isAllocatedStock, Boolean hasExpired, Pageable pageable,
			Supplier supplier, Date startDate, Date endDate, String searchName,
			String timeSearch, HttpServletRequest request,
			HttpServletResponse response) {
		String filename = "订单列表" + DateFormatUtils.format(new Date(), "yyyy-MM-dd") + "-订货单.xls";
		String[] titles = null;
		if(supplier == null) {
			titles = new String[]{"订单编号" , "下单时间" , "订单状态" , "供应商" , "商品编号" , "商品名称" , "商品规格" , "基本单位" , "订货数量","发货数量","实收数量","商品金额" , "客户名称" , "收货点" , "收货人", "手机号", "收货地址" , "收货时间"};
		}else {
			titles = new String[]{"订单编号" , "下单时间" , "订单状态" , "商品编号" , "商品名称" , "商品规格" , "基本单位" , "订货数量","发货数量","实收数量","商品金额" ,"客户名称" , "收货点" , "收货人", "手机号", "收货地址" , "收货时间"};
		}
		
		if(null == pageable){
			pageable = new Pageable(1, Integer.MAX_VALUE , true) ;
		}else {
			pageable.setPageNumber(1);
			pageable.setPageSize(Integer.MAX_VALUE , true);
		}

		// TODO: 2017/5/8 这里使用分页方法 ，后期需要修改
		List<Order> datas = null;
		Member member = memberService.findByUsername(memberUsername);
		if (StringUtils.isNotEmpty(memberUsername) && member == null) {
			datas = null;
		} else {
			datas = orderDao.findPage(type, status,null, member, null, isPendingReceive, 
					isPendingRefunds, null, null, isAllocatedStock, hasExpired, pageable, 
					supplier , startDate , endDate , searchName , timeSearch, null).getContent();
		}
		
		Map<String, List<Order>> map = new HashMap<String, List<Order>>();
		if(datas != null) {
			for(Order orders : datas) {
				List<Order> list = new ArrayList<Order>();
				//String orderName = orders.getMember().getNeed().getSupplier().getName();
				Supplier se = orders.getToSupplier() != null ? orders.getToSupplier() : orders.getSupplier();
				String orderName = se.getName();
				
				if(!map.containsKey(orderName)) {
					list.add(orders);
					map.put(orderName, list);
				}else {
					List<Order> getOrder = map.get(orderName);
					getOrder.add(orders);
					map.put(orderName, getOrder);
				}
			}
		}
		
		if(supplier == null) {
			this.orderSplitExport(filename, map, titles, supplier, request, response);
		}else {
			this.orderSplitExports(filename, map, titles,supplier, request, response);
		}
	}
	
	
	/**
	 * 超级管理员拆分导出
	 * @param filename
	 * @param map
	 * @param titles
	 * @param request
	 * @param response
	 */
	public void orderSplitExport(String filename, Map<String, List<Order>> map , String[] titles , Supplier supplier , HttpServletRequest request , HttpServletResponse response) {
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFCellStyle cellStyle = workbook.createCellStyle();
		//填充色
		cellStyle.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
		cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

		cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		cellStyle.setBorderTop(HSSFBorderFormatting.BORDER_THIN);
		cellStyle.setBorderBottom(HSSFBorderFormatting.BORDER_THIN);
		cellStyle.setBorderLeft(HSSFBorderFormatting.BORDER_THIN);
		cellStyle.setBorderRight(HSSFBorderFormatting.BORDER_THIN);

		HSSFFont font = workbook.createFont();

		font.setFontHeightInPoints((short) 11);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

		cellStyle.setFont(font);

		HSSFCellStyle contentStyle = workbook.createCellStyle();
		contentStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		int sheetIx = 0;
		for(String key : map.keySet()) {
			int rowNumber = 0;
			HSSFSheet sheet = workbook.createSheet();
			workbook.setSheetName(sheetIx, key);
			sheet.setDefaultColumnWidth(20);
			sheet.createRow(0).setHeightInPoints(100);
			if (titles != null && titles.length > 0) {
				HSSFRow row = sheet.createRow(rowNumber);
				row.setHeight((short) 400);
				for (int i = 0 ; i < titles.length; i++) {
					HSSFCell cell = row.createCell(i);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(titles[i]);
				}
				rowNumber++;
				sheetIx++;
			}
			if(CollectionUtils.isNotEmpty(map.get(key))){
				List<Order> orders = map.get(key);
				for(Order order : orders) {
					//获取商品数量
					List<OrderItem> orderItems = order.getOrderItems() ;

					Need need = order.getNeed();
					
					for(int i=0 ,len = orderItems.size() ; i < len ; i++){

						//创建行
						HSSFRow row = sheet.createRow(rowNumber);
						OrderItem orderItem = orderItems.get(i) ;
						Product product = orderItem.getProduct() ;
						Goods goods = product.getGoods() ;

						row.createCell(0).setCellValue(order.getSn());

						row.createCell(1).setCellValue(com.microBusiness.manage.util.DateUtils.formatDateToString(order.getCreateDate(), DateformatEnum.yyyyMMddHHmmss2));

						row.createCell(2).setCellValue(SpringUtils.getMessage("Order.Status." + order.getStatus()));
						
						row.createCell(3).setCellValue(order.getSupplier().getName());

						row.createCell(4).setCellValue(product.getSn());

						row.createCell(5).setCellValue(orderItem.getName());

						row.createCell(6).setCellValue(StringUtils.join(orderItem.getSpecifications(), ","));

						String basicUnit = SpringUtils.getMessage("Goods.unit." + goods.getUnit());
						if (goods.getUnit() == null) {
							basicUnit = "";
						}
						row.createCell(7).setCellValue(basicUnit);

						row.createCell(8).setCellValue(orderItem.getQuantity());

						row.createCell(9).setCellValue(orderItem.getShippedQuantity());
						
						row.createCell(10).setCellValue(order.getRealProductQuantity(product.getId()));
						
						row.createCell(11).setCellValue(orderItem.getPrice().doubleValue());
						
						//row.createCell(12).setCellValue(order.getMember().getNeed().getSupplier().getName());
						row.createCell(12).setCellValue(need.getSupplier().getName());
						
						row.createCell(13).setCellValue(need.getName());

						row.createCell(14).setCellValue(order.getConsignee());

						row.createCell(15).setCellValue(order.getPhone());

						row.createCell(16).setCellValue(order.getAreaName() + " " + order.getAddress());

						row.createCell(17).setCellValue(com.microBusiness.manage.util.DateUtils.formatDateToString(order.getReDate(), DateformatEnum.yyyyMMdd2));

						rowNumber++;
					}
				}
			}
		}
		this.exportEndWork(filename, workbook, request, response);
		
	
	}
	
	public void exportEndWork(String filename , HSSFWorkbook workbook , HttpServletRequest request , HttpServletResponse response) {
		response.setContentType("application/force-download");
		if (StringUtils.isNotEmpty(filename)) {
			try {
				String agent = request.getHeader("USER-AGENT").toLowerCase();
				if(agent.contains("firefox")) {
					response.setHeader("Content-disposition", "attachment; filename=" + new String(filename.getBytes(), "ISO8859-1"));
				}else{
					response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(filename, "UTF-8"));
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		} else {
			response.setHeader("Content-disposition", "attachment");
		}
		
		try  
        {  
			OutputStream stream = response.getOutputStream();
			workbook.write(stream);
			stream.close();
        }  
        catch (Exception e)  
        {  
            e.printStackTrace();  
        }  
	}

	@Override
	public void selectSplitExport(Long[] ids, Supplier supplier,
			HttpServletRequest request, HttpServletResponse response) {
		String filename = "订单列表" + DateFormatUtils.format(new Date(), "yyyy-MM-dd") + "-订货单.xls";
		String[] titles = null;
		if(supplier == null) {
			titles = new String[]{"订单编号" , "下单时间" , "订单状态" , "供应商" , "商品编号" , "商品名称" , "商品规格" , "基本单位" , "订货数量", "发货数量", "实收数量" , "商品金额" , "客户名称" , "收货点" , "收货人", "手机号", "收货地址" , "收货时间"};
		}else {
			titles = new String[]{"订单编号" , "下单时间" , "订单状态" , "商品编号" , "商品名称" , "商品规格" , "基本单位" , "订货数量", "发货数量", "实收数量" , "商品金额" , "客户名称" , "收货点" , "收货人", "手机号", "收货地址" , "收货时间"};
		}

		List<Order> datas = null;
		if(ids == null) {
			datas = null;
		}else {
			datas = orderDao.findListByIds(ids);
		}
		
		Map<String, List<Order>> map = new HashMap<String, List<Order>>();
		if(datas != null) {
			for(Order orders : datas) {
				List<Order> list = new ArrayList<Order>();
				//String orderName = orders.getMember().getNeed().getSupplier().getName();
				Supplier se = orders.getToSupplier() != null ? orders.getToSupplier() : orders.getSupplier();
				String orderName = se.getName();
				if(!map.containsKey(orderName)) {
					list.add(orders);
					map.put(orderName, list);
				}else {
					List<Order> getOrder = map.get(orderName);
					getOrder.add(orders);
					map.put(orderName, getOrder);
				}
			}
		}
		
		if(supplier == null) {
			this.orderSplitExport(filename, map, titles, supplier, request, response);
		}else {
			this.orderSplitExports(filename, map, titles,supplier, request, response);
		}
		
	}

	@Override
	public void batchSplitOut(Type type, Status status, String memberUsername,
			Goods goods, Boolean isPendingReceive, Boolean isPendingRefunds,
			Boolean isUseCouponCode, Boolean isExchangePoint,
			Boolean isAllocatedStock, Boolean hasExpired, Pageable pageable,
			Supplier supplier, Date startDate, Date endDate, String searchName,
			String timeSearch, HttpServletRequest request,
			HttpServletResponse response) {
		
		String filename = "订单列表" + DateFormatUtils.format(new Date(), "yyyy-MM-dd") + "-采购单.xls";
		String[] titles = new String[]{"订单编号" , "下单时间" , "订单状态" , "商品编号" , "商品名称" , "商品规格" , "基本单位" , "订货数量", "发货数量" , "实收数量" , "商品金额" , "供应商" , "收货点" , "收货人", "手机号", "收货地址" , "收货时间"};
		if(null == pageable){
			pageable = new Pageable(1, Integer.MAX_VALUE , true) ;
		}else {
			pageable.setPageNumber(1);
			pageable.setPageSize(Integer.MAX_VALUE , true);
		}

		List<Order> datas = null;
		Member member = memberService.findByUsername(memberUsername);
		if (null == supplier || CollectionUtils.isEmpty(supplier.getNeeds()) || (StringUtils.isNotEmpty(memberUsername) && member == null)) {
			datas = null;
		} else {
			datas = orderDao.findPage(type, status,null, member, null, isPendingReceive, isPendingRefunds, null, null, isAllocatedStock, hasExpired, pageable , null , startDate , endDate , searchName , timeSearch,supplier).getContent();
		}
		Map<String, List<Order>> map = new HashMap<String, List<Order>>();
		if(datas != null) {
			for(Order orders : datas) {
				List<Order> list = new ArrayList<Order>();
				String orderName = orders.getSupplier().getName();
				if(!map.containsKey(orderName)) {
					list.add(orders);
					map.put(orderName, list);
				}else {
					List<Order> getOrder = map.get(orderName);
					getOrder.add(orders);
					map.put(orderName, getOrder);
				}
			}
		}

		this.batchSplitOut(filename, map, titles, supplier, request, response);
		
	}
	
	/**
	 * 采购单拆分导出
	 * @param filename
	 * @param map
	 * @param titles
	 * @param request
	 * @param response
	 */
	public void batchSplitOut(String filename, Map<String, List<Order>> map , String[] titles , Supplier supplier, HttpServletRequest request , HttpServletResponse response) {
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFCellStyle cellStyle = workbook.createCellStyle();
		//填充色
		cellStyle.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
		cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

		cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		cellStyle.setBorderTop(HSSFBorderFormatting.BORDER_THIN);
		cellStyle.setBorderBottom(HSSFBorderFormatting.BORDER_THIN);
		cellStyle.setBorderLeft(HSSFBorderFormatting.BORDER_THIN);
		cellStyle.setBorderRight(HSSFBorderFormatting.BORDER_THIN);

		HSSFFont font = workbook.createFont();

		font.setFontHeightInPoints((short) 11);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

		cellStyle.setFont(font);

		HSSFCellStyle contentStyle = workbook.createCellStyle();
		contentStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		int sheetIx = 0;
		for(String key : map.keySet()) {
			int rowNumber = 0;
			HSSFSheet sheet = workbook.createSheet();
			workbook.setSheetName(sheetIx, key);
			sheet.setDefaultColumnWidth(10);
			sheet.createRow(0).setHeightInPoints(100);
			
			//标题
			HSSFRow titleRow = sheet.createRow(rowNumber);
			HSSFCellStyle titleStyle = workbook.createCellStyle();
			titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中  
			titleStyle.setFont(font);
			titleRow.setHeightInPoints(20);
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 16));
			HSSFCell cel = titleRow.createCell(0);
			cel.setCellValue(supplier.getName()+"--"+key);
			cel.setCellStyle(titleStyle);
			rowNumber++;
			
			if (titles != null && titles.length > 0) {
				HSSFRow row = sheet.createRow(rowNumber);
				row.setHeight((short) 400);
				for (int i = 0 ; i < titles.length; i++) {
					HSSFCell cell = row.createCell(i);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(titles[i]);
				}
				rowNumber++;
				sheetIx++;
			}
			if(CollectionUtils.isNotEmpty(map.get(key))){
				List<Order> orders = map.get(key);
				for(Order order : orders) {
					//获取商品数量
					List<OrderItem> orderItems = order.getOrderItems() ;

					Need need = order.getNeed();
					
					for(int i=0 ,len = orderItems.size() ; i < len ; i++){

						//创建行
						HSSFRow row = sheet.createRow(rowNumber);
						OrderItem orderItem = orderItems.get(i) ;
						Product product = orderItem.getProduct() ;
						Goods goods = product.getGoods() ;
						
						row.createCell(0).setCellValue(order.getSn());

						row.createCell(1).setCellValue(com.microBusiness.manage.util.DateUtils.formatDateToString(order.getCreateDate(), DateformatEnum.yyyyMMddHHmmss2));

						row.createCell(2).setCellValue(SpringUtils.getMessage("Order.Status." + order.getStatus()));

						row.createCell(3).setCellValue(product.getSn());

						row.createCell(4).setCellValue(orderItem.getName());

						row.createCell(5).setCellValue(StringUtils.join(orderItem.getSpecifications(), ","));

						String basicUnit = SpringUtils.getMessage("Goods.unit." + goods.getUnit());
						if (goods.getUnit() == null) {
							basicUnit = "";
						}
						row.createCell(6).setCellValue(basicUnit);

						row.createCell(7).setCellValue(orderItem.getQuantity());

						row.createCell(8).setCellValue(orderItem.getShippedQuantity());
						
						row.createCell(9).setCellValue(order.getRealProductQuantity(product.getId()));
						
						row.createCell(10).setCellValue(orderItem.getPrice().doubleValue());
						
						row.createCell(11).setCellValue(order.getSupplier().getName());

						row.createCell(12).setCellValue(need.getName());

						row.createCell(13).setCellValue(order.getConsignee());

						row.createCell(14).setCellValue(order.getPhone());

						row.createCell(15).setCellValue(order.getAreaName() + " " + order.getAddress());

						row.createCell(16).setCellValue(com.microBusiness.manage.util.DateUtils.formatDateToString(order.getReDate(), DateformatEnum.yyyyMMdd2));

						rowNumber++;
					}
				}
			}
		}
		
		this.exportEndWork(filename, workbook, request, response);
	
	}

	@Override
	public void selectedSplitOut(Long[] ids, Supplier supplier, HttpServletRequest request,
			HttpServletResponse response) {
		
		String filename = "订单列表" + DateFormatUtils.format(new Date(), "yyyy-MM-dd") + "-采购单.xls";
		String[] titles = new String[]{"订单编号" , "下单时间" , "订单状态" , "商品编号" , "商品名称" , "商品规格" , "基本单位" , "订货数量", "发货数量", "实收数量" , "商品金额" , "供应商" , "收货点" , "收货人", "手机号", "收货地址" , "收货时间"};

		List<Order> datas = null;
		if(ids == null) {
			datas = null;
		}else {
			datas = orderDao.findListByIds(ids);
		}
		Map<String, List<Order>> map = new HashMap<String, List<Order>>();
		if(datas != null) {
			for(Order orders : datas) {
				List<Order> list = new ArrayList<Order>();
				String orderName = orders.getSupplier().getName();
				if(!map.containsKey(orderName)) {
					list.add(orders);
					map.put(orderName, list);
				}else {
					List<Order> getOrder = map.get(orderName);
					getOrder.add(orders);
					map.put(orderName, getOrder);
				}
			}
		}
		this.batchSplitOut(filename, map, titles, supplier, request, response);
		
	}

	@Override
	public Integer searchByStatus(Status status, Supplier supplier) {
		return orderDao.searchByStatus(status, supplier);
	}

	@Override
	public OrderStatisticsDto todayOrderForm(Supplier supplier, Date startDate,
			Date endDate) {
		OrderStatisticsDto orderStatisticsDto = new OrderStatisticsDto();
		OrderStatisticsDto dto1 = orderDao.todayOrderForm1(supplier, startDate, endDate);
		OrderStatisticsDto dto2 = orderDao.todayOrderForm2(supplier, startDate, endDate);
		orderStatisticsDto.setOrderTotal(dto1.getOrderTotal()+dto2.getOrderTotal());
		if(dto1.getTotalAmount() == null) {
			dto1.setTotalAmount(new BigDecimal(0));
		}
		if(dto2.getTotalAmount() == null) {
			dto2.setTotalAmount(new BigDecimal(0));
		}
		orderStatisticsDto.setTotalAmount(dto1.getTotalAmount().add(dto2.getTotalAmount()));
		return orderStatisticsDto;
	}

	@Override
	public OrderStatisticsDto orderRelated(Supplier supplier) {
		OrderStatisticsDto statisticsDto = new OrderStatisticsDto();
		statisticsDto.setGoodTotal(orderDao.countOrderGoods(supplier).getGoodTotal());
		statisticsDto.setSupplierTotal(orderDao.orderRelated1(supplier).getSupplierTotal()+orderDao.orderRelated2(supplier).getSupplierTotal());
		return statisticsDto;
	}

	@Override
	public Integer purchaseOrderByStaticQuery(Status status, Supplier supplier) {
		return orderDao.purchaseOrderByStaticQuery(status, supplier);
	}

	@Override
	public OrderStatisticsDto todayPurchaseOrder(Supplier supplier,
			Date startDate, Date endDate) {
		return orderDao.todayPurchaseOrder(supplier, startDate, endDate);
	}

	@Override
	public OrderStatisticsDto purchaseOrderRelated(Supplier supplier) {
		return orderDao.purchaseOrderRelated(supplier);
	}

	@Override
	public List<OrderStatisticsDto> orderharts(Supplier supplier,
			Date startDate, Date endDate) {
		List<OrderStatisticsDto> list = orderDao.orderharts(supplier, startDate, endDate);
		Map<String, OrderStatisticsDto> map = new HashMap<String, OrderStatisticsDto>();
		for(OrderStatisticsDto OrderStatisticsDto : list) {
			map.put(OrderStatisticsDto.getCreateDate(), OrderStatisticsDto);
		}
		List<OrderStatisticsDto> orderStatisticsDtos = new ArrayList<OrderStatisticsDto>();
		//两个时间差 天数
		int  days = 0;
		Date now = com.microBusiness.manage.util.DateUtils.currentEndTime();
		if (endDate.getTime() > now.getTime()) {
			days = com.microBusiness.manage.util.DateUtils.daysBetween(startDate, now);
		} else {
			days = com.microBusiness.manage.util.DateUtils.daysBetween(startDate, endDate);
		}
		for(int i=0;i<=days;i++) {
			Date date = com.microBusiness.manage.util.DateUtils.plusDays(startDate, i);
			String key = com.microBusiness.manage.util.DateUtils.formatDateToString(date, DateformatEnum.yyyyMMdd2);
			OrderStatisticsDto dto = map.get(key);
			if(dto == null) {
				dto = new OrderStatisticsDto();
				dto.setCreateDate(key);
				dto.setOrderTotal(0);
			}
			orderStatisticsDtos.add(dto);
		}
		return orderStatisticsDtos;
	}

	@Override
	public List<OrderStatisticsDto> purchaseOrderCharts(Supplier supplier,
			Date startDate, Date endDate) {
		List<OrderStatisticsDto> list = orderDao.purchaseOrderCharts(supplier, startDate, endDate);
		Map<String, OrderStatisticsDto> map = new HashMap<String, OrderStatisticsDto>();
		for(OrderStatisticsDto OrderStatisticsDto : list) {
			map.put(OrderStatisticsDto.getCreateDate(), OrderStatisticsDto);
		}
		List<OrderStatisticsDto> orderStatisticsDtos = new ArrayList<OrderStatisticsDto>();
		//两个时间差 天数
		int  days = 0;
		Date now = com.microBusiness.manage.util.DateUtils.currentEndTime();
		if (endDate.getTime() > now.getTime()) {
			days = com.microBusiness.manage.util.DateUtils.daysBetween(startDate, now);
		} else {
			days = com.microBusiness.manage.util.DateUtils.daysBetween(startDate, endDate);
		}
		for(int i=0;i<=days;i++) {
			Date date = com.microBusiness.manage.util.DateUtils.plusDays(startDate, i);
			String key = com.microBusiness.manage.util.DateUtils.formatDateToString(date, DateformatEnum.yyyyMMdd2);
			OrderStatisticsDto dto = map.get(key);
			if(dto == null) {
				dto = new OrderStatisticsDto();
				dto.setCreateDate(key);
				dto.setOrderTotal(0);
			}
			orderStatisticsDtos.add(dto);
		}
		return orderStatisticsDtos;
	}

	@Override
	public void demolitionOrder(Order order, boolean passed, Admin operator,
			String deniedReason,Supplier supplier) {
		//审核
		review(order, passed, operator , deniedReason);
		//审核通过就拆单
		if (passed) {
			//根据总订单商品的supplier id 分类
			Map<Long, List<OrderItem>> map=new HashMap<Long, List<OrderItem>>();
			List<OrderItem> orderItems=order.getOrderItems();
			for (OrderItem orderItem : orderItems) {
				Goods goods=orderItem.getProduct().getGoods();
				Supplier goodSupplier;
				if (goods.getSource() != null) {
					goodSupplier=goods.getSource().getSupplier();
				}else {
					goodSupplier=goods.getSupplier();
				}
				List<OrderItem> list=map.get(goodSupplier.getId());
				if (list==null) {
					list=new ArrayList<>();
				}
				list.add(orderItem);
				map.put(goodSupplier.getId(), list);
			}
			Iterator<Map.Entry<Long, List<OrderItem>>> it = map.entrySet().iterator();
			List<Order> orders = new ArrayList<>();
			while (it.hasNext()) {
				Map.Entry<Long, List<OrderItem>> entry = it.next();

				if (entry.getKey().equals(supplier.getId())) {
					//分销商自己的订单
					Order tempOrder = createDemolitionOrder(order, entry.getValue(), supplier, null,operator, Order.Type.billGeneral);
					orders.add(tempOrder);

					weChatService.sendTemplateMessageByOrderStatus(tempOrder , Order.OrderStatus.create , weChatService.getGlobalToken() , null , this.commonTemplateId , null, "");

				} else {
					//供应商的订单
					Order tempOrder = createDemolitionOrder(order, entry.getValue(), supplierService.find(entry.getKey()), supplier,operator, Order.Type.billDistribution);
					orders.add(tempOrder);

					weChatService.sendTemplateMessageByOrderStatus(tempOrder , Order.OrderStatus.create , weChatService.getGlobalToken() , null , this.commonTemplateId , null, "") ;
				}


				//拆单向客户发送模版消息
				weChatService.sendTemplateMessageByOrderPart(orders, order.getNeed(), order.getMember(), order.getChildMember(), order.getSupplier(), weChatService.getGlobalToken(), null, this.commonTemplateId, null, order, new Date());


			}
		}else {
			order.setType(Order.Type.billGeneral);
			order.setStatus(Order.Status.denied);

			weChatService.sendTemplateMessageByOrderStatus(order , Order.OrderStatus.denied , weChatService.getGlobalToken() , null , this.commonTemplateId , null, "") ;

		}
	}

	/**
	 * 拆单
	 * @param oldOrder	原始主订单
	 * @param orderItems	原始订单分类过后的 OrderItem
	 * @param supplier		卖方
	 * @param toSupplier	买方    为分销商自己的订单的时候为null
	 * @return
	 */
	private Order createDemolitionOrder(Order oldOrder, List<OrderItem> orderItems,Supplier supplier,Supplier toSupplier,Admin operator,Order.Type type) {
		Assert.notNull(oldOrder);
		Assert.notNull(orderItems);
		Assert.notNull(supplier);
		
		Setting setting = SystemUtils.getSetting();
		Order order = new Order();
		order.setMainOrder(oldOrder);
		order.setCreateDate(oldOrder.getCreateDate());
		order.setChildMember(oldOrder.getChildMember());
		do {
			order.setSn(snDao.generate(Sn.Type.order));
		} while (orderDao.findBySn(order.getSn()) != null);
		order.setType(type);
		order.setNeed(oldOrder.getNeed());
		order.setBuyType(oldOrder.getBuyType());
		order.setSupplier(supplier);
		order.setToSupplier(toSupplier);
		order.setReDate(oldOrder.getReDate());
		order.setReCode(RandomStringUtils.randomNumeric(6));
		order.setFee(oldOrder.getFee());
		order.setFreight(oldOrder.getFreight());
		order.setPromotionDiscount(oldOrder.getPromotionDiscount());
		order.setOffsetAmount(oldOrder.getOffsetAmount());
		order.setAmountPaid(oldOrder.getAmountPaid());
		order.setRefundAmount(oldOrder.getRefundableAmount());
		order.setRewardPoint(oldOrder.getRewardPoint());
		order.setExchangePoint(oldOrder.getExchangePoint());
		order.setShippedQuantity(oldOrder.getShippedQuantity());
		order.setReturnedQuantity(oldOrder.getReturnedQuantity());
		order.setConsignee(oldOrder.getConsignee());
		order.setAreaName(oldOrder.getAreaName());
		order.setAddress(oldOrder.getAddress());
		order.setZipCode(oldOrder.getZipCode());
		order.setPhone(oldOrder.getPhone());
		order.setArea(oldOrder.getArea());
		order.setMemo(oldOrder.getMemo());
		order.setIsUseCouponCode(oldOrder.getIsUseCouponCode());
		order.setIsExchangePoint(oldOrder.getIsExchangePoint());
		order.setIsAllocatedStock(oldOrder.getIsAllocatedStock());
		order.setInvoice(oldOrder.getInvoice());
		order.setShippingMethod(oldOrder.getShippingMethod());
		order.setMember(oldOrder.getMember());
		order.setBuyMember(oldOrder.getBuyMember());
		order.setShop(oldOrder.getShop());
		order.setSupplierType(oldOrder.getSupplierType());
		order.setPromotionNames(oldOrder.getPromotionNames());
		List<Coupon> coupons=new ArrayList<>();
		try {
			BeanUtils.copyProperties(coupons, order.getCoupons());
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		order.setCoupons(coupons);
		order.setCouponDiscount(oldOrder.getCouponDiscount());
		order.setCouponCode(oldOrder.getCouponCode());

		order.setTax(oldOrder.getTax());

		order.setStatus(Order.Status.pendingReview);
		order.setPaymentMethod(oldOrder.getPaymentMethod());
		order.setSupplyNeed(oldOrder.getSupplyNeed());

		List<OrderItem> newOrderItems = order.getOrderItems();
		
		BigDecimal amount=BigDecimal.ZERO;
		BigDecimal amountToB=BigDecimal.ZERO;
		BigDecimal price=BigDecimal.ZERO;
		Integer quantity=0;
		Integer weight=0;
		
		for (OrderItem orderItem : orderItems) {
			OrderItem newOrderItem = new OrderItem();
			
			newOrderItem.setSn(orderItem.getSn());
			newOrderItem.setName(orderItem.getName());
			newOrderItem.setType(orderItem.getType());
			newOrderItem.setPrice(orderItem.getPrice());
			newOrderItem.setPriceUnit(orderItem.getPriceUnit());
			newOrderItem.setPriceB(orderItem.getPriceB());
			newOrderItem.setPriceUnitB(orderItem.getPriceUnitB());
			newOrderItem.setWeight(orderItem.getWeight());
			newOrderItem.setIsDelivery(orderItem.getIsDelivery());
			newOrderItem.setThumbnail(orderItem.getThumbnail());
			newOrderItem.setQuantity(orderItem.getQuantity());
			newOrderItem.setShippedQuantity(orderItem.getShippedQuantity());
			newOrderItem.setReturnedQuantity(orderItem.getReturnedQuantity());
			newOrderItem.setProduct(orderItem.getProduct());
			newOrderItem.setOrder(order);
			newOrderItem.setSpecifications(orderItem.getSpecifications());
			//计算重量
			if (orderItem.getProduct() != null && orderItem.getProduct().getWeight() != null && orderItem.getProduct() != null) {
				weight+=orderItem.getProduct().getWeight() * newOrderItem.getQuantity();
			}
			//计算商品金额
			price=price.add(newOrderItem.getPrice());
			//计算商品数量
			quantity+=newOrderItem.getQuantity();
			//计算订单金额--个体客户
			amount=amount.add(newOrderItem.getPriceUnit().multiply(new BigDecimal(newOrderItem.getQuantity())));
			//计算订单金额--企业客户
			if (toSupplier != null) {
				amountToB=amountToB.add(newOrderItem.getPriceUnitB().multiply(new BigDecimal(newOrderItem.getQuantity())));
			}
			newOrderItems.add(newOrderItem);
		}
		order.setWeight(weight);
		order.setPrice(price);
		order.setQuantity(quantity);
		order.setAmount(amount);
		if (toSupplier != null) {
			order.setAmountToB(amountToB);
		}
		
		order.setSupplyType(oldOrder.getSupplyType());
		
		orderDao.persist(order);
		
		
		// 下单收货点
		Need need = order.getNeed();
		// 下单企业
		Supplier bySupplier = toSupplier;
		ChildMember childMember=order.getChildMember();
		
		//下单人
		String operatorName=oldOrder.getCreateOrderLog().getOperator();
		String supplierName=oldOrder.getCreateOrderLog().getSupplierName();


		List<OrderItem> orderItemsAfter = order.getOrderItems();
		OrderItemLog orderItemLog = new OrderItemLog();

		List<OrderItemInfo> orderItemInfos = new ArrayList<>(
				orderItemsAfter.size());
		for (OrderItem orderItem : orderItemsAfter) {
			OrderItemInfo orderItemInfo = new OrderItemInfo();
			orderItemInfo.setBeforeQuantity(orderItem.getQuantity());
			orderItemInfo.setAfterQuantity(orderItem.getQuantity());
			orderItemInfo.setBeforePrice((orderItem.getPrice()));
			orderItemInfo.setAfterPrice((orderItem.getPrice()));
			orderItemInfo.setBeforePriceB(orderItem.getPriceB());
			orderItemInfo.setAfterPriceB(orderItem.getPriceB());
			orderItemInfo.setProduct(orderItem.getProduct());
			orderItemInfo.setOrderItemLog(orderItemLog);
			orderItemInfos.add(orderItemInfo);
		}

		orderItemLog.setOrder(order);
		orderItemLog.setOperatorType(OrderItemLog.OperatorType.create);
		orderItemLog.setType(OrderItemLog.Type.custom);
		orderItemLog.setOrderItemInfos(orderItemInfos);
		orderItemLog.setCreateDate(order.getCreateDate());
		orderItemLog.setOperatorName(operatorName);
		orderItemLog.setSupplierName(supplierName);
		if (order.getBuyType().equals(BuyType.general)) {
			orderItemLog.setLogType(LogType.member);
		}else {
			orderItemLog.setLogType(LogType.distributor);
		}
		
		orderItemLogDao.persist(orderItemLog);

		OrderLog orderLog = new OrderLog();
		orderLog.setOperator(operatorName);
		orderLog.setSupplierName(supplierName);
		if (order.getBuyType().equals(BuyType.general)) {
			orderLog.setLogType(LogType.member);

		}else {
			orderLog.setLogType(LogType.distributor);
		}
		orderLog.setType(OrderLog.Type.create);
		orderLog.setOrder(order);
		orderLog.setCreateDate(oldOrder.getCreateDate());
		
		orderLogDao.persist(orderLog);
		
		// 后台消息推送
		if(order.getType().equals(Order.Type.billGeneral)) {
			orderNewsPushDao.addOrderNewPush(supplier, order, OrderNewsPush.OrderStatus.placeAnOrder, need, supplier.getName(), need.getName(),OrderNewsPush.NoticeObject.order);
		}else {
			orderNewsPushDao.addOrderNewPush(supplier, order, OrderNewsPush.OrderStatus.placeAnOrder, need, bySupplier.getName(), "",OrderNewsPush.NoticeObject.order);
			orderNewsPushDao.addOrderNewPush(bySupplier, order, OrderNewsPush.OrderStatus.placeAnOrder, need, bySupplier.getName(), need.getName(),OrderNewsPush.NoticeObject.purchase);
			orderNewsPushDao.addOrderNewPush(bySupplier, order, OrderNewsPush.OrderStatus.placeAnOrder, need, bySupplier.getName(), need.getName(),OrderNewsPush.NoticeObject.order);
		}
		


		exchangePoint(order);
		if (Setting.StockAllocationTime.order.equals(setting
				.getStockAllocationTime())
				|| (Setting.StockAllocationTime.payment.equals(setting
						.getStockAllocationTime()) && (order.getAmountPaid()
						.compareTo(BigDecimal.ZERO) > 0
						|| order.getExchangePoint() > 0 || order
						.getAmountPayable().compareTo(BigDecimal.ZERO) <= 0))) {
			allocateStock(order);
		}


		/*// mailService.sendCreateOrderMail(order);
		smsService.sendCreateOrderSms(order);
		// TODO: 2017/2/14 发送模版消息
		weChatService.sendTemplateMessage(order, commonTemplateId,
				weChatService.getGlobalToken(), Order.OrderStatus.create);
		// 订单创建成功,向供应商增加手机短信提醒
		smsService.sendContent(supplier.getTel(), orderNotice);
		// 向供应商的接收员发送模版消息
		weChatService.sendTemplateMessageToNoticeUser(supplier, order,
				Order.OrderStatus.create, commonTemplateId,
				weChatService.getGlobalToken());

		// 向采购方企业接受员发送模版消息
		weChatService.sendTemplateMessageToNoticeUserPurchase(supplier,
				bySupplier, need, order, Order.OrderStatus.create,
				commonTemplateId, weChatService.getGlobalToken(),
				NoticeTypePurchase.Type.order_create, "");

*/
		return order;
	}

	@Override
	public Map<Integer, List<AssListStatisticsDto>> purchaseListKanban(
			Member member , List<Integer> status) {
		List<AssListStatisticsDto> orderStatistics = orderDao.findByList(member, status);
		List<AssListStatisticsDto> pOrderStatistics = new ArrayList<AssListStatisticsDto>();
		if(orderStatistics.size() > 0) {
			//起始年份
			Integer startYear = orderStatistics.get(0).getYears();
			//起始月份
			Integer startMonth = orderStatistics.get(0).getMonths();
			//起始日期
			Date startDate = com.microBusiness.manage.util.DateUtils.specifyMonthStartTime(startYear, startMonth);
			//获取两个时间段内所有的月份
			List<Date> list = com.microBusiness.manage.util.DateUtils.getDateList(startDate, new Date());
			for(Date date : list) {
				AssListStatisticsDto assListStatisticsDto = new AssListStatisticsDto();
				int year = com.microBusiness.manage.util.DateUtils.getSpecifiedDateByYear(date);
				int month = com.microBusiness.manage.util.DateUtils.getSpecifiedDateByMonth(date);
				assListStatisticsDto.setYears(year);
				assListStatisticsDto.setMonths(month);
				pOrderStatistics.add(assListStatisticsDto);
			}
			for(AssListStatisticsDto pdto : pOrderStatistics) {
				for(AssListStatisticsDto dto : orderStatistics) {
					if(pdto.getYears().equals(dto.getYears()) && pdto.getMonths().equals(dto.getMonths())) {
						pdto.setCounts(dto.getCounts());
					}
				}
				if(pdto.getCounts() == null) {
					pdto.setCounts(0);
				}
			}
		}
		
		//按照年份来分组
		Map<Integer , List<AssListStatisticsDto>> map = new HashMap<Integer, List<AssListStatisticsDto>>();
		for(AssListStatisticsDto dto : pOrderStatistics) {
			boolean bool = map.containsKey(dto.getYears());
			if(bool) {
				List<AssListStatisticsDto> assListDto = map.get(dto.getYears());
				assListDto.add(dto);
			}else {
				List<AssListStatisticsDto> assListStatisticsDtos2 = new ArrayList<AssListStatisticsDto>();
				assListStatisticsDtos2.add(dto);
				map.put(dto.getYears(), assListStatisticsDtos2);
			}
		}
		return map;
	}

	@Override
	public Map<String, Object> kanbandetail(Member member, Date startDate,Date endDate) {
		List<AssListStatisticsDto> dtos = orderDao.kanbandetail(member, startDate, endDate);
		List<AssListStatisticsDto> listStatisticsDtos = new ArrayList<AssListStatisticsDto>();
		List<Integer> status = new ArrayList<Integer>();
    	status.add(Order.Status.pendingReview.ordinal());
    	status.add(Order.Status.pendingShipment.ordinal());
    	status.add(Order.Status.inShipment.ordinal());
    	status.add(Order.Status.shipped.ordinal());
    	status.add(Order.Status.completed.ordinal());
    	status.add(Order.Status.applyCancel.ordinal());
    	status.add(Order.Status.denied.ordinal());
    	status.add(Order.Status.canceled.ordinal());
    	for(Integer teg : status) {
    		AssListStatisticsDto dto = new AssListStatisticsDto();
    		dto.setStatu(teg);
    		listStatisticsDtos.add(dto);
    	}
    	for(AssListStatisticsDto dto : listStatisticsDtos) {
    		for(AssListStatisticsDto dto2 : dtos) {
    			if(dto.getStatu() == dto2.getStatu()) {
    				dto.setCounts(dto2.getCounts());
    			}
    		}
    		if(dto.getCounts() == null) {
    			dto.setCounts(0);
    		}
    		
    	}
    	
    	Map<String, Object> map = new HashMap<String, Object>();
    	for(AssListStatisticsDto dto : listStatisticsDtos) {
    		if(dto.getStatu() == Order.Status.pendingReview.ordinal()) {
    			map.put("pendingReview", dto.getCounts());
    		}else if(dto.getStatu() == Order.Status.pendingShipment.ordinal()) {
    			map.put("pendingShipment", dto.getCounts());
    		}else if(dto.getStatu() == Order.Status.inShipment.ordinal()) {
    			map.put("inShipment", dto.getCounts());
    		}else if(dto.getStatu() == Order.Status.shipped.ordinal()) {
    			map.put("shipped", dto.getCounts());
    		}else if(dto.getStatu() == Order.Status.completed.ordinal()) {
    			map.put("completed", dto.getCounts());
    		}else if(dto.getStatu() == Order.Status.applyCancel.ordinal()) {
    			map.put("applyCancel", dto.getCounts());
    		}else if(dto.getStatu() == Order.Status.denied.ordinal()) {
    			map.put("denied", dto.getCounts());
    		}else if(dto.getStatu() == Order.Status.canceled.ordinal()) {
    			map.put("canceled", dto.getCounts());
    		}
    	}
		return map;
	}

	@Override
	public List<Order> find(Supplier supplier, Date startDate, Date endDate) {
		return orderDao.find(supplier, startDate, endDate);
	}

	@Override
	public void updateStatus(Order order, LocalOrderSharingStatus status,
			ChildMember childMember) {
		
		OrderShareLog orderShareLog = new OrderShareLog();
		orderShareLog.setName(childMember.getNickName());
		orderShareLog.setOrder(order);
		
		//分享
		if(status.equals(LocalOrderSharingStatus.share)) {
			if(!order.getSharingStatus().equals(Order.SharingStatus.end)) {
				order.setSharingStatus(Order.SharingStatus.share);
			}
		}else if(status.equals(LocalOrderSharingStatus.end)) {//终结
			orderShareLog.setType(OrderShareLog.Type.theEnd);
			order.setSharingStatus(Order.SharingStatus.end);
			orderShareLogDao.persist(orderShareLog);
		}else if(status.equals(LocalOrderSharingStatus.participate)) {//参与
			//加入关系
			if(orderRelationDao.findByChildMember(childMember, order) == null) {
				OrderRelation orderRelation = new OrderRelation();
				orderRelation.setType(OrderRelation.Type.participant);
				orderRelation.setChildMember(childMember);
				orderRelation.setOrder(order);
				orderRelation.setMember(childMember.getMember());
				orderRelationDao.persist(orderRelation);
			}
			orderShareLog.setType(OrderShareLog.Type.participate);
			orderShareLogDao.persist(orderShareLog);
			
		}else if(status.equals(LocalOrderSharingStatus.noparticipate)) {//退出参与
			orderShareLog.setType(OrderShareLog.Type.dropOut);
			OrderRelation orderRelation = orderRelationDao.findByChildMember(childMember, order);
			//删除关系
			orderRelationDao.remove(orderRelation);
			orderShareLogDao.persist(orderShareLog);
			
		}
		
	}

	@Override
	public int countSaleGood(Member member) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Transactional
	@Override
	public Order create(Long productId, Integer quantity, PaymentMethod paymentMethod, ShippingMethod shippingMethod,
			CouponCode couponCode, Invoice invoice, BigDecimal balance, String memo, Date reDate, Long supplierId,
			SupplyType supplyType, ChildMember childMember, Receiver receiver) {

		Member buyMember = childMember.getMember();
        BigDecimal totalPrice = BigDecimal.ZERO ;
        BigDecimal totalPriceB = BigDecimal.ZERO ;

        Supplier supplier = supplierDao.find(supplierId) ;//品牌商企业
			
		Product product = productService.find(productId);
		Goods goods = product.getGoods();
		if(goods.getSales() == null){
			goods.setSales(0l);
		}
		if(product.getSales() == null){
			product.setSales(0l);
		}
		goods.setSales(goods.getSales()+product.getSales());
		goodsService.update(goods);
		logger.info("update goods sales....");
		
		//更新product sku销量
		product.setSales(product.getSales() + Long.valueOf(quantity));
		productService.update(product);
		logger.info("update product sales....");
		
		
		BigDecimal proPrice = product.getPrice().multiply(new BigDecimal(quantity));
		totalPrice = totalPrice.add(proPrice);
		totalPriceB = totalPriceB.add(proPrice);
		

       Order order = createSingleBaseOrderForPur(buyMember, supplier, totalPrice, totalPriceB, product,
                paymentMethod, shippingMethod, couponCode, invoice, balance, memo, 
                reDate , supplyType , childMember, receiver, SupplierType.ONE, Types.platform, quantity);
       
       return order;

	}

	@Override
	@Transactional
	public int updatePaySn(Member member, Order order) {
		orderDao.persist(order);
		
		Payment payment = new Payment();
		String snPay = order.getPaySn()!=null ? order.getPaySn() : snDao.generate(Sn.Type.payment);
		payment.setFee(BigDecimal.ZERO);
		payment.setAmount(order.getAmount());		
		payment.setSn(snPay);
		payment.setOrder(order);
		payment.setMethod(Payment.Method.online);
		payment.setPaymentMethod(PAYMENT_PLUGIN.WENCHAT.PLUGIN_NAME);
		payment.setTransactionId(null);
		paymentDao.persist(payment);
		logger.info("[payment()_Payment]: payment.fee="+payment.getFee()
										+", payment.amount="+payment.getAmount()
										+", payment.sn="+payment.getSn()
										+", payment.orderId="+payment.getOrder().getId()
										+", payment.transactionId="+payment.getTransactionId());
		
		
		//step5.记录支付日志
		PaymentLog paymentLog = new PaymentLog();
		paymentLog.setType(PaymentLog.Type.payment);
		paymentLog.setStatus(PaymentLog.Status.wait);
		paymentLog.setFee(order.getFee());
		paymentLog.setAmount(order.getAmount());
		paymentLog.setPaymentPluginId(PAYMENT_PLUGIN.WENCHAT.PLUGIN_ID);
		paymentLog.setPaymentPluginName(PAYMENT_PLUGIN.WENCHAT.PLUGIN_NAME);
		paymentLog.setOrder(order);
		paymentLog.setMember(order.getMember());
		paymentLog.setSn(snPay);
		paymentLogDao.persist(paymentLog);
		
		
		
		
		
		return 1;
	}
	
	@Override
	public void payment(Order order, BigDecimal amountPaid, BigDecimal fee, 
			String transactionId, String operator, Map<String, String> weixinReMap){
		
		logger.info("[payment()]: order.sn="+order.getSn()+", amountPaid="+amountPaid+", fee="+fee+", transactionId="+transactionId+", operator="+operator);
		
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = transactionManager.getTransaction(def);		
		
		OrderLog orderLog = new OrderLog();
		
		try {
			//step1.修改支付信息
			Payment payment = paymentDao.findBySn(order.getPaySn());
			payment.setTransactionId(transactionId);
			
			PaymentLog paymentLog = paymentLogDao.findBySn(order.getPaySn());
			paymentLog.setStatus(PaymentLog.Status.success);
			paymentLogDao.persist(paymentLog);
			
			//step2.增加分配商品数量[现改为订单生成的时候就分配]

			//step3.修改订单状态
			order.setAmountPaid(order.getAmountPaid().add(payment.getEffectiveAmount()));
			order.setFee(order.getFee().add(payment.getFee()));
			if (!order.hasExpired() && Order.Status.pendingPayment.equals(order.getStatus()) 
//					&& order.getAmountPayable().compareTo(BigDecimal.ZERO) <= 0
					) {
				order.setStatus(Order.Status.pendingShipment);
				order.setExpire(null);
			}
			if(order.getOrderItems() != null){
				for (OrderItem orderItem : order.getOrderItems()) {
					Product product = orderItem.getProduct();
					if(product.getSales() == null){
						product.setSales(0l);
					}
					if(orderItem.getQuantity() == null){
						orderItem.setQuantity(0);
					}
					product.setSales(product.getSales()+orderItem.getQuantity());
					productService.update(product);
				}
			}
			//更改订单状态
			orderDao.persist(order);
			
			logger.info("[payment()_Order]: order.amountPaid="+order.getAmountPaid()
											+", order.fee="+order.getFee()
											+", order.status="+order.getStatus()
											+", order.id="+order.getId()
											+", order.Expire="+order.getExpire());

			//step4.记录订单日志
			
			orderLog.setType(OrderLog.Type.pendingShipment);
			orderLog.setOrder(order);
			orderLog.setOperator(operator);
			orderLog.setContent(ORDER_LOG_CONTENT.PAYMENT);
			orderLogDao.persist(orderLog);
			
			
			ChildMember childMember = order.getChildMember();
			//如果购买了会员商品，认证成会员
			if(childMember.getIsChecked() == null){
				childMember.setIsChecked(false);
			}
			if(!childMember.getIsChecked()){
				authChildMember(order);
			}
			//根据商品利率 判断级别，计算上级返利
			/*int level = 0;
			ChildMember c1 = childMember.getParent();
			ChildMember c2 = null;
			ChildMember c3 = null;
			if(c1 != null){
				level++;
				c2 = c1.getParent();
				if(c2 != null){
					level++;
					c3 = c2.getParent();
					if(c3 != null){
						level++;
					}
				}
			}
			
			for (OrderItem orderItem : order.getOrderItems()) {
				Goods goods = orderItem.getProduct().getGoods();
				if(level == 1){
					Float rate1 = goods.getRate1();
					rate1 = rate1 == null ? 0 : rate1;
					BigDecimal price = orderItem.getProduct().getPrice();
					BigDecimal ratePrice1 = price.multiply(new BigDecimal(rate1))
							.setScale(2, RoundingMode.HALF_UP);
					
					//返利积分记录
					orderItem.setUone(c1);
					orderItem.setUone_score(ratePrice1);
					orderItemDao.persist(orderItem);
					
					//总积分
					Member member1 = c1.getMember();
					member1.setPoint(member1.getPoint().add(ratePrice1));
					memberDao.persist(member1);
					
				}else if(level == 2){
					Float rate1 = goods.getRate1();
					rate1 = rate1 == null ? 0 : rate1;
					BigDecimal price = orderItem.getProduct().getPrice();
					BigDecimal ratePrice1 = price.multiply(new BigDecimal(rate1))
							.setScale(2, RoundingMode.HALF_UP);
					
					orderItem.setUone(c1);
					orderItem.setUone_score(ratePrice1);
					
					Member member1 = c1.getMember();
					member1.setPoint(member1.getPoint().add(ratePrice1));
					memberDao.persist(member1);
					
					Float rate2 = goods.getRate2();
					rate2 = rate2 == null ? 0 : rate2;
					BigDecimal ratePrice2 = price.multiply(new BigDecimal(rate2))
							.setScale(2, RoundingMode.HALF_UP);
					
					orderItem.setUtwo(c2);
					orderItem.setUtwo_score(ratePrice2);
					
					Member member2 = c2.getMember();
					member2.setPoint(member2.getPoint().add(ratePrice2));
					memberDao.persist(member2);
					
					orderItemDao.persist(orderItem);
				}else if(level == 3){
					Float rate1 = goods.getRate1();
					rate1 = rate1 == null ? 0 : rate1;
					logger.info("rate1:" + rate1);
					BigDecimal price = orderItem.getProduct().getPrice();
					BigDecimal ratePrice1 = price.multiply(new BigDecimal(rate1))
							.setScale(2, RoundingMode.HALF_UP);
					
					orderItem.setUone(c1);
					orderItem.setUone_score(ratePrice1);
					
					
					Member member1 = c1.getMember();
					member1.setPoint(member1.getPoint().add(ratePrice1));
					memberDao.persist(member1);
					
					
					
					Float rate2 = goods.getRate2();
					rate2 = rate2 == null ? 0 : rate2;
					logger.info("rate2:" + rate2);
					BigDecimal ratePrice2 = price.multiply(new BigDecimal(rate2))
							.setScale(2, RoundingMode.HALF_UP);
					
					orderItem.setUtwo(c2);
					orderItem.setUtwo_score(ratePrice2);
					
					Member member2 = c2.getMember();
					member2.setPoint(member2.getPoint().add(ratePrice2));
					memberDao.persist(member2);
					
					Float rate3 = goods.getRate3();
					rate3 = rate3 == null ? 0 : rate3;
					logger.info("rate3:" + rate3);
					BigDecimal ratePrice3 = price.multiply(new BigDecimal(rate3))
							.setScale(2, RoundingMode.HALF_UP);
					
					orderItem.setUthree(c3);
					orderItem.setUthree_score(ratePrice3);
					
					Member member3 = c3.getMember();
					member3.setPoint(member3.getPoint().add(ratePrice3));
					memberDao.persist(member3);
					
					orderItemDao.persist(orderItem);
					
				}
			}
			*/
			
			// 记录操作日志
			String sn = order.getSn();
			logService.createLog("订单支付", operator, "订单：" + sn + "，已付金额：" + amountPaid,
					"订单：" + sn + "，返回转换成json的数据：" + new Gson().toJson(weixinReMap).toString(), 
					IpUtil.getHostIp());
			
			transactionManager.commit(status);
			
			
			
		} catch (TransactionException e) {
			transactionManager.rollback(status);			
			logger.error("[payment()] error!", e);
			e.printStackTrace();
		}
		
	}
	
	public void authChildMember(Order order){
		if(order != null){
			for (OrderItem orderItem : order.getOrderItems()) {
				Goods goods = orderItem.getProduct().getGoods();
				if(goods.getIs2Member() != null && goods.getIs2Member()){//会员商品标识
					ChildMember childMember = order.getChildMember();
					childMember.setIsChecked(true);
					childMemberDao.persist(childMember);
					//发送通知，恭喜成为会员
					
					break;
				}
			}
		}
	}
	
	//分销结算
	public void distributionSettlement(Order order) {
		ChildMember childMember = order.getChildMember();
		//根据商品利率 判断级别，计算上级返利
		int level = 0;
		ChildMember c1 = childMember.getParent();
		ChildMember c2 = null;
		ChildMember c3 = null;
		if(c1 != null){
			level++;
			c2 = c1.getParent();
			if(c2 != null){
				level++;
				c3 = c2.getParent();
				if(c3 != null){
					level++;
				}
			}
		}
		String sn=order.getSn();
		BigDecimal amount=order.getPrice();//订单总金额
		String lastDay=com.microBusiness.manage.util.DateUtils.convertToString(new Date(), "yyyy-MM-dd");
		//计算分销商利润
		if(level == 1){
			Float rate1 = distributionRate1;
			rate1 = rate1 == null ? 0 : rate1;
			BigDecimal ratePrice1 = amount.multiply(new BigDecimal(rate1))
					.setScale(2, RoundingMode.HALF_UP);
			
			//分销返利记录
			order.setDone(c1);
			order.setDone_score(ratePrice1);
			orderDao.persist(order);
			
			MemberIncome income1=new MemberIncome();
			income1.setMember(c1);
			income1.setAmount(ratePrice1);
			income1.setOrderId(order.getId());
			income1.setTypes(MemberIncome.TYPE_INCOME);
			income1.setTitle("下级利润提成");
			income1.setLevel(1);
			memberIncomeDao.persist(income1);
			//总收益
			Member member1 = c1.getMember();
			member1.setBalance(member1.getBalance().add(ratePrice1));
			member1.setIncome(member1.getIncome().add(ratePrice1));
			member1.setLastDay(lastDay);
			memberDao.persist(member1);
			logger.info("订单号："+sn+" 的一级分销【"+c1.getSmOpenId()+"】提成："+ratePrice1);
			
		}else if(level == 2){
			Float rate1 = distributionRate1;
			rate1 = rate1 == null ? 0 : rate1;
			BigDecimal ratePrice1 = amount.multiply(new BigDecimal(rate1))
					.setScale(2, RoundingMode.HALF_UP);
			
			order.setDone(c1);
			order.setDone_score(ratePrice1);
			//orderDao.persist(order);
			
			MemberIncome income1=new MemberIncome();
			income1.setMember(c1);
			income1.setAmount(ratePrice1);
			income1.setOrderId(order.getId());
			income1.setTypes(MemberIncome.TYPE_INCOME);
			income1.setTitle("下级利润提成");
			income1.setLevel(1);
			memberIncomeDao.persist(income1);
			
			Member member1 = c1.getMember();
			member1.setBalance(member1.getBalance().add(ratePrice1));
			member1.setIncome(member1.getIncome().add(ratePrice1));
			member1.setLastDay(lastDay);
			memberDao.persist(member1);
			logger.info("订单号："+sn+" 的二级分销-一级【"+c1.getSmOpenId()+"】提成："+ratePrice1);
			Float rate2 = distributionRate2;
			rate2 = rate2 == null ? 0 : rate2;
			BigDecimal ratePrice2 = amount.multiply(new BigDecimal(rate2))
					.setScale(2, RoundingMode.HALF_UP);
			
			order.setDtwo(c2);
			order.setDtwo_score(ratePrice2);
			orderDao.persist(order);
			
			MemberIncome income2=new MemberIncome();
			income2.setMember(c2);
			income2.setAmount(ratePrice2);
			income2.setOrderId(order.getId());
			income2.setTypes(MemberIncome.TYPE_INCOME);
			income2.setTitle("下级利润提成");
			income2.setLevel(2);
			memberIncomeDao.persist(income2);
			
			Member member2 = c2.getMember();
			member2.setBalance(member2.getBalance().add(ratePrice2));
			member2.setIncome(member2.getIncome().add(ratePrice2));
			member2.setLastDay(lastDay);
			memberDao.persist(member2);
			logger.info("订单号："+sn+" 的二级分销-二级【"+c2.getSmOpenId()+"】提成："+ratePrice2);
		}else if(level == 3){
			Float rate1 = distributionRate1;
			rate1 = rate1 == null ? 0 : rate1;
			//logger.info("rate1:" + rate1);
			BigDecimal ratePrice1 = amount.multiply(new BigDecimal(rate1))
					.setScale(2, RoundingMode.HALF_UP);
			
			order.setDone(c1);
			order.setDone_score(ratePrice1);
			
			MemberIncome income1=new MemberIncome();
			income1.setMember(c1);
			income1.setAmount(ratePrice1);
			income1.setOrderId(order.getId());
			income1.setTypes(MemberIncome.TYPE_INCOME);
			income1.setTitle("下级利润提成");
			income1.setLevel(1);
			memberIncomeDao.persist(income1);
			
			
			Member member1 = c1.getMember();
			member1.setBalance(member1.getBalance().add(ratePrice1));
			member1.setIncome(member1.getIncome().add(ratePrice1));
			member1.setLastDay(lastDay);
			memberDao.persist(member1);
			logger.info("订单号："+sn+" 的三级分销-一级【"+c1.getSmOpenId()+"】提成："+ratePrice1);
			
			
			Float rate2 = distributionRate2;
			rate2 = rate2 == null ? 0 : rate2;
			//logger.info("rate2:" + rate2);
			BigDecimal ratePrice2 = amount.multiply(new BigDecimal(rate2))
					.setScale(2, RoundingMode.HALF_UP);
			
			order.setDtwo(c2);
			order.setDtwo_score(ratePrice2);
			
			
			MemberIncome income2=new MemberIncome();
			income2.setMember(c2);
			income2.setAmount(ratePrice2);
			income2.setOrderId(order.getId());
			income2.setTypes(MemberIncome.TYPE_INCOME);
			income2.setTitle("下级利润提成");
			income2.setLevel(2);
			memberIncomeDao.persist(income2);
			
			Member member2 = c2.getMember();
			member2.setBalance(member2.getBalance().add(ratePrice2));
			member2.setIncome(member2.getIncome().add(ratePrice2));
			member2.setLastDay(lastDay);
			memberDao.persist(member2);
			logger.info("订单号："+sn+" 的三级分销-二级【"+c2.getSmOpenId()+"】提成："+ratePrice2);
			
			Float rate3 = distributionRate3;
			rate3 = rate3 == null ? 0 : rate3;
			//logger.info("rate3:" + rate3);
			BigDecimal ratePrice3 = amount.multiply(new BigDecimal(rate3))
					.setScale(2, RoundingMode.HALF_UP);
			
			order.setDthree(c3);
			order.setDthree_score(ratePrice3);
			orderDao.persist(order);
			
			MemberIncome income3=new MemberIncome();
			income3.setMember(c3);
			income3.setAmount(ratePrice3);
			income3.setOrderId(order.getId());
			income3.setTypes(MemberIncome.TYPE_INCOME);
			income3.setTitle("下级利润提成");
			income3.setLevel(3);
			memberIncomeDao.persist(income3);
			
			Member member3 = c3.getMember();
			member3.setBalance(member3.getBalance().add(ratePrice3));
			member3.setIncome(member3.getIncome().add(ratePrice3));
			member3.setLastDay(lastDay);
			memberDao.persist(member3);
			logger.info("订单号："+sn+" 的三级分销-三级【"+c3.getSmOpenId()+"】提成："+ratePrice3);
		}
//		for (OrderItem orderItem : order.getOrderItems()) {
//			if(level == 1){
//				Float rate1 = distributionRate1;
//				rate1 = rate1 == null ? 0 : rate1;
//				BigDecimal price = orderItem.getProduct().getPrice();
//				BigDecimal ratePrice1 = price.multiply(new BigDecimal(rate1))
//						.setScale(2, RoundingMode.HALF_UP);
//				
//				//分销返利记录
//				orderItem.setDone(c1);
//				orderItem.setDone_score(ratePrice1);
//				orderItemDao.persist(orderItem);
//				
//				MemberIncome income1=new MemberIncome();
//				income1.setMember(c1);
//				income1.setAmount(ratePrice1);
//				income1.setOrderId(order.getId());
//				income1.setTypes(MemberIncome.TYPE_INCOME);
//				income1.setTitle("下级收益提成");
//				memberIncomeDao.persist(income1);
//				//总收益
//				Member member1 = c1.getMember();
//				member1.setBalance(member1.getBalance().add(ratePrice1));
//				member1.setIncome(member1.getIncome().add(ratePrice1));
//				member1.setLastDay(lastDay);
//				memberDao.persist(member1);
//				logger.info("订单号："+sn+" 的一级分销【"+c1.getSmOpenId()+"】提成："+ratePrice1);
//				
//			}else if(level == 2){
//				Float rate1 = distributionRate1;
//				rate1 = rate1 == null ? 0 : rate1;
//				BigDecimal price = orderItem.getProduct().getPrice();
//				BigDecimal ratePrice1 = price.multiply(new BigDecimal(rate1))
//						.setScale(2, RoundingMode.HALF_UP);
//				
//				orderItem.setDone(c1);
//				orderItem.setDone_score(ratePrice1);
//				
//				MemberIncome income1=new MemberIncome();
//				income1.setMember(c1);
//				income1.setAmount(ratePrice1);
//				income1.setOrderId(order.getId());
//				income1.setTypes(MemberIncome.TYPE_INCOME);
//				income1.setTitle("下级收益提成");
//				memberIncomeDao.persist(income1);
//				
//				Member member1 = c1.getMember();
//				member1.setBalance(member1.getBalance().add(ratePrice1));
//				member1.setIncome(member1.getIncome().add(ratePrice1));
//				member1.setLastDay(lastDay);
//				memberDao.persist(member1);
//				logger.info("订单号："+sn+" 的二级分销-一级【"+c1.getSmOpenId()+"】提成："+ratePrice1);
//				Float rate2 = distributionRate2;
//				rate2 = rate2 == null ? 0 : rate2;
//				BigDecimal ratePrice2 = price.multiply(new BigDecimal(rate2))
//						.setScale(2, RoundingMode.HALF_UP);
//				
//				orderItem.setDtwo(c2);
//				orderItem.setDtwo_score(ratePrice2);
//				
//				MemberIncome income2=new MemberIncome();
//				income2.setMember(c2);
//				income2.setAmount(ratePrice2);
//				income2.setOrderId(order.getId());
//				income2.setTypes(MemberIncome.TYPE_INCOME);
//				income2.setTitle("下级收益提成");
//				memberIncomeDao.persist(income2);
//				
//				Member member2 = c2.getMember();
//				member2.setBalance(member2.getBalance().add(ratePrice2));
//				member2.setIncome(member2.getIncome().add(ratePrice2));
//				member2.setLastDay(lastDay);
//				memberDao.persist(member2);
//				logger.info("订单号："+sn+" 的二级分销-二级【"+c2.getSmOpenId()+"】提成："+ratePrice2);
//				orderItemDao.persist(orderItem);
//			}else if(level == 3){
//				Float rate1 = distributionRate1;
//				rate1 = rate1 == null ? 0 : rate1;
//				//logger.info("rate1:" + rate1);
//				BigDecimal price = orderItem.getProduct().getPrice();
//				BigDecimal ratePrice1 = price.multiply(new BigDecimal(rate1))
//						.setScale(2, RoundingMode.HALF_UP);
//				
//				orderItem.setDone(c1);
//				orderItem.setDone_score(ratePrice1);
//				
//				MemberIncome income1=new MemberIncome();
//				income1.setMember(c1);
//				income1.setAmount(ratePrice1);
//				income1.setOrderId(order.getId());
//				income1.setTypes(MemberIncome.TYPE_INCOME);
//				income1.setTitle("下级收益提成");
//				memberIncomeDao.persist(income1);
//				
//				
//				Member member1 = c1.getMember();
//				member1.setBalance(member1.getBalance().add(ratePrice1));
//				member1.setIncome(member1.getIncome().add(ratePrice1));
//				member1.setLastDay(lastDay);
//				memberDao.persist(member1);
//				logger.info("订单号："+sn+" 的三级分销-一级【"+c1.getSmOpenId()+"】提成："+ratePrice1);
//				
//				
//				Float rate2 = distributionRate2;
//				rate2 = rate2 == null ? 0 : rate2;
//				//logger.info("rate2:" + rate2);
//				BigDecimal ratePrice2 = price.multiply(new BigDecimal(rate2))
//						.setScale(2, RoundingMode.HALF_UP);
//				
//				orderItem.setDtwo(c2);
//				orderItem.setDtwo_score(ratePrice2);
//				
//				MemberIncome income2=new MemberIncome();
//				income2.setMember(c2);
//				income2.setAmount(ratePrice2);
//				income2.setOrderId(order.getId());
//				income2.setTypes(MemberIncome.TYPE_INCOME);
//				income2.setTitle("下级收益提成");
//				memberIncomeDao.persist(income2);
//				
//				Member member2 = c2.getMember();
//				member2.setBalance(member2.getBalance().add(ratePrice2));
//				member2.setIncome(member2.getIncome().add(ratePrice2));
//				member2.setLastDay(lastDay);
//				memberDao.persist(member2);
//				logger.info("订单号："+sn+" 的三级分销-二级【"+c2.getSmOpenId()+"】提成："+ratePrice2);
//				
//				Float rate3 = distributionRate3;
//				rate3 = rate3 == null ? 0 : rate3;
//				//logger.info("rate3:" + rate3);
//				BigDecimal ratePrice3 = price.multiply(new BigDecimal(rate3))
//						.setScale(2, RoundingMode.HALF_UP);
//				
//				orderItem.setDthree(c3);
//				orderItem.setDthree_score(ratePrice3);
//				
//				MemberIncome income3=new MemberIncome();
//				income3.setMember(c3);
//				income3.setAmount(ratePrice3);
//				income3.setOrderId(order.getId());
//				income3.setTypes(MemberIncome.TYPE_INCOME);
//				income3.setTitle("下级收益提成");
//				memberIncomeDao.persist(income3);
//				
//				Member member3 = c3.getMember();
//				member3.setBalance(member3.getBalance().add(ratePrice3));
//				member3.setIncome(member3.getIncome().add(ratePrice3));
//				member3.setLastDay(lastDay);
//				memberDao.persist(member3);
//				logger.info("订单号："+sn+" 的三级分销-三级【"+c3.getSmOpenId()+"】提成："+ratePrice3);
//				
//				orderItemDao.persist(orderItem);
//				
//			}
//		}
		if(level>0) {
			//发送支付成功 告知上级
			weChatService.sendTemplateMessage2ParentChildMember(order , memberTemplateId ,weChatService.getGlobalToken());
		}
	}
	
	
	public static void main(String[] args) {
		
		BigDecimal ratePrice1 = new BigDecimal(1).multiply(new BigDecimal(0.065))
				.setScale(2, RoundingMode.HALF_UP);
		System.out.println(ratePrice1);
	}
	
}