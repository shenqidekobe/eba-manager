/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.LockModeType;

import com.microBusiness.manage.Filter;
import com.microBusiness.manage.Order;
import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.BaseEntity;

public interface BaseDao<T extends BaseEntity<ID>, ID extends Serializable> {

	T find(ID id);

	T find(ID id, LockModeType lockModeType);

	List<T> findList(Integer first, Integer count, List<Filter> filters, List<Order> orders);

	Page<T> findPage(Pageable pageable);
	
	long count(Filter... filters);

	void persist(T entity);

	T merge(T entity);

	void remove(T entity);
	
	/**
	 * 软删除
	 * @param entity
	 */
	void delete(T entity);

	void refresh(T entity);

	void refresh(T entity, LockModeType lockModeType);

	ID getIdentifier(T entity);

	boolean isLoaded(T entity);

	boolean isLoaded(T entity, String attributeName);

	boolean isManaged(T entity);

	void detach(T entity);

	LockModeType getLockMode(T entity);

	void lock(T entity, LockModeType lockModeType);

	void clear();

	void flush();

}