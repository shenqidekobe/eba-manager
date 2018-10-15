/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "xx_product_category_import")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_product_category_import")
public class ProductCategoryImport extends OrderEntity<Long> {

	private static final long serialVersionUID = -2119285624209569208L;

	public static final String TREE_PATH_SEPARATOR = ",";

	private static final String PATH_PREFIX = "/goods/list";

	private static final String PATH_SUFFIX = ".jhtml";

	private String name;

	private String seoTitle;

	private String seoKeywords;

	private String seoDescription;

	private String treePath;

	private Integer grade;

	private ProductCategoryImport parent;

	private Set<ProductCategoryImport> children = new HashSet<ProductCategoryImport>();

	private Set<Goods> goods = new HashSet<Goods>();

	private Set<Brand> brands = new HashSet<Brand>();

	private Set<Promotion> promotions = new HashSet<Promotion>();

	private Set<Parameter> parameters = new HashSet<Parameter>();

	private Set<Attribute> attributes = new HashSet<Attribute>();

	private List<SpecificationImport> specifications = new ArrayList<SpecificationImport>();
	// TODO: 2017/3/1 分类和供应商进行关联
	private Supplier supplier ;
	
	@NotEmpty
	@Length(max = 200)
	@Column(nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
		this.seoKeywords = seoKeywords;
	}

	@Length(max = 200)
	public String getSeoDescription() {
		return seoDescription;
	}

	public void setSeoDescription(String seoDescription) {
		this.seoDescription = seoDescription;
	}

	@Column(nullable = false)
	public String getTreePath() {
		return treePath;
	}

	public void setTreePath(String treePath) {
		this.treePath = treePath;
	}

	@Column(nullable = false)
	public Integer getGrade() {
		return grade;
	}

	public void setGrade(Integer grade) {
		this.grade = grade;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public ProductCategoryImport getParent() {
		return parent;
	}

	public void setParent(ProductCategoryImport parent) {
		this.parent = parent;
	}

	@OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
	@OrderBy("order asc")
	public Set<ProductCategoryImport> getChildren() {
		return children;
	}

	public void setChildren(Set<ProductCategoryImport> children) {
		this.children = children;
	}

	@OneToMany(mappedBy = "productCategory", fetch = FetchType.LAZY)
	public Set<Goods> getGoods() {
		return goods;
	}

	public void setGoods(Set<Goods> goods) {
		this.goods = goods;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "xx_product_category_brand")
	@OrderBy("order asc")
	public Set<Brand> getBrands() {
		return brands;
	}

	public void setBrands(Set<Brand> brands) {
		this.brands = brands;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "xx_product_category_promotion")
	@OrderBy("order asc")
	public Set<Promotion> getPromotions() {
		return promotions;
	}

	public void setPromotions(Set<Promotion> promotions) {
		this.promotions = promotions;
	}

	@OneToMany(mappedBy = "productCategory", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@OrderBy("order asc")
	public Set<Parameter> getParameters() {
		return parameters;
	}

	public void setParameters(Set<Parameter> parameters) {
		this.parameters = parameters;
	}

	@OneToMany(mappedBy = "productCategory", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@OrderBy("order asc")
	public Set<Attribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(Set<Attribute> attributes) {
		this.attributes = attributes;
	}

	@OneToMany(mappedBy = "productCategoryImport", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@OrderBy("order asc")
	public List<SpecificationImport> getSpecifications() {
		return specifications;
	}

	public void setSpecifications(List<SpecificationImport> specifications) {
		this.specifications = specifications;
	}

	@Transient
	public String getPath() {
		return getId() != null ? PATH_PREFIX + "/" + getId() + PATH_SUFFIX : null;
	}

	@Transient
	public Long[] getParentIds() {
		String[] parentIds = StringUtils.split(getTreePath(), TREE_PATH_SEPARATOR);
		Long[] result = new Long[parentIds.length];
		for (int i = 0; i < parentIds.length; i++) {
			result[i] = Long.valueOf(parentIds[i]);
		}
		return result;
	}

	@Transient
	public List<ProductCategoryImport> getParents() {
		List<ProductCategoryImport> parents = new ArrayList<ProductCategoryImport>();
		recursiveParents(parents, this);
		return parents;
	}

	private void recursiveParents(List<ProductCategoryImport> parents, ProductCategoryImport productCategory) {
		if (productCategory == null) {
			return;
		}
		ProductCategoryImport parent = productCategory.getParent();
		if (parent != null) {
			parents.add(0, parent);
			recursiveParents(parents, parent);
		}
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}
	
	@Transient
	public static List<Map<String  , Object>> getCategoryIterator(Set<ProductCategoryImport> productCategory){
		List<Map<String , Object>> resut = new ArrayList<>();

		if(CollectionUtils.isEmpty(productCategory)){
			return resut ;
		}

		for(ProductCategoryImport category : productCategory){
			Map<String , Object> values = new HashMap<>() ;
			values.put("value" , category.getId());
			values.put("label" , category.getName());
			if(CollectionUtils.isNotEmpty(category.getChildren())){
                values.put("children" , getCategoryIterator(category.getChildren())) ;
            }
			resut.add(values) ;
		}

		return resut ;
	}

}
