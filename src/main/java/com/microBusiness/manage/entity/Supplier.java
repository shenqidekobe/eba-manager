package com.microBusiness.manage.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.joda.time.DateTime;
import org.joda.time.Days;

/**
 * Created by mingbai on 2017/1/22.
 * 功能描述：供应商实体类
 * 修改记录：
 */
@Entity
@Table(name = "t_supplier")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_supplier")
public class Supplier extends BaseEntity<Long> {
    private static final long serialVersionUID = -1778985981185870363L;

    public enum Status{
    	notCertified,//未认证
    	authenticationFailed,//认证失败
    	certification,//认证中
    	verified//已认证
    }
    
    public enum Industry{
    	foot,
    	restaurant,
    	agricultural,
    	riyongbaihuo,
    	car,
    	office,
    	book,
    	electronic,
    	electromechanical,
    	digitalGoods,
    	householdAppliances,
    	cosmetic,
    	jewelry,
    	toys,
    	furniture,
    	apparel,
    	communicationEquipment,
    	training,
    	healthcareSupplies,
    	medicine,
    	motherAndChild,
    	WatchesAndClocks,
    	otherIndustry
    }
    
    private String name;

    private String userName;

    private String tel ;

    private Area area ;

    private String address;
    
    private Status status;
    
    private String businessCard;
    
    private String reasons;

    private Set<Admin> admins ;

    private Set<Need> needs ;

    private Set<SupplyNeed> supplyNeeds ;

    private Set<Goods> goods ;

    private Set<Order> orders ;
    
    private Set<Role> roles;
    /**
     * 临时供应关联的收货点
     */
    //private Set<Need> supplyNeeds;

    //所属行业
    private Industry industry ;
    
    //logo图片
    private String imagelogo;
    
    //电子邮箱
    private String email;
    
    //公司简介
    private String companyProfile;
    
    private OrderSetting orderSetting;

    //邀请码
    private String inviteCode ;
    //客服电话
    private String customerServiceTel;
    //推荐企业
    private Boolean recommendFlag;
    //法人姓名
    private String legalPersonName;

    private FavorCompany favorCompany;
    
    //qq客服
    private String qqCustomerService;

    //试用天数
    private int probationDays = 60 ;

    //系统设置
    private SystemSetting systemSetting ;

    // 企业接口key
    private String appId;
    
    // 密匙
    private String appKey;
    
    private Member member;
    
    private Types types;
    
    @Email
    @Length(max = 200)
    public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	@Lob
	public String getCompanyProfile() {
		return companyProfile;
	}

	public void setCompanyProfile(String companyProfile) {
		this.companyProfile = companyProfile;
	}

	public String getImagelogo() {
		return imagelogo;
	}

	public void setImagelogo(String imagelogo) {
		this.imagelogo = imagelogo;
	}

	public Industry getIndustry() {
		return industry;
	}

	public void setIndustry(Industry industry) {
		this.industry = industry;
	}
	private Set<ProductCategory> productCategories ;

    private Set<Specification> specifications ;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getBusinessCard() {
		return businessCard;
	}

	public void setBusinessCard(String businessCard) {
		this.businessCard = businessCard;
	}

	public String getReasons() {
		return reasons;
	}

	public void setReasons(String reasons) {
		this.reasons = reasons;
	}

	@OneToMany(mappedBy = "supplier", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    public Set<Admin> getAdmins() {
        return admins;
    }

    public void setAdmins(Set<Admin> admins) {
        this.admins = admins;
    }

    @OneToMany(mappedBy = "supplier", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    public Set<Need> getNeeds() {
        return needs;
    }

    public void setNeeds(Set<Need> needs) {
        this.needs = needs;
    }

    @OneToMany(mappedBy = "supplier", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    public Set<Goods> getGoods() {
        return goods;
    }

    public void setGoods(Set<Goods> goods) {
        this.goods = goods;
    }

    @OneToMany(mappedBy = "supplier" , cascade = CascadeType.REMOVE)
    public Set<SupplyNeed> getSupplyNeeds() {
        return supplyNeeds;
    }

    public void setSupplyNeeds(Set<SupplyNeed> supplyNeeds) {
        this.supplyNeeds = supplyNeeds;
    }

    @OneToMany(mappedBy = "supplier" , cascade = CascadeType.REMOVE)
    public Set<Order> getOrders() {
        return orders;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }

    @OneToMany(mappedBy = "supplier" , cascade = CascadeType.REMOVE)
    public Set<ProductCategory> getProductCategories() {
        return productCategories;
    }

    public void setProductCategories(Set<ProductCategory> productCategories) {
        this.productCategories = productCategories;
    }

    @OneToMany(mappedBy = "supplier" , cascade = CascadeType.REMOVE)
    public Set<Specification> getSpecifications() {
        return specifications;
    }

    public void setSpecifications(Set<Specification> specifications) {
        this.specifications = specifications;
    }

    @OneToOne(mappedBy = "supplier" , fetch = FetchType.LAZY , cascade = CascadeType.REMOVE)
    public OrderSetting getOrderSetting() {
		return orderSetting;
	}

	public void setOrderSetting(OrderSetting orderSetting) {
		this.orderSetting = orderSetting;
	}
	
	@OneToMany(mappedBy = "supplier" , cascade = CascadeType.REMOVE)
	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	@Transient
    public Set<Need> getTurnOverNeeds(){
        Set<Need> turnOver = new HashSet<>();
        for(Need need : this.getNeeds()){
            if(need.getType().equals(Need.Type.turnover)){
                turnOver.add(need);
            }
        }
        return turnOver ;
    }
    @Transient
    public Set<Need> getGeneralNeeds(){
        Set<Need> turnOver = new HashSet<>();
        for(Need need : this.getNeeds()){
            if(need.getType().equals(Need.Type.general) && need.getNeedStatus().equals(Need.NeedStatus.available)){
                turnOver.add(need);
            }
        }
        return turnOver ;
    }

    /**
     * 获取直营店
     * @return
     */
    @Transient
    public Set<Need> getNeedsByShopType(ShopType shopType){
        Set<Need> turnOver = new HashSet<>();
        for(Need need : this.getNeeds()){
            if(need.getType().equals(Need.Type.general) && need.getNeedStatus().equals(Need.NeedStatus.available)&& need.getShopType() != null && need.getShopType().equals(shopType)){
                turnOver.add(need);
            }
        }
        return turnOver ;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

	public String getCustomerServiceTel() {
		return customerServiceTel;
	}

	public void setCustomerServiceTel(String customerServiceTel) {
		this.customerServiceTel = customerServiceTel;
	}

	public Boolean getRecommendFlag() {
		return recommendFlag;
	}

	public void setRecommendFlag(Boolean recommendFlag) {
		this.recommendFlag = recommendFlag;
	}

	@Transient
	public FavorCompany getFavorCompany() {
		return favorCompany;
	}

	public void setFavorCompany(FavorCompany favorCompany) {
		this.favorCompany = favorCompany;
	}

	public String getLegalPersonName() {
		return legalPersonName;
	}

	public void setLegalPersonName(String legalPersonName) {
		this.legalPersonName = legalPersonName;
	}

	public String getQqCustomerService() {
		return qqCustomerService;
	}

	public void setQqCustomerService(String qqCustomerService) {
		this.qqCustomerService = qqCustomerService;
	}

    public int getProbationDays() {
        return probationDays;
    }

    public void setProbationDays(int probationDays) {
        this.probationDays = probationDays;
    }

    /**
     * 是否过期
     * @return
     */
    @Transient
    public boolean isExpired(){
        DateTime createDay = new DateTime(this.getCreateDate()).withTimeAtStartOfDay();
        DateTime nowDay = new DateTime().withTimeAtStartOfDay() ;
        Days diffDay = Days.daysBetween(createDay , nowDay) ;
        return diffDay.getDays() > this.getProbationDays() ;
    }

    /**
     * 试用结束日期
     * @return
     */
    @Transient
    public Date getProbationEndDay(){
        DateTime createDay = new DateTime(this.getCreateDate()).withTimeAtStartOfDay();
        DateTime endDay = createDay.plusDays(this.getProbationDays()) ;
        return endDay.toDate();
    }

    /**
     * 非认证的则为试用企业
     * @return
     */
    @Transient
    public boolean isProbation(){
        return this.getStatus() != Status.verified;
    }

    @OneToOne(mappedBy = "supplier", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    public SystemSetting getSystemSetting() {
        return systemSetting;
    }

    public void setSystemSetting(SystemSetting systemSetting) {
        this.systemSetting = systemSetting;
    }

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="member" ,foreignKey = @ForeignKey(name = "null"))
	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public Types getTypes() {
		return types;
	}

	public void setTypes(Types types) {
		this.types = types;
	}

    /**
     * 获取企业下所有有效的product数量
     * @return
     */
    @Transient
    public Integer getProductCount(){
        Set<Goods> goods=getGoods();
        Integer productSize=0;
        Date now=new Date();
        for (Goods good : goods) {
        	if (!good.isDeleted()) {
        		SupplierSupplier supplierSupplier=good.getSupplierSupplier();
        		if (supplierSupplier == null || (supplierSupplier.getStatus()==SupplierSupplier.Status.inTheSupply&&now.getTime() >= supplierSupplier.getStartDate().getTime()&&now.getTime() <= supplierSupplier.getEndDate().getTime())) {
        			productSize+=good.getProducts().size();
				}
        	}
        }
        return productSize;
    }

    /**
     * 获取当前企业最高管理员
     * @return
     */
    @Transient
    public Admin getSystemAdmin(){
        for (Admin admin: getAdmins()) {
            if (admin.getIsSystem()){
                return  admin;
            }
        }
       return null;
    }
}
