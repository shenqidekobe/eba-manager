package com.microBusiness.manage.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.microBusiness.manage.BaseAttributeConverter;
import com.microBusiness.manage.BigDecimalNumericFieldBridge;
import com.microBusiness.manage.entity.Goods.ParameterValueConverter;
import com.microBusiness.manage.entity.Goods.ProductImageConverter;
import com.microBusiness.manage.entity.Goods.SpecificationItemConverter;
import com.microBusiness.manage.entity.Goods.Type;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.NumericField;
import org.hibernate.search.annotations.Store;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Entity
@Table(name = "xx_goods_import_info")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_goods_import_info")
public class GoodsImportInfo extends BaseEntity<Long> {
	
	private static final long serialVersionUID = -8833332266316562756L;

	private String sn;
	
	private String name;

	private String caption;

	private Type type;

	private BigDecimal price;

	private BigDecimal marketPrice;

	private String image;

	private Goods.Unit unit;

	private Integer weight;

	private Boolean isMarketable = true;

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

	private Long hits;

	private Long weekSales;

	private Long monthSales;

	private Long sales;
	
	private Long shelfLife;
	
	private Goods.StorageConditions storageConditions;
	
	private Long packagesNum;
	
	private Goods.WeightUnit weightUnit;
	
	private BigDecimal volume;
	
	private Goods.VolumeUnit VolumeUnit;
	
	private Goods.Nature nature;

	private Date weekHitsDate;

	private Date monthHitsDate;

	private Date weekSalesDate;

	private Date monthSalesDate;

	private Goods.GenerateMethod generateMethod;

	private ProductCategoryImport productCategoryImport;

	private Brand brand;

	private List<ProductImage> productImages = new ArrayList<ProductImage>();

	private List<ParameterValue> parameterValues = new ArrayList<ParameterValue>();

	private List<SpecificationItem> specificationItems = new ArrayList<SpecificationItem>();

	private Set<Promotion> promotions = new HashSet<Promotion>();

	private Set<Tag> tags = new HashSet<Tag>();

	private Set<Consultation> consultations = new HashSet<Consultation>();

	private Set<Member> favoriteMembers = new HashSet<Member>();

	private Set<ProductImport> products = new HashSet<ProductImport>();

	private Supplier supplier ;
	
	private Set<SupplierProduct> supplierProducts=new HashSet<>();
	
	private Set<NeedProduct> needProducts=new HashSet<>();
	
	List<GoodsImportError> errors ;
	
	private ProductCategoryImport oneProductCategoryImport;
	
	 /**
     * excel 里的行号
     */
    int rowNum ;
    
    private GoodsImportLog goodsImportLog;
    
    private boolean valid;
	
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
	public Goods.Unit getUnit() {
		return unit;
	}

	public void setUnit(Goods.Unit unit) {
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
	public Goods.StorageConditions getStorageConditions() {
		return storageConditions;
	}

	public void setStorageConditions(Goods.StorageConditions storageConditions) {
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
	public Goods.WeightUnit getWeightUnit() {
		return weightUnit;
	}

	public void setWeightUnit(Goods.WeightUnit weightUnit) {
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
	public Goods.VolumeUnit getVolumeUnit() {
		return VolumeUnit;
	}

	public void setVolumeUnit(Goods.VolumeUnit volumeUnit) {
		VolumeUnit = volumeUnit;
	}

	@Field(store = Store.YES, index = Index.NO, analyze = Analyze.NO)
	public Goods.Nature getNature() {
		return nature;
	}

	public void setNature(Goods.Nature nature) {
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
	public Goods.GenerateMethod getGenerateMethod() {
		return generateMethod;
	}

	public void setGenerateMethod(Goods.GenerateMethod generateMethod) {
		this.generateMethod = generateMethod;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public ProductCategoryImport getProductCategoryImport() {
		return productCategoryImport;
	}

	public void setProductCategoryImport(ProductCategoryImport productCategoryImport) {
		this.productCategoryImport = productCategoryImport;
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

	@OneToMany(mappedBy = "goodsImportInfo", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	public Set<ProductImport> getProducts() {
		return products;
	}

	public void setProducts(Set<ProductImport> products) {
		this.products = products;
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

	@Convert(converter = GoodsImportInfo.GoodsImportErrorConverter.class)
	public List<GoodsImportError> getErrors() {
		return errors;
	}

	public void setErrors(List<GoodsImportError> errors) {
		this.errors = errors;
	}

	@Converter
    public static class GoodsImportErrorConverter extends BaseAttributeConverter<List<GoodsImportError>> implements AttributeConverter<Object, String> {
    }
	
	@Transient
	public int getRowNum() {
		return rowNum;
	}

	public void setRowNum(int rowNum) {
		this.rowNum = rowNum;
	}

    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, updatable = false ,foreignKey = @ForeignKey(name = "null"))
	public GoodsImportLog getGoodsImportLog() {
		return goodsImportLog;
	}

	public void setGoodsImportLog(GoodsImportLog goodsImportLog) {
		this.goodsImportLog = goodsImportLog;
	}

	public boolean getValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	@Transient
	public String getThumbnail() {
		if (CollectionUtils.isEmpty(getProductImages())) {
			return getImage();
		}
		return getProductImages().get(0).getThumbnail();
	}

	@Transient
	public ProductCategoryImport getOneProductCategoryImport() {
		return oneProductCategoryImport;
	}

	public void setOneProductCategoryImport(
			ProductCategoryImport oneProductCategoryImport) {
		this.oneProductCategoryImport = oneProductCategoryImport;
	}
	
}
