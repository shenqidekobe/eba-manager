package com.microBusiness.manage.service.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.microBusiness.manage.dao.OrderSettingDao;
import com.microBusiness.manage.entity.OrderSetting;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.service.OrderSettingService;

/**
 * Created by yuezhiwei on 2017/6/23.
 * 功能描述：
 * 修改记录：
 */
@Service("orderSettingServiceImpl")
public class OrderSettingServiceImpl extends BaseServiceImpl<OrderSetting, Long> implements OrderSettingService {

	@Resource
	private OrderSettingDao orderSettingDao;

	@Override
	public OrderSetting findBySupplier(Long supplier) {
		return orderSettingDao.findBySupplier(supplier);
	}

	@Override
	public boolean compareTime(OrderSetting orderSetting) {
		DateFormat df = new SimpleDateFormat("HH:mm:ss");
		try {
			Date startTime = df.parse(orderSetting.getStartTime());
			Date endTime = df.parse(orderSetting.getEndTime());
			Date currentTime = df.parse(df.format(new Date()));//获取当前的时分秒
			if(currentTime.getTime() < startTime.getTime() || currentTime.getTime() > endTime.getTime()) {
				return false;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return true;
	}

	@Override
	public boolean compareDate(OrderSetting orderSetting, Date reDate) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date currentDate = dateFormat.parse(dateFormat.format(new Date()));//获取当前的日期
			Date receiptDate = dateFormat.parse(dateFormat.format(reDate));//收货日期
			Calendar ca = Calendar.getInstance();
			ca.setTime(currentDate);
			ca.add(Calendar.DATE, orderSetting.getTimeReceipt());
			Date SpecifyDate = dateFormat.parse(dateFormat.format(ca.getTime()));//指定收货日期
			if(receiptDate.getTime() < SpecifyDate.getTime()) {
				return false;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return true;
	}

	@Override
	public OrderSetting setDefaultValue() {
		OrderSetting setting = new OrderSetting();
		setting.setStartTime("00:00:00");
		setting.setEndTime("23:59:59");
		setting.setTimeReceipt(0);
		setting.setNumberTimes(10);
		return setting;
	}

}
