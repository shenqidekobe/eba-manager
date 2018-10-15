package com.microBusiness.manage.dao.impl;

import javax.persistence.Query;

import com.microBusiness.manage.dao.InventoryGoodsDao;
import com.microBusiness.manage.entity.InventoryGoods;

import org.springframework.stereotype.Repository;

@Repository("inventoryGoodsDaoImpl")
public class InventoryGoodsDaoImpl extends BaseDaoImpl<InventoryGoods, Long> implements InventoryGoodsDao {

	@Override
	public void delete(Long inventoryForm) {
		String sql=" delete from t_inventory_goods where inventory_form = :inventoryForm ";
		Query query = entityManager.createNativeQuery(sql.toString());
		query.setParameter("inventoryForm", inventoryForm);
		query.executeUpdate();
	}

}