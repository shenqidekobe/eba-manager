package com.microBusiness.manage.dao;

import java.util.List;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.Product;
import com.microBusiness.manage.entity.Shop;

public interface ShopDao  extends BaseDao<Shop, Long>{

	Page<Shop> findPage(Pageable pageable, Member member);
	
	List<Shop> findList(Member member);

	boolean exitShopNameByMember(Member member, String name, Shop shop);

	List<Shop> findShopList(Member member);
	
	List<Shop> findList(Member member , Product product);
	
	Integer count(Member member , Product product , Shop shop);
}
