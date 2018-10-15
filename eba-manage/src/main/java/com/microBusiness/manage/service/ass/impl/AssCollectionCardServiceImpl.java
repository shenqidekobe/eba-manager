package com.microBusiness.manage.service.ass.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.ass.AssCollectionCardDao;
import com.microBusiness.manage.entity.ass.AssCard;
import com.microBusiness.manage.entity.ass.AssChildMember;
import com.microBusiness.manage.entity.ass.AssCollectionCard;
import com.microBusiness.manage.service.ass.AssCollectionCardService;
import com.microBusiness.manage.service.impl.BaseServiceImpl;

@Service("assCollectionCardServiceImpl")
public class AssCollectionCardServiceImpl extends BaseServiceImpl<AssCollectionCard, Long> implements AssCollectionCardService{

	@Resource
	private AssCollectionCardDao assCollectionCardDao ;

	@Override
	public Page<AssCollectionCard> findPage(AssChildMember assChildMember, Pageable pageable) {
		// TODO Auto-generated method stub
		return assCollectionCardDao.findPage(assChildMember,pageable);
	}

	@Override
	public AssCollectionCard getAssCollectionCard(AssChildMember assChildMember, AssCard assCard) {
		// TODO Auto-generated method stub
		return assCollectionCardDao.getAssCollectionCard(assChildMember,assCard);
		
	}
}
