package com.microBusiness.manage.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.controller.admin.NeedSupplyController.NeedProductForm;
import com.microBusiness.manage.dao.NeedDao;
import com.microBusiness.manage.dao.NeedProductDao;
import com.microBusiness.manage.dao.NeedShopProductDao;
import com.microBusiness.manage.dao.ProductDao;
import com.microBusiness.manage.dao.ShopDao;
import com.microBusiness.manage.dao.StockGoodsDao;
import com.microBusiness.manage.dao.SupplyNeedDao;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.Need;
import com.microBusiness.manage.entity.NeedProduct;
import com.microBusiness.manage.entity.NeedShopProduct;
import com.microBusiness.manage.entity.Product;
import com.microBusiness.manage.entity.Shop;
import com.microBusiness.manage.entity.ShopType;
import com.microBusiness.manage.entity.StockGoods;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.entity.SupplierNeedProduct;
import com.microBusiness.manage.entity.SupplyNeed;
import com.microBusiness.manage.entity.SupplyNeed.AssignedModel;
import com.microBusiness.manage.entity.SupplyNeed.Status;
import com.microBusiness.manage.service.SupplyNeedService;

import org.apache.commons.collections.CollectionUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by mingbai on 2017/2/5.
 * 功能描述：
 * 修改记录：
 */
@Service
public class SupplyNeedServiceImpl extends BaseServiceImpl<SupplyNeed , Long> implements SupplyNeedService {
    @Resource
    private SupplyNeedDao supplyNeedDao ;

    @Resource
    private NeedProductDao needProductDao ;

    @Resource
    private NeedDao needDao ;

    @Resource
    private ProductDao productDao ;
    
    @Resource
    private NeedShopProductDao needShopProductDao;

    @Resource
    private StockGoodsDao stockGoodsDao;
    @Resource
    private ShopDao ShopDao;
    
    @Transactional(readOnly = true)
    @Override
    public Page<SupplyNeed> findPage(Pageable pageable, SupplyNeed supplyNeed, Date startDate, Date endDate, String searchName) {
        Page<SupplyNeed> result = supplyNeedDao.findPage(pageable , supplyNeed , startDate , endDate , searchName);
        return result;
    }

    @Override
    @Transactional
    public boolean save(SupplyNeed supplyNeed, List<NeedProduct> needProducts) {
        try{
        	
//        	DateTime startDate = new DateTime(supplyNeed.getStartDate());
//    		DateTime endDate = new DateTime(supplyNeed.getEndDate());
        	
            //保存供应关系
            Need need = needDao.find(supplyNeed.getNeed().getId());
            supplyNeed.setNeed(need);
            supplyNeed.setStatus(SupplyNeed.Status.SUPPLY);
    		/*if(startDate.isAfterNow()){
    			supplyNeed.setStatus(SupplyNeed.Status.WILLSUPPLY);
    		}else if(!startDate.isAfterNow() && !endDate.isBeforeNow()){
    			supplyNeed.setStatus(SupplyNeed.Status.SUPPLY);
    		}else{
    			supplyNeed.setStatus(SupplyNeed.Status.EXPIRED);
    		}*/
            //Set<NeedProduct> tempProducts = new HashSet<>(needProducts);
            //supplyNeed.setNeedProducts(tempProducts);
            supplyNeedDao.persist(supplyNeed);

            //处理关联关系
            for(NeedProduct needProduct:needProducts ){
            	Product product=productDao.find(needProduct.getProducts().getId());
                needProduct.setSupplyNeed(supplyNeed);
                needProduct.setProducts(product);
                needProductDao.persist(needProduct);
            }
            
            if (need.getShopType() == ShopType.direct) {
            	Shop shop=need.getShops().get(0);
            	for(NeedProduct needProduct:needProducts ){
            		Product product=productDao.find(needProduct.getProducts().getId());
            		NeedShopProduct needShopProduct=new NeedShopProduct();
    				needShopProduct.setShop(shop);
    				needShopProduct.setProducts(product);
    				needShopProduct.setSupplyNeed(supplyNeed);
    				needShopProduct.setMinOrderQuantity(needProduct.getMinOrderQuantity());
    				needShopProduct.setSupplyPrice(needProduct.getSupplyPrice());
    				needShopProductDao.persist(needShopProduct);
            	}
            	
            	// 分配的商品加入库存
     	       for (NeedProduct needProduct : needProducts) {
     	    	    Product product = productDao.find(needProduct.getProducts().getId());
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
                
			}
            return true ;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }

	@Override
	public SupplyNeed findSupplyNeed(SupplyNeed supplyNeed) {
		return supplyNeedDao.findSupplyNeed(supplyNeed);
	}

    @Override
    public SupplyNeed updateStatus(SupplyNeed supplyNeed) {
        if(null == supplyNeed.getId() || null == supplyNeed.getStatus()){
            return null ;
        }

        SupplyNeed pSupplyNeed = supplyNeedDao.find(supplyNeed.getId()) ;
        pSupplyNeed.setStatus(supplyNeed.getStatus());
        return pSupplyNeed;
    }

    @Override
    public boolean update(SupplyNeed supplyNeed, List<NeedProduct> needProducts) {
    	
        SupplyNeed cSupplyNeed = supplyNeedDao.find(supplyNeed.getId()) ;
        Need need=cSupplyNeed.getNeed();
        for(NeedProduct needProduct : cSupplyNeed.getNeedProducts()){
            needProductDao.remove(needProduct);
        }

        //处理关联关系
        for(NeedProduct needProduct:needProducts ){
            needProduct.setSupplyNeed(cSupplyNeed);
            needProduct.setProducts(productDao.find(needProduct.getProducts().getId()));
            needProductDao.persist(needProduct);

        }
        
        if (need.getShopType() == ShopType.direct) {
        	Shop shop=need.getShops().get(0);
        	//删除关系
        	List<NeedShopProduct> list=needShopProductDao.getList(cSupplyNeed,shop,null);
			for (NeedShopProduct needShopProduct :list) {
				needShopProductDao.remove(needShopProduct);
			}
        	
        	for(NeedProduct needProduct:needProducts ){
        		Product product=productDao.find(needProduct.getProducts().getId());
        		NeedShopProduct needShopProduct=new NeedShopProduct();
				needShopProduct.setShop(shop);
				needShopProduct.setProducts(product);
				needShopProduct.setSupplyNeed(cSupplyNeed);
				needShopProduct.setMinOrderQuantity(needProduct.getMinOrderQuantity());
				needShopProduct.setSupplyPrice(needProduct.getSupplyPrice());
				needShopProductDao.persist(needShopProduct);
        	}
            
		}

        return true;
    }

    @Override
	public SupplyNeed update(SupplyNeed entity) {
    	SupplyNeed before = supplyNeedDao.find(entity.getId()) ;
		before.setStartDate(entity.getStartDate());
		before.setEndDate(entity.getEndDate());

		DateTime startDate = new DateTime(before.getStartDate());
		DateTime endDate = new DateTime(before.getEndDate());
		if(startDate.isAfterNow()){
			before.setStatus(SupplyNeed.Status.WILLSUPPLY);
		}else if(!startDate.isAfterNow() && !endDate.isBeforeNow()){
			before.setStatus(SupplyNeed.Status.SUPPLY);
		}else{
			before.setStatus(SupplyNeed.Status.EXPIRED);
		}
		return before ;
	}

	@Override
	public List<SupplyNeed> findByDateList(Supplier supplier, Need need) {
		return supplyNeedDao.findByDateList(supplier, need);
	}
	
	@Override
	public List<SupplyNeed> findByDateList(Supplier supplier, Long[] needIds){
		return supplyNeedDao.findByDateList(supplier, needIds);
	}

	@Override
	public void dealExpiredSupply(Date compareDate, SupplyNeed.Status status) {
		supplyNeedDao.updateExpiredSupply(compareDate , status);
	}

	@Override
	public void dealWillSupplyToSupply(Date compareDate) {
		supplyNeedDao.dealWillSupplyToSupply(compareDate);
	}

	@Override
	public List<SupplyNeed> findByNeedSupplier(Long needId, Supplier supplier) {
		return supplyNeedDao.findByNeedSupplier(needId, supplier);
	}

    /**
     * 根据状态和企业查询 个体分配供应
     *
     * @param supplier
     * @param status
     * @param assignedModel
     * @return
     */
    @Override
    public List<SupplyNeed> findBySupplier(Supplier supplier, List<Status> status , SupplyNeed.AssignedModel assignedModel) {
        return supplyNeedDao.findBySupplier(supplier , status , assignedModel);
    }
    @Override
    public SupplyNeed findSupplyNeedOnSupply(SupplyNeed supplyNeed) {
    	return supplyNeedDao.findSupplyNeedOnSupply(supplyNeed);
    }

	@Override
	public void batchSave(Supplier supplier,AssignedModel assignedModel, Long[] needIds,
			NeedProductForm NeedProductForm, Integer noticeDay, Boolean openNotice) {
		 for (Long needId : needIds) {
         	Need need=needDao.find(needId);
         	SupplyNeed supplyNeed=new SupplyNeed();
         	supplyNeed.setSupplier(supplier);
         	supplyNeed.setNeed(need);
         	//supplyNeed.setStartDate(DateUtils.specifyDateZero(startDate));
         	//supplyNeed.setEndDate(DateUtils.specifyDatetWentyour(endDate));
         	supplyNeed.setAssignedModel(assignedModel);
         	supplyNeed.setOpenNotice(openNotice);
         	supplyNeed.setNoticeDay(noticeDay);
         	supplyNeed.setStatus(SupplyNeed.Status.SUPPLY);
         	
         	//DateTime startDates = new DateTime(supplyNeed.getStartDate());
    		//DateTime endDates = new DateTime(supplyNeed.getEndDate());
        	
            //保存供应关系
    		/*if(startDates.isAfterNow()){
    			supplyNeed.setStatus(SupplyNeed.Status.WILLSUPPLY);
    		}else if(!startDates.isAfterNow() && !endDates.isBeforeNow()){
    			supplyNeed.setStatus(SupplyNeed.Status.SUPPLY);
    		}else{
    			supplyNeed.setStatus(SupplyNeed.Status.EXPIRED);
    		}*/
            //Set<NeedProduct> tempProducts = new HashSet<>(needProducts);
            //supplyNeed.setNeedProducts(tempProducts);
            supplyNeedDao.persist(supplyNeed);

            //处理关联关系
            for(NeedProduct needProduct:NeedProductForm.getNeedProductList() ){
            	NeedProduct needProduct2=new NeedProduct(); 
            	needProduct2.setMinOrderQuantity(needProduct.getMinOrderQuantity());
            	needProduct2.setStatus(needProduct.getStatus());
            	needProduct2.setSupplyPrice(needProduct.getSupplyPrice());
            	needProduct2.setSupplyNeed(supplyNeed);
            	needProduct2.setProducts(productDao.find(needProduct.getProducts().getId()));
                needProductDao.persist(needProduct2);

            }
            //直营店 同时分配到店铺
            if (need.getShopType() == ShopType.direct) {
            	Shop shop=need.getShops().get(0);
            	for(NeedProduct needProduct:NeedProductForm.getNeedProductList() ){
            		Product product=productDao.find(needProduct.getProducts().getId());
            		NeedShopProduct needShopProduct=new NeedShopProduct();
    				needShopProduct.setShop(shop);
    				needShopProduct.setProducts(product);
    				needShopProduct.setSupplyNeed(supplyNeed);
    				needShopProduct.setMinOrderQuantity(needProduct.getMinOrderQuantity());
    				needShopProduct.setSupplyPrice(needProduct.getSupplyPrice());
    				needShopProductDao.persist(needShopProduct);
            	}
                
			}
		}
		
	}

	@Override
	public List<SupplyNeed> findByMember(Member member) {
		return supplyNeedDao.findByMember(member);
	}

	@Override
	public List<SupplyNeed> findByNeeds(List<Need> needs) {
		if (CollectionUtils.isEmpty(needs)) {
			return new ArrayList<>();
		}
		return supplyNeedDao.findByNeeds(needs);
	}

	@Override
	public boolean updateNeedSupply(SupplyNeed supplyNeed,List<NeedProduct> needProducts) {
		
		SupplyNeed cSupplyNeed = supplyNeedDao.find(supplyNeed.getId()) ;
		cSupplyNeed.setOpenNotice(supplyNeed.getOpenNotice());
        cSupplyNeed.setNoticeDay(supplyNeed.getNoticeDay());
        cSupplyNeed.setAssignedModel(supplyNeed.getAssignedModel());
		supplyNeedDao.merge(cSupplyNeed);
        Need need=cSupplyNeed.getNeed();
        for(NeedProduct needProduct : cSupplyNeed.getNeedProducts()){
            needProductDao.remove(needProduct);
        }
        
        //处理关联关系
        for(NeedProduct needProduct:needProducts ){
            needProduct.setSupplyNeed(cSupplyNeed);
            needProduct.setProducts(productDao.find(needProduct.getProducts().getId()));
            needProductDao.persist(needProduct);

        }
        
        if (need.getShopType() == ShopType.direct) {
        	Shop shop=need.getShops().get(0);
        	//删除关系
        	List<NeedShopProduct> list=needShopProductDao.getList(cSupplyNeed,shop,null);
			for (NeedShopProduct needShopProduct :list) {
				needShopProductDao.remove(needShopProduct);
			}
        	
        	for(NeedProduct needProduct:needProducts ){
        		Product product=productDao.find(needProduct.getProducts().getId());
        		NeedShopProduct needShopProduct=new NeedShopProduct();
				needShopProduct.setShop(shop);
				needShopProduct.setProducts(product);
				needShopProduct.setSupplyNeed(cSupplyNeed);
				needShopProduct.setMinOrderQuantity(needProduct.getMinOrderQuantity());
				needShopProduct.setSupplyPrice(needProduct.getSupplyPrice());
				needShopProductDao.persist(needShopProduct);
        	}
            
		}
		return true;
	}

    @Override
    public void refreshSupplyNeed(SupplyNeed supplyNeed) {
        supplyNeedDao.persist(supplyNeed);
    }

	@Override
	public void batchAllocationGoods(Product product, List<Long> shopIds,
			Supplier supplier) {
		for(Long shopId : shopIds) {
			Shop shop = ShopDao.find(shopId);
			List<NeedShopProduct> list=needShopProductDao.getNeedShopProductList(shop, supplier, product);
			if(list.size() == 0) {
				NeedShopProduct needShopProduct=new NeedShopProduct();
				needShopProduct.setShop(shop);
				needShopProduct.setSupplier(supplier);
				needShopProduct.setProducts(product);
				needShopProduct.setMinOrderQuantity(product.getMinOrderQuantity());
				needShopProduct.setSupplyPrice(product.getSupplyPrice());
				needShopProductDao.persist(needShopProduct);
			}
		
			// 分配的商品加入库存
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
		
	}

}
