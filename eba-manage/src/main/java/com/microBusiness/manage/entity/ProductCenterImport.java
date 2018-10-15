package com.microBusiness.manage.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeConverter;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Converter;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PreRemove;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.microBusiness.manage.BaseAttributeConverter;

import org.apache.commons.collections.CollectionUtils;

@Entity
@Table(name = "t_product_center_import")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_product_center_import")
public class ProductCenterImport extends BaseEntity<Long> {

	private static final long serialVersionUID = 1608966405559527615L;

	private String sn;

    private BigDecimal price;

    private BigDecimal cost;
    
    private Boolean isDefault;

    private GoodsCenterImportInfo goodsCenter;

    private List<SpecificationValue> specificationValues = new ArrayList<SpecificationValue>();

    //条形码
    private String barCode ;

    @Column(nullable = false, updatable = false, unique = true)
    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    @NotNull(groups = Product.General.class)
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

    @NotNull
    @Column(nullable = false)
    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, updatable = false)
    public GoodsCenterImportInfo getGoodsCenter() {
        return goodsCenter;
    }

    public void setGoodsCenter(GoodsCenterImportInfo goodsCenter) {
        this.goodsCenter = goodsCenter;
    }

    @Valid
    @Column(length = 4000)
    @Convert(converter = Product.SpecificationValueConverter.class)
    public List<SpecificationValue> getSpecificationValues() {
        return specificationValues;
    }

    public void setSpecificationValues(List<SpecificationValue> specificationValues) {
        this.specificationValues = specificationValues;
    }

    @Transient
    public String getName() {
        return getGoodsCenter() != null ? getGoodsCenter().getName() : null;
    }

    @Transient
    public String getUnit() {
        return getGoodsCenter() != null ? getGoodsCenter().getUnit().toString() : null;
    }

    @Transient
    public Integer getWeight() {
        return getGoodsCenter() != null ? getGoodsCenter().getWeight() : null;
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
    public boolean hasSpecification() {
        return CollectionUtils.isNotEmpty(getSpecificationValues());
    }

    @PreRemove
    public void preRemove() {
    }

    @Converter
	public static class SpecificationValueConverter extends BaseAttributeConverter<List<SpecificationValue>> implements AttributeConverter<Object, String> {
	}

    @Column(length = 50)
    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

}