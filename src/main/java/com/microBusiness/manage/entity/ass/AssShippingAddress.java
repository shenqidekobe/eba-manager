package com.microBusiness.manage.entity.ass;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.microBusiness.manage.entity.Area;
import com.microBusiness.manage.entity.BaseEntity;

@Entity
@Table(name = "ass_shipping_address")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_shipping_address")
public class AssShippingAddress  extends BaseEntity<Long>{

	private static final long serialVersionUID = -6553736387069943619L;
	
	//收货点名称
	private String addressName;
	//收货人名称
	private String name;
	//收货人手机号
	private String tel;
	//收货地址
	private String address;
	
	private Area area;
	
	private AssChildMember assChildMember;
	
	public enum Defaults {
		defaults,
		noDefaults
	}
	
	//设置是否为默认地址
	private Defaults defaults;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "assChildMember", nullable = false, updatable = false)
	public AssChildMember getAssChildMember() {
		return assChildMember;
	}

	public void setAssChildMember(AssChildMember assChildMember) {
		this.assChildMember = assChildMember;
	}

	@NotNull
    @OneToOne(fetch = FetchType.LAZY)
	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public String getAddressName() {
		return addressName;
	}

	public void setAddressName(String addressName) {
		this.addressName = addressName;
	}

	public Defaults getDefaults() {
		return defaults;
	}

	public void setDefaults(Defaults defaults) {
		this.defaults = defaults;
	}

}
