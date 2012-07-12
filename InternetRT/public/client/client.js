if (!window.InternetRuntime)
	window.InternetRuntime = {};
if (!window.InternetRuntime.Client)
window.InternetRuntime.Client = {};



window.InternetRuntime.Client = new function()
{
	var CONST = 
	{
		BASE_URL: 'http://localhost:9000',
		CORE_IFRAME_SRC: this.BASE_URL + '/assets/client/Client.html';
	}
	
	function loadCore = 
	{
		var CoreIframe = document.createElement('iframe');
		CoreIframe.onready = function()
		{
			CORS.start();
		}
		CoreIframe.src = CONST.CORE_IFRAME_SRC;
		CoreIframe.style.width = 1 + 'px';
		CoreIframe.style.height = 1 + 'px';
		document.body.appendChild(CoreIframe);
	}
	
	
	
	function jumpToUrl(url)
	{
		document.location.href = url;
	}
	
	
	
	var CORS = new function
	{
		//	Receive
		window.onmessage = function(e)
		{
			MessageHandler[e.data.type](e.data);
		}
		var MessageHandler = 
		{
			jumpToUrl: function(data)
			{
				jumpToUrl(data.url);
			},
			unknownUser: function(data)
			{
				
			},
			knownUser: function(data)
			{
				
			}
			/*
			initOption: function(data)
			{
				window.InternetRuntime.Communication.initOption(data.signalname);
			},*/			
		}
		
		//	Send
		function postMessage(msg)
		{
			window.top.postMessage(msg, CONST.BASE_URL);
		}
		this.loginByJump = function()
		{
			var msg = 
			{
				type: 'loginByJump'
			}
			postMessage(msg);
		}
		this.registerByJump = function()
		{
			var msg = 
			{
				type: 'registerByJump'
			}
			postMessage(msg);
		}
		this.start = function()
		{
			var msg = 
			{
				type: 'start'
			}
			postMessage(msg);
		}
	}

	
	loadCore();
}



//test
function testLogin()
{
	gClient.login('333', '333');	
}
function testRegister()
{
	gClient.register('333', '333');	
}	
function testInitOption()
{
	gClient.initOption('share');	
}	