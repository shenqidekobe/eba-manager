package com.microBusiness.manage.entity.ass;

import java.util.List;

import javax.persistence.CascadeType;
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

import com.microBusiness.manage.entity.BaseEntity;
import com.microBusiness.manage.entity.Goods;
import com.microBusiness.manage.entity.Supplier;

/**
 * 企业添加的商品目录
 * @author admin
 *
 */
@Entity
@Table(name = "ass_good_directory")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_ass_good_directory")
public class AssGoodDirectory extends BaseEntity<Long>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6063456012584543054L;

	//目录名称
	private String theme;
	//简介
	private String profiles;
	//企业
	private Supplier supplier;
	
	private List<Goods> goods;
	
	public String getTheme() {
		return theme;
	}
	public void setTheme(String theme) {
		this.theme = theme;
	}
	public String getProfiles() {
		return profiles;
	}
	public void setProfiles(String profiles) {
		this.profiles = profiles;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="supplier")
	public Supplier getSupplier() {
		return supplier;
	}
	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "ass_good_directory_goods")
	@OrderBy("createDate desc")
	public List<Goods> getGoods() {
		return goods;
	}
	public void setGoods(List<Goods> goods) {
		this.goods = goods;
	}
	
	
	
	
}
