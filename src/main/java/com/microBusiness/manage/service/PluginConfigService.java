/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service;

import com.microBusiness.manage.entity.PluginConfig;
import com.microBusiness.manage.entity.PluginConfig;

public interface PluginConfigService extends BaseService<PluginConfig, Long> {

	boolean pluginIdExists(String pluginId);

	PluginConfig findByPluginId(String pluginId);

	void deleteByPluginId(String pluginId);

}