package com.microBusiness.manage.entity.ass;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.apache.commons.lang.RandomStringUtils;

import com.microBusiness.manage.entity.BaseEntity;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.Need;
import com.microBusiness.manage.entity.Supplier;

/**
 *	清单实体
 * @author pengtianwen
 *
 */
@Entity
@Table(name = "ass_list")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_list")
public class AssList  extends BaseEntity<Long> {

	private static final long serialVersionUID = -1125092655269728167L;
	
	public enum Type{
		self,//前端用户自己创建的供应商下的单
		supplier //绑定过后，企业端创建的供应商下的单
	}
	
	public enum Status{
		noshare, //未分享
		share,	 //已分享
		end		 //已终结
	}
	
	//下单类型
	public enum BuyType{
		//正常下单
		general,
		//代下单
		substitute,
	}
	private Type type;
	
	private String sn;
	//状态
	private Status status;
	//下单类型
	private BuyType buyType;
	//下单人
	private String singlePerson;
	//商品数量
	private Integer quantity;
	//清单所属企业
	private Supplier supplier;
	//供应商名称
	private String clientName;
	//收货点名称
	private String addressName;
	//收货人名称
	private String name;
	//收货人手机号
	private String tel;
	//收货地址
	private String address;
	
	//供应商
	private AssCustomerRelation assCustomerRelation;
	//个体客户
	private Need need;
	//微信端用户管理的收货地址
	private AssShippingAddress assShippingAddress;
	//微信账号
	private AssChildMember assChildMember;
	
	private Member member;
	
	private List<AssListItem> assListItems=new ArrayList<>();
	
	private List<AssListRelation> assListRelations=new ArrayList<>();
	
	private List<AssListLog> assListLogs=new ArrayList<>();
	
	
	@Column(nullable = false, updatable = false, unique = true)
	public String getSn() {
		return sn;
	}
	
	public void setSn(String sn) {
		this.sn = sn;
	}
	
	@Column(nullable = false, updatable = false)
	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	@Column(nullable = false)
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	@Column(nullable = false)
	public BuyType getBuyType() {
		return buyType;
	}

	public void setBuyType(BuyType buyType) {
		this.buyType = buyType;
	}

	public String getSinglePerson() {
		return singlePerson;
	}

	public void setSinglePerson(String singlePerson) {
		this.singlePerson = singlePerson;
	}

	@Column(nullable = false, updatable = true)
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, updatable = false)
	public AssCustomerRelation getAssCustomerRelation() {
		return assCustomerRelation;
	}

	public void setAssCustomerRelation(AssCustomerRelation assCustomerRelation) {
		this.assCustomerRelation = assCustomerRelation;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, updatable = false)
	public Need getNeed() {
		return need;
	}

	public void setNeed(Need need) {
		this.need = need;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, updatable = false)
	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "assShippingAddress", nullable = true, updatable = false)
	public AssShippingAddress getAssShippingAddress() {
		return assShippingAddress;
	}

	public void setAssShippingAddress(AssShippingAddress assShippingAddress) {
		this.assShippingAddress = assShippingAddress;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "assChildMember", nullable = true, updatable = false)
	public AssChildMember getAssChildMember() {
		return assChildMember;
	}

	public void setAssChildMember(AssChildMember assChildMember) {
		this.assChildMember = assChildMember;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, updatable = false)
	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}
	
	@OneToMany(mappedBy = "assList", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@OrderBy("createDate asc")
	public List<AssListItem> getAssListItems() {
		return assListItems;
	}

	public void setAssListItems(List<AssListItem> assListItems) {
		this.assListItems = assListItems;
	}
	
	@OneToMany(mappedBy = "assList", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@OrderBy("createDate asc")
	public List<AssListRelation> getAssListRelations() {
		return assListRelations;
	}

	public void setAssListRelations(List<AssListRelation> assListRelations) {
		this.assListRelations = assListRelations;
	}

	@OneToMany(mappedBy = "assList", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@OrderBy("createDate asc")
	public List<AssListLog> getAssListLogs() {
		return assListLogs;
	}

	public void setAssListLogs(List<AssListLog> assListLogs) {
		this.assListLogs = assListLogs;
	}
	
	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAddressName() {
		return addressName;
	}

	public void setAddressName(String addressName) {
		this.addressName = addressName;
	}

	/**
	 * 根据assChildMember查询是否参与此清单
	 * @param assChildMember
	 * @return
	 */
	@Transient
	public boolean participate(AssChildMember assChildMember){
		List<AssListRelation> assListRelations=getAssListRelations();
		if (assListRelations != null) {
			for (AssListRelation assListRelation : assListRelations) {
				if (assListRelation.getType().equals(AssListRelation.Type.participant)) {
					if (assChildMember != null && assChildMember.equals(assListRelation.getAssChildMember())) {
						return true;
					}
				}
			}
		}else {
			return false;
		}
		return false;
	}
	@Transient
	public String generateSn() {
		Date date=new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyMMdd");
		String sn="Z"+formatter.format(date)+RandomStringUtils.randomNumeric(6);
		return sn;
	}
}
