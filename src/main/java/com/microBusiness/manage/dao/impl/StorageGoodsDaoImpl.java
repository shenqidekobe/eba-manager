package com.microBusiness.manage.dao.impl;

import javax.persistence.Query;

import com.microBusiness.manage.dao.StorageGoodsDao;
import com.microBusiness.manage.entity.StorageGoods;

import org.springframework.stereotype.Repository;

@Repository("storageGoodsDaoImpl")
public class StorageGoodsDaoImpl extends BaseDaoImpl<StorageGoods, Long> implements StorageGoodsDao {

	@Override
	public void delete(Long storageForm) {
		String sql=" delete from t_storage_goods where storage_form = :storageForm ";
		Query query = entityManager.createNativeQuery(sql.toString());
		query.setParameter("storageForm", storageForm);
		query.executeUpdate();
	
	}

}