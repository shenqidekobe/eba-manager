package com.microBusiness.manage.dao.ass.impl;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.ass.AssCustomerRelationDao;
import com.microBusiness.manage.dao.impl.BaseDaoImpl;
import com.microBusiness.manage.dto.AssShareStatisticsDto;
import com.microBusiness.manage.dto.GoodsVisitDto;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.entity.ass.AssChildMember;
import com.microBusiness.manage.entity.ass.AssCustomerRelation;
import com.microBusiness.manage.entity.ass.AssCustomerRelation.ShareType;
import com.microBusiness.manage.entity.ass.AssCustomerRelation.SourceType;
import com.microBusiness.manage.entity.ass.AssGoods;
import com.microBusiness.manage.entity.ass.AssGoodsVisit;

import org.apache.commons.lang.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;

@Repository("assCustomerRelationDaoImpl")
public class AssCustomerRelationDaoImpl extends BaseDaoImpl<AssCustomerRelation, Long> implements AssCustomerRelationDao {

	@Override
	public Page<AssCustomerRelation> findPage(String searchName, Date startDate,
			Date endDate,Pageable pageable,
			Supplier supplier, AssCustomerRelation.SourceType sourceType, AssChildMember assChildMember) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<AssCustomerRelation> criteriaQuery = criteriaBuilder.createQuery(AssCustomerRelation.class);
		Root<AssCustomerRelation> root = criteriaQuery.from(AssCustomerRelation.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if(searchName != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.like(root.<String>get("clientName") , "%"+searchName+"%") , criteriaBuilder.like(root.<String>get("clientNum"), "%"+searchName+"%"), criteriaBuilder.like(root.<String>get("userName"), "%"+searchName+"%")));
		}
		if (startDate != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(root.<Date> get("createDate"), startDate));
		}
		if (endDate != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(root.<Date> get("createDate"), endDate));
		}
		if(supplier != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("supplier"), supplier));
		}
		if(sourceType != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("sourceType"), sourceType));
		}
		if(assChildMember != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("assChildMember"), assChildMember));
		}
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("deleted"), false));
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("shareType"), AssCustomerRelation.ShareType.noshare));
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery, pageable);
	}

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

	@Override
	public Page<AssCustomerRelation> findSupplierPage(String searchName, Date startDate, Date endDate, Pageable pageable, Supplier bySupplier) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<AssCustomerRelation> criteriaQuery = criteriaBuilder.createQuery(AssCustomerRelation.class);
		Root<AssCustomerRelation> root = criteriaQuery.from(AssCustomerRelation.class);
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
		if(null != bySupplier) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("bySupplier"), bySupplier));
		}
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery, pageable);
	}

	/**
	 * 供应商是否存在
	 */
	@Override
	public AssCustomerRelation inviteNameExists(String searchName , Supplier supplier) {
		if (StringUtils.isEmpty(searchName)) {
			return null;
		}
		try {
			String jpql = "select customerRelation from AssCustomerRelation customerRelation where customerRelation.clientName =:clientName and customerRelation.supplier=:supplier";
			AssCustomerRelation assCustomerRelation = entityManager.createQuery(jpql, AssCustomerRelation.class).setParameter("clientName", searchName).setParameter("supplier", supplier).getSingleResult();
			return assCustomerRelation;
		} catch (Exception e) {
			return null;
		}
	}
	/**
	 * 供应商是否存在
	 */
	@Override
	public AssCustomerRelation inviteNameExists(String searchName , String theme, AssChildMember assChildMember) {
		if (StringUtils.isEmpty(searchName)) {
			return null;
		}
		try {
			String jpql = "select customerRelation from AssCustomerRelation customerRelation where customerRelation.clientName =:clientName and customerRelation.assChildMember=:assChildMember and customerRelation.theme =:theme and customerRelation.deleted =:deleted and customerRelation.shareType =:shareType";
			List<AssCustomerRelation> assCustomerRelations = entityManager.createQuery(jpql, AssCustomerRelation.class).setParameter("clientName", searchName).setParameter("assChildMember", assChildMember).setParameter("deleted", false).setParameter("theme", theme).setParameter("shareType", AssCustomerRelation.ShareType.noshare).getResultList();
			if (assCustomerRelations.size() >0 ) {
				return assCustomerRelations.get(0);
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Page<AssCustomerRelation> findPage(Pageable pageable, Supplier supplier, SourceType sourceType, AssChildMember assChildMember) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<AssCustomerRelation> criteriaQuery = criteriaBuilder.createQuery(AssCustomerRelation.class);
		Root<AssCustomerRelation> root = criteriaQuery.from(AssCustomerRelation.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if(supplier != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("supplier"), supplier));
		}
		if(sourceType != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("sourceType"), sourceType));
		}
		if(assChildMember != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("assChildMember"), assChildMember));
		}
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("deleted"), false));
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery, pageable);
	}

	@Override
	public AssCustomerRelation findByCustomerRelation(Long id) {
		if (null == id) {
			return null;
		}
		try {
			String jpql = "select customerRelation from AssCustomerRelation customerRelation where customerRelation.id =:id and customerRelation.deleted=0";
			AssCustomerRelation assCustomerRelation = entityManager.createQuery(jpql, AssCustomerRelation.class).setParameter("id", id).getSingleResult();
			return assCustomerRelation;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public List<AssCustomerRelation> findList(SourceType sourceType,
			ShareType shareType, AssCustomerRelation source,
			AssChildMember assChildMember) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<AssCustomerRelation> criteriaQuery = criteriaBuilder.createQuery(AssCustomerRelation.class);
		Root<AssCustomerRelation> root = criteriaQuery.from(AssCustomerRelation.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if(shareType != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("shareType"), shareType));
		}
		if(sourceType != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("sourceType"), sourceType));
		}
		if(source != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("source"), source));
		}
		if(assChildMember != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("assChildMember"), assChildMember));
		}
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("deleted"), false));
		criteriaQuery.where(restrictions);
		return this.findList(criteriaQuery, null, null, null, null);
	}

	@Override
	public List<AssCustomerRelation> findCustomerRelationList(AssChildMember assChildMember , Long assCustomerRelationId) {
		if(null == assChildMember.getId() || null == assCustomerRelationId) {
			return null;
		}
		try {
			String jpql = "select customerRelation from AssCustomerRelation customerRelation where customerRelation.assChildMember =:assChildMember and customerRelation.deleted=0 and customerRelation.id !=:assCustomerRelationId and customerRelation.shareType =:shareType and customerRelation.sourceType=:sourceType";
			return entityManager.createQuery(jpql, AssCustomerRelation.class).setParameter("assChildMember", assChildMember).setParameter("assCustomerRelationId", assCustomerRelationId).setParameter("shareType", AssCustomerRelation.ShareType.noshare).setParameter("sourceType", AssCustomerRelation.SourceType.MOBILE).getResultList();
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public AssCustomerRelation findBySn(String sn) {
		if (null == sn) {
			return null;
		}
		try {
			String jpql = "select customerRelation from AssCustomerRelation customerRelation where customerRelation.sn =:sn and customerRelation.deleted=0";
			AssCustomerRelation assCustomerRelation = entityManager.createQuery(jpql, AssCustomerRelation.class).setParameter("sn", sn).getSingleResult();
			return assCustomerRelation;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public Page<AssCustomerRelation> findPageByShareType(AssChildMember assChildMember, ShareType shareType, Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<AssCustomerRelation> criteriaQuery = criteriaBuilder.createQuery(AssCustomerRelation.class);
		Root<AssCustomerRelation> root = criteriaQuery.from(AssCustomerRelation.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if(shareType != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("shareType"), shareType));
		}
		if(assChildMember != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("assChildMember"), assChildMember));
		}
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("deleted"), false));
		criteriaQuery.where(restrictions);
		criteriaQuery.orderBy(criteriaBuilder.desc(root.get("createDate")));
		return super.findPage(criteriaQuery, pageable);
	}

	@Override
	public BigInteger getShareAllVisit(AssChildMember assChildMember) {
		String findSql=" select count(id) from ass_customer_relation where share_type=0 and ass_child_member=:assChildMember ";
		Query query = entityManager.createNativeQuery(findSql.toString());
		query.setParameter("assChildMember", assChildMember.getId());
		return (BigInteger) query.getSingleResult();
	}

	@Override
	public List<AssCustomerRelation> findAll() {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<AssCustomerRelation> criteriaQuery = criteriaBuilder.createQuery(AssCustomerRelation.class);
		Root<AssCustomerRelation> root = criteriaQuery.from(AssCustomerRelation.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("deleted"), false));
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.isNull(root.get("type")));
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("shareType"), AssCustomerRelation.ShareType.share));
		criteriaQuery.where(restrictions);
		return super.findList(criteriaQuery, null, null, null, null);
	}

	@Override
	public Page<AssShareStatisticsDto> findSharePage(String orderBy,String searchName, Date startDate, Date endDate, Pageable pageable,
			Supplier supplier,AssCustomerRelation.ShareType shareType,AssCustomerRelation.SourceType sourceType) {
		StringBuffer findSql=new StringBuffer(); 
		findSql.append(" SELECT ");
		findSql.append(" assCustomerRelation.*, ");
		findSql.append(" (SELECT COUNT(page.id) from ass_page_visit page where page.ass_customer_relation=assCustomerRelation.id) pageVisit, ");
		findSql.append(" (SELECT COUNT(goods.id) from ass_goods_visit goods where goods.ass_customer_relation=assCustomerRelation.id) goodsVisit ");
		findSql.append(" FROM ");
		findSql.append(" ass_customer_relation assCustomerRelation ");
		findSql.append(" LEFT JOIN ass_good_directory goodDirectory ON assCustomerRelation.ass_good_directory=goodDirectory.id ");
		findSql.append(" LEFT JOIN t_supplier supplier on goodDirectory.supplier=supplier.id ");
		findSql.append(" WHERE 1=1 ");
		findSql.append(" and assCustomerRelation.deleted=0 ");
		
		if (supplier != null) {
			findSql.append(" and supplier.id=:supplier ");
		}
		if (shareType != null) {
			findSql.append(" AND assCustomerRelation.share_type=:shareType ");
		}
		if (sourceType != null) {
			findSql.append(" AND assCustomerRelation.source_type=:sourceType ");
		}
		if(null != startDate) {
			findSql.append(" and assCustomerRelation.create_date>=:startDate");
		}
		if(null != endDate) {
			findSql.append(" and assCustomerRelation.create_date<=:endDate");
		}
		if(StringUtils.isNotBlank(searchName)) {
			findSql.append(" and (assCustomerRelation.theme like :searchName or assCustomerRelation.admin_name like :searchName)");
		}
		if (StringUtils.isNotBlank(orderBy)) {
			if (StringUtils.equals("pageAsc",orderBy)) {
				findSql.append(" order by pageVisit asc ");
			}else if (StringUtils.equals("pageDesc",orderBy)) {
				findSql.append(" order by pageVisit desc ");
			}else if (StringUtils.equals("goodsAsc",orderBy)) {
				findSql.append(" order by goodsVisit asc ");
			}else if (StringUtils.equals("goodsDesc",orderBy)) {
				findSql.append(" order by goodsVisit desc ");
			}
		}else {
			findSql.append(" order by create_date desc ");
		}
		
		StringBuffer countSql=new StringBuffer(" select COUNT(datas.id) from ( ");
		countSql.append(findSql);
		countSql.append(" ) datas ");
		
		Query query = entityManager.createNativeQuery(findSql.toString());
		Query countQuery = entityManager.createNativeQuery(countSql.toString());
		
		if (supplier != null) {
			query.setParameter("supplier", supplier.getId());
			countQuery.setParameter("supplier", supplier.getId());
		}
		if (shareType != null) {
			query.setParameter("shareType", shareType.ordinal());
			countQuery.setParameter("shareType",shareType.ordinal());
		}
		if (sourceType != null) {
			query.setParameter("sourceType", sourceType.ordinal());
			countQuery.setParameter("sourceType", sourceType.ordinal());
		}
		if(null != startDate) {
			query.setParameter("startDate" , startDate);
			countQuery.setParameter("startDate", startDate);
		}
		if(null != endDate) {
			query.setParameter("endDate" , endDate);
			countQuery.setParameter("endDate", endDate);
		}
		if(StringUtils.isNotBlank(searchName)) {
			query.setParameter("searchName" , "%"+searchName+"%") ;
			countQuery.setParameter("searchName", "%"+searchName+"%");
		}
		
		query.unwrap(SQLQuery.class).addEntity(AssCustomerRelation.class).addScalar("goodsVisit",LongType.INSTANCE).addScalar("pageVisit",LongType.INSTANCE).setResultTransformer(Transformers.aliasToBean(AssShareStatisticsDto.class));
		
		BigInteger tempTotal = (BigInteger) countQuery.getSingleResult();
		int total = tempTotal.intValue();
		
		int totalPages = (int) Math.ceil((double) total / (double) pageable.getPageSize());
		if (totalPages < pageable.getPageNumber()) {
			pageable.setPageNumber(totalPages);
		}
		
		query.setFirstResult((pageable.getPageNumber() - 1) * pageable.getPageSize());
		query.setMaxResults(pageable.getPageSize());
		
		List<AssShareStatisticsDto> list=query.getResultList();
		 
		return new Page<AssShareStatisticsDto>(list, total, pageable);
	}

	@Override
	public AssCustomerRelation inviteNameExists(String clientName,String theme, SourceType sourceType , AssChildMember assChildMember) {
		if (StringUtils.isEmpty(clientName) || StringUtils.isEmpty(theme) || null == sourceType) {
			return null;
		}
		try {
			StringBuffer sql = new StringBuffer("select * from ass_customer_relation customerRelation where 1=1");
			sql.append(" and customerRelation.client_name=:clientName");
			sql.append(" and customerRelation.theme=:theme");
			sql.append(" and customerRelation.source_type=:sourceType");
			sql.append(" and customerRelation.ass_child_member=:assChildMember");
			sql.append(" and customerRelation.deleted = 0");
			sql.append(" and customerRelation.share_type = 1");
			Query query = entityManager.createNativeQuery(sql.toString(), AssCustomerRelation.class);
			query.setParameter("clientName", clientName);
			query.setParameter("theme", theme);
			query.setParameter("sourceType", sourceType.ordinal());
			query.setParameter("assChildMember", assChildMember);
			return (AssCustomerRelation) query.getSingleResult();
		} catch (Exception e) {
			return null;
		}
		
	}
}
