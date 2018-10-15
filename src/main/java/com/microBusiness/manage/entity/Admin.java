/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.microBusiness.manage.entity.ass.AssChildMember;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "xx_admin")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_admin")
public class Admin extends BaseEntity<Long> {

	private static final long serialVersionUID = -703349383658770247L;

	public static final String LOGIN_TOKEN_COOKIE_NAME = "adminLoginToken";

	private String username;

	private String password;

	private String email;

	private String name;

	//private String department;

	private Boolean isEnabled;

	private Boolean isLocked;

	private Integer loginFailureCount;

	private Date lockedDate;

	private Date loginDate;

	private String loginIp;

	private String lockKey;
	
	//是否是超级管理员
	private Boolean isSystem;

	private Set<Role> roles = new HashSet<Role>();

	private Supplier supplier ;
	
	//绑定手机号
	private String bindPhoneNum;

	private Department department ;

	public enum BootPage {
		//我要供应
		supply,
		//我要采购
		purchase
	}
	
	//引导页面（我要供应，我要采购）
	private BootPage bootPage;
	
	public enum Prompts {
		//提示
		prompt,
		//不提示
		noTips
	}
	
	//是否提示引导页面
	private Prompts prompts;
	
	private AssChildMember assChildMember;
	

	@NotEmpty(groups = Save.class)
	@Pattern(regexp = "^[0-9a-zA-Z_\\u4e00-\\u9fa5]+$")
	@Length(min = 2, max = 20)
	@Column(nullable = false, updatable = false, unique = true)
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@NotEmpty(groups = Save.class)
	@Length(min = 4, max = 20)
	@Column(nullable = false)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Email
	@Length(max = 200)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Length(max = 200)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/*@Length(max = 200)
	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}*/

	@NotNull
	@Column(nullable = false)
	public Boolean getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(Boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	@Column(nullable = false)
	public Boolean getIsLocked() {
		return isLocked;
	}

	public void setIsLocked(Boolean isLocked) {
		this.isLocked = isLocked;
	}

	@Column(nullable = false)
	public Integer getLoginFailureCount() {
		return loginFailureCount;
	}

	public void setLoginFailureCount(Integer loginFailureCount) {
		this.loginFailureCount = loginFailureCount;
	}

	public Date getLockedDate() {
		return lockedDate;
	}

	public void setLockedDate(Date lockedDate) {
		this.lockedDate = lockedDate;
	}

	public Date getLoginDate() {
		return loginDate;
	}

	public void setLoginDate(Date loginDate) {
		this.loginDate = loginDate;
	}

	public String getLoginIp() {
		return loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	@Column(nullable = false, updatable = false)
	public String getLockKey() {
		return lockKey;
	}

	public void setLockKey(String lockKey) {
		this.lockKey = lockKey;
	}

	@NotEmpty
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "xx_admin_role")
	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	@PrePersist
	public void prePersist() {
		setUsername(StringUtils.lowerCase(getUsername()));
		setEmail(StringUtils.lowerCase(getEmail()));
		setLockKey(DigestUtils.md5Hex(UUID.randomUUID() + RandomStringUtils.randomAlphabetic(30)));
	}

	@PreUpdate
	public void preUpdate() {
		setEmail(StringUtils.lowerCase(getEmail()));
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(nullable = true, updatable = false , foreignKey = @ForeignKey(name = "null"))
	public Supplier getSupplier() {
		return supplier;
	}


	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	@Column(unique = true)
	public String getBindPhoneNum() {
		return bindPhoneNum;
	}

	public void setBindPhoneNum(String bindPhoneNum) {
		this.bindPhoneNum = bindPhoneNum;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, updatable = true , foreignKey = @ForeignKey(name = "null"))
	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public BootPage getBootPage() {
		return bootPage;
	}

	public void setBootPage(BootPage bootPage) {
		this.bootPage = bootPage;
	}

	public Prompts getPrompts() {
		return prompts;
	}

	public void setPrompts(Prompts prompts) {
		this.prompts = prompts;
	}

	@OneToOne(mappedBy = "admin", fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, updatable = true , foreignKey = @ForeignKey(name = "null"))
	public AssChildMember getAssChildMember() {
		return assChildMember;
	}

	public void setAssChildMember(AssChildMember assChildMember) {
		this.assChildMember = assChildMember;
	}

	public Boolean getIsSystem() {
		return isSystem;
	}

	public void setIsSystem(Boolean isSystem) {
		this.isSystem = isSystem;
	}
	
}
