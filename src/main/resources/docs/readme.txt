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
