package com.microBusiness.manage.service.ass.impl;

import java.util.List;

import javax.annotation.Resource;

import com.microBusiness.manage.dao.ass.AssShippingAddressDao;
import com.microBusiness.manage.entity.ass.AssChildMember;
import com.microBusiness.manage.entity.ass.AssShippingAddress;
import com.microBusiness.manage.entity.ass.AssShippingAddress.Defaults;
import com.microBusiness.manage.service.ass.AssShippingAddressService;
import com.microBusiness.manage.service.impl.BaseServiceImpl;

import org.springframework.stereotype.Service;

@Service("assShippingAddressServiceImpl")
public class AssShippingAddressServiceImpl extends BaseServiceImpl<AssShippingAddress, Long> implements AssShippingAddressService {

	@Resource
	private AssShippingAddressDao assShippingAddressDao;

	@Override
	public List<AssShippingAddress> findByChildMember(AssChildMember assChildMember) {
		return assShippingAddressDao.findByChildMember(assChildMember);
	}

	@Override
	public List<AssShippingAddress> findBYAddress(
			AssChildMember assChildMember, Defaults defaults) {
		return assShippingAddressDao.findBYAddress(assChildMember, defaults);
	}

	@Override
	public AssShippingAddress getListAddress(AssChildMember assChildMember) {
		List<AssShippingAddress> assShippingAddresses = assShippingAddressDao.findByChildMember(assChildMember);
		if(assShippingAddresses.size() > 0) {
			List<AssShippingAddress> defaultAddress = assShippingAddressDao.findBYAddress(assChildMember, AssShippingAddress.Defaults.defaults);
			if(defaultAddress.size() > 0) {
				return defaultAddress.get(0);
			}else {
				return assShippingAddresses.get(0);
			}
			
		}else {
			return null;
		}
	}

}
