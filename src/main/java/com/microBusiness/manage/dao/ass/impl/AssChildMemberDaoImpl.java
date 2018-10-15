package com.microBusiness.manage.dao.ass.impl;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.microBusiness.manage.dao.ass.AssChildMemberDao;
import com.microBusiness.manage.dao.impl.BaseDaoImpl;
import com.microBusiness.manage.entity.Admin;
import com.microBusiness.manage.entity.CustomerRelation;
import com.microBusiness.manage.entity.Need;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.entity.ass.AssChildMember;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

/**
 * 功能描述：
 * 修改记录：
 */
@Repository
public class AssChildMemberDaoImpl extends BaseDaoImpl<AssChildMember , Long> implements AssChildMemberDao {
    @Override
    public AssChildMember findByOpenId(String openId) {
        if (StringUtils.isEmpty(openId)) {
            return null;
        }
        try {
            String jpql = "select childMember from AssChildMember childMember where lower(childMember.openId) = lower(:openId)";
            return entityManager.createQuery(jpql, AssChildMember.class).setParameter("openId", openId).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public AssChildMember findByUnionId(String unionId) {

        if (StringUtils.isEmpty(unionId)) {
            return null;
        }
        try {

            String jpql = "select childMember from AssChildMember childMember where lower(childMember.unionId) = lower(:unionId)";
            return entityManager.createQuery(jpql, AssChildMember.class).setParameter("unionId", unionId).getSingleResult();

        } catch (NoResultException e) {

            return null;
        }

    }

	@Override
	public List<AssChildMember> findList(Supplier supplier) {
		if(null == supplier.getId()) {
			return null;
		}
		try {
			StringBuffer buffer = new StringBuffer("select DISTINCT childMember.* from ass_child_member childMember");
			buffer.append(" INNER JOIN xx_admin admin on childMember.admin = admin.id");
			buffer.append(" INNER JOIN t_supplier supplier on admin.supplier = supplier.id where 1=1");
			buffer.append(" and supplier.id=:supplier and childMember.deleted = 0");
			Query query = entityManager.createNativeQuery(buffer.toString(), AssChildMember.class);
			query.setParameter("supplier", supplier.getId());
			return query.getResultList();
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public List<AssChildMember> findList(Need need) {
		if(null == need.getId()) {
			return null;
		}
		try {
			StringBuffer buffer = new StringBuffer("select DISTINCT childMember.* from ass_child_member childMember");
			buffer.append(" INNER JOIN xx_admin admin on childMember.admin = admin.id");
			buffer.append(" INNER JOIN t_need need on admin.id = need.admin where 1=1");
			buffer.append(" and need.id = :id and childMember.deleted = 0");
			Query query = entityManager.createNativeQuery(buffer.toString(), AssChildMember.class);
			query.setParameter("id", need.getId());
			return query.getResultList();
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public AssChildMember find(Admin admin) {
		if(null == admin || null == admin.getId()) {
			return null;
		}
		try {
			String str = "select * from ass_child_member childMember where childMember.admin = :admin";
			Query query = entityManager.createNativeQuery(str, AssChildMember.class);
			query.setParameter("admin", admin);
			return (AssChildMember) query.getSingleResult();
		} catch (Exception e) {
			return null;
		}
		
	}

	@Override
	public List<AssChildMember> findList(CustomerRelation customerRelation) {
		if(null == customerRelation) {
			return null;
		}
		try {
			StringBuffer buffer = new StringBuffer("select DISTINCT childMember.* from ass_child_member childMember");
			buffer.append(" INNER JOIN xx_admin admin on childMember.admin = admin.id");
			buffer.append(" INNER JOIN t_customer_relation customerRelation on admin.id = customerRelation.admin where 1=1");
			buffer.append(" and customerRelation.id = :customerRelation and childMember.deleted = 0");
			Query query = entityManager.createNativeQuery(buffer.toString(), AssChildMember.class);
			query.setParameter("customerRelation", customerRelation.getId());
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
}
