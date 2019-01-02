package com.microBusiness.manage.dto;

import com.microBusiness.manage.util.JsonUtils;

public class DictJson {
	
	private String rate1;
	private String rate2;
	private String rate3;
	
	private Integer levelDist;//多少级分销？
	private Integer intervalDayCommision;//间隔多少天发放佣金

	public String getRate1() {
		return rate1;
	}

	public void setRate1(String rate1) {
		this.rate1 = rate1;
	}

	public String getRate2() {
		return rate2;
	}

	public void setRate2(String rate2) {
		this.rate2 = rate2;
	}

	public String getRate3() {
		return rate3;
	}

	public void setRate3(String rate3) {
		this.rate3 = rate3;
	}

	public Integer getLevelDist() {
		return levelDist;
	}

	public void setLevelDist(Integer levelDist) {
		this.levelDist = levelDist;
	}

	public Integer getIntervalDayCommision() {
		return intervalDayCommision;
	}

	public void setIntervalDayCommision(Integer intervalDayCommision) {
		this.intervalDayCommision = intervalDayCommision;
	}
	
	public static void main(String[] args) {
		DictJson obj=new DictJson();
		obj.setLevelDist(2);
		obj.setIntervalDayCommision(15);
		obj.setRate1("0.03");
		obj.setRate2("0.02");
		obj.setRate3("0.01");
		String json=JsonUtils.toJson(obj);
		System.out.println(json);
	}
	
}
