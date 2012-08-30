if (!window.InternetRuntime)
	window.InternetRuntime = {};

if (!window.InternetRuntime.Client)
{
	window.InternetRuntime.BASE_URL = 'http://localhost:9000';
	window.InternetRuntime.CLIENT_URL = window.InternetRuntime.BASE_URL + '/assets/client/client.js';
	var ClientScript = document.createElement('script');
	document.body.appendChild(ClientScript);
	ClientScript.onload = function()
	{	
		window.InternetRuntime.Client.runClient();
	}
	ClientScript.src = window.InternetRuntime.CLIENT_URL;
}
else
{
	window.InternetRuntime.Client.runClient();	
}

