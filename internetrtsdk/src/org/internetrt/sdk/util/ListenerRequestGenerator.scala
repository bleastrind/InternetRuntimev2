package org.internetrt.sdk.util

import scala.collection.JavaConversions._
import scala.collection.JavaConverters._
import scala.collection.mutable._

class ListenerRequestGenerator(routingInstancePaser:RoutingXmlParser) {
   
	def generateSignalListenerUrl(signalData:java.util.Map[String, String], listenerconfig:ListenerConfig = null)={
	  val parser = routingInstancePaser;
	  val paramFormats = parser.getParamsFormat(listenerconfig);

	  val paramdata = generateDataByFormat(signalData, paramFormats)
	  val baseurl = parser.getReqUrl(listenerconfig);
	  baseurl + "?" + HttpHelper.generatorParamString(paramdata);
	}
	
	def generateDataByFormat(signalData:Map[String,String], format:Map[String,String]):Map[String,String]={
	  format map ( p => (p._1,  signalData.get(p._1) match{
	    case Some(v) => v
	    case None => throw new Exception("Signal Data is not enough")
	  } ) )
	}
}

object ListenerRequestGenerator{
	implicit def javaMapasMap[A, B](m : java.util.Map[A, B]): Map[A, B] = scala.collection.JavaConversions.asMap(m)
}

