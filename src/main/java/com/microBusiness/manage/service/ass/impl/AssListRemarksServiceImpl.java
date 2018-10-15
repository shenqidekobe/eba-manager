package com.microBusiness.manage.service.ass.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.ass.AssListRemarksDao;
import com.microBusiness.manage.entity.ass.AssList;
import com.microBusiness.manage.entity.ass.AssListRemarks;
import com.microBusiness.manage.service.ass.AssListRemarksService;
import com.microBusiness.manage.service.impl.BaseServiceImpl;

@Service("assListRemarksServiceImpl")
public class AssListRemarksServiceImpl extends BaseServiceImpl<AssListRemarks, Long> implements AssListRemarksService{
	
	@Resource
	private AssListRemarksDao assListRemarksDao ;
	
	@Override
	public Page<AssListRemarks> getAssListRemarks(Pageable pageable, AssList assList,Long assListRemarksId) {
		Page<AssListRemarks> page=assListRemarksDao.getAssListRemarks(pageable, assList, assListRemarksId);
		return page;
	}

	@Override
	public List<AssListRemarks> getRemarksForList(AssList assList) {
		// TODO Auto-generated method stub
		return assListRemarksDao.getRemarksForList(assList);
	}


}
