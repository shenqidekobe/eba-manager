/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.entity;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.AttributeConverter;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Converter;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.microBusiness.manage.BaseAttributeConverter;
import com.microBusiness.manage.BigDecimalNumericFieldBridge;
import com.microBusiness.manage.Setting;
import com.microBusiness.manage.TemplateConfig;
import com.microBusiness.manage.util.SystemUtils;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.NumericField;
import org.hibernate.search.annotations.Store;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

@Indexed
@Entity
@Table(name = "xx_goods")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_goods")
public class Goods extends BaseEntity<Long> {

	private static final long serialVersionUID = -8781963456943173684L;

	public static final String HITS_CACHE_NAME = "goodsHits";

	public static final int ATTRIBUTE_VALUE_PROPERTY_COUNT = 20;

	public static final String ATTRIBUTE_VALUE_PROPERTY_NAME_PREFIX = "attributeValue";

	public enum Type {

		general,

		exchange,

		gift,
		//分销类型
		distribution
	}

	public enum GenerateMethod {

		none,

		eager,

		lazy
	}

	public enum OrderType {

		topDesc,

		priceAsc,

		priceDesc,

		salesDesc,

		scoreDesc,

		dateDesc
	}

	public enum RankingType {

		score,

		scoreCount,

		weekHits,

		monthHits,

		hits,

		weekSales,

		monthSales,

		sales
	}
	public enum StorageConditions{
		roomTemperature,//常温
		refrigeration,//冷藏
		frozen			//冰冻
	}
	public enum WeightUnit{
		g,
		kg
	}
	public enum VolumeUnit{
		cubicCentimeters,//立方厘米
		cubicMeter   //立方米
	}
	public enum Nature{
		gas,//气体
		solid,//固体
		liquid//液体
	}
	public enum Unit{
		box,//箱
		bottle,//瓶
		bag,//袋
		frame,	//盒
		pack, //包
		one, //个
		pieces, //件
		barrel, //桶
		copies, //份
		jin,  //斤
		g, //克
		tank, //罐
		rise, //升
		bar, //条
		only, //只
		volume, //卷
		listen, //听
	}

	private String sn;

	private String name;

	private String caption;

	private Type type;

	/**
	 * 零售价格
	 */
	private BigDecimal price;

	/**
	 * 市场显示价格
	 */
	private BigDecimal marketPrice;
	
	/**
	 * 代理价格
	 */
	private BigDecimal proxyPrice;
	

	private String image;
	
	private List<String> images;

	private Unit unit;

	private Integer weight;

	private Boolean isMarketable;

	private Boolean isList;

	private Boolean isTop;

	private Boolean isDelivery;

	private String introduction;

	private String memo;

	private String keyword;

	private String seoTitle;

	private String seoKeywords;

	private String seoDescription;

	private Float score;

	private Long totalScore;

	private Long scoreCount;

	private Long weekHits;

	private Long monthHits;

	//点击量
	private Long hits;

	private Long weekSales;

	public BigDecimal getProxyPrice() {
		return proxyPrice;
	}

	public void setProxyPrice(BigDecimal proxyPrice) {
		this.proxyPrice = proxyPrice;
	}

	private Long monthSales;

	private Long sales;
	
	private Long shelfLife;
	
	private StorageConditions storageConditions;
	
	private Long packagesNum;
	
	private WeightUnit weightUnit;
	
	private BigDecimal volume;
	
	private VolumeUnit VolumeUnit;
	
	private Nature nature;

	private Date weekHitsDate;

	private Date monthHitsDate;

	private Date weekSalesDate;

	private Date monthSalesDate;

	private GenerateMethod generateMethod;

	private String attributeValue0;

	private String attributeValue1;

	private String attributeValue2;

	private String attributeValue3;

	private String attributeValue4;

	private String attributeValue5;

	private String attributeValue6;

	private String attributeValue7;

	private String attributeValue8;

	private String attributeValue9;

	private String attributeValue10;

	private String attributeValue11;

	private String attributeValue12;

	private String attributeValue13;

	private String attributeValue14;

	private String attributeValue15;

	private String attributeValue16;

	private String attributeValue17;

	private String attributeValue18;

	private String attributeValue19;

	private ProductCategory productCategory;

	private Brand brand;

	private List<ProductImage> productImages = new ArrayList<ProductImage>();

	private List<ParameterValue> parameterValues = new ArrayList<ParameterValue>();

	private List<SpecificationItem> specificationItems = new ArrayList<SpecificationItem>();

	private Set<Promotion> promotions = new HashSet<Promotion>();

	private Set<Tag> tags = new HashSet<Tag>();

	private Set<Review> reviews = new HashSet<Review>();

	private Set<Consultation> consultations = new HashSet<Consultation>();

	private Set<Member> favoriteMembers = new HashSet<Member>();

	private Set<Product> products = new HashSet<Product>();

	private Supplier supplier ;
	
	private Set<SupplierProduct> supplierProducts=new HashSet<>();
	
	private Set<NeedProduct> needProducts=new HashSet<>();
	
	//来源
	private Goods source;
	//供应关系
	private SupplierSupplier supplierSupplier;
	
	public enum Label {
		//新品
		newProducts,
		//热卖
		selling,
		//促销
		promotions,
		//特惠
		specialOffer,
		//人气
		popularity,
		//爆款
		explosions
	}
	
	//标签
	private List<Label> labels;
	//类型（本地/平台）
	private Types types;
	//条形码
	private String barCode;
	
	//是否是广告关联商品
	private Boolean isAd;
	
	//是否是视频广告关联商品
	private Boolean isVideoAd;
	
	//是否是主推品
	private Boolean isMainSell;
	
	//是否是活动1
	private Boolean isActivity1;
	
	private Boolean isActivity2;
	
	private Boolean isActivity3;
	
	private Boolean isActivity4;
	//是否是代理商品
	private Boolean isProxy;
	
	//是否属于会员商品
	private Boolean is2Member;
	
	//一级返利比率
	private Float rate1;
	//二级返利比率
	private Float rate2;
	//三级返利比率
	private Float rate3;
	
	public Boolean getIs2Member() {
		return is2Member;
	}

	public void setIs2Member(Boolean is2Member) {
		this.is2Member = is2Member;
	}

	public Float getRate1() {
		return rate1;
	}

	public void setRate1(Float rate1) {
		this.rate1 = rate1;
	}

	public Float getRate2() {
		return rate2;
	}

	public void setRate2(Float rate2) {
		this.rate2 = rate2;
	}

	public Float getRate3() {
		return rate3;
	}

	public void setRate3(Float rate3) {
		this.rate3 = rate3;
	}

	public Boolean getIsProxy() {
		return isProxy;
	}

	public void setIsProxy(Boolean isProxy) {
		this.isProxy = isProxy;
	}

	public Boolean getIsAd() {
		return isAd;
	}

	public void setIsAd(Boolean isAd) {
		this.isAd = isAd;
	}

	public Boolean getIsVideoAd() {
		return isVideoAd;
	}

	public void setIsVideoAd(Boolean isVideoAd) {
		this.isVideoAd = isVideoAd;
	}

	public Boolean getIsActivity1() {
		return isActivity1;
	}

	public void setIsActivity1(Boolean isActivity1) {
		this.isActivity1 = isActivity1;
	}

	public Boolean getIsActivity2() {
		return isActivity2;
	}

	public void setIsActivity2(Boolean isActivity2) {
		this.isActivity2 = isActivity2;
	}

	public Boolean getIsActivity3() {
		return isActivity3;
	}

	public void setIsActivity3(Boolean isActivity3) {
		this.isActivity3 = isActivity3;
	}

	public Boolean getIsActivity4() {
		return isActivity4;
	}

	public void setIsActivity4(Boolean isActivity4) {
		this.isActivity4 = isActivity4;
	}

	public Boolean getIsMainSell() {
		return isMainSell;
	}

	public void setIsMainSell(Boolean isMainSell) {
		this.isMainSell = isMainSell;
	}

	
	

	@Field(store = Store.YES, index = Index.YES, analyze = Analyze.NO)
	@Pattern(regexp = "^[0-9a-zA-Z_-]+$")
	@Length(max = 100)
	@Column(nullable = false, updatable = false, unique = false)
	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	@Field(store = Store.YES, index = Index.YES, analyze = Analyze.YES)
	@NotEmpty
	@Length(max = 200)
	@Column(nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Field(store = Store.YES, index = Index.NO, analyze = Analyze.NO)
	@Length(max = 200)
	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	@Field(store = Store.YES, index = Index.YES, analyze = Analyze.NO)
	@NotNull(groups = Save.class)
	@Column(nullable = false, updatable = false)
	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	@Field(store = Store.YES, index = Index.YES, analyze = Analyze.NO)
	@NumericField
	@FieldBridge(impl = BigDecimalNumericFieldBridge.class)
	@Column(nullable = false, precision = 21, scale = 6)
	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	@Field(store = Store.YES, index = Index.NO, analyze = Analyze.NO)
	@NumericField
	@FieldBridge(impl = BigDecimalNumericFieldBridge.class)
	@Column(nullable = false, precision = 21, scale = 6)
	public BigDecimal getMarketPrice() {
		return marketPrice;
	}

	public void setMarketPrice(BigDecimal marketPrice) {
		this.marketPrice = marketPrice;
	}

	@Field(store = Store.YES, index = Index.NO, analyze = Analyze.NO)
	@Length(max = 200)
	@Pattern(regexp = "^(?i)(http:\\/\\/|https:\\/\\/|\\/).*$")
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	@Field(store = Store.YES, index = Index.NO, analyze = Analyze.NO)
	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	@Field(store = Store.YES, index = Index.NO, analyze = Analyze.NO)
	@NumericField
	@Min(0)
	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}
	
	@Field(store = Store.YES, index = Index.NO, analyze = Analyze.NO)
	public Long getShelfLife() {
		return shelfLife;
	}

	public void setShelfLife(Long shelfLife) {
		this.shelfLife = shelfLife;
	}

	@Field(store = Store.YES, index = Index.NO, analyze = Analyze.NO)
	public StorageConditions getStorageConditions() {
		return storageConditions;
	}

	public void setStorageConditions(StorageConditions storageConditions) {
		this.storageConditions = storageConditions;
	}

	@Field(store = Store.YES, index = Index.NO, analyze = Analyze.NO)
	public Long getPackagesNum() {
		return packagesNum;
	}

	public void setPackagesNum(Long packagesNum) {
		this.packagesNum = packagesNum;
	}

	@Field(store = Store.YES, index = Index.NO, analyze = Analyze.NO)
	public WeightUnit getWeightUnit() {
		return weightUnit;
	}

	public void setWeightUnit(WeightUnit weightUnit) {
		this.weightUnit = weightUnit;
	}

	@Field(store = Store.YES, index = Index.NO, analyze = Analyze.NO)
	public BigDecimal getVolume() {
		return volume;
	}

	public void setVolume(BigDecimal volume) {
		this.volume = volume;
	}

	@Field(store = Store.YES, index = Index.NO, analyze = Analyze.NO)
	public VolumeUnit getVolumeUnit() {
		return VolumeUnit;
	}

	public void setVolumeUnit(VolumeUnit volumeUnit) {
		VolumeUnit = volumeUnit;
	}

	@Field(store = Store.YES, index = Index.NO, analyze = Analyze.NO)
	public Nature getNature() {
		return nature;
	}

	public void setNature(Nature nature) {
		this.nature = nature;
	}

	@Field(store = Store.YES, index = Index.YES, analyze = Analyze.NO)
	@NotNull
	@Column(nullable = false)
	public Boolean getIsMarketable() {
		return isMarketable;
	}

	public void setIsMarketable(Boolean isMarketable) {
		this.isMarketable = isMarketable;
	}

	@Field(store = Store.YES, index = Index.YES, analyze = Analyze.NO)
	@NotNull
	@Column(nullable = false)
	public Boolean getIsList() {
		return isList;
	}

	public void setIsList(Boolean isList) {
		this.isList = isList;
	}

	@Field(store = Store.YES, index = Index.YES, analyze = Analyze.NO)
	@NotNull
	@Column(nullable = false)
	public Boolean getIsTop() {
		return isTop;
	}

	public void setIsTop(Boolean isTop) {
		this.isTop = isTop;
	}

	@NotNull
	@Column(nullable = false)
	public Boolean getIsDelivery() {
		return isDelivery;
	}

	public void setIsDelivery(Boolean isDelivery) {
		this.isDelivery = isDelivery;
	}

	@Field(store = Store.YES, index = Index.YES, analyze = Analyze.YES)
	@Lob
	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	@Length(max = 200)
	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Field(store = Store.YES, index = Index.YES, analyze = Analyze.YES)
	@Length(max = 200)
	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		if (keyword != null) {
			keyword = keyword.replaceAll("[,\\s]*,[,\\s]*", ",").replaceAll("^,|,$", "");
		}
		this.keyword = keyword;
	}

	@Length(max = 200)
	public String getSeoTitle() {
		return seoTitle;
	}

	public void setSeoTitle(String seoTitle) {
		this.seoTitle = seoTitle;
	}

	@Length(max = 200)
	public String getSeoKeywords() {
		return seoKeywords;
	}

	public void setSeoKeywords(String seoKeywords) {
		if (seoKeywords != null) {
			seoKeywords = seoKeywords.replaceAll("[,\\s]*,[,\\s]*", ",").replaceAll("^,|,$", "");
		}
		this.seoKeywords = seoKeywords;
	}

	@Length(max = 200)
	public String getSeoDescription() {
		return seoDescription;
	}

	public void setSeoDescription(String seoDescription) {
		this.seoDescription = seoDescription;
	}

	@Field(store = Store.YES, index = Index.YES, analyze = Analyze.NO)
	@NumericField
	@Column(nullable = false, precision = 12, scale = 6)
	public Float getScore() {
		return score;
	}

	public void setScore(Float score) {
		this.score = score;
	}

	@Column(nullable = false)
	public Long getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(Long totalScore) {
		this.totalScore = totalScore;
	}

	@Field(store = Store.YES, index = Index.YES, analyze = Analyze.NO)
	@NumericField
	@Column(nullable = false)
	public Long getScoreCount() {
		return scoreCount;
	}

	public void setScoreCount(Long scoreCount) {
		this.scoreCount = scoreCount;
	}

	@Column(nullable = false)
	public Long getWeekHits() {
		return weekHits;
	}

	public void setWeekHits(Long weekHits) {
		this.weekHits = weekHits;
	}

	@Column(nullable = false)
	public Long getMonthHits() {
		return monthHits;
	}

	public void setMonthHits(Long monthHits) {
		this.monthHits = monthHits;
	}

	@Column(nullable = false)
	public Long getHits() {
		return hits;
	}

	public void setHits(Long hits) {
		this.hits = hits;
	}

	@Field(store = Store.YES, index = Index.YES, analyze = Analyze.NO)
	@NumericField
	@Column(nullable = false)
	public Long getWeekSales() {
		return weekSales;
	}

	public void setWeekSales(Long weekSales) {
		this.weekSales = weekSales;
	}

	@Field(store = Store.YES, index = Index.YES, analyze = Analyze.NO)
	@NumericField
	@Column(nullable = false)
	public Long getMonthSales() {
		return monthSales;
	}

	public void setMonthSales(Long monthSales) {
		this.monthSales = monthSales;
	}

	@Field(store = Store.YES, index = Index.YES, analyze = Analyze.NO)
	@NumericField
	@Column(nullable = false)
	public Long getSales() {
		return sales;
	}

	public void setSales(Long sales) {
		this.sales = sales;
	}

	@Column(nullable = false)
	public Date getWeekHitsDate() {
		return weekHitsDate;
	}

	public void setWeekHitsDate(Date weekHitsDate) {
		this.weekHitsDate = weekHitsDate;
	}

	@Column(nullable = false)
	public Date getMonthHitsDate() {
		return monthHitsDate;
	}

	public void setMonthHitsDate(Date monthHitsDate) {
		this.monthHitsDate = monthHitsDate;
	}

	@Column(nullable = false)
	public Date getWeekSalesDate() {
		return weekSalesDate;
	}

	public void setWeekSalesDate(Date weekSalesDate) {
		this.weekSalesDate = weekSalesDate;
	}

	@Column(nullable = false)
	public Date getMonthSalesDate() {
		return monthSalesDate;
	}

	public void setMonthSalesDate(Date monthSalesDate) {
		this.monthSalesDate = monthSalesDate;
	}

	@Column(nullable = false)
	public GenerateMethod getGenerateMethod() {
		return generateMethod;
	}

	public void setGenerateMethod(GenerateMethod generateMethod) {
		this.generateMethod = generateMethod;
	}

	@Length(max = 200)
	public String getAttributeValue0() {
		return attributeValue0;
	}

	public void setAttributeValue0(String attributeValue0) {
		this.attributeValue0 = attributeValue0;
	}

	@Length(max = 200)
	public String getAttributeValue1() {
		return attributeValue1;
	}

	public void setAttributeValue1(String attributeValue1) {
		this.attributeValue1 = attributeValue1;
	}

	@Length(max = 200)
	public String getAttributeValue2() {
		return attributeValue2;
	}

	public void setAttributeValue2(String attributeValue2) {
		this.attributeValue2 = attributeValue2;
	}

	@Length(max = 200)
	public String getAttributeValue3() {
		return attributeValue3;
	}

	public void setAttributeValue3(String attributeValue3) {
		this.attributeValue3 = attributeValue3;
	}

	@Length(max = 200)
	public String getAttributeValue4() {
		return attributeValue4;
	}

	public void setAttributeValue4(String attributeValue4) {
		this.attributeValue4 = attributeValue4;
	}

	@Length(max = 200)
	public String getAttributeValue5() {
		return attributeValue5;
	}

	public void setAttributeValue5(String attributeValue5) {
		this.attributeValue5 = attributeValue5;
	}

	@Length(max = 200)
	public String getAttributeValue6() {
		return attributeValue6;
	}

	public void setAttributeValue6(String attributeValue6) {
		this.attributeValue6 = attributeValue6;
	}

	@Length(max = 200)
	public String getAttributeValue7() {
		return attributeValue7;
	}

	public void setAttributeValue7(String attributeValue7) {
		this.attributeValue7 = attributeValue7;
	}

	@Length(max = 200)
	public String getAttributeValue8() {
		return attributeValue8;
	}

	public void setAttributeValue8(String attributeValue8) {
		this.attributeValue8 = attributeValue8;
	}

	@Length(max = 200)
	public String getAttributeValue9() {
		return attributeValue9;
	}

	public void setAttributeValue9(String attributeValue9) {
		this.attributeValue9 = attributeValue9;
	}

	@Length(max = 200)
	public String getAttributeValue10() {
		return attributeValue10;
	}

	public void setAttributeValue10(String attributeValue10) {
		this.attributeValue10 = attributeValue10;
	}

	@Length(max = 200)
	public String getAttributeValue11() {
		return attributeValue11;
	}

	public void setAttributeValue11(String attributeValue11) {
		this.attributeValue11 = attributeValue11;
	}

	@Length(max = 200)
	public String getAttributeValue12() {
		return attributeValue12;
	}

	public void setAttributeValue12(String attributeValue12) {
		this.attributeValue12 = attributeValue12;
	}

	@Length(max = 200)
	public String getAttributeValue13() {
		return attributeValue13;
	}

	public void setAttributeValue13(String attributeValue13) {
		this.attributeValue13 = attributeValue13;
	}

	@Length(max = 200)
	public String getAttributeValue14() {
		return attributeValue14;
	}

	public void setAttributeValue14(String attributeValue14) {
		this.attributeValue14 = attributeValue14;
	}

	@Length(max = 200)
	public String getAttributeValue15() {
		return attributeValue15;
	}

	public void setAttributeValue15(String attributeValue15) {
		this.attributeValue15 = attributeValue15;
	}

	@Length(max = 200)
	public String getAttributeValue16() {
		return attributeValue16;
	}

	public void setAttributeValue16(String attributeValue16) {
		this.attributeValue16 = attributeValue16;
	}

	@Length(max = 200)
	public String getAttributeValue17() {
		return attributeValue17;
	}

	public void setAttributeValue17(String attributeValue17) {
		this.attributeValue17 = attributeValue17;
	}

	@Length(max = 200)
	public String getAttributeValue18() {
		return attributeValue18;
	}

	public void setAttributeValue18(String attributeValue18) {
		this.attributeValue18 = attributeValue18;
	}

	@Length(max = 200)
	public String getAttributeValue19() {
		return attributeValue19;
	}

	public void setAttributeValue19(String attributeValue19) {
		this.attributeValue19 = attributeValue19;
	}

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	public ProductCategory getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(ProductCategory productCategory) {
		this.productCategory = productCategory;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Brand getBrand() {
		return brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}

	@Valid
	@Column(length = 4000)
	@Convert(converter = ProductImageConverter.class)
	public List<ProductImage> getProductImages() {
		return productImages;
	}

	public void setProductImages(List<ProductImage> productImages) {
		this.productImages = productImages;
	}

	@Valid
	@Column(length = 4000)
	@Convert(converter = ParameterValueConverter.class)
	public List<ParameterValue> getParameterValues() {
		return parameterValues;
	}

	public void setParameterValues(List<ParameterValue> parameterValues) {
		this.parameterValues = parameterValues;
	}

	@Valid
	@Column(length = 4000)
	@Convert(converter = SpecificationItemConverter.class)
	public List<SpecificationItem> getSpecificationItems() {
		return specificationItems;
	}

	public void setSpecificationItems(List<SpecificationItem> specificationItems) {
		this.specificationItems = specificationItems;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "xx_goods_promotion")
	@OrderBy("order asc")
	public Set<Promotion> getPromotions() {
		return promotions;
	}

	public void setPromotions(Set<Promotion> promotions) {
		this.promotions = promotions;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "xx_goods_tag")
	@OrderBy("order asc")
	public Set<Tag> getTags() {
		return tags;
	}

	public void setTags(Set<Tag> tags) {
		this.tags = tags;
	}

	@OneToMany(mappedBy = "goods", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	public Set<Review> getReviews() {
		return reviews;
	}

	public void setReviews(Set<Review> reviews) {
		this.reviews = reviews;
	}

	@OneToMany(mappedBy = "goods", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	public Set<Consultation> getConsultations() {
		return consultations;
	}

	public void setConsultations(Set<Consultation> consultations) {
		this.consultations = consultations;
	}

	@ManyToMany(mappedBy = "favoriteGoods", fetch = FetchType.LAZY)
	public Set<Member> getFavoriteMembers() {
		return favoriteMembers;
	}

	public void setFavoriteMembers(Set<Member> favoriteMembers) {
		this.favoriteMembers = favoriteMembers;
	}

	@OneToMany(mappedBy = "goods", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	public Set<Product> getProducts() {
		return products;
	}

	public void setProducts(Set<Product> products) {
		this.products = products;
	}

	@Transient
	public String getPath() {
		TemplateConfig templateConfig = SystemUtils.getTemplateConfig("goodsContent");
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("goods", this);
		return templateConfig.getRealStaticPath(model);
	}

	@Transient
	public String getUrl() {
		Setting setting = SystemUtils.getSetting();
		return setting.getSiteUrl() + getPath();
	}

	@Transient
	public String getThumbnail() {
		if (CollectionUtils.isEmpty(getProductImages())) {
			return getImage();
		}
		return getProductImages().get(0).getThumbnail();
	}

	@Transient
	public boolean getIsStockAlert() {
		return CollectionUtils.exists(getProducts(), new Predicate() {
			public boolean evaluate(Object object) {
				Product product = (Product) object;
				return product != null && product.getIsStockAlert();
			}
		});
	}

	@Transient
	public boolean getIsOutOfStock() {
		return CollectionUtils.exists(getProducts(), new Predicate() {
			public boolean evaluate(Object object) {
				Product product = (Product) object;
				return product != null && product.getIsOutOfStock();
			}
		});
	}

	@Transient
	public List<Integer> getSpecificationItemEntryIds() {
		List<Integer> specificationItemEntryIds = new ArrayList<Integer>();
		if (CollectionUtils.isNotEmpty(getSpecificationItems())) {
			for (SpecificationItem specificationItem : getSpecificationItems()) {
				if (CollectionUtils.isNotEmpty(specificationItem.getEntries())) {
					for (SpecificationItem.Entry entry : specificationItem.getEntries()) {
						specificationItemEntryIds.add(entry.getId());
					}
				}
			}
			Collections.sort(specificationItemEntryIds);
		}
		return specificationItemEntryIds;
	}

	@Transient
	public Product getDefaultProduct() {
		return (Product) CollectionUtils.find(getProducts(), new Predicate() {
			public boolean evaluate(Object object) {
				Product product = (Product) object;
				return product != null && product.getIsDefault();
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Transient
	public Set<Promotion> getValidPromotions() {
		if (!Type.general.equals(getType()) || CollectionUtils.isEmpty(getPromotions())) {
			return Collections.emptySet();
		}

		return new HashSet<Promotion>(CollectionUtils.select(getPromotions(), new Predicate() {
			public boolean evaluate(Object object) {
				Promotion promotion = (Promotion) object;
				return promotion != null && promotion.hasBegun() && !promotion.hasEnded() && CollectionUtils.isNotEmpty(promotion.getMemberRanks());
			}
		}));
	}

	@Transient
	public boolean hasSpecification() {
		return CollectionUtils.isNotEmpty(getSpecificationItems());
	}

	@Transient
	public boolean isValid(Promotion promotion) {
		if (!Type.general.equals(getType()) || promotion == null || !promotion.hasBegun() || promotion.hasEnded() || CollectionUtils.isEmpty(promotion.getMemberRanks())) {
			return false;
		}
		if (getValidPromotions().contains(promotion)) {
			return true;
		}
		return false;
	}

	@Transient
	public String getAttributeValue(Attribute attribute) {
		if (attribute == null || attribute.getPropertyIndex() == null) {
			return null;
		}

		try {
			String propertyName = ATTRIBUTE_VALUE_PROPERTY_NAME_PREFIX + attribute.getPropertyIndex();
			return (String) PropertyUtils.getProperty(this, propertyName);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	@Transient
	public void setAttributeValue(Attribute attribute, String attributeValue) {
		if (attribute == null || attribute.getPropertyIndex() == null) {
			return;
		}

		try {
			String propertyName = ATTRIBUTE_VALUE_PROPERTY_NAME_PREFIX + attribute.getPropertyIndex();
			PropertyUtils.setProperty(this, propertyName, attributeValue);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	@Transient
	public void removeAttributeValue() {
		for (int i = 0; i < ATTRIBUTE_VALUE_PROPERTY_COUNT; i++) {
			String propertyName = ATTRIBUTE_VALUE_PROPERTY_NAME_PREFIX + i;
			try {
				PropertyUtils.setProperty(this, propertyName, null);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e.getMessage(), e);
			} catch (InvocationTargetException e) {
				throw new RuntimeException(e.getMessage(), e);
			} catch (NoSuchMethodException e) {
				throw new RuntimeException(e.getMessage(), e);
			}
		}
	}

	@PrePersist
	public void prePersist() {
		if (CollectionUtils.isNotEmpty(getProductImages())) {
			Collections.sort(getProductImages());
		}
	}

	@PreUpdate
	public void preUpdate() {
		if (getTotalScore() != null && getScoreCount() != null && getScoreCount() > 0) {
			setScore((float) getTotalScore() / getScoreCount());
		} else {
			setScore(0F);
		}
		if (CollectionUtils.isNotEmpty(getProductImages())) {
			Collections.sort(getProductImages());
		}
	}

	@PreRemove
	public void preRemove() {
		Set<Member> favoriteMembers = getFavoriteMembers();
		if (favoriteMembers != null) {
			for (Member favoriteMember : favoriteMembers) {
				favoriteMember.getFavoriteGoods().remove(this);
			}
		}
	}

	@Converter
	public static class ProductImageConverter extends BaseAttributeConverter<List<ProductImage>> implements AttributeConverter<Object, String> {
	}

	@Converter
	public static class ParameterValueConverter extends BaseAttributeConverter<List<ParameterValue>> implements AttributeConverter<Object, String> {
	}

	@Converter
	public static class SpecificationItemConverter extends BaseAttributeConverter<List<SpecificationItem>> implements AttributeConverter<Object, String> {
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	@Transient
	public Set<SupplierProduct> getSupplierProducts() {
		return supplierProducts;
	}

	public void setSupplierProducts(Set<SupplierProduct> supplierProducts) {
		this.supplierProducts = supplierProducts;
	}
	
	@Transient
	public Set<NeedProduct> getNeedProducts() {
		return needProducts;
	}

	public void setNeedProducts(Set<NeedProduct> needProducts) {
		this.needProducts = needProducts;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Goods getSource() {
		return source;
	}

	public void setSource(Goods source) {
		this.source = source;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public SupplierSupplier getSupplierSupplier() {
		return supplierSupplier;
	}

	public void setSupplierSupplier(SupplierSupplier supplierSupplier) {
		this.supplierSupplier = supplierSupplier;
	}

	@Valid
	@Column(length = 4000)
	@Convert(converter = LabelConverter.class)
	public List<Label> getLabels() {
		return labels;
	}

	public void setLabels(List<Label> labels) {
		this.labels = labels;
	}

	public Types getTypes() {
		return types;
	}

	public void setTypes(Types types) {
		this.types = types;
	}

	@Column(length = 50)
	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	@Converter
	public static class LabelConverter extends BaseAttributeConverter<List<Label>> implements AttributeConverter<Object, String> {
	}

	@Converter
	public static class ImageConverter extends BaseAttributeConverter<List<String>> implements AttributeConverter<Object, String> {
	}

	@Valid
	@Column(length = 4000)
	@Convert(converter = ImagesConverter.class)
	public List<String> getImages() {
		return images;
	}

	public void setImages(List<String> images) {
		this.images = images;
	}

	@Converter
	public static class ImagesConverter extends BaseAttributeConverter<List<String>> implements AttributeConverter<Object, String> {
	}

}
