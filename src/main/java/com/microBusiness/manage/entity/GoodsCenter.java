package com.microBusiness.manage.entity;

import com.microBusiness.manage.BaseAttributeConverter;
import com.microBusiness.manage.BigDecimalNumericFieldBridge;
import com.microBusiness.manage.Setting;
import com.microBusiness.manage.TemplateConfig;
import com.microBusiness.manage.util.SystemUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.hibernate.search.annotations.*;
import org.hibernate.search.annotations.Index;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by afei.
 * User: mingbai
 * Date: 2018/3/5 下午1:47
 * Describe: 商品库
 * Update:
 */
@Entity
@Table(name = "t_goods_center")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_goods_center")
public class GoodsCenter extends BaseEntity<Long> {

    private static final long serialVersionUID = -8781963456943173684L;

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

    public enum StorageConditions {
        roomTemperature,//常温
        refrigeration,//冷藏
        frozen            //冰冻
    }

    public enum WeightUnit {
        g,
        kg
    }

    public enum VolumeUnit {
        cubicCentimeters,//立方厘米
        cubicMeter   //立方米
    }

    public enum Nature {
        gas,//气体
        solid,//固体
        liquid//液体
    }

    public enum Unit {
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

    private GoodsCenter.Type type;

    private BigDecimal price;

    private BigDecimal marketPrice;

    private String image;

    private List<String> images;

    private GoodsCenter.Unit unit;

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

    private Long hits;

    private Long weekSales;

    private Long monthSales;

    private Long sales;

    private Long shelfLife;

    private GoodsCenter.StorageConditions storageConditions;

    private Long packagesNum;

    private GoodsCenter.WeightUnit weightUnit;

    private BigDecimal volume;

    private GoodsCenter.VolumeUnit VolumeUnit;

    private GoodsCenter.Nature nature;

    private Date weekHitsDate;

    private Date monthHitsDate;

    private Date weekSalesDate;

    private Date monthSalesDate;

    private GoodsCenter.GenerateMethod generateMethod;

    private CategoryCenter categoryCenter;

    private List<ProductImage> productImages = new ArrayList<ProductImage>();

    private List<SpecificationItem> specificationItems = new ArrayList<SpecificationItem>();

    private Set<ProductCenter> productCenters = new HashSet<ProductCenter>();

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

    //条形码
    private String barCode;

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
    public GoodsCenter.WeightUnit getWeightUnit() {
        return weightUnit;
    }

    public void setWeightUnit(GoodsCenter.WeightUnit weightUnit) {
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
    public GoodsCenter.VolumeUnit getVolumeUnit() {
        return VolumeUnit;
    }

    public void setVolumeUnit(GoodsCenter.VolumeUnit VolumeUnit) {
        this.VolumeUnit = VolumeUnit;
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

    public GenerateMethod getGenerateMethod() {
        return generateMethod;
    }

    public void setGenerateMethod(GenerateMethod generateMethod) {
        this.generateMethod = generateMethod;
    }


    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    public CategoryCenter getCategoryCenter() {
        return categoryCenter;
    }

    public void setCategoryCenter(CategoryCenter categoryCenter) {
        this.categoryCenter = categoryCenter;
    }


    @Valid
    @Column(length = 4000)
    @Convert(converter = GoodsCenter.ProductImageConverter.class)
    public List<ProductImage> getProductImages() {
        return productImages;
    }

    public void setProductImages(List<ProductImage> productImages) {
        this.productImages = productImages;
    }


    @Valid
    @Column(length = 4000)
    @Convert(converter = GoodsCenter.SpecificationItemConverter.class)
    public List<SpecificationItem> getSpecificationItems() {
        return specificationItems;
    }

    public void setSpecificationItems(List<SpecificationItem> specificationItems) {
        this.specificationItems = specificationItems;
    }

    @OneToMany(mappedBy = "goodsCenter", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    public Set<ProductCenter> getProductCenters() {
        return productCenters;
    }

    public void setProductCenters(Set<ProductCenter> productCenters) {
        this.productCenters = productCenters;
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
    public ProductCenter getDefaultProduct() {
        return (ProductCenter) CollectionUtils.find(getProductCenters(), new Predicate() {
            public boolean evaluate(Object object) {
                ProductCenter product = (ProductCenter) object;
                return product != null && product.getIsDefault();
            }
        });
    }

    @Transient
    public boolean hasSpecification() {
        return CollectionUtils.isNotEmpty(getSpecificationItems());
    }


    @PrePersist
    public void prePersist() {
        if (CollectionUtils.isNotEmpty(getProductImages())) {
            Collections.sort(getProductImages());
        }
    }

    @PreUpdate
    public void preUpdate() {
    }

    @PreRemove
    public void preRemove() {
    }

    @Converter
    public static class ProductImageConverter extends BaseAttributeConverter<List<ProductImage>> implements AttributeConverter<Object, String> {
    }

    @Converter
    public static class SpecificationItemConverter extends BaseAttributeConverter<List<SpecificationItem>> implements AttributeConverter<Object, String> {
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


    @Valid
    @Column(length = 4000)
    @Convert(converter = LabelConverter.class)
    public List<Label> getLabels() {
        return labels;
    }

    public void setLabels(List<Label> labels) {
        this.labels = labels;
    }

    @Converter
    public static class LabelConverter extends BaseAttributeConverter<List<Label>> implements AttributeConverter<Object, String> {
    }

    @Column(length = 50)
    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }
}