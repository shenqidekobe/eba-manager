package com.microBusiness.manage.dao;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.*;

import java.util.List;

/**
 * Created by mingbai on 2017/3/29.
 * 功能描述：
 * 修改记录：
 */
public interface NoticeUserDao extends BaseDao<NoticeUser , Long> {
    Page<NoticeUser> findPage(Pageable pageable , Supplier supplier , String searchName);

    NoticeUser findByOpenId(String openId) ;

    List<NoticeUser> findList(Supplier supplier , NoticeType.Type type);

    /**
     * 获取订货单消息接受员
     * @param supplier 接受员所属企业
     * @param bySupplier 哪些企业下单
     * @param type
     * @return
     */
    List<NoticeUser> findList(Supplier supplier , Supplier bySupplier , NoticeType.Type type);

    /**
     * 获取采购单消息接受员
     * @param supplier 接受员所属企业
     * @param need 接受员所属企业下的收货点
     * @param type
     * @return
     */
    List<NoticeUser> findList(Supplier supplier , Need need , NoticeTypePurchase.Type type);

    /**
     * 订货单中 对个体客户的 消息接受员
     * @param supplier
     * @param need
     * @param type
     * @return
     */
    List<NoticeUser> findList(Supplier supplier , Need need , NoticeType.Type type);
}
