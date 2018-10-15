[#assign shiro = JspTaglibs["/WEB-INF/tld/shiro.tld"] /]
[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <title></title>
    <meta name="author" content="UTLZ Team"/>
    <meta name="copyright" content="UTLZ"/>
    <link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
    <script type="text/javascript">
        var haveIndexPerm = false ;
            [@shiro.orPermission name="admin:orderReport:orderList or admin:orderReport:purchaseList"]
            haveIndexPerm = true ;
            [/@shiro.orPermission]
        if(haveIndexPerm){
            //有权限的首页地址
            window.location.href = "../orderReport/index.jhtml" ;
        }else{
            //没权限的首页地址
            window.location.href = "../common/noAuthority.jhtml" ;
        }
    </script>
</head>
<body>
</body>
</html>
[/#escape]