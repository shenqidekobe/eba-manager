package com.microBusiness.manage.dao.impl;

import javax.persistence.NoResultException;

import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.microBusiness.manage.dao.OrderSettingDao;
import com.microBusiness.manage.entity.Need;
import com.microBusiness.manage.entity.OrderSetting;
import com.microBusiness.manage.entity.Supplier;

/**
 * Created by yuezhiwei on 2017/6/23.
 * 功能描述：
 * 修改记录：
 */
@Repository("orderSettingDaoImpl")
public class OrderSettingDaoImpl extends BaseDaoImpl<OrderSetting, Long> implements OrderSettingDao {

	@Override
	public OrderSetting findBySupplier(Long supplier) {
		if(StringUtils.isEmpty(supplier)) {
			return null;
		}
		try{
			String sql = "select orderSetting from OrderSetting orderSetting where lower(orderSetting.supplier) = lower(:supplierid)";
			return entityManager.createQuery(sql, OrderSetting.class).setParameter("supplierid", supplier).getSingleResult();
		}catch (NoResultException e) {
            return null;
        }
		
	}

}
