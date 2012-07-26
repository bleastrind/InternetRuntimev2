<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <title>Renren Webcanvas Demo -- Home</title>
</head>
<body>
  <img src="${requestScope.userHead}"/>
  <p>你好，${requestScope.userName}</p>	
  <p>${requestScope.feedName }</p>
  <p>${requestScope.feedMessage }</p>
  <p>人人stub正在运行中...</p>
</body>
</html>