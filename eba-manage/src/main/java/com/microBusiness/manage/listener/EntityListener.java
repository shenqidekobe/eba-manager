/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.listener;

import java.util.Date;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import com.microBusiness.manage.entity.BaseEntity;

public class EntityListener {

	@PrePersist
	public void prePersist(BaseEntity<?> entity) {
		if (entity.getCreateDate() == null) {
			entity.setCreateDate(new Date());
		}
		entity.setModifyDate(new Date());
		entity.setVersion(null);
	}

	@PreUpdate
	public void preUpdate(BaseEntity<?> entity) {
		entity.setModifyDate(new Date());
	}

}