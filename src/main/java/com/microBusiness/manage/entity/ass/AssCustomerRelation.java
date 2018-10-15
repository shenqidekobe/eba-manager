package com.microBusiness.manage.entity.ass;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Where;

import com.microBusiness.manage.entity.Area;
import com.microBusiness.manage.entity.BaseEntity;
import com.microBusiness.manage.entity.Supplier;

/**
 * 功能描述：助手供应商 修改记录：
 */
@Entity
@Table(name = "ass_customer_relation")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_ass_customer_relation")
public class AssCustomerRelation extends BaseEntity<Long> {

	private static final long serialVersionUID = 5076733002910843591L;
	
	// 供应商名称
	private String clientName;
	private String oldClientName;
	// 供应商编号
	private String clientNum;
	// 供应商地区
	private Area area;
	// 详细地址
	private String address;
	// 联系人
	private String userName;
	// 联系方式
	private String tel;
	// 电子邮箱
	private String email;
	// 开户名称
	private String accountName;
	// 发票抬头
	private String invoice;
	// 开户银行
	private String bank;
	// 银行账号
	private String bankAccountNum;
	// 本企业
	private Supplier supplier;
	//主题
	private String theme;
	//简介
	private String profiles;
	
	private AssChildMember assChildMember;
	
	private SourceType sourceType;
	
	private AssCustomerRelation source;

	private ShareType shareType;
	
	//同步的名片
	private AssCard assCard;
	
	public enum ShareType{
		share,
		noshare
	}

	/**
     * 来源类型
     */
    public enum SourceType{
    	//从后台同步
        syncBackstage,
		//前端之前目录
		MOBILE
    }
    
    /**
     * 分享类型
     */
    public enum Type{
    	single,   //单商品分享
    	many,	  //目录分享
    	card      //名片分享
    }
    
    private Type type;
    
    //未分享复制次数
    private int numberOfCopy;
    
    private String sn;
    
    private List<AssGoods> assGoods=new ArrayList<>();
    
    private AssGoodDirectory assGoodDirectory;
    //员工名称   由于后台统计需要查询删除过的员工数据，所以目录这边需要保存一个员工名称
    private String adminName;
    
	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getClientNum() {
		return clientNum;
	}

	public void setClientNum(String clientNum) {
		this.clientNum = clientNum;
	}

    @OneToOne(fetch = FetchType.LAZY)
	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getInvoice() {
		return invoice;
	}

	public void setInvoice(String invoice) {
		this.invoice = invoice;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getBankAccountNum() {
		return bankAccountNum;
	}

	public void setBankAccountNum(String bankAccountNum) {
		this.bankAccountNum = bankAccountNum;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="supplier" , nullable = true, updatable = false , foreignKey = @ForeignKey(name = "null"))
	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, updatable = false , foreignKey = @ForeignKey(name = "null"))
	public AssChildMember getAssChildMember() {
		return assChildMember;
	}

	public void setAssChildMember(AssChildMember assChildMember) {
		this.assChildMember = assChildMember;
	}

	public SourceType getSourceType() {
		return sourceType;
	}

	public void setSourceType(SourceType sourceType) {
		this.sourceType = sourceType;
	}

	@Transient
	public String getOldClientName() {
		return oldClientName;
	}

	public void setOldClientName(String oldClientName) {
		this.oldClientName = oldClientName;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public String getProfiles() {
		return profiles;
	}

	public void setProfiles(String profiles) {
		this.profiles = profiles;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public AssCustomerRelation getSource() {
		return source;
	}

	public void setSource(AssCustomerRelation source) {
		this.source = source;
	}

	public ShareType getShareType() {
		return shareType;
	}

	public void setShareType(ShareType shareType) {
		this.shareType = shareType;
	}

	public int getNumberOfCopy() {
		return numberOfCopy;
	}

	public void setNumberOfCopy(int numberOfCopy) {
		this.numberOfCopy = numberOfCopy;
	}
	
	@OneToMany(mappedBy = "assCustomerRelation", fetch = FetchType.LAZY ,cascade = CascadeType.REMOVE)
	@Where(clause="deleted=0")
	@OrderBy("createDate desc")
	public List<AssGoods> getAssGoods() {
		return assGoods;
	}

	public void setAssGoods(List<AssGoods> assGoods) {
		this.assGoods = assGoods;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public AssCard getAssCard() {
		return assCard;
	}

	public void setAssCard(AssCard assCard) {
		this.assCard = assCard;
	}
	@Column(updatable = true)
	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public AssGoodDirectory getAssGoodDirectory() {
		return assGoodDirectory;
	}

	public void setAssGoodDirectory(AssGoodDirectory assGoodDirectory) {
		this.assGoodDirectory = assGoodDirectory;
	}

	public String getAdminName() {
		return adminName;
	}

	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}
	

}
