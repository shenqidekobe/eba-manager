/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.entity;

import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;

import java.util.*;

@Entity
@Table(name = "xx_category_center_import")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_category_center_import")
public class CategoryCenterImport extends OrderEntity<Long> {

	public static final String TREE_PATH_SEPARATOR = ",";
	
	private String name;

	private String seoTitle;

	private String seoKeywords;

	private String seoDescription;

	private String treePath;

	private Integer grade;

	private CategoryCenterImport parent;

	private Set<CategoryCenterImport> children = new HashSet<CategoryCenterImport>();

	private Set<Goods> goods = new HashSet<Goods>();

	private List<SpecificationCenterImport> specifications = new ArrayList<SpecificationCenterImport>();

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
	public CategoryCenterImport getParent() {
		return parent;
	}

	public void setParent(CategoryCenterImport parent) {
		this.parent = parent;
	}

	@OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
	@OrderBy("order asc")
	public Set<CategoryCenterImport> getChildren() {
		return children;
	}

	public void setChildren(Set<CategoryCenterImport> children) {
		this.children = children;
	}

	@OneToMany(mappedBy = "productCategory", fetch = FetchType.LAZY)
	public Set<Goods> getGoods() {
		return goods;
	}

	public void setGoods(Set<Goods> goods) {
		this.goods = goods;
	}


	@OneToMany(mappedBy = "productCategoryImport", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@OrderBy("order asc")
	public List<SpecificationCenterImport> getSpecifications() {
		return specifications;
	}

	public void setSpecifications(List<SpecificationCenterImport> specifications) {
		this.specifications = specifications;
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
}
