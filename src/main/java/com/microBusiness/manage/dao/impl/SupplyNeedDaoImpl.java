package com.microBusiness.manage.dao.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

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

import com.microBusiness.manage.entity.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.SupplyNeedDao;
import com.microBusiness.manage.entity.SupplyNeed.Status;

/**
 * Created by mingbai on 2017/1/22.
 * 功能描述：
 * 修改记录：
 */
@Repository
public class SupplyNeedDaoImpl extends BaseDaoImpl<SupplyNeed, Long> implements SupplyNeedDao {
    @Override
    public Page<SupplyNeed> findPage(Pageable pageable, SupplyNeed supplyNeed, Date startDate, Date endDate, String searchName) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<SupplyNeed> criteriaQuery = criteriaBuilder.createQuery(SupplyNeed.class);
        Root<SupplyNeed> root = criteriaQuery.from(SupplyNeed.class);

        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();

        if(null != supplyNeed.getSupplier()){
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("supplier") , supplyNeed.getSupplier()));
        }
        if(supplyNeed.getStatus() != null) {
        	restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("status") , supplyNeed.getStatus()));
        }
        if(StringUtils.isNotEmpty(searchName)) {
        	Join<SupplyNeed , Need> needJoin = root.join("need" , JoinType.LEFT);
        	Join<Need , Supplier> supplierJoin = needJoin.join("supplier" , JoinType.LEFT);
        	restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.like(needJoin.<String>get("name") , "%"+searchName+"%") , criteriaBuilder.like(supplierJoin.<String>get("name"), "%"+searchName+"%")));
        }
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("deleted") , false));
        criteriaQuery.where(restrictions);
        return super.findPage(criteriaQuery, pageable);
    }

	@Override
	public SupplyNeed findSupplyNeed(SupplyNeed supplyNeed) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<SupplyNeed> criteriaQuery = criteriaBuilder.createQuery(SupplyNeed.class);
        Root<SupplyNeed> root = criteriaQuery.from(SupplyNeed.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("supplier") , supplyNeed.getSupplier()));
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("need") , supplyNeed.getNeed()));
        if(supplyNeed.getStatus() != null){
        	restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("status") , supplyNeed.getStatus()));
        }
        criteriaQuery.where(restrictions);
        List<SupplyNeed> list = super.findList(criteriaQuery, 0, 1, null, null);
        if(list != null && !list.isEmpty()){
        	return list.get(0);
        }
        return null;
	}

	@Override
	public List<SupplyNeed> findByDateList(Supplier supplier, Need need) {
		String sql="select supplyNeed from SupplyNeed supplyNeed where supplier=:supplier and need=:need and deleted=:deleted";
		TypedQuery<SupplyNeed> query = entityManager.createQuery(sql, SupplyNeed.class).setParameter("supplier", supplier).setParameter("need", need).setParameter("deleted" , false);
		return query.getResultList();
	}
	@Override
	public List<SupplyNeed> findByDateList(Supplier supplier, Long[] needIds) {
		String sql="select supplyNeed.* from t_supply_need supplyNeed where supplyNeed.supplier=:supplier and supplyNeed.need in(:needIds) and supplyNeed.deleted=0";
		Query query = entityManager.createNativeQuery(sql, SupplyNeed.class).setParameter("supplier", supplier.getId()).setParameter("needIds", Arrays.asList(needIds));
		return query.getResultList();
	}

	@Override
	public void updateExpiredSupply(Date compareDate, SupplyNeed.Status status) {
		String jpql = "update SupplyNeed supplyNeed set supplyNeed.status=:status where supplyNeed.endDate<:compareDate " ;
		entityManager.createQuery(jpql).setParameter("status" , SupplyNeed.Status.EXPIRED).setParameter("compareDate" , compareDate).executeUpdate() ;
	}

	@Override
	public void dealWillSupplyToSupply(Date compareDate) {
		String jpql = "update SupplyNeed supplyNeed set supplyNeed.status=:status where (supplyNeed.startDate<=:compareDate and :compareDate <= supplyNeed.endDate) and supplyNeed.status=:upStatus " ;
		entityManager.createQuery(jpql).setParameter("status" , SupplyNeed.Status.SUPPLY).setParameter("upStatus" , SupplyNeed.Status.WILLSUPPLY).setParameter("compareDate" , compareDate).executeUpdate() ;
	}

	@Override
	public List<SupplyNeed> findByNeedSupplier(Long needId, Supplier supplier) {
		 try {
             String jpql = "select supplyNeed from SupplyNeed supplyNeed where lower(supplyNeed.need) = lower(:need) and lower(supplyNeed.supplier) = lower(:supplier)";
             return entityManager.createQuery(jpql, SupplyNeed.class).setParameter("need", needId).setParameter("supplier", supplier).getResultList();
         } catch (NoResultException e) {
             return null;
         }
	}

    @Override
    public List<SupplyNeed> findBySupplier(Supplier supplier, List<Status> status ,  SupplyNeed.AssignedModel assignedModel) {
        String jpql = "select supplyNeed from SupplyNeed supplyNeed where supplyNeed.supplier = :supplier and supplyNeed.status in (:status) and supplyNeed.assignedModel = :assignedModel";
        TypedQuery query = entityManager.createQuery(jpql, SupplyNeed.class) ;

        query.setParameter("supplier" , supplier) ;
        query.setParameter("status" , status);
        query.setParameter("assignedModel" , assignedModel);

        return query.getResultList() ;

    }
    
    public SupplyNeed findSupplyNeedOnSupply(SupplyNeed supplyNeed) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<SupplyNeed> criteriaQuery = criteriaBuilder.createQuery(SupplyNeed.class);
        Root<SupplyNeed> root = criteriaQuery.from(SupplyNeed.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("supplier") , supplyNeed.getSupplier()));
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("need") , supplyNeed.getNeed()));
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("status") , SupplyNeed.Status.SUPPLY));
        criteriaQuery.where(restrictions);
        List<SupplyNeed> list = super.findList(criteriaQuery, 0, 1, null, null);
        if(list != null && !list.isEmpty()){
        	return list.get(0);
        }
        return null;
	}

    @Override
    public List<SupplyNeed> findByMember(Member member) {
        String sql="select supplyNeed.* from t_supply_need supplyNeed left join t_need need on supplyNeed.need=need.id where need.member=:member and need.shop_type=:shopType and supplyNeed.status =:status";
        Query query = entityManager.createNativeQuery(sql, SupplyNeed.class).setParameter("member", member.getId()).setParameter("shopType", ShopType.affiliate.ordinal()).setParameter("status", Status.SUPPLY.ordinal());
        return  query.getResultList();
    }

    @Override
    public List<SupplyNeed> findByNeeds(List<Need> needs) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<SupplyNeed> criteriaQuery = criteriaBuilder.createQuery(SupplyNeed.class);
        Root<SupplyNeed> root = criteriaQuery.from(SupplyNeed.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        if (needs != null && needs.size() > 0){
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.in(root.get("need")).value(needs));
        }
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("status") , SupplyNeed.Status.SUPPLY));
        criteriaQuery.where(restrictions);
        return super.findList(criteriaQuery, null, null, null, null);
    }
}
