package com.microBusiness.manage.service;

import java.util.List;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.NeedShopProduct;
import com.microBusiness.manage.entity.Product;
import com.microBusiness.manage.entity.ProductCategory;
import com.microBusiness.manage.entity.Shop;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.entity.SupplyNeed;

/**
 * @description:
 * @author: pengtianwen
 * @create: 2018-03-08 13:56
 **/
public interface NeedShopProductService  extends  BaseService<NeedShopProduct,Long>{
    List<NeedShopProduct> getList(SupplyNeed supplyNeed, Shop shop, Supplier supplier);
    
    List<NeedShopProduct> getList(SupplyNeed supplyNeed, Shop shop, Supplier supplier, String goodsName, ProductCategory productCategory);

    Page<NeedShopProduct> getPage(SupplyNeed supplyNeed, Shop shop, Supplier supplier, String goodsName,Long productCategoryId, Pageable pageable);

    List<NeedShopProduct> getByBarCode(SupplyNeed supplyNeed, Shop shop, Supplier supplier, String barCode);
    
    List<NeedShopProduct> getList();
    
    List<NeedShopProduct> getProduct(Shop shop, Product  products, Member member);
}
