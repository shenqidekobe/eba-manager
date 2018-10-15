/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service;

import java.util.List;

import com.microBusiness.manage.plugin.LoginPlugin;
import com.microBusiness.manage.plugin.PaymentPlugin;
import com.microBusiness.manage.plugin.StoragePlugin;

public interface PluginService {

	List<PaymentPlugin> getPaymentPlugins();

	List<StoragePlugin> getStoragePlugins();

	List<LoginPlugin> getLoginPlugins();

	List<PaymentPlugin> getPaymentPlugins(boolean isEnabled);

	List<StoragePlugin> getStoragePlugins(boolean isEnabled);

	List<LoginPlugin> getLoginPlugins(boolean isEnabled);

	PaymentPlugin getPaymentPlugin(String id);

	StoragePlugin getStoragePlugin(String id);

	LoginPlugin getLoginPlugin(String id);

}