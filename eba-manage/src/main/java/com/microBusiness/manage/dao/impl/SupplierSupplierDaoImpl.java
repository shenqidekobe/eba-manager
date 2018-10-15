package com.microBusiness.manage.dao.impl;

import java.util.Date;
import java.util.List;

import javax.management.Query;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import com.microBusiness.manage.util.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.SupplierSupplierDao;
import com.microBusiness.manage.entity.Area;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.Order;
import com.microBusiness.manage.entity.ProductCategory;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.entity.SupplierSupplier;
import com.microBusiness.manage.entity.SupplierSupplier.Status;

@Repository("supplierSupplierDaoImpl")
public class SupplierSupplierDaoImpl extends BaseDaoImpl<SupplierSupplier,Long> implements SupplierSupplierDao{

	
	@Override
	public Page<SupplierSupplier> findPage(Pageable pageable, Supplier supplier, Supplier bySupplier, SupplierSupplier.Status status , String searchName) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<SupplierSupplier> criteriaQuery = criteriaBuilder.createQuery(SupplierSupplier.class);
		Root<SupplierSupplier> root = criteriaQuery.from(SupplierSupplier.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (supplier != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("supplier"), supplier));
		}
		if (bySupplier != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("bySupplier"), bySupplier));
		}
		if(status != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("status"), status));
		}
		if(StringUtils.isNotEmpty(searchName)) {
			Join<SupplierSupplier , Supplier> supplierJoin = root.join("bySupplier" , JoinType.LEFT);
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.like(supplierJoin.<String>get("name"), "%"+searchName+"%"));
		}
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("deleted"), false));
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery, pageable);
	}

	@Override
	public List<SupplierSupplier> findByDateList(Supplier supplier, Supplier bySupplier, Date startDate, Date endDate,List<SupplierSupplier.Status> status) {
		//String sql="select supplierSupplier from SupplierSupplier supplierSupplier where supplier=:supplier and bySupplier=:bySupplier and ((startDate <= :startDate and endDate >= :startDate) or (startDate <= :endDate and endDate >= :endDate) or (startDate <= :startDate and endDate >= :endDate) or (startDate >= :startDate and endDate <= :endDate)) and status in (:status)";
		String sql="select supplierSupplier from SupplierSupplier supplierSupplier where supplier=:supplier and bySupplier=:bySupplier and status in (:status)";
		// FIXME: 2017/4/7 这样写的代码维护性太低了
		TypedQuery<SupplierSupplier> query = entityManager.createQuery(sql, SupplierSupplier.class).setParameter("supplier", supplier).setParameter("bySupplier", bySupplier).setParameter("status", status);
		return query.getResultList();
	}

	@Override
	public void updateExpiredSupply(Date compareDate , SupplierSupplier.Status status) {
		String jpql = "update SupplierSupplier supplierSupplier set supplierSupplier.status=:status where supplierSupplier.endDate<:compareDate " ;
		entityManager.createQuery(jpql).setParameter("status" , SupplierSupplier.Status.expired).setParameter("compareDate" , compareDate).executeUpdate() ;
	}

	@Override
	public void dealWillSupplyToSupply(Date compareDate) {
		String jpql = "update SupplierSupplier supplierSupplier set supplierSupplier.status=:status where (supplierSupplier.startDate<=:compareDate and :compareDate <= supplierSupplier.endDate) and supplierSupplier.status=:upStatus " ;
		entityManager.createQuery(jpql).setParameter("status" , SupplierSupplier.Status.inTheSupply).setParameter("upStatus" , SupplierSupplier.Status.willSupply).setParameter("compareDate" , compareDate).executeUpdate() ;
	}

	/**
	 * 供应分配列表
	 */
	@Override
	public Page<SupplierSupplier> supplyDistributionList(Pageable pageable,
			Supplier supplier, Supplier bySupplier, Status status,
			String searchName) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<SupplierSupplier> criteriaQuery = criteriaBuilder.createQuery(SupplierSupplier.class);
		Root<SupplierSupplier> root = criteriaQuery.from(SupplierSupplier.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (supplier != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("supplier"), supplier));
		}
		if (bySupplier != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("bySupplier"), bySupplier));
		}
		if(status != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("status"), status));
		}
		if(StringUtils.isNotEmpty(searchName)) {
			Join<SupplierSupplier , Supplier> supplierJoin = root.join("supplier" , JoinType.LEFT);
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.like(supplierJoin.<String>get("name"), "%"+searchName+"%"));
		}
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery, pageable);
	}

	@Override
	public SupplierSupplier getSupplierSupplier(Supplier bySupplier, Supplier supplier,Date orderTime,List<SupplierSupplier.Status> status) {
        try {
            String jpql = "select supplierSupplier from SupplierSupplier supplierSupplier where bySupplier=:bySupplier and supplier=:supplier ";
            if (orderTime != null) {
				jpql+=" and :orderTime>=startDate and :orderTime<=endDate ";
			}
            if (status != null) {
            	jpql+=" and status in(:status) ";
			}
            TypedQuery<SupplierSupplier> query= entityManager.createQuery(jpql, SupplierSupplier.class).setParameter("bySupplier", bySupplier).setParameter("supplier", supplier);
            if (orderTime != null) {
            	query.setParameter("orderTime", orderTime);
            }
            if (status!=null) {
            	query.setParameter("status", status);
			}
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
	}

	@Override
	public List<SupplierSupplier> getSupplierSupplierList(Supplier bySupplier, Supplier supplier) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<SupplierSupplier> criteriaQuery = criteriaBuilder.createQuery(SupplierSupplier.class);
		Root<SupplierSupplier> root = criteriaQuery.from(SupplierSupplier.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("supplier"), supplier));
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("bySupplier"), bySupplier));
		criteriaQuery.where(restrictions);
		return super.findList(criteriaQuery, null, null, null, null);
	}

	@Override
	public List<SupplierSupplier> getListBySupplier(Supplier bySupplier, List<Status> status) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<SupplierSupplier> criteriaQuery = criteriaBuilder.createQuery(SupplierSupplier.class);
		Root<SupplierSupplier> root = criteriaQuery.from(SupplierSupplier.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (bySupplier != null){
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("bySupplier"), bySupplier));
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(root.<Date> get("startDate"), new Date()),criteriaBuilder.greaterThanOrEqualTo(root.<Date>get("endDate"), new Date()));
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("deleted"), false));
		}
		if (status != null && status.size()>0){
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.in(root.get("status")).value(status));
		}
		criteriaQuery.where(restrictions);
		return super.findList(criteriaQuery, null, null, null, null);
	}


}
