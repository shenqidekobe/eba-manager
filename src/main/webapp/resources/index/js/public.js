

$(function(){



    var index=0,preIndex=0,lunboInterval=null,
        imgNum = $(".images .imgRun").length;
        console.log(imgNum);

    var scrollImg = function(){
        if(index == imgNum-1 && preIndex == 0){
            $(".images .imgRun").eq(preIndex).stop(true,true).animate({"left":"-100%"},1000);
            $(".images .imgRun").eq(index).css("left","100%").stop(true,true).animate({"left":"0"},1000);
        }else if(index == 0&&preIndex == imgNum-1){
            $(".images .imgRun").eq(preIndex).stop(true,true).animate({"left":"-100%"},1000);
            $(".images .imgRun").eq(index).css("left","100%").stop(true,true).animate({"left":"0"},1000);
        }else if(index>preIndex){
            $(".images .imgRun").eq(preIndex).stop(true,true).animate({"left":"-100%"},1000);
            $(".images .imgRun").eq(index).css("left","100%").stop(true,true).animate({"left":"0"},1000);
        }else if(index<preIndex){
            $(".images .imgRun").eq(preIndex).stop(true,true).animate({"left":"100%"},1000);
            $(".images .imgRun").eq(index).css("left","-100%").stop(true,true).animate({"left":"0"},1000);
        }

        $(".image_B span").removeClass("selected");
        $(".image_B span").eq(index).addClass("selected");

    }

    stopImg = function(){
        if(lunboInterval != null){
            clearInterval(lunboInterval);
        }
    }


    startImg = function(){
        lunboInterval = setInterval(function(){
            index++;
            if(index>=imgNum){
                index = 0;
                preIndex = imgNum-1;
            }
            scrollImg();
            preIndex = index;


        },6000)
    }
    startImg();


    $(".image_B").css("left",($(window).width()-imgNum*30)/2);


    $(".image_B span").on("click",function(){

        index = $(this).index();
        scrollImg();
        preIndex = index;
        stopImg();
        startImg();

    });

    //左侧滚动
    $(document).on('click','.bar-scroll',function () {
        $('html,body').animate({scrollTop: '0px'}, 800);
    });



})









