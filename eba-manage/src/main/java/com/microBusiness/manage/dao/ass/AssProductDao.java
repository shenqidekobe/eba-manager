package com.microBusiness.manage.dao.ass;

import java.util.List;

import com.microBusiness.manage.dao.BaseDao;
import com.microBusiness.manage.entity.ass.AssCustomerRelation;
import com.microBusiness.manage.entity.ass.AssGoods;
import com.microBusiness.manage.entity.ass.AssProduct;

public interface AssProductDao extends BaseDao<AssProduct, Long> {

	boolean snExists(String sn);
	
	List<AssProduct> findBySpecifications(Long id);
	
	List<AssProduct> findByList(AssGoods assGoods);

}
