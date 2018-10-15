/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;

import com.microBusiness.manage.BaseAttributeConverter;
import com.microBusiness.manage.Setting;
import com.microBusiness.manage.util.SystemUtils;
import com.microBusiness.manage.util.SystemUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * 商品SKU
 * @author suxiaozhen
 *
 */
@Entity
@Table(name = "xx_product")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_product")
public class Product extends BaseEntity<Long> {

	private static final long serialVersionUID = 8068481323404636090L;

	public interface General extends Default {

	}

	public interface Exchange extends Default {

	}

	public interface Gift extends Default {

	}

	private String sn;

	/**
	 * 零售价格
	 */
	private BigDecimal price;

	/**
	 * 成本价格
	 */
	private BigDecimal cost;

	/**
	 * 市场价格
	 */
	private BigDecimal marketPrice;
	
	/**
	 * 代理价格
	 */
	private BigDecimal proxyPrice;

	public BigDecimal getProxyPrice() {
		return proxyPrice;
	}

	public void setProxyPrice(BigDecimal proxyPrice) {
		this.proxyPrice = proxyPrice;
	}

	private Long rewardPoint;

	private Long exchangePoint;

	private Integer stock;

	private Integer allocatedStock;

	private Boolean isDefault;

	private Goods goods;

	private List<SpecificationValue> specificationValues = new ArrayList<SpecificationValue>();

	private Set<CartItem> cartItems = new HashSet<CartItem>();

	private Set<OrderItem> orderItems = new HashSet<OrderItem>();

	private Set<ShippingItem> shippingItems = new HashSet<ShippingItem>();

	private Set<ProductNotify> productNotifies = new HashSet<ProductNotify>();

	private Set<StockLog> stockLogs = new HashSet<StockLog>();

	private Set<Promotion> giftPromotions = new HashSet<Promotion>();

	//private Set<Need> needs ;

	private Set<NeedProduct> needProducts ;

	//最小起订量
	private Integer minOrderQuantity ;
	
	private BigDecimal supplyPrice;
	
	private Integer turnoverMinOrderQuantity ;
	
	private BigDecimal turnoverSupplyPrice;
	
	private Product source;
	
	//条形码
    private String barCode ;
    
  
    
    /**
     * 购买递增数量
     */
    private Integer addValue;
    
    //销量
    private Long sales;
    

	public Long getSales() {
		return sales;
	}

	public void setSales(Long sales) {
		this.sales = sales;
	}

	public Integer getAddValue() {
		return addValue;
	}

	public void setAddValue(Integer addValue) {
		this.addValue = addValue;
	}

	@Column(nullable = false, updatable = false, unique = false)
	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	@NotNull(groups = General.class)
	@Min(0)
	@Digits(integer = 12, fraction = 3)
	@Column(nullable = false, precision = 21, scale = 6)
	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	@Min(0)
	@Digits(integer = 12, fraction = 3)
	@Column(precision = 21, scale = 6)
	public BigDecimal getCost() {
		return cost;
	}

	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}

	@Min(0)
	@Digits(integer = 12, fraction = 3)
	@Column(nullable = false, precision = 21, scale = 6)
	public BigDecimal getMarketPrice() {
		return marketPrice;
	}

	public void setMarketPrice(BigDecimal marketPrice) {
		this.marketPrice = marketPrice;
	}

	@Min(0)
	@Column(nullable = false)
	public Long getRewardPoint() {
		return rewardPoint;
	}

	public void setRewardPoint(Long rewardPoint) {
		this.rewardPoint = rewardPoint;
	}

	@NotNull(groups = Exchange.class)
	@Min(0)
	@Column(nullable = false)
	public Long getExchangePoint() {
		return exchangePoint;
	}

	public void setExchangePoint(Long exchangePoint) {
		this.exchangePoint = exchangePoint;
	}

	@NotNull(groups = Save.class)
	@Min(0)
	@Column(nullable = false)
	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	@Column(nullable = false)
	public Integer getAllocatedStock() {
		return allocatedStock;
	}

	public void setAllocatedStock(Integer allocatedStock) {
		this.allocatedStock = allocatedStock;
	}

	@NotNull
	@Column(nullable = false)
	public Boolean getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(nullable = false, updatable = false)
	public Goods getGoods() {
		return goods;
	}

	public void setGoods(Goods goods) {
		this.goods = goods;
	}

	@Valid
	@Column(length = 4000)
	@Convert(converter = SpecificationValueConverter.class)
	public List<SpecificationValue> getSpecificationValues() {
		return specificationValues;
	}

	public void setSpecificationValues(List<SpecificationValue> specificationValues) {
		this.specificationValues = specificationValues;
	}

	@OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	public Set<CartItem> getCartItems() {
		return cartItems;
	}

	public void setCartItems(Set<CartItem> cartItems) {
		this.cartItems = cartItems;
	}

	@OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
	public Set<OrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(Set<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	@OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
	public Set<ShippingItem> getShippingItems() {
		return shippingItems;
	}

	public void setShippingItems(Set<ShippingItem> shippingItems) {
		this.shippingItems = shippingItems;
	}

	@OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	public Set<ProductNotify> getProductNotifies() {
		return productNotifies;
	}

	public void setProductNotifies(Set<ProductNotify> productNotifies) {
		this.productNotifies = productNotifies;
	}

	@OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	public Set<StockLog> getStockLogs() {
		return stockLogs;
	}

	public void setStockLogs(Set<StockLog> stockLogs) {
		this.stockLogs = stockLogs;
	}

	@ManyToMany(mappedBy = "gifts", fetch = FetchType.LAZY)
	public Set<Promotion> getGiftPromotions() {
		return giftPromotions;
	}

	public void setGiftPromotions(Set<Promotion> giftPromotions) {
		this.giftPromotions = giftPromotions;
	}

	@Transient
	public String getName() {
		return getGoods() != null ? getGoods().getName() : null;
	}

	@Transient
	public Goods.Type getType() {
		return getGoods() != null ? getGoods().getType() : null;
	}

	@Transient
	public String getImage() {
		return getGoods() != null ? getGoods().getImage() : null;
	}

	@Transient
	public String getUnit() {
		return getGoods() != null ? getGoods().getUnit().toString() : null;
	}

	@Transient
	public Integer getWeight() {
		return getGoods() != null ? getGoods().getWeight() : null;
	}

	@Transient
	public boolean getIsMarketable() {
		return getGoods() != null && BooleanUtils.isTrue(getGoods().getIsMarketable());
	}

	@Transient
	public boolean getIsList() {
		return getGoods() != null && BooleanUtils.isTrue(getGoods().getIsList());
	}

	@Transient
	public boolean getIsTop() {
		return getGoods() != null && BooleanUtils.isTrue(getGoods().getIsTop());
	}

	@Transient
	public boolean getIsDelivery() {
		return getGoods() != null && BooleanUtils.isTrue(getGoods().getIsDelivery());
	}

	@Transient
	public String getPath() {
		return getGoods() != null ? getGoods().getPath() : null;
	}

	@Transient
	public String getUrl() {
		return getGoods() != null ? getGoods().getUrl() : null;
	}

	@Transient
	public String getThumbnail() {
		return getGoods() != null ? getGoods().getThumbnail() : null;
	}

	@Transient
	public int getAvailableStock() {
		int availableStock = getStock() - getAllocatedStock();
		return availableStock >= 0 ? availableStock : 0;
	}

	@Transient
	public boolean getIsStockAlert() {
		Setting setting = SystemUtils.getSetting();
		return setting.getStockAlertCount() != null && getAvailableStock() <= setting.getStockAlertCount();
	}

	@Transient
	public boolean getIsOutOfStock() {
		return getAvailableStock() <= 0;
	}

	@Transient
	public List<Integer> getSpecificationValueIds() {
		List<Integer> specificationValueIds = new ArrayList<Integer>();
		if (CollectionUtils.isNotEmpty(getSpecificationValues())) {
			for (SpecificationValue specificationValue : getSpecificationValues()) {
				specificationValueIds.add(specificationValue.getId());
			}
		}
		return specificationValueIds;
	}

	@Transient
	public List<String> getSpecifications() {
		List<String> specifications = new ArrayList<String>();
		if (CollectionUtils.isNotEmpty(getSpecificationValues())) {
			for (SpecificationValue specificationValue : getSpecificationValues()) {
				specifications.add(specificationValue.getValue());
			}
		}
		return specifications;
	}

	@Transient
	public Set<Promotion> getValidPromotions() {
		return getGoods() != null ? getGoods().getValidPromotions() : Collections.<Promotion> emptySet();
	}

	@Transient
	public boolean hasSpecification() {
		return CollectionUtils.isNotEmpty(getSpecificationValues());
	}

	@Transient
	public boolean isValid(Promotion promotion) {
		return getGoods() != null ? getGoods().isValid(promotion) : false;
	}

	@PreRemove
	public void preRemove() {
//		Set<OrderItem> orderItems = getOrderItems();
//		if (orderItems != null) {
//			for (OrderItem orderItem : orderItems) {
//				orderItem.setProduct(null);
//			}
//		}
//		Set<ShippingItem> shippingItems = getShippingItems();
//		if (shippingItems != null) {
//			for (ShippingItem shippingItem : getShippingItems()) {
//				shippingItem.setProduct(null);
//			}
//		}
//		Set<Promotion> giftPromotions = getGiftPromotions();
//		if (giftPromotions != null) {
//			for (Promotion giftPromotion : giftPromotions) {
//				giftPromotion.getGifts().remove(this);
//			}
//		}
	}

	@Converter
	public static class SpecificationValueConverter extends BaseAttributeConverter<List<SpecificationValue>> implements AttributeConverter<Object, String> {
	}

	/*@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "t_need_product" ,foreignKey = @ForeignKey(name = "null"))
	public Set<Need> getNeeds() {
		return needs;
	}

	public void setNeeds(Set<Need> needs) {
		this.needs = needs;
	}*/

	@OneToMany(mappedBy = "products")
	public Set<NeedProduct> getNeedProducts() {
		return needProducts;
	}

	public void setNeedProducts(Set<NeedProduct> needProducts) {
		this.needProducts = needProducts;
	}

	public Integer getMinOrderQuantity() {
		return minOrderQuantity;
	}

	public void setMinOrderQuantity(Integer minOrderQuantity) {
		this.minOrderQuantity = minOrderQuantity;
	}
	
	@Transient
	public BigDecimal getSupplyPrice() {
		return supplyPrice;
	}

	public void setSupplyPrice(BigDecimal supplyPrice) {
		this.supplyPrice = supplyPrice;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Product getSource() {
		return source;
	}

	public void setSource(Product source) {
		this.source = source;
	}

	public Integer getTurnoverMinOrderQuantity() {
		return turnoverMinOrderQuantity;
	}

	public void setTurnoverMinOrderQuantity(Integer turnoverMinOrderQuantity) {
		this.turnoverMinOrderQuantity = turnoverMinOrderQuantity;
	}

	public BigDecimal getTurnoverSupplyPrice() {
		return turnoverSupplyPrice;
	}

	public void setTurnoverSupplyPrice(BigDecimal turnoverSupplyPrice) {
		this.turnoverSupplyPrice = turnoverSupplyPrice;
	}

	@Column(length = 50)
	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}
	
}
