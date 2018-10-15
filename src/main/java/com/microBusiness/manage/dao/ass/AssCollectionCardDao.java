package com.microBusiness.manage.dao.ass;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.BaseDao;
import com.microBusiness.manage.entity.ass.AssCard;
import com.microBusiness.manage.entity.ass.AssChildMember;
import com.microBusiness.manage.entity.ass.AssCollectionCard;

public interface AssCollectionCardDao extends BaseDao<AssCollectionCard, Long>{

	Page<AssCollectionCard> findPage(AssChildMember assChildMember, Pageable pageable);
	
	AssCollectionCard getAssCollectionCard(AssChildMember assChildMember,AssCard assCard);

}
