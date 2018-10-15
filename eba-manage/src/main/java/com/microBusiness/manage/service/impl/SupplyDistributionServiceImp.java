package com.microBusiness.manage.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.microBusiness.manage.dao.NeedDao;
import com.microBusiness.manage.dao.ProductDao;
import com.microBusiness.manage.dao.StockGoodsDao;
import com.microBusiness.manage.dao.SupplierAssignRelationDao;
import com.microBusiness.manage.dao.SupplierNeedProductDao;
import com.microBusiness.manage.entity.Need;
import com.microBusiness.manage.entity.Product;
import com.microBusiness.manage.entity.Shop;
import com.microBusiness.manage.entity.StockGoods;
import com.microBusiness.manage.entity.SupplierAssignRelation;
import com.microBusiness.manage.entity.SupplierNeedProduct;
import com.microBusiness.manage.entity.SupplierSupplier;
import com.microBusiness.manage.service.SupplierAssignRelationService;
import com.microBusiness.manage.service.SupplyDistributionService;

import org.springframework.stereotype.Service;

@Service("supplyDistributionServiceImp")
public class SupplyDistributionServiceImp extends BaseServiceImpl<SupplierAssignRelation, Long> implements SupplyDistributionService {

	@Resource(name = "supplierAssignRelationServiceImpl")
	private SupplierAssignRelationService supplierAssignRelationService;
	@Resource
	private NeedDao needDao;
	@Resource(name = "supplierNeedProductDaoImpl")
	SupplierNeedProductDao supplierNeedProductDao;
	@Resource(name = "supplierAssignRelationDaoImpl")
	SupplierAssignRelationDao supplierAssignRelationDao;
	@Resource
	private ProductDao productDao ;
	
	@Resource
	private StockGoodsDao stockGoodsDao;
	
	@Override
	public boolean supplyDistribution(SupplierSupplier supplierSupplier,
			Need need, SupplierAssignRelation supplierAssignRelation,
			List<SupplierNeedProduct> supplierNeedProductList) {

			if (supplierAssignRelation.getId() != null) {
				 for (SupplierNeedProduct sNeedProduct : supplierAssignRelation.getSupplierNeedProducts()) {
					supplierNeedProductDao.remove(sNeedProduct);
				}
			}
       
	       List<SupplierNeedProduct> newSupplierNeedProductList=new ArrayList<>();
	     //处理关联关系
	       for(SupplierNeedProduct supplierNeedProduct:supplierNeedProductList ){
	       	SupplierNeedProduct newSupplierNeedProduct=new SupplierNeedProduct();
	       	newSupplierNeedProduct.setNeed(supplierAssignRelation.getNeed());
	       	newSupplierNeedProduct.setSupplyPrice(supplierNeedProduct.getSupplyPrice());
	       	newSupplierNeedProduct.setProducts(productDao.find(supplierNeedProduct.getProducts().getId()));
	       	newSupplierNeedProduct.setSupplyRelation(supplierAssignRelation.getSupplyRelation());
	       	newSupplierNeedProduct.setAssignRelation(supplierAssignRelation);
	       	newSupplierNeedProductList.add(newSupplierNeedProduct);
	       }
	       supplierAssignRelation.setSupplierNeedProducts(newSupplierNeedProductList);
	       if (supplierAssignRelation.getId() == null) {
	       	supplierAssignRelationDao.persist(supplierAssignRelation);
	       	supplierAssignRelationDao.flush();
	       }
	       
	       // 分配的商品加入库存
	       for (SupplierNeedProduct supplierNeedProduct : newSupplierNeedProductList) {
	    	    Product product = productDao.find(supplierNeedProduct.getProducts().getId());
	    	    Shop shop = need.getShops().get(0);
				StockGoods stockGoods = stockGoodsDao.findByProduct(product, shop);
				if (stockGoods == null) {
					StockGoods stockGoodsNew = new StockGoods();
					stockGoodsNew.setProduct(product);
					stockGoodsNew.setActualStock(0);
					stockGoodsNew.setStatus(StockGoods.Status.hide);
					stockGoodsNew.setShop(shop);

					stockGoodsDao.persist(stockGoodsNew);
				}
	       }
	       
		return true;
	}
	
	@Override
	public boolean supplyDistribution(SupplierSupplier supplierSupplier,
			Long[] needId, List<SupplierNeedProduct> supplierNeedProductList) {
		
		for(int i=0 ; i < needId.length ; i++) {
			Need need=needDao.find(needId[i]);
			List<SupplierAssignRelation> supplierAssignRelations=supplierAssignRelationService.findListBySupplier(supplierSupplier, need);
            SupplierAssignRelation supplierAssignRelation=null;
            if (supplierAssignRelations.size() > 0) {
            	supplierAssignRelation=supplierAssignRelations.get(0);
			}else {
				supplierAssignRelation=new SupplierAssignRelation();
				supplierAssignRelation.setSupplyRelation(supplierSupplier);
				supplierAssignRelation.setNeed(need);
			}
            
            if (supplierAssignRelation.getId() != null) {
				 for (SupplierNeedProduct sNeedProduct : supplierAssignRelation.getSupplierNeedProducts()) {
					 supplierNeedProductDao.remove(sNeedProduct);
				 }
			}
            
            List<SupplierNeedProduct> newSupplierNeedProductList=new ArrayList<>();
          //处理关联关系
            for(SupplierNeedProduct supplierNeedProduct:supplierNeedProductList ){
            	SupplierNeedProduct newSupplierNeedProduct=new SupplierNeedProduct();
            	newSupplierNeedProduct.setNeed(supplierAssignRelation.getNeed());
            	newSupplierNeedProduct.setSupplyPrice(supplierNeedProduct.getSupplyPrice());
            	newSupplierNeedProduct.setProducts(productDao.find(supplierNeedProduct.getProducts().getId()));
            	newSupplierNeedProduct.setSupplyRelation(supplierAssignRelation.getSupplyRelation());
            	newSupplierNeedProduct.setAssignRelation(supplierAssignRelation);
            	newSupplierNeedProductList.add(newSupplierNeedProduct);
            }
            supplierAssignRelation.setSupplierNeedProducts(newSupplierNeedProductList);
            if (supplierAssignRelation.getId() == null) {
            	supplierAssignRelationDao.persist(supplierAssignRelation);
            	supplierAssignRelationDao.flush();
            }
            
            
//            supplierAssignRelationService.deleteNeed(supplierAssignRelation);
//            supplierAssignRelationService.save(supplierAssignRelation,supplierNeedProductList);
		}
		return true;
	}

}
