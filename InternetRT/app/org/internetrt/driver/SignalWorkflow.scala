//package org.internetrt.driver
//import play.api.mvc._
//import org.internetrt.SiteInternetRuntime
//import org.internetrt.core.signalsystem.SignalResponse
//
//object SignalWorkflow extends Controller {
//	def process = Action {
//	  request =>
//	    val signal = new HttpGetSignal(request)
//	    SiteInternetRuntime.executeSignal(signal);
//		
//		val processor = new HttpResponseProcessor(signal.getResponse());
//		
//	  processor.generateResult()
//	}
//}
//	
//	
//class HttpGetSignal(req:Any) extends org.internetrt.core.signalsystem.Signal{
//	type Response = SignalResponse
//	@Override
//	def getIdentifier()= {
//		// TODO Auto-generated method stub
//		"Http";
//	}
//	
//	def getResponse()={
//	  new SignalResponse();
//	}
//}