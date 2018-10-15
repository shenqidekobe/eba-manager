package com.microBusiness.manage.service;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.*;

import java.util.List;

/**
 * Created by mingbai on 2017/3/29.
 * 功能描述：
 * 修改记录：
 */
public interface NoticeUserService extends BaseService<NoticeUser , Long> {
    Page<NoticeUser> findPage(Pageable pageable , Supplier supplier , String searchName);

    NoticeUser findByOpenId(String openId);

    NoticeUser save(WeChatUserInfo userInfo , Long supplierId);

    NoticeUser update(Long id , NoticeType.Type[] types);

    List<NoticeType.Type> getOrderNoticeTypes(List<NoticeType> types);

    List<Supplier> getOrderNoticeSupplier(List<NoticeUserSupplier> noticeUserSuppliers);

    boolean saveOrderNotice(NoticeUser noticeUser , Long[] supplierIds , NoticeType.Type[] types , Long[] needIds) ;

    /**
     * 采购单通知类型
     * @param types
     * @return
     */
    List<NoticeTypePurchase.Type> getPurchaseNoticeTypes(List<NoticeTypePurchase> types);

    /**
     * 获取需要被接受消息的收货点
     * @param noticeUserNeeds
     * @return
     */
    List<Need> getPurchaseNoticeNeeds(List<NoticeUserNeed> noticeUserNeeds);

    boolean savePurchaseNotice(NoticeUser noticeUser , Long[] needIds , NoticeTypePurchase.Type[] types);

    /**
     * 订货单 需要通知的 个体客户关系
     * @param noticeUserOrderNeeds
     * @return
     */
    List<Need> getOrderNoticeNeeds(List<NoticeUserOrderNeed> noticeUserOrderNeeds);

}
