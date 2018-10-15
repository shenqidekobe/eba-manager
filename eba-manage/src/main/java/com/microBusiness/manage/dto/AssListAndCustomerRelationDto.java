package com.microBusiness.manage.dto;

import java.io.Serializable;

import com.microBusiness.manage.entity.ass.AssList;

public class AssListAndCustomerRelationDto implements Serializable {

	private static final long serialVersionUID = 1700324513338604594L;
	
	private AssList assList;
	
	private String pClientName;
	
	private String pTheme;
	
	private String adminName;
	
	public AssList getAssList() {
		return assList;
	}

	public void setAssList(AssList assList) {
		this.assList = assList;
	}

	public String getpClientName() {
		return pClientName;
	}

	public void setpClientName(String pClientName) {
		this.pClientName = pClientName;
	}

	public String getpTheme() {
		return pTheme;
	}

	public void setpTheme(String pTheme) {
		this.pTheme = pTheme;
	}

	public String getAdminName() {
		return adminName;
	}

	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}


}
