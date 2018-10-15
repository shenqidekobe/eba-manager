package com.microBusiness.manage.dao;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.Order;
import com.microBusiness.manage.entity.ShareNotes;

public interface ShareNotesDao extends BaseDao<ShareNotes, Long> {
	
	Page<ShareNotes> findPage(Pageable pageable , Order order , Long shareNotesId);

}
