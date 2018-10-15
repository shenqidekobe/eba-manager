/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.dao;

import com.microBusiness.manage.entity.PluginConfig;

public interface PluginConfigDao extends BaseDao<PluginConfig, Long> {

	boolean pluginIdExists(String pluginId);

	PluginConfig findByPluginId(String pluginId);

}