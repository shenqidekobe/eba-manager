package com.microBusiness.manage.entity.ass;

import java.util.List;

import javax.persistence.AttributeConverter;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Converter;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;

import com.microBusiness.manage.BaseAttributeConverter;
import com.microBusiness.manage.entity.BaseEntity;
import com.microBusiness.manage.entity.ass.AssGoods.Unit;

@Entity
@Table(name = "ass_list_item")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_list_item")
public class AssListItem  extends BaseEntity<Long>{

	private static final long serialVersionUID = -2631303565609319506L;
	
	//商品sn
	private String sn;
	//商品名称
	private String name;
	//商品缩略图
	private List<String> thumbnail;
	//商品数量
	private Integer quantity;

	private AssProduct assProduct;
	
	private AssList assList;
	//商品规格
	private String specification;
	//基本单位
	private Unit unit;
	
	@Column(nullable = false, updatable = false)	
	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	@Column(nullable = false, updatable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Valid
	@Column(length = 4000)
	@Convert(converter = ImageConverter.class)
	@Pattern(regexp = "^(?i)(http:\\/\\/|https:\\/\\/|\\/).*$")
	public List<String> getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(List<String> thumbnail) {
		this.thumbnail = thumbnail;
	}

	@Column(nullable = false, updatable = true)
	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public AssProduct getAssProduct() {
		return assProduct;
	}

	public void setAssProduct(AssProduct assProduct) {
		this.assProduct = assProduct;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "assList", nullable = false, updatable = false)
	public AssList getAssList() {
		return assList;
	}

	public void setAssList(AssList assList) {
		this.assList = assList;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}
	
	
	@Converter
	public static class ImageConverter extends BaseAttributeConverter<List<String>> implements AttributeConverter<Object, String> {
	}

}
