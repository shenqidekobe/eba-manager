/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.dao;

import java.util.List;

import com.microBusiness.manage.entity.Area;

public interface AreaDao extends BaseDao<Area, Long> {

	List<Area> findRoots(Integer count);

	List<Area> findParents(Area area, boolean recursive, Integer count);

	List<Area> findChildren(Area area, boolean recursive, Integer count);

	Area findByFullName(String fullName);

}