package com.microBusiness.manage.entity;

import com.microBusiness.manage.util.DateUtils;

import org.apache.shiro.util.CollectionUtils;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by mingbai on 2017/1/22.
 * 功能描述：个体的客户关系表
 * 修改记录：
 */
@Entity
@Table(name = "t_need")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_need")
public class Need extends BaseEntity<Long> {
    private static final long serialVersionUID = -7295096738098007187L;

    private String name ;

    private String userName ;

    private String tel ;

    private Area area ;

    private String address ;

    private Supplier supplier ;
    /**
     * 供应商给收货点的商品
     */
    //private Set<Product> products ;

    private String description ;

    //private Set<Member> members ; 分销版本失效的关系
    
    /**
     * 一个账号对应多个个体客户信息
     */
    private Member member;

    /**
     * 临时供应关联 收货点供应商
     */
    //private Set<Supplier> needSuppliers ;
    private Set<SupplyNeed> needSuppliers ;
    
    public enum Status{
    	on,
    	off
    }
    private Status status=Status.off;

    /**
     * 收货点类型
     */
    public enum Type{
        //普通收货点
        general("普通个体客户"),
        //流水收货点
        turnover("流水个体客户");
        private String desc ;

        Type(String desc) {
            this.desc = desc ;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }

    private Type type = Type.general ;
    
    public enum NeedStatus{
    	//可用
    	available,
    	//暂停
    	suspend
    }
    
    private SourceType sourceType;
    
    //收货点状态
    private NeedStatus needStatus = NeedStatus.available;
    
    private Set<SupplierAssignRelation> supplierAssignRelations;
    
    private Set<OrderNewsPush> orderNewsPushs;

    // 客户编号
 	private String clientNum;
    
    // 联系人
 	private String contacts;
 	// 联系方式
 	private String contactsTel;
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
 	// 纳税人识别码
 	private String identifier;
 	//所属员工
 	private Admin admin;
 	//店铺类型
 	private ShopType shopType;
 	
 	private List<Shop> shops=new ArrayList<>();
 	//收货人手机号
 	private String receiverTel;
 	
    @NotEmpty
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @NotEmpty
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @NotEmpty
    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    @NotNull
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

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, updatable = true)
    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, updatable = true)
    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    /*@ManyToMany(mappedBy = "needs" , fetch = FetchType.LAZY)
    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }*/


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


//    @OneToMany(mappedBy = "need", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
//    public Set<Member> getMembers() {
//        return members;
//    }
//
//    public void setMembers(Set<Member> members) {
//        this.members = members;
//    }

    @OneToMany(mappedBy = "need", fetch = FetchType.LAZY)
    public Set<SupplyNeed> getNeedSuppliers() {

        return needSuppliers;
    }

    public void setNeedSuppliers(Set<SupplyNeed> needSuppliers) {
        this.needSuppliers = needSuppliers;
    }

    @Transient
    public String getHiddenTel() {
        return tel.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }

    @Transient
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

    /*@Transient
	public Member getMember(){
        return this.getMembers().iterator().next() ;
    }*/


    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
    
    @Transient
    public SupplyNeed getSupplyNeedBySupply(){
    	SupplyNeed supplyNeed=null;
    	for (SupplyNeed supplyNeeds : getNeedSuppliers()) {
    		if (supplyNeeds.getStatus().equals(SupplyNeed.Status.SUPPLY)) {
    			supplyNeed=supplyNeeds;
    			break;
			}
		}
    	return supplyNeed;
    }
    @Transient
	public int isOver() throws ParseException{
    	int count = 0;
    	Date startTime = DateUtils.currentStartTime();
    	Date endTime = DateUtils.currentEndTime();
    	//for(Member member:this.members){
    		Set<Order> orders = member.getOrders() ;
    		if(CollectionUtils.isEmpty(orders)){
    			//break ;
    			return count;
    		}
    		for(Order order:orders){
    			Date orderDate =  order.getCreateDate();
    			if(orderDate.getTime() < startTime.getTime() || orderDate.getTime() > endTime.getTime()) {
					continue;
				}
    			if(!(order.getStatus() == Order.Status.canceled || order.getStatus() == Order.Status.denied)) {
    				count++;
    			}
    		}
    	//}
    	
    	return count ;
    }

	public NeedStatus getNeedStatus() {
		return needStatus;
	}

	public void setNeedStatus(NeedStatus needStatus) {
		this.needStatus = needStatus;
	}

	@OneToMany(mappedBy = "need", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	public Set<SupplierAssignRelation> getSupplierAssignRelations() {
		return supplierAssignRelations;
	}

	public void setSupplierAssignRelations(
			Set<SupplierAssignRelation> supplierAssignRelations) {
		this.supplierAssignRelations = supplierAssignRelations;
	}

	@OneToMany(mappedBy = "need", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	public Set<OrderNewsPush> getOrderNewsPushs() {
		return orderNewsPushs;
	}

	public void setOrderNewsPushs(Set<OrderNewsPush> orderNewsPushs) {
		this.orderNewsPushs = orderNewsPushs;
	}

	public String getClientNum() {
		return clientNum;
	}

	public void setClientNum(String clientNum) {
		this.clientNum = clientNum;
	}

	public String getContacts() {
		return contacts;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts;
	}

	public String getContactsTel() {
		return contactsTel;
	}

	public void setContactsTel(String contactsTel) {
		this.contactsTel = contactsTel;
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

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="admin" , nullable = true)
	public Admin getAdmin() {
		return admin;
	}

	public void setAdmin(Admin admin) {
		this.admin = admin;
	}

	public SourceType getSourceType() {
		return sourceType;
	}

	public void setSourceType(SourceType sourceType) {
		this.sourceType = sourceType;
	}

	@ManyToMany(mappedBy="needs")
	public List<Shop> getShops() {
		return shops;
	}

	public void setShops(List<Shop> shops) {
		this.shops = shops;
	}

	public ShopType getShopType() {
		return shopType;
	}

	public void setShopType(ShopType shopType) {
		this.shopType = shopType;
	}

	public String getReceiverTel() {
		return receiverTel;
	}

	public void setReceiverTel(String receiverTel) {
		this.receiverTel = receiverTel;
	}
	
}
