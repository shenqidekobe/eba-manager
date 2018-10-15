package com.microBusiness.manage.service.impl;

import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.microBusiness.manage.dao.ProductDao;
import com.microBusiness.manage.dao.SupplierAssignRelationDao;
import com.microBusiness.manage.dao.SupplierNeedProductDao;
import com.microBusiness.manage.entity.Need;
import com.microBusiness.manage.entity.SupplierAssignRelation;
import com.microBusiness.manage.entity.SupplierNeedProduct;
import com.microBusiness.manage.entity.SupplierProduct;
import com.microBusiness.manage.entity.SupplierSupplier;
import com.microBusiness.manage.service.SupplierAssignRelationService;

@Service("supplierAssignRelationServiceImpl")
public class SupplierAssignRelationServiceImpl extends BaseServiceImpl<SupplierAssignRelation, Long> implements SupplierAssignRelationService{

	@Resource(name = "supplierAssignRelationDaoImpl")
	SupplierAssignRelationDao supplierAssignRelationDao;
	@Resource(name = "supplierNeedProductDaoImpl")
	SupplierNeedProductDao supplierNeedProductDao;
	@Resource
	private ProductDao productDao ;

	@Override
	public List<SupplierAssignRelation> findListBySupplier(SupplierSupplier supplierSupplier,Need need) {
		// TODO Auto-generated method stub
		return supplierAssignRelationDao.findListBySupplier(supplierSupplier,need);
	}

	@Override
	public boolean save(SupplierAssignRelation supplierAssignRelation, List<SupplierNeedProduct> supplierNeedProductList) {
		 try{
	            //处理关联关系
	            for(SupplierNeedProduct supplierNeedProduct:supplierNeedProductList ){
	            	supplierNeedProduct.setNeed(supplierAssignRelation.getNeed());
	            	supplierNeedProduct.setProducts(productDao.find(supplierNeedProduct.getProducts().getId()));
	            	supplierNeedProduct.setSupplyRelation(supplierAssignRelation.getSupplyRelation());
	            	supplierNeedProduct.setAssignRelation(supplierAssignRelation);
	            }
	            supplierAssignRelation.setSupplierNeedProducts(supplierNeedProductList);
	            if (supplierAssignRelation.getId() == null) {
	            	supplierAssignRelationDao.persist(supplierAssignRelation);
	            	supplierAssignRelationDao.flush();
	            }
	           
	            return true ;
	        }catch (Exception e){
	            e.printStackTrace();
	            return false;
	        }
	}

	@Override
	public boolean deleteNeed(SupplierAssignRelation supplierAssignRelation) {
		// TODO Auto-generated method stub
		 try{
			 if (supplierAssignRelation.getId() != null) {
				 for (SupplierNeedProduct sNeedProduct : supplierAssignRelation.getSupplierNeedProducts()) {
					 supplierNeedProductDao.remove(sNeedProduct);
				 }
			}
			 return true;
	        }catch (Exception e){
	            e.printStackTrace();
	            return false;
	        }
	}

}
