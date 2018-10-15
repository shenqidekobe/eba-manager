/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.dao.impl;

import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.OrderItemDao;
import com.microBusiness.manage.entity.Order;
import com.microBusiness.manage.entity.OrderItem;
import com.microBusiness.manage.entity.Product;
import com.microBusiness.manage.entity.ProxyUser;

@Repository("orderItemDaoImpl")
public class OrderItemDaoImpl extends BaseDaoImpl<OrderItem, Long> implements OrderItemDao {

	@Override
	public OrderItem getOrderItemByProduct(Order order, Product product) {
		if (order == null) {
			return null;
		}
		try {
			String jpql = "select orderItem from OrderItem orderItem where orderItem.order =:order and orderItem.product =:product";
			return entityManager.createQuery(jpql, OrderItem.class).setParameter("order", order).setParameter("product", product).getSingleResult();
		} catch (NoResultException e) {
			e.printStackTrace();
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<OrderItem> findPageForGroupGoods(Pageable pageable, ProxyUser proxyUser, Date startDate, Date endDate) {
		try {
			StringBuffer findSql = new StringBuffer("select orderItem.* from xx_order_item orderItem  ");
			findSql.append(" join xx_order orders on orders.id = orderItem.orders ");
			findSql.append(" where 1=1 ");
			if(proxyUser != null) {
				findSql.append(" and orders.child_member=:childMember");
			}
			if(startDate != null){
				findSql.append(" and orders.create_date>=:startDate");
			}
			if(endDate != null){
				findSql.append(" and orders.create_date<=:endDate");
			}
			findSql.append(" group by orderItem.product ");
			Query query = entityManager.createNativeQuery(findSql.toString() , OrderItem.class);
			if(proxyUser != null) {
				query.setParameter("childMember" , proxyUser.getChildMember()) ;
			}
			if(startDate != null) {
				query.setParameter("startDate" , startDate) ;
			}
			if(endDate != null) {
				query.setParameter("endDate" , endDate) ;
			}
			List<OrderItem> list = query.getResultList();
			return list;
		} catch (NoResultException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public int sumPageForGroupGoods(ProxyUser proxyUser, Date startDate, Date endDate) {
		try {
			StringBuffer findSql = new StringBuffer("select sum(orderItem.quantity) as quantity from xx_order_item orderItem  ");
			findSql.append(" join xx_order orders on orders.id = orderItem.orders ");
			findSql.append(" where 1=1 ");
			if(proxyUser != null) {
				findSql.append(" and orders.child_member=:childMember");
			}
			if(startDate != null){
				findSql.append(" and orders.create_date>=:startDate");
			}
			if(endDate != null){
				findSql.append(" and orders.create_date<=:endDate");
			}
			Query query = entityManager.createNativeQuery(findSql.toString() , OrderItem.class) ;
			if(proxyUser != null) {
				query.setParameter("childMember" , proxyUser.getChildMember()) ;
			}
			if(startDate != null) {
				query.setParameter("startDate" , startDate) ;
			}
			if(endDate != null) {
				query.setParameter("endDate" , endDate) ;
			}
			return query.getFirstResult();
		} catch (NoResultException e) {
			e.printStackTrace();
			return 0;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<OrderItem> findPageForGroupGoods(Pageable pageable, List<ProxyUser> proxyUserList, Date startDate,
			Date endDate) {
		try {
			StringBuffer findSql = new StringBuffer("select orderItem.id, orderItem.name, orderItem.product, sum(orderItem.quantity) as quantity from xx_order_item orderItem  ");
			findSql.append(" where 1=1 ");
			StringBuffer ids = new StringBuffer();
			if(proxyUserList != null) {
				for (ProxyUser proxyUser : proxyUserList) {
					ids.append(proxyUser.getId());
					ids.append(",");
				}
				findSql.append(" and orderItem.proxy_user in (:ids)");
			}
			if(startDate != null){
				findSql.append(" and orders.create_date>=:startDate");
			}
			if(endDate != null){
				findSql.append(" and orders.create_date<=:endDate");
			}
			findSql.append(" group by orderItem.product ");
			Query query = entityManager.createNativeQuery(findSql.toString() , OrderItem.class) ;
			if(ids.length() > 0) {
				query.setParameter("ids" , ids) ;
			}
			if(startDate != null) {
				query.setParameter("startDate" , startDate) ;
			}
			if(endDate != null) {
				query.setParameter("endDate" , endDate) ;
			}
			List<OrderItem> list = query.getResultList();
			return list;
		} catch (NoResultException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public int sumPageForGroupGoods(List<ProxyUser> proxyUserList, Date startDate, Date endDate) {
		try {
			StringBuffer findSql = new StringBuffer("select sum(orderItem.quantity) as orderItem.quantity from xx_order_item orderItem  ");
			findSql.append(" where 1=1 ");
			StringBuffer ids = new StringBuffer();
			if(proxyUserList != null) {
				for (ProxyUser proxyUser : proxyUserList) {
					ids.append(proxyUser.getId());
					ids.append(",");
				}
				findSql.append(" and orderItem.proxy_user in (:ids)");
			}
			if(startDate != null){
				findSql.append(" and orders.create_date>=:startDate");
			}
			if(endDate != null){
				findSql.append(" and orders.create_date<=:endDate");
			}
			Query query = entityManager.createNativeQuery(findSql.toString() , OrderItem.class) ;
			if(ids.length() > 0) {
				query.setParameter("ids" , ids) ;
			}
			if(startDate != null) {
				query.setParameter("startDate" , startDate) ;
			}
			if(endDate != null) {
				query.setParameter("endDate" , endDate) ;
			}
			return query.getFirstResult();
		} catch (NoResultException e) {
			e.printStackTrace();
			return 0;
		}
	}


	

}