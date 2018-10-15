package com.microBusiness.manage.entity;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 功能描述：个体客户供应关系表
 * 修改记录：
 */
@Entity
@Table(name = "t_supply_need")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_supply_need")
public class SupplyNeed extends BaseEntity<Long> {

    private static final long serialVersionUID = -2402658118215362474L;

    /**
     * 建立供应关系的品牌企业
     */
    private Supplier supplier ;

    /**
     * 个体客户
     */
    private Need need ;

    public enum  AssignedModel {
    	STRAIGHT, // 直销
    	BRANCH    // 分销
    }
    
    private AssignedModel assignedModel;

    private Boolean openNotice;

    private Integer noticeDay ;
    
    public enum  Status {
    	SUPPLY,		//供应中
        STOP,		//暂停
        EXPIRED,	 //已过期
        WILLSUPPLY   //未开始
    }

    private Status status ;
    /**
     * 品牌企业和个体客户产生供应的商品关系
     */
    private Set<NeedProduct> needProducts;

    /**
     * 供应开始时间
     */
    private Date startDate;
    /**
     * 供应结束时间
     */
    private Date endDate;

    @ManyToOne
    @JoinColumn(name="supplier")
    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    @ManyToOne
    @JoinColumn(name="need")
    public Need getNeed() {
        return need;
    }

    public void setNeed(Need need) {
        this.need = need;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @OneToMany(mappedBy = "supplyNeed", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    public Set<NeedProduct> getNeedProducts() {
        return needProducts;
    }

    public void setNeedProducts(Set<NeedProduct> needProducts) {
        this.needProducts = needProducts;
    }

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public AssignedModel getAssignedModel() {
		return assignedModel;
	}

	public void setAssignedModel(AssignedModel assignedModel) {
		this.assignedModel = assignedModel;
	}

	public Boolean getOpenNotice() {
		return openNotice;
	}

	public void setOpenNotice(Boolean openNotice) {
		this.openNotice = openNotice;
	}

	public Integer getNoticeDay() {
		return noticeDay;
	}

	public void setNoticeDay(Integer noticeDay) {
		this.noticeDay = noticeDay;
	}
	
}
