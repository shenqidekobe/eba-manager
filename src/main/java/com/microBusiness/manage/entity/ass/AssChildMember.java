package com.microBusiness.manage.entity.ass;


import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.microBusiness.manage.entity.Admin;
import com.microBusiness.manage.entity.BaseEntity;
import com.microBusiness.manage.entity.Member;

import org.hibernate.annotations.Where;

/**
 * 功能描述：分享助手用户子账号实体
 * 修改记录：
 */
@Entity
@Table(name = "ass_child_member")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_ass_child_member")
public class AssChildMember extends BaseEntity<Long> {

	private static final long serialVersionUID = 1242372496540309897L;

	private String openId ;

    private Member member ;

    private String nickName ;

    private String headImgUrl ;

    private String unionId ;

    private String smOpenId;

    private List<AssForm> assForm;
    
    private List<AssShippingAddress> assShippingAddress;
    
    private Admin admin;
    
    /**
     * 来源类型
     */
    public enum SourceType{
        //公众号
        wx_public,
        //微信小程序
        wx_small
    }
    
    /**
     * 绑定标识
     */
    public enum BindFlag{
    	bind,
    	unbind  
    }

    private SourceType sourceType ;
    
    private BindFlag bindFlag ;
    
    private AssCart assCart;
    
    // 姓名
    private String name;
    
    // 电话
    private String phone;
    
    // 公司名称
    private String companyName;
    
    // 职位
    private String 	position;
    
    // 邮箱
    private String email;
    
    // 微信号
    private String wxNum;
    
    private List<AssCard> assCards;
    
    public String getOpenId() {
        return openId;
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

    @OneToOne(mappedBy = "assChildMember", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	public AssCart getAssCart() {
		return assCart;
	}

	public void setAssCart(AssCart assCart) {
		this.assCart = assCart;
	}

	public String getSmOpenId() {
		return smOpenId;
	}

	public void setSmOpenId(String smOpenId) {
		this.smOpenId = smOpenId;
	}

	public BindFlag getBindFlag() {
		return bindFlag;
	}

	public void setBindFlag(BindFlag bindFlag) {
		this.bindFlag = bindFlag;
	}

	@OneToMany(mappedBy = "assChildMember", fetch = FetchType.LAZY ,cascade = CascadeType.REMOVE)
	@Where(clause="deleted=0")
	public List<AssShippingAddress> getAssShippingAddress() {
		return assShippingAddress;
	}

	public void setAssShippingAddress(List<AssShippingAddress> assShippingAddress) {
		this.assShippingAddress = assShippingAddress;
	}

	@OneToMany(mappedBy = "assChildMember", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@OrderBy("createDate desc")
	public List<AssForm> getAssForm() {
		return assForm;
	}

	public void setAssForm(List<AssForm> assForm) {
		this.assForm = assForm;
	}

	@Transient
	public AssForm getAssFormOne() {
		List<AssForm> assForms = this.getAssForm();
		for (AssForm assForm : assForms) {
			return assForm;
		}
		
		return null;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getWxNum() {
		return wxNum;
	}

	public void setWxNum(String wxNum) {
		this.wxNum = wxNum;
	}

	@OneToMany(mappedBy = "assChildMember", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	public List<AssCard> getAssCards() {
		return assCards;
	}

	public void setAssCards(List<AssCard> assCards) {
		this.assCards = assCards;
	}

	@OneToOne(fetch = FetchType.LAZY)
	public Admin getAdmin() {
		return admin;
	}

	public void setAdmin(Admin admin) {
		this.admin = admin;
	}

	@Transient
	public AssCard getAssCard() {
		List<AssCard> assCards = this.getAssCards();
		for (AssCard assCard : assCards) {
			if (assCard.getShareType() == AssCard.ShareType.noshare && assCard.isDeleted() == false) {
				return assCard;
			}
		}
		
		return null;
	}
}
