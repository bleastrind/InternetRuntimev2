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
		BASE_URL: 'http://internetrt.org:9000',
		CORE_IFRAME_SRC: '/assets/client/Client.html',
		Lib_SRC: '/assets/client/Lib.js',
		
		MARKET_URL: 'http://market.internetrt.org:9001',
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
			loading();
			
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
				loadingFinish(null);
			},
			knownUser: function(data)
			{
				loadingFinish(data.username);
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
	
	var LoadingPanel
	function loading()
	{
		LoadingPanel = new function()
		{
			var LoadingPanelSize = new DXY(250, 70);
			var LoadingPanelCenterXY = Point.ScreenCenter;
			var LoadingLogoSize = new DXY(231, 24);
			var LoadingLogoCenterXY = new XY(125, 30);
			var	LoadingLogo = Create('img')
			.Size(LoadingLogoSize)
			.CenterXY(LoadingLogoCenterXY)
			.Src('InternetRuntime.png');
			return Create('div')
			.Size(LoadingPanelSize)
			.CenterXY(LoadingPanelCenterXY)
			.Style('first')
			.Child(LoadingLogo);
		}
		function showLoadingPanel()
		{
			LoadingPanel
			.WindowFather()
			.Opacity(0)
			.Time(600)
			.CallBack(CORS.start())
			.Opacity(1)
		}
		showLoadingPanel();
	}
	function loadingFinish(username)
	{
		function hideLoadingPanel()
		{
			var SmallSize = new InternetRuntime.UI.DXY(23, 10);
			LoadingPanel
			.Time(300)
			.To(Point.ScreenRightbottom.minus(SmallSize.scale(1.2)))
			.Opacity(0)
			.Move()
			.CallBack(InitUserPanel)
			.Size(SmallSize);
		}
		
		var UserPanelTag = new function()
		{
			var UserPanelTagSize = new DXY(25, 70);			
			return Create('div')
			.Size(UserPanelTagSize)
			.Style('tag')			
		}
		var UserPanel = new function()
		{			
			var UserPanelSize = new DXY(250, 150);
			var UserPanelTagLeftBottomXY = new XY(-2, UserPanelSize.dy);
			UserPanelTag.RightBottomXY(UserPanelTagLeftBottomXY);
			return Create('div')
			.Size(UserPanelSize).Style('first')
			.Child(UserPanelTag);
			
		}
		var UserPanelWrapper = new function()
		{
			var UserPanelWrapperSize = new DXY(UserPanel.Size().dx + UserPanelTag.Size().dx, UserPanel.Size().dy); 
			return Create('div')
			.Size(UserPanelWrapperSize)
			.Style('wraper')
		}
		var UserPanelInitPos = new XY(UserPanelWrapper.Size().dx, 0);
		function InitUserPanel()
		{	
			LoadingPanel.Style('hidden');
			UserPanel
			.XY(UserPanelInitPos);
			UserPanelWrapper.WindowFather()
			.Child(UserPanel)
			.XY(Point.ScreenRightbottom.minus(UserPanelWrapper.Size()));
			document.body.onscroll = function()
			{
				Point.fresh();
				UserPanelWrapper.XY(Point.ScreenRightbottom.minus(UserPanelWrapper.Size()));
			}
			
			UserPanelTag
			.HoverIn(showUserPanel);
			UserPanelWrapper
			.HoverOut(hideUserPanel);
			
		}
		function showUserPanel(done)
		{
			var ShowTargetXY = new XY(UserPanelTag.Size().dx, 0);
			UserPanel
			.Time(300)
			.To(ShowTargetXY)
			.CallBack(done)
			.Move();
		}
		function hideUserPanel(done)
		{
			UserPanel
			.Time(300)
			.To(UserPanelInitPos)
			.CallBack(done)
			.Move();			
		}
		
		function setPanel(username)
		{
			var NotLoginLabel = Create('h3')
			.Text('You need login first!');
			var LoginButton = Create('a')
			.Text('Login')
			.Href('#')
			.Click(function(done){
				CORS.loginByJump();
			});
			var RegisterButton = Create('a')
			.Text('Login')
			.Href('#')
			.Click(function(done){
				CORS.registerByJump();
			})
			var MarketButton = Create('a')
			.Text('Login')
			.Href('#')
			.Click(function(done){
				jumpToUrl(CONST.MARKET_URL)
			})
			if (username == null)
			{
				
			}
			else
			{
				
			}
		}
		
		hideLoadingPanel();
		
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