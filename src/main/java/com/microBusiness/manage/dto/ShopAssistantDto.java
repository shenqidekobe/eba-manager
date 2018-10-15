package com.microBusiness.manage.dto;

import java.io.Serializable;

public class ShopAssistantDto implements Serializable {

	private static final long serialVersionUID = 680193752083151789L;

	private String shopAssistantName;
	
	private String shopAssistantTel;

	public String getShopAssistantName() {
		return shopAssistantName;
	}

	public void setShopAssistantName(String shopAssistantName) {
		this.shopAssistantName = shopAssistantName;
	}

	public String getShopAssistantTel() {
		return shopAssistantTel;
	}

	public void setShopAssistantTel(String shopAssistantTel) {
		this.shopAssistantTel = shopAssistantTel;
	}
	
}
