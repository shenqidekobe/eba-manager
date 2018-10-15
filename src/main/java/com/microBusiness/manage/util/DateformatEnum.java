package com.microBusiness.manage.util;

public enum DateformatEnum {
    yyyyMMdd("yyyyMMdd"), yyyyMMddHHmmss("yyyyMMddHHmmss"), HHmmss("HHmmss"), yyyyMMdd2("yyyy-MM-dd"),yyyyPMMPdd2("yyyy.MM.dd"),
    yyyyMMddHHmmss2("yyyy-MM-dd HH:mm:ss"),HHmm("HH:mm"), yyyyMM("yyyyMM"), yyyyMM2("yyyy-MM"), yyyy年MM月("yyyy年MM月"),
    yyyy年("yyyy年"), yyyy("yyyy"),
    yyyyMMddHHmm("yyyy-MM-dd HH:mm"),
    yyyy年MM月dd日("yyyy年MM月dd日"),MMdd2("MM-dd")
    ;

    public String format;

    DateformatEnum(String format) {
	this.format = format;
    }

    public String getFormat() {
	return format;
    }

    public void setFormat(String format) {
	this.format = format;
    }

}