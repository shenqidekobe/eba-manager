package com.microBusiness.manage.dao.ass;

import java.util.List;

import com.microBusiness.manage.dao.BaseDao;
import com.microBusiness.manage.entity.ass.AssChildMember;
import com.microBusiness.manage.entity.ass.AssShippingAddress;

/**
 * 功能描述：
 * 修改记录：
 */
public interface AssShippingAddressDao extends BaseDao<AssShippingAddress, Long> {

	List<AssShippingAddress> findByChildMember(AssChildMember assChildMember);
	
	List<AssShippingAddress> findBYAddress(AssChildMember assChildMember, AssShippingAddress.Defaults defaults);
}
