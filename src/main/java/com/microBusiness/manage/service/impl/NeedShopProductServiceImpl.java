package com.microBusiness.manage.service.impl;

import java.util.List;

import javax.annotation.Resource;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.NeedShopProductDao;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.NeedShopProduct;
import com.microBusiness.manage.entity.Product;
import com.microBusiness.manage.entity.ProductCategory;
import com.microBusiness.manage.entity.Shop;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.entity.SupplyNeed;
import com.microBusiness.manage.service.NeedShopProductService;

import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: pengtianwen
 * @create: 2018-03-08 13:57
 **/
@Service("needShopProductServiceImpl")
public class NeedShopProductServiceImpl extends BaseServiceImpl<NeedShopProduct,Long> implements NeedShopProductService {
    @Resource
    private NeedShopProductDao needShopProductDao;
    @Override
    public List<NeedShopProduct> getList(SupplyNeed supplyNeed, Shop shop, Supplier supplier) {
        return needShopProductDao.getList(supplyNeed,shop,supplier);
    }

    @Override
    public Page<NeedShopProduct> getPage(SupplyNeed supplyNeed, Shop shop, Supplier supplier, String goodsName,Long productCategoryId, Pageable pageable) {
        return needShopProductDao.getPage(supplyNeed, shop, supplier, goodsName,productCategoryId, pageable);
    }

	@Override
	public List<NeedShopProduct> getList(SupplyNeed supplyNeed, Shop shop, Supplier supplier, String goodsName, ProductCategory productCategory) {
		return needShopProductDao.getList(supplyNeed, shop, supplier, goodsName, productCategory);
	}

	@Override
	public List<NeedShopProduct> getList() {
		return needShopProductDao.getList();
	}

	@Override
	public List<NeedShopProduct> getByBarCode(SupplyNeed supplyNeed, Shop shop, Supplier supplier, String barCode) {
		return needShopProductDao.getByBarCode(supplyNeed, shop, supplier, barCode);
	}

	@Override
	public List<NeedShopProduct> getProduct(Shop shop, Product products, Member member) {
		return needShopProductDao.getProduct(shop, products, member);
	}
}
