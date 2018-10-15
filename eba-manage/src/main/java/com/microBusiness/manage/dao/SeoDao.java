/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.dao;

import com.microBusiness.manage.entity.Seo;

public interface SeoDao extends BaseDao<Seo, Long> {

	Seo find(Seo.Type type);

}