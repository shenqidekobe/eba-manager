/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service;

import com.microBusiness.manage.TemplateConfig;
import com.microBusiness.manage.TemplateConfig;

public interface TemplateService {

	String read(String templateConfigId);

	String read(TemplateConfig templateConfig);

	void write(String templateConfigId, String content);

	void write(TemplateConfig templateConfig, String content);

}