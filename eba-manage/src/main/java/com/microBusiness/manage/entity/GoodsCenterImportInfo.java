package com.microBusiness.manage.entity;

import com.microBusiness.manage.BaseAttributeConverter;
import com.microBusiness.manage.entity.Goods.SpecificationItemConverter;

import javax.persistence.*;
import javax.validation.Valid;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by afei.
 * User: mingbai
 * Date: 2018/3/14 上午11:37
 * Describe: 本地商品库导入信息
 * Update:
 */
@Entity
@Table(name = "t_gc_import_info")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_gc_import_info")
public class GoodsCenterImportInfo extends BaseEntity<Long> {

	private static final long serialVersionUID = 804110464936191975L;

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
        frame,    //盒
        pack //包
    }

    private String sn;

    private String name;

    private String caption;

    private BigDecimal price;

    private BigDecimal marketPrice;

    private GoodsCenter.Unit unit;

    private Integer weight;

    private Long shelfLife;

    private GoodsCenter.StorageConditions storageConditions;

    private Long packagesNum;

    private GoodsCenter.WeightUnit weightUnit;

    private BigDecimal volume;

    private GoodsCenter.VolumeUnit volumeUnit;

    private GoodsCenter.Nature nature;

    private GoodsCenterImportLog log ;

    private List<ImportError> errors ;

    private int rowNum ;

    private boolean valid;

    private CategoryCenterImport categoryCenterImport;
    
    private List<SpecificationItem> specificationItems = new ArrayList<SpecificationItem>();
    
    private CategoryCenterImport oneProductCategoryImport;
    
    private Set<ProductCenterImport> products = new HashSet<ProductCenterImport>();
    
    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(BigDecimal marketPrice) {
        this.marketPrice = marketPrice;
    }

    public GoodsCenter.Unit getUnit() {
        return unit;
    }

    public void setUnit(GoodsCenter.Unit unit) {
        this.unit = unit;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Long getShelfLife() {
        return shelfLife;
    }

    public void setShelfLife(Long shelfLife) {
        this.shelfLife = shelfLife;
    }

    public GoodsCenter.StorageConditions getStorageConditions() {
        return storageConditions;
    }

    public void setStorageConditions(GoodsCenter.StorageConditions storageConditions) {
        this.storageConditions = storageConditions;
    }

    public Long getPackagesNum() {
        return packagesNum;
    }

    public void setPackagesNum(Long packagesNum) {
        this.packagesNum = packagesNum;
    }

    public GoodsCenter.WeightUnit getWeightUnit() {
        return weightUnit;
    }

    public void setWeightUnit(GoodsCenter.WeightUnit weightUnit) {
        this.weightUnit = weightUnit;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    public GoodsCenter.VolumeUnit getVolumeUnit() {
        return volumeUnit;
    }

    public void setVolumeUnit(GoodsCenter.VolumeUnit volumeUnit) {
        this.volumeUnit = volumeUnit;
    }

    public GoodsCenter.Nature getNature() {
        return nature;
    }

    public void setNature(GoodsCenter.Nature nature) {
        this.nature = nature;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    public GoodsCenterImportLog getLog() {
        return log;
    }

    public void setLog(GoodsCenterImportLog log) {
        this.log = log;
    }

    @Column(length = 1000)
    @Convert(converter = ImportErrorConverter.class)
    public List<ImportError> getErrors() {
        return errors;
    }

    public void setErrors(List<ImportError> errors) {
        this.errors = errors;
    }

    @Converter
    public static class GoodsInfoConvert extends BaseAttributeConverter<GoodsInfo> implements AttributeConverter<Object , String> {

    }

    @Converter
    public static class ImportErrorConverter extends BaseAttributeConverter<List<ImportError>> implements AttributeConverter<Object , String> {

    }


    public int getRowNum() {
        return rowNum;
    }

    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
    }

    public boolean getValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    @ManyToOne(fetch = FetchType.LAZY)
	public CategoryCenterImport getCategoryCenterImport() {
		return categoryCenterImport;
	}

	public void setCategoryCenterImport(CategoryCenterImport categoryCenterImport) {
		this.categoryCenterImport = categoryCenterImport;
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

	@Transient
	public CategoryCenterImport getOneProductCategoryImport() {
		return oneProductCategoryImport;
	}

	public void setOneProductCategoryImport(CategoryCenterImport oneProductCategoryImport) {
		this.oneProductCategoryImport = oneProductCategoryImport;
	}
	
	@OneToMany(mappedBy = "goodsCenter", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	public Set<ProductCenterImport> getProducts() {
		return products;
	}

	public void setProducts(Set<ProductCenterImport> products) {
		this.products = products;
	}

}
