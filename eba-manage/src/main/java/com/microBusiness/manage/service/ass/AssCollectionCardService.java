package com.microBusiness.manage.service.ass;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.ass.AssCard;
import com.microBusiness.manage.entity.ass.AssChildMember;
import com.microBusiness.manage.entity.ass.AssCollectionCard;
import com.microBusiness.manage.service.BaseService;

public interface AssCollectionCardService extends BaseService<AssCollectionCard, Long>{
	Page<AssCollectionCard> findPage(AssChildMember assChildMember,Pageable pageable);
	
	AssCollectionCard getAssCollectionCard(AssChildMember assChildMember,AssCard assCard);
}
