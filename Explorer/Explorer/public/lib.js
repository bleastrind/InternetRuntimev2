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
		}
	}
		
		
	//	Library: Style
	var Lib_Style = 
	{
		Default: function(obj)
		{},
		first: function(obj)
		{
			obj.style.backgroundColor = '#DCE9F9';
			obj.style.backgroundImage = '-moz-linear-gradient(center top , #EBF3FC, #DCE9F9)';
			obj.style.boxShadow = '0 1px 0 rgba(255, 255, 255, 0.8) inset';
			obj.style.border = '1px solid #CCCCCC';
			obj.style.borderRadius = '6px 6px 6px 6px';
		},
		item: function(obj)
		{
			obj.style.borderColor =  '#0879C0';
			obj.style.borderStyle = 'solid';
			obj.style.borderWidth = '1px';
			obj.style.padding = '0 4px';
			obj.style.backgroundColor = '#066099';
			obj.style.borderRadius = '6px 6px 6px 6px';
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
			this.x = this.x + d.dx;
			this.y = this.y + d.dy;
			return this;
		}
		this.getDst = function(xy)
		{
			return new DXY(this.x - xy.x, this.y - xy.y)
		}
	}
	//Distance between two position.
	this.DXY = DXY;
	function DXY(dx, dy)
	{
		this.dx = dx;
		this.dy = dy;
		this.scale = function(s)
		{
			return new DXY(this.dx * s, this.dy * s);
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
		if ((!domobj) || (!domobj.style))
			return null;
		var Obj = domobj;
		Obj.style.position = 'absolute';
		this.DOMObject = Obj;
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
				Lib_Style[Style](Obj);
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
				Class.src = Src;
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
		
		this.Anime = function(tween, time, func, callback)
		{
			var AnimeObject = this;
			if (time == 0)
			{					
				func(AnimeObject, 1);
				callback(AnimeObject);	
				CallBack = function(){};
				return;
			}
			var StartTime = new Date().getTime();
			function Run()
			{
				var NewTime = new Date().getTime();
				var d = tween((NewTime - StartTime) / parseFloat(time));
				if (time >= (NewTime - StartTime))
				{
					//alert(d);
					func(AnimeObject, d);
					setTimeout(Run, 10);
				}
				else
				{
					func(AnimeObject, 1);
					callback(AnimeObject);	
					CallBack = function(){};
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
			
			this.Anime(Opacity_Tween, Opacity_Time, 
				function(obj, d)
				{
					Opacity = Opacity_Start + d * (opct - Opacity_Start);
					Obj.style.opacity = Opacity;					
				},
				Opacity_CallBack);
			return this;
		}
		
		var SizeDXY = new DXY(parseInt(Obj.style.width), parseInt(Obj.style.height));	
		this.Size = function(dxy)
		{
			if (isNotNullOrUndefined(dxy))
			{
				SizeDXY = dxy;
				Obj.style.width = SizeDXY.dx + 'px';
				Obj.style.height = SizeDXY.dy + 'px';
				return this;
			}
			else
				return SizeDXY;
		}
		
		function ComponentSearcher(obj)
		{
			var ans = [];
			ans.push(obj);
			var childs = obj.Childs();			
			for (var i = 0; i < childs.length; i++)
				ans = ans.concat(ComponentSearcher(childs[i]));		
			return ans;		
		};
		
		function ResizeAll(objs, dxy, d)
		{
			for (var i = 0; i < objs.length; i++)		
				Resize(objs[i], dxy, d);
		}
		
		function Resize(Obj, dxy, d)
		{
			if (d == 1)
			{
				Obj.SizeDXY = dxy;
				Obj.DOMObject.style.width = Obj.SizeDXY.dx + 'px';
				Obj.DOMObject.style.height = Obj.SizeDXY.dy + 'px';
			}
			else
			{
				Obj.DOMObject.style.width = Obj.SizeDXY.dx + (dxy.dx -  Obj.SizeDXY.dx) * d + 'px';
				Obj.DOMObject.style.height = Obj.SizeDXY.dy + (dxy.dy -  Obj.SizeDXY.dy) * d + 'px';
			}
		}
		
		
		this.Size = function(dxy)
		{			
			
			var Resize_Objects = ComponentSearcher(this);
			var Resize_Time = Time;
			var Resize_Tween = Tween;
			var Resize_CallBack = CallBack;
			
			this.Anime(Resize_Tween, Resize_Time, 
				function(obj, d)
				{
					ResizeAll(Resize_Objects, dxy, d);
				},
				Resize_CallBack);
			return this;
		}
		
		var InFunc = function(){};
		var OutFunc = function(){};
		
		var IN = 1, OUT = 2;
		var MouseState = OUT;
		var REST = 1, RUN = 2;
		var AnimeState = REST; 
		
		this.HoverIn = function(inf)
		{
			InFunc = inf;
			Obj.onmouseover = function()
			{			
				MouseState = IN;
				if (AnimeState == REST)
				{
					AnimeState = RUN;
					InFunc(InFuncDone);
				}
			}
			return this;
		}
		
		this.HoverOut = function(outf)
		{
			OutFunc = outf;
			Obj.onmouseout = function()
			{
				MouseState = OUT;
				if (AnimeState == REST)
				{
					AnimeState = RUN;
					OutFunc(OutFuncDone);
				}						
			}
			return this;
		}								
		function InFuncDone()
		{			
			if (MouseState == OUT)
				OutFunc(OutFuncDone);
			else
				AnimeState = REST;			
		}
		function OutFuncDone()
		{
			if (MouseState == IN)
				InFunc(InFuncDone);
			else
				AnimeState = REST;				
		};	
		
		var DoFunc = function(){};
		this.Click = function(dof)
		{
			DoFunc = dof;
			Obj.onclick = function()
			{			
				if (AnimeState == REST)
				{
					AnimeState = RUN;
					DoFunc(DoFuncDone);
				}
			}		
			return this;
		}
		function DoFuncDone()
		{				
			AnimeState = REST;			
		}
	
		
	}
	//Create a DOM Obeject
	this.Create = function(type)
	{
		return new DOM_Object(Lib_SimpleObject[type]());
	}
};
