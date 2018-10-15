/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service.impl;

import java.util.List;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.SpecificationDao;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.ProductCategory;
import com.microBusiness.manage.entity.Specification;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.entity.Types;
import com.microBusiness.manage.service.SpecificationService;
import com.microBusiness.manage.entity.Specification;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("specificationServiceImpl")
public class SpecificationServiceImpl extends BaseServiceImpl<Specification, Long> implements SpecificationService {

    @Resource
    private SpecificationDao specificationDao ;
    /**
     * 查询当前供应商下的规格
     *
     * @param pageable
     * @param supplier
     * @return
     */
    @Override
    public Page<Specification> findPage(Pageable pageable, Supplier supplier) {
        return specificationDao.findPage(pageable , supplier);
    }
	@Override
	public List<Specification> findByName(String name, Supplier supplier,
			ProductCategory productCategory) {
		return specificationDao.findByName(name, supplier, productCategory);
	}
	@Override
	public Page<Specification> findPage(Pageable pageable, Member member) {
		return specificationDao.findPage(pageable, member);
	}
	@Override
	public List<Specification> existName(Member member, Types types, String name,ProductCategory productCategory) {
		return specificationDao.existName(member, types, name, productCategory);
	}

}