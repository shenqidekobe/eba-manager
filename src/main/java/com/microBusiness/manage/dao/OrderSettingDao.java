package com.microBusiness.manage.dao;

import com.microBusiness.manage.entity.OrderSetting;
import com.microBusiness.manage.entity.Supplier;

/**
 * Created by yuezhiwei on 2017/6/23.
 * 功能描述：订单设置
 * 修改记录：
 */
public interface OrderSettingDao extends BaseDao<OrderSetting, Long> {

	OrderSetting findBySupplier(Long supplier);
}
