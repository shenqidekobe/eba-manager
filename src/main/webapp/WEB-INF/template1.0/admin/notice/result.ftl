[#escape x as x?html]
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title></title>
    <style>
        body{
            background:#f9f9f9;
        }
        .box{
            text-align: center;
            padding-top:200px;
        }
        img{
            width:300px;
            height:300px;
        }
        p{
            font-size:40px;
            line-height: 100px;
        }
    </style>
</head>

<body>
<div class="box">


    <img src="${base}/resources/admin1.0/images/wancheng.svg" alt="" />

    <p>${message}</p>




</div>

</body>
</html>
[/#escape]