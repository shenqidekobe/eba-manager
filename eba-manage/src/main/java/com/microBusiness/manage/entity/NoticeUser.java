package com.microBusiness.manage.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by mingbai on 2017/3/28.
 * 功能描述：
 * 修改记录：
 */
@Entity
@Table(name = "t_notice_user")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_notice_user")
public class NoticeUser extends BaseEntity<Long> {

    private static final long serialVersionUID = 2616766458770754829L;

    /**
     * 微信同步过来的用户昵称
     */
    private String nickName ;

    private Date bindDate ;

    private Supplier supplier ;

    private List<NoticeType> noticeTypes = new ArrayList<NoticeType>();

    private String openId ;
    //是否全部企业
    private Boolean isAllSupplier ;
    //是否全部收货点
    private Boolean isAllNeed ;

    private List<NoticeTypePurchase> noticeTypePurchases = new ArrayList<>() ;

    //采购单需要通知的个体客户
    List<NoticeUserNeed> noticeUserNeeds = new ArrayList<>();

    List<NoticeUserSupplier> noticeUserSuppliers = new ArrayList<>() ;
    //订货单需要通知的个体客户
    List<NoticeUserOrderNeed> noticeUserOrderNeeds = new ArrayList<>() ;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Date getBindDate() {
        return bindDate;
    }

    public void setBindDate(Date bindDate) {
        this.bindDate = bindDate;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    @OneToMany(mappedBy = "noticeUser", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public List<NoticeType> getNoticeTypes() {
        return noticeTypes;
    }

    public void setNoticeTypes(List<NoticeType> noticeTypes) {
        this.noticeTypes = noticeTypes;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Boolean getAllSupplier() {
        return isAllSupplier;
    }

    public void setAllSupplier(Boolean allSupplier) {
        isAllSupplier = allSupplier;
    }

    public Boolean getAllNeed() {
        return isAllNeed;
    }

    public void setAllNeed(Boolean allNeed) {
        isAllNeed = allNeed;
    }

    @OneToMany(mappedBy = "noticeUser", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public List<NoticeTypePurchase> getNoticeTypePurchases() {
        return noticeTypePurchases;
    }

    public void setNoticeTypePurchases(List<NoticeTypePurchase> noticeTypePurchases) {
        this.noticeTypePurchases = noticeTypePurchases;
    }

    @OneToMany(mappedBy = "noticeUser", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public List<NoticeUserNeed> getNoticeUserNeeds() {
        return noticeUserNeeds;
    }

    public void setNoticeUserNeeds(List<NoticeUserNeed> noticeUserNeeds) {
        this.noticeUserNeeds = noticeUserNeeds;
    }

    @OneToMany(mappedBy = "noticeUser", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public List<NoticeUserSupplier> getNoticeUserSuppliers() {
        return noticeUserSuppliers;
    }

    public void setNoticeUserSuppliers(List<NoticeUserSupplier> noticeUserSuppliers) {
        this.noticeUserSuppliers = noticeUserSuppliers;
    }

    @OneToMany(mappedBy = "noticeUser", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public List<NoticeUserOrderNeed> getNoticeUserOrderNeeds() {
        return noticeUserOrderNeeds;
    }

    public void setNoticeUserOrderNeeds(List<NoticeUserOrderNeed> noticeUserOrderNeeds) {
        this.noticeUserOrderNeeds = noticeUserOrderNeeds;
    }
}
