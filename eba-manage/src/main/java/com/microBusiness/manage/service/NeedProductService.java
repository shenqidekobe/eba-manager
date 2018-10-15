package com.microBusiness.manage.service;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.Goods;
import com.microBusiness.manage.entity.Need;
import com.microBusiness.manage.entity.NeedProduct;
import com.microBusiness.manage.entity.Product;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.entity.SupplierNeedProduct;
import com.microBusiness.manage.entity.SupplierSupplier;
import com.microBusiness.manage.entity.SupplyNeed;
import com.microBusiness.manage.entity.SupplyNeed.Status;

import org.jsoup.Connection;

import java.util.List;
import java.util.Set;

/**
 * Created by mingbai on 2017/2/7.
 * 功能描述：
 * 修改记录：
 */
public interface NeedProductService extends BaseService<NeedProduct , Long> {
    boolean saveProducts(SupplyNeed supplyNeed , List<NeedProduct> needProducts);
    
    NeedProduct findNeedProduct(NeedProduct needProduct);

    List<Goods> findByKerword(Long needId,Long supplierId,String keywords,Integer isMarketable);

    List<Goods> findByKerword(Long needId, Long supplierId, String keywords, Integer isMarketable , Pageable pageable);


    List<NeedProduct> findByParams(NeedProduct needProduct);

    NeedProduct getProduct(Product product, Long supplierId , Long needId , SupplyNeed.Status status);

    List<NeedProduct> findNeedProductByProduct(Product product,Supplier supplier, List<Status> status);
    
    /**
	 * 分页查询个体供应的商品
	 * @param supplyNeed 个体供应
	 * @param pageable   分页参数
	 * @return
	 */
	Page<NeedProduct> findPage(SupplyNeed supplyNeed,Pageable pageable);
	
	/**
	 * 
	 * @Title: findList
	 * @author: yuezhiwei
	 * @date: 2018年3月26日下午2:46:38
	 * @Description: TODO
	 * @return: List<NeedProduct>
	 */
	List<NeedProduct> findList(SupplyNeed supplyNeed);
	

}
