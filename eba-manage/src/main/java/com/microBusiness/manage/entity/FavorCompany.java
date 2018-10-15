package com.microBusiness.manage.entity;

import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.microBusiness.manage.entity.CompanyGoods.Delflag;

/**
 * 收藏企业关联
 * 
 * @author 吴战波
 *
 */
@Entity
@Table(name = "t_favor_company")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_favor_company")
public class FavorCompany extends BaseEntity<Long> {

	private static final long serialVersionUID = 7658559282600521755L;

	/*
	 * 用户
	 */
	private Long adminId;

	/*
	 * 企业
	 */
	private Long supplierId;
	
	
	public enum Delflag{
		delflag_no,//0.未删除 
		delflag_had,//1.已删除
	}

	/*
	 * 删除标记
	 */
	public Delflag delflag;
	

	public FavorCompany() {
	}

	public Long getAdminId() {
		return adminId;
	}

	public void setAdminId(Long adminId) {
		this.adminId = adminId;
	}

	public Long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}

	public Delflag getDelflag() {
		return delflag;
	}

	public void setDelflag(Delflag delflag) {
		this.delflag = delflag;
	}
	
}
