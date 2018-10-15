package com.microBusiness.manage.service;

import java.util.List;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.CategoryCenter;
import com.microBusiness.manage.entity.ProductCenter;

/**
 * Created by afei.
 * User: mingbai
 * Date: 2018/3/6 上午10:48
 * Describe:
 * Update:
 */
public interface ProductCenterService extends BaseService<ProductCenter , Long> {
    /**
     * 规格商品分页
     * @param categoryCenter
     * @param goodsName
     * @param pageable
     * @return
     */
    Page<ProductCenter> findPage(CategoryCenter categoryCenter , String goodsName , Pageable pageable);
    
    /**
     * 
     * @Title: findList
     * @author: yuezhiwei
     * @date: 2018年4月12日下午5:29:00
     * @param categoryCenter 分类
     * @param goodsName 商品名称
     * @param barCode 条形码
     * @Description: 规格列表
     * @return: List<ProductCenter>
     */
    List<ProductCenter> findList(CategoryCenter categoryCenter , String goodsName , String barCode);
    
    List<ProductCenter> findList(CategoryCenter categoryCenter , String searchName);

}
