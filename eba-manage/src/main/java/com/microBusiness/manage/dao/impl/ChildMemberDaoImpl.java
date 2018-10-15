package com.microBusiness.manage.dao.impl;

import com.microBusiness.manage.dao.ChildMemberDao;
import com.microBusiness.manage.entity.ChildMember;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;

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
