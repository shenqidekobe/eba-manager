package com.microBusiness.manage.dao;

import com.microBusiness.manage.Filter;
import com.microBusiness.manage.Order;
import com.microBusiness.manage.entity.HostingShop;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.Need;
import com.microBusiness.manage.entity.Shop;

import java.util.List;

public interface HostingShopDao extends BaseDao<HostingShop,Long>{
    List<HostingShop> findListByShop(Shop shop);
    
    boolean exist(Member member , Member byMember);
    
    /**
     * 
     * @Title: findListByShop
     * @author: yuezhiwei
     * @date: 2018年3月14日下午2:22:08
     * @Description: 查询直营店店员
     * @return: List<HostingShop>
     */
    List<HostingShop> findListByShop(Need need);

    List<HostingShop> findListByMember(Member byMember);

	HostingShop findByShopAndByMember(Shop shop, Member byMember);
}
