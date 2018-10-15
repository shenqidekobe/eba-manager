package com.microBusiness.manage.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * 店铺表
 * @author pengtianwen	
 *	2018/3/1
 */
@Entity
@Table(name = "t_shop")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_shop")
public class Shop extends BaseEntity<Long> {

	private static final long serialVersionUID = 157204014761230526L;
	
	//店铺名称
	private String name ;
	
	//收货人
	private String userName ;
	
	//店铺类型
	private ShopType shopType;
	
	//店铺地址
	private Area area ;
	
	//详细地址
	private String address ;
	
	//所属用户
	private Member member;
	
	private Set<Need> needs=new HashSet<>();

	//库存商品
	private List<StockGoods> stockGoods = new ArrayList<>();
	
	//盘点单
	private List<InventoryForm> inventoryForms = new ArrayList<>();
	
	//入库单
	private List<StorageForm> storageForms = new ArrayList<>();
	//收货人手机号
	private String receiverTel;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public ShopType getShopType() {
		return shopType;
	}

	public void setShopType(ShopType shopType) {
		this.shopType = shopType;
	}

	@NotNull
    @OneToOne(fetch = FetchType.LAZY)
	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="member")
	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "t_need_shop")
	@OrderBy("createDate desc")
	public Set<Need> getNeeds() {
		return needs;
	}
	public void setNeeds(Set<Need> needs) {
		this.needs = needs;
	}
	

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "shop")
	public List<StockGoods> getStockGoods() {
		return stockGoods;
	}

	public void setStockGoods(List<StockGoods> stockGoods) {
		this.stockGoods = stockGoods;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "shop")
	public List<InventoryForm> getInventoryForms() {
		return inventoryForms;
	}

	public void setInventoryForms(List<InventoryForm> inventoryForms) {
		this.inventoryForms = inventoryForms;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "shop")
	public List<StorageForm> getStorageForms() {
		return storageForms;
	}

	public void setStorageForms(List<StorageForm> storageForms) {
		this.storageForms = storageForms;
	}

	@Transient
	public boolean exitsNeed(Need need){
		for (Need need1:getNeeds()){
			if (need1.equals(need)){
				return true;
			}
		}
		return  false;
	}

	public String getReceiverTel() {
		return receiverTel;
	}

	public void setReceiverTel(String receiverTel) {
		this.receiverTel = receiverTel;
	}

}
