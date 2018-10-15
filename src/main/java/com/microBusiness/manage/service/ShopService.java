package com.microBusiness.manage.service;

import java.util.List;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.Area;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.Product;
import com.microBusiness.manage.entity.Shop;
import com.microBusiness.manage.entity.ShopType;

public interface ShopService extends BaseService<Shop, Long>{
	
	Page<Shop> findPage(Pageable pageable,Member member);
	/**
	 * 添加店铺
	 * @param type 店铺类型
	 * @param name  店铺名称
	 * @param userName  收货人
	 * @param area   地区
	 * @param address 详细地址
	 * @param ids    店员ids
	 * @param member 所属账号
	 * @param receiverTel 收货人手机号
	 */
	void add(ShopType shopType, String name,String userName,Area area,String address,Long[] ids, Member member, String receiverTel);

	void update(Shop shop,String name,String userName,Area area,String address,Long[] ids,String receiverTel);
	
	/**
	 * 根据账号查询自己店铺和托管的店铺
	 * @param member
	 * @return
	 */
	List<Shop> findList(Member member);
	
	/**
	 * 校验同一个用户下是否有重名的店铺
	 * @param member
	 * @param name
	 * @param shop   修改店铺的时候用，排除当前店铺
	 * @return
	 */
	boolean exitShopNameByMember(Member member,String name,Shop shop);
	
	List<Shop> findShopList(Member member);
	
	/**
	 * 
	 * @Title: findList
	 * @author: yuezhiwei
	 * @date: 2018年4月13日下午5:30:08
	 * @Description: 根据条形码查询为分配此商品的门店
	 * @return: List<Shop>
	 */
	List<Shop> findList(Member member , Product product);
	
	Integer count(Member member , Product product , Shop shop);
}
