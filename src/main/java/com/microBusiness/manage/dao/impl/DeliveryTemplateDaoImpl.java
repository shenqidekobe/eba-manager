/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.dao.impl;

import javax.persistence.NoResultException;

import com.microBusiness.manage.dao.DeliveryTemplateDao;
import com.microBusiness.manage.entity.DeliveryTemplate;

import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

@Repository("deliveryTemplateDaoImpl")
public class DeliveryTemplateDaoImpl extends BaseDaoImpl<DeliveryTemplate, Long> implements DeliveryTemplateDao {

	public DeliveryTemplate findDefault() {
		try {
			String jpql = "select deliveryTemplate from DeliveryTemplate deliveryTemplate where deliveryTemplate.isDefault = true";
			return entityManager.createQuery(jpql, DeliveryTemplate.class).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public void setDefault(DeliveryTemplate deliveryTemplate) {
		Assert.notNull(deliveryTemplate);

		deliveryTemplate.setIsDefault(true);
		if (deliveryTemplate.isNew()) {
			String jpql = "update DeliveryTemplate deliveryTemplate set deliveryTemplate.isDefault = false where deliveryTemplate.isDefault = true";
			entityManager.createQuery(jpql).executeUpdate();
		} else {
			String jpql = "update DeliveryTemplate deliveryTemplate set deliveryTemplate.isDefault = false where deliveryTemplate.isDefault = true and deliveryTemplate != :deliveryTemplate";
			entityManager.createQuery(jpql).setParameter("deliveryTemplate", deliveryTemplate).executeUpdate();
		}
	}

}