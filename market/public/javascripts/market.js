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
	var applist = document.getElementById('applist').childNodes;

	
	
	for (i in applist)
		if (applist[i].className == 'appitem')
		{
			
			var item = Create('div').setObj(applist[i]).Refresh()
			.HoverIn(function(done, Obj){
				done();
				Obj.getObj().style.backgroundImage = '-moz-linear-gradient(center top , #EBF3FC, #33CC33)';
				Obj.getObj().style.backgroundImage = '-webkit-gradient(linear,0% 0%, 0% 100%, from(#EBF3FC), to(#33CC33))';
			})
			.HoverOut(function(done, Obj){
				done();
				Obj.getObj().style.backgroundImage = '-moz-linear-gradient(center top , #EBF3FC, #DCE9F9)';
				Obj.getObj().style.backgroundImage = '-webkit-gradient(linear,0% 0%, 0% 100%, from(#EBF3FC), to(#DCE9F9))';
				
			})
			.Click(function(done, Obj){
				if (!Obj.OpenState)
				{
					Obj.OpenState = 1;
					Obj
					.Time(300)
					.Tween('Quad')
					.CallBack(done)
					.Size(new DXY(Obj.Size().dx, Obj.Size().dy + 100), true);
				}
				else
				{
					Obj.OpenState = 0;
					Obj
					.Time(300)
					.Tween('Quad')
					.CallBack(done)
					.Size(new DXY(Obj.Size().dx, Obj.Size().dy - 100), true);
				}
			});
		}
}

