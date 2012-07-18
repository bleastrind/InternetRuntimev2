package controllers;

import org.internetrt.sdk.InternetRT;
import org.internetrt.sdk.util.DescribedListenerConfig;
import org.internetrt.sdk.util.FreeRoutingGenerator;
import org.internetrt.sdk.util.Signal;

import config.properties;

import cn.edu.act.internetos.appmarket.service.TermToJson;

import play.mvc.Before;
import play.mvc.Controller;
import models.RoutingChoice;
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
				System.out.println(properties.irt.getAuthCodeUrl());
				Controller.redirect(properties.irt.getAuthCodeUrl());
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
			List<scala.Tuple3<String,Signal,DescribedListenerConfig>> result = routingRecommender.getPossibleRoutings(fromAppIDString, accessToken);
			List<RoutingChoice> choices = generateChoieces(result);
			System.out.println(choices.size());
			if(choices.size()> 0)
				render("Routing/recomRouting.html",choices);
			else
				render("Routing/success.html");
		}
		
		private static List<RoutingChoice> generateChoieces(List<scala.Tuple3<String,Signal,DescribedListenerConfig>> possibleRoutings){
			List<RoutingChoice> res = new ArrayList<RoutingChoice>();
			for(scala.Tuple3<String,Signal,DescribedListenerConfig> data:possibleRoutings){
				String routing = FreeRoutingGenerator.generateRouting(data._2().name(),data._1(),data._3());
				String signalName = data._2().name();
				String signaldes = data._2().description();
				String signalApp = data._1();
				String listenerApp = data._3().appName();
				String listenerDes = data._3().description();
				res.add(new RoutingChoice(signalName,signalApp,signaldes,listenerApp,listenerDes,routing));
			}
			return res;
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
			
			for(String routing:routings){
				System.out.println(routing);
				
				rt.ConfirmRouting(accessToken,routing);
			}
			render("Routing/success.html");
			/*PrintWriter out 
			 * = response.getWriter();
			out.flush();
			out.close();*/
		}
}
