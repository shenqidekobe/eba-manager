package com.microBusiness.manage.dao.ass.impl;

import java.util.List;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.ass.AssGoodsDao;
import com.microBusiness.manage.dao.impl.BaseDaoImpl;
import com.microBusiness.manage.entity.Goods;
import com.microBusiness.manage.entity.ass.AssChildMember;
import com.microBusiness.manage.entity.ass.AssCustomerRelation;
import com.microBusiness.manage.entity.ass.AssGoods;


@Repository("assistantGoodsDaoImpl")
public class AssGoodsDaoImpl extends BaseDaoImpl<AssGoods, Long> implements AssGoodsDao {

	@Override
	public boolean snExists(String sn) {
		if (StringUtils.isEmpty(sn)) {
			return false;
		}

		String jpql = "select count(*) from AssGoods assGoods where lower(assGoods.sn) = lower(:sn)";
		Long count = entityManager.createQuery(jpql, Long.class).setParameter("sn", sn).getSingleResult();
		return count > 0;
	}

	@Override
	public Page<AssGoods> findPage(AssChildMember assChildMember,
			Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<AssGoods> criteriaQuery = criteriaBuilder.createQuery(AssGoods.class);
		Root<AssGoods> root = criteriaQuery.from(AssGoods.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if(null != assChildMember) {
			Join<AssGoods , AssCustomerRelation> assCustomerRelationJoin = root.join("assCustomerRelation" , JoinType.LEFT);
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(assCustomerRelationJoin.get("assChildMember"), assChildMember));
		}
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("deleted"), false));
		criteriaQuery.where(restrictions);
		criteriaQuery.orderBy(criteriaBuilder.desc(root.get("createDate")));
		return super.findPage(criteriaQuery, pageable);
	}

	@Override
	public Page<AssGoods> findByList(AssCustomerRelation assCustomerRelation , String name , Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<AssGoods> criteriaQuery = criteriaBuilder.createQuery(AssGoods.class);
		Root<AssGoods> root = criteriaQuery.from(AssGoods.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if(null != assCustomerRelation) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("assCustomerRelation"), assCustomerRelation));
		}
		if(null != name) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.like(root.<String>get("name"), "%"+name+"%"));
		}
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("deleted"), false));
		criteriaQuery.where(restrictions);
		criteriaQuery.orderBy(criteriaBuilder.desc(root.get("createDate")));
		return super.findPage(criteriaQuery, pageable);
	}

	@Override
	public boolean findNameExist(AssCustomerRelation assCustomerRelation,AssGoods assGoods) {
		if (StringUtils.isEmpty(assGoods.getName())) {
			return false;
		}
		StringBuffer jpql = new StringBuffer("select count(*) from AssGoods assGoods where assGoods.assCustomerRelation=:assCustomerRelation and assGoods.name=:name and assGoods.deleted=0");
		if(null != assGoods.getId()) {
			jpql.append(" and assGoods.id != :id");
		}
		Query query = entityManager.createQuery(jpql.toString(), Long.class);
		query.setParameter("assCustomerRelation", assCustomerRelation).setParameter("name", assGoods.getName());
		if(null != assGoods.getId()) {
			query.setParameter("id", assGoods.getId());
		}
		Long count = (Long) query.getSingleResult();
		return count > 0;
	}

	@Override
	public List<AssGoods> findByList(AssCustomerRelation assCustomerRelation) {
		if(assCustomerRelation.getId() == null) {
			return null;
		}
		String jpql = "select assGoods from AssGoods assGoods where assGoods.assCustomerRelation=:assCustomerRelation and assGoods.deleted=0";
		List<AssGoods> assGoods = entityManager.createQuery(jpql, AssGoods.class).setParameter("assCustomerRelation", assCustomerRelation).getResultList();
		return assGoods;
	}

	@Override
	public AssGoods findNameExist(AssCustomerRelation assCustomerRelation,String name) {
		if (StringUtils.isEmpty(name)) {
			return null;
		}
		StringBuffer jpql = new StringBuffer("select assGoods from AssGoods assGoods where assGoods.assCustomerRelation=:assCustomerRelation and assGoods.name=:name and assGoods.deleted=0");
		Query query = entityManager.createQuery(jpql.toString(), AssGoods.class);
		query.setParameter("assCustomerRelation", assCustomerRelation).setParameter("name", name);
		List<AssGoods> assGoods=query.getResultList();
		if (assGoods.size()>0) {
			return assGoods.get(0);
		}
		return null;
	}

	@Override
	public AssGoods findBySn(String sn) {
		StringBuffer jpql = new StringBuffer("select assGoods from AssGoods assGoods where assGoods.sn=:sn and assGoods.deleted=0");
		Query query = entityManager.createQuery(jpql.toString(), AssGoods.class);
		query.setParameter("sn", sn);
		List<AssGoods> assGoods=query.getResultList();
		if (assGoods.size()>0) {
			return assGoods.get(0);
		}
		return null;
	}

	@Override
	public AssGoods findBySource(AssGoods assGoods, AssChildMember assChildMember) {
		StringBuffer jpql = new StringBuffer("select assGoods from AssGoods assGoods left join assGoods.assCustomerRelation relation where assGoods.source=:source and relation.assChildMember=:assChildMember and relation.deleted=0 and assGoods.deleted=0");
		Query query = entityManager.createQuery(jpql.toString(), AssGoods.class);
		query.setParameter("source", assGoods);
		query.setParameter("assChildMember", assChildMember);
		List<AssGoods> assGoodsList=query.getResultList();
		if (assGoodsList.size()>0) {
			return assGoodsList.get(0);
		}
		return null;
	}

	@Override
	public AssGoods find(Goods goods, AssCustomerRelation assCustomerRelation) {
		try {
			StringBuffer jpql = new StringBuffer("select assGoods from AssGoods assGoods where assGoods.assCustomerRelation=:assCustomerRelation and assGoods.goods=:goods and assGoods.deleted=0");
			Query query = entityManager.createQuery(jpql.toString(), AssGoods.class);
			query.setParameter("assCustomerRelation", assCustomerRelation);
			query.setParameter("goods", goods);
			return (AssGoods) query.getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public List<Goods> find(AssCustomerRelation assCustomerRelation) {
		if(null == assCustomerRelation.getId()) {
			return null;
		}
		try {
			StringBuffer buffer = new StringBuffer("select goods.* from ass_goods assGoods");
			buffer.append(" left JOIN xx_goods goods on assGoods.goods = goods.id where 1=1");
			buffer.append(" and assGoods.ass_customer_relation=:assCustomerRelation");
			buffer.append(" and assGoods.deleted=0");
			Query query = entityManager.createNativeQuery(buffer.toString(), Goods.class);
			query.setParameter("assCustomerRelation", assCustomerRelation);
			List<Goods> list = query.getResultList();
			return query.getResultList();
		} catch (Exception e) {
			return null;
		}
		
	}

}
