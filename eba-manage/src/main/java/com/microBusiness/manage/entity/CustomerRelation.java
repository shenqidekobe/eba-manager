package com.microBusiness.manage.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Created by mingbai on 2017/6/27. 功能描述：客户关系实体 修改记录：
 */
@Entity
@Table(name = "t_customer_relation")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_customer_relation")
public class CustomerRelation extends BaseEntity<Long> {

	private static final long serialVersionUID = 8769855180788450294L;

	public enum ClientType {

		enterprise, // 企业
		individual// 个体
	}

	public enum Differentiate {
		supplier, // 供应商
		buyers// 采购商
	}

	// 邀请码
	private String inviteCode;
	// 客户名称
	private String clientName;
	// 客户编号
	private String clientNum;
	// 客户类型
	private ClientType clientType;
	// 客户地区
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
	// 纳税人识别码
	private String identifier;
	// 区分供应商/采购商
	private Differentiate differentiate;
	// 供应商名称
	private String supplierName;
	// 供应商编号
	private String supplierCode;
	// 供应商地区
	private Area supplierArea;
	// 供应商详细地址
	private String supplierAddress;
	// 供应商联系人
	private String supplierUserName;
	// 供应商联系方式
	private String supplierTel;
	// 供应商邮箱
	private String supplierEmail;
	// 供应商开户名称
	private String supAccountName;
	// 供应商发票抬头
	private String supplierInvoice;
	// 供应商开户银行
	private String supplierBank;
	// 供应商银行账号
	private String supBankAccountNum;
	// 本企业
	private Supplier supplier;
	// 关联企业
	private Supplier bySupplier;
	//所属员工
	private Admin admin;

	private SourceType sourceType;
	
	public String getInviteCode() {
		return inviteCode;
	}

	public void setInviteCode(String inviteCode) {
		this.inviteCode = inviteCode;
	}

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

	public ClientType getClientType() {
		return clientType;
	}

	public void setClientType(ClientType clientType) {
		this.clientType = clientType;
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

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public Differentiate getDifferentiate() {
		return differentiate;
	}

	public void setDifferentiate(Differentiate differentiate) {
		this.differentiate = differentiate;
	}

	
	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public String getSupplierCode() {
		return supplierCode;
	}

	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Area getSupplierArea() {
		return supplierArea;
	}

	public void setSupplierArea(Area supplierArea) {
		this.supplierArea = supplierArea;
	}

	public String getSupplierAddress() {
		return supplierAddress;
	}

	public void setSupplierAddress(String supplierAddress) {
		this.supplierAddress = supplierAddress;
	}

	public String getSupplierUserName() {
		return supplierUserName;
	}

	public void setSupplierUserName(String supplierUserName) {
		this.supplierUserName = supplierUserName;
	}

	public String getSupplierTel() {
		return supplierTel;
	}

	public void setSupplierTel(String supplierTel) {
		this.supplierTel = supplierTel;
	}

	public String getSupplierEmail() {
		return supplierEmail;
	}

	public void setSupplierEmail(String supplierEmail) {
		this.supplierEmail = supplierEmail;
	}

	public String getSupAccountName() {
		return supAccountName;
	}

	public void setSupAccountName(String supAccountName) {
		this.supAccountName = supAccountName;
	}

	public String getSupplierInvoice() {
		return supplierInvoice;
	}

	public void setSupplierInvoice(String supplierInvoice) {
		this.supplierInvoice = supplierInvoice;
	}

	public String getSupplierBank() {
		return supplierBank;
	}

	public void setSupplierBank(String supplierBank) {
		this.supplierBank = supplierBank;
	}

	public String getSupBankAccountNum() {
		return supBankAccountNum;
	}

	public void setSupBankAccountNum(String supBankAccountNum) {
		this.supBankAccountNum = supBankAccountNum;
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
	@JoinColumn(name="bySupplier" , nullable = true, updatable = false , foreignKey = @ForeignKey(name = "null"))
	public Supplier getBySupplier() {
		return bySupplier;
	}

	public void setBySupplier(Supplier bySupplier) {
		this.bySupplier = bySupplier;
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

}
