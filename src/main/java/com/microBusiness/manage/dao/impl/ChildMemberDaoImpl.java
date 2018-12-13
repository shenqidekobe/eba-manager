package com.microBusiness.manage.dao.impl;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.ChildMemberDao;
import com.microBusiness.manage.entity.ChildMember;
import com.microBusiness.manage.entity.Withdraw;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.Date;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * Created by mingbai on 2017/2/11.
 * 功能描述：
 * 修改记录：
 */
@Repository
public class ChildMemberDaoImpl extends BaseDaoImpl<ChildMember , Long> implements ChildMemberDao {
    @Override
    public ChildMember findByOpenId(String openId) {
        if (StringUtils.isEmpty(openId)) {
            return null;
        }
        try {
            String jpql = "select childMember from ChildMember childMember where lower(childMember.openId) = lower(:openId)";
            return entityManager.createQuery(jpql, ChildMember.class).setParameter("openId", openId).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
    
    @Override
    public ChildMember findBySmOpenId(String smOpenId) {
        if (StringUtils.isEmpty(smOpenId)) {
            return null;
        }
        try {
            String jpql = "select childMember from ChildMember childMember where lower(childMember.smOpenId) = lower(:smOpenId)";
            return entityManager.createQuery(jpql, ChildMember.class).setParameter("smOpenId", smOpenId).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
    
    @Override
    public  Page<ChildMember> findPage(String nickName,String smOpenId,ChildMember.SourceType type,Boolean isShoper,
 			ChildMember parent,Date startDate,Date endDate,Pageable pageable){
    	CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<ChildMember> criteriaQuery = criteriaBuilder.createQuery(ChildMember.class);
		Root<ChildMember> root = criteriaQuery.from(ChildMember.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (StringUtils.isNotEmpty(nickName)) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("nickName"), nickName));
		}
		if (StringUtils.isNotEmpty(smOpenId)) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("smOpenId"), smOpenId));
		}
		if (parent != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("parent"), parent));
		}
		if (type != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("sourceType"), type));
		}
		if (isShoper != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isShoper"), isShoper));
		}
		if (startDate != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(root.<Date> get("createDate"), startDate));
		}
		if (endDate != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(root.<Date> get("createDate"), endDate));
		}
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery, pageable);
    }

    @Override
    public ChildMember findByUnionId(String unionId) {

        if (StringUtils.isEmpty(unionId)) {
            return null;
        }
        try {

            String jpql = "select childMember from ChildMember childMember where lower(childMember.unionId) = lower(:unionId)";
            return entityManager.createQuery(jpql, ChildMember.class).setParameter("unionId", unionId).getSingleResult();

        } catch (NoResultException e) {

            return null;
        }

    }
}
