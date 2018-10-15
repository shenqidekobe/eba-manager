package com.microBusiness.manage.entity;

public enum ProxyCheckStatus {

	wait("等待审核"),
	finish("已通过"),
	rej("已拒绝");
	
	private String name;
	
	ProxyCheckStatus(String name){
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
