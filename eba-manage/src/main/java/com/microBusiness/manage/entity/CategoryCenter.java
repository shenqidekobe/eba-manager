/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "t_category_center")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_category_center")
@JsonIgnoreProperties(value = {"handler","hibernateLazyInitializer","fieldHandler"})
public class CategoryCenter extends OrderEntity<Long> {

	private static final long serialVersionUID = 402870506140238446L;

	public static final String TREE_PATH_SEPARATOR = ",";

	private String name;

	private String seoTitle;

	private String seoKeywords;

	private String seoDescription;

	private String treePath;

	private Integer grade;

	private CategoryCenter parent;

	private Set<CategoryCenter> children = new HashSet<CategoryCenter>();

	private Set<GoodsCenter> goodsCenters = new HashSet<GoodsCenter>();

	private Set<SpecificationCenter> specificationCenters = new HashSet<SpecificationCenter>();

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
	public CategoryCenter getParent() {
		return parent;
	}

	public void setParent(CategoryCenter parent) {
		this.parent = parent;
	}

	@OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
	@OrderBy("order asc")
	public Set<CategoryCenter> getChildren() {
		return children;
	}

	public void setChildren(Set<CategoryCenter> children) {
		this.children = children;
	}

	@OneToMany(mappedBy = "categoryCenter", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@OrderBy("order asc")
	public Set<SpecificationCenter> getSpecificationCenters() {
		return specificationCenters;
	}

	public void setSpecificationCenters(Set<SpecificationCenter> specificationCenters) {
		this.specificationCenters = specificationCenters;
	}

	@OneToMany(mappedBy = "categoryCenter", fetch = FetchType.LAZY)
	public Set<GoodsCenter> getGoodsCenters() {
		return goodsCenters;
	}

	public void setGoodsCenters(Set<GoodsCenter> goodsCenters) {
		this.goodsCenters = goodsCenters;
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
	public List<CategoryCenter> getParents() {
		List<CategoryCenter> parents = new ArrayList<CategoryCenter>();
		recursiveParents(parents, this);
		return parents;
	}

	private void recursiveParents(List<CategoryCenter> parents, CategoryCenter productCategory) {
		if (productCategory == null) {
			return;
		}
		CategoryCenter parent = productCategory.getParent();
		if (parent != null) {
			parents.add(0, parent);
			recursiveParents(parents, parent);
		}
	}

	@Transient
	public static List<Map<String  , Object>> getCategoryIterator(Set<CategoryCenter> productCategory){
		List<Map<String , Object>> resut = new ArrayList<>();

		if(CollectionUtils.isEmpty(productCategory)){
			return resut ;
		}

		for(CategoryCenter category : productCategory){
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
