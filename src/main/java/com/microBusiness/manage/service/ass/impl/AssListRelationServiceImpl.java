package com.microBusiness.manage.service.ass.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.ass.AssListRelationDao;
import com.microBusiness.manage.entity.ass.AssList;
import com.microBusiness.manage.entity.ass.AssListRelation;
import com.microBusiness.manage.service.ass.AssListRelationService;
import com.microBusiness.manage.service.impl.BaseServiceImpl;

@Service("assListRelationServiceImpl")
public class AssListRelationServiceImpl extends BaseServiceImpl<AssListRelation, Long> implements AssListRelationService{
	@Resource
	private AssListRelationDao assListRelationDao ;

	@Override
	public Page<AssListRelation> getAssListRelations(Pageable pageable,
			AssList assList, Long assListRelationsId) {
		Page<AssListRelation> page=assListRelationDao.getAssListRelations(pageable,assList,assListRelationsId);
		return page;
	}
}
