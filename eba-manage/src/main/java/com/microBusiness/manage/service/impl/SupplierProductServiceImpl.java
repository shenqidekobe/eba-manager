package com.microBusiness.manage.service.impl;

import java.util.List;

import javax.annotation.Resource;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.Product;
import com.microBusiness.manage.entity.SupplierSupplier;

import org.springframework.stereotype.Service;

import com.microBusiness.manage.dao.SupplierProductDao;
import com.microBusiness.manage.entity.SupplierProduct;
import com.microBusiness.manage.service.SupplierProductService;

@Service("supplierProductServiceImpl")
public class SupplierProductServiceImpl extends BaseServiceImpl<SupplierProduct, Long> implements SupplierProductService{

	@Resource(name = "supplierProductDaoImpl")
	private SupplierProductDao supplierProductDao;

	@Override
	public SupplierProduct getProduct(Long productId, Long supplierId, Long bySupplierId, SupplierSupplier.Status status, Long needId) {
		return supplierProductDao.getProduct(productId , supplierId , bySupplierId , status , needId);
	}

	@Override
	public SupplierProduct getSupplierProduct(SupplierSupplier supplierSupplier, Product product) {
		return supplierProductDao.getSupplierProduct(supplierSupplier, product);
	}

	@Override
	public Page<SupplierProduct> findPage(Pageable pageable,
			Long productCategoryId,SupplierSupplier supplierSupplier) {
		return supplierProductDao.findPage(pageable, productCategoryId, supplierSupplier);
	}

	@Override
	public List<SupplierProduct> getSupplierProductList(
			SupplierSupplier supplierSupplier) {
		return supplierProductDao.getSupplierProductList(supplierSupplier);
	}

	@Override
	public List<SupplierProduct> supplierProductList(
			SupplierSupplier supplierSupplier, List<Long> productIds) {
		return supplierProductDao.supplierProductList(supplierSupplier, productIds);
	}
	@Override
	public List<SupplierProduct> findByProduct(Product product) {
		return supplierProductDao.findByProduct(product);
	}
}
