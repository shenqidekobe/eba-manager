package com.microBusiness.manage.entity;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.NumericField;
import org.hibernate.search.annotations.Store;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.microBusiness.manage.BigDecimalNumericFieldBridge;
import com.microBusiness.manage.Setting;
import com.microBusiness.manage.TemplateConfig;
import com.microBusiness.manage.util.SystemUtils;

@Indexed
@Entity
@Table(name = "t_company_goods")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_company_goods")
public class CompanyGoods extends BaseEntity<Long> {

	private static final long serialVersionUID = 5589459377632700746L;

	public static final String HITS_CACHE_NAME = "goodsHits";

	public static final int ATTRIBUTE_VALUE_PROPERTY_COUNT = 20;

	public static final String ATTRIBUTE_VALUE_PROPERTY_NAME_PREFIX = "attributeValue";

	private String sn;

	/**
	 * 商品名称
	 */
	private String name;

	/**
	 * 参考价格
	 */
	private BigDecimal marketPrice;

	/**
	 * 图片链接
	 */
	private String image;

	/**
	 * 基本单位
	 */
	private Unit unit;

	/**
	 * 保质期
	 */
	private Long shelfLife;

	/**
	 * 保存条件
	 */
	private StorageConditions storageConditions;

	/**
	 * 商品详情
	 */
	private String introduction;

	/**
	 * 公司
	 */
	private Supplier supplier;

	/**
	 * 分类
	 */
	private Category category;
	
	/**
	 * 发布类型
	 */
	private PubType pubType;

	/**
	 * 商品规格
	 */
	private String goodsSpec;

	/**
	 * 货物来源
	 */
	private SourceType sourceType;
	
	/**
	 * 采购数
	 */
	private int needNum;

	/**
	 * 发布商品来源
	 */
	private PubfromSource pubfromSource;
	
	/**
	 * 审核状态
	 */
	private Status status;
	
	/**
	 * 删除标记
	 */
	private Delflag delflag;
	
	public enum PubType{
		pub_supply("供应"),//供应
		pub_need("采购");//采购

		private String desc ;

		PubType(String desc) {
			this.desc = desc ;
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}
	}
	
	public enum SourceType{
		source_type_has(0,"有货"),//0.有货 
		source_type_now(1,"现货");//1.现货
		
		private int value;
		private String name;
		
		SourceType(int value, String name){
			this.value = value;
			this.name = name;
		}

		public int getValue() {
			return value;
		}

		public void setValue(int value) {
			this.value = value;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
		
	}
	
	public enum PubfromSource{
		pub_diect,//0.直接发布 
		pub_from_dinghuome;//1.从微商小管理发布
	}
	
	public enum Status{
		status_wait("未审核"),//0.未审核 
		status_ok("通过"),//1.通过
		status_rej("不通过");//2.不通过
		
		private String name;
		Status(String name){
			this.name = name;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
	}
	
	public enum Delflag{
		delflag_no,//0.未删除 
		delflag_had,//1.已删除
	}

	public enum Type {

		general,

		exchange,

		gift
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
		roomTemperature("常温"),//常温
		refrigeration("冷藏"),//冷藏
		frozen("冰冻");		//冰冻
		
		private String name;
		StorageConditions(String name){
			this.name = name;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
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
		box("箱"),//箱
		bottle("瓶"),//瓶
		bag("袋"),//袋
		frame("盒"),	//盒
		pack("包"); //包
		
		private String name;
		
		Unit(String name){
			this.name = name;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
	}

	

	private String caption;

	private Type type;

	private BigDecimal price;

	private Integer weight;

	private Boolean isMarketable;

	private Boolean isList;

	private Boolean isTop;

	private Boolean isDelivery;

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

	private Long hits;

	private Long weekSales;

	private Long monthSales;

	private Long sales;
	
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

	private FavorCompanyGoods favorCompanyGoods;
	
	//@Field(store = Store.YES, index = Index.YES, analyze = Analyze.NO)
	//@Pattern(regexp = "^[0-9a-zA-Z_-]+$")
	//@Length(max = 100)
	@Column(nullable = true, updatable = false, unique = true)
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
	@Column(nullable = true, updatable = false)
	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	@Field(store = Store.YES, index = Index.YES, analyze = Analyze.NO)
	@NumericField
	@FieldBridge(impl = BigDecimalNumericFieldBridge.class)
	@Column(nullable = true, precision = 21, scale = 6)
	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public int getNeedNum() {
		return needNum;
	}

	public void setNeedNum(int needNum) {
		this.needNum = needNum;
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
	@Column(nullable = true)
	public Boolean getIsMarketable() {
		return isMarketable;
	}

	public void setIsMarketable(Boolean isMarketable) {
		this.isMarketable = isMarketable;
	}

	@Field(store = Store.YES, index = Index.YES, analyze = Analyze.NO)
	@NotNull
	@Column(nullable = true)
	public Boolean getIsList() {
		return isList;
	}

	public void setIsList(Boolean isList) {
		this.isList = isList;
	}

	@Field(store = Store.YES, index = Index.YES, analyze = Analyze.NO)
	@NotNull
	@Column(nullable = true)
	public Boolean getIsTop() {
		return isTop;
	}

	public void setIsTop(Boolean isTop) {
		this.isTop = isTop;
	}

	@NotNull
	@Column(nullable = true)
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
	@Column(nullable = true, precision = 12, scale = 6)
	public Float getScore() {
		return score;
	}

	public void setScore(Float score) {
		this.score = score;
	}

	@Column(nullable = true)
	public Long getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(Long totalScore) {
		this.totalScore = totalScore;
	}

	@Field(store = Store.YES, index = Index.YES, analyze = Analyze.NO)
	@NumericField
	@Column(nullable = true)
	public Long getScoreCount() {
		return scoreCount;
	}

	public void setScoreCount(Long scoreCount) {
		this.scoreCount = scoreCount;
	}

	@Column(nullable = true)
	public Long getWeekHits() {
		return weekHits;
	}

	public void setWeekHits(Long weekHits) {
		this.weekHits = weekHits;
	}

	@Column(nullable = true)
	public Long getMonthHits() {
		return monthHits;
	}

	public void setMonthHits(Long monthHits) {
		this.monthHits = monthHits;
	}

	@Column(nullable = true)
	public Long getHits() {
		return hits;
	}

	public void setHits(Long hits) {
		this.hits = hits;
	}

	@Field(store = Store.YES, index = Index.YES, analyze = Analyze.NO)
	@NumericField
	@Column(nullable = true)
	public Long getWeekSales() {
		return weekSales;
	}

	public void setWeekSales(Long weekSales) {
		this.weekSales = weekSales;
	}

	@Field(store = Store.YES, index = Index.YES, analyze = Analyze.NO)
	@NumericField
	@Column(nullable = true)
	public Long getMonthSales() {
		return monthSales;
	}

	public void setMonthSales(Long monthSales) {
		this.monthSales = monthSales;
	}

	@Field(store = Store.YES, index = Index.YES, analyze = Analyze.NO)
	@NumericField
	@Column(nullable = true)
	public Long getSales() {
		return sales;
	}

	public void setSales(Long sales) {
		this.sales = sales;
	}

	@Column(nullable = true)
	public Date getWeekHitsDate() {
		return weekHitsDate;
	}

	public void setWeekHitsDate(Date weekHitsDate) {
		this.weekHitsDate = weekHitsDate;
	}

	@Column(nullable = true)
	public Date getMonthHitsDate() {
		return monthHitsDate;
	}

	public void setMonthHitsDate(Date monthHitsDate) {
		this.monthHitsDate = monthHitsDate;
	}

	@Column(nullable = true)
	public Date getWeekSalesDate() {
		return weekSalesDate;
	}

	public void setWeekSalesDate(Date weekSalesDate) {
		this.weekSalesDate = weekSalesDate;
	}

	@Column(nullable = true)
	public Date getMonthSalesDate() {
		return monthSalesDate;
	}

	public void setMonthSalesDate(Date monthSalesDate) {
		this.monthSalesDate = monthSalesDate;
	}

	@Column(nullable = true)
	public GenerateMethod getGenerateMethod() {
		return generateMethod;
	}

	public void setGenerateMethod(GenerateMethod generateMethod) {
		this.generateMethod = generateMethod;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="category" , nullable = true, updatable = true , foreignKey = @ForeignKey(name = "null"))
	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
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


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="supplier" , nullable = true, updatable = false , foreignKey = @ForeignKey(name = "null"))
	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	public PubType getPubType() {
		return pubType;
	}

	public void setPubType(PubType pubType) {
		this.pubType = pubType;
	}

	public String getGoodsSpec() {
		return goodsSpec;
	}

	public void setGoodsSpec(String goodsSpec) {
		this.goodsSpec = goodsSpec;
	}

	public SourceType getSourceType() {
		return sourceType;
	}

	public void setSourceType(SourceType sourceType) {
		this.sourceType = sourceType;
	}

	public PubfromSource getPubfromSource() {
		return pubfromSource;
	}

	public void setPubfromSource(PubfromSource pubfromSource) {
		this.pubfromSource = pubfromSource;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Delflag getDelflag() {
		return delflag;
	}

	public void setDelflag(Delflag delflag) {
		this.delflag = delflag;
	}

	@Transient
	public FavorCompanyGoods getFavorCompanyGoods() {
		return favorCompanyGoods;
	}

	public void setFavorCompanyGoods(FavorCompanyGoods favorCompanyGoods) {
		this.favorCompanyGoods = favorCompanyGoods;
	}

	@Transient
	public String getTrueMarketPrice(){

		BigDecimal marketPrice = this.getMarketPrice() ;

		if(null == marketPrice){
			return "";
		}

		if(marketPrice.compareTo(new BigDecimal("-1")) == 0){
			return "面议";
		}

		return marketPrice.toString() ;
	}

}
