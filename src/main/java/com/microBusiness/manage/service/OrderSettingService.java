package com.microBusiness.manage.service;

import java.util.Date;

import com.microBusiness.manage.entity.OrderSetting;
import com.microBusiness.manage.entity.Supplier;

/**
 * Created by yuezhiwei on 2017/6/23.
 * 功能描述：
 * 修改记录：
 */
public interface OrderSettingService extends BaseService<OrderSetting, Long> {

	/*
	 * 根据企业查询所在的订单设置
	 */
	OrderSetting findBySupplier(Long supplier);
	
	/*
	 * 判断下单时间是否在设定的时间之间
	 */
	boolean compareTime(OrderSetting orderSetting);
	
	/**
	 * 判断收货时间
	 * @param orderSetting
	 * @param reDate 收货时间
	 * @return
	 */
	boolean compareDate(OrderSetting orderSetting , Date reDate);
	
	/**
	 * OrderSetting 如果不存在设置默认值
	 * @return
	 */
	OrderSetting setDefaultValue();
}
