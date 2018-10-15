package com.microBusiness.manage.entity;

import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.microBusiness.manage.entity.CompanyGoods.Delflag;

@Entity
@Table(name = "t_favor_company_goods")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_favor_company_goods")
public class FavorCompanyGoods extends BaseEntity<Long> {

	private static final long serialVersionUID = 715672161170506720L;
	
	/*
	 * 用户id
	 */
	private Long adminId;
	
	/*
	 * 产品id
	 */
	private Long companyGoodsId;
	
	/*
	 * 删除标记
	 */
	private Delflag delflag;
	
	public FavorCompanyGoods() {
	}

	public Long getAdminId() {
		return adminId;
	}

	public void setAdminId(Long adminId) {
		this.adminId = adminId;
	}

	public Long getCompanyGoodsId() {
		return companyGoodsId;
	}

	public void setCompanyGoodsId(Long companyGoodsId) {
		this.companyGoodsId = companyGoodsId;
	}

	public Delflag getDelflag() {
		return delflag;
	}

	public void setDelflag(Delflag delflag) {
		this.delflag = delflag;
	}

}
