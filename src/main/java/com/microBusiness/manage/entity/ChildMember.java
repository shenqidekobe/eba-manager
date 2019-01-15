package com.microBusiness.manage.entity;


import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 小程序会员用户记录
 */
@Entity
@Table(name = "t_child_member")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_child_member")
public class ChildMember extends BaseEntity<Long> {

	private static final long serialVersionUID = 1448553091140929172L;
	
	//会员等级
	public enum Member_Rank {
		common("普通"),
		platina("白金"),
		platinum("铂金"),
		blackplatinum("黑金");
		public String label;
		Member_Rank(String label){
			this.label=label;
		}
		public String getLabel(){
			return label;
		}
	}

	private String openId;
    private Member member;
    private String nickName;
    private String headImgUrl;
    private String unionId;
    private String smOpenId;
    private String phone;

    /*** 扩展属性  ***/
    private Boolean isChecked;//是否认证
    private Boolean isShoper;//是否会员
    private Member_Rank rank;
    
    private Integer buyNum;//自己购买数量
    private Integer subBuyNum;//下级购买数量
    private Integer subSubBuyNum;//下下级购买订单数量
    private BigDecimal buyAmount;//自己购买订单总金额累计
    private BigDecimal subBuyAmount;//下级购买订单总金额累计
    private BigDecimal subSubBuyAmount;//下级下级购买订单总金额累计
    
    private Integer totalBuyNum;//累计销售数量(自购-下级购-下下级购)
    private BigDecimal totalBuyAmount;//购买订单总金额累计(自购-下级购-下下级购)
    
    private Date platinaTime;//成为白金时间
    private Date platinumTime;//成为铂金时间
    private Date blackplatinumTime;//成为黑金时间

    private String remark;
    
    
    /**
     * 来源类型
     */
    public enum SourceType{
        //公众号
        wx_public,
        //微信小程序
        wx_small
    }

    private SourceType sourceType ;
    
    private List<OrderForm> orderForms;
    
    private ChildMember parent;
    
    
    public Boolean getIsChecked() {
		return isChecked;
	}

	public void setIsChecked(Boolean isChecked) {
		this.isChecked = isChecked;
	}
	public String getOpenId() {
        return openId;
    }

    @ManyToOne
    public ChildMember getParent() {
		return parent;
	}

	public void setParent(ChildMember parent) {
		this.parent = parent;
	}

	public void setOpenId(String openId) {
        this.openId = openId;
    }

    @ManyToOne
    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public String getNickName() {
        return nickName ;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName ;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
    public String getUnionId() {
        return unionId;
    }
    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }
    public SourceType getSourceType() {
        return sourceType;
    }
    public void setSourceType(SourceType sourceType) {
        this.sourceType = sourceType;
    }
	public String getSmOpenId() {
		return smOpenId;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Boolean getIsShoper() {
		return isShoper;
	}
	public void setIsShoper(Boolean isShoper) {
		this.isShoper = isShoper;
	}
	public Integer getSubBuyNum() {
		return subBuyNum;
	}
	public void setSubBuyNum(Integer subBuyNum) {
		this.subBuyNum = subBuyNum;
	}
	public Member_Rank getRank() {
		return rank;
	}
	public void setRank(Member_Rank rank) {
		this.rank = rank;
	}
	public Integer getTotalBuyNum() {
		return totalBuyNum;
	}

	public void setTotalBuyNum(Integer totalBuyNum) {
		this.totalBuyNum = totalBuyNum;
	}

	public Integer getBuyNum() {
		return buyNum;
	}
	public void setBuyNum(Integer buyNum) {
		this.buyNum = buyNum;
	}
	public Integer getSubSubBuyNum() {
		return subSubBuyNum;
	}
	public void setSubSubBuyNum(Integer subSubBuyNum) {
		this.subSubBuyNum = subSubBuyNum;
	}
	public BigDecimal getBuyAmount() {
		return buyAmount;
	}
	public void setBuyAmount(BigDecimal buyAmount) {
		this.buyAmount = buyAmount;
	}
	public BigDecimal getSubBuyAmount() {
		return subBuyAmount;
	}
	public void setSubBuyAmount(BigDecimal subBuyAmount) {
		this.subBuyAmount = subBuyAmount;
	}
	public BigDecimal getSubSubBuyAmount() {
		return subSubBuyAmount;
	}
	public void setSubSubBuyAmount(BigDecimal subSubBuyAmount) {
		this.subSubBuyAmount = subSubBuyAmount;
	}
	public BigDecimal getTotalBuyAmount() {
		return totalBuyAmount;
	}
	public void setTotalBuyAmount(BigDecimal totalBuyAmount) {
		this.totalBuyAmount = totalBuyAmount;
	}
	public Date getPlatinaTime() {
		return platinaTime;
	}

	public void setPlatinaTime(Date platinaTime) {
		this.platinaTime = platinaTime;
	}

	public Date getPlatinumTime() {
		return platinumTime;
	}

	public void setPlatinumTime(Date platinumTime) {
		this.platinumTime = platinumTime;
	}

	public Date getBlackplatinumTime() {
		return blackplatinumTime;
	}

	public void setBlackplatinumTime(Date blackplatinumTime) {
		this.blackplatinumTime = blackplatinumTime;
	}

	@OneToMany(mappedBy = "childMember", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@OrderBy("createDate desc")
	public List<OrderForm> getOrderForms() {
		return orderForms;
	}

	public void setOrderForms(List<OrderForm> orderForms) {
		this.orderForms = orderForms;
	}

	@OneToMany(mappedBy = "childMember", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@OrderBy("createDate desc")
	public void setSmOpenId(String smOpenId) {
		this.smOpenId = smOpenId;
	}

	@Transient
	public OrderForm getOrderFormOne() {
		List<OrderForm> orderForms = this.getOrderForms();
		if(orderForms==null)return null;
		for(OrderForm form : orderForms) {
			return form;
		}
		return null;
	}

	@Override
    public String toString() {
        return "ChildMember{" +
                "openId='" + openId + '\'' +
                ", member=" + member +
                ", nickName='" + nickName + '\'' +
                ", headImgUrl='" + headImgUrl + '\'' +
                ", unionId='" + unionId + '\'' +
                '}';
    }
}
