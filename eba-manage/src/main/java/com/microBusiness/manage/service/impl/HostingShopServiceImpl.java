package com.microBusiness.manage.service.impl;

import javax.annotation.Resource;

import com.microBusiness.manage.entity.Shop;

import org.springframework.stereotype.Service;

import com.microBusiness.manage.dao.HostingShopDao;
import com.microBusiness.manage.entity.HostingShop;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.Need;
import com.microBusiness.manage.service.HostingShopService;

import java.util.List;

@Service("hostingShopServiceImpl")
public class HostingShopServiceImpl extends BaseServiceImpl<HostingShop, Long> implements HostingShopService{

	@Resource(name = "hostingShopDaoImpl")
	private HostingShopDao hostingShopDao;

	@Override
	public boolean exist(Member member, Member byMember) {
		return hostingShopDao.exist(member, byMember);
	}

	@Override
	public List<HostingShop> findListByShop(Shop shop) {
		return hostingShopDao.findListByShop(shop);
	}

	@Override
	public List<HostingShop> findListByShop(Need need) {
		return hostingShopDao.findListByShop(need);
	}

	@Override
	public List<HostingShop> findListByMember(Member byMember) {
		return hostingShopDao.findListByMember(byMember);
	}

	@Override
	public HostingShop findByShopAndByMember(Shop shop, Member byMember) {
		// TODO Auto-generated method stub
		return hostingShopDao.findByShopAndByMember(shop,byMember);
	}
}
