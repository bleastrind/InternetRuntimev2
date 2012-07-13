package org.internetrt.sdk.util

import scala.collection.JavaConversions._
import scala.collection.JavaConverters._
import scala.collection.mutable._

class ListenerRequestGenerator(routingInstancePaser: RoutingXmlParser) {

  def generateSignalListenerUrl(signalData: java.util.Map[String, String], listenerconfig: ListenerConfig = null) = {
    val parser = routingInstancePaser;
    val paramFormats = parser.getParamsAdapter(listenerconfig);

    val paramdata = generateDataByFormat(signalData, paramFormats)
    val baseurl = parser.getReqUrl(listenerconfig);
    baseurl + "?" + HttpHelper.generatorParamString(paramdata);
  }

  def generateDataByFormat(signalData: Map[String, String], format: Map[String, DataAdapter]): Map[String, String] = {
    format map (p => (generateDataByAdapter(p._1, signalData.get(p._1).getOrElse(""), p._2 )))
  }
  
  def generateDataByAdapter(key:String, data:String, adapter:DataAdapter)={
    adapter  match {
      case DataAdapter(<var/>) => (key,data)
      case DataAdapter(<var><newkey>{newkey}</newkey></var>)=>(newkey text,data)
      case _ => throw new Exception("Format Error!")
    }
  }
}

object ListenerRequestGenerator {
  implicit def javaMapasMap[A, B](m: java.util.Map[A, B]): Map[A, B] = scala.collection.JavaConversions.asMap(m)
}

