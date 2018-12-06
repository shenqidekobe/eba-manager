package com.microBusiness.manage.entity;


import java.math.BigDecimal;
import java.util.List;

import javax.persistence.*;

/**
 * Created by mingbai on 2017/2/11.
 * 功能描述：用户子账号实体
 * 修改记录：
 */
@Entity
@Table(name = "t_child_member")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_child_member")
public class ChildMember extends BaseEntity<Long> {

    private String openId ;

    private Member member ;

    private String nickName ;

    private String headImgUrl ;

    private String unionId ;
    
    private String smOpenId;

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
    
    /**
     * 是否认证
     */
    private Boolean isChecked;
    
    
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

}
