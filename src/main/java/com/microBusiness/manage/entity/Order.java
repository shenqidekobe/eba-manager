/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.AttributeConverter;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Converter;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.groups.Default;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.microBusiness.manage.BaseAttributeConverter;

@Entity
@Table(name = "xx_order")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_order")
public class Order extends BaseEntity<Long> {

	public String getPaySn() {
		return paySn;
	}

	public void setPaySn(String paySn) {
		this.paySn = paySn;
	}

	private static final long serialVersionUID = 2253558998320786386L;

	public static final int LOCK_EXPIRE = 60;

	public interface Delivery extends Default {

	}
	//总订单类型
	public enum Type {
		//直销订单
		general,
		//分销订单
		distribution,
		//拆单后的直销单
		billGeneral,
		//拆单后的分销单
		billDistribution,
		//非自营订单
		formal,
		//本地订单
		local
	}
	//下单类型
	public enum BuyType{
		//正常下单
		general,
		//个体客户代下单
		substitute,
		//流水客户代下单
		waterSubstitute,
	}

	public enum Status {
		//等待付款
		pendingPayment,
		//等待审核
		pendingReview,
		//等待发货
		pendingShipment,
		//已发货
		shipped,
		//已收货
		received,
		//已完成
		completed,
		//已失败
		failed,
		//已取消
		canceled,
		//已拒绝
		denied,
		//申请取消
		applyCancel,
		//通过取消
		passedCancel,
		//拒绝取消
		deniedCancel,
		//发货中
		inShipment

	}
	

	private String sn;

	private Type type;
	
	private BuyType buyType;
	
	private Status status;
	//商品价格
	private BigDecimal price;

	//手续费
	private BigDecimal fee;
	//运费
	private BigDecimal freight;
	//税
	private BigDecimal tax;
	//促销折扣
	private BigDecimal promotionDiscount;
	//优惠券折扣
	private BigDecimal couponDiscount;
	//调整金额
	private BigDecimal offsetAmount;
	//订单金额---个体客户
	private BigDecimal amount;
	//订单金额---企业
	private BigDecimal amountToB;
	//已付金额
	private BigDecimal amountPaid;
	//退款金额
	private BigDecimal refundAmount;

	private Long rewardPoint;

	private Long exchangePoint;

	private Integer weight;

	private Integer quantity;

	//发货数量
	private Integer shippedQuantity;

	private Integer returnedQuantity;

	private String consignee;

	private String areaName;

	private String address;

	private String zipCode;

	private String phone;

	private String memo;

	private Date expire;

	private Boolean isUseCouponCode;

	private Boolean isExchangePoint;

	private Boolean isAllocatedStock;

	private String paymentMethodName;

	private PaymentMethod.Type paymentMethodType;

	private String shippingMethodName;

	private String lockKey;

	private Date lockExpire;

	private Date completeDate;
	//发票相关
	private Invoice invoice;

	private Area area;

	private PaymentMethod paymentMethod;

	private ShippingMethod shippingMethod;

	//主账号
	private Member member;

	//下单账号
	private Member buyMember;

	private CouponCode couponCode;
	
	private String deliveryCode;

	private List<String> promotionNames = new ArrayList<String>();

	private List<Coupon> coupons = new ArrayList<Coupon>();

	private List<OrderItem> orderItems = new ArrayList<OrderItem>();

	private Set<PaymentLog> paymentLogs = new HashSet<PaymentLog>();

	private Set<Payment> payments = new HashSet<Payment>();

	private Set<Refunds> refunds = new HashSet<Refunds>();

	private Set<Shipping> shippings = new HashSet<Shipping>();

	private Set<Returns> returns = new HashSet<Returns>();

	private Set<OrderLog> orderLogs = new HashSet<OrderLog>();

	//卖方
	private Supplier supplier ;
	
	//买方  可以为空
	private Supplier toSupplier;
	
	//主单  关联 可以为空
	private Order mainOrder;
	
	//客户关系
	
	
	/**
	 * 收货时间
	 */
	private Date reDate;
	
	private String reCode;

	/**
	 * 拒绝原因
	 */
	private String deniedReason ;


	private List<OrderFile> orderFiles = new ArrayList<>() ;


	private SupplyType supplyType ;

	//状态改变之前的状态 ， 可以通过OrderLog 进行查询，繁琐，先加个字段
	private Status beforeStatus ;

	private ChildMember childMember ;
	//订单详情操作日志
	private List<OrderItemLog> orderItemLogs = new ArrayList<>() ;
	// TODO: 2017/5/26 对于商品修改记录，可以通过orderItemLogs 进行查询，但这样可能影响性能

	//后台修改的次数
	private Integer itemAdminUpdate ;
	//前台修改次数
	private Integer itemCustomerUpdate ;
	
	//备注信息
	private Set<OrderRemarks> orderRemarks = new HashSet<>();
	
	private Set<OrderNewsPush> orderNewsPushs;
	
	private SupplyNeed supplyNeed;
	
	private SupplierSupplier supplierSupplier;
	
	//客户关系
	private Need need;

	//2018-3-12 新增  店铺
	Shop shop;

	//2018-3-12 新增  供应类型
	SupplierType supplierType;
	
	public enum SharingStatus{
		noshare, //未分享
		share,	 //已分享
		end		 //已终结
	}
	
	//订单分享状态
	private SharingStatus sharingStatus;
	
	private List<OrderRelation> orderRelations = new ArrayList<OrderRelation>();
	
	private List<StorageForm> storageForms = new ArrayList<>();
	
	private List<StorageFormLog> storageFormLogs = new ArrayList<>();
	
	
	private String paySn;
	private String prepay_id;
	
	
	//分销利润
	private ChildMember done;
	private BigDecimal done_score;
	private ChildMember dtwo;
	private BigDecimal dtwo_score;
	private ChildMember dthree;
	private BigDecimal dthree_score;
	
	
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = true, updatable = true)
	public SupplierSupplier getSupplierSupplier() {
		return supplierSupplier;
	}
	
	public void setSupplierSupplier(SupplierSupplier supplierSupplier) {
		this.supplierSupplier = supplierSupplier;
	}
	

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = true, updatable = true)
	public SupplyNeed getSupplyNeed() {
		return supplyNeed;
	}


	public void setSupplyNeed(SupplyNeed supplyNeed) {
		this.supplyNeed = supplyNeed;
	}


	@Column(nullable = false, updatable = false, unique = true)
	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	@Column(nullable = false)
	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	@Column(nullable = false)
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	@Column(nullable = false, updatable = true, precision = 21, scale = 6)
	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	@Column(nullable = false, precision = 21, scale = 6)
	public BigDecimal getFee() {
		return fee;
	}

	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}

	@NotNull(groups = Delivery.class)
	@Min(0)
	@Digits(integer = 12, fraction = 3)
	@Column(nullable = false, precision = 21, scale = 6)
	public BigDecimal getFreight() {
		return freight;
	}

	public void setFreight(BigDecimal freight) {
		this.freight = freight;
	}

	@Min(0)
	@Digits(integer = 12, fraction = 3)
	@Column(nullable = false, precision = 21, scale = 6)
	public BigDecimal getTax() {
		return tax;
	}

	public void setTax(BigDecimal tax) {
		this.tax = tax;
	}

	@Column(nullable = false, updatable = false, precision = 21, scale = 6)
	public BigDecimal getPromotionDiscount() {
		return promotionDiscount;
	}

	public void setPromotionDiscount(BigDecimal promotionDiscount) {
		this.promotionDiscount = promotionDiscount;
	}

	@Column(nullable = false, updatable = false, precision = 21, scale = 6)
	public BigDecimal getCouponDiscount() {
		return couponDiscount;
	}

	public void setCouponDiscount(BigDecimal couponDiscount) {
		this.couponDiscount = couponDiscount;
	}

	@NotNull
	@Digits(integer = 12, fraction = 3)
	@Column(nullable = false, precision = 21, scale = 6)
	public BigDecimal getOffsetAmount() {
		return offsetAmount;
	}

	public void setOffsetAmount(BigDecimal offsetAmount) {
		this.offsetAmount = offsetAmount;
	}

	@Column(nullable = false, precision = 21, scale = 6)
	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	@Column(nullable = false, precision = 21, scale = 6)
	public BigDecimal getAmountPaid() {
		return amountPaid;
	}

	public void setAmountPaid(BigDecimal amountPaid) {
		this.amountPaid = amountPaid;
	}

	@Column(nullable = false, precision = 21, scale = 6)
	public BigDecimal getRefundAmount() {
		return refundAmount;
	}

	public void setRefundAmount(BigDecimal refundAmount) {
		this.refundAmount = refundAmount;
	}

	@Min(0)
	@Column(nullable = false)
	public Long getRewardPoint() {
		return rewardPoint;
	}

	public void setRewardPoint(Long rewardPoint) {
		this.rewardPoint = rewardPoint;
	}

	@Column(nullable = false, updatable = false)
	public Long getExchangePoint() {
		return exchangePoint;
	}

	public void setExchangePoint(Long exchangePoint) {
		this.exchangePoint = exchangePoint;
	}

	@Column(nullable = true, updatable = true)
	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	@Column(nullable = false, updatable = true)
	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	@Column(nullable = false)
	public Integer getShippedQuantity() {
		return shippedQuantity;
	}

	public void setShippedQuantity(Integer shippedQuantity) {
		this.shippedQuantity = shippedQuantity;
	}

	@Column(nullable = false)
	public Integer getReturnedQuantity() {
		return returnedQuantity;
	}

	public void setReturnedQuantity(Integer returnedQuantity) {
		this.returnedQuantity = returnedQuantity;
	}

	@NotEmpty(groups = Delivery.class)
	@Length(max = 200)
	public String getConsignee() {
		return consignee;
	}

	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	@NotEmpty(groups = Delivery.class)
	@Length(max = 200)
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@NotEmpty(groups = Delivery.class)
	@Length(max = 200)
	@Pattern(regexp = "^\\d{6}$")
	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	@NotEmpty(groups = Delivery.class)
	@Length(max = 200)
	@Pattern(regexp = "^\\d{3,4}-?\\d{7,9}$")
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Length(max = 200)
	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Date getExpire() {
		return expire;
	}

	public void setExpire(Date expire) {
		this.expire = expire;
	}

	@Column(nullable = false)
	public Boolean getIsUseCouponCode() {
		return isUseCouponCode;
	}

	public void setIsUseCouponCode(Boolean isUseCouponCode) {
		this.isUseCouponCode = isUseCouponCode;
	}
	
	/*@Length(max = 6)
	@Column(nullable = false)*/
	@Transient
	public String getDeliveryCode() {
		return deliveryCode;
	}

	public void setDeliveryCode(String deliveryCode) {
		this.deliveryCode = deliveryCode;
	}
	
	@Column(nullable = false)
	public Boolean getIsExchangePoint() {
		return isExchangePoint;
	}

	public void setIsExchangePoint(Boolean isExchangePoint) {
		this.isExchangePoint = isExchangePoint;
	}

	@Column(nullable = false)
	public Boolean getIsAllocatedStock() {
		return isAllocatedStock;
	}

	public void setIsAllocatedStock(Boolean isAllocatedStock) {
		this.isAllocatedStock = isAllocatedStock;
	}

	public String getPaymentMethodName() {
		return paymentMethodName;
	}

	public void setPaymentMethodName(String paymentMethodName) {
		this.paymentMethodName = paymentMethodName;
	}

	public PaymentMethod.Type getPaymentMethodType() {
		return paymentMethodType;
	}

	public void setPaymentMethodType(PaymentMethod.Type paymentMethodType) {
		this.paymentMethodType = paymentMethodType;
	}

	public String getShippingMethodName() {
		return shippingMethodName;
	}

	public void setShippingMethodName(String shippingMethodName) {
		this.shippingMethodName = shippingMethodName;
	}

	public String getLockKey() {
		return lockKey;
	}

	public void setLockKey(String lockKey) {
		this.lockKey = lockKey;
	}

	public Date getLockExpire() {
		return lockExpire;
	}

	public void setLockExpire(Date lockExpire) {
		this.lockExpire = lockExpire;
	}

	public Date getCompleteDate() {
		return completeDate;
	}

	public void setCompleteDate(Date completeDate) {
		this.completeDate = completeDate;
	}

	@Valid
	@Embedded
	public Invoice getInvoice() {
		return invoice;
	}

	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}

	@NotNull(groups = Delivery.class)
	@ManyToOne(fetch = FetchType.LAZY)
	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public ShippingMethod getShippingMethod() {
		return shippingMethod;
	}

	public void setShippingMethod(ShippingMethod shippingMethod) {
		this.shippingMethod = shippingMethod;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, updatable = false)
	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, updatable = false)
	public Member getBuyMember() {
		return buyMember;
	}

	public void setBuyMember(Member buyMember) {
		this.buyMember = buyMember;
	}

	@OneToOne(fetch = FetchType.LAZY)
	public CouponCode getCouponCode() {
		return couponCode;
	}

	public void setCouponCode(CouponCode couponCode) {
		this.couponCode = couponCode;
	}

	@Column(updatable = false, length = 4000)
	@Convert(converter = PromotionNameConverter.class)
	public List<String> getPromotionNames() {
		return promotionNames;
	}

	public void setPromotionNames(List<String> promotionNames) {
		this.promotionNames = promotionNames;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "xx_order_coupon")
	public List<Coupon> getCoupons() {
		return coupons;
	}

	public void setCoupons(List<Coupon> coupons) {
		this.coupons = coupons;
	}

	@OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@OrderBy("type asc")
	public List<OrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	@OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@OrderBy("createDate asc")
	public Set<PaymentLog> getPaymentLogs() {
		return paymentLogs;
	}

	public void setPaymentLogs(Set<PaymentLog> paymentLogs) {
		this.paymentLogs = paymentLogs;
	}

	@OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@OrderBy("createDate asc")
	public Set<Payment> getPayments() {
		return payments;
	}

	public void setPayments(Set<Payment> payments) {
		this.payments = payments;
	}

	@OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@OrderBy("createDate asc")
	public Set<Refunds> getRefunds() {
		return refunds;
	}

	public void setRefunds(Set<Refunds> refunds) {
		this.refunds = refunds;
	}

	@OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@OrderBy("createDate asc")
	public Set<Shipping> getShippings() {
		return shippings;
	}

	public void setShippings(Set<Shipping> shippings) {
		this.shippings = shippings;
	}

	@OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@OrderBy("createDate asc")
	public Set<Returns> getReturns() {
		return returns;
	}

	public void setReturns(Set<Returns> returns) {
		this.returns = returns;
	}

	@OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@OrderBy("createDate asc")
	public Set<OrderLog> getOrderLogs() {
		return orderLogs;
	}

	public void setOrderLogs(Set<OrderLog> orderLogs) {
		this.orderLogs = orderLogs;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, updatable = true ,foreignKey = @ForeignKey(name = "null"))
	public ChildMember getDone() {
		return done;
	}

	public void setDone(ChildMember done) {
		this.done = done;
	}

	public BigDecimal getDone_score() {
		return done_score;
	}

	public void setDone_score(BigDecimal done_score) {
		this.done_score = done_score;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, updatable = true ,foreignKey = @ForeignKey(name = "null"))
	public ChildMember getDtwo() {
		return dtwo;
	}

	public void setDtwo(ChildMember dtwo) {
		this.dtwo = dtwo;
	}

	public BigDecimal getDtwo_score() {
		return dtwo_score;
	}

	public void setDtwo_score(BigDecimal dtwo_score) {
		this.dtwo_score = dtwo_score;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, updatable = true ,foreignKey = @ForeignKey(name = "null"))
	public ChildMember getDthree() {
		return dthree;
	}

	public void setDthree(ChildMember dthree) {
		this.dthree = dthree;
	}

	public BigDecimal getDthree_score() {
		return dthree_score;
	}

	public void setDthree_score(BigDecimal dthree_score) {
		this.dthree_score = dthree_score;
	}

	@Transient
	public boolean getIsDelivery() {
		return CollectionUtils.exists(getOrderItems(), new Predicate() {
			@Override
			public boolean evaluate(Object object) {
				OrderItem orderItem = (OrderItem) object;
				return orderItem != null && BooleanUtils.isTrue(orderItem.getIsDelivery());
			}
		});
	}

	@Transient
	public BigDecimal getAmountPayable() {
		if (!hasExpired() && !Status.completed.equals(getStatus()) 
				&& !Status.failed.equals(getStatus()) 
				&& !Status.canceled.equals(getStatus()) && !Status.denied.equals(getStatus())) {
			BigDecimal amountPayable = getAmount().subtract(getAmountPaid());
			return amountPayable.compareTo(BigDecimal.ZERO) >= 0 ? amountPayable : BigDecimal.ZERO;
		}
		return BigDecimal.ZERO;
	}

	@Transient
	public BigDecimal getAmountReceivable() {
		if (!hasExpired() && PaymentMethod.Type.cashOnDelivery.equals(getPaymentMethodType()) && !Status.completed.equals(getStatus()) && !Status.failed.equals(getStatus()) && !Status.canceled.equals(getStatus()) && !Status.denied.equals(getStatus())) {
			BigDecimal amountReceivable = getAmount().subtract(getAmountPaid());
			return amountReceivable.compareTo(BigDecimal.ZERO) >= 0 ? amountReceivable : BigDecimal.ZERO;
		}
		return BigDecimal.ZERO;
	}

	@Transient
	public BigDecimal getRefundableAmount() {
		if (hasExpired() || Status.failed.equals(getStatus()) || Status.canceled.equals(getStatus()) || Status.denied.equals(getStatus())) {
			BigDecimal refundableAmount = getAmountPaid();
			return refundableAmount.compareTo(BigDecimal.ZERO) >= 0 ? refundableAmount : BigDecimal.ZERO;
		}
		if (Status.completed.equals(getStatus())) {
			BigDecimal refundableAmount = getAmountPaid().subtract(getAmount());
			return refundableAmount.compareTo(BigDecimal.ZERO) >= 0 ? refundableAmount : BigDecimal.ZERO;
		}
		return BigDecimal.ZERO;
	}

	@Transient
	public int getShippableQuantity() {
		if (!hasExpired() && (Status.pendingShipment.equals(getStatus()) || Status.inShipment.equals(getStatus()))) {
			int shippableQuantity = getQuantity() - getShippedQuantity();
			return shippableQuantity >= 0 ? shippableQuantity : 0;
		}
		return 0;
	}

	@Transient
	public int getReturnableQuantity() {
		if (!hasExpired() && Status.failed.equals(getStatus())) {
			int returnableQuantity = getShippedQuantity() - getReturnedQuantity();
			return returnableQuantity >= 0 ? returnableQuantity : 0;
		}
		return 0;
	}

	@Transient
	public boolean hasExpired() {
		return getExpire() != null && !getExpire().after(new Date());
	}

	@Transient
	public OrderItem getOrderItem(String sn) {
		if (StringUtils.isEmpty(sn) || CollectionUtils.isEmpty(getOrderItems())) {
			return null;
		}
		for (OrderItem orderItem : getOrderItems()) {
			if (orderItem != null && StringUtils.equalsIgnoreCase(orderItem.getSn(), sn)) {
				return orderItem;
			}
		}
		return null;
	}

	@PrePersist
	public void prePersist() {
		if (getArea() != null) {
			setAreaName(getArea().getFullName());
		}
		if (getPaymentMethod() != null) {
			setPaymentMethodName(getPaymentMethod().getName());
			setPaymentMethodType(getPaymentMethod().getType());
		}
		if (getShippingMethod() != null) {
			setShippingMethodName(getShippingMethod().getName());
		}
		if(null == this.getItemAdminUpdate()){
			this.setItemAdminUpdate(0);
		}
		if(null == this.getItemCustomerUpdate()){
			this.setItemCustomerUpdate(0);
		}
	}

	@PreUpdate
	public void preUpdate() {
		if (getArea() != null) {
			setAreaName(getArea().getFullName());
		}
		if (getPaymentMethod() != null) {
			setPaymentMethodName(getPaymentMethod().getName());
			setPaymentMethodType(getPaymentMethod().getType());
		}
		if (getShippingMethod() != null) {
			setShippingMethodName(getShippingMethod().getName());
		}
	}

	@Converter
	public static class PromotionNameConverter extends BaseAttributeConverter<List<String>> implements AttributeConverter<Object, String> {
	}

	@Converter
	public static class OrderFilesConverter extends BaseAttributeConverter<List<OrderFile>> implements AttributeConverter<Object, String> {
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	public Date getReDate() {
		return reDate;
	}

	public void setReDate(Date reDate) {
		this.reDate = reDate;
	}
	
	public String getReCode() {
		return reCode;
	}

	public void setReCode(String reCode) {
		this.reCode = reCode;
	}
	

	/**
	 * 把Integer订单状态转成Order.Status类型的订单状态
	 * @param status
	 * @return
	 */
	public static Order.Status convertIntegerToOrderStatus(Integer status){
		Order.Status orderStatus = null;
		if(status!=null){
			switch(status)
			{
			   case 0 :  
				   orderStatus = Order.Status.pendingPayment;
				   break;
			   case 1 :  
				   orderStatus = Order.Status.pendingReview;
				   break;
			   case 2 :
				   orderStatus = Order.Status.pendingShipment;
				   break;
			   case 3 :
				   orderStatus = Order.Status.shipped;
				   break;
			   case 4 :
				   orderStatus = Order.Status.received;
				   break;
			   case 5 :
				   orderStatus = Order.Status.completed;
				   break;
			   case 6 :
				   orderStatus = Order.Status.failed;
				   break;
			   case 7 :
				   orderStatus = Order.Status.canceled;
				   break;
			   case 8 :
				   orderStatus = Order.Status.denied;
				   break;
			   default:
				   orderStatus = null;
				   break;			   
			}
		}
		return orderStatus;		
	}

	/**
	 * 提供给消息通知使用的状态
	 */
	public enum OrderStatus {
		//创建
		create,
		//取消
		canceled,
		//已发货
		shipped,
		//完成
		completed,
		//拒绝(订单审核拒绝)
		denied,
		//审核通过
		passed,
		//用户申请取消
		applyCancel,
		//商品数量修改 用户修改
		updateItems,
		//作废发货
		cancelShipped,
		//申请取消通过
		applyCancel_passed,
		//申请取消不通过
		applyCancel_denied,
		//后台用户进行订单修改
		admin_updateItems;


	}

	@Column(length = 4000)
	@Convert(converter = Order.OrderFilesConverter.class)
	public List<OrderFile> getOrderFiles() {
		return orderFiles;
	}

	public void setOrderFiles(List<OrderFile> orderFiles) {
		this.orderFiles = orderFiles;
	}

	@Length(max = 200)
	public String getDeniedReason() {
		return deniedReason;
	}

	public void setDeniedReason(String deniedReason) {
		this.deniedReason = deniedReason;
	}

	public SupplyType getSupplyType() {
		return supplyType;
	}

	public void setSupplyType(SupplyType supplyType) {
		this.supplyType = supplyType;
	}

	public Status getBeforeStatus() {
		return beforeStatus;
	}

	public void setBeforeStatus(Status beforeStatus) {
		this.beforeStatus = beforeStatus;
	}

	//实收数量
	@Transient
	public Integer getRealProductQuantity(Long proId){
		Integer all = 0;
		for(Shipping shipping : this.getShippings()){
			for(ShippingItem item : shipping.getShippingItems()){
				if(item.getProduct().getId().compareTo(proId) == 0){
					Integer realQuantity = item.getRealQuantity() ;
					all+=null == realQuantity||!shipping.getStatus().equals(Shipping.Status.senderChecked)? 0:realQuantity;
				}
			}
		}
		return all ;
	}


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, updatable = false ,foreignKey = @ForeignKey(name = "null"))
	public ChildMember getChildMember() {
		return childMember;
	}

	public void setChildMember(ChildMember childMember) {
		this.childMember = childMember;
	}

	@OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@OrderBy(value = "id desc")
	public List<OrderItemLog> getOrderItemLogs() {
		return orderItemLogs;
	}

	public void setOrderItemLogs(List<OrderItemLog> orderItemLogs) {
		this.orderItemLogs = orderItemLogs;
	}
	
	@Column(columnDefinition = "TINYINT default 0")
	public Integer getItemAdminUpdate() {
		return itemAdminUpdate;
	}

	public void setItemAdminUpdate(Integer itemAdminUpdate) {
		this.itemAdminUpdate = itemAdminUpdate;
	}
	@Column(columnDefinition = "TINYINT default 0")
	public Integer getItemCustomerUpdate() {
		return itemCustomerUpdate;
	}

	public void setItemCustomerUpdate(Integer itemCustomerUpdate) {
		this.itemCustomerUpdate = itemCustomerUpdate;
	}

	@OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@OrderBy("id desc")
	public Set<OrderRemarks> getOrderRemarks() {
		return orderRemarks;
	}

	public void setOrderRemarks(Set<OrderRemarks> orderRemarks) {
		this.orderRemarks = orderRemarks;
	}

	@OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	public Set<OrderNewsPush> getOrderNewsPushs() {
		return orderNewsPushs;
	}

	public void setOrderNewsPushs(Set<OrderNewsPush> orderNewsPushs) {
		this.orderNewsPushs = orderNewsPushs;
	}

	public BigDecimal getAmountToB() {
		return amountToB;
	}

	public void setAmountToB(BigDecimal amountToB) {
		this.amountToB = amountToB;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Supplier getToSupplier() {
		return toSupplier;
	}

	public void setToSupplier(Supplier toSupplier) {
		this.toSupplier = toSupplier;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Order getMainOrder() {
		return mainOrder;
	}

	public void setMainOrder(Order mainOrder) {
		this.mainOrder = mainOrder;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(updatable = false ,foreignKey = @ForeignKey(name = "null"))
	public Need getNeed() {
		return need;
	}

	public void setNeed(Need need) {
		this.need = need;
	}

	public BuyType getBuyType() {
		return buyType;
	}

	public void setBuyType(BuyType buyType) {
		this.buyType = buyType;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}

	public SupplierType getSupplierType() {
		return supplierType;
	}

	public void setSupplierType(SupplierType supplierType) {
		this.supplierType = supplierType;
	}

	public SharingStatus getSharingStatus() {
		return sharingStatus;
	}

	public void setSharingStatus(SharingStatus sharingStatus) {
		this.sharingStatus = sharingStatus;
	}

	public String getPrepay_id() {
		return prepay_id;
	}

	public void setPrepay_id(String prepay_id) {
		this.prepay_id = prepay_id;
	}

	@OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@OrderBy("createDate asc")
	public List<OrderRelation> getOrderRelations() {
		return orderRelations;
	}

	public void setOrderRelations(List<OrderRelation> orderRelations) {
		this.orderRelations = orderRelations;
	}
	
	@Transient
	public boolean participate(ChildMember childMember) {
		List<OrderRelation> orderRelations = getOrderRelations();
		if(orderRelations != null) {
			for(OrderRelation orderRelation : orderRelations) {
				if(orderRelation.getType().equals(OrderRelation.Type.participant)) {
					if(childMember != null && childMember.equals(orderRelation.getChildMember())) {
						return true;
					}
				}
			}
		}else {
			return false;
		}
		return false;
	}
	
	/**
	 * 3月30号    前端下单为员工名或超级管理员   后台代下单为登录账号    经过胡一柯确认过
	 * @return
	 */
	@Transient
	public OrderLog getCreateOrderLog(){
		Set<OrderLog> logs=this.getOrderLogs();
		for (OrderLog orderLog : logs) {
			if (orderLog.getType() == OrderLog.Type.create) {
				return orderLog;
			}
		}
		return null;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "order")
	public List<StorageForm> getStorageForms() {
		return storageForms;
	}

	public void setStorageForms(List<StorageForm> storageForms) {
		this.storageForms = storageForms;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "order")
	public List<StorageFormLog> getStorageFormLogs() {
		return storageFormLogs;
	}

	public void setStorageFormLogs(List<StorageFormLog> storageFormLogs) {
		this.storageFormLogs = storageFormLogs;
	}
	
	@Transient
	public List<StorageFormLog> getStorageFormLogsCreate(){
		
		List<StorageFormLog> storageLogs = new ArrayList<>();
		for (StorageFormLog storageFormLog : this.storageFormLogs) {
			if (storageFormLog.getRecord() == StorageFormLog.Record.create) {
				storageLogs.add(storageFormLog);
			}
		}
		
		return storageLogs;
	}
	
}
