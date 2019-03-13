/pages/index/cart/show/index?id=1

welcome发起跳转到api/member/login 再到b2b/activation2 再转发到/pages/index/index
小程序流程：
调用wx.login 获取code，请求后台接口获取用户openId、sessionKey
缓存openId和sessionKey
进入首页，加载首页数据
绑定手机号、获取微信手机号弹出验证码绑定

业务对象：
商品-商品分类-商品规格
购物车-订单-支付-订单记录-发货
用户-店主

用户分享带parentOpenId带来下级用户
用户支付成功回调通知处理商品返利信息
模版消息通知：返利通知上级、


权限点增加：日志、会员管理、提现管理


small.template.common.templateId：AssListController的消息通知
small.template.common.microBusiness.templateId：addShareNotes添加分享备注通知
order.template.common.templateId：订单备注、收货消息通知
childMember.template.common.templateId：分享得到的下级用户通知



11.29BUG:
购物车选择、全选 返回和重新进入有问题、下单选择某几个来回的时候出现问题
未选择地址的提示，保存地址的bug
下单没有地址，新增地址后重新选择地址回下单页
生成海报的图片，我的店铺二维码调用member/getQRCode.jhtml

12.6
小程序模版消息的formId只能是自己产生的发送给自己，且最多只能发送三条，支付ID只能发送一次
