if (!window.InternetRuntime)
	window.InternetRuntime = {};				
window.InternetRuntime.Explorer = new function()
{
	var XY = InternetRuntime.UI.XY;
	var DXY = InternetRuntime.UI.DXY;
	var Create = InternetRuntime.UI.Create;
	
	var CONST = 
	{
		EXPLORER_BASE_URL: 'http://internetrt.org:8080/Explorer',
		ICON_SRC: '/assets/Cloud.png',
		
		SHARE_SIGNAL_NAME: 'share',
		
		ICON_HIDE_DELAY: 1500
		
	}
	
	//check if an object is null or undefined
	function isNUE(obj)
	{	
		return (obj == null || obj == undefined || obj == "");
	}
	
	var OperationObject;
	
	var Filters = 
	{
		link: function(objs)
		{
			var ans = [];
			for (var i = 0; i < objs.length; i++)
				if (!isNUE(objs[i].href))
					ans.push(objs[i]);
			return ans;
		}
	}
	function SearchAllElements(obj)
	{
		var ans = [];
		if (obj.nodeType == 1)
			ans.push(obj);
		var childs = obj.childNodes;			
		for (var c in childs)
			ans = ans.concat(SearchAllElements(childs[c]));		
		return ans;		
	}
	
	var MouseState = 0;		//0:hidden	1:shown
	var AnimeState = 0;		//0:hidden	1:shown
	var DelayStartTime;
	function hoverIn(e)
	{
		MouseState = 1;
		if (AnimeState == 0)
		{
			OperationObject = e.target;
			AnimeState = 1;
			floaticon.show(new XY(e.clientX, e.clientY));
		}
		else
		{
			DelayStartTime = new Date().getTime();
		}
	
	}
	function hoverOut(e)
	{
		MouseState = 0;
		if (AnimeState == 0)
		{}
		else
		{
			DelayStartTime = new Date().getTime();
			function check()
			{
				var NowTime = new Date().getTime();
				if (NowTime - DelayStartTime >= CONST.ICON_HIDE_DELAY)
				{
					if (AnimeState == 1 && MouseState == 0)
					{
						AnimeState = 0;
						floaticon.hide()
					}
				}
				else
					setTimeout(check, 100);
			}
			check();
		}
	}
	
	function close(e)
	{
		if (AnimeState == 1 && !floaticon.Obj.isContained(e.target))
		{
			AnimeState = 0;
			floaticon.hide();
			MouseState = 0;
			AnimeState = 0;
			
		}
	}
	
	
	this.Init = function()
	{
		var objs = SearchAllElements(document.body);
		
		for (var f in Filters)
			objs = Filters[f](objs);
		
		for (var obj in objs)
		{
			objs[obj].addEventListener('mouseover', hoverIn, false);
			objs[obj].addEventListener('mouseout', hoverOut, false);
		}
		window.addEventListener('click', close, false);
		
	}
	
	var GRID = 5;
	function MenuItem(size)
	{
		var Choice;
		var Img;
		var SubMemu;
		var ContainerMenu;
		var MenuOpen = false;
		this.setMenuOpen = function(mo)
		{
			MenuOpen = mo;
		}
		var Obj = Create('div')
		.Style('item')
		.Size(size)
		.HoverIn(function(done){
			Obj
			.Time(150)
			.CallBack(done)
			.Color('ff0000')
			.TextColor('066099');
		})
		.HoverOut(function(done){
			Obj
			.Time(150)
			.CallBack(done)
			.Color('066099')
			.TextColor('ff0000');
		});		
		this.getChoice = function()
		{
			return Choice;
		}
		this.setChoice = function(c)
		{
			Choice = c;
		}
		this.Img = Img;
		this.setClick = function(func)
		{
			Obj
			.Click(function(done){
				if (!MenuOpen)
				{
					MenuOpen = true;
					func(Choice);
				}
				done();
			},
			true);
		}
		this.setSubMemu = function(menu)
		{
			SubMemu = menu;
		}
		
		this.setContainerMenu = function(menu)
		{
			ContainerMenu = menu;
		}
		this.Obj = Obj;
		this.showSubMenu = function()
		{
			var MenuXY = new XY(106, -GRID-2);
			SubMemu.show(Obj, MenuXY);
		}
	}
	function Menu()
	{	
		
		var MenuSize = new DXY(1, GRID);
		var Obj = Create('div')
		.Style('first')
		.SizeOne(MenuSize);
		var items = [];
		this.Obj = Obj;
		this.pushItem = function(item)
		{
			items.push(item);
			item.setContainerMenu(this);
			var ItemSize = item.Obj.Size();
			var NewItemXY = new XY(GRID , MenuSize.dy);
			item.Obj.XY(NewItemXY);
	
			MenuSize = new DXY(MenuSize.dx > ItemSize.dx + 2 * GRID ? MenuSize.dx : ItemSize.dx  + 2 * GRID, MenuSize.dy + ItemSize.dy + GRID);
			
			Obj
			.Child(item.Obj)
			.SizeOne(MenuSize);
		}
		this.show = function(fromObj, toxy)
		{
			
			var StartXY = new XY(0, 0);
			var SmallStartSize = new DXY(30, 30);
			Obj
			.XY(StartXY)
			.Size(SmallStartSize)
			.Opacity(0.1)
			.Father(fromObj)
			.Time(150)
			.Size(MenuSize)
			.To(toxy)
			.Opacity(1)
			.Move();
			
		}
	}
	
	
	var floaticon = new function FloatIcon()
	{
		var ICON_SIZE = new DXY(45, 46);
		var Img = Create('img')
		.Size(ICON_SIZE)
		.Src(CONST.EXPLORER_BASE_URL + CONST.ICON_SRC);
		this.MenuOpen = false;
		var Obj = Create('div')
		.Size(ICON_SIZE)
		.Child(Img)
		.Click(function(done){
			done();
			
			if (!floaticon.MenuOpen)
			{
				floaticon.MenuOpen = true;
				MouseState = 1;
				var MenuXY = new XY(50, 0);
				ExplorerMainMenu.show(Obj, MenuXY);
			}
		})
		.Style('hidden')
		.WindowFather();
		
		this.Obj = Obj;
		
		this.show = function(xy)
		{
			Obj
			.Style('shown')
			.Time(0)
			.Opacity(0)
			.XY(xy)
			.Time(150)
			.Opacity(1);
		}
		this.hide = function()
		{
			Obj			
			.Time(150)
			.CallBack(function(){
				Obj.Style('hidden');
				resetMainMenu();
			})
			.Opacity(0);
			
		}
	}
	
	var ExplorerMainMenu = null;
	var ShareItem;
	function resetMainMenu()
	{
		
		if (floaticon.Obj.Childs().length > 1)
		{
			floaticon.Obj.Remove(ExplorerMainMenu.Obj);
		}
		floaticon.MenuOpen = false;
		ExplorerMainMenu = new Menu();
		ShareItem = new MenuItem(new DXY(100, 30));
		ShareItem.Obj.Text('Share');
		ExplorerMainMenu.pushItem(ShareItem);
		ShareItem.setClick(function(){
			window.InternetRuntime.Client.initOption(CONST.SHARE_SIGNAL_NAME,
													function(option){
														OptionHandler(option, ShareItem)
													});
		});
	}
	resetMainMenu();
	
	function OptionHandler(option, optionitem)
	{
		var OptionObj;
		eval("OptionObj=" + option);
		if (OptionObj["Options"]["entry"])
		{
			var Choices = OptionObj["Options"]["entry"]["value"]["Choice"];
			
			var SubMenu = new Menu();
			for (var c in Choices)
			{
				var item = new MenuItem(new DXY(100, 30));
				item.setChoice(Choices[c]);
				item.Obj.Text(c);
				item.setClick(function(choice){
								var params = {};
								params.requestListenerIndex = '<choice>'
								+ '<RoutingId>' + choice['RoutingId'] + '</RoutingId>'
								+ '<RequestListenerId>' + choice['RequestListenerId'] + '</RequestListenerId>'
								+ '</choice>';
								params.url = OperationObject.href;
								params.format = 'redirecturl';
								window.InternetRuntime.Client.init(CONST.SHARE_SIGNAL_NAME, params, 
																	function(url){
																
																		document.location.href = url;
																	});
							});
				SubMenu.pushItem(item);
			}
			optionitem.setSubMemu(SubMenu);
			optionitem.showSubMenu();
		}
		else
		{
			var params = {};
			params.url = OperationObject.href;
			params.format = 'redirecturl';
			window.InternetRuntime.Client.init(CONST.SHARE_SIGNAL_NAME, params, 
												function(url){
													document.location.href = url;
												});
		}
		
	}
	
	
	 
}

window.InternetRuntime.Explorer.Init();