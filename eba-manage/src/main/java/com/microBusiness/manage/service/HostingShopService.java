package com.microBusiness.manage.service;

import com.microBusiness.manage.entity.HostingShop;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.Need;
import com.microBusiness.manage.entity.Shop;

import java.util.List;

public interface HostingShopService extends BaseService<HostingShop, Long>{

	/**
	 * 
	 * @Title: exist
	 * @author: yuezhiwei
	 * @date: 2018年3月13日下午3:15:08
	 * @Description: TODO
	 * @return: boolean
	 */
	boolean exist(Member member , Member byMember);

	List<HostingShop> findListByShop(Shop shop);
	
	/**
	 * 
	 * @Title: findListByShop
	 * @author: yuezhiwei
	 * @date: 2018年3月14日下午2:21:19
	 * @Description: 查询直营店店员
	 * @return: List<HostingShop>
	 */
	List<HostingShop> findListByShop(Need need);

	/**
	 * 根据账号查询该账号下托管的店铺
	 * @param byMember
	 * @return
	 */
	List<HostingShop> findListByMember(Member byMember);
	
	HostingShop findByShopAndByMember(Shop shop,Member byMember);

}
