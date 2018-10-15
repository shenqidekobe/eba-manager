package com.microBusiness.manage.entity;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.util.*;

import javax.persistence.*;

@Entity
@Table(name = "t_proxy_user")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_proxy_user")
public class ProxyUser extends OrderEntity<Long> {

	private static final long serialVersionUID = 3460880304152919962L;

	public static final String TREE_PATH_SEPARATOR = ",";
	

	/*
	 * 层级
	 */
	private Integer grade;
	
	
	/**
	 * 姓名
	 */
	private String name;
	
	/**
	 * 身份证号
	 */
	private String idenNo;
	
	/**
	 * 联系方式
	 */
	private String tel;
	
	/**
	 * 联系地址
	 */
	private String address;
	
	/**
	 * 性别
	 */
	private Gender gender;
	
	
	/**
	 * 微信号
	 */
	private String webchat;
	
	/**
	 * 省市区
	 */
	private Area area;
	
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
	private ProxyUser parent;
	
	private List<ProxyUser> proxyUsers;


	private Set<ProxyUser> children = new HashSet<ProxyUser>();
	
	/**
	 * 供应商
	 */
	private Supplier supplier;
	
	private ProxyJoinType proxyJoinType; 
	
	private ChildMember childMember;
	
	/**
	 * 下属团队总人数
	 */
	private Integer groupCount;
	

	public ProxyUser() {
	}
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	public ChildMember getChildMember() {
		return childMember;
	}

	public void setChildMember(ChildMember childMember) {
		this.childMember = childMember;
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
	public List<ProxyUser> getProxyUsers() {
		return proxyUsers;
	}

	public void setProxyUsers(List<ProxyUser> proxyUsers) {
		this.proxyUsers = proxyUsers;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public ProxyUser getParent() {
		return parent;
	}

	public void setParent(ProxyUser parent) {
		this.parent = parent;
	}

	@OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
	@OrderBy("order asc")
	public Set<ProxyUser> getChildren() {
		return children;
	}

	public void setChildren(Set<ProxyUser> children) {
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
	public static List<Map<String  , Object>> getProxyUserIterator(Set<ProxyUser> proxyUsers){
		List<Map<String , Object>> resut = new ArrayList<>();

		if(CollectionUtils.isEmpty(proxyUsers)){
			return resut ;
		}

		for(ProxyUser proxyUser : proxyUsers){
			Map<String , Object> values = new HashMap<>() ;
			values.put("value" , proxyUser.getId());
			values.put("label" , proxyUser.getName());
			if(CollectionUtils.isNotEmpty(proxyUser.getChildren())){
                values.put("children" , getProxyUserIterator(proxyUser.getChildren())) ;
            }
			resut.add(values) ;
		}

		return resut ;
	}


	@Transient
	public List<ProxyUser> getParents() {
		List<ProxyUser> parents = new ArrayList<ProxyUser>();
		recursiveParents(parents, this);
		return parents;
	}

	private void recursiveParents(List<ProxyUser> parents, ProxyUser proxyUser) {
		if (proxyUser == null) {
			return;
		}
		ProxyUser parent = proxyUser.getParent();
		if (parent != null) {
			parents.add(0, parent);
			recursiveParents(parents, parent);
		}
	}


	public ProxyJoinType getProxyJoinType() {
		return proxyJoinType;
	}


	public void setProxyJoinType(ProxyJoinType proxyJoinType) {
		this.proxyJoinType = proxyJoinType;
	}


	public String getIdenNo() {
		return idenNo;
	}


	public void setIdenNo(String idenNo) {
		this.idenNo = idenNo;
	}


	public String getTel() {
		return tel;
	}


	public void setTel(String tel) {
		this.tel = tel;
	}


	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}


	public Gender getGender() {
		return gender;
	}


	public void setGender(Gender gender) {
		this.gender = gender;
	}


	public String getWebchat() {
		return webchat;
	}


	public void setWebchat(String webchat) {
		this.webchat = webchat;
	}

	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = true, updatable = false)
	public Area getArea() {
		return area;
	}


	public void setArea(Area area) {
		this.area = area;
	}


	@Transient
	public Integer getGroupCount() {
		return groupCount;
	}


	public void setGroupCount(Integer groupCount) {
		this.groupCount = groupCount;
	}

	
	


}
