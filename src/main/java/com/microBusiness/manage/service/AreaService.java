/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service;

import java.util.List;

import com.microBusiness.manage.entity.Area;

public interface AreaService extends BaseService<Area, Long> {

	List<Area> findRoots();

	List<Area> findRoots(Integer count);

	List<Area> findParents(Area area, boolean recursive, Integer count);

	List<Area> findChildren(Area area, boolean recursive, Integer count);

}