package com.microBusiness.manage.entity;

/**
 * @description: 供应商类型   直营店 1、2、4   加盟店 3、4
 * @author: pengtianwen
 * @create: 2018-03-08 15:45
 **/
public enum SupplierType {
    //1.本企业供应商
    ONE,
    //2.企业-->企业-->门店
    TWO,
    //3.企业-->门店
    THREE,
    //4.本地供应商
    FOUR
}
