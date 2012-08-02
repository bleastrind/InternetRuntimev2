if (!window.InternetRuntime)
	window.InternetRuntime = {};


	



window.InternetRuntime.Client = new function()
{
	var XY;
	var DXY;
	var Create;
	var Point;
	function InitWithLib(callback)
	{
		XY = window.InternetRuntime.UI.XY;
		DXY = window.InternetRuntime.UI.DXY;
		Create = window.InternetRuntime.UI.Create;
		Point = new function()
		{	
			this.fresh = function()
			{ 
				this.ScreenCenter = new XY
								(document.body.scrollLeft + window.innerWidth / 2
								,document.body.scrollTop + window.innerHeight / 2);
				this.ScreenRightbottom = new XY
								(document.body.scrollLeft + document.documentElement.clientWidth
								,document.body.scrollTop + window.innerHeight);
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
		LOGO_SRC: '/assets/client/InternetRuntime.png',
		
		MARKET_URL: 'http://localhost:9001',

		
		PAGE_LOAD_SIGNAL_NAME: 'clients/pageload'
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
		var CoreIframeSize = new DXY(1, 1);
		CoreIframe = Create('iframe')
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
	
	var CallBackPoolId = 0;
	var CallBackPool = {};
	var CallBackParamsPool = {}
	this.initOption = function(signalname, callback)
	{
		var Id = CallBackPoolId;
		CallBackPoolId++;
		CallBackPool[Id] = callback
		CORS.initOption(signalname, Id);
	}
	

	
	
	this.init = function(signalname, params, callback)
	{
		var Id = CallBackPoolId;
		CallBackPoolId++;
		CallBackPool[Id] = callback;		
		CORS.init(signalname, params, Id);
	}
	
	this.queryApp = function(appid, callback, callbackparams)
	{
		var Id = CallBackPoolId;
		CallBackPoolId++;
		CallBackPool[Id] = callback;
		CallBackParamsPool[Id] = callbackparams;
		CORS.queryApp(appid, Id);
	}
	
	function jumpToUrl(url)
	{
		document.location.href = url;
	}
	function showIframe(xy, size)
	{
		CoreIframe.Size(size)
		.Size(new DXY(size.dx, size.dy));
		CoreIframe.DOMObject.style.zIndex = 2;
		
		var IframeBox = Create('div')
		.Size(new DXY(size.dx, size.dy))
		.Style('first')
		.WindowFather();
		
		var ExitButton = Create('div')
		.Size(new DXY(70, 27))
		.Text('Close')
		.XY(new XY(size.dx - 80, size.dy - 40))
		.Style('item')
		.HoverIn(function(done){
			ExitButton
			.Time(150)
			.CallBack(done)
			.Color('ff0000')
			.TextColor('066099');
		})
		.HoverOut(function(done){
			ExitButton
			.Time(150)
			.CallBack(done)
			.Color('066099')
			.TextColor('ff0000');
		})
		.Click(function(){
			IframeBox
			.Opacity(1)
			.Time(300)
			.Opacity(0);
			CoreIframe
			.Opacity(1)
			.Time(300)
			.CallBack(function(){
				IframeBox
				.Style('hidden');
				CoreIframe
				.Time(0)
				.XY(new XY(1, 1))
				.Size(new DXY(1, 1));
				ExitButton
				.Style('hidden');
			})
			.Opacity(0);
		})
		.Father(IframeBox);
		ExitButton.DOMObject.style.zIndex = 200;
		
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
			eval: function(data)
			{
				eval(data.script);
			},
			initOptionBack: function(data)
			{
				CallBackPool[data.id](data.option);
			},	
			initBack: function(data)
			{
				if (data.id != null)
					CallBackPool[data.id](data.data);
			},
			queryAppBack: function(data)
			{
				if (data.id != null)
					CallBackPool[data.id](data.data, CallBackParamsPool[data.id]);
			},
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
		this.pageLoad = function()
		{
			var msg = 
			{
				type: 'pageLoad',
				signalname: CONST.PAGE_LOAD_SIGNAL_NAME
			}
			postMessage(msg);
		}		
		this.initOption = function(signalname, id)
		{
			var msg = 
			{
				type: 'initOption',
				signalname: signalname,
				id: id
			}
			postMessage(msg);
		}
		this.init = function(signalname, params, id)
		{
			var msg =
			{
				type: 'init',
				signalname: signalname,
				params: params,
				id: id
			}
			postMessage(msg);
		}
		this.queryApp = function(appid, id)
		{
			var msg =
			{
				type: 'queryApp',
				appid: appid,
				id: id
			}
			postMessage(msg);
		}
		this.loginByJump = function()
		{
			var msg = 
			{
				type: 'loginByJump',
				oldurl: document.location.href
			}
			postMessage(msg);
		}
		this.registerByJump = function()
		{
			var msg = 
			{
				type: 'registerByJump',
				oldurl: document.location.href
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
			.Src(CONST.BASE_URL + CONST.LOGO_SRC);
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
			.CallBack(CORS.start);
			LoadingPanel
			.Opacity(1)
		}
		showLoadingPanel();
	}
	function loadingFinish(username)
	{
		
		function hideLoadingPanel()
		{
			var SmallSize = new DXY(23, 10);
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
			
			window.addEventListener('scroll', ScrollFresh, false);
			function ScrollFresh()
			{
				Point.fresh();
				UserPanelWrapper.XY(Point.ScreenRightbottom.minus(UserPanelWrapper.Size()));
			}
			
			UserPanelTag
			.HoverIn(showUserPanel, true);
			UserPanelWrapper
			.HoverOut(hideUserPanel, true);
			
			setPanel(username);
			
		}
		function showUserPanel(done)
		{
			var ShowTargetXY = new XY(UserPanelTag.Size().dx + 10, 0);
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
			
			
			var LoginButton = Create('a')
			.Text('Login')
			.Href('#')
			.XY(new XY(35, 80))
			.Click(function(done){
				CORS.loginByJump();
			});
			var RegisterButton = Create('a')
			.Text('Register')
			.Href('#')
			.XY(new XY(100, 80))
			.Click(function(done){
				CORS.registerByJump();
			})
			var MarketButton = Create('a')
			.Text('Market')
			.Href('#')
			.XY(new XY(35, 80))
			.Click(function(done){
				jumpToUrl(CONST.MARKET_URL)
			})
			if (username == null || username == "")
			{	
				var NotLoginLabel = Create('h3')
				.Text('You need login first!')
				.XY(new XY(25, 30));
				UserPanel
				.Child(NotLoginLabel)
				.Child(LoginButton)
				.Child(RegisterButton)
			}
			else
			{	
				CORS.pageLoad();
				var WelcomeLabel = Create('h3')
				.XY(new XY(25, 30))
				.Text('Hello ' + username + '!');
				UserPanel
				.Child(WelcomeLabel)
				.Child(MarketButton)
			}
		}
		
		hideLoadingPanel();
		
	}
	
}

