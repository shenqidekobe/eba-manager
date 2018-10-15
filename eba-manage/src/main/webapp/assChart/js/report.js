
$(function(){

	//商品同步统计
	$(".goodsSync span").on("click",function(){

        $(".goodsSync span").css({"background":"none"});
        $(this).css({"background":"#ffffff"});
	    var type = $(this).attr("data-type");
	    //调用接口
		goodsSyncTest(type);
	});
	//清单统计统计
	$(".orderOwn span").on("click",function(){
        $(this).siblings().removeClass("selected");
        $(this).addClass("selected");

	    var type = $(this).attr("data-type");
	    //调用接口
		orderOwnTest(type);
	});


    //var xData = ['周一','周二','周三','周四','周五','周六','周日'];
    //var values = [11, 11, 15, 13, 12, 13, 10];
    var xData=[],values=[];

    // var unionId = 'o1mHm0tirjka1xAEdgWCbtrae8X8';
    // var unionId1 = 'o1mHm0kFERids2B9e_3wwZ1vQ_WM';
    var unionId = GetQueryString("unionId");
    //alert(unionId);
    //var url = "https://tsmall.dinghuo.me";
    var url ='';
    orderOwnTest("thisWeek");
	function orderOwnTest(type){
		$.ajax({
            type:"get",
            url:url+"/ass/assList/assListStatistics.jhtml",
            data:{
               unionId: unionId,
               type:type
            },
            success:function(obj){
                console.log(obj);
                if(obj.code == 0){
                    xData = [];
                    values = [];
                    $(".ownOrderSum").html(obj.data.count);
                    obj.data.dtos.forEach(function(val){
                        xData.push(val.createDate);
                        values.push(val.number);
                    })
                    var myChart2 = echarts.init(document.getElementById("main2"));
                    var option2=getOption(xData,values,'(单位：笔)');
                    myChart2.setOption(option2);
                }
                
            }
        });
	}
	

	
    goodsShareTest();
    //商品分享统计
    function goodsShareTest(){
        $.ajax({
            type:"get",
            url:url+"/ass/customerRelation/getShareAllVisit.jhtml",
            data:{
               unionId: unionId
            },
            success:function(obj){
                console.log(obj);
                if(obj){
                    $(".shareNum span").html(obj.data.shareCount);
                    $(".pageVisit span").html(obj.data.pageAllVisit);
                    $(".goodVisit span").html(obj.data.goodsAllVisit);
                }
            }
        });
    }

    goodsSyncTest("thisWeek");
    //商品同步统计
    function goodsSyncTest(type){
        $.ajax({
            type:"get",
            url:url+"/ass/goodsSyncLog/getTotal.jhtml",
            data:{
               unionId: unionId,
               ts:type
            },
            success:function(obj){
                console.log(obj);
                if(obj.code == 0){
                    xData = [];
                    values = [];
                    $(".syncSum").html(obj.data.total);
                    obj.data.list.forEach(function(val){
                        xData.push(val.reportDate);
                        values.push(val.syncNumber);
                    })
                    var myChart = echarts.init(document.getElementById("main"));
                    var option=getOption(xData,values,'(单位：次)');
                    myChart.setOption(option);
                }
                
            }
        });
    }


    function getOption(xData,values,subtext){
        var option = {
            title: {
                text: '',
                subtext: subtext
            },
            tooltip: {
                trigger: 'axis'
            },
            xAxis:  {
                type: 'category',
                boundaryGap: false,
                data: xData,
                axisLine:{
                    lineStyle:{
                        color:'#bbbbbb'
                    }
                }
            },
            yAxis: {
                type: 'value',
                axisLabel: {
                    formatter: '{value}'
                },
                axisLine:{
                    lineStyle:{
                        color:'#bbbbbb'
                    }
                }
            },
            series: [
                {
                    name:'',
                    type:'line',
                    smooth:true,
                    data:values,
                    areaStyle: {normal:{}},
                    itemStyle : {
                        normal : {
                            lineStyle:{
                                color:"#4DA1FF"
                            },
                            color: new echarts.graphic.LinearGradient(
                                0, 0, 0, 1,
                                [
                                    {offset: 0, color: '#C7E5FF'},
                                    {offset: 1, color: '#ffffff'}
                                ]
                            )
                        }
                    }
                }
            ]
        };
        return option;
    }


    // 问题
    var event_f = function(e){e.preventDefault()};
    $(".question_o").on("click",function(){
        if($(this).hasClass("q_select")){
            $(this).removeClass("q_select");
            $(".modelDiv").css("display","none");
            $(this).siblings(".center_q").css("display","none");
            document.body.removeEventListener('touchmove', event_f, false);
        }else{
            $(this).addClass("q_select");
            $(".modelDiv").css("display","block");
            $(this).siblings(".center_q").css("display","block");
            document.body.addEventListener('touchmove', event_f, false);
        }
    });
    $(".modelDiv").on("click",function(){
        $(".question_o").removeClass("q_select");
        $(".modelDiv").css("display","none");
        $(".question_o").siblings(".center_q").css("display","none");
        document.body.removeEventListener('touchmove', event_f, false);
    });


    //跳转到商品分享统计goodShareCount
    $("#goodsShareCount").on("click",function(){
        //console.log(111);
        wx.miniProgram.navigateTo({url: '/pages/goodShareCount/goodShareCount'});
    });
    //跳转到商品同步统计goodSync
    $("#goodsSyncCount").on("click",function(){
        //console.log(222);
        wx.miniProgram.navigateTo({url: '/pages/goodSync/goodSync'});
    });
    //跳转到采购清单统计showAsslist 
    $("#orderOwnCount").on("click",function(){
        //console.log(333);
        wx.miniProgram.navigateTo({url: '/pages/showAsslist/showAsslist'});
    });


    //选中地址栏中的地址，截取需要的name部分
    function GetQueryString(name) {
        //alert(window.location);
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
        var r = window.location.search.substr(1).match(reg);
        if (r != null)
            return unescape(r[2]);
        return null;
    }


    function stopDrop() {
        var lastY;//最后一次y坐标点
        $(document.body).on('touchstart', function(event) {
            lastY = event.originalEvent.changedTouches[0].clientY;//点击屏幕时记录最后一次Y度坐标。
        });
        $(document.body).on('touchmove', function(event) {
            var y = event.originalEvent.changedTouches[0].clientY;
            var st = $(this).scrollTop(); //滚动条高度
            if (y >= lastY && st <= 10) {//如果滚动条高度小于0，可以理解为到顶了，且是下拉情况下，阻止touchmove事件。
                lastY = y;
                event.preventDefault();
            }
            lastY = y;

        });
    }
    stopDrop();

})














