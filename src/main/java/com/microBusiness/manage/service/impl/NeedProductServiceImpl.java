package com.microBusiness.manage.service.impl;

import java.util.List;

import javax.annotation.Resource;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import org.springframework.stereotype.Service;

import com.microBusiness.manage.dao.NeedProductDao;
import java.util.List;

import javax.annotation.Resource;

import com.microBusiness.manage.dao.NeedProductDao;
import com.microBusiness.manage.entity.Goods;
import com.microBusiness.manage.entity.NeedProduct;
import com.microBusiness.manage.entity.Product;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.entity.SupplyNeed;
import com.microBusiness.manage.entity.SupplyNeed.Status;
import com.microBusiness.manage.service.NeedProductService;

import org.springframework.stereotype.Service;

/**
 * Created by mingbai on 2017/2/7.
 * 功能描述：
 * 修改记录：
 */
@Service
public class NeedProductServiceImpl extends BaseServiceImpl<NeedProduct , Long> implements NeedProductService {

	@Resource
    private NeedProductDao needProductDao ;
	
	
    @Override
    public boolean saveProducts(SupplyNeed supplyNeed, List<NeedProduct> needProducts) {
        return false;
    }

	@Override
	public NeedProduct findNeedProduct(NeedProduct needProduct) {
		return needProductDao.findNeedProduct(needProduct);
	}
	
	@Override
	public List<NeedProduct> findNeedProductByProduct(Product product,Supplier supplier, List<Status> status) {
		return needProductDao.findNeedProductByProduct(product,supplier,status);
	}

	@Override
	public List<Goods> findByKerword(Long needId,Long supplierId,String keywords,Integer isMarketable) {
		return  needProductDao.findByKerword(needId,supplierId,keywords,isMarketable);
	}

	@Override
	public List<Goods> findByKerword(Long needId, Long supplierId, String keywords, Integer isMarketable, Pageable pageable) {
		return  needProductDao.findByKerword(needId,supplierId,keywords,isMarketable , pageable);
	}

	@Override
	public List<NeedProduct> findByParams(NeedProduct needProduct) {
		return needProductDao.findByParams(needProduct);
	}

	@Override
	public NeedProduct getProduct(Product product, Long supplierId, Long needId, SupplyNeed.Status status) {
		return needProductDao.getProduct(product , supplierId , needId , status);
	}

	@Override
	public Page<NeedProduct> findPage(SupplyNeed supplyNeed,Pageable pageable) {
		// TODO Auto-generated method stub
		return needProductDao.findPage(supplyNeed,pageable);
	}

	@Override
	public List<NeedProduct> findList(SupplyNeed supplyNeed) {
		return needProductDao.findList(supplyNeed);
	}
}
