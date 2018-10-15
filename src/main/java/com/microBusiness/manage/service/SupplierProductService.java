package com.microBusiness.manage.service;

import java.util.List;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.Product;
import com.microBusiness.manage.entity.SupplierProduct;
import com.microBusiness.manage.entity.SupplierSupplier;

public interface SupplierProductService extends BaseService<SupplierProduct, Long>{
    SupplierProduct getProduct(Long productId , Long supplierId , Long bySupplierId , SupplierSupplier.Status status , Long needId);
    
    SupplierProduct getSupplierProduct(SupplierSupplier supplierSupplier, Product product);
    
    Page<SupplierProduct> findPage(Pageable pageable , Long productCategoryId , SupplierSupplier supplierSupplier);
    
    /**
     * 
     * @Title: getSupplierProductList
     * @author: yuezhiwei
     * @date: 2018年3月9日下午2:31:21
     * @Description: 查询正式供应中已选择的商品
     * @return: List<SupplierProduct>
     */
    List<SupplierProduct> getSupplierProductList(SupplierSupplier supplierSupplier);
    
    /**
     * 
     * @Title: supplierProductList
     * @author: yuezhiwei
     * @date: 2018年3月10日下午5:43:46
     * @Description: TODO
     * @return: List<SupplierProduct>
     */
    List<SupplierProduct> supplierProductList(SupplierSupplier supplierSupplier , List<Long> productIds);

    /**
     * 查询商品是否在企业供应中，供应中和供应时间内
     * @param product
     * @return
     */
	List<SupplierProduct> findByProduct(Product product);
}
