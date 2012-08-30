
if (!window.InternetRuntime)
	window.InternetRuntime = {};
window.InternetRuntime.UI = new function(){
		
	//	Global Consts	
	var CONST = 
	{
		PRE: 'internetrt_',
		WRAPPER_CLASS: this.PRE + 'wrapper',
		DEFALUT_TIME: 0
	}

				
	//	Library: Origin Simple DOM Object
	var Lib_SimpleObject = 
	{
		div: function()
		{
			return document.createElement('div');
		},
		img: function()
		{
			return document.createElement('img');
		},
		iframe: function()
		{
			var obj = document.createElement('iframe');
			obj.frameBorder = 0;
			return obj;
		},
		h3: function()
		{
			return document.createElement('h3');
		},
		a: function()
		{
			return document.createElement('a');
		}
	}
		
		
	//	Library: Style
	var Lib_Style = 
	{
		Default: function(obj)
		{},
		menu: function(obj)
		{
			obj.Color('DCE9F9');
			obj.DOMObject.style.backgroundImage = '-moz-linear-gradient(center top , #CCFFFF, #CCFFFF)';
			obj.DOMObject.style.backgroundImage = '-webkit-gradient(linear,0% 0%, 0% 100%, from(#CCFFFF), to(#CCFFFF))';
			obj.DOMObject.style.boxShadow = '0 1px 0 rgba(255, 255, 255, 0.8) inset';
			obj.DOMObject.style.border = '1px solid #CCCCCC';
			obj.DOMObject.style.borderRadius = '6px 6px 6px 6px';
			obj.DOMObject.style.padding = '0px';
			obj.DOMObject.style.margin = '0px';
		},
		first: function(obj)
		{
			
			obj.Color('DCE9F9');
			obj.DOMObject.style.backgroundImage = '-moz-linear-gradient(center top , #EBF3FC, #DCE9F9)';
			obj.DOMObject.style.backgroundImage = '-webkit-gradient(linear,0% 0%, 0% 100%, from(#EBF3FC), to(#DCE9F9))';
			obj.DOMObject.style.boxShadow = '0 1px 0 rgba(255, 255, 255, 0.8) inset';
			obj.DOMObject.style.border = '1px solid #CCCCCC';
			obj.DOMObject.style.borderRadius = '6px 6px 6px 6px';
			obj.DOMObject.style.padding = '0px';
			obj.DOMObject.style.margin = '0px';
		},
		tag: function(obj)
		{
			
			obj.Color('DCE9F9');
			obj.DOMObject.style.backgroundImage = '-moz-linear-gradient(center top , #EBF3FC, #DCE9F9)';
			obj.DOMObject.style.backgroundImage = '-webkit-gradient(linear,0% 0%, 0% 100%, from(#EBF3FC), to(#DCE9F9))';
			obj.DOMObject.style.boxShadow = '0 1px 0 rgba(255, 255, 255, 0.8) inset';
			obj.DOMObject.style.border = '1px solid #CCCCCC';
			obj.DOMObject.style.borderRightStyle = 'none';
			obj.DOMObject.style.borderRadius = '6px 0px 0px 6px';
			obj.DOMObject.style.padding = '0px';
			obj.DOMObject.style.margin = '0px';
		},
		item: function(obj)
		{
			obj.DOMObject.style.borderColor =  '#0879C0';
			obj.DOMObject.style.borderStyle = 'solid';
			obj.DOMObject.style.borderWidth = '1px';
			//obj.DOMObject.style.padding = '0 4px';
			obj.DOMObject.style.textAlign = 'center';			
			obj.DOMObject.style.font = '24px arial';		
			obj.DOMObject.style.color = '#066099';		
			obj.Color('FFCC33');
			
			obj.DOMObject.style.backgroundImage = '-moz-linear-gradient(center top , #6699CC, #86B5D9)';
			obj.DOMObject.style.backgroundImage = '-webkit-gradient(linear,0% 0%, 0% 100%, from(#6699CC), to(#86B5D9))';
			
			obj.DOMObject.style.MozUserSelect = 'none';
			obj.DOMObject.style.borderRadius = '6px 6px 6px 6px';
			obj.DOMObject.style.padding = '0px';
			obj.DOMObject.style.margin = '0px';
			obj.DOMObject.style.cursor = 'pointer';
		},
		wraper: function(obj)
		{
			obj.DOMObject.style.overflow = 'hidden';
			obj.DOMObject.style.padding = '0px';
			obj.DOMObject.style.margin = '0px';
		},
		hidden: function(obj)
		{
		
			obj.DOMObject.style.display = 'none';
			obj.DOMObject.style.padding = '0px';
			obj.DOMObject.style.margin = '0px';
		},
		shown: function(obj)
		{
			obj.DOMObject.style.display = 'block';
			obj.DOMObject.style.padding = '0px';
			obj.DOMObject.style.margin = '0px';
		}
	};

	//	Library: Tween Formula 	
	var Lib_Tween =
	{
		Default: function(pct)
		{
			return pct;
		},
		Quad: function(pct)
		{
			return -(Math.pow((pct-1), 2) -1);
		}
	};

	
	//	Library: Moving Route 
	var Lib_Route =
	{
		Default: function(obj, originxy, targetxy, d)
		{			
			
			var Distance = targetxy.getDst(originxy);
			var temp = new XY(originxy.x, originxy.y);
			obj.XY(temp.plus(Distance.scale(d)));
		}
	}	
	
	function isNotNullOrUndefined(obj)
	{
		if (obj == null || obj == undefined)
			return false;
		else
			return true;
	}
	
	
	
	//Absolute position of father container.
	this.XY = XY;
	function XY(x, y)
	{		
		this.x = x;
		this.y = y;
		this.plus = function(d)
		{
			return new XY(this.x + d.dx, this.y + d.dy);
		}
		this.minus = function(d)
		{
			return new XY(this.x - d.dx, this.y - d.dy);
		}
		this.getDst = function(xy)
		{
			return new DXY(this.x - xy.x, this.y - xy.y)
		}
		this.scale = function(xs, ys)
		{
			if (isNotNullOrUndefined(ys))
				return new XY(this.x * xs, this.y * ys);
			else
				return new XY(this.x * xs, this.y * xs);
		}
	}
	//Distance between two position.
	this.DXY = DXY;
	function DXY(dx, dy)
	{
		this.dx = dx;
		this.dy = dy;
		this.scale = function(xs, ys)
		{
			if (isNotNullOrUndefined(ys))
				return new DXY(this.dx * xs, this.dy * ys);
			else
				return new DXY(this.dx * xs, this.dy * xs);
		}
	}
	
	
	//	Wrapper container
	function Warrper(xy, size)
	{
		this.prototype = new DOM_Object(Lib_SimpleObject['div']());
		this.setXY(xy);
		this.setSize(size);
		w.className = CONST.WRAPPER_CLASS;
	}
	
	//	DOM Object
	function DOM_Object(domobj)
	{
		var This = this;
		if ((!domobj) || (!domobj.style))
			return null;
		var Obj = domobj;
		Obj.style.position = 'absolute';
		this.DOMObject = Obj;
		this.getObj = function()
		{
			return Obj;
		}
		this.setObj = function(obj)
		{
			Obj = obj;
			this.DOMObject = Obj;
			return this;
		}
		var PosXY;
		if (isNaN(parseInt(Obj.style.left)))
			PosXY = new XY(0, 0);
		else
			PosXY = new XY(parseInt(Obj.style.left), parseInt(Obj.style.top));
		this.XY = function(xy)
		{
			if (isNotNullOrUndefined(xy))
			{
				PosXY = xy;
				Obj.style.left = PosXY.x + 'px';
				Obj.style.top = PosXY.y + 'px';
				return this;
			}
			else
				return PosXY;
		}
		
		this.RightTopXY = function(xy)
		{
			if (isNotNullOrUndefined(xy))
			{
				PosXY.x = xy.x - SizeDXY.dx;
				PosXY.y = xy.y;
				Obj.style.left = PosXY.x + 'px';
				Obj.style.top = PosXY.y + 'px';
				return this;
			}
			else
				return new XY(PosXY.x + SizeDXY.dx, PosXY.y);
		}
		this.LeftBottomXY = function(xy)
		{
			if (isNotNullOrUndefined(xy))
			{
				PosXY.x = xy.x;
				PosXY.y = xy.y - SizeDXY.dy;
				Obj.style.left = PosXY.x + 'px';
				Obj.style.top = PosXY.y + 'px';
				return this;
			}
			else
				return new XY(PosXY.x, PosXY.y + SizeDXY.dy);
		}
		
		this.RightBottomXY = function(xy)
		{
			if (isNotNullOrUndefined(xy))
			{
				PosXY.x = xy.x - SizeDXY.dx;
				PosXY.y = xy.y - SizeDXY.dy;
				Obj.style.left = PosXY.x + 'px';
				Obj.style.top = PosXY.y + 'px';
				return this;
			}
			else
				return new XY(PosXY.x + SizeDXY.dx, PosXY.y + SizeDXY.dy);
		}


		
		this.CenterXY =function(xy)
		{
			if (isNotNullOrUndefined(xy))
			{
				PosXY = xy.minus(SizeDXY.scale(0.5));
				Obj.style.left = PosXY.x + 'px';
				Obj.style.top = PosXY.y + 'px';
				return this;
			}
			else
				return PosXY.plus(SizeDXY.scale(0.5));
		}
		
		
		var Id = Obj.id;
		this.Id = function(id)
		{
			if (isNotNullOrUndefined(id))
			{
				Id = id;
				Obj.id = Id;
				return this;
			}
			else
				return Id;
		}
		
		var Class = Obj.className;
		this.Class = function(cls)
		{
			if (isNotNullOrUndefined(cls))
			{
				Class = cls;
				Obj.className = Class;
				return this;
			}
			else
				return Class;
		}
		
		var Style = 'Default';
		this.Style = function(stl)
		{
			if (isNotNullOrUndefined(stl))
			{
				Style = stl;
				Lib_Style[Style](this);
				return this;
			}
			else
				return Style;
		}
		
		var Src = '';
		this.Src = function(src)
		{
			if (isNotNullOrUndefined(src))
			{
				Src = src;
				Obj.src = Src;
				return this;
			}
			else
				return Src;
		}
		
		var Father = new DOM_Object(Obj.parentNode);
		this.Father = function(fth)
		{
			if (isNotNullOrUndefined(fth))
			{
				Father = fth;
				fth.Child(this);
				return this;
			}
			else
				return Father;
		}
		this.WindowFather = function()
		{			
			Father = null;
			document.body.appendChild(Obj);
			return this;		
		}
		
		var Childs = [];
		this.Child = function(chld)
		{
			Childs.push(chld);
			Obj.appendChild(chld.DOMObject);		
			return this;
		}
		this.Childs = function(chlds)
		{
			if (isNotNullOrUndefined(chlds))
			{
				Childs = chlds;
				for (var i = 0; i < chlds.length; i++)
					Obj.appendChild(chlds[i].DOMObject);
				return this;
			}
			else
				return Childs;
		}
		
		
		var Time = CONST.DEFALUT_TIME;
		this.Time = function(tm)
		{
			if (isNotNullOrUndefined(tm))
			{
				Time = tm;
				return this;
			}
			else 
				return Time;
		}
		
		var Tween = Lib_Tween['Default'];
		this.Tween = function(twn)
		{
			if (isNotNullOrUndefined(twn))
			{
				Tween = Lib_Tween[twn];
				return this;
			}
			else 
				return Tween;
		}
		
		
		var To = PosXY;
		this.To = function(t)
		{
			if (isNotNullOrUndefined(t))
			{
				To = t;				
				return this;
			}
			else
				return To;
		}
		
		
		var Route = Lib_Route['Default'];
		this.Route = function(rt)
		{
			if (isNotNullOrUndefined(rt))
			{
				Route = Lib_Route[rt];
				return this;
			}
			else 
				return Route;
		}
		
		var CoverWrapper = null;
		this.CoverFrom = function()
		{
			
		}
		this.CoverTo = function()
		{
		
		}
		
		var CallBack = function(){};
		this.CallBack = function(clbk)
		{
			if (isNotNullOrUndefined(clbk))
			{
				CallBack = clbk;
				return this;
			}
			else 
				return CallBack;
		}
		
		//why this. ???
		this.Anime = function(tween, time, func, callback)
		{
			var AnimeObject = this;
			if (time == 0)
			{					
				func(AnimeObject, 1);
				callback(AnimeObject);	
				return;
			}
			var StartTime = new Date().getTime();
			function Run()
			{
				var NewTime = new Date().getTime();
				var d = tween((NewTime - StartTime) / parseFloat(time));
				if (time >= (NewTime - StartTime))
				{
					
					func(AnimeObject, d);
					setTimeout(Run, 10);
				}
				else
				{
					func(AnimeObject, 1);
					callback(AnimeObject);	
				}
			}
			Run();
		}
		
		this.Move = function()
		{
	
			var Move_Time = Time;
			var Move_From = PosXY;
			var Move_To = To;
			var Move_Tween = Tween;
			var Move_Route = Route;
			var Move_CallBack = CallBack;
			CallBack = function(){};
			
			this.Anime(Move_Tween, Move_Time, 
				function(obj, d)
				{	
					Move_Route(obj, Move_From, Move_To, d);
				},
				Move_CallBack);
			return this;
		}
		
		
		var Opacity = 1;
		this.Opacity = function(opct)
		{
			var Opacity_Start = Opacity;
			var Opacity_Time = Time;;
			var Opacity_Tween = Tween;
			var Opacity_CallBack = CallBack;
			CallBack = function(){};
			
			this.Anime(Opacity_Tween, Opacity_Time, 
				function(obj, d)
				{
					Opacity = Opacity_Start + d * (opct - Opacity_Start);
					Obj.style.opacity = Opacity;					
				},
				Opacity_CallBack);
			return this;
		}
		
		//var SizeDXY = new DXY(parseInt(Obj.style.width), parseInt(Obj.style.height));	
		var SizeDXY = new DXY(1, 1);	
		
		function ComponentSearcher(obj)
		{
			var ans = [];
			ans.push(obj);
			var childs = obj.Childs();			
			for (var i = 0; i < childs.length; i++)
				ans = ans.concat(ComponentSearcher(childs[i]));		
			return ans;		
		};
		
		function ResizeAll(objs, dxscale, dyscale, d)
		{
			for (var i = 0; i < objs.length; i++)
				if (objs[i].getObj() != Obj)
					objs[i].Resize(dxscale, dyscale, d, true);
				else
					objs[i].Resize(dxscale, dyscale, d, false);
		}
		
		this.Resize = function(dxscale, dyscale, d, posflag)
		{
			if (d == 1)
			{
				SizeDXY = SizeDXY.scale(dxscale, dyscale);
				Obj.style.width = SizeDXY.dx + 'px';
				Obj.style.height = SizeDXY.dy + 'px';
				if (posflag)
				{
					PosXY = PosXY.scale(dxscale, dyscale);
					Obj.style.left = PosXY.x + 'px';
					Obj.style.top = PosXY.y + 'px';
				}
			}
			else
			{
				Obj.style.width = SizeDXY.dx * (1 + (dxscale - 1) * d) + 'px';
				Obj.style.height = SizeDXY.dy * (1 + (dyscale - 1) * d) + 'px';
				if (posflag)
				{
					Obj.style.left = PosXY.x * (1 + (dxscale - 1) * d) + 'px';
					Obj.style.top = PosXY.y * (1 + (dyscale - 1) * d) + 'px';
				}
			}
		}
				
		
		this.SizeOne = function(dxy)
		{
			if (isNotNullOrUndefined(dxy))
			{
				var tmp = [];
				tmp.push(this);
				var Resize_Objects = tmp;
				var Resize_Time = Time;
				var Resize_Tween = Tween;
				var Resize_CallBack = CallBack;
				CallBack = function(){};
				
				this.Anime(Resize_Tween, Resize_Time, 
					function(obj, d)
					{
						ResizeAll(Resize_Objects, dxy.dx / parseFloat(SizeDXY.dx), dxy.dy / parseFloat(SizeDXY.dy), d);
					},
					Resize_CallBack);
				
				return this;
			}
			else
				return SizeDXY;
		}
		
		
		this.Size = function(dxy, subnotworkflag)
		{			
			if (isNotNullOrUndefined(dxy))
			{
				var Resize_Objects = [];
				if (subnotworkflag)
					Resize_Objects.push(this);
				else
					Resize_Objects = ComponentSearcher(this);
					
				var Resize_Time = Time;
				var Resize_Tween = Tween;
				var Resize_CallBack = CallBack;
				CallBack = function(){};
				
				this.Anime(Resize_Tween, Resize_Time, 
					function(obj, d)
					{
						ResizeAll(Resize_Objects, dxy.dx / parseFloat(SizeDXY.dx), dxy.dy / parseFloat(SizeDXY.dy), d);
					},
					Resize_CallBack);
				
				return this;
			}
			else
				return SizeDXY;
		}
		
		var InFunc = function(){};
		var OutFunc = function(){};
		
		var IN = 1, OUT = 2;
		var MouseState = OUT;
		var REST = 1, RUN = 2;
		var AnimeState = REST; 
		
		
		
		this.HoverIn = function(inf, subworkflag)
		{
			InFunc = inf;
			Obj.onmouseover = function(e)
			{		
				if (e.relatedTarget && checkContain(Obj, e.relatedTarget))				
					return;		
				if (!subworkflag && e.target != Obj)
					return;
				MouseState = IN;
				if (AnimeState == REST)
				{
					AnimeState = RUN;
					InFunc(InFuncDone, This);
				}
			}
			return this;
		}
		
		this.HoverOut = function(outf, subworkflag)
		{
			OutFunc = outf;
			Obj.onmouseout = function(e)
			{					
				if (e.relatedTarget &&  checkContain(Obj, e.relatedTarget))				
					return;			
				if (!subworkflag && e.target != Obj)
					return;
					
				MouseState = OUT;
				if (AnimeState == REST)
				{
					AnimeState = RUN;
					OutFunc(OutFuncDone, This);
				}						
			}
			return this;
		}								
		function InFuncDone()
		{			
			if (MouseState == OUT)
				OutFunc(OutFuncDone, This);
			else
				AnimeState = REST;			
		}
		function OutFuncDone()
		{
			if (MouseState == IN)
				InFunc(InFuncDone, This);
			else
				AnimeState = REST;				
		};	
		
		
		
		var ClickFunc = function(){};
		
		var AnimeState_Click = REST; 
		this.Click = function(dof, subnotworkflag)
		{
			ClickFunc = dof;
			Obj.onclick = function(e)
			{	
				if (subnotworkflag && e.target != Obj)
					return;
				if (AnimeState_Click == REST)
				{
					if (Time != 0)
						AnimeState_Click = RUN;
					ClickFunc(ClickFuncDone, This);
				}
			}		
			return this;
		}
		function ClickFuncDone()
		{				
			AnimeState_Click = REST;			
		}
		
		var LoadFunc = function(){};
		this.LoadFunc = function(ldfunc)
		{
			LoadFunc = ldfunc;
			Obj.onload = function()
			{							
				LoadFunc();
				Obj.onload = function(){};
			}		
			return this;
		}
		
		
		this.isContained = function(simpleobj)
		{
			return checkContain(Obj, simpleobj);
		}
		function checkContain(Obj, simpleobj)
		{
			var childs = Obj.childNodes;
			if (Obj == simpleobj)
				return true;
			if (childs)	
				for (var i = 0; i < childs.length; i++)
				{
					if (checkContain(childs[i], simpleobj))
						return true;
				}
			return false;
		}
		
		var BackGroundColor = "";
		this.Color = function(clr)
		{	
			if (isNotNullOrUndefined(clr))
			{
				if (BackGroundColor == "")
				{
					BackGroundColor = clr;
					Obj.style.backgroundColor = '#' + BackGroundColor;
					return this;
				}
				var Color_Start = BackGroundColor;
				var Color_Time = Time;
				var Color_Tween = Tween;
				var Color_CallBack = CallBack;
				CallBack = function(){};
				
				this.Anime(Color_Tween, Color_Time, 
					function(obj, d)
					{
						var clrH = '0x' + clr;
						var Color_StartH = '0x' + Color_Start;
						var B = clrH % 0x100 - Color_StartH % 0x100;
						var G = Math.floor(clrH % 0x10000 / 0x100) - Math.floor(Color_StartH % 0x10000 / 0x100);
						var R = Math.floor(clrH % 0x1000000 / 0x10000) - Math.floor(Color_StartH % 0x1000000 / 0x10000);
						
						BackGroundColor = parseInt(Color_StartH) + parseInt(d * B) 
													  + parseInt(d * G) * 0x100
													  + parseInt(d * R) * 0x10000;
						BackGroundColor = BackGroundColor.toString(16);					
						Obj.style.backgroundColor = '#' + BackGroundColor;				
					},
					Color_CallBack);
				return this;
			}
			else
				return BackGroundColor;
		}
		
		var TextColor = "";
		this.TextColor = function(clr)
		{	
			if (isNotNullOrUndefined(clr))
			{
				if (TextColor == "")
				{
					TextColor = clr;
					Obj.style.color = '#' + TextColor;
					return this;
				}
				var Color_Start = TextColor;
				var Color_Time = Time;
				var Color_Tween = Tween;
				var Color_CallBack = CallBack;
				CallBack = function(){};
				
				this.Anime(Color_Tween, Color_Time, 
					function(obj, d)
					{
						var clrH = '0x' + clr;
						var Color_StartH = '0x' + Color_Start;
						var B = clrH % 0x100 - Color_StartH % 0x100;
						var G = Math.floor(clrH % 0x10000 / 0x100) - Math.floor(Color_StartH % 0x10000 / 0x100);
						var R = Math.floor(clrH % 0x1000000 / 0x10000) - Math.floor(Color_StartH % 0x1000000 / 0x10000);
						
						TextColor = parseInt(Color_StartH) + parseInt(d * B) 
													  + parseInt(d * G) * 0x100
													  + parseInt(d * R) * 0x10000;
						TextColor = TextColor.toString(16);					
						Obj.style.color = '#' + TextColor;				
					},
					Color_CallBack);
				return this;
			}
			else
				return TextColor;
		} 
		
		this.Text = function(text)
		{
			var textnode = document.createTextNode(text);
			Obj.appendChild(textnode);
			return this;
		}
		
		var Href = ""
		this.Href = function(href)
		{
			if (isNotNullOrUndefined(href))
			{	
				Href = href;
				Obj.href = href;
				return this;
			}
			else
				return Hrefl
		}
		
		this.Remove = function(rmv)
		{	
			var ans = [];
			for (var i in Childs)
				if (Childs[i] != rmv)
					ans.push(Childs[i]);
			Childs = ans;
			Obj.removeChild(rmv.DOMObject);		
			return this;
		}
		
		this.Refresh = function()
		{
			SizeDXY = new DXY(parseInt(Obj.style.width), parseInt(Obj.style.height));
			return this;
		}
		
		
		
		
	}
	//Create a DOM Obeject
	this.Create = function(type)
	{
		return new DOM_Object(Lib_SimpleObject[type]());
	}
	
};

/*
	Pos: InternetRuntime.UI.DXY(x, y)
			plus(XY): XY
			minus(XY): XY
			getDst(XY): DXY		
	Distance: InternetRuntime.UI.DXY(Int x, Int y)
			scale(Int): DXY
	Create: InternetRuntime.UI.Create(Lib_SimpleObject)
	Object:	InternetRuntime.UI.DOM_Object()
			DOMObject
			XY(XY)
			RightTopXY(XY)
			LeftBottomXY(XY)
			RightBottomXY(XY)
			CenterXY(XY)
			Id(String)
			Class(String)
			Style(Lib_Style)
			Sec(String)
			Father(DOM_Object)
			WindowFather()
			Child(DOM_Object)
			Childs(DOM_Object[])
			Time(Int)
			Tween(Lib_Tween)
			To(XY)
			Route(Lib_Route)
			CallBack(Function)
			Move()
			Opacity(Float)
			Size(DXY)
			HoverIn(Function(done))
			HoverOut(Function(done))
			Click(Function(done))
			LoadFunc(Function())
			Color('RRGGBB')
			TextColor('RRGGBB')
			Text(String)
			setObj(obj)
*/