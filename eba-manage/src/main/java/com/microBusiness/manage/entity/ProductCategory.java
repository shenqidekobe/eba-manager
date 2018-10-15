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
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
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
@Table(name = "xx_product_category")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_product_category")
public class ProductCategory extends OrderEntity<Long> {

	private static final long serialVersionUID = 402870506140238446L;

	public static final String TREE_PATH_SEPARATOR = ",";

	private static final String PATH_PREFIX = "/goods/list";

	private static final String PATH_SUFFIX = ".jhtml";

	private String name;

	private String seoTitle;

	private String seoKeywords;

	private String seoDescription;

	private String treePath;

	private Integer grade;

	private ProductCategory parent;

	private Set<ProductCategory> children = new HashSet<ProductCategory>();

	private Set<Goods> goods = new HashSet<Goods>();

	private Set<Brand> brands = new HashSet<Brand>();

	private Set<Promotion> promotions = new HashSet<Promotion>();

	private Set<Parameter> parameters = new HashSet<Parameter>();

	private Set<Attribute> attributes = new HashSet<Attribute>();

	private Set<Specification> specifications = new HashSet<Specification>();
	// TODO: 2017/3/1 分类和供应商进行关联
	private Supplier supplier ;
	
	//来源
	private ProductCategory source;
	
	//供应关系
	private SupplierSupplier supplierSupplier;
	//类型（本地/平台）
	private Types types;
	
	//本地分类才会用menber
	private Member member;

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
	public ProductCategory getParent() {
		return parent;
	}

	public void setParent(ProductCategory parent) {
		this.parent = parent;
	}

	@OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
	@OrderBy("order asc")
	public Set<ProductCategory> getChildren() {
		return children;
	}

	public void setChildren(Set<ProductCategory> children) {
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

	@OneToMany(mappedBy = "productCategory", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@OrderBy("order asc")
	public Set<Specification> getSpecifications() {
		return specifications;
	}

	public void setSpecifications(Set<Specification> specifications) {
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
	public List<ProductCategory> getParents() {
		List<ProductCategory> parents = new ArrayList<ProductCategory>();
		recursiveParents(parents, this);
		return parents;
	}

	private void recursiveParents(List<ProductCategory> parents, ProductCategory productCategory) {
		if (productCategory == null) {
			return;
		}
		ProductCategory parent = productCategory.getParent();
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

	/**
	 * 从上往下
	 * @param productCategory
	 * @return
	 */
	@Transient
	public static List<Map<String  , Object>> getCategoryIterator(Set<ProductCategory> productCategory){
		List<Map<String , Object>> resut = new ArrayList<>();

		if(CollectionUtils.isEmpty(productCategory)){
			return resut ;
		}

		for(ProductCategory category : productCategory){
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

	/**
	 * 从下往上
	 * @param
	 * @return
	 */
	@Transient
	public static List<Map<String  , Object>> getCategorySmallToLarge(List<Map<String  , Object>> resultList,Map<String , Object> map,ProductCategory category){
		if (exits(resultList,category)){
			return resultList;
		}
		map.put("value" , category.getId());
		map.put("label" , category.getName());
		if(category.getParent() != null){
			List<Map<String  , Object>> list=new ArrayList<>();
			list.add(map);
			Map<String,Object> newMap=new HashMap<>();
			newMap.put("children" , list) ;
			getCategorySmallToLarge(resultList,newMap,category.getParent());
		}else {
			resultList.add(map);
		}
		return resultList ;
	}
	@Transient
	public static boolean exits(List<Map<String  , Object>> resultList,ProductCategory category){
		boolean exits=false;

		for (Map<String  , Object> map : resultList) {
			if (category.getParent() == null){
				if (category.getId().equals(map.get("value"))) {
					exits = true;
					break;
				}
			}else {
				if (category.getParent().getId().equals(map.get("value"))) {
					Map<String, Object> map1 = new HashMap<>();
					map1.put("value", category.getId());
					map1.put("label", category.getName());

					List<Map<String, Object>> list ;
					if ( map.get("children") != null){
						list=(List<Map<String, Object>>) map.get("children");
					}else {
						list=new ArrayList<>();
						map.put("children",list);
					}
					list.add(map1);
					exits = true;
				}
			}
		}
		if (!exits){
			for (Map<String  , Object> map : resultList) {
				if (map.get("children") != null) {
					List<Map<String, Object>> list = (List<Map<String, Object>>) map.get("children");
					if (exits(list, category)){
						break;
					}
				}
			}
		}
		return exits;
	}


	@ManyToOne(fetch = FetchType.LAZY)
	public ProductCategory getSource() {
		return source;
	}

	public void setSource(ProductCategory source) {
		this.source = source;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public SupplierSupplier getSupplierSupplier() {
		return supplierSupplier;
	}

	public void setSupplierSupplier(SupplierSupplier supplierSupplier) {
		this.supplierSupplier = supplierSupplier;
	}

	public Types getTypes() {
		return types;
	}

	public void setTypes(Types types) {
		this.types = types;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="member" ,foreignKey = @ForeignKey(name = "null"))
	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

}
