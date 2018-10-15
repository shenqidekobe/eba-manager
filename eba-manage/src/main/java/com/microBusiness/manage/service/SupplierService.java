package com.microBusiness.manage.service;

import java.util.List;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dto.SupplierDto;
import com.microBusiness.manage.entity.*;
import com.microBusiness.manage.entity.Supplier.Status;

/**
 * Created by mingbai on 2017/2/6.
 * 功能描述：
 * 修改记录：
 */
public interface SupplierService extends BaseService<Supplier, Long> {
	boolean exists(String name);
	public Page<Supplier> findPage(Pageable pageable , Supplier.Status status);
	List<Supplier> findByStatus(Status... status);


	boolean update(Supplier supplier,Long adminId , String ...supplierIgnore);

	List<Supplier> findFormal(Need need , Pageable pageable);

	/**
	 * 通过被供应企业查询供应的企业
	 * @param bySupplierId
	 * @return
	 */
	List<Supplier> getSupplierFromBy(Long bySupplierId);
	
	/**
	 * 修改时判断用户名是否存在
	 * @param name
	 * @param id
	 * @return
	 */
	boolean existaName(String name,Long id);

	/**
	 * 通过邀请码查询企业
	 * @param inviteCode
	 * @return
	 */
	Supplier getSupplierByInviteCode(String inviteCode);
	
	/**
	 * 邀请码是否存在
	 * @param inviteCode
	 * @return
	 */
	boolean inviteCodeExists(String inviteCode);

	/**
	 * 推荐企业
	 * 
	 * @param count
	 * 			个数
	 * @return 企业列表
	 */
	List<Supplier> recommendSupplierList(Long count);
	
	/**
	 * 企业展示
	 * 
	 * @param pageable
	 * @param name
	 * @param status
	 * @return
	 */
	public Page<Supplier> findPage(Admin admin, Pageable pageable, String name, Status status);
	
	/**
	 * 更新企业推荐
	 * 
	 * @param ids
	 * @param idsNo
	 * @return
	 */
	boolean updateSupplier(Long[] ids, Long[] idsNo);
	
	/**
	 * 通过个体客户获取建立了客户供应关系的企业
	 * @param need
	 * @return
	 */
	public List<Supplier> findBySupplierNeed(Need need);

	public List<SupplierDto> findSupplierListByMember(Member member , Long supplierId);
	
	List<SupplierDto> findFormal(Member member , Long supplierId);
	
	/**
	 * 根据appId查找企业
	 * 
	 * @param appId
	 * 			 企业接口key
	 * @return 企业
	 */
	public Supplier findByAppId(String appId);
	
	
	/**
	 * 
	 * @Title: doesItExistName
	 * @author: yuezhiwei
	 * @param member 
	 * @date: 2018年3月7日下午4:13:19
	 * @Description: 查询是否有重名的企业名称
	 * @return: boolean
	 * name 企业名称
	 * types 类型（本地/平台）
	 */
	boolean doesItExistName(String name , Types types, Member member);

	/**
	 * 根据账号查询本地供应商
	 * @param member
	 * @return
	 */
	List<Supplier> getSupplierListByMember(Member member);

	/**
	 * 微商小管理小程序端 供应分配
	 * @param shop
	 * @param supplierType     供应商类型
	 * @param supplier            供应商
	 * @param supplyNeed        店铺供应
	 * @param assignedModel      直销or分销
	 * @param supplierSupplier    企业供应
	 * @param productList        分配的商品list
	 */
    void assignProducts(Shop shop, SupplierType supplierType, Supplier supplier, Long relationId, SupplyNeed.AssignedModel assignedModel, List<Product> productList);
    
    
    /**
     * 
     * @Title: deletedLocalSupplier
     * @author: yuezhiwei
     * @date: 2018年3月27日下午1:54:12
     * @Description: 删除本地供应商
     * @return: boolean
     */
    boolean deletedLocalSupplier(Supplier supplier , List<Goods> goods);
    
    List<Supplier> getSupplierListByMember(Member member , ProductCenter productCenter , String name);

}
