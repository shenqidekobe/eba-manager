/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.dao;

import com.microBusiness.manage.entity.MessageConfig;

public interface MessageConfigDao extends BaseDao<MessageConfig, Long> {

	MessageConfig find(MessageConfig.Type type);

}