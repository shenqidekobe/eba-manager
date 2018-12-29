package com.microBusiness.manage.dao.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BigDecimalType;
import org.hibernate.type.DateType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;

import com.microBusiness.manage.Filter;
import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.OrderDao;
import com.microBusiness.manage.dto.AssListStatisticsDto;
import com.microBusiness.manage.dto.CustomerReportDto;
import com.microBusiness.manage.dto.GoodNeedDto;
import com.microBusiness.manage.dto.GoodSupplierDto;
import com.microBusiness.manage.dto.OrderReportDto;
import com.microBusiness.manage.dto.OrderStatisticsDto;
import com.microBusiness.manage.entity.ChildMember;
import com.microBusiness.manage.entity.Goods;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.Need;
import com.microBusiness.manage.entity.Order;
import com.microBusiness.manage.entity.Order.Status;
import com.microBusiness.manage.entity.Order.Type;
import com.microBusiness.manage.entity.OrderItem;
import com.microBusiness.manage.entity.OrderLog;
import com.microBusiness.manage.entity.OrderRelation;
import com.microBusiness.manage.entity.PaymentMethod;
import com.microBusiness.manage.entity.Product;
import com.microBusiness.manage.entity.Shop;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.entity.SupplyNeed;
import com.microBusiness.manage.entity.SupplyType;

@Repository("orderDaoImpl")
public class OrderDaoImpl extends BaseDaoImpl<Order, Long> implements OrderDao {

	public Order findBySn(String sn) {
		if (StringUtils.isEmpty(sn)) {
			return null;
		}
		String jpql = "select orders from Order orders where lower(orders.sn) = lower(:sn)";
		try {
			return entityManager.createQuery(jpql, Order.class).setParameter("sn", sn).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public List<Order> findList(Order.Type type, Order.Status status, Member member, Goods goods, Boolean isPendingReceive, Boolean isPendingRefunds, Boolean isUseCouponCode, Boolean isExchangePoint, Boolean isAllocatedStock, Boolean hasExpired, Integer count, List<Filter> filters,
			List<com.microBusiness.manage.Order> orders) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
		Root<Order> root = criteriaQuery.from(Order.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (type != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("type"), type));
		}
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.notEqual(root.get("type"), Type.local));
		if (status != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("status"), status));
		}
		if (member != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("member"), member));
		}
		if (goods != null) {
			Subquery<Product> productSubquery = criteriaQuery.subquery(Product.class);
			Root<Product> productSubqueryRoot = productSubquery.from(Product.class);
			productSubquery.select(productSubqueryRoot);
			productSubquery.where(criteriaBuilder.equal(productSubqueryRoot.get("goods"), goods));

			Subquery<OrderItem> orderItemSubquery = criteriaQuery.subquery(OrderItem.class);
			Root<OrderItem> orderItemSubqueryRoot = orderItemSubquery.from(OrderItem.class);
			orderItemSubquery.select(orderItemSubqueryRoot);
			orderItemSubquery.where(criteriaBuilder.equal(orderItemSubqueryRoot.get("order"), root), criteriaBuilder.in(orderItemSubqueryRoot.get("product")).value(productSubquery));
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.exists(orderItemSubquery));
		}
		if (isPendingReceive != null) {
			Predicate predicate = criteriaBuilder.and(criteriaBuilder.or(root.get("expire").isNull(), criteriaBuilder.greaterThan(root.<Date> get("expire"), new Date())), criteriaBuilder.equal(root.get("paymentMethodType"), PaymentMethod.Type.cashOnDelivery),
					criteriaBuilder.notEqual(root.get("status"), Order.Status.completed), criteriaBuilder.notEqual(root.get("status"), Order.Status.failed), criteriaBuilder.notEqual(root.get("status"), Order.Status.canceled), criteriaBuilder.notEqual(root.get("status"), Order.Status.denied),
					criteriaBuilder.lessThan(root.<BigDecimal> get("amountPaid"), root.<BigDecimal> get("amount")));
			if (isPendingReceive) {
				restrictions = criteriaBuilder.and(restrictions, predicate);
			} else {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.not(predicate));
			}
		}
		if (isPendingRefunds != null) {
			Predicate predicate = criteriaBuilder.or(
					criteriaBuilder.and(
							criteriaBuilder.or(criteriaBuilder.and(root.get("expire").isNotNull(), criteriaBuilder.lessThanOrEqualTo(root.<Date> get("expire"), new Date())), criteriaBuilder.equal(root.get("status"), Order.Status.failed),
									criteriaBuilder.equal(root.get("status"), Order.Status.canceled), criteriaBuilder.equal(root.get("status"), Order.Status.denied)), criteriaBuilder.greaterThan(root.<BigDecimal> get("amountPaid"), BigDecimal.ZERO)),
					criteriaBuilder.and(criteriaBuilder.equal(root.get("status"), Order.Status.completed), criteriaBuilder.greaterThan(root.<BigDecimal> get("amountPaid"), root.<BigDecimal> get("amount"))));
			if (isPendingRefunds) {
				restrictions = criteriaBuilder.and(restrictions, predicate);
			} else {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.not(predicate));
			}
		}
		if (isUseCouponCode != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isUseCouponCode"), isUseCouponCode));
		}
		if (isExchangePoint != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isExchangePoint"), isExchangePoint));
		}
		if (isAllocatedStock != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isAllocatedStock"), isAllocatedStock));
		}
		if (hasExpired != null) {
			if (hasExpired) {
				restrictions = criteriaBuilder.and(restrictions, root.get("expire").isNotNull(), criteriaBuilder.lessThanOrEqualTo(root.<Date> get("expire"), new Date()));
			} else {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(root.get("expire").isNull(), criteriaBuilder.greaterThan(root.<Date> get("expire"), new Date())));
			}
		}
		criteriaQuery.where(restrictions);
		return super.findList(criteriaQuery, null, count, filters, orders);
	}

	public Page<Order> findPage(Order.Type type, Order.Status status, Member member, Goods goods, Boolean isPendingReceive, Boolean isPendingRefunds, Boolean isUseCouponCode, Boolean isExchangePoint, Boolean isAllocatedStock, Boolean hasExpired, Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
		Root<Order> root = criteriaQuery.from(Order.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (type != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("type"), type));
		}
		if (status != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("status"), status));
		}
		if (member != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("member"), member));
		}
		if (goods != null) {
			Subquery<Product> productSubquery = criteriaQuery.subquery(Product.class);
			Root<Product> productSubqueryRoot = productSubquery.from(Product.class);
			productSubquery.select(productSubqueryRoot);
			productSubquery.where(criteriaBuilder.equal(productSubqueryRoot.get("goods"), goods));

			Subquery<OrderItem> orderItemSubquery = criteriaQuery.subquery(OrderItem.class);
			Root<OrderItem> orderItemSubqueryRoot = orderItemSubquery.from(OrderItem.class);
			orderItemSubquery.select(orderItemSubqueryRoot);
			orderItemSubquery.where(criteriaBuilder.equal(orderItemSubqueryRoot.get("order"), root), criteriaBuilder.in(orderItemSubqueryRoot.get("product")).value(productSubquery));
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.exists(orderItemSubquery));
		}
		if (isPendingReceive != null) {
			Predicate predicate = criteriaBuilder.and(criteriaBuilder.or(root.get("expire").isNull(), criteriaBuilder.greaterThan(root.<Date> get("expire"), new Date())), criteriaBuilder.equal(root.get("paymentMethodType"), PaymentMethod.Type.cashOnDelivery),
					criteriaBuilder.notEqual(root.get("status"), Order.Status.completed), criteriaBuilder.notEqual(root.get("status"), Order.Status.failed), criteriaBuilder.notEqual(root.get("status"), Order.Status.canceled), criteriaBuilder.notEqual(root.get("status"), Order.Status.denied),
					criteriaBuilder.lessThan(root.<BigDecimal> get("amountPaid"), root.<BigDecimal> get("amount")));
			if (isPendingReceive) {
				restrictions = criteriaBuilder.and(restrictions, predicate);
			} else {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.not(predicate));
			}
		}
		if (isPendingRefunds != null) {
			Predicate predicate = criteriaBuilder.or(
					criteriaBuilder.and(
							criteriaBuilder.or(criteriaBuilder.and(root.get("expire").isNotNull(), criteriaBuilder.lessThanOrEqualTo(root.<Date> get("expire"), new Date())), criteriaBuilder.equal(root.get("status"), Order.Status.failed),
									criteriaBuilder.equal(root.get("status"), Order.Status.canceled), criteriaBuilder.equal(root.get("status"), Order.Status.denied)), criteriaBuilder.greaterThan(root.<BigDecimal> get("amountPaid"), BigDecimal.ZERO)),
					criteriaBuilder.and(criteriaBuilder.equal(root.get("status"), Order.Status.completed), criteriaBuilder.greaterThan(root.<BigDecimal> get("amountPaid"), root.<BigDecimal> get("amount"))));
			if (isPendingRefunds) {
				restrictions = criteriaBuilder.and(restrictions, predicate);
			} else {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.not(predicate));
			}
		}
		if (isUseCouponCode != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isUseCouponCode"), isUseCouponCode));
		}
		if (isExchangePoint != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isExchangePoint"), isExchangePoint));
		}
		if (isAllocatedStock != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isAllocatedStock"), isAllocatedStock));
		}
		if (hasExpired != null) {
			if (hasExpired) {
				restrictions = criteriaBuilder.and(restrictions, root.get("expire").isNotNull(), criteriaBuilder.lessThanOrEqualTo(root.<Date> get("expire"), new Date()));
			} else {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(root.get("expire").isNull(), criteriaBuilder.greaterThan(root.<Date> get("expire"), new Date())));
			}
		}
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery, pageable);
	}

	public Long count(Order.Type type, Order.Status status, Member member, Goods goods, Boolean isPendingReceive, Boolean isPendingRefunds, Boolean isUseCouponCode, Boolean isExchangePoint, Boolean isAllocatedStock, Boolean hasExpired) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
		Root<Order> root = criteriaQuery.from(Order.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (type != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("type"), type));
		}
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.notEqual(root.get("type"), Type.local));

		if (status != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("status"), status));
		}
		if (member != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("member"), member));
		}
		if (goods != null) {
			Subquery<Product> productSubquery = criteriaQuery.subquery(Product.class);
			Root<Product> productSubqueryRoot = productSubquery.from(Product.class);
			productSubquery.select(productSubqueryRoot);
			productSubquery.where(criteriaBuilder.equal(productSubqueryRoot.get("goods"), goods));

			Subquery<OrderItem> orderItemSubquery = criteriaQuery.subquery(OrderItem.class);
			Root<OrderItem> orderItemSubqueryRoot = orderItemSubquery.from(OrderItem.class);
			orderItemSubquery.select(orderItemSubqueryRoot);
			orderItemSubquery.where(criteriaBuilder.equal(orderItemSubqueryRoot.get("order"), root), criteriaBuilder.in(orderItemSubqueryRoot.get("product")).value(productSubquery));
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.exists(orderItemSubquery));
		}
		if (isPendingReceive != null) {
			Predicate predicate = criteriaBuilder.and(criteriaBuilder.or(root.get("expire").isNull(), criteriaBuilder.greaterThan(root.<Date> get("expire"), new Date())), criteriaBuilder.equal(root.get("paymentMethodType"), PaymentMethod.Type.cashOnDelivery),
					criteriaBuilder.notEqual(root.get("status"), Order.Status.completed), criteriaBuilder.notEqual(root.get("status"), Order.Status.failed), criteriaBuilder.notEqual(root.get("status"), Order.Status.canceled), criteriaBuilder.notEqual(root.get("status"), Order.Status.denied),
					criteriaBuilder.lessThan(root.<BigDecimal> get("amountPaid"), root.<BigDecimal> get("amount")));
			if (isPendingReceive) {
				restrictions = criteriaBuilder.and(restrictions, predicate);
			} else {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.not(predicate));
			}
		}
		if (isPendingRefunds != null) {
			Predicate predicate = criteriaBuilder.or(
					criteriaBuilder.and(
							criteriaBuilder.or(criteriaBuilder.and(root.get("expire").isNotNull(), criteriaBuilder.lessThanOrEqualTo(root.<Date> get("expire"), new Date())), criteriaBuilder.equal(root.get("status"), Order.Status.failed),
									criteriaBuilder.equal(root.get("status"), Order.Status.canceled), criteriaBuilder.equal(root.get("status"), Order.Status.denied)), criteriaBuilder.greaterThan(root.<BigDecimal> get("amountPaid"), BigDecimal.ZERO)),
					criteriaBuilder.and(criteriaBuilder.equal(root.get("status"), Order.Status.completed), criteriaBuilder.greaterThan(root.<BigDecimal> get("amountPaid"), root.<BigDecimal> get("amount"))));
			if (isPendingRefunds) {
				restrictions = criteriaBuilder.and(restrictions, predicate);
			} else {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.not(predicate));
			}
		}
		if (isUseCouponCode != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isUseCouponCode"), isUseCouponCode));
		}
		if (isExchangePoint != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isExchangePoint"), isExchangePoint));
		}
		if (isAllocatedStock != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isAllocatedStock"), isAllocatedStock));
		}
		if (hasExpired != null) {
			if (hasExpired) {
				restrictions = criteriaBuilder.and(restrictions, root.get("expire").isNotNull(), criteriaBuilder.lessThanOrEqualTo(root.<Date> get("expire"), new Date()));
			} else {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(root.get("expire").isNull(), criteriaBuilder.greaterThan(root.<Date> get("expire"), new Date())));
			}
		}
		criteriaQuery.where(restrictions);
		return super.count(criteriaQuery, null);
	}

	public Long createOrderCount(Date beginDate, Date endDate) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
		Root<Order> root = criteriaQuery.from(Order.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (beginDate != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(root.<Date> get("createDate"), beginDate));
		}
		if (endDate != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(root.<Date> get("createDate"), endDate));
		}
		criteriaQuery.where(restrictions);
		return super.count(criteriaQuery, null);
	}

	public Long completeOrderCount(Date beginDate, Date endDate) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
		Root<Order> root = criteriaQuery.from(Order.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (beginDate != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(root.<Date> get("completeDate"), beginDate));
		}
		if (endDate != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(root.<Date> get("completeDate"), endDate));
		}
		criteriaQuery.where(restrictions);
		return super.count(criteriaQuery, null);
	}

	public BigDecimal createOrderAmount(Date beginDate, Date endDate) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<BigDecimal> criteriaQuery = criteriaBuilder.createQuery(BigDecimal.class);
		Root<Order> root = criteriaQuery.from(Order.class);
		criteriaQuery.select(criteriaBuilder.sum(root.<BigDecimal> get("amount")));
		Predicate restrictions = criteriaBuilder.conjunction();
		if (beginDate != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(root.<Date> get("createDate"), beginDate));
		}
		if (endDate != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(root.<Date> get("createDate"), endDate));
		}
		criteriaQuery.where(restrictions);
		BigDecimal result = entityManager.createQuery(criteriaQuery).getSingleResult();
		return result != null ? result : BigDecimal.ZERO;
	}

	public BigDecimal completeOrderAmount(Date beginDate, Date endDate) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<BigDecimal> criteriaQuery = criteriaBuilder.createQuery(BigDecimal.class);
		Root<Order> root = criteriaQuery.from(Order.class);
		criteriaQuery.select(criteriaBuilder.sum(root.<BigDecimal> get("amount")));
		Predicate restrictions = criteriaBuilder.conjunction();
		if (beginDate != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(root.<Date> get("completeDate"), beginDate));
		}
		if (endDate != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(root.<Date> get("completeDate"), endDate));
		}
		criteriaQuery.where(restrictions);
		BigDecimal result = entityManager.createQuery(criteriaQuery).getSingleResult();
		return result != null ? result : BigDecimal.ZERO;
	}

	@Override
	public Page<Order> findPage(Order.Type type, Order.Status status, Member member, Goods goods, Boolean isPendingReceive, Boolean isPendingRefunds, Boolean isUseCouponCode, Boolean isExchangePoint, Boolean isAllocatedStock, Boolean hasExpired, Pageable pageable, Supplier supplier) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
		Root<Order> root = criteriaQuery.from(Order.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (type != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("type"), type));
		}
		if (status != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("status"), status));
		}
		if (member != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("member"), member));
		}
		if(null != supplier){
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("supplier"), supplier));
		}
		if (goods != null) {
			Subquery<Product> productSubquery = criteriaQuery.subquery(Product.class);
			Root<Product> productSubqueryRoot = productSubquery.from(Product.class);
			productSubquery.select(productSubqueryRoot);
			productSubquery.where(criteriaBuilder.equal(productSubqueryRoot.get("goods"), goods));

			Subquery<OrderItem> orderItemSubquery = criteriaQuery.subquery(OrderItem.class);
			Root<OrderItem> orderItemSubqueryRoot = orderItemSubquery.from(OrderItem.class);
			orderItemSubquery.select(orderItemSubqueryRoot);
			orderItemSubquery.where(criteriaBuilder.equal(orderItemSubqueryRoot.get("order"), root), criteriaBuilder.in(orderItemSubqueryRoot.get("product")).value(productSubquery));
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.exists(orderItemSubquery));
		}
		if (isPendingReceive != null) {
			Predicate predicate = criteriaBuilder.and(criteriaBuilder.or(root.get("expire").isNull(), criteriaBuilder.greaterThan(root.<Date> get("expire"), new Date())), criteriaBuilder.equal(root.get("paymentMethodType"), PaymentMethod.Type.cashOnDelivery),
					criteriaBuilder.notEqual(root.get("status"), Order.Status.completed), criteriaBuilder.notEqual(root.get("status"), Order.Status.failed), criteriaBuilder.notEqual(root.get("status"), Order.Status.canceled), criteriaBuilder.notEqual(root.get("status"), Order.Status.denied),
					criteriaBuilder.lessThan(root.<BigDecimal> get("amountPaid"), root.<BigDecimal> get("amount")));
			if (isPendingReceive) {
				restrictions = criteriaBuilder.and(restrictions, predicate);
			} else {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.not(predicate));
			}
		}
		if (isPendingRefunds != null) {
			Predicate predicate = criteriaBuilder.or(
					criteriaBuilder.and(
							criteriaBuilder.or(criteriaBuilder.and(root.get("expire").isNotNull(), criteriaBuilder.lessThanOrEqualTo(root.<Date> get("expire"), new Date())), criteriaBuilder.equal(root.get("status"), Order.Status.failed),
									criteriaBuilder.equal(root.get("status"), Order.Status.canceled), criteriaBuilder.equal(root.get("status"), Order.Status.denied)), criteriaBuilder.greaterThan(root.<BigDecimal> get("amountPaid"), BigDecimal.ZERO)),
					criteriaBuilder.and(criteriaBuilder.equal(root.get("status"), Order.Status.completed), criteriaBuilder.greaterThan(root.<BigDecimal> get("amountPaid"), root.<BigDecimal> get("amount"))));
			if (isPendingRefunds) {
				restrictions = criteriaBuilder.and(restrictions, predicate);
			} else {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.not(predicate));
			}
		}
		if (isUseCouponCode != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isUseCouponCode"), isUseCouponCode));
		}
		if (isExchangePoint != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isExchangePoint"), isExchangePoint));
		}
		if (isAllocatedStock != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isAllocatedStock"), isAllocatedStock));
		}
		if (hasExpired != null) {
			if (hasExpired) {
				restrictions = criteriaBuilder.and(restrictions, root.get("expire").isNotNull(), criteriaBuilder.lessThanOrEqualTo(root.<Date> get("expire"), new Date()));
			} else {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(root.get("expire").isNull(), criteriaBuilder.greaterThan(root.<Date> get("expire"), new Date())));
			}
		}
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery, pageable);
	}


	@Override
	public Page<Order> findPage(Order.Type type, Order.Status status, Member member, Goods goods, Boolean isPendingReceive, Boolean isPendingRefunds, Boolean isUseCouponCode, Boolean isExchangePoint, Boolean isAllocatedStock, Boolean hasExpired, Pageable pageable, Set<Need> needs , Date startDate , Date endDate) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
		Root<Order> root = criteriaQuery.from(Order.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (type != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("type"), type));
		}
		if (status != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("status"), status));
		}
		if (member != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("member"), member));
		}

		if (startDate != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(root.<Date> get("reDate"), startDate));
		}
		if (endDate != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(root.<Date> get("reDate"), endDate));
		}

		if(CollectionUtils.isNotEmpty(needs)){

			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.in(root.get("need")).value(needs));

		}

		if (goods != null) {
			Subquery<Product> productSubquery = criteriaQuery.subquery(Product.class);
			Root<Product> productSubqueryRoot = productSubquery.from(Product.class);
			productSubquery.select(productSubqueryRoot);
			productSubquery.where(criteriaBuilder.equal(productSubqueryRoot.get("goods"), goods));

			Subquery<OrderItem> orderItemSubquery = criteriaQuery.subquery(OrderItem.class);
			Root<OrderItem> orderItemSubqueryRoot = orderItemSubquery.from(OrderItem.class);
			orderItemSubquery.select(orderItemSubqueryRoot);
			orderItemSubquery.where(criteriaBuilder.equal(orderItemSubqueryRoot.get("order"), root), criteriaBuilder.in(orderItemSubqueryRoot.get("product")).value(productSubquery));
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.exists(orderItemSubquery));
		}
		if (isPendingReceive != null) {
			Predicate predicate = criteriaBuilder.and(criteriaBuilder.or(root.get("expire").isNull(), criteriaBuilder.greaterThan(root.<Date> get("expire"), new Date())), criteriaBuilder.equal(root.get("paymentMethodType"), PaymentMethod.Type.cashOnDelivery),
					criteriaBuilder.notEqual(root.get("status"), Order.Status.completed), criteriaBuilder.notEqual(root.get("status"), Order.Status.failed), criteriaBuilder.notEqual(root.get("status"), Order.Status.canceled), criteriaBuilder.notEqual(root.get("status"), Order.Status.denied),
					criteriaBuilder.lessThan(root.<BigDecimal> get("amountPaid"), root.<BigDecimal> get("amount")));
			if (isPendingReceive) {
				restrictions = criteriaBuilder.and(restrictions, predicate);
			} else {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.not(predicate));
			}
		}
		if (isPendingRefunds != null) {
			Predicate predicate = criteriaBuilder.or(
					criteriaBuilder.and(
							criteriaBuilder.or(criteriaBuilder.and(root.get("expire").isNotNull(), criteriaBuilder.lessThanOrEqualTo(root.<Date> get("expire"), new Date())), criteriaBuilder.equal(root.get("status"), Order.Status.failed),
									criteriaBuilder.equal(root.get("status"), Order.Status.canceled), criteriaBuilder.equal(root.get("status"), Order.Status.denied)), criteriaBuilder.greaterThan(root.<BigDecimal> get("amountPaid"), BigDecimal.ZERO)),
					criteriaBuilder.and(criteriaBuilder.equal(root.get("status"), Order.Status.completed), criteriaBuilder.greaterThan(root.<BigDecimal> get("amountPaid"), root.<BigDecimal> get("amount"))));
			if (isPendingRefunds) {
				restrictions = criteriaBuilder.and(restrictions, predicate);
			} else {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.not(predicate));
			}
		}
		if (isUseCouponCode != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isUseCouponCode"), isUseCouponCode));
		}
		if (isExchangePoint != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isExchangePoint"), isExchangePoint));
		}
		if (isAllocatedStock != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isAllocatedStock"), isAllocatedStock));
		}
		if (hasExpired != null) {
			if (hasExpired) {
				restrictions = criteriaBuilder.and(restrictions, root.get("expire").isNotNull(), criteriaBuilder.lessThanOrEqualTo(root.<Date> get("expire"), new Date()));
			} else {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(root.get("expire").isNull(), criteriaBuilder.greaterThan(root.<Date> get("expire"), new Date())));
			}
		}
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery, pageable);
	}

	/**
	 * 采购单列表查询
	 */
	@Override
	public Page<Order> findPage(Type type, Status status,Order.Status[] statuses, Member member,
			Goods goods, Boolean isPendingReceive, Boolean isPendingRefunds,
			Boolean isUseCouponCode, Boolean isExchangePoint,
			Boolean isAllocatedStock, Boolean hasExpired, Pageable pageable,
			Set<Need> needs, Date startDate, Date endDate, String searchName , String timeSearch,Supplier toSupplier) {
		
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
		Root<Order> root = criteriaQuery.from(Order.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		
		if (type != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("type"), type));
		}else {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.notEqual(root.get("type"), Order.Type.distribution),criteriaBuilder.notEqual(root.get("type"), Type.local));
		}
		if (status != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("status"), status));
		}else if (statuses != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.in(root.get("status")).value(Arrays.asList(statuses)));
		}
		if (member != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("member"), member));
		}
		if(StringUtils.isNotEmpty(timeSearch)){
			if(timeSearch.equalsIgnoreCase("createTime")) {
				if (startDate != null) {
					restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(root.<Date> get("createDate"), startDate));
				}
				if (endDate != null) {
					restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(root.<Date> get("createDate"), endDate));
				}
			}else if(timeSearch.equalsIgnoreCase("completeTime")) {
				if (startDate != null) {
					restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(root.<Date> get("completeDate"), startDate));
				}
				if (endDate != null) {
					restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(root.<Date> get("completeDate"), endDate));
				}
			}else {
				if (startDate != null) {
					restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(root.<Date> get("reDate"), startDate));
				}
				if (endDate != null) {
					restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(root.<Date> get("reDate"), endDate));
				}
			}
		
		}else {
			if (startDate != null) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(root.<Date> get("reDate"), startDate));
			}
			if (endDate != null) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(root.<Date> get("reDate"), endDate));
			}
		}
		if(StringUtils.isNotEmpty(searchName)){
			Join<Order , Supplier> supplierJoin = root.join("supplier" , JoinType.LEFT);
            Join<Order , Supplier> toSupplierJoin = root.join("toSupplier" , JoinType.LEFT);
            Join<Order , Need> needJoin = root.join("need" , JoinType.LEFT);

            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.like(root.<String>get("sn") , "%"+searchName+"%") , criteriaBuilder.like(supplierJoin.<String>get("name"), "%"+searchName+"%"),criteriaBuilder.like(toSupplierJoin.<String>get("name"), "%"+searchName+"%"),criteriaBuilder.like(needJoin.<String>get("name"), "%"+searchName+"%")));
        }

		if(null != toSupplier){
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("toSupplier"), toSupplier));
		}
		
		if(CollectionUtils.isNotEmpty(needs)){

			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.in(root.get("need")).value(needs));

		}

		if (goods != null) {
			Subquery<Product> productSubquery = criteriaQuery.subquery(Product.class);
			Root<Product> productSubqueryRoot = productSubquery.from(Product.class);
			productSubquery.select(productSubqueryRoot);
			productSubquery.where(criteriaBuilder.equal(productSubqueryRoot.get("goods"), goods));

			Subquery<OrderItem> orderItemSubquery = criteriaQuery.subquery(OrderItem.class);
			Root<OrderItem> orderItemSubqueryRoot = orderItemSubquery.from(OrderItem.class);
			orderItemSubquery.select(orderItemSubqueryRoot);
			orderItemSubquery.where(criteriaBuilder.equal(orderItemSubqueryRoot.get("order"), root), criteriaBuilder.in(orderItemSubqueryRoot.get("product")).value(productSubquery));
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.exists(orderItemSubquery));
		}
		if (isPendingReceive != null) {
			Predicate predicate = criteriaBuilder.and(criteriaBuilder.or(root.get("expire").isNull(), criteriaBuilder.greaterThan(root.<Date> get("expire"), new Date())), criteriaBuilder.equal(root.get("paymentMethodType"), PaymentMethod.Type.cashOnDelivery),
					criteriaBuilder.notEqual(root.get("status"), Order.Status.completed), criteriaBuilder.notEqual(root.get("status"), Order.Status.failed), criteriaBuilder.notEqual(root.get("status"), Order.Status.canceled), criteriaBuilder.notEqual(root.get("status"), Order.Status.denied),
					criteriaBuilder.lessThan(root.<BigDecimal> get("amountPaid"), root.<BigDecimal> get("amount")));
			if (isPendingReceive) {
				restrictions = criteriaBuilder.and(restrictions, predicate);
			} else {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.not(predicate));
			}
		}
		if (isPendingRefunds != null) {
			Predicate predicate = criteriaBuilder.or(
					criteriaBuilder.and(
							criteriaBuilder.or(criteriaBuilder.and(root.get("expire").isNotNull(), criteriaBuilder.lessThanOrEqualTo(root.<Date> get("expire"), new Date())), criteriaBuilder.equal(root.get("status"), Order.Status.failed),
									criteriaBuilder.equal(root.get("status"), Order.Status.canceled), criteriaBuilder.equal(root.get("status"), Order.Status.denied)), criteriaBuilder.greaterThan(root.<BigDecimal> get("amountPaid"), BigDecimal.ZERO)),
					criteriaBuilder.and(criteriaBuilder.equal(root.get("status"), Order.Status.completed), criteriaBuilder.greaterThan(root.<BigDecimal> get("amountPaid"), root.<BigDecimal> get("amount"))));
			if (isPendingRefunds) {
				restrictions = criteriaBuilder.and(restrictions, predicate);
			} else {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.not(predicate));
			}
		}
		if (isUseCouponCode != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isUseCouponCode"), isUseCouponCode));
		}
		if (isExchangePoint != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isExchangePoint"), isExchangePoint));
		}
		if (isAllocatedStock != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isAllocatedStock"), isAllocatedStock));
		}
		if (hasExpired != null) {
			if (hasExpired) {
				restrictions = criteriaBuilder.and(restrictions, root.get("expire").isNotNull(), criteriaBuilder.lessThanOrEqualTo(root.<Date> get("expire"), new Date()));
			} else {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(root.get("expire").isNull(), criteriaBuilder.greaterThan(root.<Date> get("expire"), new Date())));
			}
		}
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery, pageable);
		
	}
	
	/**
	 * 订货单列表查询
	 */
	@Override
	public Page<Order> findPage(Type type, Status status,Order.Status[] reportStatus, Member member,
			Goods goods, Boolean isPendingReceive, Boolean isPendingRefunds,
			Boolean isUseCouponCode, Boolean isExchangePoint,
			Boolean isAllocatedStock, Boolean hasExpired, Pageable pageable,
			Supplier supplier, Date startDate, Date endDate , String searchName , String timeSearch, ChildMember childMember) {
		
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
		Root<Order> root = criteriaQuery.from(Order.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		
		if (type != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("type"), type));
		}else {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.notEqual(root.get("type"), Order.Type.distribution), criteriaBuilder.notEqual(root.get("type"), Order.Type.local));
		}

		if (status != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("status"), status));
		}else if (reportStatus != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.in(root.get("status")).value(Arrays.asList(reportStatus)));
		}
		if (member != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("member"), member));
		}
		if (childMember != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("childMember"), childMember));
		}
		
		if(null != supplier){
			restrictions = criteriaBuilder.and(restrictions,criteriaBuilder.or(criteriaBuilder.equal(root.get("supplier"), supplier),criteriaBuilder.and(criteriaBuilder.equal(root.get("toSupplier"), supplier),criteriaBuilder.notEqual(root.get("supplyType"), SupplyType.formal))));
			
		}
		/*if (startDate != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(root.<Date> get("reDate"), startDate));
		}
		if (endDate != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(root.<Date> get("reDate"), endDate));
		}*/
		if(StringUtils.isNotEmpty(timeSearch)){
			if(timeSearch.equalsIgnoreCase("createTime")) {
				if (startDate != null) {
					restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(root.<Date> get("createDate"), startDate));
				}
				if (endDate != null) {
					restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(root.<Date> get("createDate"), endDate));
				}
			}else if(timeSearch.equalsIgnoreCase("completeTime")) {
				if (startDate != null) {
					restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(root.<Date> get("completeDate"), startDate));
				}
				if (endDate != null) {
					restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(root.<Date> get("completeDate"), endDate));
				}
			}else {
				if (startDate != null) {
					restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(root.<Date> get("reDate"), startDate));
				}
				if (endDate != null) {
					restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(root.<Date> get("reDate"), endDate));
				}
			}
		
		}else {
			if (startDate != null) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(root.<Date> get("reDate"), startDate));
			}
			if (endDate != null) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(root.<Date> get("reDate"), endDate));
			}
		}
		if(StringUtils.isNotEmpty(searchName)){
            Join<Order , Supplier> supplierJoin = root.join("supplier" , JoinType.LEFT);
            Join<Order , Supplier> toSupplierJoin = root.join("toSupplier" , JoinType.LEFT);
            //Join<Order , Need> needJoin = root.join("need" , JoinType.LEFT);

            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.like(root.<String>get("sn") , "%"+searchName+"%") , 
            		criteriaBuilder.like(supplierJoin.<String>get("name"), "%"+searchName+"%"),
            		criteriaBuilder.like(toSupplierJoin.<String>get("name"), "%"+searchName+"%")
//           		criteriaBuilder.like(needJoin.<String>get("name"), "%"+searchName+"%")
            		));

        }
		if (goods != null) {
			Subquery<Product> productSubquery = criteriaQuery.subquery(Product.class);
			Root<Product> productSubqueryRoot = productSubquery.from(Product.class);
			productSubquery.select(productSubqueryRoot);
			productSubquery.where(criteriaBuilder.equal(productSubqueryRoot.get("goods"), goods));

			Subquery<OrderItem> orderItemSubquery = criteriaQuery.subquery(OrderItem.class);
			Root<OrderItem> orderItemSubqueryRoot = orderItemSubquery.from(OrderItem.class);
			orderItemSubquery.select(orderItemSubqueryRoot);
			orderItemSubquery.where(criteriaBuilder.equal(orderItemSubqueryRoot.get("order"), root), criteriaBuilder.in(orderItemSubqueryRoot.get("product")).value(productSubquery));
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.exists(orderItemSubquery));
		}
		if (isPendingReceive != null) {
			Predicate predicate = criteriaBuilder.and(criteriaBuilder.or(root.get("expire").isNull(), criteriaBuilder.greaterThan(root.<Date> get("expire"), new Date())), criteriaBuilder.equal(root.get("paymentMethodType"), PaymentMethod.Type.cashOnDelivery),
					criteriaBuilder.notEqual(root.get("status"), Order.Status.completed), criteriaBuilder.notEqual(root.get("status"), Order.Status.failed), criteriaBuilder.notEqual(root.get("status"), Order.Status.canceled), criteriaBuilder.notEqual(root.get("status"), Order.Status.denied),
					criteriaBuilder.lessThan(root.<BigDecimal> get("amountPaid"), root.<BigDecimal> get("amount")));
			if (isPendingReceive) {
				restrictions = criteriaBuilder.and(restrictions, predicate);
			} else {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.not(predicate));
			}
		}
		if (isPendingRefunds != null) {
			Predicate predicate = criteriaBuilder.or(
					criteriaBuilder.and(
							criteriaBuilder.or(criteriaBuilder.and(root.get("expire").isNotNull(), criteriaBuilder.lessThanOrEqualTo(root.<Date> get("expire"), new Date())), criteriaBuilder.equal(root.get("status"), Order.Status.failed),
									criteriaBuilder.equal(root.get("status"), Order.Status.canceled), criteriaBuilder.equal(root.get("status"), Order.Status.denied)), criteriaBuilder.greaterThan(root.<BigDecimal> get("amountPaid"), BigDecimal.ZERO)),
					criteriaBuilder.and(criteriaBuilder.equal(root.get("status"), Order.Status.completed), criteriaBuilder.greaterThan(root.<BigDecimal> get("amountPaid"), root.<BigDecimal> get("amount"))));
			if (isPendingRefunds) {
				restrictions = criteriaBuilder.and(restrictions, predicate);
			} else {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.not(predicate));
			}
		}
		if (isUseCouponCode != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isUseCouponCode"), isUseCouponCode));
		}
		if (isExchangePoint != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isExchangePoint"), isExchangePoint));
		}
		if (isAllocatedStock != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isAllocatedStock"), isAllocatedStock));
		}
		if (hasExpired != null) {
			if (hasExpired) {
				restrictions = criteriaBuilder.and(restrictions, root.get("expire").isNotNull(), criteriaBuilder.lessThanOrEqualTo(root.<Date> get("expire"), new Date()));
			} else {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(root.get("expire").isNull(), criteriaBuilder.greaterThan(root.<Date> get("expire"), new Date())));
			}
		}
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery, pageable);
		
	}
	/**
	 * 前端订货单查询  平台订单
	 */
	@Override
	public Page<Order> findPageByMember(Status status, List<Shop> shops,
			Pageable pageable) {
		List<Order.Status> statuses=new ArrayList<>();
		statuses.add(Order.Status.pendingReview);
		statuses.add(Order.Status.canceled);
		statuses.add(Order.Status.denied);
		statuses.add(Order.Status.applyCancel);
		
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
		Root<Order> root = criteriaQuery.from(Order.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (status != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("status"), status));
		}
		//restrictions = criteriaBuilder.and(restrictions,criteriaBuilder.or(criteriaBuilder.and(criteriaBuilder.notEqual(root.get("type"),Order.Type.distribution)),criteriaBuilder.and(criteriaBuilder.equal(root.get("type"),Order.Type.distribution),criteriaBuilder.in(root.get("status")).value(statuses))));
//		if(CollectionUtils.isNotEmpty(shops)){
//			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.in(root.get("shop")).value(shops));
//		}
		//restrictions=criteriaBuilder.and(restrictions,criteriaBuilder.notEqual(root.get("type"), Type.local));
		criteriaQuery.where(restrictions);
		criteriaQuery.orderBy(criteriaBuilder.desc(root.get("createDate")));
		return super.findPage(criteriaQuery, pageable);
	}

	/**
	 *
	 * @param startRow
	 * @param offset
	 * @return
	 */
	/*@Override
	public List<Order> getOrderInSupply(int startRow, int offset , Date compareDate) {
		StringBuffer findSql = new StringBuffer("select * from (select orders.* , TIMESTAMPDIFF(DAY,DATE(orders.create_date),DATE(:compareDate)) noOrderDays , supplierSupplier.notice_day noticeDay from xx_order orders ")
				.append(" inner join xx_member member on orders.member=member.id ")
				.append(" inner join t_need need ON member.need = need.id")
				.append(" inner join t_supplier_assign_relation assRel ON assRel.need = need.id")
				.append(" inner join t_supplier_supplier supplierSupplier ON assRel.supply_relation = supplierSupplier.id")
				.append(" where 1=1")
				.append(" and supplierSupplier.status=").append(SupplierSupplier.Status.inTheSupply.ordinal())
				.append(" and orders.supply_type=").append(SupplyType.formal.ordinal())
				.append(" and need.type=").append(Need.Type.general.ordinal())
				.append(" and supplierSupplier.start_date <= orders.create_date")
				.append(" and orders.create_date <= supplierSupplier.end_date")
				.append(" and supplierSupplier.open_notice=").append(true)
				.append(" order by orders.create_date desc) t")
				.append(" group by supplier , member")
				.append(" having noOrderDays>=noticeDay and MOD(noOrderDays , noticeDay)=0");

		Query query = entityManager.createNativeQuery(findSql.toString() , Order.class) ;

		query.setParameter("compareDate" , compareDate);

		query.setFirstResult(startRow);
		query.setMaxResults(offset);

		return query.getResultList();
	}*/
	
	@Override
	public List<Order> getOrderInSupply(int startRow, int offset , Date compareDate) {
		StringBuffer findSql = new StringBuffer("select * from (select orders.* , TIMESTAMPDIFF(DAY,DATE(orders.create_date),DATE(:compareDate)) noOrderDays , supply.notice_day noticeDay from xx_order orders ")
				.append(" inner join xx_member member on orders.member=member.id ")
				.append(" inner join t_need need ON member.need = need.id")
				.append(" inner join t_supply_need supply ON supply.need = need.id")
				.append(" where 1=1")
				.append(" and supply.status=").append(SupplyNeed.Status.SUPPLY.ordinal())
				.append(" and orders.supply_type=").append(SupplyType.formal.ordinal())
				.append(" and need.type=").append(Need.Type.general.ordinal())
				.append(" and supply.start_date <= orders.create_date")
				.append(" and orders.create_date <= supply.end_date")
				.append(" and orders.type!=").append(Order.Type.local.ordinal())
				.append(" and supply.open_notice=").append(true)
				.append(" order by orders.create_date desc) t")
				.append(" group by supplier , member")
				.append(" having noOrderDays>=noticeDay and MOD(noOrderDays , noticeDay)=0");

		Query query = entityManager.createNativeQuery(findSql.toString() , Order.class) ;

		query.setParameter("compareDate" , compareDate);

		query.setFirstResult(startRow);
		query.setMaxResults(offset);

		return query.getResultList();
	}

	@Override
	public int countNumByOrder(Need need,
			Date startDate, Date endDate , Supplier supplier) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
		Root<Order> root = criteriaQuery.from(Order.class);
		criteriaQuery.select(criteriaBuilder.countDistinct(root));
		Predicate restrictions = criteriaBuilder.conjunction();
		if (need != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("member"), need.getMember()));
		}
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.notEqual(root.get("type"), Type.local));
		if (startDate != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(root.<Date> get("createDate"), startDate));
		}
		if (endDate != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(root.<Date> get("createDate"), endDate));
		}
		if(supplier != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("supplier"), supplier));
		}
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.notEqual(root.get("status"), Order.Status.canceled));
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.notEqual(root.get("status"), Order.Status.denied));
		criteriaQuery.where(restrictions);
		return entityManager.createQuery(criteriaQuery).getSingleResult().intValue();
		
	}

	/**
	 * 根据id查询订单
	 */
	@Override
	public List<Order> findListByIds(Long[] ids) {
		String jpql = "select order from Order order where order.id in (:ids)";
		TypedQuery<Order> query = entityManager.createQuery(jpql,Order.class).setParameter("ids",Arrays.asList(ids));
		return query.getResultList();
	}

	@Override
	public List<Product> findByProduct(Status status, Supplier supplier,
			Date startDate, Date endDate, String searchName, String timeSearch) {
		StringBuffer findSql = new StringBuffer("select DISTINCT product.* from xx_order orders");
		findSql.append(" INNER JOIN xx_order_item orderItem on orders.id = orderItem.orders");
		findSql.append(" INNER JOIN xx_product product on orderItem.product = product.id");
		findSql.append(" INNER JOIN t_need need on orders.need = need.id");
		findSql.append(" LEFT JOIN t_supplier supplier on orders.to_supplier = supplier.id");
		findSql.append(" where ((orders.supplier=:supplier and orders.type != 1) or ( orders.to_supplier =:supplier and orders.type = 3))");
		findSql.append(" and  orders.type != ").append(Type.local.ordinal());
		if(status != null) {
			findSql.append(" and orders.status=:status");
		}
		if(startDate != null && endDate != null) {
			if(timeSearch != null) {
				if(timeSearch.equalsIgnoreCase("createTime")) {
					findSql.append(" and orders.create_date>=:startDate");
					findSql.append(" and orders.create_date<=:endDate");
				}else if(timeSearch.equalsIgnoreCase("completeTime")) {
					findSql.append(" and orders.complete_date>=:startDate");
					findSql.append(" and orders.complete_date<=:endDate");
				}else {
					findSql.append(" and orders.re_date>=:startDate");
					findSql.append(" and orders.re_date<=:endDate");
				}
			}else {
				findSql.append(" and orders.re_date>=:startDate");
				findSql.append(" and orders.re_date<=:endDate");
			}
		}
		if(searchName != null) {
			findSql.append(" and (orders.sn like :searchName or supplier.name like :searchName or need.name like :searchName)");
		}
		Query query = entityManager.createNativeQuery(findSql.toString() , Product.class) ;
		
		if(status != null) {
			query.setParameter("status" , status.ordinal()) ;
		}
		query.setParameter("supplier" , supplier.getId()) ;
		if(startDate != null && endDate != null) {
			query.setParameter("startDate" , startDate) ;
			query.setParameter("endDate" , endDate) ;
		}
		if(searchName != null) {
			query.setParameter("searchName" , "%"+searchName+"%") ;
		}
		List<Product> lists = query.getResultList();
		return lists;
	}

	/**
	 * 采购商信息
	 */
	@Override
	public List<Supplier> findBySupplier(Status status, Supplier supplier,
			Date startDate, Date endDate, String searchName, String timeSearch) {
		StringBuffer findSql = new StringBuffer("select DISTINCT supplier.* from xx_order orders");
		findSql.append(" INNER JOIN t_need need on orders.need=need.id");
		findSql.append(" LEFT JOIN t_supplier supplier on orders.to_supplier=supplier.id");
		findSql.append(" where ((orders.supplier=:supplier and orders.type != 1) or ( orders.to_supplier =:supplier and orders.type = 3))");
		findSql.append(" and  orders.type != ").append(Type.local.ordinal());
		if(status != null) {
			findSql.append(" and orders.status=:status");
		}
		if(startDate != null && endDate != null) {
			if(timeSearch != null) {
				if(timeSearch.equalsIgnoreCase("createTime")) {
					findSql.append(" and orders.create_date>=:startDate");
					findSql.append(" and orders.create_date<=:endDate");
				}else if(timeSearch.equalsIgnoreCase("completeTime")) {
					findSql.append(" and orders.complete_date>=:startDate");
					findSql.append(" and orders.complete_date<=:endDate");
				}else {
					findSql.append(" and orders.re_date>=:startDate");
					findSql.append(" and orders.re_date<=:endDate");
				}
			}else {
				findSql.append(" and orders.re_date>=:startDate");
				findSql.append(" and orders.re_date<=:endDate");
			}
		}
		if(searchName != null) {
			findSql.append(" and (orders.sn like :searchName or supplier.name like :searchName or need.name like :searchName)");
		}
		Query query = entityManager.createNativeQuery(findSql.toString() , Supplier.class) ;
		if(status != null) {
			query.setParameter("status" , status.ordinal()) ;
		}
		query.setParameter("supplier" , supplier.getId()) ;
		if(startDate != null && endDate != null) {
			query.setParameter("startDate" , startDate) ;
			query.setParameter("endDate" , endDate) ;
		}
		if(searchName != null) {
			query.setParameter("searchName" , "%"+searchName+"%") ;
		}
		List<Supplier> lists = query.getResultList();
		return lists;
	}

	@Override
	public List<GoodNeedDto> findByGoodsAndNeed(Status status,
			Supplier supplier, Date startDate, Date endDate, String searchName,
			String timeSearch) {
		StringBuffer findSql = new StringBuffer("select area.full_name fullName,needs.address address,orderItem.quantity as quantity,orders.create_date as createDate,needs.name as needName,orders.phone,goods.* from xx_order orders");
		findSql.append(" INNER JOIN xx_order_item orderItem on orders.id=orderItem.orders");
		findSql.append(" INNER JOIN xx_product product on orderItem.product=product.id");
		findSql.append(" INNER JOIN xx_goods goods on product.goods=goods.id");
		findSql.append(" INNER JOIN t_need needs on needs.id=orders.need");
		findSql.append(" INNER JOIN xx_area area on area.id=needs.area");
		findSql.append(" LEFT JOIN t_supplier supplier on orders.supplier=supplier.id");
		findSql.append(" where ((orders.supplier=:supplier and orders.type != 1) or ( orders.to_supplier =:supplier and orders.type = 3))");
		findSql.append(" and  orders.type != ").append(Type.local.ordinal());
		if(status != null) {
			findSql.append(" and orders.status=:status");
		}
		if(startDate != null && endDate != null) {
			if(timeSearch != null) {
				if(timeSearch.equalsIgnoreCase("createTime")) {
					findSql.append(" and orders.create_date>=:startDate");
					findSql.append(" and orders.create_date<=:endDate");
				}else if(timeSearch.equalsIgnoreCase("completeTime")) {
					findSql.append(" and orders.complete_date>=:startDate");
					findSql.append(" and orders.complete_date<=:endDate");
				}else {
					findSql.append(" and orders.re_date>=:startDate");
					findSql.append(" and orders.re_date<=:endDate");
				}
			}else {
				findSql.append(" and orders.re_date>=:startDate");
				findSql.append(" and orders.re_date<=:endDate");
			}
		}
		if(searchName != null) {
			findSql.append(" and (orders.sn like :searchName or supplier.name like :searchName or needs.name like :searchName)");
		}
		findSql.append(" ORDER BY orders.create_date ASC");
		Query query = entityManager.createNativeQuery(findSql.toString()) ;
		
		if(status != null) {
			query.setParameter("status" , status.ordinal()) ;
		}
		query.setParameter("supplier" , supplier.getId()) ;
		if(startDate != null && endDate != null) {
			query.setParameter("startDate" , startDate) ;
			query.setParameter("endDate" , endDate) ;
		}
		if(searchName != null) {
			query.setParameter("searchName" , "%"+searchName+"%") ;
		}
		query.unwrap(SQLQuery.class).addEntity(Goods.class).addScalar("fullName" , StringType.INSTANCE).addScalar("address" , StringType.INSTANCE).addScalar("quantity" , IntegerType.INSTANCE).addScalar("createDate" , DateType.INSTANCE).addScalar("needName" , StringType.INSTANCE).addScalar("phone" , StringType.INSTANCE).setResultTransformer(Transformers.aliasToBean(GoodNeedDto.class));
		List<GoodNeedDto> lists = query.getResultList();
		return lists;
	}

	/**
	 * 查询供货者信息
	 */
	@Override
	public List<Supplier> findBySuppliers(Status status, Set<Need> needs,
			Date startDate, Date endDate, String searchName, String timeSearch) {
		
		
		StringBuffer findSql = new StringBuffer("select DISTINCT supplier.* from xx_order orders ");
		findSql.append(" LEFT OUTER JOIN t_need need ON orders.need = need.id");
		findSql.append(" LEFT OUTER JOIN t_supplier supplier ON orders.supplier = supplier.id");
		findSql.append(" where 1=1");
		findSql.append(" and  orders.type != ").append(Type.local.ordinal());
		if(needs != null) {
			findSql.append(" and need.id in(:ids)");
		}
		if(status != null) {
			findSql.append(" and orders.status=:status");
		}
		if(startDate != null && endDate != null) {
			if(timeSearch != null) {
				if(timeSearch.equalsIgnoreCase("createTime")) {
					findSql.append(" and orders.create_date>=:startDate");
					findSql.append(" and orders.create_date<=:endDate");
				}else if(timeSearch.equalsIgnoreCase("completeTime")) {
					findSql.append(" and orders.complete_date>=:startDate");
					findSql.append(" and orders.complete_date<=:endDate");
				}else {
					findSql.append(" and orders.re_date>=:startDate");
					findSql.append(" and orders.re_date<=:endDate");
				}
			}else {
				findSql.append(" and orders.re_date>=:startDate");
				findSql.append(" and orders.re_date<=:endDate");
			}
		}
		if(searchName != null) {
			findSql.append(" and (orders.sn like :searchName or supplier.name like :searchName or need.name like :searchName)");
		}
		Query query = entityManager.createNativeQuery(findSql.toString() , Supplier.class) ;
		List<Long> ids = new ArrayList<>();
		if(needs != null) {
			for(Need need : needs) {
				ids.add(need.getId());
			}
			query.setParameter("ids" , ids) ;
		}
		if(status != null) {
			query.setParameter("status" , status.ordinal()) ;
		}
		if(startDate != null && endDate != null) {
			query.setParameter("startDate" , startDate) ;
			query.setParameter("endDate" , endDate) ;
		}
		if(searchName != null) {
			query.setParameter("searchName" , "%"+searchName+"%") ;
		}
		List<Supplier> lists = query.getResultList();
		return lists;
		
	}

	/**
	 * 查询进货信息
	 */
	@Override
	public List<GoodSupplierDto> findByGoodsAndSupplier(Status status,
			Set<Need> needs, Date startDate, Date endDate, String searchName,
			String timeSearch) {
		
		StringBuffer findSql = new StringBuffer("select goods.* ,supplier.* , supplier.name supplierName ,orderItem.quantity quantity ,orders.create_date createDate from xx_order orders");
		findSql.append(" INNER JOIN xx_order_item orderItem ON orders.id = orderItem.orders");
		findSql.append(" INNER JOIN xx_product product ON orderItem.product = product.id");
		findSql.append(" INNER JOIN xx_goods goods ON product.goods = goods.id");
		findSql.append(" INNER JOIN t_need need ON orders.need = need.id");
		findSql.append(" INNER JOIN t_supplier supplier ON orders.supplier = supplier.id");
		findSql.append(" where 1=1");
		findSql.append(" and  orders.type != ").append(Type.local.ordinal());
		if(needs != null) {
			findSql.append(" and need.id in(:ids)");
		}
		if(status != null) {
			findSql.append(" and orders.status=:status");
		}
		if(startDate != null && endDate != null) {
			if(timeSearch != null) {
				if(timeSearch.equalsIgnoreCase("createTime")) {
					findSql.append(" and orders.create_date>=:startDate");
					findSql.append(" and orders.create_date<=:endDate");
				}else if(timeSearch.equalsIgnoreCase("completeTime")) {
					findSql.append(" and orders.complete_date>=:startDate");
					findSql.append(" and orders.complete_date<=:endDate");
				}else {
					findSql.append(" and orders.re_date>=:startDate");
					findSql.append(" and orders.re_date<=:endDate");
				}
			}else {
				findSql.append(" and orders.re_date>=:startDate");
				findSql.append(" and orders.re_date<=:endDate");
			}
		}
		if(searchName != null) {
			findSql.append(" and (orders.sn like :searchName or supplier.name like :searchName or need.name like :searchName)");
		}
		findSql.append(" ORDER BY orders.create_date ASC");
		Query query = entityManager.createNativeQuery(findSql.toString()) ;
		List<Long> ids = new ArrayList<>();
		if(needs != null) {
			for(Need need : needs) {
				ids.add(need.getId());
			}
			query.setParameter("ids" , ids) ;
		}
		if(status != null) {
			query.setParameter("status" , status.ordinal()) ;
		}
		if(startDate != null && endDate != null) {
			query.setParameter("startDate" , startDate) ;
			query.setParameter("endDate" , endDate) ;
		}
		if(searchName != null) {
			query.setParameter("searchName" , "%"+searchName+"%") ;
		}
		query.unwrap(SQLQuery.class).addEntity(Goods.class).addEntity(Supplier.class).addScalar("quantity" , IntegerType.INSTANCE).addScalar("createDate" , DateType.INSTANCE).addScalar("supplierName" , StringType.INSTANCE).setResultTransformer(Transformers.aliasToBean(GoodSupplierDto.class));
		List<GoodSupplierDto> lists = query.getResultList();
		return lists;
	}

	@Override
	public List<Product> findBuyersByProduct(Order.Status status,Set<Need> needs, 
			Date startDate, Date endDate , String searchName , String timeSearch) {
		StringBuffer findSql = new StringBuffer("select DISTINCT product.* from xx_order orders");
		findSql.append(" INNER JOIN t_need need on orders.need = need.id");
		findSql.append(" INNER JOIN t_supplier supplier on orders.supplier = supplier.id");
		findSql.append(" INNER JOIN xx_order_item orderItem on orders.id = orderItem.orders");
		findSql.append(" INNER JOIN xx_product product on orderItem.product = product.id");
		findSql.append(" and  orders.type != ").append(Type.local.ordinal());
		findSql.append(" where 1 = 1");
		if(needs != null) {
			findSql.append(" and need.id in(:ids)");
		}
		if(status != null) {
			findSql.append(" and orders.status=:status");
		}
		if(startDate != null && endDate != null) {
			if(timeSearch != null) {
				if(timeSearch.equalsIgnoreCase("createTime")) {
					findSql.append(" and orders.create_date>=:startDate");
					findSql.append(" and orders.create_date<=:endDate");
				}else if(timeSearch.equalsIgnoreCase("completeTime")) {
					findSql.append(" and orders.complete_date>=:startDate");
					findSql.append(" and orders.complete_date<=:endDate");
				}else {
					findSql.append(" and orders.re_date>=:startDate");
					findSql.append(" and orders.re_date<=:endDate");
				}
			}else {
				findSql.append(" and orders.re_date>=:startDate");
				findSql.append(" and orders.re_date<=:endDate");
			}
		}
		if(searchName != null) {
			findSql.append(" and (orders.sn like :searchName or supplier.name like :searchName or need.name like :searchName)");
		}
		
		Query query = entityManager.createNativeQuery(findSql.toString() , Product.class) ;
		List<Long> ids = new ArrayList<>();
		if(needs != null) {
			for(Need need : needs) {
				ids.add(need.getId());
			}
			query.setParameter("ids" , ids) ;
		}
		if(status != null) {
			query.setParameter("status" , status.ordinal()) ;
		}
		if(startDate != null && endDate != null) {
			query.setParameter("startDate" , startDate) ;
			query.setParameter("endDate" , endDate) ;
		}
		if(searchName != null) {
			query.setParameter("searchName" , "%"+searchName+"%") ;
		}
		List<Product> lists = query.getResultList();
		return lists;
	}

	@Override
	public List<Product> findByids(Long[] ids) {
		StringBuffer findSql = new StringBuffer("select DISTINCT product.* from xx_product product");
		findSql.append(" INNER JOIN xx_order_item orderItem on product.id = orderItem.product");
		findSql.append(" INNER JOIN xx_order orders on orderItem.orders = orders.id");
		findSql.append(" where orders.id in (:ids)");
		TypedQuery<Product> query = (TypedQuery<Product>) entityManager.createNativeQuery(findSql.toString(),Product.class).setParameter("ids",Arrays.asList(ids));
		return query.getResultList();
	}

	@Override
	public List<Supplier> queryBuyersByids(Long[] ids) {
		StringBuffer buffer = new StringBuffer("select DISTINCT supplier.* from t_supplier supplier");
		buffer.append(" INNER JOIN xx_order orders on orders.to_supplier = supplier.id");
		buffer.append(" where orders.id in (:ids)");
		TypedQuery<Supplier> query = (TypedQuery<Supplier>) entityManager.createNativeQuery(buffer.toString(),Supplier.class).setParameter("ids",Arrays.asList(ids));
		return query.getResultList();
	}

	@Override
	public List<GoodNeedDto> queryGoodsAndNeedByids(Long[] ids) {
		StringBuffer buffer = new StringBuffer("select goods.*,areas.full_name fullName,need.address address,orderItem.quantity quantity,orders.create_date createDate,need.name needName,orders.phone phone from xx_goods goods");
		buffer.append(" INNER JOIN xx_product product on goods.id = product.goods");
		buffer.append(" INNER JOIN xx_order_item orderItem on product.id = orderItem.product");
		buffer.append(" INNER JOIN xx_order orders on orders.id = orderItem.orders ");
		buffer.append(" INNER JOIN t_need need on orders.need = need.id");
		buffer.append(" INNER JOIN xx_area areas on areas.id = need.area");
		buffer.append(" where orders.id in (:ids)");
		buffer.append(" ORDER BY orders.create_date ASC");
		Query query = entityManager.createNativeQuery(buffer.toString()).setParameter("ids",Arrays.asList(ids));
		query.unwrap(SQLQuery.class).addEntity(Goods.class).addScalar("fullName" , StringType.INSTANCE).addScalar("address" , StringType.INSTANCE).addScalar("quantity" , IntegerType.INSTANCE).addScalar("createDate" , DateType.INSTANCE).addScalar("needName" , StringType.INSTANCE).addScalar("phone" , StringType.INSTANCE).setResultTransformer(Transformers.aliasToBean(GoodNeedDto.class));
		List<GoodNeedDto> lists = query.getResultList();
		return query.getResultList();
	}

	@Override
	public List<Supplier> querySupplierByids(Long[] ids) {
		StringBuffer buffer = new StringBuffer("select DISTINCT supplier.* from t_supplier supplier");
		buffer.append(" INNER JOIN xx_order orders on supplier.id = orders.supplier");
		buffer.append(" where orders.id in (:ids)");
		TypedQuery<Supplier> query = (TypedQuery<Supplier>) entityManager.createNativeQuery(buffer.toString(),Supplier.class).setParameter("ids",Arrays.asList(ids));
		return query.getResultList();
	}

	@Override
	public List<GoodSupplierDto> queryGoodSupplierByids(Long[] ids) {
		StringBuffer buffer = new StringBuffer("select goods.*,supplier.*,supplier.name supplierName,orders.quantity quantity,orders.create_date createDate from xx_goods goods");
		buffer.append(" INNER JOIN xx_product product on goods.id = product.goods");
		buffer.append(" INNER JOIN xx_order_item orderItem on orderItem.product = product.id");
		buffer.append(" INNER JOIN xx_order orders on orderItem.orders = orders.id");
		buffer.append(" INNER JOIN t_supplier supplier on supplier.id = orders.supplier");
		buffer.append(" where orders.id in (:ids)");
		buffer.append(" ORDER BY orders.create_date ASC");
		Query query = entityManager.createNativeQuery(buffer.toString()).setParameter("ids",Arrays.asList(ids));
		query.unwrap(SQLQuery.class).addEntity(Goods.class).addEntity(Supplier.class).addScalar("quantity" , IntegerType.INSTANCE).addScalar("createDate" , DateType.INSTANCE).addScalar("supplierName" , StringType.INSTANCE).setResultTransformer(Transformers.aliasToBean(GoodSupplierDto.class));
		List<GoodSupplierDto> lists = query.getResultList();
		return query.getResultList();
	}

	/**
	 * 统计直销单和拆单后的直销单、拆单后的分销单中当前企业为分销商的订单
	 */
	@Override
	public List<OrderReportDto> queryOrderReportByDate(List<Integer> status, Date startDate, Date endDate, 
				Supplier supplier, ChildMember childMember) {
		List<Integer> typeOne=new ArrayList<>();
		typeOne.add(Order.Type.general.ordinal());
		typeOne.add(Order.Type.billGeneral.ordinal());
		
		List<Integer> typeTwo=new ArrayList<>();
		typeTwo.add(Order.Type.billDistribution.ordinal());
		
		StringBuffer buffer = new StringBuffer("SELECT SUM(a.con) orderNumber,count(DISTINCT a.need) customersNumber,SUM(a.amount) amount,a.d reportDate FROM ( ");
		buffer.append(" SELECT count(orders.id) con, SUM(orders.amount) amount,date(orders.create_date) d,orders.need need FROM xx_order orders ");
		buffer.append(" WHERE 1=1  ");
		if (childMember != null) {
			buffer.append(" and orders.child_member = :childMember ");
		}
		if (startDate != null) {
			buffer.append(" and orders.create_date >= :startDate ");
		}
		if (endDate != null) {
			buffer.append(" AND orders.create_date <= :endDate ");
		}
		if (status != null) {
			buffer.append(" AND orders.status in (:status) ");
		}
		if (supplier != null) {
			buffer.append(" AND ( orders.supplier =:supplier and orders.type in (:typeOne) ) or ( orders.to_supplier =:supplier and orders.type in (:typeTwo) ) ");
		}
		buffer.append(" GROUP BY d,need ");
		buffer.append(" ) a  GROUP BY reportDate ");
		Query query = entityManager.createNativeQuery(buffer.toString());
		if (childMember != null) {
			query.setParameter("childMember", childMember);
		}
		if (startDate != null) {
			query.setParameter("startDate",startDate);
		}
		if (endDate != null) {
			query.setParameter("endDate",endDate);
		}
		if (status != null) {
			query.setParameter("status", status);
		}
		if (supplier != null) {
			query.setParameter("supplier", supplier.getId());
		}
		query.setParameter("typeOne", typeOne);
		query.setParameter("typeTwo", typeTwo);
		query.unwrap(SQLQuery.class).addScalar("orderNumber", IntegerType.INSTANCE).addScalar("customersNumber", IntegerType.INSTANCE).addScalar("amount",BigDecimalType.INSTANCE).addScalar("reportDate", StringType.INSTANCE).setResultTransformer(Transformers.aliasToBean(OrderReportDto.class));
		List<OrderReportDto> lists = query.getResultList();
		return lists;
	}
	
	/**
	 * 统计拆单后的分销单中当前企业为供应商的订单
	 */
	@Override
	public List<OrderReportDto> queryOrderReportByDate2(List<Integer> status, Date startDate, Date endDate,Supplier supplier) {
		List<Integer> type=new ArrayList<>();
		type.add(Order.Type.billDistribution.ordinal());
		type.add(Order.Type.formal.ordinal());
		
		StringBuffer buffer = new StringBuffer("SELECT SUM(a.con) orderNumber,count(DISTINCT a.tosupplier) customersNumber,SUM(a.amount) amount,a.d reportDate FROM ( ");
		buffer.append(" SELECT count(orders.id) con, SUM(orders.amount_tob) amount,date(orders.create_date) d,orders.to_supplier tosupplier FROM xx_order orders ");
		buffer.append(" WHERE 1=1  ");
		if (startDate != null) {
			buffer.append(" and orders.create_date >= :startDate ");
		}
		if (endDate != null) {
			buffer.append(" AND orders.create_date <= :endDate ");
		}
		if (status != null) {
			buffer.append(" AND orders.status in (:status) ");
		}
		if (supplier != null) {
			buffer.append(" AND orders.supplier = :supplier and orders.type in (:type) ");
		}
		buffer.append(" GROUP BY d,tosupplier ");
		buffer.append(") a  GROUP BY reportDate ");
		Query query = entityManager.createNativeQuery(buffer.toString());
		if (startDate != null) {
			query.setParameter("startDate",startDate);
		}
		if (endDate != null) {
			query.setParameter("endDate",endDate);
		}
		if (status != null) {
			query.setParameter("status", status);
		}
		if (supplier != null) {
			query.setParameter("supplier", supplier.getId());
		}
		query.setParameter("type", type);
		query.unwrap(SQLQuery.class).addScalar("orderNumber", IntegerType.INSTANCE).addScalar("customersNumber", IntegerType.INSTANCE).addScalar("amount",BigDecimalType.INSTANCE).addScalar("reportDate", StringType.INSTANCE).setResultTransformer(Transformers.aliasToBean(OrderReportDto.class));
		List<OrderReportDto> lists = query.getResultList();
		return lists;
	}

	@Override
	public List<OrderReportDto> queryPurchaseFormByDate(List<Integer> status, Date startDate, Date endDate,
			Supplier supplier) {
		List<Integer> types=new ArrayList<>();
		types.add(Order.Type.billDistribution.ordinal());
		types.add(Order.Type.formal.ordinal());
		StringBuffer buffer = new StringBuffer("SELECT SUM(a.con) orderNumber,count(DISTINCT a.supplier) customersNumber,SUM(a.amount) amount,a.d reportDate FROM ( ");
		buffer.append(" SELECT count(orders.id) con, SUM(orders.amount_tob) amount,date(orders.create_date) d,orders.supplier supplier FROM xx_order orders");
		buffer.append(" WHERE 1=1  ");
		if (startDate != null) {
			buffer.append(" and orders.create_date >= :startDate ");
		}
		if (endDate != null) {
			buffer.append(" AND orders.create_date <= :endDate ");
		}
		if (status != null) {
			buffer.append(" AND orders.status in (:status) ");
		}
		if (supplier != null) {
			buffer.append(" AND orders.to_supplier = :supplier ");
		}
		buffer.append(" AND orders.type in (:type) ");
		buffer.append(" GROUP BY d,supplier ");
		buffer.append(") a  GROUP BY reportDate ");
		Query query = entityManager.createNativeQuery(buffer.toString());
		if (startDate != null) {
			query.setParameter("startDate",startDate);
		}
		if (endDate != null) {
			query.setParameter("endDate",endDate);
		}
		if (status != null) {
			query.setParameter("status", status);
		}
		if (supplier != null) {
			query.setParameter("supplier", supplier.getId());
		}
		query.setParameter("type", types);
		query.unwrap(SQLQuery.class).addScalar("orderNumber", IntegerType.INSTANCE).addScalar("customersNumber", IntegerType.INSTANCE).addScalar("amount",BigDecimalType.INSTANCE).addScalar("reportDate", StringType.INSTANCE).setResultTransformer(Transformers.aliasToBean(OrderReportDto.class));
		List<OrderReportDto> lists = query.getResultList();
		return lists;
	}

	@Override
	public Integer getCustomersNumber(List<Integer> status, Date startDate, Date endDate, Supplier supplier) {
		List<Integer> typeOne=new ArrayList<>();
		typeOne.add(Order.Type.general.ordinal());
		typeOne.add(Order.Type.billGeneral.ordinal());
		
		List<Integer> typeTwo=new ArrayList<>();
		typeTwo.add(Order.Type.billDistribution.ordinal());
		
		StringBuffer buffer = new StringBuffer(" SELECT count(DISTINCT a.need) customersNumber ");
		buffer.append(" FROM( SELECT orders.need need FROM xx_order orders ");
		buffer.append(" WHERE 1=1  ");
		if (startDate != null) {
			buffer.append(" and orders.create_date >= :startDate ");
		}
		if (endDate != null) {
			buffer.append(" AND orders.create_date <= :endDate ");
		}
		if (status != null) {
			buffer.append(" AND orders.status in (:status) ");
		}
		if (supplier != null) {
			buffer.append(" AND ( orders.supplier =:supplier and orders.type in (:typeOne) ) or ( orders.to_supplier =:supplier and orders.type in (:typeTwo) ) ");
		}
		buffer.append(" GROUP BY need ");
		buffer.append(" ) a ");
		Query query = entityManager.createNativeQuery(buffer.toString());
		if (startDate != null) {
			query.setParameter("startDate",startDate);
		}
		if (endDate != null) {
			query.setParameter("endDate",endDate);
		}
		if (status != null) {
			query.setParameter("status", status);
		}
		if (supplier != null) {
			query.setParameter("supplier", supplier.getId());
		}
		query.setParameter("typeOne", typeOne);
		query.setParameter("typeTwo", typeTwo);
		//个体客户
		Integer csustomersNumber=((BigInteger) query.getSingleResult()).intValue();
		
		//企业客户
		buffer = new StringBuffer(" SELECT count(DISTINCT a.toSupplier) customersNumber ");
		buffer.append(" FROM( SELECT orders.to_supplier toSupplier FROM xx_order orders ");
		buffer.append(" WHERE 1=1  ");
		if (startDate != null) {
			buffer.append(" and orders.create_date >= :startDate ");
		}
		if (endDate != null) {
			buffer.append(" AND orders.create_date <= :endDate ");
		}
		if (status != null) {
			buffer.append(" AND orders.status in (:status) ");
		}
		if (supplier != null) {
			buffer.append(" AND orders.supplier =:supplier and orders.type in (:typeTwo) ");
		}
		buffer.append(" GROUP BY toSupplier ");
		buffer.append(" ) a ");
		query = entityManager.createNativeQuery(buffer.toString());
		if (startDate != null) {
			query.setParameter("startDate",startDate);
		}
		if (endDate != null) {
			query.setParameter("endDate",endDate);
		}
		if (status != null) {
			query.setParameter("status", status);
		}
		if (supplier != null) {
			query.setParameter("supplier", supplier.getId());
		}
		query.setParameter("typeTwo", typeTwo);
		Integer supplierNumber=((BigInteger) query.getSingleResult()).intValue();
		
		return csustomersNumber+supplierNumber;
	}

	@Override
	public Integer getPurchaseCustomersNumber(List<Integer> status, Date startDate, Date endDate, Supplier supplier) {
		StringBuffer buffer = new StringBuffer("SELECT	count(DISTINCT a.supplier) c FROM	(");
		buffer.append(" SELECT orders.supplier supplier FROM xx_order orders ");
		buffer.append(" WHERE 1=1 ");
		if (startDate != null) {
			buffer.append(" and orders.create_date >= :startDate ");
		}
		if (endDate != null) {
			buffer.append(" AND orders.create_date <= :endDate ");
		}
		if (status != null) {
			buffer.append(" AND orders.status in (:status) ");
		}
		if (supplier != null) {
			buffer.append(" AND orders.to_supplier = :supplier ");
		}
		buffer.append(" GROUP BY supplier ");
		buffer.append(" ) a ");
		Query query = entityManager.createNativeQuery(buffer.toString());
		if (startDate != null) {
			query.setParameter("startDate",startDate);
		}
		if (endDate != null) {
			query.setParameter("endDate",endDate);
		}
		if (status != null) {
			query.setParameter("status", status);
		}
		if (supplier != null) {
			query.setParameter("supplier", supplier.getId());
		}
		Integer csustomersNumber=((BigInteger) query.getSingleResult()).intValue();
				
		return csustomersNumber;
	}

	@Override
	public List<CustomerReportDto> queryCustomersReportByDate(List<Integer> status, Date startDate, Date endDate, Supplier supplier) {
		
		StringBuffer buffer = new StringBuffer("SELECT count(orders.id) orderNumber,sum(orders.amount) amount,needs.name name,needs.id needId  ");
		buffer.append(" FROM xx_order orders LEFT JOIN t_need needs on needs.id=orders.need left join t_supplier supplier on supplier.id=needs.supplier ");
		buffer.append(" WHERE 1 = 1 and supplier.id=:supplier ");
		if (startDate != null) {
			buffer.append(" and orders.create_date >= :startDate ");
		}
		if (endDate != null) {
			buffer.append(" AND orders.create_date <= :endDate ");
		}
		if (status != null) {
			buffer.append(" AND orders.status in (:status) ");
		}
		if (supplier != null) {
			buffer.append(" AND ( orders.supplier =:supplier or (orders.to_supplier =:supplier and orders.type != :formal)  ) and orders.type != :distribution ");
		}
		buffer.append(" GROUP BY needs.id ");
		buffer.append(" ORDER BY orderNumber DESC ");
		
		Query query = entityManager.createNativeQuery(buffer.toString());
		if (startDate != null) {
			query.setParameter("startDate",startDate);
		}
		if (endDate != null) {
			query.setParameter("endDate",endDate);
		}
		if (status != null) {
			query.setParameter("status", status);
		}
		if (supplier != null) {
			query.setParameter("supplier", supplier.getId());
		}
		query.setParameter("formal", Order.Type.formal.ordinal());
		query.setParameter("distribution", Order.Type.distribution.ordinal());
		query.unwrap(SQLQuery.class).addScalar("needId", StringType.INSTANCE).addScalar("name", StringType.INSTANCE).addScalar("orderNumber", IntegerType.INSTANCE).addScalar("amount",BigDecimalType.INSTANCE).setResultTransformer(Transformers.aliasToBean(CustomerReportDto.class));
		List<CustomerReportDto> lists = query.getResultList();
		return lists;
	}
	
	@Override
	public List<CustomerReportDto> queryCustomersReportByDate2(List<Integer> status, Date startDate, Date endDate, Supplier supplier) {
		
		StringBuffer buffer = new StringBuffer("SELECT count(orders.id) orderNumber,sum(orders.amount_tob) amount,supplier.name name,supplier.id supplierId ");
		buffer.append(" FROM xx_order orders LEFT JOIN t_need needs on needs.id=orders.need left join t_supplier supplier on supplier.id=needs.supplier  ");
		buffer.append(" WHERE 1 = 1 and supplier.id != :supplier ");
		if (startDate != null) {
			buffer.append(" and orders.create_date >= :startDate ");
		}
		if (endDate != null) {
			buffer.append(" AND orders.create_date <= :endDate ");
		}
		if (status != null) {
			buffer.append(" AND orders.status in (:status) ");
		}
		if (supplier != null) {
			buffer.append(" AND ( orders.supplier =:supplier or (orders.to_supplier =:supplier and orders.type != :formal) ) and orders.type != :distribution ");
		}
		buffer.append(" GROUP BY supplier.id ");
		buffer.append(" ORDER BY orderNumber DESC ");
		
		Query query = entityManager.createNativeQuery(buffer.toString());
		if (startDate != null) {
			query.setParameter("startDate",startDate);
		}
		if (endDate != null) {
			query.setParameter("endDate",endDate);
		}
		if (status != null) {
			query.setParameter("status", status);
		}
		if (supplier != null) {
			query.setParameter("supplier", supplier.getId());
		}
		query.setParameter("formal", Order.Type.formal.ordinal());
		query.setParameter("distribution", Order.Type.distribution.ordinal());
		query.unwrap(SQLQuery.class).addScalar("supplierId", StringType.INSTANCE).addScalar("name", StringType.INSTANCE).addScalar("orderNumber", IntegerType.INSTANCE).addScalar("amount",BigDecimalType.INSTANCE).setResultTransformer(Transformers.aliasToBean(CustomerReportDto.class));
		List<CustomerReportDto> lists = query.getResultList();
		return lists;
	}
	
	@Override
	public CustomerReportDto queryCustomersReportGoodByDate(List<Integer> status, Date startDate, Date endDate,
			Supplier supplier, String needId,String supplierId) {
		
		StringBuffer buffer = new StringBuffer(" SELECT IFNULL(count(DISTINCT goods.id),0) goodsNumber,IFNULL(SUM(orderItem.quantity),0) orderQuantity FROM xx_order orders ");
		buffer.append(" LEFT JOIN xx_order_item orderItem ON orderItem.orders = orders.id ");
		buffer.append(" LEFT JOIN xx_product products ON products.id = orderItem.product ");
		buffer.append(" LEFT JOIN xx_goods goods ON goods.id = products.goods ");
		buffer.append(" where 1=1 ");
		
		if (startDate != null) {
			buffer.append(" and orders.create_date >= :startDate ");
		}
		if (endDate != null) {
			buffer.append(" AND orders.create_date <= :endDate ");
		}
		if (status != null) {
			buffer.append(" AND orders.status in (:status) ");
		}
		
		buffer.append(" AND ( orders.supplier =:supplier or (orders.to_supplier =:supplier and orders.type != :formal) ) and orders.type != :distribution  ");
		if (StringUtils.isNotBlank(needId)) {
			buffer.append(" AND orders.need = :needId ");
		}
		if (StringUtils.isNotBlank(supplierId)) {
			buffer.append(" AND orders.to_supplier = :supplierId ");
		}
		
		Query query = entityManager.createNativeQuery(buffer.toString());
		if (startDate != null) {
			query.setParameter("startDate",startDate);
		}
		if (endDate != null) {
			query.setParameter("endDate",endDate);
		}
		if (status != null) {
			query.setParameter("status", status);
		}
		if (supplier != null) {
			query.setParameter("supplier", supplier.getId());
		}
		if (StringUtils.isNotBlank(needId)) {
			query.setParameter("needId", needId);
		}
		if (StringUtils.isNotBlank(supplierId)) {
			query.setParameter("supplierId",supplierId);
		}
		query.setParameter("formal", Order.Type.formal.ordinal());
		query.setParameter("distribution", Order.Type.distribution.ordinal());
		query.unwrap(SQLQuery.class).addScalar("goodsNumber", IntegerType.INSTANCE).addScalar("orderQuantity", IntegerType.INSTANCE).setResultTransformer(Transformers.aliasToBean(CustomerReportDto.class));
		CustomerReportDto dto = (CustomerReportDto) query.getSingleResult();
		return dto;
	}
	@Override
	public Integer searchByStatus(Status status, Supplier supplier) {
		StringBuffer buffer = new StringBuffer("select COUNT(orders.id) from xx_order orders where 1=1");
		if(null != supplier) {
			buffer.append(" and (orders.supplier=:supplierId or orders.to_supplier=:supplierId)");
		}
		if(null != status) {
			buffer.append(" and orders.status=:status");
		}
		buffer.append(" and orders.type != 1");
		buffer.append(" and  orders.type != ").append(Type.local.ordinal());

		Query query = entityManager.createNativeQuery(buffer.toString());
		if(null != supplier) {
			query.setParameter("supplierId", supplier.getId());
		}
		if(null != status) {
			query.setParameter("status", status.ordinal());
		}
		Integer count=((BigInteger) query.getSingleResult()).intValue();
		return count;
	}
	
	@Override
	public OrderStatisticsDto todayOrderForm1(Supplier supplier, Date startDate,
			Date endDate) {
		StringBuffer buffer = new StringBuffer("select COUNT(orders.id) orderTotal,SUM(orders.amount) totalAmount from xx_order orders where 1=1");
		if(null != supplier) {
			buffer.append(" and ((orders.supplier=:supplierId and orders.type IN (0,2) ) or (orders.to_supplier=:supplierId and orders.type IN (3)))");
		}
		if(null != startDate) {
			buffer.append(" and orders.create_date >= :startDate");
		}
		if(null != endDate) {
			buffer.append(" and orders.create_date <= :endDate");
		}
		buffer.append(" and orders.status in(1,2,12,3,5,9)");
		
		Query query = entityManager.createNativeQuery(buffer.toString());
		if(null != supplier) {
			query.setParameter("supplierId", supplier.getId());
		}
		if(null != startDate) {
			query.setParameter("startDate", startDate);
		}
		if(null != endDate) {
			query.setParameter("endDate",endDate);
		}
		query.unwrap(SQLQuery.class).addScalar("orderTotal", IntegerType.INSTANCE).addScalar("totalAmount",BigDecimalType.INSTANCE).setResultTransformer(Transformers.aliasToBean(OrderStatisticsDto.class));
		return (OrderStatisticsDto) query.getSingleResult();
	}
	
	@Override
	public OrderStatisticsDto todayOrderForm2(Supplier supplier, Date startDate,
			Date endDate) {
		StringBuffer buffer = new StringBuffer("select COUNT(orders.id) orderTotal,SUM(orders.amount_tob) totalAmount from xx_order orders where 1=1");
		if(null != supplier) {
			buffer.append(" and orders.supplier=:supplierId and orders.type IN (3,4)");
		}
		if(null != startDate) {
			buffer.append(" and orders.create_date >= :startDate");
		}
		if(null != endDate) {
			buffer.append(" and orders.create_date <= :endDate");
		}
		buffer.append(" and orders.status in(1,2,12,3,5,9)");
		
		Query query = entityManager.createNativeQuery(buffer.toString());
		if(null != supplier) {
			query.setParameter("supplierId", supplier.getId());
		}
		if(null != startDate) {
			query.setParameter("startDate", startDate);
		}
		if(null != endDate) {
			query.setParameter("endDate",endDate);
		}
		query.unwrap(SQLQuery.class).addScalar("orderTotal", IntegerType.INSTANCE).addScalar("totalAmount",BigDecimalType.INSTANCE).setResultTransformer(Transformers.aliasToBean(OrderStatisticsDto.class));
		return (OrderStatisticsDto) query.getSingleResult();
	}

	@Override
	public OrderStatisticsDto orderRelated1(Supplier supplier) {
		StringBuffer buffer = new StringBuffer("select COUNT(DISTINCT orders.need) supplierTotal from xx_order orders");
		buffer.append(" LEFT JOIN xx_order_item orderItem on orders.id = orderItem.orders");
		buffer.append(" LEFT JOIN xx_product product on orderItem.product = product.id");
		buffer.append(" LEFT JOIN xx_goods goods on product.goods = goods.id where 1=1");
		if(null != supplier) {
			buffer.append(" and ((orders.supplier=:supplierId and orders.type IN (0,2) ) or (orders.to_supplier=:supplierId and orders.type IN (3)))");
		}
		Query query = entityManager.createNativeQuery(buffer.toString());
		if(null != supplier) {
			query.setParameter("supplierId", supplier.getId());
		}
		query.unwrap(SQLQuery.class).addScalar("supplierTotal",IntegerType.INSTANCE).setResultTransformer(Transformers.aliasToBean(OrderStatisticsDto.class));
		return (OrderStatisticsDto) query.getSingleResult();
	}
	
	@Override
	public OrderStatisticsDto orderRelated2(Supplier supplier) {
		StringBuffer buffer = new StringBuffer("select COUNT(DISTINCT orders.to_supplier) supplierTotal from xx_order orders");
		buffer.append(" LEFT JOIN xx_order_item orderItem on orders.id = orderItem.orders");
		buffer.append(" LEFT JOIN xx_product product on orderItem.product = product.id");
		buffer.append(" LEFT JOIN xx_goods goods on product.goods = goods.id where 1=1");
		if(null != supplier) {
			buffer.append(" and orders.supplier=:supplierId and orders.type IN (3)");
		}
		Query query = entityManager.createNativeQuery(buffer.toString());
		if(null != supplier) {
			query.setParameter("supplierId", supplier.getId());
		}
		query.unwrap(SQLQuery.class).addScalar("supplierTotal",IntegerType.INSTANCE).setResultTransformer(Transformers.aliasToBean(OrderStatisticsDto.class));
		return (OrderStatisticsDto) query.getSingleResult();
	}
	
	@Override
	public OrderStatisticsDto countOrderGoods(Supplier supplier) {
		StringBuffer buffer = new StringBuffer("select COUNT(DISTINCT goods.id) goodTotal from xx_order orders");
		buffer.append(" LEFT JOIN xx_order_item orderItem on orders.id = orderItem.orders");
		buffer.append(" LEFT JOIN xx_product product on orderItem.product = product.id");
		buffer.append(" LEFT JOIN xx_goods goods on product.goods = goods.id where 1=1");
		buffer.append(" and orders.type != 1");
		buffer.append(" and  orders.type != ").append(Type.local.ordinal());
		if(null != supplier) {
			buffer.append(" and (orders.supplier=:supplierId or orders.to_supplier=:supplierId)");
		}
		Query query = entityManager.createNativeQuery(buffer.toString());
		if(null != supplier) {
			query.setParameter("supplierId", supplier.getId());
		}
		query.unwrap(SQLQuery.class).addScalar("goodTotal", IntegerType.INSTANCE).setResultTransformer(Transformers.aliasToBean(OrderStatisticsDto.class));
		return (OrderStatisticsDto) query.getSingleResult();
	}
	

	@Override
	public Integer purchaseOrderByStaticQuery(Status status, Supplier supplier) {
		StringBuffer buffer = new StringBuffer("select COUNT(orders.id) from xx_order orders where 1=1 ");
		
		if(null != supplier) {
			buffer.append(" and orders.to_supplier = :supplierId");
		}
		if(null != status) {
			buffer.append(" and orders.status = :status");
		}
		buffer.append(" and orders.type !=1");
		buffer.append(" and  orders.type != ").append(Type.local.ordinal());
		Query query = entityManager.createNativeQuery(buffer.toString());
		if(null != supplier) {
			query.setParameter("supplierId", supplier.getId());
		}
		if(null != status) {
			query.setParameter("status", status.ordinal());
		}
		Integer count=((BigInteger) query.getSingleResult()).intValue();
		return count;
	}

	@Override
	public OrderStatisticsDto todayPurchaseOrder(Supplier supplier,
			Date startDate, Date endDate) {
		/*StringBuffer buffer = new StringBuffer("select COUNT(orders.id) orderTotal,SUM(orders.amount) totalAmount from xx_order orders");
		buffer.append(" INNER JOIN xx_member member on orders.member = member.id");
		buffer.append(" INNER JOIN t_need need on member.need = need.id");
		buffer.append(" INNER JOIN t_supplier supplier ON need.supplier = supplier.id where 1=1");*/
		
		StringBuffer buffer = new StringBuffer("select COUNT(orders.id) orderTotal,SUM(orders.amount) totalAmount from xx_order orders where 1=1");
		
		if(null != supplier) {
			buffer.append(" and orders.to_supplier = :supplierId");
		}
		if(null != startDate) {
			buffer.append(" and orders.create_date >= :startDate");
		}
		if(null != endDate) {
			buffer.append(" and orders.create_date <= :endDate");
		}
		buffer.append(" and orders.status in(1,2,12,3,5,9)");
		buffer.append(" and orders.type != 1");
		buffer.append(" and  orders.type != ").append(Type.local.ordinal());
		Query query = entityManager.createNativeQuery(buffer.toString());
		if(null != supplier) {
			query.setParameter("supplierId", supplier.getId());
		}
		if(null != startDate) {
			query.setParameter("startDate", startDate);
		}
		if(null != endDate) {
			query.setParameter("endDate",endDate);
		}
		query.unwrap(SQLQuery.class).addScalar("orderTotal", IntegerType.INSTANCE).addScalar("totalAmount",BigDecimalType.INSTANCE).setResultTransformer(Transformers.aliasToBean(OrderStatisticsDto.class));
		return (OrderStatisticsDto) query.getSingleResult();
	}

	@Override
	public OrderStatisticsDto purchaseOrderRelated(Supplier supplier) {
		StringBuffer buffer = new StringBuffer("select COUNT(DISTINCT goods.id) goodTotal,COUNT(DISTINCT orders.supplier) supplierTotal from xx_order orders");
		buffer.append(" INNER JOIN xx_order_item orderItem on orders.id = orderItem.orders");
		buffer.append(" INNER JOIN xx_product product on orderItem.product = product.id");
		buffer.append(" INNER JOIN xx_goods goods on product.goods = goods.id where 1=1");
		buffer.append(" and  orders.type != ").append(Type.distribution.ordinal());
		buffer.append(" and  orders.type != ").append(Type.local.ordinal());
		if(null != supplier) {
			buffer.append(" and orders.to_supplier = :supplierId");
		}
		Query query = entityManager.createNativeQuery(buffer.toString());
		if(null != supplier) {
			query.setParameter("supplierId", supplier.getId());
		}
		query.unwrap(SQLQuery.class).addScalar("goodTotal", IntegerType.INSTANCE).addScalar("supplierTotal",IntegerType.INSTANCE).setResultTransformer(Transformers.aliasToBean(OrderStatisticsDto.class));
		return (OrderStatisticsDto) query.getSingleResult();
	}

	@Override
	public List<OrderStatisticsDto> orderharts(Supplier supplier,
			Date startDate, Date endDate) {
		StringBuffer buffer = new StringBuffer("select COUNT(orders.id) orderTotal,DATE(orders.create_date) createDate from xx_order orders where 1=1");
		if(null != supplier) {
			buffer.append(" and orders.supplier = :supplierId");
		}
		if(null != startDate) {
			buffer.append(" and orders.create_date >= :startDate");
		}
		if(null != endDate) {
			buffer.append(" and orders.create_date <= :endDate");
		}
		buffer.append(" and orders.status in(1,2,12,3,5,9)");
		buffer.append(" and orders.type != 1");
		buffer.append(" and  orders.type != ").append(Type.local.ordinal());
		buffer.append(" GROUP BY createDate");
		Query query = entityManager.createNativeQuery(buffer.toString());
		if(null != supplier) {
			query.setParameter("supplierId", supplier.getId());
		}
		if(null != startDate) {
			query.setParameter("startDate", startDate);
		}
		if(null != endDate) {
			query.setParameter("endDate",endDate);
		}
		query.unwrap(SQLQuery.class).addScalar("orderTotal", IntegerType.INSTANCE).addScalar("createDate",StringType.INSTANCE).setResultTransformer(Transformers.aliasToBean(OrderStatisticsDto.class));
		return (List<OrderStatisticsDto>) query.getResultList();
	}

	@Override
	public List<OrderStatisticsDto> purchaseOrderCharts(Supplier supplier,
			Date startDate, Date endDate) {
		StringBuffer buffer = new StringBuffer("select COUNT(orders.id) orderTotal,DATE(orders.create_date) createDate from xx_order orders where 1=1");
		if(null != supplier) {
			buffer.append(" and orders.to_supplier = :supplierId");
		}
		if(null != startDate) {
			buffer.append(" and orders.create_date >= :startDate");
		}
		if(null != endDate) {
			buffer.append(" and orders.create_date <= :endDate");
		}
		buffer.append(" and orders.status in(1,2,12,3,5,9)");
		buffer.append(" and orders.type != 1");
		buffer.append(" and  orders.type != ").append(Type.local.ordinal());
		buffer.append(" GROUP BY createDate");
		Query query = entityManager.createNativeQuery(buffer.toString());
		if(null != supplier) {
			query.setParameter("supplierId", supplier.getId());
		}
		if(null != startDate) {
			query.setParameter("startDate", startDate);
		}
		if(null != endDate) {
			query.setParameter("endDate",endDate);
		}
		query.unwrap(SQLQuery.class).addScalar("orderTotal", IntegerType.INSTANCE).addScalar("createDate",StringType.INSTANCE).setResultTransformer(Transformers.aliasToBean(OrderStatisticsDto.class));
		return (List<OrderStatisticsDto>) query.getResultList();
	}

	@Override
	public List<AssListStatisticsDto> findByList(Member member,
			List<Integer> status) {
		if(null == status) {
			return null;
		}
		StringBuffer buffer = new StringBuffer("select year(orders.create_date) years,month(orders.create_date) months, COUNT(*) counts from xx_order orders where 1=1");
		if(null != member) {
			buffer.append(" and orders.member = :member");
		}
		buffer.append(" and ((orders.type != 1 and orders.status in(:status)) or (orders.type = 1 and orders.status in(1,7,8,9)))");
		buffer.append(" and  orders.type != ").append(Type.local.ordinal());
		buffer.append(" GROUP BY years,months ORDER BY years,months asc");
		Query query = entityManager.createNativeQuery(buffer.toString());
		if(null != member) {
			query.setParameter("member", member);
		}
		query.setParameter("status", status);
		query.unwrap(SQLQuery.class).addScalar("years",IntegerType.INSTANCE).addScalar("months",IntegerType.INSTANCE).addScalar("counts", IntegerType.INSTANCE)
		.setResultTransformer(Transformers.aliasToBean(AssListStatisticsDto.class));
		List<AssListStatisticsDto> result = query.getResultList();
		return result;
	}

	@Override
	public List<AssListStatisticsDto> kanbandetail(Member member, Date startDate,
			Date endDate) {
    	StringBuffer sql = new StringBuffer("select orders.status statu, count(*) counts from xx_order orders where 1=1");
    	if(null != member) {
    		sql.append(" and orders.member = :member");
    	}
    	if(null != startDate) {
    		sql.append(" and orders.create_date >= :startDate");
    	}
    	if(null != endDate) {
    		sql.append(" and orders.create_date <= :endDate");
    	}
    	sql.append(" and (orders.type != 1 or (orders.type = 1 and orders.status in(1,7,8,9)))");
		sql.append(" and  orders.type != ").append(Type.local.ordinal());
    	sql.append(" GROUP BY statu");
    	
    	
		Query query = entityManager.createNativeQuery(sql.toString());
		if(null != member) {
			query.setParameter("member", member);
		}
		if(null != startDate) {
			query.setParameter("startDate", startDate);
		}
		if(null != endDate) {
			query.setParameter("endDate", endDate);
		}
		query.unwrap(SQLQuery.class).addScalar("counts", IntegerType.INSTANCE).addScalar("statu", IntegerType.INSTANCE)
		.setResultTransformer(Transformers.aliasToBean(AssListStatisticsDto.class));
		List<AssListStatisticsDto> result = query.getResultList();
		return result;
	}

	@Override
	public List<Order> find(Supplier supplier, Date startDate, Date endDate) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
		Root<Order> root = criteriaQuery.from(Order.class);
		Join<Order, OrderLog> OrderLogJoin = root.join("orderLogs" , JoinType.LEFT);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		
		if(null != supplier){
			restrictions = criteriaBuilder.and(restrictions,criteriaBuilder.or(criteriaBuilder.equal(root.get("supplier"), supplier),criteriaBuilder.and(criteriaBuilder.equal(root.get("toSupplier"), supplier),criteriaBuilder.notEqual(root.get("supplyType"), SupplyType.formal))));
			
		}
		restrictions=criteriaBuilder.and(restrictions,criteriaBuilder.notEqual(root.get("type"), Type.local));

		if (startDate != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(OrderLogJoin.<Date> get("createDate"), startDate));
		}
		if (endDate != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(OrderLogJoin.<Date> get("createDate"), endDate));
		}
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(OrderLogJoin.get("type"), OrderLog.Type.review));
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("status"), Order.Status.pendingShipment));
		criteriaQuery.where(restrictions);
		return super.findList(criteriaQuery, null, null, null, null);
	}

	@Override
	public Page<Order> findPageOrderLocal(String sharingStatus,List<Shop> shops, Shop shop, ChildMember childMember, Pageable pageable) {
		Order.SharingStatus sharingStatuses=null;
		StringBuffer buffer = new StringBuffer(" select DISTINCT orders.* from xx_order orders ");
		if (shop == null){
			buffer.append(" left join t_order_relation relation on relation.orders=orders.id ");
		}
		buffer.append(" where 1=1 and orders.deleted=0 ");
		if (shop!=null){
			buffer.append(" and orders.shop=:shop ");
			if (sharingStatus != null && !sharingStatus.equals("")) {
				switch (sharingStatus) {
				case "noshare":
					sharingStatuses = Order.SharingStatus.noshare;
					break;
				case "share":
					sharingStatuses = Order.SharingStatus.share;
					break;
				case "end":
					sharingStatuses = Order.SharingStatus.end;
					break;
				}
				buffer.append(" and orders.sharing_status=").append(sharingStatuses.ordinal());
			}
		}else {
			if (shops != null && shops.size()>0){
				if (sharingStatus == null || sharingStatus.equals("")) {
					buffer.append(" and (orders.shop in(:shops) or (relation.child_member=:childMember and relation.type=").append(OrderRelation.Type.participant.ordinal()).append("))");
				}else if("participate".equals(sharingStatus)){
					buffer.append(" and relation.child_member=:childMember and relation.type=").append(OrderRelation.Type.participant.ordinal());
				}else {
					switch (sharingStatus) {
						case "noshare":
							sharingStatuses = Order.SharingStatus.noshare;
							break;
						case "share":
							sharingStatuses = Order.SharingStatus.share;
							break;
						case "end":
							sharingStatuses = Order.SharingStatus.end;
							break;
					}
					buffer.append(" and orders.shop in(:shops) ");
					buffer.append(" and orders.sharing_status=").append(sharingStatuses.ordinal());
				}
			}
		}
		buffer.append(" and orders.type=").append(Type.local.ordinal());
		buffer.append(" order by create_date desc ");

		StringBuffer countSql=new StringBuffer(" select COUNT(datas.id) from ( ");
		countSql.append(buffer);
		countSql.append(" ) datas ");

		Query query = entityManager.createNativeQuery(buffer.toString(),Order.class);
		Query countQuery = entityManager.createNativeQuery(countSql.toString());

		if (shop!=null){
			query.setParameter("shop" , shop.getId());
			countQuery.setParameter("shop" ,shop.getId());
		}else {
			if (shops != null && shops.size()>0){
				if (sharingStatus == null || sharingStatus.equals("")) {
					query.setParameter("childMember" , childMember.getId());
					countQuery.setParameter("childMember" ,childMember.getId());
					
					List<Long> shopIds=new ArrayList<>();
					for (Shop shop1:shops){
						shopIds.add(shop1.getId());
					}
					query.setParameter("shops" , shopIds);
					countQuery.setParameter("shops" ,shopIds);
				}else if("participate".equals(sharingStatus)){
					query.setParameter("childMember" , childMember.getId());
					countQuery.setParameter("childMember" ,childMember.getId());
				}else {
					List<Long> shopIds=new ArrayList<>();
					for (Shop shop1:shops){
						shopIds.add(shop1.getId());
					}
					query.setParameter("shops" , shopIds);
					countQuery.setParameter("shops" ,shopIds);
					
				}
			}
		}
		BigInteger tempTotal = (BigInteger) countQuery.getSingleResult();
		int total = tempTotal.intValue();

		int totalPages = (int) Math.ceil((double) total / (double) pageable.getPageSize());
		if (totalPages < pageable.getPageNumber()) {
			pageable.setPageNumber(totalPages);
		}
		query.setFirstResult((pageable.getPageNumber() - 1) * pageable.getPageSize());
		query.setMaxResults(pageable.getPageSize());
		List<Order> list = query.getResultList();

		return new Page<>(list, total, pageable);
	}
	
	@Override
	public List<Order> findNoRakeBackList(){
		String jpql = "select orders from Order orders where orders.status=5 and orders.rake_back=false";
		try {
			return entityManager.createQuery(jpql, Order.class).getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}

}