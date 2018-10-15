package com.microBusiness.manage.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.ProductDao;
import com.microBusiness.manage.dao.SupplierAssignRelationDao;
import com.microBusiness.manage.dao.SupplierNeedProductDao;
import com.microBusiness.manage.dao.SupplierProductDao;
import com.microBusiness.manage.dao.SupplierSupplierDao;
import com.microBusiness.manage.entity.Need;
import com.microBusiness.manage.entity.NeedProduct;
import com.microBusiness.manage.entity.Product;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.entity.SupplierAssignRelation;
import com.microBusiness.manage.entity.SupplierNeedProduct;
import com.microBusiness.manage.entity.SupplierProduct;
import com.microBusiness.manage.entity.SupplierSupplier;
import com.microBusiness.manage.entity.SupplierSupplier.Status;
import com.microBusiness.manage.entity.SupplyNeed;
import com.microBusiness.manage.service.SupplierAssignRelationService;
import com.microBusiness.manage.service.SupplierSupplierService;

@Service("supplierSupplierServiceImpl")
public class SupplierSupplierServiceImpl extends BaseServiceImpl<SupplierSupplier, Long> implements SupplierSupplierService{
	@Resource(name = "supplierSupplierDaoImpl")
	private SupplierSupplierDao supplierSupplierDao;
	@Resource(name = "supplierProductDaoImpl")
	private SupplierProductDao supplierProductDao;
	@Resource
	private ProductDao productDao ;
	@Resource
	private SupplierAssignRelationDao supplierAssignRelationDao;
	@Resource
	private SupplierNeedProductDao supplierNeedProductDao;

	@Override
	public Page<SupplierSupplier> findPage(Pageable pageable, Supplier supplier, Supplier bySupplier, SupplierSupplier.Status status , String searchName) {
		// TODO Auto-generated method stub
		return supplierSupplierDao.findPage(pageable,supplier,bySupplier,status,searchName);
	}

	@Override
	public List<SupplierSupplier> findByDateList(Supplier supplier, Supplier bySupplier, Date startDate, Date endDate,List<SupplierSupplier.Status> status) {
		// TODO Auto-generated method stub
		return supplierSupplierDao.findByDateList(supplier,bySupplier,startDate,endDate,status);
	}

	@Override
	public boolean save(SupplierSupplier supplierSupplier, List<SupplierProduct> supplierProductList) {
		 try{
	            supplierSupplierDao.persist(supplierSupplier);

	            //处理关联关系
	            for(SupplierProduct supplierProduct:supplierProductList ){
	            	supplierProduct.setSupplyRelation(supplierSupplier);
	            	supplierProduct.setProducts(productDao.find(supplierProduct.getProducts().getId()));
	            	supplierProductDao.persist(supplierProduct);
	            }
	            return true ;
	        }catch (Exception e){
	            e.printStackTrace();
	            return false;
	        }
	}

	@Override
	public SupplierSupplier updateStatus(SupplierSupplier supplierSupplier) {
		if (supplierSupplier.getId()==null || supplierSupplier.getStatus() == null) {
			return null;
		}
		
		SupplierSupplier bysupplierSupplier=supplierSupplierDao.find(supplierSupplier.getId());
		if(supplierSupplier.getStatus() == SupplierSupplier.Status.rejected ||
				supplierSupplier.getStatus() == SupplierSupplier.Status.suspendSupply ||
					(supplierSupplier.getStatus() == SupplierSupplier.Status.inTheSupply && bysupplierSupplier.getStatus() == SupplierSupplier.Status.suspendSupply) ) {
			
			bysupplierSupplier.setStatus(supplierSupplier.getStatus());
			return bysupplierSupplier;
		}
		
		Supplier supplier = bysupplierSupplier.getSupplier();
		Supplier bySupplier = bysupplierSupplier.getBySupplier();
		List<SupplierSupplier.Status> status=new ArrayList<>();
		status.add(SupplierSupplier.Status.inTheSupply);
		status.add(SupplierSupplier.Status.suspendSupply);
		//status.add(SupplierSupplier.Status.willSupply);
		List<SupplierSupplier> supplierSuppliers = supplierSupplierDao.findByDateList(supplier, bySupplier, null, null, status);
		if(supplierSuppliers.size() > 0) {
			SupplierSupplier oldSupplierSupplier = supplierSuppliers.get(0);
			List<SupplierAssignRelation> assignRelations = supplierAssignRelationDao.findListBySupplier(oldSupplierSupplier, null);
			for(SupplierAssignRelation sar : assignRelations) {
				//查询已经分配的商品
				List<Product> oldProducts = productDao.supplyDistributionProducts(sar.getNeed(), oldSupplierSupplier);
				
				List<Product> newProducts = productDao.supplyProducts(bysupplierSupplier);
				
				List<Product> products = (List<Product>) CollectionUtils.retainAll(oldProducts, newProducts);
				List<Long> ids = new ArrayList<Long>();
				for(Product pro : products) {
					ids.add(pro.getId());
				}
				if(ids.size() > 0) {
					List<SupplierProduct> supplierProducts = supplierProductDao.supplierProductList(bysupplierSupplier, ids);
					SupplierAssignRelation assignRelation = new SupplierAssignRelation();
					assignRelation.setNeed(sar.getNeed());
					assignRelation.setSupplyRelation(bysupplierSupplier);
					
					List<SupplierNeedProduct> newSupplierNeedProductList=new ArrayList<>();
					for(SupplierProduct sp : supplierProducts) {
						SupplierNeedProduct newSupplierNeedProduct=new SupplierNeedProduct();
				       	newSupplierNeedProduct.setNeed(sar.getNeed());
				       	newSupplierNeedProduct.setSupplyPrice(sp.getSupplyPrice());
				       	newSupplierNeedProduct.setProducts(sp.getProducts());
				       	newSupplierNeedProduct.setSupplyRelation(bysupplierSupplier);
				       	newSupplierNeedProduct.setAssignRelation(assignRelation);
				       	//supplierNeedProductDao.persist(newSupplierNeedProduct);
				       	newSupplierNeedProductList.add(newSupplierNeedProduct);
					}
					
					assignRelation.setSupplierNeedProducts(newSupplierNeedProductList);
					supplierAssignRelationDao.persist(assignRelation);
			       	supplierAssignRelationDao.flush();
				}
				
			}
			oldSupplierSupplier.setStatus(SupplierSupplier.Status.expired);
			
		}
		
		bysupplierSupplier.setStatus(supplierSupplier.getStatus());
		
		/*DateTime startDate = new DateTime(bysupplierSupplier.getStartDate());
		DateTime endDate = new DateTime(bysupplierSupplier.getEndDate());
		// FIXME: 2017/4/25 状态添加导致业务逻辑有问题
		if(supplierSupplier.getStatus().equals(SupplierSupplier.Status.inTheSupply)){
			if(startDate.isAfterNow()){
				bysupplierSupplier.setStatus(SupplierSupplier.Status.willSupply);
			}else if(!startDate.isAfterNow() && !endDate.isBeforeNow()){
				//供应中
				bysupplierSupplier.setStatus(SupplierSupplier.Status.inTheSupply);
			}else{
				//(endDate.isBeforeNow()) 过期
				bysupplierSupplier.setStatus(SupplierSupplier.Status.expired);
			}
		}else{

			bysupplierSupplier.setStatus(supplierSupplier.getStatus());
		}*/

		return bysupplierSupplier;
	}

	/**
	 * 处理过期的供应关系
	 *
	 * @param compareDate
	 * @param status
	 */
	@Override
	public void dealExpiredSupply(Date compareDate, SupplierSupplier.Status status) {
        supplierSupplierDao.updateExpiredSupply(compareDate , status);
	}

	@Override
	public SupplierSupplier update(SupplierSupplier entity) {
		SupplierSupplier before = supplierSupplierDao.find(entity.getId()) ;
		before.setStartDate(entity.getStartDate());
		before.setEndDate(entity.getEndDate());
		before.setNoticeDay(entity.getNoticeDay());
		before.setOpenNotice(entity.getOpenNotice());

//		DateTime startDate = new DateTime(before.getStartDate());
//		DateTime endDate = new DateTime(before.getEndDate());
//		//未开始
//		if(startDate.isAfterNow()){
//			before.setStatus(SupplierSupplier.Status.willSupply);
//		}else if(!startDate.isAfterNow() && !endDate.isBeforeNow()){
//			//供应中
//			before.setStatus(SupplierSupplier.Status.inTheSupply);
//		}else{
//			//(endDate.isBeforeNow()) 过期
//			before.setStatus(SupplierSupplier.Status.expired);
//		}
		return before ;
	}

	/**
	 * 处理未开始的未供应中
	 *
	 * @param compareDate
	 */
	@Override
	public void dealWillSupplyToSupply(Date compareDate) {
		supplierSupplierDao.dealWillSupplyToSupply(compareDate);
	}

	/**
	 * 供应分配列表
	 */
	@Override
	public Page<SupplierSupplier> supplyDistributionList(Pageable pageable,
			Supplier supplier, Supplier bySupplier, Status status,
			String searchName) {
		return supplierSupplierDao.supplyDistributionList(pageable, supplier, bySupplier, status, searchName);
	}

	/**
	 * 获取供应关系
	 * @param bySupplier
	 * @param supplier
	 * @param status
	 * @return
	 */
	@Override
	public SupplierSupplier getSupplierSupplier(Supplier bySupplier, Supplier supplier,Date orderTime,List<SupplierSupplier.Status> status) {
		return supplierSupplierDao.getSupplierSupplier(bySupplier,supplier,orderTime,status);
	}

	@Override
	public List<SupplierSupplier> getSupplierSupplierList(Supplier bySupplier, Supplier supplier) {
		return supplierSupplierDao.getSupplierSupplierList(bySupplier, supplier);
	}

	@Override
	public List<SupplierSupplier> getListBySupplier(Supplier bySupplier, List<Status> status) {
		return supplierSupplierDao.getListBySupplier(bySupplier,status);
	}

	@Override
	public boolean updateUnconfirmed(SupplierSupplier supplierSupplier,
			List<SupplierProduct> supplierProductList) {
		try{
            supplierSupplierDao.persist(supplierSupplier);
            
            List<SupplierProduct> supplierProducts = supplierProductDao.getSupplierProductList(supplierSupplier);
            for(SupplierProduct sp : supplierProducts) {
            	supplierProductDao.remove(sp);
            }

            //处理关联关系
            for(SupplierProduct supplierProduct:supplierProductList ){
            	supplierProduct.setSupplyRelation(supplierSupplier);
            	supplierProduct.setProducts(productDao.find(supplierProduct.getProducts().getId()));
            	supplierProductDao.persist(supplierProduct);
            }
            return true ;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
	}

	@Override
	public Integer getSupplyBatch(Supplier supplier, Supplier bySupplier) {
		List<SupplierSupplier> supplierSuppliers = supplierSupplierDao.getSupplierSupplierList(bySupplier, supplier);
		Integer batch = supplierSuppliers.size() + 1;
		return batch;
	}

	@Override
	public void refreshSupplierSupplier(SupplierSupplier supplierSupplier) {
		supplierSupplierDao.persist(supplierSupplier);
	}

}
