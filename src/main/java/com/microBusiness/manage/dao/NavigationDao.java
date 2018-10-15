/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.dao;

import java.util.List;

import com.microBusiness.manage.entity.Navigation;
import com.microBusiness.manage.entity.Navigation;

public interface NavigationDao extends BaseDao<Navigation, Long> {

	List<Navigation> findList(Navigation.Position position);

}