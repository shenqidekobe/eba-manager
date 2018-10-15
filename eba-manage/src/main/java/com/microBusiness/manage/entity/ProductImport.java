package com.microBusiness.manage.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
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

import org.apache.commons.collections.CollectionUtils;

@Entity
@Table(name = "xx_product_import")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_product_import")
public class ProductImport extends BaseEntity<Long> {

	private static final long serialVersionUID = -3163100483538098657L;

	public interface General extends Default {

	}

	public interface Exchange extends Default {

	}

	public interface Gift extends Default {

	}

	private String sn;

	private BigDecimal price;

	private String barCode;
	
	private BigDecimal cost;

	private BigDecimal marketPrice;

	private Long rewardPoint;

	private Long exchangePoint;

	private Integer stock;

	private Integer allocatedStock;

	private Boolean isDefault;

	private GoodsImportInfo goodsImportInfo;

	private List<SpecificationValue> specificationValues = new ArrayList<SpecificationValue>();

	private Set<StockLog> stockLogs = new HashSet<StockLog>();

	private Set<Promotion> giftPromotions = new HashSet<Promotion>();

	private Integer minOrderQuantity ;
	
	private BigDecimal supplyPrice;
	
	private Integer turnoverMinOrderQuantity ;
	
	private BigDecimal turnoverSupplyPrice;
	
	private ProductImport source;

	@Transient
	public boolean hasSpecification() {
		return CollectionUtils.isNotEmpty(getSpecificationValues());
	}
	
	@Column(nullable = false, updatable = false, unique = true)
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
	public GoodsImportInfo getGoodsImportInfo() {
		return goodsImportInfo;
	}

	public void setGoodsImportInfo(GoodsImportInfo goodsImportInfo) {
		this.goodsImportInfo = goodsImportInfo;
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

	@Transient
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
	public ProductImport getSource() {
		return source;
	}

	public void setSource(ProductImport source) {
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

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

}
