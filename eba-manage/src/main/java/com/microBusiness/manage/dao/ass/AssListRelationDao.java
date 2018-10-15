package com.microBusiness.manage.dao.ass;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.BaseDao;
import com.microBusiness.manage.entity.ass.AssChildMember;
import com.microBusiness.manage.entity.ass.AssList;
import com.microBusiness.manage.entity.ass.AssListRelation;

public interface AssListRelationDao extends BaseDao<AssListRelation, Long>{
	Page<AssListRelation> getAssListRelations(Pageable pageable,AssList assList, Long assListRelationsId);
	
	AssListRelation findByChildMember(AssChildMember assChildMember,AssList assList);

}
