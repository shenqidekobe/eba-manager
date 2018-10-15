package com.microBusiness.manage.service.ass;

import java.util.List;
import java.util.Map;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.ass.AssList;
import com.microBusiness.manage.entity.ass.AssListRelation;
import com.microBusiness.manage.service.BaseService;

public interface AssListRelationService extends BaseService<AssListRelation, Long>{

	Page<AssListRelation> getAssListRelations(Pageable pageable,AssList assList, Long assListRelationsId);
}
