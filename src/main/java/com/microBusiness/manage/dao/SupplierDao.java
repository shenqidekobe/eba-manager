package com.microBusiness.manage.dao;


import java.util.List;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dto.SupplierDto;
import com.microBusiness.manage.entity.FavorCompany;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.Need;
import com.microBusiness.manage.entity.Product;
import com.microBusiness.manage.entity.ProductCenter;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.entity.Supplier.Status;
import com.microBusiness.manage.entity.Types;

/**
 * Created by mingbai on 2017/1/22.
 * 功能描述：
 * 修改记录：
 */
public interface SupplierDao extends BaseDao<Supplier, Long> {

	boolean exists(String name);
	public Page<Supplier> findPage(Pageable pageable , Supplier.Status status);
	List<Supplier> findByStatus(Status... status);

	List<Supplier> findFormal(Need need , Pageable pageable);

	/**
	 * 通过被供应企业查询供应的企业
	 * @param bySupplierId
	 * @return
	 */
	List<Supplier> getSupplierFromBy(Long bySupplierId);
	
	boolean existaName(String name,Long id);

	/**
	 * 邀请码是否存在。
	 * @param inviteCode
	 * @return
	 */
	boolean inviteCodeExists(String inviteCode);

	Supplier getSupplierByInviteCode(String inviteCode);

	Supplier getSupplierByNeed(Long needId);
	
	/**
	 * 推荐企业
	 * 
	 * @param count
	 * 			个数
	 * @return 企业列表
	 */
	List<Supplier> getSupplierList(Long count);
	
	/**
	 * 企业展示
	 * 
	 * @param pageable
	 * @param name
	 * @param status
	 * @return
	 */
	public Page<Supplier> findPage(Pageable pageable , String name, Boolean status);
	
	/**
	 * 查询当前用户所收藏的企业信息
	 * @param adminId
	 * @param delflag 伪删除标识
	 * @param pageable
	 * @return
	 */
	public Page<Supplier> findPage(Long adminId , FavorCompany.Delflag delflag , Pageable pageable);
	
	public List<Supplier> findBySupplierNeed(Need need);

	public List<SupplierDto> findSupplierListByMember(Member member , Long supplierId );
	
	List<SupplierDto> findFormal(Member member  , Long supplierId);
	
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
    
    List<Supplier> getSupplierListByMember(Member member , ProductCenter productCenter , String name);
}
