package com.microBusiness.manage.dao;

import java.util.List;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.*;

public interface SupplierProductDao extends BaseDao<SupplierProduct, Long>{

    SupplierProduct getProduct(Product product , Supplier supplier , Supplier bySupplier , SupplierSupplier.Status status , Need need);

    SupplierProduct getProduct(Long productId , Long supplierId , Long bySupplierId , SupplierSupplier.Status status , Long needId);

    SupplierProduct getSupplierProduct(SupplierSupplier supplierSupplier,Product product);
    
    SupplierProduct findSupplierProduct(SupplierSupplier supplierSupplier, Product product);
    
    Page<SupplierProduct> findPage(Pageable pageable , Long productCategoryId , SupplierSupplier supplierSupplier);
    
    /**
     * 
     * @Title: getSupplierProductList
     * @author: yuezhiwei
     * @date: 2018年3月9日下午2:32:56
     * @Description: 查询正式供应中已经选择的商品
     * @return: List<SupplierProduct>
     */
    List<SupplierProduct> getSupplierProductList(SupplierSupplier supplierSupplier);
    
    /**
     * 
     * @Title: supplierProductList
     * @author: yuezhiwei
     * @date: 2018年3月10日下午5:44:31
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
