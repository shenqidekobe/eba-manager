package com.microBusiness.manage.dao;

import java.util.List;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.Need;
import com.microBusiness.manage.entity.Product;
import com.microBusiness.manage.entity.ProductCategory;
import com.microBusiness.manage.entity.Shop;
import com.microBusiness.manage.entity.SupplierAssignRelation;
import com.microBusiness.manage.entity.SupplierNeedProduct;
import com.microBusiness.manage.entity.SupplierSupplier;

public interface SupplierNeedProductDao extends BaseDao<SupplierNeedProduct, Long>{

	/**
	 * 获取给收货点分配的商品列表
	 * @param supplierSupplier
	 * @param need
	 * @return
	 */
	List<SupplierNeedProduct> findBySupplier(SupplierSupplier supplierSupplier, Need need);
	
	List<SupplierNeedProduct> findBySupplier(SupplierSupplier supplierSupplier, Need need, String goodsName, ProductCategory productCategory);
	
	SupplierNeedProduct findSupplierNeedProduct(SupplierAssignRelation supplierAssignRelation, Need need,
			Product product);

	SupplierNeedProduct findSupplierNeedProduct(SupplierSupplier supplierSupplier, Need need,
												Product product);
	
	List<SupplierNeedProduct> findBySupplierBarCode(SupplierSupplier supplierSupplier, Need need, String barCode);

    Page<SupplierNeedProduct> findBySupplierPage(SupplierSupplier supplierSupplier, Need need, String goodsName, Long productCategoryId, Pageable pageable);

    List<SupplierNeedProduct> getProduct(Product products, Member member);
}
