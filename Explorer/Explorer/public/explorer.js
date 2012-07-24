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
		
		SHARE_SIGNAL_NAME: 'share'
		
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
	function show(e)
	{
		OperationObject = e.target;
		floaticon.show(new XY(e.clientX, e.clientY));
	}
	function hide(e)
	{
	
	}
	
	
	this.Init = function()
	{
		var objs = SearchAllElements(document.body);
		
		for (var f in Filters)
			objs = Filters[f](objs);
		
		for (var obj in objs)
		{
			objs[obj].addEventListener('mouseover', show, false);
			objs[obj].addEventListener('mouseout', hide, false);
		}
		
	}
	
	
	function MenuItem(size)
	{
		var Choice;
		var Img;
		var SubMemu;
		var ContainerMenu;
		var Obj = Create('div')
		.Style('item')
		.Size(size)
		.HoverIn(function(done){
			Obj
			.Time(150)
			.CallBack(done)
			.Color('ff0000');
		})
		.HoverOut(function(done){
			Obj
			.Time(150)
			.CallBack(done)
			.Color('066099');
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
				func(Choice);
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
			
			var MenuXY = new XY(110, 0);
			SubMemu.show(Obj, MenuXY);
		}
	}
	function Menu()
	{	
		var GRID = 5;
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
			.Time(300)
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
		var Obj = Create('div')
		.Size(ICON_SIZE)
		.Child(Img)
		.Click(function(){
			

			var MenuXY = new XY(50, 0);
			ExplorerMainMenu.show(Obj, MenuXY);
		})
		.WindowFather();
		
		this.show = function(xy)
		{
			Obj
			.Time(0)
			.Opacity(0)
			.XY(xy)
			.Time(300)
			.Opacity(1);
		}
		this.hide = function()
		{
			Obj			
			.Time(300)
			.Opacity(1);
		}
	}
	
	var ExplorerMainMenu = new Menu();
	var ShareItem = new MenuItem(new DXY(100, 30));
	ExplorerMainMenu.pushItem(ShareItem);
	ShareItem.setClick(function(){
		window.InternetRuntime.Client.initOption(CONST.SHARE_SIGNAL_NAME,
												function(option){
													OptionHandler(option, ShareItem)
												});
	});
	
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