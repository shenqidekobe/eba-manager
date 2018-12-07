package com.microBusiness.manage.entity;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.util.*;

import javax.persistence.*;

/**
 * 
 * 类别
 */
@Entity
@Table(name = "t_category")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_category")
public class Category extends OrderEntity<Long> {

	private static final long serialVersionUID = 3460880304152919962L;

	public static final String TREE_PATH_SEPARATOR = ",";
	

	/*
	 * 层级
	 */
	private Integer grade;
	
	/*
	 * 名称
	 */
	private String name;
	
	private String seoDescription;
	
	private String seoKeywords;
	
	private String seoTitle;
	
	/*
	 * 多个父级分类ID，逗号分隔
	 */
	private String treePath;
	
	/*
	 * 商流分类父类ID
	 */
	private Category parent;
	
	private List<Category> categorys;


	private Set<Category> children = new HashSet<Category>();

	public Category() {
	}

	public Integer getGrade() {
		return grade;
	}

	public void setGrade(Integer grade) {
		this.grade = grade;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSeoDescription() {
		return seoDescription;
	}

	public void setSeoDescription(String seoDescription) {
		this.seoDescription = seoDescription;
	}

	public String getSeoKeywords() {
		return seoKeywords;
	}

	public void setSeoKeywords(String seoKeywords) {
		this.seoKeywords = seoKeywords;
	}

	public String getSeoTitle() {
		return seoTitle;
	}

	public void setSeoTitle(String seoTitle) {
		this.seoTitle = seoTitle;
	}

	public String getTreePath() {
		return treePath;
	}

	public void setTreePath(String treePath) {
		this.treePath = treePath;
	}

	@Transient
	public List<Category> getCategorys() {
		return categorys;
	}

	public void setCategorys(List<Category> categorys) {
		this.categorys = categorys;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Category getParent() {
		return parent;
	}

	public void setParent(Category parent) {
		this.parent = parent;
	}

	@OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
	@OrderBy("order asc")
	public Set<Category> getChildren() {
		return children;
	}

	public void setChildren(Set<Category> children) {
		this.children = children;
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
	public static List<Map<String  , Object>> getCategoryIterator(Set<Category> categories){
		List<Map<String , Object>> resut = new ArrayList<>();

		if(CollectionUtils.isEmpty(categories)){
			return resut ;
		}

		for(Category category : categories){
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


	@Transient
	public List<Category> getParents() {
		List<Category> parents = new ArrayList<Category>();
		recursiveParents(parents, this);
		return parents;
	}

	private void recursiveParents(List<Category> parents, Category category) {
		if (category == null) {
			return;
		}
		Category parent = category.getParent();
		if (parent != null) {
			parents.add(0, parent);
			recursiveParents(parents, parent);
		}
	}


}
