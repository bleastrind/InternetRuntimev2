package controllers;

import org.internetrt.sdk.InternetRT;
import org.internetrt.sdk.util.RoutingGenerator;

import config.properties;

import cn.edu.act.internetos.appmarket.service.TermToJson;

import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.Scope.Session;
import models.App;
import models.RoutingRecommender;

public class RoutingRecomController extends Controller{
	
	
		public static String getAccessToken() {
			System.out.println("get token!!!!!!!!!!!!!!!!!");
			return session.get("token");
		}
		
		   @Before(only = { "index", "recomRoutingBaseFrom", "recomRoutingBaseTo" })
		public static void checkUser() {
			System.out.println("checkUser");
			String token = getAccessToken();
			if (token == null) {
				System.out.println(properties.irt.getAuthCodeUrl(config.properties.appID,config.properties.redirect));
				Controller.redirect(properties.irt.getAuthCodeUrl(config.properties.appID,config.properties.redirect));
				System.out.println("checkUser!!!!");
			}
		}
	   
		public static void index(String id){
			session.put("installappid",id);
			render("Routing/recomRouting.html");
		}
		
		public static void recomRoutingBaseFrom(){
			String fromAppIDString = session.get("installappid");
			String accessToken = getAccessToken();
			
			//session.setAttribute("fromApp", fromAppIDString);
			session.put("fromApp", fromAppIDString);
			
			RoutingRecommender routingRecommender = new RoutingRecommender();
			String result = routingRecommender.RecomRoutingBaseFrom(fromAppIDString, accessToken);
			System.out.println("result "+result);
			response.print(result);
			render("Routing/recomRouting.html");
			/*PrintWriter out = response.getWriter();
			out.write(result);
			out.flush();
			out.close();*/
		}
		
		public static void recomRoutingBaseTo(){
			String toAppId  = session.get("installappid");
			
			//HttpSession session = request.getSession();
			String accessToken = getAccessToken();
			session.put("toApp", toAppId);
			
			RoutingRecommender routingRecommender = new RoutingRecommender();
			String result = routingRecommender.recomRoutingBaseTo(toAppId, accessToken);
			System.out.println("result "+result);
			response.print(result);
			/*PrintWriter out = response.getWriter();
			out.write(result);
			out.flush();
			out.close();*/
			
		}
		
		public static void GenRecomRouting(){
			String triggerChannel = null;
			String actionChannel = null;

			String trigger = request.params.get("signal");
			String receiver = request.params.get("receiver");
			String sender = request.params.get("sender");
			String type = request.params.get("type");
			
			System.out.println("signal: "+trigger);
			System.out.println("receiver: "+receiver);
			System.out.println("sender: "+sender);
			System.out.println("type: "+type);
			
			if(type.equals("receiver"))
			{
				triggerChannel = receiver;
				actionChannel = (String) session.get("installappid");
			}
			else {
				triggerChannel = sender;
				actionChannel = (String) session.get("installappid");
			}
			
			InternetRT rt = config.properties.irt;
			
			
			String accessToken = session.get("accessToken").toString();
			
			String signalXml = rt.getSignalDefination(trigger);
			String appXml = rt.getAppDetail(actionChannel, accessToken);
			
			RoutingGenerator routingGenerator = new RoutingGenerator(signalXml, appXml);
			String routingXml = routingGenerator.generateRouting(trigger, triggerChannel, actionChannel);
			
			String resultString = TermToJson.stringToJson("routingXml", routingXml).toString();
			
			response.print(resultString);
			/*PrintWriter out = response.getWriter();
			out.write(resultString);
			out.flush();
			out.close();*/
		}
		
		public static void ConfirmRecomRouting(){
			String routing = request.params.get("routing");
			InternetRT rt = config.properties.irt;
			
			System.out.println(routing);
			
			String accessToken = getAccessToken();
			
			rt.ConfirmRouting(accessToken,routing);
			
			/*PrintWriter out = response.getWriter();
			out.flush();
			out.close();*/
		}
}
