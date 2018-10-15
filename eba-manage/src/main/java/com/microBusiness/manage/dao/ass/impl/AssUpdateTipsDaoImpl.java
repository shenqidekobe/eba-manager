package com.microBusiness.manage.dao.ass.impl;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.microBusiness.manage.dao.ass.AssUpdateTipsDao;
import com.microBusiness.manage.dao.impl.BaseDaoImpl;
import com.microBusiness.manage.entity.CustomerRelation;
import com.microBusiness.manage.entity.Need;
import com.microBusiness.manage.entity.ass.AssChildMember;
import com.microBusiness.manage.entity.ass.AssGoodDirectory;
import com.microBusiness.manage.entity.ass.AssUpdateTips;
import com.microBusiness.manage.entity.ass.AssUpdateTips.Type;
import com.microBusiness.manage.entity.ass.AssUpdateTips.WhetherUpdate;

@Repository("assUpdateTipsDaoImpl")
public class AssUpdateTipsDaoImpl extends BaseDaoImpl<AssUpdateTips, Long> implements AssUpdateTipsDao {

	@Override
	public List<AssUpdateTips> findList(AssChildMember assChildMember,
			AssGoodDirectory assGoodDirectory, WhetherUpdate whetherUpdate, AssUpdateTips.Type type) {
		try {
			StringBuffer buffer = new StringBuffer("select * from ass_update_tips updateTips where 1=1");
			if(null != assChildMember) {
				buffer.append(" and updateTips.ass_child_member=:assChildMember");
			}
			if(null != assGoodDirectory) {
				buffer.append(" and updateTips.ass_good_directory=:assGoodDirectory");
			}
			if(null != whetherUpdate) {
				buffer.append(" and updateTips.whether_update=:whetherUpdate");
			}
			if(null != type) {
				buffer.append(" and updateTips.type = :type");
			}
			buffer.append(" and updateTips.deleted = 0");
			Query query = entityManager.createNativeQuery(buffer.toString(), AssUpdateTips.class);
			if(null != assChildMember) {
				query.setParameter("assChildMember", assChildMember);
			}
			if(null != assGoodDirectory) {
				query.setParameter("assGoodDirectory", assGoodDirectory);
			}
			if(null != whetherUpdate) {
				query.setParameter("whetherUpdate", whetherUpdate.ordinal());
			}
			if(null != type) {
				query.setParameter("type", type.ordinal());
			}
			return query.getResultList();
		} catch (Exception e) {
			return null;
		}
		
	}

	@Override
	public AssUpdateTips find(AssChildMember assChildMember,
			AssGoodDirectory assGoodDirectory,AssUpdateTips.Type type) {
		if(null == assChildMember.getId() || null == assGoodDirectory.getId()) {
			return null;
		}
		try {
			StringBuffer buffer = new StringBuffer("select * from ass_update_tips updateTips where updateTips.ass_child_member=:assChildMember and updateTips.ass_good_directory=:assGoodDirectory and updateTips.type = :type");
			Query query = entityManager.createNativeQuery(buffer.toString(), AssUpdateTips.class);
			query.setParameter("assChildMember", assChildMember);
			query.setParameter("assGoodDirectory", assGoodDirectory);
			query.setParameter("type", type.ordinal());
			return (AssUpdateTips) query.getSingleResult();
		} catch (Exception e) {
			return null;
		}
		
	}

	@Override
	public AssUpdateTips find(AssChildMember assChildMember,
			CustomerRelation customerRelation, Need need, Type type) {
		if(null == assChildMember || null == assChildMember.getId()) {
			return null;
		}
		try {
			StringBuffer buffer = new StringBuffer("select * from ass_update_tips updateTips where 1=1");
			buffer.append(" and updateTips.ass_child_member = :assChildMember");
			if(null != customerRelation) {
				buffer.append(" and updateTips.customer_relation = :customerRelation");
			}
			if(null != need) {
				buffer.append(" and updateTips.need = :need");
			}
			if(null != type) {
				buffer.append(" and updateTips.type = :type");
			}
			Query query = entityManager.createNativeQuery(buffer.toString(), AssUpdateTips.class);
			query.setParameter("assChildMember", assChildMember);
			if(null != customerRelation) {
				query.setParameter("customerRelation", customerRelation);
			}
			if(null != need) {
				query.setParameter("need", need);
			}
			if(null != type) {
				query.setParameter("type", type.ordinal());
			}
			return (AssUpdateTips) query.getSingleResult();
		} catch (Exception e) {
			return null;
		}
		
	}

	@Override
	public List<AssUpdateTips> findList(CustomerRelation customerRelation,
			Need need, Type type) {
		try {
			StringBuffer buffer = new StringBuffer("select * from ass_update_tips updateTips where 1=1");
			if(null != customerRelation) {
				buffer.append(" and updateTips.customer_relation = :customerRelation");
			}
			if(null != need) {
				buffer.append(" and updateTips.need = :need");
			}
			if(null != type) {
				buffer.append(" and updateTips.type = :type");
			}
			buffer.append(" and updateTips.deleted = 0");
			Query query = entityManager.createNativeQuery(buffer.toString(), AssUpdateTips.class);
			if(null != customerRelation) {
				query.setParameter("customerRelation", customerRelation.getId());
			}
			if(null != need) {
				query.setParameter("need", need.getId());
			}
			if(null != type) {
				query.setParameter("type", type.ordinal());
			}
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<AssUpdateTips> findList(AssChildMember assChildMember,AssUpdateTips.WhetherUpdate whetherUpdate,
			List<Integer> types) {
		try {
			StringBuffer buffer = new StringBuffer("select * from ass_update_tips updateTips where 1=1");
			if(null != assChildMember) {
				buffer.append(" and updateTips.ass_child_member = :assChildMember");
			}
			if(null != whetherUpdate) {
				buffer.append(" and updateTips.whether_update = :whetherUpdate");
			}
			if(null != types) {
				buffer.append(" and updateTips.type in(:types)");
			}
			buffer.append(" and updateTips.deleted = 0");
			Query query = entityManager.createNativeQuery(buffer.toString(), AssUpdateTips.class);
			if(null != assChildMember) {
				query.setParameter("assChildMember", assChildMember);
			}
			if(null != whetherUpdate) {
				query.setParameter("whetherUpdate", whetherUpdate.ordinal());
			}
			if(null != types) {
				query.setParameter("types", types);
			}
			return query.getResultList();
		} catch (Exception e) {
			return null;
		}
		
	}

}
