package com.microBusiness.manage.entity.ass;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeConverter;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Converter;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.groups.Default;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.springframework.util.StringUtils;

import com.microBusiness.manage.BaseAttributeConverter;
import com.microBusiness.manage.entity.BaseEntity;
import com.microBusiness.manage.entity.Product;
import com.microBusiness.manage.entity.SpecificationValue;
import com.microBusiness.manage.entity.ass.AssGoods.SourceType;

@Entity
@Table(name = "ass_product")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_ass_product")
public class AssProduct extends BaseEntity<Long> {

	private static final long serialVersionUID = 5659138037527680347L;

	private String sn;

	//private BigDecimal price;
	
	private AssGoods assGoods;
	
	private List<SpecificationValue> specificationValues = new ArrayList<SpecificationValue>();
	
	private String specification;
	
	public enum Exist {
		delete,
		notDeleted
	}
	
	private Exist exist = Exist.notDeleted;
	
	private Product product;
	
	public enum SourceType {
		//从后台同步
        syncBackstage,
        //前端之前目录
		MOBILE
		
	}
	
	private SourceType sourceType;

	@Column(nullable = false, updatable = false, unique = true)
	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	/*@Min(0)
	@Digits(integer = 12, fraction = 3)
	@Column(nullable = false, precision = 21, scale = 6)
	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}
*/
	@Valid
	@Column(length = 4000)
	@Convert(converter = SpecificationValueConverter.class)
	public List<SpecificationValue> getSpecificationValues() {
		return specificationValues;
	}

	public void setSpecificationValues(List<SpecificationValue> specificationValues) {
		this.specificationValues = specificationValues;
	}

	@Transient
	public String getName() {
		return getAssGoods() != null ? getAssGoods().getName() : null;
	}


	@Transient
	public List<String> getImage() {
		return getAssGoods() != null ? getAssGoods().getImage() : null;
	}

	/*@Transient
	public boolean getIsMarketable() {
		return getAssGoods() != null && BooleanUtils.isTrue(getAssGoods().getIsMarketable());
	}*/
	
	@Transient
	public boolean hasSpecification() {
		return CollectionUtils.isNotEmpty(getSpecificationValues());
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


	@Converter
	public static class SpecificationValueConverter extends BaseAttributeConverter<List<SpecificationValue>> implements AttributeConverter<Object, String> {
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(nullable = false, updatable = false)
	public AssGoods getAssGoods() {
		return assGoods;
	}

	public void setAssGoods(AssGoods assGoods) {
		this.assGoods = assGoods;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	@Transient
	public Exist getExist() {
		return exist;
	}

	public void setExist(Exist exist) {
		this.exist = exist;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, updatable = true , foreignKey = @ForeignKey(name = "null"))
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public SourceType getSourceType() {
		return sourceType;
	}

	public void setSourceType(SourceType sourceType) {
		this.sourceType = sourceType;
	}
}
