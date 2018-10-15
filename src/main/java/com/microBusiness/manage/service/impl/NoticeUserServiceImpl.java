package com.microBusiness.manage.service.impl;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.*;
import com.microBusiness.manage.entity.*;
import com.microBusiness.manage.service.NoticeUserService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by mingbai on 2017/3/29.
 * 功能描述：
 * 修改记录：
 */
@Service
public class NoticeUserServiceImpl extends BaseServiceImpl<NoticeUser, Long> implements NoticeUserService {
    @Resource
    private NoticeUserDao noticeUserDao ;
    @Resource
    private SupplierDao supplierDao ;
    @Resource
    private NoticeTypeDao noticeTypeDao ;

    @Resource
    private NoticeUserSupplierDao noticeUserSupplierDao ;

    @Resource
    private NoticeTypePurchaseDao noticeTypePurchaseDao ;

    @Resource
    private NoticeUserNeedDao noticeUserNeedDao ;

    @Resource
    private NoticeUserOrderNeedDao noticeUserOrderNeedDao ;

    @Resource
    private NeedDao needDao ;


    @Override
    public Page<NoticeUser> findPage(Pageable pageable , Supplier supplier , String searchName) {
        return noticeUserDao.findPage(pageable , supplier , searchName);
    }

    @Override
    public NoticeUser findByOpenId(String openId) {
        return noticeUserDao.findByOpenId(openId);
    }

    @Override
    public NoticeUser save(WeChatUserInfo userInfo, Long supplierId) {
        Supplier supplier = supplierDao.find(supplierId);
        NoticeUser noticeUser = new NoticeUser() ;
        noticeUser.setSupplier(supplier);
        noticeUser.setOpenId(userInfo.getOpenId());
        noticeUser.setNickName(userInfo.getNickName());
        noticeUser.setBindDate(new Date());
        noticeUser.setNoticeTypes(null);
        noticeUserDao.persist(noticeUser);
        return noticeUser ;
    }

    @Override
    public NoticeUser update(Long id, NoticeType.Type[] types) {
        NoticeUser noticeUser = noticeUserDao.find(id);

        for(NoticeType type : noticeUser.getNoticeTypes()){
            noticeTypeDao.remove(type);
        }

        if(null == types){
            noticeUser.setNoticeTypes(null);
            return noticeUser ;
        }


        List<NoticeType> noticeTypes = new ArrayList<>();
        for(NoticeType.Type type : types){
            NoticeType temp = new NoticeType();
            temp.setNoticeUser(noticeUser);
            temp.setType(type);
            noticeTypes.add(temp);
        }
        noticeUser.setNoticeTypes(noticeTypes);

        return noticeUser;
    }

    @Override
    public List<NoticeType.Type> getOrderNoticeTypes(List<NoticeType> types) {
        List<NoticeType.Type> retTypes = new ArrayList<>();
        if(CollectionUtils.isEmpty(types)){
            return retTypes ;
        }

        for (NoticeType type : types){
            retTypes.add(type.getType());
        }
        return retTypes;
    }

    @Override
    public List<Supplier> getOrderNoticeSupplier(List<NoticeUserSupplier> noticeUserSuppliers) {
        List<Supplier> suppliers = new ArrayList<>();
        if (CollectionUtils.isEmpty(noticeUserSuppliers)) {
            return suppliers;
        }
        for(NoticeUserSupplier noticeUserSupplier : noticeUserSuppliers){
            suppliers.add(noticeUserSupplier.getSupplier());
        }
        return suppliers;
    }


    @Override
    public boolean saveOrderNotice(NoticeUser noticeUser, Long[] supplierIds, NoticeType.Type[] types , Long[] needIds ) {

        for(NoticeType type : noticeUser.getNoticeTypes()){
            noticeTypeDao.remove(type);
        }
        List<NoticeType> noticeTypes = new ArrayList<>();
        for(NoticeType.Type type : types){
            NoticeType saveType = new NoticeType();
            saveType.setNoticeUser(noticeUser);
            saveType.setType(type);
            //noticeTypeDao.persist(saveType);
            noticeTypes.add(saveType);
        }

        for(NoticeUserSupplier noticeUserSupplier : noticeUser.getNoticeUserSuppliers()){
            noticeUserSupplierDao.remove(noticeUserSupplier);
        }

        List<NoticeUserSupplier> noticeUserSuppliers = new ArrayList<>() ;
        if(ArrayUtils.isNotEmpty(supplierIds)){
            for(Long supplierId : supplierIds){
                NoticeUserSupplier saveEntity = new NoticeUserSupplier() ;
                saveEntity.setNoticeUser(noticeUser);
                saveEntity.setSupplier(supplierDao.find(supplierId));
                //noticeUserSupplierDao.persist(saveEntity);
                noticeUserSuppliers.add(saveEntity);
            }
        }


        for(NoticeUserOrderNeed noticeUserOrderNeed : noticeUser.getNoticeUserOrderNeeds()){
            noticeUserOrderNeedDao.remove(noticeUserOrderNeed);
        }

        List<NoticeUserOrderNeed> noticeUserOrderNeeds = new ArrayList<>() ;
        if(ArrayUtils.isNotEmpty(needIds)){
            for(Long needId : needIds){
                NoticeUserOrderNeed saveEntity = new NoticeUserOrderNeed() ;
                saveEntity.setNoticeUser(noticeUser);
                saveEntity.setNeed(needDao.find(needId));
                //noticeUserSupplierDao.persist(saveEntity);
                noticeUserOrderNeeds.add(saveEntity);
            }
        }



        noticeUser.setNoticeUserSuppliers(noticeUserSuppliers);
        noticeUser.setNoticeUserOrderNeeds(noticeUserOrderNeeds);
        noticeUser.setNoticeTypes(noticeTypes);

        return true;
    }


    /**
     * 采购单通知类型
     *
     * @param types
     * @return
     */
    @Override
    public List<NoticeTypePurchase.Type> getPurchaseNoticeTypes(List<NoticeTypePurchase> types) {

        List<NoticeTypePurchase.Type> retTypes = new ArrayList<>();
        if(CollectionUtils.isEmpty(types)){
            return retTypes ;
        }

        for (NoticeTypePurchase type : types){
            retTypes.add(type.getType());
        }

        return retTypes;
    }


    /**
     * 获取需要被接受消息的收货点
     *
     * @param noticeUserNeeds
     * @return
     */
    @Override
    public List<Need> getPurchaseNoticeNeeds(List<NoticeUserNeed> noticeUserNeeds) {
        List<Need> needs = new ArrayList<>();
        if (CollectionUtils.isEmpty(noticeUserNeeds)) {
            return needs;
        }
        for(NoticeUserNeed noticeUserNeed : noticeUserNeeds){
            needs.add(noticeUserNeed.getNeed());
        }
        return needs;
    }


    @Override
    public boolean savePurchaseNotice(NoticeUser noticeUser, Long[] needIds, NoticeTypePurchase.Type[] types) {

        for(NoticeTypePurchase type : noticeUser.getNoticeTypePurchases()){
            noticeTypePurchaseDao.remove(type);
        }
        List<NoticeTypePurchase> noticeTypePurchases = new ArrayList<>();

        for(NoticeTypePurchase.Type type : types){
            NoticeTypePurchase saveType = new NoticeTypePurchase();
            saveType.setNoticeUser(noticeUser);
            saveType.setType(type);
            //noticeTypeDao.persist(saveType);
            noticeTypePurchases.add(saveType);
        }

        for(NoticeUserNeed noticeUserNeed : noticeUser.getNoticeUserNeeds()){
            noticeUserNeedDao.remove(noticeUserNeed);
        }

        List<NoticeUserNeed> noticeUserNeeds = new ArrayList<>() ;
        for(Long needId : needIds){
            NoticeUserNeed saveEntity = new NoticeUserNeed() ;
            saveEntity.setNoticeUser(noticeUser);
            saveEntity.setNeed(needDao.find(needId));
            //noticeUserSupplierDao.persist(saveEntity);
            noticeUserNeeds.add(saveEntity);
        }

        noticeUser.setNoticeUserNeeds(noticeUserNeeds);
        noticeUser.setNoticeTypePurchases(noticeTypePurchases);

        return true;

    }

    /**
     * 订货单 需要通知的 个体客户关系
     *
     * @param noticeUserOrderNeeds
     * @return
     */
    @Override
    public List<Need> getOrderNoticeNeeds(List<NoticeUserOrderNeed> noticeUserOrderNeeds) {
        List<Need> needs = new ArrayList<>();
        if (CollectionUtils.isEmpty(noticeUserOrderNeeds)) {
            return needs;
        }
        for(NoticeUserOrderNeed noticeUserOrderNeed : noticeUserOrderNeeds){
            needs.add(noticeUserOrderNeed.getNeed());
        }
        return needs;
    }
}
