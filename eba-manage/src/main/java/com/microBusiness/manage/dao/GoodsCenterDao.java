package com.microBusiness.manage.dao;


import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.CategoryCenter;
import com.microBusiness.manage.entity.GoodsCenter;
import com.microBusiness.manage.entity.Supplier;

/**
 * Created by afei.
 * User: mingbai
 * Date: 2018/3/5 下午2:56
 * Describe:
 * Update:
 */
public interface GoodsCenterDao extends BaseDao<GoodsCenter , Long> {
    Page<GoodsCenter> findPage(GoodsCenter.Type type, CategoryCenter productCategory , Boolean isMarketable, Boolean isList, Boolean isTop , GoodsCenter.OrderType orderType, Pageable pageable);

    boolean snExists(String sn);

    boolean findByName(String name);
    
    GoodsCenter findBySn(String sn);
}
