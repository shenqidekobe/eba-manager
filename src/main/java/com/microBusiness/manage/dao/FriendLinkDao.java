/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.dao;

import java.util.List;

import com.microBusiness.manage.entity.FriendLink;

public interface FriendLinkDao extends BaseDao<FriendLink, Long> {

	List<FriendLink> findList(FriendLink.Type type);

}