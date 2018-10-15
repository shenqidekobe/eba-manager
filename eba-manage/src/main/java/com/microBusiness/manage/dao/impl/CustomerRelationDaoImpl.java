package com.microBusiness.manage.dao.impl;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.CustomerRelationDao;
import com.microBusiness.manage.entity.Admin;
import com.microBusiness.manage.entity.CustomerRelation;
import com.microBusiness.manage.entity.Goods;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.Order;
import com.microBusiness.manage.entity.Supplier.Status;
import com.microBusiness.manage.entity.SupplierSupplier;
import com.microBusiness.manage.entity.CustomerRelation.ClientType;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.entity.ass.AssChildMember;
import com.microBusiness.manage.entity.ass.AssCustomerRelation;
import com.microBusiness.manage.entity.ass.AssGoodDirectory;
import com.microBusiness.manage.entity.ass.AssGoods;

@Repository("customerRelationDaoImpl")
public class CustomerRelationDaoImpl extends BaseDaoImpl<CustomerRelation, Long> implements CustomerRelationDao {

	/**
	 * 客户管理列表
	 */
	@Override
	public Page<CustomerRelation> findPage(String searchName, Date startDate,
			Date endDate, CustomerRelation.ClientType clientType , Pageable pageable,
			Supplier supplier) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<CustomerRelation> criteriaQuery = criteriaBuilder.createQuery(CustomerRelation.class);
		Root<CustomerRelation> root = criteriaQuery.from(CustomerRelation.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if(StringUtils.isNotEmpty(searchName)) {
			Join<CustomerRelation , Admin> adminJoin = root.join("admin" , JoinType.LEFT);
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.like(adminJoin.<String>get("name") , "%"+searchName+"%") ,criteriaBuilder.like(root.<String>get("clientName") , "%"+searchName+"%") , criteriaBuilder.like(root.<String>get("clientNum"), "%"+searchName+"%"), criteriaBuilder.like(root.<String>get("userName"), "%"+searchName+"%")));
		}
		if (startDate != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(root.<Date> get("createDate"), startDate));
		}
		if (endDate != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(root.<Date> get("createDate"), endDate));
		}
		if(clientType != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("clientType"), clientType));
		}
		if(supplier != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("supplier"), supplier));
		}
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery, pageable);
	}

	/**
	 * 邀请码是否存在
	 */
	@Override
	public boolean inviteCodeExists(String inviteCode , Supplier supplier) {
		if (StringUtils.isEmpty(inviteCode)) {
			return false;
		}
		String jpql = "select count(*) from CustomerRelation customerRelation where lower(customerRelation.inviteCode) = lower(:inviteCode) and customerRelation.supplier=:supplier";
		Long count = entityManager.createQuery(jpql, Long.class).setParameter("inviteCode", inviteCode).setParameter("supplier", supplier).getSingleResult();
		return count > 0;
	}

	/**
	 * 查询正式供应有客户关系的企业
	 */
	@Override
	public List<Supplier> findByOfficialCustomers(Supplier supplier , Supplier.Status status) {
		StringBuffer findSql = new StringBuffer("select supplier.* from t_customer_relation relation inner join t_supplier supplier on relation.by_supplier = supplier.id");
		findSql.append(" where 1=1");
		if(null != supplier) {
			findSql.append(" and relation.supplier=:supplier");
		}
		if(null != status) {
			findSql.append(" and supplier.status=:status");
		}
		findSql.append(" ORDER BY CONVERT(supplier.name using gbk) collate gbk_chinese_ci ASC ");
		//.append("where supplier.status=:status")
		//.append(" and relation.supplier=:supplier");
		Query query = entityManager.createNativeQuery(findSql.toString() , Supplier.class) ;
		if(null != supplier) {
			query.setParameter("supplier" , supplier.getId());
		}
		if(null != status) {
			query.setParameter("status" , status.ordinal());
		}
		List<Supplier> lists = query.getResultList();
		return lists;
	}


	/**
	 * 供应商关系 列表
	 *
	 * @param searchName
	 * @param startDate
	 * @param endDate
	 * @param clientType
	 * @param pageable
	 * @param bySupplier
	 * @return
	 */
	@Override
	public Page<CustomerRelation> findSupplierPage(String searchName, Date startDate, Date endDate, ClientType clientType, Pageable pageable, Supplier bySupplier) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<CustomerRelation> criteriaQuery = criteriaBuilder.createQuery(CustomerRelation.class);
		Root<CustomerRelation> root = criteriaQuery.from(CustomerRelation.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if(searchName != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.like(root.<String>get("supplierName") , "%"+searchName+"%") , criteriaBuilder.like(root.<String>get("supplierCode"), "%"+searchName+"%"), criteriaBuilder.like(root.<String>get("supplierUserName"), "%"+searchName+"%")));
		}
		if (startDate != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(root.<Date> get("createDate"), startDate));
		}
		if (endDate != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(root.<Date> get("createDate"), endDate));
		}
		if(clientType != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("clientType"), clientType));
		}
		if(null != bySupplier) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("bySupplier"), bySupplier));
		}
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery, pageable);
	}

	/**
	 * 查询临时供应有客户关系的企业
	 */
	@Override
	public List<Supplier> findByTemporarySupply(Supplier supplier, Status status) {
		StringBuffer findSql = new StringBuffer("select supplier.* from t_customer_relation relation inner join t_supplier supplier on relation.by_supplier = supplier.id ")
		.append("where supplier.status !=:status")
		.append(" and relation.supplier=:supplier");
		Query query = entityManager.createNativeQuery(findSql.toString() , Supplier.class) ;
		query.setParameter("status" , status.ordinal()) ;
		query.setParameter("supplier" , supplier.getId()) ;
		List<Supplier> lists = query.getResultList();
		return lists;
	}

	/**
	 * 
	 * 查询没有供应关系的企业(正式供应和临时供应)
	 */
	@Override
	public List<CustomerRelation> findByNoSupplyRelationship(Long id) {
		StringBuffer findSql = new StringBuffer("SELECT relation.*");
		findSql.append(" FROM t_customer_relation relation INNER JOIN t_supplier_supplier supplierSupplier");
		findSql.append(" ON relation.supplier = supplierSupplier.supplier AND relation.by_supplier = supplierSupplier.by_supplier");
		findSql.append(" WHERE (supplierSupplier.status=:start1 OR supplierSupplier.status=:start2 OR supplierSupplier.status=:start3)");
		findSql.append(" AND relation.id=:id");
		findSql.append(" UNION");
		findSql.append(" SELECT relation.*");
		findSql.append(" FROM t_customer_relation relation INNER JOIN (SELECT supplyNeed.supplier,supplier.id AS by_supplier");
		findSql.append(" FROM t_supply_need supplyNeed INNER JOIN t_need need ON supplyNeed.need = need.id");
		findSql.append(" INNER JOIN t_supplier supplier ON supplier.id = need.supplier) supplierSupplier");
		findSql.append(" ON relation.supplier = supplierSupplier.supplier AND relation.by_supplier = supplierSupplier.by_supplier");
		findSql.append(" WHERE relation.id=:id");
		Query query = entityManager.createNativeQuery(findSql.toString() , CustomerRelation.class) ;
		query.setParameter("id" , id) ;
		query.setParameter("start1" , SupplierSupplier.Status.inTheSupply.ordinal()) ;
		query.setParameter("start2" , SupplierSupplier.Status.suspendSupply.ordinal()) ;
		query.setParameter("start3" , SupplierSupplier.Status.willSupply.ordinal()) ;
		query.setParameter("id" , id) ;
		List<CustomerRelation> lists = query.getResultList();
		return lists;
	}

	@Override
	public Page<CustomerRelation> findPage(AssChildMember assChildMember,
			Pageable pageable) {
		try {
			StringBuffer buffer = new StringBuffer("select customerRelation.* from t_customer_relation customerRelation");
			buffer.append(" INNER JOIN xx_admin admin on customerRelation.admin = admin.id");
			buffer.append(" INNER JOIN ass_child_member assChildMember on admin.id = assChildMember.admin where 1=1");
			if(null != assChildMember) {
				buffer.append(" and assChildMember.id = :id");
			}
			buffer.append(" ORDER BY customerRelation.create_date DESC");
			StringBuffer countSql = new StringBuffer("select count(*) from t_customer_relation customerRelation");
			countSql.append(" INNER JOIN xx_admin admin on customerRelation.admin = admin.id");
			countSql.append(" INNER JOIN ass_child_member assChildMember on admin.id = assChildMember.admin where 1=1");
			if(null != assChildMember) {
				countSql.append(" and assChildMember.id = :id");
			}
			Query query = entityManager.createNativeQuery(buffer.toString(), CustomerRelation.class);
			Query countQuery = entityManager.createNativeQuery(countSql.toString());
			
			if(null != assChildMember) {
				query.setParameter("id", assChildMember.getId());
			}
			if(null != assChildMember) {
				countQuery.setParameter("id", assChildMember.getId());
			}
			
			BigInteger tempTotal = (BigInteger) countQuery.getSingleResult();
			int total = tempTotal.intValue();
			
			int totalPages = (int) Math.ceil((double) total / (double) pageable.getPageSize());
			if (totalPages < pageable.getPageNumber()) {
				pageable.setPageNumber(totalPages);
			}
			
			query.setFirstResult((pageable.getPageNumber() - 1) * pageable.getPageSize());
			query.setMaxResults(pageable.getPageSize());
			
			List<CustomerRelation> list = query.getResultList();
			
			return new Page<CustomerRelation>(list, total, pageable);
		} catch (Exception e) {
			return null;
		}
		
	}

	@Override
	public List<CustomerRelation> findList(AssChildMember assChildMember) {
		if(null == assChildMember.getId()) {
			return null;
		}
		try {
			StringBuffer buffer = new StringBuffer("select customerRelation.* from t_customer_relation customerRelation");
			buffer.append(" INNER JOIN xx_admin admin on customerRelation.admin = admin.id ");
			buffer.append(" INNER JOIN ass_child_member childMember on admin.id = childMember.admin where 1=1");
			buffer.append(" and childMember.id = :id");
			buffer.append(" and customerRelation.deleted = 0");
			Query query = entityManager.createNativeQuery(buffer.toString() , CustomerRelation.class);
			query.setParameter("id",assChildMember.getId());
			return query.getResultList();
		} catch (Exception e) {
			return null;
		}
	}
}
