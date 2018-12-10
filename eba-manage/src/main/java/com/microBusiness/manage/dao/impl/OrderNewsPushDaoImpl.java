package com.microBusiness.manage.dao.impl;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.OrderNewsPushDao;
import com.microBusiness.manage.entity.Need;
import com.microBusiness.manage.entity.Order;
import com.microBusiness.manage.entity.OrderNewsPush;
import com.microBusiness.manage.entity.OrderNewsPush.NoticeObject;
import com.microBusiness.manage.entity.OrderNewsPush.OrderStatus;
import com.microBusiness.manage.entity.OrderNewsPush.Status;
import com.microBusiness.manage.entity.Supplier;

@Repository("orderNewsPushDaoImpl")
public class OrderNewsPushDaoImpl extends BaseDaoImpl<OrderNewsPush, Long> implements OrderNewsPushDao {

	@Override
	public Page<OrderNewsPush> findPageOrder(Supplier supplier , OrderNewsPush.Status status,
			Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<OrderNewsPush> criteriaQuery = criteriaBuilder.createQuery(OrderNewsPush.class);
		Root<OrderNewsPush> root = criteriaQuery.from(OrderNewsPush.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if(null != supplier) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("supplier"), supplier));
		}
		if(null != status) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("status"), status));
		}
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery, pageable);
	}

	@Override
	public Page<OrderNewsPush> findPageByPurchaseOrder(Supplier supplier,
			Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<OrderNewsPush> criteriaQuery = criteriaBuilder.createQuery(OrderNewsPush.class);
		Root<OrderNewsPush> root = criteriaQuery.from(OrderNewsPush.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if(supplier != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("supplier"), supplier));
		}
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery, pageable);
	}

	@Override
	public boolean updateByOrder(Supplier supplier) {
		if (null == supplier) {
			return false;
		}
		String sql = "update OrderNewsPush set status = 1 where supplier =:supplier and status = 0 and mark != 1";
		int ex = entityManager.createQuery(sql).setParameter("supplier", supplier).executeUpdate();
		return ex > 0;
		
	}

	@Override
	public boolean updateByPurchase(Supplier supplier) {
		if (null == supplier) {
			return false;
		}
		String sql = "update OrderNewsPush set purchaseViewStatus = 1 where buyers =:supplier and purchaseViewStatus = 0 and mark != 0";
		int ex = entityManager.createQuery(sql).setParameter("supplier", supplier).executeUpdate();
		return ex > 0;
	}

	@Override
	public OrderNewsPush addOrderNewPush(Supplier supplier, Order order,
			OrderStatus orderStatus, Need need, String send, String receive, OrderNewsPush.NoticeObject noticeObject) {
		OrderNewsPush newsPush = new OrderNewsPush();
		newsPush.setOrder(order);
		newsPush.setNeed(need);
		newsPush.setSupplier(supplier);
		newsPush.setOrderStatus(orderStatus);
		newsPush.setStatus(OrderNewsPush.Status.unread);
		newsPush.setSend(send);
		newsPush.setReceive(receive);
		newsPush.setNoticeObject(noticeObject);
		super.persist(newsPush);
		return newsPush;
	}
	
	@Override
	public  OrderNewsPush addWithdrawPush(String send,String receive,Long linkId) {
		OrderNewsPush newsPush = new OrderNewsPush();
		
		return newsPush;
	}

	@Override
	public Page<OrderNewsPush> findPage(Supplier supplier, Status status,
			OrderStatus orderStatus, NoticeObject noticeObject,
			Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<OrderNewsPush> criteriaQuery = criteriaBuilder.createQuery(OrderNewsPush.class);
		Root<OrderNewsPush> root = criteriaQuery.from(OrderNewsPush.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if(null != supplier) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("supplier"), supplier));
		}
		if(null != status) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("status"), status));
		}
		if(null != orderStatus) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("orderStatus"), orderStatus));
		}
		if(null != noticeObject) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("noticeObject"), noticeObject));
		}
		criteriaQuery.where(restrictions);
		criteriaQuery.orderBy(criteriaBuilder.desc(root.get("createDate")), criteriaBuilder.desc(root.get("id")));
		return super.findPage(criteriaQuery, pageable);
	}

	@Override
	public boolean update(Supplier supplier, OrderNewsPush.NoticeObject noticeObject) {
		if (null == supplier) {
			return false;
		}
		String sql = "update OrderNewsPush set status = 1 where supplier =:supplier and noticeObject=:noticeObject and status = 0";
		int ex = entityManager.createQuery(sql).setParameter("supplier", supplier).setParameter("noticeObject", noticeObject).executeUpdate();
		return ex > 0;
	}

}
