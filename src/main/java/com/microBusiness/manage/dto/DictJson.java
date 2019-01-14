package com.microBusiness.manage.dto;

public class DictJson {
	
	private String platinum_to;//需满多少金额成为铂金
	private String platinum_rate1; //铂金 一级提成
	private String platinum_rate2; //铂金 二级提成
	private String platinum_buy_amount; //铂金 自购满多少金额返钱
	private String platinum_buy_rate; //铂金 自购提成
	
	private String blackplatinum_to;//需满多少金额成为黑金
	private String blackplatinum_rate1; //黑金 一级提成
	private String blackplatinum_rate2; //黑金 二级提成
	private String blackplatinum_buy_amount; //黑金 自购满多少金额返钱
	private String blackplatinum_buy_rate; //黑金 自购提成
	
	private Integer levelDist;//几级分销？
	private Integer intervalDayCommision;//间隔多少天发放佣金

	
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
	public String getPlatinum_to() {
		return platinum_to;
	}
	public void setPlatinum_to(String platinum_to) {
		this.platinum_to = platinum_to;
	}
	public String getPlatinum_rate1() {
		return platinum_rate1;
	}
	public void setPlatinum_rate1(String platinum_rate1) {
		this.platinum_rate1 = platinum_rate1;
	}
	public String getPlatinum_rate2() {
		return platinum_rate2;
	}
	public void setPlatinum_rate2(String platinum_rate2) {
		this.platinum_rate2 = platinum_rate2;
	}
	public String getPlatinum_buy_amount() {
		return platinum_buy_amount;
	}
	public void setPlatinum_buy_amount(String platinum_buy_amount) {
		this.platinum_buy_amount = platinum_buy_amount;
	}
	public String getPlatinum_buy_rate() {
		return platinum_buy_rate;
	}
	public void setPlatinum_buy_rate(String platinum_buy_rate) {
		this.platinum_buy_rate = platinum_buy_rate;
	}
	public String getBlackplatinum_to() {
		return blackplatinum_to;
	}
	public void setBlackplatinum_to(String blackplatinum_to) {
		this.blackplatinum_to = blackplatinum_to;
	}
	public String getBlackplatinum_rate1() {
		return blackplatinum_rate1;
	}
	public void setBlackplatinum_rate1(String blackplatinum_rate1) {
		this.blackplatinum_rate1 = blackplatinum_rate1;
	}
	public String getBlackplatinum_rate2() {
		return blackplatinum_rate2;
	}
	public void setBlackplatinum_rate2(String blackplatinum_rate2) {
		this.blackplatinum_rate2 = blackplatinum_rate2;
	}
	public String getBlackplatinum_buy_amount() {
		return blackplatinum_buy_amount;
	}
	public void setBlackplatinum_buy_amount(String blackplatinum_buy_amount) {
		this.blackplatinum_buy_amount = blackplatinum_buy_amount;
	}
	public String getBlackplatinum_buy_rate() {
		return blackplatinum_buy_rate;
	}
	public void setBlackplatinum_buy_rate(String blackplatinum_buy_rate) {
		this.blackplatinum_buy_rate = blackplatinum_buy_rate;
	}
	
}
