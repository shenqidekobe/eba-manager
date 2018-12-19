INSERT INTO `xx_admin` (`id`, `create_date`, `deleted`, `modify_date`, `version`, `bind_phone_num`, `boot_page`, `email`, `is_enabled`, `is_locked`, `is_system`, `lock_key`, `locked_date`, `login_date`, `login_failure_count`, `login_ip`, `name`, `password`, `prompts`, `username`, `department`, `supplier`) VALUES (1, '2018-05-16 21:40:58', b'0', '2018-10-26 14:53:02', 181, '18880809203', 0, NULL, b'1', b'0', b'1', '50943e002c993e84b7385d93867ce388', NULL, '2018-10-26 14:53:02', 0, '114.94.199.166', NULL, '1dc356b4741fb72a7fa35389e4502d5c', 0, 'admin', NULL, 1);

INSERT INTO `t_supplier` (`id`, `create_date`, `deleted`, `modify_date`, `version`, `address`, `app_id`, `app_key`, `business_card`, `company_profile`, `customer_service_tel`, `email`, `imagelogo`, `industry`, `invite_code`, `legal_person_name`, `name`, `probation_days`, `qq_customer_service`, `reasons`, `recommend_flag`, `status`, `tel`, `types`, `user_name`, `area`, `member`) VALUES (1, '2018-05-16 21:40:58', b'0', '2018-05-16 21:40:58', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'UDR926', NULL, '均瑶董事会', 60000, NULL, NULL, NULL, 3, NULL, 0, NULL, NULL, NULL);


INSERT INTO `xx_role` (`id`, `create_date`, `modify_date`, `version`, `authorities`, `description`, `is_system`, `name`, `switchs`, `supplier`, `deleted`) 
VALUES (1, '2018-12-17 12:50:58', '2018-12-17 12:50:58', 0, '["admin:homePage","admin:productCategory:add","admin:productCategory:edit","admin:productCategory:delete","admin:specification:add","admin:specification:edit","admin:specification:delete","admin:goods:add","admin:goods:edit","admin:goods:delete","admin:order:checkBatchReview","admin:order:review","admin:order:updateItems","admin:order:shipping","admin:order:addRemarks","admin:order:applyCancel","admin:order:cancel","admin:order:complete","admin:orderReport:orderList","admin:commodityReport:orderForm","admin:ad","admin:adPosition","admin:member:add","admin:member:edit","admin:member:income","admin:withdraw:edit","admin:role:add","admin:role:edit","admin:role:delete","admin:department:add","admin:department:edit","admin:department:delete","admin:admin:add","admin:admin:edit","admin:admin:delete","admin:log"]', '系统管理员', b'0', '总经办', NULL, 1, b'0');


INSERT INTO `xx_admin_role` (`admins`, `roles`) VALUES (1, 1);



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


INSERT INTO `xx_delivery_corp` (`id`, `create_date`, `modify_date`, `version`, `orders`, `code`, `name`, `url`, `deleted`) VALUES (1, '2017-02-14 10:53:52', '2017-02-14 10:54:08', 1, 1, 'zhongtong', '中通', 'https://www.zto.com', b'0');
INSERT INTO `xx_delivery_corp` (`id`, `create_date`, `modify_date`, `version`, `orders`, `code`, `name`, `url`, `deleted`) VALUES (2, '2017-02-14 10:53:52', '2017-02-14 10:54:08', 1, 2, 'shentong', '申通', 'http://www.sto.cn', b'0');
INSERT INTO `xx_delivery_corp` (`id`, `create_date`, `modify_date`, `version`, `orders`, `code`, `name`, `url`, `deleted`) VALUES (3, '2017-02-14 10:53:52', '2017-02-14 10:54:08', 1, 3, 'yunatong', '圆通', 'http://www.yto.net.cn', b'0');
INSERT INTO `xx_delivery_corp` (`id`, `create_date`, `modify_date`, `version`, `orders`, `code`, `name`, `url`, `deleted`) VALUES (4, '2017-02-14 10:53:52', '2017-02-14 10:54:08', 1, 4, 'yunda', '韵达', 'http://www.yundaex.com', b'0');
INSERT INTO `xx_delivery_corp` (`id`, `create_date`, `modify_date`, `version`, `orders`, `code`, `name`, `url`, `deleted`) VALUES (5, '2017-02-14 10:53:29', '2017-02-14 10:53:59', 1, 5, 'shunfeng', '顺丰', 'http://www.sf-express.com', b'0');
INSERT INTO `xx_delivery_corp` (`id`, `create_date`, `modify_date`, `version`, `orders`, `code`, `name`, `url`, `deleted`) VALUES (6, '2017-02-14 10:53:52', '2017-02-14 10:54:08', 1, 6, 'debangwuliu', '德邦', 'http://www.deppon.com', b'0');
INSERT INTO `xx_delivery_corp` (`id`, `create_date`, `modify_date`, `version`, `orders`, `code`, `name`, `url`, `deleted`) VALUES (7, '2017-03-27 13:52:48', '2017-03-27 13:52:48', 0, 7, 'ziyou', '自有物流', NULL, b'0');
