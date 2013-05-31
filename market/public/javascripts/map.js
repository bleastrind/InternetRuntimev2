var CONST = 
{
	BASE_URL: 'http://localhost:9000',
	Lib_SRC: '/assets/client/Lib.js',
	MARKET_URL: 'http://localhost:9001',
	PAGE_LOAD_SIGNAL_NAME: 'clients/pageload'
}
if (!window.InternetRuntime)
	window.InternetRuntime = {};

if (!window.InternetRuntime.UI)
{
	var LibScritp = document.createElement('script');
	document.body.appendChild(LibScritp);
	LibScritp.onload = function()
	{
		InitWithLib();
	}
	LibScritp.src = CONST.BASE_URL + CONST.Lib_SRC;
}
var XY;
var DXY;
var Create;
var GenObject;
function InitWithLib()
{
	XY = window.InternetRuntime.UI.XY;
	DXY = window.InternetRuntime.UI.DXY;
	Create = window.InternetRuntime.UI.Create;
	GenObject = window.InternetRuntime.UI.GenObject;
	Work();
}


function Work()
{
	var Center = new XY(396, 400);
	/*
	var Pos = new function()
	{
		this.Sina = new XY(Center.x - 250, Center.y - 150);
		this.renren = new XY(Center.x - 170, Center.y + 130);
		this.Zoho = new XY(Center.x + 130, Center.y - 130);
		this.Explorer = new XY(Center.x + 200, Center.y + 130);
	}*/
	var Pos = new function()
	{
		this.Sina = new XY(Center.x - 130, Center.y - 340);
		this.renren = new XY(Center.x - 290, Center.y - 160);
		this.Zoho = new XY(Center.x + 270, Center.y - 150);
		this.Explorer = new XY(Center.x + 20, Center.y - 170);
	}


	

	var you = document.getElementById('you');
	var youitem = Create('div').setObj(you).Refresh();
	
	
	var idarray = [];
	for (p in Pos)
		idarray.push(p);
	
	function Init()
	{
		function showItem(i)
		{
			if (document.getElementById(idarray[i]))
			{
				var StartXY = Center;
				var SmallStartSize = new DXY(10, 10);
				var FinalSize = new DXY(70, 70);
				var item = Create('div').setObj(document.getElementById(idarray[i])).Refresh();
				item
				.XY(StartXY)
				.Size(SmallStartSize)
				.Opacity(0.1)
				.Style('shown')
				.Style('first')
				.HoverIn(function(done, Obj){
					done();
					Obj.getObj().style.backgroundImage = '-moz-linear-gradient(center top , #EBF3FC, #33CC33)';
					Obj.getObj().style.backgroundImage = '-webkit-gradient(linear,0% 0%, 0% 100%, from(#EBF3FC), to(#33CC33))';
				},
				true)
				.HoverOut(function(done, Obj){
					done();
					Obj.getObj().style.backgroundImage = '-moz-linear-gradient(center top , #EBF3FC, #DCE9F9)';
					Obj.getObj().style.backgroundImage = '-webkit-gradient(linear,0% 0%, 0% 100%, from(#EBF3FC), to(#DCE9F9))';
					
				},
				true)
				.Click(function(done, Obj){
					if (!Obj.OpenState)
					{
						Obj.OpenState = 1;
						Obj
						.Time(300)
						.Tween('Quad')
						.CallBack(done)
						.Size(new DXY(Obj.Size().dx + 300, Obj.Size().dy + 100), true);
					}
					else
					{
						Obj.OpenState = 0;
						Obj
						.Time(300)
						.Tween('Quad')
						.CallBack(done)
						.Size(new DXY(Obj.Size().dx - 300, Obj.Size().dy - 100), true);
					}
				})
				.Time(150)
				.Size(FinalSize)
				.To(Pos[idarray[i]])
				.Opacity(1)
				.Move();
			}
			if (i + 1 < idarray.length)
				setTimeout(function(){showItem(i + 1);}, 300);
			else
			{
				setTimeout(function(){Line()}, 300);
			}
		}
		setTimeout(function(){showItem(0);}, 300);
	}
	var script1 = document.body.appendChild(document.createElement('script'));
	script1.onload = function()
	{
		var script2 = document.body.appendChild(document.createElement('script'));
		script2.onload = function()
		{
			var jsplumbscript = document.body.appendChild(document.createElement('script'));			
			jsplumbscript.onload = Init;
			jsplumbscript.src='/public/javascripts/jquery.jsPlumb-1.3.6-all-min.js';
		}
		script2.src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.13/jquery-ui.min.js";
	}
	script1.src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js";

	
	function Line()
	{
		var routinginfo = document.getElementById('routings').childNodes;
		var routingarr = [];
		for (var r in routinginfo)
		{
			if (routinginfo[r].attributes && routinginfo[r].attributes["listenerappname"])
				routingarr.push(routinginfo[r]);
		}
		for (var r in routingarr)
		{
			//alert(routingarr[r].attributes["signalappname"].value + "   " + routingarr[r].attributes["listenerappname"].value);
			var s;
			var t;
			var signalname;
			
			if (routingarr[r].attributes["signalappname"].value == 'jsslimclient')
				s = 'you';
			else
				s = routingarr[r].attributes["signalappname"].value;
			t = routingarr[r].attributes["listenerappname"].value;
			signalname = routingarr[r].attributes["signalname"].value;
			if (s != t)
			{
				var dynamicAnchors = [[0.1, 0.5, -1, 0], [0.9, 0.5, 1, 0],
					[0.5, 0.1, 0, -1], [0.5, 0.9, 0, 1],
					[0.1, 0.1, -1, -1], [0.9, 0.1, 1, -1],
					[0.1, 0.9, -1, 1], [0.9, 0.9, 1, 1]];
			
				var common = {
					cssClass	:	"line",
					hoverClass	:	"lineHover"
				};
				
				jsPlumb.connect({
					source: s,
					target: t,
					anchor:dynamicAnchors,
					endpoint:"Blank",
					connector:[ "StateMachine", { curviness:30 }],
					overlays:[ 
						["Arrow", { width:20, length:20, location:0.9 , id:"arrow" } ],
						["Label", { label:signalname, location:0.7, id:"myLabel", cssClass: 'signal'} ]
					],
					paintStyle:{ 
								lineWidth: 4, 
								gradient :{ stops:[ [ 0, "#EBF3FC" ], [ 1, "#DCE9F9" ] ] },
								strokeStyle:"#EBF3FC"
								}
				});
			}
			
		}
		var clist = $('._jsPlumb_connector');
		for (var i = 0; i < clist.length; i++)
		{
			var item = Create('div').setObj(clist[i]).Refresh();
			item
			.Time(0)
			.Opacity(0)
			.Style('shown')
			.Time(300)
			.Opacity(1);
		}
		
		var olist = $('._jsPlumb_overlay');
		for (var i = 0; i < olist.length; i++)
		{
			var item = Create('div').setObj(olist[i]).Refresh();
			item
			.Time(0)
			.Opacity(0)
			.Style('shown')
			.Time(300)
			.Opacity(1);
		}
	}



}

