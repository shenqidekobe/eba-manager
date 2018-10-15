package com.microBusiness.manage.service.impl;

import java.util.List;

import javax.annotation.Resource;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.SupplierNeedProductDao;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.Need;
import com.microBusiness.manage.entity.Product;
import com.microBusiness.manage.entity.ProductCategory;
import com.microBusiness.manage.entity.SupplierAssignRelation;
import com.microBusiness.manage.entity.SupplierNeedProduct;
import com.microBusiness.manage.entity.SupplierSupplier;
import com.microBusiness.manage.service.SupplierNeedProductService;

import org.springframework.stereotype.Service;

@Service("supplierNeedProductServiceImpl")
public class SupplierNeedProductServiceImpl extends BaseServiceImpl<SupplierNeedProduct, Long> implements SupplierNeedProductService{

	@Resource(name = "supplierNeedProductDaoImpl")
	SupplierNeedProductDao supplierNeedProductDao;

	@Override
	public List<SupplierNeedProduct> findBySupplier(SupplierSupplier supplierSupplier, Need need) {
		// TODO Auto-generated method stub
		return supplierNeedProductDao.findBySupplier(supplierSupplier,need);
	}

	@Override
	public Page<SupplierNeedProduct> findBySupplierPage(SupplierSupplier supplierSupplier, Need need, String goodsName, Long productCategoryId, Pageable pageable) {
		return supplierNeedProductDao.findBySupplierPage(supplierSupplier, need, goodsName, productCategoryId, pageable);
	}

	@Override
	public SupplierNeedProduct findSupplierNeedProduct(SupplierAssignRelation supplierAssignRelation, Need need,
			Product product) {
		return supplierNeedProductDao.findSupplierNeedProduct(supplierAssignRelation, need, product);
	}

	@Override
	public List<SupplierNeedProduct> findBySupplier( SupplierSupplier supplierSupplier, Need need, String goodsName, ProductCategory productCategory) {
		return supplierNeedProductDao.findBySupplier(supplierSupplier, need, goodsName, productCategory);
	}

	@Override
	public List<SupplierNeedProduct> findBySupplierBarCode(SupplierSupplier supplierSupplier, Need need, String barCode) {
		return supplierNeedProductDao.findBySupplierBarCode(supplierSupplier, need, barCode);
	}

	@Override
	public List<SupplierNeedProduct> getProduct(Product products, Member member) {
		return supplierNeedProductDao.getProduct(products, member );
	}
}
