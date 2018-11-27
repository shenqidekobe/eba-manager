INSERT INTO `xx_admin` (`id`, `create_date`, `deleted`, `modify_date`, `version`, `bind_phone_num`, `boot_page`, `email`, `is_enabled`, `is_locked`, `is_system`, `lock_key`, `locked_date`, `login_date`, `login_failure_count`, `login_ip`, `name`, `password`, `prompts`, `username`, `department`, `supplier`) VALUES (1, '2018-05-16 21:40:58', b'0', '2018-10-26 14:53:02', 181, '18121109203', 0, NULL, b'1', b'0', b'1', '50943e002c993e84b7385d93867ce388', NULL, '2018-10-26 14:53:02', 0, '114.94.199.166', NULL, '7a5b0cbffdd99d9b977dc09f3825045e', 0, 'shchunxiao', NULL, 1);

INSERT INTO `t_supplier` (`id`, `create_date`, `deleted`, `modify_date`, `version`, `address`, `app_id`, `app_key`, `business_card`, `company_profile`, `customer_service_tel`, `email`, `imagelogo`, `industry`, `invite_code`, `legal_person_name`, `name`, `probation_days`, `qq_customer_service`, `reasons`, `recommend_flag`, `status`, `tel`, `types`, `user_name`, `area`, `member`) VALUES (1, '2018-05-16 21:40:58', b'0', '2018-05-16 21:40:58', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'UDR926', NULL, '均瑶董事会', 60000, NULL, NULL, NULL, 3, NULL, 0, NULL, NULL, NULL);


INSERT INTO `xx_role` (`id`, `create_date`, `modify_date`, `version`, `authorities`, `description`, `is_system`, `name`, `switchs`, `supplier`, `deleted`) VALUES (1, '2015-12-01 00:59:21', '2015-12-01 00:59:21', 0, '["admin:goods","admin:stock","admin:productCategory","admin:parameter","admin:attribute","admin:specification","admin:brand","admin:productNotify","admin:order","admin:print","admin:payment","admin:refunds","admin:shipping","admin:returns","admin:deliveryCenter","admin:deliveryTemplate","admin:member","admin:memberRank","admin:memberAttribute","admin:point","admin:deposit","admin:review","admin:consultation","admin:messageConfig","admin:navigation","admin:article","admin:articleCategory","admin:tag","admin:friendLink","admin:adPosition","admin:ad","admin:template","admin:theme","admin:cache","admin:static","admin:index","admin:promotion","admin:coupon","admin:seo","admin:sitemap","admin:statistics","admin:memberStatistic","admin:orderStatistic","admin:memberRanking","admin:goodsRanking","admin:setting","admin:area","admin:paymentMethod","admin:shippingMethod","admin:deliveryCorp","admin:paymentPlugin","admin:storagePlugin","admin:loginPlugin","admin:admin","admin:role","admin:message","admin:log","admin:need" ,"admin:temporary" ,"admin:supplier" , "admin:formalSupply" , "admin:supplyDistribution","admin:notice","admin:orderSetting","admin:customerRelation","admin:supplierRelation","admin:commonCategory" , "admin:supplyCheck" , "admin:purchaseCheck" , "admin:recommendSupplier","admin:systemSetting","admin:goodsCenter"]', '拥有管理后台最高权限', b'1', '超级管理员', NULL, NULL, b'0');
INSERT INTO `xx_role` (`id`, `create_date`, `modify_date`, `version`, `authorities`, `description`, `is_system`, `name`, `switchs`, `supplier`, `deleted`) VALUES (2, '2017-01-23 14:04:41', '2017-02-13 15:47:35', 2, '["admin:goods","admin:temporary","admin:order","admin:print","admin:need"]', NULL, b'0', '供应商', NULL, NULL, b'0');
INSERT INTO `xx_role` (`id`, `create_date`, `modify_date`, `version`, `authorities`, `description`, `is_system`, `name`, `switchs`, `supplier`, `deleted`) VALUES (3, '2017-02-15 11:53:14', '2017-02-15 11:53:14', 0, '["admin:goods","admin:productCategory","admin:parameter","admin:attribute","admin:specification","admin:brand","admin:temporary","admin:order","admin:print","admin:setting","admin:area","admin:shippingMethod","admin:deliveryCorp","admin:admin","admin:role","admin:message","admin:log","admin:need", "admin:supplier"]', NULL, b'0', '总后台管理员', NULL, NULL, b'0');
INSERT INTO `xx_role` (`id`, `create_date`, `modify_date`, `version`, `authorities`, `description`, `is_system`, `name`, `switchs`, `supplier`, `deleted`) VALUES (4, '2017-03-01 10:25:00', '2017-03-01 10:25:00', 0, '["admin:admin"]', NULL, b'0', '账户管理员', NULL, NULL, b'0');
INSERT INTO `xx_role` (`id`, `create_date`, `modify_date`, `version`, `authorities`, `description`, `is_system`, `name`, `switchs`, `supplier`, `deleted`) VALUES (5, '2017-03-01 10:28:05', '2017-03-01 10:28:05', 0, '["admin:need"]', NULL, b'0', '需方管理', NULL, NULL, b'0');
INSERT INTO `xx_role` (`id`, `create_date`, `modify_date`, `version`, `authorities`, `description`, `is_system`, `name`, `switchs`, `supplier`, `deleted`) VALUES (6, '2017-04-11 16:11:26', '2017-10-31 21:49:02', 4, '["admin:homePage","admin:productCategory:add","admin:productCategory:edit","admin:productCategory:delete","admin:specification:add","admin:specification:edit","admin:specification:delete","admin:goods:add","admin:goods:edit","admin:goods:delete","admin:need:add","admin:need:edit","admin:need:updateneedStatus","admin:need:importMore","admin:formalSupply:add","admin:formalSupply:view","admin:formalSupply:edit","admin:formalSupply:updateStatus","admin:formalSupply:delete","admin:supplyDistribution:view","admin:supplyDistribution:distributionList","admin:supplierRelation:edit","admin:print:orderBatchPrint","admin:order:checkBatchReview","admin:order:getOut","admin:print:order","admin:order:review","admin:order:updateItems","admin:order:shipping","admin:order:addRemarks","admin:order:applyCancel","admin:order:cancel","admin:order:complete","admin:print:orderShippingInfo","admin:order:cancelShipped","admin:print:verificationDeliveryInfo","admin:ownOrder:getOut","admin:ownOrder:add","admin:ownOrder:addMore","admin:print:purchaseorder","admin:ownOrder:updateItems","admin:ownOrder:applicationCancel","admin:ownOrder:addRemarks","admin:print:deliveryInfor","admin:customerRelation:add","admin:customerRelation:edit","admin:customerRelation:delete","admin:orderReport:orderList","admin:orderReport:purchaseList","admin:commodityReport:orderForm","admin:commodityReport:purchaseOrder","admin:customerReport","admin:admin:add","admin:admin:edit","admin:admin:delete","admin:notice:add","admin:notice:delete","admin:notice:orderNotice","admin:notice:purchaseNotice","admin:orderSetting","admin:enterpriseInfo","admin:role","admin:department","admin:distributionOrder","admin:needSupply","admin:proxyOrder","admin:turnoverSupply","admin:systemSetting","admin:goods:distribution","admin:goodDirectory","admin:shareStatistics","admin:listStatistics","admin:goods:importMore","admin:adPosition","admin:ad","admin:proxyUser"]', NULL, b'0', '未认证企业', NULL, NULL, b'0');
INSERT INTO `xx_role` (`id`, `create_date`, `modify_date`, `version`, `authorities`, `description`, `is_system`, `name`, `switchs`, `supplier`, `deleted`) VALUES (7, '2017-04-11 16:12:09', '2017-12-29 11:40:36', 12, '["admin:homePage","admin:productCategory:add","admin:productCategory:edit","admin:productCategory:delete","admin:specification:add","admin:specification:edit","admin:specification:delete","admin:goods:add","admin:goods:edit","admin:goods:delete","admin:need:add","admin:need:edit","admin:need:updateneedStatus","admin:need:importMore","admin:formalSupply:add","admin:formalSupply:view","admin:formalSupply:edit","admin:formalSupply:updateStatus","admin:formalSupply:delete","admin:supplyDistribution:view","admin:supplyDistribution:distributionList","admin:supplierRelation:edit","admin:print:orderBatchPrint","admin:order:checkBatchReview","admin:order:getOut","admin:print:order","admin:order:review","admin:order:updateItems","admin:order:shipping","admin:order:addRemarks","admin:order:applyCancel","admin:order:cancel","admin:order:complete","admin:print:orderShippingInfo","admin:order:cancelShipped","admin:print:verificationDeliveryInfo","admin:ownOrder:getOut","admin:ownOrder:add","admin:ownOrder:addMore","admin:print:purchaseorder","admin:ownOrder:updateItems","admin:ownOrder:applicationCancel","admin:ownOrder:addRemarks","admin:print:deliveryInfor","admin:customerRelation:add","admin:customerRelation:edit","admin:customerRelation:delete","admin:orderReport:orderList","admin:orderReport:purchaseList","admin:commodityReport:orderForm","admin:commodityReport:purchaseOrder","admin:customerReport","admin:admin:add","admin:admin:edit","admin:admin:delete","admin:notice:add","admin:notice:delete","admin:notice:orderNotice","admin:notice:purchaseNotice","admin:orderSetting","admin:enterpriseInfo","admin:role","admin:department","admin:distributionOrder","admin:needSupply","admin:proxyOrder","admin:turnoverSupply","admin:systemSetting","admin:goods:distribution","admin:goodDirectory","admin:shareStatistics","admin:listStatistics","admin:goods:importMore","admin:adPosition","admin:ad","admin:proxyUser:add","admin:proxyUser:edit","admin:proxyUser:delete","admin:proxyCheck:check"]', NULL, b'0', '认证企业', NULL, NULL, b'0');
INSERT INTO `xx_role` (`id`, `create_date`, `modify_date`, `version`, `authorities`, `description`, `is_system`, `name`, `switchs`, `supplier`, `deleted`) VALUES (8, '2017-04-11 16:12:40', '2017-04-11 17:03:43', 1, '["admin:goods","admin:productCategory","admin:specification","admin:formalSupply","admin:temporary"]', NULL, b'0', '供应管理员', 1, NULL, b'0');
INSERT INTO `xx_role` (`id`, `create_date`, `modify_date`, `version`, `authorities`, `description`, `is_system`, `name`, `switchs`, `supplier`, `deleted`) VALUES (45, '2018-05-17 12:48:53', '2018-05-18 14:29:41', 1, '["admin:homePage","admin:productCategory:add","admin:productCategory:edit","admin:productCategory:delete","admin:specification:add","admin:specification:edit","admin:specification:delete","admin:goods:add","admin:goods:edit","admin:goods:delete","admin:proxyUser:add","admin:proxyUser:edit","admin:proxyUser:delete","admin:proxyCheck:check","admin:order:checkBatchReview","admin:order:review","admin:order:updateItems","admin:order:shipping","admin:order:addRemarks","admin:order:applyCancel","admin:order:cancel","admin:order:complete","admin:orderReport:orderList","admin:commodityReport:orderForm","admin:role:add","admin:role:edit","admin:role:delete","admin:department:add","admin:department:edit","admin:department:delete","admin:admin:add","admin:admin:edit","admin:admin:delete"]', '开放所有权限', b'0', '合伙人', NULL, 1, b'0');
INSERT INTO `xx_role` (`id`, `create_date`, `modify_date`, `version`, `authorities`, `description`, `is_system`, `name`, `switchs`, `supplier`, `deleted`) VALUES (46, '2018-05-17 12:50:23', '2018-05-17 12:51:16', 1, '["admin:order:checkBatchReview","admin:order:review","admin:order:updateItems","admin:order:shipping","admin:order:addRemarks","admin:order:applyCancel","admin:order:cancel","admin:order:complete","admin:orderReport:orderList","admin:commodityReport:orderForm"]', '订单、报表', b'0', '销售运营部', NULL, 1, b'0');
INSERT INTO `xx_role` (`id`, `create_date`, `modify_date`, `version`, `authorities`, `description`, `is_system`, `name`, `switchs`, `supplier`, `deleted`) VALUES (47, '2018-05-17 12:50:58', '2018-05-17 12:50:58', 0, '["admin:homePage","admin:productCategory:add","admin:productCategory:edit","admin:productCategory:delete","admin:specification:add","admin:specification:edit","admin:specification:delete","admin:goods:add","admin:goods:edit","admin:goods:delete"]', '首页、商品', b'0', '市场部', NULL, 1, b'0');


INSERT INTO `xx_admin_role` (`admins`, `roles`) VALUES (1, 7);



INSERT INTO `xx_member_rank` (`id`, `create_date`, `modify_date`, `version`, `amount`, `is_default`, `is_special`, `name`, `scale`, `deleted`) VALUES (1, '2015-12-01 00:59:09', '2015-12-01 00:59:09', 0, 0.000000, b'1', b'0', '普通会员', 1, b'0');
INSERT INTO `xx_member_rank` (`id`, `create_date`, `modify_date`, `version`, `amount`, `is_default`, `is_special`, `name`, `scale`, `deleted`) VALUES (2, '2015-12-01 00:59:10', '2015-12-01 00:59:10', 0, 1000.000000, b'0', b'0', '铜牌会员', 1, b'0');
INSERT INTO `xx_member_rank` (`id`, `create_date`, `modify_date`, `version`, `amount`, `is_default`, `is_special`, `name`, `scale`, `deleted`) VALUES (3, '2015-12-01 00:59:11', '2015-12-01 00:59:11', 0, 10000.000000, b'0', b'0', '银牌会员', 1, b'0');
INSERT INTO `xx_member_rank` (`id`, `create_date`, `modify_date`, `version`, `amount`, `is_default`, `is_special`, `name`, `scale`, `deleted`) VALUES (4, '2015-12-01 00:59:12', '2015-12-01 00:59:12', 0, 100000.000000, b'0', b'0', '金牌会员', 1, b'0');


INSERT INTO `xx_plugin_config` (`id`, `create_date`, `modify_date`, `version`, `orders`, `attributes`, `is_enabled`, `plugin_id`, `deleted`) VALUES (1, '2015-12-01 00:57:42', '2015-12-01 00:57:42', 0, 100, NULL, b'1', 'localStoragePlugin', b'0');
INSERT INTO `xx_plugin_config` (`id`, `create_date`, `modify_date`, `version`, `orders`, `attributes`, `is_enabled`, `plugin_id`, `deleted`) VALUES (2, '2015-12-01 00:57:43', '2015-12-01 00:57:43', 0, 1, '{"fee":"0","paymentName":"财付通","logo":"http://image.demo.shopxx.net/4.0/201501/2e05d165-ff0a-4bf7-ab32-0f5431b579e4.gif","description":null,"feeType":"fixed","partner":"abc","key":"abc"}', b'1', 'tenpayDirectPaymentPlugin', b'0');
INSERT INTO `xx_plugin_config` (`id`, `create_date`, `modify_date`, `version`, `orders`, `attributes`, `is_enabled`, `plugin_id`, `deleted`) VALUES (3, '2015-12-01 22:32:38', '2015-12-01 22:32:38', 0, NULL, NULL, b'0', 'weiboLoginPlugin', b'0');



INSERT INTO `xx_sn` (`id`, `create_date`, `modify_date`, `version`, `last_value`, `type`, `deleted`) VALUES (1, '2015-12-01 00:57:35', '2018-07-31 17:44:20', 188, 182, 0, b'0');
INSERT INTO `xx_sn` (`id`, `create_date`, `modify_date`, `version`, `last_value`, `type`, `deleted`) VALUES (2, '2015-12-01 00:57:36', '2018-09-24 08:14:16', 186, 175, 1, b'0');
INSERT INTO `xx_sn` (`id`, `create_date`, `modify_date`, `version`, `last_value`, `type`, `deleted`) VALUES (3, '2015-12-01 00:57:37', '2015-12-01 00:57:37', 0, 0, 2, b'0');
INSERT INTO `xx_sn` (`id`, `create_date`, `modify_date`, `version`, `last_value`, `type`, `deleted`) VALUES (4, '2015-12-01 00:57:38', '2017-02-09 15:28:11', 1, 0, 3, b'0');
INSERT INTO `xx_sn` (`id`, `create_date`, `modify_date`, `version`, `last_value`, `type`, `deleted`) VALUES (5, '2015-12-01 00:57:39', '2015-12-01 00:57:39', 0, 0, 4, b'0');
INSERT INTO `xx_sn` (`id`, `create_date`, `modify_date`, `version`, `last_value`, `type`, `deleted`) VALUES (6, '2015-12-01 00:57:40', '2018-07-28 02:36:35', 107, 99, 5, b'0');
INSERT INTO `xx_sn` (`id`, `create_date`, `modify_date`, `version`, `last_value`, `type`, `deleted`) VALUES (7, '2015-12-01 00:57:41', '2015-12-01 00:57:41', 0, 0, 6, b'0');
INSERT INTO `xx_sn` (`id`, `create_date`, `modify_date`, `version`, `last_value`, `type`, `deleted`) VALUES (8, '2015-12-01 00:57:41', '2018-04-02 22:27:30', 100, 100, 7, b'0');
INSERT INTO `xx_sn` (`id`, `create_date`, `modify_date`, `version`, `last_value`, `type`, `deleted`) VALUES (9, '2017-11-30 18:42:13', '2018-04-02 22:27:50', 26, 26, 8, b'0');
INSERT INTO `xx_sn` (`id`, `create_date`, `modify_date`, `version`, `last_value`, `type`, `deleted`) VALUES (10, '2017-11-30 18:42:24', '2018-03-27 15:50:55', 21, 21, 9, b'0');
INSERT INTO `xx_sn` (`id`, `create_date`, `modify_date`, `version`, `last_value`, `type`, `deleted`) VALUES (11, '2018-01-10 18:11:16', '2018-04-10 12:16:05', 11, 11, 10, b'0');



INSERT INTO `xx_payment_method` (`id`, `create_date`, `modify_date`, `version`, `orders`, `content`, `description`, `icon`, `method`, `name`, `timeout`, `type`, `deleted`) VALUES (1, '2015-12-01 00:58:54', '2015-12-01 00:58:54', 0, 1, NULL, '支持支付宝、财付通、快钱以及大多数网上银行支付', 'http://image.demo.shopxx.net/4.0/201501/b0b6da31-6abf-4824-8dfa-c1f251732e20.gif', 0, '网上支付', 1440, 0, b'0');
INSERT INTO `xx_payment_method` (`id`, `create_date`, `modify_date`, `version`, `orders`, `content`, `description`, `icon`, `method`, `name`, `timeout`, `type`, `deleted`) VALUES (2, '2015-12-01 00:58:55', '2015-12-01 00:58:55', 0, 2, '<p><img src="http://image.demo.shopxx.net/4.0/201501/c4e81c9e-7ce0-4cb8-b7b4-1473d2233110.jpg"/></p>', '支持工商银行、建设银行、农业银行汇款支付，收款时间一般为汇款后的1-2个工作日', 'http://image.demo.shopxx.net/4.0/201501/c0a00d6b-b144-43c3-ad49-ac226be9288f.gif', 1, '银行汇款', 10080, 0, b'0');
INSERT INTO `xx_payment_method` (`id`, `create_date`, `modify_date`, `version`, `orders`, `content`, `description`, `icon`, `method`, `name`, `timeout`, `type`, `deleted`) VALUES (3, '2015-12-01 00:58:56', '2015-12-01 00:58:56', 0, 3, NULL, '由快递公司送货上门，您签收后直接将货款交付给快递员', 'http://image.demo.shopxx.net/4.0/201501/ac183b5f-edcb-48b7-9961-f50f1b16d45c.gif', 1, '货到付款', NULL, 1, b'0');


INSERT INTO `xx_shipping_method` (`id`, `create_date`, `modify_date`, `version`, `orders`, `continue_weight`, `default_continue_price`, `default_first_price`, `description`, `first_weight`, `icon`, `name`, `default_delivery_corp`, `deleted`) VALUES (1, '2015-12-01 00:59:02', '2017-02-14 10:53:44', 1, 1, 1000, 0.000000, 0.000000, '系统将根据您的收货地址自动匹配快递公司进行配送，享受免运费服务', 10000, 'http://image.demo.shopxx.net/4.0/201501/473d43a5-f519-4d31-bc96-a90599aaf4a7.gif', '普通快递', NULL, b'0');
INSERT INTO `xx_shipping_method` (`id`, `create_date`, `modify_date`, `version`, `orders`, `continue_weight`, `default_continue_price`, `default_first_price`, `description`, `first_weight`, `icon`, `name`, `default_delivery_corp`, `deleted`) VALUES (2, '2015-12-01 00:59:03', '2017-02-14 10:53:44', 1, 2, 1000, 1.000000, 10.000000, '支持货到付款，不享受免运费服务', 10000, 'http://image.demo.shopxx.net/4.0/201501/769c0550-1f8f-4313-a2c8-f79c30162b96.gif', '顺丰速运', NULL, b'0');



INSERT INTO `xx_delivery_corp` (`id`, `create_date`, `modify_date`, `version`, `orders`, `code`, `name`, `url`, `deleted`) VALUES (5, '2017-02-14 10:53:29', '2017-02-14 10:53:59', 1, 1, NULL, '顺丰', NULL, b'0');
INSERT INTO `xx_delivery_corp` (`id`, `create_date`, `modify_date`, `version`, `orders`, `code`, `name`, `url`, `deleted`) VALUES (6, '2017-02-14 10:53:52', '2017-02-14 10:54:08', 1, 2, 'debangwuliu', '德邦物流', 'http://www.deppon.com', b'0');
INSERT INTO `xx_delivery_corp` (`id`, `create_date`, `modify_date`, `version`, `orders`, `code`, `name`, `url`, `deleted`) VALUES (7, '2017-03-27 13:52:48', '2017-03-27 13:52:48', 0, NULL, NULL, '自有物流', NULL, b'0');
