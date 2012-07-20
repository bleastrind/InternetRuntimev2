if (!window.InternetRuntime)
	window.InternetRuntime = {};
	
window.InternetRuntime.Explorer = new function()
{
	var UI = window.InternetRuntime.UI;
	//check if an object is null or undefined
	function isNUE(obj)
	{	
		return (obj == null || obj == undefined || obj == "");
	}
	
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
		alert(123123);
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
}

window.InternetRuntime.Explorer.Init();