/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.microBusiness.manage.Filter;
import com.microBusiness.manage.Order;
import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.BaseEntity;

public interface BaseService<T extends BaseEntity<ID>, ID extends Serializable> {

	T find(ID id);

	List<T> findAll();

	List<T> findList(ID... ids);

	List<T> findList(Integer count, List<Filter> filters, List<Order> orders);

	List<T> findList(Integer first, Integer count, List<Filter> filters, List<Order> orders);

	Page<T> findPage(Pageable pageable);
	
	long count();

	long count(Filter... filters);

	boolean exists(ID id);

	boolean exists(Filter... filters);

	T save(T entity);

	T update(T entity);

	T update(T entity, String... ignoreProperties);

	void delete(ID id);

	void delete(ID... ids);

	void delete(T entity);
	
	/**
	 * 软删除
	 */
	void deleted(ID id);

	void deleted(ID... ids);

	void deleted(T entity);
	
	void copyEntity(T source , T target , String ...ignoreProperties) ;
}