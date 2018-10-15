package com.microBusiness.manage.service.impl;

import javax.annotation.Resource;

import com.microBusiness.manage.dao.*;
import com.microBusiness.manage.entity.*;

import org.springframework.stereotype.Service;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.service.ShopService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service("shopServiceImpl")
public class ShopServiceImpl extends BaseServiceImpl<Shop,Long> implements ShopService{

	@Resource
	private ShopDao shopDao;
	@Resource
	private NeedDao needDao;
	@Resource
	private MemberDao memberDao;
	@Resource
	private HostingShopDao hostingShopDao;
	@Resource
	private MemberMemberDao memberMemberDao;

	@Override
	public Page<Shop> findPage(Pageable pageable, Member member) {
		// TODO Auto-generated method stub
		return shopDao.findPage(pageable,member);
	}

	@Override
	public void add(ShopType shopType,String name, String userName, Area area, String address, Long[] ids, Member member, String receiverTel) {
		Shop shop=new Shop();
		shop.setShopType(shopType);
		shop.setName(name);
		shop.setUserName(userName);
		shop.setArea(area);
		shop.setAddress(address);
		shop.setMember(member);
		shop.setReceiverTel(receiverTel);

		//新建need
		if (ShopType.direct.equals(shopType)) {
			Need need=new Need();
			need.setName(name);
			need.setUserName(userName);
			need.setTel(member.getMobile());
			need.setArea(area);
			need.setAddress(address);
			need.setMember(member);
			need.setShopType(shopType);
			need.setSourceType(SourceType.small);
			need.setSupplier(member.getAdmin().getSupplier());
			need.setReceiverTel(receiverTel);
			need.getShops().add(shop);
			shop.getNeeds().add(need);
			needDao.persist(need);
		}
		shopDao.persist(shop);
		//托管关系
		if (ids != null){
			for (Long id : ids) {
				MemberMember memberMember=memberMemberDao.find(id);
				Member byMember=memberMember.getByMember();
				HostingShop hostingShop=new HostingShop();
				hostingShop.setMember(member);
				hostingShop.setByMember(byMember);
				hostingShop.setShop(shop);
				hostingShop.setMemberMember(memberMember);
				hostingShopDao.persist(hostingShop);
			}
		}

	}

	@Override
	public void update(Shop shop, String name, String userName, Area area, String address, Long[] ids,String receiverTel) {
		shop.setName(name);
		shop.setUserName(userName);
		shop.setArea(area);
		shop.setAddress(address);
		shop.setReceiverTel(receiverTel);
		shopDao.persist(shop);

		if (ShopType.direct.equals(shop.getShopType())) {
			Need need=shop.getNeeds().iterator().next();
			need.setName(name);
			need.setUserName(userName);
			need.setArea(area);
			need.setAddress(address);
			need.setReceiverTel(receiverTel);
			needDao.persist(need);
		}

		//托管关系
		if (ids != null){
			List<HostingShop> hostingShops=hostingShopDao.findListByShop(shop);
			for(HostingShop hostingShop:hostingShops){
				hostingShopDao.remove(hostingShop);
			}

			for (Long id : ids) {
				MemberMember memberMember=memberMemberDao.find(id);
				Member byMember=memberMember.getByMember();
				HostingShop hostingShop=new HostingShop();
				hostingShop.setMember(shop.getMember());
				hostingShop.setByMember(byMember);
				hostingShop.setShop(shop);
				hostingShop.setMemberMember(memberMember);
				hostingShopDao.persist(hostingShop);
			}
		}
	}

	@Override
	public List<Shop> findList(Member member) {
		return shopDao.findList(member);
	}

	@Override
	public boolean exitShopNameByMember(Member member, String name, Shop shop) {
		// TODO Auto-generated method stub
		return shopDao.exitShopNameByMember(member,name,shop);
	}

	@Override
	public List<Shop> findShopList(Member member) {
		return shopDao.findShopList(member);
	}

	@Override
	public List<Shop> findList(Member member, Product product) {
		return shopDao.findList(member, product);
	}

	@Override
	public Integer count(Member member, Product product, Shop shop) {
		return shopDao.count(member, product, shop);
	}
}
