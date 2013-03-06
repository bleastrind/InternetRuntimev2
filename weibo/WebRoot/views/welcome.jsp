<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'welcome.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  
  <body>&nbsp; 
  <style>
body{
	background:url("http://qimeng.appsina.com/images/1.jpg");
}
</style>
<script src="http://tjs.sjs.sinajs.cn/t35/apps/opent/js/frames/client.js" language="JavaScript"></script>
<script> 
function authLoad(){
 	App.AuthDialog.show({
	client_id : '1876388041',    //必选，appkey
	redirect_uri : 'http://other.internetrt.org:8080/weibot/home',     //必选，授权后的回调地址，例如：http://apps.weibo.com/giftabc
	height: 120    //可选，默认距顶端120px
	});
}
authLoad()
</script>
    欢迎使用IRT分享工具 <br>
  </body>
</html>
