if (!window.InternetRuntime)
	window.InternetRuntime = {};


	



window.InternetRuntime.Client = new function()
{
	var UI;
	var Point;
	function InitWithLib(callback)
	{
		UI = window.InternetRuntime.UI;
		Point = new function()
		{	
			this.fresh = function()
			{
				this.ScreenCenter = new UI.XY
								(document.body.scrollLeft + window.innerWidth / 2
								,document.body.scrollTop + window.innerHeight / 2);
				this.ScreenRightbottom = new UI.XY
								(document.body.scrollLeft + document.body.clientWidth
								,document.body.scrollTop + document.body.clientHeight);
			}
			this.fresh();
		}
		loadCore(callback);
	}
	var CONST = 
	{
		BASE_URL: 'http://localhost:9000',
		CORE_IFRAME_SRC: '/assets/client/Client.html',
		Lib_SRC: '/assets/client/Lib.js'
	}
	
	var CoreIframe;
	function Init(func)
	{		
		loadUILib(func);
	}
	function loadUILib(callback)
	{
		
		if (!window.InternetRuntime.UI)
		{
			
			var LibScritp = document.createElement('script');
			document.body.appendChild(LibScritp);
			LibScritp.onload = function()
			{
				InitWithLib(callback);
			}
			
			LibScritp.src = CONST.BASE_URL + CONST.Lib_SRC;
		}
		else
			InitWithLib(callback);
	}
	function loadCore(callback)
	{	
		var CoreIframeSize = new UI.DXY(1, 1);
		CoreIframe = UI.Create('iframe')
		.LoadFunc(callback)
		.Src(CONST.BASE_URL + CONST.CORE_IFRAME_SRC)
		.Size(CoreIframeSize)
		.WindowFather();
	}
	

	//	API
	this.runClient = function()
	{
		Init(function(){
			CORS.start();
		});
	}
	
	this.installRootApp = function()
	{
		
		Init(function(){
			
			CORS.installRootApp();
		});
	}
	
	
	
	
	function jumpToUrl(url)
	{
		document.location.href = url;
	}
	function showIframe(xy, size)
	{
		CoreIframe.Size(size)
		.Size(new UI.DXY(size.dx, size.dy));
		CoreIframe.DOMObject.style.zIndex = 2;
		
		var IframeBox = UI.Create('div')
		.Size(new UI.DXY(size.dx, size.dy))
		.Style('first')
		.WindowFather();
		
		if (xy == 'center')
		{
			IframeBox.CenterXY(Point.ScreenCenter);
			CoreIframe.CenterXY(Point.ScreenCenter);
		}
		else
		{
			IframeBox.XY(xy);
			CoreIframe.XY(xy);
		}
		
		IframeBox
		.Opacity(0)
		.Time(300)
		.Opacity(1);
		CoreIframe
		.Opacity(0)
		.Time(300)
		.Opacity(1);
		
		
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
			CoreIframe.DOMObject.contentWindow.postMessage(msg, CONST.BASE_URL);
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