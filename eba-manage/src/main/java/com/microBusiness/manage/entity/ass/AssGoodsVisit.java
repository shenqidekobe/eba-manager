package com.microBusiness.manage.entity.ass;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.microBusiness.manage.entity.BaseEntity;

/**
 * 商品访问记录
 * @author admin
 *
 */
@Entity
@Table(name = "ass_goods_visit")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_goods_visit")
public class AssGoodsVisit extends BaseEntity<Long> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1950804767849728836L;
	//用户名称
	private String name;
	//商品名称
	private String goodsName;
	//访问的分享源
	private AssCustomerRelation assCustomerRelation;
	//访问的商品
	private AssGoods assGoods;
	//访问用户
	private AssChildMember assChildMember;
	//访问用户的ip
	private String ip;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	public AssCustomerRelation getAssCustomerRelation() {
		return assCustomerRelation;
	}
	public void setAssCustomerRelation(AssCustomerRelation assCustomerRelation) {
		this.assCustomerRelation = assCustomerRelation;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	public AssGoods getAssGoods() {
		return assGoods;
	}
	public void setAssGoods(AssGoods assGoods) {
		this.assGoods = assGoods;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	public AssChildMember getAssChildMember() {
		return assChildMember;
	}
	public void setAssChildMember(AssChildMember assChildMember) {
		this.assChildMember = assChildMember;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	
	
}
