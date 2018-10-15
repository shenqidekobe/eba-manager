package com.microBusiness.manage.service;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dto.ShopAssistantDto;
import com.microBusiness.manage.entity.*;
import com.microBusiness.manage.entity.Need.NeedStatus;
import com.microBusiness.manage.entity.Need.Type;
import com.microBusiness.manage.entity.ass.AssChildMember;

import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by mingbai on 2017/1/23.
 * 功能描述：
 * 修改记录：
 */
public interface NeedService extends BaseService<Need, Long> {
    /**
     *
     * @param pageable
     * @return
     */
    Page<Need> findPage(Pageable pageable , Need need , String searchName, Date startDate, Date endDate) ;

    @Deprecated
    Page<Need> findPage(Pageable pageable , Need need , Date startDate , Date endDate);

    List<Need> findBySupplier(Supplier supplier, Need.Type type);

    Need findByTel(String tel);

	Page<Need> findPage(Pageable pageable, Supplier supplier);

    Page<Need> findPage(Pageable pageable, Supplier supplier , Need.Type type,String searchName);
    
    Page<Need> findPage(Pageable pageable, Supplier supplier, Type type, String searchName,
			List<NeedStatus> needStatus);

    /**
     * 处理批量导入收货点
     * @param multipartFile
     * @param admin
     * @return
     */
    NeedImportLog dealImportMore(MultipartFile multipartFile , Admin admin , Supplier supplier);


    boolean saveMore(NeedImportLog needImportLog, Admin admin);
    
    /*
	 * 个体是否存在
	 */
	boolean telExists(String tel , Supplier supplier);
	
	Need findByTelSupplier(String tel , Supplier supplier);
    
    Need modifyCheckTel(String tel , Long id);
    
    /**
     * 修改收货点是否可用的状态
     * @param need
     * @return
     */
    Need updateneedStatus(Need need);
    
    Need updateNeed(Need need, Admin admin);
    
    Page<Need> findPage(Pageable pageable, Supplier supplier , SupplierSupplier supplyRelation ,  Need.Type type , Need.NeedStatus needStatus , Need.Status status,ShopType shopType);
 
    
    /**
     * 根据个体客户关系和手机号找到个体信息
     * @return
     */
    Need findNeedByMemberSupplier(Supplier supplier, Member member);
    
    /**
     * 订货助理我的客户中个体客户列表
     * @param assChildMember
     * @param pageable
     * @return
     */
    Page<Need> findPage(AssChildMember assChildMember , Pageable pageable);
    
    /**
     * 订货助理更新个体客户
     * @param need
     * @return
     */
    Need updateIndividualCustomers(Need need);
    
    List<Need> findList(AssChildMember assChildMember);

    
    /**
     * 
     * @Title: SaveDirectStore
     * @author: yuezhiwei
     * @date: 2018年3月12日下午1:52:32
     * @Description: 添加直营店
     * @param need
     * @param shopAssistantName 店员姓名
     * @param shopAssistantTel 店员手机号
     * @return: Need
     */
    Need saveDirectStore(Need need , String shopAssistantName , String shopAssistantTel);
    
    /**
     * 
     * @Title: existTel
     * @author: yuezhiwei
     * @date: 2018年3月12日下午7:09:32
     * @Description: TODO
     * @return: boolean
     */
    boolean existTel(Supplier supplier , String tel , ShopType shopType);
    
    /**
     * 
     * @Title: updateDirectNeed
     * @author: yuezhiwei
     * @date: 2018年3月14日下午3:19:22
     * @Description: 更新直营店
     * @return: Need
     */
    Need updateDirectNeed(Need need , Admin admin , List<ShopAssistantDto> assistantDtos);

    void refreshNeed(Need need);
    
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
     * @Title: nameExists
     * @author: yuezhiwei
     * @date: 2018年3月29日下午7:05:32
     * @Description: 门店名称不能重复
     * @return: boolean
     */
    boolean nameExists(String name , Supplier supplier);
    
    /**
     * 
     * @Title: findByPage
     * @author: yuezhiwei
     * @date: 2018年3月29日下午7:22:59
     * @Description: 门店列表
     * @return: Page<Need>
     */
    Page<Need> findByPage(Pageable pageable , Need need , String searchName, Date startDate, Date endDate) ;
    
    /**
     * 
     * @Title: nameExists
     * @author: yuezhiwei
     * @date: 2018年3月30日下午1:19:35
     * @Description: 门店名称不能重复
     * @return: boolean
     */
    boolean nameExists(String name , Supplier supplier , Need need);

}

