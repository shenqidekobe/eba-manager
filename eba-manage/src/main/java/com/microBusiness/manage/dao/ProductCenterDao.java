package com.microBusiness.manage.dao;

import java.util.List;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.CategoryCenter;
import com.microBusiness.manage.entity.ProductCenter;

/**
 * Created by afei.
 * User: mingbai
 * Date: 2018/3/6 上午10:45
 * Describe:
 * Update:
 */
public interface ProductCenterDao extends BaseDao<ProductCenter, Long> {
    boolean snExists(String sn);

    Page<ProductCenter> findPage(CategoryCenter categoryCenter , String goodsName , Pageable pageable);
    
    List<ProductCenter> findList(CategoryCenter categoryCenter , String goodsName , String barCode);
    
    List<ProductCenter> findList(CategoryCenter categoryCenter , String searchName);
}
