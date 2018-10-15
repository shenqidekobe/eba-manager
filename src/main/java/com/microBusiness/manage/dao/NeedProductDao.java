package com.microBusiness.manage.dao;

import java.util.List;
import java.util.Set;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.*;
import com.microBusiness.manage.entity.SupplyNeed.Status;

/**
 * Created by mingbai on 2017/1/22.
 * 功能描述：
 * 修改记录：
 */
public interface NeedProductDao extends BaseDao<NeedProduct , Long> {
	
	NeedProduct findNeedProduct(NeedProduct needProduct);

	List<Goods> findByKerword(Long needId,Long supplierId,String keywords,Integer isMarketable);

	List<Goods> findByKerword(Long needId, Long supplierId, String keywords, Integer isMarketable , Pageable pageable);

	/**
	 *
	 * @param needProduct
	 * @return
	 */
	List<NeedProduct> findByParams(NeedProduct needProduct);

	NeedProduct getProduct(Product product, Long supplierId , Long needId , SupplyNeed.Status status);

	/**
	 * 根据个体客户供应关系和商品获取供应的商品信息
	 * @param supplyNeed 个体客户供应关系
	 * @param product 商品
	 * @return
	 */
	NeedProduct findByNeedSupplier(SupplyNeed supplyNeed,Product product);
	
	/**
	 * 根据个体客户供应关系和product查询供应的product信息
	 * @param supplyNeed
	 * @param products
	 * @return
	 */
	List<NeedProduct> getNeeedProducts(SupplyNeed supplyNeed,Set<Product> products);

	List<NeedProduct> findNeedProductByProduct(Product product,Supplier supplier, List<Status> status);

	/**
	 * 分页查询个体供应的商品
	 * @param supplyNeed 个体供应
	 * @param pageable   分页参数
	 * @return
	 */
	Page<NeedProduct> findPage(SupplyNeed supplyNeed, Pageable pageable);

	
	/**
	 * 
	 * @Title: findList
	 * @author: yuezhiwei
	 * @date: 2018年3月26日下午2:47:47
	 * @Description: TODO
	 * @return: List<NeedProduct>
	 */
	List<NeedProduct> findList(SupplyNeed supplyNeed);

	
	

}
