package com.microBusiness.manage.service.ass;

import java.util.List;

import com.microBusiness.manage.entity.Product;
import com.microBusiness.manage.entity.ass.AssCustomerRelation;
import com.microBusiness.manage.entity.ass.AssGoods;
import com.microBusiness.manage.entity.ass.AssProduct;
import com.microBusiness.manage.service.BaseService;

public interface AssProductService extends BaseService<AssProduct, Long> {
	
	List<AssProduct> findByList(AssGoods assGoods);
}
