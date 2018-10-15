package com.microBusiness.manage.service.ass;

import java.util.List;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.ass.AssList;
import com.microBusiness.manage.entity.ass.AssListRemarks;
import com.microBusiness.manage.service.BaseService;

public interface AssListRemarksService extends BaseService<AssListRemarks, Long>{
	Page<AssListRemarks> getAssListRemarks(Pageable pageable,AssList assList,Long assListRemarksId);
	
	List<AssListRemarks> getRemarksForList(AssList assList);
}
