package com.microBusiness.manage.dao.impl;

import java.util.List;

import javax.persistence.NoResultException;

import com.microBusiness.manage.dao.InventoryFormLogDao;
import com.microBusiness.manage.entity.InventoryForm;
import com.microBusiness.manage.entity.InventoryFormLog;
import com.microBusiness.manage.entity.Shop;

import org.springframework.stereotype.Repository;

@Repository("inventoryFormLogDaoImpl")
public class InventoryFormLogDaoImpl extends BaseDaoImpl<InventoryFormLog, Long> implements InventoryFormLogDao {
	
	@Override
	public List<InventoryFormLog> query(Shop shop, InventoryForm inventoryForm) {
		try {
			String jpql = "select inventoryFormLog from InventoryFormLog inventoryFormLog where inventoryFormLog.shop =:shop and inventoryFormLog.inventoryForm =:inventoryForm order by inventoryFormLog.createDate DESC";
			return entityManager.createQuery(jpql, InventoryFormLog.class).setParameter("shop", shop).setParameter("inventoryForm", inventoryForm).getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}

}