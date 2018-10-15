/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.microBusiness.manage.Filter;
import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.Principal;
import com.microBusiness.manage.dao.AdminDao;
import com.microBusiness.manage.dao.RoleDao;
import com.microBusiness.manage.dao.SupplierDao;
import com.microBusiness.manage.dao.SystemSettingDao;
import com.microBusiness.manage.entity.Admin;
import com.microBusiness.manage.entity.Admin.Prompts;
import com.microBusiness.manage.entity.Department;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.Role;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.entity.SystemSetting;
import com.microBusiness.manage.entity.Types;
import com.microBusiness.manage.service.AdminService;
import com.microBusiness.manage.service.BindPhoneSmsService;

@Service("adminServiceImpl")
public class AdminServiceImpl extends BaseServiceImpl<Admin, Long> implements AdminService {

	@Resource(name = "adminDaoImpl")
	private AdminDao adminDao;

	@Resource
	private SupplierDao supplierDao ;

	@Value("${supplier.role.notCertified}")
	private Long notCertifiedRoleId ;

	@Resource
	private RoleDao roleDao ;

	@Resource
	private SystemSettingDao systemSettingDao ;
	@Resource
	private BindPhoneSmsService bindPhoneSmsService;
	
	@Transactional(readOnly = true)
	public boolean usernameExists(String username) {
		return adminDao.usernameExists(username);
	}

	@Transactional(readOnly = true)
	public Admin findByUsername(String username) {
		try {
			return adminDao.findByUsername(username);
		}catch (Exception e){
			e.printStackTrace();
		}
		return null ;
	}

	@Transactional(readOnly = true)
	public List<String> findAuthorities(Long id) {
		List<String> authorities = new ArrayList<String>();
		Admin admin = adminDao.find(id);
		if (admin != null && admin.getRoles() != null) {
			for (Role role : admin.getRoles()) {
				if (role != null && role.getAuthorities() != null) {
					authorities.addAll(role.getAuthorities());
				}
			}
		}
		return authorities;
	}

	@Transactional(readOnly = true)
	public boolean isAuthenticated() {
		Subject subject = SecurityUtils.getSubject();
		if (subject != null) {
			return subject.isAuthenticated();
		}
		return false;
	}

	@Transactional(readOnly = true)
	public Admin getCurrent() {
		Subject subject = SecurityUtils.getSubject();
		if (subject != null) {
			Principal principal = (Principal) subject.getPrincipal();
			if (principal != null) {
				return adminDao.find(principal.getId());
			}
		}
		return null;
	}

	@Transactional(readOnly = true)
	public String getCurrentUsername() {
		Subject subject = SecurityUtils.getSubject();
		if (subject != null) {
			Principal principal = (Principal) subject.getPrincipal();
			if (principal != null) {
				return principal.getUsername();
			}
		}
		return null;
	}

	@Transactional(readOnly = true)
	@Cacheable(value = "loginToken")
	public String getLoginToken() {
		return DigestUtils.md5Hex(UUID.randomUUID() + RandomStringUtils.randomAlphabetic(30));
	}

	@Override
	@Transactional
	@CacheEvict(value = "authorization", allEntries = true)
	public Admin save(Admin admin) {
		return super.save(admin);
	}

	@Override
	@Transactional
	@CacheEvict(value = "authorization", allEntries = true)
	public Admin update(Admin admin) {
		return super.update(admin);
	}

	@Override
	@Transactional
	@CacheEvict(value = "authorization", allEntries = true)
	public Admin update(Admin admin, String... ignoreProperties) {
		return super.update(admin, ignoreProperties);
	}

	@Override
	@Transactional
	@CacheEvict(value = "authorization", allEntries = true)
	public void delete(Long id) {
		super.delete(id);
	}

	@Override
	@Transactional
	@CacheEvict(value = "authorization", allEntries = true)
	public void delete(Long... ids) {
		super.delete(ids);
	}

	@Override
	@Transactional
	@CacheEvict(value = "authorization", allEntries = true)
	public void delete(Admin admin) {
		super.delete(admin);
	}

	/**
	 * 获取当前登录用户所属 供应商
	 * @return
	 */
	@Override
	public Supplier getCurrentSupplier() {
		Admin currentAdmin = this.getCurrent() ;
		if(null == currentAdmin){
			// TODO: 2017/1/23
			return null ;
		}
		return currentAdmin.getSupplier() ;

	}

	@Override
	public Page<Admin> findPage(String searchValue,Pageable pageable, Supplier currentSupplier,Long adminId) {
		return adminDao.findPage(searchValue,pageable,currentSupplier,adminId);
	}

	@Override
	public boolean register(Supplier supplier, Admin admin,Member member) {

		supplier.setStatus(Supplier.Status.notCertified);
		//添加邀请码，邀请码规则为 3为字母+3位数字
		// FIXME: 2017/6/26 这里可存在邀请码重复的情况，需要做校验，问题：在数据量增长的情况下，会大概率重复

		String inviteCode ;
		do{
			String randomStr = RandomStringUtils.random(3 , 65 , 90 , true , false);
			String randomNumber = RandomStringUtils.randomNumeric(3);
			inviteCode = randomStr + randomNumber ;
		}while (supplierDao.inviteCodeExists(inviteCode));
		supplier.setInviteCode(inviteCode);
		supplier.setTypes(Types.platform);

		supplierDao.persist(supplier);


		//创建企业设置
		SystemSetting systemSetting = new SystemSetting() ;
		systemSetting.setSupplier(supplier);
		systemSettingDao.persist(systemSetting);

		Long[] roleIds={notCertifiedRoleId};

		List<Filter> filters = new ArrayList<>();

		filters.add(Filter.in("id" , Arrays.asList(roleIds)));

		List<Role> roles = roleDao.findList(null , null , filters , null);


		admin.setSupplier(supplier);
		admin.setRoles(new HashSet<Role>(roles));
		admin.setIsLocked(false);
		admin.setLoginFailureCount(0);
		admin.setLockedDate(null);
		admin.setLoginDate(null);
		admin.setLoginIp(null);
		admin.setLockKey(null);
		admin.setName(null);
		admin.setIsEnabled(true);
		String pwd = admin.getPassword();
		admin.setPassword(DigestUtils.md5Hex(admin.getPassword()));
		admin.setPrompts(Admin.Prompts.prompt);
		admin.setIsSystem(true);
		adminDao.persist(admin);

		if (member != null){
			member.setAdmin(admin);
		}
		String content = "您已成功注册微商小管理账号，用户名:" + admin.getUsername()
				+ "，密码:" + pwd + "，妥善保管用户名和密码，请勿泄露，登录地址:www.dinghuo.me/admin";
		
		bindPhoneSmsService.sendSms(admin.getBindPhoneNum(), content);
		
		return true;
	}

	@Override
	public Admin findBybindPhoneNum(String tel) {

		return adminDao.findBybindPhoneNum(tel);
	}

	@Override
	public Admin operationTips(Prompts prompts , Admin admin) {
		Admin ad = adminDao.find(admin.getId());
		if(prompts != null) {
			ad.setPrompts(prompts);
		}
		return ad;
	}

	@Override
	public List<Admin> getListByDepartment(Department department,Supplier supplier) {
		// TODO Auto-generated method stub
		return adminDao.getListByDepartment(department,supplier);
	}

	@Override
	public Admin find(Supplier supplier, boolean isSystem) {
		return adminDao.find(supplier, isSystem);
	}

	@Override
	public void refreshAdmin(Admin admin) {
		adminDao.persist(admin);
	}

}