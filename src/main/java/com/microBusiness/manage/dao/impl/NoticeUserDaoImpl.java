package com.microBusiness.manage.dao.impl;

import com.microBusiness.manage.Filter;
import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.NoticeUserDao;
import com.microBusiness.manage.entity.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mingbai on 2017/3/29.
 * 功能描述：
 * 修改记录：
 */
@Repository
public class NoticeUserDaoImpl extends BaseDaoImpl<NoticeUser , Long> implements NoticeUserDao {
    @Override
    public Page<NoticeUser> findPage(Pageable pageable, Supplier supplier , String searchName) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<NoticeUser> criteriaQuery = criteriaBuilder.createQuery(NoticeUser.class);
        Root<NoticeUser> root = criteriaQuery.from(NoticeUser.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        if(null != supplier){
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("supplier"), supplier));
        }
        if(StringUtils.isNotEmpty(searchName)) {
        	restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.like(root.<String>get("nickName"), "%"+searchName+"%"));
        }
        criteriaQuery.where(restrictions);
        return super.findPage(criteriaQuery , pageable);
    }

    @Override
    public NoticeUser findByOpenId(String openId) {

        if (StringUtils.isEmpty(openId)) {
            return null;
        }
        try {
            String jpql = "select noticeUser from NoticeUser noticeUser where noticeUser.openId=:openId";
            return entityManager.createQuery(jpql, NoticeUser.class).setParameter("openId", openId).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<NoticeUser> findList(Supplier supplier, NoticeType.Type type) {
        StringBuffer findSql = new StringBuffer("select noticeUser.* from t_notice_user noticeUser") ;
        findSql.append(" inner join t_notice_type noticeType on noticeUser.id=noticeType.notice_user");
        findSql.append(" where 1=1");
        if(null != supplier && null != supplier.getId()){
            findSql.append(" and noticeUser.supplier=:supplier");
        }
        if(null != type){
            findSql.append(" and noticeType.type=:type");
        }
        Query query = entityManager.createNativeQuery(findSql.toString() , NoticeUser.class);
        if(null != supplier && null != supplier.getId()){
            query.setParameter("supplier" , supplier.getId());
        }

        if(null != type){
            query.setParameter("type" , type.ordinal());
        }

        return query.getResultList();
    }

    /**
     * @param supplier   接受员所属企业
     * @param bySupplier 哪些企业下单
     * @param type
     * @return
     */
    @Override
    public List<NoticeUser> findList(Supplier supplier, Supplier bySupplier, NoticeType.Type type) {
        StringBuffer findSql = new StringBuffer("select noticeUser.* from t_notice_user noticeUser") ;
        findSql.append(" inner join t_notice_type noticeType on noticeUser.id=noticeType.notice_user");
        findSql.append(" inner join t_notice_user_supplier noticeUserSupplier on noticeUser.id=noticeUserSupplier.notice_user");

        findSql.append(" where 1=1");
        if(null != supplier && null != supplier.getId()){
            findSql.append(" and noticeUser.supplier=:supplier");
        }
        if(null != type){
            findSql.append(" and noticeType.type=:type");
        }

        if(null != bySupplier){
            findSql.append(" and noticeUserSupplier.supplier=:bySupplier");
        }

        Query query = entityManager.createNativeQuery(findSql.toString() , NoticeUser.class);
        if(null != supplier && null != supplier.getId()){
            query.setParameter("supplier" , supplier.getId());
        }

        if(null != type){
            query.setParameter("type" , type.ordinal());
        }

        if(null != bySupplier){
            query.setParameter("bySupplier" , bySupplier.getId());
        }

        return query.getResultList();
    }

    /**
     * @param supplier 接受员所属企业
     * @param need     接受员所属企业下的收货点
     * @param type
     * @return
     */
    @Override
    public List<NoticeUser> findList(Supplier supplier, Need need, NoticeTypePurchase.Type type) {
        StringBuffer findSql = new StringBuffer("select noticeUser.* from t_notice_user noticeUser") ;
        findSql.append(" inner join t_notice_type_purchase purchaseNoticeType on noticeUser.id=purchaseNoticeType.notice_user");
        findSql.append(" inner join t_notice_user_need noticeUserNeed on noticeUser.id=noticeUserNeed.notice_user");

        findSql.append(" where 1=1");
        if(null != supplier && null != supplier.getId()){
            findSql.append(" and noticeUser.supplier=:supplier");
        }
        if(null != type){
            findSql.append(" and purchaseNoticeType.type=:type");
        }

        if(null != need){
            findSql.append(" and noticeUserNeed.need=:byNeed");
        }

        Query query = entityManager.createNativeQuery(findSql.toString() , NoticeUser.class);
        if(null != supplier && null != supplier.getId()){
            query.setParameter("supplier" , supplier.getId());
        }

        if(null != type){
            query.setParameter("type" , type.ordinal());
        }

        if(null != need){
            query.setParameter("byNeed" , need.getId());
        }

        return query.getResultList();
    }

    /**
     * 订货单中 对个体客户的 消息接受员
     *
     * @param supplier
     * @param need
     * @param type
     * @return
     */
    @Override
    public List<NoticeUser> findList(Supplier supplier, Need need, NoticeType.Type type) {
        StringBuffer findSql = new StringBuffer("select noticeUser.* from t_notice_user noticeUser") ;
        findSql.append(" inner join t_notice_type noticeType on noticeUser.id=noticeType.notice_user");
        findSql.append(" inner join t_notice_user_order_need noticeUserOrderNeed on noticeUser.id=noticeUserOrderNeed.notice_user");

        findSql.append(" where 1=1");
        if(null != supplier && null != supplier.getId()){
            findSql.append(" and noticeUser.supplier=:supplier");
        }
        if(null != type){
            findSql.append(" and noticeType.type=:type");
        }

        if(null != need){
            findSql.append(" and noticeUserOrderNeed.need=:byNeed");
        }

        Query query = entityManager.createNativeQuery(findSql.toString() , NoticeUser.class);
        if(null != supplier && null != supplier.getId()){
            query.setParameter("supplier" , supplier.getId());
        }

        if(null != type){
            query.setParameter("type" , type.ordinal());
        }

        if(null != need){
            query.setParameter("byNeed" , need.getId());
        }

        return query.getResultList();
    }
}
