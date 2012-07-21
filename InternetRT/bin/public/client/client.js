if (!window.InternetRuntime)
	window.InternetRuntime = {};
if (!window.InternetRuntime.Client)
window.InternetRuntime.Client = {};



window.InternetRuntime.Client = new function()
{
	var CONST = 
	{
		BASE_URL: 'http://internetrt.org:9000',
		CORE_IFRAME_SRC: '/assets/client/Client.html'
	}
	
	var CoreIframe;
	
	function loadCore()
	{
		CoreIframe = document.createElement('iframe');
		CoreIframe.onload = function()
		{	
			CORS.installRootApp();
			//CORS.start();
		}
		CoreIframe.src = CONST.BASE_URL + CONST.CORE_IFRAME_SRC;
		CoreIframe.style.width = 1 + 'px';
		CoreIframe.style.height = 1 + 'px';
		document.body.appendChild(CoreIframe);
	}
	
	
	
	function jumpToUrl(url)
	{
		document.location.href = url;
	}
	
	
	function showIframe(xy, size)
	{
		if (xy == 'center')
		{
			CoreIframe.style.left = document.body.scrollLeft + window.innerWidth / 2 - size.dx / 2 + 'px';
			CoreIframe.style.top = document.body.scrollTop + window.innerHeight / 2 - size.dy / 2 + 'px';
		}
		else
		{
			CoreIframe.style.left = xy.x + 'px';
			CoreIframe.style.top = xy.y + 'px';
		}
		CoreIframe.style.width = size.dx + 20 + 'px';
		CoreIframe.style.height = size.dy + 20 + 'px';	
		CoreIframe.style.position = 'absolute';
		CoreIframe.frameBorder = 0;
	}
	
	
	var CORS = new function()
	{
		//	Receive
		window.onmessage = function(e)
		{
			MessageHandler[e.data.type](e.data);
		}
		var MessageHandler = 
		{
			showIframe: function(data)
			{
				showIframe(data.xy, data.size);
			},			
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
			CoreIframe.contentWindow.postMessage(msg, CONST.BASE_URL);
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
		this.installRootApp = function()
		{
			
			var msg = 
			{
				type: 'installRootApp'
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