/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service;

import com.microBusiness.manage.entity.Seo;
import com.microBusiness.manage.entity.Seo;

public interface SeoService extends BaseService<Seo, Long> {

	Seo find(Seo.Type type);

	Seo find(Seo.Type type, boolean useCache);

}