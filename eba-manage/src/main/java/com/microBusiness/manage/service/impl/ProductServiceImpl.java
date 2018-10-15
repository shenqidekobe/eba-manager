/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.persistence.LockModeType;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.*;
import com.microBusiness.manage.dto.ProductSupplierDto;
import com.microBusiness.manage.dto.SupplyProductDto;
import com.microBusiness.manage.entity.*;
import com.microBusiness.manage.entity.Goods.OrderType;
import com.microBusiness.manage.entity.Goods.Type;
import com.microBusiness.manage.service.ProductService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Service("productServiceImpl")
public class ProductServiceImpl extends BaseServiceImpl<Product, Long> implements ProductService {

	@Resource(name = "productDaoImpl")
	private ProductDao productDao;
	@Resource(name = "stockLogDaoImpl")
	private StockLogDao stockLogDao;

	@Resource
	private NeedProductDao needProductDao ;

	@Resource
	private NeedDao needDao ;

	@Resource
	private SupplierProductDao supplierProductDao ;
	@Resource(name = "goodsDaoImpl")
	private GoodsDao goodsDao;
	@Resource
	private SupplierDao supplierDao ;
	@Resource
    private SupplyNeedDao supplyNeedDao ;
	@Resource
	private NeedShopProductDao needShopProductDao;
	@Resource
	private SupplierSupplierDao supplierSupplierDao;


	@Transactional(readOnly = true)
	public boolean snExists(String sn, Supplier supplier) {
		return productDao.snExists(sn,supplier);
	}

	@Transactional(readOnly = true)
	public Product findBySn(String sn, Supplier supplier) {
		return productDao.findBySn(sn,supplier);
	}

	@Transactional(readOnly = true)
	public List<Product> search(Goods.Type type, String keyword, Set<Product> excludes, Integer count) {
		return productDao.search(type, keyword, excludes, count);
	}

	public void addStock(Product product, int amount, StockLog.Type type, Admin operator, String memo) {
		Assert.notNull(product);
		Assert.notNull(type);

		if (amount == 0) {
			return;
		}

		if (!LockModeType.PESSIMISTIC_WRITE.equals(productDao.getLockMode(product))) {
			productDao.refresh(product, LockModeType.PESSIMISTIC_WRITE);
		}

		Assert.notNull(product.getStock());
		Assert.state(product.getStock() + amount >= 0);

		boolean previousOutOfStock = product.getIsOutOfStock();

		product.setStock(product.getStock() + amount);
		productDao.flush();

		Goods goods = product.getGoods();
		if (goods != null) {
			if (product.getIsOutOfStock() != previousOutOfStock) {
				goods.setGenerateMethod(Goods.GenerateMethod.eager);
			} else {
				goods.setGenerateMethod(Goods.GenerateMethod.lazy);
			}
		}

		StockLog stockLog = new StockLog();
		stockLog.setType(type);
		stockLog.setInQuantity(amount > 0 ? amount : 0);
		stockLog.setOutQuantity(amount < 0 ? Math.abs(amount) : 0);
		stockLog.setStock(product.getStock());
		stockLog.setOperator(operator);
		stockLog.setMemo(memo);
		stockLog.setProduct(product);
		stockLogDao.persist(stockLog);
	}

	public void addAllocatedStock(Product product, int amount) {
		Assert.notNull(product);

		if (amount == 0) {
			return;
		}

		if (!LockModeType.PESSIMISTIC_WRITE.equals(productDao.getLockMode(product))) {
			productDao.refresh(product, LockModeType.PESSIMISTIC_WRITE);
		}

		Assert.notNull(product.getAllocatedStock());
		Assert.state(product.getAllocatedStock() + amount >= 0);

		boolean previousOutOfStock = product.getIsOutOfStock();

		product.setAllocatedStock(product.getAllocatedStock() + amount);
		productDao.flush();

		Goods goods = product.getGoods();
		if (goods != null) {
			if (product.getIsOutOfStock() != previousOutOfStock) {
				goods.setGenerateMethod(Goods.GenerateMethod.eager);
			} else {
				goods.setGenerateMethod(Goods.GenerateMethod.lazy);
			}
		}
	}

	@Transactional(readOnly = true)
	public void filter(List<Product> products) {
		CollectionUtils.filter(products, new Predicate() {
			public boolean evaluate(Object object) {
				Product product = (Product) object;
				return product != null && product.getStock() != null;
			}
		});
	}

	@Override
	public List<Product> getproductSpecifications(Long goodsId) {
		return productDao.getproductSpecifications(goodsId);
	}


	/**
	 * 获取商品正式供应的规格
	 *
	 * @param goodsId
	 * @param supplierId
	 * @return
	 */
	@Override
	public List<Product> getFormalProduct(Long goodsId, Long supplierId ,  Need need) {
		return null;
	}

	/**
	 * 获取商品临时供应的规格
	 *
	 * @param goodsId
	 * @param supplierId
	 * @return
	 */
	@Override
	public List<Product> getTemporaryProduct(Long goodsId, Long supplierId ,  Need need) {
		return null;
	}

	@Override
	public List<Product> getProduct(Long goodsId, Long supplierId, SupplyType supplyType , Need need) {
		List<Product> products = new ArrayList<>();
		if(supplyType.equals(SupplyType.temporary)){
			products = productDao.getTemporaryProduct(goodsId , supplierId , need) ;
		}else if(supplyType.equals(SupplyType.formal)){
			products = productDao.getFormalProduct(goodsId , supplierId , need) ;
		}
		return products ;
	}

	@Override
	public Page<Product> getProductsPage(Long supplierId, SupplyType supplyType, Need need) {

		return null;
	}

	@Override
	public Page<Product> getTemporaryProductPage(Long supplierId, Long needId, Pageable pageable , Long productCategoryId,String searchName) {
		Page<Product> result = productDao.getTemporaryProductPage(supplierId , needId , pageable , productCategoryId,searchName);
		//处理最小起订量
		List<Product> products = result.getContent() ;
		Need need = needDao.find(needId) ;
		for(Product product : products){
			Integer minOrderQuantity = this.getMinOrderQuantity(product , supplierId , SupplyType.temporary , need) ;
			product.setMinOrderQuantity(minOrderQuantity);
		}
		return result;
	}
	
	@Override
	public Page<Product> getFormalProductPage(Long supplierId, Long needId, Pageable pageable , Long productCategoryId,String searchName) {
		Page<Product> result = productDao.getFormalProductPage(supplierId , needId , pageable , productCategoryId,searchName);
		//处理最小起订量
		List<Product> products = result.getContent() ;
		Need need = needDao.find(needId) ;
		for(Product product : products){
			Integer minOrderQuantity = this.getMinOrderQuantity(product , supplierId , SupplyType.formal , need) ;
			product.setMinOrderQuantity(minOrderQuantity);
		}
		return result;
	}

	/**
	 * 获取供应商之间分配的商品
	 *
	 * @param supplierId
	 * @param bySupplierId
	 * @param pageable
	 * @return
	 */
	@Override
	public Page<Product> getSupplierAssignProducts(Long supplierId, Pageable pageable , Long productCategoryId) {
		Page<Product> page = productDao.getSupplierAssignProducts(supplierId , pageable , productCategoryId);
		return page ;
	}

	/**
	 * @param goodsId
	 * @param supplierId
	 * @param supplyType
	 * @param need
	 * @return
	 */
	@Override
	public List<SupplyProductDto> getSupplyProduct(Long goodsId, Long supplierId, SupplyType supplyType, Need need) {
		List<SupplyProductDto> products = new ArrayList<>();
		if(supplyType.equals(SupplyType.temporary)){
			products = productDao.getTemporarySupplyProduct(goodsId , supplierId , need) ;
		}else if(supplyType.equals(SupplyType.formal)){
			products = productDao.getFormalSupplyProduct(goodsId , supplierId , need) ;
		}
		return products ;
	}
	
	@Override
	public List<NeedProduct> getNeeedProducts(Long goodsId, Long supplierId, SupplyType supplyType, Need need) {
		List<NeedProduct> needProducts=new ArrayList<>();
		Goods goods=goodsDao.find(goodsId);
		Supplier supplier=supplierDao.find(supplierId);
		if(supplyType.equals(SupplyType.temporary)){
			SupplyNeed supplyNeed=new SupplyNeed();
			supplyNeed.setSupplier(supplier);
			supplyNeed.setNeed(need);
			supplyNeed=supplyNeedDao.findSupplyNeedOnSupply(supplyNeed);
			needProducts=needProductDao.getNeeedProducts(supplyNeed, goods.getProducts());
		}else if(supplyType.equals(SupplyType.formal)){
//			products = productDao.getFormalSupplyProduct(goodsId , supplierId , need) ;
		}
		return needProducts ;
	}

	@Override
	public Integer getMinOrderQuantity(Product product, Long supplierId, SupplyType supplyType , Need need) {
		Integer minOrderQuantity = 1 ;
		if(supplyType.equals(SupplyType.temporary)){

			NeedProduct needProduct = needProductDao.getProduct(product , supplierId , need.getId() , SupplyNeed.Status.SUPPLY) ;
			minOrderQuantity = needProduct.getMinOrderQuantity() ;

		}else if(supplyType.equals(SupplyType.formal)){
			SupplierProduct supplierProduct = supplierProductDao.getProduct(product.getId() , supplierId , need.getSupplier().getId() , SupplierSupplier.Status.inTheSupply , need.getId());
			minOrderQuantity = supplierProduct.getMinOrderQuantity() ;
		}
		return minOrderQuantity ;
	}
	
	@Override
	public Page<Product> getGoodListPage(SupplyNeed supplyNeed,Supplier supplier,Supplier cruuSupplier,Boolean hasDistribution,

			ProductCategory productCategory, String goodName,Pageable pageable,Long[] ids) {
		// TODO Auto-generated method stub
		Page<Product> page=productDao.getGoodListPage(supplyNeed,supplier,cruuSupplier,hasDistribution, productCategory, goodName, pageable,ids);
		
		List<Product> list=page.getContent();
		for (Product product : list) {
			if (supplyNeed != null) {
				NeedProduct needProduct=needProductDao.findByNeedSupplier(supplyNeed, product);
				product.setMinOrderQuantity(needProduct.getMinOrderQuantity());
				product.setSupplyPrice(needProduct.getSupplyPrice());
			}else {
				product.setMinOrderQuantity(product.getTurnoverMinOrderQuantity());
				product.setSupplyPrice(product.getTurnoverSupplyPrice());
			}
		}
		return page;
	}
	
	@Override
	public List<Product> getGoodList(SupplyNeed supplyNeed,Supplier supplier,Supplier cruuSupplier,Boolean hasDistribution,
			ProductCategory productCategory, String goodName,Pageable pageable) {
		// TODO Auto-generated method stub
		List<Product> page=productDao.getGoodList(supplyNeed,supplier,cruuSupplier,hasDistribution, productCategory, goodName, pageable);
		
		for (Product product : page) {
			if (supplyNeed != null) {
				NeedProduct needProduct=needProductDao.findByNeedSupplier(supplyNeed, product);
				product.setMinOrderQuantity(needProduct.getMinOrderQuantity());
				product.setSupplyPrice(needProduct.getSupplyPrice());
			}else {
				product.setMinOrderQuantity(product.getTurnoverMinOrderQuantity());
				product.setSupplyPrice(product.getTurnoverSupplyPrice());
			}
		}
		return page;
	}

	@Override
	public boolean update(List<Product> productList) {
		try {
			for (Product product : productList) {
				Product productNew = this.find(product.getId());
				productNew.setTurnoverMinOrderQuantity(product.getTurnoverMinOrderQuantity());
				productNew.setTurnoverSupplyPrice(product.getTurnoverSupplyPrice());
				this.update(productNew);
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	@Override
	public Page<Product> getGoodListPageByFormal(SupplierSupplier supplierSupplier,Need need,
			ProductCategory productCategory, String goodName,Pageable pageable,Long[] ids) {
		// TODO Auto-generated method stub
		Page<Product> page=productDao.getGoodListPageByFormal(supplierSupplier,need, productCategory, goodName, pageable,ids);
		List<Product> list=page.getContent();
		for (Product product : list) {
			SupplierProduct supplierProduct=supplierProductDao.getSupplierProduct(supplierSupplier,product);
			product.setMinOrderQuantity(supplierProduct.getMinOrderQuantity());
			product.setSupplyPrice(supplierProduct.getSupplyPrice());
		}
		return page;
	}

	
	@Override
	public List<Product> getGoodListByFormal(SupplierSupplier supplierSupplier,Need need) {
		// TODO Auto-generated method stub
		List<Product> page=productDao.getGoodListByFormal(supplierSupplier,need);
		for (Product product : page) {
			SupplierProduct supplierProduct=supplierProductDao.getSupplierProduct(supplierSupplier,product);
			product.setMinOrderQuantity(supplierProduct.getMinOrderQuantity());
			product.setSupplyPrice(supplierProduct.getSupplyPrice());
		}
		return page;
	}

	
	/**
	 * 获取供应商之间分配的商品
	 *
	 * @param supplierId
	 * @param bySupplierId
	 * @param pageable
	 * @return
	 */
	@Override
	public Page<Product> getSupplierAssignProductsFormal(Long supplierId, Long bySupplierId, Pageable pageable , Long productCategoryId) {
		Page<Product> page = productDao.getSupplierAssignProductsFormal(supplierId , bySupplierId , pageable , productCategoryId);
		List<Product> products = page.getContent() ;
		for(Product product : products){
			SupplierProduct supplierProduct = supplierProductDao.getProduct(product.getId() , supplierId , bySupplierId , SupplierSupplier.Status.inTheSupply , null) ;
			product.setMinOrderQuantity(supplierProduct.getMinOrderQuantity());
		}
		return page ;
	}

	@Override
	public Page<Product> findPage(Type type, ProductCategory productCategory,
			Brand brand, Promotion promotion, Tag tag,
			Map<Attribute, String> attributeValueMap, BigDecimal startPrice,
			BigDecimal endPrice, Boolean isMarketable, Boolean isList,
			Boolean isTop, Boolean isOutOfStock, Boolean isStockAlert,
			Boolean hasPromotion, OrderType orderType, Pageable pageable,
			Supplier supplier, Boolean sourceFlag, Boolean branchFlag,String searchName) {
		return productDao.findPage(type, productCategory, brand, promotion, tag, attributeValueMap, startPrice, endPrice, isMarketable, isList, isTop, isOutOfStock, isStockAlert, hasPromotion, orderType, pageable, supplier, sourceFlag, branchFlag,searchName);
	}

	@Override
	public Page<NeedProduct> findPage(SupplyNeed supplyNeed, Pageable pageable,
			Long productCategoryId) {
		return productDao.findPage(supplyNeed, pageable, productCategoryId);
	}

	@Override
	public List<Product> supplyDistributionProducts(Need need,
			SupplierSupplier supplierSupplier) {
		return productDao.supplyDistributionProducts(need, supplierSupplier);
	}

	@Override
	public List<Product> supplyProducts(SupplierSupplier supplierSupplier) {
		return productDao.supplyProducts(supplierSupplier);
	}

	@Override
	public Integer getMinOrderQuantity(Shop shop,Long supplierId, SupplierType supplierType, Long relationId, Product product) {
		Integer minOrderQuantity=1;
		if (SupplierType.ONE.equals(supplierType) || SupplierType.THREE.equals(supplierType)){
			SupplyNeed supplyNeed=supplyNeedDao.find(relationId);
			NeedShopProduct needShopProduct=needShopProductDao.getNeedShopProduct(supplyNeed,shop,null,product);
			if (needShopProduct == null){
				minOrderQuantity = -1;
			}else {
				minOrderQuantity=needShopProduct.getMinOrderQuantity();
			}
		}else if (SupplierType.FOUR.equals(supplierType)){
			Supplier supplier=supplierDao.find(supplierId);
			NeedShopProduct needShopProduct=needShopProductDao.getNeedShopProduct(null,shop,supplier,product);
			if (needShopProduct == null){
				minOrderQuantity = -1;
			}else {
				minOrderQuantity=needShopProduct.getMinOrderQuantity();
			}
		}else if (SupplierType.TWO.equals(supplierType)){
			SupplierSupplier supplierSupplier=supplierSupplierDao.find(relationId);
			SupplierProduct supplierProduct=supplierProductDao.getSupplierProduct(supplierSupplier,product);
			if (supplierProduct == null){
				minOrderQuantity = -1;
			}else {
				minOrderQuantity=supplierProduct.getMinOrderQuantity();
			}
		}
		return minOrderQuantity;
	}

	@Override
	public Page<Product> findPageSmall(Supplier supplier, ProductCategory productCategory, String goodsName, Long[] ids, Pageable pageable) {
		return productDao.findPageSmall(supplier,productCategory,goodsName,ids,pageable);
	}


	@Override
	public List<Product> findList(String goodsSn, Supplier supplier) {
		return productDao.findList(goodsSn, supplier);
	}

	@Override
	public List<ProductSupplierDto> findByProductAndSupplier(Member member,
			String barCode, String goodsName, Types types, Long shopId) {
		return productDao.findByProductAndSupplier(member, barCode, goodsName, types, shopId);
	}

	@Override
	public List<ProductSupplierDto> findByProductAndSupplier(Member member,
			String searchName, Types types) {
		return productDao.findByProductAndSupplier(member, searchName, types);
	}

	@Override
	public List<ProductSupplierDto> findShopByProductAndSupplier(Member member,
			String barCode, Types types, Shop shop) {
		return productDao.findShopByProductAndSupplier(member, barCode, types, shop);
	}

}