package com.microBusiness.manage.dao;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.*;

import java.util.List;

/**
 * @description:
 * @author: pengtianwen
 * @create: 2018-03-08 13:52
 **/
public interface NeedShopProductDao extends  BaseDao<NeedShopProduct, Long>{
    /**
     * 获取店铺分配商品列表
     * @param supplyNeed
     * @param shop
     * @param supplier
     * @return
     */
    List<NeedShopProduct> getList(SupplyNeed supplyNeed, Shop shop, Supplier supplier);

    List<NeedShopProduct> getList(SupplyNeed supplyNeed, Shop shop, Supplier supplier, String goodsName, ProductCategory productCategory);

    NeedShopProduct getNeedShopProduct(SupplyNeed supplyNeed, Shop shop, Supplier supplier, Product product);

    Page<NeedShopProduct> getPage(SupplyNeed supplyNeed, Shop shop, Supplier supplier, String goodsName,Long productCategoryId, Pageable pageable);
    
    List<NeedShopProduct> getList();
    
    List<NeedShopProduct> getByBarCode(SupplyNeed supplyNeed, Shop shop, Supplier supplier, String barCode);
    
    List<NeedShopProduct> getProduct(Shop shop, Product  products, Member member);
    
    List<NeedShopProduct> getNeedShopProductList(Shop shop, Supplier supplier, Product product);
}
