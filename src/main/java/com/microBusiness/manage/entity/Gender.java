package com.microBusiness.manage.entity;

public enum Gender {

	male("男"),

	female("女");
	
	private String name;
	
	Gender(String name){
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
