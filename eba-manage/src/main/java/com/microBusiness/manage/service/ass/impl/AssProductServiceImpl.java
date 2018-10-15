package com.microBusiness.manage.service.ass.impl;


import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.microBusiness.manage.dao.ass.AssProductDao;
import com.microBusiness.manage.entity.ass.AssCustomerRelation;
import com.microBusiness.manage.entity.ass.AssGoods;
import com.microBusiness.manage.entity.ass.AssProduct;
import com.microBusiness.manage.service.ass.AssProductService;
import com.microBusiness.manage.service.impl.BaseServiceImpl;

@Service("assProductServiceImpl")
public class AssProductServiceImpl extends BaseServiceImpl<AssProduct, Long> implements AssProductService {

	@Resource
	private AssProductDao assProductDao;
	
	@Override
	public List<AssProduct> findByList(AssGoods assGoods) {
		return assProductDao.findByList(assGoods);
	}
}
