/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service;

import java.util.List;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.ProductCategory;
import com.microBusiness.manage.entity.Specification;
import com.microBusiness.manage.entity.Specification;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.entity.Types;

public interface SpecificationService extends BaseService<Specification, Long> {
    /**
     * 查询当前供应商下的规格
     * @param pageable
     * @param supplier
     * @return
     */
    Page<Specification> findPage(Pageable pageable , Supplier supplier);
    
    List<Specification> findByName(String name, Supplier supplier, ProductCategory productCategory);
    
    /**
     * 
     * @Title: findPage
     * @author: yuezhiwei
     * @date: 2018年3月7日下午2:33:16
     * @Description: 查询本地规格
     * @return: Page<Specification>
     */
    Page<Specification> findPage(Pageable pageable , Member member);
    
    /**
     * 
     * @Title: existName
     * @author: yuezhiwei
     * @date: 2018年3月31日下午2:38:51
     * @Description: 验证规格名称是否重复
     * @return: boolean
     */
    List<Specification> existName(Member member , Types types , String name , ProductCategory productCategory);
}