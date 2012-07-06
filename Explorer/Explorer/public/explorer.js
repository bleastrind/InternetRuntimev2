if (!window.InternetRuntime)
	window.InternetRuntime = {};
	
window.InternetRuntime.Explorer = new function()
{
	function isNUE(obj)
	{	
		if (obj == null || obj == undefined || obj = '')
			return true;
		else
			return false;
	}
	var Filters = 
	{
		link: function(objs)
		{
			var ans = {};
			for (var obj in objs)
				if (!isNUE(obj.href))
					ans[obj] = objs[obj];
			return ans;
		}
	}
	function SearchAllElements(obj)
	{
		var ans = [];
		if (obj.NodeType == ELEMENT_NODE)
			ans.push(obj);
		var childs = obj.childNodes;			
		for (var c in childs)
			ans = ans.concat(SearchAllElements(c));		
		return ans;		
	}
	function show(e)
	{
	
	}
	function hide(e)
	{
	
	}
	
	
	this.Init()
	{
		var objs = SearchAllElements(document.body);
		for (var f in Filters)
			objs = Filters[f](objs);
		for (var obj in objs)
		{
			obj.addEventListener('mouseover', show, false);
			obj.addEventListener('mouseout', hide, false);
		}
		addEventListener
	}
}

window.InternetRuntime.Explorer.Init();