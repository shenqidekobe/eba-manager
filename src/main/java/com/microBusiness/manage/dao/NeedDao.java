package com.microBusiness.manage.dao;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.Need;
import com.microBusiness.manage.entity.Need.NeedStatus;
import com.microBusiness.manage.entity.Need.Type;
import com.microBusiness.manage.entity.ShopType;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.entity.SupplierSupplier;
import com.microBusiness.manage.entity.ass.AssChildMember;

import java.util.Date;
import java.util.List;

/**
 * Created by mingbai on 2017/1/22.
 * 功能描述：
 * 修改记录：
 */
public interface NeedDao extends BaseDao<Need, Long> {
    Page<Need> findPage(Pageable pageable , Need need , String searchName, Date startDate, Date endDate);

    /**
     * 不再使用
     * @param pageable
     * @param need
     * @param startDate
     * @param endDate
     * @return
     */
    @Deprecated
    Page<Need> findPage(Pageable pageable, Need need, Date startDate, Date endDate) ;

    boolean telExists(String tel , Supplier supplier);
    
    /**
     * 门店名称不能重复
     * 
     * @param tel
     * @param supplier
     * @return
     */
    boolean nameExists(String name , Supplier supplier);

    Need findByTelSupplier(String tel , Supplier supplier);
    
    List<Need> findBySupplier(Supplier supplier, Need.Type type);

    Need findByTel(String tel);

	Page<Need> findPage(Pageable pageable, Supplier supplier);

    Page<Need> findPage(Pageable pageable, Supplier supplier , Need.Type type,String searchName);
    
    Need modifyCheckTel(String tel , Long id);
    
    Member findByMember(Long needId);
    
    Page<Need> findPage(Pageable pageable, Supplier supplier , SupplierSupplier supplyRelation, Need.Type type , Need.NeedStatus needStatus , Need.Status status,ShopType shopType);
    
    Need findNeedByMemberSupplier(Supplier supplier, Member member);
    
    /**
     * 订货助理我的客户中个体客户列表
     * @param assChildMember
     * @param pageable
     * @return
     */
    Page<Need> findPage(AssChildMember assChildMember , Pageable pageable);
    
    List<Need> findList(AssChildMember assChildMember);

    
    /**
     * 
     * @Title: existTel
     * @author: yuezhiwei
     * @date: 2018年3月12日下午7:11:43
     * @Description: TODO
     * @return: boolean
     */
    boolean existTel(Supplier supplier , String tel , ShopType shopType);
    
    /**
     * 
     * @Title: findList
     * @author: yuezhiwei
     * @date: 2018年3月26日下午7:02:20
     * @Description: 查询门店（向本企业代下单时查询门店）
     * @return: List<Need>
     */
    List<Need> findList(Supplier supplier , ShopType shopType);

    
    /**
     * 
     * @Title: findByList
     * @author: yuezhiwei
     * @date: 2018年3月19日上午11:15:54
     * @Description: 添加个体供应中查询客户（按名称首字母排序）
     * @return: List<Need>
     */
    List<Need> findByList(Supplier supplier, Need.Type type);
    
    Need find(String clientNum , Supplier supplier);
    
    /**
     * 
     * @Title: findByPage
     * @author: yuezhiwei
     * @date: 2018年3月29日下午7:23:55
     * @Description: 查询门店列表
     * @return: Page<Need>
     */
    Page<Need> findByPage(Pageable pageable , Need need , String searchName, Date startDate, Date endDate) ;
    
    /**
     * 
     * @Title: nameExists
     * @author: yuezhiwei
     * @date: 2018年3月30日下午1:20:46
     * @Description: 门店名称不能重复
     * @return: boolean
     */
    boolean nameExists(String name , Supplier supplier , Need need);

	Page<Need> findPage(Pageable pageable, Supplier supplier, Type type, String searchName,
			List<NeedStatus> needStatus);

}
