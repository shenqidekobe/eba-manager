package com.microBusiness.manage.service.ass.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.microBusiness.manage.dao.ass.AssUpdateTipsDao;
import com.microBusiness.manage.entity.CustomerRelation;
import com.microBusiness.manage.entity.Need;
import com.microBusiness.manage.entity.ass.AssChildMember;
import com.microBusiness.manage.entity.ass.AssGoodDirectory;
import com.microBusiness.manage.entity.ass.AssUpdateTips;
import com.microBusiness.manage.entity.ass.AssUpdateTips.Type;
import com.microBusiness.manage.entity.ass.AssUpdateTips.WhetherUpdate;
import com.microBusiness.manage.service.ass.AssUpdateTipsService;
import com.microBusiness.manage.service.impl.BaseServiceImpl;

@Service("assUpdateTipsServiceImpl")
public class AssUpdateTipsServiceImpl extends BaseServiceImpl<AssUpdateTips, Long> implements AssUpdateTipsService {

	@Resource
	private AssUpdateTipsDao assUpdateTipsDao;
	
	@Override
	public List<AssUpdateTips> findList(AssChildMember assChildMember,
			AssGoodDirectory assGoodDirectory, WhetherUpdate whetherUpdate, AssUpdateTips.Type type) {
		return assUpdateTipsDao.findList(assChildMember, assGoodDirectory, whetherUpdate, type);
	}

	@Override
	public AssUpdateTips find(AssChildMember assChildMember,
			AssGoodDirectory assGoodDirectory,AssUpdateTips.Type type) {
		return assUpdateTipsDao.find(assChildMember, assGoodDirectory, type);
	}

	@Override
	public AssUpdateTips find(AssChildMember assChildMember,
			CustomerRelation customerRelation, Need need, Type type) {
		return assUpdateTipsDao.find(assChildMember, customerRelation, need, type);
	}

	@Override
	public List<AssUpdateTips> findList(CustomerRelation customerRelation,
			Need need, Type type) {
		return assUpdateTipsDao.findList(customerRelation, need, type);
	}

	@Override
	public List<AssUpdateTips> findList(AssChildMember assChildMember,AssUpdateTips.WhetherUpdate whetherUpdate,
			List<Integer> types) {
		return assUpdateTipsDao.findList(assChildMember, whetherUpdate, types);
	}

}
