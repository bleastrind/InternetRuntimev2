function getPic(token)
{
	var params = {};							
	params.token = token;
	params.reqWidth = 400;
	params.reqHeight = 500;
	window.InternetRuntime.Client.init("getPic", params, function(){});
	
	websocket = new WebSocket("ws://192.168.3.160:9003/refreshPic?token=" + token);
	websocket.onmessage = function(evt) 
	{ 
		document.getElementById("face").src = "http://192.168.3.160:9003/getPic/face.jpg?token=" + evt.data;
	};
}
