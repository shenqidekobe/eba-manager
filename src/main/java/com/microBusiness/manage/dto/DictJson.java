package com.microBusiness.manage.dto;

import com.microBusiness.manage.util.JsonUtils;

public class DictJson {
	
	private String platina_rate1; //白金 一级提成
	private String platina_rate2; //白金 二级提成
	private String platina_buy_amount; //白金 自购满多少金额返钱
	private String platina_buy_rate; //白金 自购提成
	
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
	private Boolean buySSRakeBack;//自购-上上级是否返佣
	private Boolean blackplatinumBuyNoAs;//黑金自购不满黑金自购标准是否按铂金自购返

	
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
	
	public String getPlatina_rate1() {
		return platina_rate1;
	}
	public void setPlatina_rate1(String platina_rate1) {
		this.platina_rate1 = platina_rate1;
	}
	public String getPlatina_rate2() {
		return platina_rate2;
	}
	public void setPlatina_rate2(String platina_rate2) {
		this.platina_rate2 = platina_rate2;
	}
	public String getPlatina_buy_amount() {
		return platina_buy_amount;
	}
	public void setPlatina_buy_amount(String platina_buy_amount) {
		this.platina_buy_amount = platina_buy_amount;
	}
	public String getPlatina_buy_rate() {
		return platina_buy_rate;
	}
	public void setPlatina_buy_rate(String platina_buy_rate) {
		this.platina_buy_rate = platina_buy_rate;
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
	public Boolean getBuySSRakeBack() {
		return buySSRakeBack;
	}
	public void setBuySSRakeBack(Boolean buySSRakeBack) {
		this.buySSRakeBack = buySSRakeBack;
	}
	public Boolean getBlackplatinumBuyNoAs() {
		return blackplatinumBuyNoAs;
	}
	public void setBlackplatinumBuyNoAs(Boolean blackplatinumBuyNoAs) {
		this.blackplatinumBuyNoAs = blackplatinumBuyNoAs;
	}
	
	public static void main(String[] args) {
		DictJson json=new DictJson();
		json.setLevelDist(2);
		json.setIntervalDayCommision(15);
		json.setBuySSRakeBack(false);
		json.setBlackplatinumBuyNoAs(true);
		
		json.setPlatina_rate1("0.25");
		json.setPlatina_rate2("0.075");
		json.setPlatina_buy_amount("5000");
		json.setPlatina_buy_rate("0.075");
		
		json.setPlatinum_to("10000");
		json.setPlatinum_rate1("0.30");
		json.setPlatinum_rate2("0.09");
		json.setPlatinum_buy_amount("5000");
		json.setPlatinum_buy_rate("0.075");
		
		json.setBlackplatinum_to("30000");
		json.setBlackplatinum_rate1("0.35");
		json.setBlackplatinum_rate2("0.105");
		json.setBlackplatinum_buy_amount("5000");
		json.setBlackplatinum_buy_rate("0.09");
		
		String str=JsonUtils.toJson(json);
		System.out.println(str);
	}
	
}
