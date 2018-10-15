/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service;

import com.microBusiness.manage.entity.MessageConfig;

public interface MessageConfigService extends BaseService<MessageConfig, Long> {

	MessageConfig find(MessageConfig.Type type);

}