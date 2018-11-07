小程序流程：
调用wx.login 获取code，请求后台接口获取用户openId、sessionKey
缓存openId和sessionKey
进入首页，加载首页数据
绑定手机号、获取微信手机号弹出验证码绑定