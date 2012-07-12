<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <title>Renren Webcanvas Demo -- Welcome</title>
  <script type="text/javascript" src="../js/renren_js_sdk/renren.js"></script>
</head>
<body>&nbsp; 
<script type="text/javascript">
  	try{
	  var uiOpts = {
		  url : "http://graph.renren.com/oauth/authorize",
		  display : "iframe",
		  params : {"response_type":"token","client_id":"${requestScope.appId}","scope":"publish_feed read_user_feed"},
		  onSuccess: function(r){
		    top.location = "http://apps.renren.com/vinsiademo/home";
		  },
		  onFailure: function(r){} 
	  };
	  Renren.ui(uiOpts);
	  }catch(err){
	  alert(err)
	  }
  </script> 
  Hi, Welcome!</body>
</html>