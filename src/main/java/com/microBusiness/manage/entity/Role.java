/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.AttributeConverter;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Converter;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.microBusiness.manage.BaseAttributeConverter;

import com.microBusiness.manage.BaseAttributeConverter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "xx_role")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_role")
public class Role extends BaseEntity<Long> {

	private static final long serialVersionUID = -1253084296846842304L;

	public enum Switchs{
		off,
		on
	}
	private String name;

	private Boolean isSystem;

	private String description;
	
	private Switchs switchs;

	private List<String> authorities = new ArrayList<String>();

	private Set<Admin> admins = new HashSet<Admin>();
	
	private Supplier supplier ;

	@NotEmpty
	@Length(max = 200)
	@Column(nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(nullable = false, updatable = false)
	public Boolean getIsSystem() {
		return isSystem;
	}

	public void setIsSystem(Boolean isSystem) {
		this.isSystem = isSystem;
	}

	@Length(max = 200)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Switchs getSwitchs() {
		return switchs;
	}

	public void setSwitchs(Switchs switchs) {
		this.switchs = switchs;
	}

	@NotEmpty
	@Column(nullable = false, length = 4000)
	@Convert(converter = AuthorityConverter.class)
	public List<String> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(List<String> authorities) {
		this.authorities = authorities;
	}

	@ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
	public Set<Admin> getAdmins() {
		return admins;
	}

	public void setAdmins(Set<Admin> admins) {
		this.admins = admins;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	@Converter
	public static class AuthorityConverter extends BaseAttributeConverter<List<String>> implements AttributeConverter<Object, String> {
	}

}
