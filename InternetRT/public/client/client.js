var BaseUrl = "http://localhost:9000";
var ClientIframeSrc = BaseUrl + '/assets/client/Client.html';
function Client()
{
	var IframePage = document.createElement('iframe');
	IframePage.src = ClientIframeSrc;
	IframePage.style.width = 10 + 'px';
	IframePage.style.height = 10 + 'px';
	document.body.appendChild(IframePage);
	this.login = function(username, password)
	{	
		var msg = 
		{
			type: 'login',
			username: username,
			password: password
		}
		IframePage.contentWindow.postMessage(msg, BaseUrl);
	}
	this.register = function(username, password)
	{	
		var msg = 
		{
			type: 'register',
			username: username,
			password: password
		}
		IframePage.contentWindow.postMessage(msg, BaseUrl);
	}
	this.initOption = function(signalname)
	{	
		var msg = 
		{
			type: 'initOption',
			signalname: signalname
		}
		IframePage.contentWindow.postMessage(msg, BaseUrl);
	}
}
var gClient = new Client();


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