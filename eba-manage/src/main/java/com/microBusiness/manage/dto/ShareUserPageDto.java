package com.microBusiness.manage.dto;

import java.io.Serializable;
import java.util.Date;

public class ShareUserPageDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3492007034601294284L;

	private Long id;
	private String name;
	private String headImgUrl;
	private Date date;
	private Long pageVisit;
	private Long goodsVisit;
	private boolean isSynchronize;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getHeadImgUrl() {
		return headImgUrl;
	}
	public void setHeadImgUrl(String headImgUrl) {
		this.headImgUrl = headImgUrl;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Long getPageVisit() {
		return pageVisit;
	}
	public void setPageVisit(Long pageVisit) {
		this.pageVisit = pageVisit;
	}
	public Long getGoodsVisit() {
		return goodsVisit;
	}
	public void setGoodsVisit(Long goodsVisit) {
		this.goodsVisit = goodsVisit;
	}
	public boolean isSynchronize() {
		return isSynchronize;
	}
	public void setSynchronize(boolean isSynchronize) {
		this.isSynchronize = isSynchronize;
	}
	
	
}
