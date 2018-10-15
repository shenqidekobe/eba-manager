package com.microBusiness.manage.dao.impl;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.microBusiness.manage.dao.SupplierAssignRelationDao;
import com.microBusiness.manage.entity.Need;
import com.microBusiness.manage.entity.SupplierAssignRelation;
import com.microBusiness.manage.entity.SupplierSupplier;

@Repository("supplierAssignRelationDaoImpl")
public class SupplierAssignRelationDaoImpl extends BaseDaoImpl<SupplierAssignRelation, Long> implements SupplierAssignRelationDao{

	@Override
	public List<SupplierAssignRelation> findListBySupplier(SupplierSupplier supplierSupplier,Need need) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<SupplierAssignRelation> criteriaQuery = criteriaBuilder.createQuery(SupplierAssignRelation.class);
		Root<SupplierAssignRelation> root = criteriaQuery.from(SupplierAssignRelation.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (supplierSupplier != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("supplyRelation"), supplierSupplier));
		}
		if (need != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("need"), need));
		}
		criteriaQuery.where(restrictions);
		return super.findList(criteriaQuery, null, null, null, null);
	}

}
