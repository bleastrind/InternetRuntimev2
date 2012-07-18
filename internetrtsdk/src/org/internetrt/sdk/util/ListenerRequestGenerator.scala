package org.internetrt.sdk.util
import org.internetrt.sdk.exceptions.FormatErrorException
import org.internetrt.sdk.exceptions.DataNotEnoughException


object ListenerRequestGenerator{
  val parser = RoutingXmlParser;

  
  def generateSignalListenerUrl(signalData: scala.collection.Map[String, Seq[String]], listenerconfig: ListenerConfig, extData:GlobalData):String = { 
    val paramFormats = parser.paramsAdapter(listenerconfig);
 
    val paramdata = ListenerRequestGenerator.generateDataByFormat(signalData, paramFormats, extData)
    val baseurl = parser.getReqUrl(listenerconfig);
    System.out.println("BaseUrl"+baseurl)
    baseurl + "?" + HttpHelper.generatorParamString(scala.collection.JavaConversions.asJavaMap(paramdata));
  }
  
  def generateSignalListenerUrl(signalData: java.util.Map[String, String], listenerconfig: ListenerConfig,extData:GlobalData):String = {
    val data = scala.collection.JavaConversions.asMap(signalData).mapValues(v => Seq(v))    
    generateSignalListenerUrl(data,listenerconfig,extData)
  }
  
  private def generateDataByFormat(signalData: scala.collection.Map[String, Seq[String]], format: Map[String, DataAdapter],extData:GlobalData): Map[String, String] = {
    try{
      format map (p => (generateDataByAdapter(p._1, signalData.get(p._1), p._2 ,extData)))
    }catch{
      case e:java.util.NoSuchElementException => throw new DataNotEnoughException()
    }
  }
  
  private def generateDataByAdapter(key:String, data:Option[Seq[String]], adapter:DataAdapter, extData:GlobalData)={
    adapter  match {
      case DataAdapter(<value><var/></value>) => (key,data.get.head)
      case DataAdapter(<value><var><newkey>{newkey}</newkey></var></value>)=>(newkey.text,data.get.head)
      case DataAdapter(<value><var><ID/></var></value>) => (key,extData.map.get(RoutingXmlParser.ROUTING_INSTANCE_ID_KEY).get)
      case DataAdapter(x) => {
          System.out.println("Not supported format:"+x)
    	  throw new FormatErrorException("Format Error!"+x)
      }
    }
  }
}
class ListenerRequestGenerator{}


