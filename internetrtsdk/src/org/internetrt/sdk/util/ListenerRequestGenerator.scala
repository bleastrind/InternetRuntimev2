package org.internetrt.sdk.util
import org.internetrt.sdk.exceptions.FormatErrorException
import org.internetrt.sdk.exceptions.DataNotEnoughException


object ListenerRequestGenerator{
  val parser = RoutingXmlParser;
  
  def generateDataByFormat(signalData: java.util.Map[String, String], format:ListenerDataFormat, extData:GlobalData):java.util.Map[String,String]={
    val data = scala.collection.JavaConversions.asMap(signalData).mapValues(v => Seq(v)) 
    val result = generateDataByFormat(data, format.map, extData)
    scala.collection.JavaConversions.mapAsJavaMap(result)
  }
  def generateDataByFormat(signalData: scala.collection.Map[String, Seq[String]], format:ListenerDataFormat, extData:GlobalData):Map[String,String]={
    generateDataByFormat(signalData, format.map, extData) 
  }
  
  def generateDataByFormat(signalData: scala.collection.Map[String, Seq[String]], format: Map[String, DataAdapter],extData:GlobalData): Map[String, String] = {
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
      case DataAdapter(<value>{text}</value>) => (key, text.text)
      case DataAdapter(x) => {
          System.out.println("Not supported format:"+x)
    	  throw new FormatErrorException("Format Error!"+x)
      }
    }
  }
}
class ListenerRequestGenerator{}


