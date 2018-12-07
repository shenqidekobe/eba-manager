[#assign shiro = JspTaglibs["/WEB-INF/tld/shiro.tld"] /]
[#escape x as x?html]

    <footer class="footer_nav">
        <a href="javascript:;" class="nav_li nav_index">首页</a>
        <a href="javascript:;" class="nav_li nav_order ">订单</a>
        <a href="javascript:;" class="nav_li nav_Customer">会员</a>
        <a href="javascript:;" class="nav_li nav_supply">店主</a>
        <a href="javascript:;" class="nav_li nav_setting">设置</a>
    </footer>

    <script type="text/javascript">

        $(function(){

            /*报表*/
            var orderReport = false;
            var commodityReport = false;
            var customerReport = false;


            function orderReportFun(){

                reportBoolTest();
                reportTest();

            }

            function reportBoolTest(){
                /*商品报表是否有权限*/
                [@shiro.orPermission name="admin:commodityReport:orderForm or admin:commodityReport:purchaseOrder"]
                    commodityReport = true;
                [/@shiro.orPermission]
                /*订单报表是否有权限*/
                [@shiro.orPermission name="admin:orderReport:orderList or admin:orderReport:purchaseList"]
                    orderReport = true;
                [/@shiro.orPermission]
                /*客户报表是否有权限*/
                [@shiro.hasPermission name="admin:customerReport"]
                    customerReport = true;
                [/@shiro.hasPermission]
            }
            function reportTest(){//是否有权限，跳转页面
                    if(orderReport){
                        window.location.href = '../orderReport/index.jhtml';
                    }else if(commodityReport){
                        window.location.href = '../commodityReport/index.jhtml';
                    }else if(customerReport){
                        window.location.href = '../customerReport/index.jhtml';
                    }else{
                        errorInfoFun("无操作权限");
                    }
            }
            /*点击报表的按钮判断是否权限*/
            $(".reportClass li").on("click",function(){

                var reportData = $(this).data('report');


                reportBoolTest();
                if(reportData == 'orderReport'){
                    if(orderReport){
                        window.location.href = '../orderReport/index.jhtml';
                    }else{
                        errorInfoFun("无操作权限");
                    }
                }
                if(reportData == 'commodityReport'){
                    if(commodityReport){
                        window.location.href = '../commodityReport/index.jhtml';
                    }else{
                        errorInfoFun("无操作权限");
                    }
                }
                if(reportData == 'customerReport'){
                    if(customerReport){
                        window.location.href = '../customerReport/index.jhtml';
                    }else{
                        errorInfoFun("无操作权限");
                    }
                }

            });
            /*订单*/
            var order = false,
                ownOrder = false,
                distribution = false;

            function orderFun(){
                orderBoolTest();
                orderTest();
            }

            function orderBoolTest(){
                [@shiro.orPermission name="admin:print:orderBatchPrint or admin:order:checkBatchReview or admin:order:getOut or admin:print:order or admin:order:review or admin:order:updateItems or admin:order:shipping or admin:order:applyCancel or admin:order:cancel or admin:order:complete or admin:order:addRemarks or admin:print:orderShippingInfo or admin:order:cancelShipped"]
                    /*订货单*/
                    order = true;
                [/@shiro.orPermission]

                [@shiro.orPermission name="admin:print:verificationDeliveryInfo or admin:ownOrder:add or admin:ownOrder:addMore or admin:ownOrder:getOut or admin:print:purchaseorder or admin:ownOrder:updateItems or admin:ownOrder:applicationCancel or admin:ownOrder:addRemarks or admin:print:deliveryInfor"]
                    /*采购单*/
                    ownOrder = true;
                [/@shiro.orPermission]
                [#if isDistributionModel == "true"]
                [@shiro.orPermission name="admin:print:distributionOrder or admin:distributionOrder:checkBatchReview or admin:distributionOrder:getOut or admin:distributionOrder:review or admin:distributionOrder:applyCancel or admin:distributionOrder:cancel"]
                    /*分销单待审核*/
                    distribution = true;
                [/@shiro.orPermission]
                [/#if]

            }
            function orderTest(){
                    if(order){
                        window.location.href = '../order/list.jhtml';
                    }else if(ownOrder){
                        window.location.href = '../ownOrder/list.jhtml';
                    }else if(distribution){
                        window.location.href = '../distributionOrder/list.jhtml';
                    }else{
                        errorInfoFun("无操作权限");
                    }
            }
            /*点击订单的按钮判断是否权限*/
            $(".orderClass a").on("click",function(){

                var orderData = $(this).data('order');

                orderBoolTest();
                if(orderData == 'order'){
                    if(order){
                        window.location.href = '../order/list.jhtml';
                    }else{
                        errorInfoFun("无操作权限");
                    }
                }
                if(orderData == 'ownOrder'){
                    if(ownOrder){
                        window.location.href = '../ownOrder/list.jhtml';
                    }else{
                        errorInfoFun("无操作权限");
                    }
                }
                if(orderData == 'distributionOrder'){
                    if(distribution){
                        window.location.href = '../distributionOrder/list.jhtml';
                    }else{
                        errorInfoFun("无操作权限");
                    }
                }
            });
            /*客户*/
            var customer = false,
                need = false;

            function customerFun(){
                customerBoolTest();
                customerTest();

            }

            function customerBoolTest(){//判断客户是否有权限
                [@shiro.orPermission name="admin:customerRelation:add or admin:customerRelation:edit or admin:customerRelation:delete"]
                    /*订货单*/
                    customer = true;
                [/@shiro.orPermission]

                [@shiro.orPermission name="admin:need:add or admin:need:edit or admin:need:importMore"]
                    /*采购单*/
                    need = true;
                [/@shiro.orPermission]
            }

            function customerTest(){//客户是否有权限，跳转页面
                    if(customer){
                        window.location.href = '../member/list.jhtml';
                    }else if(need){
                        window.location.href = '../member/list.jhtml';
                    }else{
                        errorInfoFun("无操作权限");
                    }
            }

            /*点击客户的按钮判断是否权限*/
            $(".customerClass li").on("click",function(){

                var customerData = $(this).data('customer');

                customerBoolTest();
                if(customerData == 'customerRelation'){
                    if(customer){
                        window.location.href = '../member/list.jhtml';
                    }else{
                        errorInfoFun("无操作权限");
                    }
                }
                if(customerData == 'need'){
                    if(need){
                        window.location.href = '../member/list.jhtml';
                    }else{
                        errorInfoFun("无操作权限");
                    }
                }
            });
            /*供应*/
            var formalSupply = false,
                needSupply = false,
                supplydistribution = false;

            function supplyFun(){
                supplyBoolTest();
                supplyTest();

            }

            function supplyBoolTest(){
                [@shiro.orPermission name="admin:formalSupply:add or admin:formalSupply:view or admin:formalSupply:edit or admin:formalSupply:delete or admin:formalSupply:updateStatus"]
                    /*企业供应*/
                    formalSupply = true;
                [/@shiro.orPermission]

                [@shiro.orPermission name="admin:needSupply:add or admin:needSupply:view or admin:needSupply:edit or admin:needSupply:delete or admin:needSupply:updateStatus"]
                    /*个体供应*/
                    needSupply = true;
                [/@shiro.orPermission]

                [@shiro.orPermission name="admin:supplyDistribution:view or admin:supplyDistribution:distributionList"]
                    /*供应确认*/
                    supplydistribution = true;
                [/@shiro.orPermission]
            }
            function supplyTest(){
                    if(formalSupply){
                        window.location.href = '../formalSupply/list.jhtml';
                    }else if(needSupply){
                        window.location.href = '../needSupply/list.jhtml';
                    }else if(supplydistribution){
                        window.location.href = '../supplydistribution/list.jhtml';
                    }else{
                        errorInfoFun("无操作权限");
                    }

            }

            /*点击供应的按钮判断是否权限*/
            $(".supplyClass li").on("click",function(){

                var supplyData = $(this).data('supply');

                supplyBoolTest();
                if(supplyData == 'formalSupply'){
                    if(formalSupply){
                        window.location.href = '../formalSupply/list.jhtml';
                    }else{
                        errorInfoFun("无操作权限");
                    }
                }
                if(supplyData == 'needSupply'){
                    console.log(needSupply);
                    if(needSupply){
                        window.location.href = '../needSupply/list.jhtml';
                    }else{
                        errorInfoFun("无操作权限");
                    }
                }
                if(supplyData == 'supplyDistribution'){
                    if(supplydistribution){
                        window.location.href = '../supplyDistribution/list.jhtml';
                    }else{
                        errorInfoFun("无操作权限");
                    }
                }
            });

            /*设置*/
            var wxSettingBool = false;
            function wxSettingFun(){
                
                [@shiro.hasPermission name="admin:enterpriseInfo"]
                    wxSettingBool = true;
                [/@shiro.hasPermission]


                if(wxSettingBool){
                    window.location.href = '../enterpriseInfo/index.jhtml';
                }else{
                    errorInfoFun("无操作权限");
                }

            }
            /*企业信息的权限*/
            $(".qyInfo").on("click",function(){
                wxSettingFun();
            });
            //首页
            $(".nav_index").on("click",function(){
                //window.location = "../orderReport/index.jhtml";
                orderReportFun();
            });
            //订单
            $(".nav_order").on("click",function(){
                //window.location = "../order/list.jhtml";
                orderFun();
            });
            //设置
            $(".nav_setting").on("click",function(){
                window.location = "../wxSetting/index.jhtml";
            });
            //客户
            $(".nav_Customer").on("click",function(){
                //window.location = "../customerRelation/list.jhtml";
                customerFun();
            });
            //供应
            $(".nav_supply").on("click",function(){
                //window.location = "../formalSupply/list.jhtml";
                supplyFun();
            });
        })
    </script>

[/#escape]