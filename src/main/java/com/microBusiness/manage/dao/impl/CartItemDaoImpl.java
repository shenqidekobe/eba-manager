/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.dao.impl;

import javax.persistence.Query;
import javax.persistence.criteria.*;

import com.microBusiness.manage.entity.*;
import org.springframework.stereotype.Repository;

import com.microBusiness.manage.dao.CartItemDao;

import java.util.List;

@Repository("cartItemDaoImpl")
public class CartItemDaoImpl extends BaseDaoImpl<CartItem, Long> implements CartItemDao {

	@Override
	public CartItem deleteByCartItem(CartItem cartItem) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaDelete<CartItem> criteriaDelete = criteriaBuilder.createCriteriaDelete(CartItem.class);
        Root<CartItem> root = criteriaDelete.from(CartItem.class);
        Predicate restrictions = criteriaBuilder.conjunction();
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("product") , cartItem.getProduct()));
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("cart") , cartItem.getCart()));

        if(null != cartItem.getSupplier()){
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("supplier") , cartItem.getSupplier()));
        }

        if(null != cartItem.getSupplyType()){
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("supplyType") , cartItem.getSupplyType()));
        }
        if(null != cartItem.getSupplyNeed()){
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("supplyNeed") , cartItem.getSupplyNeed()));
        }
        if(null != cartItem.getSupplierSupplier()){
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("supplierSupplier") , cartItem.getSupplierSupplier()));
        }

        criteriaDelete.where(restrictions);
        Query query = entityManager.createQuery(criteriaDelete);
        query.executeUpdate();
        return cartItem;
	}

    @Override
    public List<CartItem> getCartItems(Cart cart, Shop shop, Types types, SupplyNeed supplyNeed, SupplierSupplier supplierSupplier,Supplier supplier) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<CartItem> criteriaQuery = criteriaBuilder.createQuery(CartItem.class);
        Root<CartItem> root = criteriaQuery.from(CartItem.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("cart") , cart));
        //restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("shop") , shop));
        //restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("types") , types));
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("supplier") , supplier));
//        if (supplier != null){
//            restrictions = criteriaBuilder.and(restrictions,root.get("supplyNeed").isNull(),root.get("supplierSupplier").isNull(), criteriaBuilder.equal(root.get("supplier") , supplier));
//        }else {
//            if (supplyNeed != null){
//                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("supplyNeed") , supplyNeed));
//            }
//            if (supplierSupplier != null){
//                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("supplierSupplier") , supplierSupplier));
//            }
//        }
        criteriaQuery.where(restrictions);
        return super.findList(criteriaQuery, null, null, null, null);
    }

    @Override
    public CartItem getCartItem(Cart cart, Shop shop, Types types, SupplyNeed supplyNeed, SupplierSupplier supplierSupplier, Supplier supplier, Product product) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<CartItem> criteriaQuery = criteriaBuilder.createQuery(CartItem.class);
        Root<CartItem> root = criteriaQuery.from(CartItem.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("cart") , cart));
        //restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("shop") , shop));
        //restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("types") , types));
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("product") , product));
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("supplier") , supplier));
//        if (supplier != null){
//            restrictions = criteriaBuilder.and(restrictions,root.get("supplyNeed").isNull(),root.get("supplierSupplier").isNull(), criteriaBuilder.equal(root.get("supplier") , supplier));
//        }else {
//            if (supplyNeed != null){
//                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("supplyNeed") , supplyNeed));
//            }
//            if (supplierSupplier != null){
//                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("supplierSupplier") , supplierSupplier));
//            }
//        }
        criteriaQuery.where(restrictions);
        List<CartItem> list=super.findList(criteriaQuery, null, null, null, null);
        if (list!=null && list.size() >0 ){
            return list.get(0);
        }
        return null;

    }

}