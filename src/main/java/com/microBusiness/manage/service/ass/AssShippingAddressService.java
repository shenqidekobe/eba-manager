package com.microBusiness.manage.service.ass;

import java.util.List;

import com.microBusiness.manage.entity.ass.AssChildMember;
import com.microBusiness.manage.entity.ass.AssShippingAddress;
import com.microBusiness.manage.service.BaseService;

/**
 * 功能描述：
 * 修改记录：
 */
public interface AssShippingAddressService extends BaseService<AssShippingAddress, Long> {

	List<AssShippingAddress> findByChildMember(AssChildMember assChildMember);
	
	List<AssShippingAddress> findBYAddress(AssChildMember assChildMember, AssShippingAddress.Defaults defaults);

	/**
	 * 创建采购清单时获取地址
	 * @param assChildMember
	 * @return
	 */
	AssShippingAddress getListAddress(AssChildMember assChildMember);
}
