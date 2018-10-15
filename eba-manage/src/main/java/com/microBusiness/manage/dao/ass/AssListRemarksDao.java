package com.microBusiness.manage.dao.ass;

import java.util.List;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.BaseDao;
import com.microBusiness.manage.entity.ass.AssList;
import com.microBusiness.manage.entity.ass.AssListRemarks;

public interface AssListRemarksDao extends BaseDao<AssListRemarks, Long>{
	Page<AssListRemarks> getAssListRemarks(Pageable pageable,AssList assList,Long assListRemarksId);
	
	List<AssListRemarks> getRemarksForList(AssList assList);
}
