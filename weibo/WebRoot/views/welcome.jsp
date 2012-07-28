<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
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
  
  <body>
  <h1>Weibo stub</h1>
  <style>
body{
	background:url("http://qimeng.appsina.com/images/1.jpg");
}
</style>
<script src="http://tjs.sjs.sinajs.cn/t35/apps/opent/js/frames/client.js" language="JavaScript"></script>
<script> 
function authLoad(){
 	App.AuthDialog.show({
	client_id : '1876388041',    //��ѡ��appkey
	redirect_uri : 'http://apps.weibo.com/vinsiatest/weibot/home',     //��ѡ����Ȩ��Ļص���ַ�����磺http://apps.weibo.com/giftabc
	height: 120    //��ѡ��Ĭ�Ͼඥ��120px
	});
}
authLoad()
</script>
     
  </body>
</html>
